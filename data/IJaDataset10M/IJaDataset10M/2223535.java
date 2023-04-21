package net.jadoth.sqlengine.dbms.h2;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import net.jadoth.sqlengine.dbms.DbmsLogic;
import net.jadoth.sqlengine.sql.types.CallResultRetriever;
import net.jadoth.sqlengine.sql.types.SqlContext;

public class H2DbmsLogic extends DbmsLogic.Implementation<H2DbmsAdaptor>
{
	///////////////////////////////////////////////////////////////////////////
	// constants        //
	/////////////////////

	/* (09.11.2011)NOTE: Workaround for H2 buggy behaviour. See H2 roadmap entry:
	 * CALL is incompatible with other databases because it returns a result set, so that
	 * CallableStatement.execute() returns true.
	 */
	private static final CallResultRetriever<Object> CALL_RESULT_RETRIEVER_VALUE = new CallResultRetriever<Object>(){
		@Override
		public Object retrieveResult(
			final SqlContext<?> context,
			final CallableStatement jdbcStatement,
			final Class<Object> type,
			final int index
		)
			throws SQLException
		{
			// (10.11.2011)NOTE: close statement here in any case because outer
			try(ResultSet rs = jdbcStatement.getResultSet(); CallableStatement stmt = jdbcStatement){
				if(rs.next()){
					return rs.getObject(index);
				}
				return null;
			}
		}
	};

	private static final CallResultRetriever<ResultSet> CALL_RESULT_RETRIEVER_RS = new CallResultRetriever<ResultSet>(){
		@Override
		public ResultSet retrieveResult(
			final SqlContext<?> context,
			final CallableStatement jdbcStatement,
			final Class<ResultSet> type,
			final int index
		)
			throws SQLException
		{
			return jdbcStatement.getResultSet();
		}
	};



	///////////////////////////////////////////////////////////////////////////
	// constructors     //
	/////////////////////

	protected H2DbmsLogic(final H2DbmsAdaptor dbms)
	{
		super(dbms);
	}



	///////////////////////////////////////////////////////////////////////////
	// override methods //
	/////////////////////

	@SuppressWarnings("unchecked")
	@Override
	public <R> CallResultRetriever<R> getCallResultRetriever(final Class<R> type)
	{
		if(ResultSet.class.isAssignableFrom(type)){
			return (CallResultRetriever<R>)CALL_RESULT_RETRIEVER_RS;
		}
		return (CallResultRetriever<R>)CALL_RESULT_RETRIEVER_VALUE;
	}

}
