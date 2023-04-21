package it.ismb.locationService.Nodes;

import it.ismb.locationService.PacketDecoder;
import it.ismb.locationService.Room;
import it.ismb.locationService.packets.AnnounceBlindPkt;

public class BlindNode extends Node {

    private double timeOut;

    private boolean hasGood;

    private Good associatedGood;

    private boolean distribuitedLoc;

    public BlindNode(int[] longAddr, int shortAddr, double x, double y, Room room, int sensNo, long timest, String nodename, boolean automode, double cycleTime) {
        super(longAddr, shortAddr, x, y, room, sensNo, timest, nodename, automode, cycleTime, false);
        this.distribuitedLoc = false;
        this.associatedGood = null;
        this.hasGood = false;
    }

    public BlindNode(AnnounceBlindPkt blindAnnounce) {
        super(blindAnnounce.getIEEEaddr(), blindAnnounce.getNTWaddr(), 0, 0, PacketDecoder.getRoomById(0), blindAnnounce.getNumOfSens(), System.currentTimeMillis(), "New BlindNode", blindAnnounce.isAutoMode(), blindAnnounce.getCycle(), false);
        if (this.numOfSens > 0) {
            for (int i = 0; i < numOfSens; i++) {
                Sensor currentSensor = new Sensor(blindAnnounce.getSensId()[i]);
                this.sensors.put(blindAnnounce.getSensId()[i], currentSensor);
            }
        }
        this.cycle = blindAnnounce.getCycle();
        this.timeOut = blindAnnounce.getTimeout();
        this.autoMode = blindAnnounce.isAutoMode();
        this.distribuitedLoc = blindAnnounce.isDistribuitedLoc();
        this.associatedGood = null;
        this.hasGood = false;
    }

    public void setTimeOut(double time) {
        this.timeOut = time;
    }

    public double getTimeOut() {
        return this.timeOut;
    }

    public boolean hasGood() {
        return this.hasGood;
    }

    public void associateGood(Good good) {
        this.associatedGood = good;
        good.assocBN(this);
        this.hasGood = true;
    }

    public void assocG(Good good) {
        this.associatedGood = good;
        this.hasGood = true;
    }

    public Good getGood() {
        return this.associatedGood;
    }

    public void disassociate() {
        this.associatedGood.disassoc();
        this.associatedGood = null;
        this.hasGood = false;
    }

    public void disassoc() {
        this.associatedGood = null;
        this.hasGood = false;
    }

    public void setDistribuiteLoc(boolean isDistribuitedLoc) {
        this.distribuitedLoc = isDistribuitedLoc;
    }

    public boolean isDistribuitedLoc() {
        return this.distribuitedLoc;
    }
}
