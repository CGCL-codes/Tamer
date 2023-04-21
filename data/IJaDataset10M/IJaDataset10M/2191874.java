package ingenias.jade.components;

import java.util.*;
import ingenias.jade.exception.*;

public abstract class VisualizationAppApp extends MultipleOwnersApplication {

    public VisualizationAppApp() {
        super();
    }

    public abstract void start();

    public abstract void update(int component, Object data);

    public abstract void sendEvent(String fuente);
}
