package org.book4j.modules;

public class ModuleLoader {

    public Object CreateObject(String className) {
        try {
            Class c = Class.forName(className);
            return c.newInstance();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
