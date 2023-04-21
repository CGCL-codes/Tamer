package de.objectcode.time4u.android.entities.impl;

import com.j256.ormlite.field.DatabaseField;
import de.objectcode.time4u.android.entities.IEntity;

public class BaseEntity implements IEntity {

    @DatabaseField(generatedId = true)
    private int id;

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof BaseEntity) {
            BaseEntity entity = (BaseEntity) obj;
            if (entity.getId() == this.getId()) {
                return true;
            }
            return false;
        }
        return super.equals(obj);
    }
}
