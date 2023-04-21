package com.smtphoneypot.ui;

import com.smtphoneypot.core.IncomingMessageTable;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

public class FilterTableContentProvider implements IStructuredContentProvider {

    public Object[] getElements(Object arg0) {
        IncomingMessageTable inMessage = IncomingMessageTable.getInstance();
        try {
            inMessage.getAllData();
        } catch (Exception e) {
            return new Object[] {};
        }
        return inMessage.getMessages().toArray();
    }

    public void dispose() {
    }

    public void inputChanged(Viewer arg0, Object arg1, Object arg2) {
    }
}
