package com.kopiright.tanit.plsql;

import com.kopiright.compiler.base.PositionedError;
import com.kopiright.compiler.base.TokenReference;
import com.kopiright.kopi.comp.kjc.CType;
import com.kopiright.xkopi.comp.sqlc.Expression;

/**
 * Root class for all number literals expression
 */
public abstract class PLNumberLiteral extends PLLiteral {

    /**
   * Construct a node in the parsing tree
   * This method is directly called by the parser
   * @param     where           the line of this node in the source code
   */
    public PLNumberLiteral(TokenReference where) {
        super(where);
    }
}
