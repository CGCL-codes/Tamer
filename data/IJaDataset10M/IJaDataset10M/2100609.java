package net.sf.amemailchecker.gui.component.chooser;

import javax.swing.*;
import javax.swing.event.EventListenerList;
import java.awt.event.ActionListener;

public class AlignmentChooser extends JComponent {

    private static final String uiClassID = "AlignmentChooserUI";

    private AlignmentChooserModel model;

    private EventListenerList actionListenerList;

    private boolean fillWholeCell;

    public AlignmentChooser() {
        model = new DefaultAlignmentChooserModel();
        actionListenerList = new EventListenerList();
        updateUI();
    }

    public void setModel(AlignmentChooserModel model) {
        AlignmentChooserModel old = this.model;
        this.model = model;
        firePropertyChange("model", old, model);
    }

    public AlignmentChooserModel getModel() {
        return model;
    }

    public synchronized void addActionListener(ActionListener listener) {
        actionListenerList.add(ActionListener.class, listener);
    }

    public synchronized void removeActionListener(ActionListener listener) {
        actionListenerList.remove(ActionListener.class, listener);
    }

    public ActionListener[] getActionListeners() {
        return actionListenerList.getListeners(ActionListener.class);
    }

    public void updateUI() {
        if (UIManager.get(getUIClassID()) != null) {
            setUI((AlignmentChooserUI) UIManager.getUI(this));
        } else {
            setUI(new BasicAlignmentChooserUI());
        }
    }

    public void setUI(AlignmentChooserUI ui) {
        super.setUI(ui);
    }

    public AlignmentChooserUI getUI() {
        return (AlignmentChooserUI) ui;
    }

    public String getUIClassID() {
        return uiClassID;
    }

    public boolean isFillWholeCell() {
        return fillWholeCell;
    }

    public void setFillWholeCell(boolean fillWholeCell) {
        this.fillWholeCell = fillWholeCell;
    }
}
