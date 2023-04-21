package org.opengoss.snmphibernate.mib.host;

import java.io.Serializable;
import org.opengoss.snmphibernate.api.SmiType;
import org.opengoss.snmphibernate.api.annotation.MibIndex;
import org.opengoss.snmphibernate.api.annotation.MibObjectType;
import org.opengoss.snmphibernate.api.annotation.MibTable;
import org.opengoss.snmphibernate.api.annotation.MibObjectType.Access;

@SuppressWarnings("serial")
@MibTable
public class HrStorageEntry implements Serializable {

    @MibIndex(no = 1, length = 1)
    @MibObjectType(oid = "1.3.6.1.2.1.25.2.3.1.1", smiType = SmiType.INTEGER, access = Access.READ)
    private int hrStorageIndex;

    @MibObjectType(oid = "1.3.6.1.2.1.25.2.3.1.2", smiType = SmiType.OID, access = Access.READ)
    private String hrStorageType;

    @MibObjectType(oid = "1.3.6.1.2.1.25.2.3.1.3", smiType = SmiType.DISPLAY_STRING, access = Access.READ)
    private String hrStorageDescr;

    @MibObjectType(oid = "1.3.6.1.2.1.25.2.3.1.4", smiType = SmiType.INTEGER, access = Access.READ)
    private int hrStorageAllocationUnits;

    @MibObjectType(oid = "1.3.6.1.2.1.25.2.3.1.5", smiType = SmiType.INTEGER, access = Access.READ)
    private int hrStorageSize;

    @MibObjectType(oid = "1.3.6.1.2.1.25.2.3.1.6", smiType = SmiType.INTEGER, access = Access.READ)
    private int hrStorageUsed;

    public HrStorageEntry() {
    }

    public HrStorageEntry(int index) {
        this.hrStorageIndex = index;
    }

    public boolean isDisk() {
        return "1.3.6.1.2.1.25.2.1.4".equals(getHrStorageType());
    }

    public boolean isVirtualMemory() {
        return "1.3.6.1.2.1.25.2.1.3".equals(getHrStorageType());
    }

    public boolean isRam() {
        return "1.3.6.1.2.1.25.2.1.2".equals(getHrStorageType());
    }

    public int getHrStorageAllocationUnits() {
        return hrStorageAllocationUnits;
    }

    public void setHrStorageAllocationUnits(int hrStorageAllocationUnits) {
        this.hrStorageAllocationUnits = hrStorageAllocationUnits;
    }

    public String getHrStorageDescr() {
        return hrStorageDescr;
    }

    public void setHrStorageDescr(String hrStorageDescr) {
        this.hrStorageDescr = hrStorageDescr;
    }

    public int getHrStorageIndex() {
        return hrStorageIndex;
    }

    public void setHrStorageIndex(int hrStorageIndex) {
        this.hrStorageIndex = hrStorageIndex;
    }

    public int getHrStorageSize() {
        return hrStorageSize;
    }

    public void setHrStorageSize(int hrStorageSize) {
        this.hrStorageSize = hrStorageSize;
    }

    public String getHrStorageType() {
        return hrStorageType;
    }

    public void setHrStorageType(String hrStorageType) {
        this.hrStorageType = hrStorageType;
    }

    public int getHrStorageUsed() {
        return hrStorageUsed;
    }

    public void setHrStorageUsed(int hrStorageUsed) {
        this.hrStorageUsed = hrStorageUsed;
    }
}
