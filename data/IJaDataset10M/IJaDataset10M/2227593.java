package com.tensegrity.webetlclient.modules.core.client.model;

import com.google.gwt.xml.client.Element;

public class JobRefNode extends RefNode {

    private static final String REF_ATTRIBUTE = "nameref";

    public JobRefNode(Element element, JobsNode jobsNode) {
        this();
        setNameRef(element.getAttribute(REF_ATTRIBUTE));
    }

    public JobRefNode() {
    }

    public JobRefNode(JobNode jobNode) {
        setNameRef(jobNode.getName());
    }

    public int getTypeID() {
        return JOB_REF_TYPE;
    }
}
