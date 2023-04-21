package org.monet.modelling.ui.views.utils;

import java.util.Comparator;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.TreeColumn;

public class ServersViewSorter extends ViewerSorter {

    private class SortInfo {

        @SuppressWarnings("unused")
        int columnIndex;

        Comparator<Object> comparator;

        boolean descending;
    }

    private TreeViewer viewer;

    private SortInfo[] infos;

    public ServersViewSorter(TreeViewer viewer, TreeColumn[] columns, Comparator<Object>[] comparators) {
        this.viewer = viewer;
        infos = new SortInfo[columns.length];
        for (int index = 0; index < columns.length; index++) {
            infos[index] = new SortInfo();
            infos[index].columnIndex = index;
            infos[index].comparator = comparators[index];
            infos[index].descending = false;
            createSelectionListener(columns[index], infos[index]);
        }
    }

    public int compare(Viewer viewer, Object element1, Object element2) {
        for (int index = 0; index < infos.length; index++) {
            int result = infos[index].comparator.compare(element1, element2);
            if (result != 0) {
                if (infos[index].descending) return -result; else return result;
            }
        }
        return 0;
    }

    private void createSelectionListener(final TreeColumn columns, final SortInfo info) {
        columns.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                sortUsing(info);
            }
        });
    }

    protected void sortUsing(SortInfo info) {
        if (info == infos[0]) info.descending = !info.descending; else {
            for (int index = 0; index < infos.length; index++) {
                if (info == infos[index]) {
                    System.arraycopy(infos, 0, infos, 1, index);
                    infos[0] = info;
                    info.descending = false;
                    break;
                }
            }
        }
        viewer.refresh();
    }
}
