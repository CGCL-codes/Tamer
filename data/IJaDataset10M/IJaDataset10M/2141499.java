package com.zeat.doubleleg.view;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

/**
 * Provides labels for items in the wrestler tree.
 * 
 * @author Adam Taylor
 * @version 1.0
 * Copyright 2007 Adam Taylor
 * This program is distributed under the GNU Lesser General Public License.
 */
public class NameLabelProvider extends LabelProvider {

    public NameLabelProvider() {
    }

    @Override
    public Image getImage(Object element) {
        return null;
    }

    @Override
    public String getText(Object element) {
        return element.toString();
    }
}
