package org.spantus.work.ui.cmd;

import java.util.Set;
import org.spantus.work.ui.dto.SpantusWorkInfo;

public class StopCmd extends AbsrtactCmd {

    public StopCmd(CommandExecutionFacade executionFacade) {
        super(executionFacade);
    }

    public Set<String> getExpectedActions() {
        return createExpectedActions(GlobalCommands.sample.stop);
    }

    public String execute(SpantusWorkInfo ctx) {
        ctx.setPlaying(false);
        return null;
    }
}
