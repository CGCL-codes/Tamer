package com.codemonster.nato.component;

import com.codemonster.nato.NATOMain;
import com.codemonster.nato.ResourceLauncherMouseListener;
import com.codemonster.nato.listener.EscapeToTreeKeyAdapter;
import com.codemonster.nato.model.TaskNode;
import com.codemonster.nato.model.TaskRootNode;
import com.codemonster.nato.model.TaskState;
import com.codemonster.nato.model.TaskStatus;
import com.codemonster.nato.util.AppResources;
import com.codemonster.nato.util.KeyCodes;
import com.codemonster.nato.util.TreeUtil;
import org.sourceforge.jcalendarbutton.JCalendarButton;
import javax.swing.*;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;
import java.awt.*;
import java.awt.event.*;
import java.util.Date;

/**
 *
 */
public class DetailPanelTop extends JPanel {

    private JLabel createdField = new JLabel();

    private JLabel dueField = new JLabel();

    private JTextField nameField = new JTextField();

    private JComboBox percentComboBox = new JComboBox();

    private JComboBox statusComboBox = new JComboBox();

    private JTextArea descriptionField = new JTextArea();

    private JLabel dueLabel = new JLabel("Due:");

    private UndoManager undoMgr = new UndoManager();

    private Document taskDoc = descriptionField.getDocument();

    private Color subduedText = new Color(102, 102, 102);

    private final JCalendarButton calendarButton = new org.sourceforge.jcalendarbutton.JCalendarButton();

    public DetailPanelTop() {
        setup();
    }

    private void setup() {
        SpringLayout layout = new SpringLayout();
        this.setLayout(layout);
        int TOP_INSET = 10;
        int LABEL_WIDTH = 90;
        int LABEL_HEIGHT = 20;
        int DATE_FIELD_WIDTH = 100;
        int FIELD_WIDTH = 450;
        int FIELD_HEIGHT = 20;
        int FIELD_PADDING = 5;
        int FIELD_MARGIN = LABEL_WIDTH + 10;
        createdField.setForeground(subduedText);
        dueField.setForeground(subduedText);
        dueLabel.setVisible(false);
        dueLabel.setIcon(AppResources.normalChip);
        TaskStatus[] statuses = TaskStatus.values();
        for (int i = 0; i < statuses.length; i++) {
            TaskStatus status = statuses[i];
            statusComboBox.addItem(status);
        }
        for (int i = 0; i < AppResources.percent.length; i++) {
            percentComboBox.addItem(AppResources.percent[i]);
        }
        JLabel createdLabel = new JLabel("Created:");
        JLabel nameLabel = new JLabel("Name:");
        JLabel descriptionLabel = new JLabel("Description:");
        calendarButton.setOpaque(false);
        calendarButton.setIcon(AppResources.normalDateIcon);
        calendarButton.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                TaskNode node = NATOMain.instance.currentNode;
                if (node != null) calendarButton.init("Completion Date", node.getCompletionDate(), "");
            }
        });
        calendarButton.addPropertyChangeListener(new java.beans.PropertyChangeListener() {

            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                Object obj = evt.getNewValue();
                if (null == obj) {
                    obj = evt.getOldValue();
                }
                if (NATOMain.instance != null) {
                    TaskNode targetNode = NATOMain.instance.currentNode;
                    if (obj != null && targetNode != null && obj instanceof Date) {
                        targetNode.setCompletionDate((Date) evt.getNewValue());
                        dueField.setText(NATOMain.dateFormatter.format(targetNode.getCompletionDate()));
                        TreeUtil.refreshTreeTaskState();
                        TaskState nodeState = targetNode.getState();
                        setDateState(nodeState);
                    }
                }
            }
        });
        createdLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        dueLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        nameLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        descriptionLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        nameField.addKeyListener(new NameFieldKeyAdapter());
        nameField.addKeyListener(new EscapeToTreeKeyAdapter());
        statusComboBox.getModel().addListDataListener(new StatusListDataListener());
        statusComboBox.addKeyListener(new EscapeToTreeKeyAdapter());
        statusComboBox.addKeyListener(new KeyAdapter() {

            public void keyReleased(KeyEvent e) {
                int code = e.getKeyCode();
                if (KeyCodes.KEY_ENTER == code) {
                    NATOMain.historyTextArea.append(new Date().toString() + " --> " + ((TaskStatus) statusComboBox.getSelectedItem()).toString() + "\n");
                }
            }
        });
        percentComboBox.addKeyListener(new EscapeToTreeKeyAdapter());
        descriptionField.setMargin(new Insets(3, 3, 3, 3));
        descriptionField.setLineWrap(true);
        descriptionField.setWrapStyleWord(true);
        descriptionField.setFont(AppResources.monoFont);
        descriptionField.addKeyListener(new EscapeToTreeKeyAdapter());
        descriptionField.addMouseListener(new ResourceLauncherMouseListener());
        installTextUndoRedo(descriptionField, undoMgr, taskDoc);
        JScrollPane scroller = new JScrollPane(descriptionField);
        this.add(createdLabel);
        layout.getConstraints(createdLabel).setY(Spring.constant(TOP_INSET));
        layout.getConstraints(createdLabel).setX(Spring.constant(0));
        layout.getConstraints(createdLabel).setWidth(Spring.constant(LABEL_WIDTH));
        layout.getConstraints(createdLabel).setHeight(Spring.constant(20));
        this.add(createdField);
        layout.getConstraints(createdField).setY(Spring.constant(TOP_INSET));
        layout.getConstraints(createdField).setX(Spring.constant(FIELD_MARGIN));
        layout.getConstraints(createdField).setWidth(Spring.constant(DATE_FIELD_WIDTH));
        layout.getConstraints(createdField).setHeight(Spring.constant(FIELD_HEIGHT));
        this.add(dueLabel);
        layout.getConstraints(dueLabel).setY(Spring.constant(TOP_INSET));
        layout.getConstraints(dueLabel).setX(Spring.constant(195));
        layout.getConstraints(dueLabel).setWidth(Spring.constant(50));
        layout.getConstraints(dueLabel).setHeight(Spring.constant(20));
        dueField.setOpaque(false);
        dueField.setBackground(Color.WHITE);
        dueField.getInsets().set(0, 3, 0, 0);
        this.add(dueField);
        layout.getConstraints(dueField).setY(Spring.constant(TOP_INSET));
        layout.getConstraints(dueField).setX(Spring.constant(250));
        layout.getConstraints(dueField).setWidth(Spring.constant(DATE_FIELD_WIDTH));
        layout.getConstraints(dueField).setHeight(Spring.constant(20));
        this.add(calendarButton);
        layout.getConstraints(calendarButton).setY(Spring.constant(TOP_INSET));
        layout.getConstraints(calendarButton).setX(Spring.constant(360));
        layout.getConstraints(calendarButton).setWidth(Spring.constant(19));
        layout.getConstraints(calendarButton).setHeight(Spring.constant(19));
        this.add(nameLabel);
        layout.getConstraints(nameLabel).setY(Spring.constant(TOP_INSET + LABEL_HEIGHT));
        layout.getConstraints(nameLabel).setX(Spring.constant(0));
        layout.getConstraints(nameLabel).setWidth(Spring.constant(LABEL_WIDTH));
        layout.getConstraints(nameLabel).setHeight(Spring.constant(20));
        this.add(descriptionLabel);
        layout.getConstraints(descriptionLabel).setY(Spring.constant(TOP_INSET + (LABEL_HEIGHT * 3)));
        layout.getConstraints(descriptionLabel).setX(Spring.constant(0));
        layout.getConstraints(descriptionLabel).setWidth(Spring.constant(LABEL_WIDTH));
        layout.getConstraints(descriptionLabel).setHeight(Spring.constant(20));
        this.add(statusComboBox);
        layout.getConstraints(statusComboBox).setY(Spring.constant(TOP_INSET));
        layout.getConstraints(statusComboBox).setX(Spring.constant(420));
        layout.getConstraints(statusComboBox).setWidth(Spring.constant(120));
        layout.getConstraints(statusComboBox).setHeight(Spring.constant(FIELD_HEIGHT));
        this.add(percentComboBox);
        layout.getConstraints(percentComboBox).setY(Spring.constant(TOP_INSET));
        layout.getConstraints(percentComboBox).setX(Spring.constant(FIELD_MARGIN * 4 + 150));
        layout.getConstraints(percentComboBox).setWidth(Spring.constant(120));
        layout.getConstraints(percentComboBox).setHeight(Spring.constant(FIELD_HEIGHT));
        nameField.setMaximumSize(new Dimension(1000, 1000));
        nameField.setMinimumSize(new Dimension(600, 50));
        this.add(nameField);
        layout.putConstraint(SpringLayout.WEST, nameField, 5, SpringLayout.EAST, nameLabel);
        layout.getConstraints(nameField).setY(Spring.constant(TOP_INSET + LABEL_HEIGHT + FIELD_PADDING));
        layout.getConstraints(nameField).setWidth(Spring.constant(FIELD_WIDTH + 125));
        layout.getConstraints(nameField).setHeight(Spring.constant(FIELD_HEIGHT));
        this.add(scroller);
        layout.putConstraint(SpringLayout.NORTH, scroller, 15, SpringLayout.SOUTH, nameField);
        layout.putConstraint(SpringLayout.WEST, scroller, 5, SpringLayout.EAST, descriptionLabel);
        layout.putConstraint(SpringLayout.SOUTH, this, 20, SpringLayout.SOUTH, scroller);
        layout.putConstraint(SpringLayout.EAST, this, 20, SpringLayout.EAST, scroller);
        layout.layoutContainer(this);
    }

    private void setDateState(TaskState nodeState) {
        switch(nodeState) {
            case Critical:
                dueLabel.setIcon(AppResources.criticalChip);
                break;
            case Urgent:
                dueLabel.setIcon(AppResources.urgentChip);
                break;
            default:
                dueLabel.setIcon(AppResources.normalChip);
        }
    }

    private class NameFieldKeyAdapter extends KeyAdapter {

        public void keyReleased(KeyEvent e) {
            String text = nameField.getText();
            if (text != null) {
                if (NATOMain.masterTree.getSelectionCount() > 0) {
                    NATOMain.instance.currentNode.setName(text);
                    NATOMain.masterTree.updateUI();
                }
            }
        }
    }

    /**
     * We won't try to guess what the caller wants to do with this because it is a more complex component.
     *
     * @return Returns the combo box component.
     */
    public JComboBox getStatusComboBox() {
        return statusComboBox;
    }

    /**
     * We won't try to guess what the caller wants to do with this because it is a more complex component.
     *
     * @return Returns the combo box component.
     */
    public JComboBox getPercentComboBox() {
        return percentComboBox;
    }

    public String getDescriptionField() {
        return descriptionField.getText();
    }

    public void setTaskName(String name) {
        nameField.setText(name);
    }

    public void setDetailData(TaskNode currentNode) {
        if (currentNode != null) {
            nameField.setText(currentNode.getName());
            createdField.setText(NATOMain.dateFormatter.format(currentNode.getCreationDate()));
            TaskState nodeState = currentNode.getState();
            setDateState(nodeState);
            String formattedDate = "";
            if (currentNode.getCompletionDate() != null) {
                formattedDate = NATOMain.dateFormatter.format(currentNode.getCompletionDate());
                dueLabel.setVisible(true);
            } else {
                dueLabel.setVisible(false);
            }
            dueField.setText(formattedDate);
            descriptionField.setText(currentNode.getShortDescription());
            descriptionField.setCaretPosition(0);
            if (currentNode instanceof TaskRootNode) {
                statusComboBox.setEnabled(false);
                percentComboBox.setEnabled(false);
                statusComboBox.setSelectedItem(null);
                percentComboBox.setSelectedItem(null);
            } else {
                statusComboBox.setEnabled(true);
                percentComboBox.setEnabled(true);
                statusComboBox.setSelectedItem(currentNode.getStatus());
                percentComboBox.setSelectedItem(currentNode.getCompletionPercent());
            }
            NATOMain.historyTextArea.setText(currentNode.getFullDescription());
            NATOMain.historyTextArea.setCaretPosition(0);
        }
    }

    public void installTextUndoRedo(JTextComponent component, final UndoManager mgr, Document taskDoc) {
        taskDoc.addUndoableEditListener(new UndoableEditListener() {

            public void undoableEditHappened(UndoableEditEvent evt) {
                mgr.addEdit(evt.getEdit());
            }
        });
        component.getActionMap().put("Undo", new AbstractAction("Undo") {

            public void actionPerformed(ActionEvent evt) {
                try {
                    if (mgr.canUndo()) {
                        mgr.undo();
                    }
                } catch (CannotUndoException e) {
                }
            }
        });
        component.getInputMap().put(KeyStroke.getKeyStroke("control Z"), "Undo");
        component.getActionMap().put("Redo", new AbstractAction("Redo") {

            public void actionPerformed(ActionEvent evt) {
                try {
                    if (mgr.canRedo()) {
                        mgr.redo();
                    }
                } catch (CannotRedoException e) {
                }
            }
        });
        component.getInputMap().put(KeyStroke.getKeyStroke("control Y"), "Redo");
    }

    private class StatusListDataListener implements ListDataListener {

        public void intervalAdded(ListDataEvent e) {
        }

        public void intervalRemoved(ListDataEvent e) {
        }

        public void contentsChanged(ListDataEvent e) {
            TaskNode node = NATOMain.instance.currentNode;
            if (node instanceof TaskRootNode) {
                return;
            }
            if (node != null) {
                TaskStatus status = (TaskStatus) statusComboBox.getSelectedItem();
                if (node != null && !node.getStatus().equals(status)) {
                    node.setStatus(status);
                    TreeUtil.refreshTreeTaskState();
                    NATOMain.masterTree.updateUI();
                }
            }
        }
    }
}
