package org.jowidgets.examples.common.workbench.widgets.views;

import org.jowidgets.api.widgets.IContainer;
import org.jowidgets.api.widgets.blueprint.ILabelBluePrint;
import org.jowidgets.api.widgets.blueprint.factory.IBluePrintFactory;
import org.jowidgets.common.widgets.layout.MigLayoutDescriptor;
import org.jowidgets.workbench.api.IView;
import org.jowidgets.workbench.api.IViewContext;

public class LabelWithTextView extends AbstractHowToView implements IView {

    public static final String ID = LabelWithTextView.class.getName();

    public static final String DEFAULT_LABEL = "Label with text";

    public LabelWithTextView(final IViewContext context) {
        super(context);
    }

    @Override
    public void createViewContent(final IContainer container, final IBluePrintFactory bpFactory) {
        container.setLayout(new MigLayoutDescriptor("[]", "[]"));
        final ILabelBluePrint labelBp = bpFactory.label().setText("The label text");
        container.add(labelBp, "");
    }
}
