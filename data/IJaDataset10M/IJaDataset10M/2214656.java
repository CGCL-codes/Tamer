package org.nakedobjects.runtime.persistence.oidgenerator.simple;

import static org.hamcrest.CoreMatchers.is;
import static org.nakedobjects.commons.ensure.Ensure.ensureThatArg;
import static org.nakedobjects.commons.matchers.NofMatchers.greaterThan;
import org.nakedobjects.commons.debug.DebugString;
import org.nakedobjects.metamodel.adapter.oid.Oid;
import org.nakedobjects.metamodel.adapter.oid.stringable.directly.OidStringifierDirect;
import org.nakedobjects.runtime.persistence.oidgenerator.OidGenerator;
import org.nakedobjects.runtime.persistence.oidgenerator.OidGeneratorAbstract;

/**
 * Generates OIDs based on monotonically.
 * 
 * <p>
 * Specifies the {@link OidStringifierDirect} as the {@link #getOidStringifier() OID stringifier} 
 * ({@link SerialOid} is conformant)).
 */
public class SimpleOidGenerator extends OidGeneratorAbstract {

    public static class Memento {

        private long persistentSerialNumber;

        private long transientSerialNumber;

        Memento(long persistentSerialNumber, long transientSerialNumber) {
            this.persistentSerialNumber = persistentSerialNumber;
            this.transientSerialNumber = transientSerialNumber;
        }

        public long getTransientSerialNumber() {
            return transientSerialNumber;
        }

        public long getPersistentSerialNumber() {
            return persistentSerialNumber;
        }
    }

    private long persistentSerialNumber;

    private long transientSerialNumber;

    public SimpleOidGenerator() {
        this(1);
    }

    /**
     * Persistent {@link Oid}s count up from the provided seed parameter, while
     * {@link Oid#isTransient()} transient {@link Oid}s count down.  
     */
    public SimpleOidGenerator(final long seed) {
        this(seed, Long.MIN_VALUE + seed);
    }

    public SimpleOidGenerator(Memento memento) {
        this(memento.getPersistentSerialNumber(), memento.getTransientSerialNumber());
    }

    private SimpleOidGenerator(long persistentSerialNumber, long transientSerialNumber) {
        super(new OidStringifierDirect(SerialOid.class));
        ensureThatArg(persistentSerialNumber, is(greaterThan(0L)));
        this.persistentSerialNumber = persistentSerialNumber;
        this.transientSerialNumber = transientSerialNumber;
    }

    public String name() {
        return "Simple Serial OID Generator";
    }

    public synchronized SerialOid createTransientOid(final Object object) {
        return SerialOid.createTransient(transientSerialNumber--);
    }

    public synchronized void convertTransientToPersistentOid(final Oid oid) {
        if (!(oid instanceof SerialOid)) {
            throw new IllegalArgumentException("Oid is not a SerialOid");
        }
        final SerialOid serialOid = (SerialOid) oid;
        serialOid.setId(persistentSerialNumber++);
        serialOid.makePersistent();
    }

    public Memento getMemento() {
        return new Memento(this.persistentSerialNumber, this.transientSerialNumber);
    }

    /**
     * Reset to a {@link Memento} previously obtained via {@link #getMemento()}.
     * 
     * <p>
     * Used in particular by the <tt>InMemoryObjectStore</tt> to reset (a new {@link OidGenerator}
     * is created each time).
     */
    public void resetTo(Memento memento) {
        this.persistentSerialNumber = memento.getPersistentSerialNumber();
        this.transientSerialNumber = memento.getTransientSerialNumber();
    }

    public void debugData(final DebugString debug) {
        debug.appendln("Persistent", persistentSerialNumber);
        debug.appendln("Transient", transientSerialNumber);
    }

    public String debugTitle() {
        return name();
    }
}
