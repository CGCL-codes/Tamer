package dplayer.gui.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import dplayer.Settings;
import dplayer.gui.commands.ExpandDirectoryCommand;
import dplayer.gui.commands.RefreshAllCommand;
import dplayer.gui.commands.RefreshDirectoryCommand;
import dplayer.gui.commands.ScanDirectoryCommand;
import dplayer.gui.commands.ScanFileCommand;
import dplayer.queue.QueueItem;
import dplayer.queue.commands.CommandListener;
import dplayer.queue.commands.CommandManager;
import dplayer.queue.commands.ScannerCommand;
import dplayer.queue.events.EventManager;
import dplayer.scanner.Directory;
import dplayer.scanner.Location;
import dplayer.scanner.Scanner;
import dplayer.scanner.ScannerUtils;
import dplayer.scanner.events.LocationListChangedEvent;

/**
 * Controller of scanner.
 */
final class ScannerController {

    private static final Logger logger = Logger.getLogger(ScannerController.class);

    private Scanner mScanner;

    private List<Location> mLocationList;

    ScannerController() {
        mScanner = new Scanner();
        mLocationList = new ArrayList<Location>();
        CommandManager.addListener(ScannerCommand.class, new ScannerCommandListener());
        File[] roots;
        if (Settings.ROOTS.getString().equals("")) {
            roots = File.listRoots();
        } else {
            final String[] sa = Settings.ROOTS.getString().split("[,]");
            roots = new File[sa.length];
            for (int i = 0; i < sa.length; i++) {
                roots[i] = new File(sa[i].trim());
            }
        }
        for (final File root : roots) {
            if (root.listFiles() != null) {
                final String absolutePath = ScannerUtils.getAbsolutePath(root);
                logger.info("Adding root " + absolutePath);
                addLocation(new Location(root.getPath(), absolutePath));
            }
        }
    }

    private synchronized void addLocation(final Location location) {
        mLocationList.add(location);
        EventManager.send(new LocationListChangedEvent(mLocationList));
        mScanner.scanDirectory(location.getDirectory(), Settings.getScannerPolicy() == Scanner.Policy.AGGRESSIVE, true);
    }

    private final class ScannerCommandListener extends CommandListener {

        private ScannerCommandListener() {
            super("ScannerController(ScannerCommand)");
        }

        /** {@inheritDoc} */
        public void receive(final QueueItem command) {
            if (command instanceof ExpandDirectoryCommand) {
                final ExpandDirectoryCommand edc = (ExpandDirectoryCommand) command;
                mScanner.scanDirectory(edc.getDirectory(), false, true);
            } else if (command instanceof RefreshDirectoryCommand) {
                final RefreshDirectoryCommand rdc = (RefreshDirectoryCommand) command;
                mScanner.scanDirectory(rdc.getDirectory(), true, false);
            } else if (command instanceof RefreshAllCommand) {
                synchronized (mLocationList) {
                    for (final Location l : mLocationList) {
                        mScanner.scanDirectory(l.getDirectory(), true, false);
                    }
                }
            } else if (command instanceof ScanDirectoryCommand) {
                final ScanDirectoryCommand sdc = (ScanDirectoryCommand) command;
                sdc.getDirectory().setDefaultSelectFlat(sdc.isFlat());
                mScanner.scanDirectory(sdc.getDirectory(), sdc.isFlat(), true);
            } else if (command instanceof ScanFileCommand) {
                final ScanFileCommand sfc = (ScanFileCommand) command;
                final String path;
                if (sfc.getFile().isFile()) {
                    path = ScannerUtils.getParent(ScannerUtils.getAbsolutePath(sfc.getFile()));
                } else {
                    path = ScannerUtils.getAbsolutePath(sfc.getFile());
                }
                final Location location = findLocation(path);
                if (location != null) {
                    final Directory directory = findDirectory(path);
                    if (directory != null) {
                        mScanner.scanDirectory(directory, false, true);
                    } else {
                        mScanner.preferDirectory(path);
                        mScanner.scanDirectory(location.getDirectory(), false, false);
                    }
                }
            }
        }

        private Location findLocation(final String absolutePath) {
            assert absolutePath != null && absolutePath.length() > 0;
            for (final Location l : mLocationList) {
                if (absolutePath.startsWith(l.getDirectory().getAbsolutePath())) {
                    return l;
                }
            }
            return null;
        }

        private Directory findDirectory(final String absolutePath) {
            assert absolutePath != null && absolutePath.length() > 0;
            for (final Location l : mLocationList) {
                if (l.getDirectory().getAbsolutePath().equals(absolutePath)) {
                    return l.getDirectory();
                }
                final Directory found = findDirectory(l.getDirectory().getDirectoryList(), absolutePath);
                if (found != null) {
                    return found;
                }
            }
            return null;
        }

        private Directory findDirectory(final List<Directory> list, final String absolutePath) {
            assert list != null;
            assert absolutePath != null && absolutePath.length() > 0;
            for (final Directory d : list) {
                if (d.getAbsolutePath().equals(absolutePath)) {
                    return d;
                }
                final Directory found = findDirectory(d.getDirectoryList(), absolutePath);
                if (found != null) {
                    return found;
                }
            }
            return null;
        }
    }
}
