package org.deft.format;

import java.util.LinkedList;
import java.util.List;
import org.deft.QueryCreator;
import org.deft.helper.TreeViewerHelper;
import org.deft.repository.ast.annotation.Format;
import org.deft.transform.OutlineNode;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.TreeItem;

public class FormatFactory {

    /**
	 * Creates a format object with data from the FragmentFormatDialog
	 * 
	 * @param name
	 * @param displayType
	 * @param treeViewer
	 * @return
	 */
    public static Format createFormat(String name, String[] displayType, TreeViewer treeViewer) {
        if (name == null) return null;
        if (displayType == null || displayType.length == 0) return null;
        if (treeViewer == null) return null;
        Format format = new Format(name);
        format.addDisplayList(displayType);
        addHideReplacements(format, treeViewer);
        return format;
    }

    private static void addHideReplacements(Format format, TreeViewer treeViewer) {
        QueryCreator qc = QueryCreator.getInstance();
        for (TreeItem ti : treeViewer.getTree().getItems()) {
            TreeItem target = ti;
            String targetPath = qc.getXPath((OutlineNode) ti.getData());
            if (ti.getChecked()) {
                List<TreeItem> uncheckElements = TreeViewerHelper.getUncheckedElements(treeViewer, ti);
                for (TreeItem item : uncheckElements) {
                    String xpath = qc.getRelativeXPath((OutlineNode) item.getData(), (OutlineNode) target.getData());
                    String replace = item.getText(1);
                    if (replace.length() == 0) replace = null;
                    List<String> lHide = new LinkedList<String>();
                    lHide.add(xpath);
                    format.addReplaceAll(targetPath, lHide, replace);
                }
            } else {
                String replace = ti.getText(1);
                if (replace.length() == 0) replace = null;
                List<String> lHide = new LinkedList<String>();
                lHide.add(".");
                format.addReplaceAll(targetPath, lHide, replace);
            }
        }
    }
}
