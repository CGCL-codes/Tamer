package com.crumbach.jcycledata.gui;

import com.crumbach.jcycledata.base.LogEntry;
import com.crumbach.jcycledata.base.Activity;
import com.crumbach.jcycledata.base.Cyclist;
import com.crumbach.jcycledata.base.JCDResourceManager;
import com.crumbach.jcycledata.base.ResourceKeys;
import com.crumbach.jcycledata.base.output.*;
import com.crumbach.tools.ResourceManager;
import com.crumbach.tools.gui.PrintPreview;
import com.crumbach.tools.gui.ProgressDisplay;
import com.crumbach.tools.gui.SwingWorker;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.print.PageFormat;
import java.awt.print.PrinterJob;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *  DisplayController provides the needed Action objects. This class is used to
 *  encapsulate the view controllers like the Add-/Edit-/Delete-Actions
 *
 * @author     Manfred Crumbach
 * @created    19. November 2001
 */
class DisplayController implements ChangeListener {

    Cyclist _cyclist = null;

    Display _display = null;

    private Action actionNew;

    private Action actionOpen;

    private Action actionOpenWindow;

    private Action actionSave;

    private Action actionSaveAs;

    private Action actionSaveAsHTML;

    private Action actionSaveAsPDF;

    private Action actionExit;

    private Action actionAbout;

    private Action actionAdd;

    private Action actionEdit;

    private Action actionDelete;

    private Action actionPrint;

    private Action actionPreview;

    private final JFileChooser jFC = new JFileChooser(".");

    private final ResourceManager rm = JCDResourceManager.getTheInstance();

    /**
    *  initialise all actions (Add, Edit, Delete, Print, ...). Names for Actions
    *  should be self explanatory.
    *
    * @param  cyclist  Cyclist to be displayed
    * @param  display  Display to be controlled
    */
    public DisplayController(Cyclist cyclist, Display display) {
        super();
        _cyclist = cyclist;
        _display = display;
        String[] gFFext = { ".jcd" };
        jFC.setFileFilter(new com.crumbach.tools.GenericFileFilter(gFFext, rm.getString(ResourceKeys.FILE_TYPE_JCD), true));
        actionNew = new AbstractAction(rm.getString(ResourceKeys.NEW), IconLocator.getIcon(ResourceKeys.ACTION_ICON_NEW)) {

            /**
                  *  re-initialise Cyclist
                  *
                  * @param  e  ActionEvent
                  */
            public void actionPerformed(ActionEvent e) {
                resetCyclist();
            }
        };
        actionOpen = new AbstractAction(rm.getString(ResourceKeys.OPEN), IconLocator.getIcon(ResourceKeys.ACTION_ICON_OPEN)) {

            /**
                  *  load new Cyclist in current window
                  *
                  * @param  e  ActionEvent
                  */
            public void actionPerformed(ActionEvent e) {
                if (jFC.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    resetCyclist();
                    loadCyclist(jFC.getSelectedFile().getAbsolutePath());
                }
            }
        };
        actionOpenWindow = new AbstractAction(rm.getString(ResourceKeys.OPEN_WINDOW), IconLocator.getIcon(ResourceKeys.ACTION_ICON_OPEN)) {

            /**
                  *  load new Cyclist in new window
                  *
                  * @param  e  ActionEvent
                  */
            public void actionPerformed(ActionEvent e) {
                if (jFC.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    new Display(jFC.getSelectedFile().getAbsolutePath());
                }
            }
        };
        actionSave = new AbstractAction(rm.getString(ResourceKeys.SAVE), IconLocator.getIcon(ResourceKeys.ACTION_ICON_SAVE)) {

            /**
                  *  save changes
                  *
                  * @param  e  ActionEvent
                  */
            public void actionPerformed(ActionEvent e) {
                try {
                    saveCyclist(rm.getResource(ResourceKeys.TO_XML).toURI());
                } catch (URISyntaxException ex) {
                    Logger.getLogger(DisplayController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        };
        actionSave.setEnabled(false);
        actionSave.putValue(Action.SHORT_DESCRIPTION, rm.getString(ResourceKeys.SAVE_DESCR));
        actionSaveAs = new AbstractAction(rm.getString(ResourceKeys.SAVE_AS), null) {

            /**
                  *  save Cyclist in new file
                  *
                  * @param  e  ActionEvent
                  */
            public void actionPerformed(ActionEvent e) {
                try {
                    String jCD;
                    if (jFC.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
                        jCD = jFC.getSelectedFile().getAbsolutePath();
                        _cyclist.setFile(jCD);
                        saveCyclist(rm.getResource(ResourceKeys.TO_XML).toURI());
                    }
                } catch (Exception exc) {
                    JOptionPane.showMessageDialog(null, rm.getString(ResourceKeys.ERROR_WRITE) + exc);
                    exc.printStackTrace();
                }
            }
        };
        actionSaveAsHTML = new AbstractAction(rm.getString(ResourceKeys.SAVE_HTML), null) {

            /**
                  *  save Cyclist in a HTML file
                  *
                  * @param  e  ActionEvent
                  */
            public void actionPerformed(ActionEvent e) {
                try {
                    String jCD;
                    if (jFC.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
                        _display.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                        jCD = jFC.getSelectedFile().getAbsolutePath();
                        OutputConverter converter = ConverterFactory.getConverter(ConverterFactory.HTML, _cyclist);
                        converter.convert(new FileOutputStream(jCD));
                        _display.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                    }
                } catch (IOException exc) {
                    JOptionPane.showMessageDialog(null, rm.getString(ResourceKeys.ERROR_WRITE) + exc);
                }
            }
        };
        actionSaveAsHTML.putValue(Action.SHORT_DESCRIPTION, rm.getString(ResourceKeys.SAVE_HTML_DESCR));
        actionSaveAsPDF = new AbstractAction(rm.getString(ResourceKeys.SAVE_PDF), null) {

            /**
                  *  save Cyclist in a PDF document
                  *
                  * @param  e  ActionEvent
                  */
            public void actionPerformed(ActionEvent e) {
                try {
                    String jCD;
                    if (jFC.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
                        _display.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                        jCD = jFC.getSelectedFile().getAbsolutePath();
                        OutputConverter converter = ConverterFactory.getConverter(ConverterFactory.PDF, _cyclist);
                        converter.convert(new FileOutputStream(jCD));
                        _display.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                    }
                } catch (IOException exc) {
                    JOptionPane.showMessageDialog(null, rm.getString(ResourceKeys.ERROR_WRITE) + exc);
                }
            }
        };
        actionExit = new AbstractAction(rm.getString(ResourceKeys.EXIT)) {

            /**
                  *  perform an orderly shutdown
                  *
                  * @param  e  ActionEvent
                  */
            public void actionPerformed(ActionEvent e) {
                _display.shutdown(null);
            }
        };
        actionAdd = new AbstractAction(rm.getString(ResourceKeys.ADD), IconLocator.getIcon(ResourceKeys.ACTION_ICON_ADD)) {

            /**
                  *  add an LogEntry - here: activities
                  *
                  * @param  e  ActionEvent
                  */
            public void actionPerformed(ActionEvent e) {
                LogEntry logEntry = Activity.factory(com.crumbach.jcycledata.base.ActivityType.TRAINING, _cyclist);
                _display.selectLogEntry(logEntry);
            }
        };
        actionEdit = new AbstractAction(rm.getString(ResourceKeys.EDIT), IconLocator.getIcon(ResourceKeys.ACTION_ICON_EDIT)) {

            /**
                  *  Edit LogEntry
                  *
                  * @param  e  ActionEvent
                  */
            public void actionPerformed(ActionEvent e) {
                LogEntry logEntry = _display.getSelectedLogEntry();
                if (logEntry != null) {
                    logEntry.customize();
                }
            }
        };
        actionDelete = new AbstractAction(rm.getString(ResourceKeys.DELETE), IconLocator.getIcon(ResourceKeys.ACTION_ICON_DELETE)) {

            /**
                  *  Delete LogEntry
                  *
                  * @param  e  ActionEvent
                  */
            public void actionPerformed(ActionEvent e) {
                LogEntry logEntry = _display.getSelectedLogEntry();
                if (logEntry != null) {
                    _cyclist.delete(logEntry);
                }
            }
        };
        actionPrint = new AbstractAction(rm.getString(ResourceKeys.PRINT), IconLocator.getIcon(ResourceKeys.ACTION_ICON_PRINT)) {

            /**
                  *  print currently selected Tab
                  *
                  * @param  e  ActionEvent
                  */
            public void actionPerformed(ActionEvent e) {
                PrinterJob pj = PrinterJob.getPrinterJob();
                pj.setPageable(_display);
                pj.validatePage(_display.getPageFormat(0));
                if (pj.printDialog()) {
                    try {
                        pj.print();
                    } catch (Exception printException) {
                        printException.printStackTrace();
                    }
                }
            }
        };
        actionPreview = new AbstractAction(rm.getString(ResourceKeys.PRINT_PREVIEW)) {

            /**
                  *  Display a preview window before printing
                  *
                  * @param  e  ActionEvent
                  */
            public void actionPerformed(ActionEvent e) {
                Thread runner = new Thread() {

                    public void run() {
                        if (_display.getPrintable(0) != null) {
                            _display.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                            new PrintPreview(_display.getPrintable(0), rm.getString(ResourceKeys.PRINT_PREVIEW_LANDSCAPE), PageFormat.LANDSCAPE, IconLocator.getIcon(ResourceKeys.ACTION_ICON_PRINT));
                            _display.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                        }
                    }
                };
                runner.start();
            }
        };
        actionAbout = new AbstractAction(rm.getString(ResourceKeys.ABOUT), null) {

            /**
                  *  display a dialog with information on JCycleData
                  *
                  * @param  e  ActionEvent
                  */
            public void actionPerformed(ActionEvent e) {
                AboutDG about = new AboutDG(null, true);
                about.setVisible(true);
            }
        };
    }

    /**
    *  Sets the Cyclist attribute of the DisplayController object
    *
    * @param  cyclist  The new Cyclist value
    */
    public void setCyclist(Cyclist cyclist) {
        _cyclist = cyclist;
        actionSave.setEnabled(false);
    }

    /**
    *  Gets the Action to create a new Cyclist file
    *
    * @return    The Action New
    */
    public Action getActionNew() {
        return actionNew;
    }

    /**
    *  Gets the Action to Open a Cyclist file
    *
    * @return    The Action Open
    */
    public Action getActionOpen() {
        return actionOpen;
    }

    /**
    *  Gets the Action to Open a Cyclist file in a new Window
    *
    * @return    The Action OpenWindow
    */
    public Action getActionOpenWindow() {
        return actionOpenWindow;
    }

    /**
    *  Gets the Action to Save the Cyclist's file
    *
    * @return    The Action Save
    */
    public Action getActionSave() {
        return actionSave;
    }

    /**
    *  Gets the Action to Save the Cyclist's file under a different name
    *
    * @return    The Action Save As
    */
    public Action getActionSaveAs() {
        return actionSaveAs;
    }

    /**
    *  Gets the Action to Save the Cyclist's file as HTML
    *
    * @return    The Action Save As HTML
    */
    public Action getActionSaveAsHTML() {
        return actionSaveAsHTML;
    }

    /**
    *  Gets the Action to Save the Cyclist's file as PDF
    *
    * @return    The Action Save As PDF
    */
    public Action getActionSaveAsPDF() {
        return actionSaveAsPDF;
    }

    /**
    *  Gets the Action to Exit the Application
    *
    * @return    The Action Exit
    */
    public Action getActionExit() {
        return actionExit;
    }

    /**
    *  Gets the Action to display an About dialogue
    *
    * @return    The Action About dialogue
    */
    public Action getActionAbout() {
        return actionAbout;
    }

    /**
    *  Gets the Action to Print
    *
    * @return    The Action Print
    */
    public Action getActionPrint() {
        return actionPrint;
    }

    /**
    *  Gets the Action to display a Print Preview
    *
    * @return    The Action Print Preview
    */
    public Action getActionPreview() {
        return actionPreview;
    }

    /**
    *  Gets the Action to Add an LogEntry
    *
    * @return    The Action Add an LogEntry
    */
    public Action getActionAdd() {
        return actionAdd;
    }

    /**
    *  Gets the Action to Edit an LogEntry
    *
    * @return    The Action Edit an LogEntry
    */
    public Action getActionEdit() {
        return actionEdit;
    }

    /**
    *  Gets the Action to Delete an LogEntry
    *
    * @return    The Action Delete an LogEntry
    */
    public Action getActionDelete() {
        return actionDelete;
    }

    /**
    *  the ChangeListener reacts to changes of the selected JTabbedPane and
    *  controls the state of Edit/Delete LogEntry Buttons
    *
    * @param  e  ChangeEvent
    */
    public void stateChanged(ChangeEvent e) {
        int idx = 0;
        if (idx == 0) {
            actionDelete.setEnabled(true);
            actionEdit.setEnabled(true);
        } else {
            actionDelete.setEnabled(false);
            actionEdit.setEnabled(false);
        }
        if (_cyclist.hasChanged()) {
            actionSave.setEnabled(true);
        } else {
            actionSave.setEnabled(false);
        }
    }

    /**
    *  Cyclist's data is loaded from a XML file
    *
    * @param  file  load Cyclist from this file
    */
    protected void loadCyclist(final String file) {
        ProgressDisplay progressMon = null;
        SwingWorker worker = null;
        progressMon = new ProgressDisplay(_display.getRootPane(), rm.getString(ResourceKeys.LOADING_FILE));
        progressMon.setProgressing(_cyclist);
        worker = new SwingWorker() {

            /**
                  *  construct a thread to process the XML file
                  *
                  * @return    a DOM represantation of the XML file
                  */
            public Object construct() {
                try {
                    _display.setStatus(rm.getString(ResourceKeys.LOADING_FILE));
                    _display.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                    _cyclist.read(file);
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                    JOptionPane.showMessageDialog(null, rm.getString(ResourceKeys.ERROR_LOAD) + ioe.getMessage());
                }
                return _cyclist;
            }

            /**
                  *  this method is called after processing of the XML file is done
                  */
            @Override
            public void finished() {
                _display.setCyclist(_cyclist);
                _display.setStatus(rm.getString(ResourceKeys.ACTION_DONE));
                _display.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            }
        };
        worker.start();
    }

    /**
    *  Cyclist's data is saved by processing XML data by the given XSL Style
    *  sheet.
    *
    * @param  styleSheet  two Style Sheets for XML (standard) and HTML data are
    *      currently supported.
    */
    protected void saveCyclist(final URI styleSheetURI) {
        if (styleSheetURI == null) {
            JOptionPane.showMessageDialog(null, rm.getString(ResourceKeys.XSLT_MISSING));
        } else {
            String file = _cyclist.getFile();
            if (file == null) {
                java.io.File fle;
                if (jFC.showSaveDialog(null) == javax.swing.JFileChooser.APPROVE_OPTION) {
                    fle = jFC.getSelectedFile();
                    file = fle.getAbsolutePath();
                    _cyclist.setFile(file);
                }
            }
            if (file != null) {
                _display.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                try {
                    _cyclist.write(file, styleSheetURI);
                    actionSave.setEnabled(false);
                } catch (Exception exc) {
                    JOptionPane.showMessageDialog(null, rm.getString(ResourceKeys.ERROR_WRITE) + "\n" + "styleSheet: " + styleSheetURI + "\n" + exc);
                    exc.printStackTrace();
                }
                _display.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                _display.setStatus(rm.getString(ResourceKeys.ACTION_DONE));
            }
        }
    }

    /**
    *  delete all Activities from cyclist and reset name and maxHR
    */
    private void resetCyclist() {
        _cyclist.reset();
    }
}
