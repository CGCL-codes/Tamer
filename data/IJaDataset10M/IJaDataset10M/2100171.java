package net.jadoth.sqlengine.sql.definitions;

import net.jadoth.collections.types.XGettingSequence;
import net.jadoth.sqlengine.dbms.DbmsSqlAssembler;
import net.jadoth.sqlengine.sql.conditions.BooleanOperation;
import net.jadoth.sqlengine.sql.conditions.SqlBooleanExpression;
import net.jadoth.sqlengine.sql.descriptors.SqlTypeDescriptor;
import net.jadoth.sqlengine.sql.descriptors.TableDescriptor;
import net.jadoth.sqlengine.sql.expressions.SqlExpression;
import net.jadoth.sqlengine.sql.statements.SqlQuery;
import net.jadoth.sqlengine.sql.syntax.AND;
import net.jadoth.sqlengine.sql.syntax.OR;
import net.jadoth.sqlengine.sql.syntax.SQL;
import net.jadoth.sqlengine.sql.types.SqlAssembable;
import net.jadoth.util.chars.VarChar;

/**
 * Sql assembly variable that can be set as either a {@link SqlExpression} or a {@link SqlBooleanExpression} placeholder
 * that gets replaced by an actual string at assembly time.
 * <p>
 * The difference to {@link SqlAssembable.ReferenceExpression} or {@link SqlAssembable.ReferenceCondition} is that
 * not the content of the instance itself is used to assemble the SQL string but the variable instance provides a
 * key instance that is used to look up the content in a map provided for the assembly.
 *
 * @author Thomas M�nz
 */
public interface SqlVariable extends SqlBooleanExpression, SqlTable, SqlDerivedColumn {

    public Object key();

    @Override
    public SqlVariable AS(Object stuff);

    public class Implementation extends SqlExpression.Implementation implements SqlVariable {

        private final Object key;

        private SqlBooleanExpression previous;

        private BooleanOperation nextConditionFactor;

        public Implementation(final Object key) {
            super();
            this.key = key;
            this.previous = null;
            this.nextConditionFactor = null;
        }

        @Override
        public Object key() {
            return this.key;
        }

        @Override
        public SqlBooleanExpression AND(final Object condition) {
            SqlBooleanExpression.Static.clearConditionFactorReference(this.nextConditionFactor);
            return (this.nextConditionFactor = new AND.Implementation(SQL.condition(condition))).getBooleanTerm();
        }

        @Override
        public SqlBooleanExpression OR(final Object condition) {
            SqlBooleanExpression.Static.clearConditionFactorReference(this.nextConditionFactor);
            return (this.nextConditionFactor = new OR.Implementation(SQL.condition(condition))).getBooleanTerm();
        }

        public BooleanOperation getAttachedConditionFactor() {
            return this.nextConditionFactor;
        }

        @Override
        public SqlBooleanExpression getPreviousChainCondition() {
            return this.previous;
        }

        @Override
        public BooleanOperation getNextConditionFactor() {
            return this.nextConditionFactor;
        }

        @Override
        public SqlBooleanExpression setPreviousChainCondition(final SqlBooleanExpression previousChainCondition) {
            this.previous = previousChainCondition;
            return this;
        }

        @Override
        public VarChar sqlAssemble(final DbmsSqlAssembler<?> assembler, final VarChar vc, final int flags) {
            return assembler.assemble(vc, flags, this);
        }

        @Override
        public SqlIdentifier sqlAlias() {
            throw new UnsupportedOperationException();
        }

        @Override
        public XGettingSequence<? extends SqlColumn<? extends SqlTable>> columns() {
            throw new UnsupportedOperationException();
        }

        @Override
        public SqlVariable AS(final Object label) {
            throw new UnsupportedOperationException();
        }

        @Override
        public SqlExpression sqlExpression() {
            throw new UnsupportedOperationException();
        }

        @Override
        public SqlIdentifier sqlLabel() {
            throw new UnsupportedOperationException();
        }

        @Override
        public SqlTypeDescriptor sqlType() {
            throw new UnsupportedOperationException();
        }

        @Override
        public SqlIdentifier sqlIdentifier() {
            throw new UnsupportedOperationException();
        }

        @Override
        public String sqlName() {
            throw new UnsupportedOperationException();
        }

        @Override
        public int getColumnCount() {
            throw new UnsupportedOperationException();
        }

        @Override
        public SqlQuery<?> sqlOwner() {
            throw new UnsupportedOperationException();
        }

        @Override
        public SqlColumnDescriptor<? extends TableDescriptor> sqlDescriptor() {
            throw new UnsupportedOperationException();
        }

        @Override
        public String sqlSyntaxName() {
            return "<?>";
        }
    }
}
