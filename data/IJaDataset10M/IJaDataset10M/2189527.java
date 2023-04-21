package jetty;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 *
 * @author custom
 */
public class ObjectsList extends CopyOnWriteArrayList<BaseObject> {

    private static ObjectsList instance = ObjectsList.getInstance();

    public static ObjectsList getInstance() {
        if (instance == null) {
            instance = new ObjectsList();
        }
        return instance;
    }

    public ObjectsList() {
        for (int i = 1; i < 10; i++) {
            add(new Enemy("img/barque90.png", 90, 90, 72));
        }
    }
}
