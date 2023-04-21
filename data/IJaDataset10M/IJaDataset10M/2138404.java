package chapt05.spot;

import java.awt.event.*;
import javax.media.opengl.GLProfile;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.awt.GLCanvas;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class SpotSwingGLCanvas implements ActionListener {

    private static GLCanvas glcanvas;

    static {
        GLProfile.initSingleton(false);
    }

    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    private static void createAndShowGUI() {
        GLProfile glprofile = GLProfile.getDefault();
        GLCapabilities glcapabilities = new GLCapabilities(glprofile);
        glcanvas = new GLCanvas(glcapabilities);
        glcanvas.addGLEventListener(new SpotGLEventListener());
        glcanvas.addKeyListener(new SpotKeyListener(glcanvas));
        final JFrame jframe = new JFrame("Triangle Culling Example");
        jframe.addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent windowevent) {
                jframe.dispose();
                System.exit(0);
            }
        });
        SpotSwingGLCanvas demo = new SpotSwingGLCanvas();
        demo.createPopupMenu(glcanvas);
        jframe.getContentPane().add(glcanvas, BorderLayout.CENTER);
        jframe.setSize(600, 400);
        jframe.setVisible(true);
    }

    public void createPopupMenu(GLCanvas glcanvas) {
        JMenuItem menuItem;
        JPopupMenu popup = new JPopupMenu();
        menuItem = new JMenuItem("Flat Shading");
        menuItem.addActionListener(this);
        popup.add(menuItem);
        menuItem = new JMenuItem("Smooth Shading");
        menuItem.addActionListener(this);
        popup.add(menuItem);
        menuItem = new JMenuItem("VL Tess");
        menuItem.addActionListener(this);
        popup.add(menuItem);
        menuItem = new JMenuItem("MD Tess");
        menuItem.addActionListener(this);
        popup.add(menuItem);
        menuItem = new JMenuItem("VH Tess");
        menuItem.addActionListener(this);
        popup.add(menuItem);
        MouseListener popupListener = new SpotPopupListener(popup);
        glcanvas.addMouseListener(popupListener);
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                createAndShowGUI();
            }
        });
    }

    public void actionPerformed(ActionEvent e) {
        JMenuItem source = (JMenuItem) (e.getSource());
        String text = source.getText();
        if (text == "Flat Shading") Spot.processMenu(1); else if (text == "Smooth Shading") Spot.processMenu(2); else if (text == "VL Tess") Spot.processMenu(3); else if (text == "MD Tess") Spot.processMenu(4); else if (text == "VH Tess") Spot.processMenu(5);
        glcanvas.display();
    }
}
