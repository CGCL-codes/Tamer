package net.sourceforge.ondex.wsapi.result;

import net.sourceforge.ondex.core.EvidenceType;

/**
 * The evidence type belonging to an Evidence for a Concept or Relation. It has
 * a mandatory name and an optional description field for additional
 * information.
 * 
 * @author David Withers
 */
public class WSEvidenceType extends WSMetaData {

    public WSEvidenceType() {
    }

    public WSEvidenceType(EvidenceType evidenceType) {
        super(evidenceType);
    }

    /*********************************************************************/
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return (id + ", " + fullname + ", " + description);
    }
}
