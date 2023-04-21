package com.wozgonon.console;

import java.awt.BorderLayout;
import java.awt.Component;
import javax.swing.Action;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import com.wozgonon.WozgononConstants;
import com.wozgonon.docustrate.Description;
import com.wozgonon.docustrate.DesignPattern;
import com.wozgonon.docustrate.Feature;
import com.wozgonon.eventstore.PlugInRunnable;
import com.wozgonon.eventstore.PluginExecutor;
import com.wozgonon.eventstore.UseCaseManager;
import com.wozgonon.eventstore.UseCaseManager.ChangeEvent;
import com.wozgonon.plugin.multicast.MulticastEventSource;
import com.wozgonon.plugin.mxbean.SystemEventSource;
import com.wozgonon.plugin.random.RandomEventSource;
import com.wozgonon.plugin.rmi.RemoteEventSource;
import com.wozgonon.swing.CrossPlatformPopupMenu;
import com.wozgonon.swing.FrameWithBars;
import com.wozgonon.swing.Icons;
import com.wozgonon.swing.ScrollPaneWithTable;

/**
 * Provides a GUI allowing users to configure and manage event sources
 * 
 * <li>FIXME EventSourceManager be referenced independently  
 */
@Feature(value = "Aggregate remote events sources", description = "Tools and framework to aggregate different types of remote event sources.", status = "working")
@DesignPattern(url = "http://en.wikipedia.org/wiki/Model%E2%80%93view%E2%80%93controller", usage = "EventSourceBox provides a view on the underlying EventSourceModel.")
public class PlugInBox extends JPanel {

    private static final long serialVersionUID = 3091903669964738537L;

    public static final Description description = new Description("Event source plug in manager", 'M', Icons.getImageIcon("/images/plugin.png"), "Please choose a event source plug in or to monitor application use cases please create your own.");

    private final ScrollPaneWithTable scrollPane = new ScrollPaneWithTable();

    public PlugInBox(final Console console) {
        final FrameWithBars frame = console.frame;
        final CrossPlatformPopupMenu menu = new CrossPlatformPopupMenu();
        final JTable table = this.scrollPane.getTable();
        final JPanel buttonPane = new EventSourceButtonPanel(console);
        table.addMouseListener(menu);
        table.setAutoCreateRowSorter(true);
        table.setModel(model);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        super.setLayout(new BorderLayout());
        super.add(scrollPane, BorderLayout.CENTER);
        super.add(buttonPane, BorderLayout.EAST);
        UseCaseManager.current().addChangeListener(new UseCaseManager.ChangeListener() {

            @Override
            public void usecaseManagerChanged(ChangeEvent event) {
                switch(event) {
                    case BASE_DATA_CHANGED:
                    case PLUGIN_ADDED_OR_REMOVED:
                        model.fireTableStructureChanged();
                }
            }
        });
        menu.add(frame.new UserAction(new Description("Delete listener", 'D', Icons.getImageIcon("/images/table_row_delete.png"), "Delete currently selected listener ...")) {

            private static final long serialVersionUID = 1L;

            @Override
            protected Object actionPerformed() throws Exception {
                final int row = table.getSelectedRow();
                if (row >= 0 && row < getExecutor().size()) {
                    getExecutor().removeWithoutNotify(row);
                    model.fireTableRowsDeleted(row, row);
                    return "deleted";
                }
                return "nothing deleted";
            }
        });
    }

    private PluginExecutor getExecutor() {
        return UseCaseManager.current().getExecutor();
    }

    private final AbstractTableModel model = new AbstractTableModel() {

        private static final long serialVersionUID = 8024608283684869841L;

        public Class<?> getColumnClass(int column) {
            switch(column) {
                case 0:
                    return java.util.Date.class;
                default:
                    return String.class;
            }
        }

        public String getColumnName(int column) {
            final String[] columnNames = new String[] { "Created", "Source", "Class", "Parameters" };
            return columnNames[column];
        }

        @Override
        public int getColumnCount() {
            return 4;
        }

        @Override
        public int getRowCount() {
            return getExecutor().size();
        }

        @Override
        public Object getValueAt(int row, int column) {
            assert row >= 0;
            assert column >= 0;
            final PlugInRunnable source = getExecutor().get(row);
            switch(column) {
                case 0:
                    return source.getCreated();
                case 1:
                    return source;
                case 2:
                    return source.getClass().getName();
                case 3:
                    return source.getArgument();
                default:
                    return "";
            }
        }
    };
}

/**
 * Displays a user interface for selecting and creating event sources
 * <li>LIMITATION Only allows one argument to be entered on creating an event source.
 * <li>TODO Provide a tool to popup up a dialog box with multiple arguments for configuration
 * <li>TODO Provide a means to import external plugin classes - make it data driven provide a list of classes and use reflection to instanciate the classes
 */
class EventSourceButtonPanel extends JPanel {

    private static final long serialVersionUID = 928088063856697935L;

    EventSourceButtonPanel(final Console console) {
        final FrameWithBars frame = console.frame;
        final PluginExecutor manager = UseCaseManager.current().getExecutor();
        super.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        final Action simulator = frame.new UserAction(RandomEventSource.description) {

            private static final long serialVersionUID = 1L;

            @Override
            protected Object actionPerformed() throws Exception {
                return manager.newEventSource(RandomEventSource.class, null);
            }
        };
        final Action multicast = frame.new UserAction(MulticastEventSource.description) {

            private static final long serialVersionUID = 1L;

            protected Object actionPerformed() throws Exception {
                final String address = (String) JOptionPane.showInputDialog(frame, "Please enter IP multicast group and port in format 123.456.789.123:4567\n(Please note this implementation is not yet robust)", WozgononConstants.CONSOLE + " multicast", JOptionPane.OK_CANCEL_OPTION);
                if (address == null || address.length() == 0) {
                    return "No address given";
                }
                return manager.newEventSource(MulticastEventSource.class, address);
            }
        };
        final Action remote = frame.new UserAction(RemoteEventSource.description) {

            private static final long serialVersionUID = 1L;

            protected Object actionPerformed() throws Exception {
                final String address = (String) JOptionPane.showInputDialog(frame, "Please enter IP address and port in format 'localhost:1069'\n(Please note this implementation is not yet robust)", WozgononConstants.CONSOLE + " remote (RMI)", JOptionPane.OK_CANCEL_OPTION);
                if (address == null || address.length() == 0) {
                    return "No address given";
                }
                return manager.newEventSource(RemoteEventSource.class, address);
            }
        };
        final Action os = frame.new UserAction(SystemEventSource.description) {

            private static final long serialVersionUID = 1L;

            protected Object actionPerformed() throws Exception {
                return manager.newEventSource(SystemEventSource.class, null);
            }
        };
        class PlugInButton extends JButton {

            private static final long serialVersionUID = 1L;

            PlugInButton(Action action) {
                super(action);
                super.setAlignmentX(Component.LEFT_ALIGNMENT);
                EventSourceButtonPanel.this.add(this);
            }
        }
        ;
        new PlugInButton(simulator);
        new PlugInButton(multicast);
        new PlugInButton(remote);
        new PlugInButton(os);
        new PlugInButton(console.actions.eclipse);
    }
}
