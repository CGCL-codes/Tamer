package fireteam.orb.util;

import java.io.File;
import java.io.FileInputStream;
import java.util.Hashtable;

/**
  * User: tolik1
 * Date: 06.08.2007
 * Time: 17:01:38
 * To change this template use File | Settings | File Templates.
 */
public class FileClassLoader extends ClassLoader {

    private static final Hashtable<String, Class> g_classes = new Hashtable<String, Class>();

    public static FileClassLoader getInstance() {
        return new FileClassLoader();
    }

    private FileClassLoader() {
        reset();
    }

    public Class<?> loadFile(File fd) {
        try {
            FileInputStream is = new FileInputStream(fd);
            int sz = (int) is.getChannel().size();
            byte[] Data = new byte[sz];
            is.read(Data);
            is.close();
            Class<?> nClass = loadClassData("fireteam.orb.server.processors." + fd.getName().replaceAll(".class", ""), Data);
            g_classes.put(nClass.getName(), nClass);
            return nClass;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private Class<?> loadClassData(String sName, byte[] Data) {
        return defineClass(sName, Data, 0, Data.length);
    }

    @Override
    public Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        Object value;
        value = g_classes.get(name);
        if (value != null) {
            Class<?> klass = (Class) value;
            if (resolve) resolveClass(klass);
            return klass;
        } else return super.loadClass(name, resolve);
    }

    private void reset() {
        g_classes.clear();
    }
}
