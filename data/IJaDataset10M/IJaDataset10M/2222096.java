package com.sourceforge.oraclewicket.app.usermgr.mgr.standard;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.wicket.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.authorization.strategies.role.Roles;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.RefreshingView;
import org.apache.wicket.markup.repeater.util.ModelIteratorAdapter;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.util.value.ValueMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.sourceforge.oraclewicket.app.exception.NothingToDoException;
import com.sourceforge.oraclewicket.app.login.AppUserSessionBean;
import com.sourceforge.oraclewicket.markup.html.constants.PropertyFileKeys;
import com.sourceforge.oraclewicket.markup.html.constants.PropertyFileMessages;
import com.sourceforge.oraclewicket.markup.html.IntegerSelectChoice;
import com.sourceforge.oraclewicket.markup.html.SelectChoiceList;
import com.sourceforge.oraclewicket.markup.html.StandardDateLabel;
import com.sourceforge.oraclewicket.markup.html.StringSelectChoice;
import com.sourceforge.oraclewicket.markup.html.form.RecordsPerPageChoice;
import com.sourceforge.oraclewicket.markup.html.form.YesNoChoice;
import com.sourceforge.oraclewicket.markup.html.panel.PaginationPanel;
import com.sourceforge.oraclewicket.markup.html.template.page.StandardTemplatePage;
import com.sourceforge.oraclewicket.markup.html.template.panel.menu.postlogin.PostLoginMenuPanelFactory;
import com.sourceforge.oraclewicket.oracle.constants.SQLErrorCodes;
import com.sourceforge.oraclewicket.util.app.PaginationBean;

@AuthorizeInstantiation(RequiredRoles.ROLE_STANDARD_APP_USER_MGR)
public final class StandardUserMgrPage extends StandardTemplatePage {

    private static final Logger LOGGER = LoggerFactory.getLogger(StandardUserMgrPage.class);

    /** used to control whether or not data will be retrieved by the refreshing report view */
    private boolean isFirstRender = true;

    private UserMgrSearchForm searchForm;

    private UserMgrForm mgrForm;

    private Map<String, SelectChoiceList<IntegerSelectChoice>> keyValueRefData;

    /**
     * Disable every component on the page in the event of an error
     */
    private void disablePage() {
        if (searchForm != null) {
            searchForm.disable();
        }
        if (mgrForm != null) {
            mgrForm.disable();
        }
    }

    /**
     * Retrieve drop-down choice backing data
     */
    private void setSelectChoiceData() throws SQLException {
        OracleStandardUserMgrDAO dataService = null;
        try {
            final AppUserSessionBean appUserSession = (AppUserSessionBean) getSession();
            dataService = new OracleStandardUserMgrDAO(appUserSession.getUsername(), appUserSession.getPassword());
            keyValueRefData = dataService.getKeyValueRefData();
        } catch (SQLException sqle) {
            LOGGER.error("SQL Exception while retrieving list reference data -> {}; error code -> {}; sql state -> {}", new Object[] { sqle.getMessage(), sqle.getErrorCode(), sqle.getSQLState() });
            throw sqle;
        } finally {
            if (!dataService.closeConnection()) {
                final String errmsg = getLocalizer().getString(PropertyFileKeys.UNEXPECTED_ERROR, this, PropertyFileMessages.UNEXPECTED_ERROR);
                error(errmsg);
            }
        }
    }

    /**
     * Constructor
     */
    public StandardUserMgrPage() {
        setPageTitle(getLocalizer().getString(PropertyFileKeys.PAGE_TITLE, this, "Manage Standard Users"));
        SearchCriteria s = new SearchCriteria();
        add(PostLoginMenuPanelFactory.getPostLoginMenuPanel());
        try {
            setSelectChoiceData();
            if (keyValueRefData != null) {
                searchForm = new UserMgrSearchForm("standardUserMgrSearchForm", s, keyValueRefData.get("DBRL"), keyValueRefData.get("LNG"));
                mgrForm = new UserMgrForm("standardUserMgrForm", s, keyValueRefData.get("ULNG"));
                add(searchForm);
                add(mgrForm);
            }
        } catch (SQLException sqle) {
            String errmsg;
            switch(sqle.getErrorCode()) {
                case SQLErrorCodes.PERMISSION_DENIED:
                    errmsg = getLocalizer().getString(PropertyFileKeys.PERMISSION_DENIED, this, PropertyFileMessages.PERMISSION_DENIED);
                    error(errmsg);
                    break;
                default:
                    errmsg = getLocalizer().getString(PropertyFileKeys.UNEXPECTED_ERROR, this, PropertyFileMessages.UNEXPECTED_ERROR);
                    LOGGER.error("SQL Exception while retrieving standard users -> {}; error code -> {}; sql state -> {}", new Object[] { sqle.getMessage(), sqle.getErrorCode(), sqle.getSQLState() });
                    error(errmsg);
                    break;
            }
        } catch (Exception e) {
            LOGGER.error("Unexpected error while updating standard users -> class -> {} ; message -> {}", e.getClass(), e.getMessage());
            final String errmsg = getLocalizer().getString(PropertyFileKeys.UNEXPECTED_ERROR, this, PropertyFileMessages.UNEXPECTED_ERROR);
            error(errmsg);
        }
        if (searchForm == null || mgrForm == null) {
            searchForm = new UserMgrSearchForm("standardUserMgrSearchForm", s, null, null);
            mgrForm = new UserMgrForm("standardUserMgrForm", s, null);
            add(searchForm);
            add(mgrForm);
            disablePage();
        }
    }

    /**
     * Used for searching standard users
     */
    private class UserMgrSearchForm extends Form<SearchCriteria> {

        private static final long serialVersionUID = 1L;

        private SearchCriteria searchCriteria;

        private Button searchButton = new Button("searchButton");

        private static final int RECS_PER_PAGE_START = 5;

        private static final int RECS_PER_PAGE_END = 30;

        private static final int RECS_PER_PAGE_INCREMENT = 5;

        /**
         * Constructor
         */
        public UserMgrSearchForm(final String pId, final SearchCriteria pSearchCriteria, final SelectChoiceList<IntegerSelectChoice> pDbrlRefData, final SelectChoiceList<IntegerSelectChoice> pLngRefData) {
            super(pId);
            searchCriteria = pSearchCriteria;
            add(searchButton);
            add(new TextField<String>("username", new PropertyModel<String>(searchCriteria, "username")));
            add(new YesNoChoice("isAccountEnabled", new PropertyModel<StringSelectChoice>(searchCriteria, "isAccountEnabled")).setNullValid(true));
            add(new YesNoChoice("isTracingEnabled", new PropertyModel<StringSelectChoice>(searchCriteria, "isTracingEnabled")).setNullValid(true));
            add(new RecordsPerPageChoice("recordsPerPage", new PropertyModel<IntegerSelectChoice>(searchCriteria, "recordsPerPage"), RECS_PER_PAGE_START, RECS_PER_PAGE_END, RECS_PER_PAGE_INCREMENT).setNullValid(true));
            if (pDbrlRefData == null || pDbrlRefData.size() == 0) {
                add(new DropDownChoice<IntegerSelectChoice>("dbrl").setEnabled(false));
            } else {
                add(new DropDownChoice<IntegerSelectChoice>("dbrl", new PropertyModel<IntegerSelectChoice>(searchCriteria, "dbrlId"), pDbrlRefData, pDbrlRefData).setNullValid(true));
            }
            if (pLngRefData == null || pLngRefData.size() == 0) {
                add(new DropDownChoice<IntegerSelectChoice>("lng").setEnabled(false));
            } else {
                add(new DropDownChoice<IntegerSelectChoice>("lng", new PropertyModel<IntegerSelectChoice>(searchCriteria, "lngId"), pLngRefData, pLngRefData).setNullValid(true));
            }
        }

        /**
         * pressing the search button ( bypassing the record pagination links ) effectively starts a new search
         */
        @Override
        public void onSubmit() {
            searchCriteria.reset();
        }

        /**
         * Disable the form in the event of an error
         */
        public void disable() {
            setEnabled(false);
            searchButton.setVisible(false);
        }
    }

    /**
     * Manage registered users
     */
    private class UserMgrForm extends Form<StandardUserMgrBean> {

        private static final long serialVersionUID = 1L;

        private transient OracleStandardUserMgrDAO dataService;

        private PaginationPanel formPaginationPanel;

        private SearchCriteria formSearchCriteria;

        private final Button applyUserChanges = new Button("applyUserChanges");

        private final Label instructionLabel = new Label("instructionLabel", getLocalizer().getString("chooseCriteriaPrompt", this, "Please choose some criteria before searching the users"));

        private final Label resultSummary = new Label("resultSummary", getLocalizer().getString("resultSummary", this, ""));

        private final Label usernameTblHeader = new Label("usernameTblHeader", getLocalizer().getString("usernameTblHeader", this, "Username"));

        private final Label profileTblHeader = new Label("profileTblHeader", getLocalizer().getString("profileTblHeader", this, "Profile"));

        private final Label isEnabledTblHeader = new Label("isEnabledTblHeader", getLocalizer().getString("isEnabledTblHeader", this, "Enabled"));

        private final Label isTracingEnabledTblHeader = new Label("isTracingEnabledTblHeader", getLocalizer().getString("isTracingEnabledTblHeader", this, "Tracing Enabled"));

        private final Label languageTblHeader = new Label("languageTblHeader", getLocalizer().getString("languageTblHeader", this, "User Language"));

        private final Label dateCreatedTblHeader = new Label("dateCreatedTblHeader", getLocalizer().getString("dateCreatedTblHeader", this, "Date Created"));

        private final Label modRolesTblHeader = new Label("modRolesTblHeader", getLocalizer().getString("modRolesTblHeader", this, "Set Roles"));

        private final Label modPasswordTblHeader = new Label("modPasswordTblHeader", getLocalizer().getString("modPasswordTblHeader", this, "Change Password"));

        private List<StandardUserMgrBean> userMgrBeanList = new ArrayList<StandardUserMgrBean>();

        private boolean hasRoleStdAppUserRoleMgr = false;

        private boolean hasRoleStdUserPasswordMgr = false;

        /**
         * how many results have been retrieved from the db
         */
        private int getNumResults() {
            int temp = 0;
            if (userMgrBeanList != null) {
                temp = userMgrBeanList.size();
            }
            return temp;
        }

        /**
         * Disable the form in the event of an error
         */
        public void disable() {
            userMgrBeanList.clear();
            setEnabled(false);
        }

        /**
         * Constructor
         */
        public UserMgrForm(final String id, final SearchCriteria pSearchCriteria, final SelectChoiceList<IntegerSelectChoice> pULngRefData) {
            super(id);
            formSearchCriteria = pSearchCriteria;
            formPaginationPanel = new PaginationPanel("pagination", formSearchCriteria);
            add(formPaginationPanel);
            add(applyUserChanges);
            add(instructionLabel);
            add(resultSummary);
            add(usernameTblHeader);
            add(profileTblHeader);
            add(isEnabledTblHeader);
            add(isTracingEnabledTblHeader);
            add(languageTblHeader);
            add(dateCreatedTblHeader);
            add(modRolesTblHeader);
            add(modPasswordTblHeader);
            Roles userRoles = ((AppUserSessionBean) getSession()).getRoles();
            hasRoleStdAppUserRoleMgr = userRoles.hasRole(RequiredRoles.ROLE_STD_APP_USER_ROLE_MGR);
            hasRoleStdUserPasswordMgr = userRoles.hasRole(RequiredRoles.ROLE_STD_USER_PASSWORD_MGR);
            add(new RefreshingView<StandardUserMgrBean>("userMgrBean") {

                private static final long serialVersionUID = 1L;

                /**
                     * retrieve the list of users from the data source
                     */
                @Override
                protected Iterator<IModel<StandardUserMgrBean>> getItemModels() {
                    Iterator<StandardUserMgrBean> userMgrBeanIterator = null;
                    if (!isFirstRender) {
                        try {
                            final AppUserSessionBean appUserSession = (AppUserSessionBean) getSession();
                            dataService = new OracleStandardUserMgrDAO(appUserSession.getUsername(), appUserSession.getPassword());
                            userMgrBeanList = dataService.getUsers(formSearchCriteria.getUsername(), formSearchCriteria.getIsAccountEnabled().getKey(), formSearchCriteria.getIsTracingEnabled().getKey(), formSearchCriteria.getDbrlId().getKey(), formSearchCriteria.getLngId().getKey(), formSearchCriteria.getLowerLimit(), formSearchCriteria.getUpperLimit());
                            userMgrBeanIterator = userMgrBeanList.iterator();
                        } catch (SQLException sqle) {
                            disablePage();
                            String errmsg;
                            switch(sqle.getErrorCode()) {
                                case SQLErrorCodes.PERMISSION_DENIED:
                                    errmsg = getLocalizer().getString(PropertyFileKeys.PERMISSION_DENIED, this, PropertyFileMessages.PERMISSION_DENIED);
                                    error(errmsg);
                                    break;
                                default:
                                    errmsg = getLocalizer().getString(PropertyFileKeys.UNEXPECTED_ERROR, this, PropertyFileMessages.UNEXPECTED_ERROR);
                                    LOGGER.error("SQL Exception while retrieving standard users -> {}; error code -> {}; sql state -> {}", new Object[] { sqle.getMessage(), sqle.getErrorCode(), sqle.getSQLState() });
                                    error(errmsg);
                                    break;
                            }
                        } catch (Exception e) {
                            disablePage();
                            LOGGER.error("Unexpected error while retrieving standard users -> class -> {} ; message -> {}", e.getClass(), e.getMessage());
                            final String errmsg = getLocalizer().getString(PropertyFileKeys.UNEXPECTED_ERROR, this, PropertyFileMessages.UNEXPECTED_ERROR);
                            error(errmsg);
                        } finally {
                            if (!dataService.closeConnection()) {
                                final String errmsg = getLocalizer().getString(PropertyFileKeys.UNEXPECTED_ERROR, this, PropertyFileMessages.UNEXPECTED_ERROR);
                                error(errmsg);
                            }
                        }
                    }
                    if (userMgrBeanIterator == null || !this.isEnabled()) {
                        userMgrBeanIterator = new ArrayList<StandardUserMgrBean>().iterator();
                    }
                    return new ModelIteratorAdapter<StandardUserMgrBean>(userMgrBeanIterator) {

                        @Override
                        protected IModel<StandardUserMgrBean> model(final StandardUserMgrBean object) {
                            return new CompoundPropertyModel<StandardUserMgrBean>(object);
                        }
                    };
                }

                /**
                     * Populate the form
                     */
                @Override
                protected void populateItem(final Item<StandardUserMgrBean> item) {
                    final StandardUserMgrBean userBean = item.getModelObject();
                    final String itemUsername = userBean.getAurUsername();
                    item.add(new Label("aurUsername"));
                    item.add(new Label("aurProfile"));
                    item.add(new YesNoChoice("aurIsAccountEnabled").setEnabled(!itemUsername.equals(dataService.getUsername())));
                    item.add(new YesNoChoice("aurIsTracingEnabled"));
                    item.add(new DropDownChoice<IntegerSelectChoice>("aurLanguage", pULngRefData, pULngRefData));
                    item.add(new StandardDateLabel("aurCreatedDate"));
                    item.add(new Link<StandardUserMgrBean>("modRolesLink") {

                        private static final long serialVersionUID = 1L;

                        @Override
                        public void onClick() {
                            setResponsePage(new StandardUserRoleMgrPage(userBean));
                        }
                    }.setVisible(hasRoleStdAppUserRoleMgr));
                    item.add(new Link<Void>("modPasswordLink") {

                        private static final long serialVersionUID = 1L;

                        @Override
                        public void onClick() {
                            setResponsePage(new StandardUserPasswordMgrPage(userBean));
                        }
                    }.setVisible(hasRoleStdUserPasswordMgr).setEnabled(!itemUsername.equals(dataService.getUsername())));
                }
            });
        }

        @Override
        public void onBeforeRender() {
            formPaginationPanel.remove();
            super.onBeforeRender();
            final int numResults = this.getNumResults();
            final int recordsPerPage = formSearchCriteria.getRecordsPerPage().getKey();
            final boolean isFinalPage = (numResults < formSearchCriteria.getRecordsPerPage().getKey());
            final boolean reportHasResults = (numResults > 0);
            formPaginationPanel.setIsFinalPage(isFinalPage);
            formPaginationPanel.setVisible((reportHasResults && recordsPerPage != PaginationBean.ALL_RECORDS_ON_PAGE) || (formSearchCriteria.getCurrPage() > 1 && isFinalPage));
            add(formPaginationPanel);
            usernameTblHeader.setVisible(reportHasResults);
            profileTblHeader.setVisible(reportHasResults);
            isEnabledTblHeader.setVisible(reportHasResults);
            isTracingEnabledTblHeader.setVisible(reportHasResults);
            languageTblHeader.setVisible(reportHasResults);
            dateCreatedTblHeader.setVisible(reportHasResults);
            modRolesTblHeader.setVisible(reportHasResults && hasRoleStdAppUserRoleMgr);
            modPasswordTblHeader.setVisible(reportHasResults && hasRoleStdUserPasswordMgr);
            instructionLabel.setVisible((!reportHasResults) && isEnabled());
            applyUserChanges.setVisible(reportHasResults);
            resultSummary.setVisible(reportHasResults);
            if (reportHasResults) {
                ValueMap resultSummaryMap = new ValueMap();
                int startResultNum;
                if (formSearchCriteria.getLowerLimit() == 0) {
                    startResultNum = 0;
                } else {
                    startResultNum = formSearchCriteria.getLowerLimit();
                }
                int endResultNum = 0;
                if (formSearchCriteria.getLowerLimit() == 0) {
                    endResultNum = formSearchCriteria.getLowerLimit() + numResults;
                } else {
                    endResultNum = formSearchCriteria.getLowerLimit() + numResults - 1;
                }
                resultSummaryMap.put("startResultNum", startResultNum);
                resultSummaryMap.put("endResultNum", endResultNum);
                resultSummary.setVisible(true).setDefaultModel(new StringResourceModel("resultSummary", this, new Model<ValueMap>(resultSummaryMap)));
            } else {
                if (!isFirstRender) {
                    instructionLabel.setDefaultModelObject(getLocalizer().getString("noDataFoundPrompt", this, "No data has been found that matched your search criteria"));
                }
            }
            isFirstRender = false;
        }

        /**
         * Submit the user management form and apply the changes to the users
         */
        @Override
        protected void onSubmit() {
            try {
                final AppUserSessionBean appUserSession = (AppUserSessionBean) getSession();
                dataService = new OracleStandardUserMgrDAO(appUserSession.getUsername(), appUserSession.getPassword());
                dataService.updateUsers(userMgrBeanList);
                dataService.doCommit();
                final String infomsg = getLocalizer().getString("successMessage", this, "The specified changes were successfully made");
                info(infomsg);
            } catch (NothingToDoException ntde) {
                final String errmsg = getLocalizer().getString("nothingToDo", this, "No changes to the users were specified");
                error(errmsg);
            } catch (SQLException sqle) {
                disablePage();
                String errmsg;
                switch(sqle.getErrorCode()) {
                    case SQLErrorCodes.PERMISSION_DENIED:
                        errmsg = getLocalizer().getString(PropertyFileKeys.PERMISSION_DENIED, this, PropertyFileMessages.PERMISSION_DENIED);
                        error(errmsg);
                        break;
                    default:
                        errmsg = getLocalizer().getString(PropertyFileKeys.UNEXPECTED_ERROR, this, PropertyFileMessages.UNEXPECTED_ERROR);
                        LOGGER.error("SQL Exception while updating standard users -> {}; error code -> {}; sql state -> {}", new Object[] { sqle.getMessage(), sqle.getErrorCode(), sqle.getSQLState() });
                        error(errmsg);
                        break;
                }
            } catch (Exception e) {
                disablePage();
                LOGGER.error("Unexpected error while updating standard users -> class -> {} ; message -> {}", e.getClass(), e.getMessage());
                final String errmsg = getLocalizer().getString(PropertyFileKeys.UNEXPECTED_ERROR, this, PropertyFileMessages.UNEXPECTED_ERROR);
                error(errmsg);
            } finally {
                if (!dataService.closeConnection()) {
                    final String errmsg = getLocalizer().getString(PropertyFileKeys.UNEXPECTED_ERROR, this, PropertyFileMessages.UNEXPECTED_ERROR);
                    error(errmsg);
                }
            }
        }
    }
}
