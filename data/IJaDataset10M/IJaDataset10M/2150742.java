package net.sourceforge.eclipsesyslog.views;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.PreferenceDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.part.ViewPart;
import org.osgi.framework.Bundle;

/**
 * This sample class demonstrates how to plug-in a new workbench view. The view
 * shows messages obtained from the model. The sample creates a dummy model on the
 * fly, but a real implementation would connect to the model available either in
 * this or another plug-in (e.g. the workspace). The view is connected to the
 * model using a content provider.
 * <p>
 * The view uses a label provider to define how model objects should be
 * presented in the view. Each view can present the same model objects using
 * different labels and icons, if needed. Alternatively, a single label provider
 * can be shared between views in order to ensure that objects of the same type
 * are presented in the same way everywhere.
 * <p>
 */
public class SyslogView extends ViewPart {

    @Override
    public void dispose() {
        thread.setFinishThread(true);
        super.dispose();
    }

    private TableViewer viewer;

    private Action action1;

    private Action action2;

    private Action action3;

    private Action action4;

    private Action doubleClickAction;

    private Collection<SyslogMessage> messages;

    private int ignoredMessages;

    private Label label;

    private int receivedMessages;

    private SyslogThread thread;

    private Collection<Filter> filters;

    private Logfile logfileforsyslogmsgs;

    private Action action5;

    class ViewContentProvider implements IStructuredContentProvider {

        public void inputChanged(Viewer v, Object oldInput, Object newInput) {
        }

        public void dispose() {
        }

        public Object[] getElements(Object parent) {
            return messages.toArray();
        }
    }

    class ViewLabelProvider extends LabelProvider implements ITableLabelProvider {

        public String getColumnText(Object obj, int index) {
            SyslogMessage msg = (SyslogMessage) obj;
            String columnName = viewer.getTable().getColumn(index).getText();
            if (columnName.equals("Facility")) return String.valueOf(msg.getFacility()); else if (columnName.equals("Severity")) return String.valueOf(msg.getSeverity()); else if (columnName.equals("Timestamp")) return msg.getTimestamp().toString(); else if (columnName.equals("Hostname")) return msg.getHostname(); else if (columnName.equals("Tag")) return msg.getTag(); else if (columnName.equals("Content")) return msg.getContent(); else if (columnName.equals("Number")) return String.valueOf(msg.getNumber()); else if (columnName.equals("ReceiveDate")) return msg.getReceivedDate().toString(); else if (columnName.equals("Address")) return msg.getAddress().getHostAddress(); else if (columnName.equals("Port")) return String.valueOf(msg.getPort()); else if (columnName.equals("ComputerHostname")) return msg.getAddress().getHostAddress(); else return "";
        }

        public Image getColumnImage(Object obj, int index) {
            return null;
        }

        public Image getImage(Object obj) {
            return PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_ELEMENT);
        }
    }

    class NameSorter extends ViewerSorter {

        @Override
        public int compare(Viewer viewer, Object e1, Object e2) {
            SyslogMessage msg1;
            SyslogMessage msg2;
            TableViewer tViewer = (TableViewer) viewer;
            if (tViewer.getTable().getSortDirection() == SWT.DOWN) {
                msg1 = (SyslogMessage) e1;
                msg2 = (SyslogMessage) e2;
            } else {
                msg2 = (SyslogMessage) e1;
                msg1 = (SyslogMessage) e2;
            }
            String columnName = tViewer.getTable().getSortColumn().getText();
            if (columnName.equals("Facility")) return String.valueOf(msg1.getFacility()).compareTo(String.valueOf(msg2.getFacility())); else if (columnName.equals("Severity")) return String.valueOf(msg1.getSeverity()).compareTo(String.valueOf(msg2.getSeverity())); else if (columnName.equals("Timestamp")) return msg1.getTimestamp().compareTo(msg2.getTimestamp()); else if (columnName.equals("Hostname")) return msg1.getHostname().compareTo(msg2.getHostname()); else if (columnName.equals("Tag")) return msg1.getTag().compareTo(msg2.getTag()); else if (columnName.equals("Content")) return msg1.getContent().compareTo(msg2.getContent()); else if (columnName.equals("Number")) return (msg1.getNumber() > msg2.getNumber()) ? 1 : -1; else if (columnName.equals("ReceiveDate")) return msg1.getReceivedDate().compareTo(msg2.getReceivedDate()); else if (columnName.equals("Address")) return msg1.getAddress().getHostAddress().compareTo(msg2.getAddress().getHostAddress()); else if (columnName.equals("Port")) return (msg1.getPort() > msg2.getPort()) ? 1 : -1; else if (columnName.equals("ComputerHostname")) return msg1.getAddress().getCanonicalHostName().compareTo(msg2.getAddress().getCanonicalHostName()); else return 0;
        }
    }

    /**
	 * The constructor.
	 */
    public SyslogView() {
        this.messages = new LinkedList<SyslogMessage>();
        this.ignoredMessages = 0;
        this.receivedMessages = 0;
    }

    /**
	 * This is a callback that will allow us to create the viewer and initialize
	 * it.
	 */
    public void createPartControl(Composite parent) {
        {
            this.logfileforsyslogmsgs = new Logfile();
            this.logfileforsyslogmsgs.openFile(net.sourceforge.eclipsesyslog.Activator.getDefault().getPreferenceStore().getString("File"));
            String[] messages = this.logfileforsyslogmsgs.readSavedMessages();
            for (String msg : messages) {
                InetAddress dummy = null;
                try {
                    dummy = InetAddress.getByName("0.0.0.0");
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
                this.insertNewMessage(msg, dummy, 0);
            }
        }
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 1;
        gridLayout.marginHeight = 0;
        gridLayout.marginWidth = 0;
        parent.setLayout(gridLayout);
        {
            label = new Label(parent, SWT.NONE);
            GridData data = new GridData(SWT.H_SCROLL | SWT.V_SCROLL);
            data.horizontalAlignment = GridData.FILL;
            label.setLayoutData(data);
        }
        viewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION);
        {
            SelectionListener ColumnSelectionListener = new SelectionListener() {

                @Override
                public void widgetDefaultSelected(SelectionEvent e) {
                }

                @Override
                public void widgetSelected(SelectionEvent e) {
                    TableColumn column = (TableColumn) e.getSource();
                    for (int i = 0; i < viewer.getTable().getColumnCount(); ++i) {
                        if (column == viewer.getTable().getColumn(i)) {
                            if (viewer.getTable().getColumn(i) == viewer.getTable().getSortColumn()) {
                                if (viewer.getTable().getSortDirection() == SWT.UP) viewer.getTable().setSortDirection(SWT.DOWN); else viewer.getTable().setSortDirection(SWT.UP);
                            }
                            viewer.getTable().setSortColumn(column);
                            viewer.refresh();
                            break;
                        }
                    }
                    return;
                }
            };
            Table table = viewer.getTable();
            TableColumn column = new TableColumn(table, SWT.LEFT);
            column.setText("Facility");
            column.setWidth(100);
            column.addSelectionListener(ColumnSelectionListener);
            TableColumn column2 = new TableColumn(table, SWT.LEFT);
            column2.setText("Severity");
            column2.setWidth(100);
            column2.addSelectionListener(ColumnSelectionListener);
            TableColumn column3 = new TableColumn(table, SWT.LEFT);
            column3.setText("Timestamp");
            column3.setWidth(100);
            column3.addSelectionListener(ColumnSelectionListener);
            TableColumn column4 = new TableColumn(table, SWT.LEFT);
            column4.setText("Hostname");
            column4.setWidth(100);
            column4.addSelectionListener(ColumnSelectionListener);
            TableColumn column5 = new TableColumn(table, SWT.LEFT);
            column5.setText("Tag");
            column5.setWidth(100);
            column5.addSelectionListener(ColumnSelectionListener);
            TableColumn column6 = new TableColumn(table, SWT.LEFT);
            column6.setText("Content");
            column6.setWidth(100);
            column6.addSelectionListener(ColumnSelectionListener);
            TableColumn column7 = new TableColumn(table, SWT.LEFT);
            column7.setText("Number");
            column7.setWidth(100);
            column7.addSelectionListener(ColumnSelectionListener);
            TableColumn column8 = new TableColumn(table, SWT.LEFT);
            column8.setText("ReceiveDate");
            column8.setWidth(100);
            column8.addSelectionListener(ColumnSelectionListener);
            TableColumn column9 = new TableColumn(table, SWT.LEFT);
            column9.setText("Address");
            column9.setWidth(100);
            column9.addSelectionListener(ColumnSelectionListener);
            TableColumn column10 = new TableColumn(table, SWT.LEFT);
            column10.setText("Port");
            column10.setWidth(100);
            column10.addSelectionListener(ColumnSelectionListener);
            TableColumn column11 = new TableColumn(table, SWT.LEFT);
            column11.setText("ComputerHostname");
            column11.setWidth(100);
            column11.addSelectionListener(ColumnSelectionListener);
            table.setHeaderVisible(true);
            table.setLinesVisible(true);
            table.setSortColumn(column3);
            table.setSortDirection(SWT.DOWN);
            String columnOrder = net.sourceforge.eclipsesyslog.Activator.getDefault().getPreferenceStore().getString("ColumnOrder");
            updateColumnsOrderFromProperty(columnOrder);
            String columnVisibility = net.sourceforge.eclipsesyslog.Activator.getDefault().getPreferenceStore().getString("ColumnVisible");
            updateColumnVisibilityFromProperty(columnVisibility);
        }
        viewer.setContentProvider(new ViewContentProvider());
        viewer.setLabelProvider(new ViewLabelProvider());
        viewer.setSorter(new NameSorter());
        viewer.setInput(getViewSite());
        filters = new LinkedList<Filter>();
        filters.add(new Filter(false, "Facility", net.sourceforge.eclipsesyslog.Activator.getDefault().getPreferenceStore().getString("Filter_" + "Facility")));
        filters.add(new Filter(false, "Severity", net.sourceforge.eclipsesyslog.Activator.getDefault().getPreferenceStore().getString("Filter_" + "Severity")));
        filters.add(new Filter(false, "Timestamp", net.sourceforge.eclipsesyslog.Activator.getDefault().getPreferenceStore().getString("Filter_" + "Timestamp")));
        filters.add(new Filter(false, "Hostname", net.sourceforge.eclipsesyslog.Activator.getDefault().getPreferenceStore().getString("Filter_" + "Hostname")));
        filters.add(new Filter(false, "Tag", net.sourceforge.eclipsesyslog.Activator.getDefault().getPreferenceStore().getString("Filter_" + "Tag")));
        filters.add(new Filter(false, "Content", net.sourceforge.eclipsesyslog.Activator.getDefault().getPreferenceStore().getString("Filter_" + "Content")));
        filters.add(new Filter(false, "Number", net.sourceforge.eclipsesyslog.Activator.getDefault().getPreferenceStore().getString("Filter_" + "Number")));
        filters.add(new Filter(false, "ReceiveDate", net.sourceforge.eclipsesyslog.Activator.getDefault().getPreferenceStore().getString("Filter_" + "ReceiveDate")));
        filters.add(new Filter(false, "Address", net.sourceforge.eclipsesyslog.Activator.getDefault().getPreferenceStore().getString("Filter_" + "Address")));
        filters.add(new Filter(false, "Port", net.sourceforge.eclipsesyslog.Activator.getDefault().getPreferenceStore().getString("Filter_" + "Port")));
        filters.add(new Filter(false, "ComputerHostname", net.sourceforge.eclipsesyslog.Activator.getDefault().getPreferenceStore().getString("Filter_" + "ComputerHostname")));
        String activeFilters = net.sourceforge.eclipsesyslog.Activator.getDefault().getPreferenceStore().getString("ActiveFilters");
        setActiveFilters(activeFilters);
        viewer.setFilters(new ViewerFilter[] { new ViewerFilter() {

            @Override
            public boolean select(Viewer viewer, Object parentElement, Object element) {
                try {
                    for (Iterator<Filter> iterator = filters.iterator(); iterator.hasNext(); ) {
                        Filter filter = (Filter) iterator.next();
                        SyslogMessage msg = ((SyslogMessage) element);
                        if (!filter.isActive()) continue;
                        Pattern p = Pattern.compile(filter.getFilter());
                        if (filter.getColumn().equals("Facility")) {
                            Matcher m = p.matcher(String.valueOf(msg.getFacility()));
                            if (!m.find()) return false;
                        } else if (filter.getColumn().equals("Severity")) {
                            Matcher m = p.matcher(String.valueOf(msg.getSeverity()));
                            if (!m.find()) return false;
                        } else if (filter.getColumn().equals("Timestamp")) {
                            Matcher m = p.matcher(msg.getTimestamp());
                            if (!m.find()) return false;
                        } else if (filter.getColumn().equals("Hostname")) {
                            Matcher m = p.matcher(msg.getHostname());
                            if (!m.find()) return false;
                        } else if (filter.getColumn().equals("Tag")) {
                            Matcher m = p.matcher(msg.getTag());
                            if (!m.find()) return false;
                        } else if (filter.getColumn().equals("Content")) {
                            Matcher m = p.matcher(msg.getContent());
                            if (!m.find()) return false;
                        } else if (filter.getColumn().equals("Number")) {
                            Matcher m = p.matcher(String.valueOf(msg.getNumber()));
                            if (!m.find()) return false;
                        } else if (filter.getColumn().equals("ReceiveDate")) {
                            Matcher m = p.matcher(msg.getReceivedDate().toString());
                            if (!m.find()) return false;
                        } else if (filter.getColumn().equals("Address")) {
                            Matcher m = p.matcher(msg.getAddress().getHostAddress());
                            if (!m.find()) return false;
                        } else if (filter.getColumn().equals("Port")) {
                            Matcher m = p.matcher(String.valueOf(msg.getPort()));
                            if (!m.find()) return false;
                        } else if (filter.getColumn().equals("ComputerHostname")) {
                            Matcher m = p.matcher(msg.getAddress().getCanonicalHostName());
                            if (!m.find()) return false;
                        }
                    }
                } catch (Exception e) {
                    return true;
                }
                return true;
            }
        } });
        makeActions();
        hookContextMenu();
        hookDoubleClickAction();
        contributeToActionBars();
        getViewSite().getActionBars().setGlobalActionHandler(ActionFactory.COPY.getId(), action4);
        getViewSite().getActionBars().setGlobalActionHandler(ActionFactory.SELECT_ALL.getId(), action5);
        getViewSite().setSelectionProvider(viewer);
        {
            thread = new SyslogThread() {

                private boolean finishThread = false;

                public void run() {
                    this.setName("SyslogCollector");
                    try {
                        java.net.DatagramSocket sock;
                        sock = new java.net.DatagramSocket(514);
                        sock.setSoTimeout(500);
                        while (true) {
                            byte[] data = new byte[1024];
                            DatagramPacket packet = new DatagramPacket(data, data.length);
                            try {
                                sock.receive(packet);
                            } catch (SocketTimeoutException e) {
                                if (getFinishThread()) {
                                    return;
                                }
                                continue;
                            }
                            byte[] strippedData = new byte[packet.getLength()];
                            System.arraycopy(data, 0, strippedData, 0, strippedData.length);
                            String message = new String(strippedData);
                            insertNewMessage(message, packet.getAddress(), packet.getPort());
                            logfileforsyslogmsgs.writeSyslogMessage(message);
                        }
                    } catch (SocketException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                public synchronized boolean getFinishThread() {
                    return finishThread;
                }

                public synchronized void setFinishThread(boolean finish) {
                    this.finishThread = finish;
                }
            };
            thread.start();
        }
        {
            net.sourceforge.eclipsesyslog.Activator.getDefault().getPreferenceStore().addPropertyChangeListener(new IPropertyChangeListener() {

                @Override
                public void propertyChange(PropertyChangeEvent event) {
                    if (0 == event.getProperty().compareTo("ColumnOrder")) {
                        String newValue = (String) event.getNewValue();
                        updateColumnsOrderFromProperty(newValue);
                    } else if (0 == event.getProperty().compareTo("ColumnVisible")) {
                        String newValue = (String) event.getNewValue();
                        updateColumnVisibilityFromProperty(newValue);
                    } else if (event.getProperty().contains("Filter_")) {
                        for (Filter filter : filters) {
                            if (("Filter_" + filter.getColumn()).equals(event.getProperty())) {
                                filter.setFilter((String) event.getNewValue());
                            }
                        }
                        viewer.refresh();
                        updateStatusLabel();
                    } else if (event.getProperty().equals("ActiveFilters")) {
                        String activeFilters = (String) event.getNewValue();
                        setActiveFilters(activeFilters);
                        viewer.refresh();
                    } else if (event.getProperty().equals("File")) {
                        logfileforsyslogmsgs.openFile((String) event.getNewValue());
                    }
                    updateStatusLabel();
                    return;
                }
            });
        }
        {
            GridData data = new GridData(SWT.H_SCROLL | SWT.V_SCROLL);
            data.horizontalAlignment = GridData.FILL;
            data.verticalAlignment = GridData.FILL;
            data.grabExcessVerticalSpace = true;
            viewer.getTable().setLayoutData(data);
        }
        {
            updateStatusLabel();
        }
    }

    private void setActiveFilters(String activeFilters) {
        for (Iterator<Filter> iterator = filters.iterator(); iterator.hasNext(); ) {
            Filter filter = (Filter) iterator.next();
            filter.setActive(false);
        }
        String[] activeFilterArray = activeFilters.split("\\|");
        for (Iterator<Filter> iterator = filters.iterator(); iterator.hasNext(); ) {
            Filter filter = (Filter) iterator.next();
            for (int j = 0; j < activeFilterArray.length; j++) {
                String string = activeFilterArray[j];
                if (filter.getColumn().equals(string)) {
                    filter.setActive(true);
                    break;
                }
            }
        }
    }

    private void updateColumnVisibilityFromProperty(String newValue) {
        if (0 == newValue.compareTo("")) {
            TableColumn[] cols = viewer.getTable().getColumns();
            for (int x = 0; x < cols.length; x++) {
                cols[x].setWidth(100);
            }
        } else {
            String[] checkedcols = newValue.split(",");
            TableColumn[] cols = viewer.getTable().getColumns();
            for (int i = 0; i < cols.length; i++) {
                cols[i].setWidth(0);
                for (int x = 0; x < checkedcols.length; x++) {
                    if (cols[i].getText().equals(checkedcols[x])) {
                        cols[i].setWidth(100);
                    }
                }
            }
        }
    }

    private void updateColumnsOrderFromProperty(String newValue) {
        if (0 == newValue.compareTo("")) {
            int[] order = new int[11];
            for (int i = 0; i < order.length; ++i) {
                order[i] = i;
            }
            viewer.getTable().setColumnOrder(order);
        } else {
            String[] values = newValue.split(",");
            int[] order = new int[11];
            int i = 0;
            for (String val : values) {
                if (val.equals("Facility")) order[i++] = 0; else if (val.equals("Severity")) order[i++] = 1; else if (val.equals("Timestamp")) order[i++] = 2; else if (val.equals("Hostname")) order[i++] = 3; else if (val.equals("Tag")) order[i++] = 4; else if (val.equals("Content")) order[i++] = 5; else if (val.equals("Number")) order[i++] = 6; else if (val.equals("ReceiveDate")) order[i++] = 7; else if (val.equals("Address")) order[i++] = 8; else if (val.equals("Port")) order[i++] = 9; else if (val.equals("ComputerHostname")) order[i++] = 10;
            }
            viewer.getTable().setColumnOrder(order);
        }
    }

    private void updateStatusLabel() {
        String filterNotice = "";
        for (Iterator<Filter> iterator = filters.iterator(); iterator.hasNext(); ) {
            Filter filter = (Filter) iterator.next();
            if (filter.isActive()) {
                filterNotice = ", filter is active";
                break;
            }
        }
        label.setText("received: " + this.receivedMessages + ", ignored: " + this.ignoredMessages + ", buffered: " + this.messages.size() + ", displayed: " + viewer.getTable().getItemCount() + filterNotice);
    }

    private void hookContextMenu() {
        MenuManager menuMgr = new MenuManager("#PopupMenu");
        menuMgr.setRemoveAllWhenShown(true);
        menuMgr.addMenuListener(new IMenuListener() {

            public void menuAboutToShow(IMenuManager manager) {
                SyslogView.this.fillContextMenu(manager);
            }
        });
        Menu menu = menuMgr.createContextMenu(viewer.getControl());
        viewer.getControl().setMenu(menu);
        getSite().registerContextMenu(menuMgr, viewer);
    }

    private void contributeToActionBars() {
        IActionBars bars = getViewSite().getActionBars();
        fillLocalPullDown(bars.getMenuManager());
        fillLocalToolBar(bars.getToolBarManager());
    }

    private void fillLocalPullDown(IMenuManager manager) {
        manager.add(action4);
        manager.add(action2);
        manager.add(new Separator());
        manager.add(action1);
        manager.add(action3);
    }

    private void fillContextMenu(IMenuManager manager) {
        manager.add(action4);
        manager.add(action2);
        manager.add(new Separator());
        manager.add(action1);
        manager.add(action3);
        manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
    }

    private void fillLocalToolBar(IToolBarManager manager) {
        manager.add(action4);
        manager.add(action2);
        manager.add(new Separator());
        manager.add(action1);
        manager.add(action3);
    }

    private void makeActions() {
        action1 = new Action() {

            public void run() {
                PreferenceDialog diag = org.eclipse.ui.dialogs.PreferencesUtil.createPreferenceDialogOn(null, "net.sourceforge.eclipsesyslog.preferences.SyslogPreferencePage", new String[] { "Syslog" }, null);
                diag.open();
            }
        };
        action1.setText("Preferences");
        action1.setToolTipText("Open preference page");
        action1.setImageDescriptor(getImageDescriptor("properties.gif"));
        action2 = new Action() {

            public void run() {
                messages.clear();
                viewer.refresh();
                updateStatusLabel();
                logfileforsyslogmsgs.cleanFile();
            }
        };
        action2.setText("Clean");
        action2.setToolTipText("Delete all messages");
        action2.setImageDescriptor(getImageDescriptor("clear.gif"));
        doubleClickAction = new Action() {

            public void run() {
                try {
                    PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(org.eclipse.ui.IPageLayout.ID_PROP_SHEET);
                } catch (PartInitException e) {
                    e.printStackTrace();
                }
            }
        };
        action3 = new Action() {

            public void run() {
                PreferenceDialog diag = org.eclipse.ui.dialogs.PreferencesUtil.createPreferenceDialogOn(null, "net.sourceforge.eclipsesyslog.filter.preferences.SyslogFiltersPreferencePage", new String[] { "Filters" }, null);
                diag.open();
            }
        };
        action3.setText("Filters");
        action3.setToolTipText("Open preference page for the filters");
        action3.setImageDescriptor(getImageDescriptor("filter_history.gif"));
        action4 = new Action() {

            private Clipboard cb;

            public void run() {
                cb = new Clipboard(Display.getDefault());
                String val_to_copy = "";
                TableItem[] items = viewer.getTable().getSelection();
                for (int i = 0; i < items.length; i++) {
                    val_to_copy += ((SyslogMessage) items[i].getData()).getMsg();
                    val_to_copy += "\r\n";
                }
                TextTransfer textTransfer = TextTransfer.getInstance();
                cb.setContents(new Object[] { val_to_copy }, new Transfer[] { textTransfer });
            }
        };
        action4.setText("Copy");
        action4.setToolTipText("Copy selected colmuns");
        action4.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_TOOL_COPY));
        action5 = new Action() {

            public void run() {
                viewer.getTable().selectAll();
            }
        };
        action5.setText("Copy");
        action5.setToolTipText("Copy selected colmuns");
        action5.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_TOOL_COPY));
    }

    private static URL makeImageURL(String prefix, String name) {
        Bundle bundle = Platform.getBundle("net.sourceforge.eclipsesyslog");
        String location = bundle.getLocation();
        location = location.substring(location.indexOf("@") + 1, location.length());
        URL url = null;
        try {
            url = new URL("file:" + location + prefix + name);
        } catch (MalformedURLException e) {
            return null;
        }
        return url;
    }

    private ImageDescriptor getImageDescriptor(String relativePath) {
        String iconPath = "icons/";
        return ImageDescriptor.createFromURL(makeImageURL(iconPath, relativePath));
    }

    private void hookDoubleClickAction() {
        viewer.addDoubleClickListener(new IDoubleClickListener() {

            public void doubleClick(DoubleClickEvent event) {
                doubleClickAction.run();
            }
        });
    }

    private void showMessage(String message) {
        MessageDialog.openInformation(viewer.getControl().getShell(), "Eclipse Syslog Plug-in", message);
    }

    private void insertNewMessage(String message, InetAddress address, int port) {
        final Display display = Display.getDefault();
        SyslogMessage newMessage = null;
        receivedMessages++;
        Pattern p = Pattern.compile("<(\\d{1,3})>(Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec) (\\d{1,2}| \\d{1}) (\\d{1,2}):(\\d{1,2}):(\\d{1,2}) (.*?) (.*?) (.*)");
        Matcher m = p.matcher(message);
        if (m.find()) {
            int i = m.groupCount();
            if (i == 9) {
                newMessage = new SyslogMessage(Integer.valueOf(m.group(1)) / 8, Integer.valueOf(m.group(1)) % 8, m.group(2) + " " + m.group(3) + " " + m.group(4) + ":" + m.group(5) + ":" + m.group(6), m.group(7), m.group(8), m.group(9), address, port, message);
            }
        }
        if (newMessage == null) {
            ignoredMessages++;
            display.asyncExec(new Runnable() {

                public void run() {
                    updateStatusLabel();
                }
            });
        } else {
            final SyslogMessage newMessageForRunnableThread = newMessage;
            display.asyncExec(new Runnable() {

                public void run() {
                    try {
                        messages.add(newMessageForRunnableThread);
                        viewer.add(newMessageForRunnableThread);
                        updateStatusLabel();
                    } catch (Exception e) {
                        return;
                    }
                }
            });
        }
    }

    /**
	 * Passing the focus request to the viewer's control.
	 */
    public void setFocus() {
        viewer.getControl().setFocus();
    }
}
