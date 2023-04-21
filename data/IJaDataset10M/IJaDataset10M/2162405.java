package goldengate.snmp.test;

import org.snmp4j.smi.OID;
import goldengate.snmp.interf.GgGauge32;

/**
 * Example of GgGauge32 Usage
 * 
 * @author Frederic Bregier
 * 
 */
@SuppressWarnings("serial")
public class ExampleImplGauge32 extends GgGauge32 {

    public OID oid;

    long _internalValue = 42;

    protected void setInternalValue() {
        _internalValue++;
        setValue(_internalValue);
    }

    protected void setInternalValue(long value) {
        _internalValue = value;
        setValue(_internalValue);
    }

    /**
     * 
     */
    public ExampleImplGauge32(OID oid) {
        super();
        this.oid = oid;
    }

    /**
     * 
     */
    public ExampleImplGauge32(OID oid, long value) {
        super(value);
        this.oid = oid;
    }
}
