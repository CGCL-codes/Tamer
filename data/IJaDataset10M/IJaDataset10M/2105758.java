package enml.net;

import java.util.ArrayList;

public class Infrastructure {

    private ArrayList<Infrastructure> children;

    private Infrastructure parent;

    private String name;

    private SubNetwork subnetwork;

    public Infrastructure() {
    }

    public Infrastructure(String n) {
    }

    @Override
    public String toString() {
        return null;
    }

    public ArrayList<Infrastructure> getChildren() {
        return null;
    }

    public String getName() {
        return null;
    }

    public void setName(String val) {
    }

    public SubNetwork getSubnetwork() {
        return null;
    }

    public void setSubnetwork(SubNetwork val) {
    }

    public void setChildren(ArrayList<Infrastructure> val) {
    }

    public Infrastructure getParent() {
        return null;
    }

    public void setParent(Infrastructure val) {
    }
}
