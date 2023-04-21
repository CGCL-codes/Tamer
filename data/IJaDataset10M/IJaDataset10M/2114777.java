package com.wd.abom.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingWorker;
import javax.swing.border.EmptyBorder;
import com.wd.abom.backend.Configuration;
import com.wd.abom.system.Tempfile;

public class InitFrame implements Runnable {

    private final Logger logger = Logger.getLogger(InitFrame.class.getName());

    private JFrame frame;

    private JDialog dialog;

    private JLabel infoLabel;

    private JProgressBar infoProgressBar;

    /**
	 * This frame comes up during initialization of the application. It launches
	 * some init tasks and stores some stings in the configuration
	 */
    public InitFrame() {
        this.frame = new JFrame();
        this.dialog = new JDialog();
        this.infoLabel = new JLabel();
        this.infoProgressBar = new JProgressBar();
    }

    @Override
    public void run() {
        AppInitTask ept = new AppInitTask();
        dialog.setModal(true);
        dialog.setLayout(new BorderLayout());
        dialog.add(createImagePanel(), BorderLayout.CENTER);
        dialog.add(createInfoPanel(), BorderLayout.SOUTH);
        dialog.pack();
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        int x = (screenSize.width - dialog.getWidth()) / 2;
        int y = (screenSize.height - dialog.getHeight()) / 2;
        dialog.setLocation(x, y);
        ept.addPropertyChangeListener(new SwingWorkerCompletionWaiter(dialog));
        ept.execute();
        dialog.setVisible(true);
        initComponents();
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        ept.addPropertyChangeListener(new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                if ("progress".equals(evt.getPropertyName())) {
                    infoProgressBar.setValue((Integer) evt.getNewValue());
                }
            }
        });
        ept.execute();
        while (!ept.isDone()) {
            try {
                Thread.sleep(1L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        frame.setVisible(false);
        frame.dispose();
    }

    private void initComponents() {
        frame.setLayout(new BorderLayout());
        frame.add(createImagePanel(), BorderLayout.CENTER);
        frame.add(createInfoPanel(), BorderLayout.SOUTH);
    }

    private Component createImagePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBorder(new EmptyBorder(1, 1, 1, 1));
        ImageIcon icon = new ImageIcon(InitFrame.class.getResource("/resources/title.jpg"));
        panel.add(new JLabel(icon));
        return panel;
    }

    private Component createInfoPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBorder(new EmptyBorder(1, 1, 1, 1));
        infoLabel.setText(" ");
        infoProgressBar.setString(" ");
        panel.add(infoProgressBar);
        return panel;
    }

    private class AppInitTask extends SwingWorker<Void, Void> {

        @Override
        public Void doInBackground() {
            logger.info("Starting file extraction task");
            int tempFileCount = 0;
            int currentFile = 0;
            String path;
            tempFileCount += 2;
            tempFileCount += 1;
            tempFileCount += 1;
            if (Configuration.getCurrentChapterizer().equals(Configuration.SSA)) {
                tempFileCount += 13;
            } else if (Configuration.getCurrentChapterizer().equals(Configuration.MP4V2)) {
                tempFileCount += 1;
            }
            infoLabel.setText("Extracting Files");
            infoProgressBar.setStringPainted(true);
            try {
                infoProgressBar.setString("Extracting Files");
                infoProgressBar.setMinimum(0);
                infoProgressBar.setMaximum(tempFileCount);
                setProgress(currentFile++);
                infoProgressBar.setString("Extracting mp4Box/js32.dll");
                Tempfile.createTempfile("/resources/mp4Box/js32.dll");
                setProgress(currentFile++);
                infoProgressBar.setString("Extracting mp4Box/MP4Box.exe");
                path = Tempfile.createTempfile("/resources/mp4Box/MP4Box.exe").getAbsolutePath();
                setProgress(currentFile++);
                Configuration.setPathMerger(path);
                logger.info("extracted MP4Box.exe to " + Configuration.getPathMerger());
                infoProgressBar.setString("Extracting faac/faac.exe");
                path = Tempfile.createTempfile("/resources/faac/faac.exe").getAbsolutePath();
                setProgress(currentFile++);
                Configuration.setFaacPath(path);
                logger.info("extracted faac.exe to " + Configuration.getFaacPath());
                infoProgressBar.setString("Extracting madplay/madplay.exe");
                path = Tempfile.createTempfile("/resources/madplay/madplay.exe").getAbsolutePath();
                setProgress(currentFile++);
                Configuration.setPathMadplay(path);
                logger.info("extracted faac.exe to " + Configuration.getPathMadplay());
                if (Configuration.getCurrentChapterizer().equals(Configuration.SSA)) {
                    infoProgressBar.setString("Extracting ssa/blank100.jpg");
                    Tempfile.createTempfile("/resources/ssa/blank100.jpg");
                    setProgress(currentFile++);
                    infoProgressBar.setString("Extracting ssa/DW.dll");
                    Tempfile.createTempfile("/resources/ssa/DW.dll");
                    setProgress(currentFile++);
                    infoProgressBar.setString("Extracting ssa/DWHelper.dll");
                    Tempfile.createTempfile("/resources/ssa/DWHelper.dll");
                    setProgress(currentFile++);
                    infoProgressBar.setString("Extracting ssa/fnf100.jpg");
                    Tempfile.createTempfile("/resources/ssa/fnf100.jpg");
                    setProgress(currentFile++);
                    infoProgressBar.setString("Extracting ssa/FreeImage.dll");
                    Tempfile.createTempfile("/resources/ssa/FreeImage.dll");
                    setProgress(currentFile++);
                    infoProgressBar.setString("Extracting ssa/license-fi.txt");
                    Tempfile.createTempfile("/resources/ssa/license-fi.txt");
                    setProgress(currentFile++);
                    infoProgressBar.setString("Extracting ssa/license.txt");
                    Tempfile.createTempfile("/resources/ssa/license.txt");
                    setProgress(currentFile++);
                    infoProgressBar.setString("Extracting ssa/Microsoft.VC80.CRT.manifest");
                    Tempfile.createTempfile("/resources/ssa/Microsoft.VC80.CRT.manifest");
                    setProgress(currentFile++);
                    infoProgressBar.setString("Extracting ssa/msvcp80.dll");
                    Tempfile.createTempfile("/resources/ssa/msvcp80.dll");
                    setProgress(currentFile++);
                    infoProgressBar.setString("Extracting ssa/msvcr80.dll");
                    Tempfile.createTempfile("/resources/ssa/msvcr80.dll");
                    setProgress(currentFile++);
                    infoProgressBar.setString("Extracting ssa/SSA.exe");
                    path = Tempfile.createTempfile("/resources/ssa/SSA.exe").getAbsolutePath();
                    setProgress(currentFile++);
                    infoProgressBar.setString("Extracting ssa/zlib.dll");
                    Tempfile.createTempfile("/resources/ssa/zlib.dll");
                    setProgress(currentFile++);
                    infoProgressBar.setString("Extracting ssa/x.bat");
                    Tempfile.createTempfile("/resources/ssa/x.bat");
                    setProgress(currentFile++);
                    Configuration.setPathChapterizer(path);
                    logger.info("extracted ssa.exe to " + Configuration.getPathChapterizer());
                } else if (Configuration.getCurrentChapterizer().equals(Configuration.MP4V2)) {
                    infoProgressBar.setString("Extracting mp4v2/mp4chaps.exe");
                    path = Tempfile.createTempfile("/resources/mp4v2/mp4chaps.exe").getAbsolutePath();
                    setProgress(currentFile++);
                    File f = new File(path);
                    Configuration.setPathMp4v2Tools(f.getParent());
                    logger.info("extracted mp4v2 tools to " + Configuration.getPathMp4v2Tools());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            logger.info("All files extracted");
            return null;
        }

        @Override
        public void done() {
        }
    }
}
