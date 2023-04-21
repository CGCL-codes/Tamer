package clp.concept;

import clp.ast.ASTNode;
import clp.ast.List;
import clp.core.CLPEngineState;
import clp.core.Util;

public class ConceptRelation extends ConceptNode {

    public ConceptRelation() {
        super();
    }

    public ConceptRelation(String name) {
        super(name);
    }

    public ConceptRelation(String name, String pid) {
        super(name);
        setPhraseId(pid);
    }

    @Override
    public void setSlots() {
        addSlot("ordinal");
        addSlot("relatedto");
    }

    public String toString() {
        ConceptNode relatedTo = getSlot("relatedto");
        String strRelatedTo = "";
        if (relatedTo != null) {
            strRelatedTo = relatedTo.getValue();
        }
        return "[relation: " + strRelatedTo + "]";
    }

    public String displaySummary() {
        return toString();
    }
}
