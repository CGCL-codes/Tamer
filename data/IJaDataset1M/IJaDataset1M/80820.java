package net.sourceforge.domian.entity;

/**
 * Marker interface for all <i>transient</i> entity objects.
 * <p/>
 * Transient entities are objects that definitively are entities by definition, but are transient in nature.
 * Typical examples are "timestamped value objects" and entities deducted/generated from other entities.
 * <p/>
 * <i>Persisting transient entities should as far as possible, be avoided!</i>
 *
 * @author Eirik Torske
 * @since 0.4
 */
public interface TransientEntity extends Entity {
}
