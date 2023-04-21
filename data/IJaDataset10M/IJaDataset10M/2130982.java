package com.simpleftp.ftp.server.command;

import java.io.IOException;
import com.simpleftp.ftp.server.filesystem.FileSystemManager;
import com.simpleftp.ftp.server.filesystem.FtpFile;
import com.simpleftp.ftp.server.objects.FtpReply;
import com.simpleftp.ftp.server.objects.FtpUserSession;
import com.simpleftp.ftp.server.utils.FtpLogger;

public class FtpSIZECommand extends AbstractCommand {

    private boolean paramError = false;

    private String fileName;

    FtpSIZECommand(String cmd) {
        super(cmd);
    }

    public boolean execute(FtpUserSession session) {
        if (paramError) {
            if (FtpLogger.debug) {
                logger.debug("Parameter error in SIZE Command, The parameter passed is " + fileName);
            }
            session.getControlConnection().scheduleSend(FtpReply.getFtpReply(501));
            return false;
        }
        FileSystemManager fileSystemManager = FileSystemManager.getFileSystemManager();
        FtpFile sizeFile = null;
        try {
            sizeFile = fileSystemManager.getFtpFileWRTPwd(session.getUser(), fileName);
        } catch (IOException e) {
            logger.error("File System error" + e);
            session.getControlConnection().scheduleSend(FtpReply.getFtpReply(550));
            return false;
        }
        if (!sizeFile.exists() || sizeFile.isDirectory()) {
            if (FtpLogger.debug) {
                logger.debug("File does not exist on the FileSystem or is a Directory " + fileName);
            }
            session.getControlConnection().scheduleSend(FtpReply.getFtpReply(550));
            return false;
        }
        try {
            sizeFile.length();
            session.getControlConnection().scheduleSend(FtpReply.getFtpReply(250));
            return true;
        } catch (Throwable t) {
            logger.error("Illegal Access in SIZE  Command", t);
            session.getControlConnection().scheduleSend(FtpReply.getFtpReply(550));
            return false;
        }
    }

    @Override
    public boolean isCommandInSequence() {
        return false;
    }

    @Override
    public void setCommandInSequence(boolean inSequence) {
    }

    public void setParameter(String[] params) {
        if (params == null) {
            paramError = true;
            return;
        }
        fileName = params[0];
        if (FtpLogger.debug) {
            logger.debug("Setting the direcotry name to " + fileName);
        }
        paramError = false;
        return;
    }
}
