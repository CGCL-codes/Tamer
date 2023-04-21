package org.sodeja.rel.relations;

import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import org.sodeja.collections.SetUtils;
import org.sodeja.functional.Function1;
import org.sodeja.rel.AttributeValue;
import org.sodeja.rel.Entity;
import org.sodeja.rel.Relation;

public class ProjectAwayRelation extends UnaryRelation {

    protected final Set<String> attributes;

    public ProjectAwayRelation(String name, Relation relation, String... attributes) {
        this(name, relation, SetUtils.asSet(attributes));
    }

    public ProjectAwayRelation(String name, Relation relation, Set<String> attributes) {
        super(name, relation);
        this.attributes = new TreeSet<String>(attributes);
    }

    @Override
    public Set<Entity> select() {
        Set<Entity> entities = right.select();
        return SetUtils.map(entities, new Function1<Entity, Entity>() {

            @Override
            public Entity execute(Entity p) {
                Set<AttributeValue> vals = p.getValues();
                SortedSet<AttributeValue> filtered = new TreeSet<AttributeValue>();
                for (AttributeValue val : vals) {
                    if (!shouldRemove(val)) {
                        filtered.add(val);
                    }
                }
                return new Entity(filtered);
            }
        });
    }

    protected boolean shouldRemove(AttributeValue val) {
        return attributes.contains(val.attribute.name);
    }
}
