package allensoft.javacvs.client.ui.command.event;

import allensoft.javacvs.client.ui.command.*;

/** The listener interface for events fired by a CommandInterpretter. */
public interface CommandInterpretterListener {

    void interprettingCommand(InterprettingCommandEvent event);
}
