package org.sodeja.rel.relations;

import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import org.sodeja.collections.SetUtils;
import org.sodeja.functional.Function1;
import org.sodeja.rel.AttributeValue;
import org.sodeja.rel.CalculatedAttribute;
import org.sodeja.rel.Entity;
import org.sodeja.rel.Relation;

public class ExtendRelation extends UnaryRelation {

    protected final CalculatedAttribute[] attributes;

    public ExtendRelation(String name, Relation relation, CalculatedAttribute... attributes) {
        super(name, relation);
        this.attributes = attributes;
    }

    @Override
    public Set<Entity> select() {
        Set<Entity> entities = right.select();
        return SetUtils.map(entities, new Function1<Entity, Entity>() {

            @Override
            public Entity execute(Entity p) {
                SortedSet<AttributeValue> nvalues = new TreeSet<AttributeValue>(p.getValues());
                for (CalculatedAttribute att : attributes) {
                    Object o = att.calculate(p);
                    if (!att.type.accepts(o)) {
                        throw new RuntimeException("Result from calculation is not accepted by the type of the attribute - " + att.name);
                    }
                    nvalues.add(new AttributeValue(att, att.type.canonize(o)));
                }
                return new Entity(nvalues);
            }
        });
    }
}
