package net.sf.jqql.packets.out;

import java.nio.ByteBuffer;
import net.sf.jqql.QQ;
import net.sf.jqql.beans.QQUser;
import net.sf.jqql.packets.PacketParseException;
import net.sf.jqql.packets._08BasicOutPacket;

/**
 * <pre>
 * 群操作包的基类，其包含了一些群操作包的公共字段，比如子命令类型
 * </pre>
 *
 * @author luma
 */
public class ClusterCommandPacket extends _08BasicOutPacket {

    protected byte subCommand;

    protected int clusterId;

    /**
     * 字体属性
     */
    protected static final byte NONE = 0x00;

    protected static final byte BOLD = 0x20;

    protected static final byte ITALIC = 0x40;

    protected static final byte UNDERLINE = (byte) 0x80;

    /**
     * 构造函数
     */
    public ClusterCommandPacket(QQUser user) {
        super(QQ.QQ_CMD_CLUSTER_CMD, true, user);
    }

    /**
     * @param buf
     * @param length
     * @throws PacketParseException
     */
    public ClusterCommandPacket(ByteBuffer buf, int length, QQUser user) throws PacketParseException {
        super(buf, length, user);
    }

    @Override
    protected void parseBody(ByteBuffer buf) throws PacketParseException {
        subCommand = buf.get();
    }

    @Override
    public String getPacketName() {
        switch(subCommand) {
            case QQ.QQ_CLUSTER_CMD_ACTIVATE_CLUSTER:
                return "Cluster Activate _08Packet";
            case QQ.QQ_CLUSTER_CMD_MODIFY_MEMBER:
                return "Cluster Modify Member _08Packet";
            case QQ.QQ_CLUSTER_CMD_CREATE_CLUSTER:
                return "Cluster Create _08Packet";
            case QQ.QQ_CLUSTER_CMD_EXIT_CLUSTER:
                return "Cluster Exit _08Packet";
            case QQ.QQ_CLUSTER_CMD_GET_CLUSTER_INFO:
                return "Cluster Get Info _08Packet";
            case QQ.QQ_CLUSTER_CMD_GET_MEMBER_INFO:
                return "Cluster Get Member Info _08Packet";
            case QQ.QQ_CLUSTER_CMD_GET_ONLINE_MEMBER:
                return "Cluster Get Online Member _08Packet";
            case QQ.QQ_CLUSTER_CMD_JOIN_CLUSTER:
                return "Cluster Join _08Packet";
            case QQ.QQ_CLUSTER_CMD_JOIN_CLUSTER_AUTH:
                return "Cluster Auth _08Packet";
            case QQ.QQ_CLUSTER_CMD_MODIFY_CLUSTER_INFO:
                return "Cluster Modify Info _08Packet";
            case QQ.QQ_CLUSTER_CMD_SEARCH_CLUSTER:
                return "Cluster Search _08Packet";
            case QQ.QQ_CLUSTER_CMD_SEND_IM_EX:
                return "Cluster Send IM Ex _08Packet";
            case QQ.QQ_CLUSTER_CMD_MODIFY_TEMP_MEMBER:
                return "Cluster Modify Temp Cluster Member _08Packet";
            case QQ.QQ_CLUSTER_CMD_GET_TEMP_INFO:
                return "Cluster Get Temp Cluster Info _08Packet";
            case QQ.QQ_CLUSTER_CMD_ACTIVATE_TEMP:
                return "Cluster Activate Temp Cluster _08Packet";
            case QQ.QQ_CLUSTER_CMD_EXIT_TEMP:
                return "Cluster Exit Temp Cluster _08Packet";
            case QQ.QQ_CLUSTER_CMD_CREATE_TEMP:
                return "Cluster Create Temp Cluster _08Packet";
            default:
                return "Unknown Cluster Command _08Packet";
        }
    }

    /**
     * @return Returns the subCommand.
     */
    public byte getSubCommand() {
        return subCommand;
    }

    /**
     * @param subCommand The subCommand to set.
     */
    public void setSubCommand(byte subCommand) {
        this.subCommand = subCommand;
    }

    /**
     * @return Returns the clusterId.
     */
    public int getClusterId() {
        return clusterId;
    }

    /**
     * @param clusterId The clusterId to set.
     */
    public void setClusterId(int clusterId) {
        this.clusterId = clusterId;
    }

    @Override
    protected void putBody(ByteBuffer buf) {
    }
}
