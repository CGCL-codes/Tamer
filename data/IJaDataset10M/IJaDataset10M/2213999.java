package logic;

import java.util.HashMap;

/**
 * Observer interface, part of the observer design pattern.
 * @author wernerflatz
 *
 */
public interface Observer {

    public void update();

    public void update(KeyAction keyAction, HashMap<String, Object> keyValuePairs);
}
