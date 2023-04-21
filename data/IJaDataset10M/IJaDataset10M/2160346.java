package repast.simphony.ui.plugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import repast.simphony.engine.environment.ControllerAction;
import repast.simphony.plugin.CompositeControllerActionCreator;

/**
 * @author Nick Collier
 * @version $Revision: 1.1 $ $Date: 2006/01/09 15:55:03 $
 */
public class CompositeActionEditorExtensions {

    private Map<String, String> extLabelMap = new HashMap<String, String>();

    private Map<ControllerAction, ParentActionUI> editorMap = new HashMap<ControllerAction, ParentActionUI>();

    private Map<String, List<EditorMenuItem>> itemMap = new HashMap<String, List<EditorMenuItem>>();

    public void addEditorMenuItem(String parentActionID, EditorMenuItem item) {
        List<EditorMenuItem> list = itemMap.get(parentActionID);
        if (list == null) {
            list = new ArrayList<EditorMenuItem>();
            itemMap.put(parentActionID, list);
        }
        list.add(item);
    }

    public void addLabel(String creatorClassName, String label) {
        extLabelMap.put(creatorClassName, label);
    }

    public void addUI(ControllerAction action, CompositeControllerActionCreator creator) {
        String label = extLabelMap.get(creator.getClass().getName());
        if (label == null) label = action.toString();
        ParentActionUI editor = new ParentActionUI(label);
        List<EditorMenuItem> items = itemMap.get(creator.getID());
        if (items != null) {
            for (EditorMenuItem item : items) {
                editor.addEditorMenuItem(item);
            }
        }
        editorMap.put(action, editor);
    }

    public ParentActionUI getUI(ControllerAction action) {
        return editorMap.get(action);
    }

    public void addUI(ControllerAction action, String label) {
        editorMap.put(action, new ParentActionUI(label));
    }
}
