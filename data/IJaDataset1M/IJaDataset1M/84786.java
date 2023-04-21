package org.tunesremote_se;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import net.firefly.client.controller.request.PlaylistRequestManager;
import net.firefly.client.controller.request.TunesRemoteRequestManager;
import net.firefly.client.gui.context.Context;
import net.firefly.client.gui.swing.dialog.DatabaseDialog;
import net.firefly.client.gui.swing.frame.ClientFrame;
import net.firefly.client.model.configuration.Configuration;
import net.firefly.client.model.library.LibraryInfo;
import net.firefly.client.player.MediaPlayer;
import org.libtunesremote_se.CloseOnLastWindow;
import org.libtunesremote_se.LibraryDetails;
import org.tunesremote.daap.Session;

public class TunesRemoteSession {

    private final ClientFrame client;

    private final Configuration config;

    private final Context context;

    private final MediaPlayer mediaPlayer;

    private final LibraryInfo libraryInfo;

    protected TunesRemoteRequestManager mainRequestManager;

    protected PlaylistRequestManager playlistRequestManager;

    private final Session session;

    private void close() {
        client.setVisible(false);
        client.updateConfigOnClose();
        mediaPlayer.close();
        client.dispose();
        CloseOnLastWindow.unregisterWindow();
    }

    public TunesRemoteSession(LibraryDetails library, Session session) {
        this.config = Configuration.getInstance();
        this.context = new Context(this.config);
        this.context.setSession(session);
        this.session = session;
        this.mediaPlayer = new MediaPlayer(context, session);
        this.context.setPlayer(this.mediaPlayer);
        this.libraryInfo = new LibraryInfo();
        libraryInfo.setHost(library.getAddress());
        libraryInfo.setPort(library.getPort());
        libraryInfo.setSongCount(0);
        libraryInfo.setLibraryName(library.getLibraryName());
        libraryInfo.setLibraryId(library.getLibrary());
        this.context.setLibraryInfo(libraryInfo);
        this.mainRequestManager = new TunesRemoteRequestManager(this.session);
        this.context.setMainRequestManager(mainRequestManager);
        this.playlistRequestManager = new PlaylistRequestManager(this.context, this.session);
        this.context.setPlaylistRequestManager(this.playlistRequestManager);
        client = new ClientFrame(context);
        client.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                close();
            }
        });
        CloseOnLastWindow.registerWindow();
        client.setVisible(true);
        context.getLibraryInfo().setSongCount(this.mainRequestManager.getTrackCount());
        context.setServerVersion(library.getLibraryType());
        DatabaseDialog nextDialog = new DatabaseDialog(context, client);
        nextDialog.setVisible(true);
    }
}
