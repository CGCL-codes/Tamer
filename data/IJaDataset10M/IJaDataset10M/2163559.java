package seedpod.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import seedpod.exceptions.SeedpodException;
import seedpod.kb2db.ClsMap;
import seedpod.kb2db.ModelMap;
import seedpod.kb2db.SlotMap;
import seedpod.model.rdb.RdbValueType;
import seedpod.rdb.PMFactory;
import seedpod.rdb.PManager;
import seedpod.rdb.RdbConnection;
import seedpod.rdb.SqlUtil;

public class InstanceRelationship extends Relationship {

    class FKPair extends GenericPair {

        FKPair(Integer source, Integer target) {
            super(source, target);
        }

        @Override
        public Integer getSource() {
            return (Integer) super.getSource();
        }

        @Override
        public Integer getTarget() {
            return (Integer) super.getTarget();
        }
    }

    /**
	 * Creates an InstanceRelationship from an user entry
	 * 
	 * @param attribute
	 * @param obj
	 * @param userEntry
	 * @return
	 */
    public static InstanceRelationship createRelation(SlotMap attribute, SeedpodDO obj, Object userEntry) {
        if (userEntry instanceof InstanceRelationship) return (InstanceRelationship) userEntry; else if (userEntry instanceof String) try {
            Integer target = Integer.parseInt((String) userEntry);
            InstanceRelationship rel = new InstanceRelationship(attribute, obj);
            rel.addTarget(target);
            return rel;
        } catch (Exception e) {
            return null;
        } else if (userEntry instanceof Integer) {
            InstanceRelationship rel = new InstanceRelationship(attribute, obj);
            rel.addTarget((Integer) userEntry);
            return rel;
        } else return null;
    }

    private ArrayList<FKPair> _relations = new ArrayList<FKPair>();

    public InstanceRelationship(SlotMap slot) {
        super(slot);
    }

    public InstanceRelationship(SlotMap slot, int sourceOid) {
        super(slot, sourceOid);
    }

    public InstanceRelationship(SlotMap slot, SeedpodDO obj) {
        super(slot, obj);
    }

    public SlotMap getRelationshipSlot() {
        return _relationSlot;
    }

    public void addRelationship(int source, int target) {
        _relations.add(new FKPair(source, target));
    }

    public void addTarget(Integer targetOid) {
        _relations.add(new FKPair(_sourceOid, targetOid));
    }

    public void addTarget(SeedpodDO target) {
        _relations.add(new FKPair(_sourceOid, target.getOid()));
    }

    /**
	 * @return the _reifiedCls
	 */
    public ClsMap getReifiedCls() {
        return _reifiedCls;
    }

    /**
	 * @return the _relations
	 */
    public ArrayList<FKPair> getRelations() {
        return _relations;
    }

    public String getSourceClassName() {
        return _relationSlot.getDomainCls();
    }

    /**
	 * @return the _sourceObject
	 */
    public SeedpodDO getSourceObject() {
        return _sourceObject;
    }

    public ClsMap getTargetClass() {
        String allowedClsName = _relationSlot.getAllowedCls();
        return _relationSlot.getModelMap().getClsMap(allowedClsName);
    }

    public String getTargetClassName() {
        return getTargetClassName(_relationSlot);
    }

    /**
	 * fetch all of the target objects. note that this means the relationship object only keeps a list of target OIDs
	 * and not the actual objects
	 * 
	 * @return vector of seedpodDO.
	 */
    @Override
    public ArrayList<Object> getTargetObjects() {
        ArrayList<Object> targets = new ArrayList<Object>(_relations.size());
        PManager manager;
        try {
            manager = PMFactory.getInstance().createManager();
            for (FKPair rel : _relations) try {
                targets.add(manager.createObject(rel.getTarget()));
            } catch (NoObjectFoundException e) {
                continue;
            }
        } catch (SeedpodException e) {
            System.out.println("Cann't connect to PManager while getting target objects in InstanceRelationship");
        }
        return targets;
    }

    /**
	 * @return the _targetOID
	 */
    public Integer getTargetOid() {
        return _relations.get(0).getTarget();
    }

    /**
	 * @param index
	 * @return targetOId by index. Returns -1 if the index is invalid
	 */
    public Integer getTargetOid(int index) {
        if (index < _relations.size() && index > 0) return _relations.get(index).getTarget(); else return -1;
    }

    public ArrayList<Integer> getTargetOids() {
        ArrayList<Integer> targets = new ArrayList<Integer>(_relations.size());
        for (FKPair rel : _relations) targets.add(rel.getTarget());
        return targets;
    }

    public void init1to1Relationship(Integer srcOid) {
        _sourceOid = srcOid;
        if (_sourceObject != null) {
            AVPair targetFK = _sourceObject.getAttributeValue(_relationSlot.getName());
            if (targetFK != null) addTarget((Integer) targetFK.getValue());
        }
    }

    public void init1ToManyRelationship(Integer srcOid) {
        _sourceOid = srcOid;
        PManager manager;
        String q = null;
        RdbValueType dbType = _relationSlot.getRdbValueType();
        String inverseSlotName = getInverseSlotName(_relationSlot);
        try {
            manager = PMFactory.getInstance().createManager();
        } catch (SeedpodException e1) {
            return;
        }
        if (dbType.equals(RdbValueType.RELATION)) {
            String reifiedClsName = getTargetClassName(_relationSlot);
            ClsMap reifiedCls = manager.getModelMap().getClsMap(reifiedClsName);
            if (reifiedCls == null) return;
            SlotMap from = getFromSlot(reifiedCls);
            SlotMap to = getToSlot(reifiedCls);
            q = " SELECT " + SqlUtil.QQ(to.getName()) + " FROM " + SqlUtil.QQ(reifiedClsName) + " WHERE " + SqlUtil.QQ(from.getName()) + " = " + srcOid;
        } else if (inverseSlotName.length() > 0) {
            String fromClsName = _relationSlot.getAllowedCls();
            q = "SELECT " + SqlUtil.QQ("ID") + " FROM " + SqlUtil.QQ(fromClsName) + " WHERE " + SqlUtil.QQ(inverseSlotName) + "=" + srcOid;
        }
        if (q != null) try {
            RdbConnection conn = manager.getRdbConnection();
            ResultSet rs = conn.executeQuery(q);
            while (rs.next()) {
                Integer targetOid = rs.getInt(1);
                addRelationship(srcOid, targetOid);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        if (isOneToOne()) return getTargetOid() == null ? null : getTargetOid().toString(); else {
            String result = "{";
            for (Integer t : getTargetOids()) result += t + ", ";
            result = result.substring(0, result.lastIndexOf(",") - 1);
            result += "}";
            return result;
        }
    }

    /**
	 * Save the relationship to the database. This is a bit tricky because deleted relationships might not get saved
	 * properly.
	 */
    public void save() {
    }

    /**
	 * Returns the inverse of the relationship Slot. An inverse may not exist as in a one-to-one relationship An inverse
	 * is in reality where the srcOid is stored in the database. For example, in a one-to-many relationship, the
	 * inverseSlot is srcOid stored in the target table (allowed table)
	 * 
	 * @return SlotMap
	 */
    public SlotMap getSlotInverse() {
        ModelMap map = _relationSlot.getModelMap();
        SlotMap inverseSlot = null;
        if (isOneToOne()) return null;
        String inverseName = _relationSlot.getSlotInverse();
        if (inverseName != null && inverseName.length() > 0) {
            inverseSlot = getTargetClass().getSlotMap(inverseName);
        } else if (!_relationSlot.isMultiple()) {
            ClsMap target = getTargetClass();
            if (target.hasPrimaryKey()) return target.getSlotMap(target.getPrimaryKey());
        } else if (_relationSlot.getRdbValueType().equals(RdbValueType.RELATION)) {
            ClsMap implementingCls = map.getClsMap(_relationSlot.getRdbTargetName());
            inverseSlot = getFromSlot(implementingCls);
        }
        return inverseSlot;
    }

    /**
	 * Find candidate target objects for this relationship that's not already part of the relationship
	 * @return ArrayList of target oids
	 */
    public ArrayList<Integer> getPotentialTargetObjects() {
        ArrayList<Integer> potentialTargets = new ArrayList<Integer>();
        SlotMap m_inverseSlot = getSlotInverse();
        String sql = "";
        String filter = "";
        PManager manager;
        try {
            manager = PMFactory.getInstance().createManager();
        } catch (SeedpodException e1) {
            e1.printStackTrace();
            return new ArrayList<Integer>();
        }
        if (isOneToOne()) {
            init1to1Relationship(_sourceOid);
            if (getTargetObjects().size() > 0) filter = getTargetClass().getPrimaryKey() + "<>" + getTargetOid(0);
            sql = " SELECT " + SqlUtil.QQ(getTargetClass().getPrimaryKey()) + " FROM " + SqlUtil.QQ(getTargetClassName());
            sql += (filter.length() > 0) ? " WHERE " + filter : "";
            try {
                ResultSet rs = manager.getRdbConnection().executeQuery(sql);
                if (rs != null) while (rs.next()) potentialTargets.add(rs.getInt(1));
            } catch (SQLException e) {
                e.printStackTrace();
                return new ArrayList<Integer>();
            }
        } else if (m_inverseSlot.getDomainCls().equals(_relationSlot.getAllowedCls())) {
            filter = SqlUtil.QQ(m_inverseSlot.getName()) + " <> " + _sourceOid + " LIMIT 100 ";
            try {
                ArrayList<SeedpodDO> tObj = manager.query(getTargetClassName(), filter);
                for (SeedpodDO tmp : tObj) potentialTargets.add(tmp.getOid());
            } catch (SeedpodException e) {
                e.printStackTrace();
            }
        } else {
            String bridgeTableName = _relationSlot.getRdbTargetName();
            String allowedTableName = _relationSlot.getAllowedCls();
            ClsMap bridgeTable = _relationSlot.getModelMap().getClsMap(bridgeTableName);
            sql = " SELECT " + SqlUtil.QQ(allowedTableName) + "." + SqlUtil.QQ("ID") + " FROM " + SqlUtil.QQ(allowedTableName) + ", " + SqlUtil.QQ(bridgeTableName) + " WHERE " + SqlUtil.QQ(bridgeTableName) + "." + SqlUtil.QQ(getToSlot(bridgeTable).getName()) + "<>" + SqlUtil.QQ(allowedTableName) + "." + SqlUtil.QQ("ID");
            try {
                ResultSet rs = manager.getRdbConnection().executeQuery(sql);
                if (rs != null) while (rs.next()) potentialTargets.add(rs.getInt(1));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return potentialTargets;
    }
}
