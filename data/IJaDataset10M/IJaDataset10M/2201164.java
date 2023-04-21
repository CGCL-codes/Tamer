package com.manydesigns.portofino.base;

import com.manydesigns.portofino.base.cache.BlobTokenCache;
import com.manydesigns.portofino.base.cache.Query;
import com.manydesigns.portofino.base.cache.UserQueryCache;
import com.manydesigns.portofino.base.calculations.MDFuncCalcType;
import com.manydesigns.portofino.base.navigation.Navigation;
import com.manydesigns.portofino.base.operations.*;
import com.manydesigns.portofino.base.path.MDPathElement;
import com.manydesigns.portofino.base.portal.*;
import com.manydesigns.portofino.base.users.MDMetaUserGroup;
import com.manydesigns.portofino.base.users.User;
import com.manydesigns.portofino.base.workflow.MDWfAction;
import com.manydesigns.portofino.base.workflow.MDWfState;
import com.manydesigns.portofino.base.workflow.MDWfTransition;
import com.manydesigns.portofino.database.DatabaseAbstraction;
import com.manydesigns.portofino.util.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.MessageFormat;
import java.util.*;
import java.util.Map.Entry;

/**
 * Questa class rappresenta un &quot;piano&quot; nella terminologia di
 * Portofino. E' il punto iniziale da cui si può accedere ai metadati del
 * piano, quali classi, attributi, ecc. Inoltre permette di accedere
 * ai parametri di configurazione relativi al piano, quali nome
 * dell'applicazione, attivazione della gestione utenti, ecc.
 * <p>Per quanto riguarda le istanze di MDConfig,
 * queste sono fornite nel modo più semplice attraverso il metodo
 * doMethod() di com.manydesigs.methods.BasicServlet .
 * Alternativamente (p.e. nel caso in cui si sviluppa un sistema non web,
 * dove quindi non si può utilizzare BasicServlet) è possibile ottenere
 * istanze di MDConfig attraverso i metodi getAppConfig() e getMetaConfig()
 * di ConfigContainer, che funge da factory per MDConfig.
 */
public class MDConfigImpl implements MDConfig {

    public static final String copyright = "Copyright (c) 2005-2009, ManyDesigns srl";

    private static final int DEFAULT_USER_QUERY_CACHE_SIZE = 1000;

    private static final int DEFAULT_BLOB_CACHE_SIZE = 100;

    private final Map<Integer, MDClass> classIdCache;

    private final Map<String, MDClass> classNameCache;

    private final Map<Integer, MDWfState> wfStateIdCache;

    private final Map<Integer, MDWfTransition> wfTransitionIdCache;

    private final Map<Integer, MDWfAction> wfActionIdCache;

    private final Map<Integer, MDPortlet> portletIdCache;

    private final Map<Integer, MDJasperReports> jasperReportsCache;

    private final Map<Integer, MDAttribute> attrIdCache;

    private final Map<Integer, MDObjectOperation> objectOpIdCache;

    private final Map<Integer, MDObjectSetOperation> objectSetOpIdCache;

    private final Map<Integer, MDClassOperation> classOpIdCache;

    private final Map<Integer, MDGlobalOperation> globalOpIdCache;

    private final Map<Integer, MDMetaUserGroup> mugIdCache;

    private final Map<String, MDFuncCalcType> funcCalcTypeNameCache;

    private final Map<Integer, MDPathElement> pathElementIdCache;

    private final List<MDPortlet> portletsOnHomePage;

    private int maxBlobId;

    private final Set<MDClass> classesSortedByOrder;

    private final Set<MDClass> classesSortedByPrettyName;

    private final Set<MDClass> classesSortedByPrettyPlural;

    private final Set<MDObjectOperation> objectOperationSortedByOrder;

    private final Set<MDObjectSetOperation> objectSetOperationSortedByOrder;

    private final Set<MDClassOperation> classOperationSortedByOrder;

    private final Set<MDGlobalOperation> globalOperationSortedByOrder;

    private final QueryCache queryCache;

    private final UserQueryCache userQueryCache;

    private final PortletCache portletCache;

    private final BlobTokenCache blobTokenCache;

    private final MDConfigContainer configContainer;

    private final boolean doUser;

    private final boolean allowAnonymous;

    private final LocaleInfo localeInfo;

    private final Locale locale;

    private final int maxPathSearchDepth;

    private final int maxSearchResultPerPage;

    private final String schema2;

    private final String schema1;

    private final String schema0;

    private final String style;

    private final String systemName;

    private final String mdTemplate;

    private final String mdCvsWorkingDirectory;

    private final String mdCvsRepository;

    private final String mdCvsModule;

    private final String mdCvsPath;

    private final boolean upstairs;

    private final boolean isMetaEnabled;

    private final Navigation navigation;

    private boolean loginEnabled;

    private final MDThreadLocals threadLocals;

    private final Log log = LogFactory.getLog(MDConfigImpl.class);

    public MDConfigImpl(MDConfigContainer configContainer, MDThreadLocals threadLocals, boolean doUser, boolean allowAnonymous, LocaleInfo localeInfo, Locale locale, int maxPathSearchDepth, int maxSearchResultPerPage, String schema2, String schema1, String schema0, String style, String systemName, String mdTemplate, String mdCvsWorkingDirectory, String mdCvsRepository, String mdCvsModule, String mdCvsPath, boolean upstairs, boolean isMetaEnabled) {
        log.info(MessageFormat.format("Resetting MDConfig ''{0}''...", systemName));
        this.configContainer = configContainer;
        this.threadLocals = threadLocals;
        this.doUser = doUser;
        this.allowAnonymous = allowAnonymous;
        this.localeInfo = localeInfo;
        this.locale = locale;
        this.maxPathSearchDepth = maxPathSearchDepth;
        this.maxSearchResultPerPage = maxSearchResultPerPage;
        this.schema2 = schema2;
        this.schema1 = schema1;
        this.schema0 = schema0;
        this.style = style;
        this.systemName = systemName;
        this.mdTemplate = mdTemplate;
        this.mdCvsWorkingDirectory = mdCvsWorkingDirectory;
        this.mdCvsRepository = mdCvsRepository;
        this.mdCvsModule = mdCvsModule;
        this.mdCvsPath = mdCvsPath;
        this.upstairs = upstairs;
        this.isMetaEnabled = isMetaEnabled;
        classIdCache = new HashMap<Integer, MDClass>();
        classNameCache = new HashMap<String, MDClass>();
        wfStateIdCache = new HashMap<Integer, MDWfState>();
        wfTransitionIdCache = new HashMap<Integer, MDWfTransition>();
        wfActionIdCache = new HashMap<Integer, MDWfAction>();
        jasperReportsCache = new LinkedHashMap<Integer, MDJasperReports>();
        portletIdCache = new LinkedHashMap<Integer, MDPortlet>();
        attrIdCache = new HashMap<Integer, MDAttribute>();
        objectOpIdCache = new HashMap<Integer, MDObjectOperation>();
        objectSetOpIdCache = new HashMap<Integer, MDObjectSetOperation>();
        classOpIdCache = new HashMap<Integer, MDClassOperation>();
        globalOpIdCache = new HashMap<Integer, MDGlobalOperation>();
        mugIdCache = new HashMap<Integer, MDMetaUserGroup>();
        funcCalcTypeNameCache = new HashMap<String, MDFuncCalcType>();
        pathElementIdCache = new HashMap<Integer, MDPathElement>();
        portletsOnHomePage = new ArrayList<MDPortlet>();
        classesSortedByOrder = new TreeSet<MDClass>(new MDClassComparatorByOrder());
        classesSortedByPrettyName = new TreeSet<MDClass>(new MDClassComparatorByPrettyName());
        classesSortedByPrettyPlural = new TreeSet<MDClass>(new MDClassComparatorByPrettyPlural());
        objectOperationSortedByOrder = new TreeSet<MDObjectOperation>(new MDOperationComparatorByOrder());
        objectSetOperationSortedByOrder = new TreeSet<MDObjectSetOperation>(new MDOperationComparatorByOrder());
        classOperationSortedByOrder = new TreeSet<MDClassOperation>(new MDOperationComparatorByOrder());
        globalOperationSortedByOrder = new TreeSet<MDGlobalOperation>(new MDOperationComparatorByOrder());
        queryCache = new QueryCache(this, threadLocals);
        userQueryCache = new UserQueryCache(threadLocals, DEFAULT_USER_QUERY_CACHE_SIZE);
        portletCache = new PortletCache(threadLocals, locale);
        blobTokenCache = new BlobTokenCache(threadLocals, DEFAULT_BLOB_CACHE_SIZE);
        navigation = new Navigation(this);
    }

    public MDConfigContainer getConfigContainer() {
        return configContainer;
    }

    public String getName() {
        return systemName;
    }

    public boolean allowAnonymous() {
        return allowAnonymous;
    }

    public LocaleInfo getLocaleInfo() {
        return localeInfo;
    }

    public Locale getLocale() {
        return locale;
    }

    public int getMaxPathSearchDepth() {
        return maxPathSearchDepth;
    }

    public int getMaxSearchResultPerPage() {
        return maxSearchResultPerPage;
    }

    public String getSchema2() {
        return schema2;
    }

    public String getSchema1() {
        return schema1;
    }

    public String getSchema0() {
        return schema0;
    }

    public String getStyle() {
        return style;
    }

    public String getSystemName() {
        return systemName;
    }

    public String getMdTemplate() {
        return mdTemplate;
    }

    public String getMDCvsWorkingDirectory() {
        return mdCvsWorkingDirectory;
    }

    public String getMDCvsRepository() {
        return mdCvsRepository;
    }

    public String getMDCvsModule() {
        return mdCvsModule;
    }

    public String getCvsPath() {
        return mdCvsPath;
    }

    public boolean isUpstairs() {
        return upstairs;
    }

    public boolean isMetaEnabled() {
        return isMetaEnabled;
    }

    public void registerMDClass(MDClass cls) {
        classIdCache.put(cls.getId(), cls);
        classNameCache.put(cls.getName(), cls);
        classesSortedByOrder.add(cls);
        classesSortedByPrettyName.add(cls);
        classesSortedByPrettyPlural.add(cls);
    }

    public void registerAttribute(MDAttribute attr) {
        attrIdCache.put(attr.getId(), attr);
    }

    public void registerFuncCalcType(MDFuncCalcType funcCalcType) {
        funcCalcTypeNameCache.put(funcCalcType.getName(), funcCalcType);
    }

    public void registerPathElement(MDPathElement el) {
        pathElementIdCache.put(el.getId(), el);
    }

    public void registerObjectOperation(MDObjectOperation op) {
        objectOpIdCache.put(op.getId(), op);
        objectOperationSortedByOrder.add(op);
    }

    public void registerObjectSetOperation(MDObjectSetOperation op) {
        objectSetOpIdCache.put(op.getId(), op);
        objectSetOperationSortedByOrder.add(op);
    }

    public void registerClassOperation(MDClassOperation op) {
        classOpIdCache.put(op.getId(), op);
        classOperationSortedByOrder.add(op);
    }

    public void registerGlobalOperation(MDGlobalOperation op) {
        globalOpIdCache.put(op.getId(), op);
        globalOperationSortedByOrder.add(op);
    }

    public void registerWorkflowState(MDWfState state) {
        wfStateIdCache.put(state.getId(), state);
    }

    public void registerWorkflowTransition(MDWfTransition wft) {
        wfTransitionIdCache.put(wft.getId(), wft);
    }

    public void registerMetaUserGroup(MDMetaUserGroup mug) {
        mugIdCache.put(mug.getId(), mug);
    }

    public void registerPortlet(MDPortlet portlet, boolean onHomepage) {
        portletIdCache.put(portlet.getId(), portlet);
        if (onHomepage) {
            portletsOnHomePage.add(portlet);
        }
    }

    public void registerJasperReports(MDJasperReports jrpt) {
        jasperReportsCache.put(jrpt.getId(), jrpt);
    }

    public void registerWorkflowAction(MDWfAction wfa) {
        wfActionIdCache.put(wfa.getId(), wfa);
    }

    public synchronized void postResetSetup() throws Exception {
        recalculatePermissionFree();
        for (MDClass current : classIdCache.values()) {
            current.postResetSetup();
        }
        for (MDAttribute current : attrIdCache.values()) {
            if (current instanceof MDRelAttribute) ((MDRelAttribute) current).postResetSetup();
        }
        for (MDPathElement current : pathElementIdCache.values()) {
            current.postResetSetup();
        }
        maxBlobId = 0;
        for (MDAttribute current : attrIdCache.values()) {
            if (current instanceof MDBlobAttribute) {
                int max = ((MDBlobAttribute) current).getMaxBlobId();
                if (max > maxBlobId) {
                    maxBlobId = max;
                }
            }
        }
        for (MDPortlet current : portletIdCache.values()) {
            current.postResetSetup();
        }
        boolean userTablesOk;
        try {
            MDClass userCls = getMDClassByName(Defs.USER_CLS);
            MDTextAttribute loginAttr = (MDTextAttribute) userCls.getAttributeByName(Defs.USER_LOGIN_ATTR);
            MDTextAttribute passwordAttr = (MDTextAttribute) userCls.getAttributeByName(Defs.USER_PASSWORD_ATTR);
            MDTextAttribute emailAttr = (MDTextAttribute) userCls.getAttributeByName(Defs.USER_EMAIL_ATTR);
            getMDClassByName(Defs.USERGRP_CLS);
            getMDClassByName(Defs.USER_USERGRP_CLS);
            userTablesOk = true;
        } catch (Exception e) {
            userTablesOk = false;
        }
        loginEnabled = doUser && userTablesOk;
        getNavigation().setup(loginEnabled);
    }

    public synchronized void recalculatePermissionFree() {
        for (MDClass current : classIdCache.values()) {
            if (current.getParent() != null) continue;
            current.calculatePermFree1();
        }
        for (MDClass current : classIdCache.values()) {
            current.calculatePermFree2();
        }
    }

    public void selfTest() throws Exception {
        Collection<String> errors = new ArrayList<String>();
        selfTest(errors);
        if (!errors.isEmpty()) {
            throw new Exception(errors.toString());
        }
    }

    public void selfTest(Collection<String> errors) {
        Transaction tx = getCurrentTransaction();
        for (MDClass cls : classIdCache.values()) {
            cls.selfTest(errors);
        }
        if (loginEnabled) {
            PreparedStatement st = null;
            ResultSet rs = null;
            try {
                MDClass userCls = getMDClassByName(Defs.USER_CLS);
                MDClass userGrpCls = getMDClassByName(Defs.USERGRP_CLS);
                MDClass userUserGrpCls = getMDClassByName(Defs.USER_USERGRP_CLS);
                DatabaseAbstraction mdDataBase = getConfigContainer().getMDDataBase();
                String sql = mdDataBase.verifyUserGroup(getSchema1(), getSchema2(), Defs.USERGROUP_META_CLS, Defs.USERGRP_CLS);
                st = tx.prepareStatement(sql);
                rs = st.executeQuery();
                while (rs.next()) {
                    Integer mugId = null;
                    if (rs.getObject("mug_id") != null) mugId = rs.getInt("mug_id");
                    Integer ugId = null;
                    if (rs.getObject("ug_id") != null) ugId = rs.getInt("ug_id");
                    String mugName = null;
                    if (rs.getObject("mug_name") != null) mugName = rs.getString("mug_name");
                    String ugName = null;
                    if (rs.getObject("ug_name") != null) ugName = rs.getString("ug_name");
                    if (mugId == null) {
                        String msg = MessageFormat.format(Util.getLocalizedString(Defs.MDLIBI18N, getLocale(), "Missing_class_with_id"), Defs.USERGROUP_META_CLS, ugId);
                        errors.add(msg);
                    }
                    if (ugId == null) {
                        String msg = MessageFormat.format(Util.getLocalizedString(Defs.MDLIBI18N, getLocale(), "Missing_class_with_id"), Defs.USERGRP_CLS, mugId);
                        errors.add(msg);
                    }
                    if (mugName == null) {
                        String msg = MessageFormat.format(Util.getLocalizedString(Defs.MDLIBI18N, getLocale(), "class_with_id_has_null_name"), Defs.USERGROUP_META_CLS, mugId);
                        errors.add(msg);
                    }
                    if (ugName == null) {
                        String msg = MessageFormat.format(Util.getLocalizedString(Defs.MDLIBI18N, getLocale(), "class_with_id_has_null_name"), Defs.USERGRP_CLS, ugId);
                        errors.add(msg);
                    }
                    if (!ugName.equals(mugName)) {
                        String msg = MessageFormat.format(Util.getLocalizedString(Defs.MDLIBI18N, getLocale(), "c_and_c_with_id_have_different_names"), Defs.USERGRP_CLS, Defs.USERGROUP_META_CLS, ugId);
                        errors.add(msg);
                    }
                }
            } catch (Exception e) {
                errors.add(e.getMessage());
            } finally {
                try {
                    if (rs != null) rs.close();
                } catch (Exception e) {
                }
                try {
                    if (st != null) st.close();
                } catch (Exception e) {
                }
            }
        }
        selfTestFunctionTypes(errors);
        if (!isUpstairs()) {
            selfTestObjOps(tx, errors);
            selfTestFilterOps(tx, errors);
            selfTestTimeFilterTypes(tx, errors);
            selfTestTimeUnits(tx, errors);
            selfTest1DPrptTypes(tx, errors);
            selfTest2DPrptTypes(tx, errors);
            selfTestOrientation(tx, errors);
            selfTestPathSegments(tx, errors);
            if (isUpstairs()) {
                selfTestModelObjOps(tx, errors);
                selfTestModelGlobalOps(tx, errors);
                selfTestModelClassOps(tx, errors);
                selfTestModelUsers(tx, errors);
                selfTestModelUserGroups(tx, errors);
            }
        }
        log.debug(MessageFormat.format("Self test of '{0}' completed", getName()));
    }

    private void selfTestFunctionTypes(Collection<String> errors) {
        String functions[] = { "sum", "add", "sub", "mul", "div", "parenthesis", "cat", "count", "eq", "gt", "lt", "ge", "le", "neq", "uminus", "and", "or", "not", "if", "len", "match", "isnull", "min", "max", "mcd", "urlencode", "checkcodicefiscale" };
        for (String current : functions) {
            MDFuncCalcType fct = funcCalcTypeNameCache.get(current);
            if (fct == null) {
                String msg = MessageFormat.format(Util.getLocalizedString(Defs.MDLIBI18N, getLocale(), "Function_calculation_type_not_found"), current);
                errors.add(msg);
            }
        }
        if (funcCalcTypeNameCache.size() != functions.length) {
            Testing.assertEquals(errors, "Incorrect function calculation type count.", functions.length, funcCalcTypeNameCache.size(), locale);
        }
    }

    private void selfTestTimeFilterTypes(Transaction tx, Collection<String> errors) {
        String query = MessageFormat.format("SELECT * FROM \"{0}\".\"{1}\"", getSchema2(), Defs.TIMEFILTERTYP_CLS);
        boolean t_last = false;
        boolean t_current = false;
        boolean t_next = false;
        boolean t_since = false;
        boolean t_until = false;
        boolean t_past = false;
        boolean t_future = false;
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = tx.prepareStatement(query);
            rs = st.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String msg = "Valore 'name' incorretto per time filter type ";
                switch(id) {
                    case MDTimeFilterType.LAST_PERIOD:
                        t_last = true;
                        Testing.assertEquals(errors, msg + id + ": ", "last period", name, locale);
                        break;
                    case MDTimeFilterType.CURRENT_PERIOD:
                        t_current = true;
                        Testing.assertEquals(errors, msg + id + ": ", "current period", name, locale);
                        break;
                    case MDTimeFilterType.NEXT_PERIOD:
                        t_next = true;
                        Testing.assertEquals(errors, msg + id + ": ", "next period", name, locale);
                        break;
                    case MDTimeFilterType.SINCE_THE_BEGINNING_OF_THE_CURRENT_PERIOD:
                        t_since = true;
                        Testing.assertEquals(errors, msg + id + ": ", "since the beginning of the current period", name, locale);
                        break;
                    case MDTimeFilterType.UNTIL_THE_END_OF_THE_CURRENT_PERIOD:
                        t_until = true;
                        Testing.assertEquals(errors, msg + id + ": ", "until the end of the current period", name, locale);
                        break;
                    case MDTimeFilterType.ONE_PERIOD_IN_THE_PAST:
                        t_past = true;
                        Testing.assertEquals(errors, msg + id + ": ", "one period in the past", name, locale);
                        break;
                    case MDTimeFilterType.ONE_PERIOD_IN_THE_FUTURE:
                        t_future = true;
                        Testing.assertEquals(errors, msg + id + ": ", "one period in the future", name, locale);
                        break;
                    default:
                        errors.add("Time filter type sconosciuto: " + id + " - " + name);
                }
            }
            Testing.assertTrue(errors, "'last period' assente", t_last);
            Testing.assertTrue(errors, "'current period' assente", t_current);
            Testing.assertTrue(errors, "'next period' assente", t_next);
            Testing.assertTrue(errors, "'since the beginning of the current period' assente", t_since);
            Testing.assertTrue(errors, "'until the end of the current period' assente", t_until);
            Testing.assertTrue(errors, "'one period in the past' assente", t_past);
            Testing.assertTrue(errors, "'one period in the future' assente", t_future);
        } catch (Exception e) {
            errors.add(e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
            } catch (Exception e) {
            }
            try {
                if (st != null) st.close();
            } catch (Exception e) {
            }
        }
    }

    private void selfTestTimeUnits(final Transaction tx, Collection<String> errors) {
        String query = MessageFormat.format("SELECT * FROM \"{0}\".\"{1}\"", getSchema2(), Defs.TIMEUNIT_CLS);
        boolean t_day = false;
        boolean t_week = false;
        boolean t_month = false;
        boolean t_quarter = false;
        boolean t_quadrimester = false;
        boolean t_semester = false;
        boolean t_year = false;
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = tx.prepareStatement(query);
            rs = st.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String msg = Util.getLocalizedString(Defs.MDLIBI18N, getLocale(), "Wrong_name_value_for_time_unit_");
                switch(id) {
                    case MDTimeUnit.DAY:
                        t_day = true;
                        Testing.assertEquals(errors, msg + id + ": ", "day", name, locale);
                        break;
                    case MDTimeUnit.WEEK:
                        t_week = true;
                        Testing.assertEquals(errors, msg + id + ": ", "week", name, locale);
                        break;
                    case MDTimeUnit.MONTH:
                        t_month = true;
                        Testing.assertEquals(errors, msg + id + ": ", "month", name, locale);
                        break;
                    case MDTimeUnit.QUARTER:
                        t_quarter = true;
                        Testing.assertEquals(errors, msg + id + ": ", "quarter", name, locale);
                        break;
                    case MDTimeUnit.QUADRIMESTER:
                        t_quadrimester = true;
                        Testing.assertEquals(errors, msg + id + ": ", "quadrimester", name, locale);
                        break;
                    case MDTimeUnit.SEMESTER:
                        t_semester = true;
                        Testing.assertEquals(errors, msg + id + ": ", "semester", name, locale);
                        break;
                    case MDTimeUnit.YEAR:
                        t_year = true;
                        Testing.assertEquals(errors, msg + id + ": ", "year", name, locale);
                        break;
                    default:
                        msg = MessageFormat.format(Util.getLocalizedString(Defs.MDLIBI18N, getLocale(), "Unknown_time_unit"), id, name);
                        errors.add(msg);
                }
            }
        } catch (Exception e) {
            errors.add(e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
            } catch (Exception e) {
            }
            try {
                if (st != null) st.close();
            } catch (Exception e) {
            }
        }
        String msg = Util.getLocalizedString(Defs.MDLIBI18N, getLocale(), "Missing_Time_unit_");
        Testing.assertTrue(errors, msg + "day", t_day);
        Testing.assertTrue(errors, msg + "week", t_week);
        Testing.assertTrue(errors, msg + "month", t_month);
        Testing.assertTrue(errors, msg + "quarter", t_quarter);
        Testing.assertTrue(errors, msg + "quadrimester", t_quadrimester);
        Testing.assertTrue(errors, msg + "semester", t_semester);
        Testing.assertTrue(errors, msg + "year", t_year);
    }

    private void selfTestObjOps(final Transaction tx, Collection<String> errors) {
        String query = MessageFormat.format("SELECT * FROM \"{0}\".\"{1}_view\"", getSchema2(), Defs.OBJOP_CLS);
        boolean t_create = false;
        boolean t_delete = false;
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = tx.prepareStatement(query);
            rs = st.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                Object cid = rs.getObject("class");
                Object link = rs.getObject("link");
                Integer order = null;
                if (rs.getObject("order") != null) {
                    order = rs.getInt("order");
                }
                String msg1 = Util.getLocalizedString(Defs.MDLIBI18N, locale, "Class_value_not_null_for_object_operation_");
                String msg2 = Util.getLocalizedString(Defs.MDLIBI18N, locale, "Link_value_not_null_for_object_operation_");
                String msg3 = Util.getLocalizedString(Defs.MDLIBI18N, locale, "Wrong_order_value_for_object_operation_");
                String msg4 = Util.getLocalizedString(Defs.MDLIBI18N, locale, "Wrong_name_value_for_object_operation_");
                switch(id) {
                    case MDOperation.CREATE:
                        t_create = true;
                        Testing.assertEquals(errors, msg4, "create", name, locale);
                        Testing.assertNull(errors, msg1 + id + " - create: ", cid);
                        Testing.assertNull(errors, msg2 + id + " - create: ", link);
                        Testing.assertEquals(errors, msg3 + id + " - create: ", new Integer(1), order, locale);
                        break;
                    case MDOperation.DELETE:
                        t_delete = true;
                        Testing.assertEquals(errors, msg4, "delete", name, locale);
                        Testing.assertNull(errors, msg1 + id + " - delete: ", cid);
                        Testing.assertNull(errors, msg2 + id + " - delete: ", link);
                        Testing.assertEquals(errors, msg3 + id + " - delete: ", new Integer(2), order, locale);
                        break;
                    default:
                }
            }
        } catch (Exception e) {
            errors.add(e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
            } catch (Exception e) {
            }
            try {
                if (st != null) st.close();
            } catch (Exception e) {
            }
        }
        String msg = Util.getLocalizedString(Defs.MDLIBI18N, getLocale(), "Missing_object_operation_not_found_");
        Testing.assertTrue(errors, msg + "create", t_create);
        Testing.assertTrue(errors, msg + "delete", t_delete);
    }

    private void selfTestModelObjOps(final Transaction tx, Collection<String> errors) {
        String query = MessageFormat.format("SELECT * FROM \"{0}\".\"{1}_view\"", getSchema2(), Defs.OBJOP_CLS);
        boolean t_permessi = false;
        boolean t_sincronizza = false;
        boolean t_compila = false;
        boolean t_formula = false;
        boolean t_operazioni = false;
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = tx.prepareStatement(query);
            rs = st.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                Object cid = rs.getObject("class");
                String link = rs.getString("link");
                Integer order = null;
                if (rs.getObject("order") != null) {
                    order = rs.getInt("order");
                }
                String msg1 = Util.getLocalizedString(Defs.MDLIBI18N, getLocale(), "Wrong_name_value_for_object_operation_");
                String msg2 = Util.getLocalizedString(Defs.MDLIBI18N, getLocale(), "Class_value_not_null_for_object_operation_");
                String msg3 = Util.getLocalizedString(Defs.MDLIBI18N, getLocale(), "Wrong_link_value_for_object_operation_");
                String msg4 = Util.getLocalizedString(Defs.MDLIBI18N, getLocale(), "Wrong_order_value_for_object_operation_");
                switch(id) {
                    case MDOperation.CLS_PERM:
                        t_permessi = true;
                        Testing.assertEquals(errors, msg1 + id + ": ", "permissions", name, locale);
                        Testing.assertNotNull(errors, msg2 + id + " - permissions: ", cid);
                        Testing.assertEquals(errors, msg3 + id + " - permissions: ", "SetPerms", link, locale);
                        Testing.assertEquals(errors, msg4 + id + " - permissions: ", new Integer(6), order, locale);
                        break;
                    case MDOperation.CVS_SYNC:
                        t_sincronizza = true;
                        Testing.assertEquals(errors, msg1 + id + ": ", "synchronize", name, locale);
                        Testing.assertNotNull(errors, msg2 + id + " - synchronize: ", cid);
                        Testing.assertEquals(errors, msg3 + id + " - synchronize: ", "Sync", link, locale);
                        Testing.assertEquals(errors, msg4 + id + " - synchronize: ", new Integer(6), order, locale);
                        break;
                    case MDOperation.JRPT_COMPILE:
                        t_compila = true;
                        Testing.assertEquals(errors, msg1 + id + ": ", "compile", name, locale);
                        Testing.assertNotNull(errors, msg2 + id + " - compile: ", cid);
                        Testing.assertEquals(errors, msg3 + id + " - compile: ", "CompileJasperReports", link, locale);
                        Testing.assertEquals(errors, msg4 + id + " - compile: ", new Integer(6), order, locale);
                        break;
                    case MDOperation.ATTR_FORMULA:
                        t_formula = true;
                        Testing.assertEquals(errors, msg1 + id + ": ", "formula", name, locale);
                        Testing.assertNotNull(errors, msg2 + id + " - formula: ", cid);
                        Testing.assertEquals(errors, msg3 + id + " - formula: ", "DefineFormula", link, locale);
                        Testing.assertEquals(errors, msg4 + id + " - formula: ", new Integer(6), order, locale);
                        break;
                    case MDOperation.WF_OPERATION:
                        t_operazioni = true;
                        Testing.assertEquals(errors, msg1 + id + ": ", "operations", name, locale);
                        Testing.assertNotNull(errors, msg2 + id + " - operations: ", cid);
                        Testing.assertEquals(errors, msg3 + id + " - operations: ", "SetWfTran", link, locale);
                        Testing.assertEquals(errors, msg4 + id + " - operations: ", new Integer(7), order, locale);
                        break;
                    default:
                        if (id > MDOperation.DELETE) {
                            String msg = MessageFormat.format(Util.getLocalizedString(Defs.MDLIBI18N, getLocale(), "Unknown_Object_operation"), id, name);
                            errors.add(msg);
                        }
                }
            }
        } catch (Exception e) {
            errors.add(e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
            } catch (Exception e) {
            }
            try {
                if (st != null) st.close();
            } catch (Exception e) {
            }
        }
        String msg = Util.getLocalizedString(Defs.MDLIBI18N, getLocale(), "Missing_object_operation_not_found_");
        Testing.assertTrue(errors, msg + "permissions", t_permessi);
        Testing.assertTrue(errors, msg + "synchronize", t_sincronizza);
        Testing.assertTrue(errors, msg + "compile", t_compila);
        Testing.assertTrue(errors, msg + "formula", t_formula);
        Testing.assertTrue(errors, msg + "operations", t_operazioni);
    }

    private void selfTestModelGlobalOps(Transaction tx, Collection<String> errors) {
        String query = MessageFormat.format("SELECT * FROM \"{0}\".\"{1}_view\"", getSchema2(), Defs.GLOBALOP_CLS);
        boolean t_versionamento = false;
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = tx.prepareStatement(query);
            rs = st.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                boolean tab = rs.getBoolean("tab");
                String link = rs.getString("link");
                Integer order = null;
                if (rs.getObject("order") != null) {
                    order = rs.getInt("order");
                }
                String msg1 = Util.getLocalizedString(Defs.MDLIBI18N, getLocale(), "Wrong_name_value_for_object_operation_");
                String msg2 = Util.getLocalizedString(Defs.MDLIBI18N, getLocale(), "Wrong_tab_value_for_object_operation_");
                String msg3 = Util.getLocalizedString(Defs.MDLIBI18N, getLocale(), "Wrong_link_value_for_object_operation_");
                String msg4 = Util.getLocalizedString(Defs.MDLIBI18N, getLocale(), "Wrong_order_value_for_object_operation_");
                switch(id) {
                    case MDOperation.VERSIONING:
                        t_versionamento = true;
                        Testing.assertEquals(errors, msg1 + id + ": ", "versioning", name, locale);
                        Testing.assertFalse(errors, msg2 + id + " - versioning: ", tab);
                        Testing.assertEquals(errors, msg3 + id + " - versioning: ", "Versioning", link, locale);
                        Testing.assertEquals(errors, msg4 + id + " - versioning: ", new Integer(2), order, locale);
                        break;
                    default:
                        String msg = MessageFormat.format(Util.getLocalizedString(Defs.MDLIBI18N, getLocale(), "Unknown_Global_operation"), id, name);
                        errors.add(msg);
                }
            }
        } catch (Exception e) {
            errors.add(e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
            } catch (Exception e) {
            }
            try {
                if (st != null) st.close();
            } catch (Exception e) {
            }
        }
        String msg = Util.getLocalizedString(Defs.MDLIBI18N, getLocale(), "Missing_global_operation_");
        Testing.assertTrue(errors, msg + "versioning", t_versionamento);
    }

    private void selfTestModelClassOps(Transaction tx, Collection<String> errors) {
        String query = MessageFormat.format("SELECT * FROM \"{0}\".\"{1}_view\"", getSchema2(), Defs.CLSOP_CLS);
        boolean t_utenti = false;
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = tx.prepareStatement(query);
            rs = st.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                Integer clsId = null;
                if (rs.getObject("class") != null) {
                    clsId = rs.getInt("class");
                }
                String link = rs.getString("link");
                Integer order = null;
                if (rs.getObject("order") != null) {
                    order = rs.getInt("order");
                }
                String msg1 = Util.getLocalizedString(Defs.MDLIBI18N, getLocale(), "Wrong_name_value_for_object_operation_");
                String msg2 = Util.getLocalizedString(Defs.MDLIBI18N, getLocale(), "Wrong_tab_value_for_object_operation_");
                String msg3 = Util.getLocalizedString(Defs.MDLIBI18N, getLocale(), "Wrong_link_value_for_object_operation_");
                String msg4 = Util.getLocalizedString(Defs.MDLIBI18N, getLocale(), "Wrong_order_value_for_object_operation_");
                switch(id) {
                    case MDOperation.CREATE_USER:
                        t_utenti = true;
                        Testing.assertEquals(errors, msg1 + id + ": ", "create user management", name, locale);
                        Testing.assertEquals(errors, msg2 + id + " - create user management: ", 156, clsId.intValue(), locale);
                        Testing.assertEquals(errors, msg3 + id + " - create user management: ", "CreateUserTables", link, locale);
                        Testing.assertEquals(errors, msg4 + id + " - create user management: ", new Integer(1), order, locale);
                        break;
                    default:
                        String msg = MessageFormat.format(Util.getLocalizedString(Defs.MDLIBI18N, getLocale(), "Unknown_Global_operation"), id, name);
                        errors.add(msg);
                }
            }
        } catch (Exception e) {
            errors.add(e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
            } catch (Exception e) {
            }
            try {
                if (st != null) st.close();
            } catch (Exception e) {
            }
        }
        String msg = Util.getLocalizedString(Defs.MDLIBI18N, getLocale(), "Missing_global_operation_");
        Testing.assertTrue(errors, msg + "create user management", t_utenti);
    }

    private void selfTestModelUsers(Transaction tx, Collection<String> errors) {
        String query = MessageFormat.format("SELECT * FROM \"{0}\".\"{1}\"", getSchema1(), Defs.USER_CLS);
        boolean t_anonimo = false;
        boolean t_utente = false;
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = tx.prepareStatement(query);
            rs = st.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String login = null;
                if (rs.getObject("login") != null) {
                    login = rs.getString("login");
                }
                String email = null;
                if (rs.getObject("email") != null) {
                    email = rs.getString("email");
                }
                String password = null;
                if (rs.getObject("password") != null) {
                    password = rs.getString("password");
                }
                String msg1 = Util.getLocalizedString(Defs.MDLIBI18N, getLocale(), "Wrong_login_value_for_user_");
                String msg2 = Util.getLocalizedString(Defs.MDLIBI18N, getLocale(), "Wrong_password_value_for_user_");
                String msg3 = Util.getLocalizedString(Defs.MDLIBI18N, getLocale(), "Wrong_email_value_for_user_");
                switch(id) {
                    case 0:
                        t_anonimo = true;
                        Testing.assertEquals(errors, msg1 + id + ": ", "anonymous", login, getLocale());
                        Testing.assertNull(errors, msg2 + id + " - anonymous", password);
                        Testing.assertNull(errors, msg3 + id + " - anonymous", email);
                        break;
                    case 1:
                        t_utente = true;
                        Testing.assertEquals(errors, msg1 + id + ": ", "admin", login, locale);
                        Testing.assertNotNull(errors, msg2 + id + " - admin", password);
                        break;
                    default:
                }
            }
        } catch (Exception e) {
            errors.add(e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
            } catch (Exception e) {
            }
            try {
                if (st != null) st.close();
            } catch (Exception e) {
            }
        }
        String msg = Util.getLocalizedString(Defs.MDLIBI18N, getLocale(), "Missing_user");
        Testing.assertTrue(errors, msg + "0 - anonymous", t_anonimo);
        Testing.assertTrue(errors, msg + "1 - user", t_utente);
    }

    private void selfTestModelUserGroups(Transaction tx, Collection<String> errors) {
        String query = MessageFormat.format("SELECT * FROM \"{0}\".\"{1}\"", getSchema1(), Defs.USERGRP_CLS);
        boolean t_userAdmins = false;
        boolean t_modelers = false;
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = tx.prepareStatement(query);
            rs = st.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = null;
                if (rs.getObject("name") != null) {
                    name = rs.getString("name");
                }
                String msg1 = Util.getLocalizedString(Defs.MDLIBI18N, getLocale(), "Wrong_name_value_for_user_group_");
                switch(id) {
                    case 2:
                        t_userAdmins = true;
                        Testing.assertEquals(errors, msg1 + id + ": ", "User administrators", name, getLocale());
                        break;
                    case 1:
                        t_modelers = true;
                        Testing.assertEquals(errors, msg1 + id + ": ", "Modelers", name, locale);
                        break;
                    default:
                }
            }
        } catch (Exception e) {
            errors.add(e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
            } catch (Exception e) {
            }
            try {
                if (st != null) st.close();
            } catch (Exception e) {
            }
        }
        String msg = Util.getLocalizedString(Defs.MDLIBI18N, getLocale(), "Missing_user_group");
        Testing.assertTrue(errors, msg + "1 - Modelers", t_modelers);
        Testing.assertTrue(errors, msg + "2 - User administrators", t_userAdmins);
    }

    private void selfTestFilterOps(final Transaction tx, Collection<String> errors) {
        String query = MessageFormat.format("SELECT * FROM \"{0}\".\"{1}\"", getSchema2(), Defs.FILTEROP_CLS);
        boolean t_eq = false;
        boolean t_ge = false;
        boolean t_le = false;
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = tx.prepareStatement(query);
            rs = st.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String msg = Util.getLocalizedString(Defs.MDLIBI18N, getLocale(), "Name_value_incorrect_for_filter_operator_");
                switch(id) {
                    case MDFilterOperator.EQ:
                        t_eq = true;
                        Testing.assertEquals(errors, msg + id + ": ", "=", name, locale);
                        break;
                    case MDFilterOperator.GE:
                        t_ge = true;
                        Testing.assertEquals(errors, msg + id + ": ", ">=", name, locale);
                        break;
                    case MDFilterOperator.LE:
                        t_le = true;
                        Testing.assertEquals(errors, msg + id + ": ", "<=", name, locale);
                        break;
                    default:
                        msg = MessageFormat.format(Util.getLocalizedString(Defs.MDLIBI18N, getLocale(), "Unknown_Filter_operation"), id, name);
                        errors.add(msg);
                }
            }
        } catch (Exception e) {
            errors.add(e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
            } catch (Exception e) {
            }
            try {
                if (st != null) st.close();
            } catch (Exception e) {
            }
        }
        String msg = Util.getLocalizedString(Defs.MDLIBI18N, getLocale(), "Missing_filter_operation_");
        Testing.assertTrue(errors, msg + "=", t_eq);
        Testing.assertTrue(errors, msg + ">=", t_ge);
        Testing.assertTrue(errors, msg + "<=", t_le);
    }

    private void selfTest1DPrptTypes(final Transaction tx, Collection<String> errors) {
        String query = MessageFormat.format("SELECT * FROM \"{0}\".\"{1}\"", getSchema2(), Defs.PRPT1D_TYPE_CLS);
        boolean t_table = false;
        boolean t_pieChart = false;
        boolean t_pieChart3d = false;
        boolean t_ringChart = false;
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = tx.prepareStatement(query);
            rs = st.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String msg = Util.getLocalizedString(Defs.MDLIBI18N, getLocale(), "Name_value_incorrect_for_1D_portlet_");
                switch(id) {
                    case MD1DPortletReportType.TABLE:
                        t_table = true;
                        Testing.assertEquals(errors, msg + id + ": ", "table", name, locale);
                        break;
                    case MD1DPortletReportType.PIE_CHART:
                        t_pieChart = true;
                        Testing.assertEquals(errors, msg + id + ": ", "pie chart", name, locale);
                        break;
                    case MD1DPortletReportType.PIE_CHART_3D:
                        t_pieChart3d = true;
                        Testing.assertEquals(errors, msg + id + ": ", "pie chart 3d", name, locale);
                        break;
                    case MD1DPortletReportType.RING_CHART:
                        t_ringChart = true;
                        Testing.assertEquals(errors, msg + id + ": ", "ring chart", name, locale);
                        break;
                    default:
                        msg = MessageFormat.format(Util.getLocalizedString(Defs.MDLIBI18N, getLocale(), "Unknown_portlet_1D"), id, name);
                        errors.add(msg);
                }
            }
        } catch (Exception e) {
            errors.add(e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
            } catch (Exception e) {
            }
            try {
                if (st != null) st.close();
            } catch (Exception e) {
            }
        }
        String msg = Util.getLocalizedString(Defs.MDLIBI18N, getLocale(), "Missing_1D_portlet_");
        Testing.assertTrue(errors, msg + "table", t_table);
        Testing.assertTrue(errors, msg + "tree", t_pieChart);
        Testing.assertTrue(errors, msg + "area chart", t_pieChart3d);
        Testing.assertTrue(errors, msg + "bar chart", t_ringChart);
    }

    private void selfTest2DPrptTypes(final Transaction tx, Collection<String> errors) {
        String query = MessageFormat.format("SELECT * FROM \"{0}\".\"{1}\"", getSchema2(), Defs.PRPT2D_TYPE_CLS);
        boolean t_table = false;
        boolean t_tree = false;
        boolean t_areaChart = false;
        boolean t_barChart = false;
        boolean t_barChart3d = false;
        boolean t_stackedBarChart = false;
        boolean t_stackedBarChart3d = false;
        boolean t_lineChart = false;
        boolean t_lineChart3d = false;
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = tx.prepareStatement(query);
            rs = st.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String msg = Util.getLocalizedString(Defs.MDLIBI18N, getLocale(), "name_value_incorrect_for_2D_portlet_");
                switch(id) {
                    case MD2DPortletReportType.TABLE:
                        t_table = true;
                        Testing.assertEquals(errors, msg + id + ": ", "table", name, locale);
                        break;
                    case MD2DPortletReportType.TREE:
                        t_tree = true;
                        Testing.assertEquals(errors, msg + id + ": ", "tree", name, locale);
                        break;
                    case MD2DPortletReportType.AREA_CHART:
                        t_areaChart = true;
                        Testing.assertEquals(errors, msg + id + ": ", "area chart", name, locale);
                        break;
                    case MD2DPortletReportType.BAR_CHART:
                        t_barChart = true;
                        Testing.assertEquals(errors, msg + id + ": ", "bar chart", name, locale);
                        break;
                    case MD2DPortletReportType.BAR_CHART_3D:
                        t_barChart3d = true;
                        Testing.assertEquals(errors, msg + id + ": ", "bar chart 3d", name, locale);
                        break;
                    case MD2DPortletReportType.STACKED_BAR_CHART:
                        t_stackedBarChart = true;
                        Testing.assertEquals(errors, msg + id + ": ", "stacked bar chart", name, locale);
                        break;
                    case MD2DPortletReportType.STACKED_BAR_CHART_3D:
                        t_stackedBarChart3d = true;
                        Testing.assertEquals(errors, msg + id + ": ", "stacked bar chart 3d", name, locale);
                        break;
                    case MD2DPortletReportType.LINE_CHART:
                        t_lineChart = true;
                        Testing.assertEquals(errors, msg + id + ": ", "line chart", name, locale);
                        break;
                    case MD2DPortletReportType.LINE_CHART_3D:
                        t_lineChart3d = true;
                        Testing.assertEquals(errors, msg + id + ": ", "line chart 3d", name, locale);
                        break;
                    default:
                        msg = MessageFormat.format(Util.getLocalizedString(Defs.MDLIBI18N, getLocale(), "Unknown_portlet_2D"), id, name);
                        errors.add(msg);
                }
            }
        } catch (Exception e) {
            errors.add(e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
            } catch (Exception e) {
            }
            try {
                if (st != null) st.close();
            } catch (Exception e) {
            }
        }
        String msg = Util.getLocalizedString(Defs.MDLIBI18N, getLocale(), "Missing_2D_portlet_");
        Testing.assertTrue(errors, msg + "table", t_table);
        Testing.assertTrue(errors, msg + "tree", t_tree);
        Testing.assertTrue(errors, msg + "area chart", t_areaChart);
        Testing.assertTrue(errors, msg + "bar chart", t_barChart);
        Testing.assertTrue(errors, msg + "bar chart 3d", t_barChart3d);
        Testing.assertTrue(errors, msg + "stacked bar chart", t_stackedBarChart);
        Testing.assertTrue(errors, msg + "stacked bar chart 3d", t_stackedBarChart3d);
        Testing.assertTrue(errors, msg + "line chart", t_lineChart);
        Testing.assertTrue(errors, msg + "line chart 3d", t_lineChart3d);
    }

    private void selfTestOrientation(final Transaction tx, Collection<String> errors) {
        String query = MessageFormat.format("SELECT \"id\",\"name\" FROM \"{0}\".\"{1}\"", getSchema2(), Defs.ORIENTATION_CLS);
        boolean horizontal = false;
        boolean vertical = false;
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = tx.prepareStatement(query);
            rs = st.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                switch(id) {
                    case MD2DPortletReport.ORIENTATION_HORIZONTAL:
                        horizontal = true;
                        Testing.assertEquals(errors, Util.getLocalizedString(Defs.MDLIBI18N, getLocale(), "Wrong_name_value_for_horizontal_orientation"), "horizontal", name, locale);
                        break;
                    case MD2DPortletReport.ORIENTATION_VERTICAL:
                        vertical = true;
                        Testing.assertEquals(errors, Util.getLocalizedString(Defs.MDLIBI18N, getLocale(), "Wrong_name_value_for_vertical_orientation"), "vertical", name, locale);
                        break;
                    default:
                        String msg = MessageFormat.format(Util.getLocalizedString(Defs.MDLIBI18N, getLocale(), "Unknown_orientation"), id, name);
                        errors.add(msg);
                }
            }
        } catch (Exception e) {
            errors.add(e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
            } catch (Exception e) {
            }
            try {
                if (st != null) st.close();
            } catch (Exception e) {
            }
        }
        Testing.assertTrue(errors, Util.getLocalizedString(Defs.MDLIBI18N, getLocale(), "Missing_horizontal_orientation"), horizontal);
        Testing.assertTrue(errors, Util.getLocalizedString(Defs.MDLIBI18N, getLocale(), "Missing_vertical_orientation"), vertical);
    }

    private void selfTestPathSegments(Transaction tx, Collection<String> errors) {
        PreparedStatement st = null;
        ResultSet rs = null;
        String query = MessageFormat.format("SELECT uras.\"id\", ra.\"name\"" + " FROM \"{0}\".\"{1}_view\" uras" + " JOIN \"{0}\".\"{2}_view\" ra" + " ON ra.\"id\" = uras.\"rel. attribute\"" + " WHERE ra.\"class\" <> uras.\"class\"" + " OR ra.\"opp. end class\" NOT IN (" + " SELECT inup.\"id\"" + " FROM \"{0}\".\"{3}\" pe" + " JOIN \"{0}\".\"parent class_up\" inup" + " ON inup.\"refid\" = pe.\"class\"" + " WHERE pe.\"id\" = uras.\"previous el.\"" + " )", getSchema2(), Defs.UPRELSEG_CLS, Defs.RELATTR_CLS, Defs.PATHEL_CLS);
        try {
            st = tx.prepareStatement(query);
            rs = st.executeQuery();
            if (rs.next()) {
                String msg = MessageFormat.format(Util.getLocalizedString(Defs.MDLIBI18N, getLocale(), "Segment_incoherent"), "UpRelSegment", rs.getInt("id"), "attribute", rs.getString("name"));
                errors.add(msg);
            }
        } catch (Exception e) {
            errors.add(e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
            } catch (Exception e) {
            }
            try {
                if (st != null) st.close();
            } catch (Exception e) {
            }
        }
        query = MessageFormat.format("SELECT dras.\"id\", ra.\"name\"" + " FROM \"{0}\".\"{1}_view\" dras" + " JOIN \"{0}\".\"{2}_view\" ra" + " ON ra.\"id\" = dras.\"rel. attribute\"" + " WHERE ra.\"opp. end class\" <> dras.\"class\"" + " OR ra.\"class\" NOT IN (" + " SELECT inup.\"id\"" + " FROM \"{0}\".\"{3}\" pe" + " JOIN \"{0}\".\"parent class_up\" inup" + " ON inup.\"refid\" = pe.\"class\"" + " WHERE pe.\"id\" = dras.\"previous el.\"" + " )", getSchema2(), Defs.DNRELSEG_CLS, Defs.RELATTR_CLS, Defs.PATHEL_CLS);
        try {
            st = tx.prepareStatement(query);
            rs = st.executeQuery();
            if (rs.next()) {
                String msg = MessageFormat.format(Util.getLocalizedString(Defs.MDLIBI18N, getLocale(), "Segment_incoherent"), "DownRelSegment", rs.getInt("id"), "attribute", rs.getString("name"));
                errors.add(msg);
            }
        } catch (Exception e) {
            errors.add(e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
            } catch (Exception e) {
            }
            try {
                if (st != null) st.close();
            } catch (Exception e) {
            }
        }
    }

    public void versionTest() throws Exception {
        Transaction tx = getCurrentTransaction();
        String verMetaWar = Defs.META_VERSION;
        String sql = MessageFormat.format("SELECT \"{2}\" FROM \"{0}\".\"{1}\"", getSchema2(), Escape.dbSchemaEscape(Defs.VERNUM), Escape.dbSchemaEscape(Defs.VERNUM_VER));
        ResultSet rs = null;
        PreparedStatement st = null;
        try {
            st = tx.prepareStatement(sql);
            rs = st.executeQuery();
            if (!rs.next()) {
                String msg = MessageFormat.format(Util.getLocalizedString(Defs.MDLIBI18N, getLocale(), "Disappeared_table"), Defs.VERNUM);
                throw new Exception(msg);
            }
            String verMetaDB = rs.getString(Defs.VERNUM_VER);
            if (verMetaDB == null) throw new Exception(" $version db null ");
            if (!verMetaDB.equals(verMetaWar)) {
                String msg = MessageFormat.format(Util.getLocalizedString(Defs.MDLIBI18N, getLocale(), "WAR_file_version_doesnt_correspond_to_database_version"), verMetaWar, verMetaDB);
                throw new Exception(msg);
            }
        } catch (Exception e) {
            throw new Exception(" Version error : " + e);
        } finally {
            try {
                if (rs != null) rs.close();
            } catch (Exception e) {
            }
            try {
                if (st != null) st.close();
            } catch (Exception e) {
            }
        }
    }

    public MDClass getMDClassById(int id) throws Exception {
        MDClass result = classIdCache.get(id);
        if (result == null) {
            String msg = MessageFormat.format(Util.getLocalizedString(Defs.MDLIBI18N, getLocale(), "element_not_exist"), "Class", id);
            throw new MDClassNotFoundException(msg);
        }
        return result;
    }

    public MDAttribute getMDAttributeById(int id) throws Exception {
        MDAttribute result = attrIdCache.get(id);
        if (result == null) {
            String msg = MessageFormat.format(Util.getLocalizedString(Defs.MDLIBI18N, getLocale(), "element_not_exist"), "Attribute", id);
            throw new MDClassNotFoundException(msg);
        }
        return result;
    }

    public MDClass getMDClassByName(String name) throws Exception {
        MDClass result = classNameCache.get(name);
        if (result == null) {
            String msg = MessageFormat.format(Util.getLocalizedString(Defs.MDLIBI18N, getLocale(), "element_not_exist"), "Class", name);
            throw new MDClassNotFoundException(msg);
        }
        return result;
    }

    public Collection<MDClass> getAllMDClasses() {
        return classIdCache.values();
    }

    public Collection<MDAttribute> getAllAttributes() {
        return attrIdCache.values();
    }

    public MDPortlet getPortletById(int id) throws Exception {
        MDPortlet result = portletIdCache.get(id);
        if (result == null) {
            String msg = MessageFormat.format(Util.getLocalizedString(Defs.MDLIBI18N, getLocale(), "element_not_exist"), "Portlet", id);
            throw new Exception(msg);
        }
        return result;
    }

    public MDJasperReports getJasperReportsById(int id) throws Exception {
        MDJasperReports result = jasperReportsCache.get(id);
        if (result == null) {
            String msg = MessageFormat.format(Util.getLocalizedString(Defs.MDLIBI18N, getLocale(), "element_not_exist"), "JasperReports", id);
            throw new Exception(msg);
        }
        return result;
    }

    public Collection<MDMetaUserGroup> getAllMetaUserGroups() {
        return mugIdCache.values();
    }

    public MDWfAction getWfActionById(int id) throws Exception {
        MDWfAction result = wfActionIdCache.get(id);
        if (result == null) {
            String msg = MessageFormat.format(Util.getLocalizedString(Defs.MDLIBI18N, getLocale(), "element_not_exist"), "Action", id);
            throw new Exception(msg);
        }
        return result;
    }

    public MDWfState getWfStateById(int id) throws Exception {
        MDWfState result = wfStateIdCache.get(id);
        if (result == null) {
            String msg = MessageFormat.format(Util.getLocalizedString(Defs.MDLIBI18N, getLocale(), "element_not_exist"), "Workflow state", id);
            throw new Exception(msg);
        }
        return result;
    }

    public MDWfTransition getWfTransitionById(int id) throws Exception {
        MDWfTransition result = wfTransitionIdCache.get(id);
        if (result == null) {
            String msg = MessageFormat.format(Util.getLocalizedString(Defs.MDLIBI18N, getLocale(), "element_not_exist"), "Workflow transition", id);
            throw new Exception(msg);
        }
        return result;
    }

    public MDAttribute getAttributeById(int id) throws Exception {
        MDAttribute result = attrIdCache.get(id);
        if (result == null) {
            String msg = MessageFormat.format(Util.getLocalizedString(Defs.MDLIBI18N, getLocale(), "element_not_exist"), "Attribute", id);
            throw new Exception(msg);
        }
        return result;
    }

    public MDPathElement getPathElementById(int id) throws Exception {
        MDPathElement result = pathElementIdCache.get(id);
        if (result == null) {
            String msg = MessageFormat.format(Util.getLocalizedString(Defs.MDLIBI18N, getLocale(), "element_not_exist"), "Path element", id);
            throw new Exception(msg);
        }
        return result;
    }

    public MDObjectOperation getObjectOperationById(int id) throws Exception {
        MDObjectOperation result = objectOpIdCache.get(id);
        if (result == null) {
            String msg = MessageFormat.format(Util.getLocalizedString(Defs.MDLIBI18N, getLocale(), "element_not_exist"), "Object operation", id);
            throw new Exception(msg);
        }
        return result;
    }

    public MDObjectSetOperation getObjectSetOperationById(int id) throws Exception {
        MDObjectSetOperation result = objectSetOpIdCache.get(id);
        if (result == null) {
            String msg = MessageFormat.format(Util.getLocalizedString(Defs.MDLIBI18N, getLocale(), "element_not_exist"), "Object set operation", id);
            throw new Exception(msg);
        }
        return result;
    }

    public MDClassOperation getClassOperationById(int id) throws Exception {
        MDClassOperation result = classOpIdCache.get(id);
        if (result == null) {
            String msg = MessageFormat.format(Util.getLocalizedString(Defs.MDLIBI18N, getLocale(), "element_not_exist"), "Operation on class", id);
            throw new Exception(msg);
        }
        return result;
    }

    public Collection<MDObjectOperation> getAllObjectOperations() {
        return objectOpIdCache.values();
    }

    public Collection<MDClassOperation> getAllClassOperations() {
        return classOpIdCache.values();
    }

    public Collection<MDGlobalOperation> getAllGlobalOperations() {
        return globalOpIdCache.values();
    }

    public Set<MDObjectOperation> getAllObjectOperationsSortedByOrder() {
        return objectOperationSortedByOrder;
    }

    public Set<MDObjectSetOperation> getAllObjectSetOperationsSortedByOrder() {
        return objectSetOperationSortedByOrder;
    }

    public Set<MDClassOperation> getAllClassOperationsSortedByOrder() {
        return classOperationSortedByOrder;
    }

    public Set<MDGlobalOperation> getAllGlobalOperationsSortedByOrder() {
        return globalOperationSortedByOrder;
    }

    public MDGlobalOperation getGlobalOperationById(int id) throws Exception {
        MDGlobalOperation result = globalOpIdCache.get(id);
        if (result == null) {
            String msg = MessageFormat.format(Util.getLocalizedString(Defs.MDLIBI18N, getLocale(), "element_not_exist"), "Global operation", id);
            throw new Exception(msg);
        }
        return result;
    }

    public Collection<MDFuncCalcType> getAllFuncCalcs() {
        return funcCalcTypeNameCache.values();
    }

    public MDMetaUserGroup getMetaUserGroupById(int id) throws Exception {
        MDMetaUserGroup result = mugIdCache.get(id);
        if (result == null) {
            String msg = MessageFormat.format(Util.getLocalizedString(Defs.MDLIBI18N, getLocale(), "element_not_exist"), "MetaUserGroup", id);
            throw new Exception(msg);
        }
        return result;
    }

    public MDFuncCalcType getFuncCalcTypeByName(String name) throws Exception {
        MDFuncCalcType result = funcCalcTypeNameCache.get(name);
        if (result == null) {
            String msg = MessageFormat.format(Util.getLocalizedString(Defs.MDLIBI18N, getLocale(), "element_not_exist"), "FuncCalcType", name);
            throw new Exception(msg);
        }
        return result;
    }

    public Collection<MDPortlet> getPortletsOnHomepage() {
        return portletsOnHomePage;
    }

    public void delete(Collection<MDObject> objs, Map<MDObject, MDRelAttribute> relAttrForUpdate) throws Exception {
        Transaction tx = getCurrentTransaction();
        for (Entry<MDObject, MDRelAttribute> current : relAttrForUpdate.entrySet()) {
            MDObject y = current.getKey();
            MDRelAttribute relAttr = current.getValue();
            y.setRelAttribute(relAttr, null);
        }
        tx.sync();
        DatabaseAbstraction mdDataBase = getCurrentTransaction().getMDDataBase();
        for (MDObject obj : objs) {
            mdDataBase.objMarkForDeletion(obj);
        }
        tx.sync();
    }

    public List<MDObject> propagateForDelete(List<MDObject> coseDaFare, Map<MDObject, MDRelAttribute> relAttrForUpdate) throws Exception {
        List<MDObject> coseFatte = new ArrayList<MDObject>();
        DependenceGraphForDelete graph = new DependenceGraphForDelete();
        while (!coseDaFare.isEmpty()) {
            MDObject x = coseDaFare.remove(0);
            coseFatte.add(x);
            List<MDObject> dipendenze = propagateForDelete2(x, relAttrForUpdate, graph);
            for (MDObject y : dipendenze) {
                if (!coseDaFare.contains(y) && !coseFatte.contains(y)) coseDaFare.add(y);
            }
        }
        return graph.sort();
    }

    private List<MDObject> propagateForDelete2(MDObject obj, Map<MDObject, MDRelAttribute> relAttrForUpdate, DependenceGraphForDelete graph) throws Exception {
        List<MDObject> result = new ArrayList<MDObject>();
        loadRelAttribute(result, relAttrForUpdate, obj, graph);
        return (result);
    }

    private void loadRelAttribute(List<MDObject> result, Map<MDObject, MDRelAttribute> relAttrForUpdate, MDObject obj, DependenceGraphForDelete graph) throws Exception {
        Transaction tx = getCurrentTransaction();
        boolean dependenceFound = false;
        MDClass cls = obj.getActualClass();
        for (MDRelAttribute relAttr : cls.getRelAttributes()) {
            MDClass relAttrOwnerCls = relAttr.getOwnerClass();
            Query query = relAttr.getRelatedObjectsQuery(obj.getId());
            PreparedStatement st = null;
            ResultSet rs = null;
            try {
                st = query.prepareStatement(tx);
                st.clearParameters();
                st.setInt(1, obj.getId());
                rs = st.executeQuery();
                while (rs.next()) {
                    MDObject y = relAttrOwnerCls.getMDObject(rs.getInt("id"));
                    if (relAttr.isRequired() || relAttr.isImmutable()) {
                        dependenceFound = true;
                        graph.addLink(y, obj);
                        result.add(y);
                    } else {
                        relAttrForUpdate.put(y, relAttr);
                    }
                }
            } finally {
                try {
                    if (rs != null) rs.close();
                } catch (Exception e) {
                }
                try {
                    if (st != null) st.close();
                } catch (Exception e) {
                }
            }
        }
        if (!dependenceFound) graph.addVertex(obj);
    }

    public QueryCache getQueryCache() {
        return queryCache;
    }

    public UserQueryCache getUserQueryCache() {
        return userQueryCache;
    }

    public BlobTokenCache getBlobTokenCache() {
        return blobTokenCache;
    }

    public PortletCache getPortletCache() {
        return portletCache;
    }

    public Set<MDClass> getAllMDClassesSortedByOrder() {
        return classesSortedByOrder;
    }

    public Set<MDClass> getAllMDClassesSortedByPrettyName() {
        return classesSortedByPrettyName;
    }

    public Set<MDClass> getAllMDClassesSortedByPrettyPlural() {
        return classesSortedByPrettyPlural;
    }

    public Collection<MDPortlet> getAllPortlets() {
        return portletIdCache.values();
    }

    public Collection<MDJasperReports> getAllJasperReports() {
        return jasperReportsCache.values();
    }

    public Navigation getNavigation() {
        return navigation;
    }

    public synchronized int getNextBlobId() {
        maxBlobId++;
        return maxBlobId;
    }

    public boolean isLoginEnabled() {
        return doUser;
    }

    public void writeStartTransactionDDL() throws Exception {
        String cvsModule = getMDCvsModule();
        if (mdCvsWorkingDirectory == null || "".equals(mdCvsWorkingDirectory) || cvsModule == null || "".equals(cvsModule)) {
            return;
        }
        OutputStreamWriter log = null;
        try {
            String path = mdCvsWorkingDirectory + File.separatorChar + cvsModule + File.separatorChar;
            log = new OutputStreamWriter(new FileOutputStream(path + Defs.CVS_PORTOFINODDL, true), Defs.ENCODING_FILE);
            log.write(Defs.START_TRANSACTION + ";\n");
        } finally {
            if (log != null) try {
                log.close();
            } catch (Exception ignore) {
            }
        }
    }

    public void writeCommitDDL() throws Exception {
        String cvsModule = getMDCvsModule();
        if (mdCvsWorkingDirectory == null || "".equals(mdCvsWorkingDirectory) || cvsModule == null || "".equals(cvsModule)) {
            return;
        }
        OutputStreamWriter log = null;
        try {
            String path = mdCvsWorkingDirectory + File.separatorChar + cvsModule + File.separatorChar;
            log = new OutputStreamWriter(new FileOutputStream(path + Defs.CVS_PORTOFINODDL, true), Defs.ENCODING_FILE);
            log.write(Defs.COMMIT_TRANSACTION + ";\n");
        } finally {
            if (log != null) try {
                log.close();
            } catch (Exception ignore) {
            }
        }
    }

    public User getCurrentUser() {
        return threadLocals.getCurrentUser();
    }

    public void setCurrentUser(User user) {
        threadLocals.setCurrentUser(user);
    }

    public Transaction getCurrentTransaction() {
        return configContainer.getCurrentTransaction();
    }

    @Override
    public String toString() {
        return "\"" + getName() + "\"";
    }

    public void visit(MDConfigVisitor visitor) {
        visitor.doConfigPre(this);
        visitor.doMetaUserGroupListPre();
        for (MDMetaUserGroup mug : mugIdCache.values()) {
            mug.visit(visitor);
        }
        visitor.doMetaUserGroupListPost();
        visitor.doFuncCalcTypeListPre();
        for (MDFuncCalcType funcCalcType : funcCalcTypeNameCache.values()) {
            visitor.doFuncCalcType(funcCalcType);
        }
        visitor.doFuncCalcTypeListPost();
        visitor.doClassListPre();
        for (MDClass cls : classIdCache.values()) {
            cls.visit(visitor);
        }
        visitor.doClassListPost();
        visitor.doOperationListPre();
        for (MDObjectOperation op : objectOpIdCache.values()) {
            visitor.doObjectOperation(op);
        }
        for (MDGlobalOperation op : globalOpIdCache.values()) {
            op.visit(visitor);
        }
        for (MDClassOperation op : classOpIdCache.values()) {
            op.visit(visitor);
        }
        for (MDObjectSetOperation op : objectSetOpIdCache.values()) {
            op.visit(visitor);
        }
        visitor.doOperationListPost();
        visitor.doPortletListPre();
        for (MDPortlet portlet : portletIdCache.values()) {
            portlet.visit(visitor);
        }
        visitor.doPortletListPost();
        visitor.doJasperReportsListPre();
        for (MDJasperReports jrpt : jasperReportsCache.values()) {
            jrpt.visit(visitor);
        }
        visitor.doJasperReportsListPost();
        visitor.doConfigPost();
    }
}
