package org.nexopenframework.ide.eclipse.jee.viewers;

import java.util.ArrayList;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.ColumnLayoutData;
import org.eclipse.jface.viewers.ColumnPixelData;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.IWorkbenchPartSite;
import org.nexopenframework.ide.eclipse.jee.JeeServiceComponentUIPlugin;
import org.nexopenframework.ide.eclipse.jee.model.IJeeLifecyle;

/**
 * <p>NexOpen Framework</p>
 * 
 * <p>Extension of a {@link TableViewer} for dealing with process defintions</p>
 * 
 * @see TableViewer
 * @author Francesc Xavier Magdaleno
 * @version 1.0
 * @since 1.0
 */
public class JeeLifecycleViewer extends TableViewer {

    /***/
    private final String columnHeaders[] = { "Name", "Operation" };

    private final ColumnLayoutData columnLayouts[] = { new ColumnPixelData(100, true, true), new ColumnWeightData(100) };

    public JeeLifecycleViewer(Composite parent, IStructuredContentProvider contentProvider) {
        super(createTable(parent));
        createColumns();
        setContentProvider(contentProvider);
        setLabelProvider(new ProcessDefinitionProvider());
        setInput(new ArrayList());
    }

    /**
	 * Creates the table control.
	 */
    private static Table createTable(Composite parent) {
        Table t = new Table(parent, SWT.H_SCROLL | SWT.V_SCROLL);
        GridData gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.heightHint = 80;
        gd.widthHint = 200;
        t.setLayoutData(gd);
        t.setLinesVisible(true);
        return t;
    }

    private void createColumns() {
        TableLayout layout = new TableLayout();
        getTable().setLayout(layout);
        getTable().setHeaderVisible(true);
        for (int i = 0; i < columnHeaders.length; i++) {
            layout.addColumnData(columnLayouts[i]);
            TableColumn tc = new TableColumn(getTable(), SWT.NONE, i);
            tc.setResizable(columnLayouts[i].resizable);
            tc.setText(columnHeaders[i]);
        }
    }

    /**
	 * Attaches a contextmenu listener to the tree
	 */
    void initContextMenu(IMenuListener menuListener, String popupId, IWorkbenchPartSite viewSite) {
        MenuManager menuMgr = new MenuManager();
        menuMgr.setRemoveAllWhenShown(true);
        menuMgr.addMenuListener(menuListener);
        Menu menu = menuMgr.createContextMenu(getControl());
        getControl().setMenu(menu);
        viewSite.registerContextMenu(popupId, menuMgr, this);
    }

    /**
	 * 
	 */
    void clearViewer() {
        setInput("");
    }

    class ProcessDefinitionProvider extends LabelProvider implements ITableLabelProvider {

        public Image getColumnImage(Object element, int columnIndex) {
            if (columnIndex == 0) {
                return JeeServiceComponentUIPlugin.getImageDescriptor("icons/jar_l_obj.gif").createImage();
            }
            return null;
        }

        public String getColumnText(Object element, int columnIndex) {
            if (element instanceof IJeeLifecyle && columnIndex == 0) {
                return ((IJeeLifecyle) element).getName();
            } else if (element instanceof IJeeLifecyle && columnIndex == 1) {
                return ((IJeeLifecyle) element).getOperation();
            }
            return element.toString();
        }
    }
}
