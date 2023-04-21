package com.bitgate.util.peer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import com.bitgate.server.stream.StreamTransmitter;
import com.bitgate.util.broadcast.BroadcastID;
import com.bitgate.util.broadcast.BroadcastID.DataType;
import com.bitgate.util.broadcast.BroadcastID.MediaType;
import com.bitgate.util.constants.Constants;
import com.bitgate.util.constants.Defines;
import com.bitgate.util.debug.Debug;
import com.bitgate.util.filesystem.FileBuffer;
import com.bitgate.util.packetizer.Packetizer;
import com.bitgate.util.thread.ThreadController;
import com.bitgate.util.thread.ThreadManager;

class PeerFileWorker extends ThreadController {

    private Socket s;

    private BroadcastID bcID;

    private int channelCounter;

    public static final String VERSION = "Silicon 0.1";

    public PeerFileWorker(Socket s) {
        super("PeerFileWorker");
        this.s = s;
        this.channelCounter = 0;
        Debug.debug("Running.");
    }

    public void retrieveFileList() {
        Filelist fl = new Filelist();
        BufferedReader bis;
        try {
            bis = new BufferedReader(new InputStreamReader(s.getInputStream()));
        } catch (Exception e) {
            Debug.debug("Unable to create new BufferedReader stream: " + e.getMessage());
            return;
        }
        Debug.debug("Retrieving filelist.");
        while (true) {
            String filename = null;
            try {
                filename = bis.readLine();
            } catch (Exception e) {
                Debug.debug("Exception during line read ... winging it: " + e.getMessage());
                break;
            }
            if (filename == null) {
                Debug.debug("Line is null; end of stream reached.");
                break;
            }
            Debug.debug("Retrieved file '" + filename + "' for broadcast ID '" + bcID.ID + "'");
            fl.addFile(filename);
        }
        Debug.debug("File list retrieval complete.");
        BroadcastIDManager.getDefault().add(Long.toString(bcID.ID), fl);
        Debug.debug("Starting new broadcast playlist thread.");
        try {
            ThreadManager.getDefault().add(new StreamTransmitter(bcID.ID, channelCounter, bcID.mediaType));
            channelCounter++;
        } catch (Exception e) {
            Debug.debug("Unable to start a StreamTransmitter thread: " + e.getMessage());
        }
    }

    public void retrieveFile() {
        String broadcastDir = null;
        if (Constants.getDefault().getProperties().getProperty(Defines.STRM_SERVER_DIRECTORY) != null) {
            broadcastDir = Constants.getDefault().getProperties().getProperty(Defines.STRM_SERVER_DIRECTORY) + "/";
            if (bcID.streamType == BroadcastID.DataType.PUBLIC || bcID.streamType == BroadcastID.DataType.PLAYLIST) {
                broadcastDir += Constants.getDefault().getProperties().getProperty(Defines.STRM_SERVER_DIRECTORY_BROADCAST) + "/";
            } else {
                broadcastDir += Constants.getDefault().getProperties().getProperty(Defines.STRM_SERVER_DIRECTORY_DEMAND) + "/";
            }
            if (bcID.mediaType == BroadcastID.MediaType.AUDIO) {
                broadcastDir += Constants.getDefault().getProperties().getProperty(Defines.STRM_SERVER_DIRECTORY_AUDIO);
                Debug.debug("File type and stream cache directory set to AUDIO.");
            } else if (bcID.mediaType == BroadcastID.MediaType.VIDEO) {
                broadcastDir += Constants.getDefault().getProperties().getProperty(Defines.STRM_SERVER_DIRECTORY_VIDEO);
                Debug.debug("File type and stream cache directory set to VIDEO.");
            } else if (bcID.mediaType == BroadcastID.MediaType.OTHER) {
                Debug.debug("File type and stream cache directory set to OTHER.");
            }
        } else {
            broadcastDir = Constants.getDefault().getProperties().getProperty(Defines.STRM_SERVER_DIRECTORY) + "/";
            if (bcID.streamType == BroadcastID.DataType.PUBLIC || bcID.streamType == BroadcastID.DataType.PLAYLIST) {
                broadcastDir += Constants.getDefault().getProperties().getProperty(Defines.STRM_SERVER_DIRECTORY_BROADCAST) + "/";
            } else {
                broadcastDir += Constants.getDefault().getProperties().getProperty(Defines.STRM_SERVER_DIRECTORY_DEMAND) + "/";
            }
            if (bcID.mediaType == BroadcastID.MediaType.AUDIO) {
                broadcastDir += Constants.getDefault().getProperties().getProperty(Defines.STRM_SERVER_DIRECTORY_AUDIO);
                Debug.debug("File type and stream cache directory set to AUDIO.");
            } else if (bcID.mediaType == BroadcastID.MediaType.VIDEO) {
                broadcastDir += Constants.getDefault().getProperties().getProperty(Defines.STRM_SERVER_DIRECTORY_VIDEO);
                Debug.debug("File type and stream cache directory set to VIDEO.");
            } else if (bcID.mediaType == BroadcastID.MediaType.OTHER) {
                Debug.debug("File type and stream cache directory set to OTHER.");
            }
        }
        String writeFile = broadcastDir + "/" + bcID.filename;
        File file;
        FileBuffer fb;
        byte data[];
        int bytesRead, totalBytesRead;
        long startTime, endTime;
        int bytesPerRead = Integer.parseInt(Constants.getDefault().getProperties().getProperty(Defines.STRM_SERVER_TWEAK_READCHUNKSIZE));
        if (Constants.getDefault().getProperties().getProperty(Defines.STRM_SERVER_TWEAK_READCHUNKSIZE) != null) {
            bytesPerRead = Integer.parseInt(Constants.getDefault().getProperties().getProperty(Defines.STRM_SERVER_TWEAK_READCHUNKSIZE));
        } else {
            bytesPerRead = 65536;
        }
        startTime = System.currentTimeMillis();
        totalBytesRead = 0;
        endTime = 0L;
        try {
            fb = new FileBuffer(writeFile, bytesPerRead);
        } catch (Exception e) {
            Debug.debug("Unable to create FileBuffer object: " + e.getMessage());
            return;
        }
        data = new byte[bytesPerRead];
        Debug.debug("File opened for writing: " + writeFile + " bytes per chunk=" + bytesPerRead);
        while (true) {
            try {
                bytesRead = s.getInputStream().read(data);
            } catch (Exception e) {
                Debug.debug("Unable to read data from stream: " + e.getMessage());
                break;
            }
            if (bytesRead <= 0) {
                Debug.debug("File write of " + writeFile + " completed.");
                break;
            }
            totalBytesRead += bytesRead;
            try {
                fb.add(data, bytesRead);
            } catch (Exception e) {
                Debug.debug("Unable to write data to file: " + e.getMessage());
                break;
            }
        }
        try {
            s.close();
            fb.close();
        } catch (Exception e) {
            Debug.debug("Unable to close streams: " + e.getMessage());
        }
        endTime = System.currentTimeMillis();
        Debug.debug("Stats: FILE=" + writeFile + " SIZE=" + totalBytesRead + " XFERTime=" + (endTime - startTime) + "ms (" + ((endTime - startTime) / 1000) + "s)");
    }

    public void process() {
        try {
            bcID = Packetizer.get(s);
        } catch (Exception e) {
            Debug.debug("Packetizer fetch failed: " + e.getMessage());
            return;
        }
        Debug.debug("Broadcast Data Follows:");
        Debug.debug("ID=" + bcID.ID + " FILENAME=" + bcID.filename + " FILESIZE=" + bcID.filesize + " MEDIA=" + bcID.mediaType + " STREAM=" + bcID.streamType);
        if ((bcID.filename == null && bcID.filesize == 0) || bcID.streamType == BroadcastID.DataType.PLAYLIST) {
            retrieveFileList();
        } else if (bcID.filename != null) {
            retrieveFile();
        }
        setControllerInactive();
    }
}

class PeerFileServer extends ThreadController {

    private String serverName;

    private ServerSocket ss;

    private int port;

    private ThreadManager tManager;

    public PeerFileServer(String serverName, int port) {
        super("PeerFileServer: Listening on '" + serverName + ":" + port + "'");
        this.serverName = serverName;
        this.port = port;
        try {
            ss = new ServerSocket(port, 10, InetAddress.getByName(serverName));
        } catch (Exception e) {
            Debug.debug("PeerFileServer: Unable to start server: " + e.getMessage());
            setControllerInactive();
        }
        Debug.debug("Started.  Port=" + port + ", Host=" + serverName);
    }

    public void process() {
        Socket s = null;
        try {
            s = ss.accept();
        } catch (Exception e) {
            Debug.debug("Could not accept incoming socket request: " + e.getMessage());
        }
        try {
            s.setSoTimeout(0);
        } catch (Exception e) {
            Debug.debug("Unable to set socket timeout: " + e.getMessage());
        }
        try {
            ThreadManager.getDefault().add(new PeerFileWorker(s));
        } catch (Exception e) {
            Debug.debug("Unable to add new PeerFileWorker: " + e.getMessage());
        }
    }
}

class PeerSocket extends ThreadController {

    private Socket s;

    private BufferedReader is;

    private PrintStream ps;

    private String serverAddress;

    public PeerSocket(Socket s, String serverAddress) {
        super("PeerSocket: Connected via server address '" + serverAddress + "' from '" + s.getInetAddress().getHostAddress() + "'");
        this.s = s;
        this.serverAddress = serverAddress;
        try {
            is = new BufferedReader(new InputStreamReader(s.getInputStream()));
            ps = new PrintStream(s.getOutputStream());
        } catch (Exception e) {
            Debug.debug("Unable to create BufferedReader/PrintStream classes: " + e.getMessage());
            setControllerInactive();
        }
        Debug.debug("Incoming connection from '" + s.getInetAddress().getHostAddress() + "'");
    }

    public void sendStreamFile(String media, String file) {
    }

    public void process() {
        while (true) {
            String line = null;
            try {
                line = is.readLine();
            } catch (Exception e) {
                Debug.debug("Unable to read line from socket: " + e.getMessage());
                break;
            }
            if (line == null) {
                Debug.debug("Socket connection closed from remote end.");
                break;
            }
            Debug.debug("Got request '" + line + "'");
            if (line.startsWith("WHOHAS")) {
                if (line.indexOf("(") == -1 || line.indexOf(")") == -1) {
                    ps.println("IR");
                    continue;
                }
                String mediaFormat = line.substring(line.indexOf("(") + 1, line.indexOf(")"));
                String whohasFile = line.substring(line.indexOf(" ") + 1);
                String whohas = null;
                Debug.debug("Request of who has file '" + whohasFile + "', media type='" + mediaFormat + "'");
                if (mediaFormat.equalsIgnoreCase("audio")) {
                    whohas = DemandFileList.whoHasAudio(whohasFile);
                } else if (mediaFormat.equalsIgnoreCase("video")) {
                    whohas = DemandFileList.whoHasVideo(whohasFile);
                } else {
                    whohas = "UNKNOWNFORMAT";
                }
                ps.println(whohas);
            } else if (line.startsWith("IHAVE")) {
                if (line.indexOf("(") == -1 || line.indexOf(")") == -1 || line.indexOf(" ") == -1 || line.lastIndexOf(" ") == -1 || line.indexOf(" ") == line.lastIndexOf(" ")) {
                    ps.println("IR");
                    continue;
                }
                String mediaFormat = line.substring(line.indexOf("(") + 1, line.indexOf(")"));
                String whohasFile = line.substring(line.indexOf(" ") + 1, line.lastIndexOf(" "));
                String whohasIP = line.substring(line.lastIndexOf(" ") + 1);
                Debug.debug("Setting IHAVE for file '" + whohasFile + "', media type='" + mediaFormat + "', to " + "'" + whohasIP + "'");
                if (mediaFormat.equalsIgnoreCase("audio")) {
                    DemandFileList.whoHasAudio(whohasFile, whohasIP);
                    ps.println("OK");
                } else if (mediaFormat.equalsIgnoreCase("video")) {
                    DemandFileList.whoHasAudio(whohasFile, whohasIP);
                    ps.println("OK");
                } else {
                    ps.println("UNKNOWNFORMAT");
                }
            } else if (line.startsWith("SEND")) {
                Socket sock;
                if (line.indexOf("(") == -1 || line.indexOf(")") == -1 || line.indexOf(",") == -1 || line.indexOf(" ") == -1) {
                    ps.println("IR");
                    continue;
                }
                String mediaFormat = line.substring(line.indexOf("(") + 1, line.indexOf(","));
                String mediaType = line.substring(line.indexOf(",") + 1, line.indexOf(")"));
                String whohasFile = line.substring(line.indexOf(" ") + 1);
                String broadcastDirectory = null;
                if (Constants.getDefault().getProperties().getProperty(Defines.DIST_SERVER_DIRECTORY) != null) {
                    broadcastDirectory = Constants.getDefault().getProperties().getProperty(Defines.DIST_SERVER_DIRECTORY) + "/";
                } else {
                    broadcastDirectory = Constants.getDefault().getProperties().getProperty(Defines.STRM_SERVER_DIRECTORY) + "/";
                }
                DataType streamType = DataType.PUBLIC;
                MediaType streamMediaType = MediaType.OTHER;
                BroadcastID bcID = null;
                Debug.debug("Requesting send for file '" + whohasFile + "', mediaFormat='" + mediaFormat + "', " + "mediaType='" + mediaType + "'");
                if (mediaType.equalsIgnoreCase("broadcast")) {
                    if (Constants.getDefault().getProperties().getProperty(Defines.DIST_SERVER_DIRECTORY_BROADCAST) != null) {
                        broadcastDirectory += Constants.getDefault().getProperties().getProperty(Defines.DIST_SERVER_DIRECTORY_BROADCAST) + "/";
                    } else {
                        broadcastDirectory += Constants.getDefault().getProperties().getProperty(Defines.STRM_SERVER_DIRECTORY_BROADCAST) + "/";
                    }
                    streamType = BroadcastID.DataType.PUBLIC;
                } else if (mediaType.equalsIgnoreCase("ondemand")) {
                    if (Constants.getDefault().getProperties().getProperty(Defines.DIST_SERVER_DIRECTORY_DEMAND) != null) {
                        broadcastDirectory += Constants.getDefault().getProperties().getProperty(Defines.DIST_SERVER_DIRECTORY_DEMAND) + "/";
                    } else {
                        broadcastDirectory += Constants.getDefault().getProperties().getProperty(Defines.STRM_SERVER_DIRECTORY_DEMAND) + "/";
                    }
                    streamType = BroadcastID.DataType.DEMAND;
                }
                if (mediaFormat.equalsIgnoreCase("audio")) {
                    if (Constants.getDefault().getProperties().getProperty(Defines.DIST_SERVER_DIRECTORY_AUDIO) != null) {
                        broadcastDirectory += Constants.getDefault().getProperties().getProperty(Defines.DIST_SERVER_DIRECTORY_AUDIO) + "/";
                    } else {
                        broadcastDirectory += Constants.getDefault().getProperties().getProperty(Defines.STRM_SERVER_DIRECTORY_AUDIO) + "/";
                    }
                    streamMediaType = BroadcastID.MediaType.AUDIO;
                } else if (mediaFormat.equalsIgnoreCase("video")) {
                    if (Constants.getDefault().getProperties().getProperty(Defines.DIST_SERVER_DIRECTORY_VIDEO) != null) {
                        broadcastDirectory += Constants.getDefault().getProperties().getProperty(Defines.DIST_SERVER_DIRECTORY_VIDEO) + "/";
                    } else {
                        broadcastDirectory += Constants.getDefault().getProperties().getProperty(Defines.STRM_SERVER_DIRECTORY_VIDEO) + "/";
                    }
                    streamMediaType = BroadcastID.MediaType.VIDEO;
                }
                broadcastDirectory += whohasFile;
                File file = new File(broadcastDirectory);
                Debug.debug("Filename='" + broadcastDirectory + "'");
                if (!file.exists()) {
                    ps.println("NE");
                    continue;
                }
                bcID = new BroadcastID(System.currentTimeMillis(), null, whohasFile, file.length(), streamMediaType, streamType);
                FileInputStream is = null;
                try {
                    is = new FileInputStream(file);
                } catch (Exception e) {
                    Debug.debug("Cannot create input file connection: " + e.getMessage());
                    ps.println("IE");
                    continue;
                }
                byte data[];
                Debug.debug("Sending file, length=" + file.length());
                try {
                    sock = new Socket(s.getInetAddress().getHostAddress(), Defines.BROADCAST_SCHEDULED_STREAM_PORT);
                    PeerStatistics.getDefault().peerActive();
                } catch (Exception e) {
                    Debug.debug("Could not connect to server " + s.getInetAddress().getHostAddress() + ": " + e.getMessage());
                    ps.println("IE");
                    continue;
                }
                Packetizer.send(sock, bcID);
                Debug.debug("Broadcast ID (sending to client) is as follows:");
                Debug.debug("ID=" + bcID.ID + " PASS=" + bcID.password + " FILE=" + bcID.filename + " SIZE=" + bcID.filesize + " MEDIA=" + bcID.mediaType + " STREAM=" + bcID.streamType);
                data = new byte[32768];
                int totalBytesSent = 0;
                OutputStream os = null;
                try {
                    os = sock.getOutputStream();
                } catch (Exception e) {
                    Debug.debug("Cannot get output stream: " + e.getMessage());
                    ps.println("IE");
                    setControllerInactive();
                }
                ps.println("OK");
                while (true) {
                    int bytesRead;
                    try {
                        bytesRead = is.read(data);
                    } catch (Exception e) {
                        Debug.debug("Unable to read data from file: " + e.getMessage());
                        break;
                    }
                    if (bytesRead <= 0) {
                        Debug.debug("File send of " + broadcastDirectory + " complete.");
                        break;
                    }
                    totalBytesSent += bytesRead;
                    try {
                        os.write(data, 0, bytesRead);
                        os.flush();
                    } catch (Exception e) {
                        Debug.debug("Unable to stream data to peer: " + e.getMessage());
                        break;
                    }
                }
                Debug.debug("Statistic: File '" + broadcastDirectory + "': total bytes sent=" + totalBytesSent);
                try {
                    os.flush();
                    is.close();
                    os.close();
                    PeerStatistics.getDefault().peerNotActive();
                } catch (Exception e) {
                    Debug.debug("Unable to close output streams: " + e.getMessage());
                }
            } else if (line.equalsIgnoreCase("QUIT")) {
                break;
            }
        }
        try {
            s.close();
        } catch (Exception e) {
            Debug.debug("Cannot close socket.");
        }
        setControllerInactive();
    }
}

/**
 * This is the Server Class for the Peering Server.
 *
 * @author Kenji Hollis &lt;kenji@nuklees.com&gt;
 * @version $Id: //depot/nuklees/util/peer/Server.java#11 $
 */
public class Server extends ThreadController {

    private int serverMode;

    private ServerSocket server;

    private String serverAddress;

    public static final int MODE_DISTRIBUTION = 1;

    public static final int MODE_STREAM = 2;

    /**
     * This is the constructor.  The <code>mode</code> specifies the type of Peering server to start.
     *
     * @param mode <code>MODE_DISTRIBUTION</code> for Distribution, <code>MODE_STREAM</code> for Streaming.
     * @throws Exception On any errors.
     */
    public Server(int mode) {
        super("Peer Server: Mode '" + mode + "'");
        if (Constants.getDefault().getProperties().get(Defines.ENABLE_PEER) != null) {
            if (((String) Constants.getDefault().getProperties().get(Defines.ENABLE_PEER)).equalsIgnoreCase("true")) {
                serverAddress = null;
                int serverPort = 0;
                DemandFileList.getDefault().instantiate();
                if (mode == MODE_STREAM) {
                    serverAddress = Constants.getDefault().getProperties().getProperty(Defines.PEER_PORT_STREAM_ADDRESS);
                    serverPort = Integer.parseInt(Constants.getDefault().getProperties().getProperty(Defines.PEER_PORT_STREAM_PORT));
                } else {
                    String svr = Constants.getDefault().getProperties().getProperty(Defines.PEER_PORT_DISTRIBUTION);
                    if (svr.indexOf(':') != -1) {
                        serverAddress = svr.substring(0, svr.indexOf(':'));
                        serverPort = Integer.parseInt(svr.substring(svr.indexOf(':') + 1));
                    } else {
                        Debug.debug("Server: Cannot start peering server: Malformed Address: " + svr + " (requires port number)");
                        setControllerInactive();
                    }
                }
                serverMode = mode;
                try {
                    server = new ServerSocket(serverPort, 10, InetAddress.getByName(serverAddress));
                } catch (Exception e) {
                    Debug.debug("Server: Cannot assign address " + serverAddress + ":" + serverPort);
                    setControllerInactive();
                }
                Debug.debug("Peering server listening on '" + serverAddress + ":" + serverPort + "'");
                try {
                    ThreadManager.getDefault().add(new PeerFileServer(serverAddress, Defines.BROADCAST_LIST_PORT));
                    ThreadManager.getDefault().add(new PeerFileServer(serverAddress, Defines.BROADCAST_SCHEDULED_STREAM_PORT));
                } catch (Exception e) {
                    Debug.debug("Unable to start PeerFileServer: " + e.getMessage());
                    setControllerInactive();
                }
            } else {
                Debug.debug("Peering not enabled (enable.peer not present or set to false.)");
                setControllerInactive();
            }
        }
    }

    /**
     * This starts the server.
     */
    public void process() {
        Socket s = null;
        if (server == null) {
            Debug.debug("Unable to properly start peering server.  Stopping listener.");
            this.setControllerInactive();
            return;
        }
        try {
            s = server.accept();
        } catch (Exception e) {
            Debug.debug("Could not accept incoming socket request: " + e);
        }
        try {
            s.setSoTimeout(0);
        } catch (Exception e) {
            Debug.debug("Unable to set socket timeout: " + e.getMessage());
        }
        try {
            ThreadManager.getDefault().add(new PeerSocket(s, serverAddress));
        } catch (Exception e) {
            Debug.debug("Unable to add new PeerSocket: " + e.getMessage());
        }
    }
}
