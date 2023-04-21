package de.sonivis.tool.core.gnu.r;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.rosuda.JRI.REXP;
import org.rosuda.JRI.RList;
import org.rosuda.JRI.Rengine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import de.sonivis.tool.core.CorePlugin;
import de.sonivis.tool.core.CoreTooling;
import de.sonivis.tool.core.DataModelPreferencesControl;
import de.sonivis.tool.core.ModelManager;
import de.sonivis.tool.core.SONIVISCore;
import de.sonivis.tool.core.datamodel.Graph;
import de.sonivis.tool.core.datamodel.Node;
import de.sonivis.tool.core.metricsystem.IMeasurable;

/**
 * Singleton instance for connection to GNU R.
 * 
 * @author Benedikt Meuthrath <br>
 *         Anne Baumgrass
 * @version $Date: 2010-03-20 13:05:37 -0400 (Sat, 20 Mar 2010) $<br>
 *          $Rev: 1580 $
 */
public final class RManager {

    private static Rengine rInstance;

    private static final RManager INSTANCE = new RManager();

    /**
	 * R constants
	 */
    public static final String R_PROJECT_NAME = "SONIVIS";

    public static final String R_GRAPH = "networkGraph";

    public static final String ERROR_MSG = "geterrmessage()";

    public static final String EXCEPTION_MEASUREABLE = "Exception at IMeasureable ";

    public static final String R_SCRIPT_FILE_NAME = "SONIVISCommands.R";

    public static final String R_DB_CONNECTION = "dbCon";

    private String lastException = null;

    private static Logger logger = LoggerFactory.getLogger(RManager.class.getName());

    private transient String rOutputFile = "";

    /**
	 * Getter for the instance of the RManager, to call GNU R routines, without loading any
	 * packages.
	 * 
	 * @return Instance of the RManager
	 */
    public static synchronized RManager getInstance() {
        return INSTANCE;
    }

    /**
	 * Getter for the actual exception which is thrown inside of R. Deletion of error message inside
	 * of R after getter is called.
	 * 
	 * @return String value of the last error message called in the R thread
	 */
    public String getLastException() {
        final String tempException = lastException;
        lastException = null;
        return tempException;
    }

    /**
	 * Constructor <br>
	 * <br>
	 * Inside a new REngine to call R routines is established, a file to export all called routines
	 * and the needed packages of R (sna, igraph, rJava) are loaded.
	 */
    private RManager() {
        logger = LoggerFactory.getLogger(RManager.class.getName());
        if (logger.isDebugEnabled()) {
            logger.debug("java.library.path: " + System.getProperty("java.library.path"));
            logger.debug("java.class.path: " + System.getProperty("java.class.path"));
            logger.debug("R_HOME: " + System.getenv("R_HOME"));
            logger.debug("PATH: " + System.getenv("PATH"));
        }
        rInstance = new Rengine(new String[] { "--vanilla" }, false, null);
        if (ResourcesPlugin.getWorkspace().getRoot().getProjects().length == 0) {
            final String path = SONIVISCore.getWorkspaceFolder().concat(File.separator).concat(R_PROJECT_NAME);
            final File pathFile = new File(path);
            if (!pathFile.exists()) {
                pathFile.mkdirs();
            }
            rOutputFile = path.concat(File.separator).concat(R_SCRIPT_FILE_NAME);
            final File file = new File(rOutputFile);
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (final IOException e) {
                    if (logger.isErrorEnabled()) {
                        logger.error("RManager initialization failed.", e);
                    }
                }
            }
        } else {
            final IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(R_PROJECT_NAME);
            rOutputFile = project.findMember(R_SCRIPT_FILE_NAME).getLocation().toOSString();
        }
        try {
            evalWithoutResult("library(igraph)");
            evalWithoutResult("library(rJava)");
            evalWithoutResult("library(Matrix)");
        } catch (final AbstractRException e) {
            if (logger.isErrorEnabled()) {
                logger.error("There where problems loading the R libraries for starting. ", e);
            }
        }
        rInstance.eval("{library(rJava);.jinit()}", false);
        rInstance.eval("{library(rJava);.jinit()}", false);
        try {
            evalWithoutResult("library(cluster)");
            evalWithoutResult("library(fpc)");
            evalWithoutResult("library(clv)");
            evalWithoutResult("library(Rstem)");
            evalWithoutResult("library(tm)");
        } catch (final AbstractRException e) {
            if (logger.isErrorEnabled()) {
                logger.error("There where problems loading the R libraries for text mining. ", e);
            }
        }
    }

    private void writeToOutputFile(final String rString) {
        if (!rOutputFile.equals("")) {
            try {
                final BufferedWriter bufWriter = new BufferedWriter(new FileWriter(rOutputFile, true));
                bufWriter.write(rString);
                bufWriter.newLine();
                bufWriter.flush();
                bufWriter.close();
            } catch (final IOException e) {
                final String message = "There was and I/O error while loading this file. Please try again. If this message persists, the file may be corrupted.";
                if (logger.isErrorEnabled()) {
                    logger.error("Error! {} {}", message, "");
                }
            }
        }
    }

    /**
	 * Executes an R evaluation and returns nothing.
	 * 
	 * @param rString
	 *            String to evaluate
	 * @throws AbstractRException
	 *             throws a exception when something went wrong
	 */
    public void evalWithoutResult(final String rString) throws AbstractRException {
        writeToOutputFile(rString);
        final REXP exp = rInstance.eval(rString);
        if (exp == null) {
            if (lastException == null) {
                lastException = rInstance.eval(ERROR_MSG).asString();
            }
            final RList rtrace = rInstance.eval(".Traceback").asList();
            String trace = "";
            if (rtrace != null) {
                trace = "\nTraceback\n";
                for (int i = 0; i < rtrace.keys().length; i++) {
                    trace += i + ": " + rtrace.at(i).asString() + "\n";
                }
            }
            throw new RNullException("R Expression was null with: " + rString + rInstance.eval(ERROR_MSG).asString() + trace);
        }
    }

    /**
	 * Executes an R evaluation and returns double.
	 * 
	 * @param rString
	 *            String to evaluate
	 * @return double value of return from GNU R
	 * @throws AbstractRException
	 *             throws a exception if the content or the return of the evaluation is null
	 */
    public double evalAsDouble(final String rString) throws AbstractRException {
        return eval(rString).asDouble();
    }

    /**
	 * Executes an R evaluation and returns double[].
	 * 
	 * @param rString
	 *            String to evaluate
	 * @return double array value of return from GNU R
	 * @throws AbstractRException
	 *             throws a exception if the content or the return of the evaluation is null
	 */
    public double[] evalAsDoubleArray(final String rString) throws AbstractRException {
        return eval(rString).asDoubleArray();
    }

    /**
	 * Executes an R evaluation and returns double[][].
	 * 
	 * @param rString
	 *            String to evaluate
	 * @return double matrix value of return from GNU R
	 * @throws AbstractRException
	 *             throws a exception if the content or the return of the evaluation is null
	 */
    public double[][] evalAsDoubleMatrix(final String rString) throws AbstractRException {
        return eval(rString).asDoubleMatrix();
    }

    /**
	 * Executes an R evaluation and returns String[].
	 * 
	 * @param rString
	 *            String to evaluate
	 * @return String array value of return from GNU R
	 * @throws AbstractRException
	 *             throws a exception if the content or the return of the evaluation is null
	 */
    public String[] evalAsStringArray(final String rString) throws AbstractRException {
        return eval(rString).asStringArray();
    }

    /**
	 * Executes an R evaluation and returns String.
	 * 
	 * @param rString
	 *            String to evaluate
	 * @return String value of return from GNU R
	 * @throws AbstractRException
	 *             throws a exception if the content or the return of the evaluation is null
	 */
    public String evalAsString(final String rString) throws AbstractRException {
        return eval(rString).asString();
    }

    /**
	 * Executes an R evaluation and returns boolean.
	 * 
	 * @param rString
	 *            String to evaluate
	 * @return boolean value of return from GNU R
	 * @throws AbstractRException
	 *             throws a exception if the content or the return of the evaluation is null
	 */
    public boolean evalAsBool(final String rString) throws AbstractRException {
        return eval(rString).asBool().isTRUE();
    }

    /**
	 * Executes an R evaluation and returns the String representation of the result may be
	 * <code>null</code>.
	 * 
	 * @param rString
	 *            String to evaluate
	 * @return String representation of the return from GNU R
	 */
    public String evalToString(final String rString) {
        final REXP exp = rInstance.eval(rString);
        return exp == null ? null : exp.toString();
    }

    /**
	 * Executes an R evaluation and returns int.
	 * 
	 * @param rString
	 *            String to evaluate
	 * @return int value of return from GNU R
	 * @throws AbstractRException
	 *             throws a exception if the content or the return of the
	 * 
	 */
    public int evalAsInt(final String rString) throws AbstractRException {
        final REXP exp = eval(rString);
        return exp == null ? null : exp.asInt();
    }

    /**
	 * Executes an R evaluation and returns int[].
	 * 
	 * @param rString
	 *            String to evaluate
	 * @return int array value of return from GNU R
	 * @throws AbstractRException
	 *             throws a exception if the content or the return of the
	 * 
	 */
    public int[] evalAsIntArray(final String rString) throws AbstractRException {
        final REXP exp = eval(rString);
        return exp == null ? null : exp.asIntArray();
    }

    public void createRJavaRef(final Object o) {
        rInstance.createRJavaRef(o);
    }

    /**
	 * Executes an R evaluation and checks weather the result is null. If the result is null, an
	 * error will be fired.
	 * 
	 * Parameters: eval ~ string of evaluation
	 * 
	 * @param rString
	 *            String to evaluate
	 * @return the result of the evaluation
	 * @throws AbstractRException
	 * 
	 */
    public REXP eval(final String rString) throws AbstractRException {
        REXP exp = null;
        writeToOutputFile(rString);
        exp = rInstance.eval(rString);
        if (exp == null) {
            if (lastException == null) {
                lastException = rInstance.eval(ERROR_MSG).asString();
            }
            final RList rtrace = rInstance.eval(".Traceback").asList();
            String trace = "";
            if (rtrace != null) {
                trace = "\nTraceback\n";
                for (int i = 0; i < rtrace.keys().length; i++) {
                    trace += i + ": " + rtrace.at(i).asString() + "\n";
                }
            }
            throw new RNullException("R Expression was null with: " + rString + rInstance.eval(ERROR_MSG).asString() + trace);
        } else if (exp.getContent() == null) {
            if (lastException == null) {
                lastException = "R ERROR: There is missing a sign in the R-Code: " + rString;
            }
            throw new REvalException("R ERROR: There is missing a sign in the R-Code: " + rString);
        }
        return exp;
    }

    /**
	 * Executes an R assign (Assigning a String Array)
	 * 
	 * @param variable
	 *            String (in R) to be assigned arrayString
	 * @param arrayString
	 *            String[] to assign
	 * 
	 */
    public void assign(final String variable, final String[] arrayString) {
        rInstance.assign(variable, arrayString);
    }

    public void assignJavaObj(final String variable, final Object o) {
        rInstance.assign("o", rInstance.createRJavaRef(o));
    }

    public void initializeDB() {
        try {
            evalWithoutResult(R_DB_CONNECTION + getRDBConnection());
        } catch (final AbstractRException e) {
            if (logger.isErrorEnabled()) {
                logger.error("There where problems initialize the DB with R. ", e);
            }
        }
    }

    public String getRDBConnection() {
        final String dbDRIVER = "MySQL";
        final String dbNAME = CorePlugin.getDefault().getPreferenceStore().getString(DataModelPreferencesControl.INTERNAL_DATABASE);
        final String dbUSERNAME = CorePlugin.getDefault().getPreferenceStore().getString(DataModelPreferencesControl.INTERNAL_DB_USERNAME);
        final String dbPW = CorePlugin.getDefault().getPreferenceStore().getString(DataModelPreferencesControl.INTERNAL_DB_PASSWORD);
        final String dbHOSTNAME = CorePlugin.getDefault().getPreferenceStore().getString(DataModelPreferencesControl.INTERNAL_DB_HOSTNAME);
        final Integer dbPORT = CorePlugin.getDefault().getPreferenceStore().getInt(DataModelPreferencesControl.INTERNAL_DB_PORT);
        return " <- dbConnect(dbDriver(\"" + dbDRIVER + "\"), dbname=\"" + dbNAME + "\", host=\"" + dbHOSTNAME + "\", port=" + dbPORT.toString() + ", username=\"" + dbUSERNAME + "\", password=\"" + dbPW + "\")";
    }

    public void addNode(final Long nodeId) {
        final long startTime = System.nanoTime();
        final String addVertices = R_GRAPH + "<- add.vertices(" + R_GRAPH + ",1,attr=list(id=\"" + nodeId + "\"))";
        try {
            evalWithoutResult(addVertices);
        } catch (final AbstractRException e) {
            if (logger.isErrorEnabled()) {
                logger.error("There where problems adding a node with the id '" + nodeId + "' to the R graph. ", e);
            }
        }
        if (logger.isDebugEnabled()) {
            logger.debug("Node added in " + CoreTooling.NANO_TIME_FACTOR * (System.nanoTime() - startTime) + CoreTooling.NANO_TIME_IDENTIFIER);
        }
    }

    /**
	 * This method returns the call (in String) for getting the corresponding id of a node (given by
	 * serial id) used in the R model.
	 * 
	 * @param nodeId
	 *            The serial id of a node for which the Id is needed.
	 * @return String to call the NodeId in R.
	 */
    public String getRcallForNodeID(final Long nodeId) {
        return "as.integer(V(" + R_GRAPH + ")[id==\"" + nodeId + "\"])";
    }

    /**
	 * This method returns the id of a node (given by its serial id) which is used in R.
	 * 
	 * @param nodeId
	 *            The serialId of a node for which the Id is needed.
	 * @return long Returns the id of a node used in R for the given node (given by serialId).
	 */
    public long getRNodeID(final Long nodeId) {
        long result = 0;
        try {
            result = evalAsInt(getRcallForNodeID(nodeId));
        } catch (final AbstractRException e) {
            if (logger.isErrorEnabled()) {
                logger.error("There where problems getting a node with the id '" + nodeId + "' in the R model.", e);
            }
        }
        return result;
    }

    /**
	 * This method returns the call (in String) for getting the corresponding id of a edge used in
	 * the R model.
	 * 
	 * @param edgeId
	 *            The serialId of the edge for which the Id is needed.
	 * @return String to call the EdgeId in R.
	 */
    public String getRcallForEdgeID(final Long edgeId) {
        return "as.integer(E(" + R_GRAPH + ")[id==\"" + edgeId + "\"])";
    }

    /**
	 * This method returns the id of a given edge which is used in R.
	 * 
	 * @param edgeId
	 *            The serialId of the edge for which the Id is needed.
	 * @return long Returns the id of a edge in R used for the given edge (by serialId).
	 */
    public long getREdgeID(final Long edgeId) {
        long result = 0;
        try {
            result = evalAsInt(getRcallForEdgeID(edgeId));
        } catch (final AbstractRException e) {
            if (logger.isErrorEnabled()) {
                logger.error("There where problems getting a edge with the id '" + edgeId + "' in the R model.", e);
            }
        }
        return result;
    }

    /**
	 * TODO
	 */
    public Map<Long, Long> getAllRNodeIDs(final Graph graph) {
        final List<Node> nodes = graph.getNodes();
        final Map<Long, Long> result = new HashMap<Long, Long>();
        for (final Node node : nodes) {
            if (node != null) {
                final Long nodeId = node.getSerialId();
                result.put(nodeId, getRNodeID(nodeId));
            }
        }
        return result;
    }

    /**
	 * This method adds a property in R to a given node.
	 * 
	 * @param node
	 *            The node for which the property is added.
	 * @param propertyName
	 *            The name of the property.
	 * @param property
	 *            The property value which is added to the node.
	 */
    public void addNodeProperty(final Long nodeSerialId, final String propertyName, final Object property) {
        final long startTime = System.nanoTime();
        if (property.getClass().getSimpleName().equals("String")) {
            final String addNodeProperty = getRcallForNodeID(nodeSerialId) + "$" + propertyName + " <- '" + property + "'";
            try {
                evalWithoutResult(addNodeProperty);
            } catch (final AbstractRException e) {
                if (logger.isErrorEnabled()) {
                    logger.error("There where problems adding the " + propertyName + " porperty to a node with the id '" + nodeSerialId + "' .", e);
                }
            }
        } else {
            final String addNodeProperty = getRcallForNodeID(nodeSerialId) + "$" + propertyName + " <- " + property;
            try {
                evalWithoutResult(addNodeProperty);
            } catch (final AbstractRException e) {
                if (logger.isErrorEnabled()) {
                    logger.error("There where problems adding the " + propertyName + " porperty to a node with the id '" + nodeSerialId + "' .", e);
                }
            }
        }
        if (logger.isDebugEnabled()) {
            logger.debug("Property " + propertyName + " added on a node with the id '" + nodeSerialId + "' in " + CoreTooling.NANO_TIME_FACTOR * (System.nanoTime() - startTime) + CoreTooling.NANO_TIME_IDENTIFIER);
        }
    }

    public void addNodePropertyNetworkWide(final String propertyName, final String rMetricName) {
        final long startTime = System.nanoTime();
        final String addNodeProperty = "V(" + R_GRAPH + ")$" + propertyName + " <- " + rMetricName;
        try {
            evalWithoutResult(addNodeProperty);
        } catch (final AbstractRException e) {
            if (logger.isErrorEnabled()) {
                logger.error("There where problems adding the " + propertyName + " porperty to all nodes.", e);
            }
        }
        if (logger.isDebugEnabled()) {
            logger.debug("Property " + propertyName + " added networkwide in " + CoreTooling.NANO_TIME_FACTOR * (System.nanoTime() - startTime) + CoreTooling.NANO_TIME_IDENTIFIER);
        }
    }

    public void addEdge(final Long edgeSerialId, final Long sourceSerialId, final Long targetSerialId) {
        final long startTime = System.nanoTime();
        final String addEdges = R_GRAPH + "<- add.edges(" + R_GRAPH + ", c(" + getRcallForNodeID(sourceSerialId) + "," + getRcallForNodeID(targetSerialId) + "))";
        try {
            evalWithoutResult(addEdges);
        } catch (final AbstractRException e) {
            if (logger.isErrorEnabled()) {
                logger.error("There where problems adding a edge between the nodes with the ids '" + sourceSerialId + "' and '" + targetSerialId + "' in the R model.", e);
            }
        }
        if (logger.isDebugEnabled()) {
            logger.debug("Edge added in " + CoreTooling.NANO_TIME_FACTOR * (System.nanoTime() - startTime) + CoreTooling.NANO_TIME_IDENTIFIER);
        }
    }

    public void removeNode(final Node node) {
        try {
            evalWithoutResult(R_GRAPH + "<- delete.vertices(" + R_GRAPH + ",c(" + ModelManager.getInstance().getIdToRIdMap().get(node.getSerialId()) + "))");
        } catch (final AbstractRException e) {
            if (logger.isErrorEnabled()) {
                logger.error("There where problems removing the node " + node + ".", e);
            }
        }
    }

    public void removeEdge(final Node startNode, final Node endNode) {
        try {
            evalWithoutResult(R_GRAPH + "<- delete.edges(" + R_GRAPH + ", E(" + R_GRAPH + ",P=c(" + +ModelManager.getInstance().getIdToRIdMap().get(startNode.getSerialId()) + "," + ModelManager.getInstance().getIdToRIdMap().get(endNode.getSerialId()) + "))");
        } catch (final AbstractRException e) {
            if (logger.isErrorEnabled()) {
                logger.error("There where problems removing the edge between nodes " + startNode + " and " + endNode + ".", e);
            }
        }
    }

    /**
	 * Executes an metric evaluation in GNU R.
	 * 
	 * @param measured
	 *            object for which the calculation is instantiated.
	 * @param metricDescr
	 *            String to evaluate
	 * @return double value of the metric calculated in R
	 * @throws AbstractRException
	 *             throws a exception when something went wrong in calling R methods
	 */
    public double getMetricFromR(final IMeasurable measured, final String metricDescr) throws AbstractRException {
        double erg = Double.NaN;
        try {
            erg = evalAsDouble(metricDescr);
        } catch (final AbstractRException ex) {
            if (logger.isErrorEnabled()) {
                logger.error(EXCEPTION_MEASUREABLE + measured.toString(), ex);
            }
        }
        return erg;
    }

    /**
	 * @return
	 */
    public String getROutputFile() {
        return rOutputFile;
    }

    /**
	 * Method to load a network from a xml file in GraphML format.
	 * 
	 * @param file
	 *            to read the network
	 * @throws IOException
	 */
    public void loadNetworkFromGraphML(final File file) throws IOException {
        if (file.isFile()) {
            if (file.canRead()) {
                final String rString = R_GRAPH + " <- read.graph(\"" + file.getCanonicalFile().toString().replace("\\", "/") + "\", format=\"graphml\")";
                try {
                    evalWithoutResult(rString);
                } catch (final AbstractRException e) {
                    if (logger.isErrorEnabled()) {
                        logger.error("There where problems reading the graph from GraphML in R.", e);
                    }
                }
            } else {
                if (logger.isWarnEnabled()) {
                    logger.warn(file.getCanonicalFile().toString() + " not readable!");
                }
            }
        } else {
            if (logger.isWarnEnabled()) {
                logger.warn(file.getCanonicalFile().toString() + " is not a file!");
            }
        }
    }
}
