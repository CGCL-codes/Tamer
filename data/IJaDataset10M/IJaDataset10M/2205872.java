package com.l2fprod.common.application.core;

import com.l2fprod.common.application.selection.Selection;
import com.l2fprod.common.application.selection.SelectionListener;
import java.awt.Component;

/**
 * SelectionManager.<br>
 *
 */
public interface SelectionManager {

    Component getFocusOwner();

    Selection getSelection();

    void addSelectionListener(SelectionListener listener);

    void removeSelectionListener(SelectionListener listener);
}
