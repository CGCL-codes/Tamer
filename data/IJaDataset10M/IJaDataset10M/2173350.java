package cn.vlabs.clb.security.permission;

public class EntityImpl implements Entity {

    private String entityName;

    public String getName() {
        return entityName;
    }

    public void setName(String entity) {
        entityName = entity;
    }

    public void Entity(String entity) {
        entityName = entity;
    }
}
