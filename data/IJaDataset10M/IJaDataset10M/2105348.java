package net.minecraft.src;

import java.util.ArrayList;

public class TeleportNode {

    private int xCoord, yCoord, zCoord;

    private int[] signature;

    private String nodeID;

    private boolean isSender;

    private final int SIGSIZE = 4;

    private final int NODESIZE = 5;

    private static ArrayList<String> usedIDsend = new ArrayList<String>();

    private static ArrayList<String> usedIDreceive = new ArrayList<String>();

    public TeleportNode(int[] signature, int xCoord, int yCoord, int zCoord, boolean isSender) {
        this.xCoord = xCoord;
        this.yCoord = yCoord;
        this.zCoord = zCoord;
        this.signature = signature;
        this.isSender = isSender;
        nodeID = giveID();
    }

    public int getXcoord() {
        return xCoord;
    }

    public int getYcoord() {
        return yCoord;
    }

    public int getZcoord() {
        return zCoord;
    }

    public int[] getSignature() {
        return signature;
    }

    public String getID() {
        return nodeID;
    }

    public void removeID() {
        if (this.isSender) usedIDsend.remove(nodeID); else usedIDreceive.remove(nodeID);
        nodeID = "invalid";
    }

    public boolean isSender() {
        return isSender;
    }

    public int getSigSize() {
        return SIGSIZE;
    }

    public int getNodeSize() {
        return NODESIZE;
    }

    public boolean compareSignatureTo(TeleportNode node) {
        int[] nodeSignature = node.getSignature();
        for (int pos = 0; pos < SIGSIZE; pos++) {
            if (signature[pos] != nodeSignature[pos]) return false;
        }
        return true;
    }

    public boolean compareTo(TeleportNode node) {
        if (this.xCoord != node.getXcoord()) return false;
        if (this.yCoord != node.getYcoord()) return false;
        if (this.zCoord != node.getZcoord()) return false;
        int[] nodeSignature = node.getSignature();
        for (int pos = 0; pos < SIGSIZE; pos++) {
            if (signature[pos] != nodeSignature[pos]) return false;
        }
        return true;
    }

    public String toString() {
        return "NodeID=" + nodeID + ", x=" + xCoord + ", y=" + yCoord + ", z=" + zCoord;
    }

    private String giveID() {
        String newID = "";
        for (int pos = 0; pos < SIGSIZE; pos++) newID = newID + "" + signature[pos];
        if (isSender) {
            usedIDsend.add(newID);
            return newID;
        } else if (!usedIDreceive.contains(newID)) {
            usedIDreceive.add(newID);
            return newID;
        }
        return "invalid";
    }
}
