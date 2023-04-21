package ch.intertec.storybook.view.dialog;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import net.miginfocom.swing.MigLayout;
import ch.intertec.storybook.action.TableEditAction;
import ch.intertec.storybook.main.MainFrame;
import ch.intertec.storybook.model.PCSDispatcher;
import ch.intertec.storybook.model.Scene;
import ch.intertec.storybook.model.PCSDispatcher.Property;
import ch.intertec.storybook.toolkit.I18N;
import ch.intertec.storybook.toolkit.swing.ReadOnlyTable;
import ch.intertec.storybook.toolkit.swing.SwingTools;
import ch.intertec.storybook.toolkit.swing.table.StandardTableCellRenderer;
import ch.intertec.storybook.toolkit.swing.table.StatusTableCellRenderer;
import ch.intertec.storybook.view.IRefreshable;
import ch.intertec.storybook.view.ViewTools;
import ch.intertec.storybook.view.model.TaskTableModel;

@SuppressWarnings("serial")
public class TaskListFrame extends JFrame implements MouseListener, IRefreshable, PropertyChangeListener, WindowListener {

    private JTable table;

    public TaskListFrame() {
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        addWindowListener(this);
        addListeners();
        initGUI();
    }

    @Override
    public void refresh() {
        refreshTable();
        Container contentPane = getContentPane();
        for (Component comp : contentPane.getComponents()) {
            if (comp instanceof JScrollPane) {
                JScrollPane scroller = (JScrollPane) comp;
                scroller.setViewportView(table);
                contentPane.validate();
                contentPane.repaint();
                return;
            }
        }
    }

    private void initGUI() {
        MigLayout layout = new MigLayout("wrap,fill");
        setLayout(layout);
        setPreferredSize(new Dimension(700, 525));
        setTitle(I18N.getMsg("msg.tasklist.title"));
        ImageIcon icon = (ImageIcon) I18N.getIcon("icon.sb");
        setIconImage(icon.getImage());
        refreshTable();
        JScrollPane scroller = new JScrollPane(table);
        scroller.setBorder(SwingTools.getEtchedBorder());
        JButton btFind = new JButton();
        btFind.setAction(getFindAction());
        btFind.setText(I18N.getMsg("msg.common.find"));
        btFind.setIcon(I18N.getIcon("icon.small.search"));
        JButton btEdit = new JButton();
        btEdit.setAction(getEditAction());
        btEdit.setText(I18N.getMsg("msg.common.edit"));
        btEdit.setIcon(I18N.getIcon("icon.small.edit"));
        JButton btRefresh = new JButton();
        btRefresh.setAction(getRefreshAction());
        btRefresh.setIcon(I18N.getIcon("icon.small.refresh"));
        btRefresh.setToolTipText(I18N.getMsg("msg.common.refresh"));
        JButton btClose = new JButton();
        btClose.setAction(getCloseAction());
        SwingTools.addEscAction(btClose, getCloseAction());
        btClose.setText(I18N.getMsg("msg.common.close"));
        btClose.setIcon(I18N.getIcon("icon.small.close"));
        add(scroller, "grow");
        add(btFind, "split");
        add(btEdit);
        add(btRefresh);
        add(btClose, "gap push");
    }

    private void refreshTable() {
        TaskTableModel model = new TaskTableModel();
        table = new ReadOnlyTable(model);
        table.setRowHeight(25);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setAutoCreateRowSorter(true);
        table.setDefaultRenderer(Object.class, new StandardTableCellRenderer());
        table.addMouseListener(this);
        TableColumnModel colModel = table.getColumnModel();
        for (int i = 0; i < colModel.getColumnCount(); ++i) {
            TableColumn col = colModel.getColumn(i);
            int width = 0;
            if (i == 0) {
                width = 300;
            }
            if (i == 1) {
                width = 200;
            }
            if (i == 2) {
                width = 140;
                col.setCellRenderer(new StatusTableCellRenderer());
            }
            if (i == 3) {
                width = 50;
            }
            col.setPreferredWidth(width);
        }
    }

    private AbstractAction getFindAction() {
        return new AbstractAction() {

            public void actionPerformed(ActionEvent evt) {
                int row = table.getSelectedRow();
                if (row == -1) {
                    return;
                }
                try {
                    MainFrame.getInstance().requestFocus();
                    int rowModel = table.convertRowIndexToModel(row);
                    TaskTableModel model = (TaskTableModel) table.getModel();
                    Scene scene = model.getSceneAt(rowModel);
                    ViewTools.scrollToScene(scene);
                } catch (ClassCastException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    private AbstractAction getEditAction() {
        return new AbstractAction() {

            public void actionPerformed(ActionEvent evt) {
                int row = table.getSelectedRow();
                if (row == -1) {
                    return;
                }
                try {
                    int rowModel = table.convertRowIndexToModel(row);
                    TaskTableModel model = (TaskTableModel) table.getModel();
                    Scene scene = model.getSceneAt(rowModel);
                    TableEditAction tea = new TableEditAction(scene);
                    tea.setParentFrame(MainFrame.getInstance());
                    tea.actionPerformed(null);
                    getThis().setVisible(true);
                } catch (ClassCastException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    private AbstractAction getRefreshAction() {
        return new AbstractAction() {

            public void actionPerformed(ActionEvent evt) {
                getThis().refresh();
            }
        };
    }

    private AbstractAction getCloseAction() {
        return new AbstractAction() {

            public void actionPerformed(ActionEvent evt) {
                removeListeners();
                getThis().dispose();
            }
        };
    }

    private TaskListFrame getThis() {
        return this;
    }

    private void addListeners() {
        PCSDispatcher pcs = PCSDispatcher.getInstance();
        pcs.addPropertyChangeListener(Property.SCENE, this);
        pcs.addPropertyChangeListener(Property.PROJECT, this);
    }

    private void removeListeners() {
        PCSDispatcher.getInstance().removeAllPropertyChangeListener(this);
    }

    @Override
    public void mouseClicked(MouseEvent evt) {
        Object o = evt.getSource();
        if (o instanceof JTable) {
            if (evt.getClickCount() == 2) {
                getFindAction().actionPerformed(null);
            }
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (PCSDispatcher.isPropertyFired(Property.SCENE, evt)) {
            refresh();
            return;
        }
        if (PCSDispatcher.isPropertyFired(Property.PROJECT, evt)) {
            if (PCSDispatcher.isPropertyRemoved(evt)) {
                dispose();
                return;
            }
            if (PCSDispatcher.isPropertyNew(evt)) {
                refresh();
                return;
            }
        }
    }

    @Override
    public void windowActivated(WindowEvent e) {
    }

    @Override
    public void windowClosed(WindowEvent e) {
    }

    @Override
    public void windowClosing(WindowEvent e) {
        removeListeners();
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
    }

    @Override
    public void windowIconified(WindowEvent e) {
    }

    @Override
    public void windowOpened(WindowEvent e) {
    }
}
