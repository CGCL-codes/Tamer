package com.loribel.commons.swing.panel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import com.loribel.commons.swing.GB_ButtonAction;
import com.loribel.commons.swing.GB_ButtonFactory;
import com.loribel.commons.swing.GB_Panel;
import com.loribel.commons.swing.tools.GB_ButtonTools;
import com.loribel.commons.util.GB_IconTools;

/**
 * Decorate a list with buttons.
 * <p>
 * Buttons of this panel :
 * <ul>
 *   <li>up first</li>
 *   <li>up</li>
 *   <li>down</li>
 *   <li>down last</li>
 * </ul>
 * Methods to use :
 * <ul>
 *   <li>setItems()</li>
 *   <li>getItems()</li>
 * </ul>
 * Example :
 * <pre>
 *    List l_items = ...;
 *    GB_StringsOrderVM l_vm = new GB_StringsOrderVM(l_items);
 *    GB_ViewManagerTools.showOKCancelDialog(frame, l_vm, "Sort items", new
 * Dimension(600, 400), true);
 *    if (r == JOptionPane.OK_OPTION) {
 *        ...
 *    }
 * </pre>
 * This manager directly changes the list of items. If you want preserve
 * old order see the example below :
 * <pre>
 *    List l_items2 = new ArrayList(l_items);
 *    GB_StringsOrderVM l_vm = new GB_StringsOrderVM(l_items);
 *    int r = GB_ViewManagerTools.showOKCancelDialog(frame, l_vm, "Sort items", new
 * Dimension(600, 400), true);
 *    if (r == JOptionPane.OK_OPTION) {
 *        l_items.clear();
 *        l_items.addAll(l_items2);
 *    }
 * </pre>
 * <p>
 *
 * @author Gregory Borelli
 */
public class GB_ListOrderDecorator extends GB_Panel implements ListSelectionListener {

    protected JComponent listComponent;

    protected JList list;

    protected JTable table;

    protected ListSelectionModel listSelectionModel;

    protected GB_ListModel listModel;

    private JButton buttonUpFirst;

    private JButton buttonUp;

    private JButton buttonDown;

    private JButton buttonDownLast;

    protected GB_ListOrderDecorator(JList a_list) {
        this(a_list, (GB_ListModel) a_list.getModel());
    }

    public GB_ListOrderDecorator(JList a_list, GB_ListModel a_model) {
        super();
        listComponent = a_list;
        list = a_list;
        listModel = a_model;
        if (a_list.getModel() != listModel) {
            throw new RuntimeException("Invalid model for the List: " + listComponent);
        }
        listSelectionModel = a_list.getSelectionModel();
        buildPanel();
    }

    public GB_ListOrderDecorator(JTable a_table, GB_ListModel a_model) {
        super();
        listComponent = a_table;
        table = a_table;
        listModel = a_model;
        if (a_table.getModel() != listModel) {
            throw new RuntimeException("Invalid model for the List: " + listComponent);
        }
        listSelectionModel = a_table.getSelectionModel();
        buildPanel();
    }

    protected void buildPanel() {
        listSelectionModel.addListSelectionListener(this);
        this.setLayout(new BorderLayout());
        this.add(new JScrollPane(listComponent), BorderLayout.CENTER);
        this.add(buildButtonsBar(), BorderLayout.EAST);
        this.setPreferredSize(new Dimension(300, 200));
    }

    /**
     * Enables / disables button according to selection.
     *
     * @param e ListSelectionEvent -
     */
    public void valueChanged(ListSelectionEvent e) {
        updateEnabled();
    }

    protected void updateEnabled() {
        if (listComponent == null) {
            return;
        }
        int l_index = getSelectedRowIndex();
        int l_maxIndex = listModel.getSize() - 1;
        if (l_maxIndex == -1 || l_index == -1) {
            buttonUpFirst.setEnabled(false);
            buttonUp.setEnabled(false);
            buttonDown.setEnabled(false);
            buttonDownLast.setEnabled(false);
        } else if (l_index == 0) {
            buttonUpFirst.setEnabled(false);
            buttonUp.setEnabled(false);
        } else {
            buttonUpFirst.setEnabled(true);
            buttonUp.setEnabled(true);
            if (l_index == l_maxIndex) {
                buttonDown.setEnabled(false);
                buttonDownLast.setEnabled(false);
            } else {
                buttonDown.setEnabled(true);
                buttonDownLast.setEnabled(true);
            }
        }
    }

    /**
     * Build the pannels with order buttons.
     *
     * @return JComponent
     */
    protected JComponent buildButtonsBar() {
        Collection l_buttons = buildButtons();
        JComponent retour = GB_ButtonFactory.newToolBarVertical(l_buttons);
        return retour;
    }

    /**
     * Build the pannels with order buttons.
     *
     * @return JComponent
     */
    protected List buildButtons() {
        List retour = new ArrayList();
        buttonUpFirst = new MyButtonUpFirst();
        buttonUp = new MyButtonUp();
        buttonDown = new MyButtonDown();
        buttonDownLast = new MyButtonDownLast();
        retour.add(buttonUpFirst);
        retour.add(buttonUp);
        retour.add(buttonDown);
        retour.add(buttonDownLast);
        return retour;
    }

    public JComponent getList() {
        return listComponent;
    }

    protected int getSelectedRowIndex() {
        return listSelectionModel.getMinSelectionIndex();
    }

    protected void setSelectedRow(int a_index) {
        if (list != null) {
            list.setSelectedIndex(a_index);
            list.ensureIndexIsVisible(a_index);
        } else {
            table.setRowSelectionInterval(a_index, a_index);
        }
    }

    /**
     * Button.
     */
    private class MyButtonDown extends GB_ButtonAction {

        public MyButtonDown() {
            super();
            this.setIcon(GB_IconTools.get(AA.ICON.X16_ARROW_DOWN));
            this.setEnabled(false);
            GB_ButtonTools.decoreTools(this);
        }

        public void doAction() {
            int l_index = getSelectedRowIndex();
            int l_maxIndex = listModel.getSize() - 1;
            if (l_index == l_maxIndex) {
                return;
            }
            Object l_value = listModel.remove(l_index);
            listModel.add(l_index + 1, l_value);
            listModel.fireContentsChanged(l_index, l_index + 1);
            setSelectedRow(l_index + 1);
        }
    }

    /**
     * Button.
     */
    private class MyButtonDownLast extends GB_ButtonAction {

        public MyButtonDownLast() {
            super();
            this.setIcon(GB_IconTools.get(AA.ICON.X16_ARROW_DOWN_DOUBLE));
            this.setEnabled(false);
            GB_ButtonTools.decoreTools(this);
        }

        public void doAction() {
            int l_index = getSelectedRowIndex();
            int l_maxIndex = listModel.getSize() - 1;
            if (l_index == l_maxIndex) {
                return;
            }
            Object l_value = listModel.remove(l_index);
            listModel.add(l_value);
            listModel.fireContentsChanged(l_index, l_maxIndex + 1);
            setSelectedRow(l_maxIndex);
        }
    }

    /**
     * Button.
     */
    private class MyButtonUp extends GB_ButtonAction {

        public MyButtonUp() {
            super();
            this.setIcon(GB_IconTools.get(AA.ICON.X16_ARROW_UP));
            this.setEnabled(false);
            GB_ButtonTools.decoreTools(this);
        }

        public void doAction() {
            int l_index = getSelectedRowIndex();
            if (l_index == 0) {
                return;
            }
            Object l_value = listModel.remove(l_index);
            listModel.add(l_index - 1, l_value);
            listModel.fireContentsChanged(l_index - 1, l_index);
            setSelectedRow(l_index - 1);
        }
    }

    /**
     * Button.
     */
    private class MyButtonUpFirst extends GB_ButtonAction {

        public MyButtonUpFirst() {
            super();
            this.setIcon(GB_IconTools.get(AA.ICON.X16_ARROW_UP_DOUBLE));
            this.setEnabled(false);
            GB_ButtonTools.decoreTools(this);
        }

        public void doAction() {
            int l_index = getSelectedRowIndex();
            if (l_index == 0) {
                return;
            }
            Object l_value = listModel.remove(l_index);
            listModel.add(0, l_value);
            listModel.fireContentsChanged(0, l_index);
            setSelectedRow(0);
        }
    }
}
