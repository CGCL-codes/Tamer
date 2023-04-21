package logic.common.missions;

public class MissionEvent {

    protected Mission mission;

    public MissionEvent(Mission mission) {
        this.mission = mission;
    }

    public Mission getMission() {
        return mission;
    }
}
