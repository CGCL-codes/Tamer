package com.persistent.appfabric.sample.salesdataclient.logger;

import com.persistent.appfabric.common.LoggerUtil;
import com.persistent.appfabric.common.logger.LogRecord;

public class LoggerHelper {

    public static enum RecordType {

        GetRegion_REQUEST, GetRegion_RESPONSE, GetProduct_REQUEST, GetProduct_RESPONSE, CompanySalesData_REQUEST, CompanySalesData_RESPONSE, PersonalSalesData_REQUEST, PersonalSalesData_RESPONSE, AllSalesData_REQUEST, AllSalesData_RESPONSE, AddEditSalesData_REQUEST, AddEditSalesData_RESPONSE, UpdateSalesOrder_REQUEST, UpdateSalesOrder_RESPONSE
    }

    private static String getRequestDesc(RecordType code) {
        if (code == RecordType.GetRegion_REQUEST) return "Request To GetRegion";
        if (code == RecordType.GetRegion_RESPONSE) return "Response For GetRegion";
        if (code == RecordType.GetProduct_REQUEST) return "Request To GetProduct";
        if (code == RecordType.GetProduct_RESPONSE) return "Response For GetProduct";
        if (code == RecordType.CompanySalesData_REQUEST) return "Request To CompanySalesData";
        if (code == RecordType.CompanySalesData_RESPONSE) return "Response For CompanySalesData";
        if (code == RecordType.PersonalSalesData_REQUEST) return "Request To PersonalSalesData";
        if (code == RecordType.PersonalSalesData_RESPONSE) return "Response For PersonalSalesData";
        if (code == RecordType.AllSalesData_REQUEST) return "Request To AllSalesData";
        if (code == RecordType.AllSalesData_RESPONSE) return "Response For AllSalesData";
        if (code == RecordType.AddEditSalesData_REQUEST) return "Request To AddEditSalesData";
        if (code == RecordType.AddEditSalesData_RESPONSE) return "Response For AddEditSalesData";
        if (code == RecordType.UpdateSalesOrder_REQUEST) return "Request To UpdateSalesOrder";
        if (code == RecordType.UpdateSalesOrder_RESPONSE) return "Response For UpdateSalesOrder";
        return "REQUEST";
    }

    public static void logMessage(String xmlString, RecordType recordType) {
        LogRecord record = new LogRecord(getRequestDesc(recordType), xmlString);
        LoggerUtil.getLogger().logMessage(record);
    }
}
