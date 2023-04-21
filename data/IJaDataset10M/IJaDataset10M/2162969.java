package org.mitre.rt.client.ui.common.interfaces;

import javax.swing.*;

/**
 *
 * @author BWORRELL
 */
public interface ITablePanel {

    public JTable getTable();

    public JScrollPane getScrollPane();

    public void doEditItem();

    public void doAddItem();

    public void doDeleteItem();

    public void doViewItem();
}
