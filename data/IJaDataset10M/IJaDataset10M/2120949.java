package pl.vdl.azbest.mremote.cmd;

import pl.vdl.azbest.mremote.com.CConnect;
import pl.vdl.azbest.mremote.mob.ATOutputStream;

public class Cmd_getBTSCellName implements Command {

    private final String at_command = "AT+CGREG?";

    public void execute() {
        CConnect.getInstance().write(at_command);
    }
}
