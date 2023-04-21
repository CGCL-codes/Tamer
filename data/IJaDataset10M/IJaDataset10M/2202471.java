package net.sf.jqql.robot;

import net.sf.jqql.packets.in.ReceiveIMPacket;

/**
 * 为LumaQQ提供通用的聊天机器人支持，用户如果有现成的机器人程序，可以通过实现这个接口
 * 对其包装，然后修改robot.xml文件，即可通过系统菜单打开/关闭机器人功能
 *
 * @author luma
 */
public interface IRobot {

    /**
     * 根据message得到一条回复消息
     *
     * @param packet 接受消息包
     * @return 回复的消息，返回null表示不响应这条消息
     */
    public String getReply(ReceiveIMPacket packet);
}
