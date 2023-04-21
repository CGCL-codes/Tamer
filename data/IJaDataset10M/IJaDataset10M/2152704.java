package org.jmove.zui.search;

import org.jmove.java.model.JPackage;
import org.jmove.java.model.Type;
import org.jmove.oo.Module;
import org.jmove.zui.core.InteractionContext;
import org.jmove.zui.icons.Icons;
import org.jmove.zui.util.InputActionMap;
import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.lang.reflect.Modifier;
import java.util.Vector;

/**
 * Instances of this class display a list of model elements in a {@link JList}.
 * The window size will within certain constraints adopt dynamically to the size which is required
 * to display the list elements.<br/>
 * The class contains a cell renderer which is used to render the the list cells
 * according to the model element type.
 *
 * @author Axel Terfloth
 */
public class SearchResultWindow extends JWindow {

    public static int MAX_LIST_HEIGHT = 300;

    public static int MIN_LIST_WIDTH = 300;

    private InteractionContext myContext;

    private JList myResultList;

    public SearchResultWindow(InteractionContext context, Window window) throws HeadlessException {
        super(window);
        myContext = context;
        setVisible(false);
        getContentPane().setLayout(new BorderLayout());
        myResultList = new JList();
        getContentPane().add(myResultList, BorderLayout.CENTER);
        myResultList.setCellRenderer(new ResultListCellRenderer());
        JScrollPane scrollPane = new JScrollPane(myResultList);
        scrollPane.setPreferredSize(new Dimension(400, 300));
        getContentPane().add(scrollPane);
        setSize(new Dimension(0, 0));
        MouseInputAdapter listener = new MouseInputAdapter() {

            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    KeyStroke keyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0);
                    ActionListener actionListener = myResultList.getActionForKeyStroke(keyStroke);
                    if (actionListener != null) {
                        String command = (String) myResultList.getInputMap().get(keyStroke);
                        actionListener.actionPerformed(new ActionEvent(e.getSource(), e.getID(), command));
                    }
                }
            }
        };
        myResultList.addMouseListener(listener);
    }

    public Object getSelectedElement() {
        return myResultList.getSelectedValue();
    }

    public void selectPrevious() {
        if (myResultList.getModel().getSize() > 0) {
            int idx = myResultList.getSelectedIndex();
            if (idx == -1) {
                idx = 0;
            } else {
                idx--;
            }
            if (idx < 0) {
                idx = myResultList.getModel().getSize() - 1;
            }
            myResultList.setSelectedIndex(idx);
            myResultList.ensureIndexIsVisible(idx);
        }
    }

    public void selectNext() {
        if (myResultList.getModel().getSize() > 0) {
            int idx = myResultList.getSelectedIndex();
            if (idx == -1) {
                idx = 0;
            } else {
                idx++;
            }
            if (idx >= myResultList.getModel().getSize()) {
                idx = 0;
            }
            myResultList.setSelectedIndex(idx);
            myResultList.ensureIndexIsVisible(idx);
        }
    }

    public void selectPageUp() {
        int idx = myResultList.getFirstVisibleIndex();
        ListSelectionModel lsm = myResultList.getSelectionModel();
        if (lsm.getLeadSelectionIndex() == idx) {
            Rectangle visRect = myResultList.getVisibleRect();
            visRect.y = Math.max(0, visRect.y - visRect.height);
            idx = myResultList.locationToIndex(visRect.getLocation());
        }
        myResultList.setSelectedIndex(idx);
        myResultList.ensureIndexIsVisible(idx);
    }

    public void selectPageDown() {
        int idx = myResultList.getLastVisibleIndex();
        ListSelectionModel lsm = myResultList.getSelectionModel();
        if (idx == -1) {
            idx = myResultList.getModel().getSize() - 1;
        }
        if (lsm.getLeadSelectionIndex() == idx) {
            Rectangle visRect = myResultList.getVisibleRect();
            visRect.y += visRect.height + visRect.height - 1;
            idx = myResultList.locationToIndex(visRect.getLocation());
            if (idx == -1) {
                idx = myResultList.getModel().getSize() - 1;
            }
        }
        myResultList.setSelectedIndex(idx);
        myResultList.ensureIndexIsVisible(idx);
    }

    /**
	 * This method allows to extend the key stroke action binding of the list component.
	 * So the behavior of this window can be customized from outside.
	 *
	 * @param inputActions
	 */
    public void useInputActions(InputActionMap inputActions) {
        inputActions.addActionMappingTo(myResultList);
    }

    public void setValues(Vector resultEntries) {
        myResultList.setListData(resultEntries);
        Point point = getOwner().getLocation();
        setLocation((int) point.getX(), getOwner().getHeight() + (int) point.getY());
        if (resultEntries.size() == 0) {
            setSize(0, 0);
        } else {
            Dimension cellBounds = myResultList.getPreferredSize();
            int width = Math.max(cellBounds.width, MIN_LIST_WIDTH);
            int height = Math.min(cellBounds.height, MAX_LIST_HEIGHT);
            invalidate();
            setSize(width + 5, height + 5);
            validateTree();
            if (myResultList.getSelectedIndex() == -1) {
                myResultList.setSelectedIndex(0);
            }
        }
    }

    public static class ResultListCellRenderer extends JPanel implements ListCellRenderer {

        private JLabel myPackageLabel;

        private JLabel myTypeLabel;

        public ResultListCellRenderer() {
            myTypeLabel = new JLabel();
            myPackageLabel = new JLabel();
            myPackageLabel.setForeground(Color.GRAY);
            setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
            add(myTypeLabel);
            add(myPackageLabel);
        }

        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            if (value instanceof Type) {
                myTypeLabel.setText(((Type) value).name());
                myPackageLabel.setText("  (" + ((Type) value).getPackage().id() + ")");
                if (Modifier.isInterface(((Type) value).getModifiers())) {
                    myTypeLabel.setIcon(Icons.INTERFACE_ICON);
                } else {
                    myTypeLabel.setIcon(Icons.CLASS_ICON);
                }
            } else {
                myTypeLabel.setText(value.toString());
                myPackageLabel.setText("");
                if (value instanceof JPackage) {
                    myTypeLabel.setIcon(Icons.PACKAGE_ICON);
                } else if (value instanceof Module) {
                    myTypeLabel.setIcon(Icons.JAR_ICON);
                } else {
                    myTypeLabel.setIcon(null);
                }
            }
            if (isSelected) {
                setBackground(list.getSelectionBackground());
                setForeground(list.getSelectionForeground());
            } else {
                setBackground(list.getBackground());
                setForeground(list.getForeground());
            }
            setEnabled(list.isEnabled());
            setFont(list.getFont());
            setOpaque(true);
            validateTree();
            getLayout().layoutContainer(this);
            setSize(getLayout().preferredLayoutSize(this));
            return this;
        }
    }
}
