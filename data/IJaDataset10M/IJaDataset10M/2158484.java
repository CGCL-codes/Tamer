package net.sf.echopm.sample.command;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * The default handler executes the command in a transaction locally to the
 * process that supplied the command.
 * 
 * @author ron
 */
public class DefaultCommandHandler implements CommandHandler {

    @Transactional(propagation = Propagation.REQUIRED, noRollbackFor = {  })
    public <T extends Command> T execute(T command) throws Exception {
        command.execute();
        return command;
    }
}
