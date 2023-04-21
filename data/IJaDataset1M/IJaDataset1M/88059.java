package com.uwyn.jhighlight.pcj.hash;

import com.uwyn.jhighlight.pcj.hash.CharHashFunction;
import com.uwyn.jhighlight.pcj.hash.DefaultCharHashFunction;
import java.io.Serializable;

/**
 *  This class provides a default hash function for
 *  char values.
 *
 *  @serial     exclude
 *  @author     S&oslash;ren Bak
 *  @version    1.2     2003/5/3
 *  @since      1.0
 */
public class DefaultCharHashFunction implements CharHashFunction, Serializable {

    /** Default instance of this hash function. */
    public static final CharHashFunction INSTANCE = new DefaultCharHashFunction();

    /** Default constructor to be invoked by sub-classes. */
    protected DefaultCharHashFunction() {
    }

    public int hash(char v) {
        return (int) v;
    }
}
