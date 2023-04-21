package net.sf.jga.swing.spreadsheet;

import java.awt.event.ActionEvent;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.MessageFormat;
import javax.swing.Action;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import net.sf.jga.fn.BinaryFunctor;
import net.sf.jga.fn.Generator;
import net.sf.jga.fn.UnaryFunctor;
import net.sf.jga.fn.adaptor.ConstantBinary;
import net.sf.jga.fn.adaptor.ConstantUnary;
import net.sf.jga.fn.adaptor.Identity;
import net.sf.jga.fn.adaptor.Project2nd;
import net.sf.jga.parser.JFXGParser;
import net.sf.jga.parser.ParseException;
import net.sf.jga.swing.GenericAction;

/**
 * Provides for generally common operations on a spreadsheet when it is in
 * an application.
 * <p>
 * Copyright &copy; 2004-2005  David A. Hall
 * @author <a href="mailto:davidahall@users.sf.net">David A. Hall</a>
 */
public class Controller {

    private Spreadsheet _sheet;

    private JFXGParser _parser;

    private BinaryFunctor<String, String, String> _promptFn = new ConstantBinary<String, String, String>("");

    private BinaryFunctor<String, String, Integer> _confirmFn = new ConstantBinary<String, String, Integer>(null);

    private BinaryFunctor<String, String, ?> _errorFn = new ConstantBinary<String, String, String>("");

    private UnaryFunctor<Spreadsheet, Integer> _loadFn = new ConstantUnary<Spreadsheet, Integer>(null);

    private BinaryFunctor<Spreadsheet, Boolean, Integer> _saveFn = new ConstantBinary<Spreadsheet, Boolean, Integer>(null);

    public static final int YES_OPTION = JOptionPane.YES_OPTION;

    public static final int NO_OPTION = JOptionPane.NO_OPTION;

    public static final int CANCEL_OPTION = JOptionPane.CANCEL_OPTION;

    /**
     * Builds a Controller to control the given sheet widget.
     */
    public Controller(Spreadsheet sheet) {
        _sheet = sheet;
        _parser = new JFXGParser();
        _parser.bindThis(this);
    }

    /**
     * Prompts the user for a string value.
     * @param msg the description of the string to be shown to the user
     * @param val the default or existing value of the string
     */
    public String prompt(String msg, String val) {
        return _promptFn.fn(msg, val);
    }

    /**
     * Sets the functor used to prompt the user for simple information.
     */
    public void setPromptFunctor(BinaryFunctor<String, String, String> fn) {
        _promptFn = fn;
    }

    /**
     * Asks the user a Yes/No/Cancel question.
     * @return a value taken fro
     */
    public int confirm(String msg, String title) {
        return _confirmFn.fn(msg, title);
    }

    /**
     * Sets the functor used to ask the user a yes/no/cancel question
     */
    public void setConfirmFunctor(BinaryFunctor<String, String, Integer> fn) {
        _confirmFn = fn;
    }

    /**
     * Displays an error message to the user
     */
    public void notify(String msg, String title) {
        _errorFn.fn(msg, title);
    }

    /**
     * Sets the functor used to display an error message.
     */
    public void setErrorFunctor(BinaryFunctor<String, String, ?> fn) {
        _errorFn = fn;
    }

    /**
     * Loads a spreadsheet
     */
    public int loadSheet(Spreadsheet sheet) {
        return _loadFn.fn(sheet);
    }

    /**
     * Sets the functor used to get an output stream
     */
    public void setLoadFunctor(UnaryFunctor<Spreadsheet, Integer> fn) {
        _loadFn = fn;
    }

    /**
     * Saves a spreadsheet
     */
    public int saveSheet(Spreadsheet sheet, boolean prompt) {
        return _saveFn.fn(sheet, prompt);
    }

    /**
     * Sets the functor used to get an output stream
     */
    public void setSaveFunctor(BinaryFunctor<Spreadsheet, Boolean, Integer> fn) {
        _saveFn = fn;
    }

    /**
     * Returns an Action that resets the spreadsheet to default state.
     */
    public Action getFileNewCmd() {
        return new GenericAction(parseAction("this.newWorksheet()"), "New");
    }

    /**
     * Returns an Action that sets the spreadsheet to the contents of a file.
     */
    public Action getFileOpenCmd() {
        return new GenericAction(parseAction("this.openFile()"), "Open");
    }

    /**
     * Returns an Action that saves the spreadsheet to the file from whence it came.
     */
    public Action getFileSaveCmd() {
        return new GenericAction(parseAction("this.saveFile()"), "Save");
    }

    /**
     * Returns an Action that saves the spreadsheet to a file.
     */
    public Action getFileSaveAsCmd() {
        return new GenericAction(parseAction("this.saveFileAs()"), "Save As...");
    }

    /**
     * Returns an Action that changes the default cell format.
     */
    public Action getDefaultEditableCmd() {
        GenericAction cmd = new GenericAction(new Identity<ActionEvent>(), "Editable");
        cmd.setEnabled(false);
        return cmd;
    }

    /**
     * Returns an Action that changes the default cell type.
     */
    public Action getDefaultTypeCmd() {
        return new GenericAction(parseAction("this.setDefaultType()"), "Cell Type");
    }

    /**
     * Returns an Action that changes the default cell type.
     */
    public Action getDefaultValueCmd() {
        return new GenericAction(parseAction("this.setDefaultValue()"), "Cell Value");
    }

    /**
     * Returns an Action that changes the default cell format.
     */
    public Action getDefaultFormatCmd() {
        GenericAction cmd = new GenericAction(new Identity<ActionEvent>(), "Format");
        cmd.setEnabled(false);
        return cmd;
    }

    /**
     * Returns an Action that changes the default cell renderer.
     */
    public Action getDefaultRendererCmd() {
        GenericAction cmd = new GenericAction(new Identity<ActionEvent>(), "Renderer");
        cmd.setEnabled(false);
        return cmd;
    }

    /**
     * Returns an Action that changes the default cell editor.
     */
    public Action getDefaultEditorCmd() {
        GenericAction cmd = new GenericAction(new Identity<ActionEvent>(), "Editor");
        cmd.setEnabled(false);
        return cmd;
    }

    /**
     * Returns an Action that resizes the worksheet.
     */
    public Action getSheetColumnsCmd() {
        return new GenericAction(parseAction("this.setColumnCount()"), "Set Column Count");
    }

    /**
     * Returns an Action that resizes the worksheet.
     */
    public Action getSheetRowsCmd() {
        return new GenericAction(parseAction("this.setRowCount()"), "Set Row Count");
    }

    /**
     * Returns an Action that allows the user to import a class.
     */
    public Action getImportClassCmd() {
        GenericAction cmd = new GenericAction(new Identity<ActionEvent>(), "Import Class");
        cmd.setEnabled(false);
        return cmd;
    }

    /**
     * Returns an Action that renames the current cell.
     */
    @SuppressWarnings("unchecked")
    public Action getCellRenameCmd() {
        String cellNameExp = "this.setCellName(x.getSelectedRow(),x.getSelectedColumn())";
        try {
            UnaryFunctor<ActionEvent, Object> cellNameFn = new Project2nd<ActionEvent, Object>().generate2nd((Generator<Object>) _parser.parseUnary(cellNameExp, Spreadsheet.class).bind(_sheet));
            return new GenericAction(cellNameFn, "Set Name");
        } catch (ParseException x) {
            x.printStackTrace();
            GenericAction cmd = new GenericAction(new ConstantUnary<ActionEvent, Object>(null), "Set Name");
            cmd.setEnabled(false);
            return cmd;
        }
    }

    /**
     * Returns an Action that reformats the current cell.
     */
    public Action getCellFormatCmd() {
        GenericAction cmd = new GenericAction(new Identity<ActionEvent>(), "Set Format");
        cmd.setEnabled(false);
        return cmd;
    }

    /**
     * Returns an Action that changes the type of the current cell.
     */
    public Action getCellTypeCmd() {
        GenericAction cmd = new GenericAction(new Identity<ActionEvent>(), "Set Type");
        cmd.setEnabled(false);
        return cmd;
    }

    /**
     * Returns an Action that changes the type of the current cell.
     */
    public Action getCellRendererCmd() {
        GenericAction cmd = new GenericAction(new Identity<ActionEvent>(), "Set Renderer");
        cmd.setEnabled(false);
        return cmd;
    }

    /**
     * Returns an Action that changes the type of the current cell.
     */
    public Action getCellEditorCmd() {
        GenericAction cmd = new GenericAction(new Identity<ActionEvent>(), "Set Editor");
        cmd.setEnabled(false);
        return cmd;
    }

    /**
     * Returns an Action that changes the type of the current cell.
     */
    public Action getCellValidatorCmd() {
        GenericAction cmd = new GenericAction(new Identity<ActionEvent>(), "Set Validator");
        cmd.setEnabled(false);
        return cmd;
    }

    /**
     * Prompts the user for the cell type.
     */
    public void setDefaultType() {
        String prompt = "Enter default type for uninitialized cells";
        String name = _promptFn.fn(prompt, _sheet.getDefaultCellType().getName());
        if (name == null) return;
        try {
            _sheet.setDefaultCellType(Class.forName(name));
        } catch (ClassNotFoundException x) {
            String fmt = "Class {0} not found";
            String msg = MessageFormat.format(fmt, new Object[] { x.getMessage() });
            _errorFn.fn(msg, "ClassNotFoundException");
        }
    }

    /**
     * Prompts the user for the number of columns
     */
    public void setColumnCount() {
        String prompt = "Enter the number of columns";
        String countstr = _promptFn.fn(prompt, String.valueOf(_sheet.getColumnCount()));
        if (countstr == null) return;
        try {
            _sheet.setColumnCount(Integer.parseInt(countstr));
        } catch (NumberFormatException x) {
            String fmt = "{0} isn't a number";
            String msg = MessageFormat.format(fmt, new Object[] { countstr });
            _errorFn.fn(msg, "NumberFormatException");
        }
    }

    /**
     * Prompts the user for the number of rows
     */
    public void setRowCount() {
        String prompt = "Enter the number of rows";
        String countstr = _promptFn.fn(prompt, String.valueOf(_sheet.getRowCount()));
        if (countstr == null) return;
        try {
            _sheet.setRowCount(Integer.parseInt(countstr));
        } catch (NumberFormatException x) {
            String fmt = "{0} isn't a number";
            String msg = MessageFormat.format(fmt, new Object[] { countstr });
            _errorFn.fn(msg, "NumberFormatException");
        }
    }

    /**
     * Prompts the user for the default cell value.
     */
    public void setDefaultValue() {
        String prompt = "Enter default value for uninitialized cells";
        String exp = _promptFn.fn(prompt, "" + _sheet.getDefaultCellValue());
        if (exp == null) return;
        try {
            Generator<?> gen = _sheet.getParser().parseGenerator(exp);
            _sheet.setDefaultCellValue(gen.fn());
        } catch (ParseException x) {
            _errorFn.fn(x.getMessage(), getExceptionName(getRootCause(x)));
        }
    }

    /**
     * Prompts the user for the name of the designated cell.
     */
    public void setCellName(int row, int col) {
        String prompt = "Enter name for cell(" + row + "," + col + ")";
        Cell cell = _sheet.getCellIfPresent(row, col);
        String oldName = (cell != null) ? cell.getName() : "";
        String name = _promptFn.fn(prompt, oldName);
        if (name != null && !name.equals(oldName)) _sheet.setCellName(name, row, col);
    }

    /**
     * Creates a new, empty spreadsheet with default settings.
     */
    public void newWorksheet() {
        int ans = JOptionPane.YES_OPTION;
        if (isSheetDirty()) {
            ans = promptAndSave();
        }
        if (ans != JOptionPane.CANCEL_OPTION) {
            _sheet.clear();
            setSheetSource(null);
            setSheetDirty(false);
        }
    }

    /**
     * Prompts the user for a file to open, and sets the spreadsheet to the file's contents.
     */
    public int openFile() {
        return loadSheet(_sheet);
    }

    /**
     */
    public int saveFile() {
        return saveSheet(_sheet, false);
    }

    /**
     */
    public int saveFileAs() {
        return saveSheet(_sheet, true);
    }

    /**
     * Prompts the user using a supplied functor, and returns one of YES, NO, or CANCEL
     */
    public int promptAndSave() {
        String format = "save {0}?";
        Object name = getSheetSource();
        if (name == null) name = "worksheet";
        String msg = MessageFormat.format(format, new Object[] { name });
        int ans = confirm(msg, msg);
        if (ans == Controller.YES_OPTION) {
            int choice = saveFile();
            if (choice == Controller.CANCEL_OPTION || choice == JFileChooser.ERROR_OPTION) {
                ans = Controller.CANCEL_OPTION;
            }
        }
        return ans;
    }

    private final String DIRTY_PROP = "dirty";

    public boolean isSheetDirty() {
        Object dirtyProp = _sheet.getClientProperty(DIRTY_PROP);
        return Boolean.TRUE.equals(dirtyProp);
    }

    public void setSheetDirty(boolean flag) {
        _sheet.putClientProperty(DIRTY_PROP, Boolean.valueOf(flag));
    }

    private final String SOURCE_PROP = "source";

    public URL getSheetSource() {
        Object sourceURL = _sheet.getClientProperty(SOURCE_PROP);
        if (sourceURL == null) return null;
        if (sourceURL instanceof URL) return (URL) sourceURL;
        try {
            return new File(new File("."), "worksheet1.hwks").toURI().toURL();
        } catch (MalformedURLException x) {
            x.printStackTrace();
            return null;
        }
    }

    public void setSheetSource(URL url) {
        _sheet.putClientProperty(SOURCE_PROP, url);
    }

    @SuppressWarnings("unchecked")
    private UnaryFunctor<ActionEvent, ?> parseAction(String exp) {
        try {
            return new Project2nd<ActionEvent, Object>().generate2nd((Generator<Object>) _parser.parseGenerator(exp));
        } catch (ParseException x) {
            x.printStackTrace();
            return null;
        }
    }

    static Throwable getRootCause(Throwable t) {
        for (Throwable t1 = t.getCause(); t1 != null; t1 = t.getCause()) t = t1;
        return t;
    }

    static String getExceptionName(Throwable t) {
        String fqcn = t.getClass().getName();
        return fqcn.substring(fqcn.lastIndexOf(".") + 1);
    }
}
