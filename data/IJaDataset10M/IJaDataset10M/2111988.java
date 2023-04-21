package org.mitre.lattice.taglib;

import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import org.mitre.lattice.graph.GraphUserLattice;
import org.mitre.lattice.lattice.InsufficentGroupAccessException;
import org.mitre.lattice.lattice.InvalidLatticeStructureException;
import org.mitre.lattice.lattice.LatticeFunctions;
import org.mitre.lattice.lattice.LatticeNode;
import org.mitre.lattice.lattice.LatticeTree;
import org.mitre.lattice.lattice.NodeNotFoundException;
import org.mitre.lattice.lattice.NullLatticeNodeException;
import org.mitre.mrald.util.Config;
import org.mitre.mrald.util.MraldException;
import org.mitre.mrald.util.User;

/**
 *@author     Gail Hamilton
 *@created    November 4, 2003
 */
public class GroupModifyListTag extends TagSupport {

    private String keyGroup = null;

    /**
	 *  Constructor for the AllTablesListTag object
	 */
    public GroupModifyListTag() {
    }

    /**
	 *  Gets the body attribute of the BuildForm object
	 *
	 *@param  keyGroup  Description of the Parameter
	 *@return           The body value
	 */
    public String getAddGroups(String keyGroup) {
        try {
            StringBuffer buffer = new StringBuffer();
            buffer.append("<input type=\"hidden\" NAME=\"Lattice\">\n");
            buffer.append("\n<tr><td colspan=\"1\">");
            buffer.append("<b>Specify Name</b></td>");
            buffer.append("\n<td colspan=\"2\"><input type='text' name=\"Lattice\"</td></tr>");
            String addLinks = getAddLinkGroups(keyGroup);
            return buffer.toString() + addLinks;
        } catch (Exception e) {
            return null;
        }
    }

    /**
	 *  Gets the body attribute of the BuildForm object
	 *
	 *@param  keyGroup  Description of the Parameter
	 *@return           The body value
	 */
    public String getDeleteLinks(String keyGroup) {
        try {
            StringBuffer buffer = new StringBuffer();
            ArrayList allGroups = (ArrayList) getAllGroups(keyGroup);
            buffer.append("<input type=\"hidden\" NAME=\"securityGroup\" value=\"" + keyGroup + "\">\n");
            buffer.append("\n<tr><td colspan=\"3\">");
            buffer.append("<b>Remove Link:</b></td></tr>\n");
            buffer.append("<tr><td colspan=\"1\"><b>Parent</b></td><td colspan=\"2\"><b>Child</b></td></tr>");
            int count = 0;
            for (int i = 0; i < allGroups.size(); i++) {
                LatticeNode linkNode = (LatticeNode) allGroups.get(i);
                String linkParent = linkNode.getName();
                ArrayList children = linkNode.getChildren();
                for (int j = 0; j < children.size(); j++) {
                    count++;
                    buffer.append("\n<tr>");
                    buffer.append("<td align='center'>" + linkParent + "</td>");
                    buffer.append("<td width=7 align='center'> " + ((LatticeNode) children.get(j)).getName() + "</td>");
                    buffer.append("<td width=7 align='center'><input type='checkbox' name='" + "Lattice" + count + "' value='Parent:" + linkParent + "~Child:" + ((LatticeNode) children.get(j)).getName() + "~Type:DeleteLink'></td>");
                    buffer.append("</tr>");
                }
            }
            return buffer.toString();
        } catch (Exception e) {
            return null;
        }
    }

    /**
	 *  Gets the body attribute of the BuildForm object
	 *
	 *@param  keyGroup  Description of the Parameter
	 *@return           The body value
	 */
    public String getAddLinkGroups(String keyGroup) throws MraldException {
        try {
            StringBuffer buffer = new StringBuffer();
            String groupName;
            ArrayList allGroups = (ArrayList) getAllGroups(keyGroup);
            buffer.append("<tr><td colspan=\"1\"><b>Or Select Group:</b><td colspan=\"2\"><SELECT NAME=\"Lattice\">\n<OPTION></OPTION>\n");
            for (int i = 0; i < allGroups.size(); i++) {
                groupName = ((LatticeNode) allGroups.get(i)).getName().toString();
                buffer.append("<OPTION value='addNode:" + groupName + "'>" + groupName + "</OPTION>");
            }
            buffer.append("</SELECT></td></tr>");
            buffer.append("<tr><td colspan=\"2\"><b>Show Whole Lattice</b></td><td colspan=\"1\"><input type='checkbox' name='" + "showLattice" + "'></td></tr>\n");
            buffer.append("\n<tr><td colspan=\"1\">");
            buffer.append("<b>Add Parent:</b></td>");
            buffer.append("<td colspan=\"1\"><b>Add Child:</b></td><td colspan=\"1\"><b>Groups</b></td></tr>");
            for (int i = 0; i < allGroups.size(); i++) {
                groupName = ((LatticeNode) allGroups.get(i)).getName().toString();
                buffer.append("\n<tr>");
                buffer.append("<td width=7 align='center'><input type='checkbox' name='Lattice' value=\"Parent:" + groupName + "~Type:Add\"></td>");
                buffer.append("<td width=7 align='center'><input type='checkbox' name='Lattice' value=\"Child:" + groupName + "~Type:Add\"></td>");
                buffer.append("<td>" + groupName + "</td>");
                buffer.append("</tr>");
            }
            return buffer.toString();
        } catch (IOException e) {
            throw new MraldException(e.getMessage());
        }
    }

    /**
	 *  Gets the body attribute of the BuildForm object
	 *
	 *@param  keyGroup  Description of the Parameter
	 *@return           The body value
	 */
    public String getDeleteGroups(String keyGroup) {
        try {
            StringBuffer buffer = new StringBuffer();
            String groupName;
            ArrayList allGroups = (ArrayList) getAllGroups(keyGroup);
            buffer.append("<tr><td colspan=\"2\"><b>Select Groups to Delete: </b></td></tr>\n");
            for (int i = 0; i < allGroups.size(); i++) {
                groupName = ((LatticeNode) allGroups.get(i)).getName().toString();
                buffer.append("\n<tr>");
                buffer.append("<td width=7 align='center'><input type='checkbox' name='Lattice' value='deleteGroup:" + groupName + "~Type:Delete'></td>");
                buffer.append("<td>" + groupName + "</td>");
                buffer.append("</tr>");
            }
            return buffer.toString();
        } catch (Exception e) {
            return null;
        }
    }

    /**
	 *  Gets the allGroups attribute of the GroupModifyListTag object
	 *
	 *@param  keyGroup                              Description of the Parameter
	 *@return                                       The allGroups value
	 *@exception  InsufficentGroupAccessException   Description of the Exception
	 *@exception  NodeNotFoundException             Description of the Exception
	 *@exception  NullLatticeNodeException          Description of the Exception
	 *@exception  InvalidLatticeStructureException  Description of the Exception
	 *@exception  MraldException                    Description of the Exception
	 */
    public java.util.List getAllGroups(String keyGroup) throws InsufficentGroupAccessException, NodeNotFoundException, NullLatticeNodeException, InvalidLatticeStructureException, MraldException, java.io.IOException {
        ArrayList allGroups = new ArrayList();
        LatticeTree newLattice = (LatticeTree) GraphUserLattice.getLattice();
        LatticeNode key = null;
        try {
            key = newLattice.searchTree(keyGroup);
        } catch (NodeNotFoundException e) {
            key = newLattice.getRootNode();
        }
        if (key != newLattice.getRootNode()) {
            LatticeTree childTree = new LatticeTree((LatticeNode) LatticeFunctions.getChildTree(key));
            if ((childTree == null) || (childTree.getRootNode() == null)) {
                throw new InsufficentGroupAccessException("You have insufficent privilidges to access this system. Please contact your system Administrator.");
            }
            allGroups = LatticeFunctions.getFlatLattice(childTree.getRootNode());
            java.util.List allGroupsSorted = LatticeFunctions.sort(allGroups);
            return allGroupsSorted;
        } else {
            LatticeTree childTree = new LatticeTree(Config.getLatticeFactory().copyNode(key));
            allGroups = LatticeFunctions.getFlatLattice(childTree.getRootNode());
            return allGroups;
        }
    }

    /**
	 *  Description of the Method
	 *
	 *@return                   Description of the Return Value
	 *@exception  JspException  Description of the Exception
	 */
    public int doStartTag() throws JspException {
        try {
            init();
            HttpServletRequest req = (HttpServletRequest) pageContext.getRequest();
            String groupList = process(req);
            pageContext.getOut().print(groupList);
            return 0;
        } catch (NullPointerException e) {
            HttpServletRequest req = (HttpServletRequest) pageContext.getRequest();
            HttpServletResponse resp = (HttpServletResponse) pageContext.getResponse();
            try {
                resp.sendRedirect(Config.getProperty("BaseUrl") + "/LatticeLogin.jsp?pageurl=" + req.getRequestURL());
            } catch (IOException ie) {
                JspException se = new JspException(ie.getMessage());
                se.fillInStackTrace();
                throw se;
            }
        } catch (IOException e) {
            JspException se = new JspException(e.getMessage());
            se.fillInStackTrace();
            throw se;
        }
        return 0;
    }

    /**
	 *  Description of the Method
	 *
	 *@return                   Description of the Return Value
	 *@exception  JspException  Description of the Exception
	 */
    public String process(HttpServletRequest req) throws JspException {
        try {
            init();
            User user = (User) req.getSession().getAttribute(Config.getProperty("cookietag"));
            try {
                keyGroup = user.getGroup();
            } catch (NullPointerException e) {
                throw e;
            }
            String modify = req.getParameter("ModifyNode");
            StringBuffer groupList = new StringBuffer();
            groupList.append("\n<tr><th colspan=\"2\"></th></tr><td><b>Current COI:</b></td><td><b>" + keyGroup + "</b></td></tr>");
            if (modify.equals("Add")) {
                groupList.append(getAddGroups(keyGroup));
            } else if (modify.equals("Delete")) {
                groupList.append(getDeleteGroups(keyGroup));
            } else if (modify.equals("Delete Link")) {
                groupList.append(getDeleteLinks(keyGroup));
            } else if (modify.equals("Add Link")) {
                groupList.append(getAddLinkGroups(keyGroup));
            }
            return groupList.toString();
        } catch (MraldException e) {
            throw new JspException(e);
        }
    }

    /**
	 *  Initializes the Lattice
	 */
    public void init() {
    }
}
