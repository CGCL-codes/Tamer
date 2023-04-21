package job;

import java.util.concurrent.ConcurrentHashMap;

public class HashMapClassLoader extends ClassLoader {

    private ConcurrentHashMap<String, byte[]> classMap;

    public HashMapClassLoader(ConcurrentHashMap<String, byte[]> classMap) {
        super();
        this.classMap = classMap;
    }

    @Override
    public Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        return super.loadClass(name, resolve);
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        System.out.println("findClass()");
        System.out.flush();
        System.out.println("\tpackaged classes: ");
        System.out.flush();
        for (String c : classMap.keySet()) {
            System.out.println("\t\t" + c);
            System.out.flush();
        }
        byte[] buffer = classMap.get(name);
        System.out.print("class buffer null? ");
        if (buffer != null) {
            System.out.println("false(" + buffer.length + ")");
            System.out.flush();
        } else {
            System.out.println("true");
            System.out.flush();
        }
        return defineClass(name, buffer, 0, buffer.length);
    }
}
