package org.eaasyst.eaa.apps;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.ActionForm;
import org.apache.struts.util.MessageResources;
import org.eaasyst.eaa.Constants;
import org.eaasyst.eaa.data.DataAccessBeanFactory;
import org.eaasyst.eaa.data.DataConnector;
import org.eaasyst.eaa.forms.DynaSearchForm;
import org.eaasyst.eaa.security.UserProfileManager;
import org.eaasyst.eaa.syst.EaasyStreet;
import org.eaasyst.eaa.syst.data.persistent.SavedSearch;
import org.eaasyst.eaa.syst.data.persistent.SearchDefinition;
import org.eaasyst.eaa.syst.data.persistent.SearchFieldDefinition;
import org.eaasyst.eaa.syst.data.transients.Event;
import org.eaasyst.eaa.syst.data.transients.SearchFilter;
import org.eaasyst.eaa.syst.data.transients.SearchFormSearchField;
import org.eaasyst.eaa.syst.data.transients.SearchFormSortField;
import org.eaasyst.eaa.syst.data.transients.SearchSpecification;
import org.eaasyst.eaa.utils.StringUtils;
import org.eaasyst.search.data.impl.SavedSearchDabFactory;
import org.eaasyst.search.data.impl.SearchDefinitionDabFactory;

/**
 * <p>This abstract class is the base class for all EaasyStreet dynamic
 * "search" applications. A dynamic search application is an application
 * that presents an input form for the purposes of collecting
 * database search criteria.  The search / sort critera can then be
 * used to produce a list of the results or a report or both.</p>
 *
 * @version 2.9
 * @author Jeff Chilton
 * @author Peter Ruan
 */
public abstract class DynaSearchApplicationBase extends ApplicationBase {

    private static final String METHOD_IN = Constants.METHOD_IN + "DynaSearchApplicationBase(";

    private static final String METHOD_OUT = Constants.METHOD_OUT + "DynaSearchApplicationBase(";

    private static final String INIT_METHOD = ").initialize()";

    private static final String ACTION_METHOD = ").processAction()";

    private static final String OUTPUT_METHOD = ").prepareOutput()";

    private static final String TERM_METHOD = ").terminate()";

    private String searchDefinitionId = null;

    private boolean basicSearch = false;

    /**
	 * <p>Constructs a new <code>DynaSearchApplicationBase</code>.</p>
	 *
	 * @since Eaasy Street 2.1
	 */
    public DynaSearchApplicationBase() {
        super();
        setSystAction(new String[] { "Cancel" });
        setFormName("dynaSearchForm");
        setFormType("org.eaasyst.eaa.forms.DynaSearchForm");
        setViewComponent(EaasyStreet.getProperty(Constants.CFG_PAGES_SEARCH_DYNASEARCH));
        setHeaderLabelKey("label.search.header");
        setAccessBeanVersion(DataConnector.READ_COMMAND);
        setAccessBeanFactory(new SearchDefinitionDabFactory());
    }

    /**
	 * Called by the <code>Controller</code> whenever the application is
	 * invoked through either navigation or an application request.
	 *
	 * @param req the <code>HttpServletRequest</code> object
	 * @since Eaasy Street 2.3.2
	 */
    public void initialize(HttpServletRequest req) {
        EaasyStreet.logTrace(METHOD_IN + className + INIT_METHOD);
        super.initialize(req);
        EaasyStreet.logTrace("initialize(): Key = " + req.getParameter(Constants.RPK_RECORD_KEY));
        DynaSearchForm form = null;
        DataConnector dc = null;
        String user = UserProfileManager.getUserId();
        user += " (" + UserProfileManager.getUserName() + ")";
        if (req.getParameter("key") != null) {
            Map parameters = new HashMap();
            parameters.put(DataConnector.RECORD_KEY_PARAMETER, new Integer(StringUtils.intValue(req.getParameter("key"))));
            DataAccessBeanFactory savedSearchDabFactory = new SavedSearchDabFactory();
            dc = new DataConnector(savedSearchDabFactory.getAccessBean(DataConnector.READ_COMMAND));
            dc.setParameters(parameters);
            dc.setCommand(DataConnector.READ_COMMAND);
            dc.execute();
            if (dc.getResponseCode() == 0) {
                SavedSearch savedSearch = (SavedSearch) dc.getExecutionResults();
                SearchDefinition searchDefinition = (SearchDefinition) savedSearch.getSearchDefinition();
                form = new DynaSearchForm(searchDefinition, savedSearch);
                HttpSession ses = req.getSession();
                int index = getIndex(ses, true);
                ses.setAttribute(Constants.SAK_APPL_DATA + index, form);
                List authorizedActions = new ArrayList();
                if (!StringUtils.nullOrBlank(searchDefinition.getTargetApplicationText())) {
                    MessageResources resources = EaasyStreet.getApplicationResources();
                    List apps = searchDefinition.getTargetApplications();
                    Iterator i = apps.iterator();
                    while (i.hasNext()) {
                        String appName = (String) i.next();
                        if (EaasyStreet.isAuthorized(Constants.RESOURCE_TYPE_APPLICATION, appName, req)) {
                            authorizedActions.add(resources.getMessage("control.for." + appName));
                        }
                    }
                }
                if (authorizedActions.size() > 0) {
                    authorizedActions.add("Save");
                    ses.setAttribute(Constants.SAK_APPL_ACTIONS + index, authorizedActions.toArray(new String[0]));
                    EaasyStreet.handleSafeEvent(req, new Event(Constants.EAA0044N, new String[] { user, savedSearch.getId() + "", savedSearch.getName() }));
                } else {
                    req.setAttribute(Constants.RAK_SYSTEM_ACTION, Constants.SYSTEM_ACTION_BACK);
                    EaasyStreet.handleSafeEvent(req, new Event(Constants.EAA0009U, new String[] { getClassName() + "/" + searchDefinition.getIdString() }));
                }
            } else {
                req.setAttribute(Constants.RAK_SYSTEM_ACTION, Constants.SYSTEM_ACTION_BACK);
                if (dc.getResponseCode() == 1) {
                    EaasyStreet.handleSafeEvent(req, new Event(Constants.EAA0011I, new String[] { "Dynamic Search Definition" }));
                } else {
                    EaasyStreet.handleSafeEvent(req, new Event(Constants.EAA0012I, new String[] { "Dynamic Search Definition", Constants.EMPTY_STRING + dc.getResponseCode(), dc.getResponseString() }));
                }
            }
        } else if (req.getParameter("searchId") != null || req.getAttribute("searchId") != null) {
            dc = new DataConnector(getAccessBeanFactory().getAccessBean(getAccessBeanVersion()));
            dc.setParameters(getBeanParameters(req));
            dc.setCommand(DataConnector.READ_COMMAND);
            dc.execute();
            if (dc.getResponseCode() == 0) {
                SearchDefinition searchDefinition = (SearchDefinition) dc.getExecutionResults();
                if (basicSearch) {
                    form = new DynaSearchForm(searchDefinition, basicSearch);
                } else {
                    form = new DynaSearchForm(searchDefinition);
                }
                HttpSession ses = req.getSession();
                int index = getIndex(ses, true);
                ses.setAttribute(Constants.SAK_APPL_DATA + index, form);
                List authorizedActions = new ArrayList();
                if (!StringUtils.nullOrBlank(searchDefinition.getTargetApplicationText())) {
                    MessageResources resources = EaasyStreet.getApplicationResources();
                    List apps = searchDefinition.getTargetApplications();
                    Iterator i = apps.iterator();
                    while (i.hasNext()) {
                        String appName = (String) i.next();
                        if (EaasyStreet.isAuthorized(Constants.RESOURCE_TYPE_APPLICATION, appName, req)) {
                            authorizedActions.add(resources.getMessage("control.for." + appName));
                        }
                    }
                }
                if (authorizedActions.size() > 0) {
                    if (!basicSearch) {
                        authorizedActions.add("Save");
                    }
                    ses.setAttribute(Constants.SAK_APPL_ACTIONS + index, authorizedActions.toArray(new String[0]));
                    if (basicSearch) {
                        EaasyStreet.handleSafeEvent(req, new Event(Constants.EAA0042N, new String[] { user, req.getParameter("searchId") }));
                    } else {
                        EaasyStreet.handleSafeEvent(req, new Event(Constants.EAA0043N, new String[] { user, req.getParameter("searchId") }));
                    }
                } else {
                    req.setAttribute(Constants.RAK_SYSTEM_ACTION, Constants.SYSTEM_ACTION_BACK);
                    EaasyStreet.handleSafeEvent(req, new Event(Constants.EAA0009U, new String[] { getClassName() + "/" + searchDefinition.getIdString() }));
                }
            } else {
                req.setAttribute(Constants.RAK_SYSTEM_ACTION, Constants.SYSTEM_ACTION_BACK);
                if (dc.getResponseCode() == 1) {
                    EaasyStreet.handleSafeEvent(req, new Event(Constants.EAA0011I, new String[] { "Dynamic Search Definition" }));
                } else {
                    EaasyStreet.handleSafeEvent(req, new Event(Constants.EAA0012I, new String[] { "Dynamic Search Definition", Constants.EMPTY_STRING + dc.getResponseCode(), dc.getResponseString() }));
                }
            }
        }
        EaasyStreet.logTrace(METHOD_OUT + className + INIT_METHOD);
    }

    /**
	 * Called by the <code>Controller</code> to handle user action requests.
	 * 
	 * @param req
	 *            the <code>HttpServletRequest</code> object
	 * @param form
	 *            the Struts <code>ActionForm</code> object
	 * @since Eaasy Street 2.3.4
	 * @author Peter Ruan
	 */
    public void processAction(HttpServletRequest req, ActionForm actionForm) {
        EaasyStreet.logTrace(METHOD_IN + className + ACTION_METHOD);
        DynaSearchForm form = (DynaSearchForm) actionForm;
        SearchDefinition searchDefinition = form.getSearchDefinition();
        Map definedFields = new HashMap();
        if (searchDefinition.getField() != null) {
            Iterator i = searchDefinition.getField().iterator();
            while (i.hasNext()) {
                SearchFieldDefinition field = (SearchFieldDefinition) i.next();
                definedFields.put(field.getFieldName(), field);
            }
        }
        String application = null;
        String action = req.getParameter(Constants.RPK_APPLICATION_ACTION);
        if ("save".equalsIgnoreCase(action)) {
            application = "org.eaasyst.search.apps.SaveSearch";
        } else if (!StringUtils.nullOrBlank(searchDefinition.getTargetApplicationText())) {
            MessageResources resources = EaasyStreet.getApplicationResources();
            List apps = searchDefinition.getTargetApplications();
            Iterator i = apps.iterator();
            while (i.hasNext()) {
                String appName = (String) i.next();
                if (resources.getMessage("control.for." + appName).equalsIgnoreCase(action)) {
                    application = appName;
                }
            }
        }
        if (!StringUtils.nullOrBlank(application)) {
            List filters = new ArrayList();
            if (form.getSearchFields() != null) {
                int max = StringUtils.intValue(form.getSearchFieldCt());
                for (int i = 0; i < max; i++) {
                    SearchFormSearchField formField = (SearchFormSearchField) form.getSearchFields().get(i);
                    SearchFilter filter = new SearchFilter();
                    if (!StringUtils.nullOrBlank(formField.getFieldName())) {
                        filter.setFieldName(formField.getFieldName());
                        filter.setFilterType(formField.getSearchType());
                        filter.setFieldValue(new String[] { formField.getSearchValue() });
                        SearchFieldDefinition fieldDef = (SearchFieldDefinition) definedFields.get(formField.getFieldName());
                        filter.setNumericComparison("number".equalsIgnoreCase(fieldDef.getFieldType()));
                        if (StringUtils.nullOrBlank(filter.getFieldValue())) {
                            filter.setFieldValue(formField.getSelectValue());
                        }
                        filters.add(filter);
                    }
                }
            }
            List sortBy = new ArrayList();
            if (form.getSortFields() != null) {
                int max = StringUtils.intValue(form.getSortFieldCt());
                for (int i = 0; i < max; i++) {
                    SearchFormSortField sortField = (SearchFormSortField) form.getSortFields().get(i);
                    if (!StringUtils.nullOrBlank(sortField.getFieldName())) {
                        String thisSort = sortField.getFieldName();
                        if ("on".equalsIgnoreCase(sortField.getDescending())) {
                            thisSort += " desc";
                            sortField.setDescending("true");
                        } else {
                            sortField.setDescending("false");
                        }
                        sortBy.add(thisSort);
                    }
                }
            }
            if (filters.size() > 0) {
                SearchSpecification spec = new SearchSpecification();
                spec.setFilter((SearchFilter[]) filters.toArray(new SearchFilter[0]));
                spec.setSortField((String[]) sortBy.toArray(new String[0]));
                HashMap parameters = new HashMap();
                parameters.put("filter", spec);
                req.setAttribute("searchDefinition", form.getSearchDefinition());
                req.setAttribute(Constants.RAK_SEARCH_PARAMETERS, parameters);
                req.setAttribute(Constants.RAK_SYSTEM_ACTION, Constants.SYSTEM_ACTION_PUSH);
                req.setAttribute(Constants.RAK_NEXT_APPLICATION, application);
            } else {
                EaasyStreet.handleSafeEvent(req, new Event(Constants.EAA0033I));
            }
        } else {
            super.processAction(req, actionForm);
        }
        EaasyStreet.logTrace(METHOD_OUT + className + ACTION_METHOD);
    }

    /**
	 * Called by the <code>Controller</code> to obtain unformatted
	 * application results.
	 *
	 * @param req the <code>HttpServletRequest</code> object
	 * @since Eaasy Street 2.1
	 */
    public void prepareOutput(HttpServletRequest req) {
        EaasyStreet.logTrace(METHOD_IN + className + OUTPUT_METHOD);
        super.prepareOutput(req);
        HttpSession ses = req.getSession();
        int index = getIndex(ses);
        DynaSearchForm form = (DynaSearchForm) ses.getAttribute(Constants.SAK_APPL_DATA + index);
        req.setAttribute(getFormName(), form);
        req.setAttribute("searchFieldOptions", form.getSearchFieldOptions());
        req.setAttribute("sortFieldOptions", form.getSortFieldOptions());
        ses.setAttribute(Constants.SAK_INPUT_FORM, form);
        EaasyStreet.logTrace(METHOD_OUT + className + OUTPUT_METHOD);
    }

    /**
	 * Called by the <code>Controller</code> whenever the application is
	 * terminated, whether by the application itself or through a system
	 * action.
	 *
	 * @param req the <code>HttpServletRequest</code> object
	 * @since Eaasy Street 2.1
	 */
    public void terminate(HttpServletRequest req) {
        super.terminate(req);
        EaasyStreet.logTrace(METHOD_IN + className + TERM_METHOD);
        HttpSession ses = req.getSession();
        int index = getIndex(ses);
        ses.removeAttribute(Constants.SAK_APPL_DATA + index);
        EaasyStreet.logTrace(METHOD_OUT + className + TERM_METHOD);
    }

    /**
	 * Returns a HashMap object containing the required parameters for
	 * the data access bean.
	 *
	 * @param req the <code>HttpServletRequest</code> object
	 * @return the data access bean execution parameters
	 * @since Eaasy Street 2.1
	 */
    protected Map getBeanParameters(HttpServletRequest req) {
        Map parameters = new HashMap();
        if (StringUtils.nullOrBlank(searchDefinitionId)) {
            if (StringUtils.nullOrBlank(req.getParameter("searchId"))) {
                parameters.put(DataConnector.RECORD_KEY_PARAMETER, req.getAttribute("searchId"));
            } else {
                parameters.put(DataConnector.RECORD_KEY_PARAMETER, req.getParameter("searchId"));
            }
        } else {
            parameters.put(DataConnector.RECORD_KEY_PARAMETER, searchDefinitionId);
        }
        return parameters;
    }

    /**
	 * @return Returns the basicSearch.
	 */
    public boolean isBasicSearch() {
        return basicSearch;
    }

    /**
	 * @param basicSearch The basicSearch to set.
	 */
    public void setBasicSearch(boolean basicSearch) {
        this.basicSearch = basicSearch;
    }

    /**
	 * @return Returns the searchDefinitionId.
	 */
    public String getSearchDefinitionId() {
        return searchDefinitionId;
    }

    /**
	 * @param searchDefinitionId The searchDefinitionId to set.
	 */
    public void setSearchDefinitionId(String searchDefinitionId) {
        this.searchDefinitionId = searchDefinitionId;
    }
}
