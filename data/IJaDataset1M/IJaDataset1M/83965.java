package action;

import java.util.Hashtable;

public class ActionFactory {

    private Hashtable actions = new Hashtable();

    public Action getAction(String classname, ClassLoader loader) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        Action action = (Action) actions.get(classname);
        if (action == null) {
            Class klass = loader.loadClass(classname);
            action = (Action) klass.newInstance();
            actions.put(classname, action);
        }
        return action;
    }
}
