package de.emoo.ui.swing.table.model;

import de.emoo.database.Artist;
import de.emoo.database.Genre;
import de.emoo.database.Song;
import de.emoo.database.Vote;
import de.emoo.database.persistence.EntityFactory;
import de.emoo.database.persistence.PersistenceException;
import de.emoo.util.EmooUtils;

/**
 * <b>SongTableModel</b>:
 * 
 * @author gandalf
 * @version $Id: SongTableModel.java,v 1.6 2004/08/14 11:39:03 tbuchloh Exp $
 */
public final class SongTableModel extends EmooTableModel {

    private static final String LOC_CNT = "#Files";

    private static final String VOTE = "Vote";

    private static final String LENGTH = "Length";

    private static final String TITLE = "Title";

    private static final String ARTIST = "Artist";

    private static final String GENRE = "Genre";

    private EntityFactory _factory;

    /**
     * creates a new SongTableModel
     * @param factory is the factory which is used to create new instances of
     *        artists and votes.
     */
    public SongTableModel(EntityFactory factory) {
        super(Song.class);
        _factory = factory;
        addTableColumn(new EmooTableColumn(ARTIST, String.class, true));
        addTableColumn(new EmooTableColumn(TITLE, String.class, true));
        addTableColumn(new EmooTableColumn(LENGTH, String.class));
        addTableColumn(new EmooTableColumn(GENRE, Genre.class, true));
        addTableColumn(new EmooTableColumn(VOTE, Vote.class, true));
        addTableColumn(new EmooTableColumn(LOC_CNT, Integer.class));
    }

    /**
     * creates a new SongTableModel
     */
    public SongTableModel() {
        this(null);
    }

    /**
     * Overridden!
     * @see javax.swing.table.TableModel#getValueAt(int, int)
     */
    public Object getValueAt(int rowIndex, int columnIndex) {
        Song row = (Song) getRow(rowIndex);
        Object ret = null;
        String colName = getColumnName(columnIndex);
        if (colName.equals(ARTIST)) {
            ret = row.getArtist().getName();
        } else if (colName.equals(TITLE)) {
            ret = row.getTitle();
        } else if (colName.equals(LENGTH)) {
            ret = EmooUtils.getPlayingTimeString(row.getPlayingTime());
        } else if (colName.equals(VOTE)) {
            ret = row.getVote();
        } else if (GENRE.equals(colName)) {
            ret = row.getGenre();
        } else if (LOC_CNT.equals(colName)) {
            ret = new Integer(row.getLocations().size());
        } else {
            assert false;
        }
        return ret;
    }

    /**
     * Overridden!
     * @see javax.swing.table.TableModel#setValueAt(java.lang.Object, int, int)
     */
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        try {
            Song file = (Song) getRow(rowIndex);
            String cName = getColumnName(columnIndex);
            if (cName.equals(ARTIST)) {
                file.getArtist().removeSong(file);
                Artist artist = _factory.createArtist((String) aValue);
                file.setArtist(artist);
                artist.addSong(file);
            } else if (cName.equals(TITLE)) {
                file.setTitle((String) aValue);
            } else if (cName.equals(VOTE)) {
                file.setVote((Vote) aValue);
            } else if (GENRE.equals(cName)) {
                file.setGenre((Genre) aValue);
            } else {
                assert false;
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (PersistenceException e) {
            e.printStackTrace();
        }
    }
}
