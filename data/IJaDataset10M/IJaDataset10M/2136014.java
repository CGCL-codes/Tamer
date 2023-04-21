package be.vds.jtbdive.core.view.event;

import java.util.List;
import be.vds.jtbdive.core.model.Diver;

public class DiverObserverEvent {

    public static final int SAVE = 0;

    public static final int DELETE = 1;

    public static final int UPDATE = 2;

    private int action;

    private List<Diver> divers;

    public DiverObserverEvent(int action, List<Diver> divers) {
        this.action = action;
        this.divers = divers;
    }

    public List<Diver> getDivers() {
        return divers;
    }

    public int getAction() {
        return action;
    }
}
