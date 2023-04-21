package com.avatal.content.business.ejb.entity.scormcourse;

import java.util.Collection;
import java.util.Iterator;
import javax.ejb.CreateException;
import javax.ejb.EntityBean;
import javax.ejb.FinderException;
import javax.naming.NamingException;
import com.avatal.business.exception.ServiceLocatorException;
import com.avatal.business.util.AvArrayList;
import com.avatal.business.util.ServiceLocator;
import com.avatal.content.vo.course.scorm.scormMetaData.CatalogEntry;
import com.avatal.content.vo.course.scorm.scormMetaData.General;

/**
 * @ejb.bean name="GeneralEntity"
 *	jndi-name="GeneralEntityBean"
 *  view-type="local"
 *	type="CMP"
 *  primkey-field="id"
 *  schema="LSGeneral" 
 *  cmp-version="2.x"
 * 
 *  @ejb.persistence 
 *   table-name="GENERAL" 
 * 
 **/
public abstract class GeneralEntityBean implements EntityBean {

    /**
	 * The  ejbCreate method.
	 * 
	 * @ejb.create-method 
	 */
    public java.lang.Integer ejbCreate(General general) throws NamingException, ServiceLocatorException, CreateException, FinderException {
        setId(com.avatal.business.util.EjbUtil.getNextId(com.avatal.DatabaseTableConstants.GENERAL_TABLE));
        Iterator iterator = general.descriptions.iterator();
        StringBuffer buffer = new StringBuffer();
        while (iterator.hasNext()) {
            buffer.append(iterator.next().toString());
            if (iterator.hasNext()) {
                buffer.append(",");
            }
        }
        setDescription(buffer.toString());
        setIdentifier(general.identifier);
        setKeywords(general.keywords.toString());
        setLanguage(general.languages.toString());
        setTitle(general.title);
        return null;
    }

    /**
	 * The container invokes this method immediately after it calls ejbCreate.
	 * 
	 */
    public void ejbPostCreate(General general) throws NamingException, ServiceLocatorException, CreateException, FinderException {
        Iterator iterator = general.catalogentrys.iterator();
        AvArrayList CatalogEntrysLocalCollection = new AvArrayList();
        while (iterator.hasNext()) {
            CatalogEntryEntityLocalHome catalogEntryEntityLocalHome = (CatalogEntryEntityLocalHome) ServiceLocator.getInstance().getLocalHome(CatalogEntryEntityLocalHome.JNDI_NAME, CatalogEntryEntityLocalHome.class);
            CatalogEntryEntityLocal catalogEntryEntityLocal = catalogEntryEntityLocalHome.create((CatalogEntry) iterator.next(), getId());
            CatalogEntrysLocalCollection.add(catalogEntryEntityLocal);
        }
        this.setCatalogEntrys(CatalogEntrysLocalCollection);
    }

    /**
	* Returns the aggregationLevel
	* @return the aggregationLevel
	* 
	* @ejb.persistent-field 
	* @ejb.persistence
	*    column-name="AggregationLevel"
	*     sql-type="VARCHAR"
	*  
	* @ejb.interface-method
	*/
    public abstract java.lang.String getAggregationLevel();

    /**
	* Sets the aggregationLevel
	* 
	* @param java.lang.String the new aggregationLevel value
	* 
	* @ejb.interface-method
	*/
    public abstract void setAggregationLevel(java.lang.String aggregationLevel);

    /**
	* @ejb.relation
	*    name="General-CatalogEntry"
	*    role-name="General-has-CatalogEntry"	 
	* @ejb.interface-method view-type="local"
	*/
    public abstract Collection getCatalogEntrys();

    /**
	* Sets ClassificationElements
	*
	* @param  AvArrayList of Classifications
	*
	* @ejb:interface-method view-type="local"
	*/
    public abstract void setCatalogEntrys(java.util.Collection catalogEntrys);

    /**
	* Returns the coverage
	* @return the coverage
	* 
	* @ejb.persistent-field 
	* @ejb.persistence
	*    column-name="Coverage"
	*     sql-type="VARCHAR"  
		* @ejb.interface-method
		*/
    public abstract java.lang.String getCoverage();

    /**
	* Sets the coverage
	* 
	* @param java.lang.String the new coverage value
	* 
	* @ejb.interface-method
	*/
    public abstract void setCoverage(java.lang.String coverage);

    /**
	* Returns the description
	* @return the descirption
	* 
	* @ejb.persistent-field 
	* @ejb.persistence
	*    column-name="Description"
	*     jdbc-type="VARCHAR"
	*     sql-type="TEXT"  
	* @ejb.interface-method
	*/
    public abstract java.lang.String getDescription();

    /**
	* Sets the description
	* 
	* @param java.lang.String the new description value
	* 
	* @ejb.interface-method
	*/
    public abstract void setDescription(java.lang.String description);

    /**
	* Returns the identifier
	* @return the identifier
	* 
	* @ejb.persistent-field 
	* @ejb.persistence
	*    column-name="Identifier"
	*     sql-type="VARCHAR"  
	* @ejb.interface-method
	*/
    public abstract java.lang.String getIdentifier();

    /**
	* Sets the identifer
	* 
	* @param java.lang.String the new identifier value
	* 
	* @ejb.interface-method
	*/
    public abstract void setIdentifier(java.lang.String identifier);

    /**
	* Returns the keywords
	* @return the keywords
	* 
	* @ejb.persistent-field 
	* @ejb.persistence
	*    column-name="Keywords"
	*     sql-type="VARCHAR"  
	* @ejb.interface-method
	*/
    public abstract java.lang.String getKeywords();

    /**
	* Sets the keywords
	* 
	* @param java.lang.String the new keywords value
	* 
	* @ejb.interface-method
	*/
    public abstract void setKeywords(java.lang.String keywords);

    /**
	* Returns the language
	* @return the language
	* 
	* @ejb.persistent-field 
	* @ejb.persistence
	*    column-name="Language"
	*     sql-type="VARCHAR"  
	* @ejb.interface-method
	*/
    public abstract java.lang.String getLanguage();

    /**
	* Sets the language
	* 
	* @param java.lang.String the new keywords value
	* 
	* @ejb.interface-method
	*/
    public abstract void setLanguage(java.lang.String language);

    /**
	* Returns the structure
	* @return the structure
	* 
	* @ejb.persistent-field 
	* @ejb.persistence
	*    column-name="Structure"
	*     sql-type="VARCHAR"  
	* @ejb.interface-method
	*/
    public abstract java.lang.String getStructure();

    /**
	* Sets the structure
	* 
	* @param java.lang.String the new structure value
	* 
	* @ejb.interface-method
	*/
    public abstract void setStructure(java.lang.String structure);

    /**
	* Returns the title
	* @return the title
	* 
	* @ejb.persistent-field 
	* @ejb.persistence
	*    column-name="Title"
	*     sql-type="VARCHAR"  
	* @ejb.interface-method
	*/
    public abstract java.lang.String getTitle();

    /**
	* Sets the title
	* 
	* @param java.lang.String the new title value
	* 
	* @ejb.interface-method
	*/
    public abstract void setTitle(java.lang.String title);

    /**
	 * @ejb.interface-method
	 *	tview-type="remote" 
	**/
    public General getGeneral() {
        General general = new General();
        return general;
    }

    /**
	* Returns the id
	* @return the id
	* 
	* @ejb.persistent-field 
	* @ejb.persistence
	*    column-name="id"
	*     sql-type="Integer"
	* @ejb.pk-field 
	* @ejb.interface-method
	*/
    public abstract java.lang.Integer getId();

    /**
	* Sets the id
	* 
	* @param java.lang.String the new id value
	* 
	* @ejb.interface-method
	*/
    public abstract void setId(java.lang.Integer id);
}
