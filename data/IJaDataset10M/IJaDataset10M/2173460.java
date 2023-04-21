package de.bea.services.vidya.client.datastructures;

import de.bea.services.vidya.client.datasource.types.WSFlyInOut;

public class CFlyInOut {

    protected String inDirection = "left";

    protected int inDelay = 0, inSpeed = 0;

    protected String outDirection = "left";

    protected int outDelay = 0, outSpeed = 0;

    public CFlyInOut() {
    }

    public CFlyInOut(WSFlyInOut fly) {
        if (fly != null) {
            inDirection = fly.getInDirection();
            inSpeed = fly.getInSpeed();
            inDelay = fly.getInDelay();
            outDirection = fly.getOutDirection();
            outSpeed = fly.getOutSpeed();
            outDelay = fly.getOutDelay();
        }
    }

    public WSFlyInOut convertToWS() {
        WSFlyInOut wsFly = new WSFlyInOut();
        wsFly.setInDelay(inDelay);
        wsFly.setInDirection(inDirection);
        wsFly.setInSpeed(inSpeed);
        wsFly.setOutDelay(outDelay);
        wsFly.setOutDirection(outDirection);
        wsFly.setOutSpeed(outSpeed);
        return wsFly;
    }

    public int getInDelay() {
        return inDelay;
    }

    public void setInDelay(int inDelay) {
        this.inDelay = inDelay;
    }

    public String getInDirection() {
        return inDirection;
    }

    public void setInDirection(String inDirection) {
        this.inDirection = inDirection;
    }

    public int getOutDelay() {
        return outDelay;
    }

    public void setOutDelay(int outDelay) {
        this.outDelay = outDelay;
    }

    public String getOutDirection() {
        return outDirection;
    }

    public void setOutDirection(String outDirection) {
        this.outDirection = outDirection;
    }

    public int getInSpeed() {
        return inSpeed;
    }

    public void setInSpeed(int inSpeed) {
        this.inSpeed = inSpeed;
    }

    public int getOutSpeed() {
        return outSpeed;
    }

    public void setOutSpeed(int outSpeed) {
        this.outSpeed = outSpeed;
    }
}
