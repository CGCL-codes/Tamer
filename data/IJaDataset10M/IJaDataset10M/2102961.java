package kfschmidt.qvii.controlpanel;

import kfschmidt.qvii.QVIIProject;
import kfschmidt.qvii.QVIIProjectListener;
import kfschmidt.qvii.QVII;
import kfschmidt.qvii.Task;
import kfschmidt.data4d.VolumeTimeSeries;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ProjectPanel extends JPanel implements MouseListener {

    ControlPanelWindow mControlPanelWindow;

    QVIIProject mProject;

    JScrollPane mScrollPane;

    JPanel mInternalPanel;

    ProjectPanelItem mCurrentlySelectedItem;

    public void mouseClicked(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
        ProjectPanelItem clickeditem = ((ProjectPanelItem) e.getSource());
        if (mCurrentlySelectedItem == null) {
            clickeditem.setSelected(true);
            mCurrentlySelectedItem = clickeditem;
        } else if (clickeditem.equals(mCurrentlySelectedItem)) {
            clickeditem.setSelected(false);
            mCurrentlySelectedItem = null;
        } else {
            mCurrentlySelectedItem.setSelected(false);
            clickeditem.setSelected(true);
            mCurrentlySelectedItem = clickeditem;
        }
        if (clickeditem.getAssociatedObject() instanceof VolumeTimeSeries) {
            if (mCurrentlySelectedItem == null) mControlPanelWindow.disableVolEdit(); else mControlPanelWindow.enableVolEdit();
        }
    }

    public void setCurrentlySelectedItem(Object the_associated_object) {
        Component[] panel_items = mInternalPanel.getComponents();
        if (panel_items == null || panel_items.length == 0) return;
        for (int a = 0; a < panel_items.length; a++) {
            if (((ProjectPanelItem) panel_items[a]).getAssociatedObject().equals(the_associated_object)) {
                if (mCurrentlySelectedItem != null) mCurrentlySelectedItem.setSelected(false);
                ((ProjectPanelItem) panel_items[a]).setSelected(true);
                mCurrentlySelectedItem = (ProjectPanelItem) panel_items[a];
            }
        }
    }

    public Object getCurrentlySelectedItem() {
        if (mCurrentlySelectedItem == null) return null; else return mCurrentlySelectedItem.getAssociatedObject();
    }

    public ProjectPanel(ControlPanelWindow parent) {
        mControlPanelWindow = parent;
        mInternalPanel = new JPanel();
        mInternalPanel.add(new JLabel(" "));
        mScrollPane = new JScrollPane(mInternalPanel);
        setLayout(new GridLayout(1, 1));
        add(mScrollPane);
    }

    public void setProject(QVIIProject project) {
        mProject = project;
        updateProject();
    }

    public void cleanup() {
        System.out.println("ProjectPanel.cleanup()");
        Component[] items = null;
        if (mInternalPanel != null) items = mInternalPanel.getComponents();
        if (items != null && items.length > 0) {
            for (int a = 0; a < items.length; a++) {
                if (items[a] instanceof ProjectPanelItem) {
                    mControlPanelWindow.getQVII().removeProjectListener((QVIIProjectListener) items[a]);
                    ((ProjectPanelItem) items[a]).removeMouseListener(this);
                    ((ProjectPanelItem) items[a]).finalize();
                    items[a] = null;
                }
            }
        }
        mInternalPanel = null;
    }

    public void updateProject() {
        Component[] items = mInternalPanel.getComponents();
        mInternalPanel.removeAll();
        ProjectPanelItem recentlyselected = mCurrentlySelectedItem;
        mCurrentlySelectedItem = null;
        int size = 0;
        if (mProject != null && mProject.getItems() != null) {
            size += mProject.getItems().size();
        }
        Task[] tasks = mControlPanelWindow.getQVII().getTasks();
        if (tasks != null) {
            size += tasks.length;
        }
        if (size < 6) size = 6;
        mInternalPanel.setLayout(new GridLayout(size, 1));
        setSize(new Dimension(size * 27, 200));
        if (mProject != null && mProject.getItems() != null) {
            for (int a = 0; a < mProject.getItems().size(); a++) {
                ProjectPanelItem panitem = new ProjectPanelItem(mProject.getItems().elementAt(a));
                mInternalPanel.add(panitem);
                mControlPanelWindow.getQVII().addProjectListener(panitem);
                panitem.addMouseListener(this);
                if (recentlyselected != null && panitem.getAssociatedObject().equals(recentlyselected.getAssociatedObject())) {
                    mCurrentlySelectedItem = panitem;
                    panitem.setSelected(true);
                }
            }
        }
        if (tasks != null) {
            for (int a = 0; a < tasks.length; a++) {
                ProjectPanelItem panitem = new ProjectPanelItem(tasks[a]);
                mInternalPanel.add(panitem);
                mControlPanelWindow.getQVII().addProjectListener(panitem);
                panitem.addMouseListener(this);
                if (recentlyselected != null && panitem.getAssociatedObject().equals(recentlyselected.getAssociatedObject())) {
                    mCurrentlySelectedItem = panitem;
                    panitem.setSelected(true);
                }
            }
        }
        if (items != null && items.length > 0) {
            for (int a = 0; a < items.length; a++) {
                if (items[a] instanceof ProjectPanelItem) {
                    mControlPanelWindow.getQVII().removeProjectListener((QVIIProjectListener) items[a]);
                    ((ProjectPanelItem) items[a]).removeMouseListener(this);
                    ((ProjectPanelItem) items[a]).finalize();
                    items[a] = null;
                }
            }
        }
        revalidate();
    }
}
