package org.mxeclipse.object.tree;

import java.util.Comparator;
import matrix.db.Context;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import com.matrixone.apps.domain.util.FrameworkException;
import org.mxeclipse.MxEclipsePlugin;
import org.mxeclipse.model.MxTableColumn;
import org.mxeclipse.model.MxTreeDomainObject;
import org.mxeclipse.utils.MxEclipseLogger;
import org.mxeclipse.utils.MxEclipseUtils;

/**
 * <p>Title: MxTreeObjectSorter</p>
 * <p>Description: TODO class description?</p>
 * <p>Company: ABB Switzerland</p>
 * @author chtiili
 * @version 1.0
 */
public class MxTreeObjectSorter extends ViewerSorter {

    private String columnName;

    public MxTreeObjectSorter(String columnName) {
        super();
        this.columnName = columnName;
    }

    @Override
    public int compare(Viewer viewer, Object e1, Object e2) {
        MxTreeDomainObject do1 = (MxTreeDomainObject) e1;
        MxTreeDomainObject do2 = (MxTreeDomainObject) e2;
        if (columnName.equals(MxTableColumn.BASIC_TYPE)) {
            return do1.getType().compareToIgnoreCase(do2.getType());
        } else if (columnName.equals(MxTableColumn.BASIC_NAME)) {
            return do1.getName().compareToIgnoreCase(do2.getName());
        } else if (columnName.equals(MxTableColumn.BASIC_REVISION)) {
            return do1.getRevision().compareToIgnoreCase(do2.getRevision());
        } else if (columnName.equals(MxTableColumn.BASIC_RELATIONSHIP)) {
            if (do1.getRelFromName() == null || do2.getRelToName() == null) return -1;
            boolean from1 = do1.getRelFromName().equals(do1.getName());
            boolean from2 = do2.getRelFromName().equals(do2.getName());
            if (from1 && !from2) {
                return 1;
            } else if (!from1 && from2) {
                return -1;
            } else {
                return do1.getRelType().compareToIgnoreCase(do2.getRelType());
            }
        } else {
            try {
                Context context = MxEclipsePlugin.getDefault().getContext();
                if (columnName.equals(MxTableColumn.BASIC_STATE)) {
                    return do1.getDomainObject().getInfo(context, "current").compareToIgnoreCase(do2.getDomainObject().getInfo(context, "current"));
                } else {
                }
            } catch (FrameworkException e) {
                MxEclipseLogger.getLogger().warning("Error in sorter, column " + columnName + ". Cause: " + e.getMessage());
            }
        }
        return -1;
    }
}
