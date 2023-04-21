package no.ugland.utransprod.model;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * Klasse for tabell EMPLOYEE_TYPE
 * 
 * @author atle.brekka
 * 
 */
public class EmployeeType extends BaseObject {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    /**
	 * 
	 */
    private Integer employeeTypeId;

    /**
	 * 
	 */
    private String employeeTypeName;

    /**
	 * 
	 */
    private String employeeTypeDescription;

    /**
	 * 
	 */
    public EmployeeType() {
        super();
    }

    /**
	 * @param employeeTypeId
	 * @param employeeTypeName
	 * @param employeeTypeDescription
	 */
    public EmployeeType(Integer employeeTypeId, String employeeTypeName, String employeeTypeDescription) {
        super();
        this.employeeTypeId = employeeTypeId;
        this.employeeTypeName = employeeTypeName;
        this.employeeTypeDescription = employeeTypeDescription;
    }

    /**
	 * @return beskrivelse
	 */
    public String getEmployeeTypeDescription() {
        return employeeTypeDescription;
    }

    /**
	 * @param employeeTypeDescription
	 */
    public void setEmployeeTypeDescription(String employeeTypeDescription) {
        this.employeeTypeDescription = employeeTypeDescription;
    }

    /**
	 * @return id
	 */
    public Integer getEmployeeTypeId() {
        return employeeTypeId;
    }

    /**
	 * @param employeeTypeId
	 */
    public void setEmployeeTypeId(Integer employeeTypeId) {
        this.employeeTypeId = employeeTypeId;
    }

    /**
	 * @return typenavn
	 */
    public String getEmployeeTypeName() {
        return employeeTypeName;
    }

    /**
	 * @param employeeTypeName
	 */
    public void setEmployeeTypeName(String employeeTypeName) {
        this.employeeTypeName = employeeTypeName;
    }

    /**
	 * @see no.ugland.utransprod.model.BaseObject#equals(java.lang.Object)
	 */
    @Override
    public boolean equals(final Object other) {
        if (!(other instanceof EmployeeType)) return false;
        EmployeeType castOther = (EmployeeType) other;
        return new EqualsBuilder().append(employeeTypeName, castOther.employeeTypeName).isEquals();
    }

    /**
	 * @see no.ugland.utransprod.model.BaseObject#hashCode()
	 */
    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(employeeTypeName).toHashCode();
    }

    /**
	 * @see no.ugland.utransprod.model.BaseObject#toString()
	 */
    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE).append("employeeTypeName", employeeTypeName).toString();
    }
}
