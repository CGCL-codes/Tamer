package net.jadoth.sqlengine.sql.definitions;

import net.jadoth.collections.EqHashEnum;
import net.jadoth.collections.types.XEnum;
import net.jadoth.collections.types.XGettingEnum;
import net.jadoth.lang.functional.Function;
import net.jadoth.lang.functional.Predicate;
import net.jadoth.lang.functional.Procedure;
import net.jadoth.sqlengine.SqlEngineTableInitializationException;
import net.jadoth.sqlengine.SqlName;
import net.jadoth.sqlengine.dbms.DbmsSqlAssembler;
import net.jadoth.sqlengine.sql.descriptors.BaseTableDescriptor;
import net.jadoth.sqlengine.sql.types.SqlReferentialAction;
import net.jadoth.sqlengine.sql.types.SqlReferentialMatchType;
import net.jadoth.util.chars.VarChar;


public interface SqlConstraintReferential extends SqlConstraintUsingColumnList
{
	public static final Function<SqlConstraintReferential, BaseTableDescriptor> getReferencedTable =
		new Function<SqlConstraintReferential, BaseTableDescriptor>(){
			@Override public BaseTableDescriptor apply(final SqlConstraintReferential input) {
				return input == null ?null :input.getReferencedTable();
			}
		}
	;


	public BaseTableDescriptor getReferencedTable();

	public XGettingEnum<BaseTableColumn.Descriptor> getReferencedColumns();

	public SqlReferentialMatchType getMatchType();

	public SqlReferentialAction getOnUpdateAction();

	public SqlReferentialAction getOnDeleteAction();

	public SqlIndex getSourceIndex();

	public SqlConstraintUnique getTargetConstraint();

	public void validateColumns(BaseTableDescriptor owner);
	public void validateReferencedColumns(BaseTableDescriptor referencedTable);

	public SqlIdentifier createGeneratedTargetConstraintName(BaseTableDescriptor referencedTable);
	public SqlIdentifier createGeneratedSourceIndexName();



	public interface Mutable extends SqlConstraintReferential, SqlConstraintUsingColumnList.Initializable
	{
		public SqlConstraintReferential.Mutable setReferencedTable(BaseTableDescriptor referencedTable);

		@Override
		public XEnum<BaseTableColumn.Descriptor> getReferencedColumns();

		public SqlConstraintReferential.Mutable setSourceIndex(SqlIndex index);

		public SqlConstraintReferential.Mutable setTargetConstraint(SqlConstraintUnique uniqueConstraint);

	}



	public abstract class Implementation
	extends SqlConstraintUsingColumnList.Implementation
	implements Mutable
	{
		///////////////////////////////////////////////////////////////////////////
		// instance fields  //
		/////////////////////

		BaseTableDescriptor referencedTable = null;
		SqlIndex sourceIndex = null;
		SqlConstraintUnique targetConstraint = null;
		final EqHashEnum<BaseTableColumn.Descriptor> referencedColumns = new EqHashEnum<>();



		///////////////////////////////////////////////////////////////////////////
		// constructors     //
		/////////////////////

		public Implementation()
		{
			super();
		}

		public Implementation(
			final SqlIdentifier identifier,
			final BaseTableDescriptor owner,
			final SqlIdentifier oldIdentifier,
			final boolean initialized
		)
		{
			super(identifier, owner, oldIdentifier, initialized);
		}



		///////////////////////////////////////////////////////////////////////////
		// getters          //
		/////////////////////

		@Override
		public BaseTableDescriptor getReferencedTable()
		{
			return this.referencedTable;
		}

		@Override
		public XEnum<BaseTableColumn.Descriptor> getReferencedColumns()
		{
			return this.referencedColumns;
		}

		@Override
		public SqlIndex getSourceIndex()
		{
			return this.sourceIndex;
		}

		@Override
		public SqlConstraintUnique getTargetConstraint()
		{
			return this.targetConstraint;
		}



		///////////////////////////////////////////////////////////////////////////
		// setters          //
		/////////////////////

		@Override
		protected void internalRegisterAtOwner(final BaseTableDescriptor.Mutable owner)
		{
			owner.registerLocalReferentialConstraint(this);
			this.getMutableColumns().execute(new Procedure<BaseTableColumn.Descriptor>() {
				@Override public void apply(final BaseTableColumn.Descriptor e) {
					if(e instanceof BaseTableColumn.Descriptor.Mutable){
						((BaseTableColumn.Descriptor.Mutable)e).registerUsingReferentialConstraint(SqlConstraintReferential.Implementation.this);
					}
				}
			});
		}

		@Override
		public SqlConstraintReferential.Implementation linkTable(final SqlBaseTable owner)
		{
			this.internalSetOwner(owner.sqlDescriptor());
			return this;
		}

		protected void internalSetReferencedTable(final BaseTableDescriptor referencedTable)
		{
			BaseTableDescriptor tableDescriptor = referencedTable.sqlDescriptor();
			if(tableDescriptor == null){
				// if table is not initialized, yet, create a stub instead.
				tableDescriptor = TableDelegates.table(referencedTable.sqlSchemaName(), referencedTable.sqlName());
			}
			this.referencedTable = tableDescriptor;
		}

		@Override
		public SqlConstraintReferential.Implementation setSourceIndex(final SqlIndex index)
		{
			this.sourceIndex = index;
			return this;
		}

		@Override
		public SqlConstraintReferential.Implementation setTargetConstraint(final SqlConstraintUnique uniqueConstraint)
		{
			this.targetConstraint = uniqueConstraint;
			return this;
		}



		///////////////////////////////////////////////////////////////////////////
		// override methods //
		/////////////////////

		protected void setActualColumns(
			final XEnum<BaseTableColumn.Descriptor> currentColumns,
			final XGettingEnum<? extends BaseTableColumn.Descriptor> actualColumns,
			final BaseTableDescriptor table
		)
		{
			// (05.11.2011)NOTE: two-dimensional iteration without lambda syntax. No fun :-/.
			currentColumns.modify(new Function<BaseTableColumn.Descriptor, BaseTableColumn.Descriptor>() {
				@Override public BaseTableColumn.Descriptor apply(final BaseTableColumn.Descriptor lc) {
					final BaseTableColumn.Descriptor matchingRemoteColumns =
						actualColumns.search(new Predicate<BaseTableColumn.Descriptor>(){
							@Override public boolean apply(final BaseTableColumn.Descriptor rc) {
								return rc == lc || rc.sqlName().equals(lc.sqlName());
							}
						})
					;
					if(matchingRemoteColumns == null){
						throw new SqlEngineTableInitializationException(
							"Column mismatch: "+lc+" not found in "+table
						);
					}
					return matchingRemoteColumns;
				}
			});
		}

		@Override
		public void validateColumns(final BaseTableDescriptor owner)
		{
			this.setActualColumns(this.getMutableColumns(), owner.columns(), owner);
		}

		@Override
		public void validateReferencedColumns(final BaseTableDescriptor referencedTable)
		{
			this.setActualColumns(this.referencedColumns, referencedTable.columns(), referencedTable);
		}

		@Override
		public VarChar sqlAssemble(final DbmsSqlAssembler<?> assembler, final VarChar vc, final int flags)
		{
			return assembler.ddlAssemble(vc, flags, this);
		}

		@Override
		public SqlIdentifier createGeneratedTargetConstraintName(final BaseTableDescriptor referencedTable)
		{
			final SqlIdentifier refTableName = referencedTable.sqlIdentifier();
			final SqlIdentifier generatedTargetConstraintName = SqlName.constraintName(
				this.sqlName()+"_generatedTarget_"+refTableName.sqlName()
			);
			return generatedTargetConstraintName;
		}

		@Override
		public SqlIdentifier createGeneratedSourceIndexName()
		{
			final SqlIdentifier generatedSourceIndexName = SqlName.indexName(
				this.sqlName()+"_generatedSource"
			);
			return generatedSourceIndexName;
		}

		@Override
		public String toString()
		{
			return "FOREIGN KEY "+this.sqlName()+" ON "+this.sqlOwner()+this.columns()+"\nREFERENCES "+this.referencedTable+this.referencedColumns;
		}

	}
}
