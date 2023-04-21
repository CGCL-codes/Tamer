package com.velocityme.entity;

import javax.ejb.*;
import javax.naming.*;
import java.rmi.*;
import javax.rmi.*;
import java.util.*;
import com.velocityme.interfaces.*;
import com.velocityme.interfaces.ServiceUnavailableException;
import com.velocityme.utility.ServerConfiguration;
import com.velocityme.valueobjects.FileAttachmentValue;
import java.io.File;

/**
 *
 * @author  Robert
 * @ejb.bean
 *           type="CMP"
 *           cmp-version="2.x"
 *           name="FileAttachment"
 *           jndi-name="ejb/FileAttachment"
 *           view-type="local"
 * @ejb.transaction type="Required"
 *
 * @ejb.value-object match="*"
 *
 * @ejb.persistence table-name="fileattachment"
 *
 * @ejb.util generate="physical"
 *
 * @ejb.finder signature="com.velocityme.interfaces.FileAttachment findByServerName(java.lang.String serverName)"
 *              query="SELECT OBJECT(o) FROM FileAttachment o WHERE o.serverName = ?1"
 *
 * @jboss.persistence create-table="true"
 *                    remove-table="false"
 */
public abstract class FileAttachmentBean implements EntityBean {

    /**
    * Context set by container
    */
    private javax.ejb.EntityContext m_entityContext;

    public void setEntityContext(javax.ejb.EntityContext entityContext) {
        m_entityContext = entityContext;
    }

    public void unsetEntityContext() {
        m_entityContext = null;
    }

    /** @ejb.create-method */
    public com.velocityme.interfaces.FileAttachmentPK ejbCreate(java.lang.String p_name, java.lang.String p_contentType, com.velocityme.interfaces.ChangeDeltaLocal p_changeDeltaLocal) throws CreateException {
        try {
            setFileAttachmentId(generateNewID());
        } catch (ServiceUnavailableException e) {
            throw new EJBException(e.getMessage());
        }
        return null;
    }

    public void ejbPostCreate(java.lang.String p_name, java.lang.String p_contentType, com.velocityme.interfaces.ChangeDeltaLocal p_changeDeltaLocal) throws CreateException {
        setName(p_name);
        setServerName(FileAttachmentUtil.generateGUID(this));
        setContentType(p_contentType);
        setIsCompressed(Boolean.FALSE);
        setLength(new Long(-1));
        setChangeDeltaLocal(p_changeDeltaLocal);
    }

    /**
     * Retrive a unique creation id to use for this bean.  This will end up
     * demarcating this bean from others when it is stored as a record
     * in the database.
     *
     * @return Returns an integer that can be used as a unique creation id.
     *
     * @throws ServiceUnavailableException Indicating that it was not possible
     *                                     to retrieve a new unqiue ID because
     *                                     the service is not available
     **/
    private Integer generateNewID() throws ServiceUnavailableException {
        int lUniqueId = -1;
        try {
            SequenceGenerator lBean = SequenceGeneratorUtil.getHome().create();
            lUniqueId = lBean.getNextNumber("fileattachment", "fileAttachmentId");
            lBean.remove();
        } catch (NamingException ne) {
            throw new ServiceUnavailableException("Naming lookup failure: " + ne.getMessage());
        } catch (CreateException ce) {
            throw new ServiceUnavailableException("Failure while creating a generator session bean: " + ce.getMessage());
        } catch (RemoveException re) {
        } catch (RemoteException rte) {
            throw new ServiceUnavailableException("Remote exception occured while accessing generator session bean: " + rte.getMessage());
        }
        return new Integer(lUniqueId);
    }

    /**
     *  This gets a local object from a remote object.
     *  @ejb.interface-method view-type="remote"
     */
    public com.velocityme.interfaces.FileAttachmentLocal getLocal() {
        return (FileAttachmentLocal) m_entityContext.getEJBLocalObject();
    }

    /** @ejb.interface-method view-type="local" */
    public abstract com.velocityme.valueobjects.FileAttachmentValue getFileAttachmentValue();

    /** @ejb.interface-method view-type="local" */
    public abstract void setFileAttachmentValue(com.velocityme.valueobjects.FileAttachmentValue p_value);

    /**
     * @ejb.persistence column-name="fileAttachmentId"
     * @ejb.interface-method view-type="local"
     * @ejb.pk-field 
     */
    public abstract java.lang.Integer getFileAttachmentId();

    /** @ejb.interface-method view-type="local" */
    public abstract void setFileAttachmentId(java.lang.Integer fileAttachmentId);

    /**
     * @ejb.persistence column-name="name"
     * @ejb.interface-method view-type="local"
     */
    public abstract java.lang.String getName();

    /** @ejb.interface-method view-type="local" */
    public abstract void setName(java.lang.String p_name);

    /**
     * @ejb.persistence column-name="serverName"
     * @ejb.interface-method view-type="local"
     */
    public abstract java.lang.String getServerName();

    /** @ejb.interface-method view-type="local" */
    public abstract void setServerName(java.lang.String p_serverName);

    /**
     * @ejb.persistence column-name="contentType"
     * @ejb.interface-method view-type="local"
     */
    public abstract java.lang.String getContentType();

    /** @ejb.interface-method view-type="local" */
    public abstract void setContentType(java.lang.String p_contentType);

    /**
     * @ejb.persistence column-name="isCompressed"
     * @ejb.interface-method view-type="local"
     */
    public abstract java.lang.Boolean getIsCompressed();

    /** @ejb.interface-method view-type="local" */
    public abstract void setIsCompressed(java.lang.Boolean isCompressed);

    /**
     * @ejb.persistence column-name="length"
     * @ejb.interface-method view-type="local"
     */
    public abstract java.lang.Long getLength();

    /** @ejb.interface-method view-type="local" */
    public abstract void setLength(java.lang.Long length);

    /**
     * @ejb.interface-method view-type="local"
     * @ejb.relation name="FileAttachment-ChangeDelta"
     *               role-name="FileAttachment-has-a-ChangeDelta"
     * @jboss.relation fk-column="changeDeltaIdFk"
     *                 related-pk-field="changeDeltaId"
     */
    public abstract com.velocityme.interfaces.ChangeDeltaLocal getChangeDeltaLocal();

    /** @ejb.interface-method view-type="local" */
    public abstract void setChangeDeltaLocal(com.velocityme.interfaces.ChangeDeltaLocal changeDeltaLocal);

    /**
     * @ejb.interface-method view-type="local"
     */
    public java.io.File getFile() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(ServerConfiguration.getInstance().getFileAttachmentHome());
        stringBuffer.append(File.separatorChar);
        stringBuffer.append("dir");
        stringBuffer.append(Integer.toString(getFileAttachmentId().intValue() / 100));
        stringBuffer.append(File.separatorChar);
        stringBuffer.append(getServerName());
        return new File(stringBuffer.toString());
    }
}
