package org.nakedobjects.runtime.persistence.oidgenerator.timebased;

import java.util.Date;
import org.nakedobjects.metamodel.adapter.oid.Oid;
import org.nakedobjects.runtime.persistence.oidgenerator.simple.SimpleOidGenerator;

/**
 * Generates {@link Oid}s based on the system clock.
 * 
 * <p>
 * The same algorithm and {@link Oid} implementation as 
 * {@link SimpleOidGenerator} is used; it just seeds with a different value.
 */
public class TimeBasedOidGenerator extends SimpleOidGenerator {

    @Override
    public String name() {
        return "Time Initialised OID Generator";
    }

    public TimeBasedOidGenerator() {
        super(new Date().getTime());
    }
}
