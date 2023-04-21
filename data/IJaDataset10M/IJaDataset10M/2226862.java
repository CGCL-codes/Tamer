package org.semanticweb.skos;

/**
 * Author: Simon Jupp<br>
 * Date: Mar 12, 2008<br>
 * The University of Manchester<br>
 * Bio-Health Informatics Group<br>
 */
public class RemoveAssertion extends SKOSChange {

    public RemoveAssertion(SKOSDataset dataSet, SKOSAssertion assertion) {
        super(dataSet, assertion);
    }

    public boolean isAdd() {
        return false;
    }

    public boolean isRemove() {
        return true;
    }
}
