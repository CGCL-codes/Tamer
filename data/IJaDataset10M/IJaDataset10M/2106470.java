package jset.view.widgetinfo;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import jset.Util;
import jset.model.gui.GUIElement;
import jset.project.Project;
import jset.view.AbstractView;
import jset.view.ViewContext;
import jset.view.widgetinfo.event.PropertySelectionEvent;
import jset.view.widgetinfo.event.PropertySelectionListener;
import org.apache.commons.lang.NotImplementedException;

public class WidgetInfoView extends AbstractView implements ListSelectionListener {

    private static final long serialVersionUID = 1L;

    private Project leftProject;

    private Project rightProject;

    private JTabbedPane home;

    private ScreenshotsPanel screenshotsPanel;

    private JTable generalPropsTable;

    private JTable listenerPropsTable;

    private GUIElement leftComponent;

    private GUIElement rightComponent;

    PropertySelectionEvent psEvent = null;

    public WidgetInfoView(ViewContext context) {
        super(context);
        unwrapContext();
        initPanel();
    }

    private void unwrapContext() {
        Project[] projects = (Project[]) context.get(WidgetInfoContext.PROJECTS);
        leftProject = projects[0];
        if (projects.length > 1) {
            rightProject = projects[1];
        }
    }

    private void initPanel() {
        setLayout(new GridBagLayout());
        setPreferredSize(new Dimension(600, 400));
        home = new JTabbedPane();
        screenshotsPanel = new ScreenshotsPanel();
        screenshotsPanel.setScreenshotDirectories(leftProject.getScreenshotDirectory(), (rightProject != null ? rightProject.getScreenshotDirectory() : null));
        home.addTab("Screenshot", screenshotsPanel);
        generalPropsTable = new JTable();
        generalPropsTable.setColumnSelectionAllowed(false);
        generalPropsTable.setRowSelectionAllowed(true);
        generalPropsTable.setAutoCreateRowSorter(true);
        generalPropsTable.setDefaultRenderer(Object.class, new PropertiesTableCellRenderer());
        generalPropsTable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        generalPropsTable.getSelectionModel().addListSelectionListener(this);
        home.addTab("Properties", new JScrollPane(generalPropsTable));
        listenerPropsTable = new JTable();
        listenerPropsTable.setColumnSelectionAllowed(false);
        listenerPropsTable.setRowSelectionAllowed(true);
        listenerPropsTable.setAutoCreateRowSorter(true);
        listenerPropsTable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listenerPropsTable.getSelectionModel().addListSelectionListener(this);
        home.addTab("Listeners", new JScrollPane(listenerPropsTable));
        GridBagConstraints c = new GridBagConstraints();
        c.weightx = 1d;
        c.weighty = 1d;
        c.fill = GridBagConstraints.BOTH;
        this.add(home, c);
    }

    private void refresh() {
        generalPropsTable.setModel(Util.buildGeneralPropertiesTableModel(leftComponent, rightComponent));
        listenerPropsTable.setModel(Util.buildListenerTableModel(leftComponent, rightComponent));
    }

    public void setDisplayedComponents(GUIElement leftComponent, GUIElement rightComponent) {
        this.leftComponent = leftComponent;
        this.rightComponent = rightComponent;
        screenshotsPanel.setComponents(leftComponent, rightComponent);
        refresh();
    }

    @Override
    public String getName() {
        return "Widget Information";
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (e.getValueIsAdjusting() == true) {
            return;
        }
        JTable source = null;
        if (e.getSource() == generalPropsTable.getSelectionModel()) {
            source = generalPropsTable;
        }
        if (e.getSource() == listenerPropsTable.getSelectionModel()) {
            source = listenerPropsTable;
        }
        if (source == null) {
            throw new NotImplementedException();
        }
        int row = source.getSelectedRow();
        if (row > -1) {
            firePropertySelectionEvent((String) source.getModel().getValueAt(row, 0), (String) source.getModel().getValueAt(row, 1));
        } else {
            firePropertySelectionEvent(null, null);
        }
    }

    public void addPropertySelectionListener(PropertySelectionListener l) {
        listenerList.add(PropertySelectionListener.class, l);
    }

    public void removePropertySelectionListener(PropertySelectionListener l) {
        listenerList.remove(PropertySelectionListener.class, l);
    }

    protected void firePropertySelectionEvent(String propertyName, String propertyValue) {
        Object[] listeners = listenerList.getListenerList();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == PropertySelectionListener.class) {
                psEvent = new PropertySelectionEvent(propertyName, propertyValue, this);
                ((PropertySelectionListener) listeners[i + 1]).valueChanged(psEvent);
            }
        }
    }
}

class PropertiesTableCellRenderer extends DefaultTableCellRenderer {

    private static Color CHANGED_ROW_BACKGROUND = new Color(245, 205, 205);

    private static Color DEFAULT_ROW_BACKGROUND = Color.WHITE;

    public PropertiesTableCellRenderer() {
        setOpaque(true);
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        Object rowVal = table.getModel().getValueAt(row, 1);
        String val = (rowVal != null ? rowVal.toString() : "");
        if (val.contains(" | ") == true) {
            setBackground(CHANGED_ROW_BACKGROUND);
        } else {
            setBackground(DEFAULT_ROW_BACKGROUND);
        }
        if (isSelected) {
            setBackground(table.getSelectionBackground());
        }
        return this;
    }
}
