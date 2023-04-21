package net.sf.jqql.packets.out;

import java.nio.ByteBuffer;
import java.util.List;
import net.sf.jqql.QQ;
import net.sf.jqql.beans.QQUser;
import net.sf.jqql.packets.PacketParseException;

/**
 * <pre>
 * 创建群请求包，格式为：
 * 1. 头部
 * 2. 群命令类型，1字节，创建是0x01
 * 3. 群的类型，固定还是临时，1字节
 * 4. 是否需要认证，1字节
 * 5. 2004群分类，4字节
 * 6. 2005群分类，4字节
 * 7. 群名称长度，1字节
 * 8. 群名称
 * 9. 未知的2字节，0x0000
 * 10. 群声明长度，1字节
 * 11. 群声明
 * 12. 群简介长度，1字节
 * 13. 群简介
 * 14. 群现有成员的QQ号列表，每个QQ号4字节
 * 15. 尾部
 * </pre>
 *
 * @author luma
 */
public class ClusterCreatePacket extends ClusterCommandPacket {

    private byte type;

    private byte authType;

    private int oldCategory;

    private int category;

    private String name;

    private String notice;

    private String description;

    private List<Integer> members;

    /**
     * 构造函数
     */
    public ClusterCreatePacket(QQUser user) {
        super(user);
        this.subCommand = QQ.QQ_CLUSTER_CMD_CREATE_CLUSTER;
        this.type = QQ.QQ_CLUSTER_TYPE_PERMANENT;
        this.authType = QQ.QQ_AUTH_CLUSTER_NEED;
        this.oldCategory = 0;
    }

    /**
     * @param buf
     * @param length
     * @throws PacketParseException
     */
    public ClusterCreatePacket(ByteBuffer buf, int length, QQUser user) throws PacketParseException {
        super(buf, length, user);
    }

    @Override
    public String getPacketName() {
        return "Cluster Create _08Packet";
    }

    @Override
    protected void putBody(ByteBuffer buf) {
        buf.put(subCommand);
        buf.put(type);
        buf.put(authType);
        buf.putInt(oldCategory);
        buf.putInt(category);
        byte[] b = name.getBytes();
        buf.put((byte) (b.length & 0xFF));
        buf.put(b);
        buf.putChar((char) 0);
        b = notice.getBytes();
        buf.put((byte) (b.length & 0xFF));
        buf.put(b);
        b = description.getBytes();
        buf.put((byte) (b.length & 0xFF));
        buf.put(b);
        for (int i : members) buf.putInt(i);
    }

    /**
     * @return Returns the authType.
     */
    public byte getAuthType() {
        return authType;
    }

    /**
     * @param authType The authType to set.
     */
    public void setAuthType(byte authType) {
        this.authType = authType;
    }

    /**
     * @return Returns the description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description The description to set.
     */
    public void setDescription(String description) {
        this.description = description;
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
     * @return Returns the notice.
     */
    public String getNotice() {
        return notice;
    }

    /**
     * @param notice The notice to set.
     */
    public void setNotice(String notice) {
        this.notice = notice;
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

    /**
     * @return Returns the members.
     */
    public List<Integer> getMembers() {
        return members;
    }

    /**
     * @param members The members to set.
     */
    public void setMembers(List<Integer> members) {
        this.members = members;
    }

    /**
     * @return Returns the category.
     */
    public int getCategory() {
        return category;
    }

    /**
     * @param category The category to set.
     */
    public void setCategory(int category) {
        this.category = category;
    }

    /**
     * @return Returns the oldCategory.
     */
    public int getOldCategory() {
        return oldCategory;
    }

    /**
     * @param oldCategory The oldCategory to set.
     */
    public void setOldCategory(int oldCategory) {
        this.oldCategory = oldCategory;
    }
}
