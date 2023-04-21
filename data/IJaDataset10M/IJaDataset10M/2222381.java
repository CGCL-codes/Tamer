package com.meidusa.amoeba.mysql.filter;

import org.apache.log4j.Logger;
import com.meidusa.amoeba.mysql.filter.FilterInvocation.Result;
import com.meidusa.amoeba.mysql.handler.MySqlCommandDispatcher;
import com.meidusa.amoeba.mysql.net.packet.MysqlPacketBuffer;
import com.meidusa.amoeba.mysql.net.packet.OkPacket;
import com.meidusa.amoeba.mysql.net.packet.QueryCommandPacket;

/**
 * 
 * @author <a href=mailto:piratebase@sina.com>Struct chen</a>
 *
 */
public class PacketIOFilter extends AbstractIOFilter {

    protected static Logger logger = Logger.getLogger(MySqlCommandDispatcher.class);

    @Override
    protected Result packetFilter(byte[] message) {
        if (MysqlPacketBuffer.isPacketType(message, QueryCommandPacket.COM_QUIT)) {
            return Result.QUIT;
        } else if (MysqlPacketBuffer.isPacketType(message, QueryCommandPacket.COM_STMT_CLOSE)) {
            return Result.RETURN;
        } else if (MysqlPacketBuffer.isPacketType(message, QueryCommandPacket.COM_PING)) {
            OkPacket ok = new OkPacket();
            ok.affectedRows = 0;
            ok.insertId = 0;
            ok.packetId = 1;
            ok.serverStatus = 2;
            this.setResultBuffer(ok.toByteBuffer(null).array());
            return Result.RETURN;
        }
        return null;
    }
}
