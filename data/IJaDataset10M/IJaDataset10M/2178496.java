package barrywei.igosyncdocs.action;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.JOptionPane;
import com.google.gdata.data.acl.AclEntry;
import com.google.gdata.data.acl.AclRole;
import com.google.gdata.util.ServiceException;
import barrywei.igosyncdocs.biz.IGoSyncDocsBiz;
import barrywei.igosyncdocs.biz.impl.SyncDocsException;
import barrywei.igosyncdocs.factory.AbstractFactory;
import barrywei.igosyncdocs.gui.FaceRunner;
import barrywei.igosyncdocs.gui.ShareWithEmailDialog;
import barrywei.igosyncdocs.gui.SplashDialogProgress;
import barrywei.igosyncdocs.gui.model.IGoSyncDocsRemoteViewTableItem;

/**
 * 
 *
 *
 * @author BarryWei
 * @version 1.0, Aug 19, 2010
 * @since JDK1.6
 */
public class MakeAsReaderWriterAction implements ActionListener, Runnable {

    private ShareWithEmailDialog dialog;

    private IGoSyncDocsRemoteViewTableItem item;

    private int type;

    private IGoSyncDocsBiz biz;

    private SplashDialogProgress frame = new SplashDialogProgress();

    /**
	 * 
	 * @param dialog
	 * @param item
	 * @param type 1 reader 0 writer -1 owner
	 */
    public MakeAsReaderWriterAction(ShareWithEmailDialog dialog, IGoSyncDocsRemoteViewTableItem item, int type) {
        try {
            this.dialog = dialog;
            biz = AbstractFactory.createSyncDocsBizObject();
            this.item = item;
            this.type = type;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "IGoSyncDocs Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void actionPerformed(ActionEvent e) {
        new Thread(this).start();
    }

    public void run() {
        dialog.setVisible(false);
        FaceRunner.run(frame, new Dimension(450, 300), "iGoSyncDocs Logining...", true);
        frame.startProgress();
        frame.setAlwaysOnTop(true);
        try {
            if (type == 0) {
                AclEntry entry = dialog.getSelectedAclEntry();
                entry.setRole(new AclRole("writer"));
                entry.update();
            } else if (type == 1) {
                AclEntry entry = dialog.getSelectedAclEntry();
                entry.setRole(new AclRole("reader"));
                entry.update();
            } else {
                AclEntry entry = dialog.getSelectedAclEntry();
                entry.setRole(new AclRole("owner"));
                entry.update();
            }
            this.dialog.initNeededData();
        } catch (SyncDocsException e) {
            JOptionPane.showMessageDialog(null, "Server Response:" + e.getMessage(), "iGoSyncDocs Error", JOptionPane.ERROR_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Server Response:" + e.getMessage(), "iGoSyncDocs Error", JOptionPane.ERROR_MESSAGE);
        } catch (ServiceException e) {
            JOptionPane.showMessageDialog(null, "Server Response:" + e.getMessage(), "iGoSyncDocs Error", JOptionPane.ERROR_MESSAGE);
        }
        frame.setVisible(false);
        dialog.setVisible(true);
        frame.dispose();
    }
}
