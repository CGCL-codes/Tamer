package de.wilanthaou.songbookcreator.ui.view;

import de.wilanthaou.commons.swing.IconLoader;
import de.wilanthaou.commons.util.ResourceUtils;
import de.wilanthaou.songbookcreator.model.Song;
import de.wilanthaou.songbookcreator.ui.view.event.SelectionChangedEvent;
import de.wilanthaou.songbookcreator.ui.view.event.SelectionChangedListener;
import java.awt.BorderLayout;
import java.util.Collection;
import java.util.ResourceBundle;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;

/**
 * The applications main view.
 * @author Alexander Metzner
 * @version $Revision: 1.7 $
 * @since 1.0
 */
public class MainView extends JPanel {

    private ResourceBundle resources = ResourceUtils.getResourceBundle(MainView.class);

    private SongbookView songbookView;

    private JSplitPane verticalSplit;

    private JSplitPane horizontalSplit;

    private SongSelectionView selectionView;

    private PlaylistView playlistView;

    private SongView songView;

    private LibraryView libraryView;

    private JTabbedPane leftTab;

    private IconLoader iconLoader;

    public void setPlaylistView(PlaylistView playlistView) {
        this.playlistView = playlistView;
    }

    public void setIconLoader(IconLoader iconLoader) {
        this.iconLoader = iconLoader;
    }

    public void setLibraryView(LibraryView libraryView) {
        this.libraryView = libraryView;
    }

    public void setSongbookView(SongbookView databaseView) {
        this.songbookView = databaseView;
    }

    public void setSelectionView(SongSelectionView selectionView) {
        this.selectionView = selectionView;
    }

    public void setSongView(SongView songView) {
        this.songView = songView;
    }

    public void initialize() {
        initializeComponents();
        initializeLayout();
        songbookView.addSelectionChangedListener(new SelectionChangedListener<Song>() {

            public void viewSelectionChanged(SelectionChangedEvent<Song> e) {
                Collection<Song> t = e.getSelectedObjects();
                if ((t == null) || t.isEmpty()) {
                    return;
                }
                songView.setModel(t.iterator().next());
            }
        });
    }

    private void initializeComponents() {
        verticalSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        horizontalSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        verticalSplit.setRightComponent(horizontalSplit);
        JPanel vertTop = new JPanel(new BorderLayout());
        vertTop.add(selectionView, BorderLayout.CENTER);
        vertTop.add(playlistView, BorderLayout.EAST);
        horizontalSplit.setTopComponent(vertTop);
        JScrollPane sp = new JScrollPane(songView);
        JPanel pv = new JPanel(new BorderLayout());
        pv.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), resources.getString("preview.title")));
        pv.add(sp, BorderLayout.CENTER);
        horizontalSplit.setBottomComponent(pv);
        leftTab = new JTabbedPane();
        leftTab.addTab(resources.getString("tab.songbook"), iconLoader.loadIcon("tabs/songbook"), songbookView);
        leftTab.addTab(resources.getString("tab.library"), iconLoader.loadIcon("tabs/library"), libraryView);
        verticalSplit.setLeftComponent(leftTab);
    }

    private void initializeLayout() {
        setLayout(new BorderLayout());
        add(verticalSplit, BorderLayout.CENTER);
    }
}
