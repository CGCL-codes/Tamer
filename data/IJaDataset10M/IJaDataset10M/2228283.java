package org.omwg.mediation;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import org.omwg.mediation.language.objectmodel.api.comparators.BinaryComparator;
import org.omwg.mediation.language.objectmodel.impl.comparators.StrContainsImpl;
import org.omwg.mediation.language.objectmodel.impl.datatypes.IntegerXsImpl;

public class InterfaceTest {

    public static void main(final String[] args) {
        for (Class clazz : IntegerXsImpl.class.getInterfaces()) {
            System.out.println(clazz.getName());
        }
        System.out.println("-- new --");
        Class clazz = StrContainsImpl.class;
        List<Class> list = new ArrayList<Class>();
        addAllInterfaces(clazz, list);
        for (Class c : list) {
            System.out.println(c.getName());
        }
        System.out.println();
        if (list.contains(BinaryComparator.class)) {
            System.out.println("contains");
            for (Method meth : clazz.getMethods()) {
                System.out.print(meth.getName() + ": ");
                for (Class param : meth.getParameterTypes()) {
                    System.out.print(param.getSimpleName() + " ");
                }
                System.out.println();
            }
        }
    }

    public static void addAllInterfaces(final Class source, final List<Class> list) {
        for (Class clazz : source.getInterfaces()) {
            list.add(clazz);
            addAllInterfaces(clazz, list);
        }
    }
}
