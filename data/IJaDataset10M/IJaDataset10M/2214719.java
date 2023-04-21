package org.blueoxygen.cimande.sitemanager;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.blueoxygen.cimande.modulefunction.ModuleFunction;
import org.blueoxygen.cimande.persistence.PersistenceManager;

/**
 * Treemenu recursive function This function will read the database module
 * function and generate a linkedlist The LinkedList will be use by
 * NavigationTree to generate MTMScript This function can be used to generate
 * another tree (XSL or any function in the future)
 *
 * @version 0.1
 * @author <a href="mailto:frans@blueoxygen.org">Frans Thamura</a>
 */
public class YUINavTreeLeaf {

    private ModuleFunction moduleFunction = null;

    private String rootId = "";

    private String sId = "";

    private String MTMJavaScript = "", variableNode = "";

    private int i, Node;

    private PersistenceManager pm;

    private HttpServletRequest request;

    String mySQL;

    /**
	 * Constructor for recursive only
	 */
    public YUINavTreeLeaf(String rootId, String variableNode, int Node, PersistenceManager manager, HttpServletRequest request) throws ClassNotFoundException, Exception {
        this.rootId = rootId;
        this.sId = rootId;
        this.moduleFunction = (ModuleFunction) manager.getById(ModuleFunction.class, rootId);
        this.Node = Node;
        this.variableNode = variableNode;
        this.pm = manager;
        this.request = request;
    }

    /**
	 * return root id
	 */
    public String getRootId() {
        return this.rootId;
    }

    /**
	 * return moduleFunction
	 */
    public ModuleFunction getRoot() {
        return this.moduleFunction;
    }

    /**
	 * return int that contains count all downline
	 */
    public int getChildCount() throws ClassNotFoundException, SQLException, Exception {
        mySQL = "FROM " + ModuleFunction.class.getName() + " mf WHERE mf.logInformation.activeFlag='1' AND mf.moduleFunction.id='" + this.sId + "'";
        List temp = new ArrayList();
        temp = pm.getList(mySQL, null, null);
        return temp.size();
    }

    public String getMTMJavaScript() throws ClassNotFoundException, SQLException, Exception {
        YUINavTreeLeaf dbTreeWalkerChild;
        String sParentId = "";
        i = 0;
        mySQL = "FROM mf in " + ModuleFunction.class + " WHERE mf.moduleFunction.id = '" + this.sId + "' ORDER BY(mf.description)";
        List<ModuleFunction> modules = new ArrayList<ModuleFunction>();
        modules = (List<ModuleFunction>) pm.getList(mySQL, null, null);
        for (ModuleFunction mf : modules) {
            sParentId = mf.getId();
            ModuleFunction mFunction = (ModuleFunction) pm.getById(ModuleFunction.class, sParentId);
            int totalChild;
            dbTreeWalkerChild = new YUINavTreeLeaf(mFunction.getId(), variableNode + "_" + Node, i, pm, request);
            totalChild = dbTreeWalkerChild.getChildCount();
            if (totalChild > 0) {
                MTMJavaScript = MTMJavaScript + "<div class=\"pkg\"><h3>" + mf.getDescription() + "</h3><div class=\"pkg-body\">";
                MTMJavaScript = MTMJavaScript + dbTreeWalkerChild.getMTMJavaScript();
                MTMJavaScript = MTMJavaScript + "</div></div>";
            } else {
                String sActionFlag = "", sUrlAction = "";
                sActionFlag = String.valueOf(mf.getModuleDescriptor().getTypeFlag());
                if (sActionFlag.equals("1")) {
                    sUrlAction = mf.getModuleDescriptor().getUrlAction() + "&";
                } else if (sActionFlag.equals("2")) {
                    sUrlAction = request.getContextPath() + "/module/" + mf.getModuleDescriptor().getName() + "/";
                } else if (sActionFlag.equals("3")) {
                    sUrlAction = request.getContextPath() + "/module/window/genform?window.id=" + mf.getModuleDescriptor().getWindow().getId() + "&";
                } else {
                    sUrlAction = request.getContextPath() + "/descriptor/" + mf.getModuleDescriptor().getUrlDescriptor() + "?";
                }
                MTMJavaScript = MTMJavaScript + "<div class=\"pkg\"><h3>" + mf.getDescription() + "</h3><div class=\"pkg-body\">";
                if (sActionFlag.equals("2")) {
                    MTMJavaScript = MTMJavaScript + "<a href=\"" + sUrlAction + "create\">New</a>";
                } else {
                    MTMJavaScript = MTMJavaScript + "<a href=\"" + sUrlAction + "action=new\">New</a>";
                }
                if (sActionFlag.equals("2")) {
                    MTMJavaScript = MTMJavaScript + "<a href=\"" + sUrlAction + "filter\">Search</a>";
                } else {
                    MTMJavaScript = MTMJavaScript + "<a href=\"" + sUrlAction + "action=search\">Search</a>";
                }
                MTMJavaScript = MTMJavaScript + "</div></div>";
            }
            i++;
        }
        return MTMJavaScript;
    }
}
