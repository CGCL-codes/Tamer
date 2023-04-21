package org.hardtokenmgmt.admin.ui.panels.editres;

import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import org.hardtokenmgmt.admin.control.AdminInterfacesFactory;
import org.hardtokenmgmt.admin.ui.AdminUIUtils;
import org.hardtokenmgmt.common.vo.ResourceDataVO;
import org.hardtokenmgmt.core.log.LocalLog;
import org.hardtokenmgmt.core.ui.UIHelper;
import org.hardtokenmgmt.ws.gen.AuthorizationDeniedException_Exception;
import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

/**
 * Add resource manually dialog
 * 
 * 
 * @author Philip Vendil 15 mar 2009
 *
 * @version $Id$
 */
public class UploadResourceDialog extends JDialog {

    private Dimension DIALOG_SIZE = new Dimension(600, 235);

    private List<ResourceDataVO> resources;

    private static final long serialVersionUID = 1L;

    private JLabel statusLabel;

    private JLabel numberLabel;

    private UploadThread uploadThread;

    public UploadResourceDialog() {
        super();
        initComponents();
    }

    public UploadResourceDialog(List<ResourceDataVO> resources, Window owner) {
        super(owner, ModalityType.APPLICATION_MODAL);
        this.resources = resources;
        initComponents();
    }

    private void initComponents() {
        setSize(DIALOG_SIZE);
        getContentPane().setLayout(new FormLayout(new ColumnSpec[] { ColumnSpec.decode("11dlu"), ColumnSpec.decode("69dlu"), ColumnSpec.decode("11dlu"), ColumnSpec.decode("74dlu"), FormFactory.RELATED_GAP_COLSPEC, ColumnSpec.decode("16dlu"), FormFactory.RELATED_GAP_COLSPEC, ColumnSpec.decode("61dlu"), FormFactory.RELATED_GAP_COLSPEC, ColumnSpec.decode("42dlu") }, new RowSpec[] { RowSpec.decode("11dlu"), FormFactory.DEFAULT_ROWSPEC, FormFactory.UNRELATED_GAP_ROWSPEC, RowSpec.decode("12dlu"), RowSpec.decode("14dlu"), FormFactory.DEFAULT_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.DEFAULT_ROWSPEC }));
        setTitle(UIHelper.getText("editres.uploadingfiles"));
        setIconImage(AdminUIUtils.getAdminWindowIcon());
        final JButton cancelButton = new JButton();
        cancelButton.addActionListener(new ActionListener() {

            public void actionPerformed(final ActionEvent e) {
                uploadThread.stop();
                dispose();
            }
        });
        cancelButton.setText(UIHelper.getText("cancel"));
        cancelButton.setIcon(UIHelper.getImage("disable.png"));
        getContentPane().add(cancelButton, new CellConstraints(6, 8, 3, 1, CellConstraints.LEFT, CellConstraints.DEFAULT));
        final JLabel logoLabel = new JLabel();
        logoLabel.setIcon(UIHelper.getImage("add_resource.gif"));
        getContentPane().add(logoLabel, new CellConstraints(2, 2));
        final JLabel titleLabel = new JLabel();
        titleLabel.setText(UIHelper.getText("editres.uploadingfiles"));
        titleLabel.setFont(UIHelper.getTitleFont());
        getContentPane().add(titleLabel, new CellConstraints(4, 2, 7, 1, CellConstraints.LEFT, CellConstraints.DEFAULT));
        statusLabel = new JLabel();
        getContentPane().add(statusLabel, new CellConstraints(2, 4, 9, 1, CellConstraints.LEFT, CellConstraints.DEFAULT));
        numberLabel = new JLabel();
        getContentPane().add(numberLabel, new CellConstraints(2, 5, 9, 1, CellConstraints.LEFT, CellConstraints.DEFAULT));
        uploadThread = new UploadThread(resources);
        Thread t = new Thread(uploadThread);
        t.start();
    }

    public static void main(String[] args) {
        UIHelper.setTheme();
        List<ResourceDataVO> resources = new ArrayList<ResourceDataVO>();
        resources.add(new ResourceDataVO("test", null, null, null, null, null, "asfdasfdasfd".getBytes()));
        resources.add(new ResourceDataVO("test2filnamnaganska", null, null, null, null, null, "asfdasfdaasdfasdfsfd".getBytes()));
        UploadResourceDialog d = new UploadResourceDialog(resources, null);
        d.setVisible(true);
    }

    private class UploadThread implements Runnable {

        private boolean run = true;

        private List<ResourceDataVO> resources;

        private UploadThread(List<ResourceDataVO> resources) {
            this.resources = resources;
        }

        public void run() {
            int index = 0;
            for (final ResourceDataVO res : resources) {
                if (!run) {
                    break;
                } else {
                    try {
                        ArrayList<ResourceDataVO> upLoadResources = new ArrayList<ResourceDataVO>();
                        upLoadResources.add(res);
                        final int i = ++index;
                        SwingUtilities.invokeLater(new Runnable() {

                            public void run() {
                                statusLabel.setText(UIHelper.getText("editres.statusmsg1") + ": " + res.getName() + ", " + UIHelper.getText("editres.statusmsg2") + " " + AdminUIUtils.getReadableDataSizes(res.getData().length));
                                numberLabel.setText(UIHelper.getText("editres.numbermsg1") + " " + i + " " + UIHelper.getText("editres.numbermsg2") + " " + resources.size());
                            }
                        });
                        AdminInterfacesFactory.getResourceManager().setResources(upLoadResources);
                    } catch (final IOException e1) {
                        LocalLog.getLogger().log(Level.SEVERE, "Error adding resources:", e1);
                        SwingUtilities.invokeLater(new Runnable() {

                            public void run() {
                                statusLabel.setText(UIHelper.getText("editadmins.error") + " " + e1.getClass().getSimpleName() + ", " + e1.getMessage());
                            }
                        });
                    } catch (final AuthorizationDeniedException_Exception e1) {
                        LocalLog.getLogger().log(Level.SEVERE, "Error adding resources:", e1);
                        SwingUtilities.invokeLater(new Runnable() {

                            public void run() {
                                statusLabel.setText(UIHelper.getText("editadmins.error") + " " + e1.getClass().getSimpleName() + ", " + e1.getMessage());
                            }
                        });
                    }
                }
            }
            dispose();
        }

        public void stop() {
            run = false;
        }
    }

    @Override
    protected void finalize() throws Throwable {
        if (uploadThread != null) {
            uploadThread.stop();
        }
    }
}
