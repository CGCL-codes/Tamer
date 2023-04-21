package jp.riken.omicspace.osml.impl;

import jp.riken.omicspace.osml.*;
import java.lang.*;
import java.util.*;

public class ExperimentContainer extends BaseContainer {

    public ExperimentContainer() {
        setClassType(BaseContainer.CLASS_TYPE_EXPERIMENT);
        addAttribute("name", BaseContainer.ATTRIBUTE_TYPE_TEXT, null);
        addAttribute("note", BaseContainer.ATTRIBUTE_TYPE_TEXT, null);
        addAttribute("id", BaseContainer.ATTRIBUTE_TYPE_TEXT, null);
    }

    public void searchContainer(Hashtable hParams, ArrayList vResult, int classType) {
        matches(hParams, vResult, classType);
    }

    public BaseContainer getCloneContainer() {
        ExperimentContainer container = new ExperimentContainer();
        setCloneContainer(container);
        return container;
    }

    public void setCloneContainer(ExperimentContainer container) {
        super.setCloneContainer(container);
    }

    public static BaseContainer createContainer() {
        ExperimentContainer container = new ExperimentContainer();
        container.setNote("new experiment");
        container.setId(container.getNewId());
        return (BaseContainer) container;
    }

    public String getTreeInformation() {
        return getName();
    }
}
