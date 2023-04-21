package de.fhhannover.inform.wari.grp8.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import de.fhhannover.inform.studienklassen.semester3.GeneralStatics;
import de.fhhannover.inform.wari.grp8.spiel.Logger;

public class Gui extends JFrame implements ActionListener {

    private static final long serialVersionUID = 1L;

    Container con;

    Gui_Controller mGuiCon;

    static final int MULDEN_ANZAHL = 6;

    static final int GEWINNMULDEN_IMG_ANZAHL = 36;

    static final int MULDEN_IMG_ANZAHL = 36;

    private static String statusMsg = " :: Klicken Sie auf 'Spiel' um ein neues Spiel zu starten";

    JButton[] mulde_A_btns = new JButton[MULDEN_ANZAHL];

    JButton[] mulde_B_btns = new JButton[MULDEN_ANZAHL];

    ImageIcon[] gewinnmulden_imgs = new ImageIcon[GEWINNMULDEN_IMG_ANZAHL];

    ImageIcon[] gewinnmulden_imgs_help = new ImageIcon[2];

    ImageIcon[] mulden_imgs = new ImageIcon[MULDEN_IMG_ANZAHL];

    ImageIcon[] mulden_imgs_A_help = new ImageIcon[MULDEN_ANZAHL];

    ImageIcon[] mulden_imgs_B_help = new ImageIcon[MULDEN_ANZAHL];

    ImageIcon turn_img = new ImageIcon();

    JLabel[] mulde_A_label = new JLabel[MULDEN_ANZAHL];

    JLabel[] mulde_B_label = new JLabel[MULDEN_ANZAHL];

    JLabel spielerA = new JLabel();

    JLabel spielerB = new JLabel();

    JLabel turnimg_lbl = new JLabel();

    JButton log_lbl;

    JButton gewinn_mulde_A_btn;

    JButton gewinn_mulde_B_btn;

    JLabel gewinn_mulde_A_lbl;

    JLabel gewinn_mulde_B_lbl;

    JPanel north_pnl;

    JPanel south_pnl;

    JTextField statusleiste;

    int[] buf_array = new int[MULDEN_ANZAHL * 2];

    Logger mylog;

    private int time;

    public Gui(Gui_Controller guicon, String title, int time_in) {
        super(title);
        time = time_in;
        mGuiCon = guicon;
        con = this.getContentPane();
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        con.setLayout(new BorderLayout());
        this.setSize(806, 420);
        this.setLocation(170, 100);
        this.setResizable(false);
        north_pnl = new JPanel();
        north_pnl.setPreferredSize(new Dimension(810, 350));
        north_pnl.setLayout(null);
        north_pnl.setBackground(mGuiCon.getColor());
        south_pnl = new JPanel();
        log_lbl = new JButton("Logs");
        log_lbl.setBounds(720, 15, 80, 20);
        log_lbl.setBackground(mGuiCon.getColor());
        log_lbl.addActionListener(this);
        north_pnl.add(log_lbl);
        statusleiste = new JTextField(statusMsg, 73);
        statusleiste.setFont(new Font("Arial", 1, 12));
        statusleiste.setEditable(false);
        south_pnl.setPreferredSize(new Dimension(810, 25));
        south_pnl.add(statusleiste);
        cacheGewinnmuldenImgs();
        cacheMuldenImgs();
        initialiseImgs();
        createAndArrangeBoard();
        setTurnImg(true);
        setSpielerA("Spieler A");
        setSpielerB("Spieler B");
        con.add(north_pnl, BorderLayout.NORTH);
        con.add(south_pnl, BorderLayout.SOUTH);
    }

    public void setMenu(Menu menu) {
        this.setJMenuBar(menu.getMenuBar());
    }

    public void updatePits(int[] curPits) {
        buf_array = curPits;
        for (int i = 0; i < MULDEN_ANZAHL; i++) {
            mulden_imgs_A_help[i] = mulden_imgs[buf_array[i]];
            mulde_A_btns[i].setIcon(mulden_imgs_A_help[i]);
            mulde_A_btns[i].setDisabledIcon(mulden_imgs_A_help[i]);
        }
        int z = 5;
        for (int i = 11; i >= (MULDEN_ANZAHL); i--, z--) {
            mulden_imgs_B_help[z] = mulden_imgs[buf_array[i]];
            mulde_B_btns[z].setIcon(mulden_imgs_B_help[z]);
            mulde_B_btns[z].setDisabledIcon(mulden_imgs_B_help[z]);
        }
    }

    public void updatePitsA(int[] curPits) {
        buf_array = curPits;
        for (int i = 0; i < MULDEN_ANZAHL; i++) {
            mulden_imgs_A_help[i] = mulden_imgs[buf_array[i]];
            mulde_A_btns[i].setIcon(mulden_imgs_A_help[i]);
            mulde_A_btns[i].setDisabledIcon(mulden_imgs_A_help[i]);
            mulde_A_label[i].setText("" + buf_array[i]);
            sleep();
        }
    }

    public void updatePitsB(int[] curPits) {
        buf_array = curPits;
        for (int i = 5; i >= 0; i--) {
            mulden_imgs_B_help[i] = mulden_imgs[buf_array[i]];
            mulde_B_btns[i].setIcon(mulden_imgs_B_help[i]);
            mulde_B_btns[i].setDisabledIcon(mulden_imgs_B_help[i]);
            mulde_B_label[i].setText("" + buf_array[i]);
            sleep();
        }
    }

    public void updatePit(int i, int buf_array) {
        if (i < 6) {
            mulden_imgs_A_help[i] = mulden_imgs[buf_array];
            mulde_A_btns[i].setIcon(mulden_imgs_A_help[i]);
            mulde_A_btns[i].setDisabledIcon(mulden_imgs_A_help[i]);
            mulde_A_label[i].setText("" + buf_array);
        } else {
            i = (6 - i) + 5;
            mulden_imgs_B_help[i] = mulden_imgs[buf_array];
            mulde_B_btns[i].setIcon(mulden_imgs_B_help[i]);
            mulde_B_btns[i].setDisabledIcon(mulden_imgs_B_help[i]);
            mulde_B_label[i].setText("" + buf_array);
        }
        sleep();
    }

    protected void sleep() {
        if (time != 0) {
            try {
                Thread.sleep(time);
                paint(getGraphics());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    protected void updateGewinnMulden(int[] GewinnMulden) {
        sleep();
        this.gewinnmulden_imgs_help[0] = gewinnmulden_imgs[GewinnMulden[0]];
        this.gewinn_mulde_A_btn.setDisabledIcon(gewinnmulden_imgs_help[0]);
        this.gewinnmulden_imgs_help[1] = gewinnmulden_imgs[GewinnMulden[1]];
        this.gewinn_mulde_B_btn.setDisabledIcon(gewinnmulden_imgs_help[1]);
    }

    public void initialiseImgs() {
        for (int i = 0; i < MULDEN_ANZAHL; i++) {
            mulden_imgs_A_help[i] = mulden_imgs[0];
            mulden_imgs_B_help[i] = mulden_imgs[0];
        }
        gewinnmulden_imgs_help[0] = gewinnmulden_imgs[0];
        gewinnmulden_imgs_help[1] = gewinnmulden_imgs[0];
    }

    public void createAndArrangeBoard() {
        createMuldenA(mulden_imgs_A_help);
        createMuldenB(mulden_imgs_B_help);
        createAndArrangeGewinnMulden(gewinnmulden_imgs_help);
    }

    public void createMuldenA(ImageIcon[] imgs) {
        for (int index = 0; index < MULDEN_ANZAHL; index++) {
            mulde_A_btns[index] = new JButton("");
            mulde_A_btns[index].setBounds(100 * (index + 1), 200, 100, 100);
            mulde_A_btns[index].addActionListener(this);
            mulde_A_label[index] = new JLabel("0");
            mulde_A_label[index].setBounds(100 * (index + 1) + 50, 298, 20, 20);
            mulde_A_btns[index].setIcon(imgs[index]);
            mulde_A_btns[index].setDisabledIcon(imgs[index]);
            mulde_A_btns[index].setEnabled(false);
            north_pnl.add(mulde_A_btns[index]);
            north_pnl.add(mulde_A_label[index]);
        }
    }

    public void createMuldenB(ImageIcon[] imgs) {
        for (int index = 0; index < MULDEN_ANZAHL; index++) {
            mulde_B_btns[index] = new JButton("");
            mulde_B_btns[index].setBounds(100 * (index + 1), 100, 100, 100);
            mulde_B_btns[index].addActionListener(this);
            mulde_B_label[index] = new JLabel("0");
            mulde_B_label[index].setBounds(100 * (index + 1) + 50, 80, 20, 20);
            mulde_B_btns[index].setDisabledIcon(imgs[index]);
            mulde_B_btns[index].setEnabled(false);
            mulde_B_btns[index].setIcon(imgs[index]);
            north_pnl.add(mulde_B_btns[index]);
            north_pnl.add(mulde_B_label[index]);
        }
    }

    public void createAndArrangeGewinnMulden(ImageIcon[] imgs) {
        gewinn_mulde_A_btn = new JButton("");
        gewinn_mulde_A_btn.setBounds(700, 100, 100, 200);
        gewinn_mulde_A_lbl = new JLabel("0");
        gewinn_mulde_A_lbl.setBounds(750, 80, 20, 20);
        gewinn_mulde_A_btn.setEnabled(false);
        gewinn_mulde_A_btn.setDisabledIcon(imgs[0]);
        gewinn_mulde_A_btn.setIcon(imgs[0]);
        north_pnl.add(gewinn_mulde_A_btn);
        north_pnl.add(gewinn_mulde_A_lbl);
        gewinn_mulde_B_btn = new JButton("");
        gewinn_mulde_B_btn.setBounds(0, 100, 100, 200);
        gewinn_mulde_B_lbl = new JLabel("0");
        gewinn_mulde_B_lbl.setBounds(50, 80, 20, 20);
        gewinn_mulde_B_btn.setEnabled(false);
        gewinn_mulde_B_btn.setDisabledIcon(imgs[1]);
        gewinn_mulde_B_btn.setIcon(imgs[1]);
        north_pnl.add(gewinn_mulde_B_btn);
        north_pnl.add(gewinn_mulde_B_lbl);
    }

    public void cacheGewinnmuldenImgs() {
        for (int index = 0; index < GEWINNMULDEN_IMG_ANZAHL; index++) {
            gewinnmulden_imgs[index] = GeneralStatics.createImageIcon("/images/gewinn_mulde_" + Integer.toString(index) + ".jpg", "Gewinnmulde", this);
        }
    }

    public void cacheMuldenImgs() {
        for (int index = 0; index < MULDEN_IMG_ANZAHL; index++) {
            mulden_imgs[index] = GeneralStatics.createImageIcon("/images/wari_mulde_" + Integer.toString(index) + ".jpg", "Mulde", this);
        }
    }

    protected void activateBoard(boolean seiteA, boolean seiteB) {
        for (int i = 0; i < MULDEN_ANZAHL; i++) {
            if (seiteA && !seiteB) {
                mulde_A_btns[i].setEnabled(true);
                mulde_B_btns[i].setEnabled(false);
            }
            if (seiteB && !seiteA) {
                mulde_A_btns[i].setEnabled(false);
                mulde_B_btns[i].setEnabled(true);
            }
            if (seiteB && seiteA) {
                mulde_A_btns[i].setEnabled(true);
                mulde_B_btns[i].setEnabled(true);
            }
            if (!seiteB && !seiteA) {
                mulde_A_btns[i].setEnabled(false);
                mulde_B_btns[i].setEnabled(false);
            }
        }
    }

    protected void deactivateBoard(boolean seite, int muldeIndex) {
        if (seite) mulde_A_btns[muldeIndex].setEnabled(false); else mulde_B_btns[muldeIndex].setEnabled(false);
    }

    protected void deactivateBoard(boolean seite, int[] curPits) {
        for (int i = 0; i < MULDEN_ANZAHL; i++) {
            if (seite && curPits[i] == 0) mulde_A_btns[i].setEnabled(false);
            if (!seite && curPits[i] == 0) mulde_B_btns[i].setEnabled(false);
        }
    }

    public void setSpielerA(String name) {
        spielerA.setText(name);
        spielerA.setBounds(380, 320, 250, 20);
        spielerA.setForeground(new Color(21, 143, 53));
        spielerA.setFont(new Font("Arial", 1, 14));
        north_pnl.add(spielerA);
    }

    public void setSpielerB(String name) {
        spielerB.setText(name);
        spielerB.setBounds(380, 50, 250, 20);
        spielerB.setForeground(new Color(21, 143, 53));
        spielerB.setFont(new Font("Arial", 1, 14));
        north_pnl.add(spielerB);
    }

    public void setTurnImg(boolean seite) {
        turn_img = GeneralStatics.createImageIcon("/images/turnarray.jpg", "Turn_Img", this);
        turnimg_lbl.setIcon(turn_img);
        if (seite) turnimg_lbl.setBounds(330, 318, 35, 25); else turnimg_lbl.setBounds(330, 48, 35, 25);
        north_pnl.add(turnimg_lbl);
    }

    protected void setStatusMsg(String msg) {
        this.statusleiste.setText(msg);
    }

    protected void setMuldenLabel(int[] curPits) {
        buf_array = curPits;
        int z = 0;
        for (int i = 0; i < MULDEN_ANZAHL; i++) {
            mulde_A_label[i].setText("" + buf_array[i]);
        }
        for (int i = 6; i < (MULDEN_ANZAHL * 2); i++) {
            mulde_B_label[z].setText("" + buf_array[i]);
            z++;
        }
    }

    protected void setGewinnMuldenLabel(int[] gmulden) {
        gewinn_mulde_A_lbl.setText("" + gmulden[0]);
        gewinn_mulde_B_lbl.setText("" + gmulden[1]);
    }

    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        for (int i = 0; i < MULDEN_ANZAHL; i++) {
            if (source == mulde_A_btns[i]) {
                mGuiCon.forwardMove(i);
            }
            if (source == mulde_B_btns[i]) {
                mGuiCon.forwardMove(i);
            }
        }
        if (source == log_lbl) {
            try {
                mGuiCon.showLogger();
            } catch (Exception ex) {
            }
        }
    }
}
