package org.wikiup.modules.ibatis.entity;

import java.util.HashMap;
import java.util.Map;
import org.wikiup.core.Wikiup;
import org.wikiup.core.impl.filter.TypeCastFilter;
import org.wikiup.core.inf.Attribute;
import org.wikiup.core.util.ValueUtil;

public class MapEntity extends AbstractEntity {

    private Map<String, Attribute> attributes = new HashMap<String, Attribute>();

    public MapEntity(String name, Map<String, Object> map) {
        super(name);
        for (String key : map.keySet()) attributes.put(key, new MapEntityAttribute(key, map));
    }

    public Attribute get(String name) {
        return attributes.get(name);
    }

    public Iterable<Attribute> getAttributes() {
        return attributes.values();
    }

    private static class MapEntityAttribute implements Attribute {

        private String name;

        private Map<String, Object> map;

        private MapEntityAttribute(String name, Map<String, Object> map) {
            this.name = name;
            this.map = map;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Object getObject() {
            return map.get(name);
        }

        public void setObject(Object obj) {
            Object org = map.get(name);
            map.put(name, (org != null && obj != null && !org.getClass().equals(obj.getClass())) ? Wikiup.getModel(TypeCastFilter.class).cast(org.getClass(), obj) : obj);
        }

        @Override
        public String toString() {
            return ValueUtil.toString(getObject());
        }
    }
}
