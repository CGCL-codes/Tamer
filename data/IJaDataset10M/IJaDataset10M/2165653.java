package fr.univ_tln.inf9.exaplanning.api.db;

/**
 * Encapsulation d'un objet persistant dans une collection du gestionnaire de session.
 * Une entitée est de 3 types différents, cf EntityType.
 * @author emilien
 *
 */
public class Entity {

    private final Object o;

    private final EntityType type;

    protected Entity(Object o, EntityType type) {
        super();
        this.o = o;
        this.type = type;
    }

    public Object getObject() {
        return o;
    }

    public EntityType getType() {
        return type;
    }
}
