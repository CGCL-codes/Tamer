package packet;

import util.*;

public class StatusElem {

    private int routerID;

    private int routerPortID;

    private int cycle;

    private int code = -1;

    public StatusElem(int routerID, int routerPortID, int cycle) {
        this.routerID = routerID;
        this.routerPortID = routerPortID;
        this.cycle = cycle;
    }

    public void setBlock() {
        this.setCode(0);
    }

    public void setArrive() {
        this.setCode(1);
    }

    public void setDepart() {
        this.setCode(2);
    }

    public void setInject() {
        this.setCode(3);
    }

    public void setEject() {
        this.setCode(4);
    }

    private void setCode(int code) {
        if (this.code == -1) {
            this.code = code;
        }
    }

    public int getCycle() {
        return this.cycle;
    }

    public int getRouterID() {
        return this.routerID;
    }

    public int getRouterPortID() {
        return this.routerPortID;
    }

    public int getStatusID() {
        return this.code;
    }

    public String getStatusStr() {
        switch(this.code) {
            case 0:
                return "blocked";
            case 1:
                return "arrived";
            case 2:
                return "departed";
            case 3:
                return "injected";
            case 4:
                return "ejected";
            default:
                return "invalid";
        }
    }

    public void toXml() {
        Xml.opnBlk("cycle", "id=" + this.getCycle());
        Xml.opnBlk("router", "id=" + this.getRouterID());
        Xml.tag("port", this.getRouterPortID());
        Xml.clsBlk();
        Xml.tag("status", "id=" + this.getStatusID(), this.getStatusStr());
        Xml.clsBlk();
    }

    public static int getStatusID(String status) {
        if (status.equalsIgnoreCase("blocked")) return 0;
        if (status.equalsIgnoreCase("arrived")) return 1;
        if (status.equalsIgnoreCase("departed")) return 2;
        if (status.equalsIgnoreCase("injected")) return 3;
        if (status.equalsIgnoreCase("ejected")) return 4;
        return -1;
    }
}
