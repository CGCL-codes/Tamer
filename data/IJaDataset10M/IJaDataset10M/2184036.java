package org.deri.iris.operations.relations;

import static org.deri.iris.factory.Factory.RELATION;
import java.util.ArrayList;
import java.util.List;
import org.deri.iris.api.storage.IRelation;

/**
 * @deprecated use GeneralUnion.
 * 
 * @author Darko Anicic
 * @date 23.09.2006
 * 
 */
public class Union {

    private List<IRelation> rels = null;

    private IRelation unionRel = null;

    Union(List<IRelation> arg) {
        if (arg == null) {
            throw new IllegalArgumentException("Input parameter must not be null");
        }
        int arity = 0;
        int prevArity = 0;
        int start = 0;
        this.rels = new ArrayList<IRelation>();
        for (IRelation r : arg) {
            if (r != null && r.size() > 0) {
                arity = r.getArity();
                if (arity == prevArity || start == 0) {
                    this.rels.add(r);
                    start++;
                } else throw new IllegalArgumentException("Cannot do union due to different arities");
                prevArity = arity;
            }
        }
        this.unionRel = RELATION.getRelation(arity);
    }

    Union(IRelation... args) {
        if (args == null) {
            throw new IllegalArgumentException("Input parameter must not be null");
        }
        int arity = 0;
        int prevArity = 0;
        int start = 0;
        this.rels = new ArrayList<IRelation>();
        for (IRelation r : args) {
            if (r != null && r.size() > 0) {
                arity = r.getArity();
                if (arity == prevArity || start == 0) {
                    this.rels.add(r);
                    start++;
                } else throw new IllegalArgumentException("Cannot do union due to different arities");
                prevArity = arity;
            }
        }
        this.unionRel = RELATION.getRelation(arity);
    }

    public IRelation union() {
        for (IRelation r : this.rels) {
            this.unionRel.addAll(r);
        }
        return this.unionRel;
    }
}
