package edu.hawaii.ics.ami.app.experiment.model;

import edu.hawaii.ics.ami.element.model.module.NavigationEvent;
import edu.hawaii.ics.ami.element.model.module.TargetModuleAdapter;
import edu.hawaii.ics.ami.element.model.module.CalibrationInformation;
import edu.hawaii.ics.ami.event.model.DataEvent;
import edu.hawaii.ics.ami.event.model.NumberEvent;
import edu.hawaii.ics.ami.event.model.ParameterEvent;
import edu.hawaii.ics.ami.event.model.ResourceEvent;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Target module for experiment. Displays what happened in the corresponding source
 * module.
 * 
 * @author    king
 * @since     September 23, 2004
 */
public class ExperimentTargetModule extends TargetModuleAdapter implements CalibrationInformation {

    /** Resource event with all the resources attached. */
    private ResourceEvent resourceEvent = null;

    /** Holds the target states that have been executed. */
    private ArrayList<TargetState> states = new ArrayList<TargetState>();

    /** Holds list of errors that occured. */
    private ArrayList<String> errors = new ArrayList<String>();

    /** The width of the panel in pixels where this experiment was running in. */
    private int panelWidth = -1;

    /** The height of the panel in pixels where this experiment was running in. */
    private int panelHeight = -1;

    /** The screen resolution in dots per inch. */
    private int screenResolution = -1;

    /**
   * Returns the name of this object.
   *
   * @return   The name of this element.
   */
    public String getName() {
        return "Suite Target Module";
    }

    /**
   * Receives DataEvents through this method and stores them internally. 
   *
   * @param dataEvent  DataEvents that are comming in will be stored internally.
   */
    public synchronized void dataEventOccurred(DataEvent dataEvent) {
        if (this.panelWidth == -1) {
            ExperimentSourceModule sourceModule = (ExperimentSourceModule) dataEvent.getSourceModule();
            this.panelWidth = sourceModule.getPanelWidth();
            this.panelHeight = sourceModule.getPanelHeight();
            this.screenResolution = sourceModule.getScreenResolution();
        }
        if (dataEvent instanceof ResourceEvent) {
            this.resourceEvent = (ResourceEvent) dataEvent;
        } else {
            String eventName = dataEvent.getName();
            if (eventName.equals(ExperimentSourceModule.EXPERIMENT_STATE_EVENT)) {
                ParameterEvent stateEvent = (ParameterEvent) dataEvent;
                if (stateEvent.getParameter("EventType").equals("Stream")) {
                    if (stateEvent.getParameter("Time").equals("Start")) {
                        if (this.states.size() > 0) {
                            TargetState lastState = (TargetState) this.states.get(this.states.size() - 1);
                            if (lastState.getEndEvent() == null) {
                                this.errors.add("State " + lastState + " didn't get end event.");
                            }
                        }
                        SourceState sourceState = (SourceState) stateEvent.getParameter("State");
                        TargetState targetState = null;
                        if (sourceState instanceof CalibrationSourceState) {
                            targetState = new CalibrationTargetState(this);
                        } else if (sourceState instanceof TextSourceState) {
                            targetState = new TextTargetState(this);
                        } else if (sourceState instanceof ImageSourceState) {
                            targetState = new ImageTargetState(this);
                        } else if (sourceState instanceof SurveySourceState) {
                            targetState = new SurveyTargetState(this);
                        }
                        this.states.add(targetState);
                        targetState.setStartEvent(stateEvent);
                    } else {
                        TargetState lastState = (TargetState) this.states.get(this.states.size() - 1);
                        if (lastState.getEndEvent() != null) {
                            errors.add("State " + lastState + " already got end event.");
                        }
                        lastState.setEndEvent(stateEvent);
                        List resources = this.resourceEvent.getResources();
                        int resourceIndex = 0;
                        for (int i = 0; i < (this.states.size() - 1); i++) {
                            resourceIndex += ((NumberEvent) resources.get(resourceIndex)).getNumber().intValue();
                            resourceIndex++;
                        }
                        int packets = ((NumberEvent) resources.get(resourceIndex)).getNumber().intValue();
                        resourceIndex++;
                        DataEvent dataEvents[] = new DataEvent[packets];
                        for (int i = 0; i < packets; i++) {
                            dataEvents[i] = (DataEvent) resources.get(resourceIndex);
                            resourceIndex++;
                        }
                        lastState.setResources(dataEvents);
                    }
                } else {
                    errors.add("EventType not defined: " + stateEvent.getParameter("EventType"));
                }
            } else {
                TargetState targetState = (TargetState) this.states.get(this.states.size() - 1);
                targetState.dataEventOccurred(dataEvent);
            }
        }
    }

    /**
   * Returns the target state for the given time.
   * 
   * @return  The target state for the given time. 
   */
    public TargetState getTargetState() {
        long time = getNavigation().getTime();
        if (getNavigation().getInterval() > 0) {
            time += getNavigation().getInterval() - 1;
        }
        TargetState matchingTargetState = null;
        for (int i = this.states.size() - 1; i >= 0; i--) {
            TargetState targetState = (TargetState) this.states.get(i);
            if (time >= targetState.getStartEvent().getTimeStamp()) {
                if (matchingTargetState == null) {
                    matchingTargetState = targetState;
                    return matchingTargetState;
                }
            }
        }
        matchingTargetState = (TargetState) this.states.get(0);
        return matchingTargetState;
    }

    /**
   * Returns all the target states for this experiment.
   * 
   * @return  A list of all the TargetState objects.
   */
    public List<TargetState> getTargetStates() {
        return this.states;
    }

    /**
   * Returns the calibration times, when the subject got calibrated.
   * 
   * @return  An array of 8 elements with start/end times of calibration. null if
   *          not given.
   */
    public long[] getCalibrationTimes() {
        for (int i = 0; i < this.states.size(); i++) {
            if (this.states.get(i) instanceof CalibrationTargetState) {
                CalibrationTargetState calibrationState = (CalibrationTargetState) this.states.get(i);
                return calibrationState.getCalibrationTimes();
            }
        }
        return null;
    }

    /**
   * Returns the calibration points related to the calibration times.
   * 
   * @return  An array of 8 elements containing x and y coordinate of the 4 calibration
   *          points shown. null if not given.
   */
    public float[] getCalibrationPoints() {
        for (int i = 0; i < this.states.size(); i++) {
            if (this.states.get(i) instanceof CalibrationTargetState) {
                CalibrationTargetState calibrationState = (CalibrationTargetState) this.states.get(i);
                return calibrationState.getCalibrationPoints();
            }
        }
        return null;
    }

    /**
   * Returns the width of the panel where this experiment was running in.
   * 
   * @return  The width in pixels.
   */
    public int getPanelWidth() {
        return this.panelWidth;
    }

    /**
   * Returns the height of the panel where this experiment was running in.
   * 
   * @return  The height in pixels.
   */
    public int getPanelHeight() {
        return this.panelHeight;
    }

    /**
   * Returns the screen resolution in dots per inch of the panel shown.
   * 
   * @return  The screen resolution in DPI.
   */
    public int getScreenResolution() {
        return this.screenResolution;
    }

    /**
   * Called, if a navigation event came in.
   * 
   * @param navigationEvent  The navigation event that occured.
   */
    public void navigationEventOccurred(NavigationEvent navigationEvent) {
        if (navigationEvent == NavigationEvent.TIME) {
            setChanged();
            notifyObservers("Time");
        } else {
            setChanged();
            notifyObservers();
        }
    }

    /**
   * Saves the data from this target module to a file.
   * 
   * @param fileName  The file name where to save the data to.
   * @throws Exception  If there is a problem saving the file.
   */
    public void save(String fileName) throws Exception {
        File file = new File(fileName);
        DataOutputStream outputStream = new DataOutputStream(new FileOutputStream(file));
        outputStream.writeBytes("Experiment Data Output\n");
        outputStream.writeBytes("----------------------\n");
        ParameterEvent startEvent = ((TargetState) getTargetStates().get(0)).getStartEvent();
        long startTime = startEvent.getTimeStamp();
        ParameterEvent endEvent = ((TargetState) getTargetStates().get(getTargetStates().size() - 1)).getEndEvent();
        long endTime = endEvent.getTimeStamp();
        ExperimentSourceModule sourceModule = (ExperimentSourceModule) startEvent.getSourceModule();
        outputStream.writeBytes("Source: " + sourceModule.toString() + "\n");
        outputStream.writeBytes("Start Time: " + new Date(startTime) + " (" + startTime + "ms)\n");
        outputStream.writeBytes("End Time: " + new Date(endTime) + " (" + endTime + "ms)\n");
        outputStream.writeBytes("\n");
        List targetStates = getTargetStates();
        for (int i = 0; i < targetStates.size(); i++) {
            TargetState targetState = (TargetState) targetStates.get(i);
            long time = targetState.getStartEvent().getTimeStamp() - startTime;
            outputStream.writeBytes(time + " " + targetState.toString());
            outputStream.writeBytes("\n");
        }
        outputStream.writeBytes("\n");
        outputStream.writeBytes("Spreadsheet Format:\n");
        outputStream.writeBytes("State");
        for (int i = 0; i < targetStates.size(); i++) {
            TargetState targetState = (TargetState) targetStates.get(i);
            outputStream.writeBytes("," + targetState.toString());
        }
        outputStream.writeBytes("\n");
        outputStream.writeBytes("Duration [ms]");
        for (int i = 0; i < targetStates.size(); i++) {
            TargetState targetState = (TargetState) targetStates.get(i);
            long time0 = targetState.getStartEvent().getTimeStamp();
            long time1 = targetState.getEndEvent().getTimeStamp();
            long duration = time1 - time0;
            outputStream.writeBytes("," + duration);
        }
        outputStream.writeBytes("\n");
        outputStream.close();
    }

    /**
   * Opens this module.
   *
   * @exception IOException  If something goes wrong opening this module.
   */
    public void open() throws IOException {
    }

    /**
   * Closes this module.
   *
   * @exception IOException  If something goes wrong closing this module.
   */
    public void close() throws IOException {
    }

    /**
   * Status information of a device. true if the data stream
   * is ok.
   *
   * @return   true, if the data stream is ok. false otherwise.
   */
    public boolean status() {
        return true;
    }

    /**
   * Returns the number of packets that got generated/received by the module. 
   *
   * @return The number of packets.
   */
    public int packets() {
        int packets = 0;
        for (int i = 0; i < this.states.size(); i++) {
            packets += ((TargetState) this.states.get(i)).packets();
        }
        return packets;
    }

    /**
   * Returns a list with errors occured.
   *
   * @return A list of strings with errors occured.
   */
    public List<String> errors() {
        return this.errors;
    }

    /**
   * Returns all the data for the given target module.
   * 
   * @return  All the data for the given target module.
   */
    public List<DataEvent> getData() {
        List<DataEvent> data = new ArrayList<DataEvent>();
        for (int i = 0; i < this.states.size(); i++) {
            List<DataEvent> stateData = ((TargetState) this.states.get(i)).getData();
            for (int k = 0; k < stateData.size(); k++) {
                data.add(stateData.get(k));
            }
        }
        return data;
    }
}
