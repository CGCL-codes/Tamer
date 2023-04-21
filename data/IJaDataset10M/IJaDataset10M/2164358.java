package org.qtitools.constructr.assessment;

import java.io.Serializable;
import org.qtitools.constructr.itembank.Item;
import org.qtitools.qti.node.test.AssessmentItemRef;
import org.qtitools.qti.node.test.AssessmentSection;

public class ItemModel implements Serializable {

    private static final long serialVersionUID = 1L;

    Item item;

    String itempath;

    SectionModel parent;

    public ItemModel(SectionModel p) {
        parent = p;
        item = null;
    }

    public SectionModel getParent() {
        return parent;
    }

    public void setParent(SectionModel parent) {
        this.parent = parent;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    @Override
    public String toString() {
        return getItem().getName();
    }

    public AssessmentItemRef convertToQTI(AssessmentSection as, String ident) {
        AssessmentItemRef air = new AssessmentItemRef(as);
        air.setIdentifier(ident);
        air.setHref(itempath);
        return air;
    }
}
