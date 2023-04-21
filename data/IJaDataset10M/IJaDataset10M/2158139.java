package candy.core.entity.eav;

import candy.core.entity.IEntity;

public final class Eav_Entity_Store implements IEntity {

    private long entity_Store_ID;

    private long entity_Type_ID;

    private long store_ID;

    private String increment_Perfix;

    private String increment_Last_ID;

    public long getEntity_Store_ID() {
        return entity_Store_ID;
    }

    public void setEntity_Store_ID(long entity_Store_ID) {
        this.entity_Store_ID = entity_Store_ID;
    }

    public long getEntity_Type_ID() {
        return entity_Type_ID;
    }

    public void setEntity_Type_ID(long entity_Type_ID) {
        this.entity_Type_ID = entity_Type_ID;
    }

    public long getStore_ID() {
        return store_ID;
    }

    public void setStore_ID(long store_ID) {
        this.store_ID = store_ID;
    }

    public String getIncrement_Perfix() {
        return increment_Perfix;
    }

    public void setIncrement_Perfix(String increment_Perfix) {
        this.increment_Perfix = increment_Perfix;
    }

    public String getIncrement_Last_ID() {
        return increment_Last_ID;
    }

    public void setIncrement_Last_ID(String increment_Last_ID) {
        this.increment_Last_ID = increment_Last_ID;
    }

    @Override
    public void setKeyValue(long value) {
        this.entity_Store_ID = (Long) value;
    }

    @Override
    public long getKeyValue() {
        return entity_Store_ID;
    }
}
