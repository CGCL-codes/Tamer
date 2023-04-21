package it.f2.gestRip.ui;

import it.f2.gestRip.EnvConstants;
import it.f2.gestRip.control.CheckUpdates;
import it.f2.gestRip.control.CommonMetodBin;
import it.f2.gestRip.control.CommonMetodBin.CheckStatus;
import it.f2.gestRip.ui.messages.Messages;
import it.f2.gestRip.util.LinksUtils;
import java.awt.Frame;
import java.awt.HeadlessException;
import java.awt.Rectangle;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.apache.log4j.Logger;

public class VcDlgCheckUpdate extends JDialog {

    private static final long serialVersionUID = 1L;

    private Frame parent = null;

    private JPanel jContentPane = null;

    private JLabel lblName = null;

    private JLabel lblInstalledVersion = null;

    private JLabel lblActualVersion = null;

    private JButton btnOk = null;

    private JLabel lblTxtVersioneInstallata = null;

    private JLabel lblTxtVersioneAttuale = null;

    private JLabel lblMsg = null;

    /**
	 * Costruttore della classe
	 * 
	 * @param parent
	 * @throws HeadlessException
	 */
    public VcDlgCheckUpdate(Frame parent) throws HeadlessException {
        super(parent, true);
        Logger.getRootLogger().debug("VcDlgAbout constructor...");
        this.parent = parent;
        CheckUpdates.check();
        initialize();
    }

    /**
	 * This method initializes this
	 * 
	 * @return void
	 */
    private void initialize() {
        this.setSize(366, 198);
        this.setContentPane(getJContentPane());
    }

    /**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
    private JPanel getJContentPane() {
        if (jContentPane == null) {
            lblMsg = new JLabel();
            lblMsg.setBounds(new Rectangle(13, 119, 210, 23));
            lblMsg.setText(getMsg());
            lblTxtVersioneAttuale = new JLabel();
            lblTxtVersioneAttuale.setBounds(new Rectangle(118, 67, 108, 22));
            lblTxtVersioneAttuale.setText(Messages.getString("VcDlgCheckUpdate.lblActualVersion"));
            lblTxtVersioneInstallata = new JLabel();
            lblTxtVersioneInstallata.setBounds(new Rectangle(118, 33, 116, 22));
            lblTxtVersioneInstallata.setText(Messages.getString("VcDlgCheckUpdate.lblInstalledVersion"));
            lblActualVersion = new JLabel();
            lblActualVersion.setBounds(new Rectangle(237, 67, 112, 22));
            lblActualVersion.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
            lblActualVersion.setText(CommonMetodBin.getInstance().getActualRelease().toString());
            lblInstalledVersion = new JLabel();
            lblInstalledVersion.setBounds(new Rectangle(237, 33, 111, 22));
            lblInstalledVersion.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
            lblInstalledVersion.setText(CommonMetodBin.getInstance().getCurrentRelease().toString());
            lblName = new JLabel();
            lblName.setBounds(new Rectangle(13, 13, 90, 79));
            lblName.setFont(new java.awt.Font("Courier New", java.awt.Font.BOLD, 26));
            lblName.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            lblName.setText("");
            lblName.setIcon(new ImageIcon(getClass().getResource("/it/f2/gestRip/ui/img/logo64.png")));
            jContentPane = new JPanel();
            jContentPane.setLayout(null);
            jContentPane.add(lblName, null);
            jContentPane.add(lblInstalledVersion, null);
            jContentPane.add(lblActualVersion, null);
            jContentPane.add(getBtnOk(), null);
            jContentPane.add(lblTxtVersioneInstallata, null);
            jContentPane.add(lblTxtVersioneAttuale, null);
            jContentPane.add(lblMsg, null);
        }
        return jContentPane;
    }

    /**
	 * This method initializes btnOk
	 * 
	 * @return javax.swing.JButton
	 */
    private JButton getBtnOk() {
        if (btnOk == null) {
            btnOk = new JButton();
            btnOk.setBounds(new Rectangle(228, 114, 103, 32));
            String text = CommonMetodBin.getInstance().getStatusUpdate().equals(CheckStatus.NEW_UPDATE) ? Messages.getString("VcDlgCheckUpdate.btnDownload") : Messages.getString("VcDlgCheckUpdate.btnOk");
            btnOk.setText(text);
            btnOk.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    if (CommonMetodBin.getInstance().getStatusUpdate().equals(CheckStatus.NEW_UPDATE)) LinksUtils.openUrl(parent, EnvConstants.LINK_DOWNLOAD); else close();
                }
            });
        }
        return btnOk;
    }

    private String getMsg() {
        switch(CommonMetodBin.getInstance().getStatusUpdate()) {
            case LAST_UPDATE:
                return Messages.getString("VcDlgCheckUpdate.lblRLInstalled");
            case NEW_UPDATE:
                return Messages.getString("VcDlgCheckUpdate.lblRLNewVersion");
            case NOT_CHECKED:
                return Messages.getString("VcDlgCheckUpdate.lblRLCheckError");
            default:
                break;
        }
        return "";
    }

    /**
	 * Effettua la chiusura della dialog.
	 */
    private void close() {
        this.dispose();
    }
}
