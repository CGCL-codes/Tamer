package net.jadoth.sqlengine.sql.descriptors;

import static net.jadoth.sqlengine.sql.types.SqlIndexType.NORMAL;
import static net.jadoth.util.chars.JaChars.isEqual;
import static net.jadoth.util.chars.JaChars.isEqualIgnoreCase;

import java.lang.reflect.Field;

import net.jadoth.Jadoth;
import net.jadoth.collections.EmptyTable;
import net.jadoth.collections.EqHashEnum;
import net.jadoth.collections.EqHashTable;
import net.jadoth.collections.HashEnum;
import net.jadoth.collections.X;
import net.jadoth.collections.types.XEnum;
import net.jadoth.collections.types.XGettingEnum;
import net.jadoth.collections.types.XGettingList;
import net.jadoth.collections.types.XGettingTable;
import net.jadoth.lang.Equalator;
import net.jadoth.lang.functional.Function;
import net.jadoth.lang.functional.IndexProcedure;
import net.jadoth.lang.functional.Predicate;
import net.jadoth.lang.functional.Procedure;
import net.jadoth.sqlengine.SqlCommonTypeNames;
import net.jadoth.sqlengine.SqlEngineTableInitializationException;
import net.jadoth.sqlengine.SqlName;
import net.jadoth.sqlengine.SqlUtils;
import net.jadoth.sqlengine.dbms.DbmsSqlAssembler;
import net.jadoth.sqlengine.sql.definitions.BaseTableColumn;
import net.jadoth.sqlengine.sql.definitions.BaseTableMember;
import net.jadoth.sqlengine.sql.definitions.SqlBaseTable;
import net.jadoth.sqlengine.sql.definitions.SqlColumnDescriptor;
import net.jadoth.sqlengine.sql.definitions.SqlConstraintCheck;
import net.jadoth.sqlengine.sql.definitions.SqlConstraintPrimaryKey;
import net.jadoth.sqlengine.sql.definitions.SqlConstraintReferential;
import net.jadoth.sqlengine.sql.definitions.SqlConstraintUnique;
import net.jadoth.sqlengine.sql.definitions.SqlIdentifier;
import net.jadoth.sqlengine.sql.definitions.SqlIndex;
import net.jadoth.sqlengine.sql.definitions.SqlTrigger;
import net.jadoth.sqlengine.sql.definitions.SqlUniqueIndex;
import net.jadoth.sqlengine.sql.definitions.TableMember;
import net.jadoth.sqlengine.sql.syntax.CREATE_TABLE;
import net.jadoth.sqlengine.sql.syntax.SQL;
import net.jadoth.sqlengine.sql.types.SqlCatalog;
import net.jadoth.sqlengine.sql.types.SqlPersistenceType;
import net.jadoth.sqlengine.sql.types.SqlSchema;
import net.jadoth.util.chars.VarChar;

public interface BaseTableDescriptor extends SqlBaseTable, TableDescriptor
{
	@Override
	public XGettingEnum<? extends BaseTableColumn.Descriptor> columns();

	public SqlPersistenceType sqlPersistenceType();

	public SqlConstraintPrimaryKey getPrimaryKey();

	public XGettingEnum<SqlConstraintReferential> getReferencingForeignKeys();

	public <T extends BaseTableMember<?>> XGettingEnum<T> get(Class<T> type, Predicate<? super BaseTableMember<?>> predicate);
	public <T extends BaseTableMember<?>> XGettingEnum<T> get(Class<T> type);


	public CREATE_TABLE CREATE_TABLE();

	public VarChar describe(VarChar vc);

	public XGettingTable<Field, BaseTableMember<?>> getMembersRegisteredByField();
	public XGettingTable<String, BaseTableMember<?>> getMembersRegisteredByName();



	public static final Equalator<BaseTableDescriptor> equalName = new Equalator<BaseTableDescriptor>(){
		@Override public boolean equal(final BaseTableDescriptor t1, final BaseTableDescriptor t2) {
			return isEqual(t1.sqlSchemaName(), t2.sqlSchemaName()) && isEqual(t1.sqlName(), t2.sqlName());
		}
	};

	public static final Equalator<BaseTableDescriptor> equalNameIgnoreCase = new Equalator<BaseTableDescriptor>(){
		@Override public boolean equal(final BaseTableDescriptor t1, final BaseTableDescriptor t2) {
			return isEqualIgnoreCase(t1.sqlSchemaName(), t2.sqlSchemaName()) && isEqualIgnoreCase(t1.sqlName(), t2.sqlName());
		}
	};


	public interface Mutable extends BaseTableDescriptor
	{
		public boolean isInitialized();

		public void initializeSqlColumnDescriptors(XGettingEnum<? extends BaseTableColumn.Descriptor> columnDescriptors);

		public BaseTableDescriptor setPrimaryKey(SqlConstraintPrimaryKey primaryKey);

		public BaseTableDescriptor setSchema(SqlSchema<SqlCatalog<?>> schema);

		public BaseTableDescriptor registerIndex(SqlIndex index);
		public BaseTableDescriptor registerUniqueConstraint(SqlConstraintUnique constraint);
		public BaseTableDescriptor registerCheckConstraint(SqlConstraintCheck constraint);
		public BaseTableDescriptor registerLocalReferentialConstraint(SqlConstraintReferential constraint);
		public BaseTableDescriptor registerReferencingReferentialConstraint(SqlConstraintReferential constraint);
		public BaseTableDescriptor registerTrigger(SqlTrigger trigger);

		public boolean registerMember(BaseTableMember<? extends SqlBaseTable> member, String memberName);
		public boolean registerMember(BaseTableMember.InitItem memberItem);
	}




	public class Implementation extends TableDescriptor.Implementation implements BaseTableDescriptor.Mutable
	{
		///////////////////////////////////////////////////////////////////////////
		// constants        //
		/////////////////////

		static final Function<SqlConstraintUnique, SqlUniqueIndex> getUniqueIndex = new Function<SqlConstraintUnique, SqlUniqueIndex>(){
			@Override public SqlUniqueIndex apply(final SqlConstraintUnique input) {
				return input.getUniqueIndex();
			}
		};



		///////////////////////////////////////////////////////////////////////////
		// instance fields  //
		/////////////////////

		private final SqlPersistenceType persistenceType;

		private SqlConstraintPrimaryKey               primaryKey        = null;

		// all FOREIGN KEYS referencing this table
		private final XEnum<SqlConstraintReferential> refForeignKeys    = new HashEnum<>();

		final EqHashEnum<BaseTableColumn.Descriptor> columnDescriptors = EqHashEnum.<BaseTableColumn.Descriptor>New(SqlColumnDescriptor.equalName);

		EqHashTable<Field, BaseTableMember<?>>   memberRegistryByField     = null; // lazy instantiated on demand
		XGettingTable<Field, BaseTableMember<?>> viewMemberRegistryByField = new EmptyTable<>();

		EqHashTable<String, BaseTableMember<?>>   memberRegistryByName     = X.Map(String.class);
		XGettingTable<String, BaseTableMember<?>> viewMemberRegistryByName = this.memberRegistryByName.view();
		EqHashTable<String, BaseTableMember<?>>.Values members             = this.memberRegistryByName.values();



		///////////////////////////////////////////////////////////////////////////
		// constructors     //
		/////////////////////

		public Implementation(final SqlSchema<SqlCatalog<?>> schema, final SqlIdentifier name)
		{
			this(schema, name, SqlPersistenceType.PERSISTENT);
		}

		public Implementation(
			final SqlSchema<SqlCatalog<?>> schema,
			final SqlIdentifier name,
			final SqlPersistenceType persistenceType
		)
		{
			this(schema, name, SqlName.aliasName(SqlName.deriveAlias(name.sqlName())), persistenceType);
		}

		public Implementation(
			final SqlSchema<SqlCatalog<?>> schema,
			final SqlIdentifier name,
			final SqlIdentifier alias,
			final SqlPersistenceType persistenceType
		)
		{
			super(schema, name, alias);
			this.persistenceType = persistenceType;
		}



		///////////////////////////////////////////////////////////////////////////
		// getters          //
		/////////////////////

		@Override
		public SqlPersistenceType sqlPersistenceType()
		{
			return this.persistenceType;
		}

		@Override
		public boolean isInitialized()
		{
			return !this.columnDescriptors.isEmpty();
		}

		@Override
		public SqlConstraintPrimaryKey getPrimaryKey()
		{
			return this.primaryKey;
		}

		@Override
		public <T extends BaseTableMember<?>> XGettingEnum<T> get(final Class<T> type)
		{
			/*(28.11.2011)NOTE:
			 * the interesting question here is: is this an unclean instanceof-programming style
			 * or is it a perfectly valid downcast (and safe and non-heap-polluting, btw) collecting algorithm?
			 * Looks a little like both ^^.
			 * But I don't have an idea how to better query a central member collection
			 */
			final HashEnum<T> result = new HashEnum<>();
			this.members.execute(new Procedure<TableMember<?>>() {
				@Override public void apply(final TableMember<?> e) {
					if(type.isInstance(e)){
						result.add(type.cast(e));
					}
				}
			});
			return result;
		}

		@Override
		public <T extends BaseTableMember<?>> XGettingEnum<T> get(
			final Class<T> type,
			final Predicate<? super BaseTableMember<?>> predicate
		)
		{
			/*(28.11.2011)NOTE:
			 * the interesting question here is: is this an unclean instanceof-programming style
			 * or is it a perfectly valid downcast (and safe and non-heap-polluting, btw) collecting algorithm?
			 * Looks a little like both ^^.
			 * But I don't have an idea how to better query a central member collection
			 */
			final HashEnum<T> result = new HashEnum<>();
			this.members.execute(new Procedure<BaseTableMember<?>>() {
				@Override public void apply(final BaseTableMember<?> e) {
					if(type.isInstance(e) && predicate.apply(e)){
						result.add(type.cast(e));
					}
				}
			});
			return result;
		}


		@Override
		public XGettingEnum<SqlConstraintReferential> getReferencingForeignKeys()
		{
			return this.refForeignKeys;
		}


		@Override
		public int getColumnCount()
		{
			return this.columnDescriptors.size();
		}

		@Override
		public XGettingEnum<? extends BaseTableColumn.Descriptor> columns()
		{
			return this.columnDescriptors;
		}



		///////////////////////////////////////////////////////////////////////////
		// setters          //
		/////////////////////

		@Override
		public BaseTableDescriptor.Implementation setPrimaryKey(final SqlConstraintPrimaryKey primaryKey)
		{
			this.primaryKey = primaryKey;
			return this;
		}

		@Override
		public BaseTableDescriptor setSchema(final SqlSchema<SqlCatalog<?>> schema)
		{
			this.internalSetSchema(schema);
			return this;
		}



		///////////////////////////////////////////////////////////////////////////
		// override methods //
		/////////////////////

		@Override
		public SqlBaseTable AS(final Object alias)
		{
			return new SqlBaseTable.Implementation(this, SqlName.aliasName(alias));
		}

		@Override
		public VarChar sqlAssemble(final DbmsSqlAssembler<?> assembler, final VarChar vc, final int flags)
		{
			return assembler.assembleIdentifier(vc, flags, this);
		}

		@Override
		public VarChar sqlDdlAssemble(final DbmsSqlAssembler<?> assembler, final VarChar vc, final int flags)
		{
			return assembler.ddlAssemble(vc, flags, this, true);
		}

		@Override
		public void initializeSqlColumnDescriptors(final XGettingEnum<? extends BaseTableColumn.Descriptor> columnDescriptors)
		{
			if(this.isInitialized()){
				throw new SqlEngineTableInitializationException("Already initialized");
			}
			this.columnDescriptors.addAll(columnDescriptors);
			columnDescriptors.execute(new IndexProcedure<BaseTableColumn.Descriptor>() {
				@Override public void apply(final BaseTableColumn.Descriptor e, final int index) {
					if(e instanceof BaseTableColumn.Descriptor.Mutable){
						((BaseTableColumn.Descriptor.Mutable)e).setOwner(BaseTableDescriptor.Implementation.this);
						((BaseTableColumn.Descriptor.Mutable)e).setOrdinalPosition(index+1);
					}
					BaseTableDescriptor.Implementation.this.memberRegistryByName.add(e.sqlName(), e);
				}
			});
		}


		protected void linkSourceIndexFor(final SqlConstraintReferential referential)
		{
			final XGettingEnum<BaseTableColumn.Descriptor> fkColumns = referential.columns();
			SqlIndex matchingIndex = (SqlIndex)this.members.search(new Predicate<BaseTableMember<?>>() {
				@Override public boolean apply(final BaseTableMember<?> e) {
					return e instanceof SqlIndex && ((SqlIndex)e).columns().equalsContent(fkColumns, Jadoth.identical);
				}
			});
			if(matchingIndex == null){
				final SqlIdentifier generatedIndexName = referential.createGeneratedSourceIndexName();
				final SqlIndex.Implementation newIndex = new SqlIndex.Implementation(NORMAL, generatedIndexName, this, null, true)
					.setIsReferentialGenerated(true)
				;
				newIndex.getMutableColumns().addAll(fkColumns);
				matchingIndex = newIndex;
				matchingIndex.linkTable(this);
			}
			if(matchingIndex instanceof SqlIndex.Mutable){
				((SqlIndex.Mutable)matchingIndex).getMutableUsingReferentialConstraints().add(referential);
			}
			if(referential instanceof SqlConstraintReferential.Mutable){
				((SqlConstraintReferential.Mutable)referential).setSourceIndex(matchingIndex);
			}
		}

		@Override
		public CREATE_TABLE CREATE_TABLE()
		{
			return SQL.CREATE_TABLE(this);
		}

		@Override
		public BaseTableDescriptor sqlDescriptor()
		{
			return this;
		}

		@Override
		public VarChar describe(final VarChar vc)
		{
			SqlUtils.describeTable(this, vc);
			return vc;
		}

		@Override
		public BaseTableDescriptor registerIndex(final SqlIndex index)
		{
			this.memberRegistryByName.add(index.sqlName(), index);
			return this;
		}

		@Override
		public BaseTableDescriptor registerUniqueConstraint(final SqlConstraintUnique constraint)
		{
			this.memberRegistryByName.add(constraint.sqlName(), constraint);
			return this;
		}

		@Override
		public BaseTableDescriptor registerCheckConstraint(final SqlConstraintCheck constraint)
		{
			this.memberRegistryByName.add(constraint.sqlName(), constraint);
			return this;
		}

		@Override
		public BaseTableDescriptor registerLocalReferentialConstraint(final SqlConstraintReferential constraint)
		{
			this.memberRegistryByName.add(constraint.sqlName(), constraint);
			constraint.validateColumns(this);
			this.linkSourceIndexFor(constraint);
			return this;
		}

		@Override
		public BaseTableDescriptor registerReferencingReferentialConstraint(final SqlConstraintReferential constraint)
		{
			this.refForeignKeys.add(constraint);
			return this;
		}

		@Override
		public BaseTableDescriptor registerTrigger(final SqlTrigger trigger)
		{
			this.memberRegistryByName.add(trigger.sqlName(), trigger);
			return this;
		}

		@Override
		public XGettingTable<Field, BaseTableMember<?>> getMembersRegisteredByField()
		{
			return this.viewMemberRegistryByField;
		}

		@Override
		public XGettingTable<String, BaseTableMember<?>> getMembersRegisteredByName()
		{
			return this.viewMemberRegistryByName;
		}

		@Override
		public boolean registerMember(final BaseTableMember<? extends SqlBaseTable> member, final String memberName)
		{
			return this.memberRegistryByName.add(memberName, member);
		}

		@Override
		public boolean registerMember(final BaseTableMember.InitItem memberItem)
		{
			final BaseTableMember<? extends SqlBaseTable> member = memberItem.getMember().sqlDescriptorMember();
//			final BaseTableMember<SqlBaseTable> actualMember = member instanceof COLUMN ?((COLUMN)member).sqlDescriptor() :member;
			final String memberSqlName = BaseTableMember.Static.getMemberName(memberItem);
			final boolean addedByName = this.registerMember(member, memberSqlName);

			final Field field = memberItem.getDeclaringField();
			if(field != null && !memberItem.isDefinedInArray()){
				if(this.memberRegistryByField == null){ // create reflection registry instance only on demand
					this.viewMemberRegistryByField = (this.memberRegistryByField = X.Map(Field.class)).view();
				}
				final boolean addedByField = this.memberRegistryByField.add(field, member);
				if(!addedByField && addedByName){
					// rollback registration by name
					this.memberRegistryByName.remove(member.sqlName());
				}
				return addedByName || addedByField;
			}
			return addedByName;
		}

		@Override
		public String sqlSyntaxName()
		{
			return SqlCommonTypeNames.TABLE;
		}

	}



	public interface Reference extends BaseTableDescriptor
	{
		public BaseTableDescriptor getReferencedTableDescriptor();

		public interface Mutable extends Reference
		{
			public void setReferencedTableDescriptor(BaseTableDescriptor tableDescriptor);
		}

	}



	public class Decorator implements Reference
	{
		///////////////////////////////////////////////////////////////////////////
		// instance fields  //
		/////////////////////

		BaseTableDescriptor descriptor;



		@Override
		public <T extends BaseTableMember<?>> XGettingEnum<T> get(final Class<T> type)
		{
			return this.descriptor.get(type);
		}



		@Override
		public <T extends BaseTableMember<?>> XGettingEnum<T> get(
			final Class<T> type,
			final Predicate<? super BaseTableMember<?>> predicate
		)
		{
			return this.descriptor.get(type, predicate);
		}



		///////////////////////////////////////////////////////////////////////////
		// constructors     //
		/////////////////////

		public Decorator(final BaseTableDescriptor descriptor)
		{
			super();
			this.descriptor = descriptor;
		}



		///////////////////////////////////////////////////////////////////////////
		// declared methods //
		/////////////////////

		protected void setReferencedTableDescriptor(final BaseTableDescriptor descriptor)
		{
			this.descriptor = descriptor;
		}



		///////////////////////////////////////////////////////////////////////////
		// override methods //
		/////////////////////

		@Override
		public XGettingList<Object> getCustomProperties()
		{
			return this.descriptor.getCustomProperties();
		}

		@Override
		public XGettingEnum<? extends BaseTableColumn.Descriptor> columns()
		{
			return this.descriptor.columns();
		}

		@Override
		public SqlSchema<SqlCatalog<?>> sqlSchema()
		{
			return this.descriptor.sqlSchema();
		}

		@Override
		public String sqlName()
		{
			return this.descriptor.sqlName();
		}

		@Override
		public String sqlSchemaName()
		{
			return this.descriptor.sqlSchemaName();
		}

		@Override
		public SqlIdentifier sqlIdentifier()
		{
			return this.descriptor.sqlIdentifier();
		}

		@Override
		public VarChar sqlAssemble(final DbmsSqlAssembler<?> assembler, final VarChar vc, final int flags)
		{
			return this.descriptor.sqlAssemble(assembler, vc, flags);
		}

		@Override
		public VarChar sqlDdlAssemble(final DbmsSqlAssembler<?> assembler, final VarChar vc, final int flags)
		{
			return this.descriptor.sqlDdlAssemble(assembler, vc, flags);
		}

		@Override
		public SqlIdentifier sqlAlias()
		{
			return this.descriptor.sqlAlias();
		}

		@Override
		public SqlBaseTable AS(final Object newAlias)
		{
			return this.descriptor.AS(newAlias);
		}

		@Override
		public int getColumnCount()
		{
			return this.descriptor.getColumnCount();
		}

		@Override
		public SqlConstraintPrimaryKey getPrimaryKey()
		{
			return this.descriptor.getPrimaryKey();
		}

		@Override
		public SqlPersistenceType sqlPersistenceType()
		{
			return this.descriptor.sqlPersistenceType();
		}

		@Override
		public net.jadoth.sqlengine.sql.syntax.CREATE_TABLE CREATE_TABLE()
		{
			return this.descriptor.CREATE_TABLE();
		}

		@Override
		public BaseTableDescriptor sqlDescriptor()
		{
			return this; // must keep own identity
		}

		@Override
		public String sqlQualifiedName()
		{
			return this.descriptor.sqlQualifiedName();
		}

		@Override
		public XGettingEnum<SqlConstraintReferential> getReferencingForeignKeys()
		{
			return this.descriptor.getReferencingForeignKeys();
		}

		@Override
		public VarChar describe(final VarChar vc)
		{
			return this.descriptor.describe(vc);
		}

		@Override
		public BaseTableDescriptor getReferencedTableDescriptor()
		{
			return this.descriptor;
		}

		@Override
		public XGettingTable<Field, BaseTableMember<?>> getMembersRegisteredByField()
		{
			return new EmptyTable<>();
		}

		@Override
		public XGettingTable<String, BaseTableMember<?>> getMembersRegisteredByName()
		{
			return new EmptyTable<>();
		}

		@Override
		public String sqlSyntaxName()
		{
			return SqlCommonTypeNames.TABLE;
		}

	}


	public class Stub extends Decorator
	{

		public Stub(final BaseTableDescriptor descriptor)
		{
			super(descriptor);
		}

		public Stub(final SqlSchema<SqlCatalog<?>> schema, final SqlIdentifier name)
		{
			this(new BaseTableDescriptor.Implementation(schema, name));
		}

		@Override
		public BaseTableDescriptor getReferencedTableDescriptor()
		{
			// returning null indicates this stub can (shall) be replaced by the actual TableDescriptor
			return null;
		}

		@Override
		public String sqlSyntaxName()
		{
			return SqlCommonTypeNames.TABLE;
		}

		@Override
		public String toString()
		{
			return this.getClass().getSimpleName()+" "+this.sqlQualifiedName();
		}

	}

}
