package interfaces;

import java.util.LinkedList;
import World.*;

public class Event_AlternateLanes extends Event {

    LinkedList<Lane> lla;

    public LinkedList<Lane> getLla() {
        return lla;
    }

    public void setLla(LinkedList<Lane> lla) {
        this.lla = lla;
    }

    public Event_AlternateLanes() {
        lla = new LinkedList<Lane>();
    }

    @Override
    public boolean FillData(Project project, CntAgent agent) {
        if (agent.getCurLane() != null) {
            lla = agent.getCurLane().getNextLanes();
        }
        return true;
    }
}
