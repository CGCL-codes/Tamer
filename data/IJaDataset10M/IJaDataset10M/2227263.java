package trafficjams.model.classes;

import trafficjams.model.Numbers;
import trafficjams.model.interfaces.*;
import trafficjams.model.registers.TrafficRegister;
import trafficjams.model.util.Point;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Администратор
 * Date: 13.11.11
 * Time: 15:50
 * To change this template use File | Settings | File Templates.
 */
public class Auto implements IVehicle {

    private class DriveThread extends Thread {

        private ArrayList<IVehicle> vehicles;

        private int count;

        public DriveThread(ArrayList<IVehicle> vehicles, int count) {
            super();
            this.vehicles = vehicles;
            this.count = count;
        }

        public void run() {
            while (count != vehicles.size()) {
            }
            while (Auto.this.isDriveAllowed()) {
                try {
                    tryToGetNextPosition();
                    this.wait(MONITOR_INTERVAL_MILLIS);
                } catch (InterruptedException ex) {
                } catch (IllegalMonitorStateException exx) {
                }
            }
            System.out.println("прибыли");
        }
    }

    public static long MONITOR_INTERVAL_MILLIS = Numbers.MONITOR_INTERVAL_MILLIS;

    private IElementPosition currentPosition = null;

    private IMapElement currentElement = null;

    private IElementPosition finishPosition = null;

    private Point finishCoord = null;

    private IMapElement startElement = null;

    private IMapElement finishElement = null;

    private IElementPosition startPosition = null;

    private Point startCoord = null;

    private Point coord = null;

    private float maxSpeed = 0;

    private float currentSpeed = 0;

    private boolean startDriveFlag = false;

    private ICrossRoad nextCrossRoad = null;

    private Trip currentTrip = null;

    private DriveThread driveThread = null;

    public float getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(float maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public void setFinishPosition(IElementPosition finishPosition) {
        this.finishPosition = finishPosition;
    }

    public void setFinishCoord(Point finishCoord) {
        this.finishCoord = finishCoord;
    }

    public void setStartElement(IMapElement startElement) {
        this.startElement = startElement;
    }

    public void setFinishElement(IMapElement finishElement) {
        this.finishElement = finishElement;
    }

    public void setStartPosition(IElementPosition startPosition) {
        this.startPosition = startPosition;
    }

    public void setStartCoord(Point startCoord) {
        this.startCoord = startCoord;
    }

    public void setCurrentPosition(IElementPosition currentPosition) {
        this.currentPosition = currentPosition;
    }

    public void setCurrentElement(IMapElement currentElement) {
        this.currentElement = currentElement;
    }

    public void setCoord(Point coord) {
        this.coord = coord;
    }

    public Trip getCurrentTrip() {
        return currentTrip;
    }

    public void setCurrentTrip(Trip currentTrip) {
        this.currentTrip = currentTrip;
    }

    public IMapElement getCurrentElement() {
        return currentElement;
    }

    public IElementPosition getCurrentPosition() {
        return currentPosition;
    }

    public Point getCoord() {
        return coord;
    }

    public Point getStartCoord() {
        return startCoord;
    }

    public Point getFinishCoord() {
        return finishCoord;
    }

    public IMapElement getStartElement() {
        return startElement;
    }

    public IMapElement getFinishElement() {
        return finishElement;
    }

    public IElementPosition getStartPosition() {
        return startPosition;
    }

    public IElementPosition getFinishPosition() {
        return finishPosition;
    }

    IRoad nextEl = null;

    public void start(ArrayList<IVehicle> vehicles, int count) {
        startDriveFlag = true;
        nextEl = (IRoad) currentElement;
        this.currentSpeed = TrafficRegister.getInstance().getRoadMap().getMaxSpeed();
    }

    @Deprecated
    public IRoad getNextRoad() {
        return null;
    }

    @Deprecated
    public ICrossRoad getNextCrossRoad() {
        return this.nextCrossRoad;
    }

    private boolean isDriveAllowed() {
        return startDriveFlag && isArrived();
    }

    private boolean isArrived() {
        return true;
    }

    public void tryToGetNextPosition() {
        IElementPosition newPosition = null;
        try {
            newPosition = currentPosition.getNextPosition(this, Auto.MONITOR_INTERVAL_MILLIS * currentSpeed, nextTripElement());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (newPosition != null) {
            this.setCurrentElement(newPosition.getElement());
            this.setCurrentPosition(newPosition);
            this.setCoord(newPosition.getCoord());
        }
    }

    private IRoad nextTripElement() throws Exception {
        boolean fToL = false;
        try {
            if (((Float) this.currentPosition.getValue()).floatValue() >= 0) {
                fToL = true;
            }
        } catch (Exception ex) {
        }
        return currentTrip.getNextElement(currentElement, fToL);
    }
}
