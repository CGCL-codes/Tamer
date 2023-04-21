package normal.engine.algebra;

import normal.engine.ShareableObject;

public interface NGroupExpression extends ShareableObject {

    long getNumberOfTerms();

    long getGenerator(long index);

    long getExponent(long index);

    void addTermFirst(long generator, long exponent);

    void addTermLast(long generator, long exponent);

    NGroupExpression inverse();

    NGroupExpression power(long exponent);

    boolean simplify();

    boolean simplify(boolean cyclic);

    boolean substitute(long generator, NGroupExpression expansion);

    boolean substitute(long generator, NGroupExpression expansion, boolean cyclic);
}
