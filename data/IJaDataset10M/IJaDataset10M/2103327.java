package com.ivis.xprocess.core.impl;

import java.util.List;
import java.util.Set;
import com.ivis.xprocess.core.ActionCall;
import com.ivis.xprocess.core.ExternalEventType;
import com.ivis.xprocess.core.Parameter;
import com.ivis.xprocess.core.ParameterizedAction;
import com.ivis.xprocess.core.WorkflowSource;
import com.ivis.xprocess.framework.Xelement;
import com.ivis.xprocess.framework.impl.XelementImpl;
import com.ivis.xprocess.framework.properties.PropertyDefinition;
import com.ivis.xprocess.framework.schema.SchemaRepository;
import com.ivis.xprocess.framework.xml.IPersistenceHelper;
import com.ivis.xprocess.properties.impl.RecordListProperty;

public class ExternalEventTypeImpl extends XelementImpl implements ExternalEventType {

    public ExternalEventTypeImpl(IPersistenceHelper ph) {
        super(ph);
    }

    public ExternalEventTypeImpl(String name, IPersistenceHelper persistenceHelper) {
        this(persistenceHelper);
        this.setName(name);
    }

    public String getLabel() {
        if (this.isGhost()) {
            if (this.getPersistenceHelper().getDataSource().getVcsProvider() != null) {
                return "[this " + this.getClass().getSimpleName() + " is not accessible or has been deleted]";
            } else {
                return "[this " + this.getClass().getSimpleName() + " has been deleted]";
            }
        }
        return getName();
    }

    public String getName() {
        return getStringProperty(NAME);
    }

    public void setName(String name) {
        setStringProperty(NAME, name);
    }

    public void setDescription(String description) {
        setStringProperty(DESCRIPTION, description);
    }

    public String getDescription() {
        return getStringProperty(DESCRIPTION);
    }

    public void setApplicableTo(Class<? extends WorkflowSource> clazz) {
        setClassProperty(APPLICABLE_TO, clazz);
    }

    @SuppressWarnings("unchecked")
    public Class<? extends WorkflowSource> getApplicableTo() {
        return (Class<? extends WorkflowSource>) getClass(APPLICABLE_TO);
    }

    public boolean isApplicableTo(Class<? extends Xelement> source) {
        if (getApplicableTo() == null) {
            return true;
        }
        return getApplicableTo().isAssignableFrom(source);
    }

    @SuppressWarnings("unchecked")
    public ActionCall addActionCall(ParameterizedAction action, Parameter... parameters) {
        ActionCall actionCall = ph.createRecord(ActionCall.class, this.getContainedIn());
        actionCall.setAction(action);
        actionCall.setParameters(parameters);
        RecordListProperty<ActionCall> property = (RecordListProperty<ActionCall>) getProperty(ACTIONCALLS);
        if (property == null) {
            setActionCalls(actionCall);
        } else {
            property.add(actionCall);
        }
        return actionCall;
    }

    public void removeActionCall(ActionCall action) {
        this.removeRecordFromList(ACTIONCALLS, action);
    }

    public void setActionCalls(ActionCall... calls) {
        RecordListProperty<ActionCall> property = new RecordListProperty<ActionCall>(ACTIONCALLS);
        for (ActionCall call : calls) {
            property.add(call);
        }
        assignProperty(property);
    }

    public List<ActionCall> getActionCalls() {
        return getRecordList(ACTIONCALLS, ActionCall.class);
    }

    @Override
    public Set<PropertyDefinition> getDefaultPropertyDefinitions() {
        return SchemaRepository.getInstance().getProperties(ExternalEventType.class);
    }
}
