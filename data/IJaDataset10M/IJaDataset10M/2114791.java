package net.hanjava.alole.action;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import net.hanjava.alole.MainPanel;
import net.hanjava.alole.util.FileSystemConverter;
import net.hanjava.alole.view.FileView;
import net.hanjava.util.CheckableState;
import net.hanjava.util.HanAbstractAction;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

public class ExportAction extends HanAbstractAction {

    private MainPanel mainPanel;

    public ExportAction(MainPanel mainPanel, CheckableState[] checks) {
        super("Export", checks);
        this.mainPanel = mainPanel;
    }

    public void actionPerformed(ActionEvent e) {
        JFileChooser saveChooser = new JFileChooser();
        int response = saveChooser.showSaveDialog(mainPanel);
        if (response == JFileChooser.APPROVE_OPTION) {
            String fullFilePath = saveChooser.getSelectedFile().toString();
            FileView fileView = mainPanel.getView();
            POIFSFileSystem fileSystem = fileView.getFileSystem();
            File outputDirPath = saveChooser.getSelectedFile();
            try {
                FileSystemConverter.toPhysicalFileSystem(fileSystem, outputDirPath.toString());
            } catch (IOException e1) {
                JOptionPane.showMessageDialog(mainPanel, e1.getMessage(), "Shit!!", JOptionPane.ERROR_MESSAGE);
                e1.printStackTrace();
            }
        }
    }

    @Override
    protected String getLongDescription() {
        return null;
    }

    @Override
    protected String getShortDescription() {
        return "Export to a folder";
    }

    @Override
    protected String getIconPath() {
        return "net/hanjava/alole/action/export.gif";
    }
}
