package com.google.code.ftspc.LectorInstaller.SomeFunctions;

import com.google.code.ftspc.LectorInstaller.UnPack.UnZipTomCat;
import com.google.code.ftspc.LectorInstaller.UnPack.UnZip;
import com.google.code.ftspc.LectorInstaller.Downloads.DownloadTomCat;
import com.google.code.ftspc.LectorInstaller.Downloads.DownloadApacheTomcat;
import com.google.code.ftspc.LectorInstaller.MainFrame;
import java.util.TimerTask;
import javax.swing.JOptionPane;

/**
 *
 * @author Arthur Khusnutdinov
 */
public class InfoThread extends TimerTask {

    @Override
    public void run() {
        try {
            if (UnZipTomCat.UnZipDone && DownloadTomCat.downloadDone && UnZip.UnZipDone) {
                MainFrame.jLabel7.setText("All done. You can close the installer.");
                DownloadApacheTomcat.jLabel2.setText("All done. You can close the installer.");
                JOptionPane.showMessageDialog(null, "Installation complete. " + "\nYou can close the installer.", "LectorInstaller", 1);
                this.cancel();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
