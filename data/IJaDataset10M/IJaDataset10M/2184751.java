package ch.arpage.collaboweb.struts.forms;

import java.util.Map;
import ch.arpage.collaboweb.model.RelationshipType;

/**
 * ActionForm for the relationship type model class
 * 
 * @see RelationshipType
 * @author <a href="mailto:patrick@arpage.ch">Patrick Herber</a>
 */
public class RelationshipTypeForm extends AbstractForm {

    /**
	 * serialVersionUID
	 */
    private static final long serialVersionUID = 1L;

    private RelationshipType relationshipType;

    /**
	 * Creates a new RelationshipTypeForm.
	 */
    public RelationshipTypeForm() {
        this(new RelationshipType());
    }

    /**
	 * Returns the relationshipType.
	 * @return the relationshipType
	 */
    public RelationshipType getRelationshipType() {
        return relationshipType;
    }

    /**
	 * 
	 * @param relationshipType
	 */
    public RelationshipTypeForm(RelationshipType relationshipType) {
        this.relationshipType = relationshipType;
    }

    /**
	 * Returns the communityId
	 * @return the communityId
	 * @see ch.arpage.collaboweb.model.RelationshipType#getCommunityId()
	 */
    public long getCommunityId() {
        return relationshipType.getCommunityId();
    }

    /**
	 * Returns the labels
	 * @return the labels
	 * @see ch.arpage.collaboweb.model.LabelableBean#getLabels()
	 */
    public Map<String, String> getLabels() {
        return relationshipType.getLabels();
    }

    /**
	 * Returns the relationshipTypeId
	 * @return the relationshipTypeId
	 * @see ch.arpage.collaboweb.model.RelationshipType#getRelationshipTypeId()
	 */
    public int getRelationshipTypeId() {
        return relationshipType.getRelationshipTypeId();
    }

    /**
	 * Set the communityId.
	 * @param communityId	The communityId to set
	 * @see ch.arpage.collaboweb.model.RelationshipType#setCommunityId(long)
	 */
    public void setCommunityId(long communityId) {
        relationshipType.setCommunityId(communityId);
    }

    /**
	 * Set the labels.
	 * @param labels	The labels to set
	 * @see ch.arpage.collaboweb.model.LabelableBean#setLabels(java.util.Map)
	 */
    public void setLabels(Map<String, String> labels) {
        relationshipType.setLabels(labels);
    }

    /**
	 * Set the relationshipTypeId.
	 * @param relationshipTypeId	The relationshipTypeId to set
	 * @see ch.arpage.collaboweb.model.RelationshipType#setRelationshipTypeId(int)
	 */
    public void setRelationshipTypeId(int relationshipTypeId) {
        relationshipType.setRelationshipTypeId(relationshipTypeId);
    }
}
