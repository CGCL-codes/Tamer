package kfschmidt.qvii.controlpanel;

import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import kfschmidt.data4d.io.*;
import kfschmidt.data4d.*;
import kfschmidt.stackrenderer.SlicePlan;
import kfschmidt.qvii.Literals;
import kfschmidt.qvii.Task;
import kfschmidt.qvii.TaskRunner;
import java.net.*;
import java.awt.*;
import java.io.*;
import kfschmidt.qvii.QVII;
import kfschmidt.qvii.QVIIProjectListener;
import kfschmidt.ijcommon.IJAdapterQV;
import kfschmidt.qvii.Literals;
import kfschmidt.qvii.visualizer.VisualizerWindow;

public class ControlPanelWindow extends JFrame implements WindowListener, ActionListener, QVIIProjectListener {

    QVII mQVII;

    int mDefaultWidth;

    int mDefaultHeight;

    CPMenu mMenu;

    IJAdapterQV mIJada;

    ProjectPanel mProjectPane;

    FreeMemDisplayThread mMemThread;

    JToolBar mToolBar;

    JButton mOpenVisualizer;

    JButton mImportVolume;

    JButton mOpenProject;

    JButton mRemoveItem;

    JButton mStopItem;

    /**
     *  returns the object associated with the currently selected
     *  project panel item, or null
     */
    public Object getCurrentlySelectedItem() {
        if (mProjectPane != null && mProjectPane.getCurrentlySelectedItem() != null) {
            return mProjectPane.getCurrentlySelectedItem();
        } else return null;
    }

    public void setCurrentlySelectedItem(Object o) {
        mProjectPane.setCurrentlySelectedItem(o);
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource().equals(mMenu.mFileImportIJ) || ae.getSource().equals(mImportVolume)) {
            new VolImportDialog(this);
        } else if (ae.getSource().equals(mOpenVisualizer) || ae.getSource().equals(mMenu.mVolOpenVis)) {
            openVisualizer();
        } else if (ae.getSource().equals(mRemoveItem)) {
            mQVII.remove(mProjectPane.getCurrentlySelectedItem());
        } else if (ae.getSource().equals(mMenu.mVolEdit)) {
            new VolumeEditorWindow(mQVII, (VolumeTimeSeries) mProjectPane.getCurrentlySelectedItem());
        } else if (ae.getSource().equals(mMenu.mFileOpen)) {
            importQV2();
        } else if (ae.getSource().equals(mStopItem)) {
            killCurrentlySelectedTask();
        } else if (ae.getSource().equals(mMenu.mFileImportStim)) {
            importStim();
        } else if (ae.getSource().equals(mMenu.mFileExportStim)) {
            exportStim();
        } else if (ae.getSource().equals(mMenu.mFileExportIJ)) {
            notYetImplemented();
        } else if (ae.getSource().equals(mMenu.mFileSave)) {
            saveVolume();
        } else if (ae.getSource().equals(mMenu.mVolEdit)) {
            notYetImplemented();
        } else if (ae.getSource().equals(mMenu.mVolOpenVis)) {
            notYetImplemented();
        } else if (ae.getSource().equals(mMenu.mVolNewVOI)) {
            notYetImplemented();
        } else if (ae.getSource().equals(mMenu.mVolMarch)) {
            notYetImplemented();
        } else if (ae.getSource().equals(mMenu.mVolMeasureVOI)) {
            measureVOI();
        } else if (ae.getSource().equals(mMenu.mHelpAbout)) {
            notYetImplemented();
        }
    }

    public void cleanup() {
        System.out.println("ControlPanelWindow.cleanup()");
        mProjectPane.cleanup();
    }

    public void measureVOI() {
        new MeasureVOIDialog(this);
    }

    public void measureVOI(BinaryVolumeTimeSeries voi, FloatVolumeTimeSeries vol) {
        System.out.println("measureVOI(" + voi + ", " + vol + ")");
        TimeSeriesMeasurement meas = TimeSeriesMeasurer.measureVOI(voi, vol);
        System.out.println("got results: " + meas);
    }

    public void killCurrentlySelectedTask() {
        try {
            Object curselected = getCurrentlySelectedItem();
            if (curselected instanceof Task) {
                mQVII.getTaskRunner().killTask((Task) curselected);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void importStim() {
        loadStimVolumes(this);
    }

    public void exportStim() {
        saveStimVolume(this);
    }

    public void importQV2() {
        loadQV2Volumes(this);
    }

    public void saveVolume() {
        try {
            Object curselected = getCurrentlySelectedItem();
            if (curselected instanceof VolumeTimeSeries) {
                File defaultdir = IJAdapterQV.getWorkingDirectory();
                String filename = ((VolumeTimeSeries) curselected).getDescription();
                filename = filename.replace('.', '_');
                filename = filename.replace(' ', '_');
                filename += ".qv2";
                File outfile = new File(filename);
                JFileChooser chooser = new JFileChooser(outfile);
                chooser.setSelectedFile(outfile);
                QV2FileFilter filter = new QV2FileFilter();
                chooser.setFileFilter(filter);
                int returnVal = chooser.showSaveDialog(this);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    outfile = chooser.getSelectedFile();
                }
                QVExportTask exporttask = new QVExportTask(mQVII.getTaskRunner(), (VolumeTimeSeries) curselected, outfile);
                mQVII.getTaskRunner().startTask(exporttask);
            }
        } catch (Exception e) {
            processError(e);
        }
    }

    public void processError(Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, Literals.CP_ERROR_PREFIX + e.getMessage(), Literals.CP_ERROR_TITLE, JOptionPane.INFORMATION_MESSAGE);
    }

    public void showErrorMsg(String msg) {
        JOptionPane.showMessageDialog(this, Literals.CP_ERROR_PREFIX + msg, Literals.CP_ERROR_TITLE, JOptionPane.INFORMATION_MESSAGE);
    }

    public void notYetImplemented() {
        JOptionPane.showMessageDialog(this, "Feature not yet implemented", "Not yet implemented", JOptionPane.INFORMATION_MESSAGE);
    }

    public QVII getQVII() {
        return mQVII;
    }

    public void qviiProjectUpdated(int code, Object o) {
        if (code == QVIIProjectListener.UPDATE_ALL) {
            updateProject();
        }
    }

    public void windowActivated(WindowEvent e) {
    }

    public void windowClosed(WindowEvent e) {
    }

    public void windowClosing(WindowEvent e) {
        mMemThread.killUpdater();
        mQVII.sendExitRequest();
    }

    public void windowDeactivated(WindowEvent e) {
    }

    public void windowDeiconified(WindowEvent e) {
    }

    public void windowIconified(WindowEvent e) {
    }

    public void windowOpened(WindowEvent e) {
    }

    public ControlPanelWindow(QVII qvii, int initial_width, int initial_height) {
        mQVII = qvii;
        mQVII.addProjectListener(this);
        mIJada = new IJAdapterQV();
        setTitle(Literals.CP_WINDOW_TITLE);
        mDefaultWidth = initial_width;
        mDefaultHeight = initial_height;
        mMemThread = new FreeMemDisplayThread(this);
        mMemThread.startUpdater();
        init();
    }

    public void openVisualizer() {
        new VisualizerWindow(mQVII, 400, 400);
    }

    protected void enableVolEdit() {
        mMenu.enableVolEdit();
    }

    protected void disableVolEdit() {
        mMenu.disableVolEdit();
    }

    private JButton makeButton(String imageName, String toolTipText, String altText) {
        String image = "/kfschmidt/qvii/graphics/" + imageName;
        URL imageURL = ControlPanelWindow.class.getResource(image);
        JButton button = new JButton();
        button.setToolTipText(toolTipText);
        button.addActionListener(this);
        if (imageURL != null) {
            button.setIcon(new ImageIcon(imageURL, altText));
        } else {
            button.setText(altText);
            System.err.println("Resource not found: " + image);
        }
        return button;
    }

    private void init() {
        setResizable(false);
        addWindowListener(this);
        mMenu = new CPMenu(this);
        mMenu.addActionListener(this);
        setJMenuBar(mMenu);
        mToolBar = new JToolBar("QVII Control Panel");
        mOpenVisualizer = makeButton("AlignCenter16.gif", Literals.TOOLBAR_OPEN_VIS, Literals.TOOLBAR_OPEN_VIS_ALT);
        mImportVolume = makeButton("Import16.gif", Literals.TOOLBAR_IMPORT_VOL, Literals.TOOLBAR_IMPORT_VOL_ALT);
        mRemoveItem = makeButton("Delete16.gif", Literals.TOOLBAR_DELETE_ITEM, Literals.TOOLBAR_DELETE_ITEM_ALT);
        mStopItem = makeButton("Stop16.gif", Literals.TOOLBAR_STOP_ITEM, Literals.TOOLBAR_STOP_ITEM_ALT);
        mToolBar.add(mOpenVisualizer);
        mToolBar.add(mImportVolume);
        mToolBar.add(mRemoveItem);
        mToolBar.add(mStopItem);
        mProjectPane = new ProjectPanel(this);
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(mToolBar, BorderLayout.PAGE_START);
        getContentPane().add(mProjectPane, BorderLayout.CENTER);
        setSize(new Dimension(mDefaultWidth, mDefaultHeight));
        show();
    }

    public void importVolume(int ij_id) {
        mQVII.importVolume(ij_id);
    }

    public void updateProject() {
        mProjectPane.setProject(mQVII.getProject());
        repaint();
    }

    public void loadStimVolumes(Component parent) {
        try {
            File[] sprfiles = getTheSPRFiles(parent);
            if (sprfiles == null) return;
            for (int a = 0; a < sprfiles.length; a++) {
                StimulateImportTask importtask = new StimulateImportTask(mQVII.getTaskRunner(), sprfiles[a], mQVII);
                mQVII.getTaskRunner().startTask(importtask);
            }
        } catch (Exception e) {
            processError(e);
        }
    }

    public SlicePlan getSlicePlanFromUser(Component parent, boolean allow_native) {
        SlicePlan[] plans = mQVII.getCurrentlyAvailableSlicePlans();
        if (!allow_native && plans != null) {
            Vector v = new Vector();
            for (int a = 0; a < plans.length; a++) {
                if (plans[a].getType() != SlicePlan.NATIVE) v.addElement(plans[a]);
            }
            if (v.size() > 0) {
                plans = new SlicePlan[v.size()];
                v.copyInto(plans);
            } else {
                return null;
            }
        }
        if (plans == null) {
            if (allow_native) {
                return new SlicePlan();
            } else {
                showErrorMsg(Literals.NO_REAL_SPACE_SLICE_PLAN_MSG);
                return null;
            }
        }
        SlicePlan selectedplan = (SlicePlan) JOptionPane.showInputDialog(null, Literals.CHOOSE_PLAN_MSG, Literals.CHOOSE_PLAN_TITLE, JOptionPane.INFORMATION_MESSAGE, null, plans, plans[0]);
        return selectedplan;
    }

    public void saveStimVolume(Component parent) {
        VolumeTimeSeries testvol = mQVII.getCurrentlySelectedVolume();
        if (testvol == null || !(testvol instanceof FloatVolumeTimeSeries)) {
            showErrorMsg("Please select a Float volume \nto save to Stimulate (SDT) format.");
            return;
        }
        FloatVolumeTimeSeries vol = (FloatVolumeTimeSeries) testvol;
        SlicePlan plan = getSlicePlanFromUser(parent, false);
        if (plan == null) return;
        try {
            File sprfile = getTheSPRFile(parent, vol.getDescription());
            if (sprfile == null) return;
            StimulateExportTask exporttask = new StimulateExportTask(mQVII.getTaskRunner(), vol, plan, sprfile, mQVII);
            mQVII.getTaskRunner().startTask(exporttask);
        } catch (Exception e) {
            processError(e);
        }
    }

    public void loadQV2Volumes(Component parent) {
        try {
            File[] qv2files = null;
            File defaultdir = (new File("afile")).getParentFile();
            defaultdir = IJAdapterQV.getWorkingDirectory();
            JFileChooser chooser = null;
            if (defaultdir != null) {
                chooser = new JFileChooser(defaultdir);
            } else {
                chooser = new JFileChooser();
            }
            QV2FileFilter filter = new QV2FileFilter();
            chooser.setFileFilter(filter);
            chooser.setMultiSelectionEnabled(true);
            int returnVal = chooser.showOpenDialog(parent);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                qv2files = chooser.getSelectedFiles();
                for (int a = 0; a < qv2files.length; a++) {
                    QVImportTask importtask = new QVImportTask(mQVII.getTaskRunner(), qv2files[a], mQVII);
                    mQVII.getTaskRunner().startTask(importtask);
                }
            }
        } catch (Exception e) {
            processError(e);
        }
    }

    /**
     *   used to open SDT volumes
     */
    private static File[] getTheSPRFiles(Component c) {
        File[] sprfiles = null;
        File defaultdir = (new File("afile")).getParentFile();
        defaultdir = IJAdapterQV.getWorkingDirectory();
        JFileChooser chooser = null;
        if (defaultdir != null) {
            chooser = new JFileChooser(defaultdir);
        } else {
            chooser = new JFileChooser();
        }
        SDTFileFilter filter = new SDTFileFilter();
        chooser.setFileFilter(filter);
        chooser.setMultiSelectionEnabled(true);
        int returnVal = chooser.showOpenDialog(c);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            sprfiles = chooser.getSelectedFiles();
        }
        if (sprfiles != null && sprfiles.length > 0) IJAdapterQV.setWorkingDirectory(sprfiles[0]);
        return sprfiles;
    }

    /**
     *   used to save a volume
     */
    private static File getTheSPRFile(Component c, String default_filename) {
        String fname = default_filename.replace(' ', '_');
        fname += ".spr";
        File sprfile = null;
        File defaultdir = (new File("afile")).getParentFile();
        defaultdir = IJAdapterQV.getWorkingDirectory();
        JFileChooser chooser = null;
        if (defaultdir != null) {
            chooser = new JFileChooser(defaultdir);
        } else {
            chooser = new JFileChooser();
        }
        SDTFileFilter filter = new SDTFileFilter();
        chooser.setFileFilter(filter);
        chooser.setSelectedFile(new File(chooser.getCurrentDirectory(), fname));
        int returnVal = chooser.showSaveDialog(c);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            sprfile = chooser.getSelectedFile();
        }
        if (sprfile != null) IJAdapterQV.setWorkingDirectory(sprfile);
        return sprfile;
    }
}

class FreeMemDisplayThread implements Runnable {

    boolean mExitNow = false;

    ControlPanelWindow mParent;

    public FreeMemDisplayThread(ControlPanelWindow parent) {
        mParent = parent;
    }

    public void killUpdater() {
        mExitNow = true;
        mParent = null;
    }

    public void startUpdater() {
        Thread t = new Thread(this);
        t.start();
    }

    public void run() {
        String mem_string = Literals.CP_WINDOW_TITLE;
        while (!mExitNow && mParent != null) {
            int freemegs = (int) ((double) Runtime.getRuntime().freeMemory() / 1048576d);
            mParent.setTitle(Literals.CP_WINDOW_TITLE + Literals.CP_WINDOW_FREERAM + freemegs + "M");
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
            }
        }
    }
}

class SDTFileFilter extends javax.swing.filechooser.FileFilter {

    public boolean accept(File f) {
        if (f != null) {
            if (f.isDirectory()) {
                return true;
            }
            String extension = getExtension(f);
            if (extension != null && extension.toLowerCase().equals("spr")) {
                return true;
            }
            ;
        }
        return false;
    }

    public String getDescription() {
        return "Stimulate Data";
    }

    /**
     * Return the extension portion of the file's name .
     *
     * @see #getExtension
     * @see FileFilter#accept
     */
    public String getExtension(File f) {
        if (f != null) {
            String filename = f.getName();
            int i = filename.lastIndexOf('.');
            if (i > 0 && i < filename.length() - 1) {
                return filename.substring(i + 1).toLowerCase();
            }
            ;
        }
        return null;
    }
}

class QV2FileFilter extends javax.swing.filechooser.FileFilter {

    public boolean accept(File f) {
        if (f != null) {
            if (f.isDirectory()) {
                return true;
            }
            String extension = getExtension(f);
            if (extension != null && extension.toLowerCase().equals("qv2")) {
                return true;
            }
            ;
        }
        return false;
    }

    public String getDescription() {
        return "Quickvol II Files";
    }

    /**
     * Return the extension portion of the file's name .
     *
     * @see #getExtension
     * @see FileFilter#accept
     */
    public String getExtension(File f) {
        if (f != null) {
            String filename = f.getName();
            int i = filename.lastIndexOf('.');
            if (i > 0 && i < filename.length() - 1) {
                return filename.substring(i + 1).toLowerCase();
            }
            ;
        }
        return null;
    }
}
