package org.drftpd.permissions.fxp;

import java.net.InetAddress;
import org.drftpd.GlobalContext;
import org.drftpd.commandmanager.CommandRequest;
import org.drftpd.commandmanager.CommandRequestInterface;
import org.drftpd.commandmanager.PreHookInterface;
import org.drftpd.commandmanager.StandardCommandManager;
import org.drftpd.commands.dataconnection.DataConnectionHandler;
import org.drftpd.master.BaseFtpConnection;
import org.drftpd.master.config.ConfigInterface;
import org.drftpd.slave.Transfer;
import org.drftpd.vfs.DirectoryHandle;

/**
 * @author fr0w
 * @version $Id: FXPPermissionPreHook.java 2157 2010-10-08 18:50:53Z djb61 $
 */
public class FXPPermissionPreHook implements PreHookInterface {

    public void initialize(StandardCommandManager manager) {
    }

    public CommandRequestInterface checkDownloadFXPPerm(CommandRequest request) {
        return checkFXPPerm(request, Transfer.TRANSFER_SENDING_DOWNLOAD);
    }

    public CommandRequestInterface checkUploadFXPPerm(CommandRequest request) {
        return checkFXPPerm(request, Transfer.TRANSFER_RECEIVING_UPLOAD);
    }

    public CommandRequestInterface checkFXPPerm(CommandRequest request, char direction) {
        DirectoryHandle fromDir = request.getCurrentDirectory();
        ConfigInterface config = GlobalContext.getConfig();
        String directive = direction == Transfer.TRANSFER_RECEIVING_UPLOAD ? "deny_upfxp" : "deny_dnfxp";
        String mask = "*@*";
        if (config.checkPathPermission(directive, request.getSession().getUserNull(request.getUser()), fromDir)) {
            InetAddress inetAdd = request.getSession().getObject(BaseFtpConnection.ADDRESS, null);
            mask = "*@" + inetAdd.getHostAddress();
        }
        request.getSession().setObject(DataConnectionHandler.INET_ADDRESS, mask);
        return request;
    }
}
