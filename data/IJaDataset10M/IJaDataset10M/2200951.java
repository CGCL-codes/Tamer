package com.leemba.monitor.server.objects;

import org.terracotta.modules.annotations.InstrumentedClass;

/**
 * <p>Arg. Using a plain Object instance for identity in a ConcurrentHashMap gets us this error:
 *
 * <p>Exception in thread "sensor-report-queue" java.lang.IllegalArgumentException: An object of type
 * [class java.lang.Object] was added to a clustered ConcurrentHashMap but the object does not
 * override hashCode() and was not previously added to clustered state before being added to the map.
 * Please implement hashCode() and equals() on this type and/or share this object by referring
 * to it from clustered state before adding it to this data structure.
 * 
 * <p>Nice! :-)
 *
 * @author mrjohnson
 */
@InstrumentedClass
public class Identity {

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
