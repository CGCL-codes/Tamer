package org.jtools.clan.feature;

public class StringConstructor {

    protected StringConstructor() {
    }

    private static final Class[] stringconstr = { "".getClass() };

    public static StringConstructor createFeature(org.jtools.clan.Clazz clazz, String feature) {
        if (!"StringConstructor".equals(feature)) return null;
        try {
            java.lang.Class c = clazz.getJClass();
            if (c == null) return null;
            if (c.isInterface()) return null;
            if (c.isPrimitive()) return null;
            if (c.isArray()) return null;
            int mod = c.getModifiers();
            if (java.lang.reflect.Modifier.isAbstract(mod)) return null;
            if (!java.lang.reflect.Modifier.isPublic(mod)) return null;
            java.lang.reflect.Constructor cc = c.getConstructor(stringconstr);
            if (cc == null) return null;
            return new StringConstructor();
        } catch (Exception ex) {
            return null;
        } catch (Error er) {
            return null;
        }
    }
}
