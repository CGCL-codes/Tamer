package org.blueoxygen.cimande.sitemanager;

import org.blueoxygen.dal.DbBean;
import org.blueoxygen.cimande.sitemanager.NavigationModuleFunction;
import java.util.*;
import java.sql.*;

/** Treemenu recursive function
 * This function will read the database module function and generate a linkedlist
 * The LinkedList will be use by NavigationTree to generate MTMScript
 * This function can be used to generate another tree (XSL or any function in the future)
 * @version   0.1
 * @author    <a href="mailto:frans@blueoxygen.org">Frans Thamura</a>
 */
public class NavigationTreeMenu {

    private NavigationModuleFunction moduleFunction = null;

    private String rootId = "";

    private int recursiveChildCount = 0;

    private String sId = "";

    private int level = 0;

    private LinkedList recursiveList, levelList;

    private String MTMJavaScript = "", variableNode = "";

    private int i, Node;

    private String sTable = "module_function";

    ResultSet myResultSet = null;

    String mySQL;

    DbBean myDbBean = new DbBean();

    /**
	 * Constructor for recursive only
	 */
    public NavigationTreeMenu(String rootId, String variableNode, int Node) throws ClassNotFoundException, Exception {
        this.rootId = rootId;
        this.sId = rootId;
        this.moduleFunction = new NavigationModuleFunction(rootId, "");
        this.Node = Node;
        this.variableNode = variableNode;
    }

    /**
	  * return root id
	  */
    public String getRootId() {
        return this.rootId;
    }

    /**
	  * return level value of this object
	  * return a DbModule Object
	  */
    public NavigationModuleFunction getRoot() {
        return this.moduleFunction;
    }

    /**
	 * return LinkedList that contains all downline in ordered level
	 */
    public int getChildCount() throws ClassNotFoundException, SQLException, Exception {
        int totalFields = 0;
        myDbBean.connect();
        mySQL = "SELECT COUNT(*) as total FROM " + sTable + " WHERE active_flag='1' AND iparent='" + this.sId + "' ORDER BY description ASC ";
        myResultSet = myDbBean.execSQL(mySQL);
        myResultSet.next();
        totalFields = myResultSet.getInt("total");
        myDbBean.close();
        return totalFields;
    }

    /**
	 * return LinkedList that contains all downline in ordered level
	 */
    public String getMTMJavaScript() throws ClassNotFoundException, SQLException, Exception {
        NavigationTreeMenu dbTreeWalkerChild;
        Iterator iterChild;
        int levelChild;
        NavigationModuleFunction dbTreeChild, dbTreeChildIterator;
        myDbBean.connect();
        mySQL = "SELECT DISTINCT module_function.description as module_description, module_function.ref_desc, module_function.id,descriptor.url_descriptor, descriptor.type_flag, descriptor.url_action, descriptor.active_flag, descriptor.description, descriptor.name, module_function.viewall_flag FROM module_function, descriptor WHERE module_function.descriptor_id=descriptor.id AND descriptor.active_flag='1' AND iparent='" + this.sId + "' ORDER BY descriptor.description";
        myResultSet = myDbBean.execSQL(mySQL);
        String sParentId = "";
        i = 0;
        while (myResultSet.next()) {
            sParentId = myResultSet.getString("id");
            dbTreeChild = new NavigationModuleFunction(sParentId, "");
            MTMJavaScript = MTMJavaScript + variableNode + "_" + Node + ".MTMAddItem(new MTMenuItem(\"";
            int totalChild;
            dbTreeWalkerChild = new NavigationTreeMenu(dbTreeChild.getId(), variableNode + "_" + Node, i);
            totalChild = dbTreeWalkerChild.getChildCount();
            if (totalChild > 0) {
                MTMJavaScript = MTMJavaScript + myResultSet.getString("module_description") + "\"));" + "\n";
                ;
                MTMJavaScript = MTMJavaScript + "\n" + "var " + variableNode + "_" + Node + "_" + i + " = null;	" + variableNode + "_" + Node + "_" + i + " = new MTMenu();" + "\n";
                MTMJavaScript = MTMJavaScript + dbTreeWalkerChild.getMTMJavaScript() + "\n";
                MTMJavaScript = MTMJavaScript + variableNode + "_" + Node + ".items[" + i + "].MTMakeSubmenu(" + variableNode + "_" + Node + "_" + i + ");" + "\n";
            } else {
                String sActionFlag = "", sUrlAction = "";
                sActionFlag = myResultSet.getString("type_flag");
                if (sActionFlag.equals("1")) {
                    sUrlAction = myResultSet.getString("url_action") + "&";
                } else if (sActionFlag.equals("2")) {
                    sUrlAction = "../module/" + myResultSet.getString("name") + "/";
                    System.out.println("Action 2");
                } else {
                    sUrlAction = "../descriptor/" + myResultSet.getString("url_descriptor") + "?";
                }
                MTMJavaScript = MTMJavaScript + myResultSet.getString("description") + "\"));" + "\n";
                ;
                MTMJavaScript = MTMJavaScript + "\n" + "var " + variableNode + "_" + Node + "_" + i + " = null;	" + variableNode + "_" + Node + "_" + i + " = new MTMenu();" + "\n";
                MTMJavaScript = MTMJavaScript + variableNode + "_" + Node + "_" + i + ".MTMAddItem(new MTMenuItem(\"";
                MTMJavaScript = MTMJavaScript + "New";
                if (sActionFlag.equals("2")) {
                    MTMJavaScript = MTMJavaScript + "\",\"" + sUrlAction + "create.action\",\"text\"));" + "\n";
                } else {
                    MTMJavaScript = MTMJavaScript + "\",\"" + sUrlAction + "action=new\",\"text\"));" + "\n";
                }
                MTMJavaScript = MTMJavaScript + variableNode + "_" + Node + "_" + i + ".MTMAddItem(new MTMenuItem(\"";
                MTMJavaScript = MTMJavaScript + "Search";
                if (sActionFlag.equals("2")) {
                    MTMJavaScript = MTMJavaScript + "\",\"" + sUrlAction + "filter.action\",\"text\"));" + "\n";
                } else {
                    MTMJavaScript = MTMJavaScript + "\",\"" + sUrlAction + "action=search\",\"text\"));" + "\n";
                }
                MTMJavaScript = MTMJavaScript + variableNode + "_" + Node + ".items[" + i + "].MTMakeSubmenu(" + variableNode + "_" + Node + "_" + i + ");" + "\n";
            }
            i++;
        }
        myDbBean.close();
        return MTMJavaScript;
    }
}
