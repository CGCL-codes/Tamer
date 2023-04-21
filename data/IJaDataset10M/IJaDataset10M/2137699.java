package org.bitdrive.network.filelist.impl;

import org.bitdrive.core.logging.api.Level;
import org.bitdrive.core.logging.api.Logger;
import org.bitdrive.core.settings.api.Settings;
import org.bitdrive.external.Misc;
import org.bitdrive.file.core.api.FileFactory;
import org.bitdrive.network.core.api.*;
import org.bitdrive.network.filelist.api.ListHandler;
import org.bitdrive.network.filelist.impl.FileList;
import org.bitdrive.network.filelist.impl.messages.FileListResponseMessage;
import org.bitdrive.network.filelist.impl.messages.FileListResponseMessageAdaptor;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.LinkedList;

public class RemoteFileListManagerImpl implements MessageHandler {

    private final String SETTING_FILE_LIST_FOLDER = "SETTING_FILE_LIST_FOLDER";

    private Logger logger;

    private File fileListFolder;

    private MessageFactory messageFactory;

    private FileFactory fileFactory;

    private LinkedList<ListHandler> listHandlers;

    public LinkedList<ListHandler> getFileListHandlers() {
        return listHandlers;
    }

    public RemoteFileListManagerImpl(Logger logger, Settings settings, FileFactory fileFactory, MessageFactory messageFactory) {
        this.logger = logger;
        this.fileFactory = fileFactory;
        this.messageFactory = messageFactory;
        this.listHandlers = new LinkedList<ListHandler>();
        this.fileListFolder = new File(settings.getString(SETTING_FILE_LIST_FOLDER, settings.getString(Settings.SETTING_FOLDER) + "file_lists/"));
        if (!this.fileListFolder.exists()) {
            this.fileListFolder.mkdir();
        }
    }

    public short[][] getMessageTypes() {
        return new short[][] { Activator.FILE_LIST_UPDATED_TYPE_ID, FileListResponseMessageAdaptor.TYPE_ID };
    }

    public ConnectionState[] getConnectionStates() {
        return new ConnectionState[] { ConnectionState.CONNECTED, ConnectionState.DISCONNECTED };
    }

    private void addFileListToHandlers(Connection connection, File sourceFile) throws IOException {
        FileList fileList = FileList.fromXML(new FileInputStream(sourceFile));
        for (ListHandler handler : listHandlers) {
            byte[] data = fileList.getFileList(handler.getName());
            if (data != null) {
                handler.addFileList(connection, data);
            } else {
                logger.log(Level.WARNING, "RemoteFileListManager.addFileListToHandlers: List for name '" + handler.getName() + "' is null");
            }
        }
    }

    private void onFileList(Connection connection, FileListResponseMessage responseMessage) {
        File file = new File(fileListFolder + "/" + Misc.byteArrayToHexString(responseMessage.getHash()) + ".xml");
        if (file.exists()) {
            try {
                logger.log(Level.FINE, "RemoteFileListManager.onFileList: File list is on disk, loading from disk (file '" + file.getName() + "'");
                addFileListToHandlers(connection, file);
                return;
            } catch (IOException e) {
                logger.log(Level.SEVERE, "RemoteFileListManager.onFileList: Failed to read in file list", e);
            }
        }
        logger.log(Level.FINE, "RemoteFileListManager.onFileList: Downloading file list to disk (file '" + file.getName() + "'");
        try {
            fileFactory.addFile(responseMessage.getHash(), responseMessage.getSize(), connection);
            fileFactory.copyFile(responseMessage.getHash(), file);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "RemoteFileListManager.onFileList: Failed to download file list", e);
            return;
        }
        try {
            addFileListToHandlers(connection, file);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "RemoteFileListManager.onFileList: Read file list", e);
        }
    }

    public boolean onMessage(Connection c, Message msg) {
        if (msg.isType(Activator.FILE_LIST_UPDATED_TYPE_ID)) {
            logger.log(Level.FINE, "RemoteFileListManager.onMessage: Requesting file list (from file list update message)");
            c.sendMessage(messageFactory.createMessage(Activator.FILE_LIST_REQUEST_TYPE_ID));
        } else {
            logger.log(Level.FINE, "RemoteFileListManager.onMessage: Got file list response");
            onFileList(c, (FileListResponseMessage) msg.getPayload());
        }
        return false;
    }

    public boolean onConnectionStateChange(Connection c, ConnectionState state) {
        switch(state) {
            case CONNECTED:
                c.sendMessage(messageFactory.createMessage(Activator.FILE_LIST_REQUEST_TYPE_ID));
                break;
            case DISCONNECTED:
                for (ListHandler handler : listHandlers) handler.removeFileList(c);
                break;
        }
        return false;
    }
}
