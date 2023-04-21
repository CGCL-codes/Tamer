package modrcon;

import java.awt.Cursor;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.*;

/**
 * This panel displays a JTextField with a folder icon next to it,
 * in such a way that the folder icon looks like it is inside of the
 * JTextField. Clicking the folder icon brings up a JFileChooser so
 * that the user can select a file, and its path will be shown in the
 * JTextField. This component is used in the SettingManager window.
 *
 * @author Pyrite
 */
public class FileChooserPanel extends JPanel implements MouseListener {

    private MainWindow parent;

    private ImageIcon folderIcon;

    private JTextField gamePathText;

    private JLabel folderLabel;

    private JFileChooser jfc;

    /**
     * Constructs a FileChooserPanel.
     *
     * @param columns The number of columns to set the JTextField to.
     */
    public FileChooserPanel(MainWindow owner, int columns) {
        super();
        this.parent = owner;
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        folderIcon = new ImageIcon(getClass().getResource("/modrcon/resources/folder_find.png"));
        folderLabel = new JLabel(folderIcon);
        gamePathText = new JTextField(columns);
        folderLabel.addMouseListener(this);
        folderLabel.setToolTipText("Click here to browse for Urban Terror");
        jfc = new JFileChooser();
        this.setBorder(this.gamePathText.getBorder());
        gamePathText.setBorder(null);
        folderLabel.setBorder(null);
        this.add(gamePathText);
        this.add(folderLabel);
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                setPreferredSize(new java.awt.Dimension((int) getWidth(), (int) gamePathText.getHeight()));
            }
        });
    }

    public void mouseClicked(MouseEvent e) {
        int choice = jfc.showDialog(this.parent, "Select");
        if (choice == JFileChooser.APPROVE_OPTION) {
            String path = jfc.getSelectedFile().getAbsolutePath();
            if (ModRconUtil.isMac()) {
                path += (!path.endsWith(".app") ? ".app" : "") + "/Contents/MacOS/ioUrbanTerror.ub";
            }
            this.gamePathText.setText(path);
        }
    }

    public void mousePressed(MouseEvent e) {
        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }

    public void mouseReleased(MouseEvent e) {
        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }

    public void mouseEntered(MouseEvent e) {
        setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    public void mouseExited(MouseEvent e) {
        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }

    /**
     * Gets the text inside the JTextField.
     *
     * @return The Game Path Text.
     */
    public String getGamePath() {
        return this.gamePathText.getText();
    }

    /**
     * Sets the text inside the JTextField.
     *
     * @param path The Game Path Text.
     */
    public void setGamePath(String path) {
        this.gamePathText.setText(path);
    }
}
