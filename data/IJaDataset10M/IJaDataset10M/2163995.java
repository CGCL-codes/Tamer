package com.halozat2009osz;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import com.halozat2009osz.conndata.FileTransfer;
import com.halozat2009osz.conndata.FileTransfer.Direction;
import com.halozat2009osz.filetransfer.FileUtil;
import com.halozat2009osz.logger.Logger;
import com.halozat2009osz.logger.Logger.LogLevel;
import com.halozat2009osz.windows.PrivateChatWindow;

/**
 * Parses and handles messages sent by the server.
 */
public class Commands {

    static void execute(String message, ChatClient chatClient) {
        Parser parser = new Parser();
        parser.parse(message);
        if (parser.getCommand() == null) return;
        Class<?> commandClass;
        CommandType commandInstance;
        try {
            commandClass = Class.forName("com.halozat2009osz.Command_" + parser.getCommand().toUpperCase());
            commandInstance = (CommandType) commandClass.newInstance();
        } catch (Exception e) {
            Logger.log(chatClient, LogLevel.ERROR, "Unknown command " + "sent by server: " + parser.getCommand());
            return;
        }
        commandInstance.run(parser.getSender(), parser.getParams(), chatClient);
    }
}

interface CommandType {

    /**
	 * This method is called for handling each command supported by the
	 * protocol.
	 * 
	 * @param sender the sender of the command, if available
	 * @param param the parameters of the command
	 * @param cc reference to the main class
	 */
    public void run(String sender, String[] params, ChatClient cc);
}

class Command_ERROR implements CommandType {

    public void run(String sender, String[] params, ChatClient cc) {
        Logger.log(cc, LogLevel.SERVER, "Error: " + params[0]);
    }
}

class Command_HELLO implements CommandType {

    public void run(String sender, String[] params, ChatClient cc) {
        Logger.log(cc, LogLevel.SERVER, params[0]);
    }
}

class Command_PING implements CommandType {

    public void run(String sender, String[] params, ChatClient cc) {
        cc.getConnection().send("PING");
    }
}

class Command_ENDNICK implements CommandType {

    public void run(String sender, String[] params, ChatClient cc) {
        cc.connData.setLoggedIn(true);
        cc.eventLoggedIn(true);
        Logger.log(cc, LogLevel.INFO, cc.lang.get("LOGIN_SUCCESSFUL"));
    }
}

class Command_NICK implements CommandType {

    public void run(String sender, String[] params, ChatClient cc) {
        if (sender == null) {
            if (params.length > 0) {
                cc.connData.setMyNick(params[0]);
                Logger.log(cc, LogLevel.INFO, cc.lang.get("NICK_CHANGED_BY_SERVER", params[0]));
            }
        } else {
            if (params.length == 0) {
                cc.addNick(sender);
                Logger.log(cc, LogLevel.INFO, cc.lang.get("USER_JOINED", sender));
            } else {
                cc.removeNick(sender);
                cc.addNick(params[0]);
                PrivateChatWindow window = cc.getPrivateChatWindow(sender, false);
                if (window != null) window.updateRecipient(params[0]);
                ArrayList<FileTransfer> transfers = cc.connData.transfers.get(sender);
                if (transfers != null) for (FileTransfer transfer : transfers) {
                    transfer.setOtherClient(params[0]);
                    transfer.getFileTransferPanel().eventNickChange();
                }
                Logger.log(cc, LogLevel.INFO, cc.lang.get("USER_CHANGED_NICK", sender, params[0]));
            }
        }
    }
}

class Command_QUIT implements CommandType {

    public void run(String sender, String[] params, ChatClient cc) {
        String quitMsg = cc.lang.get("USER_QUIT", sender, params.length > 0 ? (" (" + params[0] + ")") : "");
        Logger.log(cc, LogLevel.INFO, quitMsg);
        PrivateChatWindow window = cc.getPrivateChatWindow(sender, false);
        if (window != null) {
            window.eventConnectionClosed();
            Logger.log(window, LogLevel.INFO, quitMsg);
        }
        cc.removeNick(sender);
        ArrayList<FileTransfer> transfers = cc.connData.transfers.get(sender);
        if (transfers != null) {
            transfers = new ArrayList<FileTransfer>(transfers);
            for (FileTransfer transfer : transfers) cc.connData.transfers.remove(transfer);
        }
    }
}

class Command_MSG implements CommandType {

    public void run(String sender, String[] params, ChatClient cc) {
        if (params.length == 1) {
            cc.printMessage("<" + sender + "> " + params[0]);
        } else {
            PrivateChatWindow window = cc.getPrivateChatWindow(sender);
            window.printMessage("<" + sender + "> " + params[1]);
        }
    }
}

class Command_FILE implements CommandType {

    public void run(String sender, String[] params, ChatClient cc) {
        int otherId = Integer.parseInt(params[0]);
        long size = params.length > 2 ? Integer.parseInt(params[1]) : -1;
        String file = params.length > 2 ? params[2] : params[1];
        PrivateChatWindow window = cc.getPrivateChatWindow(sender);
        Logger.log(window, LogLevel.INFO, cc.lang.get("FILE_OFFERED_BY_USER", sender, file, FileUtil.getReadableSize(size)));
        FileTransfer transfer = cc.connData.transfers.add(Direction.FT_RECEIVE, sender);
        transfer.setFile(new File(file));
        transfer.setSize(size);
        transfer.setOtherId(otherId);
        transfer.setOffered(true);
        cc.getFileTransfersWindow().add(transfer, window);
    }
}

class Command_ACCEPT implements CommandType {

    public void run(String sender, String[] params, ChatClient cc) {
        int id = Integer.parseInt(params[0]);
        PrivateChatWindow window = cc.getPrivateChatWindow(sender);
        FileTransfer transfer = cc.connData.transfers.get(id);
        transfer.setAccepted(true);
        Logger.log(window, LogLevel.INFO, cc.lang.get("FILE_ACCEPTED_BY_USER", sender, transfer.getFile().getName()));
        try {
            transfer.startSender(cc, window);
        } catch (FileNotFoundException e) {
            Logger.log(window, LogLevel.ERROR, "Can't open file " + transfer.getFile().getPath() + " for reading: " + e.getMessage());
            cc.getConnection().send("CANCEL");
            cc.connData.transfers.remove(transfer);
        }
    }
}

class Command_REFUSE implements CommandType {

    public void run(String sender, String[] params, ChatClient cc) {
        int id = Integer.parseInt(params[0]);
        PrivateChatWindow window = cc.getPrivateChatWindow(sender);
        FileTransfer transfer = cc.connData.transfers.get(id);
        Logger.log(window, LogLevel.INFO, cc.lang.get("FILE_REFUSED_BY_USER", sender, transfer.getFile().getName()));
        cc.connData.transfers.remove(transfer);
    }
}

class Command_DATA implements CommandType {

    public void run(String sender, String[] params, ChatClient cc) {
        int id;
        try {
            id = Integer.parseInt(params[0]);
        } catch (Exception e) {
            Logger.log(cc, LogLevel.ERROR, "Protocol error: Invalid DATA parameters.");
            return;
        }
        FileTransfer transfer = cc.connData.transfers.get(id);
        sender = transfer.getOtherClient();
        PrivateChatWindow window = cc.getPrivateChatWindow(sender);
        String errorMsg = null;
        try {
            transfer.getWriterInstance().write(params[1]);
            transfer.getFileTransferPanel().updateProgress();
        } catch (FileNotFoundException e) {
            errorMsg = "Can't open file " + transfer.getFile().getPath() + " for writing: " + e.getMessage();
        } catch (IOException e) {
            errorMsg = "Can't write to file " + transfer.getFile().getPath() + ": " + e.getMessage();
        } catch (Exception e) {
            errorMsg = "Error receiving file " + transfer.getFile().getPath() + ": " + e.getMessage();
        } finally {
            if (errorMsg != null) {
                Logger.log(window, LogLevel.ERROR, errorMsg);
                cc.getConnection().send("CANCEL");
                cc.connData.transfers.remove(transfer);
            }
        }
    }
}

class Command_ENDFILE implements CommandType {

    public void run(String sender, String[] params, ChatClient cc) {
        int id = Integer.parseInt(params[0]);
        FileTransfer transfer = cc.connData.transfers.get(id);
        sender = transfer.getOtherClient();
        PrivateChatWindow window = cc.getPrivateChatWindow(sender);
        try {
            transfer.getWriterInstance();
            transfer.getFileTransferPanel().updateProgress();
            Logger.log(window, LogLevel.INFO, cc.lang.get("FILE_RECEIVE_SUCCESSFUL", transfer.getFile().getName()));
        } catch (FileNotFoundException e) {
            Logger.log(window, LogLevel.ERROR, "Can't open file " + transfer.getFile().getPath() + " for writing: " + e.getMessage());
        }
        cc.connData.transfers.remove(transfer);
    }
}

class Command_CANCEL implements CommandType {

    public void run(String sender, String[] params, ChatClient cc) {
        int id = Integer.parseInt(params[0]);
        FileTransfer transfer = cc.connData.transfers.get(id);
        sender = transfer.getOtherClient();
        PrivateChatWindow window = cc.getPrivateChatWindow(sender);
        Logger.log(window, LogLevel.ERROR, cc.lang.get("FILE_CANCELLED_BY_USER", transfer.getFile().getName(), sender));
        cc.connData.transfers.remove(transfer);
    }
}
