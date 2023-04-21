package org.dyndns.doujindb.db.impl;

import java.io.*;
import java.util.*;
import org.dyndns.doujindb.db.*;
import org.dyndns.doujindb.db.records.*;

final class ArtistImpl extends RecordImpl implements Artist, Serializable {

    private static final long serialVersionUID = 0xFEED0001L;

    public ArtistImpl(org.dyndns.doujindb.db.cayenne.Artist ref) throws DataBaseException {
        this.ref = ref;
    }

    @Override
    public synchronized String getJapaneseName() throws DataBaseException {
        return ((org.dyndns.doujindb.db.cayenne.Artist) ref).getJapaneseName();
    }

    @Override
    public synchronized void setJapaneseName(String japaneseName) throws DataBaseException {
        ((org.dyndns.doujindb.db.cayenne.Artist) ref).setJapaneseName(japaneseName);
    }

    @Override
    public synchronized String getTranslatedName() throws DataBaseException {
        return ((org.dyndns.doujindb.db.cayenne.Artist) ref).getTranslatedName();
    }

    @Override
    public synchronized void setTranslatedName(String translatedName) throws DataBaseException {
        ((org.dyndns.doujindb.db.cayenne.Artist) ref).setTranslatedName(translatedName);
    }

    @Override
    public synchronized String getRomanjiName() throws DataBaseException {
        return ((org.dyndns.doujindb.db.cayenne.Artist) ref).getRomanjiName();
    }

    @Override
    public synchronized void setRomanjiName(String romanjiName) throws DataBaseException {
        ((org.dyndns.doujindb.db.cayenne.Artist) ref).setRomanjiName(romanjiName);
    }

    @Override
    public synchronized String getWeblink() throws DataBaseException {
        return ((org.dyndns.doujindb.db.cayenne.Artist) ref).getWeblink();
    }

    @Override
    public synchronized void setWeblink(String weblink) throws DataBaseException {
        ((org.dyndns.doujindb.db.cayenne.Artist) ref).setWeblink(weblink);
    }

    @Override
    public synchronized RecordSet<Book> getBooks() throws DataBaseException {
        Set<Book> set = new TreeSet<Book>();
        Set<org.dyndns.doujindb.db.cayenne.Book> result = ((org.dyndns.doujindb.db.cayenne.Artist) ref).getBooks();
        for (org.dyndns.doujindb.db.cayenne.Book r : result) if (!r.getRecycled()) set.add(new BookImpl(r));
        return new RecordSetImpl<Book>(set);
    }

    @Override
    public synchronized RecordSet<Circle> getCircles() throws DataBaseException {
        Set<Circle> set = new TreeSet<Circle>();
        Set<org.dyndns.doujindb.db.cayenne.Circle> result = ((org.dyndns.doujindb.db.cayenne.Artist) ref).getCircles();
        for (org.dyndns.doujindb.db.cayenne.Circle r : result) if (!r.getRecycled()) set.add(new CircleImpl(r));
        return new RecordSetImpl<Circle>(set);
    }

    @Override
    public synchronized String toString() {
        return this.getJapaneseName();
    }

    @Override
    public void addBook(Book book) throws DataBaseException {
        if (getBooks().contains(book)) return;
        ((org.dyndns.doujindb.db.cayenne.Artist) ref).addToBooks((org.dyndns.doujindb.db.cayenne.Book) ((org.dyndns.doujindb.db.impl.BookImpl) book).ref);
    }

    @Override
    public void addCircle(Circle circle) throws DataBaseException {
        if (getCircles().contains(circle)) return;
        ((org.dyndns.doujindb.db.cayenne.Artist) ref).addToCircles((org.dyndns.doujindb.db.cayenne.Circle) ((org.dyndns.doujindb.db.impl.CircleImpl) circle).ref);
    }

    @Override
    public void removeBook(Book book) throws DataBaseException {
        ((org.dyndns.doujindb.db.cayenne.Artist) ref).removeFromBooks((org.dyndns.doujindb.db.cayenne.Book) ((org.dyndns.doujindb.db.impl.BookImpl) book).ref);
    }

    @Override
    public void removeCircle(Circle circle) throws DataBaseException {
        ((org.dyndns.doujindb.db.cayenne.Artist) ref).removeFromCircles((org.dyndns.doujindb.db.cayenne.Circle) ((org.dyndns.doujindb.db.impl.CircleImpl) circle).ref);
    }

    @Override
    public synchronized String getID() throws DataBaseException {
        Integer ID = ((org.dyndns.doujindb.db.cayenne.Artist) ref).getID();
        if (ID == null) return null; else return "A" + String.format("%08x", ID);
    }

    @Override
    public void doRecycle() throws DataBaseException {
        ((org.dyndns.doujindb.db.cayenne.Artist) ref).setRecycled(true);
    }

    @Override
    public void doRestore() throws DataBaseException {
        ((org.dyndns.doujindb.db.cayenne.Artist) ref).setRecycled(false);
    }

    @Override
    public boolean isRecycled() throws DataBaseException {
        return ((org.dyndns.doujindb.db.cayenne.Artist) ref).getRecycled();
    }

    @Override
    public void removeAll() throws DataBaseException {
        {
            Set<org.dyndns.doujindb.db.cayenne.Book> result = ((org.dyndns.doujindb.db.cayenne.Artist) ref).getBooks();
            for (org.dyndns.doujindb.db.cayenne.Book book : result) ((org.dyndns.doujindb.db.cayenne.Artist) ref).removeFromBooks(book);
        }
        {
            Set<org.dyndns.doujindb.db.cayenne.Circle> result = ((org.dyndns.doujindb.db.cayenne.Artist) ref).getCircles();
            for (org.dyndns.doujindb.db.cayenne.Circle circle : result) ((org.dyndns.doujindb.db.cayenne.Artist) ref).removeFromCircles(circle);
        }
    }
}
