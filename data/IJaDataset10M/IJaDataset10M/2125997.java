package plankton.configGui;

import java.util.HashMap;
import javax.swing.ActionMap;
import plankton.structure.DataModel;

/**
 * @author Administrator
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class EditorActionFactory {

    HashMap actionMap;

    private static EditorActionFactory factory = new EditorActionFactory();

    private EditorActionFactory() {
        actionMap = new HashMap();
    }

    public static void registerAction(String name, EditorAction action) {
        factory.registerInternalAction(name, action);
    }

    public static void registerAction(String name, String clazzName) {
        factory.registerInternalAction(name, clazzName);
    }

    public static void executeEditorAction(String name, DataModel mod) {
        factory.executeInternalAction(name, mod);
    }

    /**
	 * @param mod
	 */
    private void executeInternalAction(String name, DataModel mod) {
        EditorAction action = (EditorAction) actionMap.get(name);
        action.executeAction(mod);
    }

    /**
	 * @param name
	 * @param action
	 */
    private void registerInternalAction(String name, EditorAction action) {
        actionMap.put(name, action);
    }

    /**
	 * @param name
	 * @param action
	 */
    private void registerInternalAction(String name, String clazzName) {
        EditorAction action;
        try {
            action = (EditorAction) Class.forName(clazzName).newInstance();
            actionMap.put(name, action);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }
}
