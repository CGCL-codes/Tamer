package de.emoo.ui.swing.dialogs;

import java.awt.BorderLayout;
import java.text.MessageFormat;
import java.util.Iterator;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import de.emoo.database.Playlist;
import de.emoo.database.Song;
import de.emoo.ui.swing.EmooBaseDialog;
import de.emoo.ui.swing.EmooComponent;
import de.emoo.ui.swing.main.EmooMainFrame;
import de.emoo.ui.swing.table.model.EmooTableModel;
import de.emoo.ui.swing.table.model.SongTableModel;
import de.emoo.ui.swing.widgets.TableSelectionComponent;
import de.emoo.util.EmooException;
import de.emoo.util.EmooUtils;
import de.tbuchloh.util.event.ContentChangedEvent;
import de.tbuchloh.util.event.ContentListener;
import de.tbuchloh.util.swing.GridBagBuilder;

/**
 * <b>EditPlaylistComponent</b>:
 * Provides manipulation of Playlists
 * 
 * @author gandalf
 * @version $Id: EditPlaylistDialog.java,v 1.4 2004/08/11 11:52:13 tbuchloh Exp $
 */
final class EditPlaylistComponent extends EmooComponent {

    private static final String SELECTED_SONGS_TITLE = "Selected songs";

    private static final String ALL_SONGS_TITLE = "All Songs";

    private static final String COUNT_LABEL = "Count:";

    private static final String LENGTH_LABEL = "Length:";

    private static final String SIZE_LABEL = "Size:";

    /**
     * <b>StatisticsComponent</b>:
     * presents some values like aggregated length, size and counts.
     * 
     * @author gandalf
     * @version $Id: EditPlaylistDialog.java,v 1.4 2004/08/11 11:52:13 tbuchloh Exp $
     */
    private static final class StatisticsComponent extends JComponent {

        private JLabel _sizePanel;

        private JLabel _lengthPanel;

        private JLabel _countPanel;

        /**
         * creates a new StatisticsComponent
         */
        public StatisticsComponent() {
            _sizePanel = new JLabel("0");
            _lengthPanel = new JLabel("00:00:00");
            _countPanel = new JLabel("0");
            initLayout();
        }

        private void initLayout() {
            setLayout(new BorderLayout());
            setBorder(BorderFactory.createEmptyBorder());
            JPanel p = new JPanel();
            GridBagBuilder builder = new GridBagBuilder(p);
            builder.add(new JLabel(COUNT_LABEL));
            builder.addLast(_countPanel);
            builder.add(new JLabel(LENGTH_LABEL));
            builder.addLast(_lengthPanel);
            builder.add(new JLabel(SIZE_LABEL));
            builder.addLast(_sizePanel);
            this.add(p, BorderLayout.EAST);
        }

        /**
         * @param songs the Song objects to aggregate.
         */
        public void recalculate(List songs) {
            Playlist tmp = new Playlist();
            for (Iterator i = songs.iterator(); i.hasNext(); ) {
                Song s = (Song) i.next();
                tmp.addSong(s);
            }
            _sizePanel.setText(EmooUtils.getSizeString(tmp.getTotalSize()));
            _lengthPanel.setText(EmooUtils.getPlayingTimeString(tmp.getPlayingTime()));
            _countPanel.setText(String.valueOf(songs.size()));
        }
    }

    private Playlist _pl;

    private StatisticsComponent _statsComp;

    private TableSelectionComponent _tables;

    /**
     * creates a new EditPlaylistComponent
     * @param allSongs are all songs out of which we can select.
     * @param pl is the playlist to edit.
     */
    public EditPlaylistComponent(List allSongs, Playlist pl) {
        _pl = pl;
        _statsComp = new StatisticsComponent();
        EmooTableModel allSongsModel = new SongTableModel();
        allSongsModel.setEditable(false);
        allSongsModel.setRows(allSongs);
        EmooTableModel plSongsModel = new SongTableModel();
        plSongsModel.setEditable(false);
        plSongsModel.setRows(pl.getSongs());
        _tables = new TableSelectionComponent(ALL_SONGS_TITLE, allSongsModel, SELECTED_SONGS_TITLE, plSongsModel);
        _tables.addContentListener(new ContentListener() {

            public void contentChanged(ContentChangedEvent e) {
                recalculate();
            }
        });
        initLayout();
        recalculate();
    }

    private void initLayout() {
        setLayout(new BorderLayout());
        add(_tables, BorderLayout.CENTER);
        add(_statsComp, BorderLayout.SOUTH);
    }

    /**
     * Overridden!
     * @see de.emoo.ui.swing.EmooComponent#save()
     */
    public void save() throws EmooException {
        _pl.clear();
        for (Iterator i = _tables.getSelectedValues().iterator(); i.hasNext(); ) {
            Song s = (Song) i.next();
            _pl.addSong(s);
        }
    }

    protected void recalculate() {
        _statsComp.recalculate(_tables.getSelectedValues());
    }
}

/**
 * <b>EditPlaylistDialog</b>:
 * provides the ability to mainpulate a playlist.
 * 
 * @author gandalf
 * @version $Id: EditPlaylistDialog.java,v 1.4 2004/08/11 11:52:13 tbuchloh Exp $
 */
public final class EditPlaylistDialog extends EmooBaseDialog {

    private static final MessageFormat TITLE = new MessageFormat("Edit playlist \"{0}\"");

    /**
     * creates a new EditPlaylistDialog
     * @see EditPlaylistComponent#EditPlaylistComponent(List, Playlist)
     */
    public EditPlaylistDialog(List allSongs, Playlist pl) {
        super(EmooMainFrame.getInstance(), TITLE.format(new Object[] { pl.getName() }), true);
        setMainPanel(new EditPlaylistComponent(allSongs, pl));
    }
}
