package ufrj.safcp.demo.fingerprint;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import ufrj.safcp.Resources;

public class TelaInicial extends JFrame {

    JComboBox comboNames;

    JLabel lblImagem;

    JButton btnUserMgt;

    JButton btnClose;

    private ControleLeitor fingerprintControle;

    private JPanel panelLeitor;

    private JLabel imgDigital = new JLabel("");

    private JLabel imgSinal = new JLabel("");

    private int i = 0;

    private URL paths[] = { Resources.class.getResource("tools/fingerprint/figuras/sinalVermelho.PNG"), Resources.class.getResource("tools/fingerprint/figuras/sinalVerde.PNG") };

    public TelaInicial() {
        Container container = getContentPane();
        fingerprintControle = new ControleLeitor(this);
        panelLeitor = new JPanel();
        panelLeitor.setLayout(new GridLayout(1, 2));
        panelLeitor.add(imgDigital);
        panelLeitor.add(imgSinal);
        container.add(panelLeitor, BorderLayout.WEST);
        setSize(800, 600);
        setTitle("Sistema Anti-Fraude em Concurso Público");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent e) {
                fingerprintControle.destroy();
                System.exit(0);
            }
        });
        setVisible(true);
    }

    public void showImage(Image img) {
        ImageIcon icon = new ImageIcon(img);
        imgDigital.setIcon(icon);
        imgDigital.repaint();
    }

    public void showSinal(boolean digitalValida) {
        ImageIcon icon;
        if (!digitalValida) {
            icon = new ImageIcon(paths[0]);
            imgSinal.setIcon(icon);
            imgSinal.repaint();
            i++;
        } else {
            icon = new ImageIcon(paths[1]);
            imgSinal.setIcon(icon);
            imgSinal.repaint();
            i = 0;
            try {
                Thread.sleep(5000);
                imgDigital.setIcon(new ImageIcon());
                imgDigital.repaint();
                imgSinal.setIcon(new ImageIcon());
                imgSinal.repaint();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
