package org.renjin.primitives.match;

import org.renjin.eval.Calls;
import org.renjin.eval.Context;
import org.renjin.eval.EvalException;
import org.renjin.primitives.annotations.Current;
import org.renjin.primitives.annotations.Primitive;
import org.renjin.sexp.AtomicVector;
import org.renjin.sexp.Closure;
import org.renjin.sexp.Environment;
import org.renjin.sexp.FunctionCall;
import org.renjin.sexp.IntVector;
import org.renjin.sexp.LogicalVector;
import org.renjin.sexp.Null;
import org.renjin.sexp.PairList;
import org.renjin.sexp.SEXP;
import org.renjin.sexp.StringVector;
import org.renjin.sexp.Symbols;
import org.renjin.sexp.Vector;

/**
 * Default implementations of match() related functions.
 */
public class Match {

    private static final int UNMATCHED = -1;

    private Match() {
    }

    /**
   * match returns a vector of the positions of (first) matches of its first argument in its second.
   * @param search vector or NULL: the values to be matched.
   * @param table vector or NULL: the values to be matched against.
   * @param noMatch the value to be returned in the case when no match is found. Note that it is coerced to integer.
   * @param incomparables a vector of values that cannot be matched. Any value in x matching a value in this vector is assigned the nomatch value.
   *        For historical reasons, FALSE is equivalent to NULL.
   * @return
   */
    public static int[] match(AtomicVector search, AtomicVector table, int noMatch, AtomicVector incomparables) {
        if (incomparables.equals(LogicalVector.FALSE)) {
            incomparables = Null.INSTANCE;
        }
        int[] matches = new int[search.length()];
        for (int i = 0; i != search.length(); ++i) {
            if (incomparables.contains(search, i)) {
                matches[i] = noMatch;
            } else {
                int pos;
                if (search.isElementNA(i)) {
                    pos = table.indexOfNA();
                } else {
                    pos = table.indexOf(search, i, 0);
                }
                matches[i] = pos >= 0 ? pos + 1 : noMatch;
            }
        }
        return matches;
    }

    /**
   * pmatch seeks matches for the elements of its first argument among those of its second.
   *
   * The behaviour differs by the value of duplicates.ok. Consider first the case
   * if this is true. First exact matches are considered, and the positions of the
   * first exact matches are recorded. Then unique partial matches are considered,
   * and if found recorded. (A partial match occurs if the whole of the element of x
   * matches the beginning of the element of table.) Finally, all remaining elements of
   * x are regarded as unmatched. In addition, an empty string can match nothing, not even an
   * exact match to an empty string. This is the appropriate behaviour for partial matching
   * of character indices, for example.
   *
   * <p>If duplicates.ok is FALSE, values of table once matched are excluded from the
   * search for subsequent matches. This behaviour is equivalent to the R algorithm
   * for argument matching, except for the consideration of empty strings (which in
   * argument matching are matched after exact and partial matching to any remaining arguments).
   *
   * @param x the values to be matched
   * @param table the values to be matched against: converted to a character vector.
   * @param noMatch the value to be returned at non-matching or multiply partially matching positions.
   * @param duplicatesOk should elements be in table be used more than once?
   * @return An integer vector (possibly including NA if nomatch = NA) of the same length as x,
   * giving the indices of the elements in table which matched, or {@code nomatch}.
   */
    public static IntVector pmatch(StringVector x, StringVector table, int noMatch, boolean duplicatesOk) {
        IntVector.Builder result = new IntVector.Builder(x.length());
        boolean matchedTable[] = new boolean[table.length()];
        boolean matchedSearch[] = new boolean[x.length()];
        for (int i = 0; i != x.length(); ++i) {
            String toMatch = pmatchElementAt(x, i);
            int match = exactMatch(toMatch, table);
            if (match != UNMATCHED && (duplicatesOk || !matchedTable[match])) {
                result.set(i, match + 1);
                matchedTable[match] = true;
                matchedSearch[i] = true;
            }
        }
        for (int i = 0; i != x.length(); ++i) {
            if (!matchedSearch[i]) {
                String toMatch = pmatchElementAt(x, i);
                int match = uniquePartialMatch(toMatch, table);
                if (match != UNMATCHED && (duplicatesOk || !matchedTable[match])) {
                    result.set(i, match + 1);
                    matchedTable[match] = true;
                } else {
                    result.set(i, noMatch);
                }
            }
        }
        return result.build();
    }

    private static int exactMatch(String toMatch, StringVector table) {
        for (int i = 0; i != table.length(); ++i) {
            String t = pmatchElementAt(table, i);
            if (toMatch.equals(t)) {
                return i;
            }
        }
        return -1;
    }

    private static int uniquePartialMatch(String toMatch, StringVector table) {
        int partialMatch = UNMATCHED;
        for (int i = 0; i != table.length(); ++i) {
            String t = pmatchElementAt(table, i);
            if (t.startsWith(toMatch)) {
                if (partialMatch != UNMATCHED) {
                    return UNMATCHED;
                }
                partialMatch = i;
            }
        }
        return partialMatch;
    }

    private static String pmatchElementAt(StringVector vector, int i) {
        return vector.isElementNA(i) ? "NA" : vector.getElementAsString(i);
    }

    @Primitive("match.call")
    public static SEXP matchCall(@Current Context context, @Current Environment rho, SEXP definition, FunctionCall call, boolean expandDots) {
        Closure closure;
        if (definition instanceof Closure) {
            closure = (Closure) definition;
        } else if (definition == Null.INSTANCE) {
            if (context.getParent().getType() != Context.Type.FUNCTION) {
                throw new EvalException("match.call() was called from outside a function");
            }
            closure = context.getParent().getClosure();
        } else {
            throw new EvalException("match.call cannot use definition of type '%s'", definition.getTypeName());
        }
        PairList matched = Calls.matchArguments(closure.getFormals(), call.getArguments());
        if (expandDots) {
            PairList.Builder expandedArgs = new PairList.Builder();
            for (PairList.Node node : matched.nodes()) {
                if (node.getTag() == Symbols.ELLIPSES) {
                    for (PairList.Node elipseNode : ((PairList) node.getValue()).nodes()) {
                        expandedArgs.add(elipseNode.getRawTag(), elipseNode.getValue());
                    }
                } else {
                    expandedArgs.add(node.getTag(), node.getValue());
                }
            }
            matched = expandedArgs.build();
        }
        return new FunctionCall(call.getFunction(), matched);
    }

    /**
   * Returns an IntVector indices of elements that are {@code TRUE}.
   * 
   * <p>Note that the which() function in the base package handles 
   * array indices and names, this internal function simply returns
   * the indices
   */
    @Primitive
    public static IntVector which(Vector x) {
        IntVector.Builder indices = new IntVector.Builder();
        for (int i = 0; i != x.length(); ++i) {
            if (x.isElementTrue(i)) {
                indices.add(i + 1);
            }
        }
        return indices.build();
    }
}
