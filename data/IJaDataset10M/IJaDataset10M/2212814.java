package org.qsardb.model;

import javax.xml.bind.annotation.*;

@XmlRootElement(name = "Workflow")
@XmlType(name = "Workflow")
public class Workflow extends Container<WorkflowRegistry, Workflow> {

    @Deprecated
    public Workflow() {
    }

    public Workflow(String id) {
        super(id);
    }
}
