package edu.yale.csgp.vitapad.gui.components;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JToggleButton;
import javax.swing.JViewport;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import edu.berkeley.guir.prefuse.graph.Entity;
import edu.yale.csgp.vitapad.Buffer;
import edu.yale.csgp.vitapad.ControlCenter;
import edu.yale.csgp.vitapad.GUIUtils;
import edu.yale.csgp.vitapad.TypeManager;
import edu.yale.csgp.vitapad.message.DialogUpdate;

public class TypeViewer extends JTabbedPane implements ActionListener, ListSelectionListener {

    private JViewport typeImageView;

    private ButtonGroup typeGroup;

    private VPPanel typeImagePanel;

    private JList typeNameList;

    private JScrollPane typeImagePanelScroller;

    private JScrollPane typeNameListScroller;

    private Buffer buffer;

    private TypeButton[] typeButtons;

    public TypeViewer(Buffer buffer, int typeKind) {
        this.buffer = buffer;
        typeImagePanel = new VPPanel();
        typeGroup = new ButtonGroup();
        Map currentType = ((Entity) buffer.getBufferMod().firstValue()).getAttributes();
        List elements = TypeManager.getDrawableTypes(typeKind);
        typeButtons = new TypeButton[elements.size()];
        TypeButton newButton = null;
        for (int i = 0; i < typeButtons.length; i++) {
            Entity temp = (Entity) elements.get(i);
            typeButtons[i] = new TypeButton(temp);
            typeButtons[i].addActionListener(this);
            typeGroup.add(typeButtons[i]);
        }
        if (!TypeManager.containsType(currentType)) {
            currentType.put("name", "Unsaved Type");
            int len = typeButtons.length;
            TypeButton[] newTypeButtons = new TypeButton[len + 1];
            System.arraycopy(typeButtons, 0, newTypeButtons, 0, len);
            typeButtons = newTypeButtons;
            newButton = new TypeButton(TypeManager.typeToElement(currentType));
            newButton.addActionListener(this);
            typeGroup.add(newButton);
            typeButtons[len] = newButton;
        }
        int rows;
        int size = typeGroup.getButtonCount();
        if (size > 4) {
            rows = size / 2;
            rows = (size % 2 == 0) ? rows : (rows + 1);
        } else rows = 2;
        typeImagePanel.setBorder(new EmptyBorder(3, 4, 5, 0));
        typeImagePanel.setLayout(new GridLayout(rows, 2, 0, 2));
        for (int i = 0; i < typeButtons.length; i++) {
            typeImagePanel.add(typeButtons[i]);
        }
        if (newButton != null) typeImagePanel.add(newButton);
        for (int i = 0; i < (rows * 2) - size; i++) {
            typeImagePanel.add(new JLabel());
        }
        Vector types = new Vector();
        for (Enumeration e = typeGroup.getElements(); e.hasMoreElements(); ) {
            TypeButton tb = (TypeButton) e.nextElement();
            types.add(tb.getType());
        }
        typeNameList = new JList(types);
        typeNameList.addListSelectionListener(this);
        typeImagePanelScroller = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        typeImageView = new JViewport();
        typeImageView.setPreferredSize(new Dimension(128, 110));
        typeImageView.setView(typeImagePanel);
        typeImagePanelScroller.setViewport(typeImageView);
        typeNameListScroller = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        JViewport view2 = new JViewport();
        view2.setPreferredSize(typeImageView.getPreferredSize());
        view2.setView(typeNameList);
        typeNameListScroller.setViewport(view2);
        addTab("Image", typeImagePanelScroller);
        addTab("Name", typeNameListScroller);
    }

    public void propertiesChanged() {
        Map currentType = ((Entity) buffer.getBufferMod().firstValue()).getAttributes();
        typeNameList.setSelectedValue(currentType, true);
        for (int i = 0; i < typeButtons.length; i++) {
            if (currentType.equals(typeButtons[i].getType())) {
                typeGroup.setSelected((ButtonModel) typeButtons[i].getModel(), true);
                if (!typeImageView.getViewRect().contains(typeButtons[i].getLocation())) typeImageView.setViewPosition(new Point(0, typeButtons[i].getLocation().y));
            }
        }
    }

    public void actionPerformed(ActionEvent e) {
        ButtonModel tbm = typeGroup.getSelection();
        TypeButton currentButton = null;
        for (int i = 0; i < typeButtons.length; i++) {
            if (typeButtons[i].getModel() == tbm) currentButton = typeButtons[i];
        }
        Map m = currentButton.getType();
        buffer.getBufferMod().updateAll(m, true);
        ControlCenter.send(new DialogUpdate(buffer, null, DialogUpdate.TYPE_CHANGED));
    }

    public void valueChanged(ListSelectionEvent e) {
        Map currentType = (Map) typeNameList.getSelectedValue();
        buffer.getBufferMod().updateAll(currentType, true);
        ControlCenter.send(new DialogUpdate(buffer, null, DialogUpdate.TYPE_CHANGED));
    }
}
