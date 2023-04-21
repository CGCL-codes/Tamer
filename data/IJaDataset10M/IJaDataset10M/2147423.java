package org.openscience.cdk.applications.taverna.database.pgchem;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.embl.ebi.escience.baclava.DataThing;
import org.embl.ebi.escience.scufl.DuplicatePortNameException;
import org.embl.ebi.escience.scufl.InputPort;
import org.embl.ebi.escience.scufl.OutputPort;
import org.embl.ebi.escience.scufl.PortCreationException;
import org.embl.ebi.escience.scufl.XScufl;
import org.jdom.Element;
import org.openscience.cdk.applications.taverna.scuflworkers.cdk.CDKLocalWorker;
import org.openscience.cdk.applications.taverna.scuflworkers.cdk.CDKLocalWorkerWithDatabaseConnection;
import org.openscience.cdk.applications.taverna.scuflworkers.cdk.CDKProcessor;
import org.openscience.cdk.applications.taverna.scuflworkers.cdk.XMLExtensible;
import uk.ac.soton.itinnovation.taverna.enactor.entities.TaskExecutionException;

/**
 * Class which implements a local worker for the cdk-taverna project.
 * This worker calculates the molecular weight for molecules from the database. Therefore it uses the molweight function from the pgchem tigress postgres database extension.
 * @author Thomas Kuhn
 *
 */
public class GetMolecularWeightFromDB implements CDKLocalWorker, CDKLocalWorkerWithDatabaseConnection, XMLExtensible {

    private static final String[] inputNames = new String[] { "DatabaseURL", "UserName", "Password", "SQL_Query" };

    private static final String[] outputNames = new String[] { "moleculeIDList", "MoleuclarWeight" };

    /**
	 * Object which stores the database connection
	 */
    private DBConnector dbConnector;

    private static final String EXTENSIONS = "extensions";

    private static final String GETQSARVECTORFRMDB = "GetQSARVectorFromDB";

    /**
	 * Get the database connection for this worker
	 * @return the dbConnector
	 */
    public DBConnector getDbConnector() {
        return dbConnector;
    }

    /**
	 * Set the database connection of this worker
	 * @param dbConnector the dbConnector to set
	 */
    public void setDbConnector(DBConnector dbConnector) {
        this.dbConnector = dbConnector;
    }

    public String[] inputNames() {
        return inputNames;
    }

    public String[] inputTypes() {
        return new String[] { CDKLocalWorker.STRING, CDKLocalWorker.STRING, CDKLocalWorker.STRING, CDKLocalWorker.STRING };
    }

    public String[] outputNames() {
        return outputNames;
    }

    public String[] outputTypes() {
        return new String[] { CDKLocalWorker.STRING_ARRAY, CDKLocalWorker.DoubleList };
    }

    public List<InputPort> inputPorts(CDKProcessor processor) throws DuplicatePortNameException, PortCreationException {
        List<InputPort> input = new ArrayList<InputPort>();
        InputPort inPort;
        for (int i = 0; i < inputNames.length; i++) {
            inPort = new InputPort(processor, inputNames[i]);
            inPort.setSyntacticType(inputTypes()[i]);
            input.add(inPort);
        }
        return input;
    }

    public List<OutputPort> outputPorts(CDKProcessor processor) throws DuplicatePortNameException, PortCreationException {
        List<OutputPort> output = new ArrayList<OutputPort>();
        OutputPort outPort;
        for (int i = 0; i < outputNames.length; i++) {
            outPort = new OutputPort(processor, outputNames[i]);
            outPort.setSyntacticType(outputTypes()[i]);
            output.add(outPort);
        }
        return output;
    }

    /**
	 *This worker calculates the molecular weight for molecules from the database. Therefore it uses the molweight function from the pgchem tigress postgres database extension.
	 */
    public Map<String, DataThing> execute(Map<String, DataThing> inputs) throws TaskExecutionException {
        Map<String, DataThing> outputs = new HashMap<String, DataThing>();
        if (this.dbConnector == null) {
            this.dbConnector = new DBConnector();
        }
        if (inputs.get(inputNames[0]) != null) {
            this.dbConnector.setDbURL((String) ((DataThing) (inputs.get(inputNames[0]))).getDataObject());
        }
        if (inputs.get(inputNames[1]) != null) {
            this.dbConnector.setUserName((String) ((DataThing) (inputs.get(inputNames[1]))).getDataObject());
        }
        if (inputs.get(inputNames[2]) != null) {
            this.dbConnector.setUserPassword((String) ((DataThing) (inputs.get(inputNames[2]))).getDataObject());
        }
        PreparedStatement readStatement = null;
        ResultSet resultSet = null;
        List<Double> listOfMolecularWeights;
        List<String> moleucleIDs;
        try {
            if (this.dbConnector != null) {
                this.dbConnector.connect();
                readStatement = this.dbConnector.getConnectedPreparedStatement();
                resultSet = readStatement.executeQuery();
                moleucleIDs = new ArrayList<String>(10000);
                listOfMolecularWeights = new ArrayList<Double>(10000);
                while (resultSet.next()) {
                    moleucleIDs.add(String.valueOf(resultSet.getInt(1)));
                    listOfMolecularWeights.add(resultSet.getDouble(2));
                }
                outputs.put(outputNames[0], new DataThing(moleucleIDs));
                outputs.put(outputNames[1], new DataThing(listOfMolecularWeights));
                readStatement.close();
                this.dbConnector.closeConnection();
                if (readStatement != null && !readStatement.isClosed()) {
                    readStatement.close();
                }
                if (resultSet != null && !resultSet.isClosed()) {
                    resultSet.close();
                }
                if (resultSet != null) {
                    resultSet = null;
                }
                if (this.dbConnector != null) {
                    this.dbConnector.closeConnection();
                }
            }
        } catch (Exception exception) {
            throw new TaskExecutionException(exception);
        }
        return outputs;
    }

    public void consumeXML(Element element) {
        Element ele = element.getChild(GETQSARVECTORFRMDB, XScufl.XScuflNS);
        if (ele == null) {
            return;
        }
        this.dbConnector = new DBConnector(ele);
    }

    public Element provideXML() {
        Element extensions = new Element(EXTENSIONS, XScufl.XScuflNS);
        Element insertElement = new Element(GETQSARVECTORFRMDB, XScufl.XScuflNS);
        if (this.dbConnector == null) {
            this.dbConnector = new DBConnector();
        }
        insertElement.addContent(this.dbConnector.getAsXMLElement());
        extensions.addContent(insertElement);
        return extensions;
    }
}
