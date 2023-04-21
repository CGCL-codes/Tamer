package engine;

import java.util.Observer;

public interface ObserverRoute extends Observer {

    boolean update = true;

    Object subject = null;

    public void update();
}
