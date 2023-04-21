package com.modelmetrics.cloudconverter.importxls.services;

import com.modelmetrics.cloudconverter.importxls.struts2.UploadContext;
import com.modelmetrics.cloudconverter.util.OperationStatusSubscriber;
import com.modelmetrics.common.sforce.SalesforceSession;

public interface SalesforceService {

    public void setSalesforceSession(SalesforceSession salesforceSession);

    public void execute(UploadContext uploadContext) throws Exception;

    public void execute(CloudConverterObject current, OperationStatusSubscriber migrationStatusSubscriber) throws Exception;

    public boolean containsObject(String objectName) throws Exception;
}
