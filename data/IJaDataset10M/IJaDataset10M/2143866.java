package com.windsor.node.plugin.rcra50;

import java.util.List;
import javax.sql.DataSource;
import com.windsor.node.common.domain.CommonTransactionStatusCode;
import com.windsor.node.common.domain.DataServiceRequestParameter;
import com.windsor.node.common.domain.NodeTransaction;
import com.windsor.node.common.domain.ProcessContentResult;
import com.windsor.node.common.domain.ServiceType;
import com.windsor.node.plugin.rcra50.dao.RcraStatusDao;

/**
 * Resets the CDXPROCESSINGSTATUS column of the WQX_SUBMISSIONHISTORY table, so
 * that the next run can set a status of &quot;Pending&quot; if it needs to,
 * while leaving the submission history intact.
 */
public class RcraStatusResetter extends BaseRcra50Plugin {

    public RcraStatusResetter() {
        super();
        debug("Setting service types");
        getSupportedPluginTypes().add(ServiceType.TASK);
        debug("RcraStatusResetter instantiated.");
    }

    public ProcessContentResult process(NodeTransaction transaction) {
        debug("Processing transaction...");
        ProcessContentResult result = new ProcessContentResult();
        result.setSuccess(false);
        result.setStatus(CommonTransactionStatusCode.Failed);
        try {
            DataSource ds = (DataSource) getDataSources().get(ARG_DS_SOURCE);
            RcraStatusDao dao = new RcraStatusDao(ds);
            result.getAuditEntries().add(makeEntry("Attempting to reset CDXPROCESSINGSTATUS so there are no rows marked pending."));
            dao.resetStatus(getOperationTypeFromTransaction(transaction));
            result.setSuccess(true);
            result.setStatus(CommonTransactionStatusCode.Processed);
            result.getAuditEntries().add(makeEntry("Done: OK"));
        } catch (Exception ex) {
            error(ex);
            ex.printStackTrace();
            result.setSuccess(false);
            result.setStatus(CommonTransactionStatusCode.Failed);
            result.getAuditEntries().add(makeEntry("Error while executing: " + this.getClass().getName() + "Message: " + ex.getMessage()));
        }
        return result;
    }

    public void afterPropertiesSet() {
        super.afterPropertiesSet();
        debug("Validating data sources");
        if (getDataSources() == null) {
            throw new RuntimeException("Data sources not set");
        }
        if (!getDataSources().containsKey(ARG_DS_SOURCE)) {
            throw new RuntimeException("Data source not set");
        }
        debug("DwqHistoryResetter data source validated");
    }

    @Override
    public List<DataServiceRequestParameter> getServiceRequestParamSpecs(String serviceName) {
        return null;
    }
}
