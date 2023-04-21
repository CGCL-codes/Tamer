package jpatch.boundary.sidebar;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;
import jpatch.entity.*;
import jpatch.boundary.*;
import jpatch.boundary.action.*;
import jpatch.boundary.ui.*;

public class LightPanel extends SidePanel implements ChangeListener {

    /**
	 * 
	 */
    private static final long serialVersionUID = 6771633567920818871L;

    JPatchInput inputName;

    AnimLight light;

    public LightPanel(AnimLight light) {
        this.light = light;
        add(new JButton(new EditAnimObjectAction(light)));
        add(new JButton(new DeleteAnimObjectAction(light)));
        JPatchInput.setDimensions(50, 150, 20);
        inputName = new JPatchInput("Name:", light.getName());
        JPanel detailPanel = MainFrame.getInstance().getSideBar().getDetailPanel();
        detailPanel.removeAll();
        detailPanel.add(inputName);
        inputName.addChangeListener(this);
        detailPanel.repaint();
    }

    public void stateChanged(ChangeEvent changeEvent) {
        light.setName(inputName.getStringValue());
        ((DefaultTreeModel) MainFrame.getInstance().getTree().getModel()).nodeChanged(light);
        MainFrame.getInstance().requestFocus();
    }
}
