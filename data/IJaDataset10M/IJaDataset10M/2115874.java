package com.ivis.xprocess.ui.tables.projectavailability;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import com.ivis.xprocess.ui.datawrappers.OrganizationWrapper;
import com.ivis.xprocess.ui.editors.ElementEditor;
import com.ivis.xprocess.ui.menu.MenuUtil;
import com.ivis.xprocess.ui.preferences.managers.DatePreferenceChangeManager;
import com.ivis.xprocess.ui.preferences.managers.IPreferencePageListener;
import com.ivis.xprocess.ui.preferences.managers.PreferenceChangeManager.ChangedPreference;
import com.ivis.xprocess.ui.refresh.ChangeRecord;
import com.ivis.xprocess.ui.refresh.ChangeEventFactory.ChangeEvent;
import com.ivis.xprocess.ui.tables.columns.ProjectAvailabilityColumnManager;
import com.ivis.xprocess.ui.tables.columns.definition.XProcessColumn;
import com.ivis.xprocess.ui.tables.viewmanagers.TableViewManager;
import com.ivis.xprocess.ui.util.ViewUtil;
import com.ivis.xprocess.ui.util.XProcessTableViewer;

public class ProjectAvailabilityTreeViewManager extends TableViewManager implements IPreferencePageListener {

    private int column = -1;

    private int row = -1;

    public ProjectAvailabilityTreeViewManager(Composite parent, ProjectAvailabilityColumnManager columnManager, ElementEditor editor) {
        super(parent, columnManager, editor);
        viewer.setSorter(null);
        addPropertyListeners();
        viewer.getTable().addMouseListener(new MouseListener() {

            public void mouseDoubleClick(MouseEvent e) {
            }

            public void mouseDown(MouseEvent e) {
                Table table = viewer.getTable();
                Rectangle clientArea = table.getClientArea();
                Point point = new Point(e.x, e.y);
                int index = 0;
                column = -1;
                row = -1;
                while (index < table.getItemCount()) {
                    boolean visible = false;
                    TableItem item = table.getItem(index);
                    for (int i = 0; i < table.getColumns().length; i++) {
                        Rectangle rect = item.getBounds(i);
                        if (rect.contains(point)) {
                            column = i;
                            row = index;
                            break;
                        }
                        if (!visible && rect.intersects(clientArea)) {
                            visible = true;
                        }
                    }
                    index++;
                }
                if ((row >= 0) && ((column > 1) && (column < 9))) {
                    inplaceTableEditor.cellEditor(viewer.getTable().getItem(row), column);
                }
            }

            public void mouseUp(MouseEvent e) {
            }
        });
    }

    @Override
    protected void createView(Composite parent) {
        viewer = new XProcessTableViewer(parent, SWT.MULTI | SWT.FULL_SELECTION);
    }

    @Override
    public void resize() {
        if (viewer.getTable().isDisposed()) {
            return;
        }
        int width = this.getTotalWidth();
        int height = this.getTotalHeight();
        viewer.getTable().setSize(width, height);
        viewer.getTable().layout(true, true);
    }

    public int getTotalWidth() {
        int width = 0;
        for (int i = 0; i < columnManager.getDisplayedColumns().length; i++) {
            XProcessColumn xProcessColumn = columnManager.getDisplayedColumns()[i];
            width += xProcessColumn.getWidth();
        }
        return width + 1;
    }

    public int getTotalHeight() {
        int height = viewer.getTable().getHeaderHeight();
        for (int i = 0; i < viewer.getTable().getItemCount(); i++) {
            height += viewer.getTable().getHeaderHeight();
        }
        return height;
    }

    @Override
    public boolean refreshEvent(ChangeRecord changeRecord) {
        if (viewer.getTable().isDisposed()) {
            return false;
        }
        boolean shouldRefresh = false;
        if (changeRecord.hasChange(ChangeEvent.VCS_UPDATE)) {
            shouldRefresh = true;
        }
        if (changeRecord.hasChange(ChangeEvent.FIELDS_CHANGED) && changeRecord.hasProperty("modifyingavailability")) {
            shouldRefresh = true;
        }
        if (changeRecord.hasChange(ChangeEvent.ELEMENT_DELETED) && (changeRecord.getChangedObject() instanceof OrganizationWrapper)) {
            shouldRefresh = true;
        }
        if (changeRecord.hasChange(ChangeEvent.NEW_ELEMENT) && (changeRecord.getChangedObject() instanceof OrganizationWrapper)) {
            shouldRefresh = true;
        }
        if (changeRecord.hasChange(ChangeEvent.FIELDS_CHANGED) && (changeRecord.getChangedObject() instanceof OrganizationWrapper)) {
            shouldRefresh = true;
        }
        if (shouldRefresh) {
            refreshOnChange();
        }
        return false;
    }

    private void refreshOnChange() {
        Display display = ViewUtil.getDisplay();
        display.asyncExec(new Runnable() {

            public void run() {
                if (viewer.getTable().isDisposed()) {
                    return;
                }
                viewer.refresh();
            }
        });
    }

    @Override
    protected void fillContextMenu(IMenuManager manager) {
        MenuUtil.createBasicMenuStructure(manager, viewer.getSelection());
    }

    @Override
    public void setInput(Object object) {
        super.setInput(object);
    }

    @Override
    public void saveText(Item item, String text, int column) {
        super.saveText(item, text, column);
        viewer.setSelection(null);
    }

    public Object[] getItems() {
        return new Object[] {};
    }

    @Override
    public void refresh() {
        super.refresh();
    }

    private void addPropertyListeners() {
        DatePreferenceChangeManager.addPreferencePageListener(this);
    }

    @Override
    public void dispose() {
        super.dispose();
        columnManager.dispose();
    }

    public void preferencePageChanged(ChangedPreference changePreference) {
        if (viewer.getTable().isDisposed()) {
            return;
        }
        viewer.refresh();
        return;
    }
}
