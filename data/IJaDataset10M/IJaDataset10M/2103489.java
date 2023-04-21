package test.ejb;

import java.util.Date;
import javax.ejb.CreateException;
import javax.ejb.EntityBean;
import javax.ejb.EntityContext;
import javax.ejb.RemoveException;

/**
 * @author <a href="mailto:julien_viet@yahoo.fr">Julien Viet</a>
 * @version $Revision: 1.1 $
 *
 * @ejb.bean
 *    name="File"
 *    displayContent-name="File"
 *    type="CMP"
 *    cmp-version="2.x"
 *    view-type="local"
 *    local-jndi-name="${ejb.prefix}/File"
 *    schema="file"
 *    primkey-field="id"
 *
 * @ejb.pk
 *    class="java.lang.String"
 *
 * @ejb.transaction
 *    type="Required"
 *
 * @ejb.finder
 *    signature="test.ejb.FileBeanLocal findByPrimaryKey(java.lang.String id)"
 *
 * @ejb.finder
 *    signature="java.util.Collection findAll()"
 *    query="SELECT OBJECT(f) FROM file AS f"
 *
 * @ejb.finder
 *    signature="java.util.Collection findRangeOrderById(java.lang.Integer start, java.lang.Integer size)"
 *    query="SELECT OBJECT(f) FROM file AS f"
 *
 * @ejb.finder
 *    signature="java.util.Collection findRangeOrderByCreationDate(java.lang.Integer start, java.lang.Integer size)"
 *    query="SELECT OBJECT(f) FROM file AS f"
 *
 * @ejb.finder
 *    signature="java.util.Collection findRangeOrderByContentType(java.lang.Integer start, java.lang.Integer size)"
 *    query="SELECT OBJECT(f) FROM file AS f"
 *
 * @jboss.query
 *    signature="java.util.Collection findRangeOrderById(java.lang.Integer start, java.lang.Integer size)"
 *    query="SELECT OBJECT(f) FROM file AS f ORDER BY f.id OFFSET ?1 LIMIT ?2"
 *
 * @jboss.query
 *    signature="java.util.Collection findRangeOrderByCreationDate(java.lang.Integer start, java.lang.Integer size)"
 *    query="SELECT OBJECT(f) FROM file AS f ORDER BY f.creationDate OFFSET ?1 LIMIT ?2"
 *
 * @jboss.query
 *    signature="java.util.Collection findRangeOrderByContentType(java.lang.Integer start, java.lang.Integer size)"
 *    query="SELECT OBJECT(f) FROM file AS f ORDER BY f.contentType OFFSET ?1 LIMIT ?2"
 *
 * @jboss.persistence
 *    create-table="true"
 *    remove-table="true"
 *
 * @jboss.read-ahead
 *    strategy="on-load"
 *    page-size="10"
 *    eager-load-group="lightweight"
 *
 * @jboss.eager-load-group
 *    name="lightweight"
 *
 * @jboss.lazy-load-group
 *    name="heavyweight"
 *
 * @jboss.load-group
 *    name="lightweight"
 *
 * @jboss.load-group
 *    name="heavyweight"
 *
 */
public abstract class FileBean implements EntityBean {

    /**
    * @ejb.create-method
    */
    public String ejbCreate(String id, String contentType, byte[] content) throws CreateException {
        setId(id);
        setContentType(contentType);
        setContent(content);
        setCreationDate(new Date());
        return null;
    }

    public void ejbPostCreate(String id, int uid, String contentType, byte[] content) {
    }

    /**
    * @ejb.interface-method
    * @ejb.persistent-field
    * @ejb.pk-field
    * @jboss.load-group
    *    name="lightweight"
    * @jboss.load-group
    *    name="heavyweight"
    */
    public abstract String getId();

    /**
    * @ejb.interface-method
    */
    public abstract void setId(String id);

    /**
    * @ejb.interface-method
    * @ejb.persistence
    * @jboss.load-group
    *    name="heavyweight"
    */
    public abstract byte[] getContent();

    /**
    * @ejb.interface-method
    */
    public abstract void setContent(byte[] content);

    /**
    * @ejb.interface-method
    * @ejb.persistence
    * @jboss.load-group
    *    name="lightweight"
    * @jboss.load-group
    *    name="heavyweight"
    */
    public abstract String getContentType();

    /**
    * @ejb.interface-method
    */
    public abstract void setContentType(String contentType);

    /**
    * @ejb.interface-method
    * @ejb.persistence
    * @jboss.load-group
    *    name="lightweight"
    * @jboss.load-group
    *    name="heavyweight"
    */
    public abstract java.util.Date getCreationDate();

    /**
    * @ejb.interface-method
    */
    public abstract void setCreationDate(java.util.Date creationDate);

    public void ejbActivate() {
    }

    public void ejbPassivate() {
    }

    public void ejbRemove() throws RemoveException {
    }

    public void setEntityContext(EntityContext entityContext) {
    }

    public void unsetEntityContext() {
    }

    public void ejbLoad() {
    }

    public void ejbStore() {
    }
}
