package net.sf.jqql.packets.out;

import java.nio.ByteBuffer;
import net.sf.jqql.Util;
import net.sf.jqql.QQ;
import net.sf.jqql.beans.QQUser;
import net.sf.jqql.packets.PacketParseException;

/**
 * <pre>
 * 修改临时群资料
 * 1. 头部
 * 2. 命令，1字节, 0x34
 * 3. 临时群类型, 1字节
 * 4. 父群内部id，4字节
 * 5. 临时群内部id，4字节
 * 6. 临时群名称字节长度，1字节
 * 7. 临时群名称
 * 8. 尾部
 * </pre>
 *
 * @author luma
 */
public class ClusterModifyTempInfoPacket extends ClusterCommandPacket {

    private int parentClusterId;

    private String name;

    private byte type;

    public ClusterModifyTempInfoPacket(ByteBuffer buf, int length, QQUser user) throws PacketParseException {
        super(buf, length, user);
    }

    public ClusterModifyTempInfoPacket(QQUser user) {
        super(user);
        subCommand = QQ.QQ_CLUSTER_CMD_MODIFY_TEMP_INFO;
    }

    @Override
    public String getPacketName() {
        return "Cluster Modify Temp Cluster Info _08Packet";
    }

    @Override
    protected void putBody(ByteBuffer buf) {
        buf.put(subCommand);
        buf.put(type);
        buf.putInt(parentClusterId);
        buf.putInt(clusterId);
        byte[] nameBytes = Util.getBytes(name);
        buf.put((byte) (nameBytes.length & 0xFF));
        buf.put(nameBytes);
    }

    /**
     * @return Returns the name.
     */
    public String getName() {
        return name;
    }

    /**
     * @param name The name to set.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return Returns the parentClusterId.
     */
    public int getParentClusterId() {
        return parentClusterId;
    }

    /**
     * @param parentClusterId The parentClusterId to set.
     */
    public void setParentClusterId(int parentClusterId) {
        this.parentClusterId = parentClusterId;
    }

    /**
     * @return Returns the type.
     */
    public byte getType() {
        return type;
    }

    /**
     * @param type The type to set.
     */
    public void setType(byte type) {
        this.type = type;
    }
}
