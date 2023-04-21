package org.nakedobjects.nos.remote.command.java;

import org.nakedobjects.nof.core.persist.AllInstances;

public class AllInstancesCriteriaData extends AbstractCriteriaData {

    private static final long serialVersionUID = 1L;

    public AllInstancesCriteriaData(AllInstances instances) {
        super(instances);
    }

    public Class getCriteriaClass() {
        return AllInstances.class;
    }
}
