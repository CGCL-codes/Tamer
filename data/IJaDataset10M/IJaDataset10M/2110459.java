package clump.kernel.core;

import clump.kernel.IPackageVariable;
import java.util.HashMap;
import java.util.Map;

public class PackageVariables {

    private static Map<String, IPackageVariable> dictionnary = null;

    private static synchronized Map<String, IPackageVariable> getDictionnary() {
        if (dictionnary == null) {
            dictionnary = new HashMap<String, IPackageVariable>();
        }
        return dictionnary;
    }

    public static synchronized IPackageVariable solve(Class aClass) {
        IPackageVariable model = getDictionnary().get(aClass.getName());
        if (model == null) {
            try {
                final Object object = aClass.newInstance();
                if (object instanceof IPackageVariable) {
                    final IPackageVariable variable = (IPackageVariable) object;
                    dictionnary.put(aClass.getName(), variable);
                    return variable;
                } else {
                    throw new RuntimeException(aClass + "is not an instance of " + IPackageVariable.class.getName());
                }
            } catch (InstantiationException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        } else {
            return model;
        }
    }

    public static synchronized void define(IPackageVariable aClass) {
        final String name = aClass.getClass().getName();
        final IPackageVariable model = getDictionnary().get(name);
        if (model == null) {
            getDictionnary().put(name, aClass);
        }
    }
}
