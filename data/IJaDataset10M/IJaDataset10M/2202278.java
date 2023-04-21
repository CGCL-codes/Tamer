package cowsultants.itracker.ejb.beans.entity;

import javax.ejb.CreateException;
import javax.ejb.EntityBean;

/**
 * @ejb.bean
 *        cmp-version = "2.x"
 *        name = "IDSeries"
 *        primkey-field = "id"
 *        type = "CMP"
 *        view-type = "local"
 *
 * @ejb.persistence
 *        table-name = "iTrackerIDSeries"
 *
 * @ejb.finder
 *        query = "SELECT OBJECT(o) FROM IDSeries o"
 *        signature = "java.util.Collection findAll()"
 *
 * @ejb.finder
 *        query = "SELECT OBJECT(o) FROM IDSeries o WHERE o.id = ?1"
 *        signature = "IDSeriesLocal findByName(java.lang.String name)"
 *
 * @author  tyler
 */
public abstract class IDSeriesBean implements EntityBean {

    /**
     * @ejb.create-method
     *
     * @throws  CreateException
     * @param  value The new ID of this bean.
     */
    public String ejbCreate(String name, Integer firstID) throws CreateException {
        this.setId(name);
        this.setValue(firstID);
        return null;
    }

    public void ejbPostCreate(String name, Integer firstID) throws CreateException {
    }

    /**
     * @ejb.persistence
     *
     * @ejb.interface-method
     *
     * @ejb.pk-field
     *
     * @return  ID
     */
    public abstract String getId();

    /**
     * @ejb.interface-method
     *
     */
    public abstract void setId(String value);

    /**
     * @ejb.persistence
     *
     * @ejb.interface-method
     *
     * @return  ID
     */
    public abstract Integer getValue();

    /**
     * @ejb.interface-method
     *
     */
    public abstract void setValue(Integer value);
}
