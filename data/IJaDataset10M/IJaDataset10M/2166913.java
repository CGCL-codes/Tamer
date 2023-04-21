package org.mitre.lattice.graph;

import org.mitre.mrald.util.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.mitre.lattice.lattice.InsufficentGroupAccessException;
import org.mitre.lattice.lattice.InvalidLatticeStructureException;
import org.mitre.lattice.lattice.LatticeElement;
import org.mitre.lattice.lattice.LatticeErrorHandler;
import org.mitre.lattice.lattice.LatticeException;
import org.mitre.lattice.lattice.LatticeFunctions;
import org.mitre.lattice.lattice.LatticeNode;
import org.mitre.lattice.lattice.LatticeTree;
import org.mitre.lattice.lattice.LatticeUserGroup;
import org.mitre.lattice.lattice.MultipleParentsException;
import org.mitre.lattice.lattice.NodeNotFoundException;
import org.mitre.mrald.control.AbstractStep;
import org.mitre.mrald.control.MsgObject;
import org.mitre.mrald.control.MsgObjectException;
import org.mitre.mrald.control.WorkflowStepException;
import org.mitre.mrald.util.Config;
import org.mitre.mrald.util.FileUtils;
import org.mitre.mrald.util.FormTags;
import org.mitre.mrald.util.MiscUtils;
import org.mitre.mrald.util.MraldConnection;
import org.mitre.mrald.util.MraldException;
import org.mitre.mrald.util.User;

/**
 *  This function is a wrapper class for graphing the Users Lattice. Based on a
 *  login.
 *
 *@author     ghamilton
 *@created    November 12, 2003
 */
public class GraphUserLattice extends AbstractStep {

    /**
     *  Description of the Field
     */
    public static final String KEY = GraphUserLattice.class.getName();

    /**
     *  Description of the Field
     */
    public static final Logger log = Logger.getLogger(KEY);

    static {
        log.setLevel(Level.FINEST);
    }

    private LatticeException latticeErr = null;

    private String keyGroup = null;

    private String[] userGroups = null;

    private static LatticeTree lattice = null;

    /**
     *  Constructor for the GraphUserLattice object
     */
    public GraphUserLattice() {
        try {
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *  Description of the Method
     *
     *@param  msg                                                Description of the
     *      Parameter
     *@exception  org.mitre.mrald.control.WorkflowStepException  Description of the
     *      Exception
     *@exception  MraldException                                 Description of the
     *      Exception
     */
    public void graphLatticeNonGeneric(MsgObject msg) throws org.mitre.mrald.control.WorkflowStepException, MraldException {
        try {
            LatticeFunctions.setImplmnt();
            String[] showLattice = msg.getValue("showLattice");
            boolean showWholeLattice = true;
            if (showLattice[0].equals("")) {
                showWholeLattice = false;
            }
            LatticeTree newLattice = (LatticeTree) getLattice();
            LatticeTree childTree = null;
            HttpServletRequest req = msg.getReq();
            HttpSession ses = req.getSession();
            LatticeUserGroup userGroup = null;
            User user = null;
            if (ses == null) {
                HttpServletResponse resp = msg.getRes();
                resp.sendRedirect(Config.getProperty("BaseUrl") + "/LatticeLogin.jsp?pageurl=" + req.getRequestURL());
                return;
            } else {
                user = (User) ses.getAttribute(Config.getProperty("cookietag"));
                userGroup = new LatticeUserGroup(user.getGroup());
            }
            ArrayList latticeElements = msg.getWorkingObjects();
            try {
                keyGroup = user.getGroup();
            } catch (NullPointerException e) {
            }
            newLattice = (LatticeTree) processElements(latticeElements, newLattice);
            if (!keyGroup.equals("")) {
                userGroup.setUserGroups(userGroups);
                String keyNodeStr = userGroup.getMostDominant(newLattice.getRootNode());
                LatticeNode key = newLattice.searchTree(keyNodeStr);
                if (!showWholeLattice) {
                    if (key != newLattice.getRootNode()) {
                        childTree = new LatticeTree((LatticeNode) LatticeFunctions.getChildTree(key));
                        if ((childTree == null) || (childTree.getRootNode() == null)) {
                            throw new InsufficentGroupAccessException("You have insufficent privilidges to access this system. Please contact your system Administrator..");
                        }
                    } else {
                        childTree = new LatticeTree(Config.getLatticeFactory().copyNode(key));
                    }
                } else {
                }
                GraphLattice newGraph = new GraphLattice();
                newGraph.graphSolution();
                GraphLattice.createImageFile(GraphLattice.getGraph(), user);
                String output = GraphLattice.convertToHtml(GraphLattice.getGraph(), Config.getProperty("latticeGraph"), showLattice[0], user);
                outputHtml(msg, output);
                storeLattice(newLattice);
            } else {
                latticeErr = new InsufficentGroupAccessException("LATT-003");
            }
        } catch (LatticeException e) {
            latticeErr = e;
        } catch (ServletException e) {
            throw new WorkflowStepException(e);
        } catch (IOException e) {
            throw new WorkflowStepException(e);
        } catch (MsgObjectException e) {
            throw new WorkflowStepException(e);
        } catch (Exception e) {
            throw new WorkflowStepException(e);
        }
    }

    /**
     *  Description of the Method
     *
     *@param  msg                                                Description of the
     *      Parameter
     *@exception  org.mitre.mrald.control.WorkflowStepException  Description of the
     *      Exception
     *@exception  MraldException                                 Description of the
     *      Exception
     */
    public void graphLattice(MsgObject msg) throws org.mitre.mrald.control.WorkflowStepException, MraldException {
        try {
            if (!Config.getLatticeFactory().getUsingLatticeSecurityModel()) latticeErr = new LatticeException("The Lattice Security Model is currently disabled. Please contact your system Administrator if you require Lattice Security to be enabled. ");
        } catch (LatticeException e) {
            latticeErr = e;
            throw e;
        }
        try {
            log.entering(KEY, "graphLattice(MsgObject)");
            LatticeFunctions.setImplmnt();
            String[] showLattice = msg.getValue("showLattice");
            boolean showWholeLattice = true;
            if (showLattice[0].equals("")) {
                showWholeLattice = false;
            }
            log.finest("About to start MinimizingDistance");
            LatticeTree newLattice = (LatticeTree) getLattice();
            log.finest("Lattice retrieved");
            LatticeTree childTree = null;
            HttpServletRequest req = msg.getReq();
            HttpSession ses = req.getSession();
            LatticeUserGroup userGroup = null;
            User user = null;
            if (ses == null) {
                HttpServletResponse resp = msg.getRes();
                resp.sendRedirect(Config.getProperty("BaseUrl") + "/LatticeLogin.jsp?pageurl=" + req.getRequestURL());
                return;
            } else {
                user = (User) ses.getAttribute(Config.getProperty("cookietag"));
                userGroup = new LatticeUserGroup(user.getGroup());
            }
            ArrayList latticeElements = msg.getWorkingObjects();
            try {
                keyGroup = user.getGroup();
            } catch (NullPointerException e) {
            }
            log.finest("About to process elements (add, delete, etc.");
            newLattice = (LatticeTree) processElements(latticeElements, newLattice);
            if (!keyGroup.equals("")) {
                log.finest("COI assigned: " + keyGroup);
                userGroup.setUserGroups(userGroups);
                String keyNodeStr = userGroup.getMostDominant(newLattice.getRootNode());
                LatticeNode key = newLattice.searchTree(keyNodeStr);
                if (!showWholeLattice) {
                    if (key != newLattice.getRootNode()) {
                        childTree = new LatticeTree((LatticeNode) LatticeFunctions.getChildTree(key));
                        if ((childTree == null) || (childTree.getRootNode() == null)) {
                            throw new InsufficentGroupAccessException("You have insufficent privilidges to access this system. Please contact your system Administrator..");
                        }
                    } else {
                        childTree = new LatticeTree(Config.getLatticeFactory().copyNode(key));
                    }
                    childTree = new LatticeTree((LatticeNode) LatticeFunctions.getSubTree(childTree, newLattice.getRootNode()));
                    MinimizingDistance.setLatticeTree(childTree);
                } else {
                    MinimizingDistance.setLatticeTree(newLattice);
                }
                Object[] solnPoints = MinimizingDistance.calcDistance();
                log.finest("About to creat Graph Lattice");
                GraphLattice newGraph = new GraphLattice();
                log.finest("Success: created GraphLattice");
                newGraph.graphSolution((Object[]) solnPoints[0], (Object[]) solnPoints[1]);
                log.finest("Dumping a new image file");
                GraphLattice.createImageFile(GraphLattice.getGraph(), user);
                String output = GraphLattice.convertToHtml(GraphLattice.getGraph(), Config.getProperty("latticeGraph"), showLattice[0], user);
                log.finest("sending HTML to the client");
                outputHtml(msg, output);
            } else {
                if (Config.usingSecurity) {
                    log.finest("No COI assigned - need to go log in");
                    latticeErr = new InsufficentGroupAccessException("LATT-003: You have insufficent privilidges to access this system. Please contact your system Administrator..");
                } else {
                    latticeErr = new LatticeException("Lattice Security cannot be enabled with Log In Security turned off. Please contact your system Administrator if you wish to have Lattice Security feature enabled. ");
                }
            }
            log.exiting(KEY, "graphLattice(MsgObject)");
        } catch (LatticeException e) {
            latticeErr = e;
        } catch (ServletException e) {
            throw new WorkflowStepException(e);
        } catch (IOException e) {
            throw new WorkflowStepException(e);
        } catch (MsgObjectException e) {
            throw new WorkflowStepException(e);
        } catch (Exception e) {
            throw new WorkflowStepException(e);
        }
    }

    /**
     *  This method prepares the output file for the HTML format data
     *
     *@return                     The lattice value
     *@exception  MraldException  Description of the Exception
     *@since
     */
    public static void resetLattice() throws MraldException {
        lattice = null;
    }

    /**
     *  This method prepares the output file for the HTML format data
     *
     *@return                     The lattice value
     *@exception  MraldException  Description of the Exception
     *@since
     */
    public static Object getLattice() throws MraldException {
        try {
            log.info("Checking to see if the lattice tree file is null");
            if (lattice == null) {
                String fileName = Config.getProperty("LatticeFile");
                log.info("About to get the lattice tree file");
                lattice = LatticeFunctions.recoverLatticeTree(fileName);
                if (lattice != null) {
                    log.info("Got old lattice file");
                    return lattice;
                } else {
                    log.info("Got completely new lattice file");
                    return SampleLattices.buildNewLattice();
                }
            }
            log.info("Returning the Lattice Tree from memory");
            return lattice;
        } catch (FileNotFoundException e) {
            log.log(Level.INFO, "Serialized lattice file wasn't found.  The lattice file is being blow away.", e);
            return SampleLattices.buildNewLattice();
        } catch (IOException e) {
            log.log(Level.INFO, "Other IO problem.  The lattice file is being blow away.", e);
            return SampleLattices.buildNewLattice();
        }
    }

    /**
     *  This method prepares the output file for the HTML format data
     *
     *@param  lattice             Description of the Parameter
     *@exception  MraldException  Description of the Exception
     *@since
     */
    public static void storeLattice(Object lattice) throws MraldException {
        try {
            String filename = Config.getProperty("LatticeFile");
            String latticeDir = filename.substring(0, filename.lastIndexOf("/"));
            if (!new File(latticeDir).exists()) {
                boolean created = new File(latticeDir).mkdirs();
                if (!created) {
                    throw new IOException("Couldn't create the directory \"" + latticeDir + "\".  Check your file system, though, some intermediate directories may have been created");
                }
            }
            if (!new File(filename).exists()) {
                boolean created = new File(Config.getProperty("LatticeFile")).createNewFile();
                if (!created) {
                    throw new IOException("Couldn't create the directory \"" + Config.getProperty("LatticeFile") + "\".  Check your file system, though, some intermediate directories may have been created");
                }
            }
            backupLattice(filename);
            LatticeFunctions.serializeLatticeTree((LatticeTree) lattice, filename);
        } catch (IOException ioe) {
            throw new MraldException(ioe.getMessage());
        }
    }

    /**
     *  This method prepares the output file for the HTML format data
     *
     *@param  filename            Description of the Parameter
     *@exception  MraldException  Description of the Exception
     *@since
     */
    public static void backupLattice(String filename) throws MraldException {
        try {
            String latticeFile = Config.getProperty("LatticeFile");
            String fileName = latticeFile.substring(latticeFile.lastIndexOf("/"));
            String latticeDir = latticeFile.substring(0, latticeFile.lastIndexOf("/"));
            String backupDir = latticeDir + "/backup/last";
            FileUtils fileUtils = new FileUtils();
            fileUtils.backupFile(latticeFile, backupDir + "/" + fileName);
            Calendar now = Calendar.getInstance();
            int month = new Integer(now.get(Calendar.MONTH)).intValue() + 1;
            String dateDirectory = month + "_" + now.get(Calendar.DAY_OF_MONTH) + "_" + now.get(Calendar.YEAR);
            dateDirectory = latticeDir + "/backup/" + dateDirectory;
            fileUtils.backupFile(latticeFile, dateDirectory + "/" + fileName);
        } catch (Exception e) {
            throw new MraldException(e.getMessage());
        }
    }

    /**
     *  This method prepares the output file for the HTML format data
     *
     *@param  deleteNodes                           Description of the Parameter
     *@param  lattice                               Description of the Parameter
     *@return                                       Description of the Return Value
     *@exception  NodeNotFoundException             Description of the Exception
     *@exception  LatticeException                  Description of the Exception
     *@exception  MultipleParentsException          Description of the Exception
     *@exception  InvalidLatticeStructureException  Description of the Exception
     *@since
     */
    public static Object deleteNode(ArrayList deleteNodes, Object lattice) throws NodeNotFoundException, LatticeException, MultipleParentsException, InvalidLatticeStructureException {
        if (deleteNodes.size() == 0) {
            return lattice;
        }
        LatticeElement elem = (LatticeElement) deleteNodes.get(0);
        MsgObject deleteMsg = elem.getNameValues();
        String[] deleteParents = deleteMsg.getValue("deleteGroup");
        for (int i = 0; i < deleteParents.length; i++) {
            String parentStr = deleteParents[i];
            LatticeNode deleteNode = ((LatticeTree) lattice).searchTree(parentStr);
            ArrayList parents = deleteNode.getParents();
            if (parents.size() > 1) {
                throw new MultipleParentsException("LATT-001");
            }
            for (int j = 0; j < parents.size(); j++) {
                LatticeNode parent = (LatticeNode) parents.get(j);
                parent.removeChild(deleteNode);
                parent.reAssignChild(deleteNode);
            }
            ArrayList children = deleteNode.getChildren();
            for (int k = 0; k < children.size(); k++) {
                LatticeNode child = (LatticeNode) children.get(k);
                child.removeParent(deleteNode);
            }
            deleteNode = null;
        }
        return lattice;
    }

    /**
     *  This method prepares the output file for the HTML format data
     *
     *@param  latticeElements                       Description of the Parameter
     *@param  lattice                               Description of the Parameter
     *@return                                       Description of the Return Value
     *@exception  NodeNotFoundException             Description of the Exception
     *@exception  InvalidLatticeStructureException  Description of the Exception
     *@exception  LatticeException                  Description of the Exception
     *@exception  MraldException                    Description of the Exception
     *@since
     */
    public Object processElements(ArrayList latticeElements, Object lattice) throws NodeNotFoundException, InvalidLatticeStructureException, LatticeException, MraldException {
        LatticeTree newLattice = (LatticeTree) lattice;
        ArrayList<LatticeElement> viewElements = new ArrayList<LatticeElement>();
        ArrayList<LatticeElement> addElements = new ArrayList<LatticeElement>();
        ArrayList<LatticeElement> deleteElements = new ArrayList<LatticeElement>();
        ArrayList<LatticeElement> deleteLinkElements = new ArrayList<LatticeElement>();
        for (int i = 0; i < latticeElements.size(); i++) {
            LatticeElement latticeElem = (LatticeElement) latticeElements.get(i);
            String type = (latticeElem.getNameValues().getValue("Type"))[0];
            if (type.equals("Add")) {
                addElements.add(latticeElem);
            } else if (type.equals("Delete")) {
                deleteElements.add(latticeElem);
            } else if (type.equals("DeleteLink")) {
                deleteLinkElements.add(latticeElem);
            } else if (type.equals("securityGroup")) {
                viewElements.add(latticeElem);
            } else {
                addElements.add(latticeElem);
            }
        }
        newLattice = (LatticeTree) addNode(addElements, newLattice);
        newLattice = (LatticeTree) deleteNode(deleteElements, newLattice);
        newLattice = (LatticeTree) deleteLink(deleteLinkElements, newLattice);
        keyGroup = getKeyGroup(viewElements);
        log.finest("Storing the lattice file");
        storeLattice(newLattice);
        return newLattice;
    }

    /**
     *  Gets the keyGroup attribute of the GraphUserLattice object
     *
     *@param  viewElements  Description of the Parameter
     *@return               The keyGroup value
     */
    public String getKeyGroup(ArrayList viewElements) {
        for (int i = 0; i < viewElements.size(); i++) {
            LatticeElement lattElem = (LatticeElement) viewElements.get(i);
            userGroups = lattElem.getNameValues().getValue("securityGroup");
        }
        if (userGroups == null) {
            userGroups = new String[] { keyGroup };
            return keyGroup;
        }
        if (userGroups.length > 0) {
            keyGroup = userGroups[0];
        }
        return keyGroup;
    }

    /**
     *  This method prepares the output file for the HTML format data
     *
     *@param  deleteLinks                           Description of the Parameter
     *@param  lattice                               Description of the Parameter
     *@return                                       Description of the Return Value
     *@exception  NodeNotFoundException             Description of the Exception
     *@exception  InvalidLatticeStructureException  Description of the Exception
     *@since
     */
    public static Object deleteLink(ArrayList deleteLinks, Object lattice) throws NodeNotFoundException, InvalidLatticeStructureException {
        MsgObject deleteMsg = null;
        LatticeTree latticeTree = (LatticeTree) lattice;
        if (latticeTree.getRootNode().equals(latticeTree.searchTree("Public"))) {
        } else {
        }
        for (int i = 0; i < deleteLinks.size(); i++) {
            LatticeElement latticeElem = (LatticeElement) deleteLinks.get(i);
            deleteMsg = latticeElem.getNameValues();
            String parent = deleteMsg.getValue("Parent")[0];
            LatticeNode deleteNode = latticeTree.searchTree(parent);
            String childName = deleteMsg.getValue("Child")[0];
            LatticeNode child = latticeTree.searchTree(childName);
            if (child.equals(latticeTree.getRootNode())) {
                throw new InvalidLatticeStructureException("LATT-006", "");
            }
            child.getParents().remove(deleteNode);
            if (!deleteNode.reAssignChild(child)) {
                deleteNode.reAssignParentsToPublic(latticeTree.getRootNode());
            }
        }
        return lattice;
    }

    /**
     *  This method prepares the output file for the HTML format data
     *
     *@param  addNodes                              The feature to be added to the
     *      Node attribute
     *@param  lattice                               The feature to be added to the
     *      Node attribute
     *@return                                       Description of the Return Value
     *@exception  NodeNotFoundException             Description of the Exception
     *@exception  InvalidLatticeStructureException  Description of the Exception
     *@exception  MraldException                    Description of the Exception
     *@since
     */
    public Object addNode(ArrayList addNodes, Object lattice) throws NodeNotFoundException, InvalidLatticeStructureException, MraldException {
        if (addNodes.size() == 0) {
            return lattice;
        }
        boolean childAdded = false;
        boolean parentAdded = false;
        boolean isNewNode = true;
        LatticeNode newNode = null;
        LatticeTree latticeTree = ((LatticeTree) lattice);
        LatticeElement elem = (LatticeElement) addNodes.get(0);
        MsgObject addMsg = elem.getNameValues();
        String newNodeName = addMsg.getValue(FormTags.VALUE_TAG)[0];
        if (newNodeName == "") {
            newNodeName = addMsg.getValue("addNode")[0];
        }
        try {
            newNode = latticeTree.searchTree(newNodeName);
            isNewNode = false;
        } catch (NodeNotFoundException e) {
            newNode = Config.getLatticeFactory().createNode(newNodeName);
            log.info(" Node creation here : " + newNodeName + " no type ");
            newNode = setCustomizable(addMsg, newNode);
        }
        String[] children = addMsg.getValue("Child");
        for (int i = 0; i < children.length; i++) {
            String childName = children[i];
            if (!childName.equals("")) {
                LatticeNode childKey = latticeTree.searchTree(childName);
                if (childKey.addParent(newNode)) {
                    childAdded = newNode.addChild(childKey);
                }
                if (childAdded) {
                    latticeTree.removeCyclic(childKey, newNode);
                }
            }
        }
        if (!childAdded && isNewNode) {
            LatticeNode childKey = latticeTree.searchTree("Public");
            if (childKey.addParent(newNode)) {
                childAdded = newNode.addChild(childKey);
            }
            if (childAdded) {
                latticeTree.removeCyclic(childKey, newNode);
            }
        }
        String[] addParents = addMsg.getValue("Parent");
        for (int i = 0; i < addParents.length; i++) {
            String parentName = addParents[i];
            if (!parentName.equals("")) {
                LatticeNode parentKey = latticeTree.searchTree(parentName);
                if (parentKey.addChild(newNode)) {
                    parentAdded = newNode.addParent(parentKey);
                }
                if (parentAdded) {
                    latticeTree.removeCyclicChild(parentKey, newNode);
                }
            }
        }
        addToLatticeGroups(new String[] { newNodeName });
        return latticeTree;
    }

    /**
     *  This method prepares the output file for the HTML format data
     *
     *@param  newNodes            The feature to be added to the ToLatticeGroups
     *      attribute
     *@exception  MraldException  Description of the Exception
     *@since
     */
    public static void addToLatticeGroups(String[] newNodes) throws MraldException {
        try {
            MraldConnection conn = new MraldConnection(MetaData.ADMIN_DB);
            String selectStr = "select latticegroupid from latticegroup ";
            ResultSet rs = conn.executeQuery(selectStr);
            ArrayList<String> cois = new ArrayList<String>();
            while (rs.next()) {
                cois.add(rs.getString(1));
            }
            String updateStatement = "INSERT INTO latticegroup values( '<:groupid:>', '<:groupName:>', '')";
            for (int i = 0; i < newNodes.length; i++) {
                if (!newNodes[i].equals("")) {
                    if (!cois.contains(newNodes[i])) {
                        String coi = MiscUtils.checkApostrophe(MiscUtils.checkApostrophe(newNodes[i]));
                        updateStatement = updateStatement.replaceAll("<:groupid:>", coi);
                        updateStatement = updateStatement.replaceAll("<:groupName:>", coi);
                        conn.executeUpdate(updateStatement);
                    }
                }
            }
            conn.close();
        } catch (Exception e) {
            MraldException me = new MraldException(e.getMessage());
            throw me;
        }
    }

    /**
     *  This method prepares the output file for the HTML format data
     *
     *@param  msg                     Description of the Parameter
     *@param  output                  Description of the Parameter
     *@exception  ServletException    Description of the Exception
     *@exception  IOException         Description of the Exception
     *@exception  MsgObjectException  Description of the Exception
     *@since
     */
    public static void outputHtml(MsgObject msg, String output) throws ServletException, IOException, MsgObjectException {
        HttpServletResponse res = msg.getRes();
        res.sendRedirect(Config.getProperty("BaseUrl") + "/LatticeManagement.jsp");
    }

    /**
     *@param  args  the command-line arguments.
     */
    public static void main(String[] args) {
        try {
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *  Description of the Method
     *
     *@param  msg                        Description of the Parameter
     *@exception  WorkflowStepException  Description of the Exception
     */
    public void execute(MsgObject msg) throws WorkflowStepException {
        try {
            graphLattice(msg);
            if (latticeErr != null) {
                LatticeErrorHandler.handleException(msg, latticeErr, true);
            }
        } catch (Exception e) {
            throw new WorkflowStepException(e);
        }
    }

    /**
     *  If there are any customizable attributes to be set Then these should be set
     *  in this method in the inherited class.
     *
     *@param  msg      The new customizable value
     *@param  newNode  The new customizable value
     *@return          Description of the Return Value
     *@since
     */
    public LatticeNode setCustomizable(MsgObject msg, LatticeNode newNode) {
        log.info("Node type is: None ");
        return newNode;
    }
}
