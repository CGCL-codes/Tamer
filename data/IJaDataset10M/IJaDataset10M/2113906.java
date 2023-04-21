package plugins.mdb;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JFileChooser;
import core.Core;
import database.Database;
import database.Word;

public class MDBImportModel implements ActionListener {

    private MDBImportView view;

    public MDBImportModel(MDBImportView view) {
        this.view = view;
    }

    boolean isWindowsOS() {
        String operatingSystem = System.getProperty("os.name");
        if (operatingSystem.startsWith("Win")) return true; else return false;
    }

    void fillWithUsernames() {
        view.usernameChooser.setModel(new DefaultComboBoxModel(view.plugin.getUsernames()));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(view.fileChooserBtn)) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.showOpenDialog(view);
            String path = fileChooser.getSelectedFile().getAbsolutePath();
            view.filePath.setText(path);
            view.plugin.setPathToMDB(view.filePath.getText());
            fillWithUsernames();
        } else if (e.getSource().equals(view.startImportBtn)) {
            view.plugin.setUsername("Mauri");
            Vector<Word> imported = view.plugin.importData();
            Database database = Core.getDatabase();
            for (Word word : imported) {
                database.createWord(word);
            }
        }
    }
}
