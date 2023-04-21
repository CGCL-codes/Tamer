package com.nithinphilips.wifimusicsync;

import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;
import org.openqa.selenium.remote.me.util.StringUtils;
import net.rim.device.api.applicationcontrol.ApplicationPermissions;
import net.rim.device.api.applicationcontrol.ApplicationPermissionsManager;
import net.rim.device.api.command.CommandHandler;
import net.rim.device.api.command.ReadOnlyCommandMetadata;
import net.rim.device.api.ui.component.progressindicator.ActivityIndicatorController;
import net.rim.device.api.ui.component.progressindicator.ActivityIndicatorModel;
import net.rim.device.api.ui.component.progressindicator.ActivityIndicatorView;
import net.rim.device.api.ui.component.StandardTitleBar;
import net.rim.device.api.ui.menu.CommandItem;
import net.rim.device.api.ui.menu.CommandItemProvider;
import net.rim.device.api.ui.menu.DefaultContextMenuProvider;
import net.rim.device.api.ui.menu.SubMenu;
import net.rim.device.api.util.StringProvider;
import net.rim.device.api.system.ApplicationDescriptor;
import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.DrawStyle;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.MenuItem;
import net.rim.device.api.ui.TransitionContext;
import net.rim.device.api.ui.Ui;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.UiEngineInstance;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.ListField;
import net.rim.device.api.ui.component.Menu;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.container.MainScreen;
import net.rim.device.api.ui.container.VerticalFieldManager;
import net.rim.device.api.ui.decor.BackgroundFactory;
import net.rim.device.api.ui.image.Image;
import net.rim.device.api.ui.image.ImageFactory;
import com.fairview5.keepassbb2.common.ui.ProgressDialog;
import com.nithinphilips.Debug;
import com.nithinphilips.AssemblyInfo;
import com.nithinphilips.wifimusicsync.components.ProgressListModel;
import com.nithinphilips.wifimusicsync.components.WifiMusicSyncProperties;
import com.nithinphilips.wifimusicsync.controller.PlaylistDownloader;
import com.nithinphilips.wifimusicsync.controller.Subscriber;
import com.nithinphilips.wifimusicsync.model.PlaylistInfo;
import com.nithinphilips.wifimusicsync.model.SyncAction;
import com.nithinphilips.wifimusicsync.model.SyncResponse;
import com.nithinphilips.wifimusicsync.permissions.MusicSyncPermissionReasonProvider;

public class WifiMusicSyncScreen extends MainScreen {

    ListField myListView;

    ProgressListModel myListModel;

    LabelField statusLabel;

    public static final int SUBSCRIBE_PLAYLISTS = 0;

    public static final int SUBSCRIBE_ARTISTS = 1;

    public static final int SUBSCRIBE_ALBUMS = 2;

    final MenuItem menuItemSync;

    final MenuItem menuItemChoosePlaylists;

    final MenuItem menuItemChooseAlbums;

    final MenuItem menuItemChooseArtists;

    final MenuItem menuItemOptions;

    final MenuItem menuItemAbout;

    final MenuItem menuItemTest;

    final SubMenu subMenuChoose;

    final ActivityIndicatorView view = new ActivityIndicatorView(FIELD_VCENTER);

    final ActivityIndicatorModel model = new ActivityIndicatorModel();

    final ActivityIndicatorController controller = new ActivityIndicatorController();

    HorizontalFieldManager statusContainer = new HorizontalFieldManager(Field.USE_ALL_WIDTH) {

        public void paint(Graphics graphics) {
            graphics.setColor(Color.WHITE);
            super.paint(graphics);
        }
    };

    public WifiMusicSyncScreen() {
        super(VERTICAL_SCROLL);
        checkPermissions();
        ((VerticalFieldManager) getMainManager()).setBackground(BackgroundFactory.createSolidBackground(Color.BLACK));
        StandardTitleBar _titleBar = new StandardTitleBar();
        _titleBar.addTitle(AssemblyInfo.Title);
        _titleBar.addNotifications();
        _titleBar.addSignalIndicator();
        this.setTitle(_titleBar);
        this.setTitle(new LabelField(AssemblyInfo.Title));
        this.myListView = new ListField();
        this.myListView.setEmptyString("Choose 'Sync' to start syncing.", DrawStyle.HCENTER);
        this.myListModel = new ProgressListModel(this.myListView, new Vector());
        Bitmap bitmap = Bitmap.getBitmapResource("progress-spinner.png");
        view.setController(controller);
        view.setModel(model);
        controller.setModel(model);
        controller.setView(view);
        model.setController(controller);
        view.createActivityImageField(bitmap, 5, Field.FIELD_LEFT);
        add(this.myListView);
        statusLabel = new LabelField("", LabelField.USE_ALL_WIDTH);
        statusContainer.setBackground(BackgroundFactory.createSolidBackground(Color.BLACK));
        statusContainer.add(statusLabel);
        setStatus(statusContainer);
        setContextMenuProvider(new DefaultContextMenuProvider());
        CommandItemProvider provider = new CommandItemProvider() {

            public Object getContext(Field field) {
                return field;
            }

            public Vector getItems(Field field) {
                Vector items = new Vector();
                Image syncIcon = ImageFactory.createImage(Bitmap.getBitmapResource("sync.png"));
                items.addElement(new CommandItem(new StringProvider("Sync"), syncIcon, new net.rim.device.api.command.Command(syncCommand)));
                Image subscribePlaylistsIcon = ImageFactory.createImage(Bitmap.getBitmapResource("choose-playlist.png"));
                items.addElement(new CommandItem(new StringProvider("Choose Playlists"), subscribePlaylistsIcon, new net.rim.device.api.command.Command(subscribePlaylistsCommand)));
                Image subscribeAlbumIcon = ImageFactory.createImage(Bitmap.getBitmapResource("choose-album.png"));
                items.addElement(new CommandItem(new StringProvider("Choose Albums"), subscribeAlbumIcon, new net.rim.device.api.command.Command(subscribeAlbumsCommand)));
                Image subscribeArtistIcon = ImageFactory.createImage(Bitmap.getBitmapResource("choose-artist.png"));
                items.addElement(new CommandItem(new StringProvider("Choose Artists"), subscribeArtistIcon, new net.rim.device.api.command.Command(subscribeArtistsCommand)));
                return items;
            }
        };
        setCommandItemProvider(provider);
        menuItemSync = new MenuItem(new StringProvider("Sync"), 10000000, 100);
        menuItemSync.setCommand(new net.rim.device.api.command.Command(syncCommand));
        menuItemChoosePlaylists = new MenuItem(new StringProvider("Playlists"), 100, 100);
        menuItemChoosePlaylists.setCommand(new net.rim.device.api.command.Command(subscribePlaylistsCommand));
        menuItemChooseAlbums = new MenuItem(new StringProvider("Albums"), 200, 100);
        menuItemChooseAlbums.setCommand(new net.rim.device.api.command.Command(subscribeAlbumsCommand));
        menuItemChooseArtists = new MenuItem(new StringProvider("Artists"), 300, 100);
        menuItemChooseArtists.setCommand(new net.rim.device.api.command.Command(subscribeArtistsCommand));
        subMenuChoose = new SubMenu(new MenuItem[] { menuItemChoosePlaylists, menuItemChooseAlbums, menuItemChooseArtists }, "Sync Selection", 10000000, 100);
        menuItemOptions = new MenuItem(new StringProvider("Options"), 100000, 100);
        menuItemOptions.setCommand(new net.rim.device.api.command.Command(optionsCommand));
        menuItemAbout = new MenuItem(new StringProvider("About"), 100000, 100);
        menuItemAbout.setCommand(new net.rim.device.api.command.Command(showAboutCommand));
        menuItemTest = new MenuItem(new StringProvider("Test"), 10000, 100);
        menuItemTest.setCommand(new net.rim.device.api.command.Command(runTestCommand));
        menuItemSync = new MenuItem("Sync", 10000000, 100) {

            public void run() {
                sync();
            }
        };
        menuItemChoosePlaylists = new MenuItem("Choose Playlists", 100000, 100) {

            public void run() {
                subscribe(SUBSCRIBE_PLAYLISTS);
            }
        };
        menuItemChooseAlbums = new MenuItem("Choose Albums", 100000, 100) {

            public void run() {
                subscribe(SUBSCRIBE_ALBUMS);
            }
        };
        menuItemChooseArtists = new MenuItem("Choose Artists", 100000, 100) {

            public void run() {
                subscribe(SUBSCRIBE_ARTISTS);
            }
        };
        menuItemOptions = new MenuItem("Options", 100000, 100) {

            public void run() {
                pushSettingsScreen();
            }
        };
        menuItemAbout = new MenuItem("About", 100000, 100) {

            public void run() {
                pushAboutScreen();
            }
        };
        menuItemTest = new MenuItem("Test", 100000, 100) {

            public void run() {
                runTest();
            }
        };
    }

    protected void makeMenu(Menu menu, int instance) {
        menu.add(subMenuChoose);
        menu.add(menuItemChoosePlaylists);
        menu.add(menuItemChooseAlbums);
        menu.add(menuItemChooseArtists);
        menu.add(menuItemSync);
        menu.add(menuItemAbout);
        menu.add(menuItemOptions);
        if (Debug.DEBUG) menu.add(menuItemTest);
        super.makeMenu(menu, instance);
    }

    protected void onUiEngineAttached(boolean attached) {
        if (attached && StringUtils.isEmpty(WifiMusicSyncProperties.fetch().getServerIp())) {
            UiApplication.getUiApplication().invokeLater(new Runnable() {

                public void run() {
                    if (Dialog.ask(Dialog.D_YES_NO, "You must configure the server first. Do you want to do that now?") == Dialog.YES) {
                        pushSettingsScreen();
                    } else {
                        System.exit(0);
                    }
                }
            });
        }
    }

    final CommandHandler optionsCommand = new CommandHandler() {

        public void execute(ReadOnlyCommandMetadata metadata, Object context) {
            pushSettingsScreen();
        }
    };

    final CommandHandler syncCommand = new CommandHandler() {

        public void execute(ReadOnlyCommandMetadata metadata, Object context) {
            sync();
        }
    };

    final CommandHandler subscribePlaylistsCommand = new CommandHandler() {

        public void execute(ReadOnlyCommandMetadata metadata, Object context) {
            subscribe(SUBSCRIBE_PLAYLISTS);
        }
    };

    final CommandHandler subscribeArtistsCommand = new CommandHandler() {

        public void execute(ReadOnlyCommandMetadata metadata, Object context) {
            subscribe(SUBSCRIBE_ARTISTS);
        }
    };

    final CommandHandler subscribeAlbumsCommand = new CommandHandler() {

        public void execute(ReadOnlyCommandMetadata metadata, Object context) {
            subscribe(SUBSCRIBE_ALBUMS);
        }
    };

    final CommandHandler showAboutCommand = new CommandHandler() {

        public void execute(ReadOnlyCommandMetadata metadata, Object context) {
            pushAboutScreen();
        }
    };

    final CommandHandler runTestCommand = new CommandHandler() {

        public void execute(ReadOnlyCommandMetadata metadata, Object context) {
            runTest();
        }
    };

    void runTest() {
        PlaylistInfo[] choices = new PlaylistInfo[20];
        for (int i = 0; i < choices.length; i++) choices[i] = new PlaylistInfo("", "Test " + i, i);
        pushChoicesScreen(choices, "Tests");
    }

    void pushSettingsScreen() {
        SettingsScreen screen = new SettingsScreen();
        TransitionContext transitionContextIn;
        TransitionContext transitionContextOut;
        UiEngineInstance engine = Ui.getUiEngineInstance();
        transitionContextIn = new TransitionContext(TransitionContext.TRANSITION_SLIDE);
        transitionContextIn.setIntAttribute(TransitionContext.ATTR_DURATION, 250);
        transitionContextIn.setIntAttribute(TransitionContext.ATTR_DIRECTION, TransitionContext.DIRECTION_UP);
        transitionContextOut = new TransitionContext(TransitionContext.TRANSITION_SLIDE);
        transitionContextOut.setIntAttribute(TransitionContext.ATTR_DURATION, 250);
        transitionContextOut.setIntAttribute(TransitionContext.ATTR_DIRECTION, TransitionContext.DIRECTION_DOWN);
        transitionContextOut.setIntAttribute(TransitionContext.ATTR_KIND, TransitionContext.KIND_OUT);
        engine.setTransition(null, screen, UiEngineInstance.TRIGGER_PUSH, transitionContextIn);
        engine.setTransition(screen, null, UiEngineInstance.TRIGGER_POP, transitionContextOut);
        UiApplication.getUiApplication().pushModalScreen(screen);
    }

    void pushAboutScreen() {
        AboutScreen screen = new AboutScreen();
        TransitionContext transitionContextIn;
        TransitionContext transitionContextOut;
        UiEngineInstance engine = Ui.getUiEngineInstance();
        transitionContextIn = new TransitionContext(TransitionContext.TRANSITION_SLIDE);
        transitionContextIn.setIntAttribute(TransitionContext.ATTR_DURATION, 300);
        transitionContextIn.setIntAttribute(TransitionContext.ATTR_DIRECTION, TransitionContext.DIRECTION_DOWN);
        transitionContextIn.setIntAttribute(TransitionContext.ATTR_KIND, TransitionContext.KIND_IN);
        transitionContextOut = new TransitionContext(TransitionContext.TRANSITION_SLIDE);
        transitionContextOut.setIntAttribute(TransitionContext.ATTR_DURATION, 300);
        transitionContextOut.setIntAttribute(TransitionContext.ATTR_DIRECTION, TransitionContext.DIRECTION_UP);
        transitionContextOut.setIntAttribute(TransitionContext.ATTR_KIND, TransitionContext.KIND_OUT);
        engine.setTransition(null, screen, UiEngineInstance.TRIGGER_PUSH, transitionContextIn);
        engine.setTransition(screen, null, UiEngineInstance.TRIGGER_POP, transitionContextOut);
        UiApplication.getUiApplication().pushModalScreen(screen);
    }

    boolean pushChoicesScreen(PlaylistInfo[] choices, String title) {
        SyncItemsSelectionScreen screen = new SyncItemsSelectionScreen(choices, title);
        TransitionContext transitionContextIn;
        TransitionContext transitionContextOut;
        UiEngineInstance engine = Ui.getUiEngineInstance();
        transitionContextIn = new TransitionContext(TransitionContext.TRANSITION_SLIDE);
        transitionContextIn.setIntAttribute(TransitionContext.ATTR_DURATION, 250);
        transitionContextIn.setIntAttribute(TransitionContext.ATTR_DIRECTION, TransitionContext.DIRECTION_LEFT);
        transitionContextIn.setIntAttribute(TransitionContext.ATTR_KIND, TransitionContext.KIND_IN);
        transitionContextOut = new TransitionContext(TransitionContext.TRANSITION_SLIDE);
        transitionContextOut.setIntAttribute(TransitionContext.ATTR_DURATION, 250);
        transitionContextOut.setIntAttribute(TransitionContext.ATTR_DIRECTION, TransitionContext.DIRECTION_RIGHT);
        transitionContextOut.setIntAttribute(TransitionContext.ATTR_KIND, TransitionContext.KIND_OUT);
        engine.setTransition(null, screen, UiEngineInstance.TRIGGER_PUSH, transitionContextIn);
        engine.setTransition(screen, null, UiEngineInstance.TRIGGER_POP, transitionContextOut);
        UiApplication.getUiApplication().pushModalScreen(screen);
        return screen.getResult() == Dialog.OK;
    }

    void subscribe(final int sourceType) {
        beginProgressTask();
        setStatusMessage("Connecting...");
        ProgressDialog.prepareModal("Connecting...");
        Thread t = new Thread() {

            public void run() {
                try {
                    final PlaylistInfo[] playlists;
                    final String title;
                    final WifiMusicSyncProperties props = WifiMusicSyncProperties.fetch();
                    final Subscriber subscriber = new Subscriber(props.getServerUrl(), props.getLocalStoreRoot(), props.getClientId());
                    if (sourceType == SUBSCRIBE_ALBUMS) {
                        playlists = subscriber.getAlbums();
                        title = "Albums";
                    } else if (sourceType == SUBSCRIBE_ARTISTS) {
                        playlists = subscriber.getArtists();
                        title = "Artists";
                    } else {
                        playlists = subscriber.getPlaylists();
                        title = "Playlists";
                    }
                    ProgressDialog.closeProgress();
                    if (playlists != null) {
                        UiApplication.getUiApplication().invokeLater(new Runnable() {

                            public void run() {
                                try {
                                    if (!pushChoicesScreen(playlists, title)) {
                                        setStatusMessage("Nothing changed.");
                                        resetStatusMessage();
                                        return;
                                    }
                                    ProgressDialog.prepareModal("Getting cleanup info...");
                                    setStatusMessage("Getting cleanup information...");
                                    Thread u = new Thread() {

                                        public void run() {
                                            try {
                                                for (int i = 0; i < playlists.length; i++) {
                                                    if (playlists[i].isSelected()) playlists[i].createOnFileSystem(props.getLocalStoreRoot()); else playlists[i].deleteOnFileSystem(props.getLocalStoreRoot());
                                                }
                                                final SyncResponse response = subscriber.updateSubscription();
                                                ProgressDialog.closeProgress();
                                                setStatusMessage("Cleaning up...");
                                                if (response != null) {
                                                    final SyncAction[] actions = response.getActions();
                                                    UiApplication.getUiApplication().invokeLater(new Runnable() {

                                                        public void run() {
                                                            myListModel.erase();
                                                            for (int i = 0; i < actions.length; i++) {
                                                                myListModel.insert(actions[i]);
                                                            }
                                                        }
                                                    });
                                                    PlaylistDownloader.executeActions(actions);
                                                    setStatusMessage("Done");
                                                } else {
                                                    setStatusMessage("Error updating subscriptions.");
                                                }
                                            } catch (Throwable e) {
                                                if (Debug.DEBUG) setStatusMessage(e.toString() + e.getMessage()); else setStatusMessage("Critical error. Subscription failed.");
                                            } finally {
                                                resetStatusMessage();
                                            }
                                        }

                                        ;
                                    };
                                    u.start();
                                    ProgressDialog.doModal();
                                } catch (Throwable e) {
                                    if (Debug.DEBUG) setStatusMessage(e.toString() + e.getMessage()); else setStatusMessage("Critical error. Subscription failed.");
                                }
                            }
                        });
                    } else setStatusMessage("Error. No response from server.");
                } catch (Throwable e) {
                    if (Debug.DEBUG) setStatusMessage(e.toString() + e.getMessage()); else setStatusMessage("Critical error. Subscription failed.");
                } finally {
                    resetStatusMessage();
                }
            }
        };
        t.start();
        ProgressDialog.doModal();
    }

    void sync() {
        beginProgressTask();
        setStatusMessage("Syncing...");
        this.myListView.setEmptyString("Sync in progress...", DrawStyle.HCENTER);
        ProgressDialog.prepareModal("Connecting to server...");
        Thread t = new Thread() {

            public void run() {
                try {
                    WifiMusicSyncProperties props = WifiMusicSyncProperties.fetch();
                    Vector playlists = Subscriber.findPlaylists(props.getLocalStoreRoot());
                    if (playlists != null) {
                        for (int i = 0; i < playlists.size(); i++) {
                            String playlistName = (String) playlists.elementAt(i);
                            String playlist = props.getLocalStoreRoot() + playlistName;
                            ProgressDialog.closeProgress();
                            setStatusMessage("Syncing " + PlaylistInfo.getFriendlyPlaylistName(playlistName) + "...");
                            PlaylistDownloader downloader = new PlaylistDownloader(props.getServerUrl(), playlist, props.getLocalStoreRoot(), props.getClientId());
                            SyncResponse response = downloader.getResponse();
                            if (response != null) {
                                final SyncAction[] actions = response.getActions();
                                UiApplication.getUiApplication().invokeLater(new Runnable() {

                                    public void run() {
                                        WifiMusicSyncScreen.this.myListModel.erase();
                                        for (int i = 0; i < actions.length; i++) {
                                            WifiMusicSyncScreen.this.myListModel.insert(actions[i]);
                                        }
                                    }
                                });
                                downloader.handleResponse();
                            } else {
                                setStatusMessage("Error Syncing " + playlistName);
                            }
                        }
                        setStatusMessage("Sync Complete");
                    } else setStatusMessage("No playlists found");
                } catch (Throwable e) {
                    if (Debug.DEBUG) setStatusMessage(e.toString() + e.getMessage()); else setStatusMessage("Critical error. Sync failed.");
                    ProgressDialog.closeProgress();
                } finally {
                    resetStatusMessage();
                }
            }
        };
        t.start();
        ProgressDialog.doModal();
    }

    void resetStatusMessage() {
        UiApplication.getUiApplication().invokeLater(new Runnable() {

            public void run() {
                myListView.setEmptyString("Choose 'Sync' to start syncing.", DrawStyle.HCENTER);
            }
        });
        TimerTask task = new TimerTask() {

            public void run() {
                UiApplication.getUiApplication().invokeLater(new Runnable() {

                    public void run() {
                        setStatusMessage("");
                        myListModel.erase();
                    }
                });
            }
        };
        Timer timer = new Timer();
        timer.schedule(task, 10000);
        endProgressTask();
    }

    void beginProgressTask() {
        if (UiApplication.getUiApplication().isEventThread()) {
            statusContainer.deleteAll();
            statusContainer.add(view);
            statusContainer.add(statusLabel);
            return;
        }
        UiApplication.getUiApplication().invokeLater(new Runnable() {

            public void run() {
                statusContainer.deleteAll();
                statusContainer.add(view);
                statusContainer.add(statusLabel);
            }
        });
    }

    void endProgressTask() {
        if (UiApplication.getUiApplication().isEventThread()) {
            statusContainer.deleteAll();
            statusContainer.add(statusLabel);
            return;
        }
        UiApplication.getUiApplication().invokeLater(new Runnable() {

            public void run() {
                statusContainer.deleteAll();
                statusContainer.add(statusLabel);
            }
        });
    }

    void setStatusMessage(final String status) {
        if (UiApplication.getUiApplication().isEventThread()) {
            statusLabel.setText(status);
            return;
        }
        UiApplication.getUiApplication().invokeLater(new Runnable() {

            public void run() {
                statusLabel.setText(status);
            }
        });
    }

    private void checkPermissions() {
        ApplicationPermissionsManager apm = ApplicationPermissionsManager.getInstance();
        ApplicationPermissions original = apm.getApplicationPermissions();
        MusicSyncPermissionReasonProvider drp = new MusicSyncPermissionReasonProvider();
        apm.addReasonProvider(ApplicationDescriptor.currentApplicationDescriptor(), drp);
        if (original.getPermission(ApplicationPermissions.PERMISSION_FILE_API) == ApplicationPermissions.VALUE_ALLOW && original.getPermission(ApplicationPermissions.PERMISSION_INTERNET) == ApplicationPermissions.VALUE_ALLOW && original.getPermission(ApplicationPermissions.PERMISSION_WIFI) == ApplicationPermissions.VALUE_ALLOW && original.getPermission(ApplicationPermissions.PERMISSION_DEVICE_SETTINGS) == ApplicationPermissions.VALUE_ALLOW && original.getPermission(ApplicationPermissions.PERMISSION_CROSS_APPLICATION_COMMUNICATION) == ApplicationPermissions.VALUE_ALLOW) {
            return;
        }
        ApplicationPermissions permRequest = new ApplicationPermissions();
        permRequest.addPermission(ApplicationPermissions.PERMISSION_FILE_API);
        permRequest.addPermission(ApplicationPermissions.PERMISSION_INTERNET);
        permRequest.addPermission(ApplicationPermissions.PERMISSION_WIFI);
        permRequest.addPermission(ApplicationPermissions.PERMISSION_DEVICE_SETTINGS);
        permRequest.addPermission(ApplicationPermissions.PERMISSION_CROSS_APPLICATION_COMMUNICATION);
        boolean acceptance = ApplicationPermissionsManager.getInstance().invokePermissionsRequest(permRequest);
        if (acceptance) {
            return;
        } else {
        }
    }
}
