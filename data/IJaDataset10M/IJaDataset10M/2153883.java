package goldengate.snmp.interf;

import org.snmp4j.smi.Gauge32;

/**
 * Generic Gauge32 with update possibility for GoldenGate
 * 
 * @author Frederic Bregier
 * 
 */
@SuppressWarnings("serial")
public abstract class GgGauge32 extends Gauge32 {

    /**
     * Function to set the data before it is accessed by SNMP4J. This function
     * MUST call setValue(long)
     */
    protected abstract void setInternalValue();

    /**
     * Function to set the data before it is accessed by SNMP4J. This function
     * MUST call setValue(long)
     */
    protected abstract void setInternalValue(long value);

    public GgGauge32() {
        setInternalValue();
    }

    public GgGauge32(long value) {
        setInternalValue(value);
    }

    @Override
    public long getValue() {
        setInternalValue();
        return super.getValue();
    }

    @Override
    public Object clone() {
        setInternalValue();
        return super.clone();
    }

    @Override
    public boolean isDynamic() {
        return true;
    }
}
