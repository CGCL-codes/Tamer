package org.gerhardb.lib.print;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.swing.*;
import org.gerhardb.jibs.Jibs;
import org.gerhardb.lib.image.ImageUtil;
import org.gerhardb.lib.swing.GlassPane;

/**
 * Static methods.
 */
public class SaveAsJpgWorker extends SwingWorker<Object, Object> {

    Pageable pageable;

    JFrame frame;

    GlassPane eatMe = new GlassPane();

    SaveAsJpgWorker(Pageable pageableIn, JFrame frameIn) {
        this.pageable = pageableIn;
        this.frame = frameIn;
        this.frame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        this.eatMe.setGlassPane(this.frame);
    }

    @Override
    protected void done() {
        this.frame.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        this.eatMe.removeGlassPane();
    }

    @Override
    protected Object doInBackground() throws Exception {
        boolean checkOverwrite = true;
        String front = this.pageable.getSaveInfo().getSaveDirectory() + System.getProperty("file.separator") + this.pageable.getSaveInfo().fileNamePattern() + "-";
        int pageCount = this.pageable.getNumberOfPages();
        for (int i = 1; i <= pageCount; i++) {
            String fileName = front + PageableUtils.FILE_COUNT_FORMAT.format(i) + ".jpg";
            File file = new File(fileName);
            if (file.exists() && checkOverwrite) {
                int overwrite = JOptionPane.showConfirmDialog(this.frame, Jibs.getString("ContactSheetSaveOptions.14"), Jibs.getString("ContactSheetSaveOptions.15"), JOptionPane.YES_NO_OPTION);
                if (overwrite == JOptionPane.NO_OPTION) {
                    return null;
                }
                checkOverwrite = false;
            }
            PageLayout layout = this.pageable.getPageLayout();
            BufferedImage buff = new BufferedImage(layout.getPageWidth(), layout.getPageHeight(), BufferedImage.TYPE_INT_RGB);
            Graphics2D g2 = buff.createGraphics();
            try {
                this.pageable.prepareToPrint(i);
                this.pageable.print(g2);
                ImageUtil.saveJPEG(buff, 1.0f, file);
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this.frame, ex.getMessage(), Jibs.getString("problem"), JOptionPane.ERROR_MESSAGE);
                return null;
            }
            super.setProgress(i);
        }
        return null;
    }
}
