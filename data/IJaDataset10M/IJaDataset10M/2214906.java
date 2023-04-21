package edu.vt.eng.swat.workflow.db.base.entity;

import edu.vt.eng.swat.workflow.db.base.DBType;
import edu.vt.eng.swat.workflow.db.base.cache.Cacheable;

public class Category1 implements DescriptionEntity<Long>, Cacheable<Category1> {

    private Long id;

    private String description;

    @Override
    public DBType getIdType() {
        return DBType.LONG;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int compareTo(Category1 o) {
        if (o == null) return 1;
        if (o.getId() == null || getId() == null) {
            throw new RuntimeException("Category1 with NULL 'id' is compared");
        }
        return getId().compareTo(o.getId());
    }
}
