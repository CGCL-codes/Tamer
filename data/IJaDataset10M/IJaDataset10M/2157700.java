package csiebug.domain.hibernateImpl;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import csiebug.domain.Code;

/**
 * 
 * @author George_Tsai
 * @version 2009/7/16
 *
 */
public class CodeImpl extends BasicObjectImpl implements Code {

    private static final long serialVersionUID = 1L;

    private String codeId;

    private String codeType;

    private String codeValue;

    private String codeDescription;

    private Boolean enabled;

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof CodeImpl)) {
            return false;
        }
        CodeImpl code = (CodeImpl) obj;
        return new EqualsBuilder().append(this.codeId, code.getCodeId()).append(this.codeType, code.getCodeType()).isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder().append(this.codeId).append(this.codeType).toHashCode();
    }

    public void setCodeId(String codeId) {
        this.codeId = codeId;
    }

    public String getCodeId() {
        return codeId;
    }

    public void setCodeType(String codeType) {
        this.codeType = codeType;
    }

    public String getCodeType() {
        return codeType;
    }

    public void setCodeValue(String codeValue) {
        this.codeValue = codeValue;
    }

    public String getCodeValue() {
        return codeValue;
    }

    public void setCodeDescription(String codeDescription) {
        this.codeDescription = codeDescription;
    }

    public String getCodeDescription() {
        return codeDescription;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Boolean getEnabled() {
        return enabled;
    }
}
