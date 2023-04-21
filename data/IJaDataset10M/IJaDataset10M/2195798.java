package net.jankenpoi.sudokuki.ui.swing;

import static net.jankenpoi.i18n.I18n._;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.filechooser.FileFilter;
import net.jankenpoi.sudokuki.view.GridView;

@SuppressWarnings("serial")
public class SaveAsAction extends AbstractAction {

    private final GridView view;

    private final JFrame frame;

    SaveAsAction(JFrame frame, GridView view, ActionsRepository actions) {
        this.frame = frame;
        this.view = view;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        final JFileChooser fc = new JFileChooser() {

            @Override
            public File getSelectedFile() {
                File file = super.getSelectedFile();
                if (file == null) {
                    return null;
                }
                if (getExtension(file) == null) {
                    file = new File(file.getAbsolutePath() + ".skg");
                }
                return file;
            }
        };
        fc.setDialogTitle(_("Save as..."));
        fc.setAcceptAllFileFilterUsed(false);
        fc.setFileFilter(new FileFilter() {

            @Override
            public String getDescription() {
                return _("Sudokuki grid files");
            }

            @Override
            public boolean accept(File f) {
                String extension = getExtension(f);
                if (f.isDirectory() || "skg".equals(extension)) {
                    return true;
                }
                return false;
            }
        });
        int returnVal = fc.showOpenDialog(frame);
        if (returnVal != JFileChooser.APPROVE_OPTION) {
            return;
        }
        File fileToSave = fc.getSelectedFile();
        if (fileToSave == null) {
            return;
        }
        fileToSave.delete();
        try {
            fileToSave.createNewFile();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(fileToSave);
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        }
        if (fos == null) {
            return;
        }
        int[] cellInfos = view.getController().getCellInfosFromModel();
        for (int i = 0; i < cellInfos.length; i++) {
            byte lo = (byte) (cellInfos[i] & 0xFF);
            byte hi = (byte) ((cellInfos[i] & 0xFF00) >> 8);
            try {
                fos.write(lo);
                fos.write(hi);
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
        try {
            fos.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    private static String getExtension(File f) {
        String ext = null;
        String s = f.getName();
        int i = s.lastIndexOf('.');
        if (i > 0 && i < s.length() - 1) {
            ext = s.substring(i + 1).toLowerCase();
        }
        return ext;
    }
}
