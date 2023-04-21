package jeliot.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ResourceBundle;
import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.PlainDocument;
import jeliot.mcode.Highlight;
import jeliot.mcode.MCodeUtilities;
import jeliot.tracker.Tracker;
import jeliot.tracker.TrackerClock;
import jeliot.util.DebugUtil;
import jeliot.util.ResourceBundles;
import jeliot.util.UserProperties;
import org.syntax.jeliot_jedit.JEditTextArea;
import org.syntax.jeliot_jedit.tokenmarker.JavaTokenMarker;

/**
 * The simple code editor for the users to code their algorithm.
 * 
 * @author Pekka Uronen
 * @author Niko Myller
 */
public class CodeEditor2 extends JComponent {

    /**
     * The resource bundle for gui package
     */
    private static UserProperties propertiesBundle = ResourceBundles.getGuiUserProperties();

    /**
     * The resource bundle for gui package
     */
    private static ResourceBundle messageBundle = ResourceBundles.getGuiMessageResourceBundle();

    /**
     * 
     */
    private UserProperties jeliotUserProperties = ResourceBundles.getJeliotUserProperties();

    /**
     * The String for the basic code template that is shown to the user in the
     * beginning.
     */
    private String template = messageBundle.getString("code_editor.template");

    /**
     * Comment for <code>title</code>
     */
    private String title = messageBundle.getString("name") + " " + messageBundle.getString("version");

    /**
     * Tells whether or not the current file is changed since last loading or
     * saving. Used to determine when the saving dialog should be popped up.
     */
    private boolean changed = false;

    /**
     * Pointing to the current file that is edited for saving before the
     * compilation.
     */
    private File currentFile = null;

    /**
     * 
     */
    private File currentDirectory;

    {
        if (propertiesBundle.containsKey("current.user.directory")) {
            currentDirectory = new File(propertiesBundle.getStringProperty("current.user.directory"));
            if (!currentDirectory.exists()) {
                currentDirectory = null;
            }
        }
    }

    /**
     * The directory from which the program was executed.
     */
    private String udir;

    /**
     * The file chooser in which the users can load and save the program codes.
     */
    private JFileChooser fileChooser;

    /**
     * Text area that is used from the JEdit project.
     */
    private JEditTextArea area;

    /**
     * A component showing line numbers.
     */
    private LineNumbers ln;

    /**
     * 
     */
    private FindDialog findDialog;

    /**
     * 
     */
    private UndoRedoSyntaxDocument.UndoRedo undoRedo;

    /**
     * returns true if the document is changed and false if it is not changed.
     * This is the value of the changed field.
     * 
     * @return if the document is changed or not.
     */
    public boolean isChanged() {
        return changed;
    }

    /**
     * Set wheter or not the document is changed or not.
     * 
     * @param changed
     *            if true the document is changed if false the document is not
     *            changed (means that it is just loaded or saved).
     */
    public void setChanged(boolean changed) {
        if (changed && this.changed != changed && masterFrame != null && !masterFrame.getTitle().endsWith("*")) {
            masterFrame.setTitle(masterFrame.getTitle() + " *");
        }
        this.changed = changed;
    }

    /**
     * Document listener is used to handle the line numbering correctly when the
     * new line is created or the document is scrolled by the user
     */
    private DocumentListener dcl = new DocumentListener() {

        public void changedUpdate(DocumentEvent e) {
            setChanged(true);
        }

        public void insertUpdate(DocumentEvent e) {
            setChanged(true);
        }

        public void removeUpdate(DocumentEvent e) {
            setChanged(true);
        }
    };

    /**
     * The master frame.
     */
    private JFrame masterFrame;

    /**
     * ActionListener that handles the saving of the program code from the code
     * area.
     */
    private ActionListener saver = new ActionListener() {

        public void actionPerformed(ActionEvent e) {
            Tracker.trackEvent(TrackerClock.currentTimeMillis(), Tracker.BUTTON, -1, -1, "SaveButton");
            saveProgram();
        }
    };

    /**
     * ActionListener that handles the saving of the program code from the code
     * area.
     */
    private ActionListener saveAs = new ActionListener() {

        public void actionPerformed(ActionEvent e) {
            Tracker.trackEvent(TrackerClock.currentTimeMillis(), Tracker.BUTTON, -1, -1, "SaveAsButton");
            saveAsProgram();
        }
    };

    /**
     * ActionListener that handles the loading of the program code to the code
     * area.
     */
    private ActionListener loader = new ActionListener() {

        public void actionPerformed(ActionEvent e) {
            Tracker.trackEvent(TrackerClock.currentTimeMillis(), Tracker.BUTTON, -1, -1, "LoadButton");
            loadProgram();
        }
    };

    /**
     * ActionListener that handels the clearing of the code area.
     */
    private ActionListener clearer = new ActionListener() {

        public void actionPerformed(ActionEvent e) {
            Tracker.trackEvent(TrackerClock.currentTimeMillis(), Tracker.BUTTON, -1, -1, "ClearButton");
            clearProgram();
        }
    };

    /**
     * ActionListener that handels the clearing of the code area.
     */
    private ActionListener cutter = new ActionListener() {

        public void actionPerformed(ActionEvent e) {
            Tracker.trackEvent(TrackerClock.currentTimeMillis(), Tracker.BUTTON, -1, -1, "CutButton");
            area.cut();
        }
    };

    /**
     * ActionListener that handels the copying of the code area.
     */
    private ActionListener copyist = new ActionListener() {

        public void actionPerformed(ActionEvent e) {
            Tracker.trackEvent(TrackerClock.currentTimeMillis(), Tracker.BUTTON, -1, -1, "CopyButton");
            area.copy();
        }
    };

    /**
     * ActionListener that handels the pasting of the code area.
     */
    private ActionListener pasteur = new ActionListener() {

        public void actionPerformed(ActionEvent e) {
            Tracker.trackEvent(TrackerClock.currentTimeMillis(), Tracker.BUTTON, -1, -1, "�asteButton");
            area.paste();
        }
    };

    /**
     * ActionListener that handels the selection of the whole code area.
     */
    private ActionListener allSelector = new ActionListener() {

        public void actionPerformed(ActionEvent e) {
            Tracker.trackEvent(TrackerClock.currentTimeMillis(), Tracker.BUTTON, -1, -1, "SelectAllButton");
            area.requestFocusInWindow();
            area.selectAll();
        }
    };

    /**
     * Set the given frame as the masterFrame.
     * 
     * @param frame
     *            The Frame that is set as new masterFrame.
     */
    public void setMasterFrame(JFrame frame) {
        this.masterFrame = frame;
    }

    /**
     * Sets the layout and adds the JScrollPane with JTextArea area and JToolbar
     * in it. Initializes the FileChooser.
     */
    public CodeEditor2(String udir, String defaultIO) {
        this.udir = udir;
        this.template = defaultIO + "\n\n" + template;
        initFileChooser();
        UndoRedoSyntaxDocument document = new UndoRedoSyntaxDocument();
        this.undoRedo = document.getUndoredo();
        area = new JEditTextArea(true, document);
        area.setTokenMarker(new JavaTokenMarker());
        area.getPainter().setFont(new Font(propertiesBundle.getStringProperty("font.code_editor.family"), Font.PLAIN, Integer.parseInt(propertiesBundle.getStringProperty("font.code_editor.size"))));
        area.getDocument().getDocumentProperties().put(PlainDocument.tabSizeAttribute, Integer.valueOf(propertiesBundle.getStringProperty("editor.tab_size")));
        area.getDocument().addDocumentListener(dcl);
        area.setHorizontalOffsetForScrollBar(5);
        area.setHorizontalOffset(5);
        ln = new LineNumbers(new Font(propertiesBundle.getStringProperty("font.code_editor.family"), Font.PLAIN, Integer.parseInt(propertiesBundle.getStringProperty("font.code_editor.size"))), new Insets(1, 0, 0, 0));
        area.addToLeft(ln);
        LineNumbersAdjustmentHandler lnah = new LineNumbersAdjustmentHandler(area, ln);
        area.addAdjustListernerForVertical(lnah);
        setLayout(new BorderLayout());
        add("Center", area);
        add("North", makeToolBar());
        lnah.adjustmentValueChanged(null);
        area.revalidate();
        clearProgram();
    }

    /**
     * Sets up the file chooser with the user's working directory as default
     * directory.
     */
    private void initFileChooser() {
        if (currentDirectory == null) {
            currentDirectory = new File(udir + File.separator + propertiesBundle.getStringProperty("directory.examples"));
        }
        fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(currentDirectory);
        fileChooser.setFileFilter(new JavaFileFilter());
    }

    /**
     * Makes the JButton from the parameters given.
     * 
     * @param label
     *            The label of the button.
     * @param iconName
     *            The name of the image for the button.
     * @param listener
     *            The actionlistener for that button.
     * @return The constructed button from the given parameters.
     */
    private JButton makeToolButton(String label, String iconName, ActionListener listener) {
        URL imageURL = this.getClass().getClassLoader().getResource(propertiesBundle.getStringProperty("directory.images") + iconName);
        if (imageURL == null) {
            imageURL = Thread.currentThread().getContextClassLoader().getResource(propertiesBundle.getStringProperty("directory.images") + iconName);
        }
        ImageIcon icon = new ImageIcon(imageURL);
        JButton b = new JButton(label, icon);
        b.setVerticalTextPosition(AbstractButton.BOTTOM);
        b.setHorizontalTextPosition(AbstractButton.CENTER);
        b.setMargin(new Insets(0, 0, 0, 0));
        b.addActionListener(listener);
        return b;
    }

    /**
     * The method makes the Buttons for the toolbar of the codearea. Then it
     * adds the button to the JToolBar and returns it. Uses
     * makeToolButton(String, String, ActionListener) -method.
     * 
     * @return The finished toolbar for the code editor.
     * @see #makeToolButton(String, String, ActionListener)
     */
    private JToolBar makeToolBar() {
        JButton loadButton = makeToolButton(messageBundle.getString("button.open"), propertiesBundle.getStringProperty("image.open_icon"), loader);
        loadButton.setMnemonic(KeyEvent.VK_O);
        JButton saveButton = makeToolButton(messageBundle.getString("button.save"), propertiesBundle.getStringProperty("image.save_icon"), saver);
        saveButton.setMnemonic(KeyEvent.VK_S);
        JButton clearButton = makeToolButton(messageBundle.getString("button.new"), propertiesBundle.getStringProperty("image.new_icon"), clearer);
        clearButton.setMnemonic(KeyEvent.VK_N);
        JButton cutButton = makeToolButton(messageBundle.getString("button.cut"), propertiesBundle.getStringProperty("image.cut_icon"), cutter);
        cutButton.setMnemonic(KeyEvent.VK_U);
        JButton copyButton = makeToolButton(messageBundle.getString("button.copy"), propertiesBundle.getStringProperty("image.copy_icon"), copyist);
        copyButton.setMnemonic(KeyEvent.VK_Y);
        JButton pasteButton = makeToolButton(messageBundle.getString("button.paste"), propertiesBundle.getStringProperty("image.paste_icon"), pasteur);
        pasteButton.setMnemonic(KeyEvent.VK_T);
        JToolBar p = new JToolBar();
        p.add(clearButton);
        p.add(loadButton);
        p.add(saveButton);
        p.addSeparator();
        p.add(cutButton);
        p.add(copyButton);
        p.add(pasteButton);
        return p;
    }

    /**
     * Constructs the Program menu.
     * 
     * @return The Program menu
     */
    JMenu makeProgramMenu() {
        JMenu menu = new JMenu(messageBundle.getString("menu.program"));
        menu.setMnemonic(KeyEvent.VK_F);
        JMenuItem menuItem;
        menuItem = new JMenuItem(messageBundle.getString("menu.program.new"));
        menuItem.setMnemonic(KeyEvent.VK_N);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
        menuItem.addActionListener(clearer);
        menu.add(menuItem);
        menuItem = new JMenuItem(messageBundle.getString("menu.program.open"), KeyEvent.VK_O);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
        menuItem.addActionListener(loader);
        menu.add(menuItem);
        menuItem = new JMenuItem(messageBundle.getString("menu.program.save"));
        menuItem.setMnemonic(KeyEvent.VK_S);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
        menuItem.addActionListener(saver);
        menu.add(menuItem);
        menuItem = new JMenuItem(messageBundle.getString("menu.program.save_as"));
        menuItem.setMnemonic(KeyEvent.VK_A);
        menuItem.addActionListener(saveAs);
        menu.add(menuItem);
        return menu;
    }

    /**
     * Constructs the Edit menu.
     * 
     * @return The Edit menu
     */
    JMenu makeEditMenu() {
        JMenu menu = new JMenu(messageBundle.getString("menu.edit"));
        menu.setMnemonic(KeyEvent.VK_E);
        JMenuItem menuItem;
        menuItem = menu.add(this.undoRedo.undoAction);
        this.undoRedo.undoAction.updateUndoState();
        menuItem.setMnemonic(KeyEvent.VK_D);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, ActionEvent.CTRL_MASK));
        menuItem = menu.add(this.undoRedo.redoAction);
        this.undoRedo.redoAction.updateRedoState();
        menuItem.setMnemonic(KeyEvent.VK_R);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, ActionEvent.CTRL_MASK));
        menu.addSeparator();
        menuItem = new JMenuItem(messageBundle.getString("menu.edit.cut"));
        menuItem.setMnemonic(KeyEvent.VK_U);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.CTRL_MASK));
        menuItem.addActionListener(cutter);
        menu.add(menuItem);
        menuItem = new JMenuItem(messageBundle.getString("menu.edit.copy"));
        menuItem.setMnemonic(KeyEvent.VK_Y);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK));
        menuItem.addActionListener(copyist);
        menu.add(menuItem);
        menuItem = new JMenuItem(messageBundle.getString("menu.edit.paste"));
        menuItem.setMnemonic(KeyEvent.VK_T);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, ActionEvent.CTRL_MASK));
        menuItem.addActionListener(pasteur);
        menu.add(menuItem);
        menu.addSeparator();
        menuItem = new JMenuItem(messageBundle.getString("menu.edit.select_all"));
        menuItem.setMnemonic(KeyEvent.VK_A);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionEvent.CTRL_MASK));
        menuItem.addActionListener(allSelector);
        menu.add(menuItem);
        menu.addSeparator();
        Action findAction = new AbstractAction("Find...") {

            public void actionPerformed(ActionEvent e) {
                if (findDialog == null) {
                    findDialog = new FindDialog(CodeEditor2.this, 0, CodeEditor2.this.masterFrame);
                } else {
                    findDialog.setSelectedIndex(0);
                }
                Dimension d1 = findDialog.getSize();
                Dimension d2 = CodeEditor2.this.masterFrame.getSize();
                int x = Math.max((d2.width - d1.width) / 2, 0);
                int y = Math.max((d2.height - d1.height) / 2, 0);
                findDialog.setBounds(x + CodeEditor2.this.masterFrame.getX(), y + CodeEditor2.this.masterFrame.getY(), d1.width, d1.height);
                findDialog.pack();
                findDialog.setVisible(true);
            }
        };
        menuItem = menu.add(findAction);
        menuItem.setMnemonic('f');
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, KeyEvent.CTRL_MASK));
        Action replaceAction = new AbstractAction("Replace...") {

            public void actionPerformed(ActionEvent e) {
                if (findDialog == null) {
                    findDialog = new FindDialog(CodeEditor2.this, 1, CodeEditor2.this.masterFrame);
                } else {
                    findDialog.setSelectedIndex(1);
                }
                Dimension d1 = findDialog.getSize();
                Dimension d2 = CodeEditor2.this.masterFrame.getSize();
                int x = Math.max((d2.width - d1.width) / 2, 0);
                int y = Math.max((d2.height - d1.height) / 2, 0);
                findDialog.setBounds(x + CodeEditor2.this.masterFrame.getX(), y + CodeEditor2.this.masterFrame.getY(), d1.width, d1.height);
                findDialog.pack();
                findDialog.setVisible(true);
            }
        };
        menuItem = menu.add(replaceAction);
        menuItem.setMnemonic('r');
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, KeyEvent.CTRL_MASK));
        return menu;
    }

    /**
     * @param filename
     */
    public void setTitle(String filename) {
        if (masterFrame != null) {
            if (filename != null && filename.equals("")) {
                masterFrame.setTitle(title);
                MCodeUtilities.setFilename("untitled");
                Tracker.trackEvent(TrackerClock.currentTimeMillis(), Tracker.OTHER, -1, -1, "File: untitled");
            } else {
                masterFrame.setTitle(title + " - " + filename);
                MCodeUtilities.setFilename(filename);
                Tracker.trackEvent(TrackerClock.currentTimeMillis(), Tracker.OTHER, -1, -1, "File: " + filename);
            }
        }
    }

    /**
     * The given String program object will be set as the text inside the
     * JTextArea area.
     * 
     * @param program
     *            The string that will be set in JTextArea area as the program
     *            code.
     */
    public void setProgram(String program) {
        program = replaceTabs(program);
        area.setText(program);
        setChanged(false);
        area.setFirstLine(0);
        area.setCaretPosition(0);
        area.requestFocusInWindow();
        undoRedo.undo.discardAllEdits();
        undoRedo.undoAction.updateUndoState();
        undoRedo.redoAction.updateRedoState();
    }

    /**
     * Method returns the program code inside the JTextArea as String -object
     * Tabulators are changed to spaces for uniform handling of white spaces.
     * One tabulator corresponds four ASCII white spaces (this is changable from resources/properties).
     * 
     * @return The program code inside the JTextArea area.
     */
    public String getProgram() {
        String programCode = area.getText() + "\n";
        programCode = replaceTabs(programCode);
        return programCode;
    }

    /**
     * 
     * @param code
     * @return
     */
    public String replaceTabs(String code) {
        int n = Integer.parseInt(propertiesBundle.getStringProperty("editor.tab_size"));
        String spaces = "";
        for (int i = 0; i < n; i++) {
            spaces += " ";
        }
        return code.replaceAll("\\t", spaces);
    }

    /**
     * Method highlights the specified code area by selecting it.
     * 
     * @param h
     *            contains the area that should be highlighted.
     */
    public void highlight(Highlight h) {
        int l = 0, r = 0;
        try {
            if (h.getBeginLine() > 0) {
                l = area.getLineStartOffset(h.getBeginLine() - 1);
            }
            l += h.getBeginColumn();
            int nextLineStart = -1;
            if (h.getEndLine() > 0) {
                r = area.getLineStartOffset(h.getEndLine() - 1);
                nextLineStart = area.getLineStartOffset(h.getEndLine());
            }
            r += h.getEndColumn();
            if (nextLineStart > 0) {
                r = Math.min(r, nextLineStart);
            }
        } catch (Exception e) {
        }
        final int left = l - 1;
        final int right = r;
        area.requestFocusInWindow();
        if (left >= 0) {
            if (left != 0 && left == right) {
                area.select(left, right + 1);
            } else {
                area.select(left, right);
            }
        }
    }

    /**
     * 
     * Loads the program from the file selected by the user
     * to the text area. 
     * 
     * @see #loadProgram(File)
     * @see #setProgram(String)
     */
    void loadProgram() {
        int caretPosition = area.getCaretPosition();
        int selectionStart = area.getSelectionStart();
        int selectionEnd = area.getSelectionEnd();
        fileChooser.rescanCurrentDirectory();
        int returnVal = fileChooser.showOpenDialog(masterFrame);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            rememberPath(file);
            loadProgram(file);
        } else {
            if (selectionStart != selectionEnd) {
                if (caretPosition == selectionStart) {
                    area.select(selectionEnd, selectionStart);
                } else {
                    area.select(selectionStart, selectionEnd);
                }
            } else {
                area.setCaretPosition(caretPosition);
            }
        }
        area.requestFocusInWindow();
    }

    private void rememberPath(File file) {
        currentDirectory = file.getParentFile();
        if (currentDirectory != null) {
            propertiesBundle.setStringProperty("current.user.directory", currentDirectory.getAbsolutePath());
        }
    }

    /**
     * Read the given file and sets the text in the given file to the
     * text area and the name of the file to the title of the frame.
     * Uses readProgram(File file) method to read the file.
     * Uses setProgram(String str) method to set the content
     * of the file into the JTextArea area.
     * 
     * @param f The file that should be loaded to the text area
     * 
     * @see #readProgram(File)
     */
    void loadProgram(File file) {
        String program = readProgram(file);
        setProgram(program);
        currentFile = file;
        setChanged(false);
        setTitle(file.getName());
    }

    /**
     * 
     *
     */
    void saveProgram() {
        String code = area.getText();
        int tabSize = Integer.parseInt(propertiesBundle.getStringProperty("editor.tab_size"));
        int caretPosition = area.getCaretPosition();
        int selectionStart = area.getSelectionStart();
        int selectionEnd = area.getSelectionEnd();
        boolean saved = false;
        if (currentFile != null) {
            saved = writeProgram(currentFile);
        } else {
            saved = saveAsProgram();
        }
        if (saved) {
            if (selectionStart != selectionEnd) {
                selectionStart = getCorrectTextPosition(code, selectionStart, tabSize);
                selectionEnd = getCorrectTextPosition(code, selectionEnd, tabSize);
            }
            caretPosition = getCorrectTextPosition(code, caretPosition, tabSize);
        }
        if (selectionStart != selectionEnd) {
            if (caretPosition == selectionStart) {
                area.select(selectionEnd, selectionStart);
            } else {
                area.select(selectionStart, selectionEnd);
            }
        } else {
            area.setCaretPosition(caretPosition);
        }
        area.requestFocusInWindow();
    }

    /**
     * Saves the program from the JTextArea area to the file. Uses
     * writeProgram(File file) method to write the code into a file.
     * 
     * @see #writeProgram(File)
     */
    private boolean saveAsProgram() {
        fileChooser.rescanCurrentDirectory();
        int returnVal = fileChooser.showSaveDialog(masterFrame);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            rememberPath(file);
            if (file.getName().indexOf(".") < 0) {
                file = new File(file.getPath() + ".java");
            }
            return writeProgram(file);
        }
        return false;
    }

    /**
     * Saves the content of the JTextArea area to a given file.
     * 
     * @param file
     *            The file where the content of JTextArea is saved.
     * 
     * @see JeliotWindow#tryToEnterAnimate()
     */
    private boolean writeProgram(File file) {
        try {
            String code = replaceTabs(area.getText());
            BufferedWriter w = null;
            if (this.jeliotUserProperties.getBooleanProperty("save_unicode")) {
                w = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), Charset.forName("UTF-8")));
            } else {
                w = new BufferedWriter(new FileWriter(file));
            }
            w.write(replaceEndOfLines(code));
            w.flush();
            w.close();
            area.setText(code);
            currentFile = file;
            setChanged(false);
            setTitle(file.getName());
            return true;
        } catch (IOException e) {
            JOptionPane.showMessageDialog(masterFrame, messageBundle.getString("code_editor.save_failed"));
        }
        return false;
    }

    private static String EOL = System.getProperty("line.separator");

    private String replaceEndOfLines(String text) {
        text = text.replaceAll("\\r?\\n", EOL);
        return text;
    }

    /**
     * 
     * @param position
     * @return
     */
    public int getCorrectTextPosition(String code, int position, int tabSize) {
        int tabs = code.substring(0, position).split("\t", -1).length - 1;
        return position + (tabs * tabSize) - tabs;
    }

    /**
     * Reads the content of the given file and returns the content of the file
     * as String.
     * 
     * @param file
     *            The file from which the content is read and returned for the
     *            use of loadProgram() method.
     * @return The content of the file that was given as parameter.
     * @see #loadProgram()
     */
    String readProgram(File file) {
        try {
            BufferedReader r = null;
            if (this.jeliotUserProperties.getBooleanProperty("save_unicode")) {
                r = new BufferedReader(new InputStreamReader(new FileInputStream(file), Charset.forName("UTF-8")));
            } else {
                r = new BufferedReader(new FileReader(file));
            }
            StringBuffer buff = new StringBuffer();
            String line;
            while ((line = r.readLine()) != null) {
                buff.append(line);
                buff.append("\n");
            }
            r.close();
            return buff.toString();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(masterFrame, messageBundle.getString("code_editor.read_failed"));
            if (DebugUtil.DEBUGGING) {
                e.printStackTrace();
            }
            return null;
        }
    }

    /**
     * Sets in JTextAre area the default text as given in template.
     */
    void clearProgram() {
        setProgram(template);
        area.setCaretPosition(0);
        area.requestFocusInWindow();
        currentFile = null;
        setTitle("");
    }

    /**
     * @return Returns the currentFile.
     */
    public File getCurrentFile() {
        return currentFile;
    }

    /**
     * @param currentFile
     *            The currentFile to set.
     */
    public void setCurrentFile(File currentFile) {
        this.currentFile = currentFile;
    }

    /**
     * Calculates the number of lines in the program source code.
     * 
     * @param text
     *            the program source code.
     * @return the number of lines in the given program source code
     */
    public int calculateLines(String text) {
        int lines = 1;
        int index = text.indexOf("\n");
        while (index >= 0) {
            lines++;
            index++;
            index = text.indexOf("\n", index);
        }
        return lines;
    }

    /**
     * Validates the scroll pane's line numbering.
     */
    public void validateScrollPane() {
        final int lines = calculateLines(area.getText());
        if (ln != null) {
            Runnable updateAComponent = new Runnable() {

                public void run() {
                    ln.setHeightByLines(lines);
                }
            };
            SwingUtilities.invokeLater(updateAComponent);
        }
    }

    /**
     * @param line
     */
    public void highlightLineNumber(int line) {
        ln.setHighlightedLine(line);
    }

    /**
     * @return Returns the area.
     */
    public JEditTextArea getTextArea() {
        return area;
    }

    /**
     * @param font Font to be set.
     */
    public void setFont(Font font) {
        getTextArea().getPainter().setFont(font);
        ln.setFont(font);
        propertiesBundle.setFontProperty("font.code_editor", font);
    }

    public boolean requestFocusInWindow() {
        return this.area.requestFocusInWindow();
    }
}
