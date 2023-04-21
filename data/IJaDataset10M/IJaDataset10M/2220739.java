package gov.cdc.ncphi.phgrid.amds.service;

import java.math.BigInteger;
import java.rmi.RemoteException;
import java.util.Calendar;
import v2.AMDSQueryResponse;
import v2.AMDSRecordSummaryType;
import v2.AMDSRecordType;
import v2.ConditionsSupportedType;
import v2.CountSetType;
import v2.GeoLocationSupportedType;
import v2.MetaDataQueryResponse;
import v2.MetaDataRecordType;
import v2.TimePeriodSupportedType;
import v2.AMDSQueryRequest;

/** 
 * TODO:I am the service side implementation class.  IMPLEMENT AND DOCUMENT ME
 * 
 * @created by Introduce Toolkit version 1.2
 * 
 */
public class AMDSImpl extends AMDSImplBase {

    BigInteger cellSuppressionRule = null;

    ConditionsSupportedType conditionsSupported = null;

    String dataSourceOID = null;

    GeoLocationSupportedType geoLocationSupported = null;

    TimePeriodSupportedType timePeriodSupported = null;

    public AMDSImpl() throws RemoteException {
        super();
    }

    public v2.MetaDataQueryResponse metaDataQuery() throws RemoteException {
        MetaDataQueryResponse response = new MetaDataQueryResponse();
        MetaDataRecordType metaDataRecord = new MetaDataRecordType();
        metaDataRecord.setCellSuppressionRule(cellSuppressionRule);
        metaDataRecord.setConditionsSupported(conditionsSupported);
        metaDataRecord.setDataSourceOID(dataSourceOID);
        metaDataRecord.setGeoLocationSupported(geoLocationSupported);
        metaDataRecord.setTimePeriodSupported(timePeriodSupported);
        response.setMetaDataRecord(metaDataRecord);
        return response;
    }

    public v2.AMDSQueryResponse amdsQuery(v2.AMDSQueryRequest amdsQueryRequest) throws RemoteException {
        AMDSQueryResponse response = new AMDSQueryResponse();
        AMDSRecordType amdsRecord = new AMDSRecordType();
        AMDSRecordSummaryType amdsRecordSummary = new AMDSRecordSummaryType();
        amdsRecordSummary.setCellSuppressionRule(cellSuppressionRule);
        amdsRecordSummary.setCreationDateTime(Calendar.getInstance());
        amdsRecordSummary.setDataSourceOID(dataSourceOID);
        amdsRecordSummary.setDateEnd(amdsQueryRequest.getAMDSRequestRecord().getAMDSRecordSummary().getDateEnd());
        amdsRecordSummary.setDateStart(amdsQueryRequest.getAMDSRequestRecord().getAMDSRecordSummary().getDateStart());
        amdsRecordSummary.setGeoLocation(amdsQueryRequest.getAMDSRequestRecord().getAMDSRecordSummary().getGeoLocation());
        amdsRecordSummary.setGeoLocationSupported(amdsQueryRequest.getAMDSRequestRecord().getAMDSRecordSummary().getGeoLocationSupported());
        amdsRecordSummary.setRequestingUser(amdsQueryRequest.getAMDSRequestRecord().getAMDSRecordSummary().getRequestingUser());
        amdsRecordSummary.setTargetQuery(amdsQueryRequest.getAMDSRequestRecord().getAMDSRecordSummary().getTargetQuery());
        amdsRecord.setAMDSRecordSummary(amdsRecordSummary);
        CountSetType countSet = new CountSetType();
        amdsRecord.setCountSet(countSet);
        response.setAMDSRecord(amdsRecord);
        return response;
    }
}
