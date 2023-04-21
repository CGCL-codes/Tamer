package me.buick.util.snmp.core.dao.impl.snmp.os.windows.entity;

import java.io.Serializable;
import org.opengoss.snmphibernate.api.SmiType;
import org.opengoss.snmphibernate.api.annotation.MibIndex;
import org.opengoss.snmphibernate.api.annotation.MibObjectType;
import org.opengoss.snmphibernate.api.annotation.MibTable;
import org.opengoss.snmphibernate.api.annotation.MibObjectType.Access;

/**
 * 
 * Java data converter for SNMP table hrSWRunTable ".1.3.6.1.2.1.25.4.2"
 * 
 * You can find the definition in HOST-RESOURCES-MIB-->hrSWRun-->hrSWRunTable
 * 
 * @author buick
 *
 */
@SuppressWarnings("serial")
@MibTable
public class HrSWRunEntry implements Serializable {

    @MibIndex(no = 1, length = 1)
    @MibObjectType(oid = ".1.3.6.1.2.1.25.4.2.1.1", smiType = SmiType.INTEGER, access = Access.READ)
    private int hrSWRunIndex;

    @MibObjectType(oid = ".1.3.6.1.2.1.25.4.2.1.2", smiType = SmiType.DISPLAY_STRING, access = Access.READ)
    private String hrSWRunName;

    @MibObjectType(oid = ".1.3.6.1.2.1.25.4.2.1.4", smiType = SmiType.DISPLAY_STRING, access = Access.READ)
    private String hrSWRunPath;

    @MibObjectType(oid = ".1.3.6.1.2.1.25.4.2.1.5", smiType = SmiType.DISPLAY_STRING, access = Access.READ)
    private String hrSWRunParameters;

    @MibObjectType(oid = ".1.3.6.1.2.1.25.4.2.1.6", smiType = SmiType.INTEGER, access = Access.READ)
    private int hrSWRunType;

    @MibObjectType(oid = ".1.3.6.1.2.1.25.4.2.1.7", smiType = SmiType.INTEGER, access = Access.READ)
    private int hrSWRunStatus;

    public HrSWRunEntry() {
    }

    public int getHrSWRunIndex() {
        return hrSWRunIndex;
    }

    public void setHrSWRunIndex(int hrSWRunIndex) {
        this.hrSWRunIndex = hrSWRunIndex;
    }

    public String getHrSWRunName() {
        return hrSWRunName;
    }

    public void setHrSWRunName(String hrSWRunName) {
        this.hrSWRunName = hrSWRunName;
    }

    public String getHrSWRunPath() {
        return hrSWRunPath;
    }

    public void setHrSWRunPath(String hrSWRunPath) {
        this.hrSWRunPath = hrSWRunPath;
    }

    public String getHrSWRunParameters() {
        return hrSWRunParameters;
    }

    public void setHrSWRunParameters(String hrSWRunParameters) {
        this.hrSWRunParameters = hrSWRunParameters;
    }

    public int getHrSWRunType() {
        return hrSWRunType;
    }

    public void setHrSWRunType(int hrSWRunType) {
        this.hrSWRunType = hrSWRunType;
    }

    public int getHrSWRunStatus() {
        return hrSWRunStatus;
    }

    public void setHrSWRunStatus(int hrSWRunStatus) {
        this.hrSWRunStatus = hrSWRunStatus;
    }
}
