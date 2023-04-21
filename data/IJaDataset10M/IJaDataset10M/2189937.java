package com.kopiright.tanit.v2trigger;

import com.kopiright.compiler.base.PositionedError;
import com.kopiright.compiler.base.TokenReference;
import com.kopiright.kopi.comp.kjc.JStatement;
import com.kopiright.tanit.plsql.PLSQLStatement;
import java.util.ArrayList;

/**
 * Root class for all statements
 */
public class V2LockTable extends V2Statement {

    /**
   * Constructor
   */
    public V2LockTable(TokenReference ref, String name) {
        super(ref);
        this.name = name;
    }

    /**
   * Converts the statement in PLSQL
   */
    public void convert(ArrayList list) {
    }

    String name;
}
