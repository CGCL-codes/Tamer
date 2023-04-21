package verinec.netsim.gui;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JOptionPane;
import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import verinec.VerinecException;
import verinec.netsim.ISimulation;
import verinec.netsim.NetSimException;
import verinec.netsim.SimulationFactory;
import verinec.netsim.constants.Layers;
import verinec.netsim.loggers.events.ApplicationEvent;
import verinec.netsim.loggers.events.Event;
import verinec.netsim.loggers.events.Events;
import verinec.netsim.loggers.events.EventsJDOMFactory;
import verinec.gui.VerinecStudio;
import verinec.gui.GuiUtil;
import verinec.gui.IVerinecModule;
import verinec.gui.core.NwComponent;
import verinec.gui.core.PCNode;
import verinec.util.FileUtil;
import verinec.util.LocalSAXBuilder;
import verinec.util.LogUtil;
import verinec.util.SchemaValidator;

/**
 * Simulator module for Verinec. 
 * Provides ways to start a simulation and visualizes the simulation output.
 * This module will disallow modifications.
 * 
 * @see SimulatorThread to learn how to run simulation in endless loop
 * @author david.buchmann at unifr.ch
 */
public class Simulator implements IVerinecModule {

    private Logger logger = Logger.getLogger(getClass().getName());

    /** Icon for the new action.
      * Taken from crystalsvg /usr/share/icons/crystalsvg/16x16/filesystems/ 
      */
    private static final String NETWORK_ICON = "/res/pictures/network_activity.png";

    private JMenu menuSimulation;

    private Action loadSimuInputAction;

    private Action saveSimuInputAction;

    private Action runSimulationAction;

    private Action loadSimuResultAction;

    private Action saveSimeResultAction;

    private Events events;

    private Events simulationEvents;

    private SimulationToolbar simulationToolbar;

    private VerinecStudio verinecStudio;

    private Component configPanel;

    /** @see verinec.gui.IVerinecModule#toString() */
    public String toString() {
        return "Simulator";
    }

    /**
	 * @see verinec.gui.IVerinecModule#load(verinec.gui.VerinecStudio)
	 */
    public void load(VerinecStudio parent) throws VerinecException {
        if (parent.isProjectModified()) {
            Object[] options = { "Revert to last saved version", "Save current network" };
            int sel = JOptionPane.CLOSED_OPTION;
            while (sel == JOptionPane.CLOSED_OPTION) {
                sel = JOptionPane.showOptionDialog(parent, "Loading simulator module.\nThe network must be saved or reverted to the last saved version.", "Save or revert?", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
            }
            if (sel == 0) {
                parent.reloadProject();
            } else {
                parent.saveProject();
            }
        }
        if (parent.getRepository() == null || parent.getRepository().getNodeNames().length == 0) {
            throw new VerinecException("Simulator module can not be loaded when network contains no nodes.");
        }
        verinecStudio = parent;
        verinecStudio.setModifyAllowed(false);
        loadSimuInputAction = new LoadSimuInputAction();
        saveSimuInputAction = new SaveSimuInputAction();
        loadSimuResultAction = new PlaySimuResultAction();
        saveSimeResultAction = new SaveSimuResultAction();
        runSimulationAction = new RunSimulationAction();
        menuSimulation = new JMenu("Simulation");
        menuSimulation.add(loadSimuInputAction);
        menuSimulation.add(saveSimuInputAction);
        menuSimulation.addSeparator();
        menuSimulation.add(runSimulationAction);
        menuSimulation.add(saveSimeResultAction);
        menuSimulation.addSeparator();
        menuSimulation.add(loadSimuResultAction);
        verinecStudio.addMenu(menuSimulation);
        simulationToolbar = new SimulationToolbar(verinecStudio, this);
        verinecStudio.addToolBar(simulationToolbar);
        Component[] components = verinecStudio.getNetworkComponents();
        for (int i = 0; i < components.length; i++) {
            if (components[i] instanceof NwComponent) updateComponent((NwComponent) components[i]);
        }
        String rep = null;
        String stopTime = "1000";
        try {
            rep = verinecStudio.getRepository().getProjectName();
        } catch (VerinecException ex) {
            throw new RuntimeException("Unexpected Verinec Exception", ex);
        }
        events = new Events(rep, stopTime);
        configPanel = new EventListPanel(parent, events);
        parent.setTopComponent(configPanel);
    }

    /** Adds context menues depending on the types.
      * When the module is loading, this method is called for each NwComponent to  
      * update the component.
     * @param c The component to update.      
     */
    public void updateComponent(NwComponent c) {
        if (c instanceof PCNode) {
            try {
                c.addContextMenu(new GenerateAction((PCNode) c));
            } catch (VerinecException e) {
                logger.throwing(getClass().getName(), "updateComponent", e);
            }
        }
    }

    /** Tell the module whether there is a result that can be saved.
      * 
      * @param b True if there is a result available, false otherwise.
      */
    public void resultAvailable(boolean b) {
        saveSimeResultAction.setEnabled(b);
    }

    /**
	  * Stops the simulation.
	  * @see verinec.gui.IVerinecModule#unload()
	  */
    public void unload() {
        simulationToolbar.terminateSimulation();
        verinecStudio = null;
    }

    /** Add an input event to the list for running the simulation.
     * 
     * @param input The input element.
     */
    public void addSimuInput(Event input) {
        events.addEvent(input);
        saveSimuInputAction.setEnabled(true);
        runSimulationAction.setEnabled(true);
    }

    /**
     * This method indicates the config Panel that something in the event list has changed. So it can reread this list and update the display.
     */
    public void inputChanged() {
        ((EventListPanel) configPanel).eventListChanged(events);
    }

    /**
     * This method enables the save menu point in the simulation Menu
     */
    public void enableSaveMenu() {
        saveSimuInputAction.setEnabled(true);
    }

    /** Use test definition objects to run the simulator with and play the results.
      * The simulation is performed by the verinec.netsim module.
      * 
      * @return True if successful, false if failed.
      */
    public boolean calculateSimulation() {
        simulationToolbar.terminateSimulation();
        verinecStudio.setStatus("Calculating simulation result.");
        try {
            ISimulation test = SimulationFactory.createSimulation(verinecStudio.getRepository(), events.getDocument());
            simulationEvents = test.start();
            verinecStudio.setStatus("Simulation result calculated.");
            simulationToolbar.setEvents(simulationEvents);
        } catch (NetSimException e) {
            JOptionPane.showMessageDialog(verinecStudio, "Could not calculate simulation result\n" + e.getMessage(), e.getMessage(), JOptionPane.ERROR_MESSAGE);
            verinecStudio.setStatus("Could not calculate simulation result.");
            logger.throwing(getClass().getName(), "load", e);
            return false;
        } catch (Throwable t) {
            verinecStudio.setStatus("Could not calculate simulation result: " + t.getMessage());
            logger.throwing(getClass().getName(), "load", t);
            return false;
        }
        return true;
    }

    /** Start playing the simulation. */
    public void playSimulation() {
        simulationToolbar.startPlayer(simulationEvents);
    }

    /** Load a test result file and display it.
     * Similar to the {@link #calculateSimulation()} method,
     * but loads an exisiting result file. 
     * After the file is loaded, automatically starts the simulation.
     * 
     * @param filename Name of a existing test result file.
     */
    public void loadResult(String filename) {
        simulationToolbar.terminateSimulation();
        verinecStudio.setStatus("Loading stored simulation result from " + filename);
        try {
            SAXBuilder xmlbuilder = LocalSAXBuilder.instance();
            Document result;
            try {
                xmlbuilder.setFactory(new EventsJDOMFactory());
                result = xmlbuilder.build(filename);
            } catch (JDOMException e) {
                throw new Exception("Either invalid XML or no XML file at all", e);
            }
            SchemaValidator validator = new SchemaValidator();
            if (!validator.validate(result)) throw new Exception("Invalid XML loaded");
            if (result.getRootElement().getChildren("event", SimuEvent.VERINEC_NAMESPACE).isEmpty()) {
                LogUtil.logJdom(logger, result.getRootElement());
                throw new SimulationException("No simulation events");
            }
            simulationEvents = (Events) result.getRootElement();
            verinecStudio.setStatus("Stored simulation result loaded from " + filename);
            playSimulation();
        } catch (Throwable e) {
            verinecStudio.setStatus("Problem occured loading simulation result from file: " + e.toString());
            logger.throwing(getClass().getName(), "loadResult", e);
            JOptionPane.showMessageDialog(verinecStudio, "A problem occured while loading result from file: " + e.getMessage());
        }
    }

    /** Will never be called because we have no config panel.
	  * @param c Configuration panel.
	  */
    public void saveConfiguration(Component c) {
    }

    /** Will never be called because we have no config panel.
	  * @param c Configuration panel.
	  */
    public void resetConfiguration(Component c) {
    }

    /** Shows the dialog to create/edit an event.
	 * @param event the event object that will be edited in the dialog.
	 * @param isNewEvent true if a new event will be created, false otherwise.
	 */
    public void editEvent(Event event, boolean isNewEvent) {
        try {
            EventCreationDialog d = new EventCreationDialog(verinecStudio, event, isNewEvent);
            verinecStudio.setBottomComponent(d);
        } catch (Throwable t) {
            String errormsg = "Error with event creation dialog: " + t.getMessage();
            JOptionPane.showMessageDialog(verinecStudio, errormsg, "Creation Dialog", JOptionPane.ERROR_MESSAGE);
            logger.throwing(getClass().getName(), "actionPerformed", t);
        }
    }

    /**
     * @param selectedIndices
     */
    public void editEvents(int[] selectedIndices) {
        try {
            EventDeletionDialog d = new EventDeletionDialog(verinecStudio, selectedIndices);
            verinecStudio.setBottomComponent(d);
        } catch (Throwable t) {
            String errormsg = "Error with event creation dialog: " + t.getMessage();
            JOptionPane.showMessageDialog(verinecStudio, errormsg, "Creation Dialog", JOptionPane.ERROR_MESSAGE);
            logger.throwing(getClass().getName(), "actionPerformed", t);
        }
    }

    /** Gets the input Events for the Simulator 
	 * @return the input Events for the Simulator
	 */
    public Events getEvents() {
        return events;
    }

    /** Shows the dialog to edit an Event from the Events List. 
	 * @param index the index of the Event from the Events List that should be edited.
	 */
    public void editEvent(int index) {
        Event e = (Event) events.getEventList().get(index);
        editEvent(e, false);
    }

    /** Display the dialog to generate input events. 
      * @author david.buchmann at unifr.ch
      */
    class GenerateAction extends AbstractAction {

        PCNode node;

        /** Instantiate with name Create Event.
          * @param n The node this action is for.  
          * @throws VerinecException If the icon can not be loaded.
          */
        public GenerateAction(PCNode n) throws VerinecException {
            super("Create Event", GuiUtil.loadIcon(Simulator.this, NETWORK_ICON));
            putValue(SHORT_DESCRIPTION, "Display the event creation dialog");
            node = n;
        }

        /** Execute action 
          * @param e unused 
          */
        public void actionPerformed(ActionEvent e) {
            Event event = new Event("0", node.getName(), Layers.APPLICATION, "application");
            ApplicationEvent appevent = new ApplicationEvent("launch", "prog", "parameters");
            event.setEventDetail(appevent);
            editEvent(event, true);
        }
    }

    /** Load simulation input. 
	  * @author david.buchmann at unifr.ch
	  */
    class LoadSimuInputAction extends AbstractAction {

        /** Initialize with name Load Simulation Input
		  */
        public LoadSimuInputAction() {
            super("Load Simulation Input");
        }

        /** Execute action 
		  * @param e unused 
		  */
        public void actionPerformed(ActionEvent e) {
            JFileChooser chooser = new JFileChooser();
            LocalSAXBuilder xmlbuilder;
            xmlbuilder = LocalSAXBuilder.instance();
            Document newInput = null;
            int returnVal = chooser.showOpenDialog(verinecStudio);
            while (returnVal == JFileChooser.APPROVE_OPTION) {
                String filename = chooser.getSelectedFile().getAbsolutePath();
                try {
                    xmlbuilder.setFactory(new EventsJDOMFactory());
                    newInput = xmlbuilder.build(filename);
                } catch (Throwable ex) {
                    String errormsg = "Could not load simulation input file: " + ex.getMessage();
                    JOptionPane.showMessageDialog(verinecStudio, errormsg, ex.getMessage(), JOptionPane.ERROR_MESSAGE);
                    verinecStudio.setStatus("Could not load simulation input file.");
                    logger.throwing(getClass().getName(), "actionPerformed", ex);
                }
                if (newInput == null) {
                    returnVal = chooser.showOpenDialog(verinecStudio);
                } else {
                    Events myevents = (Events) newInput.getRootElement();
                    String repositoryName = null;
                    try {
                        repositoryName = verinecStudio.getRepository().getProjectName();
                    } catch (VerinecException e1) {
                        e1.printStackTrace();
                    }
                    if (!myevents.getRepositoryName().equals(repositoryName)) {
                        Object[] options = { "open", "rollback" };
                        int selected = JOptionPane.CLOSED_OPTION;
                        while (selected == JOptionPane.CLOSED_OPTION) {
                            selected = JOptionPane.showOptionDialog(verinecStudio, "Open input File that was created for another Network\nFile may be incompatible", "Open or revert?", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
                        }
                        if (selected != 0) {
                            return;
                        }
                    }
                    events = myevents;
                    verinecStudio.setStatus("Simulation input loaded from " + filename);
                    saveSimuInputAction.setEnabled(false);
                    inputChanged();
                    return;
                }
            }
            verinecStudio.setStatus("Simulation input cancelled.");
        }
    }

    /** Save simulation input that was edited for this network. 
      * @author david.buchmann at unifr.ch
      */
    class SaveSimuInputAction extends AbstractAction {

        /** Instantiate with Save Simulation Input.
          * In the beginning we do not have input to save, thus this action 
          * is disabled by default.
          */
        SaveSimuInputAction() {
            super("Save Simulation Input");
            setEnabled(false);
        }

        /** Execute action 
          * @param e unused 
          */
        public void actionPerformed(ActionEvent e) {
            JFileChooser chooser = new JFileChooser();
            int returnVal;
            boolean retry = false;
            do {
                if (retry) {
                    returnVal = JOptionPane.showConfirmDialog(verinecStudio, chooser.getSelectedFile() + " exists. Do you want to overwrite?", "Target exists", JOptionPane.YES_NO_CANCEL_OPTION);
                } else {
                    returnVal = JOptionPane.NO_OPTION;
                }
                if (returnVal == JOptionPane.CANCEL_OPTION) {
                    verinecStudio.setStatus("Saving input events cancelled.");
                    return;
                } else if (returnVal == JOptionPane.NO_OPTION) {
                    if (chooser.showSaveDialog(verinecStudio) != JFileChooser.APPROVE_OPTION) {
                        verinecStudio.setStatus("Saving input events cancelled.");
                        return;
                    }
                    retry = chooser.getSelectedFile().exists();
                } else {
                    retry = false;
                }
            } while (retry);
            try {
                events.setRepositoryName(verinecStudio.getRepository().getProjectName());
            } catch (VerinecException e1) {
                JOptionPane.showMessageDialog(verinecStudio, "Error setting Repository Name", e1.getMessage(), JOptionPane.ERROR_MESSAGE);
            }
            String filePath = chooser.getSelectedFile().getAbsolutePath();
            try {
                FileUtil.saveXMLElement(events, filePath);
            } catch (VerinecException ex) {
                JOptionPane.showMessageDialog(verinecStudio, "Could not save simulation input", ex.getMessage(), JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /** Calculate simulation result and display it. 
      * @author david.buchmann at unifr.ch
      */
    class RunSimulationAction extends AbstractAction {

        /** Instantiate with Run Simulation.
          * Disabled by default
          */
        RunSimulationAction() {
            super("Run Simulation");
            setEnabled(false);
        }

        /** Execute action 
          * @param e unused 
          */
        public void actionPerformed(ActionEvent e) {
            if (calculateSimulation()) playSimulation();
        }
    }

    /** Save simulation result, normally created by the simulator. 
      * @author david.buchmann at unifr.ch
      */
    class SaveSimuResultAction extends AbstractAction {

        /** Instantiate with Save Simulation Result.
          * In the beginning we do not have a result to save, thus this action 
          * is disabled by default.
          */
        SaveSimuResultAction() {
            super("Save Simulation Result");
            setEnabled(false);
        }

        /** Execute action 
          * @param e unused 
          */
        public void actionPerformed(ActionEvent e) {
            JFileChooser chooser = new JFileChooser();
            int returnVal = chooser.showSaveDialog(verinecStudio);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                String filePath = chooser.getSelectedFile().getAbsolutePath();
                try {
                    FileUtil.saveXMLElement(simulationEvents, filePath);
                } catch (VerinecException ex) {
                    JOptionPane.showMessageDialog(verinecStudio, "Could not save simulation result", ex.getMessage(), JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    /** Load simulation result and display it. 
	  * @author david.buchmann at unifr.ch
	  */
    class PlaySimuResultAction extends AbstractAction {

        /** Instantiate with Load Simulation Result .*/
        PlaySimuResultAction() {
            super("Play stored Simulation Result");
        }

        /** Execute action 
		  * @param e unused 
		  */
        public void actionPerformed(ActionEvent e) {
            JFileChooser chooser = new JFileChooser();
            int returnVal = chooser.showOpenDialog(verinecStudio);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                String filePath = chooser.getSelectedFile().getAbsolutePath();
                loadResult(filePath);
            }
        }
    }
}
