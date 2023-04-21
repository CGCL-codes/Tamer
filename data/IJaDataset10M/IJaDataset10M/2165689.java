package net.jadoth.sqlengine.sql.syntax;

import net.jadoth.sqlengine.dbms.DbmsSqlAssembler;
import net.jadoth.sqlengine.sql.definitions.FUNCTION;
import net.jadoth.sqlengine.sql.expressions.SqlFunctions;
import net.jadoth.util.chars.VarChar;

public interface CONCAT extends FUNCTION.Invocation {

    public class Implementation extends FUNCTION.Invocation.Implementation implements CONCAT {

        public Implementation(final Object... arguments) {
            super(SqlFunctions.CONCAT, arguments);
        }

        @Override
        public VarChar sqlAssemble(final DbmsSqlAssembler<?> assembler, final VarChar vc, final int flags) {
            return assembler.assemble(vc, flags, this);
        }
    }
}
