package net.sf.jtreefield;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Date;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.tree.TreeCellEditor;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

/**
 * Field used to select a date.
 * @author Ignacio Merani
 */
public class JTreeField extends JPanel implements ActionListener, PropertyChangeListener {

    private WindowPanel panel;

    private Date fecha;

    protected JTextField field;

    private JButton button;

    protected JTree tree;

    private boolean showOkCancel;

    private TreePath selectedpath;

    /**
     * Utility field holding list of ChangeListeners.
     */
    private transient java.util.ArrayList changeListenerList;

    /**
     * Creates a new instance of DateField
     * @param showWeekNumbers true if the week numbers must be shown
     *
     */
    public JTreeField() {
        init(false);
    }

    public JTreeField(boolean showOkCancel) {
        init(showOkCancel);
    }

    private void init(boolean showOkCancel) {
        this.showOkCancel = showOkCancel;
        tree = new JTree();
        setLayout(new BorderLayout());
        add(field = new JTextField());
        add(button = new ArrowButton(ArrowButton.SOUTH), BorderLayout.EAST);
        button.addActionListener(this);
        field.addPropertyChangeListener("value", this);
        field.setEditable(false);
        Border border = field.getBorder();
        field.setBorder(null);
        setBorder(border);
    }

    private void aceptar() {
        panel.setVisible(false);
        selectedpath = tree.getSelectionPath();
        setValue(tree.getLastSelectedPathComponent());
        fireChangeListenerStateChanged(new ChangeEvent(this));
    }

    private void cancelar() {
        panel.setVisible(false);
        tree.setSelectionPath(selectedpath);
    }

    private void createWindow() {
        Component c = this;
        Dialog d = null;
        while (!(c instanceof Dialog) && (c != null)) {
            c = c.getParent();
        }
        if (c != null) {
            panel = new WindowPanel((Dialog) c);
            return;
        }
        Frame f = JOptionPane.getFrameForComponent(this);
        panel = new WindowPanel(f);
    }

    /**
     * Invoked when an action occurs.
     * @param e the event fired
     */
    public void actionPerformed(ActionEvent e) {
        if (panel == null) createWindow();
        Point p = getLocationOnScreen();
        panel.setBounds(p.x, p.y + getHeight(), getWidth(), 150);
        panel.setVisible(true);
    }

    /**
     * Sets the current Date
     * @param value current Date
     */
    public void setValue(Object value) {
        if (value == null) {
            field.setText(null);
        } else {
            field.setText(value.toString());
        }
    }

    /**
     * Returns the current Date
     * @return current Date
     */
    public Object getValue() {
        return tree.getLastSelectedPathComponent();
    }

    /**
     * Registers ChangeListener to receive events.
     * @param listener The listener to register.
     */
    public synchronized void addChangeListener(ChangeListener listener) {
        if (changeListenerList == null) {
            changeListenerList = new java.util.ArrayList();
        }
        changeListenerList.add(listener);
    }

    /**
     * Removes ChangeListener from the list of listeners.
     * @param listener The listener to remove.
     */
    public synchronized void removeChangeListener(ChangeListener listener) {
        if (changeListenerList != null) {
            changeListenerList.remove(listener);
        }
    }

    /**
     * Notifies all registered listeners about the event.
     *
     * @param event The event to be fired
     */
    private void fireChangeListenerStateChanged(ChangeEvent event) {
        java.util.ArrayList list;
        synchronized (this) {
            if (changeListenerList == null) return;
            list = (java.util.ArrayList) changeListenerList.clone();
        }
        for (int i = 0; i < list.size(); i++) {
            ((javax.swing.event.ChangeListener) list.get(i)).stateChanged(event);
        }
    }

    /**
     * Event fired when a property changes
     * @param evt event fired
     */
    public void propertyChange(PropertyChangeEvent evt) {
        fireChangeListenerStateChanged(new ChangeEvent(this));
    }

    /** Getter for property model.
     * @return Value of property model.
     *
     */
    public TreeModel getModel() {
        return tree.getModel();
    }

    /** Setter for property model.
     * @param model New value of property model.
     *
     */
    public void setModel(TreeModel model) {
        if (model != null) tree.setModel(model);
    }

    /** Getter for property treeCellRenderer.
     * @return Value of property treeCellRenderer.
     *
     */
    public TreeCellRenderer getTreeCellRenderer() {
        return tree.getCellRenderer();
    }

    /** Setter for property treeCellRenderer.
     * @param treeCellRenderer New value of property treeCellRenderer.
     *
     */
    public void setTreeCellRenderer(TreeCellRenderer treeCellRenderer) {
        if (treeCellRenderer != null) tree.setCellRenderer(treeCellRenderer);
    }

    /** Getter for property treeCellEditor.
     * @return Value of property treeCellEditor.
     *
     */
    public TreeCellEditor getTreeCellEditor() {
        return tree.getCellEditor();
    }

    /** Setter for property treeCellEditor.
     * @param treeCellEditor New value of property treeCellEditor.
     *
     */
    public void setTreeCellEditor(TreeCellEditor treeCellEditor) {
        if (treeCellEditor != null) tree.setCellEditor(treeCellEditor);
    }

    /** Getter for property showOkCancel.
     * @return Value of property showOkCancel.
     *
     */
    public boolean isShowOkCancel() {
        return showOkCancel;
    }

    /** Setter for property showOkCancel.
     * @param showOkCancel New value of property showOkCancel.
     *
     */
    public void setShowOkCancel(boolean showOkCancel) {
        this.showOkCancel = showOkCancel;
        panel = null;
    }

    class WindowPanel extends JDialog {

        public WindowPanel(Frame parent) {
            super(parent, false);
            init();
        }

        public WindowPanel(Dialog parent) {
            super(parent, false);
            init();
        }

        private void init() {
            setUndecorated(true);
            JPanel todo = new JPanel(new BorderLayout());
            todo.setBorder(BorderFactory.createLineBorder(Color.black));
            getContentPane().add(todo);
            todo.add(new JScrollPane(tree));
            if (showOkCancel) {
                JPanel abajo = new JPanel();
                todo.add(abajo, BorderLayout.SOUTH);
                JButton ok = new JButton("Ok");
                JButton cancel = new JButton("Cancel");
                abajo.add(ok);
                abajo.add(cancel);
                getRootPane().setDefaultButton(ok);
                ok.addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent e) {
                        aceptar();
                    }
                });
                cancel.addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent e) {
                        cancelar();
                    }
                });
                tree.addMouseListener(new MouseAdapter() {

                    public void mouseClicked(MouseEvent e) {
                        if (e.getClickCount() > 1) {
                            if (tree.getPathForLocation(e.getX(), e.getY()) != null) aceptar();
                        }
                    }
                });
            } else {
                tree.addMouseListener(new MouseAdapter() {

                    public void mouseClicked(MouseEvent e) {
                        if (tree.getPathForLocation(e.getX(), e.getY()) != null) aceptar();
                    }
                });
            }
            tree.addKeyListener(new KeyAdapter() {

                public void keyTyped(KeyEvent e) {
                    if (e.getKeyChar() == 10) aceptar();
                    if (e.getKeyChar() == 27) cancelar();
                }
            });
            addWindowListener(new WindowAdapter() {

                public void windowDeactivated(WindowEvent e) {
                    cancelar();
                }
            });
            pack();
        }
    }
}
