package it.f2.gestRip.ui;

import it.f2.gestRip.control.CommonMetodBin;
import it.f2.gestRip.control.CommonMetodBin.CheckStatus;
import it.f2.gestRip.ui.messages.Messages;
import it.f2.util.ui.WindowUtil;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

public class VcPnlStatusBar extends JPanel {

    private static final long serialVersionUID = 1L;

    private JButton btnUpgrade = null;

    private Frame parent;

    public VcPnlStatusBar(Frame parent) {
        this.parent = parent;
        initialize();
    }

    private void initialize() {
        this.setSize(300, 16);
        this.setLayout(new BorderLayout());
        add(getBtnUpgrade(), BorderLayout.EAST);
    }

    /**
	 * TODO Comment for method "getBtnUpgrade" must be completed
	 * @author Fabrizio Ferraiuolo 03/nov/2010 15.15.16
	 * @return 
	 */
    private JButton getBtnUpgrade() {
        if (btnUpgrade == null) {
            btnUpgrade = new JButton();
            btnUpgrade.setPreferredSize(new Dimension(22, 22));
            if (CommonMetodBin.getInstance().getStatusUpdate().equals(CheckStatus.NEW_UPDATE)) {
                btnUpgrade.setIcon(new ImageIcon(getClass().getResource("/it/f2/gestRip/ui/img/software-update-available.png")));
                btnUpgrade.setToolTipText(Messages.getString("VcPnlStatusBar.tltNewVersion"));
            } else {
                btnUpgrade.setIcon(new ImageIcon(getClass().getResource("/it/f2/gestRip/ui/img/system-software-update.png")));
                btnUpgrade.setToolTipText(Messages.getString("VcPnlStatusBar.tltCheckUpdate"));
            }
            btnUpgrade.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    VcDlgCheckUpdate dialog = new VcDlgCheckUpdate(parent);
                    WindowUtil.centerWindow(dialog);
                    dialog.setVisible(true);
                }
            });
        }
        return btnUpgrade;
    }
}
