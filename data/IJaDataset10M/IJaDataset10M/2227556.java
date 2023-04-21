package module.Router;

import framework.Port;
import module.ModuleUI;
import framework.Packet;

/**
 * 
 * @author Rohit
 * 
 */
public class RouterMod extends module.ModuleAbstractClass {

    static final int MAXPORTS = 10;

    int noOfPorts = 0;

    private Port ports[];

    private int lastServiced = 0;

    private int serialNo = 0;

    private final String name = "Router";

    boolean newPacket;

    Packet packet;

    private PortData routInfo[];

    private ModuleUI modUI = null;

    public RouterMod() {
        buffer.EditMode.module = this;
        modUI = new RouterUI(this);
        ports = new Port[MAXPORTS];
        routInfo = new PortData[MAXPORTS];
        for (int i = 0; i < MAXPORTS; i++) routInfo[i] = new PortData();
    }

    public void setSno(int n) {
        serialNo = n;
    }

    public int getSno() {
        return serialNo;
    }

    private void servicePort(int portNo) {
        boolean found = false;
        packet = ports[portNo].getPacket(this);
        routInfo[portNo].updateCompId(packet.getFromId());
        modUI.getModWin().msg.append("\nFrom:" + packet.getFromId() + "  To:" + packet.getToId());
        if (serialNo == packet.getToId() && (packet.getData().startsWith("ping"))) {
            modUI.getModWin().amsg.append("\n Ping from " + packet.getFromId());
            Packet tempPacket = new Packet(packet.getFromId(), serialNo, 10, packet.getData() + packet.getData(), 0, true);
            ports[portNo].putPacket(tempPacket, this);
            ports[portNo].setActive(true);
            newPacket = true;
            modUI.getModWin().msg.append("\n packet on port" + portNo);
        } else if (packet.getToId() != packet.getFromId()) {
            for (int i = 0; i < noOfPorts; i++) {
                if (routInfo[i].isIdPresent(packet.getToId())) {
                    modUI.getModWin().msg.append("\nfound: " + packet.getToId() + "is at port " + i);
                    Packet tempPacket = new Packet(packet);
                    ports[i].putPacket(tempPacket, this);
                    ports[i].setActive(true);
                    newPacket = true;
                    found = true;
                    break;
                }
            }
            if (!found) {
                modUI.getModWin().msg.append("\nnot found");
                for (int i = 0; i < noOfPorts; i++) if (i != portNo) {
                    Packet tempPacket = new Packet(packet);
                    ports[i].putPacket(tempPacket, this);
                    ports[i].setActive(true);
                    newPacket = true;
                    modUI.getModWin().msg.append("\npacket on port" + i);
                }
            }
        }
    }

    /**
         * Adds a new port (connection)
         * 
         * @return The error code if any. e.g. ERROR_ALREADY_MAX_ports is returned if no more ports of type wireType can be handeled by this module.
         */
    public int addPort(Port port, int wireType) {
        if (noOfPorts < MAXPORTS) {
            ports[noOfPorts++] = port;
            port.setActive(true);
            modUI.getModWin().msg.append("\nADDED port " + noOfPorts);
            return 1;
        } else return ERROR_ALREADY_MAX_PORTS;
    }

    /**
         * If the implementing class doesn't want to extend or create an object of ModuleUI them return null.
         * 
         * @return
         */
    public ModuleUI getModuleUI() {
        return modUI;
    }

    public void setModuleUI(ModuleUI m) {
        modUI = m;
    }

    /**
         * Returns the name of the implementing module. This name is displayed by the UI.
         * 
         * @return
         */
    public String getName() {
        return name;
    }

    /**
         * USES R-R SCHEDULING TO SERVICE THE PORTS
         */
    public boolean step(double time) {
        int i = 0;
        if (noOfPorts != 0) {
            lastServiced = (lastServiced + 1) % noOfPorts;
            while (!ports[lastServiced].hasData() && i < noOfPorts) {
                i++;
                lastServiced = (lastServiced + 1) % noOfPorts;
            }
            if (ports[lastServiced].hasData() && !ports[lastServiced].isTransmiting()) servicePort(lastServiced);
        }
        String routTab = new String("Routing table:\n");
        for (i = 0; i < noOfPorts; i++) {
            routInfo[i].removeExpiredId();
            routTab = routTab.concat("\nPort: " + i + " Module: " + routInfo[i].getAllCompInfo());
        }
        modUI.getModWin().cmd.setText(routTab);
        return true;
    }

    public boolean reset() {
        for (int i = 0; i < noOfPorts; i++) {
            routInfo[i].removeAllCompInfo();
        }
        return true;
    }

    public Port[] getPorts() {
        return ports;
    }

    public boolean isNewPacket() {
        if (newPacket == true) {
            newPacket = false;
            return true;
        } else return false;
    }

    public int getNoOfPorts() {
        return noOfPorts;
    }
}
