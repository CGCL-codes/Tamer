package cxtable.core_comm;

public class xConnect implements xRemovable {

    private String inet, servname, servreg, name, regid;

    private int port;

    private String my_ip, my_port;

    private xRemoveListen xrl;

    public xConnect(String i, String s, String sr, String n, String ri, int pt, String mi, String mp) {
        inet = i;
        servname = s;
        servreg = sr;
        name = n;
        regid = ri;
        port = pt;
        my_ip = mi;
        my_port = mp;
    }

    public String get_IP() {
        return inet;
    }

    public String get_ServName() {
        return servname;
    }

    public String get_ServReg() {
        return servreg;
    }

    public String get_Name() {
        return name;
    }

    public String get_RegID() {
        return regid;
    }

    public int get_Port() {
        return port;
    }

    public String get_my_Port() {
        return my_port;
    }

    public String get_my_IP() {
        return my_ip;
    }

    public void setRemoveListen(xRemoveListen x) {
        xrl = x;
    }

    public void kill() {
        if (xrl != null) {
            xrl.remove(this);
            return;
        }
        System.out.println("Kill called on a xConnect with a null xRemoveListen");
    }

    public void kill(xClientConn x) {
        if (xrl != null) {
            try {
                xConnectPool xcp = (xConnectPool) xrl;
                xcp.remove(this, x);
                return;
            } catch (Exception e) {
                System.out.println("Weird remove called on an xConnect w/ no xConnectPool");
            }
            xrl.remove(this);
            return;
        }
        System.out.println("Kill called on a xConnect w/ a null xRemoveListen");
    }

    public static boolean are_equal(xConnect one, xConnect two) {
        if (one == two) {
            return true;
        }
        if (one.get_IP().equals(two.get_IP()) == false) {
            return false;
        }
        if (one.get_Port() != two.get_Port()) {
            return false;
        }
        if (one.get_ServName().equals(two.get_ServName()) == false) {
            return false;
        }
        if (one.get_ServReg().equals(two.get_ServReg()) == false) {
            return false;
        }
        if (one.get_Name().equals(two.get_Name()) == false) {
            return false;
        }
        if (one.get_RegID().equals(two.get_RegID()) == false) {
            return false;
        }
        return true;
    }
}
