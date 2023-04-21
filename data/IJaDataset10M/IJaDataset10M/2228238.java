package be.lassi.artnet.packets;

import be.lassi.artnet.Device;
import be.lassi.artnet.DeviceStyle;
import be.lassi.artnet.EstaCode;
import be.lassi.artnet.Port;
import be.lassi.util.Util;

/**
 * Art-Net packet with device information.
 */
public class ArtPollReply extends ArtPacket {

    public static final int OP_CODE = 0x2100;

    private final Device device;

    public ArtPollReply(final byte[] packetData) {
        super(packetData);
        byte[] address = new byte[4];
        address[0] = packetData[10];
        address[1] = packetData[11];
        address[2] = packetData[12];
        address[3] = packetData[13];
        String shortName = string(26, 18);
        String longName = string(44, 64);
        String nodeReport = string(109, 64);
        device = new Device(address, shortName, longName, nodeReport);
        int port = Util.toInt(packetData[15], packetData[14]);
        device.setPort(port);
        int versionInfo = Util.toInt(packetData[16], packetData[17]);
        device.setVersionInfo(versionInfo);
        int oemCode = Util.toInt(packetData[20], packetData[21]);
        device.setOemCode(oemCode);
        int status = packetData[23];
        device.setStatus(status);
        int code1 = packetData[24];
        int code2 = packetData[25];
        EstaCode estaCode = EstaCode.get(code1, code2);
        device.setEstaCode(estaCode);
        Port[] ports = device.getPorts();
        ports[0].setType(packetData[174]);
        ports[1].setType(packetData[175]);
        ports[2].setType(packetData[176]);
        ports[3].setType(packetData[177]);
        ports[0].setGoodInput(packetData[178]);
        ports[1].setGoodInput(packetData[179]);
        ports[2].setGoodInput(packetData[180]);
        ports[3].setGoodInput(packetData[181]);
        ports[0].setGoodOutput(packetData[182]);
        ports[1].setGoodOutput(packetData[183]);
        ports[2].setGoodOutput(packetData[184]);
        ports[3].setGoodOutput(packetData[185]);
        ports[0].setInputUniverse(packetData[186]);
        ports[1].setInputUniverse(packetData[187]);
        ports[2].setInputUniverse(packetData[188]);
        ports[3].setInputUniverse(packetData[189]);
        ports[0].setOutputUniverse(packetData[190]);
        ports[1].setOutputUniverse(packetData[191]);
        ports[2].setOutputUniverse(packetData[192]);
        ports[3].setOutputUniverse(packetData[193]);
        DeviceStyle style = DeviceStyle.get(packetData[201]);
        device.setStyle(style);
        byte[] macAddress = new byte[6];
        macAddress[0] = packetData[202];
        macAddress[1] = packetData[203];
        macAddress[2] = packetData[204];
        macAddress[3] = packetData[205];
        macAddress[4] = packetData[206];
        macAddress[5] = packetData[207];
        device.setMacAddress(macAddress);
    }

    public ArtPollReply(final Device device) {
        super(new byte[240]);
        this.device = device;
        init(OP_CODE);
        byte[] address = device.getAddress();
        data(10, address[0]);
        data(11, address[1]);
        data(12, address[2]);
        data(13, address[3]);
        int port = device.getPort();
        data(14, port & 0x00FF);
        data(15, port >> 8);
        int versionInfo = device.getVersionInfo();
        data(16, versionInfo >> 8);
        data(17, versionInfo & 0x00FF);
        int oemCode = device.getOemCode();
        data(20, oemCode >> 8);
        data(21, oemCode & 0x00FF);
        data(22, 0);
        data(23, device.getStatus());
        EstaCode estaCode = device.getEstaCode();
        if (estaCode != null) {
            data(24, estaCode.getCode1());
            data(25, estaCode.getCode2());
        }
        data(26, 18, device.getShortName());
        data(44, 64, device.getLongName());
        data(109, 64, device.getNodeReport());
        data(172, 0);
        data(173, 1);
        Port[] ports = device.getPorts();
        data(174, ports[0].getType());
        data(175, ports[1].getType());
        data(176, ports[2].getType());
        data(177, ports[3].getType());
        data(178, ports[0].getGoodInput());
        data(179, ports[1].getGoodInput());
        data(180, ports[2].getGoodInput());
        data(181, ports[3].getGoodInput());
        data(182, ports[0].getGoodOutput());
        data(183, ports[1].getGoodOutput());
        data(184, ports[2].getGoodOutput());
        data(185, ports[3].getGoodOutput());
        data(186, ports[0].getInputUniverse());
        data(187, ports[1].getInputUniverse());
        data(188, ports[2].getInputUniverse());
        data(189, ports[3].getInputUniverse());
        data(190, ports[0].getOutputUniverse());
        data(191, ports[1].getOutputUniverse());
        data(192, ports[2].getOutputUniverse());
        data(193, ports[3].getOutputUniverse());
        data(194, 0);
        data(195, 0);
        data(196, 0);
        data(201, device.getStyle().getCode());
        byte[] macAddress = device.getMacAddress();
        data(202, macAddress[0]);
        data(203, macAddress[1]);
        data(204, macAddress[2]);
        data(205, macAddress[3]);
        data(206, macAddress[4]);
        data(207, macAddress[5]);
        data(208, 0);
        data(209, 0);
        data(210, 0);
        data(211, 0);
        data(212, 0);
        data(213, 0);
        data(214, 0);
        data(215, 0);
        data(216, 0);
        data(217, 0);
        data(218, 0);
        data(219, 0);
        data(220, 0);
        data(221, 0);
        data(222, 0);
        data(223, 0);
        data(224, 0);
        data(225, 0);
        data(226, 0);
        data(227, 0);
        data(228, 0);
        data(229, 0);
        data(230, 0);
        data(231, 0);
        data(232, 0);
        data(233, 0);
        data(234, 0);
        data(235, 0);
        data(236, 0);
        data(237, 0);
        data(238, 0);
        data(239, 0);
    }

    public Device getDevice() {
        return device;
    }
}
