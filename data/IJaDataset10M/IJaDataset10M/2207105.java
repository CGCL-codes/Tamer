package org.boffyflow.ru.jathlete.gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.io.*;
import org.boffyflow.ru.jathlete.bin.*;
import org.boffyflow.ru.jathlete.gui.*;
import org.boffyflow.ru.util.*;

/********************************************************************
 <pre>
 <B>MainFrame</B>

 @version        1.0
 @author         Robert Uebbing
 
 Creation Date : 25-Jan-2001
  
 </pre>
********************************************************************/
public class MainFrame extends JFrame implements ActionListener {

    private ResourceBundle _mess;

    private ResourceBundle _strings;

    private ResourceBundle _tooltips;

    EditDayDialog _editDay = null;

    public MainFrame() {
        _strings = ResourceBundle.getBundle("org/boffyflow/ru/jathlete/props/jAthlete", jAthlete.getLocale());
        _mess = ResourceBundle.getBundle("org/boffyflow/ru/jathlete/props/jAthleteMessages", jAthlete.getLocale());
        _tooltips = ResourceBundle.getBundle("org/boffyflow/ru/jathlete/props/jAthleteToolTips", jAthlete.getLocale());
        JToolBar toolbar = new JToolBar();
        Container cpane = getContentPane();
        cpane.add(toolbar, BorderLayout.NORTH);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        addWindowListener(new WindowCloseListener());
        JButton button;
        button = new JButton(new ImageIcon("org/boffyflow/ru/jathlete/icons/New24.gif"));
        button.setActionCommand("NewFile");
        button.addActionListener(this);
        button.setToolTipText(_tooltips.getString("button.new"));
        toolbar.add(button);
        button = new JButton(new ImageIcon("org/boffyflow/ru/jathlete/icons/Open24.gif"));
        button.setActionCommand("OpenFile");
        button.addActionListener(this);
        button.setToolTipText(_tooltips.getString("button.open"));
        toolbar.add(button);
        button = new JButton(new ImageIcon("org/boffyflow/ru/jathlete/icons/Save24.gif"));
        button.setActionCommand("SaveFile");
        button.addActionListener(this);
        button.setToolTipText(_tooltips.getString("button.save"));
        toolbar.add(button);
        button = new JButton(new ImageIcon("org/boffyflow/ru/jathlete/icons/SaveAs24.gif"));
        button.setActionCommand("SaveAsFile");
        button.addActionListener(this);
        button.setToolTipText(_tooltips.getString("button.saveas"));
        toolbar.add(button);
        toolbar.add(new JToolBar.Separator());
        button = new JButton(new ImageIcon("org/boffyflow/ru/jathlete/icons/Diary24.gif"));
        button.setActionCommand("EditDay");
        button.addActionListener(this);
        button.setToolTipText(_tooltips.getString("button.editday"));
        toolbar.add(button);
        jAthleteMenuBar menuBar = new jAthleteMenuBar(this);
        setJMenuBar(menuBar);
        _editDay = new EditDayDialog(this, new GregorianCalendar());
        restoreWindowSettings();
    }

    public void actionPerformed(ActionEvent e) {
        String str = e.getActionCommand();
        if (str == "Quit") {
            if (cleanUp()) {
                jAthlete.exit(1);
            }
        } else if (str == "Preferences") {
        } else if (str == "About") {
        } else if (str == "NewFile") {
            newFile();
        } else if (str == "OpenFile") {
            openFile(true);
        } else if (str == "SaveFile") {
            if (!jAthlete.getDataFileName().equals("")) {
                saveFile(false);
            } else {
                saveFile(true);
            }
        } else if (str == "SaveAsFile") {
            saveFile(true);
        } else if (str == "EditDay") {
            if (_editDay.isShowing()) _editDay.hide(); else _editDay.show();
        }
    }

    public void newFile() {
    }

    public boolean openFile(boolean prompt) {
        if (cleanUp()) {
            String curDir = jAthlete.getProperty("current_dir", System.getProperty("user.dir"));
            if (prompt) {
                JFileChooser fc = new JFileChooser();
                ExtensionFileFilter filter = new ExtensionFileFilter("jAthlete (*.jat; *.xml)");
                filter.addExtension("jat");
                filter.addExtension("xml");
                fc.setFileFilter(filter);
                fc.setCurrentDirectory(new File(curDir));
                int returnVal = fc.showOpenDialog(this);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    jAthlete.setDataFileName(fc.getSelectedFile().getAbsolutePath());
                } else {
                    return false;
                }
            }
            try {
                jAthlete.openDataFile();
                jAthlete.setProperty("current_dir", curDir);
                jAthlete.setProperty("datafile", jAthlete.getDataFileName());
                jAthlete.setDocDirty(false);
            } catch (IOException io) {
                String mes = jAthlete.getDataFileName() + ": " + _mess.getString("openfailedxml");
                JOptionPane.showMessageDialog(this, mes);
            }
        }
        update(true);
        return true;
    }

    private boolean saveFile(boolean prompt) {
        String curDir = jAthlete.getProperty("current_dir", System.getProperty("user.dir"));
        if (prompt) {
            JFileChooser fc = new JFileChooser();
            fc.setCurrentDirectory(new File(curDir));
            int returnVal = fc.showSaveDialog(this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                jAthlete.setDataFileName(fc.getSelectedFile().getAbsolutePath());
                curDir = fc.getCurrentDirectory().getAbsolutePath();
            } else {
                return false;
            }
        }
        try {
            jAthlete.saveDataFile();
            jAthlete.setProperty("current_dir", curDir);
            jAthlete.setProperty("datafile", jAthlete.getDataFileName());
            jAthlete.setDocDirty(false);
        } catch (IOException io) {
            String mes = jAthlete.getDataFileName() + ": " + _mess.getString("savefailedxml");
            JOptionPane.showMessageDialog(this, mes);
            return false;
        }
        return true;
    }

    public void update(boolean flag) {
        refreshTitle();
        _editDay.updatePanes();
    }

    public class WindowCloseListener extends WindowAdapter {

        /**
		 * Invoked when a window has been closed as the result
		 * of calling dispose on the window.
		 */
        public void windowClosed(WindowEvent e) {
            if (cleanUp()) {
                jAthlete.exit(0);
            }
        }
    }

    public void refreshTitle() {
        String suffix = "";
        if (jAthlete.isDocDirty()) {
            suffix = " *";
        }
        this.setTitle(jAthlete.getNameAndVersion() + ": " + jAthlete.getDataFileName() + suffix);
    }

    private boolean cleanUp() {
        boolean status = true;
        if (jAthlete.isDocDirty()) {
            Object[] options = { _strings.getString("yes"), _strings.getString("no"), _strings.getString("cancel") };
            Toolkit.getDefaultToolkit().beep();
            int n = JOptionPane.showOptionDialog(this, _mess.getString("savebeforeclosing"), _strings.getString("warning"), JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[2]);
            if (n == JOptionPane.YES_OPTION) {
                if (!jAthlete.getDataFileName().equals("")) {
                    status = saveFile(false);
                } else {
                    status = saveFile(true);
                }
            } else if (n == JOptionPane.NO_OPTION) {
                status = true;
            } else if (n == JOptionPane.CANCEL_OPTION) {
                status = false;
            }
        }
        if (status) {
            saveWindowSettings();
        }
        return status;
    }

    private void saveWindowSettings() {
        Dimension dim = getSize();
        Point loc = getLocation();
        boolean show_edit = _editDay.isShowing();
        jAthlete.setProperty("MainWindow.width", Integer.toString(dim.width));
        jAthlete.setProperty("MainWindow.height", Integer.toString(dim.height));
        jAthlete.setProperty("MainWindow.x", Integer.toString(loc.x));
        jAthlete.setProperty("MainWindow.y", Integer.toString(loc.y));
        jAthlete.setProperty("EditDayDialog.showing", new Boolean(show_edit).toString());
        _editDay.saveDialogSettings();
    }

    private void restoreWindowSettings() {
        int w = Integer.parseInt(jAthlete.getProperty("MainWindow.width", "740"));
        int h = Integer.parseInt(jAthlete.getProperty("MainWindow.height", "600"));
        int x = Integer.parseInt(jAthlete.getProperty("MainWindow.x", "100"));
        int y = Integer.parseInt(jAthlete.getProperty("MainWindow.y", "100"));
        boolean show_edit = new Boolean(jAthlete.getProperty("EditDayDialog.showing", "False")).booleanValue();
        setSize(w, h);
        setLocation(x, y);
        show();
        if (show_edit) _editDay.show(new GregorianCalendar());
    }
}
