package com.kopiright.tanit.plsql;

import com.kopiright.compiler.base.PositionedError;
import com.kopiright.compiler.base.UnpositionedError;
import com.kopiright.compiler.base.TokenReference;
import com.kopiright.kopi.comp.kjc.CType;
import com.kopiright.kopi.comp.kjc.CParseClassContext;
import com.kopiright.kopi.comp.kjc.CClassNameType;
import com.kopiright.xkopi.comp.xkjc.XDatabaseColumn;

/**
 * record type
 */
public class PLRecordType extends PLTypeSpecification {

    /**
   * Constructor
   */
    public PLRecordType(TokenReference ref, String table) {
        super(ref);
        this.table = table;
    }

    public PLVariable getVariable(String name) {
        XDatabaseColumn dc = XUtils.getDatabaseColumn(tableName(), name);
        if (dc != null) {
            return new PLSimpleVariable(name, dc.getStandardType());
        } else {
            if (typeDeclaration instanceof PLCursorDeclaration) {
                return ((PLCursorDeclaration) typeDeclaration).getField(name);
            }
        }
        return null;
    }

    public void analyse(PLSQLContext context, Environment env) throws PositionedError {
        typeDeclaration = context.getTypeDeclaration(tableName());
        if (typeDeclaration != null) {
            try {
                type = typeDeclaration.getType().checkType(context.getTypeContext());
            } catch (UnpositionedError ue) {
                throw new PositionedError(getTokenReference(), ue.getFormattedMessage());
            }
        } else {
            type = env.createType("ROWTYPE_" + tableName());
        }
    }

    public CType getType() {
        return type;
    }

    public PLTypeDeclaration getTypeDeclaration() {
        return typeDeclaration;
    }

    public String toString() {
        return table + "%ROWTYPE";
    }

    public String tableName() {
        return table;
    }

    /**
   * Accepts a visitor.
   *
   * @param	visitor			the visitor
   */
    public void accept(PLVisitor visitor) throws PositionedError {
        visitor.visitPLRecordType(this, table);
    }

    private PLTypeDeclaration typeDeclaration;

    private String table;

    private CType type;
}
