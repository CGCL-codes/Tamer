package org.boudnik.qa.core;

import org.boudnik.better.sql.*;

/**
 * @author shr
 * @since Oct 20, 2005 6:38:41 AM
 */
@TABLE(8)
public class Poo extends OBJ {

    @MANDATORY
    public final REF<Foo> foo = new REF<Foo>(Foo.class);
}
