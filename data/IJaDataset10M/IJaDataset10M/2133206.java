package com.jacum.cms.rcp.ui.editors.item.controls;

import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocumentListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Widget;

/**
 * @author rich
 *
 */
public abstract class BlockableModifyListener implements ModifyListener, IDocumentListener {

    boolean blocked = false;

    private Widget documentWidget;

    /**
	 * @param blocked
	 */
    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }

    public void documentAboutToBeChanged(DocumentEvent event) {
    }

    public void documentChanged(DocumentEvent documentEvent) {
        Event event = new Event();
        event.widget = documentWidget;
        ModifyEvent modifyEvent = new ModifyEvent(event);
        modifyText(modifyEvent);
    }

    /**
	 * @param documentWidget
	 */
    public void setDocumentWidget(Widget documentWidget) {
        this.documentWidget = documentWidget;
    }
}
