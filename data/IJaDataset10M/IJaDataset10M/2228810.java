package de.humanfork.treemerge.treeeditor.report;

import java.util.ArrayList;
import org.apache.log4j.Logger;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

public class ReportContentProvider implements IStructuredContentProvider {

    private static Logger logger = Logger.getLogger(ReportContentProvider.class);

    public ReportContentProvider() {
        super();
    }

    public Object[] getElements(Object inputElement) {
        ArrayList list = (ArrayList) inputElement;
        return list.toArray();
    }

    public void dispose() {
    }

    public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
        if (logger.isDebugEnabled()) logger.debug("Input changed: old=" + oldInput + ", new=" + newInput);
    }
}
