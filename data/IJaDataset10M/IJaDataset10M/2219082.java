package org.cmsuite2.model.store;

import java.util.ArrayList;
import java.util.List;
import org.cmsuite2.model.AbstractDAO;
import org.cmsuite2.model.employee.Employee;

public class StoreDAO extends AbstractDAO<Store> {

    public StoreDAO() {
        super(Store.class);
    }

    @SuppressWarnings("unchecked")
    public List<Store> findAvailable(long idToLoad) {
        List<Store> list = new ArrayList<Store>();
        List<Store> stores = (List<Store>) getJpaTemplate().find("from Store order by name asc");
        if (stores != null && stores.size() > 0) for (Store store : stores) {
            boolean add = true;
            List<Employee> employees = store.getEmployees();
            if (employees != null && employees.size() > 0) for (Employee employee : employees) if (employee.getId() == idToLoad) add = false;
            if (add) list.add(store);
        }
        return list;
    }

    @SuppressWarnings("unchecked")
    public List<Store> findChosen(long idToLoad) {
        List<Store> list = new ArrayList<Store>();
        List<Store> stores = (List<Store>) getJpaTemplate().find("from Store order by name asc");
        if (stores != null && stores.size() > 0) for (Store store : stores) {
            boolean add = false;
            List<Employee> employees = store.getEmployees();
            if (employees != null && employees.size() > 0) for (Employee employee : employees) if (employee.getId() == idToLoad) add = true;
            if (add) list.add(store);
        }
        return list;
    }
}
