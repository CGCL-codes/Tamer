package org.osmius.service.impl;

import org.osmius.dao.jdbc.OsmAggrupationDaoJDBC;
import org.osmius.dao.hibernate.OsmAggrupationDaoHibernate;
import org.osmius.service.OsmAggrupationManager;
import org.osmius.model.OsmAggrupation;
import java.util.List;

public class OsmAggrupationManagerImpl extends BaseManager implements OsmAggrupationManager {

    private OsmAggrupationDaoJDBC osmAggrupationDaoJDBC;

    private OsmAggrupationDaoHibernate osmAggrupationDao;

    public void setOsmAggrupationDao(OsmAggrupationDaoHibernate osmAggrupationDao) {
        this.osmAggrupationDao = osmAggrupationDao;
    }

    public OsmAggrupationDaoJDBC getOsmAggrupationDaoJDBC() {
        return osmAggrupationDaoJDBC;
    }

    public void setOsmAggrupationDaoJDBC(OsmAggrupationDaoJDBC osmAggrupationDaoJDBC) {
        this.osmAggrupationDaoJDBC = osmAggrupationDaoJDBC;
    }

    public List getAggrupationsCount(int type, String tag) {
        return osmAggrupationDaoJDBC.getAggrupationsCount(type, tag);
    }

    public List getAggrupations(String aggrupation) {
        return osmAggrupationDaoJDBC.getAggrupations(aggrupation);
    }

    public List getAggrupations() {
        return osmAggrupationDaoJDBC.getAggrupations();
    }

    public void saveOrUpdateAggr(int type, String el, String aggr) {
        OsmAggrupation osmAggrupation = new OsmAggrupation();
        osmAggrupation.setDesAggrupation(aggr);
        Long id = null;
        try {
            osmAggrupationDaoJDBC.saveOsmAggrupationAndRelated(type, el, aggr);
        } catch (Exception e) {
        }
    }

    public String removeAggrupationAndRelated(int type, String el, String aggr) {
        return osmAggrupationDaoJDBC.removeAggrupationAndRelated(type, el, aggr);
    }

    public Long getServiceAggrupation(String idnService) {
        return osmAggrupationDaoJDBC.getServiceAggrupation(idnService);
    }

    public Long saveAggrupation(String desAggrupation) {
        Long id = null;
        try {
            id = osmAggrupationDao.saveAggrupation(desAggrupation);
        } catch (Exception e) {
        }
        return id;
    }

    public void removeAggrupation(Long idnAggrupation) {
        osmAggrupationDao.removeAggrupation(idnAggrupation);
    }

    public List getServices(Long idnAggrupation) {
        return osmAggrupationDao.getServices(idnAggrupation);
    }

    public void removeServices(Long idnAggrupation) {
        osmAggrupationDao.removeServices(idnAggrupation);
    }

    public void applyServices(Long idnAggrupation, String[] services) {
        osmAggrupationDao.applyServices(idnAggrupation, services);
    }

    public List getAvailableServices(Long idnAggrupation) {
        return osmAggrupationDao.getAvailableServices(idnAggrupation);
    }

    public List getAvailableServices(Long idnAggrupation, String service) {
        return osmAggrupationDao.getAvailableServices(idnAggrupation, service);
    }

    public void removeServiceAggrupation(String idnService) {
        osmAggrupationDaoJDBC.removeServiceAggrupation(idnService);
    }

    public Boolean assingService(String aggr, String service) {
        return osmAggrupationDaoJDBC.assingService(aggr, service);
    }

    public Integer getAggrupationStatus(String desAggrupation) {
        return osmAggrupationDaoJDBC.getAggrupationStatus(desAggrupation);
    }

    public List getServicesByAggrupation(String aggr) {
        return osmAggrupationDaoJDBC.getServicesByAggrupation(aggr);
    }

    public List getServicesByAggrupationAndSla(String aggr, String sla) {
        return osmAggrupationDaoJDBC.getServicesByAggrupationAndSla(aggr, sla);
    }

    public List getServicesByAggrupationAndService(String aggr, String service) {
        return osmAggrupationDaoJDBC.getServicesByAggrupationAndService(aggr, service);
    }

    public List getServicesByAggrupationAndSlaAndService(String aggr, String sla, String service) {
        return osmAggrupationDaoJDBC.getServicesByAggrupationAndSlaAndService(aggr, sla, service);
    }
}
