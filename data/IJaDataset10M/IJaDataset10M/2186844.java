package org.gwtcmis.object.impl;

import org.gwtcmis.object.CmisObject;
import org.gwtcmis.object.CmisRelationship;
import java.util.HashSet;

/**
 * Created by The eXo Platform SAS.
 *	
 * @author <a href="mailto:zhulevaanna@gmail.com">Ann Zhuleva</a>
 * @version $Id:   ${date} ${time}
 *
 */
public class CmisRelationshipImpl extends CmisObjectImpl implements CmisRelationship {

    /**
    * Source object of the relationship
    */
    private String sourceId;

    /**
    * Target object of the relationship
    */
    private String targetId;

    public CmisRelationshipImpl(CmisObject object, String sourceId, String targetId) {
        super(object.getProperties().getProperties(), object.getACL(), object.isExactACL(), new HashSet<String>(object.getPolicyIds()), object.getRelationship(), object.getRenditions(), object.getAllowableActions(), object.getChangeInfo(), object.getObjectInfo(), object.getPathSegment());
        this.sourceId = sourceId;
        this.targetId = targetId;
    }

    /**
    * @see org.gwtcmis.object.CmisRelationship#getSourceId()
    */
    public String getSourceId() {
        return sourceId;
    }

    /**
    * @see org.gwtcmis.object.CmisRelationship#getTargetId()
    */
    public String getTargetId() {
        return targetId;
    }

    /**
    * @see org.gwtcmis.object.CmisRelationship#setSourceId(java.lang.String)
    */
    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    /**
    * @see org.gwtcmis.object.CmisRelationship#setTargetId(java.lang.String)
    */
    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }
}
