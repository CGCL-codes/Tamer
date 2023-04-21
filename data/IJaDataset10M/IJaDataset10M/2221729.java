package a2;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class Gui extends JFrame {

    private static final long serialVersionUID = 1651853808205806251L;

    private JTextArea textarea = new JTextArea();

    private JPanel mainpanel, leftpanel, menu;

    private ArrayList<JLabel> rastefeldList;

    private JLabel tmpLabel;

    private ImageIcon icon1;

    private Actions actions = new Actions(textarea);

    Gui() {
        this.setTitle("Go <---Schwarz--->");
        this.setBackground(new Color(238, 219, 189));
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        this.setSize(800, 418);
        mainpanel = new JPanel();
        mainpanel.setLayout(new BorderLayout());
        mainpanel.add(BorderLayout.NORTH, new JLabel(new ImageIcon(ImageHandler.topbarImagePath), JLabel.LEFT));
        mainpanel.add(BorderLayout.SOUTH, new JLabel(new ImageIcon(ImageHandler.bottombarImagePath), JLabel.LEFT));
        mainpanel.add(BorderLayout.CENTER, new JLabel(new ImageIcon(ImageHandler.centerbarImagePath), JLabel.LEFT));
        menu = new JPanel();
        menu.setLayout(new BoxLayout(menu, BoxLayout.Y_AXIS));
        JButton undo = new JButton("                          Undo                        ");
        undo.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                if (actions.unDoPossible()) {
                    actions.gridArray = actions.unDo(rastefeldList);
                    if (actions.getWerIstDran() == 0) actions.setWerIstDran(1); else actions.setWerIstDran(0);
                }
                if (actions.getWerIstDran() == 0) setTitle("Go <---Schwarz--->"); else if (actions.getWerIstDran() == 1) setTitle("Go <---Wei�--->");
            }
        });
        menu.add(undo);
        actions.wurdeGepasst = false;
        JButton passen = new JButton("                       Passen                      ");
        passen.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                if (actions.wurdeGepasst) {
                    System.out.print("\n\n\n\n\n\n\nBeide Spieler haben gepasst\nGewonnen hat ");
                    textarea.setText("Beide Spieler haben gepasst\nGewonnen hat ");
                    if (actions.wgesamtscore > actions.bgesamtscore) {
                        System.out.println("wei�");
                        textarea.setText(textarea.getText() + "Wei�\n");
                    }
                    if (actions.wgesamtscore < actions.bgesamtscore) {
                        System.out.println("schwarz");
                        textarea.setText(textarea.getText() + "Schwarz\n");
                    }
                    if (actions.wgesamtscore == actions.bgesamtscore) {
                        System.out.println("keiner. Es ist Gleichstand");
                        textarea.setText(textarea.getText() + "keiner. Es ist Gleichstand\n");
                    }
                    textarea.setText(textarea.getText() + "\nSchwarz hat " + Actions.getGefangeneBlack() + " Gefangene\n" + actions.bpoints + " Felder eingenommen\nSCORE:" + actions.bgesamtscore);
                    textarea.setText(textarea.getText() + "\nWei� hat " + Actions.getGefangeneWhite() + " Gefangene\n" + actions.wpoints + " Felder eingenommen\nSCORE:" + actions.wgesamtscore);
                } else {
                    actions.wurdeGepasst = true;
                    if (actions.getWerIstDran() == 0) {
                        System.out.println("Schwarz hat gepasst");
                        textarea.setText("  Schwarz hat gepasst\n Gefangene von Wei�+1" + textarea.getText() + "\n\n\n\n\n\n\n\n\n\n\n\n\n");
                        actions.setWerIstDran(1);
                        Actions.gefangeneWhite++;
                    } else if (actions.getWerIstDran() == 1) {
                        System.out.println("Wei� hat gepasst");
                        textarea.setText("  Wei� hat gepasst\n Gefangene von Schwarz+1" + textarea.getText() + "\n\n\n\n\n\n\n\n\n\n\n\n\n");
                        actions.setWerIstDran(0);
                        Actions.gefangeneBlack++;
                    }
                    actions.analyse(actions.gridArray, rastefeldList, actions.getWerIstDran() + 1);
                }
                if (actions.getWerIstDran() == 0) setTitle("Go <---Schwarz--->"); else if (actions.getWerIstDran() == 1) setTitle("Go <---Wei�--->");
            }
        });
        menu.add(passen);
        menu.add(textarea);
        mainpanel.add(BorderLayout.EAST, menu);
        leftpanel = new JPanel();
        mainpanel.add(BorderLayout.WEST, leftpanel);
        this.getContentPane().add(mainpanel);
        leftpanel.setLayout(new GridLayout(9, 9));
        rastefeldList = new ArrayList<JLabel>();
        for (int i = 0; i < 81; i++) {
            icon1 = new ImageIcon(ImageHandler.icon1path);
            icon1.setDescription("clear");
            tmpLabel = new JLabel(icon1);
            tmpLabel.setName(String.valueOf(i));
            tmpLabel.addMouseListener(new MouseListener() {

                public void mouseClicked(MouseEvent arg0) {
                    if (actions.wurdeGepasst) actions.wurdeGepasst = false;
                    JLabel source = (JLabel) arg0.getSource();
                    if (actions.gridArray[(Integer.parseInt(source.getName()) / 9)][(Integer.parseInt(source.getName()) % 9)] == 0) {
                        actions.steinsetzen(source, actions.gridArray, rastefeldList);
                    }
                    if (actions.getWerIstDran() == 0) setTitle("Go <---Schwarz--->"); else if (actions.getWerIstDran() == 1) setTitle("Go <---Wei�--->");
                }

                public void mouseEntered(MouseEvent arg0) {
                    JLabel source = (JLabel) arg0.getSource();
                    if (((ImageIcon) source.getIcon()).getDescription() == "clear") {
                        source.setIcon(new ImageIcon(ImageHandler.rotateImagePath));
                        ((ImageIcon) source.getIcon()).setDescription("rotate");
                    }
                }

                public void mouseExited(MouseEvent arg0) {
                    JLabel source = (JLabel) arg0.getSource();
                    if (((ImageIcon) source.getIcon()).getDescription() == "rotate") {
                        source.setIcon(new ImageIcon(ImageHandler.icon1path));
                        ((ImageIcon) source.getIcon()).setDescription("clear");
                    }
                }

                public void mousePressed(MouseEvent arg0) {
                }

                @Override
                public void mouseReleased(MouseEvent arg0) {
                }
            });
            rastefeldList.add(tmpLabel);
            leftpanel.add(tmpLabel);
        }
        actions.saveProgress(actions.gridArray, rastefeldList);
    }

    public static void main(String[] args) {
        Gui g = new Gui();
        g.setVisible(true);
    }
}
