package uk.ac.lkl.common.util.expression;

import uk.ac.lkl.common.util.value.Value;

/**
 * An operator that operates on <code>Expression</code>s.
 * 
 * @author $Author: ToonTalk $
 * @version $Revision: 2891 $
 * @version $Date: 2009-06-21 08:33:07 -0400 (Sun, 21 Jun 2009) $
 * 
 */
public abstract class AbstractOperator<R extends Value<R>> {

    public abstract String getName();

    public abstract String getSymbol();

    public int getPrecedence() {
        return 0;
    }

    @Override
    public String toString() {
        return getSymbol();
    }
}
