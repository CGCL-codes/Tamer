package sun.awt.X11;

import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FilenameFilter;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Test for FileDialog
 * 
 * @author Costantino Cerbo (c.cerbo@gmail.com)
 */
public class FileDialogTest {

    private JFrame frame;

    private JTextField tf;

    void testFast() {
        FileDialog fd = new FileDialog((JFrame) null);
        fd.setVisible(true);
        System.out.println("Peer: " + fd.getPeer().getClass().getName());
        System.out.println("dir: " + fd.getDirectory());
        System.out.println("file: " + fd.getFile());
        System.out.println("visible: " + fd.isVisible());
        fd.dispose();
    }

    void testButton() {
        frame = new JFrame();
        tf = new JTextField(30);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.LINE_AXIS));
        tf.setMaximumSize(new Dimension(Short.MAX_VALUE, tf.getPreferredSize().height));
        mainPanel.add(tf);
        mainPanel.add(Box.createRigidArea(new Dimension(5, 0)));
        JButton openButton = new JButton("Open...");
        openButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                openFileDialog(FileDialog.LOAD);
            }
        });
        JButton saveButton = new JButton("Save...");
        saveButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                openFileDialog(FileDialog.SAVE);
            }
        });
        mainPanel.add(openButton);
        mainPanel.add(Box.createRigidArea(new Dimension(5, 0)));
        mainPanel.add(saveButton);
        frame.getContentPane().add(mainPanel);
        frame.pack();
        frame.setVisible(true);
    }

    FilenameFilter createTextFileFilter() {
        return new FilenameFilter() {

            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".txt");
            }
        };
    }

    /**
	 * @param frame
	 * @param tf
	 */
    private void openFileDialog(int mode) {
        FileDialog fd = new FileDialog(frame, "My Gtk File Dialog");
        fd.setMode(mode);
        if ((new File(tf.getText())).exists()) {
            fd.setFile(tf.getText());
        }
        fd.setVisible(true);
        System.out.println("Peer: " + fd.getPeer().getClass().getName());
        System.out.println("dir: " + fd.getDirectory());
        System.out.println("file: " + fd.getFile());
        if (fd.getFile() != null) {
            tf.setText(fd.getDirectory() + fd.getFile());
        }
    }

    public static void main(String[] args) {
        new FileDialogTest().testButton();
    }
}
