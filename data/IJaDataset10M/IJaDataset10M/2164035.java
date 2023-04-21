package org.perfectjpattern.core.behavioral.command;

import org.perfectjpattern.core.api.behavioral.command.*;

/**
 * Concrete implementation of {@link IParameterlessCommand}
 *
 * @author <a href="mailto:bravegag@hotmail.com">Giovanni Azua</a>
 * @version $Revision: 1.0 $Date: Apr 13, 2008 9:59:08 PM $
 */
public class ParameterlessCommand extends Command<NullParameter, NullResult> implements IParameterlessCommand {

    public ParameterlessCommand() {
        super();
    }

    public ParameterlessCommand(IReceiver<NullParameter, NullResult> aReceiver) {
        super(aReceiver);
    }
}
