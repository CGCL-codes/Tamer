package com.avatal.content.business.ejb.entity.course;

import java.util.Date;
import java.util.GregorianCalendar;
import javax.ejb.EntityBean;
import javax.ejb.FinderException;
import com.avatal.DatabaseTableConstants;
import com.avatal.business.exception.EJBException;
import com.avatal.business.util.HomeInterfaceFinder;
import com.avatal.content.vo.course.FaqItemVo;
import com.avatal.vo.user.UserVo;

/**
 * @ejb.bean name="FaqItemEntity"
 *	jndi-name="FaqItemEntityBean"
 *  view-type="local"
 *	type="CMP"
 *  primkey-field="id"
  *  schema="FAQ_ITEM" 
 *  cmp-version="2.x"
 * 
 *  @ejb.persistence 
 *   table-name="FAQ_ITEM" 
 * 
* @ejb.finder 
 *    query="SELECT OBJECT(a) FROM FAQ_ITEM as a WHERE a.faq.id = ?1"  
 *    signature="java.util.Collection findByFaq(java.lang.Integer faqId)"  
 * 
*
 * com.avatal.content.business.ejb.entity.course.FaqItemEntity
 **/
public abstract class FaqItemEntityBean implements EntityBean {

    /**
	 * The  ejbCreate method.
	 * 
	 * @ejb.create-method 
	 */
    public java.lang.Integer ejbCreate(UserVo actor, Integer faqId, FaqItemVo forumMessage) throws javax.ejb.CreateException, FinderException, EJBException {
        setData(forumMessage);
        setId(com.avatal.business.util.EjbUtil.getNextId(DatabaseTableConstants.FAQ_ITEM_TABLE));
        Date date = new GregorianCalendar().getTime();
        setTimeCreated(date);
        return null;
    }

    /**
	 * The container invokes this method immediately after it calls ejbCreate.
	 * 
	 */
    public void ejbPostCreate(UserVo actor, Integer faqId, FaqItemVo forumMessage) throws javax.ejb.CreateException, FinderException, EJBException {
        FaqEntityLocal faq = HomeInterfaceFinder.getFaqHome().findByPrimaryKey(faqId);
        setFaq(faq);
    }

    /**
	* Returns the id
	* @return the id
	* 
	* @ejb.persistent-field 
	* @ejb.persistence
	*    column-name="Id"
	*     sql-type="int"
	* @ejb.pk-field 
	* @ejb.interface-method
	*/
    public abstract java.lang.Integer getId();

    /**
    * Sets the id
    * 
    * @param java.lang.Integer the new id value
    * 
    * @ejb.interface-method
    */
    public abstract void setId(java.lang.Integer id);

    /**
	* Returns the state of the object
	* @return the state of the object
	* 
	* @ejb.persistent-field 
	* @ejb.persistence
	*    column-name="ObjectState"
	*     sql-type="integer"
	* @ejb.interface-method
	*/
    public abstract java.lang.Integer getObjectState();

    /**
	* Sets the state of the object
	* 
	* @param java.lang.Integer the new state of the object value
	* 
	* @ejb.interface-method
	*/
    public abstract void setObjectState(java.lang.Integer id);

    /**
	* Returns the Title
	* @return the Title
	* 
	* @ejb.persistent-field 
	* @ejb.persistence
	*    column-name="Title"
	*     sql-type="varchar"
	*  
	* @ejb.interface-method
	*/
    public abstract java.lang.String getTitle();

    /**
	 * Sets the name
	 * 
	 * @param java.lang.String the new name value
	 * 
	 * @ejb.interface-method
	 */
    public abstract void setTitle(java.lang.String title);

    /**
	 * Returns the Text
	 * @return the Text
	 * 
	 * @ejb.persistent-field 
	 * @ejb.persistence
	 *    column-name="Text"
	 *     sql-type="text"
	 *  
	 * @ejb.interface-method
	 */
    public abstract java.lang.String getText();

    /**
	 * Sets the Text
	 * 
	 * @param java.lang.String the new Text value
	 * 
	 * @ejb.interface-method
	 */
    public abstract void setText(java.lang.String text);

    /**
	 * Returns the TimeCreated
	 * @return the TimeCreated
	 * 
	 * @ejb.persistent-field 
	 * @ejb.persistence
	 *    column-name="TimeCreated"
	 *     sql-type="date"
	 *  
	 * @ejb.interface-method
	 */
    public abstract java.util.Date getTimeCreated();

    /**
	 * 
	 * @ejb.interface-method
	 */
    public abstract void setTimeCreated(java.util.Date timeCreated);

    /**
     * Returns the TimeModified
     * @return the TimeModified
     * 
     * @ejb.persistent-field 
     * @ejb.persistence
     *    column-name="TimeModified"
     *     sql-type="date"
     *  
     * @ejb.interface-method
     */
    public abstract java.util.Date getTimeModified();

    /**
     * 
     * @ejb.interface-method
     */
    public abstract void setTimeModified(java.util.Date timeModified);

    /**
	* @ejb.interface-method
	*/
    public abstract FaqItemEntityData getData();

    /**
	* @ejb.interface-method
	*/
    public abstract void setData(FaqItemEntityData faqItemData);

    /**
	 * @ejb.interface-method
	 */
    public FaqItemVo getFaqItemVo() throws EJBException {
        FaqItemVo faq = new FaqItemVo(getData());
        return faq;
    }

    /**
	 * @ejb.relation
	 *    name="Faq-FaqItem"  
	 *    role-name="FaqItem-belongs-to-Faq"
	 *    target-multiple="yes" 	 
	 * @jboss.relation
	 * 	fk-column="Faq_Id"
	 * 	related-pk-field="id"
	 * @ejb.interface-method view-type="local"
	 */
    public abstract void setFaq(FaqEntityLocal faq);

    /**
	* @ejb.interface-method
	*/
    public abstract FaqEntityLocal getFaq();

    /** 
	 * @ejb:interface-method view-type="local"
	 */
    public boolean equals(Object obj) {
        if (obj instanceof FaqItemEntityBean) {
            if (((FaqItemEntityBean) obj).getId().equals(getId())) {
                return true;
            } else {
                return false;
            }
        } else {
            return super.equals(obj);
        }
    }
}
