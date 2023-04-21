package br.unb.entities.vehicles;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;
import br.unb.entities.Destination;
import br.unb.entities.RoadSegment;
import br.unb.entities.interfaces.IDrawnable;
import br.unb.entities.interfaces.IEntity;
import br.unb.entities.interfaces.ILaneChangingModel;
import br.unb.entities.interfaces.IVehicleReactionModel;
import br.unb.entities.signalControllers.Detector;
import br.unb.entities.signalControllers.ReportMeasuresStart;
import br.unb.entities.signalControllers.SignalHead;
import br.unb.main.ModelController;

/**
 * Abstract class to define a vehicle.
 * 
 *  @author Marcelo Vale Asari
 */
public abstract class Vehicle implements IEntity, IDrawnable {

    private Integer id;

    private double speed;

    private double nextSpeed;

    private double desiredSpeed;

    private double temporaryDesiredSpeed;

    protected double position;

    private double nextPosition;

    private double nextPositionOnLastRoad;

    private double acceleration;

    private double averageAcceleration;

    private double maxAcceleration;

    private double deceleration;

    private double averageDeceleration;

    private double timeOfLastAccel;

    private double timeOfLastDecel;

    private double reactionTime;

    protected double length;

    private double maxSight;

    private Destination destination;

    private RoadSegment currentRoadSegment;

    private List<RoadSegment> nextRoadSegments;

    private RoadSegment otherLane;

    private boolean boolLaneChange = false;

    private IVehicleReactionModel reactionModel;

    private ILaneChangingModel laneChanging;

    private double timeOfLastUpdate = 0;

    private double lastTimeOfLaneChange = 0;

    private double initialTimeInSegment = 0;

    private double distanceTravelled;

    private double totalTravelTime;

    private double totalDelay;

    private double numOfStops;

    private double numOfStopsInSegment;

    private boolean stopped;

    private boolean saveDelay;

    private boolean alreadyStopped;

    private ModelController modelController;

    public Vehicle(int id, IVehicleReactionModel carFollowing, ILaneChangingModel laneChanging, ModelController modelController) {
        this.id = id;
        this.reactionModel = carFollowing;
        this.laneChanging = laneChanging;
        lastTimeOfLaneChange = -5;
        distanceTravelled = 0;
        timeOfLastUpdate = 0;
        nextRoadSegments = new ArrayList<RoadSegment>();
        alreadyStopped = false;
        this.modelController = modelController;
        saveDelay = false;
        totalDelay = 0;
    }

    public void update(double time) {
        otherLane = laneChanging.decideAboutLaneChange(this, time - lastTimeOfLaneChange, time);
        if (otherLane != null) {
            boolLaneChange = true;
            if (laneChanging.performLaneChange(this, otherLane, time)) {
                lastTimeOfLaneChange = time;
            }
        }
        nextSpeed = reactionModel.updateSpeed(this, time);
        nextPosition = getNextPosition(time, speed, nextSpeed);
    }

    public double getNextPosition(double time, double speed, double nextSpeed) {
        return position + ((speed + nextSpeed) / 2) * (time - timeOfLastUpdate);
    }

    public void updateState(double time) {
        speed = nextSpeed;
        timeOfLastUpdate = time;
        if (boolLaneChange) {
            boolLaneChange = false;
        }
        if (speed <= 1) {
            if (!stopped) {
                numOfStopsInSegment++;
                stopped = true;
                if (!alreadyStopped && saveDelay) {
                    modelController.incStoppedVehicleCount();
                    alreadyStopped = true;
                }
            }
        } else {
            stopped = false;
        }
        if (nextPosition > position) {
            distanceTravelled += (nextPosition - position);
            double backPosition = getBackPosition();
            notifyExit(backPosition, backPosition + (nextPosition - position));
        }
        nextPositionOnLastRoad = nextPosition;
        double relativePosition = position;
        for (RoadSegment road : nextRoadSegments) {
            notifyEntrance(road, relativePosition, nextPositionOnLastRoad, time);
            nextPositionOnLastRoad -= road.getFrontPosition();
            relativePosition -= road.getFrontPosition();
        }
        while (nextPositionOnLastRoad >= 0) {
            RoadSegment next = reactionModel.selectNextRoute(this, nextRoadSegments.get(nextRoadSegments.size() - 1));
            if (next != null) {
                notifyEntrance(next, -0.1, nextPositionOnLastRoad, time);
                nextRoadSegments.add(next);
                next.insertVehicle(this);
                nextPositionOnLastRoad -= next.getFrontPosition();
            } else {
                break;
            }
        }
        while (nextPosition - length > currentRoadSegment.getFrontPosition()) {
            nextPosition = nextPosition - currentRoadSegment.getFrontPosition();
            if (currentRoadSegment.isATurn()) {
                this.deceleration = this.averageDeceleration;
            }
            position = nextPosition;
            if (nextRoadSegments.size() > 1) {
                RoadSegment nextRoadSegment = nextRoadSegments.get(1);
                if (nextRoadSegment.getSpeedLimit() < this.desiredSpeed) {
                    setTemporaryDesiredSpeed(nextRoadSegment.getSpeedLimit());
                } else {
                    setTemporaryDesiredSpeed(this.desiredSpeed);
                }
                currentRoadSegment.remove(this);
                currentRoadSegment = nextRoadSegment;
                nextRoadSegments.remove(0);
                nextRoadSegment.remove(this);
                nextRoadSegment.insertVehicle(this);
            } else {
                if (saveDelay) {
                    modelController.incVehicleCount();
                    modelController.addDelay(totalDelay);
                }
                currentRoadSegment.remove(this);
                break;
            }
        }
        position = nextPosition;
    }

    /**
	 * This method notifies all entities between <code>initialPosition</code> and <code>finalPosition</code>
	 * that the vehicle has passed.
	 * 
	 * @param road
	 * @param initialPosition
	 * @param finalposition
	 * @param time
	 */
    private void notifyEntrance(RoadSegment road, double initialPosition, double finalposition, double time) {
        SignalHead signalHead = null;
        ReportMeasuresStart reportMeasuresStart = null;
        Detector detector = null;
        for (IEntity entity : road.getEntities(initialPosition, finalposition)) {
            entity.notifyVehicleEntrance(this);
            if (entity instanceof ReportMeasuresStart) {
                reportMeasuresStart = (ReportMeasuresStart) entity;
            } else if (entity instanceof SignalHead) {
                signalHead = (SignalHead) entity;
            } else if (entity instanceof Detector) {
                detector = (Detector) entity;
            }
        }
        if (saveDelay) {
            if (signalHead != null) {
                double timeInSegment = time - initialTimeInSegment;
                double delayInSegment = timeInSegment - distanceTravelled / desiredSpeed;
                signalHead.addDelay(delayInSegment);
                signalHead.addNumOfStops(numOfStopsInSegment);
                signalHead.addTravelDistance(distanceTravelled);
                signalHead.addAverageSpeed(distanceTravelled / timeInSegment);
                signalHead.incVehicleCount();
                totalTravelTime += timeInSegment;
                totalDelay += delayInSegment;
                numOfStops += numOfStopsInSegment;
                numOfStopsInSegment = 0;
                initialTimeInSegment = time;
                distanceTravelled = 0;
                signalHead = null;
            } else {
                if (reportMeasuresStart != null) {
                    double timeInSegment = time - initialTimeInSegment;
                    double delayInSegment = timeInSegment - distanceTravelled / desiredSpeed;
                    totalTravelTime += timeInSegment;
                    totalDelay += delayInSegment;
                    numOfStops += numOfStopsInSegment;
                    numOfStopsInSegment = 0;
                    initialTimeInSegment = time;
                    distanceTravelled = 0;
                    reportMeasuresStart = null;
                }
            }
            if (detector != null) {
                detector.incVehicleCount(time);
                detector.addSpeed(speed, time);
            }
        }
    }

    public void setSaveDelay(boolean saveDelay) {
        this.saveDelay = saveDelay;
    }

    private void notifyExit(double initialPosition, double finalPosition) {
        for (IEntity entity : currentRoadSegment.getEntities(initialPosition, finalPosition)) {
            entity.notifyVehicleExit(this);
        }
    }

    public void removeFromAllRoadSegments() {
        for (RoadSegment road : nextRoadSegments) {
            road.remove(this);
        }
    }

    public void paint(Graphics2D g2) {
        Rectangle2D.Double rect;
        rect = new Rectangle2D.Double(position - length, -1, length, 2);
        g2.setStroke(new BasicStroke((float) 0.1));
        g2.setColor(Color.blue);
        g2.fill(rect);
        g2.setColor(Color.black);
        g2.draw(rect);
    }

    @Override
    public double getFrontPosition() {
        return position;
    }

    @Override
    public double getBackPosition() {
        return position - length;
    }

    public void notifyVehicleEntrance(Vehicle vehicle) {
    }

    public void notifyVehicleExit(Vehicle vehicle) {
    }

    public Signal getSignal(Vehicle vehicle, double time) {
        return Signal.STOP;
    }

    public boolean isPhysical() {
        return true;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getDesiredSpeed() {
        return desiredSpeed;
    }

    public void setDesiredSpeed(double desiredSpeed) {
        this.desiredSpeed = desiredSpeed;
    }

    public double getAverageAcceleration() {
        return averageAcceleration;
    }

    public void setAverageAcceleration(double averageAcceleration) {
        this.averageAcceleration = averageAcceleration;
    }

    public double getAcceleration() {
        return acceleration;
    }

    public void setAcceleration(double acceleration) {
        this.acceleration = acceleration;
    }

    public double getDeceleration() {
        return deceleration;
    }

    public void setDeceleration(double deceleration) {
        this.deceleration = deceleration;
    }

    public double getLentgh() {
        return length;
    }

    public void setLentgh(double lentgh) {
        this.length = lentgh;
    }

    public void setPosition(double position) {
        this.position = position;
    }

    public double getTimeOfLastAccel() {
        return timeOfLastAccel;
    }

    public RoadSegment getCurrentRoadSegment() {
        return currentRoadSegment;
    }

    public void setCurrentRoadSegment(RoadSegment roadSegment) {
        currentRoadSegment = roadSegment;
    }

    public void changeToRoadSegment(RoadSegment roadSegment) {
        currentRoadSegment = roadSegment;
        for (RoadSegment road : nextRoadSegments) {
            road.remove(this);
        }
        nextRoadSegments.clear();
        nextRoadSegments.add(currentRoadSegment);
    }

    public double getMaxSight() {
        return position + maxSight;
    }

    public void setMaxSight(double maxSight) {
        this.maxSight = maxSight;
    }

    public double getMaxAcceleration() {
        return maxAcceleration;
    }

    public void setMaxAcceleration(double maxAcceleration) {
        this.maxAcceleration = maxAcceleration;
    }

    public Destination getDestination() {
        return destination;
    }

    public void setDestination(Destination destination) {
        this.destination = destination;
    }

    public IVehicleReactionModel getCarFollowing() {
        return reactionModel;
    }

    public void setCarFollowing(IVehicleReactionModel carFollowing) {
        this.reactionModel = carFollowing;
    }

    public ILaneChangingModel getLaneChanging() {
        return laneChanging;
    }

    public void setLaneChanging(ILaneChangingModel laneChanging) {
        this.laneChanging = laneChanging;
    }

    public double getTotalDelay() {
        return totalDelay;
    }

    public boolean boolLaneChange() {
        return boolLaneChange;
    }

    public double getLastTimeOfLaneChange() {
        return lastTimeOfLaneChange;
    }

    public void setLastTimeOfLaneChange(double lastTimeOfLaneChange) {
        this.lastTimeOfLaneChange = lastTimeOfLaneChange;
    }

    public void setTimeOfLastUpdate(double timeFromLastUpdate) {
        this.timeOfLastUpdate = timeFromLastUpdate;
    }

    public void setTimeOfLastAccel(double timeFromLastAccel) {
        this.timeOfLastAccel = timeFromLastAccel;
    }

    public double getAverageDeceleration() {
        return averageDeceleration;
    }

    public void setAverageDeceleration(double averageDeceleration) {
        this.averageDeceleration = averageDeceleration;
    }

    public double getTimeOfLastDecel() {
        return timeOfLastDecel;
    }

    public void setTimeOfLastDecel(double timeOfLastDecel) {
        this.timeOfLastDecel = timeOfLastDecel;
    }

    public double getTemporaryDesiredSpeed() {
        return temporaryDesiredSpeed;
    }

    public void setTemporaryDesiredSpeed(double temporaryDesiredSpeed) {
        this.temporaryDesiredSpeed = temporaryDesiredSpeed;
    }

    public double getReactionTime() {
        return reactionTime;
    }

    public void setReactionTime(double reactionTime) {
        this.reactionTime = reactionTime;
    }

    public void setInitialTimeInSegment(double initialTimeInSegment) {
        this.initialTimeInSegment = initialTimeInSegment;
    }

    public boolean contains(Point2D point) {
        return false;
    }

    public boolean isSelected() {
        return false;
    }

    public void setSelected(boolean selected) {
    }

    @Override
    public double getX0() {
        return 0;
    }

    @Override
    public double getXf() {
        return 0;
    }

    @Override
    public double getY0() {
        return 0;
    }

    @Override
    public double getYf() {
        return 0;
    }

    @Override
    public void openEditPanel() {
    }

    @Override
    public void setFinalPosition(double xf, double yf, boolean updateLength) {
    }

    @Override
    public void setInitialPosition(double x0, double y0, boolean updateLength) {
    }

    @Override
    public void setColor(Color color) {
    }

    @Override
    public void setSelectedColor(Color color) {
    }
}
