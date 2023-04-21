package net.sf.ninjakore.messagebean.serverTypeBase;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import net.sf.ninjakore.messagebean.common.AbstractMessageBean;
import net.sf.ninjakore.messagebean.common.MessageBean;
import net.sf.ninjakore.messagebean.common.MessageBeanType;

public class MapServerInfoBase extends AbstractMessageBean {

    protected int CHARACTER_ID_SIZE = 4;

    protected int NAME_SIZE = 16;

    protected int IP_SIZE = 4;

    protected int PORT_SIZE = 2;

    private int characterId;

    private String name;

    private byte[] ip;

    private short port;

    @Override
    public MessageBeanType getBeanType() {
        return MessageBeanType.MAP_SERVER_INFO;
    }

    @Override
    public short getHeader() {
        return 0x71;
    }

    @Override
    public MessageBean prototype() {
        return new MapServerInfoBase();
    }

    @Override
    public byte[] serialize() {
        setLength((short) (HEADER_SIZE + CHARACTER_ID_SIZE + NAME_SIZE + IP_SIZE + PORT_SIZE));
        ByteBuffer buffer = ByteBuffer.allocate(getLength()).order(ByteOrder.LITTLE_ENDIAN);
        buffer.putShort(getHeader());
        buffer.putInt(getCharacterId());
        buffer.put(getName().getBytes());
        buffer.put(getIp());
        buffer.putShort(getPort());
        return buffer.array();
    }

    @Override
    public MessageBean unserialize(byte[] bytes) {
        return this;
    }

    public void setIp(String ipString) {
        InetAddress address;
        try {
            address = InetAddress.getByName(ipString);
        } catch (UnknownHostException e) {
            throw new IllegalArgumentException("Unable to parse " + ipString, e);
        }
        this.ip = address.getAddress();
    }

    public byte[] getIp() {
        return ip.clone();
    }

    public short getPort() {
        return port;
    }

    public void setPort(short port) {
        this.port = port;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = new String(Arrays.copyOf(name.getBytes(), NAME_SIZE));
    }

    public int getCharacterId() {
        return characterId;
    }

    public void setCharacterId(int characterId) {
        this.characterId = characterId;
    }
}
