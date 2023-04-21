package com.modelmetrics.cloudconverter.importxls.struts2;

import com.modelmetrics.cloudconverter.importxls.services.CloudConverterObject;
import com.modelmetrics.cloudconverter.importxls.services.CustomObjectNameDecorator;
import com.modelmetrics.cloudconverter.importxls.services.ExistsInSalesforceDecorator;
import com.modelmetrics.cloudconverter.importxls.services.SalesforceService;
import com.opensymphony.xwork2.Action;

public class ImportPrepareAction extends AbstractUploadContextAware {

    private SalesforceService salesforceService;

    public String execute() throws Exception {
        this.getUploadContext().setCurrentCloudConverterObjectIndex(-1);
        new CustomObjectNameDecorator().decorate(this.getUploadContext().getCloudConverterObjects());
        try {
            new ExistsInSalesforceDecorator().decorate(this.getUploadContext().getCloudConverterObjects(), this.getSalesforceService());
        } catch (Exception e) {
            this.getUploadContext().setLastException(e);
            this.getUploadContext().setMessage("This error is often caused by lack of access to the Salesforce API. Check your edition (EE, UE, Dev) and your porfile (API Enabled).");
            return Action.ERROR;
        }
        boolean needsOverride = false;
        for (CloudConverterObject current : this.getUploadContext().getCloudConverterObjects()) {
            if (current.isExistsInSalesforce()) {
                needsOverride = true;
                break;
            }
        }
        if (needsOverride) {
            return Action.INPUT;
        }
        return Action.SUCCESS;
    }

    public SalesforceService getSalesforceService() {
        return salesforceService;
    }

    public void setSalesforceService(SalesforceService salesforceService) {
        this.salesforceService = salesforceService;
    }
}
