package Cosmo.ui.sourceEditor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.SortedSet;
import java.util.StringTokenizer;
import java.util.TreeSet;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import Cosmo.event.CosmoEvent;
import Cosmo.net.CosmoClient;
import Cosmo.transformation.metaCosmoModels.MetaNsmModel;
import Cosmo.util.Constants;
import com.Ostermiller.Syntax.Lexer.JavaLexer;
import com.Ostermiller.Syntax.Lexer.Lexer;
import com.Ostermiller.Syntax.Lexer.Token;

/**
 * A <a href="http://ostermiller.org/syntax/editor.html">demonstration text editor</a>
 * that uses syntax highlighting.
 */
public class ProgramEditor extends JPanel implements ActionListener {

    protected String initString = "";

    JPanel buttonPanel = null;

    JButton saveButton = null;

    JButton cancelButton = null;

    String resetString = "";

    String modelName = "";

    String parentName = "";

    CosmoClient client = null;

    /**
     * The place where the text is drawn.
     */
    protected JTextPane textPane;

    /**
     * the styled document that is the model for
     * the textPane
     */
    protected HighLightedDocument document;

    /**
     * A reader wrapped around the document
     * so that the document can be fed into
     * the lexer.
     */
    protected DocumentReader documentReader;

    /**
     * The lexer that tells us what colors different
     * words should be.
     */
    protected Lexer syntaxLexer;

    /**
     * A thread that handles the actual coloring.
     */
    protected Colorer colorer;

    /**
     * A lock for modifying the document, or for
     * actions that depend on the document not being
     * modified.
     */
    private Object doclock = new Object();

    /**
     * Create a new Demo
     */
    public ProgramEditor(String initString, String modelName, String parentName) {
        this.initString = initString;
        this.modelName = modelName;
        this.parentName = parentName;
        resetString = initString;
        document = new HighLightedDocument();
        textPane = new JTextPane(document);
        textPane.setCaretPosition(0);
        textPane.setMargin(new Insets(5, 5, 5, 5));
        JScrollPane scrollPane = new JScrollPane(textPane);
        setLocation(50, 50);
        addButtons();
        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.NORTH);
        if (!parentName.equals("")) {
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            scrollPane.setPreferredSize(new Dimension(2 * screenSize.width / 3, 4 * screenSize.height / 5));
            add(buttonPanel, BorderLayout.SOUTH);
        }
        colorer = new Colorer();
        colorer.start();
        initStyles();
        documentReader = new DocumentReader(document);
        initDocument();
    }

    private void addButtons() {
        buttonPanel = new JPanel();
        saveButton = new JButton("Save Model");
        saveButton.addActionListener(this);
        buttonPanel.add(saveButton);
        cancelButton = new JButton("Reset");
        cancelButton.addActionListener(this);
        buttonPanel.add(cancelButton);
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == saveButton) {
            saveModel();
        } else if (ae.getSource() == cancelButton) {
            resetModel();
        }
    }

    public void saveModel() {
        String modelText = textPane.getText();
        MetaNsmModel nmb1 = new MetaNsmModel();
        modelName = removeDotJava(modelName);
        saveJavaModelOnServer(modelText, modelName);
        resetString = modelText;
    }

    private String removeDotJava(String nodeName) {
        StringTokenizer stk = new StringTokenizer(nodeName, ".");
        return stk.nextToken();
    }

    public void saveJavaModelOnServer(String modelString, String className) {
        CosmoEvent eOut = new CosmoEvent(CosmoEvent.CREATE, CosmoEvent.SOURCE_CODE_FILE, "");
        File dest = new File(Constants.EXPORT_FOLDERSTRUCTURE_PATH + Constants.DB_NAME + Constants.EXPORT_NSM_MODELS_FOLDER_NAME);
        JFileChooser fc = new JFileChooser(dest);
        fc.setSelectedFile(new File(className + ".java"));
        fc.setApproveButtonText("Save");
        int returnVal = fc.showOpenDialog(this);
        if (returnVal == JFileChooser.CANCEL_OPTION) {
            return;
        }
        String completePath = fc.getSelectedFile().getPath();
        String[] values = new String[2];
        values[0] = modelString;
        values[1] = completePath;
        eOut.setValues(values);
        client.send(eOut);
    }

    public void resetModel() {
        textPane.setText(resetString);
    }

    public void setClient(CosmoClient client) {
        this.client = client;
    }

    /**
     * Run the Syntax Highlighting as a separate thread.
     * Things that need to be colored are messaged to the
     * thread and put in a list.
     */
    private class Colorer extends Thread {

        /**
         * Keep a list of places in the file that it is safe to restart the
         * highlighting.  This happens whenever the lexer reports that it has
         * returned to its initial state.  Since this list needs to be sorted
         * and we need to be able to retrieve ranges from it, it is stored in a
         * balanced tree.
         */
        private TreeSet iniPositions = new TreeSet(new DocPositionComparator());

        /**
         * As we go through and remove invalid positions we will also be finding
		 * new valid positions. 
		 * Since the position list cannot be deleted from and written to at the same
         * time, we will keep a list of the new positions and simply add it to the
         * list of positions once all the old positions have been removed.
         */
        private HashSet newPositions = new HashSet();

        /**
         * A simple wrapper representing something that needs to be colored.
         * Placed into an object so that it can be stored in a Vector.
         */
        private class RecolorEvent {

            public int position;

            public int adjustment;

            public RecolorEvent(int position, int adjustment) {
                this.position = position;
                this.adjustment = adjustment;
            }
        }

        /**
         * Vector that stores the communication between the two threads.
         */
        private volatile Vector v = new Vector();

        /**
         * The amount of change that has occurred before the place in the
         * document that we are currently highlighting (lastPosition).
         */
        private volatile int change = 0;

        /**
         * The last position colored
         */
        private volatile int lastPosition = -1;

        private volatile boolean asleep = false;

        /**
         * When accessing the vector, we need to create a critical section.
         * we will synchronize on this object to ensure that we don't get
         * unsafe thread behavior.
         */
        private Object lock = new Object();

        /**
         * Tell the Syntax Highlighting thread to take another look at this
         * section of the document.  It will process this as a FIFO.
         * This method should be done inside a doclock.
         */
        public void color(int position, int adjustment) {
            if (position < lastPosition) {
                if (lastPosition < position - adjustment) {
                    change -= lastPosition - position;
                } else {
                    change += adjustment;
                }
            }
            synchronized (lock) {
                v.add(new RecolorEvent(position, adjustment));
                if (asleep) {
                    this.interrupt();
                }
            }
        }

        /**
         * The colorer runs forever and may sleep for long
         * periods of time.  It should be interrupted every
         * time there is something for it to do.
         */
        public void run() {
            int position = -1;
            int adjustment = 0;
            boolean tryAgain = false;
            for (; ; ) {
                synchronized (lock) {
                    if (v.size() > 0) {
                        RecolorEvent re = (RecolorEvent) (v.elementAt(0));
                        v.removeElementAt(0);
                        position = re.position;
                        adjustment = re.adjustment;
                    } else {
                        tryAgain = false;
                        position = -1;
                        adjustment = 0;
                    }
                }
                if (position != -1) {
                    SortedSet workingSet;
                    Iterator workingIt;
                    DocPosition startRequest = new DocPosition(position);
                    DocPosition endRequest = new DocPosition(position + ((adjustment >= 0) ? adjustment : -adjustment));
                    DocPosition dp;
                    DocPosition dpStart = null;
                    DocPosition dpEnd = null;
                    try {
                        workingSet = iniPositions.headSet(startRequest);
                        dpStart = ((DocPosition) workingSet.last());
                    } catch (NoSuchElementException x) {
                        dpStart = new DocPosition(0);
                    }
                    if (adjustment < 0) {
                        workingSet = iniPositions.subSet(startRequest, endRequest);
                        workingIt = workingSet.iterator();
                        while (workingIt.hasNext()) {
                            workingIt.next();
                            workingIt.remove();
                        }
                    }
                    workingSet = iniPositions.tailSet(startRequest);
                    workingIt = workingSet.iterator();
                    while (workingIt.hasNext()) {
                        ((DocPosition) workingIt.next()).adjustPosition(adjustment);
                    }
                    workingSet = iniPositions.tailSet(dpStart);
                    workingIt = workingSet.iterator();
                    dp = null;
                    if (workingIt.hasNext()) {
                        dp = (DocPosition) workingIt.next();
                    }
                    try {
                        Token t;
                        boolean done = false;
                        dpEnd = dpStart;
                        synchronized (doclock) {
                            syntaxLexer.reset(documentReader, 0, dpStart.getPosition(), 0);
                            documentReader.seek(dpStart.getPosition());
                            t = syntaxLexer.getNextToken();
                        }
                        newPositions.add(dpStart);
                        while (!done && t != null) {
                            synchronized (doclock) {
                                if (t.getCharEnd() <= document.getLength()) {
                                    document.setCharacterAttributes(t.getCharBegin() + change, t.getCharEnd() - t.getCharBegin(), getStyle(t.getDescription()), true);
                                    dpEnd = new DocPosition(t.getCharEnd());
                                }
                                lastPosition = (t.getCharEnd() + change);
                            }
                            if (t.getState() == Token.INITIAL_STATE) {
                                while (dp != null && dp.getPosition() <= t.getCharEnd()) {
                                    if (dp.getPosition() == t.getCharEnd() && dp.getPosition() >= endRequest.getPosition()) {
                                        done = true;
                                        dp = null;
                                    } else if (workingIt.hasNext()) {
                                        dp = (DocPosition) workingIt.next();
                                    } else {
                                        dp = null;
                                    }
                                }
                                newPositions.add(dpEnd);
                            }
                            synchronized (doclock) {
                                t = syntaxLexer.getNextToken();
                            }
                        }
                        workingIt = iniPositions.subSet(dpStart, dpEnd).iterator();
                        while (workingIt.hasNext()) {
                            workingIt.next();
                            workingIt.remove();
                        }
                        workingIt = iniPositions.tailSet(new DocPosition(document.getLength())).iterator();
                        while (workingIt.hasNext()) {
                            workingIt.next();
                            workingIt.remove();
                        }
                        iniPositions.addAll(newPositions);
                        newPositions.clear();
                    } catch (IOException x) {
                    }
                    synchronized (doclock) {
                        lastPosition = -1;
                        change = 0;
                    }
                    tryAgain = true;
                }
                asleep = true;
                if (!tryAgain) {
                    try {
                        sleep(0xffffff);
                    } catch (InterruptedException x) {
                    }
                }
                asleep = false;
            }
        }
    }

    /**
     * Color or recolor the entire document
     */
    public void colorAll() {
        color(0, document.getLength());
    }

    /**
     * Color a section of the document.
     * The actual coloring will start somewhere before
     * the requested position and continue as long
     * as needed.
     *
     * @param position the starting point for the coloring.
     * @param adjustment amount of text inserted or removed
     *    at the starting point.
     */
    public void color(int position, int adjustment) {
        colorer.color(position, adjustment);
    }

    /**
     * Initialize the document with some default text and set
     * they initial type of syntax highlighting.
     */
    private void initDocument() {
        syntaxLexer = new JavaLexer(documentReader);
        try {
            document.insertString(document.getLength(), initString, getStyle("text"));
        } catch (BadLocationException ble) {
            Cosmo.util.Constants.iLog.LogInfoLine("Couldn't insert initial text.");
        }
    }

    /**
     * A hash table containing the text styles.
     * Simple attribute sets are hashed by name (String)
     */
    private Hashtable styles = new Hashtable();

    /**
     * retrieve the style for the given type of text.
     *
     * @param styleName the label for the type of text ("tag" for example) 
	 *      or null if the styleName is not known.
     * @return the style
     */
    private SimpleAttributeSet getStyle(String styleName) {
        return ((SimpleAttributeSet) styles.get(styleName));
    }

    /**
     * Create the styles and place them in the hash table.
     */
    private void initStyles() {
        SimpleAttributeSet style;
        style = new SimpleAttributeSet();
        StyleConstants.setFontFamily(style, "Monospaced");
        StyleConstants.setFontSize(style, 12);
        StyleConstants.setBackground(style, Color.white);
        StyleConstants.setForeground(style, Color.black);
        StyleConstants.setBold(style, false);
        StyleConstants.setItalic(style, false);
        styles.put("body", style);
        style = new SimpleAttributeSet();
        StyleConstants.setFontFamily(style, "Monospaced");
        StyleConstants.setFontSize(style, 12);
        StyleConstants.setBackground(style, Color.white);
        StyleConstants.setForeground(style, Color.blue);
        StyleConstants.setBold(style, true);
        StyleConstants.setItalic(style, false);
        styles.put("tag", style);
        style = new SimpleAttributeSet();
        StyleConstants.setFontFamily(style, "Monospaced");
        StyleConstants.setFontSize(style, 12);
        StyleConstants.setBackground(style, Color.white);
        StyleConstants.setForeground(style, Color.blue);
        StyleConstants.setBold(style, false);
        StyleConstants.setItalic(style, false);
        styles.put("endtag", style);
        style = new SimpleAttributeSet();
        StyleConstants.setFontFamily(style, "Monospaced");
        StyleConstants.setFontSize(style, 12);
        StyleConstants.setBackground(style, Color.white);
        StyleConstants.setForeground(style, Color.black);
        StyleConstants.setBold(style, false);
        StyleConstants.setItalic(style, false);
        styles.put("reference", style);
        style = new SimpleAttributeSet();
        StyleConstants.setFontFamily(style, "Monospaced");
        StyleConstants.setFontSize(style, 12);
        StyleConstants.setBackground(style, Color.white);
        StyleConstants.setForeground(style, new Color(0xB03060));
        StyleConstants.setBold(style, true);
        StyleConstants.setItalic(style, false);
        styles.put("name", style);
        style = new SimpleAttributeSet();
        StyleConstants.setFontFamily(style, "Monospaced");
        StyleConstants.setFontSize(style, 12);
        StyleConstants.setBackground(style, Color.white);
        StyleConstants.setForeground(style, new Color(0xB03060));
        StyleConstants.setBold(style, false);
        StyleConstants.setItalic(style, true);
        styles.put("value", style);
        style = new SimpleAttributeSet();
        StyleConstants.setFontFamily(style, "Monospaced");
        StyleConstants.setFontSize(style, 12);
        StyleConstants.setBackground(style, Color.white);
        StyleConstants.setForeground(style, Color.black);
        StyleConstants.setBold(style, true);
        StyleConstants.setItalic(style, false);
        styles.put("text", style);
        style = new SimpleAttributeSet();
        StyleConstants.setFontFamily(style, "Monospaced");
        StyleConstants.setFontSize(style, 12);
        StyleConstants.setBackground(style, Color.white);
        StyleConstants.setForeground(style, Color.blue);
        StyleConstants.setBold(style, false);
        StyleConstants.setItalic(style, false);
        styles.put("reservedWord", style);
        style = new SimpleAttributeSet();
        StyleConstants.setFontFamily(style, "Monospaced");
        StyleConstants.setFontSize(style, 12);
        StyleConstants.setBackground(style, Color.white);
        StyleConstants.setForeground(style, Color.black);
        StyleConstants.setBold(style, false);
        StyleConstants.setItalic(style, false);
        styles.put("identifier", style);
        style = new SimpleAttributeSet();
        StyleConstants.setFontFamily(style, "Monospaced");
        StyleConstants.setFontSize(style, 12);
        StyleConstants.setBackground(style, Color.white);
        StyleConstants.setForeground(style, new Color(0xB03060));
        StyleConstants.setBold(style, false);
        StyleConstants.setItalic(style, false);
        styles.put("literal", style);
        style = new SimpleAttributeSet();
        StyleConstants.setFontFamily(style, "Monospaced");
        StyleConstants.setFontSize(style, 12);
        StyleConstants.setBackground(style, Color.white);
        StyleConstants.setForeground(style, new Color(0x000080));
        StyleConstants.setBold(style, false);
        StyleConstants.setItalic(style, false);
        styles.put("separator", style);
        style = new SimpleAttributeSet();
        StyleConstants.setFontFamily(style, "Monospaced");
        StyleConstants.setFontSize(style, 12);
        StyleConstants.setBackground(style, Color.white);
        StyleConstants.setForeground(style, Color.black);
        StyleConstants.setBold(style, true);
        StyleConstants.setItalic(style, false);
        styles.put("operator", style);
        style = new SimpleAttributeSet();
        StyleConstants.setFontFamily(style, "Monospaced");
        StyleConstants.setFontSize(style, 12);
        StyleConstants.setBackground(style, Color.white);
        StyleConstants.setForeground(style, Color.green.darker());
        StyleConstants.setBold(style, false);
        StyleConstants.setItalic(style, false);
        styles.put("comment", style);
        style = new SimpleAttributeSet();
        StyleConstants.setFontFamily(style, "Monospaced");
        StyleConstants.setFontSize(style, 12);
        StyleConstants.setBackground(style, Color.white);
        StyleConstants.setForeground(style, new Color(0xA020F0).darker());
        StyleConstants.setBold(style, false);
        StyleConstants.setItalic(style, false);
        styles.put("preprocessor", style);
        style = new SimpleAttributeSet();
        StyleConstants.setFontFamily(style, "Monospaced");
        StyleConstants.setFontSize(style, 12);
        StyleConstants.setBackground(style, Color.white);
        StyleConstants.setForeground(style, Color.black);
        StyleConstants.setBold(style, false);
        StyleConstants.setItalic(style, false);
        styles.put("whitespace", style);
        style = new SimpleAttributeSet();
        StyleConstants.setFontFamily(style, "Monospaced");
        StyleConstants.setFontSize(style, 12);
        StyleConstants.setBackground(style, Color.white);
        StyleConstants.setForeground(style, Color.red);
        StyleConstants.setBold(style, false);
        StyleConstants.setItalic(style, false);
        styles.put("error", style);
        style = new SimpleAttributeSet();
        StyleConstants.setFontFamily(style, "Monospaced");
        StyleConstants.setFontSize(style, 12);
        StyleConstants.setBackground(style, Color.white);
        StyleConstants.setForeground(style, Color.orange);
        StyleConstants.setBold(style, false);
        StyleConstants.setItalic(style, false);
        styles.put("unknown", style);
    }

    /**
     * Just like a DefaultStyledDocument but intercepts inserts and
     * removes to color them.
     */
    private class HighLightedDocument extends DefaultStyledDocument {

        public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
            synchronized (doclock) {
                super.insertString(offs, str, a);
                color(offs, str.length());
                documentReader.update(offs, str.length());
            }
        }

        public void remove(int offs, int len) throws BadLocationException {
            synchronized (doclock) {
                super.remove(offs, len);
                color(offs, -len);
                documentReader.update(offs, -len);
            }
        }
    }
}

/**
 * A wrapper for a position in a document appropriate for storing
 * in a collection.
 */
class DocPosition {

    /**
     * The actual position
     */
    private int position;

    /**
     * Get the position represented by this DocPosition
     *
     * @return the position
     */
    int getPosition() {
        return position;
    }

    /**
     * Construct a DocPosition from the given offset into the document.
     *
     * @param position The position this DocObject will represent
     */
    public DocPosition(int position) {
        this.position = position;
    }

    /**
     * Adjust this position.
     * This is useful in cases that an amount of text is inserted
     * or removed before this position.
     *
     * @param adjustment amount (either positive or negative) to adjust this position.
     * @return the DocPosition, adjusted properly.
     */
    public DocPosition adjustPosition(int adjustment) {
        position += adjustment;
        return this;
    }

    /**
     * Two DocPositions are equal iff they have the same internal position.
     *
     * @return if this DocPosition represents the same position as another.
     */
    public boolean equals(Object obj) {
        if (obj instanceof DocPosition) {
            DocPosition d = (DocPosition) (obj);
            if (this.position == d.position) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * A string representation useful for debugging.
     *
     * @return A string representing the position.
     */
    public String toString() {
        return "" + position;
    }
}

/**
 * A comparator appropriate for use with Collections of
 * DocPositions.
 */
class DocPositionComparator implements Comparator {

    /**
     * Does this Comparator equal another?
     * Since all DocPositionComparators are the same, they
     * are all equal.
     *
     * @return true for DocPositionComparators, false otherwise.
     */
    public boolean equals(Object obj) {
        if (obj instanceof DocPositionComparator) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Compare two DocPositions
     *
     * @param o1 first DocPosition
     * @param o2 second DocPosition
     * @return negative if first < second, 0 if equal, positive if first > second
     */
    public int compare(Object o1, Object o2) {
        if (o1 instanceof DocPosition && o2 instanceof DocPosition) {
            DocPosition d1 = (DocPosition) (o1);
            DocPosition d2 = (DocPosition) (o2);
            return (d1.getPosition() - d2.getPosition());
        } else if (o1 instanceof DocPosition) {
            return -1;
        } else if (o2 instanceof DocPosition) {
            return 1;
        } else if (o1.hashCode() < o2.hashCode()) {
            return -1;
        } else if (o2.hashCode() > o1.hashCode()) {
            return 1;
        } else {
            return 0;
        }
    }
}

/**
 * A reader interface for an abstract document.  Since
 * the syntax highlighting packages only accept Stings and
 * Readers, this must be used.
 * Since the close() method does nothing and a seek() method
 * has been added, this allows us to get some performance
 * improvements through reuse.  It can be used even after the
 * lexer explicitly closes it by seeking to the place that
 * we want to read next, and reseting the lexer.
 */
class DocumentReader extends Reader {

    /**
     * Modifying the document while the reader is working is like
     * pulling the rug out from under the reader.  Alerting the
     * reader with this method (in a nice thread safe way, this
     * should not be called at the same time as a read) allows
     * the reader to compensate.
     */
    public void update(int position, int adjustment) {
        if (position < this.position) {
            if (this.position < position - adjustment) {
                this.position = position;
            } else {
                this.position += adjustment;
            }
        }
    }

    /**
     * Current position in the document. Incremented
     * whenever a character is read.
     */
    private long position = 0;

    /**
     * Saved position used in the mark and reset methods.
     */
    private long mark = -1;

    /**
     * The document that we are working with.
     */
    private AbstractDocument document;

    /**
     * Construct a reader on the given document.
     *
     * @param document the document to be read.
     */
    public DocumentReader(AbstractDocument document) {
        this.document = document;
    }

    /**
     * Has no effect.  This reader can be used even after
     * it has been closed.
     */
    public void close() {
    }

    /**
     * Save a position for reset.
     *
     * @param readAheadLimit ignored.
     */
    public void mark(int readAheadLimit) {
        mark = position;
    }

    /**
     * This reader support mark and reset.
     *
     * @return true
     */
    public boolean markSupported() {
        return true;
    }

    /**
     * Read a single character.
     *
     * @return the character or -1 if the end of the document has been reached.
     */
    public int read() {
        if (position < document.getLength()) {
            try {
                char c = document.getText((int) position, 1).charAt(0);
                position++;
                return c;
            } catch (BadLocationException x) {
                return -1;
            }
        } else {
            return -1;
        }
    }

    /**
     * Read and fill the buffer.
     * This method will always fill the buffer unless the end of the document is reached.
     *
     * @param cbuf the buffer to fill.
     * @return the number of characters read or -1 if no more characters are available in the document.
     */
    public int read(char[] cbuf) {
        return read(cbuf, 0, cbuf.length);
    }

    /**
     * Read and fill the buffer.
     * This method will always fill the buffer unless the end of the document is reached.
     *
     * @param cbuf the buffer to fill.
     * @param off offset into the buffer to begin the fill.
     * @param len maximum number of characters to put in the buffer.
     * @return the number of characters read or -1 if no more characters are available in the document.
     */
    public int read(char[] cbuf, int off, int len) {
        if (position < document.getLength()) {
            int length = len;
            if (position + length >= document.getLength()) {
                length = document.getLength() - (int) position;
            }
            if (off + length >= cbuf.length) {
                length = cbuf.length - off;
            }
            try {
                String s = document.getText((int) position, length);
                position += length;
                for (int i = 0; i < length; i++) {
                    cbuf[off + i] = s.charAt(i);
                }
                return length;
            } catch (BadLocationException x) {
                return -1;
            }
        } else {
            return -1;
        }
    }

    /**
     * @return true
     */
    public boolean ready() {
        return true;
    }

    /**
     * Reset this reader to the last mark, or the beginning of the document if a mark has not been set.
     */
    public void reset() {
        if (mark == -1) {
            position = 0;
        } else {
            position = mark;
        }
        mark = -1;
    }

    /**
     * Skip characters of input.
     * This method will always skip the maximum number of characters unless
     * the end of the file is reached.
     *
     * @param n number of characters to skip.
     * @return the actual number of characters skipped.
     */
    public long skip(long n) {
        if (position + n <= document.getLength()) {
            position += n;
            return n;
        } else {
            long oldPos = position;
            position = document.getLength();
            return (document.getLength() - oldPos);
        }
    }

    /**
     * Seek to the given position in the document.
     *
     * @param n the offset to which to seek.
     */
    public void seek(long n) {
        if (n <= document.getLength()) {
            position = n;
        } else {
            position = document.getLength();
        }
    }
}
