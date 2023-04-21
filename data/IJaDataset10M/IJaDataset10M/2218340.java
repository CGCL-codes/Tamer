package client.presentation.main;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

class ViewAbout extends JDialog implements IViewAboutConstants {

    private class CloseButtonHandler implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            ViewAbout.this.dispose();
        }
    }

    ;

    private JLabel lblName;

    private JLabel lblVersion;

    private JLabel lblCopyright;

    private JButton btnClose;

    private JPanel pnlInfo;

    private JPanel pnlButtons;

    private final int WIDTH = 600;

    private final int HEIGHT = 700;

    private void setupFrame() {
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setTitle(TITLE);
        setSize(WIDTH, HEIGHT);
        Dimension screenDim = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((screenDim.width - WIDTH) / 2, (screenDim.height - HEIGHT) / 2);
        this.getContentPane().setLayout(new BorderLayout());
        this.setResizable(false);
        this.setModal(true);
        this.setupPanels();
        this.setupButtons();
        this.pack();
    }

    private void setupPanels() {
        this.pnlInfo = new JPanel();
        this.pnlButtons = new JPanel();
        JLabel lblLogo = new JLabel();
        lblLogo.setIcon(new ImageIcon("resources/client/images/client/hotelbuster-logo.png"));
        pnlInfo.add(lblLogo);
        this.pnlInfo.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        this.getContentPane().add(this.pnlInfo, BorderLayout.CENTER);
        this.getContentPane().add(this.pnlButtons, BorderLayout.SOUTH);
    }

    private void setupButtons() {
        this.btnClose = new JButton(CLOSE_BUTTON);
        this.pnlButtons.add(this.btnClose, null);
        this.btnClose.addActionListener(new CloseButtonHandler());
    }

    public ViewAbout(JFrame owner) {
        super(owner);
        this.setupFrame();
    }
}

;
