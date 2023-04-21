package com.impact.xbm.adapters;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import com.impact.xbm.events.UpdateHandler;
import com.impact.xbm.exceptions.XbmException;
import com.impact.xbm.server.XbmServerUtils;
import com.impact.xbm.server.semes.SemanticMediator;
import com.impact.xbm.utils.Xml;

/**
 * VAS - violent actions suppression - model models targeting hostile agents that produce violent actions
 * to reduce the number of violent actions.
 * An Actor is initialized with two pairs of properties:
 * disposition - VASdisposition and experience - VASexperience.
 * If disposition == HOSTILE, the the actor produces violent actions, otherwise it does absolutely nothing.
 * The number of actions depends on the experience.
 * The simulation proceeds in steps. Each step every active hostile actor performs violent actions.
 * The simulation keeps track of the total numbers of the violent actions.
 *
 * Agents can be disabled. Disabled agents do not perform violent actions.
 * Agents are disabled by VASCenter by sending messages. It sends messages to what it considers to be hostile agents,
 * which is determined by VASdisposition property. Messages come in three strength levels matching agents experience level.
 * message.strength = actor.VASexperience.
 * Messages are sent in the beginning of each step.
 * Each step VASCenter produces a predefined number of messages of each strength.
 * Then it loops through the messages and for each messages loops through the actors that are thought to be hostile
 * based on actor.VASdisposition. If message.strength == actor.VASexperience, the message is "sent".
 * If message.strenth >= actor.experience, the agent is disabled and doesn't do any hostile actions this step.
 * Otherwise the message is wasted.
 * However, a quick estimate of the effectiveness of disposition - VASdisposition and experience - VASexperience matching
 * can be done simply by going through every actor and comparing them without actually running the simulation
 *
 *
 */
public class VASModelToolAdapter extends StandardToolAdapterBase {

    public enum Disposition {

        HOSTILE, FRIENDLY, NEUTRAL
    }

    public enum Experience {

        NOVICE, COMPETENT, EXPERT
    }

    private ArrayList<Actor> actors;

    private HashMap<String, String> importOntologies;

    private HashMap<String, String> allOntologies;

    private String mediationFilename;

    private String mediationUri;

    private String datasourceUri;

    private String data;

    @Override
    public void initializeEventHandlerList() {
        UpdateHandler1 updateHandler1 = new UpdateHandler1();
        this.addUpdateEventHandler(updateHandler1);
    }

    @Override
    public void configure(String configurationData) {
        this.logger.info("In " + this.instanceName + ".configure; parameters:");
        try {
            super.configure(configurationData);
        } catch (Exception e) {
        }
        importOntologies = new HashMap<String, String>();
        allOntologies = new HashMap<String, String>();
        for (Iterator it = this.parameters.keySet().iterator(); it.hasNext(); ) {
            String name = (String) it.next();
            String value = this.parameters.get(name);
            this.logger.info("\t" + name + " = " + value);
            String[] ontoNameUri = value.split("@");
            allOntologies.put(ontoNameUri[0], ontoNameUri[1]);
            if (name.equals("mediationOntology")) {
                mediationFilename = ontoNameUri[0];
                mediationUri = ontoNameUri[1];
            } else if (name.startsWith("importOntology")) {
                importOntologies.put(ontoNameUri[0], ontoNameUri[1]);
            } else if (name.equals("datasourceOntology")) {
                importOntologies.put(ontoNameUri[0], ontoNameUri[1]);
                datasourceUri = ontoNameUri[1];
            }
        }
    }

    private void InitializeActors() {
        actors = new ArrayList<Actor>();
        int VASDispositionIndex = 0, VASExperienceIndex = 0;
        XPath xpath = XPathFactory.newInstance().newXPath();
        try {
            Document doc = Xml.readXmlFromString(data);
            NodeList dmNodes = Xml.xpathEvaluateNodeList("/XBM_Data/DataMatrixSection/DataMatrix", doc, xpath);
            int numDataMatrices = dmNodes.getLength();
            if (numDataMatrices != 1) {
                throw new XbmException("Expected one data matrix, got " + Integer.toString(numDataMatrices) + " instead.");
            }
            Element dmNode = (Element) dmNodes.item(0);
            int numRows = Integer.parseInt(dmNode.getAttribute("NumberOfRows"));
            int numColumns = Integer.parseInt(dmNode.getAttribute("NumberOfColumns"));
            Element rowColumnNames = Xml.xpathEvaluateElement("column_name", dmNode, xpath);
            String[] columnNames = rowColumnNames.getTextContent().split("\\s");
            for (int i = 0; i < columnNames.length; i++) {
                if (columnNames[i].equals("VASDisposition")) {
                    VASDispositionIndex = i;
                } else if (columnNames[i].equals("VASExperience")) {
                    VASExperienceIndex = i;
                }
            }
            NodeList rowNodes = Xml.xpathEvaluateNodeList("row", dmNode, xpath);
            if (numRows != rowNodes.getLength()) {
                throw new XbmException("NumberOfRows attribute does not match number of row elements");
            }
            for (int j = 0; j < numRows; j++) {
                Element rowNode = (Element) rowNodes.item(j);
                String rowData = rowNode.getTextContent();
                String[] columnData = rowData.split("\\s");
                if (columnData.length != numColumns) {
                    throw new XbmException("Wrong number of columns in row " + j);
                }
                Actor newActor = new Actor(columnData, VASDispositionIndex, VASExperienceIndex);
                actors.add(newActor);
            }
        } catch (Exception e) {
            System.out.print(e.getMessage());
        }
    }

    /**
     * Method to run in a thread, to run the adapter's tool
     * Must be public, to implement Runnable, but should always be called via runTool
     * tbd Can we enforce this?
     */
    public void run() {
        InitializeActors();
        this.logger.info("In run() of " + this.instanceName);
        SemanticMediator mediator = new SemanticMediator();
        mediator.SetOntologies(mediationFilename, importOntologies);
        mediator.SetOntoFiles(allOntologies);
        try {
            String fileName = XbmServerUtils.getXbmDataDirectory() + File.separator + "ontologies" + File.separator + "ProbOntDemo" + File.separator + "VAS.owl";
            ArrayList<ArrayList<String>> Data = mediator.QueryData("Actor", "http://www.impact-computing.com/VAS.owl", fileName);
        } catch (Exception e) {
            System.out.print(e.toString());
        }
        ArrayList<String> eventData = new ArrayList<String>();
        try {
            this.fireUpdateEvent(eventData);
        } catch (Exception e) {
        }
        System.out.print("VAS model received data: " + data);
        HashMap<String, Double> output = new HashMap<String, Double>();
        String msg = "\tRequested Outputs: ";
        for (int i = 0; i < this.requestedOutputs.size(); i++) {
            String requestOutput = this.requestedOutputs.get(i);
            msg += requestOutput;
            if (requestOutput.equals("numberOfRuns")) {
                output.put(requestOutput, (double) this.numberOfRuns);
            }
        }
        this.logger.info(msg);
        try {
            this.fireProcessingCompletedEvent(output);
        } catch (Exception e) {
        }
    }

    class Actor {

        private String disposition;

        private String experience;

        public Actor(String[] data, int dispositionIndex, int experienceIndex) {
            disposition = data[dispositionIndex];
            experience = data[experienceIndex];
        }
    }

    private class UpdateHandler1 extends UpdateHandler {

        @Override
        public void run() {
            try {
                VASModelToolAdapter.this.logger.info("In VASModelToolAdapter.UpdateHandler1");
                ArrayList<String> eventData = this.event.getData();
                for (Iterator it = eventData.iterator(); it.hasNext(); ) {
                    String s = (String) it.next();
                    VASModelToolAdapter.this.logger.info("\t" + s);
                    VASModelToolAdapter.this.data = s;
                }
            } catch (Exception ex) {
                VASModelToolAdapter.this.updateHandlerException = ex;
            }
        }
    }
}
