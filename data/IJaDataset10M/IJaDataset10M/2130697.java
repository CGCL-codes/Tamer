package org.simpleframework.template;

import org.simpleframework.http.load.MapperEngine;
import org.simpleframework.http.serve.Context;
import java.io.IOException;
import java.io.File;

/**
 * The <code>Maintainer</code> is used to act as an administration
 * service for the <code>MapperEngine</code>. It ensures that the
 * service instances loaded contain the most up to date byte codes.
 * This is done by monitoring class files within a specified 
 * classpath of the underlying file system. If the byte codes 
 * within the path change, the <code>Maintainer</code> will unload 
 * the service using the <code>MapperEngine.unload</code> method.
 * <p>
 * The objective of the maintainer is to ensure that only the most
 * up to date byte codes are used by the <code>MapperEngine</code>.
 * This enables services to be edited and recompiled in such a way
 * that they can be reloaded into the system without administration.
 *  
 * @author Niall Gallagher
 *
 * @see org.simpleframework.http.load.LoaderEngine
 */
final class Maintainer extends Thread implements Monitor {

    /**
    * This is the <code>MapperEngine</code> that is maintained.
    */
    private MapperEngine engine;

    /**
    * This is a list that contains information about classes.
    */
    private Entry[] list;

    /**
    * Constructor for the <code>Maintainer</code> object. This will
    * maintain the loaded services for the issued engine. If the
    * services loaded by the issued engine are found within the 
    * specified classpath, then they are unloaded from the engine 
    * when the class files for those services have changed.
    *
    * @param engine this is the engine that is maintained by this
    * @param path this is the classpath that class files reside in
    *
    * @exception IOException if the path cannot be canonicalized
    */
    public Maintainer(MapperEngine engine, File path) throws IOException {
        this(engine, path.getCanonicalPath());
    }

    /**
    * Constructor for the <code>Maintainer</code> object. This will
    * maintain the loaded services for the issued engine. If the
    * services loaded by the issued engine are found within the 
    * specified classpath, then they are unloaded from the engine 
    * when the class files for those services have changed. The 
    * classpath used for this is <code>Context.getBasePath</code>.
    *
    * @param engine this is the engine that is maintained by this
    * @param context this is the context that class files reside in
    *
    * @exception IOException if the path cannot be canonicalized
    */
    public Maintainer(MapperEngine engine, Context context) throws IOException {
        this(engine, new File(context.getBasePath()));
    }

    /**
    * Constructor for the <code>Maintainer</code> object. This will
    * maintain the loaded services for the issued engine. The path
    * given to this constructor must be canonicalized to ensure 
    * that it has only one form. The path is used to register with
    * the <code>MapperEngine.update</code> method for updates.
    *
    * @param engine this is the engine that is maintained by this
    * @param path this is the classpath that class files reside in
    */
    private Maintainer(MapperEngine engine, String path) {
        this.engine = engine;
        this.init(engine, path);
        this.start();
    }

    /**
    * To ensure that class files can be maintained by this object
    * it must receive information regarding the names and paths
    * of the <code>Service</code> instances loaded by the engine.
    * This initialization method registers an agent with the
    * engine, which will make callbacks when services are loaded.
    *
    * @param engine this is the engine to register an agent with
    * @param path this is the classpath services are loaded from
    */
    private void init(MapperEngine engine, String path) {
        engine.update(path, new Delegate(this, path));
    }

    /**
    * This <code>update</code> method is used to receive callbacks 
    * from the <code>MapperEngine</code> when the state of that
    * engine changes, that is, when either a service instance is 
    * loaded or when one is unloaded. This ensures that the layout
    * of the engine is available to the <code>Maintainer</code>
    * without having to poll the engine for updates.
    *
    * @param name this is the list of service instance names
    * @param file this is a parallel list of service class files
    */
    public void update(String[] name, File[] file) {
        Entry[] list = new Entry[name.length];
        Entry[] copy = this.list;
        for (int i = 0; i < name.length; i++) {
            for (int j = 0; j < copy.length; j++) {
                if (copy[j].name.equals(name[i])) {
                    list[i] = copy[j];
                }
            }
            if (list[i] == null) {
                list[i] = new Entry(name[i], file[i]);
            }
        }
        this.list = list;
    }

    /**
    * Cleans the <code>MapperEngine</code> of any service instances
    * that contain out of date byte codes. This is where stale byte
    * codes are unloaded from the engine. To determine whether the
    * byte codes are stale, the <code>Maintainer</code> object keeps
    * an internal record of time stamps for each class file that
    * represents a loaded service implementation. If the time stamp
    * changes at any stage, then the service instance is unloaded.
    * <p>
    * Unloading services from the <code>MapperEngine</code> is
    * sufficient to maintain the engine. This is so because the auto
    * loading provided by the <code>MapperEngine</code> reloads the
    * service the next time it is referenced. So the effect of 
    * unloading stale services is transparent to the HTTP client.
    *
    * @param list a recent snapshot of the service instances loaded
    */
    private void clean(Entry[] list) {
        for (int i = 0; i < list.length; i++) {
            Entry entry = list[i];
            if (entry.isModified()) {
                entry.refresh();
                engine.unload(entry.name);
            }
        }
    }

    /**
    * Performs the cleaning of the <code>MapperEngine</code> at 
    * a fixed interval. This acquires the current list of loaded
    * services and waits for five seconds. After the peroid has
    * elapsed the time stamps of loaded implementations are 
    * checked to see if the byte codes have changed since they
    * were loaded by the engine, if the class files time stamp
    * has changed or the file is missing the object is unloaded.
    */
    public void run() {
        while (true) {
            try {
                Entry[] list = this.list;
                sleep(5000);
                clean(list);
            } catch (Exception e) {
                continue;
            }
        }
    }

    /**
    * The <code>Entry</code> object is used to group information
    * on a loaded service instance. The information kept on the
    * service are its name, class file, and modification date.
    * In order to keep track of recent modifications of the file
    * its time stamp is compared to the file modification.
    *
    * @see java.io.File#lastModified
    */
    private class Entry {

        /**
       * Represents the name of the service instance loaded.
       */
        public String name;

        /**
       * Represents the class file of the service instance.
       */
        public File file;

        /**
       * Represents the last checked file modification date.
       */
        public long stamp;

        /**
       * Determines whether the file exists as a class file.
       */
        public boolean exist;

        /**
       * Constructor for the <code>Entry</code> object. This will
       * create an entry using the specified service name and the
       * issued path as the path to the service implementation
       * byte codes. This will also take the modification date of
       * the file so that changes in the file can be tracked.
       *
       * @param name this is the name of the service instance
       * @param file this is the file that contains the class
       */
        public Entry(String name, File file) {
            this.stamp = file.lastModified();
            this.exist = file.exists();
            this.name = name;
            this.file = file;
        }

        /**
       * Determines whether the file represented by this object has
       * changed. This performs a check to determine whether the
       * file has been deleted or whether the file has been changed.
       * If either condition is true then this returns true. This
       * allows the <code>Maintainer</code> to determine whether
       * the byte codes that have been loaded are stale or not
       *
       * @return this returns true if the class file is unchanged
       */
        public boolean isModified() {
            if (exist != file.exists()) {
                return true;
            }
            if (stamp != file.lastModified()) {
                return true;
            }
            return false;
        }

        /**
       * This updates the record for the class file. If the stamp
       * and other details need to be updated to represent the 
       * most up to date details then this method should be used.
       */
        public void refresh() {
            stamp = file.lastModified();
            exist = file.exists();
        }
    }
}
