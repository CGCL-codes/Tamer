package info.mp3lib.util.cddb.FreeDB;

import info.mp3lib.util.cddb.DBResult;
import info.mp3lib.util.cddb.ITagQueryResult;
import entagged.freedb.FreedbReadResult;

public class FreeDBResult extends DBResult implements ITagQueryResult {

    FreedbReadResult readResult;

    public FreeDBResult(FreedbReadResult dbReadResult) {
        readResult = dbReadResult;
    }

    public FreeDBResult(String freedbReadResult, boolean exactMatch) {
        super();
        readResult = new FreedbReadResult(freedbReadResult, exactMatch);
    }

    public FreeDBResult(String freedbReadResult, String genre) {
        super();
        readResult = new FreedbReadResult(freedbReadResult, genre);
    }

    @Override
    public int compareTo(Object o) {
        return readResult.compareTo(o);
    }

    /**
     * @param obj
     * @return
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object obj) {
        return readResult.equals(obj);
    }

    /**
     * @return
     * @see entagged.freedb.FreedbReadResult#getAlbum()
     */
    public String getAlbum() {
        return readResult.getAlbum();
    }

    /**
     * @return
     * @see entagged.freedb.FreedbReadResult#getAlbumComment()
     */
    public String getAlbumComment() {
        return readResult.getAlbumComment();
    }

    /**
     * @return
     * @see entagged.freedb.FreedbReadResult#getArtist()
     */
    public String getArtist() {
        return readResult.getArtist();
    }

    /**
     * @return
     * @see entagged.freedb.FreedbReadResult#getCategory()
     */
    public String getCategory() {
        return readResult.getCategory();
    }

    /**
     * @return
     * @see entagged.freedb.FreedbReadResult#getDiscId()
     */
    public String getDiscId() {
        return readResult.getDiscId();
    }

    /**
     * @return
     * @see entagged.freedb.FreedbReadResult#getGenre()
     */
    public String getGenre() {
        return readResult.getGenre();
    }

    /**
     * @return
     * @see entagged.freedb.FreedbReadResult#getQuality()
     */
    public int getQuality() {
        return readResult.getQuality();
    }

    /**
     * @param i
     * @return
     * @see entagged.freedb.FreedbReadResult#getTrackComment(int)
     */
    public String getTrackComment(int i) {
        return readResult.getTrackComment(i);
    }

    /**
     * @param i
     * @return
     * @see entagged.freedb.FreedbReadResult#getTrackDuration(int)
     */
    public int getTrackDuration(int i) {
        return readResult.getTrackDuration(i);
    }

    /**
     * @param i
     * @return
     * @see entagged.freedb.FreedbReadResult#getTrackNumber(int)
     */
    public int getTrackNumber(int i) {
        return readResult.getTrackNumber(i);
    }

    /**
     * @return
     * @see entagged.freedb.FreedbReadResult#getTracksNumber()
     */
    public int getTracksNumber() {
        return readResult.getTracksNumber();
    }

    /**
     * @param i
     * @return
     * @see entagged.freedb.FreedbReadResult#getTrackTitle(int)
     */
    public String getTrackTitle(int i) {
        return readResult.getTrackTitle(i);
    }

    /**
     * @return
     * @see entagged.freedb.FreedbReadResult#getYear()
     */
    public String getYear() {
        return readResult.getYear();
    }

    /**
     * @return
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        return readResult.hashCode();
    }

    /**
     * @return
     * @see entagged.freedb.FreedbReadResult#isExactMatch()
     */
    public boolean isExactMatch() {
        return readResult.isExactMatch();
    }

    /**
     * @param i1
     * @param i2
     * @see entagged.freedb.FreedbReadResult#swapTracks(int, int)
     */
    public void swapTracks(int i1, int i2) {
        readResult.swapTracks(i1, i2);
    }

    /**
     * @return
     * @see entagged.freedb.FreedbReadResult#toString()
     */
    public String toString() {
        return readResult.toString();
    }
}
