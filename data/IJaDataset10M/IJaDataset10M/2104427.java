package example.gui.util;

import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import debug.AbstractDebuggable;

public abstract class VeryAbstractExample extends AbstractDebuggable {

    private File selectedClassFile;

    private File selectedWrapperOptionsFile;

    private JFrame frame;

    public VeryAbstractExample(String classPathAndFilename, String wrapperOptionsPathAndFilename) {
        selectedClassFile = new File(classPathAndFilename).getAbsoluteFile();
        selectedWrapperOptionsFile = new File(wrapperOptionsPathAndFilename).getAbsoluteFile();
        frame = createFrame();
        registerListeners();
    }

    protected abstract JFrame createFrame();

    protected abstract void registerListeners();

    protected abstract void update(boolean fresh);

    protected abstract void updateAST();

    protected abstract void updateLineWrapperOptions();

    protected void changeClass() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new FileFilter() {

            @Override
            public boolean accept(File f) {
                if (f.isDirectory()) {
                    return true;
                }
                String name = f.getName();
                String extension = name.substring(name.lastIndexOf(".") + 1);
                if (extension.equals("java")) {
                    return true;
                }
                return false;
            }

            @Override
            public String getDescription() {
                return "Java Source Code Files";
            }
        });
        chooser.setSelectedFile(selectedClassFile);
        int result = chooser.showOpenDialog(frame);
        if (result == JFileChooser.APPROVE_OPTION) {
            selectedClassFile = chooser.getSelectedFile();
            updateAST();
            update(true);
        }
    }

    protected void changeLineWrapperOptions() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new FileFilter() {

            @Override
            public boolean accept(File f) {
                if (f.isDirectory()) {
                    return true;
                }
                String name = f.getName();
                String extension = name.substring(name.lastIndexOf(".") + 1);
                if (extension.equals("xml")) {
                    return true;
                }
                return false;
            }

            @Override
            public String getDescription() {
                return "Line Wrapper Options Configuration Files";
            }
        });
        chooser.setSelectedFile(selectedWrapperOptionsFile);
        int result = chooser.showOpenDialog(frame);
        if (result == JFileChooser.APPROVE_OPTION) {
            selectedWrapperOptionsFile = chooser.getSelectedFile();
            updateLineWrapperOptions();
            update(true);
        }
    }

    public void run() {
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    protected void displayException(Exception e) {
        JOptionPane.showMessageDialog(frame, e.getMessage(), "Exception Caught", JOptionPane.WARNING_MESSAGE);
    }

    protected JFrame getFrame() {
        return frame;
    }

    protected File getSelectedClassFile() {
        return selectedClassFile;
    }

    protected File getSelectedWrapperOptionsFile() {
        return selectedWrapperOptionsFile;
    }
}
