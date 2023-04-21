package dplayer.gui;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.MenuAdapter;
import org.eclipse.swt.events.MenuEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import dplayer.Settings;
import dplayer.gui.commands.PlayerControlCommand;
import dplayer.gui.commands.SelectTrackCommand;
import dplayer.gui.commands.SetTrackSkippingCommand;
import dplayer.gui.i18n.I18N;
import dplayer.player.events.CoverChangedEvent;
import dplayer.player.events.SelectedTrackChangedEvent;
import dplayer.player.events.TrackListChangedEvent;
import dplayer.queue.QueueItem;
import dplayer.queue.commands.CommandManager;
import dplayer.queue.events.EventListener;
import dplayer.queue.events.EventManager;
import dplayer.scanner.Song;
import dplayer.scanner.SongExt;
import dplayer.scanner.cache.CacheManager;

/**
 * Table with tracks.
 */
final class TrackTable extends Composite {

    private static final Logger logger = Logger.getLogger(TrackTable.class);

    private static final String DATA_TRACK = "SONG";

    private Table mTable;

    private Image mCoverImage;

    private TableItem mSelectedItem;

    TrackTable(final Composite parent) {
        super(parent, SWT.NONE);
        setLayout(new FillLayout());
        mTable = new Table(this, SWT.SINGLE | SWT.VIRTUAL | SWT.FULL_SELECTION);
        mTable.setHeaderVisible(true);
        mTable.setLinesVisible(false);
        mTable.addMouseMoveListener(new MouseMoveHandler());
        mTable.addMouseListener(new MouseClickHandler());
        TableColumn col1 = new TableColumn(mTable, SWT.LEFT);
        col1.setText(I18N.get("TABLE_COLUMN1", "Filename"));
        col1.setWidth(Settings.TABLE_COLUMN1_W.getInt());
        TableColumn col2 = new TableColumn(mTable, SWT.LEFT);
        col2.setText(I18N.get("TABLE_COLUMN2", "Length (m:s)"));
        col2.setWidth(Settings.TABLE_COLUMN2_W.getInt());
        mTable.addPaintListener(new PaintListener() {

            /** {@inheritDoc} */
            public void paintControl(final PaintEvent event) {
                if (mCoverImage != null && mTable.isEnabled() && Settings.DISPLAY_COVER.getBoolean()) {
                    event.gc.drawImage(mCoverImage, 0, 0);
                }
            }
        });
        mTable.addFocusListener(new FocusAdapter() {

            @Override
            public void focusLost(final FocusEvent event) {
                if (mTable.getSelectionIndex() != -1) {
                    mTable.setSelection(mTable.getSelectionIndex());
                }
            }
        });
        EventManager.addListener(TrackListChangedEvent.class, new EventListener("TrackTable(TrackListChangedEvent)") {

            private List<TableItem> skipList = new ArrayList<TableItem>();

            public void receive(final QueueItem event) {
                final TrackListChangedEvent tlce = (TrackListChangedEvent) event;
                if (tlce.isCleared()) {
                    synchronized (mTable) {
                        mTable.setEnabled(false);
                        Cursors.setWaitCursor();
                        mTable.removeAll();
                        skipList.clear();
                    }
                } else if (tlce.isCompleted()) {
                    synchronized (mTable) {
                        for (final TableItem ti : skipList) {
                            ti.dispose();
                        }
                        mTable.redraw();
                        mTable.setEnabled(true);
                        Cursors.setNormalCursor();
                    }
                } else if (tlce.hasNewTrack()) {
                    synchronized (mTable) {
                        final Song track = tlce.getNewTrack();
                        final TableItem ti = new TableItem(mTable, SWT.NONE, tlce.getNewTrackIndex());
                        final long duration = track.getDuration();
                        ti.setText(new String[] { track.getFilename(), duration < 0 ? "" : GuiUtils.formatTimeSeconds(duration) });
                        ti.setData(DATA_TRACK, track);
                        if (SongExt.isSkip(track)) {
                            ti.setForeground(ti.getDisplay().getSystemColor(SWT.COLOR_WIDGET_NORMAL_SHADOW));
                            if (!Settings.DISPLAY_SKIPPED.getBoolean()) {
                                skipList.add(ti);
                            }
                        } else {
                            ti.setForeground(ti.getDisplay().getSystemColor(SWT.COLOR_WIDGET_FOREGROUND));
                        }
                    }
                }
            }
        });
        EventManager.addListener(SelectedTrackChangedEvent.class, new EventListener("TrackTable(SelectedTrackChangedEvent)") {

            public void receive(final QueueItem event) {
                final SelectedTrackChangedEvent stce = (SelectedTrackChangedEvent) event;
                synchronized (mTable) {
                    if (stce.getSelectedTrack() == null) {
                        mSelectedItem = null;
                    } else {
                        final TableItem item = findTableItem(stce.getSelectedTrack());
                        if (item != null) {
                            select(item);
                        } else {
                            mSelectedItem = null;
                        }
                    }
                }
            }
        });
        EventManager.addListener(CoverChangedEvent.class, new EventListener("TrackTable(CoverChangedEvent)") {

            public void receive(final QueueItem event) {
                final CoverChangedEvent cce = (CoverChangedEvent) event;
                if (!cce.hasCover()) {
                    mCoverImage = null;
                } else {
                    logger.debug(" Using " + cce.getPath() + " as cover :-)");
                    try {
                        final ImageData imageData = new ImageData(cce.getPath());
                        imageData.alpha = 64;
                        mCoverImage = new Image(getDisplay(), imageData);
                    } catch (SWTException e) {
                        logger.debug(e);
                    }
                }
            }
        });
    }

    private void select(final TableItem item) {
        assert item != null;
        mTable.setFocus();
        if (item == mSelectedItem) {
            return;
        }
        if (mSelectedItem != null && !mSelectedItem.isDisposed()) {
            mSelectedItem.setBackground(null);
        }
        mSelectedItem = item;
        mSelectedItem.setBackground(item.getDisplay().getSystemColor(SWT.COLOR_WIDGET_NORMAL_SHADOW));
        mTable.setSelection(item);
        final int index = mTable.indexOf(item);
        final Rectangle rect = mTable.getClientArea();
        final int itemHeight = mTable.getItemHeight();
        final int headerHeight = mTable.getHeaderHeight();
        final int visibleCount = (rect.height - headerHeight + itemHeight - 1) / itemHeight;
        if ((index - mTable.getTopIndex()) >= visibleCount || index < mTable.getTopIndex()) {
            final int topIndex = index - 3;
            mTable.setTopIndex(Math.max(topIndex, 0));
        }
    }

    private TableItem findTableItem(final Song song) {
        assert song != null;
        synchronized (mTable) {
            for (final TableItem item : mTable.getItems()) {
                final Song s = getTrack(item);
                assert s != null;
                if (s.equals(song)) {
                    return item;
                }
            }
            return null;
        }
    }

    private Song getTrack(final TableItem item) {
        return (Song) item.getData(DATA_TRACK);
    }

    /**
     * Handle mouse clicks.
     */
    private final class MouseClickHandler extends MouseAdapter {

        @Override
        public void mouseDoubleClick(final MouseEvent event) {
            CommandManager.send(new PlayerControlCommand(PlayerControlCommand.Control.PLAY));
        }

        @Override
        public void mouseUp(final MouseEvent event) {
            final TableItem item = mTable.getItem(new Point(event.x, event.y));
            if (event.button == 1) {
                if (item != null) {
                    final Song track = getTrack(item);
                    assert track != null;
                    CommandManager.send(new SelectTrackCommand(track));
                }
            } else {
                showPopupMenu(item);
            }
        }

        private void showPopupMenu(final TableItem item) {
            final Menu menu = new Menu(mTable);
            MenuUtils.addAboutItem(menu);
            MenuUtils.addSettingsItem(menu);
            MenuUtils.addMenuSeparator(menu);
            MenuUtils.addPlayItem(menu);
            MenuUtils.addPauseItem(menu);
            MenuUtils.addStopItem(menu);
            MenuUtils.addMenuSeparator(menu);
            MenuUtils.addPrevItem(menu);
            MenuUtils.addNextItem(menu);
            MenuUtils.addMenuSeparator(menu);
            if (item != null) {
                final Song track = getTrack(item);
                assert track != null;
                MenuUtils.addMenuItem(menu, track.getFilename(), null);
                MenuUtils.addCheckedMenuItem(menu, I18N.get("MENU_SKIP", "Skip track"), SongExt.isSkip(track), new SelectionAdapter() {

                    @Override
                    public void widgetSelected(final SelectionEvent event) {
                        CommandManager.send(new SetTrackSkippingCommand(track, !SongExt.isSkip(track)));
                    }
                });
            }
            MenuUtils.addDisplaySkippedItem(menu);
            MenuUtils.addMenuSeparator(menu);
            MenuUtils.addExitItem(menu);
            menu.addMenuListener(new MenuAdapter() {

                /** {@inheritDoc} */
                @Override
                public void menuHidden(final MenuEvent event) {
                    if (mSelectedItem != null) {
                        mTable.setSelection(mSelectedItem);
                    }
                }
            });
            menu.setVisible(true);
        }
    }

    /**
     * Handle "mouse over" movement.
     */
    private final class MouseMoveHandler implements MouseMoveListener {

        private TableItem mMouseOverItem;

        /** {@inheritDoc} */
        public void mouseMove(final MouseEvent event) {
            Settings.TABLE_COLUMN1_W.setInt(mTable.getColumn(0).getWidth());
            Settings.TABLE_COLUMN2_W.setInt(mTable.getColumn(1).getWidth());
            final TableItem item = mTable.getItem(new Point(event.x, event.y));
            if (item != null) {
                if (item != mMouseOverItem) {
                    setToolTip(getTrack(item));
                }
            } else {
                cancelToolTip();
            }
            mMouseOverItem = item;
        }

        private void setToolTip(final Song song) {
            final StringBuilder sb = new StringBuilder();
            if (song.getTitle() != null && song.getTitle().length() > 0) {
                sb.append(song.getTitle()).append('\n');
                if (song.getArtist() != null && song.getArtist().length() > 0) {
                    sb.append(song.getArtist()).append('\n');
                }
                boolean nl = false;
                if (song.getAlbum() != null && song.getAlbum().length() > 0) {
                    sb.append(song.getAlbum());
                    nl = true;
                }
                if (song.getYear() != null && song.getYear().length() > 0) {
                    sb.append(", ").append(song.getYear());
                    nl = true;
                }
                if (nl) {
                    sb.append('\n');
                }
            }
            if (CacheManager.isEnabled()) {
                DateFormat df = SimpleDateFormat.getDateInstance(SimpleDateFormat.SHORT);
                Date date = SongExt.getInsertDate(song);
                sb.append(I18N.get("STATS_INSERTED", "Inserted: {0}", new String[] { date == null ? "" : df.format(date) })).append('\n');
                final Date lastPlayed = SongExt.getLastPlayed(song);
                sb.append(I18N.get("STATS_LAST_PLAYED", "Last played: {0}", lastPlayed != null ? new String[] { df.format(lastPlayed) } : new String[] { I18N.get("STATS_NEVER_PLAYED", "never") })).append('\n');
                sb.append(I18N.get("STATS_PLAY_COUNTER", "Play counter: {0} times", new String[] { Integer.toString(SongExt.getPlayCounter(song)) })).append('\n');
            }
            if (sb.length() > 0) {
                mTable.setToolTipText(sb.substring(0, sb.length() - 1));
            } else {
                cancelToolTip();
            }
        }

        private void cancelToolTip() {
            mTable.setToolTipText(null);
        }
    }
}
