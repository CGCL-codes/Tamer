package org.octaedr.upnp.tools.devicesniffer.engine;

import java.awt.EventQueue;
import java.io.IOException;
import org.octaedr.httpudp.Message;
import org.octaedr.upnp.core.AliveMessage;
import org.octaedr.upnp.core.ByebyeMessage;
import org.octaedr.upnp.core.MonitorEventListener;
import org.octaedr.upnp.core.MonitorSocket;
import org.octaedr.upnp.core.SearchRequest;
import org.octaedr.upnp.core.SearchResponse;
import org.octaedr.upnp.cp.SearchResponseListener;
import org.octaedr.upnp.cp.SearchSocket;
import org.octaedr.upnp.tools.devicesniffer.data.Packet;
import org.octaedr.upnp.tools.devicesniffer.data.PacketGroupsModel;
import org.octaedr.upnp.tools.devicesniffer.data.PacketTableModel;

public class DeviceSnifferEngine implements MonitorEventListener {

    private static final String SERIAL_HEADER = "SnifferSerial";

    private final PacketTableModel packetTableModel;

    private final PacketGroupsModel packetGroupsModel;

    private MonitorSocket monitorSocket;

    private SearchSocket searchSocket;

    private int searchID = -1;

    public DeviceSnifferEngine() {
        this.packetTableModel = new PacketTableModel();
        this.packetGroupsModel = new PacketGroupsModel();
        startMonitoring();
    }

    public PacketTableModel getPacketTableModel() {
        return this.packetTableModel;
    }

    public PacketGroupsModel getPacketGroupsModel() {
        return this.packetGroupsModel;
    }

    public void startMonitoring() {
        if (this.monitorSocket != null) {
            return;
        }
        try {
            this.monitorSocket = new MonitorSocket(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stopMonitoring() {
        if (this.monitorSocket == null) {
            return;
        }
        this.monitorSocket.close();
        this.monitorSocket = null;
    }

    public void performSearch(final String searchTarget, final int delay) {
        if (searchTarget == null) {
            throw new IllegalArgumentException("Timeout may not be null");
        }
        if (delay <= 0) {
            throw new IllegalArgumentException("Delay must be positive");
        }
        stopSearch();
        try {
            ++this.searchID;
            if (this.searchID < 0) {
                this.searchID = 0;
            }
            final int serial = this.searchID;
            this.searchSocket = new SearchSocket(new SearchResponseListener() {

                public void responseReceived(SearchResponse response) {
                    processMessage(response, serial);
                }
            });
            SearchRequest request = new SearchRequest(delay, searchTarget);
            request.appendHeader(SERIAL_HEADER, Integer.toString(serial));
            searchRequestReceived(request);
            this.searchSocket.search(request);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void stopSearch() {
        if (this.searchSocket != null) {
            this.searchSocket.close();
        }
    }

    private void processMessage(final Message message, int packetSerial) {
        final Packet packet = new Packet(packetSerial, message);
        EventQueue.invokeLater(new Runnable() {

            public void run() {
                packetReceived(packet);
            }
        });
    }

    private void packetReceived(final Packet packet) {
        this.packetTableModel.addPacket(packet);
        this.packetGroupsModel.addPacket(packet);
    }

    public void terminate() {
        stopSearch();
        stopMonitoring();
    }

    public void clearAll() {
        this.packetGroupsModel.clearAll();
        this.packetTableModel.clearAll();
    }

    public void aliveNotifyReceived(AliveMessage extendedMessage) {
        processMessage(extendedMessage, Packet.INVALID_SERIAL);
    }

    public void byebyeNotifyReceived(ByebyeMessage extendedMessage) {
        processMessage(extendedMessage, Packet.INVALID_SERIAL);
    }

    public void searchRequestReceived(SearchRequest extendedMessage) {
        int serial = Packet.INVALID_SERIAL;
        String serialHeader = extendedMessage.getHeaderValue(SERIAL_HEADER);
        if (serialHeader != null) {
            try {
                serial = Integer.parseInt(serialHeader);
            } catch (NumberFormatException e) {
            }
        }
        processMessage(extendedMessage, serial);
    }
}
