package br.ufmg.ubicomp.decs.server.eventservice.manager;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import br.ufmg.ubicomp.decs.server.eventservice.entity.AbstractEntity;

public class ResponseObject {

    private Map<String, Set<AbstractEntity>> objs = new HashMap<String, Set<AbstractEntity>>();

    public void addObject(String type, AbstractEntity obj) {
        Set<AbstractEntity> list = getObjList(type);
        list.add(obj);
    }

    public Set<AbstractEntity> getObjList(String type) {
        Set<AbstractEntity> list = objs.get(type);
        if (list == null) {
            list = new HashSet<AbstractEntity>();
            objs.put(type, list);
        }
        return list;
    }

    public Map<String, Set<AbstractEntity>> getObjs() {
        return Collections.unmodifiableMap(objs);
    }

    public void addObjects(AbstractEntity... entities) {
        for (AbstractEntity e : entities) {
            Set<AbstractEntity> list = getObjList(e.getClass().getSimpleName());
            list.add(e);
        }
    }
}
