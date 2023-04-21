package pl.bristleback.server.bristle.action;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import pl.bristleback.server.bristle.api.action.ActionInformation;
import pl.bristleback.server.bristle.api.action.ActionMessage;
import pl.bristleback.server.bristle.exceptions.BrokenActionProtocolException;
import java.util.HashMap;
import java.util.Map;

/**
 * //@todo class description
 * <p/>
 * Created on: 2011-07-20 11:46:01 <br/>
 *
 * @author Wojciech Niemiec
 */
public class ActionClassInformation {

    private static Logger log = Logger.getLogger(ActionClassInformation.class.getName());

    private boolean singleton;

    private String name;

    private Object actionClassInstance;

    private Map<String, ActionInformation> actions;

    private ActionInformation defaultAction;

    private Class<?> type;

    public ActionClassInformation() {
        actions = new HashMap<String, ActionInformation>();
    }

    public boolean hasDefaultAction() {
        return defaultAction != null;
    }

    public ActionInformation getActionToExecute(ActionMessage actionMessage) {
        if (StringUtils.isEmpty(actionMessage.getAction())) {
            if (!hasDefaultAction()) {
                throw new BrokenActionProtocolException(BrokenActionProtocolException.ReasonType.NO_DEFAULT_ACTION_FOUND, "Action class " + name + " does not have default action, specify action to execute");
            }
            return defaultAction;
        }
        String actionName = actionMessage.getAction();
        ActionInformation actionToExecute = actions.get(actionName);
        if (actionToExecute == null) {
            throw new BrokenActionProtocolException(BrokenActionProtocolException.ReasonType.NO_ACTION_FOUND, "Action class \"" + name + "\" does not have action \"" + actionName + "\".");
        }
        return actionToExecute;
    }

    public boolean isSingleton() {
        return singleton;
    }

    public void setSingleton(boolean singleton) {
        this.singleton = singleton;
    }

    public String getName() {
        return name;
    }

    public void setDefaultAction(ActionInformation defaultAction) {
        this.defaultAction = defaultAction;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, ActionInformation> getActions() {
        return actions;
    }

    public Object getSingletonActionClassInstance() {
        return actionClassInstance;
    }

    public void setSingletonActionClassInstance(Object singletonActionClassInstance) {
        this.actionClassInstance = singletonActionClassInstance;
    }

    public Class<?> getType() {
        return type;
    }

    public void setType(Class<?> type) {
        this.type = type;
    }
}
