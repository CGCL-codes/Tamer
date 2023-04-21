package org.boudnik.qa.core;

import org.boudnik.better.sql.*;

/**
 * @author shr
 * @since Aug 31, 2005 6:46:49 PM
 */
@TABLE(20)
public class Foo extends OBJ {

    @MANDATORY
    @NAME("nickName")
    public final STR name = new STR();

    @LENGTH(8192)
    public final LONGSTR description = new LONGSTR();

    @MANDATORY
    public final INT age = new INT();

    public final INT income = new INT();

    @MANDATORY
    public final CODEREF<Sex> sex = new CODEREF<Sex>(Sex.class);

    public OBJ.ComparableFIELD[] getKey() {
        return new ComparableFIELD[] { name, sex };
    }
}
