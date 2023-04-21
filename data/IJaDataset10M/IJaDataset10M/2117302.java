package com.visitrend.ndvis.persistence;

import com.visitrend.ndvis.app.NDVis;
import com.visitrend.ndvis.gui.spi.DataVisualization;
import com.visitrend.ndvis.io.FileUtils;
import java.io.File;
import java.io.IOException;
import java.util.prefs.Preferences;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import org.openide.cookies.SaveCookie;
import org.openide.util.lookup.ServiceProvider;

@ServiceProvider(service = SaveCookie.class)
public class SavePngImageAction implements SaveCookie {

    private NDVis owner;

    public SavePngImageAction() {
        this.owner = NDVis.getDefault();
    }

    @Override
    public void save() throws IOException {
        Preferences preferences = Preferences.userNodeForPackage(getClass());
        String path = preferences.get("img-path", "");
        JFileChooser chooser = new JFileChooser(path);
        chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        chooser.setAcceptAllFileFilterUsed(false);
        chooser.setFileFilter(DimStackImageFileFilter.getInstance());
        DataVisualization dataVis = owner.getActiveDataVisualization();
        int returnVal = chooser.showSaveDialog(dataVis.getTopComponent());
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File img = chooser.getSelectedFile();
            if (img.isDirectory()) {
                preferences.put("img-path", img.getAbsolutePath());
            } else {
                preferences.put("img-path", img.getParent());
            }
            if (img.exists()) {
                int answer = JOptionPane.showConfirmDialog(null, "Overwrite existing file?");
                if (answer != JOptionPane.OK_OPTION) {
                    return;
                }
            } else {
                if (FileUtils.getExtension(img) == null) {
                    javax.swing.filechooser.FileFilter filter = chooser.getFileFilter();
                    String str = img.getAbsolutePath() + filter.getDescription();
                    img = new File(str);
                }
            }
            writeFile(img);
            dataVis.fireSaveStateChange(false);
        }
    }

    private void writeFile(File img) {
        try {
            ImageIO.write(owner.getActiveDataVisualization().getOffScreenImage(), "png", img);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
