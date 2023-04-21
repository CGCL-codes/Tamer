package net.sf.vorg.vorgautopilot.models;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.SAXException;
import net.sf.gef.core.model.AbstractGEFNode;
import net.sf.gef.core.model.IGEFNode;
import net.sf.vorg.core.VORGConstants;
import net.sf.vorg.core.enums.InputHandlerStates;
import net.sf.vorg.core.enums.InputTypes;
import net.sf.vorg.core.enums.ModelStates;
import net.sf.vorg.core.exceptions.BoatNotFoundException;
import net.sf.vorg.core.exceptions.DataLoadingException;
import net.sf.vorg.core.models.WindMapHandler;
import net.sf.vorg.vorgautopilot.core.AutopilotRunner;
import net.sf.vorg.vorgautopilot.core.IInputHandler;
import net.sf.vorg.vorgautopilot.models.wizards.PilotStoreWizardModel;
import net.sf.vorg.vorgautopilot.parsers.BoatModelParserHandler;

/**
 * This is the container for the model that describes the boats and any other data under the control of the
 * Automatic Pilot. This structure contains an internal and persistent repository with the model structure and
 * some helper classes to process outside data into the model for model creation. The class has methods to
 * perform the pilot operations, being able to scan and process the pilot data and generate all the commands
 * necessary to control the model boats.<br>
 * The main model class is the <code>PilotBoat</code> that contains the boat information along with the
 * commands and waypoints to set the route to be followed.
 */
public class PilotModelStore extends AbstractGEFNode {

    public static final String MODEL_STRUCTURE_CHANGED = "PilotModelStore.MODEL_STRUCTURE_CHANGED";

    public static final String STATE_CHANGED = "PilotModelStore.STATE_CHANGED";

    public static final String RECORD_CHANGED = "PilotModelStore.RECORD_CHANGED";

    private static Logger logger = Logger.getLogger("net.sf.vorg.vorgautopilot.models");

    private static final long serialVersionUID = 5181001026031627512L;

    public static final int DEFAULT_REFRESH_MINUTES = 1;

    public static final int DEFAULT_TIME_DELAY = 0;

    /** Path to the persistent file that is the model repository. */
    private static final String persistentModelStorageName = "BoatsOnPilot.xml";

    /** File to store the different actions and events recorded during the application processing. */
    private static final String persistentActionLogName = "ActionRecords.log";

    /**
	 * This array contains the list of boats that are being managed on the interface. There are boats that get
	 * controlled by the process and others that are just containers to see the information in the display.
	 */
    private final Vector<PilotBoat> modelContents = new Vector<PilotBoat>();

    /** Records the number of boats that are READY and correctly authorized. */
    private final int boatCount = 0;

    /**
	 * This is the hierarchical list where the application sores the different events and actions that are
	 * performed such as the reading of new boat data from the server or the commands sent to control the boats.
	 * I will add also a global element to record file updates, log information and the registration of
	 * exceptions. This structure is saved to a single file but not in XML format but in a format suitable to be
	 * used in Excel.
	 */
    private final Hashtable<String, BoatActionList> actionRecords = new Hashtable<String, BoatActionList>();

    /**
	 * This stores the status of this model container with reference to the content of the persistent model
	 * data.
	 */
    private ModelStates state = ModelStates.EMPTY;

    /** Reference to the class that will control the read and processing of the Route data. */
    private IInputHandler inputHelper = null;

    private boolean allowRun;

    private final int refresh = PilotModelStore.DEFAULT_REFRESH_MINUTES;

    private final int timeDelay = PilotModelStore.DEFAULT_TIME_DELAY;

    /** Stores a flag to signal if the model has changed with respect to the persistent stored data. */
    private boolean dirty = false;

    /** Time variables to control the refresh times and detect when a new loop pass should be made. */
    private long lastHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);

    private long lastMinute = minuteOfDay() - 100;

    /**
	 * Reference to the last exception occurred during processing. This allows graphical displays to show this
	 * message at a later moment.
	 */
    private Exception lastException;

    /**
	 * Initializes the instance by reading the contents of the persistent file. If the <code>inputHelper</code>
	 * is configured and valid, the creation can continue reading the Route data that controls the operation of
	 * the boats.
	 */
    public PilotModelStore() {
        System.out.println("Loading storage contents from: " + PilotModelStore.persistentModelStorageName);
        if (loadModelContents()) if (null != inputHelper) {
            PilotModelStore.logger.info("Loading route data contents from: " + inputHelper.getFilePath());
            inputHelper.loadContents();
        }
    }

    /**
	 * Adds a new Boat to the list of boats under the control of the model. After the addition updates the
	 * persistent storage and sends a message to all its listeners (the EditPart and the Views) to report to
	 * them the structure change in the contents of the model.
	 * 
	 * @param boatRef
	 *          the new boat to be punt on the model.
	 */
    public void addBoat(final PilotBoat boatRef) {
        if (null != boatRef) {
            if (modelContents.contains(boatRef)) return;
            buildBoat(boatRef);
            updatePersistentStorage();
            fireStructureChange(PilotModelStore.MODEL_STRUCTURE_CHANGED, this, boatRef);
        }
    }

    /**
	 * Adds the Boat to the control of the model but without updating the model storage because this method is
	 * used during the storage processing.
	 */
    public void buildBoat(final PilotBoat boatRef) {
        if (null != boatRef) {
            if (null != searchBoatName(boatRef.getBoatName())) return;
            try {
                boatRef.updateBoatData();
                modelContents.add(boatRef);
                addChild(boatRef);
            } catch (final DataLoadingException dle) {
                lastException = dle;
            } catch (final BoatNotFoundException bnfe) {
                lastException = bnfe;
            }
        }
    }

    /**
	 * Check the last time the map data was updated. If has elapsed more then one hour, then invalidate the
	 * cache to force a reload.<br>
	 * The time used to perform this comparison is the local time.
	 */
    public void checkMapReload() {
        final Calendar now = Calendar.getInstance();
        final int hour = now.get(Calendar.HOUR_OF_DAY);
        if (hour != lastHour) {
            WindMapHandler.clear();
            lastHour = hour;
        }
    }

    public void cleanActiveWaypoints() {
        final Iterator<IGEFNode> bit = getChildren().iterator();
        while (bit.hasNext()) {
            final IGEFNode node = bit.next();
            if (node instanceof PilotBoat) {
                ((PilotBoat) node).clearActiveWaypoint();
            }
        }
    }

    public void clearUpdate() {
        if (null != inputHelper) {
            inputHelper.clearUpdate();
        }
    }

    public Object[] getActionRecords() {
        return actionRecords.values().toArray();
    }

    public int getBoatCount() {
        int count = 0;
        for (final PilotBoat boatRef : modelContents) if (boatRef.getState() == ModelStates.READY) {
            count++;
        }
        return count;
    }

    public int getRefreshInterval() {
        return refresh;
    }

    public ModelStates getState() {
        return state;
    }

    public int getTimeDeviation() {
        return timeDelay;
    }

    /**
	 * Creates a new instance for a class to store the part of this model element to be shown on a wizard. The
	 * creation uses a reference to this instance but to allow a direct pass back of the model values when the
	 * update on the user interface has finished. All access to the internal variables is performed from outside
	 * the new model element being created to avoid exposing those field on the public interface.
	 * 
	 * @return a newly initialized wizard model ready to be used.
	 */
    public PilotStoreWizardModel getWizardModel() {
        final PilotStoreWizardModel wizardModel = new PilotStoreWizardModel(this);
        wizardModel.setInputPath(getInputPath());
        wizardModel.setInputType(getInputType());
        wizardModel.setRefreshInterval(refresh);
        wizardModel.setTimeDeviation(timeDelay);
        return wizardModel;
    }

    /**
	 * Executes all the processing operations for the boat control. This is the method that gets called when we
	 * reach the time to perform another loop pass to check the status and the control of the boats and the
	 * waypoints. This method will be reworked to separate the tasks that have to be made on different time
	 * elapses.
	 */
    public void performOperation() {
        refreshModel();
        final Iterator<IGEFNode> bit = getChildren().iterator();
        while (bit.hasNext()) {
            final IGEFNode node = bit.next();
            if (node instanceof PilotBoat) {
                ((PilotBoat) node).performPilot();
            }
        }
    }

    public void recordChange(final ActionRecord actionRecord) {
        BoatActionList actionList = actionRecords.get(actionRecord.getBoatName());
        if (null == actionList) {
            actionList = new BoatActionList(actionRecord.getBoatName());
            actionRecords.put(actionRecord.getBoatName(), actionList);
        }
        actionList.add(actionRecord);
        fireStructureChange(PilotModelStore.RECORD_CHANGED, this, actionRecord);
        try {
            final PrintWriter logOutput = new PrintWriter(new FileOutputStream(PilotModelStore.persistentActionLogName, true), true);
            logOutput.println(actionRecord.logOutput());
            logOutput.close();
        } catch (final FileNotFoundException fnfe) {
            lastException = fnfe;
            throw new RuntimeException(fnfe);
        }
    }

    public void refreshModel() {
        updatePersistentStorage();
        if (null != inputHelper) if (inputHelper.loadContents()) {
            fireStructureChange(PilotModelStore.MODEL_STRUCTURE_CHANGED, this, "Data reload");
        }
    }

    /**
	 * Deletes a boat for the model control
	 * 
	 * @param boatRef
	 *          the boat to be removed from the lists.
	 */
    public void removeBoat(final IGEFNode boatRef) {
        modelContents.remove(boatRef);
        removeChild(boatRef);
        updatePersistentStorage();
        fireStructureChange(PilotModelStore.MODEL_STRUCTURE_CHANGED, this, boatRef);
    }

    /**
	 * This methods starts and endless loop that iterates on the main processing section of the autopilot.<br>
	 * The game will test for some conditions on a timely base that it is commanded to the scenery level.<br>
	 * The run process start an infinite loop in a thread that has no UI interface. Any operation inside that
	 * thread that requires access to the UI (updating, creating new model elements) will be posted as another
	 * Runnable instance to the Display created for the application.
	 * 
	 * @return
	 */
    public void run() {
        allowRun = true;
        while (allowRun) {
            try {
                wait4NextIteration();
                final AutopilotRunner job = new AutopilotRunner(this, "Main Pilot Loop");
                synchronized (this) {
                    job.run();
                }
                Thread.yield();
            } catch (final InterruptedException ie) {
                System.out.println("Autopilot interrupted. Terminating current process.");
                break;
            } catch (final Exception ex) {
                System.out.println("EEE EXCEPTION - " + ex.getLocalizedMessage());
            }
        }
    }

    /**
	 * This method is used to detect when this model is used inside an application that has a Graphical User
	 * Interface.
	 * 
	 * @return it always returns false to show that this class is running inside a stand alone application.
	 */
    public boolean runningUI() {
        return false;
    }

    /** Searches the container to return a boat with the corresponding name if found or null otherwise. */
    public PilotBoat searchBoatName(final String name) {
        final Iterator<PilotBoat> bit = modelContents.iterator();
        while (bit.hasNext()) {
            final PilotBoat boat = bit.next();
            if (boat.getBoatName().toLowerCase().equals(name.toLowerCase())) return boat;
        }
        return null;
    }

    public void setDirty(final boolean dirtyState) {
        dirty = dirtyState;
    }

    /**
	 * Sets the InputHelper to the instance receives on the parameter. The state of the model is modified to the
	 * final state depending on the state of the InputHelper. The changes on the states has to be rewieved from
	 * the state model documented.
	 */
    public void setInputHandler(final IInputHandler newInputHandler) {
        if (null != newInputHandler) {
            inputHelper = newInputHandler;
            setDirty(true);
            inputHelper.setStore(this);
            if (state != ModelStates.RUNNING) {
                setState(ModelStates.CONFIGURED);
            }
            if (inputHelper.getState() == InputHandlerStates.READY) if (state != ModelStates.RUNNING) {
                setState(ModelStates.READY);
            }
        }
    }

    public void setRefreshInterval(final int refreshTime) {
    }

    public void setRefreshInterval(final String refreshTime) {
        if (null != refreshTime) {
            try {
            } catch (final Exception exc) {
            }
        }
    }

    public void setState(final ModelStates newState) {
        final ModelStates oldState = state;
        state = newState;
        firePropertyChange(PilotModelStore.STATE_CHANGED, oldState, newState);
    }

    public void setTimeDeviation(final int seconds) {
    }

    public void setTimeDeviation(final String seconds) {
        if (null != seconds) {
            try {
            } catch (final Exception exc) {
            }
        }
    }

    public void stop() {
        allowRun = false;
    }

    @Override
    public String toString() {
        final StringBuffer buffer = new StringBuffer("[PilotModelStore ");
        buffer.append("Boats=").append(modelContents).append("");
        buffer.append(VORGConstants.NEWLINE);
        return buffer.toString();
    }

    /**
	 * If the flag shows that the model in memory has been changed from the last time it was written to the
	 * persistent storage, then the method start to write down the model data to that persistent file. It there
	 * is any error during this process the flag returns back to show it is dirty and the error message gets
	 * stored for access from outside control classes.
	 */
    public void updatePersistentStorage() {
        if (dirty) {
            try {
                PilotModelStore.logger.fine("Saving contens into the current persistent store.");
                final PrintWriter persistent = new PrintWriter(PilotModelStore.persistentModelStorageName);
                final StringBuffer buffer = new StringBuffer();
                buffer.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>").append(VORGConstants.NEWLINE);
                buffer.append("<boatmodel>").append(VORGConstants.NEWLINE);
                buffer.append("<configuration");
                if (null != inputHelper) {
                    buffer.append(" location=").append(this.quote(inputHelper.getFilePath()));
                    buffer.append(" type=").append(this.quote(inputHelper.getType().toString()));
                } else {
                    buffer.append(" location=\"\"  type=\"NONE\"");
                }
                buffer.append(" refresh=").append(this.quote(refresh));
                buffer.append(" time=").append(this.quote(timeDelay));
                buffer.append(" />");
                persistent.println(buffer.toString());
                persistent.println("<boatlist>");
                final Iterator<PilotBoat> bit = modelContents.iterator();
                while (bit.hasNext()) {
                    final PilotBoat node = bit.next();
                    persistent.println(node.generatePersistentXML());
                }
                persistent.println("</boatlist>");
                persistent.println("</boatmodel>");
                persistent.close();
                dirty = false;
            } catch (final FileNotFoundException fnfe) {
                lastException = fnfe;
                throw new RuntimeException(fnfe);
            }
        }
    }

    private void dispose() {
        stop();
    }

    /**
	 * Get the route configuration file path from the input helper associated to this instance.This is a
	 * delegate method to the <code>InputHelper</code> class.
	 * 
	 * @return the file path of the input helper or the value null if there is no one configured.
	 */
    private String getInputPath() {
        if (null != inputHelper) return inputHelper.getFilePath(); else return null;
    }

    private InputTypes getInputType() {
        if (null != inputHelper) return inputHelper.getType(); else return InputTypes.NONE;
    }

    /**
	 * Opens the default model file and parses it to read the boats under control and their identifications.
	 * This persistent storage file contains also any model data that will be overrun later or that was stored
	 * on other files on previous model implementation.<br>
	 * If there are errors in the processing, the current file may be destroyed, so in case that there is an
	 * error in the file processing, the best solution is interrupt the operation but do not do anything more.<br>
	 * For this, then the method returns a <code>true</code> when the operation was performed without errors or
	 * a <code>false</code> code when it failed.
	 * 
	 * @throws RuntimeException
	 *           if there is an error in the IO different from the File Not Found.
	 */
    private boolean loadModelContents() {
        if (dirty) {
            updatePersistentStorage();
        }
        try {
            final InputStream stream = new BufferedInputStream(new FileInputStream(PilotModelStore.persistentModelStorageName));
            final SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
            final BoatModelParserHandler handler = new BoatModelParserHandler(this);
            parser.parse(stream, handler);
            fireStructureChange(PilotModelStore.MODEL_STRUCTURE_CHANGED, this, this);
            return true;
        } catch (final FileNotFoundException fnfe) {
            PilotModelStore.logger.severe("Persistent model can not be read. [" + fnfe.getLocalizedMessage() + "]");
            lastException = fnfe;
            return false;
        } catch (final ParserConfigurationException pce) {
            PilotModelStore.logger.severe("Parser error during model processing. [" + pce.getLocalizedMessage() + "]");
            lastException = pce;
            return false;
        } catch (final SAXException saxe) {
            PilotModelStore.logger.severe("Parser error during model processing. [" + saxe.getLocalizedMessage() + "]");
            lastException = saxe;
            return false;
        } catch (final IOException ioe) {
            PilotModelStore.logger.severe("Persistent model can not be read. [" + ioe.getLocalizedMessage() + "]");
            lastException = ioe;
            return false;
        }
    }

    /** Method to calculate the minuTes elapsed from the start of this date. */
    private long minuteOfDay() {
        final Calendar now = Calendar.getInstance();
        final int hour = now.get(Calendar.HOUR_OF_DAY);
        final int minute = now.get(Calendar.MINUTE);
        return hour * 60 + minute;
    }

    /**
	 * Main timing method in the autopilot processing block. It calculates the next wake up time from the
	 * refresh interval and the time delay that are configured inside the model. This is the method that will
	 * have to be upgraded to support for different time delays for the now various actions that have to be
	 * timed inside the block.
	 */
    public void wait4NextIteration() throws InterruptedException {
        final long dayMinute = minuteOfDay();
        long waitUntil = lastMinute + refresh;
        if (waitUntil > 24 * 60) {
            waitUntil -= 24 * 60;
        }
        if (dayMinute > waitUntil) {
            lastMinute = dayMinute;
            return;
        }
        final Calendar now = Calendar.getInstance();
        final int seconds = now.get(Calendar.SECOND);
        final long waitSeconds = (waitUntil - dayMinute) * 60 - seconds + timeDelay;
        try {
            Thread.yield();
            lastMinute = waitUntil;
            if (waitSeconds > 0) {
                Thread.sleep(waitSeconds * 1000);
            }
        } catch (final InterruptedException ie) {
            lastMinute -= refresh * 5;
        } catch (final IllegalArgumentException iae) {
        }
    }
}
