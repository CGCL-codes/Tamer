package edu.berkeley.cs.db.yfilter.querymodule;

import java.util.*;
import java.io.PrintWriter;
import zfilter.QueryIdEntry;

/**
 * this class is the object we store in the hash table
 * each entry can point to multiple accept states as 
 * well as the next state.
 **/
public class HashEntryBasic {

    int m_nextHtId;

    public ArrayList m_accepts;

    public int m_acceptingStateId = -1;

    public int m_matchedDocID = 0;

    int rootId;

    QueryIdEntry queryIdEntry;

    public HashEntryBasic(int nextHtId) {
        m_nextHtId = nextHtId;
    }

    public HashEntryBasic(int nextHtId, int acceptingStateId) {
        m_nextHtId = nextHtId;
        m_acceptingStateId = acceptingStateId;
    }

    /**
	 * this method gets the nextState which is
	 * an integer > 0.
	 **/
    public int getNextHtId() {
        return m_nextHtId;
    }

    public void setNextHtId(int next_htId) {
        m_nextHtId = next_htId;
    }

    public int getAcceptingStateId() {
        return m_acceptingStateId;
    }

    public void setAcceptingStateId(int id) {
        m_acceptingStateId = id;
    }

    public void addAccept(int queryId, int pathId) {
        if (m_accepts == null) m_accepts = new ArrayList(50);
        m_accepts.add(new IDPair(queryId, pathId));
    }

    public void removeAccept(int queryId, int pathId) {
        if (m_accepts == null) {
            System.out.println("Error! A query to be deleted doesnot exist in the indexed list.");
            return;
        }
        int index = m_accepts.indexOf(new IDPair(queryId, pathId));
        if (index != -1) m_accepts.remove(index); else System.out.println("Error! A query to be deleted doesnot exist in the indexed list.");
    }

    /**
	 * this method gets the set of acceptStates
	 **/
    public ArrayList getAccepts() {
        return m_accepts;
    }

    public int getAcceptsSize() {
        if (m_accepts == null) return 0;
        return m_accepts.size();
    }

    public boolean containsAccept() {
        if (m_accepts == null) return false;
        return (!m_accepts.isEmpty());
    }

    public boolean documentSeen(int docID) {
        if (m_matchedDocID < docID) {
            m_matchedDocID = docID;
            return false;
        } else return true;
    }

    public void clearState_DocID() {
        m_matchedDocID = 0;
    }

    /**
	 * this method converts the hash entry
	 * into a human readable string.
	 */
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("accepts: ");
        if (m_accepts != null) {
            int size = m_accepts.size();
            for (int i = 0; i < size; i++) sb.append(((IDPair) m_accepts.get(i)).toString());
        }
        sb.append("\n");
        sb.append("next ht: ");
        sb.append(m_nextHtId);
        sb.append("\n");
        return sb.toString();
    }

    public void print() {
        System.out.println("--accepts --");
        if (m_accepts != null) {
            int size = m_accepts.size();
            for (int i = 0; i < size; i++) ((IDPair) m_accepts.get(i)).print();
            if (m_accepts.size() > 0) System.out.println();
        }
        System.out.println("--next ht: " + m_nextHtId);
    }

    public void printToFile(PrintWriter out) {
        out.println("--accepts --");
        if (m_accepts != null) {
            int size = m_accepts.size();
            for (int i = 0; i < size; i++) ((IDPair) m_accepts.get(i)).printToFile(out);
            if (m_accepts.size() > 0) out.println();
        }
        out.println("--next ht: " + m_nextHtId);
    }

    /**
	 * @return Returns the rootId.
	 */
    public int getRootId() {
        return rootId;
    }

    /**
	 * @param rootId The rootId to set.
	 */
    public void setRootId(int rootId) {
        this.rootId = rootId;
    }

    /**
	 * @return Returns the queryIdEntry.
	 */
    public QueryIdEntry getQueryIdEntry() {
        return queryIdEntry;
    }

    /**
	 * @param queryIdEntry The queryIdEntry to set.
	 */
    public void setQueryIdEntry(QueryIdEntry queryIdEntry) {
        this.queryIdEntry = queryIdEntry;
    }
}
