package org.etexascode.api;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

public class Vehicle {

    protected int id;

    protected String type;

    protected double length;

    protected double speed;

    protected double x;

    protected double y;

    protected boolean dsrcEquipped = true;

    protected int currLane;

    public Vehicle(int id, boolean dsrcEquipped) {
        this.id = id;
        this.dsrcEquipped = dsrcEquipped;
    }

    public int getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    protected void setType(String type) {
        this.type = type;
    }

    public double getLength() {
        return length;
    }

    protected void setLength(double length) {
        this.length = length;
    }

    public double getSpeed() {
        return speed;
    }

    public double getSpeedMPH() {
        return ((speed * 3600) / 5820);
    }

    protected void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getX() {
        return x;
    }

    protected void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    protected void setY(double y) {
        this.y = y;
    }

    public boolean isDsrcEquipped() {
        return dsrcEquipped;
    }

    public void setDsrcEquipped(boolean dsrcEquipped) {
        this.dsrcEquipped = dsrcEquipped;
    }

    public int getCurrLane() {
        return currLane;
    }

    protected void setCurrLane(int currLane) {
        this.currLane = currLane;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[Vehicle ");
        sb.append(id);
        sb.append(": type = ");
        sb.append(type);
        sb.append(": length = ");
        sb.append(length);
        sb.append(": speed = ");
        sb.append(speed);
        sb.append(": pos = (");
        sb.append(x);
        sb.append(", ");
        sb.append(y);
        sb.append(")] ");
        return sb.toString();
    }

    protected Collection<VehicleListener> listeners = new LinkedList<VehicleListener>();

    public void registerListener(VehicleListener listener) {
        listeners.add(listener);
        listener.vehicleMoved(this);
    }

    public void removeListener(VehicleListener listener) {
        listeners.remove(listener);
    }

    protected void fireVehicleMovedEvent() {
        Iterator<VehicleListener> iterator = listeners.iterator();
        while (iterator.hasNext()) {
            VehicleListener listener = iterator.next();
            listener.vehicleMoved(this);
        }
    }

    protected void fireVehicleAcceleratedEvent() {
        Iterator<VehicleListener> iterator = listeners.iterator();
        while (iterator.hasNext()) {
            VehicleListener listener = iterator.next();
            listener.vehicleAccelerated(this);
        }
    }

    protected void fireVehicleLaneChangedEvent() {
        Iterator<VehicleListener> iterator = listeners.iterator();
        while (iterator.hasNext()) {
            VehicleListener listener = iterator.next();
            listener.vehicleLaneChanged(this);
        }
    }
}
