package org.jostraca.setup;

import org.jostraca.util.SimpleObjectManager;
import java.util.Vector;
import java.util.Hashtable;
import java.util.Enumeration;

/** <b>Description:</b><br>
 *  Manages a set of SetupActions.
 */
public class SetupManager extends SimpleObjectManager {

    public static final String CN = "SetupManager";

    private String[] iArgs = new String[] {};

    public SetupManager() {
        super(SetupAction.class, "org.jostraca.setup");
    }

    public void setArgs(String[] pArgs) {
        iArgs = pArgs;
    }

    public synchronized void setup() throws SetupException {
        String MN = ".setup: ";
        Hashtable doneActions = new Hashtable();
        SetupAction action = null;
        System.out.println();
        System.out.println("Jostraca Installation");
        System.out.println("---------------------");
        try {
            Hashtable config = init();
            Vector actions = getActions(config);
            int numActions = actions.size();
            for (int actionI = 0; actionI < numActions; actionI++) {
                action = (SetupAction) actions.elementAt(actionI);
                action.doSetup(config, this);
                doneActions.put(action.getName(), action);
            }
        } catch (SetupException se) {
            undoActions(doneActions);
            throw se;
        } catch (Throwable t) {
            undoActions(doneActions);
            throw new SetupException(CN + MN + "current action: " + action, SetupAction.EX_UNKNOWN, t);
        }
    }

    public void done(SetupAction pAction) {
        System.out.println(pAction.getDonePhrase());
    }

    private Hashtable init() {
        Hashtable config = new Hashtable();
        config.put(SetupAction.CONF_SetupActions, "   SetupActionFindOS" + " ,SetupActionFindHome" + " ,SetupActionFindPath" + " ,SetupActionGenerateScript" + " ,SetupActionSaveScript" + " ,SetupActionSetPermissions" + " ,SetupActionComplete");
        config.put(SetupAction.CONF_Arguments, iArgs);
        return config;
    }

    private Vector getActions(Hashtable pConfig) throws SetupException {
        String MN = ".getActions";
        String classList = "";
        try {
            classList = (String) pConfig.get(SetupAction.CONF_SetupActions);
            super.loadClasses(classList);
            return super.iObjects;
        } catch (Exception e) {
            throw new SetupException(CN + MN + ": unable to load actions: " + classList, SetupAction.EX_LOAD_ACTION_CLASSES, e);
        }
    }

    private void undoActions(Hashtable pDoneActions) {
        Enumeration names = pDoneActions.keys();
        while (names.hasMoreElements()) {
            try {
                String name = (String) names.nextElement();
                SetupAction sa = (SetupAction) pDoneActions.get(name);
                sa.undoSetup();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] pArgs) {
        try {
            SetupManager sm = new SetupManager();
            sm.setArgs(pArgs);
            sm.setup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
