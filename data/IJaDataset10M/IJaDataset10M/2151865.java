package uk.co.akademy.PhotoShow;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

public class PhotosFromPanelFolder extends AbstractPhotosFromPanel {

    private static final long serialVersionUID = -8608414821762979193L;

    private String _folder_folders;

    private JList _listFolders;

    public PhotosFromPanelFolder() {
        this.setName("Folders");
        _listFolders = new JList();
        _listFolders.setSelectedIndex(0);
        _listFolders.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    @Override
    boolean initilise(PropertyFetcher properties) {
        _folder_folders = properties.getProperty("folder.folders");
        _listFolders.setListData(_folder_folders.split(";"));
        JButton buttonChooseFolder = new JButton("Choose another folder");
        buttonChooseFolder.setActionCommand("ChooseFolder");
        buttonChooseFolder.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                fileChooser.setAcceptAllFileFilterUsed(false);
                if (fileChooser.showDialog(null, "Select folder") == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    if (_folder_folders.isEmpty()) _folder_folders = file.getAbsolutePath(); else _folder_folders += ";" + file.getAbsolutePath();
                    _listFolders.setListData(_folder_folders.split(";"));
                }
            }
        });
        this.add(new JScrollPane(_listFolders));
        this.add(buttonChooseFolder);
        return true;
    }

    public void updateProperties() {
        Program.setProperty("folder.folders", _folder_folders);
    }
}
