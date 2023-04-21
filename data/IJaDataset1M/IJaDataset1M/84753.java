package virus;

import java.util.Vector;

public class Computer {

    private int id;

    private Vector addresses = new Vector();

    private boolean infectable;

    private boolean spread;

    public int getID() {
        return id;
    }

    public void setInfectable() {
        infectable = true;
    }

    public void setSpread() {
        spread = true;
    }

    public boolean hasSpread() {
        return spread;
    }

    public boolean isInfectable() {
        return infectable;
    }

    public Vector getAddresses() {
        return new Vector(addresses);
    }

    public Computer(String s) throws Exception {
        infectable = false;
        spread = false;
        int i = 0;
        String myid = new String();
        while (true) {
            if (i >= s.length()) throw new Exception("Index out of range.");
            char c = s.charAt(i);
            if (c == '(') break;
            if (!Common.isDigit(c)) throw new Exception("No numeric ID");
            myid += c;
            i++;
        }
        id = (new Integer(myid)).intValue();
        i++;
        String addritem = new String();
        while (true) {
            if (i >= s.length()) throw new Exception("Index out of range");
            char c = s.charAt(i);
            if (Common.isDigit(c)) {
                addritem += c;
            } else if ((c == ',') || (c == ')')) {
                if (addritem.length() == 0) {
                    throw new Exception("No valid id.");
                }
                Integer remoteid = new Integer(addritem);
                if (remoteid.intValue() == id) throw new Exception("Cannot contain myself");
                addresses.add(remoteid);
                if (c == ')') break;
                addritem = new String();
            } else {
                throw new Exception("Invalid character: " + c);
            }
            i++;
        }
        if (addresses.size() < 1) throw new Exception("Less than 1 entry");
    }
}
