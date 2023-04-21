package gui.table;

import filter.aspect.AspectFilter;
import filter.aspect.AspectFilterFavorites;
import gui.NavigatorSingleton;
import java.util.Observable;
import java.util.Observer;
import org.dom4j.Element;
import org.dom4j.Node;
import org.eclipse.jface.viewers.TableTreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import data.DataSourceSingleton;

/**
 * The tabletree part of main program window, showing the list of (possibly
 * filtered) attributes. Currently, contains a single Composite (analog of
 * JPanel for those who recover from Swing-caused brain damage), which holds
 * the single element - the tabletree itself. Hovewer, there's more to come!
 * <p>
 * The tabletree is a listener for events from Navigator (which means that
 * "currently selected object changed") and DataSource("currently open data
 * source changed"), and updates itself in a correct manner.
 * 
 * @author Roman Rudenko
 * @see gui.table.XMLTableTreeContentProvider
 * @see gui.table.XMLTableTreeLabelProvider
 */
public class TableTreePane implements Observer {

    private Composite self;

    private TableTreeViewer tableTreeViewer;

    private Node root;

    private TableTreeFilter filter;

    private XMLTableTreeContentProvider contentProvider;

    public Composite getSelf() {
        return self;
    }

    /**
	 * Creates the TableTreePane inside the specified Composite.
	 * 
	 * @param parent
	 *            The parent Composite
	 */
    public TableTreePane(Composite parent) {
        filter = new TableTreeFilter();
        self = new Composite(parent, SWT.NONE);
        self.setLayout(new FillLayout());
        NavigatorSingleton.getInstance().addObserver(this);
        DataSourceSingleton.getInstance().addObserver(this);
        tableTreeViewer = new TableTreeViewer(self, SWT.BORDER);
        Table table = tableTreeViewer.getTableTree().getTable();
        table.setLinesVisible(true);
        table.setHeaderVisible(true);
        TableColumn column1 = new TableColumn(table, SWT.BORDER);
        TableColumn column2 = new TableColumn(table, SWT.BORDER);
        column1.setText("Attribute");
        column2.setText("Value");
        column1.setWidth(100);
        column2.setWidth(275);
        tableTreeViewer.setLabelProvider(new XMLTableTreeLabelProvider());
        contentProvider = new XMLTableTreeContentProvider();
        tableTreeViewer.setContentProvider(contentProvider);
        self.layout();
        root = null;
    }

    public void update(Observable o, Object arg) {
        if (o == NavigatorSingleton.getInstance()) {
            if (arg instanceof Element) {
                root = ((Element) arg).selectSingleNode("*[name()='metadata']/*[name()='lom']");
                tableTreeViewer.setInput(root);
                tableTreeViewer.expandToLevel(2);
            }
        } else if (o == DataSourceSingleton.getInstance()) tableTreeViewer.setInput(null); else if (o instanceof AspectFilterFavorites) {
            filter.setFilter((AspectFilter) arg);
            tableTreeViewer.resetFilters();
            tableTreeViewer.addFilter(filter);
        }
    }
}
