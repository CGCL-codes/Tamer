package com.google.code.linkedinapi.schema.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import com.google.code.linkedinapi.schema.Action;
import com.google.code.linkedinapi.schema.Adapter1;
import com.google.code.linkedinapi.schema.AvailableActions;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "actionList" })
@XmlRootElement(name = "available-actions")
public class AvailableActionsImpl implements Serializable, AvailableActions {

    private static final long serialVersionUID = 2461660169443089969L;

    @XmlElement(name = "action", required = true, type = ActionImpl.class)
    protected List<Action> actionList;

    @XmlAttribute(required = true)
    @XmlJavaTypeAdapter(Adapter1.class)
    protected Long total;

    public List<Action> getActionList() {
        if (actionList == null) {
            actionList = new ArrayList<Action>();
        }
        return this.actionList;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long value) {
        this.total = value;
    }
}
