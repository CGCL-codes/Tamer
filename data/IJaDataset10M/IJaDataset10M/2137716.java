package serene.validation.schema.active.components;

import serene.validation.schema.simplified.SimplifiedComponent;
import serene.validation.schema.active.ActiveComponentVisitor;
import serene.validation.schema.active.ActiveComponent;

abstract class AbstractActiveComponent implements ActiveComponent {

    protected int childIndex;

    AbstractActiveComponent() {
        childIndex = -1;
    }

    void setChildIndex(int childIndex) {
        this.childIndex = childIndex;
    }

    public int getChildIndex() {
        return childIndex;
    }
}
