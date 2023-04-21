package jfigure.commons;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import jfigure.commons.mediator.Mediator;
import jfigure.graphics2D.Painter2D;

/**
 * Fen�tre de l'afficheur
 *
 * @author  Cali
 */
public class ScreenPainterFrame extends JDialog {

    /**
     * Pour l'afficheur
     **/
    private Painter painter;

    private Mediator mediator;

    private Dimension oldPainterSize = new Dimension();

    private Dimension oldScaleSize = new Dimension();

    /**
     * Cr�ation d'une fen�tre pour acceuillir un Afficheur (<code>Painter</code>)
     */
    public ScreenPainterFrame(Mediator mediator) {
        this.mediator = mediator;
        this.setUndecorated(true);
        this.setLocation(250, 0);
        mediator.setScreenPainterFrame(this);
    }

    /**
     * Initialisation avec un afficheur (<code>Painter</code>)
     **/
    public final void init(Painter painter) {
        this.setTitle(mediator.getGuiLabel("NOM_AFFICHEUR_PLAN"));
        mediator.setComponentAttributes(this);
        this.painter = painter;
        this.oldPainterSize = this.painter.getSize();
        this.oldScaleSize = ((Painter2D) this.painter).getScale().getSize();
        JPanel centre = new JPanel();
        centre.setBackground(this.painter.getBackground());
        centre.setLayout(null);
        centre.add(this.painter);
        this.getContentPane().removeAll();
        this.getContentPane().setLayout(new BorderLayout());
        this.getContentPane().add(centre, BorderLayout.CENTER);
        JPanel pxx = (JPanel) this.getContentPane();
        pxx.setBorder(new LineBorder(mediator.getGuiConfiguration().getForeground(), 4));
        this.setResizable(false);
        this.setFocusable(true);
        this.pack();
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screen = toolkit.getScreenSize();
        this.painter.setSize((int) screen.getHeight(), (int) screen.getHeight() - 40);
        ((Painter2D) this.painter).getScale().setSize(new Dimension((int) screen.getHeight() - 80, (int) screen.getHeight() - 80));
        this.setSize((int) screen.getHeight(), (int) screen.getHeight());
        final Painter p = this.painter;
        mediator.getPainterFrame().setVisible(false);
        this.addWindowListener(new WindowListener() {

            public void windowActivated(WindowEvent arg0) {
            }

            public void windowClosed(WindowEvent arg0) {
            }

            public void windowClosing(WindowEvent arg0) {
                close();
            }

            public void windowDeactivated(WindowEvent arg0) {
            }

            public void windowDeiconified(WindowEvent arg0) {
            }

            public void windowIconified(WindowEvent arg0) {
            }

            public void windowOpened(WindowEvent arg0) {
            }
        });
        pxx.addMouseListener(new MouseAdapter() {

            public void mouseClicked(MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    close();
                }
            }
        });
    }

    /**
     * Fermeture de la fen�tre
     */
    public final void close() {
        painter.setSize(oldPainterSize);
        ((Painter2D) painter).getScale().setSize(oldScaleSize);
        setVisible(false);
        Dimension d = mediator.getPainterFrame().getSize();
        mediator.getPainterFrame().init(painter);
        mediator.getPainterFrame().setSize(d);
        mediator.getPainterFrame().revalidate();
        mediator.getPainterFrame().repaint();
        mediator.getPainterFrame().setVisible(true);
    }

    /**
     * Retourne l'afficheur associ� � cette fen�tre
     **/
    public final Painter getPainter() {
        return this.painter;
    }
}
