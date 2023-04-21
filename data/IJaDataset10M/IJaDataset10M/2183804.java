package net.hanjava.alole.action;

import java.awt.event.ActionEvent;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.swing.JFileChooser;
import net.hanjava.alole.MainPanel;
import net.hanjava.util.CheckableState;
import net.hanjava.util.HanAbstractAction;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

public class SaveAction extends HanAbstractAction {

    private MainPanel mainPanel;

    public SaveAction(MainPanel mainPanel, CheckableState[] checks) {
        super("Save", checks);
        this.mainPanel = mainPanel;
    }

    @Override
    protected String getLongDescription() {
        return null;
    }

    @Override
    protected String getShortDescription() {
        return "Save as OLE file(doc,xls,ppt)";
    }

    public void actionPerformed(ActionEvent e) {
        JFileChooser saveChooser = new JFileChooser();
        int response = saveChooser.showSaveDialog(mainPanel);
        if (response == JFileChooser.APPROVE_OPTION) {
            String fullFilePath = saveChooser.getSelectedFile().toString();
            POIFSFileSystem fs = mainPanel.getView().getFileSystem();
            exportToCompoundFile(fs, fullFilePath);
        }
    }

    private void exportToCompoundFile(POIFSFileSystem fs, String fullFilePath) {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(fullFilePath);
            fs.writeFilesystem(fos);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    protected String getIconPath() {
        return "net/hanjava/alole/action/save.gif";
    }
}
