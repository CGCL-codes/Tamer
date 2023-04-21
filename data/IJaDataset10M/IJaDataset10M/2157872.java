package org.opennms.netmgt.snmp.mock;

import java.util.ArrayList;
import org.opennms.netmgt.snmp.SnmpObjId;

public class TestVarBindList extends ArrayList<TestVarBind> {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public TestVarBindList() {
        super();
    }

    public TestVarBindList(TestVarBindList list) {
        super(list);
    }

    public void addVarBind(SnmpObjId oid) {
        add(new TestVarBind(oid));
    }

    public TestVarBind getVarBindAt(int i) {
        return get(i);
    }
}
