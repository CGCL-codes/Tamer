package de.axa.smartfix.monitoring.gui;

import java.util.Observable;
import java.util.Observer;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import de.axa.smartfix.monitoring.gui.actions.AddCompositeMonitorAction;
import de.axa.smartfix.monitoring.model.MonitorManager;
import de.axa.smartfix.monitoring.model.provider.TreeMonitorContentProvider;
import de.axa.smartfix.monitoring.model.provider.TreeMonitorLabelProvider;

/**
 * @author tobias
 * 
 */
public class MonitorWindow extends ApplicationWindow implements Observer {

    public static String[] COLUMN_PROPERTIES = { "Name", "Anzahl", "Datum" };

    private TreeViewer treeView;

    private MonitorManager monitorManager;

    public MonitorWindow() {
        super(null);
        addMenuBar();
    }

    @Override
    protected Control createContents(Composite parent) {
        this.treeView = createTree(parent);
        return treeView.getTree();
    }

    @Override
    protected void configureShell(Shell shell) {
        super.configureShell(shell);
        shell.setText("smartFIX Monitor");
        shell.setBounds(0, 0, 640, 480);
    }

    @Override
    protected MenuManager createMenuManager() {
        MenuManager bar_menu = new MenuManager("");
        MenuManager file_menu = new MenuManager("&File");
        bar_menu.add(file_menu);
        file_menu.add(new AddCompositeMonitorAction(this, "Gruppe hinzufügen"));
        return bar_menu;
    }

    private TreeViewer createTree(Composite parent) {
        TreeViewer treeView = new TreeViewer(parent);
        Tree tree = treeView.getTree();
        tree.setHeaderVisible(true);
        String[] headers = COLUMN_PROPERTIES;
        int[] widths = { 200, 200, 200 };
        int[] alignments = { SWT.LEFT, SWT.RIGHT, SWT.LEFT };
        for (int i = 0; i < headers.length; ++i) {
            TreeColumn treeColumn = new TreeColumn(tree, alignments[i]);
            treeColumn.setText(headers[i]);
            treeColumn.setWidth(widths[i]);
        }
        treeView.setColumnProperties(COLUMN_PROPERTIES);
        treeView.setLabelProvider(new TreeMonitorLabelProvider());
        treeView.setContentProvider(new TreeMonitorContentProvider());
        treeView.setUseHashlookup(true);
        this.monitorManager = MonitorManager.createMonitorManager();
        treeView.setInput(this.monitorManager);
        this.monitorManager.addObserver(this);
        return treeView;
    }

    public static void main(String[] args) {
        MonitorWindow mw = new MonitorWindow();
        mw.setBlockOnOpen(true);
        mw.open();
        Display.getCurrent().dispose();
    }

    public void update(Observable o, Object arg) {
        this.treeView.update(arg, null);
    }

    public ISelection getTreeSelection() {
        return this.treeView.getSelection();
    }

    public MonitorManager getMonitorManager() {
        return monitorManager;
    }

    public TreeViewer getTreeView() {
        return treeView;
    }
}
