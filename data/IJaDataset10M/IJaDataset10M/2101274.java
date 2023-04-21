package view;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import javax.swing.*;

public class PhotomosaicMain extends JFrame {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private Controller ctrl = null;

    private JPanel jContentPane = null;

    private ImageView imageView = null;

    private WindowAdapter windowClosingListener = null;

    public PhotomosaicMain() {
        super("Computación Gráfica - TPE1 - Segmentación");
        ctrl = new Controller(this);
        setSize(640, 480);
        setMinimumSize(new Dimension(300, 100));
        setJMenuBar(new MenuBar(ctrl));
        setContentPane(getJContentPane());
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        windowClosingListener = new WindowAdapter() {

            public void windowClosing(WindowEvent e) {
                JFrame f = (JFrame) e.getSource();
                int r = JOptionPane.showConfirmDialog(f, "¿Está seguro que desea salir?", "Confirmar salida", JOptionPane.YES_NO_OPTION);
                if (r == JOptionPane.OK_OPTION) {
                    f.dispose();
                    System.exit(0);
                }
            }
        };
        addWindowListener(windowClosingListener);
    }

    public void quit() {
        windowClosingListener.windowClosing(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
    }

    public Controller getController() {
        return ctrl;
    }

    private JPanel getJContentPane() {
        if (jContentPane == null) {
            jContentPane = new JPanel();
            jContentPane.setLayout(new GridBagLayout());
            GridBagConstraints c = new GridBagConstraints();
            c.fill = GridBagConstraints.HORIZONTAL;
            c.weightx = 1.0;
            c.gridwidth = GridBagConstraints.REMAINDER;
            c.gridheight = 1;
            jContentPane.add(new ToolBar(ctrl), c);
            c.fill = GridBagConstraints.BOTH;
            c.weighty = 1.0;
            c.gridwidth = GridBagConstraints.REMAINDER;
            c.gridheight = GridBagConstraints.REMAINDER;
            jContentPane.add(getImageView(), c);
        }
        return jContentPane;
    }

    public ImageView getImageView() {
        if (imageView == null) {
            imageView = new ImageView();
            imageView.setVisible(true);
        }
        return imageView;
    }

    public void showInformationDialog(String title, String message) {
        JOptionPane.showMessageDialog(getContentPane(), message, title, JOptionPane.INFORMATION_MESSAGE);
    }

    public void showErrorDialog(String title, String message) {
        JOptionPane.showMessageDialog(getContentPane(), message, title, JOptionPane.ERROR_MESSAGE);
    }

    public JFileChooser getFileChooser() {
        JFileChooser chooser = new JFileChooser();
        return chooser;
    }

    public File getFileFromFileChooser() {
        JFileChooser chooser = getFileChooser();
        int ret = chooser.showOpenDialog(getJContentPane());
        if (ret == JFileChooser.APPROVE_OPTION) {
            return chooser.getSelectedFile();
        }
        return null;
    }

    public File saveFileWithFileChooser() {
        JFileChooser chooser = getFileChooser();
        int ret = chooser.showSaveDialog(getJContentPane());
        if (ret == JFileChooser.APPROVE_OPTION) {
            return chooser.getSelectedFile();
        }
        return null;
    }

    public static void main(String[] args) {
        PhotomosaicMain w = new PhotomosaicMain();
        w.setVisible(true);
        w.validate();
    }
}
