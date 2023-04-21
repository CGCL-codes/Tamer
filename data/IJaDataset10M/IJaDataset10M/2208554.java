package vaspgui.poscarFiles;

import vaspgui.poscarFiles.PoscarGuided;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import vaspgui.IO;

public class PoscarEditor extends JPanel {

    /**
	 *
	 */
    private static final long serialVersionUID = 1L;

    private static JTextArea textArea;

    public PoscarEditor() {
        super();
        textArea = new JTextArea();
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        JScrollPane areaScrollPane = new JScrollPane(textArea);
        areaScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        areaScrollPane.setPreferredSize(new Dimension(800, 550));
        add(areaScrollPane);
    }

    /**
	 * This method updates the editor from the saved file.
	 */
    public static void getTextFromFile() {
        textArea.setText(PoscarFormatter.getFile());
    }

    protected static void saveFile() {
        if (IO.checkForFile(IO.getCurDir(), "POSCAR")) {
            PoscarFormatter.saveFile(textArea.getText());
        } else {
            int answer = JOptionPane.showConfirmDialog(new JFrame(), "POSCAR file does not exist.  Create?");
            if (answer == JOptionPane.YES_OPTION) {
                PoscarFormatter.saveFile(textArea.getText());
            }
        }
    }
}
