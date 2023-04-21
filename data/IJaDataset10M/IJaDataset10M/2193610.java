package org.gdbi.util.memdb;

import org.gdbi.api.*;

/**
 * This is the interface that UMemdbContext needs to use any database.
 */
public interface UMemdbIntrDatabase {

    /**
     * Used by FsdbFam only: create new mini-rec and cache result.
     * TODO: rename w/o create
     */
    public GdbiMiniFam createMiniFam(GdbiFam fam);

    /**
     * Used by FsdbIndi only: create new mini-rec and cache result.
     */
    public GdbiMiniIndi createMiniIndi(GdbiIndi indi);

    /**
     * Delete record.
     */
    public void deleteRecord(UMemdbContext.RecordBase rec) throws GdbiIOException;

    /**
     * Same as GdbiIntrDatabase.
     */
    public GdbiDatabase getGdbiDatabase();

    /**
     * Implements Comparable?
     */
    public boolean isComparable();

    /**
     * Generates incrementing int used by Comparable.
     * Only called if isComparable() returns true (for RawdbRecord).
     */
    public int nextXrefInt();

    /**
     * Write record.
     */
    public void toDatabase(GdbiIntrRecord rec) throws GdbiIOException;

    /**
     * Does this DB use GdbiRecord as GdbiMiniRecord (instead of creating a
     * separate mini-rec)?
     */
    public boolean useRecAsMiniRec();
}
