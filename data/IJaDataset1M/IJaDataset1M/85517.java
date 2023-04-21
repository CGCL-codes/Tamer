package org.oclc.da.ndiipp.packagecontainer.pvt;

import org.oclc.da.common.Ref;
import org.oclc.da.common.query.AdvancedQuery;
import org.oclc.da.common.query.Query;
import org.oclc.da.common.query.QueryConst;
import org.oclc.da.exceptions.Assertor;
import org.oclc.da.exceptions.DAException;
import org.oclc.da.exceptions.DAExceptionCodes;
import org.oclc.da.informationobject.InformationObjectType;
import org.oclc.da.ndiipp.common.CallerIdentity;
import org.oclc.da.ndiipp.common.DataDictionary;
import org.oclc.da.ndiipp.common.Manager;
import org.oclc.da.ndiipp.common.pvt.BasicManagerBL;
import org.oclc.da.ndiipp.common.pvt.DataDictionaryDataContainer;
import org.oclc.da.ndiipp.common.pvt.ManagerServiceInfo;
import org.oclc.da.ndiipp.common.pvt.storage.db.BasicDBManager;
import org.oclc.da.ndiipp.institution.InstitutionRef;
import org.oclc.da.ndiipp.packagecontainer.PackageContainerConst;
import org.oclc.da.ndiipp.packagecontainer.PackageContainerManager;

/**
 * This class defines the business logic for PackageContainerManager
 * @author tadgerm
 */
public class PackageContainerManagerBL extends BasicManagerBL {

    private BasicDBManager delegate = null;

    private CallerIdentity identity = null;

    private Assertor tester = new Assertor();

    private String instGuid = null;

    private String serviceName = PackageContainerManager.SERVICE_NAME;

    /**
     * Construct a PackageContainerManagerBL instance with 
     * another <code>PackageContainerManager</code> to delegate to.
     * @param mgr   The next manager in the chain to delegate to.
	 * @param ident identities
     */
    public PackageContainerManagerBL(Manager mgr, CallerIdentity ident) {
        super(mgr, ident, ManagerServiceInfo.NEW_PACKAGE_INFO_SERVICE_NAME);
        delegate = (BasicDBManager) mgr;
        identity = ident;
        InstitutionRef ir = (InstitutionRef) identity.getIdentity(InstitutionRef.class);
        instGuid = (ir == null) ? null : ir.getGUID();
    }

    /** (non-Javadoc)
	 * @see org.oclc.da.ndiipp.common.Manager#searchBy(org.oclc.da.common.query.Query)
	 */
    public Ref[] searchBy(Query query) throws DAException {
        tester.pre(query != null, DAExceptionCodes.MUST_NOT_BE_NULL, new String[] { "query", "into", serviceName });
        tester.pre(query.getType().equals(InformationObjectType.QUERY), DAExceptionCodes.MUST_BE_VALID, new String[] { "query.getType", "into", serviceName });
        DataDictionaryDataContainer dddc = new DataDictionaryDataContainer(query, new DataDictionary(new QueryConst()));
        if (query.getAttr(AdvancedQuery.PACKAGE_CONTAINER_STATUS_READY_FOR_REVIEW) != null || query.getAttr(AdvancedQuery.PACKAGE_CONTAINER_STATUS_HARVEST_IN_PROCESS) != null || query.getAttr(AdvancedQuery.PACKAGE_CONTAINER_STATUS_PENDING) != null || query.getAttr(AdvancedQuery.PACKAGE_CONTAINER_STATUS_HARVEST_FAILED) != null || query.getAttr(AdvancedQuery.PACKAGE_CONTAINER_STATUS_HARVEST_CANCELED) != null || query.getAttr(AdvancedQuery.PACKAGE_CONTAINER_STATUS_SAVED) != null || query.getAttr(AdvancedQuery.PACKAGE_CONTAINER_AGING_FILTER) != null || query.getAttr(AdvancedQuery.PACKAGE_CONTAINER_ENTITY_FILTER) != null || query.getAttr(AdvancedQuery.PACKAGE_CONTAINER_SERIES_FILTER) != null || query.getAttr(AdvancedQuery.PACKAGE_CONTAINER_CHILD_FILTER) != null || query.getAttr(AdvancedQuery.PACKAGE_CONTAINER_PARENT_FILTER) != null) {
            String select = "SELECT * ";
            String from = "FROM " + InformationObjectType.PACKAGE_CONTAINER + " ";
            String where = "WHERE ";
            where = where + " " + PackageContainerConst.INST_GUID + " = " + "'" + instGuid + "' " + " AND ";
            String statusFilters = "(";
            if (query.getAttr(AdvancedQuery.PACKAGE_CONTAINER_STATUS_READY_FOR_REVIEW) != null) {
                if (!"(".equals(statusFilters)) {
                    statusFilters = statusFilters + " AND ";
                }
                statusFilters = statusFilters + PackageContainerConst.STATUS + " = " + "'" + PackageContainerConst.STATUS_READY_FOR_REVIEW + "' ";
            }
            if (query.getAttr(AdvancedQuery.PACKAGE_CONTAINER_STATUS_HARVEST_IN_PROCESS) != null) {
                if (!"(".equals(statusFilters)) {
                    statusFilters = statusFilters + " AND ";
                }
                statusFilters = statusFilters + PackageContainerConst.STATUS + " = " + "'" + PackageContainerConst.STATUS_HARVEST_IN_PROCESS + "' ";
            }
            if (query.getAttr(AdvancedQuery.PACKAGE_CONTAINER_STATUS_PENDING) != null) {
                if (!"(".equals(statusFilters)) {
                    statusFilters = statusFilters + " AND ";
                }
                statusFilters = statusFilters + PackageContainerConst.STATUS + " = " + "'" + PackageContainerConst.STATUS_PENDING + "' ";
            }
            if (query.getAttr(AdvancedQuery.PACKAGE_CONTAINER_STATUS_HARVEST_FAILED) != null) {
                if (!"(".equals(statusFilters)) {
                    statusFilters = statusFilters + " AND ";
                }
                statusFilters = statusFilters + PackageContainerConst.STATUS + " = " + "'" + PackageContainerConst.STATUS_HARVEST_FAILED + "' ";
            }
            if (query.getAttr(AdvancedQuery.PACKAGE_CONTAINER_STATUS_HARVEST_CANCELED) != null) {
                if (!"(".equals(statusFilters)) {
                    statusFilters = statusFilters + " AND ";
                }
                statusFilters = statusFilters + PackageContainerConst.STATUS + " = " + "'" + PackageContainerConst.STATUS_HARVEST_CANCELED + "' ";
            }
            if (query.getAttr(AdvancedQuery.PACKAGE_CONTAINER_STATUS_SAVED) != null) {
                if (!"(".equals(statusFilters)) {
                    statusFilters = statusFilters + " AND ";
                }
                statusFilters = statusFilters + PackageContainerConst.STATUS + " = " + "'" + PackageContainerConst.STATUS_SAVED + "' ";
            }
            if ("(".equals(statusFilters)) {
                statusFilters = statusFilters + " NOT " + PackageContainerConst.STATUS + " = " + "'" + PackageContainerConst.STATUS_SCHEDULED + "' ";
            }
            statusFilters = statusFilters + ")";
            where = where + statusFilters;
            if (query.getAttr(AdvancedQuery.PACKAGE_CONTAINER_AGING_FILTER) != null) {
                String agingFilter = "";
                String ageNumber = (String) query.getAttr(AdvancedQuery.PACKAGE_CONTAINER_AGING_FILTER);
                agingFilter = agingFilter + " AND (" + PackageContainerConst.AGING_STATUS + "<=" + "'" + ageNumber + "') ";
                where = where + agingFilter;
            }
            if (query.getAttr(AdvancedQuery.PACKAGE_CONTAINER_SERIES_FILTER) != null) {
                String seriesFilter = "";
                String searchTerm = (String) query.getAttr(AdvancedQuery.PACKAGE_CONTAINER_SERIES_FILTER);
                seriesFilter = seriesFilter + " AND (" + PackageContainerConst.SERIES_REF + " LIKE " + "'" + searchTerm + "') ";
                where = where + seriesFilter;
            }
            if (query.getAttr(AdvancedQuery.PACKAGE_CONTAINER_ENTITY_FILTER) != null) {
                String entityFilter = "";
                String searchTerm = (String) query.getAttr(AdvancedQuery.PACKAGE_CONTAINER_ENTITY_FILTER);
                entityFilter = entityFilter + " AND (" + PackageContainerConst.ENTITY_REF + " LIKE " + "'" + searchTerm + "') ";
                where = where + entityFilter;
            }
            if (query.getAttr(AdvancedQuery.PACKAGE_CONTAINER_CHILD_FILTER) != null) {
                String childFilter = "";
                String child = (String) query.getAttr(AdvancedQuery.PACKAGE_CONTAINER_CHILD_FILTER);
                childFilter = childFilter + " AND (" + PackageContainerConst.CHILD + "=" + "'" + child + "') ";
                where = where + childFilter;
            }
            if (query.getAttr(AdvancedQuery.PACKAGE_CONTAINER_PARENT_FILTER) != null) {
                String parentFilter = "";
                String parent = (String) query.getAttr(AdvancedQuery.PACKAGE_CONTAINER_PARENT_FILTER);
                parentFilter = parentFilter + " AND (" + PackageContainerConst.CHILD + "=" + "'" + parent + "') ";
                where = where + parentFilter;
            }
            String order = " ORDER BY ";
            String sorts[] = (String[]) query.getAttr(QueryConst._SORTS);
            boolean ascend[] = (boolean[]) dddc.getAttr(QueryConst._SORT_ORDERS);
            for (int index = 0; (sorts != null) && (index < sorts.length); index++) {
                String sort = sorts[index];
                if (sort != null) {
                    if (order.length() > 10) {
                        order = order + ", ";
                    }
                    order = order + sort + " ";
                    if ((ascend != null) && (ascend.length > index) && (!ascend[index])) {
                        order = order + " DESC";
                    }
                }
            }
            String customQuery = "";
            if (order.equals(" ORDER BY ")) {
                customQuery = select + from + where;
            } else {
                customQuery = select + from + where + order;
            }
            query.setAttr(QueryConst._CUSTOM_QUERY, customQuery);
            Ref[] refs = (Ref[]) delegate.searchBy(query, "PackageContainer");
            return refs;
        }
        Ref[] refs = (Ref[]) delegate.searchBy(query);
        return refs;
    }

    /**
	 * Buisness logic for searchCount
	 * @param query query
	 * @return the count
	 * @throws DAException 
	 */
    public int searchCount(Query query) throws DAException {
        tester.pre(query != null, DAExceptionCodes.MUST_NOT_BE_NULL, new String[] { "query", "into", PackageContainerManager.SERVICE_NAME });
        tester.pre(query.getType().equals(InformationObjectType.QUERY), DAExceptionCodes.MUST_BE_VALID, new String[] { "quiery.getType", "into", PackageContainerManager.SERVICE_NAME });
        String originalEnd = (String) query.getAttr(QueryConst._END_INDEX);
        String originalStart = (String) query.getAttr(QueryConst._START_INDEX);
        query.setAttr(QueryConst._END_INDEX, (new Integer(Integer.MAX_VALUE)).toString());
        query.setAttr(QueryConst._START_INDEX, "0");
        int length = searchBy(query).length;
        query.setAttr(QueryConst._END_INDEX, originalEnd);
        query.setAttr(QueryConst._START_INDEX, originalStart);
        return length;
    }
}
