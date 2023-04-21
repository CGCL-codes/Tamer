package net.jadoth.sqlengine.sql.definitions;

import static net.jadoth.Jadoth.notNull;
import net.jadoth.sqlengine.SqlCommonTypeNames;
import net.jadoth.sqlengine.dbms.DbmsSqlAssembler;
import net.jadoth.sqlengine.sql.descriptors.TableDescriptor;
import net.jadoth.sqlengine.sql.expressions.SqlExpression;
import net.jadoth.util.chars.VarChar;

public interface SqlColumn<T extends SqlTable> extends TableMember<T>, SqlMember, SqlNamedExpression {

    @Override
    public T sqlOwner();

    @Override
    public SqlColumnDescriptor<? extends TableDescriptor> sqlDescriptor();

    public class Simple<T extends SqlTable> extends SqlExpression.Implementation implements SqlColumn<T> {

        final T owner;

        final SqlColumnDescriptor<? extends TableDescriptor> descriptor;

        public Simple(final T owner, final SqlColumnDescriptor<? extends TableDescriptor> descriptor) {
            super();
            this.owner = owner;
            this.descriptor = notNull(descriptor);
        }

        @Override
        public String sqlName() {
            return this.descriptor.sqlName();
        }

        @Override
        public SqlIdentifier sqlIdentifier() {
            return this.descriptor.sqlIdentifier();
        }

        @Override
        public String sqlSyntaxName() {
            return SqlCommonTypeNames.COLUMN;
        }

        @Override
        public T sqlOwner() {
            return this.owner;
        }

        @Override
        public SqlColumnDescriptor<? extends TableDescriptor> sqlDescriptor() {
            return this.descriptor;
        }

        @Override
        public VarChar sqlAssemble(final DbmsSqlAssembler<?> assembler, final VarChar vc, final int flags) {
            return assembler.assembleIdentifier(vc, flags, this);
        }
    }
}
