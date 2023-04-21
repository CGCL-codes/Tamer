package org.kablink.teaming.lucene;

import java.rmi.RemoteException;
import java.util.ArrayList;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Sort;

/**
 * Title: SsfIndexInterface Description: This is the interface definition to be
 * implemented by the server. Copyright: Copyright (c) 2005 Company: SiteScape,
 * Inc.
 * 
 * @author Roy Klein
 * @version 1.0
 */
public interface SsfIndexInterface extends java.rmi.Remote {

    public void addDocuments(String indexname, ArrayList documents) throws RemoteException;

    public void deleteDocuments(String indexname, Term term) throws RemoteException;

    public void addDeleteDocuments(String indexname, ArrayList docsToAddOrDelete) throws RemoteException;

    public void commit(String indexname) throws RemoteException;

    public void optimize(String indexname) throws RemoteException;

    public org.kablink.teaming.lucene.Hits search(String indexname, Query query) throws RemoteException;

    public org.kablink.teaming.lucene.Hits search(String indexname, Query query, int offset, int size) throws RemoteException;

    public org.kablink.teaming.lucene.Hits search(String indexname, Query query, Sort sort) throws RemoteException;

    public org.kablink.teaming.lucene.Hits search(String indexname, Query query, Sort sort, int offset, int size) throws RemoteException;

    public ArrayList getTags(String indexName, Query query, Long id, String tag, String type, boolean isSuper) throws RemoteException;

    public ArrayList getTagsWithFrequency(String indexName, Query query, Long id, String tag, String type, boolean isSuper) throws RemoteException;

    public String[] getSortedTitles(String indexName, Query query, String sortTitleFieldName, String start, String end, int skipsize) throws RemoteException;

    public void clearIndex(String indexname) throws RemoteException;

    public void shutdown() throws RemoteException;
}
