package org.spantus.work.ui.cmd;

import java.util.Set;

public interface SpantusWorkCommand {

    public Set<String> getExpectedActions();

    public void execute(SpantusWorkUIEvent event);
}
