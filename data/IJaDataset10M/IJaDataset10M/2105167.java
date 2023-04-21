package ingenias.editor.actions;

import ingenias.editor.GUIResources;
import ingenias.editor.IDEState;
import java.awt.event.ActionEvent;
import java.util.Vector;
import javax.swing.tree.TreePath;

public class SearchAction {

    private IDEState ids;

    private GUIResources resources;

    protected Vector<TreePath> foundpaths = new Vector<TreePath>();

    protected int lastFoundIndex = 0;

    protected String lastSearch = "";

    public SearchAction(IDEState ids, GUIResources resources) {
        this.ids = ids;
        this.resources = resources;
    }

    public void searchActionPerformed(ActionEvent evt) {
        String id = resources.getSearchField().getText();
        locateAndScrollToObject(id);
    }

    private void locateAndScrollToObject(String id) {
        if (id.equals(lastSearch) && lastFoundIndex < foundpaths.size()) {
            TreePath tp = (TreePath) this.foundpaths.elementAt(lastFoundIndex);
            ids.om.arbolObjetos.expandPath(tp);
            ids.om.arbolObjetos.scrollPathToVisible(tp);
            ids.om.arbolObjetos.setSelectionPath(tp);
            lastFoundIndex++;
        } else {
            foundpaths = this.ids.om.findUserObjectPathRegexp(id + ".*");
            if (foundpaths.size() > 0) {
                lastFoundIndex = 0;
                lastSearch = id;
                TreePath tp = (TreePath) this.foundpaths.elementAt(lastFoundIndex);
                ids.om.arbolObjetos.expandPath(tp);
                ids.om.arbolObjetos.scrollPathToVisible(tp);
                ids.om.arbolObjetos.setSelectionPath(tp);
                lastFoundIndex++;
            }
        }
    }
}
