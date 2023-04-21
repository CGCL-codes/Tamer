package objectif.lyon.designer.gui.test;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import objectif.lyon.designer.gui.DesignerPane;
import objectif.lyon.designer.gui.resources.ResourceManager;

public class Test {

    /**
     * @param args
     * @throws IOException 
     */
    public static void main(String[] args) throws IOException {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        final JDialog dialog = new JDialog();
        dialog.setUndecorated(true);
        dialog.setModal(true);
        final JLabel image = new JLabel();
        image.setIcon(new ImageIcon(ResourceManager.getResource("logo objectif lyon.png")));
        image.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        image.addMouseListener(new MouseAdapter() {

            private boolean pressed = false;

            @Override
            public void mouseEntered(MouseEvent e) {
                image.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                image.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }

            @Override
            public void mousePressed(MouseEvent e) {
                pressed = true;
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (pressed) {
                    dialog.setVisible(false);
                }
            }
        });
        dialog.add(image);
        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
        JFrame f = new JFrame();
        f.setIconImage(new ImageIcon(ResourceManager.getResource("logo_fenetre.png")).getImage());
        f.setTitle("Designer de plateau");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.add(new DesignerPane());
        f.pack();
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }
}
