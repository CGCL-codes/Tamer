package org.openemed.LQS;

/***/
public final class AssociationNotInSystemization extends Exception {

    public AssociationNotInSystemization() {
    }

    public AssociationNotInSystemization(String _bad_association_id) {
        bad_association_id = _bad_association_id;
    }

    public AssociationNotInSystemization(String _reason, String _bad_association_id) {
        super(_reason);
        bad_association_id = _bad_association_id;
    }

    public String bad_association_id;
}
