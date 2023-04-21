package com.widen.prima.editor.finance;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import com.widen.prima.Application;
import com.widen.prima.IImageKeys;
import com.widen.prima.finance.entites.BookOfAccountBo;
import com.widen.prima.util.Util;

public class BookOfAccountInputLabelProvider implements ITableLabelProvider {

    /**
     * Returns the image
     * 
     * @param element the element
     * @param columnIndex the column index
     * @return Image
     */
    public Image getColumnImage(Object element, int columnIndex) {
        if (columnIndex == 0) {
            return AbstractUIPlugin.imageDescriptorFromPlugin(Application.PLUGIN_ID, IImageKeys.SUBJECT).createImage();
        }
        return null;
    }

    /**
     * Returns the column text
     * 
     * @param element the element
     * @param columnIndex the column index
     * @return String
     */
    public String getColumnText(Object element, int columnIndex) {
        BookOfAccountBo boaBo = (BookOfAccountBo) element;
        switch(columnIndex) {
            case 0:
                if (boaBo.getAccountSubject() == null) {
                    return "";
                }
                return "[" + Util.getSubjectType(boaBo.getAccountSubject().getSubjectType().getValue()) + "]" + boaBo.getAccountSubject().getFullName();
            case 1:
                if (boaBo.getDirection() == null) {
                    return "";
                }
                int direction = boaBo.getDirection().getValue();
                return Util.getDirection(direction);
            case 2:
                return boaBo.getMoney().toString();
        }
        return null;
    }

    /**
     * Adds a listener
     * 
     * @param listener the listener
     */
    public void addListener(ILabelProviderListener listener) {
    }

    /**
     * Disposes any created resources
     */
    public void dispose() {
    }

    /**
     * Returns whether altering this property on this element will affect the
     * label
     * 
     * @param element the element
     * @param property the property
     * @return boolean
     */
    public boolean isLabelProperty(Object element, String property) {
        return false;
    }

    /**
     * Removes a listener
     * 
     * @param listener the listener
     */
    public void removeListener(ILabelProviderListener listener) {
    }
}
