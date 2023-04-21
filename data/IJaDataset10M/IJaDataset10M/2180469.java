package net.toften.jlips2;

import java.util.Collection;
import java.util.Properties;
import net.toften.jlips2.entity.EntityFactory;
import net.toften.jlips2.entity.EntityManager;
import net.toften.jlips2.record.ID;

/**
 * The main class
 * @author thomaslarsen
 * @version 1.0
 * @created 03-Oct-2005 20:29:22
 */
public class Lifecycle {

    private static EntityFactory entityFactory = null;

    /**
	 * 
	 * @param prop    prop
	 * @throws Exception 
	 */
    public static void init(Properties prop) throws Exception {
        if (entityFactory == null) entityFactory = LayerFactory.initialiseFactory(EntityFactory.PROP_ENTITYFACTORY, LayerFactory.DEFAULT_ENTITYFACTORY, prop, EntityFactory.class); else entityFactory.init(prop);
    }

    /**
	 * Creates a new entity
	 * @param entity
	 * @return
	 * 
	 * @param T    T
	 */
    public static <T extends Entity> T create(Class<T> entityClass) {
        EntityManager em = entityFactory.getEntityManager(entityClass);
        return em.create();
    }

    /**
	 * 
	 * @param T
	 */
    public static <T extends Entity> Collection<T> findAll(Class<T> entityClass) {
        return null;
    }

    /**
	 * 
	 * @param id
	 * @param T    T
	 */
    public static <T extends Entity> T find(ID id, Class<T> entityClass) {
        EntityManager em = entityFactory.getEntityManager(entityClass);
        return em.find(id);
    }
}
