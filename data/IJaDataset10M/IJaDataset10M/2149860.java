package org.ddsteps.util;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * @author adam
 * @version $Id: TestCaseInfo.java,v 1.1 2005/12/03 12:51:41 adamskogman Exp $
 */
public class TestCaseInfo {

    private final String methodName;

    private final String rowId;

    /**
     * Info about a row instance.
     * 
     * @param methodName
     * @param rowId
     */
    public TestCaseInfo(String methodName, String rowId) {
        this.methodName = methodName;
        this.rowId = rowId;
    }

    /**
     * Info about a method instance.
     * 
     * @param methodName
     */
    public TestCaseInfo(String methodName) {
        this.methodName = methodName;
        this.rowId = null;
    }

    /**
     * @return True if the info is about a method instance.
     */
    public boolean isMethodInstance() {
        return this.rowId == null;
    }

    /**
     * @return True if the info is about a row instance.
     */
    public boolean isRowInstance() {
        return this.rowId != null;
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return new ToStringBuilder(this).append("methodName", methodName).append("rowId", rowId).toString();
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    /**
     * @return Returns the methodName.
     */
    public String getMethodName() {
        return methodName;
    }

    /**
     * @return Returns the rowId.
     */
    public String getRowId() {
        return rowId;
    }
}
