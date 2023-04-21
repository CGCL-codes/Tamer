package edu.ucla.sspace.dependency;

import edu.ucla.sspace.index.DefaultPermutationFunction;
import edu.ucla.sspace.index.PermutationFunction;
import edu.ucla.sspace.vector.Vector;
import java.io.Serializable;
import java.util.LinkedList;

/**
 * An default {@link DependencyPermutationFunction} for permuting a {@link
 * Vector} based on a dependecny path, represented as a list of {@link
 * DependencyRelation}s.  A passed in {@link PermutationFunction} is used to
 * permute the {@link Vector}s based on the path length.
 *
 * @see edu.ucla.sspace.index.PermutationFunction
 *
 * @author Keith Stevens
 */
public class RelationSumPermutationFunction<T extends Vector> implements DependencyPermutationFunction<T>, Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * The internal {@link PermutationFunction} to use for permuting vectors.
     */
    private final PermutationFunction<T> permFunc;

    /**
     * Creates a new {@link DefaultDependencyPermutationFunction} that wraps an
     * existing {@link PermutationFunction}.
     */
    public RelationSumPermutationFunction(PermutationFunction<T> permFunc) {
        this.permFunc = permFunc;
    }

    /**
     * {@inheritDoc}
     */
    public T permute(T vector, DependencyPath path) {
        int bestRelationScore = 0;
        for (DependencyRelation link : path) vector = permFunc.permute(vector, getRelationScore(link.relation()));
        return vector;
    }

    private static int getRelationScore(String relation) {
        if (relation.length() == 0) return 0;
        if (relation.equals("SBJ")) return 6;
        if (relation.equals("OBJ")) return 5;
        if (relation.equals("NMOD")) return 4;
        if (relation.equals("VMOD")) return 3;
        if (relation.equals("ADV")) return 2;
        return 1;
    }
}
