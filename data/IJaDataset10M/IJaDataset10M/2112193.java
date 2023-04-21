package pl.radical.open.gg.packet.in;

import pl.radical.open.gg.packet.AbstractGGIncomingPacket;
import pl.radical.open.gg.packet.GGIncomingPackage;
import pl.radical.open.gg.packet.IncomingPacket;
import pl.radical.open.gg.packet.dicts.GGMessageClass;
import pl.radical.open.gg.packet.handlers.GGMessageReceivedPacketHandler;
import pl.radical.open.gg.utils.GGUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Class representing Gadu-Gadu received message packet.
 * 
 * @author <a href="mailto:mnaglik@gazeta.pl">Marcin Naglik</a>
 * @author <a href="mailto:mati@sz.home.pl">Mateusz Szczap</a>
 * @author <a href="mailto:lukasz.rzanek@radical.com.pl>Łukasz Rżanek</a>
 * 
 * @deprecated This implementation was replaced by {@link GGRecvMsg80} in current protocol implementation
 */
@IncomingPacket(type = 0x000a, label = "GG_RECV_MSG", handler = GGMessageReceivedPacketHandler.class)
@Deprecated
@Data
@EqualsAndHashCode(callSuper = false)
public class GGRecvMsg extends AbstractGGIncomingPacket implements GGMessageClass, GGIncomingPackage {

    private int sender = -1;

    private int messageSeq = -1;

    private long time = -1;

    private int msgClass = -1;

    private String message = "";

    public GGRecvMsg(final byte[] data) {
        sender = GGUtils.byteToInt(data);
        messageSeq = GGUtils.byteToInt(data, 4);
        time = GGUtils.secondsToMillis(GGUtils.byteToInt(data, 8));
        msgClass = GGUtils.byteToInt(data, 12);
        message = GGUtils.byteToString(data, 16);
    }
}
