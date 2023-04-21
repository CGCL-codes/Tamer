package org.eaasyst.eaa.data.impl;

import java.util.Iterator;
import javax.sql.DataSource;
import org.apache.struts.util.MessageResources;
import org.eaasyst.eaa.Constants;
import org.eaasyst.eaa.data.DataConnector;
import org.eaasyst.eaa.data.SqlAccessBeanBase;
import org.eaasyst.eaa.syst.EaasyStreet;
import org.eaasyst.eaa.syst.data.transients.SearchSpecification;
import org.eaasyst.eaa.utils.SqlUtils;
import org.eaasyst.eaa.utils.StringUtils;

/**
 * <p>Dynamic SQL data access bean. To use this bean, you must provide
 * a data source and an SQL statement, either via the constructor or
 * by using the appropriate setters.</p>
 *
 * @version 2.3.3
 * @author Jeff Chilton
 */
public class DynaSqlAccessBean extends SqlAccessBeanBase {

    private static final MessageResources localStrings = MessageResources.getMessageResources("org.eaasyst.eaa.syst.LocalStrings");

    private static final String METHOD_IN = Constants.METHOD_IN + "DynaSqlAccessBean.";

    private static final String METHOD_OUT = Constants.METHOD_OUT + "DynaSqlAccessBean.";

    private static final String READY_METHOD = "isReadyToExecute()";

    private static final String KEY_TAG = "{key}";

    private static final String KEY_PARM = "key";

    private static final String SORT_TAG = "{sort}";

    private static final String FILTER_TAG = "{filter}";

    private static final String FILTER_PARM = "filter";

    private static final String NOT_READY_NOTICE = "DynaSqlAccessBean not ready to execute: ";

    private static final String MISSING_PARM_KEY = "string.sql.dab.missing.parm.msg";

    private static final String INVALID_PARM_KEY = "string.sql.dab.invalid.parm.msg";

    private DataSource dataSource = null;

    private String queryStatement = null;

    private String insertStatement = null;

    private String updateStatement = null;

    private String deleteStatement = null;

    /**
	 * <p>Constructs a new "DynaSqlAccessBean" object.</p>
	 *
	 * @since Eaasy Street 2.0
	 */
    public DynaSqlAccessBean() {
        this(null, null);
    }

    /**
	 * <p>Constructs a new "DynaSqlAccessBean" object using the parameters
	 * provided.</p>
	 *
	 * @param dataSource the datasource in use for this data access bean
	 * @param queryStatement the query statement in use for this data
	 * access bean
	 * @since Eaasy Street 2.0
	 */
    public DynaSqlAccessBean(DataSource dataSource, String queryStatement) {
        className = StringUtils.computeClassName(getClass());
        this.dataSource = dataSource;
        this.queryStatement = queryStatement;
    }

    /**
	 * <p>Returns the datasource in use for this data access bean.</p>
	 * 
	 * @return the datasource in use for this data access bean
	 * @since Eaasy Street 2.0
	 */
    public DataSource getDataSource() {
        return dataSource;
    }

    /**
	 * <p>Returns the query statement in use for this data access bean.</p>
	 * 
	 * @return the query statement in use for this data access bean
	 * @since Eaasy Street 2.0
	 */
    public String getQueryStatement() {
        if (queryStatement.indexOf(KEY_TAG) != -1) {
            return resolveKeyBasedQuery();
        } else if (queryStatement.indexOf(FILTER_TAG) != -1) {
            return resolveFilterBasedQuery();
        } else if (queryStatement.indexOf(SORT_TAG) != -1) {
            return resolveSortedQuery();
        } else {
            return queryStatement;
        }
    }

    /**
	 * <p>Returns the insert statement in use for this data access bean.</p>
	 * 
	 * @return the insert statement in use for this data access bean
	 * @since Eaasy Street 2.0
	 */
    public String getInsertStatement() {
        return insertStatement;
    }

    /**
	 * <p>Returns the update statement in use for this data access bean.</p>
	 * 
	 * @return the update statement in use for this data access bean
	 * @since Eaasy Street 2.0
	 */
    public String getUpdateStatement() {
        return updateStatement;
    }

    /**
	 * <p>Returns the delete statement in use for this data access bean.</p>
	 * 
	 * @return the delete statement in use for this data access bean
	 * @since Eaasy Street 2.0
	 */
    public String getDeleteStatement() {
        return deleteStatement;
    }

    /**
	 * Returns true if the command parameters have been successfully set
	 * and the command is ready to execute.
	 * 
	 * @return Returns true if the command is ready to execute
	 * @since Eaasy Street 2.0
	 */
    public boolean isReadyToExecute() {
        EaasyStreet.logTrace(METHOD_IN + READY_METHOD);
        boolean isReady = false;
        if (dataSource != null) {
            if (command != null) {
                if (command.equals(DataConnector.READ_COMMAND)) {
                    if (queryStatement != null) {
                        if (queryStatement.indexOf(KEY_TAG) != -1) {
                            if (parameters != null) {
                                if (parameters.containsKey(DataConnector.RECORD_KEY_PARAMETER)) {
                                    isReady = true;
                                } else {
                                    EaasyStreet.logError(NOT_READY_NOTICE + localStrings.getMessage(MISSING_PARM_KEY, KEY_PARM));
                                }
                            } else {
                                EaasyStreet.logError(NOT_READY_NOTICE + localStrings.getMessage(MISSING_PARM_KEY, KEY_PARM));
                            }
                        } else if (queryStatement.indexOf(FILTER_TAG) != -1) {
                            if (parameters != null) {
                                if (parameters.containsKey(FILTER_PARM)) {
                                    isReady = true;
                                } else {
                                    EaasyStreet.logError(NOT_READY_NOTICE + localStrings.getMessage(MISSING_PARM_KEY, FILTER_PARM));
                                }
                            } else {
                                EaasyStreet.logError(NOT_READY_NOTICE + localStrings.getMessage(MISSING_PARM_KEY, FILTER_PARM));
                            }
                        } else {
                            isReady = true;
                        }
                    } else {
                        EaasyStreet.logError(NOT_READY_NOTICE + localStrings.getMessage(MISSING_PARM_KEY, "queryStatement"));
                    }
                } else if (command.equals(DataConnector.INSERT_COMMAND)) {
                    if (insertStatement != null) {
                        if (parameters != null) {
                            if (parameters.containsKey(DataConnector.RECORD_PARAMETER)) {
                                isReady = true;
                            } else {
                                EaasyStreet.logError(NOT_READY_NOTICE + localStrings.getMessage(MISSING_PARM_KEY, DataConnector.RECORD_PARAMETER));
                            }
                        } else {
                            EaasyStreet.logError(NOT_READY_NOTICE + localStrings.getMessage(MISSING_PARM_KEY, DataConnector.RECORD_PARAMETER));
                        }
                    } else {
                        EaasyStreet.logError(NOT_READY_NOTICE + localStrings.getMessage(MISSING_PARM_KEY, "insertStatement"));
                    }
                } else if (command.equals(DataConnector.UPDATE_COMMAND)) {
                    if (updateStatement != null) {
                        if (parameters != null) {
                            if (parameters.containsKey(DataConnector.RECORD_PARAMETER)) {
                                isReady = true;
                            } else {
                                EaasyStreet.logError(NOT_READY_NOTICE + localStrings.getMessage(MISSING_PARM_KEY, DataConnector.RECORD_PARAMETER));
                            }
                        } else {
                            EaasyStreet.logError(NOT_READY_NOTICE + localStrings.getMessage(MISSING_PARM_KEY, DataConnector.RECORD_PARAMETER));
                        }
                    } else {
                        EaasyStreet.logError(NOT_READY_NOTICE + localStrings.getMessage(MISSING_PARM_KEY, "insertStatement"));
                    }
                } else if (command.equals(DataConnector.DELETE_COMMAND)) {
                    if (deleteStatement != null) {
                        if (parameters != null) {
                            if (parameters.containsKey(DataConnector.RECORD_PARAMETER)) {
                                isReady = true;
                            } else {
                                EaasyStreet.logError(NOT_READY_NOTICE + localStrings.getMessage(MISSING_PARM_KEY, DataConnector.RECORD_PARAMETER));
                            }
                        } else {
                            EaasyStreet.logError(NOT_READY_NOTICE + localStrings.getMessage(MISSING_PARM_KEY, DataConnector.RECORD_PARAMETER));
                        }
                    } else {
                        EaasyStreet.logError(NOT_READY_NOTICE + localStrings.getMessage(MISSING_PARM_KEY, "insertStatement"));
                    }
                } else {
                    EaasyStreet.logError(NOT_READY_NOTICE + localStrings.getMessage(INVALID_PARM_KEY, command));
                }
            } else {
                EaasyStreet.logError(NOT_READY_NOTICE + localStrings.getMessage(MISSING_PARM_KEY, "command"));
            }
        } else {
            EaasyStreet.logError(NOT_READY_NOTICE + localStrings.getMessage(MISSING_PARM_KEY, "dataSource"));
        }
        EaasyStreet.logTrace(METHOD_OUT + READY_METHOD);
        return isReady;
    }

    /**
	 * <p>Sets the datasource in use for this data access bean.</p>
	 * 
	 * @param dataSource the datasource to be used for this data
	 * access bean
	 * @since Eaasy Street 2.0
	 */
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
	 * <p>Sets the query statement to be used for this data access
	 * bean.</p>
	 * 
	 * @param queryStatement the query statement in use for this data
	 * access bean
	 * @since Eaasy Street 2.0
	 */
    public void setQueryStatement(String queryStatement) {
        this.queryStatement = queryStatement;
    }

    /**
	 * <p>Sets the insert statement to be used for this data access
	 * bean.</p>
	 * 
	 * @param insertStatement the insert statement in use for this data
	 * access bean
	 * @since Eaasy Street 2.0
	 */
    public void setInsertStatement(String insertStatement) {
        this.insertStatement = insertStatement;
    }

    /**
	 * <p>Sets the update statement to be used for this data access
	 * bean.</p>
	 * 
	 * @param updateStatement the update statement in use for this data
	 * access bean
	 * @since Eaasy Street 2.0
	 */
    public void setUpdateStatement(String updateStatement) {
        this.updateStatement = updateStatement;
    }

    /**
	 * <p>Sets the delete statement to be used for this data access
	 * bean.</p>
	 * 
	 * @param deleteStatement the delete statement in use for this data
	 * access bean
	 * @since Eaasy Street 2.0
	 */
    public void setDeleteStatement(String deleteStatement) {
        this.deleteStatement = deleteStatement;
    }

    /**
	 * <p>Returns the query statement in use for this data access bean
	 * after parametic replacement of key value.</p>
	 * 
	 * @return the resolved query statement
	 * @since Eaasy Street 2.0
	 */
    private String resolveKeyBasedQuery() {
        String returnValue = queryStatement;
        Iterator i = parameters.keySet().iterator();
        while (i.hasNext()) {
            String key = i.next().toString();
            if (key.startsWith(DataConnector.RECORD_KEY_PARAMETER)) {
                String symbolic = "{" + key + "}";
                returnValue = StringUtils.replace(returnValue, symbolic, parameters.get(key).toString());
            }
        }
        return returnValue;
    }

    /**
	 * <p>Returns the query statement in use for this data access bean
	 * after parametic replacement of filter and optional sort values.</p>
	 * 
	 * @return the resolved query statement
	 * @since Eaasy Street 2.0
	 */
    private String resolveFilterBasedQuery() {
        String returnValue = queryStatement;
        SearchSpecification spec = (SearchSpecification) parameters.get(FILTER_PARM);
        String filter = SqlUtils.resolveFilter(spec.getFilter());
        String sort = SqlUtils.resolveSortFields(spec.getSortFields());
        returnValue = StringUtils.replace(returnValue, FILTER_TAG, filter);
        returnValue = StringUtils.replace(returnValue, SORT_TAG, sort);
        return returnValue;
    }

    /**
	 * <p>Returns the query statement in use for this data access bean
	 * after parametic replacement of filter and optional sort values.</p>
	 * 
	 * @return the resolved query statement
	 * @since Eaasy Street 2.3.3
	 */
    private String resolveSortedQuery() {
        String returnValue = queryStatement;
        SearchSpecification spec = (SearchSpecification) parameters.get(FILTER_PARM);
        String sort = SqlUtils.resolveSortFields(spec.getSortFields());
        returnValue = StringUtils.replace(returnValue, SORT_TAG, sort);
        return returnValue;
    }
}
