package org.xsocket.mail.pop3;

import java.io.IOException;
import org.xsocket.mail.CommandName;
import org.xsocket.mail.pop3.spi.IPOP3MessageSource;
import org.xsocket.stream.INonBlockingConnection;

/**
 * 
 * 
 * 
 * @author grro@xsocket.org
 */
@CommandName("TOP")
final class TOPCommand extends AbstractGetCommand {

    public TOPCommand() {
    }

    @Override
    public void execute(ICommandContext ctx, INonBlockingConnection connection, String[] arguments) throws IOException, POP3ProtocolException {
        checkState(ctx, ICommandContext.State.TRANSACTION, "have to be in TRANSACTION state");
        checkParameterCount(arguments, 2);
        Integer msgNumber = parseIntArgument(arguments[0]);
        Integer count = parseIntArgument(arguments[1]);
        if (ctx.isMessageNumberMarkedAsDeleted(msgNumber)) {
            sendNegativeResponse(connection, "message " + msgNumber + " is deleted");
        } else {
            String uid = ctx.getMessageUid(msgNumber);
            IPOP3MessageSource source = ctx.getMaildrop().getMessageHeaderAndFirstBodyLines(uid, count);
            printMessage(ctx, connection, uid, source);
        }
    }
}
