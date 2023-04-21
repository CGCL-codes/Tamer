package ar.com.temporis.framework.repository;

import java.util.List;
import ar.com.temporis.framework.domain.Entity;

/**
 * 
 * @author matias.sulik
 * 
 */
public interface Repository {

    public List<? extends Entity> findAll();

    public List<? extends Entity> findByAttribute(String attributeName, Object value);

    public Entity getAny();

    public void add(Entity entity);

    public void remove(Entity entity);

    public void update(Entity entity);
}
