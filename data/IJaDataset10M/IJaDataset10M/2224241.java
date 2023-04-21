package game.cliente.connections;

import game.cliente.components.Player;
import game.cliente.utils.Util;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.PasswordAuthentication;
import java.nio.ByteBuffer;
import java.sql.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;
import com.sun.sgs.client.ClientChannel;
import com.sun.sgs.client.ClientChannelListener;
import com.sun.sgs.client.simple.SimpleClient;
import com.sun.sgs.client.simple.SimpleClientListener;

/**
 * 
 * @author Michel Montenegro
 * 
 */
public class HostConnect implements SimpleClientListener {

    /** The version of the serialized form of this class. */
    private static final long serialVersionUID = 1L;

    /** The name of the host property. */
    public static final String HOST_PROPERTY = "tutorial.host";

    public void setReceivedMessage(String receivedMessage) {
        this.receivedMessage = receivedMessage;
    }

    public void setReceivedChannelMessage(String receivedChannelMessage) {
        this.receivedChannelMessage = receivedChannelMessage;
    }

    /** The default hostname. */
    public static final String DEFAULT_HOST = "127.0.0.1";

    /** The name of the port property. */
    public static final String PORT_PROPERTY = "tutorial.port";

    /** The default port. */
    public static final String DEFAULT_PORT = "1139";

    /** The message encoding. */
    public static final String MESSAGE_CHARSET = "UTF-8";

    /** The {@link SimpleClient} instance for this client. */
    protected final SimpleClient simpleClient;

    private String receivedMessage;

    private String receivedChannelMessage;

    private String login;

    private Player player;

    /** Sequence generator for counting channels. */
    protected final AtomicInteger channelNumberSequence = new AtomicInteger(1);

    /** Map that associates a channel name with a {@link ClientChannel}. */
    protected final Map<String, ClientChannel> channelsByName = new HashMap<String, ClientChannel>();

    public HostConnect() {
        simpleClient = new SimpleClient(this);
        this.player = new Player();
    }

    /**
	 * Initiates asynchronous login to the SGS server specified by the host and
	 * port properties.
	 */
    public void login() {
        String host = System.getProperty(HOST_PROPERTY, DEFAULT_HOST);
        String port = System.getProperty(PORT_PROPERTY, DEFAULT_PORT);
        try {
            Properties connectProps = new Properties();
            connectProps.put("host", host);
            connectProps.put("port", port);
            simpleClient.login(connectProps);
        } catch (Exception e) {
            e.printStackTrace();
            disconnected(false, e.getMessage());
        }
    }

    /**
	 * Encodes a {@code String} into a {@link ByteBuffer}.
	 * 
	 * @param s
	 *            the string to encode
	 * @return the {@code ByteBuffer} which encodes the given string
	 */
    protected static ByteBuffer encodeString(String s) {
        try {
            return ByteBuffer.wrap(s.getBytes(MESSAGE_CHARSET));
        } catch (UnsupportedEncodingException e) {
            throw new Error("Required character set " + MESSAGE_CHARSET + " not found", e);
        }
    }

    /**
	 * Decodes a {@link ByteBuffer} into a {@code String}.
	 * 
	 * @param buf
	 *            the {@code ByteBuffer} to decode
	 * @return the decoded string
	 */
    protected static String decodeString(ByteBuffer buf) {
        try {
            byte[] bytes = new byte[buf.remaining()];
            buf.get(bytes);
            return new String(bytes, MESSAGE_CHARSET);
        } catch (UnsupportedEncodingException e) {
            throw new Error("Required character set " + MESSAGE_CHARSET + " not found", e);
        }
    }

    @Override
    public void disconnected(boolean graceful, String reason) {
        System.out.println(">>>>>>>>>> Disconnected: " + reason);
    }

    @Override
    public ClientChannelListener joinedChannel(ClientChannel channel) {
        String channelName = channel.getName();
        channelsByName.put(channelName, channel);
        return new HostConnectChannelListener();
    }

    @Override
    public void reconnected() {
        System.out.println("reconnected");
    }

    @Override
    public void reconnecting() {
        System.out.println("reconnecting");
    }

    @Override
    public void receivedMessage(ByteBuffer message) {
        receivedMessage = decodeString(message);
        System.out.println("Server sent: " + receivedMessage);
        String[] msg = receivedMessage.split("/");
        if (msg[0].equalsIgnoreCase("loadPlayer")) {
            this.player.setId(Integer.valueOf(msg[1]));
            this.player.setName(msg[2]);
            this.player.setLoginId(Integer.valueOf(msg[3]));
            this.player.setMapId(Integer.valueOf(msg[4]));
            this.player.setClasseId(Integer.valueOf(msg[5]));
            this.player.setHpMax(Integer.valueOf(msg[6]));
            this.player.setHpCurr(Integer.valueOf(msg[7]));
            this.player.setManaMax(Integer.valueOf(msg[6]));
            this.player.setManaCurr(Integer.valueOf(msg[7]));
            this.player.setExpMax(Integer.valueOf(msg[10]));
            this.player.setExpCurr(Integer.valueOf(msg[11]));
            this.player.setSp(Integer.valueOf(msg[12]));
            this.player.setStr(Integer.valueOf(msg[13]));
            this.player.setDex(Integer.valueOf(msg[14]));
            this.player.setInte(Integer.valueOf(msg[15]));
            this.player.setCon(Integer.valueOf(msg[16]));
            this.player.setCha(Integer.valueOf(msg[17]));
            this.player.setWis(Integer.valueOf(msg[18]));
            this.player.setStamina(Integer.valueOf(msg[19]));
            this.player.setSex(msg[20]);
            this.player.setResMagic(Integer.valueOf(msg[21]));
            this.player.setResPhysical(Integer.valueOf(msg[22]));
            this.player.setEvasion(Integer.valueOf(msg[23]));
            this.player.setDateCreate(Date.valueOf(msg[24]));
            this.player.setOnLine(msg[25]);
            this.player.setLastAcess(Date.valueOf(msg[26]));
            this.player.setSector(Integer.valueOf(msg[27]));
            this.player.setNameClasse(msg[28]);
            this.player.setRace(msg[29]);
            this.player.setStartTileHeroPosX(Integer.valueOf(msg[30]));
            this.player.setStartTileHeroPosY(Integer.valueOf(msg[31]));
            this.player.setPosition(Integer.valueOf(msg[32]));
            Util.CHANNEL_MAP = "map_" + player.getMapId();
        }
    }

    /**
	 * Encodes the given text and sends it to the server.
	 * 
	 * @param text
	 *            the text to send.
	 */
    public String send(String text) {
        try {
            ByteBuffer message = encodeString(text);
            simpleClient.send(message);
            return receivedMessage;
        } catch (Exception e) {
            e.printStackTrace();
            return "<Vazio>";
        }
    }

    public void sendChannel(String text, String channelName) {
        ClientChannel channel = channelsByName.get(channelName);
        try {
            channel.send(encodeString(text));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public PasswordAuthentication getPasswordAuthentication() {
        String player = "player1";
        String password = "player";
        this.login = player;
        System.out.println("Logging in as " + player);
        return new PasswordAuthentication(player, password.toCharArray());
    }

    @Override
    public void loggedIn() {
        System.out.println("Estou logado");
    }

    @Override
    public void loginFailed(String arg0) {
        System.out.println("Error Loggin");
    }

    public String getReceivedMessage() {
        return receivedMessage;
    }

    public String getReceivedChannelMessage() {
        return receivedChannelMessage;
    }

    public String getLogin() {
        return login;
    }

    /**
	 * A simple listener for channel events.
	 */
    public class HostConnectChannelListener implements ClientChannelListener {

        /**
		 * An example of per-channel state, recording the number of channel
		 * joins when the client joined this channel.
		 */
        private final int channelNumber;

        /**
		 * Creates a new {@code HelloChannelListener}. Note that the listener
		 * will be given the channel on its callback methods, so it does not
		 * need to record the channel as state during the join.
		 */
        public HostConnectChannelListener() {
            channelNumber = channelNumberSequence.getAndIncrement();
        }

        /**
		 * {@inheritDoc}
		 * <p>
		 * Displays a message when this client leaves a channel.
		 */
        public void leftChannel(ClientChannel channel) {
            System.out.println("Removed from channel " + channel.getName());
        }

        /**
		 * {@inheritDoc}
		 * <p>
		 * Formats and displays messages received on a channel.
		 */
        public void receivedMessage(ClientChannel channel, ByteBuffer message) {
            receivedChannelMessage = decodeString(message);
            System.out.println("[" + channel.getName() + "/ " + channelNumber + "] " + receivedChannelMessage);
        }
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}
