package net.sf.opensmus.io;

import net.sf.opensmus.*;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.*;
import java.util.ArrayList;

public class FloodFilter implements ChannelUpstreamHandler {

    final ArrayList<ArrayList> settings;

    private int[] floodCount;

    private int[] repeatCount;

    private long[] lastMessageTime;

    private long lastMessageData = 0;

    private int lastMessageSize = 0;

    public FloodFilter(ArrayList<ArrayList> globalSettings) {
        settings = globalSettings;
        floodCount = new int[globalSettings.size()];
        repeatCount = new int[globalSettings.size()];
        lastMessageTime = new long[globalSettings.size()];
    }

    public void handleUpstream(ChannelHandlerContext ctx, ChannelEvent e) {
        if (e instanceof MessageEvent) {
            MessageEvent msg = (MessageEvent) e;
            ChannelBuffer inBuffer = (ChannelBuffer) msg.getMessage();
            inBuffer.markReaderIndex();
            inBuffer.skipBytes(8);
            int strLen = inBuffer.readInt();
            if (strLen < 0 || strLen > inBuffer.readableBytes()) {
                MUSLog.Log("Floodfilter MUSMsgHeaderString size error : " + strLen + " " + inBuffer.readableBytes() + " " + ChannelBuffers.hexDump(inBuffer), MUSLog.kDeb);
                throw new NullPointerException("Floodfilter MUSMsgHeaderString size error " + strLen + " " + inBuffer.readableBytes());
            }
            byte[] stringBytes = new byte[strLen];
            inBuffer.readBytes(stringBytes, 0, strLen);
            String subject = new String(stringBytes);
            inBuffer.resetReaderIndex();
            for (int n = 0; n < settings.size(); n++) {
                ArrayList s = settings.get(n);
                String sub = (String) s.get(0);
                if (sub.equals("*") || subject.equals(sub)) {
                    long currTime = System.currentTimeMillis();
                    if (currTime < lastMessageTime[n] + (Integer) s.get(1)) {
                        floodCount[n]++;
                        if (floodCount[n] >= (Integer) s.get(2)) {
                            MUSUser whatUser = ((SMUSPipeline) ctx.getPipeline()).user;
                            MUSMessage decodedMsg = new MUSMessage(inBuffer);
                            MUSLog.Log("User " + whatUser + " disconnected by flood prevention filter: " + decodedMsg, MUSLog.kSys);
                            notifyFloodDisconnect(whatUser);
                            whatUser.killMUSUser();
                            floodCount[n] = -9999;
                            return;
                        }
                    } else {
                        floodCount[n] = 0;
                    }
                    lastMessageTime[n] = currTime;
                    int repeatThreshold = (Integer) s.get(3) - 2;
                    if (repeatThreshold > -2) {
                        int msgSize = inBuffer.readableBytes();
                        if (msgSize == lastMessageSize) {
                            long data = inBuffer.getLong(inBuffer.writerIndex() - 8);
                            if (data == lastMessageData) {
                                repeatCount[n]++;
                                if (repeatCount[n] > repeatThreshold) {
                                    MUSUser whatUser = ((SMUSPipeline) ctx.getPipeline()).user;
                                    MUSMessage decodedMsg = new MUSMessage(inBuffer);
                                    MUSLog.Log("User " + whatUser + " disconnected by flood prevention filter (repeats): " + decodedMsg, MUSLog.kSys);
                                    notifyFloodDisconnect(whatUser);
                                    whatUser.killMUSUser();
                                    repeatCount[n] = -9999;
                                    return;
                                }
                            } else {
                                repeatCount[n] = 0;
                                lastMessageData = data;
                            }
                        } else {
                            repeatCount[n] = 0;
                            lastMessageSize = msgSize;
                        }
                    }
                    break;
                }
            }
            Channels.fireMessageReceived(ctx, inBuffer);
        } else {
            ctx.sendUpstream(e);
        }
    }

    private void notifyFloodDisconnect(MUSUser usr) {
        if (usr.m_movie == null) {
            MUSLog.Log("User movie is null in notifyFloodDisconnect!", MUSLog.kSys);
            return;
        }
        MUSMessage reply = new MUSMessage();
        reply.m_errCode = MUSErrorCode.MessageContainsErrorInfo;
        reply.m_timeStamp = usr.m_movie.getServer().timeStamp();
        reply.m_subject = new MUSMsgHeaderString("FloodDetected");
        reply.m_senderID = new MUSMsgHeaderString("System");
        reply.m_recptID = new MUSMsgHeaderStringList();
        reply.m_recptID.addElement(new MUSMsgHeaderString(usr.name()));
        reply.m_msgContent = new LVoid();
        usr.sendMessage(reply);
    }
}
