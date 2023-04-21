package net.jadoth.sqlengine.internal.tables;

import static net.jadoth.sqlengine.SQL.LANG.TRUNCATE_TABLE;
import static net.jadoth.sqlengine.SQL.Punctuation._;
import net.jadoth.sqlengine.INSERT;
import net.jadoth.sqlengine.SELECT;
import net.jadoth.sqlengine.SQL;
import net.jadoth.sqlengine.UPDATE;
import net.jadoth.sqlengine.exceptions.SQLEngineException;
import net.jadoth.sqlengine.exceptions.SQLEngineInvalidIdentifier;
import net.jadoth.sqlengine.internal.SqlAsterisk;
import net.jadoth.sqlengine.internal.SqlColumn;
import net.jadoth.sqlengine.internal.interfaces.SqlTableReference;
import net.jadoth.sqlengine.internal.tables.SqlQualifiedIdentityWithAlias.Sql;

/**
 * Type representing the identifier of an actual table. An {@link SqlTableIdentifier} is a {@link SqlTableReference}.
 * <p>
 * Roughly equivalent to SQL Standard's {@literal table name}, renamed for more expressiveness in an object oriented
 * typing world beyond the pure syntax grammar world of SQL.
 *
 * @author Thomas M�nz
 *
 */
public interface SqlTableIdentifier extends SqlTableReference {

    public Sql sql();

    /**
	 *
	 * @author Thomas Muenz
	 */
    public class Implementation extends SqlQualifiedIdentityWithAlias implements SqlTableIdentifier {

        public final Util util = new Util();

        public Implementation(final String schema, final String name, final String alias) {
            super(new Sql(schema, name, alias));
            this.sql().this$ = this;
        }

        protected Implementation(final Sql sql) {
            super(sql);
        }

        @Override
        public Sql sql() {
            return (Sql) super.sql();
        }

        @Override
        public boolean equals(final Object obj) {
            if (obj == null) {
                return false;
            }
            if (obj instanceof SqlTableIdentifier.Implementation) {
                final SqlTableIdentifier.Implementation t = (SqlTableIdentifier.Implementation) obj;
                if (this.sql().schema().equals(t.sql().schema()) && this.sql().name().equals(t.sql().name())) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public int hashCode() {
            return super.hashCode();
        }

        @Override
        public SqlTableIdentifier.Implementation AS(final String newAlias) throws SQLEngineException {
            return new SqlTableIdentifier.Implementation(this.sql().schema(), this.sql().name(), newAlias);
        }

        protected SqlTableIdentifier.Implementation getTopLevelInstance() {
            return this;
        }

        public static class Sql extends SqlQualifiedIdentityWithAlias.Sql {

            SqlTableIdentifier.Implementation this$;

            protected SqlTableIdentifier.Implementation this$() {
                return this.this$;
            }

            /**
			 * Necessary to rewire to parent instance.
			 * <p>
			 * Class can't be inner class because it gets instantiated and passed to table super constructor
			 * before table instance is created/completed.
			 *
			 * @param this$ the parent instance this instance belongs to.
			 */
            protected void this$(final SqlTableIdentifier.Implementation this$) {
                this.this$ = this$;
            }

            protected Sql(final String sqlSchema, final String sqlName, final String sqlAlias) {
                super(sqlSchema, sqlName, sqlAlias);
            }

            public INSERT INSERT(final Object[] columns, final Object... values) {
                return new INSERT().INTO(this.this$.getTopLevelInstance()).columns(columns).VALUES(values);
            }

            public UPDATE UPDATE(final Object[] columns, final Object... values) {
                return new UPDATE(this.this$.getTopLevelInstance()).columns(columns).setValues(values);
            }

            public String TRUNCATE() {
                return net.jadoth.Jadoth.glue(TRUNCATE_TABLE, this.this$.getTopLevelInstance().toString());
            }

            public SqlAsterisk ALL() {
                final SqlAsterisk star = new SqlAsterisk(this.this$.getTopLevelInstance());
                return star;
            }

            public SELECT SELECT() {
                return new SELECT().FROM(this.this$.getTopLevelInstance());
            }

            public SELECT COUNT() {
                final SELECT y = new SELECT().items(SQL.COUNT("*")).FROM(this.this$.getTopLevelInstance());
                return y;
            }

            public SqlColumn qualifyColumn(final String columnName) throws SQLEngineInvalidIdentifier {
                return new SqlColumn(this.this$.getTopLevelInstance(), columnName);
            }

            public SqlColumn col(final Object expression) {
                return new SqlColumn(this.this$.getTopLevelInstance(), expression);
            }

            @Override
            public String toString() {
                return this.getClass() + ": " + this.schema() + ", " + this.name() + ", " + this.alias();
            }
        }

        public class Util extends SqlQualifiedIdentity.Util {

            public Sql getSql() {
                return SqlTableIdentifier.Implementation.this.sql();
            }

            public AliasedSqlTable wrapWithAlias() {
                return new AliasedSqlTable(SqlTableIdentifier.Implementation.this.getTopLevelInstance());
            }

            public StringBuilder assembleName(StringBuilder sb, final boolean withAlias) {
                sb = super.assembleName(sb);
                if (withAlias) {
                    final String alias = SqlTableIdentifier.Implementation.this.sql().alias();
                    if (alias != null && alias.length() > 0) {
                        sb.append(_).append(alias);
                    }
                }
                return sb;
            }

            public String toAliasString() {
                return this.assembleName(null, true).toString();
            }
        }
    }
}
