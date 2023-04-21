package com.windsor.node.plugin.windsor.jdbc;

import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import com.windsor.node.common.domain.CommonTransactionStatusCode;
import com.windsor.node.common.domain.DataServiceRequestParameter;
import com.windsor.node.common.domain.NodeTransaction;
import com.windsor.node.common.domain.PaginationIndicator;
import com.windsor.node.common.domain.ProcessContentResult;
import com.windsor.node.common.domain.ServiceType;
import com.windsor.node.data.dao.PluginServiceParameterDescriptor;
import com.windsor.node.plugin.BaseWnosPlugin;

/**
 * @author mchmarny
 * 
 */
public class NonQuerySqlRequestProcessor extends BaseWnosPlugin {

    /**
     * runtime argument names
     */
    public static final String ARG_SQL = "Sql Query";

    public NonQuerySqlRequestProcessor() {
        super();
        setPublishForEN11(false);
        setPublishForEN20(false);
        debug("Setting internal runtime argument list");
        getConfigurationArguments().put(ARG_SQL, "");
        debug("Setting internal data source list");
        getDataSources().put(ARG_DS_SOURCE, (DataSource) null);
        getSupportedPluginTypes().add(ServiceType.TASK);
        debug("Plugin initialized");
    }

    /**
     * will be called by the plugin executor after properties are set. an
     * opportunity to validate all settings
     */
    public void afterPropertiesSet() {
        super.afterPropertiesSet();
        debug("Validating data sources");
        if (getDataSources() == null) {
            throw new RuntimeException("Data sources not set");
        }
        if (!getDataSources().containsKey(ARG_DS_SOURCE)) {
            throw new RuntimeException("Source data source not set");
        }
        debug("Validating runtime args");
        if (getConfigurationArguments() == null) {
            throw new RuntimeException("Config args not set");
        }
        if (!getConfigurationArguments().containsKey(ARG_SQL)) {
            throw new RuntimeException(ARG_SQL + " not set");
        }
        debug("Plugin validated");
    }

    /**
     * processlogger.debug
     */
    public ProcessContentResult process(NodeTransaction transaction) {
        debug("Processing transaction...");
        ProcessContentResult result = new ProcessContentResult();
        result.setSuccess(false);
        result.setStatus(CommonTransactionStatusCode.Failed);
        try {
            result.getAuditEntries().add(makeEntry("Validating transaction..."));
            validateTransaction(transaction);
            result.getAuditEntries().add(makeEntry("Acquiring arguments..."));
            DataSource dataSource = (DataSource) getDataSources().get(ARG_DS_SOURCE);
            debug("Data Source: " + dataSource);
            String sql = getRequiredConfigValueAsString(ARG_SQL);
            result.getAuditEntries().add(makeEntry("Sql Query: " + sql));
            result.getAuditEntries().add(makeEntry("Executing request..."));
            new SimpleNonQueryExecutor().executeSql(dataSource, sql);
            result.setPaginatedContentIndicator(new PaginationIndicator(transaction.getRequest().getPaging().getStart(), transaction.getRequest().getPaging().getCount(), true));
            result.setSuccess(true);
            result.setStatus(CommonTransactionStatusCode.Processed);
            result.getAuditEntries().add(makeEntry("Done: OK"));
        } catch (Exception ex) {
            error(ex);
            ex.printStackTrace();
            result.setSuccess(false);
            result.setStatus(CommonTransactionStatusCode.Failed);
            result.getAuditEntries().add(makeEntry("Error while executing: " + this.getClass().getName() + " Message: " + ex.getMessage()));
        }
        return result;
    }

    @Override
    public List<DataServiceRequestParameter> getServiceRequestParamSpecs(String serviceName) {
        return null;
    }

    @Override
    public List<PluginServiceParameterDescriptor> getParameters() {
        return new ArrayList<PluginServiceParameterDescriptor>();
    }
}
