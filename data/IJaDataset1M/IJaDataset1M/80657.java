package gruntspud.actions;

import gruntspud.ResourceUtil;
import java.util.ResourceBundle;
import javax.swing.AbstractAction;
import javax.swing.Action;

/**
 * Convencience Abstract implementation of a <code>GruntspudAction</code>
 * 
 * @author magicthize
 */
public abstract class AbstractGruntspudAction extends AbstractAction implements GruntspudAction {

    /**
   * Default contructor for AbstractGruntspudAction
   */
    public AbstractGruntspudAction() {
    }

    /**
   * Construct the action using a resource bundle to derive the name, short
   * description, long description, mnemonic and accelerator.
   * 
   * @param res resource bundle
   * @param prefix action prefix
   */
    public AbstractGruntspudAction(ResourceBundle res, String actionPrefix) {
        this();
        putValue(Action.NAME, res.getString(actionPrefix + ".name"));
        putValue(Action.SHORT_DESCRIPTION, res.getString(actionPrefix + ".shortDescription"));
        putValue(Action.LONG_DESCRIPTION, res.getString(actionPrefix + ".longDescription"));
        putValue(Action.MNEMONIC_KEY, new Integer(ResourceUtil.getResourceMnemonic(res, actionPrefix + ".mnemonic")));
        if (!"true".equals(System.getProperty("gruntspud.disableKeyboardAccelerators", "false"))) {
            putValue(Action.ACCELERATOR_KEY, ResourceUtil.getResourceKeyStroke(res, actionPrefix + ".keystroke"));
        }
        putValue(GruntspudAction.SHOW_NAME, Boolean.valueOf(ResourceUtil.getResourceBoolean(res, actionPrefix + ".showName")));
    }

    /**
   * Invoked whenever the state changes in some way. Each action must determine
   * whether or not it should be enabled or not. A return value of
   * <code>true</code> will cause the action to be enabled if it is not.
   * 
   * @return action should be enabled
   */
    public boolean checkAvailable() {
        return true;
    }
}
