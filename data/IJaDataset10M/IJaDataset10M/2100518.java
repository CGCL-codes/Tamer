package game.trainers.pso;

import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

class Okynko extends JFrame implements MouseListener {

    public Okynko(int dimenzi, double velikost_dimenzi, Ptak[] data) {
        try {
            jbInit(dimenzi, velikost_dimenzi, data);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void jbInit(int dimenzi, double velikost_dimenzi, Ptak[] data) throws Exception {
        int gX, gY;
        setTitle("Particle Swarm Optimization");
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        gY = dimenzi - 1;
        gX = 0;
        for (int i = 1; i < dimenzi; i++) gX += i;
        gX = (gX / gY);
        getContentPane().setLayout(new GridLayout(gX, gY));
        for (int x = 1; x < dimenzi; x++) for (int y = x + 1; y <= dimenzi; y++) {
            ShowComponent s = new ShowComponent(data, x - 1, y - 1, 150, velikost_dimenzi);
            getContentPane().add(s).addMouseListener(this);
        }
        pack();
        setLocationRelativeTo(null);
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
        ShowComponent s = (ShowComponent) e.getSource();
        SubOkynko so = new SubOkynko(s.X, s.Y, s.velikost_dimenzi, s.data);
        so.setVisible(true);
    }

    public void mouseReleased(MouseEvent e) {
    }
}
