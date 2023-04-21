package org.semanticweb.skos;

/**
 * Author: Simon Jupp<br>
 * Date: Apr 25, 2008<br>
 * The University of Manchester<br>
 * Bio-Health Informatics Group<br>
 */
public abstract class SKOSChange {

    private SKOSDataset set;

    private SKOSAssertion ass;

    public SKOSChange(SKOSDataset dataSet, SKOSAssertion assertion) {
        this.set = dataSet;
        this.ass = assertion;
    }

    public SKOSDataset getSKOSDataset() {
        return set;
    }

    public SKOSAssertion getSKOSAssertion() {
        return ass;
    }

    public abstract boolean isAdd();

    public abstract boolean isRemove();
}
