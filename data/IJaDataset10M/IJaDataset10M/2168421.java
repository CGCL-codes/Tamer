package com.amazonaws.eclipse.datatools.sqltools.tablewizard.simpledb.ui.popup.actions;

import org.eclipse.datatools.connectivity.sqm.core.internal.ui.icons.ImageDescription;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.ui.navigator.CommonViewer;
import com.amazonaws.eclipse.datatools.sqltools.tablewizard.simpledb.ui.Messages;

public class ForwardEngineerAction extends Action {

    private static final String TEXT = Messages.GENERATE_DDL_MENU_TEXT;

    private static final ImageDescriptor descriptor = ImageDescription.getGenerateCodeDescriptor();

    protected SelectionChangedEvent event;

    protected CommonViewer viewer;

    public ForwardEngineerAction() {
        this.setImageDescriptor(descriptor);
        this.setDisabledImageDescriptor(descriptor);
        this.setText(TEXT);
        this.setToolTipText(TEXT);
    }

    public void setCommonViewer(final CommonViewer viewer) {
        this.viewer = viewer;
    }

    public void selectionChanged(final SelectionChangedEvent event) {
        this.event = event;
        setEnabled(false);
    }

    @Override
    public void run() {
    }
}
