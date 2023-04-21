package org.genos.gmf.Core;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.TreeSet;
import javax.naming.NamingException;
import org.genos.gmf.Configuration;
import org.genos.gmf.RequestParameters;
import org.genos.gmf.ResPublisher;
import org.genos.gmf.Core.db.ContainedResourcesCache;
import org.genos.gmf.form.Button;
import org.genos.gmf.form.Form;
import org.genos.gmf.form.Form.FORMMODE;
import org.genos.gmf.resources.Resource;
import org.genos.gmf.resources.ResourceContainer;
import org.genos.gmf.resources.WorkArea;
import org.genos.gmf.resources.search.Indexing;
import org.genos.gmf.security.ACLs;
import org.genos.gmf.workflow.WorkflowManager;
import org.genos.utils.Links;

/**
 * Handles resource editing.
 */
public final class EditResource {

    /**
     * Edits a resource.
     * @param conn              Database connection.
     * @param uid               User id.
     * @param rid               Resource id being edited.
     * @param acls              New acls for this object. If null, acls are not updated.
     * @param propagateAcls     True if we want acls to be copied to all contained resources.
     * @param wa                Work Area.
     * @return  Returns a Form object if edition was not possible or failed,
     *          a message of error/warning in a String object, or
     *          null if edition was completed successfully.
     */
    public static Object editResource(Connection conn, int uid, int rid, String acls, boolean propagateAcls, WorkArea wa) throws SQLException, Exception {
        String resdefid = Core.getResDefId(conn, rid);
        if (resdefid == null) {
            return Configuration.getLocaleString(uid, "s_warn_resdeleted");
        }
        Integer parent = Core.getParent(conn, rid);
        Core.lockResource(rid);
        Core.lockResource(parent);
        try {
            if (!Resource.checkPermissions(conn, Resource.RES_ACTION_EDIT, rid, uid)) return Configuration.getLocaleString(uid, "s_warn_nopermissions");
            if (!Resource.checkPermissions(conn, ResourceContainer.RES_ACTION_EXECEDIT, parent, uid)) return Configuration.getLocaleString(uid, "s_warn_nopermissionstoedit");
            Resource r = Core.instantiateResource(conn, resdefid, rid, uid, wa);
            if (r == null) {
                Configuration.logger.error("EditResource.editResource(): couldn't instantiate resource class " + rid);
                return Configuration.getLocaleString(uid, "s_warn_resnotavailable");
            }
            r.setparentrid(parent);
            Form resForm = r.getForm();
            if (resForm == null) return Configuration.getLocaleString(uid, "s_warn_resnotform");
            resForm.setFormMode(FORMMODE.EDIT);
            RequestParameters req = r.getWorkArea().getRequestParameters();
            if (req.get("__form_message") != null) r.getForm().addMessageString((String) req.get("__form_message"));
            TreeSet<Integer> toLock = new TreeSet<Integer>();
            HashMap<String, ArrayList<Integer>> sfres = resForm.getRidsSubFormResources();
            for (ArrayList<Integer> sflist : sfres.values()) for (Integer i : sflist) toLock.add(i);
            Core.lockResource(toLock);
            try {
                return edit(conn, r, uid, acls, propagateAcls);
            } finally {
                Core.unlockResource(toLock);
            }
        } finally {
            Core.unlockResource(parent);
            Core.unlockResource(rid);
        }
    }

    /**
     * Handle the edition of resources.
     * Its purpose is to edit the resource or ask the new value of parameters.
     * @param conn  Database connection.
     * @param uid   User id.
     * @param newacls   String with new acls for this resource (format as required by ACLs.updateResourceACLsFromString). If null, ACLs are not updated.
     * @param propagate If new acls are defined for this edition and propagate is true, these ACLs will be copied to all contained resources.
     * @return      It returns a Form object. A simple Form.buildHTML(formaction, action) call will do to get 
     *              the html form code to ask the parameters. It returns null if resource was completely defined 
     *              and edited successfully.
     */
    private static Object edit(Connection conn, Resource r, int uid, String newacls, boolean propagate) throws SQLException, Exception {
        int rid = r.getrid();
        boolean allowEdition = r.before_edit_form(conn, uid);
        Form resForm = r.getForm();
        if (resForm == null) throw new Exception("EditResource.edit(): Resource form not initialized.");
        RequestParameters req = r.getWorkArea().getRequestParameters();
        allowEdition = resForm.isValidForm(conn, req, null) && allowEdition;
        if (allowEdition) {
            String uc = req.getParameter("usercommit");
            allowEdition = (uc != null && uc.equals("1"));
        }
        if (allowEdition) {
            LinkedHashMap<String, Object> postactions = new LinkedHashMap<String, Object>();
            Configuration._dbm.startTransaction(conn);
            Timestamp __sLastChanged = (Timestamp) resForm.getValue("__lastchanged");
            Timestamp __pLastChanged = (Timestamp) resForm.getPValue("__lastchanged");
            if (!__sLastChanged.equals(__pLastChanged)) {
                String sReload = req.getParameter("reload");
                boolean reload = sReload != null && sReload.equals("1");
                String sOverwrite = req.getParameter("overwrite");
                boolean overwrite = sOverwrite != null && sOverwrite.equals("1");
                if (reload) {
                    resForm.copyPValuesToValues(conn);
                    allowEdition = false;
                } else if (overwrite) {
                } else {
                    Configuration.logger.debug("EditResource.edit(): Resource has been modified while editing!!! rid=" + rid);
                    int parentId = r.getParent();
                    resForm.addMessageString(Configuration.getLocaleString(uid, "s_warn_resmodified"));
                    String action = Links.buildUrl("main?editresource=1&reload=1&rid=" + parentId + "&resid=" + rid);
                    resForm.addButton(new Button(Button.BUTTON_SUBMIT, Configuration.getLocaleString(uid, "s_reload"), action));
                    action = Links.buildUrl("main?editresource=1&overwrite=1&rid=" + parentId + "&resid=" + rid);
                    resForm.addButton(new Button(Button.BUTTON_SUBMIT, Configuration.getLocaleString(uid, "s_overwrite"), action));
                    allowEdition = false;
                }
            }
            HashMap<String, ArrayList<Resource>> sfres = resForm.getSubFormResources();
            allowEdition = r.before_edit(conn);
            for (ArrayList<Resource> sflist : sfres.values()) for (Resource raux : sflist) allowEdition = raux.before_edit(conn) && allowEdition;
            if (allowEdition) {
                allowEdition = WorkflowManager.runWorkflow(conn, r, postactions);
                for (ArrayList<Resource> sflist : sfres.values()) for (Resource raux : sflist) allowEdition = WorkflowManager.runWorkflow(conn, raux, postactions) && allowEdition;
            }
            if (allowEdition) {
                try {
                    postactions.put("_newacls", newacls);
                    postactions.put("_propagate", new Boolean(propagate));
                    Object ret = updateResource(conn, r, postactions);
                    if (ret == null) allowEdition = true; else {
                        allowEdition = false;
                        if (!ret.equals("")) {
                            resForm.addMessageString((String) ret);
                        }
                    }
                } catch (Exception e) {
                    Configuration._dbm.rollbackTransaction(conn);
                    throw e;
                }
            }
            if (allowEdition) {
                FullLog.logEdit(conn, rid, uid, resForm);
                Configuration._dbm.commitTransaction(conn);
                ContainedResourcesCache.invalidateContainedResourcesCache(r.getParent());
                PostActions.execute(conn, postactions);
                return null;
            }
            Configuration._dbm.rollbackTransaction(conn);
        }
        resForm.adminPermissions(Resource.checkPermissions(conn, Resource.RES_ACTION_ADMIN, rid, uid));
        return resForm;
    }

    /**
     * Generic edition of this resource.
     * This should only be called from Resource.edit unless you know what you're doing. 
     * This method will not check for user permissions, will not check for form validity, will not
     * execute before/post edition tasks.
     * This method is also used by PSSubForm to update resources in subforms.
     * @param conn  Database connection
     * @param r     Resource being updated.
     * @param postactions   Resources updated in this transaction.
     *                       It must contain also two input parameters:
     *                          _newacls
     *                          _propagate
     * @return      Null if everything was ok, an string with the error description if something failed,
     *              and an empty string if the operation was cancelled.
     */
    public static Object updateResource(Connection conn, Resource r, LinkedHashMap<String, Object> postactions) throws Exception {
        Object ret;
        Form f = r.getForm();
        int rid = r.getrid();
        ret = f.doUpdate(conn, "rid", new Integer(rid), postactions);
        if (ret != null) return ret;
        Indexing.indexResource(rid, f);
        if (r instanceof ResPublisher) DataPublisher.invalidatePublisherCache(rid);
        String newacls = (String) postactions.get("_newacls");
        Boolean propagate = (Boolean) postactions.get("_propagate");
        if (newacls != null) {
            HashMap<Integer, Integer> ht_acls = ACLs.getHashACLsFromString(conn, newacls);
            try {
                editPermissions(conn, ht_acls, rid, r.getuid(), propagate.booleanValue());
            } catch (Exception e) {
                throw e;
            }
        }
        if (postactions != null) postactions.put("updated", r);
        return null;
    }

    /**
     * Edit permissions for a resource.
     * @param conn          Database connection.
     * @param ht_acls       Hashtable with new acls.
     * @param rid           Resource id.
     * @param uid           User id.
     * @param propagate     If true, the acls will be propagated to all contained resources (if user has ADMIN access over them).
     * @throws NamingException
     * @throws SQLException
     * @throws Exception
     */
    private static void editPermissions(Connection conn, HashMap<Integer, Integer> ht_acls, int rid, int uid, boolean propagate) throws NamingException, SQLException, Exception {
        int resid;
        if (!Resource.checkPermissions(conn, Resource.RES_ACTION_ADMIN, rid, uid)) return;
        ACLs.updateResourceACLs(conn, rid, uid, ht_acls);
        if (!propagate) return;
        PreparedStatement stm = conn.prepareStatement("select id from resources where container=?");
        stm.setInt(1, rid);
        ResultSet rs = stm.executeQuery();
        while (rs.next()) {
            resid = rs.getInt(1);
            editPermissions(conn, ht_acls, resid, uid, propagate);
        }
        rs.close();
        stm.close();
    }
}
