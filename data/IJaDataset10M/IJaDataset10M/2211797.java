package pub.beans;

import pub.db.*;
import java.util.*;

/**
 * NullAnnotationEvidenceBean to represent a nonexistant annotation evidence
 * bean.
 * @author Thomas Yan
 */
public class NullAnnotationEvidenceBean extends AnnotationEvidenceBeanImpl {

    public NullAnnotationEvidenceBean(PubConnection conn) {
        super(new HashMap(), conn);
    }

    public boolean isNull() {
        return false;
    }

    /** Stores changes to the bean back to the database */
    public void store(PubConnection conn) {
    }

    public void setEvidenceTypeId(String evidenceTypeId, UserBean updatedBy) {
    }

    public void setEvidenceDescription(String description, UserBean updatedBy) {
    }

    public void setPubReferenceId(String pubReferenceId, UserBean updatedBy) {
    }

    public void setEvidenceWith(String evidenceWith, UserBean updatedBy) {
    }

    public void setIsObsolete(boolean status, UserBean updatedBy) {
    }
}
