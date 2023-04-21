package br.unb.entities.signalControllers;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.ArrayList;
import br.unb.entities.RoadSegment;
import br.unb.entities.interfaces.IDrawnable;
import br.unb.entities.vehicles.Vehicle;
import br.unb.entities.vehicles.reactionModels.AbstractVehicleReactionModel;
import br.unb.gui.editPanels.SignalHeadEditPanel;
import br.unb.io.IoControllerSingleton;
import br.unb.main.ModelController;

/**
 * Signal head to be used on the signal phases. It extends Detector in order to
 * record how many cars passed by.
 * 
 * All measurements are made here. When a vehicle passes, it adds the delay and
 * number of stops from the reportMeasuresStart until the signal head position
 * to the signal head.
 * 
 * To change the driver's behaviour to signal heads, extend this class and
 * overwrite the method getSignal(Vehicle vehicle))
 * 
 * @author Marcelo Vale Asari
 */
public class SignalHead extends Detector implements IDrawnable, Serializable {

    public enum SignalState {

        RED, YELLOW, GREEN
    }

    protected double position;

    protected SignalState state;

    protected SignalPhase signalPhase;

    protected ReportMeasuresStart reportMeasuresStart;

    protected double reportMeasuresStartDistance;

    protected Detector detector;

    protected double detectorDistance;

    protected ModelController modelController;

    protected double sumOfDelays;

    protected double sumOfNumOfStops;

    protected double sumOfTravelDistances;

    protected double sumOfSpeeds;

    protected int numOfVehiclesThatStopped;

    protected double x;

    protected double y;

    protected boolean selected;

    protected Rectangle2D.Float rect;

    protected SignalHeadEditPanel editPanel;

    protected Color color = Color.black;

    protected int numOfVehiclesReport;

    public SignalHead(int id, ModelController modelController, SignalPhase phase) {
        super(id, modelController, false);
        this.modelController = modelController;
        this.signalPhase = phase;
        this.state = SignalState.RED;
        reportMeasuresStart = new ReportMeasuresStart(id, modelController);
        detector = null;
        reportMeasuresStartDistance = 0;
        detectorDistance = 0;
        sumOfDelays = 0;
        sumOfNumOfStops = 0;
        sumOfTravelDistances = 0;
        numOfVehiclesReport = 0;
        roadSegmentList = new ArrayList<RoadSegment>();
    }

    /**
	 * Set the initial conditions to start a simulation
	 */
    public void init() {
        super.init();
        numOfVehiclesReport = 0;
        this.state = SignalState.RED;
        if (detector != null) {
            setDetectorDistance(detectorDistance);
            detector.init();
        }
        setReportMeasuresStartDist(reportMeasuresStartDistance);
        reportMeasuresStart.init();
        sumOfDelays = 0;
        sumOfNumOfStops = 0;
        sumOfTravelDistances = 0;
        numOfVehiclesThatStopped = 0;
        sumOfSpeeds = 0;
    }

    /**
	 * There are three possible signals to be sent:
	 * 
	 * GO: this signal head is green.
	 * 
	 * STOP: this signal head is red or yellow and the vehicle can stop with a
	 * deceleration rate smaller than its max deceleration rate.
	 * 
	 * MAXACCELERATION: this signal head is red or yellow and the vehicle is too
	 * close to stop.
	 * 
	 * 
	 * @return IEntity.Signal
	 */
    @Override
    public Signal getSignal(Vehicle vehicle, double time) {
        double distance;
        double vehicSpeed = vehicle.getSpeed();
        double reactionTime = vehicle.getReactionTime();
        if (state.compareTo(SignalState.GREEN) != 0) {
            distance = AbstractVehicleReactionModel.getDistance(vehicle, this);
            double maxStopDistance = calculateStopDistance(reactionTime, vehicSpeed, vehicle.getAverageDeceleration() * 2);
            if (distance > 0) {
                if (distance < maxStopDistance) {
                    return Signal.MAXACCELERATION;
                } else {
                    return Signal.STOP;
                }
            } else {
                return Signal.GO;
            }
        } else {
            return Signal.GO;
        }
    }

    /**
	 * Calculates the stopping distance given the current speed, the reaction
	 * time and the deceleration rate to be applied.
	 * 
	 * @param reactionTime
	 * @param speed
	 * @param decelRate
	 * @return stopDistance
	 */
    private double calculateStopDistance(double reactionTime, double speed, double decelRate) {
        double reactionDistance;
        double stopDist;
        reactionDistance = reactionTime * speed;
        stopDist = -(Math.pow(speed, 2)) / (2 * decelRate);
        return reactionDistance + stopDist;
    }

    /**
	 * Verifies if the position is consistent with the roads this signalHead is
	 * in.
	 * 
	 * @param position
	 * @return
	 */
    public boolean validatePosition(double position) {
        if (roadSegmentList != null) {
            for (RoadSegment roadSegment : roadSegmentList) {
                if ((position > roadSegment.getFrontPosition()) || (position < roadSegment.getBackPosition())) {
                    return false;
                }
            }
            return true;
        } else {
            return false;
        }
    }

    public void setReportMeasuresStartDist(double distance) {
        double realPosition = this.position - distance;
        reportMeasuresStartDistance = distance;
        reportMeasuresStart.removeFromAllRoadSegments();
        for (RoadSegment road : roadSegmentList) {
            setPositionOnRoads(realPosition, reportMeasuresStart, road);
        }
        modelController.repaint();
    }

    /**
	 * Gets the distance from the detector until this signal head.
	 * 
	 * @return -1 if the detector was not set
	 */
    public double getDistanceFromDetector() {
        if (detector != null) {
            return detectorDistance;
        } else {
            return -1;
        }
    }

    @Override
    public void addRoadSegment(RoadSegment roadSegment) {
        super.addRoadSegment(roadSegment);
        double position = this.getFrontPosition() - reportMeasuresStartDistance;
        setPositionOnRoads(position, reportMeasuresStart, roadSegment);
        if (detector != null) {
            position = this.getFrontPosition() - detectorDistance;
            setPositionOnRoads(position, detector, roadSegment);
        }
    }

    public void updateEditPanel() {
        if (editPanel != null) {
            editPanel.update();
        }
    }

    /**
	 * Gets the distance from the report measures start until this signal head.
	 * 
	 * @return distance
	 */
    public double getDistanceFromReportMeasuresStart() {
        return reportMeasuresStartDistance;
    }

    public Detector getDetector() {
        return detector;
    }

    public ReportMeasuresStart getReportMeasuresStart() {
        return reportMeasuresStart;
    }

    public void setDetectorDistance(double distance) {
        detectorDistance = distance;
        double realPosition = this.position - distance;
        if (detector == null) {
            detector = new Detector(getId(), modelController, false);
        }
        detector.removeFromAllRoadSegments();
        for (RoadSegment road : roadSegmentList) {
            setPositionOnRoads(realPosition, detector, road);
        }
        modelController.repaint();
    }

    /**
	 * Inserts the entity in the correct distance from this signalHead, on all
	 * previous road segments. TODO change
	 * 
	 * @param position
	 * @param entity
	 * @param roadSegment
	 */
    private void setPositionOnRoads(double position, Detector entity, RoadSegment roadSegment) {
        if (roadSegment != null) {
            if (position < roadSegment.getBackPosition()) {
                if (roadSegment.getListOfPreviousSegments() != null) {
                    for (RoadSegment road : roadSegment.getListOfPreviousSegments()) {
                        setPositionOnRoads(road.getFrontPosition() + position, entity, road);
                    }
                }
            } else {
                entity.setFinalPosition(position);
                entity.setInitialPosition(position);
                roadSegment.insert(entity);
                entity.addRoadSegment(roadSegment);
            }
        }
    }

    public int getNumOfVehiclesUntilDetector() {
        if (detector != null) {
            return detector.getNumOfVehicles() - this.getNumOfVehicles();
        } else {
            return -1;
        }
    }

    /***********************************************************************
	 * Drawing code *
	 ***********************************************************************/
    public void paint(Graphics2D g2) {
        if (rect != null) {
            if (selected) {
                g2.setColor(Color.red);
            } else {
                g2.setColor(color);
            }
            g2.setStroke(new BasicStroke(0.1f));
            Line2D.Double line;
            if (roadSegmentList != null) {
                for (RoadSegment roadSegment : roadSegmentList) {
                    double d = position;
                    double s = d / roadSegment.getLength();
                    double xf = s * (roadSegment.getXf() - roadSegment.getX0()) + roadSegment.getX0();
                    double yf = s * (roadSegment.getYf() - roadSegment.getY0()) + roadSegment.getY0();
                    line = new Line2D.Double(rect.getCenterX(), rect.getMaxY(), xf, yf);
                    g2.draw(line);
                }
            }
            BufferedImage img = IoControllerSingleton.getInstance().getSignalHeadImage(state);
            double scale = 0.2;
            AffineTransform af = AffineTransform.getScaleInstance(scale, scale);
            rect.setFrame(x, y, img.getWidth() * scale, img.getHeight() * scale);
            af.translate(x / scale, y / scale);
            g2.drawImage(img, af, null);
            String str = "Aproximação " + Integer.toString(this.getId());
            Font font = new Font("Dialog", Font.PLAIN, 6);
            g2.setFont(font);
            g2.drawString(str, (int) (x - 2), (int) (y - 2));
        }
    }

    public boolean contains(Point2D point) {
        return rect.contains(point);
    }

    public void setFinalPosition(double xf, double yf, boolean updateLength) {
        setInitialPosition(xf, yf, updateLength);
    }

    public void setInitialPosition(double x0, double y0, boolean updateLength) {
        if (rect == null) {
            rect = new Rectangle2D.Float((float) x0, (float) y0, 0, 0);
        } else {
            rect.x = (float) (x0 - 2.5);
            rect.y = (float) (y0 - 2.5);
        }
        this.x = x0;
        this.y = y0;
        modelController.repaint();
    }

    public void openEditPanel() {
        if (editPanel == null) {
            editPanel = new SignalHeadEditPanel(this);
        } else {
            editPanel.dispose();
            editPanel.setVisible(true);
            editPanel.toFront();
        }
    }

    /***********************************************************************
	 * Getters and setters *
	 ***********************************************************************/
    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public double getX0() {
        return x;
    }

    public double getXf() {
        return x;
    }

    public double getY0() {
        return y;
    }

    public double getYf() {
        return y;
    }

    @Override
    public double getBackPosition() {
        return position;
    }

    @Override
    public double getFrontPosition() {
        return getBackPosition();
    }

    public SignalState getState() {
        return state;
    }

    public void setState(SignalState state) {
        this.state = state;
    }

    /**
	 * Gets the position relative to the road segments start
	 * 
	 * @return
	 */
    public double getPosition() {
        return position;
    }

    public void setPosition(double position) {
        this.position = position;
        if (detector != null) {
            setDetectorDistance(detectorDistance);
        }
        setReportMeasuresStartDist(reportMeasuresStartDistance);
        modelController.repaint();
    }

    public SignalPhase getSignalPhase() {
        return signalPhase;
    }

    public void setSignalPhase(SignalPhase signalPhase) {
        this.signalPhase = signalPhase;
    }

    public double getTimeOfGreen(double time) {
        if (SignalState.GREEN.equals(state)) {
            return signalPhase.getTimeOfGreen(time);
        } else {
            return 0;
        }
    }

    @Override
    public void setColor(Color color) {
        this.color = color;
    }

    public void setDefaultColor() {
        this.color = Color.black;
    }

    @Override
    public void setSelectedColor(Color color) {
    }

    public void setDetector(Detector detector) {
        if ((detector == null) && (this.detector != null)) {
            this.detector.removeFromAllRoadSegments();
        }
        this.detector = detector;
        modelController.repaint();
    }

    public int getNumVehiclesReport() {
        return numOfVehiclesReport;
    }

    public double getAverageDelay() {
        return getNumOfVehicles() == 0 ? 0 : sumOfDelays / numOfVehiclesReport;
    }

    public double getAverageNumOfStops() {
        return sumOfNumOfStops / numOfVehiclesReport;
    }

    public double getAverageTravelDistance() {
        return sumOfTravelDistances / numOfVehiclesReport;
    }

    public void incVehicleCount() {
        numOfVehiclesReport++;
    }

    public void addDelay(double delay) {
        this.sumOfDelays += delay;
    }

    public void addAverageSpeed(double speed) {
        this.sumOfSpeeds += speed;
    }

    public void addNumOfStops(double numOfStops) {
        if (numOfStops > 0) {
            numOfVehiclesThatStopped++;
        }
        this.sumOfNumOfStops += numOfStops;
    }

    public void addTravelDistance(double travelDistance) {
        this.sumOfTravelDistances += travelDistance;
    }

    public double getSumOfStopsNum() {
        return sumOfNumOfStops;
    }

    public double getAverageSpeed() {
        return sumOfSpeeds / numOfVehiclesReport;
    }

    public double getStopPercentage() {
        return ((double) numOfVehiclesThatStopped / numOfVehiclesReport) * 100.0;
    }

    public void resetReportVariables() {
        sumOfDelays = 0;
        sumOfNumOfStops = 0;
        sumOfTravelDistances = 0;
        sumOfSpeeds = 0;
        numOfVehiclesThatStopped = 0;
        numOfVehiclesReport = 0;
    }
}
