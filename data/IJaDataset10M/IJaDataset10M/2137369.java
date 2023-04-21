package java.lang;

/**
 * 
 */
public class Class {

    private Class() {
    }

    static final Class getInstance(Object obj) {
        return new Class();
    }

    public String getName() {
        return "Classname";
    }

    public static Class forName(String className) throws ClassNotFoundException {
        throw new ClassNotFoundException("Class.forName is not yet implemented!");
    }

    public Object newInstance() throws InstantiationException, IllegalAccessException {
        throw new RuntimeException("Class.newInstance is not yet implemented!");
    }
}
