package net.sf.cacheannotations.impl.keygenerator;

import java.io.Serializable;
import java.util.ArrayList;
import net.sf.cacheannotations.CacheException;

public class ReflectionKeyPartGenerator implements KeyPartGenerator {

    public static final String NAME = "REFLECT";

    public String getName() {
        return NAME;
    }

    public Serializable generateKeyPart(Object object, String argu) throws CacheException {
        String args[] = argu.split(",");
        ArrayList<Serializable> key = new ArrayList<Serializable>(args.length + 1);
        key.add(object.getClass().getName());
        for (int i = 0; i < args.length; i++) {
            try {
                key.add((Serializable) object.getClass().getMethod(getterName(args[i]), new Class[0]).invoke(object, new Object[0]));
            } catch (Exception e) {
                throw new CacheException(e);
            }
        }
        return key;
    }

    private String getterName(String name) {
        return "get" + name.substring(0, 1).toUpperCase() + name.substring(1);
    }
}
