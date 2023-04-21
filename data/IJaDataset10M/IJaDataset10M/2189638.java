package tr.com.srdc.isurf.ws;

import tr.com.srdc.isurf.xmlbuilders.Result;

public interface CPFRService {

    public Result receivePurchaseConditionsMessage(Object pcDocument);

    public Result receiveForecastRevisionMessage(Object frDocument);

    public Result receiveProductActivityMessage(Object paDocument);

    public Result receivePerformanceHistoryMessage(Object phDocument);

    public Result receiveForecastMessage(Object fDocument);

    public Result receiveExceptionCriteriaMessage(Object ecDocument);

    public Result receiveExceptionNotificationMessage(Object enDocument);

    public Result receiveTILPMessage(Object tilpDocument);

    public Result receiveTerminationMessage(Object tDocument);

    public Result receiveRetailEventMessage(Object reDocument);

    public Result receiveInventoryActivityOrStatusMessage(Object iaosDocument);

    public Result receiveProductionPlanMessage(Object ppDocument);
}
