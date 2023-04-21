package gleam.executive.service.impl;

import java.util.ArrayList;
import java.util.List;
import gleam.executive.dao.LookupDao;
import gleam.executive.model.LabelValue;
import gleam.executive.model.Role;
import gleam.executive.model.Service;
import gleam.executive.service.LookupManager;

/**
 * Implementation of LookupManager interface to talk to the persistence
 * layer.
 * 
 * <p>
 * <a href="LookupManagerImpl.java.html"><i>View Source</i></a>
 * </p>
 * 
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 */
public class LookupManagerImpl extends BaseManager implements LookupManager {

    private LookupDao dao;

    public void setLookupDao(LookupDao dao) {
        super.dao = dao;
        this.dao = dao;
    }

    /**
   * @see gleam.executive.service.LookupManager#getAllRoles()
   */
    public List getRoles() {
        List roles = dao.getRoles();
        List<LabelValue> list = new ArrayList<LabelValue>();
        Role role = null;
        for (int i = 0; i < roles.size(); i++) {
            role = (Role) roles.get(i);
            list.add(new LabelValue(role.getName(), role.getName()));
        }
        return list;
    }

    /**
   * @see gleam.executive.service.LookupManager#getAllRoles()
   */
    public List getAllRoles() {
        List roles = dao.getAllRoles();
        List<LabelValue> list = new ArrayList<LabelValue>();
        Role role = null;
        for (int i = 0; i < roles.size(); i++) {
            role = (Role) roles.get(i);
            list.add(new LabelValue(role.getName(), role.getName()));
        }
        return list;
    }

    /**
   * @see gleam.executive.service.LookupManager#getAllServices()
   */
    public List getAllServices() {
        List services = dao.getServices();
        List<LabelValue> list = new ArrayList<LabelValue>();
        Service service = null;
        for (int i = 0; i < services.size(); i++) {
            service = (Service) services.get(i);
            list.add(new LabelValue(service.getName(), service.getId().toString()));
        }
        return list;
    }
}
