package ru.amse.soultakov.ereditor.model;

import static ru.amse.soultakov.ereditor.util.CommonUtils.newHashMap;
import static ru.amse.soultakov.ereditor.util.CommonUtils.newLinkedHashSet;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * @author sma
 * 
 */
public class Entity implements Iterable<AbstractAttribute>, ICopyable<Entity> {

    private static final String PARAM_CANT_BE_NULL = "Param must be non null value";

    private static final String RELATIONSHIP_CANT_BE_NULL = "Relationship can't be null";

    private final Set<AbstractAttribute> attributes = newLinkedHashSet();

    ;

    private final Constraint<AbstractAttribute> primaryKey = new Constraint<AbstractAttribute>();

    private final Set<Constraint<FKAttribute>> foreignKeys = newLinkedHashSet();

    private final Set<Constraint<AbstractAttribute>> uniqueAttributes = newLinkedHashSet();

    private final Set<Relationship> relationships = newLinkedHashSet();

    private final Set<Link> links = newLinkedHashSet();

    private String name;

    /**
     * @param name
     */
    public Entity(String name) {
        setName(name);
    }

    /**
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     */
    public void setName(String name) {
        if (name == null) {
            throw new IllegalArgumentException(PARAM_CANT_BE_NULL);
        }
        this.name = name;
    }

    public Iterator<AbstractAttribute> iterator() {
        return attributes.iterator();
    }

    /**
     * @param attribute
     */
    public void addAttribute(AbstractAttribute attribute) {
        if (attribute == null) {
            throw new IllegalArgumentException("Attribute can't be null");
        }
        attributes.add(attribute);
    }

    /**
     * @param attribute
     * @return
     */
    public boolean removeAttribute(AbstractAttribute attribute) {
        primaryKey.remove(attribute);
        for (Constraint<FKAttribute> c : foreignKeys) {
            c.remove(attribute);
        }
        for (Constraint<AbstractAttribute> c : uniqueAttributes) {
            c.remove(attribute);
        }
        return attributes.remove(attribute);
    }

    public boolean addRelationship(Relationship relationship) {
        if (relationship == null) {
            throw new IllegalArgumentException(RELATIONSHIP_CANT_BE_NULL);
        }
        return relationships.add(relationship);
    }

    public boolean removeRelationship(Relationship relationship) {
        return relationships.remove(relationship);
    }

    public Iterator<Relationship> relationshipsIterator() {
        return relationships.iterator();
    }

    public boolean addLink(Link link) {
        if (link == null) {
            throw new IllegalArgumentException(RELATIONSHIP_CANT_BE_NULL);
        }
        return links.add(link);
    }

    public boolean removeLink(Link link) {
        return links.remove(link);
    }

    public Iterator<Link> linksIterator() {
        return links.iterator();
    }

    @Override
    public String toString() {
        return name;
    }

    public Collection<AbstractAttribute> getAttributes() {
        return Collections.unmodifiableSet(attributes);
    }

    public Collection<Link> getLinks() {
        return Collections.unmodifiableSet(links);
    }

    public Collection<Relationship> getRelationships() {
        return Collections.unmodifiableSet(relationships);
    }

    public boolean acceptRelationshipWith(Entity entity) {
        for (Relationship r : relationships) {
            if ((r.getFirstEnd().getEntity().equals(entity)) || (r.getSecondEnd().getEntity().equals(entity))) {
                return false;
            }
        }
        return true;
    }

    public boolean acceptLinkWith(Comment comment) {
        for (Link l : links) {
            if (l.getComment() == comment) {
                return false;
            }
        }
        return true;
    }

    public void addToPrimaryKey(AbstractAttribute attribute) {
        primaryKey.add(attribute);
        attributes.add(attribute);
    }

    public Collection<AbstractAttribute> getAttributesExceptPK() {
        Set<AbstractAttribute> set = newLinkedHashSet(attributes);
        set.removeAll(primaryKey.getAttributes());
        return set;
    }

    public Constraint<AbstractAttribute> getPrimaryKey() {
        return primaryKey;
    }

    public boolean removeFromPrimaryKey(AbstractAttribute attribute) {
        return primaryKey.remove(attribute);
    }

    public Collection<Constraint<AbstractAttribute>> getUniqueAttributes() {
        return Collections.unmodifiableSet(uniqueAttributes);
    }

    public void addToUniqueAttributes(Set<AbstractAttribute> set) {
        attributes.addAll(set);
        uniqueAttributes.add(new Constraint<AbstractAttribute>(set));
    }

    public void addForeignKey(Constraint<FKAttribute> fk) {
        for (FKAttribute fka : fk) {
            if (!attributes.contains(fka)) {
                throw new IllegalArgumentException("Attributes from new FK must present in entity");
            }
        }
        foreignKeys.add(fk);
    }

    public boolean removeFromUniqueAttributes(Set<Attribute> set) {
        return uniqueAttributes.remove(set);
    }

    public Collection<Constraint<FKAttribute>> getForeignKeys() {
        return Collections.unmodifiableSet(foreignKeys);
    }

    public Entity copy() {
        Entity entity = new Entity("Copy of " + this.name);
        Map<AbstractAttribute, AbstractAttribute> map = newHashMap();
        for (AbstractAttribute a : this) {
            AbstractAttribute copy = a.copy();
            map.put(a, copy);
            entity.addAttribute(copy);
        }
        for (AbstractAttribute a : this.getPrimaryKey()) {
            entity.addToPrimaryKey(map.get(a));
        }
        for (Constraint<AbstractAttribute> c : this.getUniqueAttributes()) {
            Set<AbstractAttribute> set = newLinkedHashSet();
            for (AbstractAttribute a : c) {
                set.add(map.get(a));
            }
            entity.addToUniqueAttributes(set);
        }
        return entity;
    }

    public Constraint<AbstractAttribute> getUniqueConstraintFor(AbstractAttribute attribute) {
        for (Constraint<AbstractAttribute> c : uniqueAttributes) {
            if (c.contains(attribute)) {
                return c;
            }
        }
        return null;
    }

    public Constraint<FKAttribute> getForeignKeyConstraintFor(AbstractAttribute attribute) {
        for (Constraint<FKAttribute> c : foreignKeys) {
            if (c.contains(attribute)) {
                return c;
            }
        }
        return null;
    }

    /**
	 * @param constraint
	 */
    public void removeForeignKey(Constraint<? extends AbstractAttribute> constraint) {
        foreignKeys.remove(constraint);
    }
}
