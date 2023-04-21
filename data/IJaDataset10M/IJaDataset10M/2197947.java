package crazyOrb.gui;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JRadioButton;
import javax.swing.JButton;
import crazyOrb.musikplayer.PlayerThread;

/**
 * @author Claudia, Marlene, Ragna-Diana
 */
public class EinstellungenGUI extends JFrame implements ActionListener {

    private static final long serialVersionUID = 1L;

    private JPanel contentPane;

    private ButtonGroup bg = new ButtonGroup();

    private ButtonGroup bg2 = new ButtonGroup();

    private ButtonGroup bg3 = new ButtonGroup();

    private JRadioButton rdbtnRot = new JRadioButton("rot");

    private JRadioButton rdbtnBlau = new JRadioButton("blau");

    private JRadioButton rdbtnGruen = new JRadioButton("grün");

    private JRadioButton rdbtnWiese = new JRadioButton("Rasen");

    private JRadioButton rdbtnWolken = new JRadioButton("Wolken");

    private JRadioButton rdbtnSpace = new JRadioButton("Weltall");

    private JRadioButton rdbtnIntroX = new JRadioButton("IntroX");

    private JRadioButton rdbtnAndariloX = new JRadioButton("AndariloX");

    private JRadioButton rdbtnPecaAPeca = new JRadioButton("Peca a Peca");

    private JButton btnOk = new JButton("OK");

    private SpielGUI gui;

    /**
	 * Create the frame.
	 */
    public EinstellungenGUI(SpielGUI gui) {
        this.gui = gui;
        setTitle("Einstellungen");
        setBounds(100, 100, 378, 350);
        contentPane = new JPanel();
        contentPane.setBackground(new Color(255, 255, 102));
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);
        JPanel panel = new JPanel();
        panel.setBackground(new Color(255, 255, 102));
        panel.setBounds(10, 11, 349, 77);
        contentPane.add(panel);
        JPanel panel_1 = new JPanel();
        panel_1.setBackground(new Color(255, 255, 102));
        panel_1.setBounds(10, 188, 349, 77);
        contentPane.add(panel_1);
        JPanel panel_2 = new JPanel();
        panel_2.setBackground(new Color(255, 255, 102));
        panel_2.setBounds(10, 100, 349, 77);
        contentPane.add(panel_2);
        panel.setBorder(BorderFactory.createTitledBorder("Ballfarbe"));
        panel.setLayout(null);
        rdbtnRot.setSelected(true);
        rdbtnRot.setBackground(null);
        rdbtnRot.setBounds(18, 30, 68, 23);
        panel.add(rdbtnRot);
        rdbtnBlau.setBackground(null);
        rdbtnBlau.setBounds(127, 30, 68, 23);
        panel.add(rdbtnBlau);
        rdbtnGruen.setBackground(null);
        rdbtnGruen.setBounds(238, 30, 68, 23);
        panel.add(rdbtnGruen);
        bg.add(rdbtnRot);
        bg.add(rdbtnBlau);
        bg.add(rdbtnGruen);
        panel_1.setBorder(BorderFactory.createTitledBorder("Hintergrund"));
        panel_1.setLayout(null);
        rdbtnWiese.setSelected(true);
        bg3.add(rdbtnWiese);
        rdbtnWiese.setBackground(null);
        rdbtnWiese.setBounds(18, 30, 74, 23);
        panel_1.add(rdbtnWiese);
        bg3.add(rdbtnWolken);
        rdbtnWolken.setBackground(null);
        rdbtnWolken.setBounds(127, 30, 74, 23);
        panel_1.add(rdbtnWolken);
        bg3.add(rdbtnSpace);
        rdbtnSpace.setBackground(null);
        rdbtnSpace.setBounds(238, 30, 74, 23);
        panel_1.add(rdbtnSpace);
        panel_2.setBorder(BorderFactory.createTitledBorder("Musik"));
        panel_2.setLayout(null);
        rdbtnIntroX.setSelected(true);
        bg2.add(rdbtnIntroX);
        rdbtnIntroX.setBackground(null);
        rdbtnIntroX.setBounds(18, 30, 80, 23);
        panel_2.add(rdbtnIntroX);
        bg2.add(rdbtnAndariloX);
        rdbtnAndariloX.setBackground(null);
        rdbtnAndariloX.setBounds(127, 30, 80, 23);
        panel_2.add(rdbtnAndariloX);
        bg2.add(rdbtnPecaAPeca);
        rdbtnPecaAPeca.setBackground(null);
        rdbtnPecaAPeca.setBounds(238, 30, 105, 23);
        panel_2.add(rdbtnPecaAPeca);
        btnOk.setBounds(137, 276, 89, 23);
        contentPane.add(btnOk);
        btnOk.addActionListener(this);
        setResizable(false);
    }

    public void auswahlHintergrund() {
        ButtonModel selectedModel = bg3.getSelection();
        if (rdbtnWiese.getModel() == selectedModel) {
            PlayCanvas.bild = Bild.RASEN;
        } else if (rdbtnWolken.getModel() == selectedModel) {
            PlayCanvas.bild = Bild.WOLKEN;
        } else if (rdbtnSpace.getModel() == selectedModel) {
            PlayCanvas.bild = Bild.WELTALL;
        }
    }

    public void auswahlLied() {
        ButtonModel selectedModel = bg2.getSelection();
        if (rdbtnIntroX.getModel() == selectedModel) {
            PlayerThread.setLied("IntroX");
        } else if (rdbtnAndariloX.getModel() == selectedModel) {
            PlayerThread.setLied("AndariloX");
        } else if (rdbtnPecaAPeca.getModel() == selectedModel) {
            PlayerThread.setLied("PecaPeca");
        }
    }

    public void auswahlFarbe() {
        ButtonModel selectedModel = bg.getSelection();
        if (rdbtnBlau.getModel() == selectedModel) {
            PlayCanvas.setFarbe(Color.BLUE);
        } else if (rdbtnGruen.getModel() == selectedModel) {
            PlayCanvas.setFarbe(Color.GREEN);
        } else if (rdbtnRot.getModel() == selectedModel) {
            PlayCanvas.setFarbe(Color.RED);
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public void actionPerformed(ActionEvent e) {
        auswahlHintergrund();
        auswahlFarbe();
        if (SpielGUI.getSpiel() == true) {
            Object quelle = e.getSource();
            if (quelle == btnOk) {
                gui.setBtnMusik();
                SpielGUI.musicThread.stop();
                SpielGUI.musicThread = new PlayerThread();
                auswahlLied();
                SpielGUI.musicThread.start();
            }
        }
        SpielGUI.canvas.repaint();
        this.dispose();
    }

    public String getSelectedTrack() {
        if (rdbtnIntroX.isSelected()) return rdbtnIntroX.getText();
        if (rdbtnAndariloX.isSelected()) return rdbtnAndariloX.getText();
        if (rdbtnPecaAPeca.isSelected()) return "PecaPeca";
        return null;
    }
}
