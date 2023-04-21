package org.powerfolder.workflow.model;

import java.io.Serializable;
import org.powerfolder.workflow.model.attributes.AttributeSet;
import org.powerfolder.workflow.model.history.History;
import org.powerfolder.workflow.model.script.RootScriptTagHolder;
import org.powerfolder.workflow.model.trigger.Trigger;
import org.powerfolder.workflow.model.trigger.TriggerContext;

public interface WorkflowPrecursor extends Serializable {

    public void setException(Exception inException);

    public Exception getException();

    public boolean isExceptionPresent();

    public void setTriggerContext(TriggerContext inTc);

    public TriggerContext getTriggerContext();

    public void setAttributeSet(AttributeSet inAs);

    public AttributeSet getAttributeSet();

    public void setHistory(History inHistory);

    public History getHistory();

    public void setRootScriptTagHolder(RootScriptTagHolder inRth);

    public RootScriptTagHolder getRootScriptTagHolder();

    public void setTrigger(Trigger inTrigger);

    public Trigger getTrigger();
}
