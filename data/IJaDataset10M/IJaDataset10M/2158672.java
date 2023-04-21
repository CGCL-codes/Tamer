package model;

import java.lang.Boolean;
import java.lang.Integer;
import java.lang.String;

/**
 * @author generated
 */
public class Blomomapping extends blomo.entity.BLoMoEntity {

    @blomo.validator.Validator(validator = blomo.validator.ValidatorNotNullOrEmpty.class, message = "Blomomapping needs name to be set.", result = blomo.validator.BLoMoResults.issue)
    private String name;

    private Integer id;

    @blomo.validator.Validator(validator = blomo.validator.ValidatorNotNullOrEmpty.class, message = "Blomomapping needs mapping to be set.", result = blomo.validator.BLoMoResults.issue)
    private String mapping;

    @blomo.validator.Validator(validator = blomo.validator.ValidatorNotNullOrEmpty.class, message = "Blomomapping needs whitelist to be set.", result = blomo.validator.BLoMoResults.issue)
    private Boolean whitelist = true;

    @blomo.validator.Validator(validator = blomo.validator.ValidatorNotNullOrEmpty.class, message = "Blomomapping needs blomoDtoRole to be set.", result = blomo.validator.BLoMoResults.issue)
    private model.Blomodtorole blomoDtoRole;

    private model.Blomoflow blomoFlow;

    /**
	 * generated method
	 * @return undefined
	 */
    public String getMapping() {
        return mapping;
    }

    /**
	 * generated method
	 * @return undefined
	 */
    public java.io.Serializable getEntityId() {
        return getId();
    }

    /**
	 * generated method
	 * @return undefined
	 */
    public Integer getId() {
        return id;
    }

    /**
	 * generated method
	 * @return undefined
	 */
    public int hashCode() {
        if (getId() == null) return super.hashCode();
        return getId().hashCode();
    }

    /**
	 * generated method
	 * @param id undefined
	 */
    protected void setId(Integer id) {
        this.id = id;
    }

    /**
	 * generated method
	 * @param blomoDtoRole undefined
	 */
    public void setBlomoDtoRole(model.Blomodtorole blomoDtoRole) {
        this.blomoDtoRole = blomoDtoRole;
    }

    /**
	 * generated method
	 * @param blomoFlow undefined
	 */
    public void setBlomoFlow(model.Blomoflow blomoFlow) {
        this.blomoFlow = blomoFlow;
    }

    /**
	 * generated method
	 * @param mapping undefined
	 */
    public void setMapping(String mapping) {
        this.mapping = mapping;
    }

    /**
	 * generated method
	 * @return undefined
	 */
    public String getName() {
        return name;
    }

    /**
	 * generated method
	 * @return undefined
	 */
    public model.Blomoflow getBlomoFlow() {
        return blomoFlow;
    }

    /**
	 * generated method
	 * @return undefined
	 */
    public Boolean getWhitelist() {
        return whitelist;
    }

    /**
	 * generated method
	 * @return undefined
	 */
    public model.Blomodtorole getBlomoDtoRole() {
        return blomoDtoRole;
    }

    /**
	 * generated method
	 */
    public void preDelete() {
        if (getBlomoFlow() != null) getBlomoFlow().getBlomoMappings().remove(this);
        if (getBlomoDtoRole() != null) getBlomoDtoRole().getBlomomapping().remove(this);
    }

    /**
	 * generated method
	 * @param obj undefined
	 * @return undefined
	 */
    public boolean equals(Object obj) {
        if (getId() == null || obj == null) return super.equals(obj);
        if (obj instanceof Blomomapping) return getId().equals(((Blomomapping) obj).getId());
        return super.equals(obj);
    }

    /**
	 * generated method
	 * @param whitelist undefined
	 */
    public void setWhitelist(Boolean whitelist) {
        this.whitelist = whitelist;
    }

    /**
	 * generated method
	 * @param name undefined
	 */
    public void setName(String name) {
        this.name = name;
    }
}
