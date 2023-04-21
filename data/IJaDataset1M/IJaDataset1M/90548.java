package org.genos.gmf.resources.mgmt;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import org.genos.gmf.Configuration;
import org.genos.gmf.RequestParameters;
import org.genos.gmf.ResPublisher;
import org.genos.gmf.ResourceData;
import org.genos.gmf.Core.Core;
import org.genos.gmf.Core.PatternString;
import org.genos.gmf.Core.Patterns;
import org.genos.gmf.form.Form;
import org.genos.gmf.form.FormParameter;
import org.genos.gmf.form.ParameterSourceSQL;
import org.genos.gmf.form.ParameterType;
import org.genos.gmf.form.ParameterTypeSelect;
import org.genos.gmf.form.ParameterTypeSelectExt;
import org.genos.gmf.form.StaticDefaultValue;
import org.genos.gmf.form.SubForm;
import org.genos.gmf.resources.Resource;
import org.genos.gmf.resources.WorkArea;
import org.genos.gmf.resources.formatters.RFormatter;
import org.genos.gmf.resources.formatters.VParam;
import org.genos.gmf.resources.formatters.XSLTFormatter;
import org.genos.gmf.resources.formatters.VParamSource.VPARAMSOURCE;
import org.genos.gmf.resources.generic.ldap.LDAPUsers;
import org.genos.gmf.resources.itil.support.TimeReport;
import org.genos.gmf.security.GroupMgmt;
import org.genos.gmf.security.UserMapping;
import org.genos.gmf.workflow.Workflow;
import org.genos.utils.StringUtils;

public class WO extends Resource implements Workflow {

    private Integer trcont = null;

    private String workflow = null;

    private String otManagers = null;

    private String otTech = null;

    private String otUsers = null;

    private Boolean isManager = null;

    /**
     * Specific ParameterTypeSelect class for assignedto field
     */
    private class PTSTech extends ParameterTypeSelectExt {

        /**
         * Populated flag.
         */
        private boolean populated = false;

        /**
         * Constructor.
         * @throws Exception
         */
        public PTSTech() throws Exception {
            super();
        }

        /**
         * Populate select field from option sources.
         */
        protected void populateSourceOptions(Connection conn) throws SQLException, Exception {
            if (populated) return;
            String dn;
            String caption;
            ArrayList e = GroupMgmt.getUsersInGroup(conn, null, getTechnicians(conn), null);
            for (Iterator it = e.iterator(); it.hasNext(); ) {
                dn = (String) it.next();
                caption = LDAPUsers.sLookupUserDN(conn, uid, dn, Configuration.ldapuserdisplayattr);
                addSelectOption(dn, caption);
            }
            populated = true;
        }
    }

    /**
     * Default constructor
     */
    public WO() throws Exception {
        super();
        resForm = new Form();
        ParameterTypeSelect pts = new ParameterTypeSelect();
        pts.setMaxLength(8);
        pts.setFlags(ParameterType.PARAM_OBLIGATORY | ParameterType.PARAM_WITHDEFAULTVALUE | ParameterType.PARAM_DONTASK_ONCREATE);
        pts.addSelectOption("pendplan", "$lang:s_res_wocont_pendplan$");
        pts.addSelectOption("pendauth", "$lang:s_res_wocont_pendauth$");
        pts.addSelectOption("auth", "$lang:s_res_wocont_auth$");
        pts.addSelectOption("done", "$lang:s_res_wocont_done$");
        pts.addSelectOption("rej", "$lang:s_res_wocont_rejected$");
        pts.setDefaultValue(new StaticDefaultValue("pendplan"));
        FormParameter fp = new FormParameter(resForm, "state", new PatternString(this, "$lang:s_res_wo_state$"), null, pts, new ParameterSourceSQL(null, "state"));
        resForm.addComponent(fp);
        pts.addDependencies("executiondate");
        pts.addDependencies("executedby");
        pts.addDependencies("hours");
        pts.addDependencies("planneddate");
        pts.addDependencies("assignedto");
        ParameterType pt = new ParameterType(ParameterType.PT_TEXT, ParameterType.PARAM_OBLIGATORY | ParameterType.PARAM_READONLY_ONEDIT, 128);
        resForm.addComponent(new FormParameter(resForm, "title", new PatternString(this, "$lang:s_res_wo_title$"), null, pt, new ParameterSourceSQL(null, "title")));
        pt = new ParameterType(ParameterType.PT_TEXTAREA, ParameterType.PARAM_OPTIONAL | ParameterType.PARAM_READONLY_ONEDIT, 0);
        resForm.addComponent(new FormParameter(resForm, "description", new PatternString(this, "$lang:s_res_wo_description$"), null, pt, new ParameterSourceSQL(null, "description")));
        pt = new ParameterType(ParameterType.PT_TIMESTAMP, ParameterType.PARAM_OPTIONAL | ParameterType.PARAM_READONLY | ParameterType.PARAM_DONTASK, 0);
        resForm.addComponent(new FormParameter(resForm, "creationdate", new PatternString(this, "$lang:s_res_wo_creationdate$"), null, pt, new ParameterSourceSQL(null, "creationdate")));
        pt = new ParameterType(ParameterType.PT_TEXT, ParameterType.PARAM_OPTIONAL | ParameterType.PARAM_READONLY | ParameterType.PARAM_DONTASK, 128);
        resForm.addComponent(new FormParameter(resForm, "createdby", new PatternString(this, "$lang:s_res_wo_createdby$"), null, pt, new ParameterSourceSQL(null, "createdby")));
        pt = new ParameterType(ParameterType.PT_DATE, ParameterType.PARAM_OPTIONAL | ParameterType.PARAM_READONLY_ONEDIT | ParameterType.PARAM_DONTASK_ONEDIT, 0);
        resForm.addComponent(new FormParameter(resForm, "planneddate", new PatternString(this, "$lang:s_res_wo_planneddate$"), null, pt, new ParameterSourceSQL(null, "planneddate")));
        pt = new ParameterType(ParameterType.PT_TEXT, ParameterType.PARAM_OPTIONAL | ParameterType.PARAM_READONLY | ParameterType.PARAM_DONTASK, 128);
        resForm.addComponent(new FormParameter(resForm, "plannedby", new PatternString(this, "$lang:s_res_wo_plannedby$"), null, pt, new ParameterSourceSQL(null, "plannedby")));
        PTSTech pttech = new PTSTech();
        pttech.setFlags(ParameterType.PARAM_OPTIONAL | ParameterType.PARAM_READONLY_ONEDIT);
        pttech.setMaxLength(128);
        resForm.addParameter("assignedto", new PatternString(this, "$lang:s_res_wo_assignedto$"), null, pttech, new ParameterSourceSQL(null, "assignedto"));
        pt = new ParameterType(ParameterType.PT_DATE, ParameterType.PARAM_OPTIONAL | ParameterType.PARAM_READONLY | ParameterType.PARAM_DONTASK, 0);
        resForm.addComponent(new FormParameter(resForm, "authdate", new PatternString(this, "$lang:s_res_wo_authdate$"), null, pt, new ParameterSourceSQL(null, "authdate")));
        pt = new ParameterType(ParameterType.PT_TEXT, ParameterType.PARAM_OPTIONAL | ParameterType.PARAM_READONLY | ParameterType.PARAM_DONTASK, 128);
        resForm.addComponent(new FormParameter(resForm, "authby", new PatternString(this, "$lang:s_res_wo_authby$"), null, pt, new ParameterSourceSQL(null, "authby")));
        pt = new ParameterType(ParameterType.PT_DATE, ParameterType.PARAM_OPTIONAL | ParameterType.PARAM_DONTASK, 0);
        resForm.addComponent(new FormParameter(resForm, "executiondate", new PatternString(this, "$lang:s_res_wo_executiondate$"), null, pt, new ParameterSourceSQL(null, "executiondate")));
        pt = new ParameterType(ParameterType.PT_TEXT, ParameterType.PARAM_OPTIONAL | ParameterType.PARAM_READONLY | ParameterType.PARAM_DONTASK, 128);
        resForm.addComponent(new FormParameter(resForm, "executedby", new PatternString(this, "$lang:s_res_wo_executedby$"), null, pt, new ParameterSourceSQL(null, "executedby")));
        pt = new ParameterType(ParameterType.PT_DOUBLE, ParameterType.PARAM_OPTIONAL | ParameterType.PARAM_DONTASK, 0);
        resForm.addComponent(new FormParameter(resForm, "hours", new PatternString("hours - obsolete", true), null, pt, new ParameterSourceSQL(null, "hours")));
        resForm.addComponent(new SubForm(resForm, "timereport", new PatternString(this, "$lang:s_res_wo_tr$")));
        pt = new ParameterType(ParameterType.PT_TEXTAREA, ParameterType.PARAM_OPTIONAL | ParameterType.PARAM_DONTASK_ONCREATE | ParameterType.PARAM_ONLYAPPEND, 0);
        resForm.addComponent(new FormParameter(resForm, "comments", new PatternString(this, "$lang:s_res_wo_comments$"), null, pt, new ParameterSourceSQL(null, "comments")));
    }

    /**
	 * Init method.
	 * @param conn		Database connection
	 * @param irid		Resource id.
	 * @param iuid		User id.
	 */
    public void init(Connection conn, Integer irid, Integer iuid) throws Exception {
        super.init(conn, irid, iuid);
        WorkArea wa = getWorkArea();
        SubForm sf = (SubForm) resForm.getComponentStorage("timereport");
        sf.setBuilder(new WOTimeReportBuilder(this, conn, iuid, wa));
        sf.setFlags(ParameterType.PARAM_OPTIONAL | ParameterType.PARAM_MULTIVALUE | ParameterType.PARAM_DONTASK_ONCREATE);
    }

    /**
     * Gets the caption of the resource
     * @return  Resource's caption.
     */
    public String getResourceDescription() throws Exception {
        return Configuration.getLocaleString(uid, "s_res_wo");
    }

    /**
     * Formatter definition
     */
    public void defFormatter() throws Exception {
        super.defFormatter();
        String lang = Configuration.getUserProperty(uid, "lang");
        XSLTFormatter f = (XSLTFormatter) getFormatter();
        f.setXsl(Configuration.homeXSL + Configuration.getXslFile(lang, "wo"));
    }

    /**
     * Add virtual params to default formatter.
     * @param f                     Formatter.
     * @throws Exception
     */
    protected void defFormatterParams(RFormatter f) throws Exception {
        final String[] wocomponents = { "state", "title", "description", "creationdate", "createdby", "planneddate", "plannedby", "authdate", "authby", "executiondate", "executedby", "assignedto", "comments" };
        final String woname = "WO";
        for (int i = 0; i < wocomponents.length; ++i) {
            VParam vp = new VParam();
            vp.defComponent(VPARAMSOURCE.COMPONENT, woname, wocomponents[i]);
            f.defVParam(wocomponents[i], vp);
        }
        VParam vp = new VParam();
        vp.defComponent(VPARAMSOURCE.METHOD, woname, "getStateDisplay");
        f.defVParam("statedisplay", vp);
        vp = new VParam();
        vp.defComponent(VPARAMSOURCE.METHOD, woname, "getCreatedByDisplay");
        f.defVParam("createdbydisplay", vp);
        vp = new VParam();
        vp.defComponent(VPARAMSOURCE.METHOD, woname, "getPlannedByDisplay");
        f.defVParam("plannedbydisplay", vp);
        vp = new VParam();
        vp.defComponent(VPARAMSOURCE.METHOD, woname, "getAuthByDisplay");
        f.defVParam("authbydisplay", vp);
        vp = new VParam();
        vp.defComponent(VPARAMSOURCE.METHOD, woname, "getExecutedByDisplay");
        f.defVParam("executedbydisplay", vp);
        vp = new VParam();
        vp.defComponent(VPARAMSOURCE.METHOD, woname, "getAssignedToDisplay");
        f.defVParam("assignedtodisplay", vp);
        if (isManager(conn)) {
            vp = new VParam();
            vp.defComponent(VPARAMSOURCE.METHOD, woname, "getTimeReports");
            f.defVParam("timereports", vp);
        }
    }

    /**
     * Returns an array with all time reports in this work order
     * @return      List of time reports (format:  h h. m m - Tech: description) 
     */
    public ArrayList<String> getTimeReports() throws Exception {
        SubForm sf = (SubForm) resForm.getComponentStorage("timereport");
        ArrayList<Resource> tr = sf.getSubFormResources();
        return TimeReport.buildStringTimeReports(tr);
    }

    /**
     * Get information about the WO container
     * @param conn  Database connection
     */
    private void getWOInfo(Connection conn) {
        try {
            int changemgmt_rid = Core.getParent(conn, getParent());
            ResourceData rd = Core.getDataFromSource(conn, changemgmt_rid, uid, "CHANGEMGMTINFO", ResPublisher.SCOPE_LOCAL);
            LinkedHashMap lhm = (LinkedHashMap) rd.values().iterator().next();
            trcont = Integer.valueOf((String) lhm.get("tr"));
            workflow = (String) lhm.get("woworkflow");
            otManagers = (String) lhm.get("managers");
            otTech = (String) lhm.get("technicians");
            otUsers = (String) lhm.get("users");
        } catch (Exception e) {
            Configuration.logger.error("WO.getWOInfo(): " + e);
        }
    }

    /**
     * Returns the container id where the time reports will be stored.
     * @return      Container id.
     */
    public Integer getTimeReportContainer() {
        if (trcont == null) getWOInfo(conn);
        return trcont;
    }

    /**
     * Returns the workflow in use.
     * @return      Workflow name.
     */
    protected String getWorkflow() {
        Connection conn = null;
        try {
            if (workflow == null) {
                conn = Configuration._dbm.getConnection();
                getWOInfo(conn);
            }
        } catch (Exception e) {
            Configuration.logger.error("WO.getWorkflow(): " + e.getMessage());
        } finally {
            Configuration._dbm.closeConnection(conn);
        }
        return workflow;
    }

    /**
     * Name of the workflow used.
     * This workflow name should be updated when creating multiple
     * trouble tickets with different workflows.
     * @return  Workflow identifier.
     */
    public String getWorkflowName() {
        return getWorkflow();
    }

    /**
     * Returns state display string
     * @return      State display string
     * @throws Exception
     */
    public String getStateDisplay() throws Exception {
        ParameterTypeSelect fp = (ParameterTypeSelect) resForm.getComponentStorage("state").getType();
        String state = (String) resForm.getPValue("state");
        Patterns pat = new Patterns(this, null, null);
        return fp.getSelectCaption(pat, state);
    }

    /**
     * Returns the createdby display string
     * @return  createdby display name.
     */
    public String getCreatedByDisplay() throws Exception {
        String s = (String) resForm.getPValue("createdby");
        return LDAPUsers.sLookupUserDN(getConn(), getuid(), s, Configuration.ldapuserdisplayattr);
    }

    /**
     * Returns the plannedby display string
     * @return  plannedby display name.
     */
    public String getPlannedByDisplay() throws Exception {
        String s = (String) resForm.getPValue("plannedby");
        return LDAPUsers.sLookupUserDN(getConn(), getuid(), s, Configuration.ldapuserdisplayattr);
    }

    /**
     * Returns the authby display string
     * @return  Authby display name.
     */
    public String getAuthByDisplay() throws Exception {
        String s = (String) resForm.getPValue("authby");
        return LDAPUsers.sLookupUserDN(getConn(), getuid(), s, Configuration.ldapuserdisplayattr);
    }

    /**
     * Returns the executedby display string
     * @return  executedby display name.
     */
    public String getExecutedByDisplay() throws Exception {
        String s = (String) resForm.getPValue("executedby");
        return LDAPUsers.sLookupUserDN(getConn(), getuid(), s, Configuration.ldapuserdisplayattr);
    }

    /**
     * Returns the assignedto display string
     * @return  Assignedto display name.
     */
    public String getAssignedToDisplay() throws Exception {
        String s = (String) resForm.getPValue("assignedto");
        return LDAPUsers.sLookupUserDN(getConn(), getuid(), s, Configuration.ldapuserdisplayattr);
    }

    /**
     * Returns the Change Managers group.
     * @param conn  Database connection.
     * @return      Returns the change managers group name.
     */
    public String getChangeManager(Connection conn) {
        if (otManagers == null) getWOInfo(conn);
        return otManagers;
    }

    /**
     * Returns the technicians group.
     * @param conn  Database connection.
     * @return      Returns the technicians group name.
     */
    public String getTechnicians(Connection conn) {
        if (otTech == null) getWOInfo(conn);
        return otTech;
    }

    /**
     * Returns the users group.
     * @param conn  Database connection.
     * @return      Returns the users group name.
     */
    public String getUsers(Connection conn) {
        if (otUsers == null) getWOInfo(conn);
        return otUsers;
    }

    /**
     * Gets the parent Change resource to this wo
     * @param conn	Database connection
     * @return		Instantiated change resource.
     */
    public Change getChange(Connection conn) throws Exception {
        Change change = (Change) Core.instantiateResource(conn, "Change", getParent(), uid, null);
        return change;
    }

    /**
     * This is used to get the owner of the parent change
     * Used in the workflow definition.
     * @return	Owner DN
     */
    public String getChangeOwner(Connection conn) throws Exception {
        Change change = getChange(conn);
        return (String) change.getForm().getPValue("owner");
    }

    /**
     * This is used to get the mail of the owner
     * Used in the workflow definition.
     * @return  List of mails separated by commas.
     */
    public String getChangeOwnerMail(Connection conn) throws Exception {
        Change change = getChange(conn);
        String owner = (String) change.getForm().getPValue("owner");
        LinkedHashMap temp = LDAPUsers.lookupUserDN(conn, UserMapping.getUserId(conn, owner), owner);
        String mail = (String) temp.get("mail");
        if (mail != null && !mail.equals("")) return mail;
        return null;
    }

    /**
     * Returns true if current user is a manager
     * @param conn  Database connection.
     * @return      Returns true or false depending on the user being a manager or not.
     */
    private boolean isManager(Connection conn) throws Exception {
        RequestParameters req = workArea.getRequestParameters();
        String userdn = req.getParameter("session_dn");
        if (isManager == null) isManager = new Boolean(GroupMgmt.isUserInGroup(conn, null, getChangeManager(conn), null, userdn));
        return isManager.booleanValue();
    }

    /**
     * Path string.
     * @param uid       User id.
     * @return          Html link to this resource.
     */
    public String executeAbstractPath(int uid) {
        String title = null;
        try {
            title = (String) resForm.getPValue("title");
        } catch (Exception e) {
            Configuration.logger.error("WO.executeAbstractPath(): ", e);
            return null;
        }
        return StringUtils.scapeHtmlCharacters(title);
    }

    /**
     * Adjust some parameter properties before attempting to edit the resource.
     * @param conn  Database connection
     * @param uid   User id
     * @return True if everything was ok, False to cancel the edition.
     */
    public boolean before_edit_form(Connection conn, int uid) throws SQLException, Exception {
        RequestParameters req = workArea.getRequestParameters();
        String state = req.getParameter("state");
        if (state == null || state.length() == 0) state = (String) resForm.getPValue("state");
        String oldstate = (String) resForm.getPValue("state");
        if (oldstate.equals("pendplan")) {
            ParameterTypeSelect pts = (ParameterTypeSelect) resForm.getComponentStorage("state").getType();
            pts.changeCaption("pendauth", "Planned");
            ParameterType pt = (ParameterType) resForm.getComponentStorage("planneddate").getType();
            pt.setFlags(ParameterType.PARAM_OBLIGATORY);
            pt = (ParameterType) resForm.getComponentStorage("assignedto").getType();
            pt.setFlags(ParameterType.PARAM_OBLIGATORY);
        }
        if (state.equals("rej")) {
            ParameterType pt = (ParameterType) resForm.getComponentStorage("creationdate").getType();
            pt.setFlags(ParameterType.PARAM_OPTIONAL | ParameterType.PARAM_DONTASK);
            pt = (ParameterType) resForm.getComponentStorage("planneddate").getType();
            pt.setFlags(ParameterType.PARAM_OPTIONAL | ParameterType.PARAM_DONTASK);
            pt = (ParameterType) resForm.getComponentStorage("assignedto").getType();
            pt.setFlags(ParameterType.PARAM_OPTIONAL | ParameterType.PARAM_DONTASK);
            pt = (ParameterType) resForm.getComponentStorage("authdate").getType();
            pt.setFlags(ParameterType.PARAM_OPTIONAL | ParameterType.PARAM_DONTASK);
            pt = (ParameterType) resForm.getComponentStorage("executiondate").getType();
            pt.setFlags(ParameterType.PARAM_OPTIONAL | ParameterType.PARAM_DONTASK);
            SubForm sf = (SubForm) resForm.getComponent("timereport");
            int f = sf.getFlags();
            f = f & (f & ~ParameterType.PARAM_OBLIGATORY) | ParameterType.PARAM_OPTIONAL | ParameterType.PARAM_DONTASK;
            sf.setFlags(f);
            pt = (ParameterType) resForm.getComponentStorage("comments").getType();
            pt.setFlags(ParameterType.PARAM_OPTIONAL | ParameterType.PARAM_DONTASK);
        } else if (state != null && state.equals("done")) {
            ParameterType pt = (ParameterType) resForm.getComponentStorage("executiondate").getType();
            pt.setFlags(ParameterType.PARAM_OBLIGATORY | ParameterType.PARAM_READONLY_ONEDIT);
            if (!oldstate.equals("done")) {
                pt.setFlags(ParameterType.PARAM_OBLIGATORY);
                String userdn = req.getParameter("session_dn");
                userdn = LDAPUsers.sLookupUserDN(conn, uid, userdn, Configuration.ldapuserdisplayattr);
                resForm.setPValue("executedby", userdn);
            }
            pt = (ParameterType) resForm.getComponentStorage("executedby").getType();
            pt.setFlags(ParameterType.PARAM_OBLIGATORY | ParameterType.PARAM_READONLY_ONEDIT);
            SubForm sf = (SubForm) resForm.getComponent("timereport");
            int f = sf.getFlags();
            f = f & (f & ~ParameterType.PARAM_OPTIONAL) | ParameterType.PARAM_OBLIGATORY;
            sf.setFlags(f);
        }
        return super.before_edit_form(conn, uid);
    }

    /**
     * Executed before committing the creation of the resource.
     * @param conn  Database connection.
     * @return False cancels the creation.
     */
    public boolean before_edit(Connection conn) throws SQLException, Exception {
        RequestParameters req = workArea.getRequestParameters();
        String userdn = req.getParameter("session_dn");
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm");
        String newstate = (String) resForm.getValue("state");
        String oldstate = (String) resForm.getPValue("state");
        if (oldstate.equals("auth") && newstate.equals("auth") && resForm.getValue("executiondate") != null) {
            resForm.setValue("state", "done");
        } else if (oldstate.equals("pendauth") && newstate.equals("pendauth") && resForm.getValue("authdate") != null) {
            resForm.setValue("state", "auth");
        } else if (oldstate.equals("pendplan") && newstate.equals("pendplan") && resForm.getValue("planneddate") != null) {
            resForm.setValue("state", "pendauth");
        }
        newstate = (String) resForm.getValue("state");
        oldstate = (String) resForm.getPValue("state");
        if (newstate.equals("rej")) {
            if (resForm.isComponentEmpty("plannedby")) resForm.setValue("plannedby", userdn);
            if (resForm.isComponentEmpty("planneddate")) resForm.setValue("planneddate", new Date(System.currentTimeMillis()));
            if (resForm.isComponentEmpty("assignedto")) resForm.setValue("assignedto", userdn);
            if (resForm.isComponentEmpty("authdate")) resForm.setValue("authdate", new Date(System.currentTimeMillis()));
            if (resForm.isComponentEmpty("authby")) resForm.setValue("authby", userdn);
            if (resForm.isComponentEmpty("executedby")) resForm.setValue("executedby", userdn);
            if (resForm.isComponentEmpty("executiondate")) resForm.setValue("executiondate", new Date(System.currentTimeMillis()));
        } else if (oldstate.equals("pendplan") && newstate.equals("pendauth")) {
            resForm.setValue("plannedby", userdn);
        } else if (oldstate.equals("pendauth") && newstate.equals("auth")) {
            resForm.setValue("authdate", new Date(System.currentTimeMillis()));
            resForm.setValue("authby", userdn);
        } else if (oldstate.equals("auth") && newstate.equals("done")) {
            resForm.setValue("executedby", userdn);
        } else if (oldstate.equals("done") && newstate.equals("auth")) {
            resForm.setPValue("executiondate", null);
            resForm.setValue("executiondate", null);
            String displayName = req.getParameter("session_displayname");
            if (displayName == null) displayName = "a Manager";
            resForm.setValue("comments", (resForm.getPValue("comments") != null ? resForm.getPValue("comments") : "") + " \r\n-- " + sdf.format(new Date(System.currentTimeMillis())) + " - " + "Moved from Done to Authorized by " + displayName + " --\r\n");
        }
        return super.before_edit(conn);
    }

    /**
     * Executed after committing the edition of the resource.
     * @param conn  Database connection.
     * @throws Exception
     */
    public void after_edit(Connection conn) throws Exception {
        Change.updateState(conn, getParent(), uid, getWorkArea());
    }

    /**
     * Executed before committing the creation of the resource.
     * @param conn  Database connection.
     * @return False cancels the creation.
     */
    public boolean before_create(Connection conn) throws SQLException, Exception {
        RequestParameters req = workArea.getRequestParameters();
        String userdn = req.getParameter("session_dn");
        resForm.setValue("creationdate", new Date(System.currentTimeMillis()));
        resForm.setValue("createdby", userdn);
        Date pd = (Date) resForm.getRecentValue("planneddate");
        String at = (String) resForm.getRecentValue("assignedto");
        if (pd != null && at != null && !at.equals("")) {
            resForm.setValue("state", "pendauth");
            resForm.setValue("plannedby", userdn);
        }
        return super.before_create(conn);
    }
}
