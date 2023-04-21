package org.adapit.wctoolkit.fomda.propertyeditors.editors.features.expression;

import org.adapit.wctoolkit.fomda.propertyeditors.form.expression.ExpressionBlockListPanel;
import org.adapit.wctoolkit.fomda.propertyeditors.form.expression.ExpressionConjunctionListPanel;
import org.adapit.wctoolkit.fomda.propertyeditors.form.expression.ExpressionEditorPanel;
import org.adapit.wctoolkit.fomda.propertyeditors.form.expression.ExpressionElementListPanel;
import org.adapit.wctoolkit.infrastructure.DefaultApplicationFrame;
import org.adapit.wctoolkit.infrastructure.propertyeditors.DefaultElementPropertyEditorPane;
import org.adapit.wctoolkit.infrastructure.util.ResourceMessage;
import org.adapit.wctoolkit.infrastructure.util.SpringResourceMessage;
import org.adapit.wctoolkit.propertyeditors.form.element.stereotype.StereotypeEditorPanel;
import org.adapit.wctoolkit.propertyeditors.form.element.taggedvalue.TaggedValueEditorPanel;
import org.adapit.wctoolkit.uml.ext.core.IElement;

@SuppressWarnings({ "serial" })
public class ExpressionPropertyEditor extends DefaultElementPropertyEditorPane {

    public org.adapit.wctoolkit.fomda.features.i18n.I18N_FomdaProfile messages2 = org.adapit.wctoolkit.fomda.features.i18n.I18N_FomdaProfile.getInstance();

    private ResourceMessage messages = SpringResourceMessage.getInstance();

    protected TaggedValueEditorPanel taggedValueEditorPanel;

    protected StereotypeEditorPanel stereotypeEditorPanel;

    protected ExpressionEditorPanel baseEditorPanel;

    protected ExpressionConjunctionListPanel expressionConjunctionListPanel;

    protected ExpressionBlockListPanel expressionBlockListPanel;

    protected ExpressionElementListPanel expressionElementListPanel;

    public ExpressionPropertyEditor(DefaultApplicationFrame defaultSplitPane, IElement element) {
        super(defaultSplitPane, element);
    }

    public ExpressionPropertyEditor(DefaultApplicationFrame defaultSplitPane) {
        super(defaultSplitPane);
    }

    public ExpressionPropertyEditor() {
        super();
    }

    public void initialize() {
        this.setSize(310, 310);
        setSize(310, 310);
        baseEditorPanel = new ExpressionEditorPanel();
        add(messages.getMessage("Base"), baseEditorPanel);
        expressionConjunctionListPanel = new ExpressionConjunctionListPanel();
        add(messages2.getMessage("Expression_Conjunctions"), expressionConjunctionListPanel);
        expressionBlockListPanel = new ExpressionBlockListPanel();
        add(messages2.getMessage("Expression_Blocks"), expressionBlockListPanel);
        expressionElementListPanel = new ExpressionElementListPanel();
        add(messages2.getMessage("Expression_Elements"), expressionElementListPanel);
        taggedValueEditorPanel = new TaggedValueEditorPanel();
        add(messages.getMessage("TaggedValues"), taggedValueEditorPanel);
        stereotypeEditorPanel = new StereotypeEditorPanel();
        add(messages.getMessage("Stereotypes"), stereotypeEditorPanel);
    }

    public void notifyElementDestroyed() {
    }
}
