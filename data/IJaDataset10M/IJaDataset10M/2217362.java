package coopnetclient.utils.launcher.launchinfos;

import coopnetclient.utils.RoomData;

public abstract class LaunchInfo {

    protected RoomData roomData;

    public LaunchInfo(RoomData roomData) {
        this.roomData = roomData;
    }

    public RoomData getRoomData() {
        return roomData;
    }

    public abstract String getBinaryName();
}
