package org.dyndns.doujindb.db;

import org.dyndns.doujindb.db.query.QueryArtist;
import org.dyndns.doujindb.db.query.QueryBook;
import org.dyndns.doujindb.db.query.QueryCircle;
import org.dyndns.doujindb.db.query.QueryContent;
import org.dyndns.doujindb.db.query.QueryConvention;
import org.dyndns.doujindb.db.query.QueryParody;
import org.dyndns.doujindb.db.records.Artist;
import org.dyndns.doujindb.db.records.Book;
import org.dyndns.doujindb.db.records.Circle;
import org.dyndns.doujindb.db.records.Content;
import org.dyndns.doujindb.db.records.Convention;
import org.dyndns.doujindb.db.records.Parody;

/**  
* DataBaseContext.java - DoujinDB database context.
* @author  nozomu
* @version 1.0
*/
public interface DataBaseContext {

    public void doCommit() throws DataBaseException;

    public void doRollback() throws DataBaseException;

    public RecordSet<Book> getBooks(QueryBook query) throws DataBaseException;

    public RecordSet<Circle> getCircles(QueryCircle query) throws DataBaseException;

    public RecordSet<Artist> getArtists(QueryArtist query) throws DataBaseException;

    public RecordSet<Parody> getParodies(QueryParody query) throws DataBaseException;

    public RecordSet<Content> getContents(QueryContent query) throws DataBaseException;

    public RecordSet<Convention> getConventions(QueryConvention query) throws DataBaseException;

    public RecordSet<Record> getRecycled() throws DataBaseException;

    public RecordSet<Record> getDeleted() throws DataBaseException;

    public RecordSet<Record> getModified() throws DataBaseException;

    public RecordSet<Record> getUncommitted() throws DataBaseException;

    public <T> T doInsert(Class<? extends Record> clazz) throws DataBaseException;

    public void doDelete(Record record) throws DataBaseException;
}
