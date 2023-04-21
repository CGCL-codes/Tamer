package gov.lanl.didlwriter.profile.adore;

import org.adore.didl.content.DII;

/**
 * Simplified DII Related Identifier for aDORe usage
 * 
 * @author rchute
 */
public class AdoreDIIRelatedIdentifier {

    private String id;

    private String relType;

    /**
     * Gets the DII Identifier value
     * 
     * @return dii of item or component
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the DII Identifier value
     * 
     * @param id
     *            dii of item or component
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Gets the DII Relationship type
     * 
     * @return dii relationship type
     */
    public String getRelationshipType() {
        return relType;
    }

    /**
     * Sets the DII Relationship type
     * 
     * @param relTypeUri
     *            dii relationship type
     */
    public void setRelationshipType(String relTypeUri) {
        this.relType = relTypeUri;
    }

    /**
     * Creates a DII instance for the provided id
     * 
     * @return initialized DII with setId()
     */
    public DII create() {
        if (id == null) throw new NullPointerException();
        DII dii = new DII(DII.RELATED_IDENTIFIER, id);
        if (relType != null) dii.setRelationshipType(relType);
        return dii;
    }

    /**
     * Populates AdoreDIIRelatedIdentifier instance from DII
     * 
     * @param dii
     *            initialized DII
     */
    public void parse(DII dii) {
        id = dii.getValue();
        relType = dii.getRelationshipType();
    }
}
