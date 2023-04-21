package org.xsocket.mail.pop3;

import java.io.IOException;
import org.xsocket.mail.CommandName;
import org.xsocket.stream.INonBlockingConnection;

/**
 * 
 * 
 * @author grro@xsocket.org
 */
@CommandName("UIDL")
final class UIDLCommand extends AbstractPOP3Command {

    public UIDLCommand() {
    }

    @Override
    public void execute(ICommandContext ctx, INonBlockingConnection connection, String[] arguments) throws IOException, POP3ProtocolException {
        checkState(ctx, ICommandContext.State.TRANSACTION, "have to be in TRANSACTION state");
        if (arguments.length == 1) {
            Integer msgNumber = Integer.parseInt(arguments[0]);
            if (ctx.isMessageNumberMarkedAsDeleted(msgNumber)) {
                sendNegativeResponse(connection, "message " + msgNumber + " is deleted");
            } else {
                String uid = ctx.getMessageUid(msgNumber);
                if (uid != null) {
                    sendPositiveResponse(connection, msgNumber + " " + uid);
                } else {
                    sendNegativeResponse(connection, "Error no such message");
                }
            }
        } else {
            String[] uids = ctx.getMaildrop().getAllMessagesUid();
            Integer[] msgNumbers = ctx.mapUidToMessageNumber(uids);
            if (msgNumbers.length > 0) {
                sendPositiveResponse(connection, "");
                for (Integer msgNumber : msgNumbers) {
                    if (!ctx.isMessageNumberMarkedAsDeleted(msgNumber)) {
                        sendMultilineResponse(connection, msgNumber + " " + ctx.getMessageUid(msgNumber));
                    }
                }
                sendMultilineResponse(connection, ".");
            } else {
                sendNegativeResponse(connection, "Error no messages");
            }
        }
    }
}
