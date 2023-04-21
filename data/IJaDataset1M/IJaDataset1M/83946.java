package de.emoo.ui.swing.dialogs;

import java.awt.BorderLayout;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Iterator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import de.emoo.database.Album;
import de.emoo.database.Song;
import de.emoo.database.Track;
import de.emoo.database.persistence.PersistenceException;
import de.emoo.database.persistence.Persister;
import de.emoo.database.persistence.Transaction;
import de.emoo.ui.swing.EmooBaseDialog;
import de.emoo.ui.swing.EmooComponent;
import de.emoo.ui.swing.main.EmooMainFrame;
import de.emoo.ui.swing.table.model.SongTableModel;
import de.emoo.ui.swing.table.model.TrackTableModel;
import de.emoo.ui.swing.widgets.TableSelectionComponent;
import de.emoo.util.EmooException;

/**
 * <b>EditAlbumComponent</b>:
 * provides the ability to manipulate the tracks of an album.
 * 
 * @author gandalf
 * @version $Id: EditAlbumDialog.java,v 1.2 2004/08/09 10:02:20 tbuchloh Exp $
 */
final class EditAlbumComponent extends EmooComponent {

    private static final Log LOG = LogFactory.getLog(EditAlbumComponent.class);

    /**
     * <b>Song2TrackConverter</b>:
     * converts a Song to a Track.
     * 
     * @author gandalf
     * @version $Id: EditAlbumDialog.java,v 1.2 2004/08/09 10:02:20 tbuchloh Exp $
     */
    private static final class Song2TrackConverter implements TableSelectionComponent.ModelConverter {

        private TrackTableModel _tracks;

        private int _maxTrackNumber;

        /**
         * creates a new Song2TrackConverter
         * @param tracks are the current modeled Track objects.
         */
        public Song2TrackConverter(TrackTableModel tracks) {
            _tracks = tracks;
        }

        /**
         * Overridden!
         * @see de.emoo.ui.swing.widgets.TableSelectionComponent.ModelConverter#left2Right(java.lang.Object)
         */
        public Object left2Right(Object obj) throws EmooException {
            Song song = (Song) obj;
            Track track = new Track(getNextTrackNumber(), song);
            return track;
        }

        private int getNextTrackNumber() {
            int max = Math.max(0, _maxTrackNumber);
            for (Iterator i = _tracks.getRows().iterator(); i.hasNext(); ) {
                Track track = (Track) i.next();
                max = Math.max(max, track.getNumber());
            }
            _maxTrackNumber = max + 1;
            return _maxTrackNumber;
        }
    }

    private Album _album;

    private TableSelectionComponent _tables;

    private Transaction _tx;

    private TrackTableModel _trackModel;

    /**
     * creates a new EditAlbumComponent
     * @param album is the Album to edit.
     */
    public EditAlbumComponent(Album album) {
        _album = album;
        try {
            _tx = Persister.getDataStore().openTransaction();
            initTables();
            initLayout();
        } catch (PersistenceException e) {
            LOG.error(e);
        }
    }

    private void initLayout() {
        setLayout(new BorderLayout());
        add(_tables);
    }

    private void initTables() throws PersistenceException {
        SongTableModel allSongs = new SongTableModel();
        allSongs.setEditable(false);
        allSongs.setRows(_tx.findAll(Song.class));
        _trackModel = new TrackTableModel();
        _trackModel.setRows(new ArrayList(_album.getTracks()));
        _tables = new TableSelectionComponent("All Songs", allSongs, "Tracks", _trackModel);
        _tables.setModelConverter(new Song2TrackConverter(_trackModel));
    }

    /**
     * Overridden!
     * @see de.emoo.ui.swing.EmooComponent#save()
     */
    public void save() throws EmooException {
        _album.setTracks(_trackModel.getRows());
    }
}

/**
 * <b>EditAlbumDialog</b>:
 * 
 * @author gandalf
 * @version $Id: EditAlbumDialog.java,v 1.2 2004/08/09 10:02:20 tbuchloh Exp $
 */
public final class EditAlbumDialog extends EmooBaseDialog {

    private static final MessageFormat TITLE = new MessageFormat("Edit Album \"{0}\"");

    /**
     * creates a new EditAlbumDialog
     * @param album is the album to edit.
     */
    public EditAlbumDialog(Album album) {
        super(EmooMainFrame.getInstance(), TITLE.format(new Object[] { album.getName() }), true);
        setMainPanel(new EditAlbumComponent(album));
    }
}
