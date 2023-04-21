package start.gui;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import jaxb.messages.Folder;
import jaxb.messages.Resource;
import start.StrongBoxClient;
import virtualHD.VirtualHDException;

public class BoxWorker extends SwingWorker<Void, Void> {

    public static final int DOWNLOAD = 0;

    public static final int UPLOAD = 1;

    private StrongBoxClient sbc;

    private BoxPanel parent;

    private int mode;

    private Folder folder;

    private Resource res;

    public BoxWorker(StrongBoxClient sbc, BoxPanel parent) {
        this.sbc = sbc;
        this.parent = parent;
        this.mode = BoxWorker.DOWNLOAD;
    }

    public void setFolder(Folder f) {
        this.folder = f;
    }

    public void setResource(Resource r) {
        this.res = r;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    @Override
    protected Void doInBackground() {
        System.out.println("entra");
        parent.getProgressBar().setIndeterminate(true);
        try {
            if (mode == DOWNLOAD) sbc.get(folder, res); else if (mode == UPLOAD) sbc.put(folder, res);
        } catch (VirtualHDException ex) {
            parent.showPopup(res.getName(), ex.getMessage(), JOptionPane.ERROR_MESSAGE);
        } finally {
            parent.getProgressBar().setIndeterminate(false);
        }
        return null;
    }

    @Override
    protected void done() {
        parent.getProgressBar().setIndeterminate(false);
        if (mode == DOWNLOAD) parent.showPopup("INFO", "Download of " + res.getName() + " completed", JOptionPane.INFORMATION_MESSAGE); else if (mode == UPLOAD) {
            parent.showPopup("INFO", "Upload completed!", JOptionPane.INFORMATION_MESSAGE);
            if (parent.getCurrentFolder().equals(folder.getUserid())) {
                parent.addSingleResource2Table(res);
            }
        }
    }
}
