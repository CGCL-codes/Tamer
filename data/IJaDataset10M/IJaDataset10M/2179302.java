package com.jaccal.console.tab;

import com.jaccal.console.I18N;
import javax.swing.*;
import java.awt.*;
import java.io.*;

/**
 * All errors are sent to this tab
 *
 * @author Chang Sau Sheong
 */
public class ErrorTab extends OutputTab {

    protected void save() {
        Frame frame = getFrame();
        if (fileDialog == null) {
            fileDialog = new FileDialog(frame);
        }
        fileDialog.setMode(FileDialog.SAVE);
        fileDialog.setFile(title + ".err");
        fileDialog.show();
        String file = fileDialog.getFile();
        if (file == null) {
            return;
        }
        String directory = fileDialog.getDirectory();
        StringReader sr = new StringReader(textPane.getText());
        try {
            File f = new File(directory, file);
            FileOutputStream fos = new FileOutputStream(f);
            int i;
            while ((i = sr.read()) != -1) {
                fos.write(i);
            }
            fos.close();
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        JOptionPane.showMessageDialog(this, I18N.getString("error_saved_to_file") + directory + file + ".");
    }
}
