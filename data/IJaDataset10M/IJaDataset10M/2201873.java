package addons;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.UIManager.LookAndFeelInfo;

public final class JLookAndFeelChooser extends JDialog implements ActionListener {

    private static final LookAndFeelInfo[] LIST_L_AND_F = UIManager.getInstalledLookAndFeels();

    private Frame frame;

    private JButton boutonAnnuler = null;

    private JButton boutonOK = null;

    private JComboBox comboLandF = null;

    private JPanel jContentPane = null;

    private JPanel panelB = null;

    private JPanel panelH = null;

    public JLookAndFeelChooser(Frame frame) {
        super(frame, true);
        this.frame = frame;
        this.setTitle("Choisissez le Look & Feel...");
        this.setSize(200, 110);
        this.setLocationRelativeTo(getParent());
        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        this.setContentPane(getJContentPane());
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        Object source = ae.getSource();
        if (source == boutonOK) {
            closeAndApply();
        } else if (source == boutonAnnuler) {
            close();
        }
    }

    private JButton getBoutonAnnuler() {
        if (boutonAnnuler == null) {
            boutonAnnuler = new JButton("Annuler");
            boutonAnnuler.addActionListener(this);
        }
        return boutonAnnuler;
    }

    private JButton getBoutonOK() {
        if (boutonOK == null) {
            boutonOK = new JButton("OK");
            boutonOK.addActionListener(this);
        }
        return boutonOK;
    }

    private JComboBox getComboLandF() {
        if (comboLandF == null) {
            String[] names = new String[LIST_L_AND_F.length];
            for (byte b = 0; b < LIST_L_AND_F.length; b++) {
                names[b] = LIST_L_AND_F[b].getName();
            }
            comboLandF = new JComboBox(names);
            comboLandF.setSelectedItem(UIManager.getLookAndFeel().getName());
        }
        return comboLandF;
    }

    private JPanel getJContentPane() {
        if (jContentPane == null) {
            jContentPane = new JPanel();
            jContentPane.setLayout(new GridLayout(2, 1));
            jContentPane.add(getPanelH());
            jContentPane.add(getPanelB());
        }
        return jContentPane;
    }

    private JPanel getPanelB() {
        if (panelB == null) {
            panelB = new JPanel();
            panelB.setLayout(new FlowLayout());
            panelB.add(getBoutonOK());
            panelB.add(getBoutonAnnuler());
        }
        return panelB;
    }

    private JPanel getPanelH() {
        if (panelH == null) {
            panelH = new JPanel();
            panelH.setLayout(new FlowLayout());
            panelH.add(getComboLandF());
        }
        return panelH;
    }

    private void close() {
        this.setVisible(false);
        this.dispose();
    }

    private void closeAndApply() {
        if (frame != null) {
            byte b = (byte) comboLandF.getSelectedIndex();
            String lookAndFeel = LIST_L_AND_F[b].getClassName();
            try {
                UIManager.setLookAndFeel(lookAndFeel);
                SwingUtilities.invokeLater(new Runnable() {

                    public void run() {
                        SwingUtilities.updateComponentTreeUI(frame);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        close();
    }
}
