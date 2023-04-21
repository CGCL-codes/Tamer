package de.grogra.xl.compiler.predicate;

import antlr.collections.AST;
import de.grogra.reflect.Type;
import de.grogra.reflect.TypeId;
import de.grogra.xl.expr.Block;
import de.grogra.xl.expr.Expression;
import de.grogra.xl.qnp.Predicate;
import de.grogra.xl.qnp.TransitiveTraversal;
import de.grogra.xl.util.ObjectList;
import de.grogra.xl.util.XLUtils;

public final class TraversalData extends PredicateData {

    Place[] places;

    final CompositeData child;

    final Expression min;

    final Expression max;

    private final Place inPlace, outPlace;

    private TransitiveTraversal predicate;

    private short[] mapping;

    private Place minPlace, maxPlace;

    private final String identifier;

    TraversalData(CompositeData child, int inKind, int outKind, Expression min, Expression max, AST pos, int textualPosition, int level, PredicateBuilder builder) {
        super(null, inKind, outKind, pos, textualPosition, level, builder);
        this.inPlace = builder.createPlace(null);
        this.outPlace = builder.createPlace(null);
        this.child = child;
        this.min = min;
        this.max = max;
        this.identifier = builder.getScope().nextUniqueId();
    }

    @Override
    boolean hasInPlace() {
        return inPlace != null;
    }

    @Override
    boolean hasOutPlace() {
        return outPlace != null;
    }

    @Override
    Place getInPlace(boolean force) {
        return (inPlace != null) ? inPlace.resolve() : null;
    }

    @Override
    Place getOutPlace(boolean force) {
        return (outPlace != null) ? outPlace.resolve() : null;
    }

    @Override
    Type getLastNodeType() {
        return builder.nodeType;
    }

    @Override
    int getLabelArgument() {
        return -1;
    }

    @Override
    Place[] getPlaces() {
        return places;
    }

    @Override
    public Predicate getPredicate() {
        return predicate;
    }

    @Override
    public String toString() {
        return "Traversal " + dataId + " [" + XLUtils.arrayToString(mapping) + ',' + child + ']';
    }

    @Override
    Block getSubRoutines() {
        return child.getSubRoutines();
    }

    void complete(ObjectList outerTerms) {
        outerTerms.add(0, child.getInPlace(false).getNodeArgument());
        outerTerms.add(1, child.getOutPlace(false).getNodeArgument());
        int n = outerTerms.size();
        mapping = new short[n];
        for (int i = 0; i < n; i++) {
            mapping[i] = -1;
        }
        places = new Place[n + 2];
        for (int i = 0; i < n; i++) {
            Argument t = (Argument) outerTerms.get(i);
            int m = child.getPlaces().length;
            for (short j = 0; j < m; j++) {
                if (t == child.getArgument(j)) {
                    assert mapping[i] == -1;
                    mapping[i] = j;
                }
            }
            assert mapping[i] >= 0;
            if (i >= 2) {
                t.place.map(this, i, null).initParam(t);
            }
        }
        minPlace.map(this, n, null).initParam(minPlace.getArgument(TypeId.LONG));
        maxPlace.map(this, n + 1, null).initParam(maxPlace.getArgument(TypeId.LONG));
        predicate = new TransitiveTraversal(identifier, child.getPredicate(), mapping);
        Place.Mapping m = getInPlace(false).map(this, 0, null);
        Argument node = getInPlace(false).getNodeArgument();
        if (node != null) {
            m.initParam(node);
        } else {
            getInPlace(false).mapToArguments();
        }
        m = getOutPlace(false).map(this, 1, null);
        node = getOutPlace(false).getNodeArgument();
        if (node != null) {
            m.initParam(node);
        } else {
            getOutPlace(false).mapToArguments();
        }
    }

    String getIdentifier() {
        return identifier;
    }

    @Override
    void createSubPredicates() {
        if (minPlace == null) {
            minPlace = addExpression(min, Type.LONG).getPlaces()[0];
        }
        if (max == null) {
            maxPlace = minPlace;
        } else if (maxPlace == null) {
            maxPlace = addExpression(max, Type.LONG).getPlaces()[0];
        }
    }

    @Override
    void mapLabeledArgs() {
        minPlace = builder.getPlace(min);
        maxPlace = builder.getPlace(max);
    }

    @Override
    void mapUnlabeledArgs() {
    }
}
