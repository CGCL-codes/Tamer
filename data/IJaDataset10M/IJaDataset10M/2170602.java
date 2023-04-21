package server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Queue;

public class Lane {

    private int id;

    private int startx;

    private int starty;

    private int endx;

    private int endy;

    private int left;

    private int right;

    private boolean curve;

    private boolean deadend;

    private boolean releasedLane;

    private boolean deadentry;

    private boolean startLane;

    private TrafficLight trafLight;

    private float length;

    private Map<Integer, Lane> input;

    private Map<Integer, Lane> output;

    LinkedList<Car> carList;

    private Lane leftLane;

    private Lane rightLane;

    private float maxV;

    private Map<Integer, Guid> guidList;

    public void addCar(Car car) {
        if (carList.size() == 0) {
            carList.addLast(car);
        } else {
            double X = car.getX();
            int indexOfAdd = -1;
            ListIterator<Car> iterator = carList.listIterator(carList.size());
            while (iterator.hasPrevious()) {
                Car carIter = (Car) iterator.previous();
                if (carIter.getX() > X) {
                    indexOfAdd = carList.indexOf(carIter);
                    break;
                }
            }
            if (indexOfAdd != -1) carList.add(indexOfAdd + 1, car); else carList.add(0, car);
        }
    }

    public void removeCar(Car car) {
        carList.remove(car);
    }

    public LinkedList<Car> getCarList() {
        return carList;
    }

    public Lane() {
        input = new HashMap<Integer, Lane>();
        output = new HashMap<Integer, Lane>();
        carList = new LinkedList<Car>();
        guidList = new HashMap<Integer, Guid>();
    }

    public Map<Integer, Lane> getInput() {
        return input;
    }

    public void addOutput(Lane lane) {
        output.put(lane.getId(), lane);
    }

    public void addInput(Lane lane) {
        input.put(lane.getId(), lane);
    }

    Lane getLeftLane() {
        return leftLane;
    }

    void setLeftLane(Lane leftLane) {
        this.leftLane = leftLane;
    }

    public float getLength() {
        return length;
    }

    void setLength(float length) {
        this.length = length;
    }

    float getMaxV() {
        return maxV;
    }

    void setMaxV(float maxV) {
        this.maxV = maxV;
    }

    public Map<Integer, Lane> getOutput() {
        return output;
    }

    Lane getRightLane() {
        return rightLane;
    }

    void setRightLane(Lane rightLane) {
        this.rightLane = rightLane;
    }

    TrafficLight getTrafLight() {
        return trafLight;
    }

    void setTrafLight(TrafficLight trafLight) {
        this.trafLight = trafLight;
    }

    void setCarList(LinkedList<Car> carList) {
        this.carList = carList;
    }

    public boolean isCurve() {
        return curve;
    }

    public int getEndx() {
        return endx;
    }

    public int getEndy() {
        return endy;
    }

    public int getId() {
        return id;
    }

    public int getLeftLaneID() {
        return left;
    }

    public int getRightLaneID() {
        return right;
    }

    public int getStartx() {
        return startx;
    }

    public int getStarty() {
        return starty;
    }

    void setCurve(boolean curve) {
        this.curve = curve;
    }

    void setEndx(int endx) {
        this.endx = endx;
    }

    void setEndy(int endy) {
        this.endy = endy;
    }

    void setId(int id) {
        this.id = id;
    }

    void setLeftLaneID(int left) {
        this.left = left;
    }

    void setRightLaneID(int right) {
        this.right = right;
    }

    void setStartx(int startx) {
        this.startx = startx;
    }

    void setStarty(int starty) {
        this.starty = starty;
    }

    public boolean isDeadend() {
        return deadend;
    }

    public void setDeadend(boolean deadend) {
        this.deadend = deadend;
    }

    public boolean isReleasedLane() {
        return releasedLane;
    }

    public void setReleasedLane(boolean releasedLane) {
        this.releasedLane = releasedLane;
    }

    public Car getNextCar(double X) {
        Car nextCar = null;
        int index = -1;
        ListIterator<Car> iterator = carList.listIterator(carList.size());
        while (iterator.hasPrevious()) {
            Car car = (Car) iterator.previous();
            if (car.getX() > X) {
                index = carList.indexOf(car);
                break;
            }
        }
        if (index != -1) nextCar = carList.get(index); else nextCar = null;
        return nextCar;
    }

    public Car getPreviousCar(double X) {
        Car previousCar = new Car();
        int index = -1;
        ListIterator<Car> iterator = carList.listIterator();
        while (iterator.hasNext()) {
            Car car = (Car) iterator.next();
            if (car.getX() <= X) {
                index = carList.indexOf(car);
                break;
            }
        }
        if (index != -1) previousCar = carList.get(index); else previousCar = null;
        return previousCar;
    }

    public boolean isDeadentry() {
        return deadentry;
    }

    public void setDeadentry(boolean deadentry) {
        this.deadentry = deadentry;
    }

    public boolean isStartLane() {
        return startLane;
    }

    public void setStartLane(boolean startLane) {
        this.startLane = startLane;
    }

    public Map<Integer, Guid> getGuidList() {
        return guidList;
    }

    public void setGuidList(Map<Integer, Guid> guidList) {
        this.guidList = guidList;
    }
}
