package jrdesktop.viewer.main;

import jrdesktop.viewer.main.FileMng.FileManager;
import jrdesktop.viewer.main.FileMng.FileTransGui;
import java.awt.Rectangle;
import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;
import javax.swing.ImageIcon;
import jrdesktop.HostProperties;
import jrdesktop.ConnectionInfos;
import jrdesktop.viewer.rmi.Viewer;
import jrdesktop.utilities.ClipbrdUtility;
import jrdesktop.utilities.InetAdrUtility;

/**
 * Recorder.java
 * @author benbac
 */
public class Recorder extends Thread {

    private boolean recording = false;

    private boolean viewOnly = false;

    private boolean pause = false;

    public Viewer viewer;

    public ViewerGUI viewerGUI;

    public ScreenPlayer screenPlayer;

    public ClipbrdUtility clipbrdUtility;

    public ViewerData viewerData;

    public FileManager fileManager;

    public ConnectionInfos connectionInfos;

    public Recorder(Viewer viewer) {
        this.viewer = viewer;
        start();
        clipbrdUtility = new ClipbrdUtility();
        fileManager = new FileManager(this);
        connectionInfos = new ConnectionInfos(false);
        viewerData = new ViewerData(InetAdrUtility.getLocalAdr());
        screenPlayer = new ScreenPlayer(this);
        viewerGUI = new ViewerGUI(this);
    }

    @Override
    public void run() {
        while (true) {
            Wait();
            while (recording && !pause) {
                viewer.sendData();
                viewer.recieveData();
            }
        }
    }

    public void Wait() {
        try {
            synchronized (this) {
                wait();
            }
        } catch (Exception e) {
            e.getStackTrace();
        }
    }

    public void Notify() {
        try {
            synchronized (this) {
                notify();
            }
        } catch (Exception e) {
            e.getStackTrace();
        }
    }

    public void Stop() {
        recording = false;
        pause = true;
        viewOnly = false;
        clipbrdUtility.removeFlavorListener();
        screenPlayer.removeAdapters();
        screenPlayer.clearScreen();
        if (viewerGUI.isFullScreenMode()) viewerGUI.changeFullScreenMode();
        viewerData.setScreenRect(new Rectangle(0, 0, 0, 0));
        viewer.disconnect();
    }

    public void Start() {
        if (!viewer.isConnected()) if (viewer.connect() == -1) return;
        viewer.updateOptions();
        connectionInfos.init();
        clipbrdUtility.addFlavorListener();
        screenPlayer.addAdapters();
        recording = true;
        pause = false;
        viewOnly = false;
        Notify();
    }

    public boolean isRecording() {
        return recording;
    }

    public boolean isPaused() {
        return pause;
    }

    public void setPause(boolean bool) {
        pause = bool;
        if (pause) screenPlayer.removeAdapters(); else {
            if (recording && !viewOnly) screenPlayer.addAdapters();
            if (recording) Notify();
        }
    }

    public void setViewOnly(boolean bool) {
        viewOnly = bool;
        if (viewOnly) screenPlayer.removeAdapters(); else {
            if (recording && !pause) screenPlayer.addAdapters();
        }
    }

    public boolean isViewOnly() {
        return viewOnly;
    }

    public ArrayList<Object> getViewerData() {
        ArrayList<Object> data = new ArrayList<Object>();
        data.add(viewerData.getScreenScale());
        data.add(viewerData.getScreenRect());
        data.add(viewerData.getCompressionLevel());
        data.add(viewerData.isDataCompressionEnabled());
        data.add(viewerData.getCompressionQuality());
        data.add(viewerData.getColorQuality());
        data.add(viewerData.isClipboardTransferEnabled());
        data.add(viewerData.getInetAddress());
        return data;
    }

    public void updataData(ArrayList objects) {
        for (int i = 0; i < objects.size(); i++) {
            Object obj = objects.get(i);
            if (obj instanceof Rectangle) viewerData.setScreenRect((Rectangle) obj); else if (obj instanceof byte[]) screenPlayer.UpdateScreen((byte[]) obj); else if (obj instanceof String) clipbrdUtility.setTextToClipboard((String) obj); else if (obj instanceof ImageIcon) clipbrdUtility.setImageToClipboard((ImageIcon) obj); else if (obj instanceof File) {
                File[] files = fileManager.getFiles();
                if (files.length == 0) return;
                new FileTransGui(this).SendFiles(files);
            } else if (obj instanceof ArrayList) new FileTransGui(this).ReceiveFiles((ArrayList) obj); else if (obj instanceof Hashtable) HostProperties.displayRemoteProperties((Hashtable) obj);
        }
    }
}
