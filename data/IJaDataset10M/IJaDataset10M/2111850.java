package appengine.entity.customer;

import java.util.Date;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import candy.core.entity.IEntity;
import candy.core.entity.customer.Entity;
import appengine.AppEngine_Eav_IEntity;
import candy.utils.NumberUtils;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class AppEngine_Entity implements AppEngine_Eav_IEntity {

    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Long entity_ID;

    @Persistent
    private long entity_Type_ID;

    @Persistent
    private long attribute_Set_ID;

    @Persistent
    private String increment_ID;

    @Persistent
    private long store_ID;

    @Persistent
    private Date created_At;

    @Persistent
    private Date updated_At;

    @Persistent
    private boolean active;

    @Persistent
    private long website_ID;

    @Persistent
    private String email;

    @Persistent
    private long group_ID;

    @Override
    public Object getKey() {
        return entity_ID;
    }

    @Override
    public void EavToAppEngineEav(IEntity entity) {
        if (entity instanceof Entity) {
            Entity customer = (Entity) entity;
            if (customer.getEntity_ID() > 0) this.entity_ID = NumberUtils.longToLong(customer.getEntity_ID());
            this.entity_Type_ID = customer.getEntity_Type_ID();
            this.attribute_Set_ID = customer.getAttribute_Set_ID();
            this.increment_ID = customer.getIncrement_ID();
            this.store_ID = customer.getStore_ID();
            this.created_At = customer.getCreated_At();
            this.updated_At = customer.getUpdated_At();
            this.active = customer.getActive();
            this.website_ID = customer.getWebsite_ID();
            this.email = customer.getEmail();
            this.group_ID = customer.getGroup_ID();
        }
    }

    @Override
    public void AppEngineEavToEav(IEntity entity) {
        if (entity instanceof Entity) {
            Entity customer = (Entity) entity;
            customer.setEntity_ID(entity_ID.longValue());
            customer.setEntity_Type_ID(entity_Type_ID);
            customer.setAttribute_Set_ID(attribute_Set_ID);
            customer.setIncrement_ID(increment_ID);
            customer.setStore_ID(store_ID);
            customer.setCreated_At(created_At);
            customer.setUpdated_At(updated_At);
            customer.setActive(active);
            customer.setWebsite_ID(website_ID);
            customer.setEmail(email);
            customer.setGroup_ID(group_ID);
        }
    }

    @Override
    public IEntity AppEngineEavToEav() {
        Entity customer = new Entity();
        customer.setEntity_ID(entity_ID.longValue());
        customer.setEntity_Type_ID(entity_Type_ID);
        customer.setAttribute_Set_ID(attribute_Set_ID);
        customer.setIncrement_ID(increment_ID);
        customer.setStore_ID(store_ID);
        customer.setCreated_At(created_At);
        customer.setUpdated_At(updated_At);
        customer.setActive(active);
        customer.setWebsite_ID(website_ID);
        customer.setEmail(email);
        customer.setGroup_ID(group_ID);
        return customer;
    }
}
