package net.jadoth.sqlengine.sql.syntax;

import net.jadoth.collections.types.XList;
import net.jadoth.collections.types.XReferencing;
import net.jadoth.sqlengine.dbms.DbmsSqlAssembler;
import net.jadoth.sqlengine.sql.descriptors.ViewDescriptor;
import net.jadoth.sqlengine.sql.statements.SqlDDLStatement;
import net.jadoth.sqlengine.sql.statements.SqlStatementExecutingUpdate;
import net.jadoth.sqlengine.sql.types.SqlContext;
import net.jadoth.util.chars.VarChar;

public interface CREATE_VIEW extends SqlDDLStatement {

    public ViewDescriptor getView();

    @Override
    public CREATE_VIEW setName(String name);

    @Override
    public CREATE_VIEW setComment(String comment);

    @Override
    public CREATE_VIEW setCommentBlock(XList<String> commentBlock);

    @Override
    public CREATE_VIEW setSqlContext(XReferencing<? extends SqlContext<?>> contextProvider);

    @Override
    public CREATE_VIEW setPacked(boolean packed);

    @Override
    public CREATE_VIEW setSingleLine(boolean singleLine);

    @Override
    public CREATE_VIEW pack();

    @Override
    public CREATE_VIEW singleLine();

    @Override
    public CREATE_VIEW setExecutor(SqlStatementExecutingUpdate.Executor executor);

    public class Implementation extends SqlDDLStatement.Implementation implements CREATE_VIEW {

        private final ViewDescriptor view;

        public Implementation(final ViewDescriptor view) {
            super();
            this.view = view;
        }

        public Implementation(final CREATE_VIEW.Implementation other) {
            super();
            this.view = other.view;
        }

        @Override
        public ViewDescriptor getView() {
            return this.view;
        }

        @Override
        public CREATE_VIEW setName(final String name) {
            super.internalSetName(name);
            return this;
        }

        @Override
        public CREATE_VIEW setComment(final String comment) {
            super.internalSetComment(comment);
            return this;
        }

        @Override
        public CREATE_VIEW setCommentBlock(final XList<String> commentBlock) {
            super.internalSetCommentBlock(commentBlock);
            return this;
        }

        @Override
        public CREATE_VIEW setSqlContext(final XReferencing<? extends SqlContext<?>> contextProvider) {
            super.internalSetSqlContext(contextProvider);
            return this;
        }

        @Override
        public CREATE_VIEW setPacked(final boolean packed) {
            super.internalSetPacked(packed);
            return this;
        }

        @Override
        public CREATE_VIEW setSingleLine(final boolean singleLine) {
            super.internalSetSingleLine(singleLine);
            return this;
        }

        @Override
        public CREATE_VIEW setExecutor(final SqlStatementExecutingUpdate.Executor executor) {
            super.internalSetExecutor(executor);
            return this;
        }

        @Override
        public CREATE_VIEW.Implementation copy() {
            return new CREATE_VIEW.Implementation(this);
        }

        @Override
        public VarChar sqlAssemble(final DbmsSqlAssembler<?> assembler, final VarChar vc, final int flags) {
            return this.sqlDdlAssemble(assembler, vc, flags);
        }

        @Override
        public VarChar sqlDdlAssemble(final DbmsSqlAssembler<?> assembler, final VarChar vc, final int flags) {
            return assembler.ddlAssemble(vc, flags, this);
        }

        @Override
        public CREATE_VIEW.Implementation pack() {
            super.internalSetPacked(true);
            return this;
        }

        @Override
        public CREATE_VIEW.Implementation singleLine() {
            super.internalSetSingleLine(true);
            return this;
        }

        @Override
        public CREATE_VIEW.Implementation addCustomProperties(final Object... customProperties) {
            this.internalAddCustomProperties(customProperties);
            return this;
        }
    }
}
