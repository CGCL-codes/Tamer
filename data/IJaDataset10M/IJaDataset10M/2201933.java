package jgamess.swing;

import javax.swing.*;
import java.io.File;
import java.io.*;

public class PDFManual {

    protected File pdfFile;

    public PDFManual(File f) {
        pdfFile = f;
    }

    public PDFManual(String path) {
        pdfFile = new File(path);
    }

    public void show() {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                PDFDisplayFrame pd = new PDFDisplayFrame(false);
                pd.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
                try {
                    pd.openFile(pdfFile);
                    pd.setVisible(true);
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(null, "Failed to load documentation. \n" + e);
                    pd.setVisible(false);
                } catch (NullPointerException npe) {
                } finally {
                }
            }
        });
    }
}
