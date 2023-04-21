package bma.bricks.otter.model.attribute.custom.manager;

import bma.bricks.hibernate.DomainSessionSource;
import bma.bricks.json.JSONObject;
import bma.bricks.json.JSONParser;
import bma.bricks.json.impl.JSONObjectImpl;
import bma.bricks.otter.model.attribute.custom.EACustomCoreDomain;
import bma.bricks.otter.model.attribute.custom.database.EACustom;
import bma.bricks.util.T;

public class EACustomManager extends DomainSessionSource {

    private static EACustomManager instance = new EACustomManager();

    public static EACustomManager getInstance() {
        return instance;
    }

    public EACustomManager() {
        super(EACustomCoreDomain.class);
    }

    public void save(String eid, JSONObject json) {
        save(eid, json == null ? "" : json.toJSONString());
    }

    public void save(String eid, String value) {
        EACustom po = get(eid);
        if (po == null) {
            po = new EACustom();
            po.setEntityId(eid);
            po.setValue(value);
            super.saveObject(po);
        } else {
            po.setValue(value);
            super.updateObject(po);
        }
    }

    public boolean delete(String id) {
        return super.deleteByPK(EACustom.class, id);
    }

    public EACustom get(String eid) {
        EACustom o = super.get(EACustom.class, eid);
        return o;
    }

    public JSONObject getAttribute(String eid) {
        EACustom o = get(eid);
        if (o == null) {
            return null;
        }
        String v = o.getValue();
        if (T.notEmpty(v)) {
            return new JSONParser(o.getValue()).nextObject();
        }
        return new JSONObjectImpl();
    }
}
