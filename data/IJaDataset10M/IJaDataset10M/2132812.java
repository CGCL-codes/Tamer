package su.nsk.inp.roentgen.service.impl;

import java.util.List;
import java.util.Map;
import su.nsk.inp.roentgen.dao.HierarchicalObjectDao;
import su.nsk.inp.roentgen.model.HierarchicalObject;
import su.nsk.inp.roentgen.service.HierarchicalObjectManager;

public class HierarchicalObjectManagerImpl implements HierarchicalObjectManager {

    private Map<String, HierarchicalObjectDao<HierarchicalObject>> _map;

    public void setMap(Map<String, HierarchicalObjectDao<HierarchicalObject>> map) {
        _map = map;
    }

    public HierarchicalObject get(String type, Long id) {
        return _map.get(type).get(id);
    }

    public List<HierarchicalObject> getAll(String type) {
        return _map.get(type).getAll();
    }

    public HierarchicalObject getByName(String type, String name) {
        List<HierarchicalObject> list = _map.get(type).getAll(String.format("from %s where name = '%s'", type, name));
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    public List<HierarchicalObject> getChilds(String type, HierarchicalObject parent) {
        return _map.get(type).getAll(String.format("from %s where parent.id = %s", type, parent.getId()));
    }

    public List<HierarchicalObject> getLikeName(String type, String name) {
        return _map.get(type).getAll(String.format("from %s where name like '%s%%'", type, name));
    }

    public void remove(String type, Long id) {
        _map.get(type).remove(id);
    }

    public HierarchicalObject save(String type, HierarchicalObject hobject) {
        return _map.get(type).save(hobject);
    }

    public HierarchicalObject saveChain(String type, HierarchicalObject hobject) {
        return _map.get(type).saveChain(hobject);
    }
}
