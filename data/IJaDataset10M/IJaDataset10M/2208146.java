package javax.swing.addon;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import com.listentothesong.ImagesRepository;

@SuppressWarnings("serial")
public class JDoubleList<T> extends JPanel {

    private final HashSet<ActionListener> actionListeners = new HashSet<ActionListener>();

    private final DefaultListModel availableObjectsListModel = new DefaultListModel();

    private final DefaultListModel selectedObjectsListModel = new DefaultListModel();

    private final JList availableObjectsList = new JList(availableObjectsListModel);

    private final JList selectedObjectsList = new JList(selectedObjectsListModel);

    private final JLabel availableObjectsListLabel = new JLabel("", JLabel.CENTER);

    private final JLabel selectedObjectsListLabel = new JLabel("", JLabel.CENTER);

    public JDoubleList() {
        setLayout(new CenterColumnLayout(10));
        final JButton leftButton = new JButton(ImagesRepository.LEFT_1ARROW);
        final JButton leftMostButton = new JButton(ImagesRepository.LEFT_2ARROW);
        final JButton rightMostButton = new JButton(ImagesRepository.RIGHT_2ARROW);
        final JButton rightButton = new JButton(ImagesRepository.RIGHT_1ARROW);
        ActionListener actionListener = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == rightButton) {
                    for (Object object : availableObjectsList.getSelectedValues()) {
                        if (!selectedObjectsListModel.contains(object)) selectedObjectsListModel.addElement(object);
                    }
                    fireActionPerformed();
                } else if (e.getSource() == rightMostButton) {
                    for (int i = 0; i < availableObjectsListModel.getSize(); i++) {
                        Object object = availableObjectsListModel.getElementAt(i);
                        if (!selectedObjectsListModel.contains(object)) selectedObjectsListModel.addElement(object);
                    }
                    fireActionPerformed();
                } else if (e.getSource() == leftMostButton) {
                    selectedObjectsListModel.clear();
                    fireActionPerformed();
                } else if (e.getSource() == leftButton) {
                    for (Object object : selectedObjectsList.getSelectedValues()) selectedObjectsListModel.removeElement(object);
                    fireActionPerformed();
                }
            }
        };
        leftButton.addActionListener(actionListener);
        leftMostButton.addActionListener(actionListener);
        rightMostButton.addActionListener(actionListener);
        rightButton.addActionListener(actionListener);
        JPanel rightButtonPanel = new JPanel(new BorderLayout());
        JPanel rightMostButtonPanel = new JPanel(new BorderLayout());
        JPanel leftMostButtonPanel = new JPanel(new BorderLayout());
        JPanel leftButtonPanel = new JPanel(new BorderLayout());
        rightButtonPanel.add(rightButton);
        rightMostButtonPanel.add(rightMostButton);
        leftMostButtonPanel.add(leftMostButton);
        leftButtonPanel.add(leftButton);
        rightButtonPanel.setBorder(new EmptyBorder(5, 5, 0, 5));
        rightMostButtonPanel.setBorder(new EmptyBorder(5, 5, 0, 5));
        leftMostButtonPanel.setBorder(new EmptyBorder(5, 5, 0, 5));
        leftButtonPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        JPanel buttonsGridPanel = new JPanel(new GridLayout(4, 1));
        buttonsGridPanel.add(rightButtonPanel);
        buttonsGridPanel.add(rightMostButtonPanel);
        buttonsGridPanel.add(leftMostButtonPanel);
        buttonsGridPanel.add(leftButtonPanel);
        JPanel centerPanel = new JPanel(new CenterColumnLayout(10, true));
        centerPanel.add(buttonsGridPanel, CenterColumnLayout.CENTER);
        JPanel availableObjectsListPanel = new JPanel(new BorderLayout());
        availableObjectsListPanel.add(availableObjectsListLabel, BorderLayout.NORTH);
        availableObjectsListPanel.add(new FastScrollPane(availableObjectsList));
        JPanel selectedObjectsListPanel = new JPanel(new BorderLayout());
        selectedObjectsListPanel.add(selectedObjectsListLabel, BorderLayout.NORTH);
        selectedObjectsListPanel.add(new FastScrollPane(selectedObjectsList));
        add(availableObjectsListPanel, CenterColumnLayout.EAST);
        add(selectedObjectsListPanel, CenterColumnLayout.WEST);
        add(centerPanel, CenterColumnLayout.CENTER);
        availableObjectsList.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    Object object = availableObjectsList.getSelectedValue();
                    if (!selectedObjectsListModel.contains(object)) selectedObjectsListModel.addElement(object);
                }
            }
        });
    }

    private void fireActionPerformed() {
        ActionEvent e = new ActionEvent(this, 0, "Update");
        for (ActionListener actionListener : actionListeners) actionListener.actionPerformed(e);
    }

    public void removeAllAvailableObjects() {
        availableObjectsListModel.removeAllElements();
    }

    public void setAvailableObjectsListLabel(String label) {
        availableObjectsListLabel.setText(label + ":");
    }

    public void setSelectedObjectsListLabel(String label) {
        selectedObjectsListLabel.setText(label + ":");
    }

    public void addAvailableObjects(T... objects) {
        for (T object : objects) {
            if (object == null) throw new NullPointerException();
            availableObjectsListModel.addElement(object);
        }
    }

    public boolean addActionListener(ActionListener actionListener) {
        return actionListeners.add(actionListener);
    }

    public boolean removeActionListener(ActionListener actionListener) {
        return actionListeners.remove(actionListener);
    }

    public void deselectAllObjects() {
        selectedObjectsListModel.removeAllElements();
    }

    public void selectObjects(T... objects) {
        for (T object : objects) {
            if (object == null) throw new NullPointerException();
            if (!selectedObjectsListModel.contains(object)) selectedObjectsListModel.addElement(object);
        }
    }

    @SuppressWarnings("unchecked")
    public Collection<T> getSelectedObjects() {
        LinkedList<T> selectedObjects = new LinkedList<T>();
        for (int i = 0; i < selectedObjectsListModel.getSize(); i++) selectedObjects.add((T) selectedObjectsListModel.getElementAt(i));
        return selectedObjects;
    }

    public void setListsFont(Font font) {
        selectedObjectsList.setFont(font);
        availableObjectsList.setFont(font);
    }
}
