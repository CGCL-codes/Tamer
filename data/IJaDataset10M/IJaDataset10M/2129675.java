package net.jadoth.sqlengine.dbms.h2;

import net.jadoth.collections.types.XGettingList;
import net.jadoth.collections.types.XGettingMap;
import net.jadoth.lang.functional.Predicate;
import net.jadoth.sqlengine.SqlEngineUnsupportedSyntaxException;
import net.jadoth.sqlengine.dbms.AbstractSqlAssembler;
import net.jadoth.sqlengine.sql.definitions.BaseTableColumn.Descriptor;
import net.jadoth.sqlengine.sql.definitions.SqlConstraint;
import net.jadoth.sqlengine.sql.definitions.SqlConstraintReferential;
import net.jadoth.sqlengine.sql.definitions.SqlIdentifier;
import net.jadoth.sqlengine.sql.descriptors.BaseTableDescriptor;
import net.jadoth.sqlengine.sql.expressions.SqlBooleanValueExpression;
import net.jadoth.sqlengine.sql.statements.SqlQuery;
import net.jadoth.sqlengine.sql.syntax.CREATE_SCHEMA;
import net.jadoth.sqlengine.sql.syntax.CREATE_TABLE;
import net.jadoth.sqlengine.sql.syntax.DROP_TABLE;
import net.jadoth.sqlengine.sql.syntax.SELECT;
import net.jadoth.sqlengine.sql.types.ColumnIdentityDefinition;
import net.jadoth.sqlengine.sql.types.SqlReferentialMatchType;
import net.jadoth.util.chars.VarChar;

public class H2SqlAssembler extends AbstractSqlAssembler<H2DbmsAdaptor> {

    static final char[] IF_EXISTS_ = (H2SQL.IF_EXISTS + ' ').toCharArray();

    static final char[] IF_NOT_EXISTS_ = (H2SQL.IF_NOT_EXISTS + ' ').toCharArray();

    static final char[] _NOT_PERSISTENT = (' ' + H2SQL.NOT_PERSISTENT).toCharArray();

    static final char[] _TRANSACTIONAL = (' ' + H2SQL.TRANSACTIONAL).toCharArray();

    public H2SqlAssembler(final H2DbmsAdaptor dbms) {
        super(dbms);
    }

    public H2SqlAssembler(final H2DbmsAdaptor dbms, final XGettingMap<Object, Object> assemblyVariables) {
        super(dbms, assemblyVariables);
    }

    @Override
    protected void assembleQueryLimitation(final VarChar vc, final int flags, final SqlQuery<?> query) {
        final Integer limit;
        if ((limit = query.getFetchFirst()) == null) {
            return;
        }
        clauseSeperator(vc, flags).append("LIMIT ").append(limit.intValue());
        final Integer offset;
        if ((offset = query.getOffset()) == null) {
            return;
        }
        clauseSeperator(vc, flags).blank().append(OFFSET_).append(offset.intValue());
    }

    public VarChar ddlAssembleSchemaCreationAttachement(final VarChar vc, final int flags, final CREATE_TABLE statement) {
        final SqlIdentifier schemaIdentifier;
        if ((schemaIdentifier = statement.getTableDescriptor().sqlSchema().sqlIdentifier()) == null) {
            return vc;
        }
        final int assemblyFlags = this.buildStatementFlags(flags, statement);
        vc.append("CREATE SCHEMA IF NOT EXISTS ");
        this.assembleIdentifier(vc, assemblyFlags, schemaIdentifier);
        vc.append(';');
        newLine(vc, assemblyFlags);
        return vc;
    }

    static void checkTableCustomProperty(final VarChar vc, final XGettingList<Object> customProperties, final String h2Property, final char[] assemblyChars) {
        if (customProperties != null && customProperties.contains(h2Property)) {
            vc.append(assemblyChars);
        }
    }

    @Override
    protected void internalAssembleTableDefinition(final VarChar vc, final int flags, final BaseTableDescriptor descriptor, final boolean assembleIndices, final boolean assembleNonLocal, final boolean assembleTriggers) throws SqlEngineUnsupportedSyntaxException {
        super.internalAssembleTableDefinition(vc, flags, descriptor, assembleIndices, assembleNonLocal, assembleTriggers);
    }

    protected void internalAssembleTableDefinitionTrailingFlags(final VarChar vc, final int flags, final BaseTableDescriptor descriptor) {
        final XGettingList<Object> customProperties = descriptor.getCustomProperties();
        checkTableCustomProperty(vc, customProperties, H2SQL.NOT_PERSISTENT, _NOT_PERSISTENT);
        checkTableCustomProperty(vc, customProperties, H2SQL.TRANSACTIONAL, _TRANSACTIONAL);
    }

    @Override
    public VarChar ddlAssemble(final VarChar vc, final int flags, final CREATE_TABLE statement) throws SqlEngineUnsupportedSyntaxException {
        this.ddlAssembleSchemaCreationAttachement(vc, flags, statement);
        super.ddlAssemble(vc, flags, statement);
        this.internalAssembleTableDefinitionTrailingFlags(vc, flags, statement.getTableDescriptor());
        return vc;
    }

    @Override
    protected void assembleForeignKeyMatchType(final VarChar vc, final int flags, final SqlConstraintReferential foreignKey) {
        final SqlReferentialMatchType matchType;
        if ((matchType = foreignKey.getMatchType()) == null) {
            return;
        }
        if (matchType != SqlReferentialMatchType.SIMPLE) {
            throw new SqlEngineUnsupportedSyntaxException();
        }
    }

    @Override
    protected void assembleConstraintCheckTime(final VarChar vc, final int flags, final SqlConstraint constraint) {
    }

    @Override
    protected VarChar ddlAssembleTableCreationQueryColumnList(final VarChar vc, final int flags, final BaseTableDescriptor table, final SELECT query, final Boolean withQueryData) {
        return vc;
    }

    @Override
    protected VarChar ddlAssembleTableCreationQueryDataHint(final VarChar vc, final int flags, final BaseTableDescriptor table, final SELECT query, final Boolean withQueryData) {
        return vc;
    }

    @Override
    public VarChar ddlAssemble(final VarChar vc, final int flags, final BaseTableDescriptor table, final SELECT query, final Boolean withQueryData, final boolean withReferences) throws SqlEngineUnsupportedSyntaxException {
        this.ddlAssemble(vc, flags, table, withReferences);
        this.ddlAssembleTableCreationQuery(vc, flags, table, query, withQueryData);
        return vc;
    }

    @Override
    protected void ddlAssembleCreateSchemaHead(final VarChar vc, final int flags, final CREATE_SCHEMA statement) throws SqlEngineUnsupportedSyntaxException {
        super.ddlAssembleCreateSchemaHead(vc, flags, statement);
        checkTableCustomProperty(vc, statement.getCustomProperties(), H2SQL.IF_NOT_EXISTS, IF_NOT_EXISTS_);
    }

    @Override
    public VarChar ddlAssemble(final VarChar vc, final int flags, final DROP_TABLE statement) throws SqlEngineUnsupportedSyntaxException {
        vc.append(DROP_TABLE_);
        checkTableCustomProperty(vc, statement.getCustomProperties(), H2SQL.IF_EXISTS, IF_EXISTS_);
        this.assembleIdentifier(vc, flags, statement.getTable());
        return vc;
    }

    static final Predicate<Object> isAutoIncrementProperty = new Predicate<Object>() {

        @Override
        public boolean apply(final Object e) {
            return e instanceof AUTO_INCREMENT;
        }
    };

    @Override
    protected void ddlAssembleColumnIdentity(final VarChar vc, final int flags, final Descriptor descriptor) throws SqlEngineUnsupportedSyntaxException {
        if (descriptor.identity() == null) {
            this.internalH2DdlAssembleIncrementingTerm(vc, flags, (AUTO_INCREMENT) descriptor.getCustomProperties().search(isAutoIncrementProperty));
        }
        super.ddlAssembleColumnIdentity(vc, flags, descriptor);
    }

    @Override
    protected void ddlAssembleColumnIdentityGenerated(final VarChar vc, final int flags, final ColumnIdentityDefinition identity) {
    }

    @Override
    protected void ddlAssembleColumnIdentityIdentity(final VarChar vc, final int flags, final ColumnIdentityDefinition identity) {
        if (identity == null) {
            return;
        }
        vc.blank().add(IDENTITY);
    }

    protected void internalH2DdlAssembleIncrementingTerm(final VarChar vc, final int flags, final AUTO_INCREMENT autoIncrement) {
        if (autoIncrement == null) {
            return;
        }
        vc.add("AUTO INCREMENT");
        final Integer start = autoIncrement.startInt(), increment = autoIncrement.incrementInt();
        if (start == null && increment == null) {
            return;
        }
        vc.add('(').add(start == null ? 0 : start.intValue());
        if (increment != null) {
            vc.add(',').blank().add(increment.intValue());
        }
        vc.add(')');
    }

    @Override
    protected void ddlAssembleColumnIdentityStartValue(final VarChar vc, final int flags, final ColumnIdentityDefinition identity) {
        Number value;
        if ((value = identity.getStartValue()) == null) {
            return;
        }
        vc.add('(').add(value.intValue());
        if (identity.getIncrement() == null) {
            vc.add(')');
        }
    }

    @Override
    protected void ddlAssembleColumnIdentityIncrement(final VarChar vc, final int flags, final ColumnIdentityDefinition identity) {
        Number value;
        if ((value = identity.getIncrement()) == null) {
            return;
        }
        if (identity.getStartValue() == null) {
            vc.append('(', '0');
        }
        vc.add(',').blank().add(value.intValue()).add(')');
    }

    @Override
    protected void ddlAssembleColumnIdentityMaxValue(final VarChar vc, final int flags, final ColumnIdentityDefinition identity) {
    }

    @Override
    protected void ddlAssembleColumnIdentityMinValue(final VarChar vc, final int flags, final ColumnIdentityDefinition identity) {
    }

    @Override
    protected void ddlAssembleColumnIdentityCycle(final VarChar vc, final int flags, final ColumnIdentityDefinition identity) {
    }

    @Override
    public VarChar assemble(final VarChar vc, final int flags, final SqlBooleanValueExpression expression) {
        return vc.append(expression.value() ? TRUE : FALSE);
    }
}
