package org.adapit.wctoolkit.propertyeditors.editors.parameter;

import org.adapit.wctoolkit.infrastructure.DefaultApplicationFrame;
import org.adapit.wctoolkit.infrastructure.propertyeditors.DefaultElementPropertyEditorPane;
import org.adapit.wctoolkit.infrastructure.util.ResourceMessage;
import org.adapit.wctoolkit.infrastructure.util.SpringResourceMessage;
import org.adapit.wctoolkit.propertyeditors.form.element.base.BaseEditorPanel;
import org.adapit.wctoolkit.propertyeditors.form.element.parameter.ParameterEditorPanel;
import org.adapit.wctoolkit.propertyeditors.form.element.stereotype.StereotypeEditorPanel;
import org.adapit.wctoolkit.propertyeditors.form.element.taggedvalue.TaggedValueEditorPanel;
import org.adapit.wctoolkit.uml.ext.core.IElement;

public class ParameterPropertyEditor extends DefaultElementPropertyEditorPane {

    private static final long serialVersionUID = 35521562304571L;

    private ResourceMessage messages = SpringResourceMessage.getInstance();

    protected TaggedValueEditorPanel taggedValueEditorPanel;

    protected StereotypeEditorPanel stereotypeEditorPanel;

    protected BaseEditorPanel baseEditorPanel;

    protected ParameterEditorPanel parameterEditorPanel;

    public ParameterPropertyEditor(DefaultApplicationFrame defaultSplitPane, IElement element) {
        super(defaultSplitPane, element);
    }

    public ParameterPropertyEditor(DefaultApplicationFrame defaultSplitPane) {
        super(defaultSplitPane);
    }

    public ParameterPropertyEditor() {
        super();
    }

    public void initialize() {
        baseEditorPanel = new BaseEditorPanel(this);
        add(messages.getMessage("Base"), baseEditorPanel);
        parameterEditorPanel = new ParameterEditorPanel(this);
        add(messages.getMessage("Definitions"), parameterEditorPanel);
        taggedValueEditorPanel = new TaggedValueEditorPanel();
        add(messages.getMessage("TaggedValues"), taggedValueEditorPanel);
        stereotypeEditorPanel = new StereotypeEditorPanel();
        add(messages.getMessage("Stereotypes"), stereotypeEditorPanel);
    }

    public void notifyElementDestroyed() {
    }
}
