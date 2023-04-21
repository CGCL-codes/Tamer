package test.ejb.cmr;

import javax.ejb.EntityBean;
import javax.ejb.CreateException;
import test.ejb.cmr.EmployeeAddressValue;

/**
 * @ejb.bean
 *   name="EmployeeAddress"
 *   type="CMP"
 *   cmp-version="2.x"
 *   primkey-field="id"
 *   view-type="local"
 *
 * @ejb.persistence
 *   table-name="address"
 * @ejb.value-object
 *   name="EmployeeAddress"
 *   match="*"
 *
 * @author mvsbrito
 * @version $Revision: 1.3 $ $Date: 2003/03/04 04:48:48 $
 */
public abstract class EmployeeAddressBean implements EntityBean {

    /**
     * @ejb.interface-method
     *   view-type="local"
     * @ejb.persistence
     *   column-name="addr_id"
     */
    public abstract Integer getId();

    public abstract void setId(Integer id);

    /**
     * @ejb.interface-method
     *   view-type="local"
     * @ejb.persistence
     *   column-name="addr_description"
     */
    public abstract String getDescription();

    /**
     * @ejb.interface-method
     *   view-type="local"
     */
    public abstract void setDescription(String description);

    /**
     * @ejb.interface-method
     *   view-type="local"
     */
    public abstract EmployeeAddressValue getEmployeeAddressValue();

    /**
     * @ejb.interface-method
     *   view-type="local"
     */
    public abstract void setEmployeeAddressValue(EmployeeAddressValue value);

    /**
     * @ejb:create-method
     */
    public Integer ejbCreate(EmployeeAddressValue eav) throws CreateException {
        setId(eav.getId());
        return eav.getId();
    }
}
