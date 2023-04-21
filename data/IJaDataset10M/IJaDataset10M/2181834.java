package net.petraframe.res.data;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import net.petraframe.res.sound.PGFSound;
import net.petraframe.res.sound.PGFSoundImpl;

public class PGFDataResourceImpl implements PGFDataResource, Cloneable {

    public static int readBufferSize = PGFResourceManagerImpl.readBufferSize;

    protected PGFResourceManagerImpl manager;

    /** URL for external data of this resource; never null. */
    protected URL url;

    /** mapping key for this resource; never null. */
    protected String name;

    /** data type of this resource; normally a MIME type expression. */
    protected String dataType;

    /** set of properties defined for this resource, where each property is
    * a relation of key into value.
    */
    protected HashMap<String, String> properties;

    /** list of virtual directories where this resource is listed. */
    protected ArrayList<PGFResourceDirectory> directories;

    /** Whether this resource should be preferably kept in a cache. */
    protected boolean cachable;

    /** Whether this resource should be preferably preloaded into a cache. */
    protected boolean preloading;

    /**
    * Implementation internal constructor rendering an invalid instance
    * which has to be assigned name and url to become valid!
    */
    protected PGFDataResourceImpl() {
    }

    /**
    * Creates a <code>PGFDataResource</code> with the given name, url and
    * data type. Properties and directories features are left void.
    * (In the context of a resource manager the resource name is dealt with
    * as unique entry. A uniqueness check is not performed by this constructor.)
    *
    * @param url URL resource data access address
    * @param name String resource key (<b>null</b> is translated to "") 
    * @param type String MIME type of resource; may be <b>null</b> for void 
    * @throws NullPointerException if url is <b>null</b>
    */
    public PGFDataResourceImpl(URL url, String name, String type) {
        this(null, url, name, type);
    }

    /**
    * Creates a <code>PGFDataResource</code> with the given name, url and
    * data type. Properties and directories features are left void.
    * (In the context of a resource manager the resource name is dealt with
    * as unique entry. A uniqueness check is not performed by this constructor.)
    *
    * @param man <code>PGFResourceManagerImpl</code> the governing resource manager 
    *            for this resource, or <b>null</b>
    * @param url URL resource data access address
    * @param name String resource key (<b>null</b> is translated to "") 
    * @param type String MIME type of resource; may be <b>null</b> for void 
    * @throws NullPointerException if url is <b>null</b>
    */
    public PGFDataResourceImpl(PGFResourceManagerImpl man, URL url, String name, String type) {
        if (url == null) {
            throw new NullPointerException();
        }
        this.manager = man;
        this.url = url;
        this.name = name == null ? "" : name;
        this.dataType = type;
    }

    @Override
    public InputStream getDataStream() throws IOException {
        return getDataStream(0);
    }

    @Override
    public InputStream getDataStream(int bufferSize) throws IOException {
        InputStream in = manager == null ? url.openStream() : manager.getResourceInputStream(this);
        if (in instanceof ByteArrayInputStream || in instanceof BufferedInputStream) {
            return in;
        }
        return bufferSize == 0 ? new BufferedInputStream(in) : new BufferedInputStream(in, bufferSize);
    }

    @Override
    public byte[] getContent() throws IOException {
        byte[] data = manager == null ? null : manager.getResourceCachedData(this);
        if (data == null) {
            InputStream in = getDataStream(readBufferSize);
            data = PGFResourceManagerImpl.inputToByteBlock(in);
        }
        return data;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public URL getPackageURL() {
        throw new UnsupportedOperationException();
    }

    @Override
    public URL getResourceURL() {
        return url;
    }

    @Override
    public boolean setCachable(boolean v) {
        cachable = v & manager != null;
        if (manager != null) {
            cachable = manager.setCachableResource(this, v);
        }
        return cachable;
    }

    @Override
    public boolean isCachable() {
        return cachable;
    }

    @Override
    public boolean isPreloading() {
        return preloading;
    }

    @Override
    public boolean setPreloading(boolean v) {
        preloading = v & manager != null;
        if (manager != null) {
            preloading = manager.setPreloadingResource(this, v);
        }
        return preloading;
    }

    @Override
    public List<PGFResourceDirectory> getDirectories() {
        return directories == null ? null : new ArrayList<PGFResourceDirectory>(directories);
    }

    @Override
    public String getDataType() {
        return dataType;
    }

    @Override
    public String getProperty(String name) {
        return properties == null ? null : properties.get(name);
    }

    @Override
    public PGFResourceManager getManager() {
        return manager;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Map<String, String> getProperties() {
        return (Map<String, String>) (properties == null ? null : ((HashMap) properties).clone());
    }

    /**
    * Adds a property mapping to this resource's properties list.
    * 
    * @param key String
    * @param value String
    * @return String the previous mapping value or <b>null</b> 
    */
    protected String addProperty(String key, String value) {
        if (properties == null) {
            properties = new HashMap<String, String>(4);
        }
        synchronized (properties) {
            return properties.put(key, value);
        }
    }

    /**
    * Adds a new entry into the list of virtual directories of
    * this resource. Double entries are ignored. 
    * (This does not automatically include this resource into the directory!
    * The directory must contain this resource.)
    * 
    * @param dir <code>PGFResourceDirectory</code> virtual directory to be added
    * @return boolean <b>true</b> if and only if the list of directories has
    *         changed as a result of this call
    * @throws IllegalArgumentException if the parameter does not contain this resource        
    */
    protected boolean addDirectory(PGFResourceDirectory dir) {
        if (directories == null) {
            directories = new ArrayList<PGFResourceDirectory>(4);
        }
        if (!dir.hasResource(getName())) {
            throw new IllegalArgumentException("directory must contain this resource");
        }
        synchronized (directories) {
            if (!directories.contains(dir)) {
                return directories.add(dir);
            }
            return false;
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object clone() {
        try {
            PGFDataResourceImpl c = (PGFDataResourceImpl) super.clone();
            if (directories != null) synchronized (directories) {
                c.directories = (ArrayList<PGFResourceDirectory>) directories.clone();
            }
            if (properties != null) synchronized (properties) {
                c.properties = (HashMap<String, String>) properties.clone();
            }
            return c;
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null & obj instanceof PGFDataResource) {
            PGFDataResource s = (PGFDataResource) obj;
            return s.getManager() == this.getManager() && s.getName().equals(this.getName());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public String toString() {
        return name;
    }
}
