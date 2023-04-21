package sun.management.snmp.jvminstr;

import java.io.Serializable;
import com.sun.jmx.snmp.SnmpStatusException;
import com.sun.jmx.snmp.agent.SnmpMib;
import sun.management.snmp.jvmmib.JvmRTBootClassPathEntryMBean;

/**
 * The class is used for implementing the "JvmRTBootClassPathEntry" group.
 */
public class JvmRTBootClassPathEntryImpl implements JvmRTBootClassPathEntryMBean, Serializable {

    private final String item;

    private final int index;

    /**
     * Constructor for the "JvmRTBootClassPathEntry" group.
     */
    public JvmRTBootClassPathEntryImpl(String item, int index) {
        this.item = validPathElementTC(item);
        this.index = index;
    }

    private String validPathElementTC(String str) {
        return JVM_MANAGEMENT_MIB_IMPL.validPathElementTC(str);
    }

    /**
     * Getter for the "JvmRTBootClassPathItem" variable.
     */
    public String getJvmRTBootClassPathItem() throws SnmpStatusException {
        return item;
    }

    /**
     * Getter for the "JvmRTBootClassPathIndex" variable.
     */
    public Integer getJvmRTBootClassPathIndex() throws SnmpStatusException {
        return new Integer(index);
    }
}
