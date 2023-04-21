package com.kopiright.tanit.v2trigger;

import com.kopiright.compiler.base.PositionedError;
import com.kopiright.compiler.base.TokenReference;
import com.kopiright.kopi.comp.kjc.JStatement;
import com.kopiright.compiler.base.JavaStyleComment;
import com.kopiright.tanit.plsql.*;

/**
 * Root class for all statements
 */
public class V2FieldReference extends V2Expression {

    /**
   * Constructor
   */
    public V2FieldReference(TokenReference ref, String id, boolean hasColon) {
        super(ref);
        this.id = id;
        this.hasColon = hasColon;
    }

    /**
   * Converts the statement in PLSQL
   */
    public PLExpression convert() {
        PLNameExpression ex = PLNameExpression.build(getTokenReference(), id);
        return PLNameExpression.build(getTokenReference(), id);
    }

    private String id;

    private boolean hasColon;
}
