package org.hl7.CTSVAPI;

/**
 * A relation code was supplied that isn't valid for the code system
 */
public final class UnknownRelationshipCode extends java.lang.Exception {

    public String relationship_code = null;

    public UnknownRelationshipCode() {
    }

    public UnknownRelationshipCode(String _relationship_code) {
        relationship_code = _relationship_code;
    }

    public UnknownRelationshipCode(String $reason, String _relationship_code) {
        super($reason);
        relationship_code = _relationship_code;
    }
}
