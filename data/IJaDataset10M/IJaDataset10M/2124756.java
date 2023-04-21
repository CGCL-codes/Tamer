package org.easystub;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class ThrowableFactory {

    private static final ThrowableFactory FACTORY = new ThrowableFactory();

    public static ThrowableFactory instance() {
        return FACTORY;
    }

    public Throwable create(Class<? extends Throwable> throwableClass) {
        try {
            Constructor<?> smallest = findSmallestConstructor(throwableClass);
            Object[] arguments = determineDefaultArguments(smallest);
            return (Throwable) smallest.newInstance(arguments);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    private Object[] determineDefaultArguments(Constructor<?> smallest) {
        List<Object> arguments = new ArrayList<Object>();
        for (Class parameterClass : smallest.getParameterTypes()) {
            if (parameterClass.isAssignableFrom(String.class)) {
                arguments.add("Default Throwable created by EasyStub");
                continue;
            }
            if (parameterClass.isAssignableFrom(Throwable.class)) {
                arguments.add(new EasyStubCreatedException());
                continue;
            }
            arguments.add(DefaultValues.defaultValueFor(parameterClass));
        }
        return arguments.toArray();
    }

    private Constructor findSmallestConstructor(Class<? extends Throwable> throwableClass) {
        List<Constructor<?>> constructors = Arrays.asList(throwableClass.getConstructors());
        Collections.sort(constructors, new Comparator<Constructor<?>>() {

            @Override
            public int compare(Constructor<?> first, Constructor<?> second) {
                Integer firstParamCount = first.getParameterTypes().length;
                Integer secondParamCount = second.getParameterTypes().length;
                return firstParamCount.compareTo(secondParamCount);
            }
        });
        return constructors.get(0);
    }
}
