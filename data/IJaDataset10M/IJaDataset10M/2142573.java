package net.jadoth.sqlengine.types;

import net.jadoth.lang.reflection.annotations.Label;
import net.jadoth.sqlengine.SQL;
import net.jadoth.sqlengine.dbms.DbmsAdaptor;
import net.jadoth.sqlengine.dbms.DbmsDMLAssembler;
import net.jadoth.sqlengine.dbms.standard.StandardDbmsAdaptor;
import net.jadoth.sqlengine.internal.DatabaseGateway;
import net.jadoth.sqlengine.internal.interfaces.SqlTableReference;
import net.jadoth.sqlengine.internal.interfaces.SqlTableReference.Utils;

public interface TableQuery extends Query {

    public SqlTableReference getTable();

    abstract class Implementation extends Query.Implementation implements TableQuery {

        protected Implementation() {
            super();
        }

        protected Implementation(TableQuery.Implementation copySource) {
            super(copySource);
        }

        @Override
        public DatabaseGateway<?> getDatabaseGatewayForExecution() {
            final DatabaseGateway<?> dbg = super.getDatabaseGatewayForExecution();
            if (dbg != null) return dbg;
            return Utils.getDatabaseGateway(this.getTable());
        }

        @Override
        @Label(LABEL_assembleQuery)
        protected StringBuilder assemble(DbmsDMLAssembler<?> dmlAssembler, final StringBuilder sb, final int indentLevel, final int flags) {
            if (dmlAssembler == null) {
                dmlAssembler = StandardDbmsAdaptor.getSingletonStandardDbmsAdaptor().getDMLAssembler();
                final DbmsAdaptor<?> staticDefaultDbms = SQL.getDefaultDBMS();
                if (staticDefaultDbms != null) {
                    dmlAssembler = staticDefaultDbms.getDMLAssembler();
                }
                lookupQueryAssembler: {
                    final SqlTableReference table = this.getTable();
                    if (table == null) break lookupQueryAssembler;
                    final DatabaseGateway<?> dbcon = SqlTableReference.Utils.getDatabaseGateway(table);
                    if (dbcon == null) break lookupQueryAssembler;
                    final DbmsAdaptor<?> dbmsa = dbcon.getDbmsAdaptor();
                    if (dbmsa == null) break lookupQueryAssembler;
                    dmlAssembler = dbmsa.getDMLAssembler();
                }
            }
            final boolean reallySingleLine = this.isSingleLineMode() || isSingleLine(flags);
            final boolean reallyPacked = this.isPacked() || isPacked(flags);
            dmlAssembler.assembleQuery(this, sb, indentLevel, bitSingleLine(reallySingleLine) | bitPacked(reallyPacked));
            return sb;
        }
    }
}
