package com.taobao.top.analysis.transport;

/**
 * 用于异步发送消息定义的监听器
 * 
 * @author fangweng
 * 
 */
public interface Listener {

    public boolean doReceive(BasePacket packet);
}
