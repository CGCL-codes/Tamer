package ru.adv.repository.channel.server;

import ru.adv.repository.channel.io.ChannelException;
import ru.adv.repository.channel.io.InvokeRemoteRepositoryMethodRC;
import ru.adv.logger.TLogger;

/**
 * User: vic
 * Date: 05.10.2004
 * Time: 19:28:02
 */
public class InvokeRemoteRepositoryMethodLC extends LocalCommand {

    public Object doCommand(ChannelServerSession session) throws ChannelException {
        Object retValue = null;
        try {
            InvokeRemoteRepositoryMethodRC rc = (InvokeRemoteRepositoryMethodRC) getRemoteCommand();
            retValue = session.invokeRepositoryMethod(rc);
        } catch (Throwable e) {
            TLogger.logFatalStackTrace(InvokeRemoteRepositoryMethodLC.class, "", e);
            throw new ChannelException(e.getMessage());
        }
        return retValue;
    }
}
