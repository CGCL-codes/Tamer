package Ldb;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

/**
 *
 * @author pain
 */
public class Listener {

    public static String path;

    static String new_nodenm;

    public static ActionListener aksi = new Action();

    public static TreeSelectionListener listen_tree = new Listen();

    static TreePath tp;

    public static class Listen implements TreeSelectionListener {

        public void valueChanged(TreeSelectionEvent e) {
            tp = e.getNewLeadSelectionPath();
            String pth = Sertdir.map1.get(tp.getLastPathComponent().toString());
            DefaultMutableTreeNode dmtn = (DefaultMutableTreeNode) tp.getLastPathComponent();
            if (pth.contains(".txt")) {
                Main.area1.setEnabled(true);
                path = pth;
                Main.area1.selectAll();
                Main.area1.replaceSelection(Methode.takeString(path));
            } else {
                Main.area1.setEnabled(false);
            }
        }
    }

    public static class Action implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            Object obj1 = e.getSource();
            if (obj1 == Main.btn1) {
                try {
                    BufferedWriter out = new BufferedWriter(new FileWriter(path));
                    out.write(Main.area1.getText());
                    out.close();
                } catch (Exception ex) {
                    Errorhand err = new Errorhand(null, ex.getMessage().toString());
                }
            } else if (obj1 == Methode.btn2) {
            }
        }
    }

    public static class Mice implements MouseListener {

        public void mouseClicked(MouseEvent e) {
        }

        public void mousePressed(MouseEvent e) {
            if (e.getButton() == 3 && e.getSource() == Main.tree1) {
                System.err.println("button " + e.getButton());
                DefaultMutableTreeNode dmtn = (DefaultMutableTreeNode) tp.getLastPathComponent();
                if (dmtn.getLevel() != 3) {
                    Main.popmn1.setVisible(false);
                } else {
                    Main.popmn1.setVisible(true);
                }
                Main.pop1.show(e.getComponent(), e.getX(), e.getY());
            } else if (e.getButton() == 3 && e.getSource() == Main.area1) {
                Main.pop2.show(e.getComponent(), e.getX(), e.getY());
            } else if (e.getSource() == Main.popmn1) {
                Main.pop1.setVisible(false);
                try {
                    Methode.newLirics(Methode.showNew(Main.frm1, "Lirics"));
                } catch (IOException ex) {
                    new Errorhand(null, ex.getMessage().toString());
                }
            } else if (e.getSource() == Main.nuArtist) {
                Methode.newArtist(Methode.showNew(Main.frm1, "Artist"));
            } else if (e.getSource() == Main.nuAlbums) {
                Methode.newAlbums(Methode.showNew(Main.frm1, "Albums"));
            } else if (e.getSource() == Main.nuCustom) {
                Methode.newCustoms(Methode.showNew(Main.frm1, "Custom"));
            } else if (e.getSource() == Main.mnuAbout) {
                Methode.showAbout();
            } else if (e.getSource() == Main.popmn2) {
                Main.pop1.setVisible(false);
                Methode.delNode();
            } else if (e.getSource() == Main.popcopy || e.getSource() == Main.mn_copy) {
                Main.area1.copy();
            } else if (e.getSource() == Main.poppaste || e.getSource() == Main.mn_paste) {
                Main.area1.paste();
            }
        }

        public void mouseReleased(MouseEvent e) {
        }

        public void mouseEntered(MouseEvent e) {
        }

        public void mouseExited(MouseEvent e) {
        }
    }
}
