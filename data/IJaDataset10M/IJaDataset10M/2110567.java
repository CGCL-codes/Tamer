package net.flysource.client.network;

import net.flysource.client.FlyShareApp;
import net.flysource.client.gui.flyeditor.FlyPatternModel;
import net.flysource.client.util.FSConfig;
import net.flysource.client.util.Toolbox;
import net.flysource.common.LibraryEntry;
import net.flysource.common.exceptions.NotConnectedException;
import fi.mmm.yhteinen.swing.core.YApplicationEvent;
import fi.mmm.yhteinen.swing.core.YController;
import fi.mmm.yhteinen.swing.core.worker.SwingWorker;
import org.apache.log4j.Logger;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class DirectorySyncRunnable implements Runnable {

    public static final int SYNC_INTERVAL = 5000;

    public static final int SYNC_ENTRIES_SIZE = 100;

    private Logger LOG = Logger.getLogger(DirectorySyncRunnable.class);

    private String sessionId;

    private ArrayList removeList;

    public DirectorySyncRunnable() {
        sessionId = Toolbox.getConnectionManager().getSessionId();
        removeList = new ArrayList();
    }

    public void run() {
        while (true) {
            try {
                Thread.sleep(SYNC_INTERVAL);
            } catch (InterruptedException e) {
                return;
            }
            if (!Toolbox.getConnectionManager().isConnected()) continue;
            Collection library = Toolbox.getLibraryController().getLibraryModel().getPatterns();
            Iterator chgIter = library.iterator();
            while (chgIter.hasNext()) {
                FlyPatternModel fly = (FlyPatternModel) chgIter.next();
                if (!fly.isSentToServer() && fly.isChangeSync()) {
                    addRemoveEntry(fly.getOriginalFilename());
                    LOG.info("Remove " + fly.getFilename());
                }
            }
            if (removeList.size() > 0) {
                String[] files = new String[removeList.size()];
                for (int i = 0; i < removeList.size(); i++) {
                    files[i] = (String) removeList.get(i);
                }
                removeList.clear();
                try {
                    Toolbox.getConnectionManager().removeEntries(sessionId, files);
                } catch (NotConnectedException e) {
                    SwingWorker worker = new SwingWorker() {

                        public Object construct() {
                            YController.sendApplicationEvent(new YApplicationEvent(FlyShareApp.SERVER_CONNECT_LOST, null));
                            return null;
                        }
                    };
                    worker.start();
                    return;
                }
            }
            int numSent = 0;
            ArrayList sendList = new ArrayList();
            Iterator iter = library.iterator();
            while (iter.hasNext()) {
                FlyPatternModel fly = (FlyPatternModel) iter.next();
                if (!fly.isSentToServer() && fly.isShareable()) {
                    fly.setSentToServer(true);
                    fly.setChangeSync(false);
                    File file = new File(FSConfig.getFlyStore() + File.separator + fly.getFilename());
                    sendList.add(new LibraryEntry(fly.getName(), fly.getType(), fly.getOriginator(), fly.getFilename(), fly.genKeywords(), file.length(), fly.isHasImage(), fly.getCrc(), fly.getTier()));
                    if (numSent++ >= SYNC_ENTRIES_SIZE - 1) break;
                }
            }
            if (numSent > 0) {
                LibraryEntry[] entries = new LibraryEntry[sendList.size()];
                for (int i = 0; i < sendList.size(); i++) {
                    entries[i] = (LibraryEntry) sendList.get(i);
                }
                try {
                    Toolbox.getConnectionManager().sendLibrary(sessionId, entries);
                } catch (NotConnectedException e) {
                    SwingWorker worker = new SwingWorker() {

                        public Object construct() {
                            YController.sendApplicationEvent(new YApplicationEvent(FlyShareApp.SERVER_CONNECT_LOST, null));
                            return null;
                        }
                    };
                    worker.start();
                    return;
                }
            }
        }
    }

    protected void addRemoveEntry(String filename) {
        removeList.add(filename);
    }
}
