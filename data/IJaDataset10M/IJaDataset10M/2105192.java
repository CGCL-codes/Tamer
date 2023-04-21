package org.xsocket.mail.pop3;

import java.io.IOException;
import org.xsocket.mail.CommandName;
import org.xsocket.stream.INonBlockingConnection;

/**
 * 
 * 
 * @author grro@xsocket.org
 */
@CommandName("RSET")
final class RSETCommand extends AbstractPOP3Command {

    public RSETCommand() {
    }

    @Override
    public void execute(ICommandContext ctx, INonBlockingConnection connection, String[] arguments) throws IOException, POP3ProtocolException {
        checkState(ctx, ICommandContext.State.TRANSACTION, "have to be in TRANSACTION state");
        ctx.clearMessageNumbersToDelete();
        sendPositiveResponse(connection, "");
    }
}
