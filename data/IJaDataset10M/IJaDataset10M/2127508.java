package be.lassi.control.midi;

public class MidiShowControlMessage {

    public static final int GO = 1;

    public static final int STOP = 2;

    public static final int RESUME = 3;

    private final int deviceId;

    private final int command;

    private final String cueNumber;

    private final String cueList;

    private final String cuePath;

    public MidiShowControlMessage(final int deviceId, final int command, final String cueNumber, final String cueList, final String cuePath) {
        this.deviceId = deviceId;
        this.command = command;
        this.cueNumber = cueNumber;
        this.cueList = cueList;
        this.cuePath = cuePath;
    }

    public int getDeviceId() {
        return deviceId;
    }

    public int getCommand() {
        return command;
    }

    public String getCueNumber() {
        return cueNumber;
    }

    public String getCueList() {
        return cueList;
    }

    public String getCuePath() {
        return cuePath;
    }
}
