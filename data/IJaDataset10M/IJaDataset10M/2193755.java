package com.entelience.soap;

import java.util.List;
import com.entelience.directory.People;
import com.entelience.export.DocumentHelper;
import com.entelience.export.jasper.ReportHelper;
import com.entelience.module.Module;
import com.entelience.module.ModuleHelper;
import com.entelience.objects.DropDown;
import com.entelience.objects.GeneratedDocument;
import com.entelience.objects.PageCounter;
import com.entelience.objects.module.ModuleDetail;
import com.entelience.objects.module.ModuleInfoLine;
import com.entelience.objects.module.ModuleMetric;
import com.entelience.objects.module.ModuleReport;
import com.entelience.objects.module.Preference;
import com.entelience.objects.module.PreferenceHistory;
import com.entelience.objects.raci.RaciException;
import com.entelience.raci.module.RaciAdmin;
import com.entelience.raci.module.RaciModule;
import com.entelience.sql.Db;
import com.entelience.sql.DbHelper;
import com.entelience.util.Config;

/**
 * Implement interface for soap modules management
 */
public class soapModule extends soapBase {

    public soapModule() throws Exception {
        super();
    }

    public soapModule(Integer fakedPeopleId) throws Exception {
        super(fakedPeopleId, (Integer) null);
    }

    protected String getModule() {
        return null;
    }

    public ModuleDetail[] listActiveModules() throws Exception {
        Db db = dbRO();
        try {
            _logger.info("Module : listActiveModules");
            List<ModuleDetail> res = ModuleHelper.getModulesDetails(db, true, getCurrentUser());
            ModuleDetail[] ret = new ModuleDetail[res.size()];
            ret = res.toArray(ret);
            return ret;
        } catch (Exception e) {
            store(e);
            throw e;
        } finally {
            db.safeClose();
        }
    }

    public ModuleDetail[] listRegisteredModules() throws Exception, RaciException {
        Db db = dbRO();
        try {
            _logger.info("Module : listRegisteredModules");
            List<ModuleDetail> res = ModuleHelper.getModulesDetails(db, false, getCurrentUser());
            ModuleDetail[] ret = new ModuleDetail[res.size()];
            ret = res.toArray(ret);
            return ret;
        } catch (Exception e) {
            store(e);
            throw e;
        } finally {
            db.safeClose();
        }
    }

    public ModuleDetail[] listAllModules() throws Exception, RaciException {
        Db db = dbRO();
        try {
            _logger.info("Module : listAllModules");
            List<ModuleDetail> res = ModuleHelper.getModulesDetails(db, false, getCurrentUser());
            ModuleDetail[] ret = new ModuleDetail[res.size()];
            ret = res.toArray(ret);
            return ret;
        } catch (Exception e) {
            store(e);
            throw e;
        } finally {
            db.safeClose();
        }
    }

    public ModuleInfoLine[] getAllModulesInformations(Integer userId) throws Exception, RaciException {
        Db db = dbRO();
        try {
            _logger.info("Module : getAllModulesInformations " + userId);
            List<ModuleInfoLine> res = ModuleHelper.getModules(db, false, false, userId);
            ModuleInfoLine[] ret = new ModuleInfoLine[res.size()];
            ret = res.toArray(ret);
            return ret;
        } catch (Exception e) {
            store(e);
            throw e;
        } finally {
            db.safeClose();
        }
    }

    public ModuleInfoLine[] getRegisteredModulesInformations(Integer userId) throws Exception, RaciException {
        Db db = dbRO();
        try {
            _logger.info("Module : getRegisteredModulesInformations " + userId);
            List<ModuleInfoLine> res = ModuleHelper.getModules(db, false, true, userId);
            ModuleInfoLine[] ret = new ModuleInfoLine[res.size()];
            ret = res.toArray(ret);
            return ret;
        } catch (Exception e) {
            store(e);
            throw e;
        } finally {
            db.safeClose();
        }
    }

    public ModuleInfoLine[] getActiveModulesInformations(Integer userId) throws Exception, RaciException {
        Db db = dbRO();
        try {
            _logger.info("Module : getActiveModulesInformations " + userId);
            List<ModuleInfoLine> res = ModuleHelper.getModules(db, true, true, userId);
            ModuleInfoLine[] ret = new ModuleInfoLine[res.size()];
            ret = res.toArray(ret);
            return ret;
        } catch (Exception e) {
            store(e);
            throw e;
        } finally {
            db.safeClose();
        }
    }

    public ModuleInfoLine getModuleInformation(int moduleId) throws Exception, RaciException {
        Db db = dbRO();
        try {
            _logger.info("Module : getModuleInformation " + moduleId);
            return ModuleHelper.getModuleInformation(db, moduleId);
        } catch (Exception e) {
            store(e);
            throw e;
        } finally {
            db.safeClose();
        }
    }

    public Preference[] listModulePreferences(int moduleId) throws Exception, RaciException {
        Db db = dbRO();
        try {
            _logger.info("Module : listModulePreferences " + moduleId);
            Module m = ModuleHelper.getModule(db, moduleId);
            checkUserCanViewProperties(db, m);
            List<Preference> prefs = m.getCurrentPreferences(db);
            Preference[] ret = new Preference[prefs.size()];
            ret = prefs.toArray(ret);
            return ret;
        } catch (Exception e) {
            store(e);
            throw e;
        } finally {
            db.safeClose();
        }
    }

    public Preference[] listGlobalPreferences() throws Exception, RaciException {
        Db db = dbRO();
        try {
            _logger.info("Module : listGlobalPreferences ");
            checkUserCanAccess(db);
            List<Preference> prefs = Config.getGlobalPreferences(db);
            Preference[] ret = new Preference[prefs.size()];
            ret = prefs.toArray(ret);
            return ret;
        } catch (Exception e) {
            store(e);
            throw e;
        } finally {
            db.safeClose();
        }
    }

    public PreferenceHistory[] getPreferenceHistory(String pref) throws Exception, RaciException {
        Db db = dbRO();
        try {
            _logger.info("Module : PreferenceHistory " + pref);
            checkUserCanAccess(db);
            List<PreferenceHistory> prefs = Config.getPreferenceHistory(db, pref);
            PreferenceHistory[] ret = new PreferenceHistory[prefs.size()];
            ret = prefs.toArray(ret);
            return ret;
        } catch (Exception e) {
            store(e);
            throw e;
        } finally {
            db.safeClose();
        }
    }

    public boolean setPreference(int moduleId, Preference pref) throws Exception, RaciException {
        Db db = dbRW();
        try {
            db.begin();
            _logger.info("Module : setPreference " + moduleId + "," + pref);
            Module m = ModuleHelper.getModule(db, moduleId);
            checkUserCanModifyProperties(db, m);
            m.setPreference(db, pref, getCurrentUser());
            db.commit();
            return true;
        } catch (Exception e) {
            store(e);
            throw e;
        } finally {
            db.safeClose();
        }
    }

    /**
     * list reports for a module
     *
     *
     */
    public ModuleReport[] listModuleReports(int moduleId, Integer page, String order, String way) throws Exception, RaciException {
        Db db = dbRO();
        try {
            _logger.info("Module : listModuleReports " + moduleId + "," + page + "," + order + "," + way);
            Module m = ModuleHelper.getModule(db, moduleId);
            checkUserCanViewReports(db, m);
            List<ModuleReport> reports = ReportHelper.listModuleReports(db, Integer.valueOf(moduleId), page, order, way);
            ModuleReport[] ret = new ModuleReport[reports.size()];
            ret = reports.toArray(ret);
            return ret;
        } catch (Exception e) {
            store(e);
            throw e;
        } finally {
            db.safeClose();
        }
    }

    public PageCounter countModuleReports(int moduleId) throws Exception, RaciException {
        Db db = dbRO();
        try {
            _logger.info("Module : countModuleReports " + moduleId);
            Module m = ModuleHelper.getModule(db, moduleId);
            checkUserCanViewReports(db, m);
            return ReportHelper.countModuleReports(db, Integer.valueOf(moduleId));
        } catch (Exception e) {
            store(e);
            throw e;
        } finally {
            db.safeClose();
        }
    }

    /**
     * list reports for all active modules
     */
    public ModuleReport[] listAllActiveModulesReports(Integer page, String order, String way) throws Exception, RaciException {
        Db db = dbRO();
        try {
            _logger.info("Module : listAllActiveModulesReports " + page + "," + order + "," + way);
            List<ModuleReport> reports = ReportHelper.listAllActiveModulesReports(db, page, order, way);
            ModuleReport[] ret = new ModuleReport[reports.size()];
            ret = reports.toArray(ret);
            return ret;
        } catch (Exception e) {
            store(e);
            throw e;
        } finally {
            db.safeClose();
        }
    }

    public PageCounter countAllActiveModulesReports() throws Exception, RaciException {
        Db db = dbRO();
        try {
            _logger.info("Module : countAllActiveModulesReports");
            return ReportHelper.countAllActiveModulesReports(db);
        } catch (Exception e) {
            store(e);
            throw e;
        } finally {
            db.safeClose();
        }
    }

    /**
     * list the metrics for a module
     * you can filter to display only metrics for an indicator (I,C,A,Re,Ro,E)
     *
     */
    public ModuleMetric[] listMetricsForModule(int moduleId, String indicator) throws Exception, RaciException {
        Db db = dbRO();
        try {
            String indic = DbHelper.nullify(indicator);
            _logger.info("Module : listMetricsForModule " + moduleId + "," + indic);
            Module m = ModuleHelper.getModule(db, moduleId);
            checkUserCanViewMetrics(db, m);
            List<ModuleMetric> res = m.getMetrics(db, indic);
            ModuleMetric[] ret = new ModuleMetric[res.size()];
            ret = res.toArray(ret);
            return ret;
        } catch (Exception e) {
            store(e);
            throw e;
        } finally {
            db.safeClose();
        }
    }

    /**
     * set the target (and its timescale if applicable) for a metric
     * 
     *
     */
    public boolean setTarget(int metricId, int newTarget, Integer timingId) throws Exception, RaciException {
        Db db = dbRW();
        try {
            _logger.info("Module : setTarget " + metricId + "," + newTarget + "," + timingId);
            db.begin();
            Module m = ModuleHelper.getModuleFromMetric(db, metricId);
            if (m == null) throw new IllegalArgumentException("No module found for metricId " + metricId);
            checkUserCanChangeTargets(db, m);
            m.updateTarget(db, metricId, newTarget, timingId, getCurrentUser());
            db.commit();
            return true;
        } catch (Exception e) {
            store(e);
            throw e;
        } finally {
            db.safeClose();
        }
    }

    /**
     * Returns the list of Possible timescales
     */
    public DropDown[] getListOfTimescales() throws Exception, RaciException {
        Db db = dbRO();
        try {
            _logger.info("Module : getListOfTimescales");
            return DbHelper.getDropDown(db, "SELECT e_module_metric_timing_id, name FROM e_module_metric_timing ORDER BY name ASC;");
        } catch (Exception e) {
            store(e);
            throw e;
        } finally {
            db.safeClose();
        }
    }

    private void checkUserCanViewMetrics(Db db, Module m) throws Exception, RaciException {
        RaciModule rm = m.getRaciModuleImpl(db);
        if (!rm.canViewMetrics(db, getCurrentUser())) {
            throw rm.getRaciException(db, "You cannot view the metrics for this module");
        }
    }

    private void checkUserCanChangeTargets(Db db, Module m) throws Exception, RaciException {
        RaciModule rm = m.getRaciModuleImpl(db);
        if (!rm.canManageTargets(db, getCurrentUser())) {
            throw rm.getRaciException(db, "You cannot cgange the targets for this module");
        }
    }

    private void checkUserCanViewProperties(Db db, Module m) throws Exception, RaciException {
        RaciModule rm = m.getRaciModuleImpl(db);
        if (!rm.canViewProperties(db, getCurrentUser())) {
            throw rm.getRaciException(db, "You cannot get the preferences for this module");
        }
    }

    private void checkUserCanModifyProperties(Db db, Module m) throws Exception, RaciException {
        RaciModule rm = m.getRaciModuleImpl(db);
        if (!rm.canChangeProperties(db, getCurrentUser())) {
            throw rm.getRaciException(db, "You cannot change the properties for this module");
        }
        ModuleInfoLine mi = ModuleHelper.getModuleInformation(db, m.getModuleId(db));
        if (!mi.isActive() || !mi.isRegistered()) {
            throw rm.getRaciException(db, "The module is unregistered or inactive and cannot change its preferences");
        }
    }

    private void checkUserCanViewReports(Db db, Module m) throws Exception, RaciException {
        RaciModule rm = m.getRaciModuleImpl(db);
        if (!rm.canViewReports(db, getCurrentUser())) {
            throw rm.getRaciException(db, "You cannot view the reports for this module");
        }
    }

    /**
     * Verify admin module access or admin rights for listReportFiles and  methods
     * @param db
     */
    private void checkUserCanAccess(Db db) throws Exception, RaciException {
        RaciAdmin mod = new RaciAdmin(db);
        People p = new People(db, getCurrentUser(), getCurrentUser());
        if (!p.isAdmin() && !mod.canAccessRO(db, getCurrentUser())) {
            throw mod.getRaciException(db, "You are not allowed to access to this module");
        }
    }

    private void checkUserCanAdmin(Db db) throws Exception, RaciException {
        RaciAdmin mod = new RaciAdmin(db);
        People p = new People(db, getCurrentUser(), getCurrentUser());
        if (!p.isAdmin() && !mod.canAccessRW(db, getCurrentUser())) {
            throw mod.getRaciException(db, "You are not allowed to modify anything in this module");
        }
    }

    public GeneratedDocument[] listGeneratedDocuments(Integer reportId, Integer page, String order, String way) throws Exception {
        Db db = dbRO();
        try {
            _logger.info("Module : listGeneratedReports " + reportId + "," + page + "," + order + "," + way);
            List<GeneratedDocument> docs = DocumentHelper.listGeneratedDocuments(db, reportId, page, order, way);
            GeneratedDocument[] ret = new GeneratedDocument[docs.size()];
            ret = docs.toArray(ret);
            return ret;
        } catch (Exception e) {
            store(e);
            throw e;
        } finally {
            db.safeClose();
        }
    }

    public PageCounter countGeneratedDocuments(Integer reportId) throws Exception {
        Db db = dbRO();
        try {
            _logger.info("Module : countGeneratedDocuments " + reportId);
            return DocumentHelper.countGeneratedDocuments(db, reportId);
        } catch (Exception e) {
            store(e);
            throw e;
        } finally {
            db.safeClose();
        }
    }

    public boolean sendGeneratedReportToEmail(int generatedReportId, String email) throws Exception, RaciException {
        Db db = dbRW();
        try {
            _logger.info("soapModule: sendGeneratedReportToEmail - " + generatedReportId + "," + email);
            db.begin();
            checkUserCanAdmin(db);
            boolean b = DocumentHelper.sendGeneratedReportToEmail(db, generatedReportId, email);
            db.commit();
            return b;
        } catch (Exception e) {
            store(e);
            throw e;
        } finally {
            db.safeClose();
        }
    }
}
