package aerosys.ejb;

import java.rmi.RemoteException;
import javax.ejb.EJBException;
import javax.ejb.EntityBean;
import javax.ejb.EntityContext;
import javax.ejb.RemoveException;
import aerosys.interfaces.FlugzeugtypValue;

/**
 * @ejb.bean name="Flugzeugtyp"
 *           display-name="Name for Flugzeugtyp"
 *           description="Description for Flugzeugtyp"
 *           jndi-name="ejb/Flugzeugtyp"
 *           type="CMP"
 *           cmp-version="2.x"
 *           view-type="local"
 * @ejb.value-object
 * 			name = "Flugzeugtyp"
 * 			match = "*"
 * @ejb.finder
 *   result-type-mapping = "Local"
 *   view-type = "local"
 *   signature="java.util.Collection findAll()"
 *   query="SELECT OBJECT(p) FROM Flugzeugtyp AS p"
 *   
 * @ejb.finder
 *   result-type-mapping = "Local"
 *   view-type = "local"
 *   signature="FlugzeugtypLocal findByName(java.lang.String name)"
 *   query="SELECT OBJECT(m) FROM Flugzeugtyp AS m WHERE m.name = ?1"
 */
public abstract class Flugzeugtyp implements EntityBean {

    public Flugzeugtyp() {
        super();
    }

    public void setEntityContext(EntityContext ctx) throws EJBException, RemoteException {
    }

    public void unsetEntityContext() throws EJBException, RemoteException {
    }

    public void ejbRemove() throws RemoveException, EJBException, RemoteException {
    }

    public void ejbActivate() throws EJBException, RemoteException {
    }

    public void ejbPassivate() throws EJBException, RemoteException {
    }

    public void ejbLoad() throws EJBException, RemoteException {
    }

    public void ejbStore() throws EJBException, RemoteException {
    }

    /**
	 * Getter for CMP Field flugzeugtypid
	 *
	 * @ejb.pk-field
	 * @ejb.persistent-field
	 * @ejb.interface-method   view-type="local"
	 */
    public abstract java.lang.Integer getFlugzeugtypid();

    /**
	 * Setter for CMP Field flugzeugtypid
	 *
	 * @ejb.interface-method   view-type="local"
	 */
    public abstract void setFlugzeugtypid(java.lang.Integer value);

    /**
	 * Getter for CMP Field kurzname
	 *
	 * 
	 * @ejb.persistent-field
	 * @ejb.interface-method   view-type="local"
	 */
    public abstract java.lang.String getKurzname();

    /**
	 * Setter for CMP Field kurzname
	 *
	 * @ejb.interface-method   view-type="local"
	 */
    public abstract void setKurzname(java.lang.String value);

    /**
	 * Getter for CMP Field name
	 *
	 * 
	 * @ejb.persistent-field
	 * @ejb.interface-method   view-type="local"
	 */
    public abstract java.lang.String getName();

    /**
	 * Setter for CMP Field name
	 *
	 * @ejb.interface-method   view-type="local"
	 */
    public abstract void setName(java.lang.String value);

    /**
	 * Getter for CMP Field gebuehrid
	 *
	 * 
	 * @ejb.persistent-field
	 * @ejb.interface-method   view-type="local"
	 */
    public abstract java.lang.Integer getGebuehrid();

    /**
	 * Setter for CMP Field gebuehrid
	 *
	 * @ejb.interface-method   view-type="local"
	 */
    public abstract void setGebuehrid(java.lang.Integer value);

    /**
	 * Create method
	 * @ejb.create-method  view-type = "local"
	 */
    public aerosys.interfaces.FlugzeugtypPK ejbCreate(java.lang.Integer flugzeugid, java.lang.String kurzname, java.lang.String name, java.lang.Integer gebuehrid) throws javax.ejb.CreateException {
        setFlugzeugtypid(flugzeugid);
        setKurzname(kurzname);
        setName(name);
        setGebuehrid(gebuehrid);
        return null;
    }

    /**
	 * Post Create method
	 */
    public void ejbPostCreate(java.lang.Integer flugzeugid, java.lang.String kurzname, java.lang.String name, java.lang.Integer gebuehrid) throws javax.ejb.CreateException {
    }

    /**
	 * Business method
	 * @ejb.interface-method  view-type = "local"
	 */
    public aerosys.interfaces.FlugzeugtypValue getValue() {
        FlugzeugtypValue temp = new FlugzeugtypValue();
        temp.setFlugzeugtypid(this.getFlugzeugtypid());
        temp.setKurzname(this.getKurzname());
        temp.setName(this.getName());
        temp.setGebuehrid(this.getGebuehrid());
        return temp;
    }

    /**
	 * Getter for CMR Relationship
	 *
	 * @ejb.interface-method   view-type="local"
	 * @ejb.relation           name = "Flugzeugtyp-Gebuehr"
	 *                         role-name = "Flugzeugtyp2Gebuehr"
	 *                         target-ejb = "Gebuehr"
	 *                         target-role-name = "Gebuehr2Flugzeugtyp"
	 *                         target-multiple = "no"
	 * @jboss.relation-mapping
	 *	 						style="foreign-key"
	 * @jboss.relation
	 * 							fk-column="gebuehrid"
	 * 							related-pk-field="gebuehrid"
	 */
    public abstract aerosys.interfaces.GebuehrLocal getGebuehr();

    /**
	 * Setter for CMR Relationship
	 *
	 * @ejb.interface-method   view-type="local"
	 */
    public abstract void setGebuehr(aerosys.interfaces.GebuehrLocal value);
}
