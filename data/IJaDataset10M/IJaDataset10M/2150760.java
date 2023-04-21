package org.dyndns.doujindb.db.impl;

import java.util.*;
import org.apache.cayenne.ObjectContext;
import org.apache.cayenne.exp.Expression;
import org.apache.cayenne.query.SelectQuery;
import org.dyndns.doujindb.db.*;
import org.dyndns.doujindb.db.query.*;
import org.dyndns.doujindb.db.records.*;

public final class DataBaseContextImpl implements DataBaseContext {

    private ObjectContext context;

    public DataBaseContextImpl(ObjectContext context) throws DataBaseException {
        this.context = context;
    }

    private synchronized Artist newArtist() throws DataBaseException {
        org.dyndns.doujindb.db.cayenne.Artist o = context.newObject(org.dyndns.doujindb.db.cayenne.Artist.class);
        return new ArtistImpl(o);
    }

    private synchronized Book newBook() throws DataBaseException {
        org.dyndns.doujindb.db.cayenne.Book o = context.newObject(org.dyndns.doujindb.db.cayenne.Book.class);
        return new BookImpl(o);
    }

    private synchronized Circle newCircle() throws DataBaseException {
        org.dyndns.doujindb.db.cayenne.Circle o = context.newObject(org.dyndns.doujindb.db.cayenne.Circle.class);
        return new CircleImpl(o);
    }

    private synchronized Content newContent() throws DataBaseException {
        org.dyndns.doujindb.db.cayenne.Content o = context.newObject(org.dyndns.doujindb.db.cayenne.Content.class);
        return new ContentImpl(o);
    }

    private synchronized Convention newConvention() throws DataBaseException {
        org.dyndns.doujindb.db.cayenne.Convention o = context.newObject(org.dyndns.doujindb.db.cayenne.Convention.class);
        return new ConventionImpl(o);
    }

    private synchronized Parody newParody() throws DataBaseException {
        org.dyndns.doujindb.db.cayenne.Parody o = context.newObject(org.dyndns.doujindb.db.cayenne.Parody.class);
        return new ParodyImpl(o);
    }

    @Override
    public synchronized void doCommit() throws DataBaseException {
        context.commitChanges();
    }

    @Override
    public synchronized void doRollback() throws DataBaseException {
        context.rollbackChanges();
    }

    @Override
    public synchronized void doDelete(Record record) throws DataBaseException {
        context.deleteObject(((RecordImpl) record).ref);
    }

    @SuppressWarnings("unchecked")
    @Override
    public RecordSet<Book> getBooks(QueryBook query) throws DataBaseException {
        SelectQuery select = new SelectQuery(org.dyndns.doujindb.db.cayenne.Book.class, Expression.fromString("recycled = FALSE"));
        List<org.dyndns.doujindb.db.cayenne.Book> list = context.performQuery(select);
        Set<Book> buff = new TreeSet<Book>();
        for (org.dyndns.doujindb.db.cayenne.Book o : list) buff.add(new BookImpl(o));
        return new RecordSetImpl<Book>(buff);
    }

    @SuppressWarnings("unchecked")
    @Override
    public RecordSet<Circle> getCircles(QueryCircle query) throws DataBaseException {
        SelectQuery select = new SelectQuery(org.dyndns.doujindb.db.cayenne.Circle.class, Expression.fromString("recycled = FALSE"));
        List<org.dyndns.doujindb.db.cayenne.Circle> list = context.performQuery(select);
        Set<Circle> buff = new TreeSet<Circle>();
        for (org.dyndns.doujindb.db.cayenne.Circle o : list) buff.add(new CircleImpl(o));
        return new RecordSetImpl<Circle>(buff);
    }

    @SuppressWarnings("unchecked")
    @Override
    public RecordSet<Artist> getArtists(QueryArtist query) throws DataBaseException {
        SelectQuery select = new SelectQuery(org.dyndns.doujindb.db.cayenne.Artist.class, Expression.fromString("recycled = FALSE"));
        List<org.dyndns.doujindb.db.cayenne.Artist> list = context.performQuery(select);
        Set<Artist> buff = new TreeSet<Artist>();
        for (org.dyndns.doujindb.db.cayenne.Artist o : list) buff.add(new ArtistImpl(o));
        return new RecordSetImpl<Artist>(buff);
    }

    @SuppressWarnings("unchecked")
    @Override
    public RecordSet<Parody> getParodies(QueryParody query) throws DataBaseException {
        SelectQuery select = new SelectQuery(org.dyndns.doujindb.db.cayenne.Parody.class, Expression.fromString("recycled = FALSE"));
        List<org.dyndns.doujindb.db.cayenne.Parody> list = context.performQuery(select);
        Set<Parody> buff = new TreeSet<Parody>();
        for (org.dyndns.doujindb.db.cayenne.Parody o : list) buff.add(new ParodyImpl(o));
        return new RecordSetImpl<Parody>(buff);
    }

    @SuppressWarnings("unchecked")
    @Override
    public RecordSet<Content> getContents(QueryContent query) throws DataBaseException {
        SelectQuery select = new SelectQuery(org.dyndns.doujindb.db.cayenne.Content.class, Expression.fromString("recycled = FALSE"));
        List<org.dyndns.doujindb.db.cayenne.Content> list = context.performQuery(select);
        Set<Content> buff = new TreeSet<Content>();
        for (org.dyndns.doujindb.db.cayenne.Content o : list) buff.add(new ContentImpl(o));
        return new RecordSetImpl<Content>(buff);
    }

    @SuppressWarnings("unchecked")
    @Override
    public RecordSet<Convention> getConventions(QueryConvention query) throws DataBaseException {
        SelectQuery select = new SelectQuery(org.dyndns.doujindb.db.cayenne.Convention.class, Expression.fromString("recycled = FALSE"));
        List<org.dyndns.doujindb.db.cayenne.Convention> list = context.performQuery(select);
        Set<Convention> buff = new TreeSet<Convention>();
        for (org.dyndns.doujindb.db.cayenne.Convention o : list) buff.add(new ConventionImpl(o));
        return new RecordSetImpl<Convention>(buff);
    }

    @SuppressWarnings("unchecked")
    @Override
    public RecordSet<Record> getRecycled() throws DataBaseException {
        Set<Record> buff = new TreeSet<Record>();
        SelectQuery select;
        select = new SelectQuery(org.dyndns.doujindb.db.cayenne.Artist.class, Expression.fromString("recycled = TRUE"));
        List<org.dyndns.doujindb.db.cayenne.Artist> artist_list = context.performQuery(select);
        for (org.dyndns.doujindb.db.cayenne.Artist o : artist_list) buff.add(new ArtistImpl(o));
        select = new SelectQuery(org.dyndns.doujindb.db.cayenne.Book.class, Expression.fromString("recycled = TRUE"));
        List<org.dyndns.doujindb.db.cayenne.Book> book_list = context.performQuery(select);
        for (org.dyndns.doujindb.db.cayenne.Book o : book_list) buff.add(new BookImpl(o));
        select = new SelectQuery(org.dyndns.doujindb.db.cayenne.Circle.class, Expression.fromString("recycled = TRUE"));
        List<org.dyndns.doujindb.db.cayenne.Circle> circle_list = context.performQuery(select);
        for (org.dyndns.doujindb.db.cayenne.Circle o : circle_list) buff.add(new CircleImpl(o));
        select = new SelectQuery(org.dyndns.doujindb.db.cayenne.Content.class, Expression.fromString("recycled = TRUE"));
        List<org.dyndns.doujindb.db.cayenne.Content> content_list = context.performQuery(select);
        for (org.dyndns.doujindb.db.cayenne.Content o : content_list) buff.add(new ContentImpl(o));
        select = new SelectQuery(org.dyndns.doujindb.db.cayenne.Convention.class, Expression.fromString("recycled = TRUE"));
        List<org.dyndns.doujindb.db.cayenne.Convention> conventiony_list = context.performQuery(select);
        for (org.dyndns.doujindb.db.cayenne.Convention o : conventiony_list) buff.add(new ConventionImpl(o));
        select = new SelectQuery(org.dyndns.doujindb.db.cayenne.Parody.class, Expression.fromString("recycled = TRUE"));
        List<org.dyndns.doujindb.db.cayenne.Parody> parody_list = context.performQuery(select);
        for (org.dyndns.doujindb.db.cayenne.Parody o : parody_list) buff.add(new ParodyImpl(o));
        return new RecordSetImpl<Record>(buff);
    }

    @Override
    public RecordSet<Record> getDeleted() throws DataBaseException {
        Collection<?> deleted0 = context.deletedObjects();
        Set<Record> deleted1 = new TreeSet<Record>();
        for (Object o : deleted0) {
            if (o instanceof org.dyndns.doujindb.db.cayenne.Artist) deleted1.add(new ArtistImpl((org.dyndns.doujindb.db.cayenne.Artist) o));
            if (o instanceof org.dyndns.doujindb.db.cayenne.Circle) deleted1.add(new CircleImpl((org.dyndns.doujindb.db.cayenne.Circle) o));
            if (o instanceof org.dyndns.doujindb.db.cayenne.Book) deleted1.add(new BookImpl((org.dyndns.doujindb.db.cayenne.Book) o));
            if (o instanceof org.dyndns.doujindb.db.cayenne.Content) deleted1.add(new ContentImpl((org.dyndns.doujindb.db.cayenne.Content) o));
            if (o instanceof org.dyndns.doujindb.db.cayenne.Convention) deleted1.add(new ConventionImpl((org.dyndns.doujindb.db.cayenne.Convention) o));
            if (o instanceof org.dyndns.doujindb.db.cayenne.Parody) deleted1.add(new ParodyImpl((org.dyndns.doujindb.db.cayenne.Parody) o));
        }
        return new RecordSetImpl<Record>(deleted1);
    }

    @Override
    public RecordSet<Record> getModified() throws DataBaseException {
        Collection<?> modified0 = context.modifiedObjects();
        Set<Record> modified1 = new TreeSet<Record>();
        for (Object o : modified0) {
            if (o instanceof org.dyndns.doujindb.db.cayenne.Artist) modified1.add(new ArtistImpl((org.dyndns.doujindb.db.cayenne.Artist) o));
            if (o instanceof org.dyndns.doujindb.db.cayenne.Circle) modified1.add(new CircleImpl((org.dyndns.doujindb.db.cayenne.Circle) o));
            if (o instanceof org.dyndns.doujindb.db.cayenne.Book) modified1.add(new BookImpl((org.dyndns.doujindb.db.cayenne.Book) o));
            if (o instanceof org.dyndns.doujindb.db.cayenne.Content) modified1.add(new ContentImpl((org.dyndns.doujindb.db.cayenne.Content) o));
            if (o instanceof org.dyndns.doujindb.db.cayenne.Convention) modified1.add(new ConventionImpl((org.dyndns.doujindb.db.cayenne.Convention) o));
            if (o instanceof org.dyndns.doujindb.db.cayenne.Parody) modified1.add(new ParodyImpl((org.dyndns.doujindb.db.cayenne.Parody) o));
        }
        return new RecordSetImpl<Record>(modified1);
    }

    @Override
    public RecordSet<Record> getUncommitted() throws DataBaseException {
        Collection<?> uncommitted0 = context.uncommittedObjects();
        Set<Record> uncommitted1 = new TreeSet<Record>();
        for (Object o : uncommitted0) {
            if (o instanceof org.dyndns.doujindb.db.cayenne.Artist) uncommitted1.add(new ArtistImpl((org.dyndns.doujindb.db.cayenne.Artist) o));
            if (o instanceof org.dyndns.doujindb.db.cayenne.Circle) uncommitted1.add(new CircleImpl((org.dyndns.doujindb.db.cayenne.Circle) o));
            if (o instanceof org.dyndns.doujindb.db.cayenne.Book) uncommitted1.add(new BookImpl((org.dyndns.doujindb.db.cayenne.Book) o));
            if (o instanceof org.dyndns.doujindb.db.cayenne.Content) uncommitted1.add(new ContentImpl((org.dyndns.doujindb.db.cayenne.Content) o));
            if (o instanceof org.dyndns.doujindb.db.cayenne.Convention) uncommitted1.add(new ConventionImpl((org.dyndns.doujindb.db.cayenne.Convention) o));
            if (o instanceof org.dyndns.doujindb.db.cayenne.Parody) uncommitted1.add(new ParodyImpl((org.dyndns.doujindb.db.cayenne.Parody) o));
        }
        return new RecordSetImpl<Record>(uncommitted1);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T doInsert(Class<? extends Record> clazz) throws DataBaseException {
        if (clazz == Artist.class) return (T) newArtist();
        if (clazz == Book.class) return (T) newBook();
        if (clazz == Circle.class) return (T) newCircle();
        if (clazz == Content.class) return (T) newContent();
        if (clazz == Convention.class) return (T) newConvention();
        if (clazz == Parody.class) return (T) newParody();
        throw new DataBaseException("Invalid record class '" + clazz + "' specified.");
    }
}
