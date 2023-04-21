package se.sics.jipv6.mac;

import se.sics.jipv6.core.AbstractPacketHandler;
import se.sics.jipv6.core.IPStack;
import se.sics.jipv6.core.IPv6Packet;
import se.sics.jipv6.core.NetworkInterface;
import se.sics.jipv6.core.Packet;

public class LoWPANHandler extends AbstractPacketHandler implements NetworkInterface {

    private static final boolean DEBUG = false;

    private IPStack ipStack;

    public LoWPANHandler() {
    }

    public void setIPStack(IPStack stack) {
        ipStack = stack;
    }

    public String getName() {
        return "6lowpan";
    }

    public boolean isReady() {
        return true;
    }

    public void packetReceived(Packet packet) {
        IPv6Packet ipPacket = new IPv6Packet(packet);
        int dispatch = packet.getData(0);
        packet.setAttribute("6lowpan.dispatch", dispatch);
        ipPacket.incPos(1);
        if (dispatch == ipStack.getDefaultPacketer().getDispatch()) {
            ipStack.getDefaultPacketer().parsePacketData(ipPacket);
            ipPacket.netInterface = this;
            ipStack.receivePacket(ipPacket);
        }
    }

    public void sendPacket(IPv6Packet packet) {
        byte[] data = ipStack.getPacketer().generatePacketData((IPv6Packet) packet);
        packet.setBytes(data);
        byte[] data2 = new byte[1];
        data2[0] = ipStack.getPacketer().getDispatch();
        packet.prependBytes(data2);
        lowerLayer.sendPacket(packet);
    }

    public void sendPacket(Packet packet) {
        sendPacket((IPv6Packet) packet);
    }
}
