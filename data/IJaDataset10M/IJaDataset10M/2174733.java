package org.adapit.wctoolkit.fomda.propertyeditors.editors.features.parameter;

import org.adapit.wctoolkit.infrastructure.DefaultApplicationFrame;
import org.adapit.wctoolkit.infrastructure.propertyeditors.DefaultElementPropertyEditorPane;
import org.adapit.wctoolkit.infrastructure.util.ResourceMessage;
import org.adapit.wctoolkit.infrastructure.util.SpringResourceMessage;
import org.adapit.wctoolkit.propertyeditors.form.element.stereotype.StereotypeEditorPanel;
import org.adapit.wctoolkit.propertyeditors.form.element.taggedvalue.TaggedValueEditorPanel;
import org.adapit.wctoolkit.uml.ext.core.IElement;
import org.adapit.wctoolkit.uml.ext.fomda.metamodel.features.ParameterDescriptor;

@SuppressWarnings({ "serial" })
public class ParameterPropertyEditor extends DefaultElementPropertyEditorPane {

    private ResourceMessage messages = SpringResourceMessage.getInstance();

    protected TaggedValueEditorPanel taggedValueEditorPanel;

    protected StereotypeEditorPanel stereotypeEditorPanel;

    protected ParameterBaseEditorPanel baseEditorPanel;

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
        this.setSize(310, 310);
        setSize(310, 310);
        baseEditorPanel = new ParameterBaseEditorPanel(this);
        add(messages.getMessage("Base"), baseEditorPanel);
        taggedValueEditorPanel = new TaggedValueEditorPanel();
        add(messages.getMessage("TaggedValues"), taggedValueEditorPanel);
        stereotypeEditorPanel = new StereotypeEditorPanel();
        add(messages.getMessage("Stereotypes"), stereotypeEditorPanel);
    }

    public void notifyElementChanged() {
        baseEditorPanel.getTypeField().setText(((ParameterDescriptor) element).getType());
        super.notifyElementChanged();
    }

    public void notifyElementDestroyed() {
    }
}
