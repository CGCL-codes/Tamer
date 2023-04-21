package octoshare.client.host;

import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.util.List;
import java.util.Vector;
import octoshare.client.handle.GetDirHandle;
import octoshare.client.handle.Handle;
import octoshare.client.handle.HandleListener;
import octoshare.client.handle.HandleQueue;
import octoshare.client.handle.SendFileHandle;
import octoshare.client.handle.SendThumbHandle;
import octoshare.common.channel.StringOutputChannel;
import octoshare.common.connection.Connection;
import octoshare.common.connection.PSConnection;
import octoshare.common.filesystem.FileSender;
import octoshare.common.filesystem.Filesystem;
import octoshare.common.request.messages.DirectoryEntryMessage;
import octoshare.common.request.messages.DoneMessage;
import octoshare.common.request.messages.ErrorMessage;
import octoshare.common.request.messages.Message;
import octoshare.common.request.messages.MessageFactory;
import octoshare.common.request.messages.RequestDirMessage;
import octoshare.common.request.messages.SendFileMessage;
import octoshare.common.request.messages.SendThumbMessage;
import octoshare.common.request.messages.SendingFileInfoMessage;
import octoshare.common.thumbs.FilesystemThumbHandler;
import octoshare.server.security.SecurityHandler;
import octoshare.server.user.UserState;

public class Host implements HostInterface {

    private Connection commandConnection;

    private Connection dataConnection;

    private SecurityHandler securityHandler;

    private boolean shutdown;

    private boolean running = false;

    private UserState userState = UserState.LOGIN;

    private HandleQueue handleQueue = new HandleQueue();

    private Filesystem rootFilesystem;

    private Filesystem thumbCacheFilesystem;

    private FilesystemThumbHandler thumbHandler;

    private int dataPort;

    /**
	 * files to send after sendFileMessage was received (files wait for the
	 * client to connect to the data port
	 */
    private List<File> filesToSend = new Vector<File>();

    public Host(Socket commandSocket, Filesystem rootFilesystem, Filesystem thumbCacheFilesystem) {
        this.rootFilesystem = rootFilesystem;
        this.thumbCacheFilesystem = thumbCacheFilesystem;
        thumbHandler = new FilesystemThumbHandler(new Dimension(120, 120), thumbCacheFilesystem);
        setCommandConnection(new PSConnection(commandSocket));
    }

    public Connection getCommandConnection() {
        return commandConnection;
    }

    public void setCommandConnection(Connection commandConnection) {
        this.commandConnection = commandConnection;
    }

    public Connection getDataConnection() {
        return dataConnection;
    }

    public void setDataConnection(Connection dataConnection) {
        this.dataConnection = dataConnection;
    }

    public Filesystem getFilesystem() {
        return rootFilesystem;
    }

    public void setFilesystem(Filesystem filesystem) {
        this.rootFilesystem = filesystem;
    }

    public SecurityHandler getSecurityHandler() {
        return securityHandler;
    }

    public void setSecurityHandler(SecurityHandler securityHandler) {
        this.securityHandler = securityHandler;
    }

    public void addNewDataSocket(Socket socket) {
        if (filesToSend.size() > 0) {
            setDataConnection(new PSConnection(socket));
            File file = filesToSend.get(0);
            filesToSend.remove(0);
            FileSender fileSender = new FileSender(file, getDataConnection());
            fileSender.start();
        }
    }

    public void run() {
        running = true;
        System.out.println("Host thread running");
        while (!shutdown) {
            if (userState == UserState.LOGIN) {
                userState = UserState.ACTIVE;
            }
            try {
                if (userState == UserState.ACTIVE) {
                    Handle currentHandle = handleQueue.peek();
                    if (currentHandle instanceof GetDirHandle) {
                        System.out.println("Host Thread: GetDirHandle");
                        GetDirHandle dirHandle = (GetDirHandle) currentHandle;
                        if (dirHandle.isSendCommandPending()) {
                            System.out.println("Host Thread: Sending Dir Message");
                            RequestDirMessage outgoing = MessageFactory.getInstance().createRequestDirMessage(dirHandle.getPath());
                            sendMessage(outgoing);
                            dirHandle.commandSent();
                        } else {
                            System.out.println("Host Thread: Reading DirEntry Message");
                            Message message = readNextMessage();
                            if (message instanceof DirectoryEntryMessage) {
                                dirHandle.getEntryMessages().add((DirectoryEntryMessage) message);
                            } else if (message instanceof DoneMessage) {
                                handleQueue.leave();
                                dirHandle.done();
                                System.out.println("Host Thread: Dir Handle done");
                            } else throw new IOException("Unexpected Message received while DIR received, or so...");
                        }
                    } else if (currentHandle instanceof SendFileHandle) {
                        System.out.println("Host Thread: SendFileHandle");
                        SendFileHandle sendFileHandle = (SendFileHandle) currentHandle;
                        if (sendFileHandle.isSendCommandPending()) {
                            System.out.println("Host Thread: Sending SendFile Message");
                            SendFileMessage outgoing = MessageFactory.getInstance().createSendFileMessage(sendFileHandle.getRemoteFilename());
                            sendMessage(outgoing);
                            sendFileHandle.commandSent();
                        } else {
                            System.out.println("Host Thread: Reading SendingFileInfo Message");
                            Message message = readNextMessage();
                            if (message instanceof SendingFileInfoMessage) {
                                sendFileHandle.setSendingFileInfoMessage((SendingFileInfoMessage) message);
                                handleQueue.leave();
                                sendFileHandle.done();
                            } else throw new IOException("Unexpected Message received while SEND FILE");
                        }
                    } else if (currentHandle instanceof SendThumbHandle) {
                        System.out.println("Host Thread: SendThumbHandle");
                        SendThumbHandle sendThumbHandle = (SendThumbHandle) currentHandle;
                        if (sendThumbHandle.isSendCommandPending()) {
                            System.out.println("Host Thread: Sending SendThumb Message");
                            SendThumbMessage outgoing = MessageFactory.getInstance().createSendThumbMessage(sendThumbHandle.getRemoteFilename());
                            sendMessage(outgoing);
                            sendThumbHandle.commandSent();
                        } else {
                            System.out.println("Host Thread: Reading SendingFileInfo Message");
                            Message message = readNextMessage();
                            if (message instanceof SendingFileInfoMessage) {
                                sendThumbHandle.setSendingFileInfoMessage((SendingFileInfoMessage) message);
                                handleQueue.leave();
                                sendThumbHandle.done();
                            } else if (message instanceof ErrorMessage) {
                                ErrorMessage error = (ErrorMessage) message;
                                System.out.println("Error " + error.getNumber() + " while retreiving thumb: " + error.getFreeText());
                                handleQueue.leave();
                                sendThumbHandle.done();
                            } else throw new IOException("Unexpected Message received while SEND THUMB");
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Host thread halted");
        commandConnection.shutdown();
        if (dataConnection != null) {
            dataConnection.shutdown();
        }
        running = false;
        setUserState(UserState.DISCONNECTED);
    }

    protected void sendMessage(Message message) throws IOException {
        System.out.println("Sending message:" + message.toMessageString());
        ((StringOutputChannel) getCommandConnection().getOutputChannel()).sendMessage(message);
    }

    public void shutdown() {
        this.shutdown = true;
        setUserState(UserState.DISCONNECTED);
    }

    public UserState getUserState() {
        return userState;
    }

    public void setUserState(UserState userState) {
        this.userState = userState;
    }

    public int getDataPort() {
        return dataPort;
    }

    public void setDataPort(int dataPort) {
        this.dataPort = dataPort;
    }

    public boolean isRunning() {
        return running;
    }

    public Message readNextMessage() throws IOException {
        String line = getCommandConnection().getInputChannel().readln();
        return MessageFactory.getInstance().createMessageFromString(line);
    }

    @Deprecated
    public void displayDir(String dir) {
        if (running) {
            RequestDirMessage outgoing = MessageFactory.getInstance().createRequestDirMessage(dir);
            System.out.println("Sending command:" + outgoing.toMessageString());
            try {
                ((StringOutputChannel) getCommandConnection().getOutputChannel()).sendMessage(outgoing);
            } catch (IOException e) {
                e.printStackTrace();
            }
            String line = "";
            do {
                try {
                    line = getCommandConnection().getInputChannel().readln();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println(line);
            } while ((line != "") && (!line.endsWith("done")));
        } else {
            System.out.println("Host Thread not running");
        }
    }

    public GetDirHandle handleGetDir(String path, HandleListener listener) {
        GetDirHandle handle = new GetDirHandle(path);
        handle.addHandleListener(listener);
        handleQueue.enter(handle);
        return handle;
    }

    public SendFileHandle handleSendFile(String remotePath) {
        SendFileHandle handle = new SendFileHandle(remotePath);
        handleQueue.enter(handle);
        return handle;
    }

    public SendThumbHandle handleSendThumb(String remotePath) {
        SendThumbHandle handle = new SendThumbHandle(remotePath);
        handleQueue.enter(handle);
        return handle;
    }

    public FilesystemThumbHandler getThumbHandler() {
        return thumbHandler;
    }

    public Filesystem getThumbCacheFilesystem() {
        return thumbCacheFilesystem;
    }
}
