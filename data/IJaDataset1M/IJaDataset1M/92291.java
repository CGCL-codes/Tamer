package net.jadoth.sqlengine.sql.syntax;

import net.jadoth.collections.types.XGettingEnum;
import net.jadoth.collections.types.XList;
import net.jadoth.collections.types.XReferencing;
import net.jadoth.sqlengine.sql.definitions.BaseTableColumn;
import net.jadoth.sqlengine.sql.definitions.SqlBaseTable;
import net.jadoth.sqlengine.sql.statements.SqlInsert;
import net.jadoth.sqlengine.sql.statements.SqlStatementExecutingUpdate;
import net.jadoth.sqlengine.sql.types.SqlContext;

public interface INSERT<V extends SqlInsertValues> extends SqlInsert<V> {

    @Override
    public INSERT<V> setName(String name);

    @Override
    public INSERT<V> setComment(String comment);

    @Override
    public INSERT<V> setCommentBlock(XList<String> commentBlock);

    @Override
    public INSERT<V> setSqlContext(XReferencing<? extends SqlContext<?>> contextProvider);

    @Override
    public INSERT<V> setPacked(boolean packed);

    @Override
    public INSERT<V> setSingleLine(boolean singleLine);

    @Override
    public INSERT<V> setExecutor(SqlStatementExecutingUpdate.Executor executor);

    @Override
    public INSERT<V> addCustomProperties(Object... customProperties);

    @Override
    public INSERT<V> INTO(SqlBaseTable targetTable);

    @Override
    public INSERT<V> columns(Object... columns);

    @Override
    public INSERT<V> VALUES(V values);

    @Override
    public INSERT<V> pack();

    @Override
    public INSERT<V> singleLine();

    public class Implementation<V extends SqlInsertValues> extends SqlInsert.Implementation<V> implements INSERT<V> {

        public Implementation() {
            super();
        }

        public Implementation(final SqlInsert.Implementation<V> other) {
            super(other);
        }

        public Implementation(final SqlBaseTable targetTable, final XGettingEnum<? extends BaseTableColumn<?>> columns, final V values) {
            super(targetTable, columns, values);
        }

        @Override
        public INSERT.Implementation<V> columns(final Object... columns) {
            super.columns(columns);
            return this;
        }

        @Override
        public INSERT.Implementation<V> INTO(final SqlBaseTable targetTable) {
            super.INTO(targetTable);
            return this;
        }

        @Override
        public INSERT.Implementation<V> VALUES(final V values) {
            super.VALUES(values);
            return this;
        }

        @Override
        public INSERT.Implementation<V> copy() {
            return new INSERT.Implementation<V>(this);
        }

        @Override
        public INSERT.Implementation<V> setName(final String name) {
            super.internalSetName(name);
            return this;
        }

        @Override
        public INSERT.Implementation<V> setComment(final String comment) {
            super.internalSetComment(comment);
            return this;
        }

        @Override
        public INSERT.Implementation<V> setCommentBlock(final XList<String> commentBlock) {
            super.internalSetCommentBlock(commentBlock);
            return this;
        }

        @Override
        public INSERT.Implementation<V> setSqlContext(final XReferencing<? extends SqlContext<?>> contextProvider) {
            super.internalSetSqlContext(contextProvider);
            return this;
        }

        @Override
        public INSERT.Implementation<V> setPacked(final boolean packed) {
            super.internalSetPacked(packed);
            return this;
        }

        @Override
        public INSERT.Implementation<V> setSingleLine(final boolean singleLine) {
            super.internalSetSingleLine(singleLine);
            return this;
        }

        @Override
        public INSERT.Implementation<V> pack() {
            super.internalSetPacked(true);
            return this;
        }

        @Override
        public INSERT.Implementation<V> singleLine() {
            super.internalSetSingleLine(true);
            return this;
        }

        @Override
        public INSERT.Implementation<V> setExecutor(final SqlStatementExecutingUpdate.Executor executor) {
            super.internalSetExecutor(executor);
            return this;
        }

        @Override
        public INSERT.Implementation<V> addCustomProperties(final Object... customProperties) {
            this.internalAddCustomProperties(customProperties);
            return this;
        }
    }
}
