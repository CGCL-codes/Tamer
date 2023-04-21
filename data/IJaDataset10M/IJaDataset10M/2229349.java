package es.eucm.eadventure.editor.data.meta.ims;

import es.eucm.eadventure.editor.data.meta.Vocabulary;

public class IMSRights {

    private Vocabulary cost;

    private Vocabulary copyrightandotherrestrictions;

    public IMSRights() {
        cost = new Vocabulary(Vocabulary.IMS_YES_NO);
        copyrightandotherrestrictions = new Vocabulary(Vocabulary.IMS_YES_NO);
        ;
    }

    public Vocabulary getCost() {
        return cost;
    }

    public void setCost(int index) {
        this.cost.setValueIndex(index);
    }

    public Vocabulary getCopyrightandotherrestrictions() {
        return copyrightandotherrestrictions;
    }

    public void setCopyrightandotherrestrictions(int index) {
        this.copyrightandotherrestrictions.setValueIndex(index);
    }
}
