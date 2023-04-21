package net.jadoth.sqlengine;

import static java.lang.Math.min;
import static net.jadoth.Jadoth.coalesce;
import static net.jadoth.util.chars.VarChar.SmallVarChar;
import net.jadoth.Jadoth;
import net.jadoth.collections.MiniMap;
import net.jadoth.lang.Equalator;
import net.jadoth.lang.HashEqualator;
import net.jadoth.lang.functional.Function;
import net.jadoth.lang.functional.IndexFunction;
import net.jadoth.sqlengine.sql.definitions.BaseTableColumn;
import net.jadoth.sqlengine.sql.definitions.BaseTableColumn.InitItem;
import net.jadoth.sqlengine.sql.definitions.BaseTableMember;
import net.jadoth.sqlengine.sql.definitions.SqlBaseTable;
import net.jadoth.sqlengine.sql.definitions.SqlIdentifier;
import net.jadoth.sqlengine.sql.definitions.SqlNamed;
import net.jadoth.sqlengine.sql.definitions.SqlSchemaMember;
import net.jadoth.sqlengine.sql.definitions.SqlTable;
import net.jadoth.sqlengine.sql.statements.SqlQuery;
import net.jadoth.sqlengine.sql.syntax.SQL;
import net.jadoth.sqlengine.sql.types.SqlSchema;
import net.jadoth.util.chars.VarChar;

public class SqlName
{
	///////////////////////////////////////////////////////////////////////////
	// constants        //
	/////////////////////

	static final String Tbl = "Tbl";
	static final String Scm = "Scm";
	static final String Viw = "Viw";

	static final Function<Object, SqlIdentifier> DEFAULT_IDENTIFIOR = new Function<Object, SqlIdentifier>(){
		@Override public SqlIdentifier apply(final Object identifier) {
			return SQL.identifier(identifier);
		}
	};

	static final Function<Object, SqlIdentifier> DEFAULT_IDENTIFIOR_UNDELIMITING = new Function<Object, SqlIdentifier>(){
		@Override public SqlIdentifier apply(final Object identifier) {
			if(identifier instanceof String){
				return new SqlIdentifier.Implementation((String)identifier, Boolean.FALSE);
			}
			return SQL.identifier(identifier, Boolean.FALSE);
		}
	};

	private static final HashEqualator<SqlIdentifier> DEFAULT_IDENTIFIER_EQUALITY = new HashEqualator<SqlIdentifier>()
	{
		@Override
		public boolean equal(final SqlIdentifier identifier1, final SqlIdentifier identifier2)
		{
			return identifier1 == identifier2
				|| identifier1 != null && identifier2 != null && Jadoth.equal(identifier1.sqlName(), identifier2.sqlName())
			;
		}

		@Override
		public int hash(final SqlIdentifier identifier)
		{
			return identifier == null ?0 :identifier.sqlName().hashCode();
		}
	};

	static final Function<String, String> DEFAULT_ALIAS_DERIVER = new Function<String, String>(){
		@Override public String apply(final String tablename){
			return SqlName.deriveAlias(tablename, 3, 5);
		}
	};

	static final Function<Class<? extends SqlTable>, String> DEFAULT_TABLENAME_DERIVER =
		new Function<Class<? extends SqlTable>, String>(){
			final MiniMap<Class<?>, String> cache = new MiniMap<>();
			@Override public String apply(final Class<? extends SqlTable> tableClass){
				/* (22.11.2011)NOTE: simple default implementation.
				 * Might be extended by smarter algorithm, configurability
				 * or by searching for a declared "static final String NAME"
				 * or annotated field recursively, etc.
				 */
				String cachedName;
				if((cachedName = this.cache.get(tableClass)) == null){
					final String className = tableClass.getSimpleName();
					cachedName = Tbl.equalsIgnoreCase(className.substring(0, 3)) ?className.substring(3) :className;
					synchronized(this.cache){
						this.cache.put(tableClass, cachedName);
					}
				}
				return cachedName;
			}
		}
	;

	static final Function<Class<? extends SqlTable>, String> DEFAULT_VIEWNAME_DERIVER =
		new Function<Class<? extends SqlTable>, String>(){
			final MiniMap<Class<?>, String> cache = new MiniMap<>();
			@Override public String apply(final Class<? extends SqlTable> viewClass){
				/* (22.11.2011)NOTE: simple default implementation.
				 * Might be extended by smarter algorithm, configurability
				 * or by searching for a declared "static final String NAME"
				 * or annotated field recursively, etc.
				 */
				String cachedName;
				if((cachedName = this.cache.get(viewClass)) == null){
					final String className = viewClass.getSimpleName();
					cachedName = Viw.equalsIgnoreCase(className.substring(0, 3)) ?className.substring(3) :className;
					synchronized(this.cache){
						this.cache.put(viewClass, cachedName);
					}
				}
				return cachedName;
			}
		}
	;

	static final Function<BaseTableColumn.InitItem, String> DEFAULT_TABLECOLUMNNAME_DERIVER =
		new Function<BaseTableColumn.InitItem, String>(){
			@Override
			public String apply(final InitItem initItem)
			{
				return initItem.getDeclaringField().getName(); // provoke NPE as column cannot be unnamed
			}
		}
	;

	static final Function<BaseTableMember.InitItem, String> DEFAULT_TABLEMEMBERNAME_DERIVER =
		new Function<BaseTableMember.InitItem, String>(){
			void addTypeIndicator(final VarChar vc, final String fieldDefName, final String typeAcronym)
			{
				if(typeAcronym == null || fieldDefName.length() >= typeAcronym.length()
				&& fieldDefName.substring(0, typeAcronym.length()).equals(typeAcronym.toLowerCase())
				&& !Character.isLowerCase(fieldDefName.charAt(typeAcronym.length()))
				){
					return;
				}
				vc.add(typeAcronym).add('_');
			}

			@Override public String apply(final BaseTableMember.InitItem initItem) {
				final BaseTableMember.Mutable<? extends SqlBaseTable> member = initItem.getMember();
				// intentionally don't check initialized state here as this is a name deriver, not an initializer
				final VarChar vc = SmallVarChar().add(initItem.getTable().sqlName()).add('_');
				final String fieldDefName = initItem.getFieldDefinitionName();
				this.addTypeIndicator(vc, fieldDefName, SqlCommonTypeNames.getAcronym(member.sqlSyntaxName()));
				vc.add(fieldDefName);
				return vc.toString();
			}
		}
	;

	static final Function<Class<? extends SqlSchema<?>>, String> DEFAULT_SCHEMANAME_DERIVER =
		new Function<Class<? extends SqlSchema<?>>, String>(){
			final MiniMap<Class<?>, String> cache = new MiniMap<>();
			@Override public String apply(final Class<? extends SqlSchema<?>> schemaClass){
				/* (22.11.2011)NOTE: simple default implementation.
				 * Might be extended by smarter algorithm, configurability
				 * or by searching for a declared "static final String NAME"
				 * or annotated field recursively, etc.
				 */
				String cachedName;
				if((cachedName = this.cache.get(schemaClass)) == null){
					final String className = schemaClass.getSimpleName();
					cachedName = Scm.equalsIgnoreCase(className.substring(0, 3))?className.substring(3):className;
					cachedName = cachedName.toLowerCase();
					synchronized(this.cache){
						this.cache.put(schemaClass, cachedName);
					}
				}
				return cachedName;
			}
		}
	;

	private static final IndexFunction<SqlQuery<?>, SqlIdentifier> DEFAULT_GENERIC_COLNAME_MAPPER = new IndexFunction<SqlQuery<?>, SqlIdentifier>(){
		@Override public SqlIdentifier apply(final SqlQuery<?> query, final int index) {
			if(index < 0 || query != null && index > query.columns().size()){
				throw new IndexOutOfBoundsException("Invalid column index: "+index);
			}
			return new SqlIdentifier.Implementation("COLUMN_"+index, Boolean.FALSE);
		}
	};



	///////////////////////////////////////////////////////////////////////////
	// static fields    //
	/////////////////////

	static Function<Object, SqlIdentifier> catalogIdentifior    = DEFAULT_IDENTIFIOR;
	static Function<Object, SqlIdentifier> schemaIdentifior     = DEFAULT_IDENTIFIOR;
	static Function<Object, SqlIdentifier> tableIdentifior      = DEFAULT_IDENTIFIOR;
	static Function<Object, SqlIdentifier> columnIdentifior     = DEFAULT_IDENTIFIOR;
	static Function<Object, SqlIdentifier> indexIdentifior      = DEFAULT_IDENTIFIOR;
	static Function<Object, SqlIdentifier> constraintIdentifior = DEFAULT_IDENTIFIOR;
	static Function<Object, SqlIdentifier> triggerIdentifior    = DEFAULT_IDENTIFIOR;
	static Function<Object, SqlIdentifier> aliasIdentifior      = DEFAULT_IDENTIFIOR_UNDELIMITING;
	static Function<Object, SqlIdentifier> viewIdentifior       = DEFAULT_IDENTIFIOR;
	static Function<Object, SqlIdentifier> routineIdentifior  = DEFAULT_IDENTIFIOR;


	private static Equalator<SqlIdentifier> identifierEquality = DEFAULT_IDENTIFIER_EQUALITY;

	private static Function<Class<? extends SqlSchema<?>>, String> schemaNameDeriver      = DEFAULT_SCHEMANAME_DERIVER;
	private static Function<Class<? extends SqlTable>, String>     tableNameDeriver       = DEFAULT_TABLENAME_DERIVER;
	private static Function<Class<? extends SqlTable>, String>     viewNameDeriver        = DEFAULT_VIEWNAME_DERIVER;
	private static Function<BaseTableColumn.InitItem, String>      tableColumnNameDeriver = DEFAULT_TABLECOLUMNNAME_DERIVER;
	private static Function<BaseTableMember.InitItem, String>      tableMemberNameDeriver = DEFAULT_TABLEMEMBERNAME_DERIVER;
	private static Function<String, String>                        aliasDeriver           = DEFAULT_ALIAS_DERIVER;

	private static IndexFunction<SqlQuery<?>, SqlIdentifier> genericColumnNameMapper = DEFAULT_GENERIC_COLNAME_MAPPER;



	///////////////////////////////////////////////////////////////////////////
	// getters          //
	/////////////////////

	public static Function<Object, SqlIdentifier> getCatalogIdentifior()
	{
		return catalogIdentifior;
	}

	public static Function<Object, SqlIdentifier> getSchemaIdentifior()
	{
		return schemaIdentifior;
	}

	public static Function<Object, SqlIdentifier> getTableIdentifior()
	{
		return tableIdentifior;
	}

	public static Function<Object, SqlIdentifier> getColumnIdentifior()
	{
		return columnIdentifior;
	}

	public static Function<Object, SqlIdentifier> getIndexIdentifior()
	{
		return indexIdentifior;
	}

	public static Function<Object, SqlIdentifier> getConstraintIdentifior()
	{
		return constraintIdentifior;
	}

	public static Function<Object, SqlIdentifier> getTriggerIdentifior()
	{
		return triggerIdentifior;
	}

	public static Function<Object, SqlIdentifier> getAliasIdentifior()
	{
		return aliasIdentifior;
	}

	public static Function<Object, SqlIdentifier> getViewIdentifior()
	{
		return viewIdentifior;
	}

	public static Function<Object, SqlIdentifier> getProcedureIdentifior()
	{
		return routineIdentifior;
	}



	///////////////////////////////////////////////////////////////////////////
	// setters          //
	/////////////////////

	public static void setCatalogIdentifior(final Function<Object, SqlIdentifier> catalogIdentifior)
	{
		SqlName.catalogIdentifior = coalesce(catalogIdentifior, DEFAULT_IDENTIFIOR);
	}

	public static void setSchemaIdentifior(final Function<Object, SqlIdentifier> schemaIdentifior)
	{
		SqlName.schemaIdentifior = coalesce(schemaIdentifior, DEFAULT_IDENTIFIOR);
	}

	public static void setTableIdentifior(final Function<Object, SqlIdentifier> tableIdentifior)
	{
		SqlName.tableIdentifior = coalesce(tableIdentifior, DEFAULT_IDENTIFIOR);
	}

	public static void setColumnIdentifior(final Function<Object, SqlIdentifier> columnIdentifior)
	{
		SqlName.columnIdentifior = coalesce(columnIdentifior, DEFAULT_IDENTIFIOR);
	}

	public static void setIndexIdentifior(final Function<Object, SqlIdentifier> indexIdentifior)
	{
		SqlName.indexIdentifior = coalesce(indexIdentifior, DEFAULT_IDENTIFIOR);
	}

	public static void setConstraintIdentifior(final Function<Object, SqlIdentifier> constraintIdentifior)
	{
		SqlName.constraintIdentifior = coalesce(constraintIdentifior, DEFAULT_IDENTIFIOR);
	}

	public static void setTriggerIdentifior(final Function<Object, SqlIdentifier> triggerIdentifior)
	{
		SqlName.triggerIdentifior = coalesce(triggerIdentifior, DEFAULT_IDENTIFIOR);
	}

	public static void setAliasIdentifior(final Function<Object, SqlIdentifier> aliasIdentifior)
	{
		SqlName.aliasIdentifior = coalesce(aliasIdentifior, DEFAULT_IDENTIFIOR_UNDELIMITING);
	}

	public static void setViewIdentifior(final Function<Object, SqlIdentifier> viewIdentifior)
	{
		SqlName.viewIdentifior = coalesce(viewIdentifior, DEFAULT_IDENTIFIOR);
	}

	public static void setProcedureIdentifior(final Function<Object, SqlIdentifier> procedureIdentifior)
	{
		SqlName.routineIdentifior = coalesce(procedureIdentifior, DEFAULT_IDENTIFIOR);
	}




	///////////////////////////////////////////////////////////////////////////
	// declared methods //
	/////////////////////

	public static SqlIdentifier catalogName(final Object identifier)
	{
		return SqlName.catalogIdentifior.apply(identifier);
	}
	public static SqlIdentifier schemaName(final Object identifier)
	{
		return SqlName.schemaIdentifior.apply(identifier);
	}
	public static SqlIdentifier tableName(final Object identifier)
	{
		return SqlName.tableIdentifior.apply(identifier);
	}
	public static SqlIdentifier columnName(final Object identifier)
	{
		return SqlName.columnIdentifior.apply(identifier);
	}
	public static SqlIdentifier indexName(final Object identifier)
	{
		return SqlName.indexIdentifior.apply(identifier);
	}
	public static SqlIdentifier constraintName(final Object identifier)
	{
		return SqlName.constraintIdentifior.apply(identifier);
	}
	public static SqlIdentifier triggerName(final Object identifier)
	{
		return SqlName.triggerIdentifior.apply(identifier);
	}
	public static SqlIdentifier aliasName(final Object identifier)
	{
		return SqlName.aliasIdentifior.apply(identifier);
	}
	public static SqlIdentifier viewName(final Object identifier)
	{
		return SqlName.viewIdentifior.apply(identifier);
	}
	public static SqlIdentifier routineName(final Object identifier)
	{
		return SqlName.routineIdentifior.apply(identifier);
	}








	public static final String deriveTableName(final Class<? extends SqlTable> tableClass)
	{
		return SqlName.tableNameDeriver.apply(tableClass);
	}

	public static final String deriveViewName(final Class<? extends SqlTable> tableClass)
	{
		return SqlName.viewNameDeriver.apply(tableClass);
	}

	public static final String deriveTableColumnName(final BaseTableColumn.InitItem columnItem)
	{
		return tableColumnNameDeriver.apply(columnItem);
	}

	public static final String deriveTableMemberName(final BaseTableMember.InitItem memberItem)
	{
		return SqlName.tableMemberNameDeriver.apply(memberItem);
	}

	// don't know what difference this makes compared to Class<? extends SqlSchem<?>>, but this way it works
	public static final <S extends SqlSchema<?>> String deriveSchemaName(final Class<S> schemaClass)
	{
		return SqlName.schemaNameDeriver.apply(schemaClass);
	}

	public static final String deriveAlias(final String tablename)
	{
		return SqlName.aliasDeriver.apply(tablename);
	}

	public static SqlIdentifier genericColumnName(final SqlQuery<?> query, final int index)
	{
		return SqlName.genericColumnNameMapper.apply(query, index);
	}

	public static boolean equal(final SqlIdentifier identifier1, final SqlIdentifier identifier2)
	{
		return SqlName.identifierEquality.equal(identifier1, identifier2);
	}

	public static boolean equal(final SqlNamed named1, final SqlNamed named2)
	{
		return equal(get(named1), get(named2));
	}

	public static boolean equal(final SqlSchemaMember named1, final SqlSchemaMember named2)
	{
		return equal(named1.sqlSchema(), named2.sqlSchema()) && equal(named1.sqlIdentifier(), named2.sqlIdentifier());
	}


	public static Function<String, String> getAliasDeriver()
	{
		return SqlName.aliasDeriver;
	}

	public static void setAliasDeriver(final Function<String, String> aliasDeriver)
	{
		SqlName.aliasDeriver = coalesce(aliasDeriver, DEFAULT_ALIAS_DERIVER);
	}

	public static Function<Class<? extends SqlTable>, String> getTableNameDeriver()
	{
		return SqlName.tableNameDeriver;
	}

	public static Function<Class<? extends SqlTable>, String> getViewNameDeriver()
	{
		return SqlName.viewNameDeriver;
	}

	public static void setTableNameDeriver(final Function<Class<? extends SqlTable>, String> tableNameDeriver)
	{
		SqlName.tableNameDeriver = coalesce(tableNameDeriver, DEFAULT_TABLENAME_DERIVER);
	}

	public static void setViewNameDeriver(final Function<Class<? extends SqlTable>, String> tableNameDeriver)
	{
		SqlName.viewNameDeriver = coalesce(tableNameDeriver, DEFAULT_VIEWNAME_DERIVER);
	}

	public static Function<BaseTableMember.InitItem, String> getTableMemberNameDeriver()
	{
		return SqlName.tableMemberNameDeriver;
	}

	public static void setTableMemberNameDeriver(final Function<BaseTableMember.InitItem, String> tableMemberNameDeriver)
	{
		SqlName.tableMemberNameDeriver = coalesce(tableMemberNameDeriver, DEFAULT_TABLEMEMBERNAME_DERIVER);
	}

	public static Function<Class<? extends SqlSchema<?>>, String> getSchemaNameDeriver()
	{
		return SqlName.schemaNameDeriver;
	}

	public static void setSchemaNameDeriver(final Function<Class<? extends SqlSchema<?>>, String> schemaNameDeriver)
	{
		SqlName.schemaNameDeriver = coalesce(schemaNameDeriver, DEFAULT_SCHEMANAME_DERIVER);
	}



	public static Equalator<SqlIdentifier> getIdentifierEquality()
	{
		return SqlName.identifierEquality;
	}

	public static void setIdentifierMapper(final Equalator<SqlIdentifier> identifierEquality)
	{
		SqlName.identifierEquality = identifierEquality != null ?identifierEquality :DEFAULT_IDENTIFIER_EQUALITY;
	}


	public static IndexFunction<SqlQuery<?>, SqlIdentifier> getGenericColumnNameMapper()
	{
		return SqlName.genericColumnNameMapper;
	}

	public static void setGenericColumnNameMapper(final IndexFunction<SqlQuery<?>, SqlIdentifier> mapper)
	{
		SqlName.genericColumnNameMapper = coalesce(mapper, DEFAULT_GENERIC_COLNAME_MAPPER);
	}

	public static SqlIdentifier get(final SqlNamed named)
	{
		return named == null ?null :named.sqlIdentifier();
	}

	public static String getName(final SqlIdentifier identifier)
	{
		return identifier == null ?null :identifier.sqlName();
	}

	public static final String deriveAlias(String tablename, final int minChars, final int maxChars)
	{
		// (11.10.2011)NOTE: has to be improved, not very smart right now. Or maybe it's just the default min/max vals.
		tablename = tablename.replace(" ", "");
		if(tablename.length() <= minChars) {
			return tablename.toUpperCase();
		}
		final char[] chars = tablename.toCharArray();
		char loopChar = tablename.charAt(0);
		boolean currentUpper = Character.isUpperCase(loopChar);
		final VarChar reducedName = new VarChar(chars.length).add(loopChar);

		for(int i = 1; i < chars.length; i++) {
			if(Character.isUpperCase(loopChar = chars[i]) != currentUpper) {
				reducedName.add(loopChar);
				currentUpper = !currentUpper;
			}
		}
		if(reducedName.length() < minChars) {
			return tablename.substring(0, min((minChars+maxChars)/2 + 1, maxChars)).toUpperCase();
		}

		String returnCandidate = reducedName.toString();
		if(returnCandidate.length() > maxChars) {
			returnCandidate = reducedName.replaceAll("[a-z]", "");
		}
		if(returnCandidate.length() > maxChars) {
			returnCandidate = reducedName.replaceAll("[^a-zA-Z]", "");
		}
		if(returnCandidate.length() > maxChars) {
			returnCandidate = reducedName.replaceAll("[^A-Z]", "");
		}
		if(returnCandidate.length() > maxChars) {
			returnCandidate = returnCandidate.replaceAll("[aeiouAEIOU]", "");
			returnCandidate = returnCandidate.substring(0, min(returnCandidate.length(), maxChars));
		}
		return returnCandidate.toUpperCase();
	}

}

