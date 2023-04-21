package jbotrace.base;

import java.util.*;
import jbotrace.base.dataServer.*;

/**
 * <p>Description: Contains the information for a race.</p>
 */
public class Race {

    DataServer dataServer;

    Track track;

    List carList;

    List positionList;

    int time;

    /** Constructs a new race. */
    public Race() {
        init();
    }

    /** Contructs a new race. */
    public Race(Track track, List driverList, int laps) {
        init();
        newRace(track, driverList, laps);
    }

    /** Basic initialisation of a race. */
    private void init() {
        carList = new ArrayList();
        positionList = new ArrayList();
    }

    /** Adds addTime to the current race time. */
    void addToTime(long addTime) {
        time += addTime;
    }

    /** Resets the race with the given parameters. */
    private void newRace(Track track, List driverList, int laps) {
        carList = new ArrayList();
        positionList = new ArrayList();
        time = 0;
        this.track = track;
        Iterator driverListIterator = driverList.iterator();
        while (driverListIterator.hasNext()) {
            Car car = new Car();
            Driver driver = (Driver) driverListIterator.next();
            driver.setDriverInformation(new DriverInformation(this, car));
            car.setRace(this);
            car.setDriver(driver);
            addCar(car);
        }
        setCarsToStart();
    }

    /** Add car. */
    private void addCar(Car car) {
        carList.add(car);
    }

    /** Calculate the positionList */
    void calculatePositionList() {
        List tempList = new LinkedList();
        ListIterator carListIterator = carList.listIterator();
        while (carListIterator.hasNext()) {
            Car car = (Car) carListIterator.next();
            ListIterator tempListIterator = tempList.listIterator();
            boolean inserted = false;
            while (tempListIterator.hasNext() && inserted == false) {
                Car listCar = (Car) tempListIterator.next();
                if (car.isAhead(listCar)) {
                    tempListIterator.previous();
                    tempListIterator.add(car);
                    inserted = true;
                }
            }
            if (!inserted) tempList.add(car);
        }
        positionList = new ArrayList(tempList);
    }

    /** Returns the list of cars running in the race. */
    public List getCarList() {
        return carList;
    }

    /** Returns the number of cars in this race */
    public int getCarNumber() {
        return carList.size();
    }

    DataServer getDataServer() {
        return dataServer;
    }

    /** Returns the list with the cars in the current race order */
    public List getPositionList() {
        return positionList;
    }

    /** Return the current race time */
    public int getTime() {
        return time;
    }

    /** Returns the track of the race. */
    public Track getTrack() {
        return track;
    }

    /** Moves the cars for one timestep. */
    public void performTimestep() {
        Iterator carListIterator = carList.iterator();
        while (carListIterator.hasNext()) ((Car) carListIterator.next()).askForDriverCommand();
        carListIterator = carList.iterator();
        while (carListIterator.hasNext()) ((Car) carListIterator.next()).calculateTimestep();
    }

    /** Set cars to start positions. */
    public void setCarsToStart() {
        Straight straight;
        try {
            straight = (Straight) track.getSegmentList().get(0);
        } catch (ClassCastException e) {
            return;
        }
        Vector2d[] corners = straight.getCorners();
        Vector2d segmentStartVec = new Vector2d(corners[1]).sub(corners[0]);
        Vector2d[] posEndRow = new Vector2d[2];
        posEndRow[0] = new Vector2d(corners[0]).add(new Vector2d(segmentStartVec).mul(1d / 3d));
        posEndRow[1] = new Vector2d(corners[0]).add(new Vector2d(segmentStartVec).mul(2d / 3d));
        Vector2d[] startLine = track.getFinishLine().getPoints();
        Vector2d startLineVec = new Vector2d(startLine[1]).sub(startLine[0]);
        Vector2d[] posStartRow = new Vector2d[2];
        posStartRow[0] = new Vector2d(startLine[0]).add(new Vector2d(startLineVec).mul(1d / 3d));
        posStartRow[1] = new Vector2d(startLine[0]).add(new Vector2d(startLineVec).mul(2d / 3d));
        Vector2d[] startRowVec = new Vector2d[2];
        startRowVec[0] = new Vector2d(posEndRow[0]).sub(posStartRow[0]);
        startRowVec[1] = new Vector2d(posEndRow[1]).sub(posStartRow[1]);
        Iterator carListIterator = carList.iterator();
        Car car;
        double length = 4;
        int row = 0;
        while (carListIterator.hasNext()) {
            car = (Car) carListIterator.next();
            car.setPosition(new Vector2d(startRowVec[row]).setLength(length).add(posStartRow[row]));
            car.setDirection(straight.getDirection());
            length += 8;
            row = (row == 0 ? 1 : 0);
        }
    }

    public void setDataServer(DataServer dataServer) {
        this.dataServer = dataServer;
        Iterator carListIterator = carList.iterator();
        while (carListIterator.hasNext()) ((Car) carListIterator.next()).setDataServer(dataServer);
    }

    /** Sets the track. */
    public void setTrack(Track track) {
        this.track = track;
    }
}
