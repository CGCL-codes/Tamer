package saadadb.vo;

import java.util.ArrayList;
import saadadb.collection.SaadaInstance;
import saadadb.database.Database;
import saadadb.exceptions.FatalException;
import saadadb.exceptions.QueryException;
import saadadb.exceptions.SaadaException;
import saadadb.query.executor.Query;
import saadadb.query.result.SaadaQLResultSet;
import saadadb.util.Messenger;

/** * @version $Id: SaadaQLExecutor.java 118 2012-01-06 14:33:51Z laurent.mistahl $

 * Execute a SaadaQL/s query on a SaadaDB, and gets the result as a list of Saada OIds.
 */
public class SaadaQLExecutor {

    /** The last executed query. */
    protected Query query = null;

    public Query getQuery() {
        return query;
    }

    /**
	 *
	 * @param queryString the SaadaQL/s query
	 * @return a list of OIds
	 * @throws Exception 
	 */
    public long[] execute(String queryStr) throws Exception {
        Messenger.printMsg(Messenger.TRACE, "SaadaQL executor : Saada query received : " + queryStr);
        query = new Query();
        SaadaQLResultSet query_result = query.runQuery(queryStr);
        if (query_result == null) {
            QueryException.throwNewException(SaadaException.WRONG_PARAMETER, query.getErrorReport());
            return null;
        } else {
            long[] retour;
            if (Database.getWrapper().forwardOnly) {
                ArrayList<Long> al = new ArrayList<Long>();
                while (query_result.next()) {
                    al.add(query_result.getOid());
                }
                retour = new long[al.size()];
                for (int i = 0; i < al.size(); i++) {
                    retour[i] = al.get(i);
                }
            } else {
                retour = new long[query_result.getSize()];
                int cpt = 0;
                while (query_result.next()) {
                    retour[cpt] = query_result.getOid();
                    cpt++;
                }
            }
            return retour;
        }
    }

    /**
	 * Execute the quer and returns a resulset containing all requested columns instead of oidsaada only
	 * @param queryString the SaadaQL/s query
	 * @return a list of OIds
	 * @throws Exception 
	 */
    public SaadaQLResultSet executeInStreaming(String queryStr) throws Exception {
        Messenger.printMsg(Messenger.TRACE, "SaadaQL executor : Saada query received : " + queryStr);
        Query q = new Query();
        SaadaQLResultSet query_result = q.runQuery(queryStr, true);
        if (query_result == null) {
            QueryException.throwNewException(SaadaException.WRONG_PARAMETER, q.getErrorReport());
            return null;
        } else {
            return query_result;
        }
    }

    /**
	 * @param primoid
	 * @param rel_name
	 * @return
	 * @throws SaadaException
	 */
    public long[] getCounterparts(long primoid, String rel_name) throws SaadaException {
        if (primoid == -1 || rel_name == null) {
            Messenger.printMsg(Messenger.ERROR, "SaadaQLExecutor.getCounterparts : bad parameters ");
            FatalException.throwNewException(SaadaException.WRONG_PARAMETER, "SaadaQLExecutor.getCounterparts : bad parameters ");
        }
        try {
            SaadaInstance objSuper = (SaadaInstance) Database.getCache().getObject(primoid);
            long[] retour = objSuper.getCounterparts(rel_name);
            if (retour.length > 1000) {
                long[] subretour = new long[1000];
                Messenger.printMsg(Messenger.TRACE, "Query result truncated to 1000");
                System.arraycopy(retour, 0, subretour, 0, 1000);
                return subretour;
            }
            return retour;
        } catch (Exception e) {
            FatalException.throwNewException(SaadaException.INTERNAL_ERROR, e);
        }
        return null;
    }

    public void close() throws QueryException {
    }
}
