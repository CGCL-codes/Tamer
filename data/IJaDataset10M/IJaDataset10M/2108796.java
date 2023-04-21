package semix2.impl.robot;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Level;
import java.util.logging.Logger;
import semix2.robot.Command;
import semix2.robot.DataPacket;
import semix2.robot.DataPacketHandler;
import semix2.robot.DataPacketIterator;
import semix2.robot.Parameter;
import semix2.robot.ParameterBuilder;
import semix2.robot.RobotClient;

public class RobotClientImpl implements RobotClient, DataPacketHandler {

    private static enum State {

        NO_CONNECTION, FAILED_CONNECTION, OPENED_SOCKET, EXCHANGED_INTRODUCTIONS, REJECTED, WAITING_COMMAND_LIST, CONNECTED, LOST_CONNECTION
    }

    private final AtomicReference<State> _state;

    private final Map<Integer, CommandImpl> _commandMap;

    private final Map<Integer, List<DataPacketHandler>> _handlerMap;

    private final Logger _logger;

    private String _user;

    private String _password;

    private String _serverKey;

    private Thread _udpPacketReaderThread;

    private DatagramSocket _udpSocket;

    private Thread _tcpPacketReaderThread;

    private Socket _tcpSocket;

    private OutputStream _tcpOut;

    private long _authKey;

    private long _introKey;

    private byte[] _clientKey;

    private String _host;

    private int _port;

    public RobotClientImpl() {
        _state = new AtomicReference<State>(State.NO_CONNECTION);
        _commandMap = new HashMap<Integer, CommandImpl>();
        _handlerMap = new HashMap<Integer, List<DataPacketHandler>>();
        _user = "";
        _password = "";
        _serverKey = "";
        _host = DEFAULT_HOST;
        _port = DEFAULT_PORT;
        _logger = Logger.getLogger(RobotClient.class.getName());
    }

    public void setUser(String user) {
        _user = (user != null) ? user : "";
    }

    public void setPassword(String password) {
        _password = (password != null) ? password : "";
    }

    public void setServerKey(String serverKey) {
        _serverKey = (serverKey != null) ? serverKey : "";
    }

    public void setHost(String host) {
        _host = (host != null) ? host : DEFAULT_HOST;
    }

    public void setPort(int port) {
        _port = (port > 0) ? port : DEFAULT_PORT;
    }

    public byte[] getClientKey() {
        return _clientKey.clone();
    }

    public boolean connect() {
        State state = getState();
        if (state == State.LOST_CONNECTION) {
            _logger.log(Level.INFO, "Trying a connection on a client that'd lost connection.");
            disconnect(State.LOST_CONNECTION);
            return false;
        }
        if (state != State.NO_CONNECTION && state != State.FAILED_CONNECTION) {
            _logger.log(Level.INFO, "Connection already established or being connected.");
            return true;
        }
        if (!initTCPSocketAndStartPacketReader()) {
            _logger.log(Level.SEVERE, "Cannot initiate TCP socket.");
            disconnect(State.FAILED_CONNECTION);
            return false;
        }
        state = getState();
        while (state != State.REJECTED && state != State.CONNECTED && state != State.NO_CONNECTION) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException ignore) {
            }
            state = getState();
        }
        if (state != State.CONNECTED) {
            _logger.log(Level.SEVERE, "Cannot connect to server");
            disconnect(state);
            return false;
        }
        _logger.log(Level.INFO, "Connected");
        return true;
    }

    private boolean initTCPSocketAndStartPacketReader() {
        InputStream in = null;
        try {
            _tcpSocket = new Socket(_host, _port);
            _tcpOut = _tcpSocket.getOutputStream();
            in = _tcpSocket.getInputStream();
        } catch (Exception e) {
            _logger.log(Level.SEVERE, e.getLocalizedMessage(), e);
            return false;
        }
        setState(State.OPENED_SOCKET);
        _tcpPacketReaderThread = new Thread(new TCPPacketReader(in, this));
        _tcpPacketReaderThread.start();
        return true;
    }

    private boolean initUDPSocketAndStartPacketReader(InetAddress host, int serverReportedUDPPort) {
        try {
            _udpSocket = new DatagramSocket();
        } catch (Exception e) {
            _logger.log(Level.SEVERE, e.getLocalizedMessage(), e);
            return false;
        }
        _udpPacketReaderThread = new Thread(new UDPPacketReader(_udpSocket, host, serverReportedUDPPort, this));
        _udpPacketReaderThread.start();
        return true;
    }

    private boolean sendTCPPacket(DataPacket packet) {
        byte[] packetBytes = packet.getBytes();
        try {
            _tcpOut.write(packetBytes, 0, packetBytes.length);
        } catch (IOException e) {
            _logger.log(Level.SEVERE, e.getLocalizedMessage(), e);
            return false;
        }
        return true;
    }

    private void sendUDPPacket(DataPacket packet) {
        byte[] packetBytes = packet.getBytes();
        try {
            DatagramPacket udpPacket = new DatagramPacket(packetBytes, 0, packetBytes.length);
            udpPacket.setAddress(_tcpSocket.getInetAddress());
            udpPacket.setPort(_tcpSocket.getPort());
            _udpSocket.send(udpPacket);
        } catch (IOException e) {
            _logger.log(Level.SEVERE, e.getLocalizedMessage(), e);
        }
    }

    public void disconnect() {
        disconnect(State.NO_CONNECTION);
    }

    private void disconnect(State newState) {
        if (_udpSocket != null) {
            _udpSocket.close();
        }
        if (_tcpSocket != null) {
            try {
                _tcpSocket.close();
            } catch (IOException ignore) {
            }
        }
        if (_udpPacketReaderThread != null) {
            _udpPacketReaderThread.interrupt();
            try {
                _udpPacketReaderThread.join();
            } catch (InterruptedException ignore) {
            }
        }
        if (_tcpPacketReaderThread != null) {
            _tcpPacketReaderThread.interrupt();
            try {
                _tcpPacketReaderThread.join();
            } catch (InterruptedException ignore) {
            }
        }
        _udpPacketReaderThread = null;
        _udpSocket = null;
        _tcpPacketReaderThread = null;
        _tcpSocket = null;
        _tcpOut = null;
        _commandMap.clear();
        setState(newState);
        _logger.log(Level.INFO, "Disconnected");
    }

    private void setState(State state) {
        _state.set(state);
    }

    private State getState() {
        return _state.get();
    }

    public Command getCommand(String name) {
        synchronized (_commandMap) {
            for (Command command : _commandMap.values()) {
                if (command.getName().equals(name)) {
                    return command;
                }
            }
        }
        return null;
    }

    public Command getCommand(int commandId) {
        synchronized (_commandMap) {
            return _commandMap.get(commandId);
        }
    }

    private static final Command[] EMPTY_COMMAND = new Command[0];

    public Command[] getAllCommands() {
        synchronized (_commandMap) {
            return _commandMap.values().toArray(EMPTY_COMMAND);
        }
    }

    public void addDataPacketHandler(Command command, DataPacketHandler handler) {
        addDataPacketHandler(command.getId(), handler);
    }

    public void addDataPacketHandler(int commandId, DataPacketHandler handler) {
        List<DataPacketHandler> handlerList = null;
        synchronized (_handlerMap) {
            handlerList = _handlerMap.get(commandId);
            if (handlerList == null) {
                handlerList = new CopyOnWriteArrayList<DataPacketHandler>();
                _handlerMap.put(commandId, handlerList);
            }
        }
        handlerList.add(handler);
    }

    public void removeDataPacketHandler(Command command, DataPacketHandler handler) {
        removeDataPacketHandler(command.getId(), handler);
    }

    public void removeDataPacketHandler(int commandId, DataPacketHandler handler) {
        List<DataPacketHandler> handlerList = null;
        synchronized (_handlerMap) {
            handlerList = _handlerMap.get(commandId);
        }
        if (handlerList != null) {
            handlerList.remove(handler);
        }
    }

    public boolean request(Command command) {
        return request(command.getId(), null);
    }

    public boolean request(int commandId) {
        return request(commandId, null);
    }

    public boolean request(Command command, int mSec) {
        return request(command.getId(), null, mSec);
    }

    public boolean request(int commandId, int mSec) {
        return request(commandId, null, mSec);
    }

    public boolean request(Command command, Parameter parameter) {
        return request(command.getId(), parameter);
    }

    public boolean request(int commandId, Parameter parameter) {
        DataPacket packet = new DataPacketImpl(commandId, parameter);
        return sendTCPPacket(packet);
    }

    public boolean request(Command command, Parameter parameter, int mSec) {
        return request(command.getId(), parameter, mSec);
    }

    public boolean request(int commandId, Parameter parameter, int mSec) {
        ParameterBuilder builder = new ParameterBuilder();
        builder.appendUByte2(commandId);
        builder.appendByte4(mSec);
        if (parameter != null) {
            byte[] paramBytes = parameter.getBytes();
            for (int i = 0, n = parameter.getLength(); i < n; i++) {
                builder.appendByte(paramBytes[i]);
            }
        }
        DataPacket packet = new DataPacketImpl(ClientCommandConstants.REQUEST, builder.toParameter());
        return sendTCPPacket(packet);
    }

    public boolean stop(Command command) {
        return stop(command.getId());
    }

    public boolean stop(int commandId) {
        ParameterBuilder builder = new ParameterBuilder();
        builder.appendUByte2(commandId);
        DataPacket packet = new DataPacketImpl(ClientCommandConstants.REQUESTSTOP, builder.toParameter());
        return sendTCPPacket(packet);
    }

    public void handleDataPacket(DataPacket packet) {
        int commandId = packet.getCommandId();
        State state = getState();
        if (commandId == ServerCommandConstants.SHUTDOWN) {
            disconnect(State.LOST_CONNECTION);
        } else if (commandId == ServerCommandConstants.INTRODUCTION) {
            if (state == State.OPENED_SOCKET) {
                handleServerIntroduction(packet);
                setState(State.EXCHANGED_INTRODUCTIONS);
            } else {
                _logger.log(Level.WARNING, "introduction received when not in OPENED_SOCKET.");
            }
        } else if (commandId == ServerCommandConstants.CONNECTED) {
            if (state == State.EXCHANGED_INTRODUCTIONS) {
                handleServerConnected(packet);
                setState(State.WAITING_COMMAND_LIST);
                _logger.log(Level.FINE, "Client now connected to server.");
            } else {
                _logger.log(Level.WARNING, "connected packet received not during EXCHANGED_INTROS.");
            }
        } else if (commandId == ServerCommandConstants.REJECTED) {
            if (state != State.CONNECTED) {
                handleRejected(packet);
                setState(State.REJECTED);
            } else {
                _logger.log(Level.WARNING, "rejected packet received after connected.");
            }
        } else if (commandId == ServerCommandConstants.LIST) {
            handleAddCommandList(packet);
            if (state == State.WAITING_COMMAND_LIST) {
                setState(State.CONNECTED);
            }
        } else if (commandId == ServerCommandConstants.LISTARGRET) {
            handleSetCommandArgReturnDescriptionList(packet);
        } else if (commandId == ServerCommandConstants.LISTGROUPANDFLAGS) {
            handleSetCommandGroupNameAndAddDataFlagList(packet);
        } else if (commandId == ServerCommandConstants.LISTSINGLE) {
            handleAddCommand(packet);
        } else if (commandId == ServerCommandConstants.LISTARGRETSINGLE) {
            handleSetCommandArgReturnDescription(packet);
        } else if (commandId == ServerCommandConstants.LISTGROUPANDFLAGSSINGLE) {
            handleSetCommandGroupNameAndAddDataFlag(packet);
        } else if (commandId == ServerCommandConstants.UDP_INTRODUCTION) {
            if (state == State.CONNECTED || state == State.EXCHANGED_INTRODUCTIONS || state == State.WAITING_COMMAND_LIST) {
                DataPacketIterator iter = packet.getDataPacketIterator();
                int introKey = iter.nextByte4();
                if (_introKey == introKey) {
                    sendTCPPacket(new DataPacketImpl(ClientCommandConstants.UDP_CONFIRMATION));
                } else {
                    _logger.log(Level.WARNING, "Udp introduction packet received with wrong introKey");
                }
            } else {
                _logger.log(Level.WARNING, "Udp introduction packet received while not connected");
            }
        } else if (commandId == ServerCommandConstants.UDP_CONFIRMATION) {
            if (state == State.CONNECTED) {
                _logger.log(Level.INFO, "Udp connection to server confirmed.");
            } else {
                _logger.log(Level.WARNING, "Udp Confirmation received when not in CONNECTED.");
            }
        } else if (commandId == ServerCommandConstants.TCP_ONLY) {
            if (state == State.CONNECTED) {
                _logger.log(Level.INFO, "Client told to only use tcp.");
            }
        } else {
            List<DataPacketHandler> handlerList = null;
            synchronized (_handlerMap) {
                handlerList = _handlerMap.get(commandId);
            }
            if (handlerList != null) {
                for (DataPacketHandler handler : handlerList) {
                    handler.handleDataPacket(packet);
                }
            }
        }
    }

    private void handleServerIntroduction(DataPacket serverPacket) {
        DataPacketIterator iter = serverPacket.getDataPacketIterator();
        String version = iter.nextString();
        int serverReportedUDPPort = iter.nextUByte2();
        _authKey = iter.nextUByte4();
        _introKey = iter.nextUByte4();
        _clientKey = makeClientKey(_authKey, _introKey);
        String passwordKey = iter.nextString();
        _logger.log(Level.INFO, "Connection to version " + version + " with udp port " + serverReportedUDPPort);
        DataPacket packet = null;
        if (initUDPSocketAndStartPacketReader(_tcpSocket.getInetAddress(), serverReportedUDPPort)) {
            ParameterBuilder builder = new ParameterBuilder();
            builder.appendUByte2(_udpSocket.getLocalPort());
            builder.appendString(_user);
            MessageDigest md5 = null;
            try {
                md5 = MessageDigest.getInstance("MD5");
            } catch (NoSuchAlgorithmException ignore) {
            }
            md5.update(_serverKey.getBytes());
            md5.update(passwordKey.getBytes());
            md5.update(_password.getBytes());
            for (byte b : md5.digest()) {
                builder.appendByte(b);
            }
            packet = new DataPacketImpl(ClientCommandConstants.INTRODUCTION, builder.toParameter());
        } else {
            packet = new DataPacketImpl(ClientCommandConstants.TCP_ONLY);
        }
        sendTCPPacket(packet);
    }

    private byte[] makeClientKey(long authKey, long introKey) {
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
        md5.update((byte) ((authKey >> 24) & 0xff));
        md5.update((byte) ((authKey >> 16) & 0xff));
        md5.update((byte) ((authKey >> 8) & 0xff));
        md5.update((byte) (authKey & 0xff));
        md5.update((byte) ((introKey >> 24) & 0xff));
        md5.update((byte) ((introKey >> 16) & 0xff));
        md5.update((byte) ((introKey >> 8) & 0xff));
        md5.update((byte) (introKey & 0xff));
        Random random = new Random();
        int randomNumber = random.nextInt();
        md5.update((byte) ((randomNumber >> 24) & 0xff));
        md5.update((byte) ((randomNumber >> 16) & 0xff));
        md5.update((byte) ((randomNumber >> 8) & 0xff));
        md5.update((byte) (randomNumber & 0xff));
        int time = (int) System.currentTimeMillis();
        md5.update((byte) ((time >> 24) & 0xff));
        md5.update((byte) ((time >> 16) & 0xff));
        md5.update((byte) ((time >> 8) & 0xff));
        md5.update((byte) (time & 0xff));
        return md5.digest();
    }

    private void handleServerConnected(DataPacket serverPacket) {
        ParameterBuilder builder = new ParameterBuilder();
        builder.appendUByte4(_authKey);
        DataPacket packet = new DataPacketImpl(ClientCommandConstants.UDP_INTRODUCTION, builder.toParameter());
        sendUDPPacket(packet);
    }

    private void handleRejected(DataPacket serverPacket) {
        DataPacketIterator iter = serverPacket.getDataPacketIterator();
        int rejected = iter.nextByte2();
        String rejectedStr = iter.nextString();
        if (rejected == 1) {
            _logger.log(Level.WARNING, "Server rejected connection because of bad user/password.");
        } else if (rejected == 2) {
            _logger.log(Level.WARNING, "Server rejected connection since it is using the centralserver at " + rejectedStr + ".");
        } else {
            _logger.log(Level.WARNING, "Server rejected connection for unknown reason " + rejected + " '" + rejectedStr + "'.");
        }
    }

    private void handleAddCommand(DataPacket serverPacket) {
        DataPacketIterator iter = serverPacket.getDataPacketIterator();
        handleAddCommand(iter);
    }

    private void handleAddCommandList(DataPacket serverPacket) {
        DataPacketIterator iter = serverPacket.getDataPacketIterator();
        int listSize = iter.nextUByte2();
        for (int i = 0; i < listSize; i++) {
            handleAddCommand(iter);
        }
    }

    private void handleAddCommand(DataPacketIterator iter) {
        int commandId = iter.nextUByte2();
        String name = iter.nextString();
        String description = iter.nextString();
        CommandImpl command = new CommandImpl(commandId, name, description);
        synchronized (_commandMap) {
            _commandMap.put(commandId, command);
        }
    }

    private void handleSetCommandArgReturnDescription(DataPacket serverPacket) {
        DataPacketIterator iter = serverPacket.getDataPacketIterator();
        handleSetCommandArgReturnDescription(iter);
    }

    private void handleSetCommandArgReturnDescriptionList(DataPacket serverPacket) {
        DataPacketIterator iter = serverPacket.getDataPacketIterator();
        int listSize = iter.nextUByte2();
        for (int i = 0; i < listSize; i++) {
            handleSetCommandArgReturnDescription(iter);
        }
    }

    private void handleSetCommandArgReturnDescription(DataPacketIterator iter) {
        int commandId = iter.nextUByte2();
        String argumentsDescription = iter.nextString();
        String returnValueDescription = iter.nextString();
        synchronized (_commandMap) {
            CommandImpl command = _commandMap.get(commandId);
            if (command != null) {
                command.setArgumentsDescription(argumentsDescription);
                command.setReturnValueDescription(returnValueDescription);
            }
        }
    }

    private void handleSetCommandGroupNameAndAddDataFlag(DataPacket serverPacket) {
        DataPacketIterator iter = serverPacket.getDataPacketIterator();
        handleSetCommandGroupNameAndAddDataFlag(iter);
    }

    private void handleSetCommandGroupNameAndAddDataFlagList(DataPacket serverPacket) {
        DataPacketIterator iter = serverPacket.getDataPacketIterator();
        int listSize = iter.nextUByte2();
        for (int i = 0; i < listSize; i++) {
            handleSetCommandGroupNameAndAddDataFlag(iter);
        }
    }

    private void handleSetCommandGroupNameAndAddDataFlag(DataPacketIterator iter) {
        int commandId = iter.nextUByte2();
        String commandGroup = iter.nextString();
        String dataFlag = iter.nextString();
        synchronized (_commandMap) {
            CommandImpl command = _commandMap.get(commandId);
            if (command != null) {
                command.setCommandGroup(commandGroup);
                command.addDataFlag(dataFlag);
            }
        }
    }
}
