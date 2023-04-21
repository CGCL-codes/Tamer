package org.brainypdm.modules.datastore.bo;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.brainypdm.constants.ErrorCodes;
import org.brainypdm.dto.Host;
import org.brainypdm.dto.PerformanceData;
import org.brainypdm.dto.Service;
import org.brainypdm.dto.ServiceData;
import org.brainypdm.dto.ServiceStatus;
import org.brainypdm.dto.requests.FilterHost;
import org.brainypdm.dto.requests.PagingParams;
import org.brainypdm.exceptions.BaseException;
import org.brainypdm.modules.commons.log.BrainyLogger;
import org.brainypdm.modules.datastore.cache.DSCache;
import org.brainypdm.modules.datastore.cache.id.HostId;
import org.brainypdm.modules.datastore.constants.DSNamedQueries;
import org.brainypdm.modules.datastore.daoengine.dao.HostInterface;
import org.brainypdm.modules.datastore.daoengine.daomgr.DAOMgr;
import org.brainypdm.modules.datastore.interfaces.bo.DeviceBO;
import org.brainypdm.modules.datastore.interfaces.bo.ServiceBO;
import org.brainypdm.modules.datastore.main.DataStoreThreadImpl;
import org.hibernate.Criteria;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;

/*******************************************************************************
 * 
 * This is an HostBO implementation that include CRUD methods for manage Host
 * Definition
 * 
 * @author <a HREF="mailto:thomas@brainypdm.org">Thomas Buffagni</a>
 * 
 */
public class DeviceBOImpl extends DSBasicBO implements DeviceBO {

    private static final long serialVersionUID = -3044598969722218157L;

    private static BrainyLogger log = new BrainyLogger(DeviceBOImpl.class);

    public DeviceBOImpl() {
        log.debug("New instance created: " + this);
    }

    public void activateObject() {
        log.debug("activateObject: " + this);
    }

    public void destroyObject() {
        log.debug("destroyObject: " + this);
    }

    public void passivateObject() {
        log.debug("passivateObject: " + this);
    }

    public boolean validateObject() {
        log.debug("validateObject: " + this);
        return true;
    }

    /**
	 * update the hosts
	 * 
	 * @param hosts
	 *            the host list
	 * @throws BaseException
	 */
    public void updateHosts(Host[] hosts) throws BaseException {
        try {
            log.debug("***** updateHost");
            if (hosts == null) {
                log.warn("no host to update !");
                return;
            }
            for (Host host : hosts) {
                Session session = getSession();
                session.update(host);
                session.flush();
                final HostId id = new HostId();
                id.setId(host.getId());
                id.setName(host.getName());
                DSCache.getInstance().removeHost(host);
            }
            log.debug("Objects updated!");
        } catch (BaseException ex) {
            log.error(ex);
            throw ex;
        } catch (Exception ex) {
            BaseException b = new BaseException(ErrorCodes.CODE_517, ex.getLocalizedMessage());
            log.error(b, ex);
            throw b;
        } finally {
            log.debug("***** end - insertHost");
        }
    }

    public Long insertHost(Host host) throws BaseException {
        Long id = null;
        try {
            log.debug("***** start - insertHost");
            log.traceObjectAsDebug(host);
            Session session = getSession();
            Host myHost = getHostDef(host.getName());
            if (myHost == null) {
                session.save(host);
            } else {
                host.setId(myHost.getId());
            }
            id = host.getId();
            log.debug("Object saved!" + host);
        } catch (BaseException ex) {
            log.error(ex);
            throw ex;
        } catch (Exception ex) {
            BaseException b = new BaseException(ErrorCodes.CODE_517, ex.getLocalizedMessage());
            log.error(b, ex);
            throw b;
        } finally {
            log.debug("***** end - insertHost");
        }
        return id;
    }

    public void removeHost(Long id) throws BaseException {
        try {
            log.debug("***** start - removeHost");
            log.debug("deleteHost : " + id);
            Session session = getSession();
            Host device = this.getHost(id);
            session.delete(device);
            session.flush();
            log.debug("Object removed!");
        } catch (BaseException ex) {
            log.error(ex);
            throw ex;
        } catch (Exception ex) {
            BaseException b = new BaseException(ErrorCodes.CODE_520, ex.getLocalizedMessage());
            log.error(b, ex);
            throw b;
        } finally {
            log.debug("***** end - removeHost");
        }
    }

    public Host getHost(Long hostId) throws BaseException {
        Host retValue = null;
        try {
            log.debug("***** start - getHost");
            log.debug("getHost with id: " + hostId);
            Session session = getSession();
            retValue = (Host) session.get(Host.class, hostId);
        } catch (BaseException ex) {
            log.error(ex);
            throw ex;
        } catch (Exception ex) {
            String[] params = new String[] { String.valueOf(hostId), ex.getLocalizedMessage() };
            BaseException b = new BaseException(ErrorCodes.CODE_518, params);
            log.error(b, ex);
            throw b;
        } finally {
            log.traceObjectAsDebug(retValue);
            log.debug("***** end - getHost");
        }
        return retValue;
    }

    public Host getHostDef(String hostName) throws BaseException {
        Host retValue = null;
        try {
            log.debug("***** start - getHostByName");
            log.debug("getHost with name: " + hostName);
            Session session = getSession();
            Query query = session.getNamedQuery(DSNamedQueries.DEVICE_GET_HOST_DEF_BY_NAME);
            query.setParameter("name", hostName);
            List<?> hostList = query.list();
            if (!hostList.isEmpty()) {
                retValue = (Host) hostList.get(0);
            }
        } catch (BaseException ex) {
            log.error(ex);
            throw ex;
        } catch (Exception ex) {
            String[] params = new String[] { String.valueOf(hostName), ex.getLocalizedMessage() };
            BaseException b = new BaseException(ErrorCodes.CODE_530, params);
            log.error(b, ex);
            throw b;
        } finally {
            log.traceObjectAsDebug(retValue);
            log.debug("***** end - getHostByName");
        }
        return retValue;
    }

    public Host[] getHostList() throws BaseException {
        FilterHost filter = new FilterHost();
        filter.setWithService(true);
        filter.setWithServiceData(true);
        return getHostList(filter);
    }

    /**
	 * get the host list with all services and services data
	 */
    public Host[] getHostList(PagingParams paging) throws BaseException {
        FilterHost filter = new FilterHost();
        filter.setPaging(paging);
        filter.setWithService(true);
        filter.setWithServiceData(true);
        return getHostList(filter);
    }

    /**
	 * paging the result
	 * 
	 * @param FilterHost
	 *             
	 * @return the host list
	 * @throws BaseException
	 */
    public Host[] getHostList(FilterHost filter) throws BaseException {
        Host[] retValue = null;
        try {
            if (log.isDebugEnabled()) {
                log.debug("***** start - getHostList");
                log.debug("filter: " + filter);
            }
            Session session = getSession();
            Criteria criteria = session.createCriteria(Host.class, "host");
            criteria.setLockMode(LockMode.READ);
            criteria.addOrder(Order.asc("host.name"));
            List<Long> idlist = new ArrayList<Long>();
            if (filter.getPaging() != null) {
                Criteria criteriaP = session.createCriteria(Host.class);
                criteriaP.addOrder(Order.asc("name"));
                criteriaP.setFirstResult(filter.getPaging().getFirstResult());
                criteriaP.setMaxResults(filter.getPaging().getMaxResult());
                criteriaP.setCacheable(false);
                criteriaP.setProjection(Projections.projectionList().add(Projections.id(), "id").add(Projections.property("name"), "name"));
                if (filter.getOrderType() == FilterHost.ORDER_BY_HOST_NAME) {
                    criteriaP.addOrder(Order.asc("label").ignoreCase());
                }
                List<?> list = criteriaP.list();
                for (Iterator<?> iditer = list.iterator(); iditer.hasNext(); ) {
                    Object[] record = (Object[]) iditer.next();
                    idlist.add((Long) record[0]);
                }
                log.debug("idlist.size()=" + idlist.size());
                if (idlist.size() > 0) {
                    criteria.add(Expression.in("host.id", idlist));
                }
            }
            if (filter.isWithService() && !filter.isWithServiceData()) {
                criteria.createAlias("services", "services", Criteria.LEFT_JOIN);
                criteria.addOrder(Order.asc("services.name").ignoreCase());
            }
            if (filter.isWithService() && filter.isWithServiceData()) {
                criteria.createAlias("services", "services", Criteria.LEFT_JOIN);
                criteria.createAlias("services.serviceDataList", "serviceData", Criteria.LEFT_JOIN);
                criteria.addOrder(Order.asc("services.label").ignoreCase());
                criteria.addOrder(Order.asc("serviceData.label").ignoreCase());
            }
            if (filter.getOrderType() == FilterHost.ORDER_BY_HOST_NAME) {
                criteria.addOrder(Order.asc("host.label").ignoreCase());
            }
            criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
            List<?> list = criteria.list();
            log.debug("N. of instances: " + list.size());
            retValue = list.toArray(new Host[list.size()]);
            session.evict(list);
        } catch (BaseException ex) {
            log.error(ex);
            throw ex;
        } catch (Exception ex) {
            BaseException b = new BaseException(ErrorCodes.CODE_521, ex.getLocalizedMessage());
            log.error(b, ex);
            throw b;
        } finally {
            log.traceObjectAsDebug(retValue);
            log.debug("***** end - getHostList");
        }
        return retValue;
    }

    public boolean insertPerformanceData(Host device) throws BaseException {
        boolean out = false;
        ServiceBO serviceBO = null;
        try {
            log.debug("***** start - insertPerformanceData");
            log.trace(device);
            device.setName(device.getName().trim());
            Host hostDef = DSCache.getInstance().getHost(device.getName(), device);
            log.traceObjectAsDebug(hostDef);
            if (hostDef.isStoreable() && (device.getServices() != null)) {
                for (Service currentService : device.getServices()) {
                    currentService.setName(currentService.getName().trim());
                    serviceBO = (ServiceBO) super.getContext().getBusinessObjectByInterface(ServiceBO.class);
                    ServiceStatus status = currentService.getStatesArray()[0];
                    Service serviceDef = DSCache.getInstance().getService(hostDef, currentService, status.getCheckDate());
                    log.traceObjectAsDebug(serviceDef);
                    log.debug("hostDef=" + hostDef);
                    if (serviceDef.isStoreable()) {
                        log.debug("Saving service status...");
                        status.setIdService(serviceDef.getId());
                        serviceBO.insertServiceStatus(status);
                        log.debug("- start servicedata cycle -");
                        Set<ServiceData> sdList = currentService.getServiceDataList();
                        for (ServiceData sd : sdList) {
                            sd.setName(sd.getName().trim());
                            log.traceObjectAsDebug(sd);
                            ServiceData sdDef = DSCache.getInstance().getServiceData(serviceDef, sd);
                            log.debug(sdDef.getUom());
                            log.debug(sdDef.getUom().getTheBase());
                            if (!sdDef.getUom().getTheBase().equals(sd.getUom().getTheBase())) {
                                BaseException e = new BaseException(ErrorCodes.CODE_535, sdDef.getUom().getTheBase(), sd.getUom().getTheBase());
                                throw e;
                            }
                            if (sdDef.getStartTime().getTime() > status.getCheckDate().getTime()) {
                                sdDef.setStartTime(new Timestamp(status.getCheckDate().getTime()));
                                DataStoreThreadImpl.getInstance().saveUpdateServiceData(sdDef);
                            }
                            if (sdDef.isStoreable()) {
                                log.debug("- start performancedata cycle service data with id " + sdDef.getId() + " -");
                                for (PerformanceData pd : sd.getPerformanceDataList()) {
                                    pd.setCheckDate(status.getCheckDate());
                                    pd.setIdServiceData(sdDef.getId());
                                    pd.setDeviceId(hostDef.getId());
                                    pd.setServiceId(serviceDef.getId());
                                    pd.setBaseId(sd.getUom().getTheBase().getId().getId());
                                    pd.setCheckTimestamp(pd.getCheckDate().getTime());
                                    pd.setServiceDataName(sd.getName());
                                    serviceBO.insertPerformanceData(pd);
                                }
                                log.debug("- end performancedata cycle " + "-");
                            }
                        }
                        log.debug("- end servicedata cycle -");
                    }
                }
            }
            out = true;
        } catch (BaseException ex) {
            log.error(ex);
            throw ex;
        } catch (Exception ex) {
            BaseException e = new BaseException(ErrorCodes.CODE_533, ex);
            log.error(e, ex);
            throw e;
        } finally {
            super.getContext().returnBusinessObject(serviceBO);
            log.debug("***** end - insertPerformanceData");
        }
        return out;
    }

    public Host[] testDaoHost() throws BaseException {
        HostInterface hostDao = (HostInterface) DAOMgr.getDao(HostInterface.class);
        return hostDao.getHostList();
    }
}
