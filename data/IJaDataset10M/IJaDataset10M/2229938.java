package com.ext_it.mane.server;

import static org.junit.Assert.fail;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import org.dom4j.*;
import org.dom4j.io.*;
import com.ext_it.mane.client.model.EmailTemplate;
import com.ext_it.mane.client.model.EmailTemplate2;
import com.ext_it.mane.client.model.EmailTemplateAllData;
import com.ext_it.mane.client.model.EventRuleContainer;
import com.ext_it.mane.client.model.EventRuleTExecContainer;
import com.ext_it.mane.client.model.Folder;
import com.ext_it.mane.client.model.JdbcParameter;
import com.ext_it.mane.client.model.JdbcRuleContainer;
import com.ext_it.mane.client.model.Recurrence;
import com.ext_it.mane.client.model.RuleInFolder;
import com.ext_it.mane.client.model.RuneParameterValue;
import com.ext_it.mane.client.model.RuneRule;
import com.ext_it.mane.client.model.Recurrence.End;
import com.ext_it.mane.client.model.Recurrence.Pattern;
import com.ext_it.mane.core.Consts;
import com.ext_it.rune.RuneError;
import com.ext_it.rune.StartRune;
import com.ext_it.rune.util.RTAssertException;

/**
 * Interface between Rune and Mane: Event Rules
 * That are those that can be configured thru the UI - hence, methods in here that handle the IO.
 * This class also helps the caller with the synchronization ...
 * @author Administrator
 */
public class ManeToRuneIFEventRules {

    private final String cs_ER_JDBC_NAME = "Existing Jdbc Rules";

    private final String cs_ER_ROOT_NAME = "Existing Event Rules";

    private final String cs_EC_ROOT_NAME = "Existing Event Conditions";

    private final String cs_ET_ROOT_NAME = "Existing Email Templates";

    private final String cs_LastTreeSaveInMillis = "LastTreeSaveInMillis";

    private final String cs_NextIdCounter = "NextIdCounter";

    private final String cs_VAR_GV_EVENT_CODE = "GV_EVENT_CODE";

    private final String cs_VAR_GV_EVENT_DETAIL_INTEGER = "GV_EVENT_DETAIL_INTEGER";

    private final String cs_VAR_GV_CUSTOMER_ID = "GV_CUSTOMER_ID";

    private final String cs_VAR_GV_STUDENT_ID = "GV_STUDENT_ID";

    private final String cs_GV_CUSTOMER_EMAIL_ADDRESS = "GV_CUSTOMER_EMAIL_ADDRESS";

    private final String cs_GV_EMAIL_CC_RECIPIENTS = "GV_EMAIL_CC_RECIPIENTS";

    private final String cs_GV_EMAIL_BCC_RECIPIENTS = "GV_EMAIL_BCC_RECIPIENTS";

    private String m_sPathToEventRules;

    private String m_sPathToJdbcRules;

    private String m_sPAndFNEventRuleTree;

    private String m_sPAndFNJdbcRuleTree;

    private Document m_DOMEventRuleTree;

    private Document m_DOMJdbcRuleTree;

    private Document m_DOMAllEventRules;

    private Document m_DOMAllJdbcRules;

    private Document m_DOMEmailTemplateTree;

    private Document m_DOMEmailTemplates;

    private String m_sPAndFNEmailTemplateTree;

    private String m_sPAndFNEmailTemplateDetails;

    private String m_sPathToEmailTemplates;

    private String m_sFinalEmailsDestinationPath;

    private long m_lLastEventRulesTreeSaveInMillis;

    private long m_lLastJdbcRulesTreeSaveInMillis;

    private long m_lLastEmailTemplatesTreeSaveInMillis;

    class _ercTimeStamp {

        String m_sRuneRuleId;

        long m_lLastEventRuleSaveInMillis;
    }

    private ArrayList<_ercTimeStamp> m_lstercTimeStamps;

    public ManeToRuneIFEventRules(String sPathToEventRules, String sPAndFNEventRuleTree, Document DOMEventRuleTree, Document DOMAllEventRules, String sPathToJdbcRules, String sPAndFNJdbcRuleTree, Document DOMJdbcRuleTree, Document DOMAllJdbcRules, Document DOMEmailTemplates, Document DOMEmailTemplateTree, String sPAndFNEmailTemplateTree, String sPAndFNEmailTemplateDetails, String sPathToEmailTemplates, String sFinalEmailsDestinationPath) {
        m_sPathToEventRules = sPathToEventRules;
        m_sPathToJdbcRules = sPathToJdbcRules;
        m_sPAndFNEventRuleTree = sPAndFNEventRuleTree;
        m_sPAndFNJdbcRuleTree = sPAndFNJdbcRuleTree;
        m_DOMEventRuleTree = DOMEventRuleTree;
        m_DOMJdbcRuleTree = DOMJdbcRuleTree;
        m_DOMAllEventRules = DOMAllEventRules;
        m_DOMAllJdbcRules = DOMAllJdbcRules;
        m_DOMEmailTemplates = DOMEmailTemplates;
        m_DOMEmailTemplateTree = DOMEmailTemplateTree;
        m_sPAndFNEmailTemplateTree = sPAndFNEmailTemplateTree;
        m_sPAndFNEmailTemplateDetails = sPAndFNEmailTemplateDetails;
        m_sPathToEmailTemplates = sPathToEmailTemplates;
        m_sFinalEmailsDestinationPath = sFinalEmailsDestinationPath;
        m_lLastEventRulesTreeSaveInMillis = Calendar.getInstance().getTimeInMillis();
        m_lLastJdbcRulesTreeSaveInMillis = Calendar.getInstance().getTimeInMillis();
        m_lLastEmailTemplatesTreeSaveInMillis = Calendar.getInstance().getTimeInMillis();
        m_lstercTimeStamps = new ArrayList<_ercTimeStamp>();
    }

    /**
	 * Return the event rules tree structure of all existing folders and event rules 
	 * @return
	 */
    public Folder getEventRulesTree() {
        return getRulesTree(cs_ER_ROOT_NAME, m_DOMEventRuleTree, m_DOMAllEventRules, m_lLastEventRulesTreeSaveInMillis);
    }

    /**
	 * Return the jdbc rules tree structure of all existing folders and event rules 
	 * @return
	 */
    public Folder getJdbcRulesTree() {
        return getRulesTree(cs_ER_JDBC_NAME, m_DOMJdbcRuleTree, m_DOMAllJdbcRules, m_lLastJdbcRulesTreeSaveInMillis);
    }

    private Folder getRulesTree(String name, Document ruleTreeDom, Document allRulesDom, long lastSave) {
        Folder rootExRules = new Folder(name);
        List<?> oFolderNodes = ruleTreeDom.getRootElement().selectNodes("/root/folder");
        for (int i = 0; i < oFolderNodes.size(); i++) {
            Node oNodeFolder = (Node) oFolderNodes.get(i);
            Folder fRules = _getOneFolderForRuleTree(allRulesDom, oNodeFolder);
            rootExRules.add(fRules);
        }
        rootExRules.set(cs_LastTreeSaveInMillis, new Long(lastSave).toString());
        int iNextIdCounter = _getRuleNextIdCounter(allRulesDom);
        rootExRules.set(cs_NextIdCounter, new Integer(iNextIdCounter).toString());
        return rootExRules;
    }

    /**
	 * Return the email templates tree 
	 * @return
	 */
    public Folder getEmailTemplatesTree() {
        Folder root = new Folder(cs_ET_ROOT_NAME);
        List<?> oFolderNodes = m_DOMEmailTemplateTree.getRootElement().selectNodes("/root/folder");
        for (int i = 0; i < oFolderNodes.size(); i++) {
            Node oNodeFolder = (Node) oFolderNodes.get(i);
            Folder fRules = _getOneFolderForEmailTemplateTree(oNodeFolder);
            root.add(fRules);
        }
        root.set(cs_LastTreeSaveInMillis, new Long(m_lLastEmailTemplatesTreeSaveInMillis).toString());
        int iNextIdCounter = _getEmailTemplatesNextIdCounter();
        root.set(cs_NextIdCounter, new Integer(iNextIdCounter).toString());
        return root;
    }

    private int _getRuleNextIdCounter(Document dom) {
        return dom.getRootElement().selectNodes("/root/rules").size() + 1;
    }

    private int _getEmailTemplatesNextIdCounter() {
        int iCount = DBOperations.getEmailTemplateRecordCount();
        if (iCount < 0) {
            iCount = 1000;
        }
        return iCount;
    }

    /**
	 * Return the event conditions tree
	 * @param oDOMEventConditionTree
	 * @param maneToRuneIFCallback
	 * @return
	 */
    public Folder GetEventConditionsTree(Document oDOMEventConditionTree, ManeToRuneIF maneToRuneIFCallback) {
        Folder rootEventCondRules = new Folder(cs_EC_ROOT_NAME);
        List<?> oFolderNodes = oDOMEventConditionTree.getRootElement().selectNodes("/root/folder");
        for (int i = 0; i < oFolderNodes.size(); i++) {
            Node oNodeFolder = (Node) oFolderNodes.get(i);
            Folder fRules = _getOneFolderForEventConditionTree(oNodeFolder, maneToRuneIFCallback);
            rootEventCondRules.add(fRules);
        }
        return rootEventCondRules;
    }

    /** 
	 * Save the event rules tree structure passed in.
	 * @param f
	 * @return
	 */
    public boolean SaveEventRulesTree(Folder fRoot, StringBuffer rsb) {
        Document oDOM4Save = null;
        try {
            oDOM4Save = DocumentHelper.parseText("<root />");
        } catch (DocumentException de) {
        }
        assert (fRoot.getName().equals(cs_ER_ROOT_NAME));
        String sTimeStamp = fRoot.get(cs_LastTreeSaveInMillis);
        assert (sTimeStamp != null);
        if (!sTimeStamp.equals(new Long(m_lLastEventRulesTreeSaveInMillis).toString())) {
            rsb.append("The Event Rule tree has been saved by another user. Changes cannot be commited. Please reload the tree and apply your changes again!");
            return false;
        }
        Node oNodeRoot = oDOM4Save.getRootElement().selectSingleNode("/root");
        for (int i = 0; i < fRoot.getChildCount(); i++) {
            assert (fRoot.getChild(i).getClass().getName().equals(Folder.class.getName()));
            Folder f = (Folder) fRoot.getChild(i);
            Node oNodeFolder = CUtil.AddNode(oNodeRoot, "folder", "");
            CUtil.AddAttribute(oNodeFolder, "name", f.getName());
            if (!_saveEventRuleOneFolder(f, oNodeFolder, rsb)) return false;
        }
        try {
            CUtil.WriteXml(oDOM4Save, m_sPAndFNEventRuleTree);
            m_DOMEventRuleTree = oDOM4Save;
            m_lLastEventRulesTreeSaveInMillis = Calendar.getInstance().getTimeInMillis();
        } catch (IOException e) {
            rsb.append("An error occurred on the server while saving the Event Rule Tree in " + m_sPAndFNEventRuleTree + ". Error: " + e.toString());
            return false;
        }
        return true;
    }

    private boolean _saveEventRuleOneFolder(Folder fParent, Node oNodeFolder, StringBuffer rsb) {
        for (int i = 0; i < fParent.getChildCount(); i++) {
            if (fParent.getChild(i).getClass().getName().equals(Folder.class.getName())) {
                Folder f = (Folder) fParent.getChild(i);
                if (f.get("DELETED", "0").equals("1")) {
                    if (f.getName().length() > 0) {
                        Node oNodeSubFolder = oNodeFolder.selectSingleNode("folder[@name='" + f.getName() + "']");
                        if (oNodeSubFolder != null) {
                            Element el = (Element) oNodeFolder;
                            el.remove(oNodeSubFolder);
                        }
                    }
                } else {
                    Node oNodeSubFolder = CUtil.AddNode(oNodeFolder, "folder", "");
                    CUtil.AddAttribute(oNodeSubFolder, "name", f.getName());
                    if (!_saveEventRuleOneFolder(f, oNodeSubFolder, rsb)) return false;
                }
            } else {
                assert (fParent.getChild(i).getClass().getName().equals(RuleInFolder.class.getName()));
                RuleInFolder rif = (RuleInFolder) fParent.getChild(i);
                if (!rif.get("DELETED", "0").equals("1")) {
                    String sRuleId = rif.getID();
                    Node oNodeRule = CUtil.AddNode(oNodeFolder, "rule", "");
                    CUtil.AddAttribute(oNodeRule, "ref", sRuleId);
                    Node oRulesNode = m_DOMAllEventRules.getRootElement().selectSingleNode("/root/rules[@id='" + sRuleId + "']");
                    if (oRulesNode == null) {
                        oRulesNode = _createNewRule(sRuleId, rif.GetActive(), rif.getName());
                        if (!_saveRule(oRulesNode, sRuleId, rsb)) return false;
                    }
                }
            }
        }
        return true;
    }

    /** 
	 * Create a new rule in the DOM.
	 * @param sRuleId
	 * @param bIsActive
	 * @param sName
	 * @return
	 */
    private Node _createNewRule(String sRuleId, boolean bIsActive, String sName) {
        Node oNodeRoot = m_DOMAllEventRules.getRootElement().selectSingleNode("/root");
        Node oRulesNode = CUtil.AddNode(oNodeRoot, "rules", "");
        CUtil.AddAttribute(oRulesNode, "id", sRuleId);
        CUtil.AddAttribute(oRulesNode, "active", bIsActive ? "1" : "0");
        CUtil.AddAttribute(oRulesNode, "name", sName);
        return oRulesNode;
    }

    /** 
	 * Save the email templates tree structure passed in.
	 * @param f
	 * @return
	 */
    public boolean SaveEmailTemplatesTree(Folder fRoot, StringBuffer rsb) {
        Document oDOM4Save = null;
        try {
            oDOM4Save = DocumentHelper.parseText("<root />");
        } catch (DocumentException de) {
        }
        assert (fRoot.getName().equals(cs_ET_ROOT_NAME));
        String sTimeStamp = fRoot.get(cs_LastTreeSaveInMillis);
        assert (sTimeStamp != null);
        if (!sTimeStamp.equals(new Long(m_lLastEmailTemplatesTreeSaveInMillis).toString())) {
            rsb.append("The Email Templates tree has been saved by another user. Changes cannot be commited. Please reload the tree and apply your changes again!");
            return false;
        }
        Node oNodeRoot = oDOM4Save.getRootElement().selectSingleNode("/root");
        for (int i = 0; i < fRoot.getChildCount(); i++) {
            assert (fRoot.getChild(i).getClass().getName().equals(Folder.class.getName()));
            Folder f = (Folder) fRoot.getChild(i);
            Node oNodeFolder = CUtil.AddNode(oNodeRoot, "folder", "");
            CUtil.AddAttribute(oNodeFolder, "name", f.getName());
            if (!_saveEmailTemplateOneFolder(f, oNodeFolder, rsb)) return false;
        }
        try {
            CUtil.WriteXml(oDOM4Save, m_sPAndFNEmailTemplateTree);
            m_DOMEmailTemplateTree = oDOM4Save;
            m_lLastEmailTemplatesTreeSaveInMillis = Calendar.getInstance().getTimeInMillis();
        } catch (IOException e) {
            rsb.append("An error occurred on the server while saving the Event Rule Tree in " + m_sPAndFNEventRuleTree + ". Error: " + e.toString());
            return false;
        }
        return true;
    }

    private boolean _saveEmailTemplateOneFolder(Folder fParent, Node oNodeFolder, StringBuffer rsb) {
        for (int i = 0; i < fParent.getChildCount(); i++) {
            if (fParent.getChild(i).getClass().getName().equals(Folder.class.getName())) {
                Folder f = (Folder) fParent.getChild(i);
                if (f.get("DELETED", "0").equals("1")) {
                    Node oNodeSubFolder = oNodeFolder.selectSingleNode("folder[@name='" + f.getName() + "']");
                    if (oNodeSubFolder != null) {
                        Element el = (Element) oNodeFolder;
                        el.remove(oNodeSubFolder);
                    }
                } else {
                    Node oNodeSubFolder = CUtil.AddNode(oNodeFolder, "folder", "");
                    CUtil.AddAttribute(oNodeSubFolder, "name", f.getName());
                    if (!_saveEmailTemplateOneFolder(f, oNodeSubFolder, rsb)) return false;
                }
            } else {
                assert (fParent.getChild(i).getClass().getName().equals(RuleInFolder.class.getName()));
                RuleInFolder rif = (RuleInFolder) fParent.getChild(i);
                if (rif.get("DELETED", "0").equals("1")) {
                    String sRuleId = rif.getID();
                    if (!DBOperations.deactivateEmailTemplateIfItExists(sRuleId, rsb)) return false;
                } else {
                    String sRuleId = rif.getID();
                    Node oNodeRule = CUtil.AddNode(oNodeFolder, "rule", "");
                    CUtil.AddAttribute(oNodeRule, "ref", sRuleId);
                    EmailTemplate2 emt = null;
                    emt = DBOperations.getEmailTemplateByID(sRuleId, rsb);
                    if (emt == null) {
                        emt = new EmailTemplate2();
                        emt.setID(sRuleId);
                        String sName = "";
                        if (rif.getName() == null || rif.getName().length() == 0) sName = "New Email Template " + sRuleId; else sName = rif.getName();
                        emt.setTName(sName);
                        emt.setType("event");
                        if (!DBOperations.saveEmailTemplate(emt, rsb)) return false;
                    }
                }
            }
        }
        return true;
    }

    private boolean _saveRule(Node oRulesNode, String sRuleId, StringBuffer rsb) {
        Document oDOM = null;
        try {
            oDOM = DocumentHelper.parseText("<root />");
        } catch (DocumentException e) {
        }
        CUtil.CopyThisAndAllChildNodes(oRulesNode, oDOM.getRootElement().selectSingleNode("/root"));
        String sFilename = m_sPathToEventRules + File.separator + sRuleId + ".xml";
        try {
            CUtil.WriteXml(oDOM, sFilename);
        } catch (IOException e) {
            rsb.append("An error occurred on the server while saving the an Event Rule in " + sFilename + ". Error: " + e.toString());
            return false;
        }
        return true;
    }

    private Folder _getOneFolderForRuleTree(Document dom, Node oFolderNode) {
        String sFolderName = CUtil.GetNodeAttributeValue(oFolderNode, "", "name");
        Element elNodeRoot = (Element) oFolderNode;
        ArrayList<String> saRules = new ArrayList<String>();
        ArrayList<Folder> lstFolders = new ArrayList<Folder>();
        for (int iChild = 0; iChild < elNodeRoot.nodeCount(); iChild++) {
            Node oNodeChild = (Node) elNodeRoot.node(iChild);
            if (oNodeChild.getNodeType() == Node.ELEMENT_NODE) {
                if (oNodeChild.getName().equals("rule")) {
                    String sId = CUtil.GetNodeAttributeValue(oNodeChild, "", "ref");
                    if (sId.length() > 0) saRules.add(sId);
                } else if (oNodeChild.getName().equals("folder")) {
                    Folder f = _getOneFolderForRuleTree(dom, oNodeChild);
                    lstFolders.add(f);
                }
            }
        }
        RuleInFolder[] arrRIFs = null;
        if (saRules.size() > 0) arrRIFs = new RuleInFolder[saRules.size()];
        for (int i = 0; i < saRules.size(); i++) {
            Node oRulesNode = dom.getRootElement().selectSingleNode("/root/rules[@id='" + saRules.get(i) + "']");
            String sName = CUtil.GetNodeAttributeValue(oRulesNode, "", "name");
            String sDesc = CUtil.GetNodeAttributeValue(oRulesNode, "", "desc");
            String sActive = CUtil.GetNodeAttributeValue(oRulesNode, "", "active");
            arrRIFs[i] = new RuleInFolder(sName, saRules.get(i), sActive);
        }
        Folder fResult = null;
        if (saRules.size() > 0) fResult = new Folder(sFolderName, arrRIFs); else fResult = new Folder(sFolderName);
        for (int i = 0; i < lstFolders.size(); i++) {
            fResult.add(lstFolders.get(i));
        }
        return fResult;
    }

    private Folder _getOneFolderForEmailTemplateTree(Node oFolderNode) {
        String sFolderName = CUtil.GetNodeAttributeValue(oFolderNode, "", "name");
        Element elNodeRoot = (Element) oFolderNode;
        ArrayList<String> saRules = new ArrayList<String>();
        ArrayList<Folder> lstFolders = new ArrayList<Folder>();
        for (int iChild = 0; iChild < elNodeRoot.nodeCount(); iChild++) {
            Node oNodeChild = (Node) elNodeRoot.node(iChild);
            if (oNodeChild.getNodeType() == Node.ELEMENT_NODE) {
                if (oNodeChild.getName().equals("rule")) {
                    String sId = CUtil.GetNodeAttributeValue(oNodeChild, "", "ref");
                    if (sId.length() > 0) saRules.add(sId);
                } else if (oNodeChild.getName().equals("folder")) {
                    Folder f = _getOneFolderForEmailTemplateTree(oNodeChild);
                    lstFolders.add(f);
                }
            }
        }
        RuleInFolder[] arrRIFs = null;
        StringBuffer sb = new StringBuffer();
        ArrayList<RuleInFolder> lstRifs = new ArrayList<RuleInFolder>();
        for (int i = 0; i < saRules.size(); i++) {
            String sID = saRules.get(i);
            EmailTemplate2 emt = DBOperations.getEmailTemplateByID(sID, sb);
            if (emt != null) {
                RuleInFolder rif = new RuleInFolder(emt.getTName(), emt.getID(), emt.getActive().toString());
                lstRifs.add(rif);
            }
        }
        arrRIFs = (RuleInFolder[]) lstRifs.toArray(new RuleInFolder[0]);
        Folder fResult = null;
        if (lstRifs.size() > 0) fResult = new Folder(sFolderName, arrRIFs); else fResult = new Folder(sFolderName);
        for (int i = 0; i < lstFolders.size(); i++) {
            fResult.add(lstFolders.get(i));
        }
        return fResult;
    }

    private Folder _getOneFolderForEventConditionTree(Node oFolderNode, ManeToRuneIF maneToRuneIFCallback) {
        String sFolderName = CUtil.GetNodeAttributeValue(oFolderNode, "", "name");
        Element elNodeRoot = (Element) oFolderNode;
        ArrayList<String> saRules = new ArrayList<String>();
        ArrayList<Folder> lstFolders = new ArrayList<Folder>();
        for (int iChild = 0; iChild < elNodeRoot.nodeCount(); iChild++) {
            Node oNodeChild = (Node) elNodeRoot.node(iChild);
            if (oNodeChild.getNodeType() == Node.ELEMENT_NODE) {
                if (oNodeChild.getName().equals("rule")) {
                    String sId = CUtil.GetNodeAttributeValue(oNodeChild, "", "ref");
                    if (sId.length() > 0) saRules.add(sId);
                } else if (oNodeChild.getName().equals("folder")) {
                    Folder f = _getOneFolderForEventConditionTree(oNodeChild, maneToRuneIFCallback);
                    lstFolders.add(f);
                }
            }
        }
        RuleInFolder[] arrRIFs = null;
        if (saRules.size() > 0) arrRIFs = new RuleInFolder[saRules.size()];
        for (int i = 0; i < saRules.size(); i++) {
            RuneRule rr = maneToRuneIFCallback.RuneRuleByIdNTS(saRules.get(i));
            if (rr != null) arrRIFs[i] = new RuleInFolder(rr.GetName(), rr.GetId(), "1");
        }
        Folder fResult = null;
        if (saRules.size() > 0) fResult = new Folder(sFolderName, arrRIFs); else fResult = new Folder(sFolderName);
        for (int i = 0; i < lstFolders.size(); i++) {
            fResult.add(lstFolders.get(i));
        }
        return fResult;
    }

    /**
	 * Read a ERC from the DOM.
	 * @param sRuleId
	 * @param rsb
	 * @return
	 */
    public boolean ERCRead(ManeToRuneIF m2rIFCallback, EventRuleContainer erc, StringBuffer rsb) {
        boolean bResult = true;
        String sXPath;
        assert (erc.GetId().length() > 0);
        Node oNodeRule = m_DOMAllEventRules.getRootElement().selectSingleNode("/root/rules[@id='" + erc.GetId() + "']");
        if (oNodeRule == null) {
            oNodeRule = _createNewRule(erc.GetId(), erc.GetActive(), erc.GetName());
        }
        if (CUtil.GetNodeAttributeValue(oNodeRule, "", "active").equals("1")) erc.SetActive(true); else erc.SetActive(false);
        erc.SetName(CUtil.GetNodeAttributeValue(oNodeRule, "", "name"));
        erc.SetDesc(CUtil.GetNodeAttributeValue(oNodeRule, "", "desc"));
        erc.setJdbcRuleID(CUtil.GetNodeAttributeValue(oNodeRule, "", "jdbcRuleID"));
        Recurrence recurrence = new Recurrence();
        erc.setRecurrence(recurrence);
        ReadRecurrence(oNodeRule, recurrence);
        sXPath = "if/then/if/then/setVar/const";
        String sIDTemplateA = CUtil.GetXml(oNodeRule, sXPath);
        if (sIDTemplateA.length() > 0) {
            EmailTemplate et = m2rIFCallback.GetEmailTemplateByIdNTS(sIDTemplateA);
            erc.SetEmailTemplateA(et);
        }
        sXPath = "if/then/if/else/setVar/const";
        String sIDTemplateB = CUtil.GetXml(oNodeRule, sXPath);
        if (sIDTemplateB.length() > 0) {
            EmailTemplate et = m2rIFCallback.GetEmailTemplateByIdNTS(sIDTemplateB);
            erc.SetEmailTemplateB(et);
        }
        sXPath = "if/then/if/test/lessThanOrEqual/right/const";
        String sTemplateAPercent = CUtil.GetXml(oNodeRule, sXPath);
        if (!CUtil.IsInteger(sTemplateAPercent)) {
            erc.SetEmailTemplateAPercent(100);
            erc.SetEmailTemplateBPercent(0);
        } else {
            int iPercA = Integer.parseInt(sTemplateAPercent);
            erc.SetEmailTemplateAPercent(iPercA);
            erc.SetEmailTemplateBPercent(100 - iPercA);
        }
        sXPath = "if/test/and/exprLogic/logic";
        String sLogic = CUtil.GetXml(oNodeRule, sXPath);
        erc.SetCombinationLogic(sLogic);
        sXPath = "if/test/and/exprLogic/callRef";
        List<?> oCallRefNodes = oNodeRule.selectNodes(sXPath);
        for (int ir = 0; ir < oCallRefNodes.size(); ir++) {
            Node oCallRefNode = (Node) oCallRefNodes.get(ir);
            String sName = CUtil.GetNodeAttributeValue(oCallRefNode, "", "name");
            String sRef = CUtil.GetNodeAttributeValue(oCallRefNode, "", "ref");
            RuneRule rr = m2rIFCallback.RuneRuleByIdNTS(sRef);
            rr.SetLogicName(sName);
            erc.AddRuneRule(rr);
            List<?> oParameterNodes = oCallRefNode.selectNodes("parameters/parameter");
            for (int ip = 0; ip < oParameterNodes.size(); ip++) {
                Node oParameterNode = (Node) oParameterNodes.get(ip);
                String sId = CUtil.GetNodeAttributeValue(oParameterNode, "", "name");
                String sRuleRef = sRef;
                String sVal = CUtil.GetXml(oParameterNode, "const");
                assert (erc.GetRuneParameterValue(sRuleRef, sId) != null);
                erc.GetRuneParameterValue(sRuleRef, sId).SetValue(sVal);
            }
        }
        sXPath = "if/then/callRef[@ref='_writeTracking']/parameters/parameter[@name='";
        String sCampaignId = CUtil.GetXml(oNodeRule, sXPath + "P_CAMPAIGN_ID']/const");
        String sOfferId = CUtil.GetXml(oNodeRule, sXPath + "P_OFFER_ID']/const");
        String sVehicleId = CUtil.GetXml(oNodeRule, sXPath + "P_VEHICLE_ID']/const");
        String sChannelId = CUtil.GetXml(oNodeRule, sXPath + "P_CHANNEL_ID']/const");
        String sKeywords = CUtil.GetXml(oNodeRule, sXPath + "P_KEYWORDS']/const");
        if (CUtil.IsInteger(sCampaignId)) erc.SetCampaignId(Integer.parseInt(sCampaignId));
        if (CUtil.IsInteger(sOfferId)) erc.SetOfferId(Integer.parseInt(sOfferId));
        if (CUtil.IsInteger(sVehicleId)) erc.SetVehicleId(Integer.parseInt(sVehicleId));
        if (CUtil.IsInteger(sChannelId)) erc.SetChannelId(Integer.parseInt(sChannelId));
        erc.SetKeywords(sKeywords);
        erc.SetLastSaveTimestamp(_getRuneRuleTimestamp(erc.GetId()));
        return bResult;
    }

    private void ReadRecurrence(Node nodeRule, Recurrence recurrence) {
        Node recurrenceNode = nodeRule.selectSingleNode(Consts.XML_recurrence);
        if (recurrenceNode == null) {
            recurrence.setOn(false);
            return;
        }
        Node patternNode = recurrenceNode.selectSingleNode(Consts.XML_patern);
        if (patternNode == null) {
            recurrence.setOn(false);
            return;
        }
        Node rangeNode = recurrenceNode.selectSingleNode(Consts.XML_range);
        if (rangeNode == null) {
            recurrence.setOn(false);
            return;
        }
        recurrence.setOn(Boolean.parseBoolean(CUtil.GetNodeAttributeValue(nodeRule, Consts.XML_recurrence, Consts.XML_on)));
        Node node;
        if (patternNode.selectSingleNode(Consts.XML_repeat_daily) != null) {
            recurrence.setPattern(Pattern.DAILY);
        } else if (patternNode.selectSingleNode(Consts.XML_repeat_weekly) != null) {
            recurrence.setPattern(Pattern.WEEKLY);
        } else if (patternNode.selectSingleNode(Consts.XML_repeat_monthly) != null) {
            recurrence.setPattern(Pattern.MONTHLY);
        } else if (patternNode.selectSingleNode(Consts.XML_repeat_yearly) != null) {
            recurrence.setPattern(Pattern.YEARLY);
        } else if (patternNode.selectSingleNode(Consts.XML_repeat_every_weekday) != null) {
            recurrence.setPattern(Pattern.EVERY_WEEKDAY);
        } else if ((node = patternNode.selectSingleNode(Consts.XML_repeat_every_n_days)) != null) {
            recurrence.setEveryNDays(Integer.parseInt(node.getText()));
            recurrence.setPattern(Pattern.EVERY_N_DAYS);
        } else {
            throw new RTAssertException("DOM Error", "wrong recurr pattern");
        }
        SimpleDateFormat dateFormatter = new SimpleDateFormat(Consts.XML_date_pattern);
        if ((node = rangeNode.selectSingleNode(Consts.XML_start_date)) != null) {
            try {
                recurrence.setStartDate(dateFormatter.parse(node.getText()));
            } catch (ParseException e) {
                throw new RTAssertException("DOM Error-Wrong Start Date", e.getMessage());
            }
        } else {
            throw new RTAssertException("DOM Error", "start-date not found");
        }
        if ((node = rangeNode.selectSingleNode(Consts.XML_end_date)) != null) {
            try {
                recurrence.setEndDate(dateFormatter.parse(node.getText()));
                recurrence.setEnd(End.END_DATE);
            } catch (ParseException e) {
                throw new RTAssertException("DOM Error-Wrong End Date", e.getMessage());
            }
        } else if ((node = rangeNode.selectSingleNode(Consts.XML_end_after_occurences)) != null) {
            recurrence.setEndAfterNOccur(Integer.parseInt(node.getText()));
            recurrence.setEnd(End.END_AFTER_N_OCCUR);
        } else {
            recurrence.setEnd(End.NO_END);
        }
    }

    private void WriteRecurrence(Node nodeRule, Recurrence recurrence) {
        Node recurrenceNode = nodeRule.selectSingleNode(Consts.XML_recurrence);
        if (recurrenceNode == null) {
            recurrenceNode = CUtil.AddNode(nodeRule, Consts.XML_recurrence, "");
        } else {
            CUtil.RemoveAllChildren(recurrenceNode);
        }
        CUtil.SetNodeAttributeValue(recurrenceNode, "", Consts.XML_on, (recurrence.isOn() ? "true" : "false"));
        Node patternNode = CUtil.AddNode(recurrenceNode, Consts.XML_patern, "");
        switch(recurrence.getPattern()) {
            case DAILY:
                CUtil.AddNode(patternNode, Consts.XML_repeat_daily, "");
                break;
            case WEEKLY:
                CUtil.AddNode(patternNode, Consts.XML_repeat_weekly, "");
                break;
            case MONTHLY:
                CUtil.AddNode(patternNode, Consts.XML_repeat_monthly, "");
                break;
            case YEARLY:
                CUtil.AddNode(patternNode, Consts.XML_repeat_yearly, "");
                break;
            case EVERY_WEEKDAY:
                CUtil.AddNode(patternNode, Consts.XML_repeat_every_weekday, "");
                break;
            case EVERY_N_DAYS:
                CUtil.AddNode(patternNode, Consts.XML_repeat_every_n_days, Integer.toString(recurrence.getEveryNDays()));
                break;
            default:
        }
        ;
        Node rangeNode = CUtil.AddNode(recurrenceNode, Consts.XML_range, "");
        SimpleDateFormat dateFormatter = new SimpleDateFormat(Consts.XML_date_pattern);
        CUtil.AddNode(rangeNode, Consts.XML_start_date, dateFormatter.format(recurrence.getStartDate()).toString());
        switch(recurrence.getEnd()) {
            case NO_END:
                break;
            case END_DATE:
                CUtil.AddNode(rangeNode, Consts.XML_end_date, dateFormatter.format(recurrence.getEndDate()).toString());
                break;
            case END_AFTER_N_OCCUR:
                CUtil.AddNode(rangeNode, Consts.XML_end_after_occurences, Integer.toString(recurrence.getEndAfterNOccur()));
                break;
        }
        ;
    }

    /**
	 * Read a JRC from the DOM.
	 * @param sRuleId
	 * @param rsb
	 * @return
	 */
    public boolean JRCRead(ManeToRuneIF m2rIFCallback, JdbcRuleContainer jrc, StringBuffer rsb) {
        boolean bResult = true;
        assert (jrc.GetId().length() > 0);
        Node oNodeRule = m_DOMAllJdbcRules.getRootElement().selectSingleNode("/root/rules[@id='" + jrc.GetId() + "']");
        if (oNodeRule == null) {
            return false;
        }
        if (CUtil.GetNodeAttributeValue(oNodeRule, "", "active").equals("1")) jrc.SetActive(true); else jrc.SetActive(false);
        jrc.SetName(CUtil.GetNodeAttributeValue(oNodeRule, "", "name"));
        jrc.SetDesc(CUtil.GetNodeAttributeValue(oNodeRule, "", "desc"));
        jrc.SetLastSaveTimestamp(_getRuneRuleTimestamp(jrc.GetId()));
        List<?> parameterNodes = oNodeRule.selectNodes("callRef//parameter");
        for (int ir = 0; ir < parameterNodes.size(); ir++) {
            Node parameterNode = (Node) parameterNodes.get(ir);
            String name = CUtil.GetNodeAttributeValue(parameterNode, "", "name");
            String desc = CUtil.GetNodeAttributeValue(parameterNode, "", "desc");
            String type = CUtil.GetNodeAttributeValue(parameterNode, "", "type");
            Node defValueNode = parameterNode.selectSingleNode("const");
            String defValue = "";
            if (defValueNode != null) {
                defValue = defValueNode.getStringValue();
            }
            JdbcParameter jdbcParam = new JdbcParameter(name, desc, type, defValue);
            jrc.getParameters().add(jdbcParam);
        }
        return bResult;
    }

    /**
	 * Return the Event Code ID for a rule
	 * @param sRuleId
	 * @return
	 */
    private String _getEventCodeIdForRule(String sRuleId) {
        Node oNodeRule = m_DOMAllEventRules.getRootElement().selectSingleNode("/root/rules[@id='" + sRuleId + "']");
        if (oNodeRule == null) return "";
        String sXPath = "if/test/and/*/left/getVar[@ref='" + cs_VAR_GV_EVENT_CODE + "']/../../right/const";
        String sEventCodeId = CUtil.GetXml(oNodeRule, sXPath);
        return sEventCodeId;
    }

    private String _getEventCodeDetailOperatorForRule(String sRuleId) {
        Node oNodeRule = m_DOMAllEventRules.getRootElement().selectSingleNode("/root/rules[@id='" + sRuleId + "']");
        if (oNodeRule == null) return "";
        String sXPath = "if/test/and/*/left/getVar[@ref='" + cs_VAR_GV_EVENT_DETAIL_INTEGER + "']/../..";
        Node oNode = oNodeRule.selectSingleNode(sXPath);
        if (oNode != null) {
            return oNode.getName();
        }
        return "";
    }

    private String _getEventCodeDetailValueForRule(String sRuleId) {
        Node oNodeRule = m_DOMAllEventRules.getRootElement().selectSingleNode("/root/rules[@id='" + sRuleId + "']");
        if (oNodeRule == null) return "";
        String sXPath = "if/test/and/*/left/getVar[@ref='" + cs_VAR_GV_EVENT_DETAIL_INTEGER + "']/../..";
        Node oNode = oNodeRule.selectSingleNode(sXPath);
        if (oNode != null) {
            oNode = oNode.selectSingleNode("right/const");
            if (oNode != null) return CUtil.GetXml(oNode, "");
        }
        return "";
    }

    private long _getRuneRuleTimestamp(String sRuleId) {
        for (int i = 0; i < m_lstercTimeStamps.size(); i++) {
            if (m_lstercTimeStamps.get(i).m_sRuneRuleId.equals(sRuleId)) return m_lstercTimeStamps.get(i).m_lLastEventRuleSaveInMillis;
        }
        _ercTimeStamp e = new _ercTimeStamp();
        e.m_sRuneRuleId = sRuleId;
        e.m_lLastEventRuleSaveInMillis = Calendar.getInstance().getTimeInMillis();
        m_lstercTimeStamps.add(e);
        return e.m_lLastEventRuleSaveInMillis;
    }

    /**
	 * Write an ERC to the DOM and to the rule XML file
	 * @param m2rIFCallback
	 * @param erc
	 * @param rsb
	 * @return
	 */
    public boolean ERCWrite(ManeToRuneIF m2rIFCallback, EventRuleContainer erc, StringBuffer rsb) {
        boolean bResult = true;
        String sXPath;
        Node oNode;
        assert (erc.GetId().length() > 0);
        Node oNodeRule = m_DOMAllEventRules.getRootElement().selectSingleNode("/root/rules[@id='" + erc.GetId() + "']");
        if (oNodeRule == null) {
            oNodeRule = _createNewRule(erc.GetId(), erc.GetActive(), erc.GetName());
        }
        if (erc.GetActive()) CUtil.SetNodeAttributeValue(oNodeRule, "", "active", "1"); else CUtil.SetNodeAttributeValue(oNodeRule, "", "active", "0");
        CUtil.SetNodeAttributeValue(oNodeRule, "", "name", erc.GetName());
        CUtil.SetNodeAttributeValue(oNodeRule, "", "desc", erc.GetDesc());
        CUtil.SetNodeAttributeValue(oNodeRule, "", "jdbcRuleID", erc.getJdbcRuleID());
        Recurrence recurrence = erc.getRecurrence();
        WriteRecurrence(oNodeRule, recurrence);
        sXPath = "if/test";
        oNode = oNodeRule.selectSingleNode(sXPath);
        if (oNode != null) CUtil.RemoveAllChildren(oNode);
        sXPath = "if/then";
        oNode = oNodeRule.selectSingleNode(sXPath);
        if (oNode != null) CUtil.RemoveAllChildren(oNode);
        if (erc.GetRuneRulesSize() > 0) {
            Node oLogic = AddNodes(oNodeRule, "if/test/and/exprLogic");
            for (int i = 0; i < erc.GetRuneRulesSize(); i++) {
                RuneRule rr = erc.GetRuneRules().get(i);
                Node oNodeCallref = CUtil.AddNode(oLogic, "callRef", "");
                CUtil.AddAttribute(oNodeCallref, "name", rr.GetLogicName());
                CUtil.AddAttribute(oNodeCallref, "ref", rr.GetId());
                if (erc.GetRuneParameterValues().size() > 0) {
                    Node oNodeParameters = AddNodes(oNodeCallref, "parameters");
                    for (int ipv = 0; ipv < erc.GetRuneParameterValues().size(); ipv++) {
                        RuneParameterValue rpv = erc.GetRuneParameterValues().get(ipv);
                        if (rpv.GetRuleId().equals(rr.GetId())) {
                            Node oNodeParameter = CUtil.AddNode(oNodeParameters, "parameter", "");
                            CUtil.AddAttribute(oNodeParameter, "name", rpv.GetParamId());
                            CUtil.AddNode(oNodeParameter, "const", rpv.GetValue());
                        }
                    }
                }
            }
            CUtil.AddNode(oLogic, "logic", erc.GetCombinationLogic());
        }
        Node oNodeThen = AddNodes(oNodeRule, "if/then");
        AddNodes(oNodeThen, "setVar@id=_processed/const=1");
        if (erc.GetEmailTemplateA() != null) {
            EmailTemplate emtA = m2rIFCallback.GetEmailTemplateByIdNTS(erc.GetEmailTemplateA().GetId());
            assert (emtA != null);
            AddNodes(oNodeThen, "if/test/lessThanOrEqual/left/getRandom");
            AddNodes(oNodeThen, "if/test/lessThanOrEqual/right/const=" + erc.GetEmailTemplateAPercent());
            AddNodes(oNodeThen, "if/then/setVar@id=vTemplateID/const=" + emtA.GetId());
            if (erc.GetEmailTemplateB() != null) {
                EmailTemplate emtB = m2rIFCallback.GetEmailTemplateByIdNTS(erc.GetEmailTemplateB().GetId());
                assert (emtB != null);
                AddNodes(oNodeThen, "if/else/setVar@id=vTemplateID/const=" + emtB.GetId());
            }
        }
        String sXml = "";
        sXml += "<root>";
        sXml += "<setVar id='vEmailDestinationFilename'>";
        sXml += "	<concatenate>";
        sXml += "		<const>events/</const>";
        sXml += "		<getVar ref='GV_CUSTOMER_ID' />";
        sXml += "		<const>/</const>";
        sXml += "		<getTimestamp format='yyyyMMdd_HHmmssSSS' />";
        sXml += "		<const>_</const>";
        sXml += "		<getVar ref='vTemplateID' />";
        sXml += "		<const>.html</const>";
        sXml += "	</concatenate>";
        sXml += "</setVar>";
        sXml += "<callRef ref='_personalization'>";
        sXml += "	<parameters>";
        sXml += "		<parameter name='P_TEMPLATE'>";
        sXml += "			<getVar ref='vTemplateID' />";
        sXml += "		</parameter>";
        sXml += "		<parameter name='P_DEST'>";
        sXml += "			<getVar ref='vEmailDestinationFilename' />";
        sXml += "		</parameter>";
        sXml += "	</parameters>";
        sXml += "</callRef>";
        sXml += "<callRef ref='_sendmail'>";
        sXml += "	<parameters>";
        sXml += "		<parameter name='P_EMAIL_BODY_LOCATION'>";
        sXml += "			<getVar ref='vEmailDestinationFilename' />";
        sXml += "		</parameter>";
        sXml += "		<parameter name='P_EMAIL_SUBJECT_LOCATION'>";
        sXml += "			<concatenate>";
        sXml += "				<getVar ref='vEmailDestinationFilename' />";
        sXml += "				<const>.header</const>";
        sXml += "			</concatenate>";
        sXml += "		</parameter>";
        sXml += "		<parameter name='P_TO'>";
        sXml += "			<getNodeValue>";
        sXml += "				<const>{V_DBPERSONALIZATION_DOM}//root/customer/response/customer/email_address</const>";
        sXml += "			</getNodeValue>";
        sXml += "		</parameter>";
        sXml += "		<parameter name='P_CC'><const /></parameter>";
        sXml += "		<parameter name='P_BCC'><const /></parameter>";
        sXml += "	</parameters>";
        sXml += "</callRef>";
        sXml += "<callRef ref='_writeTracking'>";
        sXml += " <parameters>";
        sXml += "	<parameter name='P_TO'>";
        sXml += "		<getNodeValue>";
        sXml += "			<const>";
        sXml += "				{V_DBPERSONALIZATION_DOM}//root/customer/response/customer/email_address";
        sXml += "			</const>";
        sXml += "		</getNodeValue>";
        sXml += "	</parameter>";
        sXml += "	<parameter name='P_CC'>";
        sXml += "		<const />";
        sXml += "	</parameter>";
        sXml += "	<parameter name='P_BCC'>";
        sXml += "		<const />";
        sXml += "	</parameter>";
        sXml += "	<parameter name='P_TEMPLATE'>";
        sXml += "		<getVar ref='vTemplateID' />";
        sXml += "	</parameter>";
        sXml += "	<parameter name='P_EMAIL_BODY_LOCATION'>";
        sXml += "		<getVar ref='vEmailDestinationFilename' />";
        sXml += "	</parameter>";
        sXml += "	<parameter name='P_CAMPAIGN_ID'>";
        sXml += "		<const>0</const>";
        sXml += "	</parameter>";
        sXml += "	<parameter name='P_OFFER_ID'>";
        sXml += "		<const>0</const>";
        sXml += "	</parameter>";
        sXml += "	<parameter name='P_VEHICLE_ID'>";
        sXml += "		<const>0</const>";
        sXml += "	</parameter>";
        sXml += "	<parameter name='P_CHANNEL_ID'>";
        sXml += "		<const>0</const>";
        sXml += "	</parameter>";
        sXml += "	<parameter name='P_KEYWORDS'>";
        sXml += "		<const></const>";
        sXml += "	</parameter>";
        sXml += " </parameters>";
        sXml += "</callRef>";
        sXml += "</root>";
        Document oDomStatic = null;
        try {
            oDomStatic = DocumentHelper.parseText(sXml);
        } catch (DocumentException e) {
            throw new RTAssertException("DOM Error", e.getMessage());
        }
        CUtil.CopyAllChildNodes(oDomStatic.getRootElement().selectSingleNode("/root"), oNodeThen);
        String sTrXpath = "callRef[@ref='_writeTracking']/parameters/parameter[@name='";
        CUtil.SetXml(oNodeThen, sTrXpath + "P_CAMPAIGN_ID']/const", new Integer(erc.GetCampaignId()).toString());
        CUtil.SetXml(oNodeThen, sTrXpath + "P_OFFER_ID']/const", new Integer(erc.GetOfferId()).toString());
        CUtil.SetXml(oNodeThen, sTrXpath + "P_VEHICLE_ID']/const", new Integer(erc.GetVehicleId()).toString());
        CUtil.SetXml(oNodeThen, sTrXpath + "P_CHANNEL_ID']/const", new Integer(erc.GetChannelId()).toString());
        CUtil.SetXml(oNodeThen, sTrXpath + "P_KEYWORDS']/const", erc.GetKeywords());
        Document oDOM4Save = null;
        try {
            oDOM4Save = DocumentHelper.parseText("<root />");
        } catch (DocumentException e) {
        }
        CUtil.CopyThisAndAllChildNodes(oNodeRule, oDOM4Save.getRootElement().selectSingleNode("/root"));
        try {
            String sFilename = m_sPathToEventRules + File.separator + erc.GetId() + ".xml";
            CUtil.WriteXml(oDOM4Save, sFilename);
        } catch (IOException e) {
            rsb.append("An error occurred on the server while saving the Event Rule Tree in " + m_sPAndFNEventRuleTree + ". Error: " + e.toString());
            return false;
        }
        return bResult;
    }

    public Node AddNodes(Node oNodeParent, String sXPath) {
        return AddNodes(false, oNodeParent, sXPath);
    }

    public Node AddNodes(boolean bAlwaysCreateNodes, Node oNodeParent, String sXPath) {
        if (oNodeParent == null) return null;
        StringTokenizer st = new StringTokenizer(sXPath, "/");
        Node oNode = oNodeParent;
        while (st.hasMoreElements()) {
            String sTag = st.nextToken();
            if (sTag.length() > 0) {
                String sAttribute = "";
                String sAttributeValue = "";
                String sNodeValue = "";
                StringTokenizer stAttr = new StringTokenizer(sTag, "@");
                if (stAttr.countTokens() == 2) {
                    sTag = stAttr.nextToken();
                    sAttribute = stAttr.nextToken();
                    StringTokenizer stAttrVal = new StringTokenizer(sAttribute, "=");
                    if (stAttrVal.countTokens() == 2) {
                        sAttribute = stAttrVal.nextToken();
                        sAttributeValue = stAttrVal.nextToken();
                    }
                } else {
                    StringTokenizer stNodeVal = new StringTokenizer(sTag, "=");
                    if (stNodeVal.countTokens() == 2) {
                        sTag = stNodeVal.nextToken();
                        sNodeValue = stNodeVal.nextToken();
                    }
                }
                if (bAlwaysCreateNodes || oNode.selectSingleNode(sTag) == null) oNode = CUtil.AddNode(oNode, sTag, sNodeValue); else oNode = oNode.selectSingleNode(sTag);
                if (sAttribute.length() > 0) CUtil.AddAttribute(oNode, sAttribute, sAttributeValue);
            }
        }
        return oNode;
    }

    /**
	 * Execute a rule in test mode.
	 * Synchronous access blocked by the caller
	 * @param terc
	 * @param rsb
	 * @return
	 */
    public boolean RunATestExecuteForRule(ManeToRuneIF m2rIFCallback, EventRuleTExecContainer terc, StringBuffer rsb, StringBuffer rsbXmlExecuted) {
        String sUID = UUID.randomUUID().toString();
        String sTestRuleId = "__MAINTESTEXEC_" + sUID;
        boolean bResult = false;
        Document oDOM = null;
        try {
            oDOM = DocumentHelper.parseText("<root><rules id='" + sTestRuleId + "' /> </root>");
        } catch (DocumentException e) {
        }
        Node oNodeRule = oDOM.getRootElement().selectSingleNode("/root/rules");
        String sXml = "";
        sXml += "<root>";
        sXml += "<setVar id='_DEBUG'><const>1</const></setVar>";
        String sFinalEmailsDestinationPath = m_sFinalEmailsDestinationPath;
        if (!sFinalEmailsDestinationPath.endsWith(File.separator)) sFinalEmailsDestinationPath += File.separator;
        sXml += "<setVar id='_pathToCustomerEmails'>";
        sXml += "	<const>" + sFinalEmailsDestinationPath + "</const>";
        sXml += "</setVar>";
        sXml += "<setVar id='GV_CUSTOMER_ID'><const>1</const></setVar>";
        sXml += "<setVar id='GV_STUDENT_ID'><const>1</const></setVar>";
        sXml += "<setVar id='GV_EVENT_CODE'><const>100</const></setVar>";
        sXml += "<setVar id='GV_EVENT_DETAIL_INTEGER'><const>1</const></setVar>";
        sXml += "<callRef ref='_performCallback' />";
        sXml += "<setVar id='_processed'><const>0</const></setVar>";
        sXml += "<callRef ref='" + terc.GetId() + "' />";
        sXml += "</root>";
        Document oDomStatic = null;
        try {
            oDomStatic = DocumentHelper.parseText(sXml);
        } catch (DocumentException e) {
            throw new RTAssertException("Static XML parse error", e.getMessage());
        }
        CUtil.CopyAllChildNodes(oDomStatic.getRootElement().selectSingleNode("/root"), oNodeRule);
        CUtil.SetXml(oNodeRule, "setVar[@id='GV_CUSTOMER_ID']/const", terc.m_sCustomerId);
        CUtil.SetXml(oNodeRule, "setVar[@id='GV_STUDENT_ID']/const", terc.m_sUserId);
        Node oNodeRefRule = m_DOMAllEventRules.getRootElement().selectSingleNode("/root/rules[@id='" + terc.GetId() + "']");
        CUtil.CopyThisAndAllChildNodes(oNodeRefRule, oDOM.getRootElement().selectSingleNode("/root"));
        CUtil.CopyAllChildNodes(m2rIFCallback.GetDOMAllEventConditions().getRootElement().selectSingleNode("/root"), oDOM.getRootElement().selectSingleNode("/root"));
        StartRune sr = new StartRune();
        ArrayList<RuneError> lstRuneErrors = sr.Execute("", oDOM, sTestRuleId);
        if (lstRuneErrors.size() > 0) {
            for (int i = 0; i < lstRuneErrors.size(); i++) {
                rsb.append("<p>" + lstRuneErrors.get(i).toString());
                bResult = false;
            }
        } else {
            bResult = true;
        }
        m2rIFCallback.LogTestExecutionStart(sTestRuleId, oDOM, rsb);
        StringWriter out = new StringWriter();
        OutputFormat format = OutputFormat.createPrettyPrint();
        XMLWriter writer = new XMLWriter(out, format);
        try {
            writer.write(oDOM);
            writer.close();
            rsbXmlExecuted.append(out.toString());
        } catch (IOException e) {
            rsbXmlExecuted.append(oDOM.asXML());
        }
        return bResult;
    }

    /**
	 * Execute a rule in test mode.
	 * Synchronous access blocked by the caller
	 * @param terc
	 * @param rsb
	 * @return
	 */
    public boolean RunATestExecuteForJdbcRule(ManeToRuneIF m2rIFCallback, EventRuleTExecContainer terc, StringBuffer rsb, StringBuffer rsbXmlExecuted) {
        boolean result = false;
        Document domResult = null;
        try {
            domResult = DocumentHelper.parseText("<root/>");
        } catch (DocumentException de) {
            fail("stupid");
        }
        List<JdbcParameter> params = (ArrayList<JdbcParameter>) terc.m_customData;
        StartRune sr = new StartRune();
        String sXmlData = "";
        if (params.size() != 0) {
            updateCallRefValues(params, terc.GetId());
        }
        ArrayList<RuneError> lstRuneErrors = sr.Execute(sXmlData, m_DOMAllJdbcRules, terc.GetId(), domResult);
        if (lstRuneErrors.size() > 0) {
            for (int i = 0; i < lstRuneErrors.size(); i++) {
                rsb.append("<p>" + lstRuneErrors.get(i).toString());
                result = false;
            }
        } else {
            result = true;
        }
        StringWriter out = new StringWriter();
        OutputFormat format = OutputFormat.createPrettyPrint();
        XMLWriter writer = new XMLWriter(out, format);
        try {
            writer.write(domResult);
            writer.close();
            rsbXmlExecuted.append(out.toString());
        } catch (IOException e) {
            rsbXmlExecuted.append(domResult.asXML());
        }
        return result;
    }

    private void updateCallRefValues(List<JdbcParameter> params, String ruleId) {
        Node oNodeRule = m_DOMAllJdbcRules.getRootElement().selectSingleNode("/root/rules[@id='" + ruleId + "']");
        Node callRefNode = oNodeRule.selectSingleNode("callRef");
        if (callRefNode == null) {
            fail("callRef not found");
        }
        Node parametersNode = callRefNode.selectSingleNode("parameters");
        if (parametersNode == null) {
            fail("parameters not found");
        }
        for (int i = 0; i < params.size(); i++) {
            Node parameterNode = CUtil.GetAttributedNode(parametersNode, "parameter", "name", params.get(i).GetName());
            if (parameterNode == null) {
                parameterNode = CUtil.AddNode(parametersNode, "parameter", "");
                CUtil.AddAttribute(parameterNode, "name", params.get(i).GetName());
            }
            Node constNode = parameterNode.selectSingleNode("const");
            if (constNode == null) {
                CUtil.AddNode(parameterNode, "const", "");
            }
            constNode.setText(params.get(i).GetValue());
        }
    }

    /**
	 * 
	 * @param lstIn		String Array for the input - 7 elements needed. Details see below.
	 * @param lstOut		Result string array.
	 * @param rsb		Error messages.
	 * @return
	 */
    public boolean RunATestPersonalization(ManeToRuneIF m2rIFCallback, ArrayList<String> lstIn, ArrayList<String> lstOut, StringBuffer rsb) {
        boolean bResult = false;
        String sXml = "";
        lstOut.clear();
        sXml += "<root>";
        sXml += "	<runPersonalization />";
        sXml += "	<customerId />";
        sXml += "	<studentId />";
        sXml += "	<subject />";
        sXml += "	<body />";
        sXml += "	<destinationPath />";
        sXml += "	<destinationFilename />";
        sXml += "	<sendEmail>0</sendEmail>";
        sXml += "	<emailAddress />";
        sXml += "</root>";
        if (lstIn.size() != 7) {
            rsb.append("INPUT parameters missing: should be 7 but is " + lstIn.size() + "!");
            return false;
        }
        Document oDomData = null;
        try {
            oDomData = DocumentHelper.parseText(sXml);
        } catch (DocumentException e) {
            throw new RTAssertException("Internal data XML parse error", e.getMessage());
        }
        String sCustomerId = lstIn.get(0);
        String sStudentId = lstIn.get(1);
        String sEmailAddress = lstIn.get(2);
        String sSubject = lstIn.get(3);
        String sBody = lstIn.get(4);
        String sSendEmail = lstIn.get(5);
        String sRunPersonalization = lstIn.get(6);
        CUtil.SetXml(oDomData.getRootElement(), "/root/customerId", sCustomerId);
        CUtil.SetXml(oDomData.getRootElement(), "/root/studentId", sStudentId);
        CUtil.SetXml(oDomData.getRootElement(), "/root/subject", sSubject);
        CUtil.SetXml(oDomData.getRootElement(), "/root/body", sBody);
        CUtil.SetXml(oDomData.getRootElement(), "/root/emailAddress", sEmailAddress);
        CUtil.SetXml(oDomData.getRootElement(), "/root/sendEmail", sSendEmail);
        CUtil.SetXml(oDomData.getRootElement(), "/root/runPersonalization", sRunPersonalization);
        String sFinalEmailsDestinationFilename = "";
        String sFinalEmailsDestinationPath = m_sFinalEmailsDestinationPath;
        if (!sFinalEmailsDestinationPath.endsWith(File.separator)) sFinalEmailsDestinationPath += File.separator;
        CUtil.SetXml(oDomData.getRootElement(), "/root/destinationPath", sFinalEmailsDestinationPath);
        String s = UUID.randomUUID().toString();
        s = s.replace("-", "_");
        s = s.replace(" ", "_");
        sFinalEmailsDestinationFilename = "tests" + File.separator + s;
        CUtil.SetXml(oDomData.getRootElement(), "/root/destinationFilename", sFinalEmailsDestinationFilename);
        Document domResult = null;
        try {
            domResult = DocumentHelper.parseText("<root/>");
        } catch (DocumentException de) {
        }
        StartRune sr = new StartRune();
        ArrayList<RuneError> lstRuneErrors = sr.Execute(oDomData.asXML(), m2rIFCallback.GetDOMAllEventConditions(), "_runTestFromUIForEmailTemplate", domResult);
        if (lstRuneErrors.size() > 0) {
            for (int i = 0; i < lstRuneErrors.size(); i++) {
                rsb.append("<p>" + lstRuneErrors.get(i).toString());
                bResult = false;
            }
        } else {
            bResult = true;
        }
        String sBodyFN = "";
        String sHeaderFN = "";
        if (bResult) {
            String sResultSubject = "";
            String sResultBody = "";
            sBodyFN = sFinalEmailsDestinationPath + sFinalEmailsDestinationFilename;
            sHeaderFN = sBodyFN + ".header";
            if (new File(sHeaderFN).exists()) {
                Reader is;
                try {
                    long lLen = new File(sHeaderFN).length();
                    is = new FileReader(sHeaderFN);
                    sResultSubject = _readerToString(is, lLen);
                    is.close();
                } catch (IOException e) {
                    rsb.append("<p>" + "Could not read generated email file: " + sHeaderFN + ": " + e.getMessage());
                }
            }
            if (new File(sBodyFN).exists()) {
                Reader is;
                try {
                    long lLen = new File(sBodyFN).length();
                    is = new FileReader(sBodyFN);
                    sResultBody = _readerToString(is, lLen);
                    is.close();
                } catch (IOException e) {
                    rsb.append("<p>" + "Could not read generated email file: " + sBodyFN + ": " + e.getMessage());
                }
            }
            lstOut.add(sResultSubject);
            lstOut.add(sResultBody);
            if (domResult != null && domResult.getRootElement() != null) {
                StringWriter out = new StringWriter();
                OutputFormat format = OutputFormat.createPrettyPrint();
                XMLWriter writer = new XMLWriter(out, format);
                try {
                    writer.write(domResult);
                    writer.close();
                    lstOut.add(out.toString());
                } catch (IOException e) {
                    lstOut.add("");
                }
            } else lstOut.add("");
        }
        if (sBodyFN.length() > 0 && new File(sBodyFN).exists()) {
            try {
                new File(sBodyFN).delete();
            } catch (Exception e) {
            }
        }
        if (sHeaderFN.length() > 0 && new File(sHeaderFN).exists()) {
            try {
                new File(sHeaderFN).delete();
            } catch (Exception e) {
            }
        }
        return bResult;
    }

    private String _readerToString(Reader is, long lFileSize) throws IOException {
        StringBuffer sb = new StringBuffer();
        char[] b = new char[(int) lFileSize + 1];
        int n;
        while ((n = is.read(b)) > 0) {
            sb.append(b, 0, n);
        }
        return sb.toString();
    }

    /**
	 * Read an email template
	 * @param m2rIFCallback
	 * @param emtad
	 * @param rsb
	 * @return
	 */
    public boolean EMTRead(ManeToRuneIF m2rIFCallback, EmailTemplateAllData emtad, StringBuffer rsb) {
        if (emtad.GetId().length() == 0) throw new RTAssertException("EMTRead: not possible without email template ID", "");
        EmailTemplate2 emt2 = DBOperations.getEmailTemplateByID(emtad.GetId(), rsb);
        if (emt2 == null) return false;
        emtad.SetName(emt2.getTName());
        emtad.SetDesc(emt2.getDescription());
        emtad.SetFName(emt2.getFName());
        emtad.SetType(emt2.getType());
        emtad.SetActive(emt2.getActive().intValue() == 1 ? true : false);
        emtad.SetSubject(emt2.getSubject());
        emtad.SetBody(emt2.getBody());
        return true;
    }

    /**
	 * Save the subject and the body
	 * @param sPath
	 * @return
	 */
    public boolean SaveEmailTemplateFiles(String sPath, String sBodyFilename, StringBuffer rsb, String sSubject, String sBody) {
        if (sBodyFilename.length() == 0) {
            throw new RTAssertException("SaveEmailTemplateFiles - Unexpected: sBodyFilename empty", "");
        }
        String sHeaderFile = sPath;
        if (!sHeaderFile.endsWith(File.separator)) sHeaderFile += File.separator;
        String sBodyFile = sHeaderFile;
        sBodyFile += sBodyFilename;
        sHeaderFile += sBodyFilename + ".header";
        try {
            FileWriter out = new FileWriter(sBodyFile, false);
            out.write(sBody);
            out.close();
        } catch (IOException e) {
            String s = "Error writing Body to " + sBodyFile + ". Error: " + e.getMessage();
            rsb.append(s);
            return false;
        }
        try {
            FileWriter out = new FileWriter(sHeaderFile, false);
            out.write(sSubject);
            out.close();
        } catch (IOException e) {
            String s = "Error writing Subject to " + sHeaderFile + ". Error: " + e.getMessage();
            rsb.append(s);
            return false;
        }
        return true;
    }

    /**
	 * Write an email template
	 * @param m2rIFCallback
	 * @param emtad
	 * @param rsb
	 * @return
	 */
    public boolean EMTWrite(ManeToRuneIF m2rIFCallback, EmailTemplateAllData emtad, StringBuffer rsb) {
        String sFName = emtad.GetFName();
        int i = 1;
        while (DBOperations.isEmailTemplateFilenameUsed(sFName, emtad.GetId(), rsb)) {
            sFName += i++;
        }
        emtad.SetFName(sFName);
        EmailTemplate2 emt2 = new EmailTemplate2();
        emt2.setID(emtad.GetId());
        emt2.setTName(emtad.GetName());
        emt2.setFName(emtad.GetFName());
        emt2.setDescription(emtad.GetDesc());
        emt2.setType(emtad.GetType());
        emt2.setActive((emtad.IsActive() ? new Long(1) : new Long(0)));
        emt2.setSubject(emtad.GetSubject());
        emt2.setBody(emtad.GetBody());
        return DBOperations.saveEmailTemplate(emt2, rsb);
    }
}
