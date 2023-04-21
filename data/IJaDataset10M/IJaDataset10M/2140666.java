package org.vegbank.nvcrs.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.ModuleException;
import org.apache.struts.util.MessageResources;
import org.apache.commons.beanutils.PropertyUtils;
import java.sql.*;
import javax.sql.*;
import org.vegbank.nvcrs.util.*;

public class type_Form extends SuperForm {

    private String TYPE_ID;

    private String actionType;

    private String userTypeCode;

    private String VB_AccessionCode;

    private String sausageAccessionCode;

    private String level;

    private String primaryName;

    private String confidence;

    private String typeSummary;

    private String classifyComments;

    private String GRank;

    private String GRankDate;

    private String GRankReasons;

    private String wetlandIndicator;

    private String environmentSummary;

    private String vegtationSummary;

    private String succession;

    private String rational;

    private String additionalNotes;

    private String PROPOSAL_ID;

    private String classname;

    private String formationname;

    private String alliance;

    private String floristicssummary;

    public type_Form() {
        super();
        TYPE_ID = "-1";
        actionType = "";
        userTypeCode = "";
        VB_AccessionCode = "";
        sausageAccessionCode = "";
        level = "";
        primaryName = "";
        confidence = "";
        typeSummary = "";
        classifyComments = "";
        GRank = "";
        GRankDate = "";
        GRankReasons = "";
        wetlandIndicator = "";
        environmentSummary = "";
        vegtationSummary = "";
        succession = "";
        rational = "";
        additionalNotes = "";
        classname = "";
        formationname = "";
        alliance = "";
        floristicssummary = "";
        PROPOSAL_ID = "-1";
        updateFields();
    }

    public type_Form(String TYPE_ID, String actionType, String userTypeCode, String VB_AccessionCode, String sausageAccessionCode, String level, String primaryName, String confidence, String typeSummary, String classifyComments, String GRank, String GRankDate, String GRankReasons, String wetlandIndicator, String environmentSummary, String vegtationSummary, String succession, String rational, String additionalNotes, String classname, String formationname, String alliance, String floristicssummary, String PROPOSAL_ID) {
        this.TYPE_ID = TYPE_ID;
        this.actionType = actionType;
        this.userTypeCode = userTypeCode;
        this.VB_AccessionCode = VB_AccessionCode;
        this.sausageAccessionCode = sausageAccessionCode;
        this.level = level;
        this.primaryName = primaryName;
        this.confidence = confidence;
        this.typeSummary = typeSummary;
        this.classifyComments = classifyComments;
        this.GRank = GRank;
        this.GRankDate = GRankDate;
        this.GRankReasons = GRankReasons;
        this.wetlandIndicator = wetlandIndicator;
        this.environmentSummary = environmentSummary;
        this.vegtationSummary = vegtationSummary;
        this.succession = succession;
        this.rational = rational;
        this.additionalNotes = additionalNotes;
        this.classname = classname;
        this.formationname = formationname;
        this.alliance = alliance;
        this.floristicssummary = floristicssummary;
        this.PROPOSAL_ID = PROPOSAL_ID;
        updateFields();
    }

    public String getTYPE_ID() {
        return this.TYPE_ID;
    }

    public String getClassname() {
        return this.classname;
    }

    public String getAlliance() {
        return this.alliance;
    }

    public String getFormationname() {
        return this.formationname;
    }

    public String getFloristicssummary() {
        return this.floristicssummary;
    }

    public String getActionType() {
        return this.actionType;
    }

    public String getUserTypeCode() {
        return this.userTypeCode;
    }

    public String getVB_AccessionCode() {
        return this.VB_AccessionCode;
    }

    public String getSausageAccessionCode() {
        return this.sausageAccessionCode;
    }

    public String getLevel() {
        return this.level;
    }

    public String getPrimaryName() {
        return this.primaryName;
    }

    public String getConfidence() {
        return this.confidence;
    }

    public String getTypeSummary() {
        return this.typeSummary;
    }

    public String getClassifyComments() {
        return this.classifyComments;
    }

    public String getGRank() {
        return this.GRank;
    }

    public String getGRankDate() {
        return this.GRankDate;
    }

    public String getGRankReasons() {
        return this.GRankReasons;
    }

    public String getWetlandIndicator() {
        return this.wetlandIndicator;
    }

    public String getEnvironmentSummary() {
        return this.environmentSummary;
    }

    public String getVegtationSummary() {
        return this.vegtationSummary;
    }

    public String getSuccession() {
        return this.succession;
    }

    public String getRational() {
        return this.rational;
    }

    public String getAdditionalNotes() {
        return this.additionalNotes;
    }

    public String getPROPOSAL_ID() {
        return this.PROPOSAL_ID;
    }

    public void setTYPE_ID(String TYPE_ID) {
        this.TYPE_ID = TYPE_ID;
    }

    public void setClassname(String classname) {
        this.classname = classname;
    }

    public void setAlliance(String alliance) {
        this.alliance = alliance;
    }

    public void setFormationname(String formationname) {
        this.formationname = formationname;
    }

    public void setFloristicssummary(String floristicssummary) {
        this.floristicssummary = floristicssummary;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public void setUserTypeCode(String userTypeCode) {
        this.userTypeCode = userTypeCode;
    }

    public void setVB_AccessionCode(String VB_AccessionCode) {
        this.VB_AccessionCode = VB_AccessionCode;
    }

    public void setSausageAccessionCode(String sausageAccessionCode) {
        this.sausageAccessionCode = sausageAccessionCode;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public void setPrimaryName(String primaryName) {
        this.primaryName = primaryName;
    }

    public void setConfidence(String confidence) {
        this.confidence = confidence;
    }

    public void setTypeSummary(String typeSummary) {
        this.typeSummary = typeSummary;
    }

    public void setClassifyComments(String classifyComments) {
        this.classifyComments = classifyComments;
    }

    public void setGRank(String GRank) {
        this.GRank = GRank;
    }

    public void setGRankDate(String GRankDate) {
        this.GRankDate = GRankDate;
    }

    public void setGRankReasons(String GRankReasons) {
        this.GRankReasons = GRankReasons;
    }

    public void setWetlandIndicator(String wetlandIndicator) {
        this.wetlandIndicator = wetlandIndicator;
    }

    public void setEnvironmentSummary(String environmentSummary) {
        this.environmentSummary = environmentSummary;
    }

    public void setVegtationSummary(String vegtationSummary) {
        this.vegtationSummary = vegtationSummary;
    }

    public void setSuccession(String succession) {
        this.succession = succession;
    }

    public void setRational(String rational) {
        this.rational = rational;
    }

    public void setAdditionalNotes(String additionalNotes) {
        this.additionalNotes = additionalNotes;
    }

    public void setPROPOSAL_ID(String PROPOSAL_ID) {
        this.PROPOSAL_ID = PROPOSAL_ID;
    }

    public void updateFields() {
        if (fields.size() < 1) {
            addField("actionType");
            addField("userTypeCode");
            addField("VB_AccessionCode");
            addField("sausageAccessionCode");
            addField("level");
            addField("primaryName");
            addField("confidence");
            addField("typeSummary");
            addField("classifyComments");
            addField("GRank");
            addField("GRankDate");
            addField("GRankReasons");
            addField("wetlandIndicator");
            addField("environmentSummary");
            addField("vegtationSummary");
            addField("succession");
            addField("rational");
            addField("additionalNotes");
            addField("classname");
            addField("formationname");
            addField("alliance");
            addField("floristicssummary");
            addField("PROPOSAL_ID");
            setTableName("type");
            setPrimaryKey("TYPE_ID");
        }
    }

    public String getFieldValue(String fldName) throws Exception {
        if (fldName == null) throw new Exception("Null field name.");
        if (fldName.equals("TYPE_ID")) return TYPE_ID;
        if (fldName.equals("actionType")) return actionType;
        if (fldName.equals("userTypeCode")) return userTypeCode;
        if (fldName.equals("VB_AccessionCode")) return VB_AccessionCode;
        if (fldName.equals("sausageAccessionCode")) return sausageAccessionCode;
        if (fldName.equals("level")) return level;
        if (fldName.equals("primaryName")) return primaryName;
        if (fldName.equals("confidence")) return confidence;
        if (fldName.equals("typeSummary")) return typeSummary;
        if (fldName.equals("classifyComments")) return classifyComments;
        if (fldName.equals("GRank")) return GRank;
        if (fldName.equals("GRankDate")) return GRankDate;
        if (fldName.equals("GRankReasons")) return GRankReasons;
        if (fldName.equals("wetlandIndicator")) return wetlandIndicator;
        if (fldName.equals("environmentSummary")) return environmentSummary;
        if (fldName.equals("vegtationSummary")) return vegtationSummary;
        if (fldName.equals("succession")) return succession;
        if (fldName.equals("rational")) return rational;
        if (fldName.equals("additionalNotes")) return additionalNotes;
        if (fldName.equals("classname")) return classname;
        if (fldName.equals("alliance")) return alliance;
        if (fldName.equals("formationname")) return formationname;
        if (fldName.equals("floristicssummary")) return floristicssummary;
        if (fldName.equals("PROPOSAL_ID")) return PROPOSAL_ID;
        throw new Exception("Field not found: " + fldName);
    }

    public void setFieldValue(String fldName, String value) throws Exception {
        if (fldName == null) throw new Exception("Null field name.");
        if (fldName.equals("TYPE_ID")) TYPE_ID = value; else if (fldName.equals("actionType")) actionType = value; else if (fldName.equals("userTypeCode")) userTypeCode = value; else if (fldName.equals("VB_AccessionCode")) VB_AccessionCode = value; else if (fldName.equals("sausageAccessionCode")) sausageAccessionCode = value; else if (fldName.equals("level")) level = value; else if (fldName.equals("primaryName")) primaryName = value; else if (fldName.equals("confidence")) confidence = value; else if (fldName.equals("typeSummary")) typeSummary = value; else if (fldName.equals("classifyComments")) classifyComments = value; else if (fldName.equals("GRank")) GRank = value; else if (fldName.equals("GRankDate")) GRankDate = value; else if (fldName.equals("GRankReasons")) GRankReasons = value; else if (fldName.equals("wetlandIndicator")) wetlandIndicator = value; else if (fldName.equals("environmentSummary")) environmentSummary = value; else if (fldName.equals("vegtationSummary")) vegtationSummary = value; else if (fldName.equals("succession")) succession = value; else if (fldName.equals("rational")) rational = value; else if (fldName.equals("additionalNotes")) additionalNotes = value; else if (fldName.equals("classname")) classname = value; else if (fldName.equals("alliance")) alliance = value; else if (fldName.equals("formationname")) formationname = value; else if (fldName.equals("floristicssummary")) floristicssummary = value; else if (fldName.equals("PROPOSAL_ID")) PROPOSAL_ID = value; else throw new Exception("Field not found: " + fldName);
    }

    public void deleteAllDistributions() throws Exception {
        Connection con = null;
        try {
            con = database.getConnection();
            String deleteStatement = "delete from distribution where TYPE_ID=" + getTYPE_ID();
            PreparedStatement prepStmt = con.prepareStatement(deleteStatement);
            prepStmt.executeUpdate();
            prepStmt.close();
            con.close();
        } catch (Exception e) {
            if (con != null && !con.isClosed()) con.close();
            throw e;
        }
    }
}
