package org.oclc.da.ndiipp.struts.packagecontainer.util;

import java.util.ArrayList;
import java.util.Date;
import org.oclc.da.common.textformat.NDIIPPDate;
import org.oclc.da.exceptions.DAException;
import org.oclc.da.informationobject.InformationObjectType;
import org.oclc.da.ndiipp.common.CommonConst;
import org.oclc.da.ndiipp.common.DataDictionary;
import org.oclc.da.ndiipp.common.DataObject;
import org.oclc.da.ndiipp.common.pvt.DataDictionaryDataContainer;
import org.oclc.da.ndiipp.packagecontainer.PackageContainerConst;
import org.oclc.da.ndiipp.struts.system.util.SpiderSettingsBean;
import org.oclc.da.utils.StringUtils;

/**
 * Translate PackageContainer to/from PackageContainerBean
 * @author MJT
 */
public class PackageContainerTranslator {

    /**
     * Takes a package and sets the package bean passed as a param
     * @param packageInfo The spider settings holding the
     *        information
     * @param pBean The bean being set
     */
    public static void getBeanFromPackage(DataObject packageInfo, PackageContainerBean pBean) {
        DataDictionaryDataContainer dddc = new DataDictionaryDataContainer(packageInfo, new DataDictionary(InformationObjectType.PACKAGE_CONTAINER));
        pBean.setGuid(nullStr(packageInfo.getAttr(CommonConst.GUID)));
        pBean.setName(nullStr(dddc.getAttr(PackageContainerConst.NAME)));
        pBean.setStatus(nullStr(dddc.getAttr(PackageContainerConst.STATUS)));
        pBean.setUrl(nullStr(dddc.getAttr(PackageContainerConst.URL)));
        Date scheduledDate = (Date) dddc.getAttr(PackageContainerConst.DATE_SCHEDULED);
        if (scheduledDate != null) {
            NDIIPPDate changer = new NDIIPPDate();
            String dateString = changer.date(scheduledDate);
            pBean.setDateScheduled(dateString);
        }
        Date startDate = (Date) dddc.getAttr(PackageContainerConst.DATE_STARTED);
        if (startDate != null) {
            NDIIPPDate changer = new NDIIPPDate();
            String dateString = changer.date(startDate);
            pBean.setDateStarted(dateString);
        }
        Date completeDate = (Date) dddc.getAttr(PackageContainerConst.DATE_COMPLETED);
        if (completeDate != null) {
            NDIIPPDate changer = new NDIIPPDate();
            String dateString = changer.date(completeDate);
            pBean.setDateCompleted(dateString);
        }
        Date createDate = (Date) dddc.getAttr(PackageContainerConst.DATE_CREATED);
        if (createDate != null) {
            NDIIPPDate changer = new NDIIPPDate();
            String dateString = changer.date(createDate);
            pBean.setCreateDate(dateString);
        }
        Date ingestDate = (Date) dddc.getAttr(PackageContainerConst.DATE_INGESTED);
        if (ingestDate != null) {
            NDIIPPDate changer = new NDIIPPDate();
            String dateString = changer.date(ingestDate);
            pBean.setIngestDate(dateString);
        }
        Date modifiedDate = (Date) dddc.getAttr(PackageContainerConst.DATE_MODIFIED);
        if (modifiedDate != null) {
            NDIIPPDate changer = new NDIIPPDate();
            String dateString = changer.date(modifiedDate);
            pBean.setModifiedDate(dateString);
        }
        Date packageDate = (Date) dddc.getAttr(PackageContainerConst.DATE_PACKAGED);
        if (packageDate != null) {
            NDIIPPDate changer = new NDIIPPDate();
            String dateString = changer.date(packageDate);
            pBean.setPackageDate(dateString);
        }
        Date hsPackageDate = (Date) dddc.getAttr(PackageContainerConst.HS_PACKAGE_DATE);
        if (hsPackageDate != null) {
            NDIIPPDate changer = new NDIIPPDate();
            String dateString = changer.date(hsPackageDate);
            pBean.setHsPackageDate(dateString);
        }
        if (dddc.getAttr(PackageContainerConst.AGING_STATUS) != null) {
            pBean.setAgingStatus((dddc.getAttr(PackageContainerConst.AGING_STATUS)).toString());
        } else {
            pBean.setAgingStatus("");
        }
        pBean.setTransaction((String) packageInfo.getAttr(PackageContainerConst.TRANSACTION) == null ? "" : (String) packageInfo.getAttr(PackageContainerConst.TRANSACTION));
        pBean.setSpiderRef((String) packageInfo.getAttr(PackageContainerConst.SP_SETTINGS_REF) == null ? "" : (String) packageInfo.getAttr(PackageContainerConst.SP_SETTINGS_REF));
        pBean.setPdiGUID(nullStr(dddc.getAttr(PackageContainerConst.PDI_REF)));
        pBean.setReportRef((String) packageInfo.getAttr(PackageContainerConst.REPORT_REF) == null ? "" : (String) packageInfo.getAttr(PackageContainerConst.REPORT_REF));
        pBean.setRunDefRef((String) packageInfo.getAttr(PackageContainerConst.RUNDEF_REF) == null ? "" : (String) packageInfo.getAttr(PackageContainerConst.RUNDEF_REF));
        pBean.setMetadataGUID((String) packageInfo.getAttr(PackageContainerConst.DC_META_REF) == null ? "" : (String) packageInfo.getAttr(PackageContainerConst.DC_META_REF));
        pBean.setHarvestLocation(nullStr(dddc.getAttr(PackageContainerConst.HARVEST_LOC)));
        pBean.setHsPackageLocation(nullStr(dddc.getAttr(PackageContainerConst.HS_PACKAGE_LOC)));
        pBean.setHsStatus(nullStr(dddc.getAttr(PackageContainerConst.HS_STATUS)));
        pBean.setPackageLocation(nullStr(dddc.getAttr(PackageContainerConst.PACKAGE_LOC)));
        pBean.setEntityRefPlusName((String) packageInfo.getAttr(PackageContainerConst.ENTITY_REF) == null ? "" : (String) packageInfo.getAttr(PackageContainerConst.ENTITY_REF));
        pBean.setPackageError(nullStr(dddc.getAttr(PackageContainerConst.PACKAGE_ERROR)));
        pBean.setPackageID(nullStr(dddc.getAttr(PackageContainerConst.PACKAGE_ID)));
        pBean.setPackageCreator(nullStr(dddc.getAttr(PackageContainerConst.CREATOR)));
        pBean.setOclcNumber((String) packageInfo.getAttr(PackageContainerConst.OCLC_NUMBER) == null ? "" : (String) packageInfo.getAttr(PackageContainerConst.OCLC_NUMBER));
        pBean.setSeriesRef((String) packageInfo.getAttr(PackageContainerConst.SERIES_REF) == null ? "" : (String) packageInfo.getAttr(PackageContainerConst.SERIES_REF));
        if (dddc.getAttr(PackageContainerConst.AUTO_METADATA) != null) {
            pBean.setAutoMetadata(((Boolean) dddc.getAttr(PackageContainerConst.AUTO_METADATA)).booleanValue());
        }
        if (dddc.getAttr(PackageContainerConst.CHILD) != null) {
            pBean.setChild(((Boolean) dddc.getAttr(PackageContainerConst.CHILD)).booleanValue());
        }
        if (((String) packageInfo.getAttr(PackageContainerConst.HAS_TREE)).equals(CommonConst._TRUE)) {
            pBean.setHasTree(true);
        } else {
            pBean.setHasTree(false);
        }
        pBean.setElapsedTime(nullStr(dddc.getAttr(PackageContainerConst.ELAPSED_TIME)));
        String[] exclusions = (String[]) dddc.getAttr(PackageContainerConst.EXCLUSIONS);
        if ((exclusions != null) && (exclusions.length > 0)) {
            ArrayList<String> excl = new ArrayList<String>();
            for (int ii = 0; ii < exclusions.length; ii++) {
                String returned = exclusions[ii];
                excl.add(returned);
            }
            pBean.setExclusions(excl);
        }
        if (dddc.getAttr(PackageContainerConst.DEPTH_LIMIT) != null) {
            SpiderSettingsBean ssBean = new SpiderSettingsBean();
            ssBean.setName(nullStr(dddc.getAttr(PackageContainerConst.SP_SETTINGS_NAME)));
            ssBean.setType(nullStr(dddc.getAttr(PackageContainerConst.SP_TYPE)));
            ssBean.setDepthLimit(dddc.getAttr(PackageContainerConst.DEPTH_LIMIT).toString());
            ssBean.setTimeLimitHours(dddc.getAttr(PackageContainerConst.TIME_LIMIT_HOURS).toString());
            ssBean.setTimeLimitMinutes(dddc.getAttr(PackageContainerConst.TIME_LIMIT_MINS).toString());
            ssBean.setIgnoreRobots(((Boolean) dddc.getAttr(PackageContainerConst.IGNORE_ROBOTS)).booleanValue());
            ssBean.setTraversalMethod(nullStr(dddc.getAttr(PackageContainerConst.TRAVERSAL_METHOD)));
            ssBean.setMasqueradeEmail(nullStr(dddc.getAttr(PackageContainerConst.MASQUERADE_EMAIL)));
            ssBean.setMasqueradeURL(nullStr(dddc.getAttr(PackageContainerConst.MASQUERADE_URL)));
            try {
                ssBean.setHonorExclusions(((Boolean) dddc.getAttr(PackageContainerConst.HONOR_EXCLUDED_DOMAINS)).booleanValue());
            } catch (RuntimeException e) {
                ssBean.setHonorExclusions(true);
            }
            try {
                ssBean.setOffDomain(((Boolean) dddc.getAttr(PackageContainerConst.OFF_DOMAIN)).booleanValue());
            } catch (RuntimeException e) {
                ssBean.setOffDomain(false);
            }
            pBean.setSpiderSettings(ssBean);
        }
        pBean.setIngestStatus(nullStr(dddc.getAttr(PackageContainerConst.INGEST_STATUS)));
        pBean.setLocalLink(nullStr(dddc.getAttr(PackageContainerConst.LOCAL_LINK)));
    }

    /**
     * makes sure the string is never a null pointer. Changes null string
     * pointers to 0-length strings
     * @param string to check
     * @return original string or a zero-length string
     */
    private static String nullStr(Object string) {
        if (string == null) {
            return "";
        }
        String newString = (String) string;
        return (newString.trim());
    }

    /**
     * Takes a package bean and sets the package info passed as a param
     * @param pi The package info being set
     * @param pBean The bean holding the information
     * @throws DAException
     */
    public static void setPackageFromBean(DataObject pi, PackageContainerBean pBean) throws DAException {
        DataDictionaryDataContainer dddc = new DataDictionaryDataContainer(pi, new DataDictionary(InformationObjectType.PACKAGE_CONTAINER));
        boolean success = dddc.setAttr(CommonConst.GUID, pBean.getGuid() == null ? "" : pBean.getGuid()) && dddc.setAttr(PackageContainerConst.NAME, pBean.getName() == null ? "" : pBean.getName()) && dddc.setAttr(PackageContainerConst.STATUS, pBean.getStatus() == null ? "" : pBean.getStatus()) && dddc.setAttr(PackageContainerConst.URL, pBean.getUrl() == null ? "" : pBean.getUrl());
        if (!StringUtils.isEmpty(pBean.getDateScheduled())) {
            NDIIPPDate changer = new NDIIPPDate();
            Date thisDate = changer.date(pBean.getDateScheduled());
            success = success && dddc.setAttr(PackageContainerConst.DATE_SCHEDULED, thisDate);
        }
        if (!StringUtils.isEmpty(pBean.getDateStarted())) {
            NDIIPPDate changer = new NDIIPPDate();
            Date thisDate = changer.date(pBean.getDateStarted());
            success = success && dddc.setAttr(PackageContainerConst.DATE_STARTED, thisDate);
        }
        if (!StringUtils.isEmpty(pBean.getDateCompleted())) {
            NDIIPPDate changer = new NDIIPPDate();
            Date thisDate = changer.date(pBean.getDateCompleted());
            success = success && dddc.setAttr(PackageContainerConst.DATE_COMPLETED, thisDate);
        }
        if (!StringUtils.isEmpty(pBean.getCreateDate())) {
            NDIIPPDate changer = new NDIIPPDate();
            Date thisDate = changer.date(pBean.getCreateDate());
            success = success && dddc.setAttr(PackageContainerConst.DATE_CREATED, thisDate);
        }
        if (!StringUtils.isEmpty(pBean.getIngestDate())) {
            NDIIPPDate changer = new NDIIPPDate();
            Date thisDate = changer.date(pBean.getIngestDate());
            success = success && dddc.setAttr(PackageContainerConst.DATE_INGESTED, thisDate);
        }
        if (!StringUtils.isEmpty(pBean.getModifiedDate())) {
            NDIIPPDate changer = new NDIIPPDate();
            Date thisDate = changer.date(pBean.getModifiedDate());
            success = success && dddc.setAttr(PackageContainerConst.DATE_MODIFIED, thisDate);
        }
        if (!StringUtils.isEmpty(pBean.getPackageDate())) {
            NDIIPPDate changer = new NDIIPPDate();
            Date thisDate = changer.date(pBean.getPackageDate());
            success = success && dddc.setAttr(PackageContainerConst.DATE_PACKAGED, thisDate);
        }
        if (!StringUtils.isEmpty(pBean.getHsPackageDate())) {
            NDIIPPDate changer = new NDIIPPDate();
            Date thisDate = changer.date(pBean.getHsPackageDate());
            success = success && dddc.setAttr(PackageContainerConst.HS_PACKAGE_DATE, thisDate);
        }
        if ((pBean.getAgingStatus() != null) && !(pBean.getAgingStatus()).equals("")) {
            dddc.setAttr(PackageContainerConst.AGING_STATUS, pBean.getAgingStatus());
        }
        success = success && dddc.setAttr(PackageContainerConst.TRANSACTION, pBean.getTransaction() == null ? "" : pBean.getTransaction()) && dddc.setAttr(PackageContainerConst.SP_SETTINGS_REF, pBean.getSpiderRef() == null ? "" : pBean.getSpiderRef()) && dddc.setAttr(PackageContainerConst.PDI_REF, pBean.getPdiGUID() == null ? "" : pBean.getPdiGUID());
        if (!StringUtils.isEmpty(pBean.getReportRef())) {
            success = success && pi.setAttr(PackageContainerConst.REPORT_REF, pBean.getReportRef());
        }
        if (!StringUtils.isEmpty(pBean.getRunDefRef())) {
            success = success && pi.setAttr(PackageContainerConst.RUNDEF_REF, pBean.getRunDefRef());
        }
        if (!StringUtils.isEmpty(pBean.getMetadataGUID())) {
            success = success && pi.setAttr(PackageContainerConst.DC_META_REF, pBean.getMetadataGUID());
        }
        success = success && dddc.setAttr(PackageContainerConst.HARVEST_LOC, pBean.getHarvestLocation() == null ? "" : pBean.getHarvestLocation()) && dddc.setAttr(PackageContainerConst.HS_PACKAGE_LOC, pBean.getHsPackageLocation() == null ? "" : pBean.getHsPackageLocation()) && dddc.setAttr(PackageContainerConst.HS_STATUS, pBean.getHsStatus() == null ? "" : pBean.getHsStatus()) && dddc.setAttr(PackageContainerConst.PACKAGE_LOC, pBean.getPackageLocation() == null ? "" : pBean.getPackageLocation());
        if (!StringUtils.isEmpty(pBean.getEntityRefPlusName())) {
            success = success && pi.setAttr(PackageContainerConst.ENTITY_REF, pBean.getEntityRefPlusName());
        }
        success = success && dddc.setAttr(PackageContainerConst.PACKAGE_ERROR, pBean.getPackageError() == null ? "" : pBean.getPackageError()) && dddc.setAttr(PackageContainerConst.PACKAGE_ID, pBean.getPackageID() == null ? "" : pBean.getPackageID()) && dddc.setAttr(PackageContainerConst.CREATOR, pBean.getPackageCreator() == null ? "" : pBean.getPackageCreator()) && dddc.setAttr(PackageContainerConst.OCLC_NUMBER, pBean.getOclcNumber() == null ? "" : pBean.getOclcNumber());
        if (!StringUtils.isEmpty(pBean.getSeriesRef())) {
            success = success && pi.setAttr(PackageContainerConst.SERIES_REF, pBean.getSeriesRef());
        }
        success = success && dddc.setAttr(PackageContainerConst.AUTO_METADATA, pBean.isAutoMetadata() ? CommonConst._TRUE : CommonConst._FALSE) && dddc.setAttr(PackageContainerConst.CHILD, pBean.isChild() ? CommonConst._TRUE : CommonConst._FALSE) && dddc.setAttr(PackageContainerConst.ELAPSED_TIME, pBean.getElapsedTime() == null ? "" : pBean.getElapsedTime());
        if ((pBean.getExclusions() != null) && (pBean.getExclusions().size() > 0)) {
            ArrayList exclusions = pBean.getExclusions();
            String[] excl = new String[exclusions.size()];
            for (int ii = 0; ii < excl.length; ii++) {
                excl[ii] = (String) exclusions.get(ii);
            }
            success = success && dddc.setAttr(PackageContainerConst.EXCLUSIONS, excl);
        }
        if (pBean.getSpiderSettings() != null) {
            SpiderSettingsBean ssBean = pBean.getSpiderSettings();
            success = success && dddc.setAttr(PackageContainerConst.SP_SETTINGS_NAME, ssBean.getName() == null ? "" : ssBean.getName()) && dddc.setAttr(PackageContainerConst.SP_TYPE, ssBean.getType() == null ? "" : ssBean.getType()) && dddc.setAttr(PackageContainerConst.DEPTH_LIMIT, ssBean.getDepthLimit() == null ? "" : ssBean.getDepthLimit()) && dddc.setAttr(PackageContainerConst.TIME_LIMIT_HOURS, ssBean.getTimeLimitHours() == null ? "" : ssBean.getTimeLimitHours()) && dddc.setAttr(PackageContainerConst.TIME_LIMIT_MINS, ssBean.getTimeLimitMinutes() == null ? "" : ssBean.getTimeLimitMinutes()) && dddc.setAttr(PackageContainerConst.TRAVERSAL_METHOD, ssBean.getTraversalMethod() == null ? "" : ssBean.getTraversalMethod()) && dddc.setAttr(PackageContainerConst.IGNORE_ROBOTS, ssBean.isIgnoreRobots()) && dddc.setAttr(PackageContainerConst.MASQUERADE_EMAIL, ssBean.getMasqueradeEmail() == null ? "" : ssBean.getMasqueradeEmail()) && dddc.setAttr(PackageContainerConst.MASQUERADE_URL, ssBean.getMasqueradeURL() == null ? "" : ssBean.getMasqueradeURL());
        }
        success = success && dddc.setAttr(PackageContainerConst.INGEST_STATUS, pBean.getIngestStatus() == null ? "" : pBean.getIngestStatus());
        success = success && dddc.setAttr(PackageContainerConst.LOCAL_LINK, pBean.getLocalLink() == null ? "" : pBean.getLocalLink());
        if (!success) {
            throw new DAException();
        }
    }

    /**
     * Takes a package bean and sets the package info passed as a param
     * @param pi The package info being set
     * @param hBean The bean holding the information
     * @throws DAException
     */
    public static void setPackageFromBean(DataObject pi, QuickHarvestBean hBean) throws DAException {
        DataDictionaryDataContainer dddc = new DataDictionaryDataContainer(pi, new DataDictionary(InformationObjectType.PACKAGE_CONTAINER));
        boolean success = dddc.setAttr(CommonConst.GUID, hBean.getGuid() == null ? "" : hBean.getGuid()) && dddc.setAttr(PackageContainerConst.NAME, hBean.getTitle() == null ? "" : hBean.getTitle()) && dddc.setAttr(PackageContainerConst.URL, hBean.getWebsite() == null ? "" : hBean.getWebsite()) && dddc.setAttr(PackageContainerConst.STATUS, PackageContainerConst.STATUS_PENDING) && dddc.setAttr(PackageContainerConst.SP_SETTINGS_NAME, hBean.getWebsite() == null ? "" : hBean.getSpider()) && dddc.setAttr(PackageContainerConst.CREATOR, hBean.getCreator() == null ? "" : hBean.getCreator()) && dddc.setAttr(PackageContainerConst.DATE_SCHEDULED, new Date()) && dddc.setAttr(PackageContainerConst.OCLC_NUMBER, hBean.getIdNumber() == null ? "" : hBean.getIdNumber());
        dddc.setAttr(PackageContainerConst.AUTO_METADATA, CommonConst._TRUE);
        if (!success) {
            throw new DAException();
        }
    }
}
