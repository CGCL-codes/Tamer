package net.jadoth.sqlengine.sql.syntax;

import net.jadoth.sqlengine.dbms.DbmsSqlAssembler;
import net.jadoth.sqlengine.sql.clauses.SqlConditionalClause;
import net.jadoth.sqlengine.sql.conditions.SqlBooleanExpression;
import net.jadoth.util.chars.VarChar;

public interface HAVING extends SqlConditionalClause {

    public class Implementation extends SqlConditionalClause.Implementation implements HAVING {

        public Implementation(final SqlBooleanExpression headCondition) {
            super(headCondition);
        }

        public Implementation(final HAVING.Implementation other) {
            super(other);
        }

        @Override
        public VarChar sqlAssemble(final DbmsSqlAssembler<?> assembler, final VarChar vc, final int flags) {
            return assembler.assemble(vc, flags, this);
        }

        @Override
        public HAVING.Implementation copy() {
            return new HAVING.Implementation(this);
        }
    }
}
