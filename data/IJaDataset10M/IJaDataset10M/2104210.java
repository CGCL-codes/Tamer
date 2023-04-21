package net.sf.staccatocommons.companion;

import net.sf.staccatocommons.defs.Thunk;

/**
 * @author flbulgarelli
 * 
 */
public interface ValueType<A> extends Thunk<A> {

    A value();

    A copy(A value);

    A freeze(A value);
}
