package com.ivis.xprocess.ui.editors.dynamic.elements;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Layout;
import com.ivis.xprocess.ui.editors.dynamic.model.EditorContext;
import com.ivis.xprocess.ui.editors.dynamic.model.IContributeToRolledUpTitle;
import com.ivis.xprocess.ui.editors.dynamic.model.IXProcessElement;

/**
 * The XProcessWidget representation of layout.FormLayout
 *
 */
public class FormLayout extends XProcessLayoutContainer {

    private org.eclipse.swt.layout.FormLayout formLayout;

    @Override
    public void initialize(EditorContext editorContext, Composite parent) {
        this.editorContext = editorContext;
        super.initialize(editorContext, parent);
        initializeChildren(editorContext, composite);
    }

    @Override
    protected void initializeChildren(EditorContext editorContext, Composite parent) {
        for (IXProcessElement xProcessWidget : children) {
            if (xProcessWidget instanceof FormData) {
                ((FormData) xProcessWidget).setParentFormLayout(this);
            }
            xProcessWidget.initialize(editorContext, parent);
        }
    }

    @Override
    public Layout getLayout() {
        return formLayout;
    }

    @Override
    public void create(Composite parent) {
        composite = editorContext.getFormToolkit().createComposite(parent, SWT.NONE);
        formLayout = new org.eclipse.swt.layout.FormLayout();
        composite.setLayout(formLayout);
    }

    @Override
    public Control getControl() {
        return composite;
    }

    @Override
    public String getContributionToTitle() {
        String contributionToTitle = "";
        for (IXProcessElement xProcessElement : children) {
            if (xProcessElement instanceof IContributeToRolledUpTitle) {
                IContributeToRolledUpTitle contributeToRolledUpTitle = (IContributeToRolledUpTitle) xProcessElement;
                String text = contributeToRolledUpTitle.getContributionToTitle();
                if (text.length() > 0) {
                    contributionToTitle += text;
                }
            }
        }
        return contributionToTitle;
    }
}
