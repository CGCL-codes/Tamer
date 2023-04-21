package edutex.swt;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ExtendedModifyEvent;
import org.eclipse.swt.custom.ExtendedModifyListener;
import org.eclipse.swt.custom.LineStyleEvent;
import org.eclipse.swt.custom.LineStyleListener;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.custom.VerifyKeyListener;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import edutex.swt.doc.CompletionEngine;

public class EdutexDoc extends StyledText {

    LatexLineStyler lineStyler = new LatexLineStyler();

    private List<String> undoStack;

    private List<String> redoStack;

    private static final int MAX_STACK_SIZE = 25;

    private boolean save = false;

    private File file;

    private final CompletionEngine completionEngine;

    public EdutexDoc(EdutexTabDocuments parent) {
        super(parent, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL);
        this.completionEngine = new CompletionEngine(this);
        GridData spec = new GridData();
        spec.horizontalAlignment = GridData.FILL;
        spec.grabExcessHorizontalSpace = true;
        spec.verticalAlignment = GridData.FILL;
        spec.grabExcessVerticalSpace = true;
        this.setLayoutData(spec);
        this.addLineStyleListener(lineStyler);
        this.setEditable(true);
        Color bg = Display.getDefault().getSystemColor(SWT.COLOR_WHITE);
        this.setBackground(bg);
        this.addVerifyKeyListener(new VerifyKeyListener() {

            @Override
            public void verifyKey(VerifyEvent event) {
                if (event.keyCode == SWT.ARROW_DOWN || event.keyCode == SWT.ARROW_UP || event.keyCode == SWT.ARROW_RIGHT || event.keyCode == SWT.ARROW_LEFT || event.keyCode == SWT.CTRL) {
                    return;
                }
                if ((event.stateMask & SWT.CTRL) != 0) {
                    if (event.keyCode == 's') {
                        save();
                        return;
                    }
                    if (event.keyCode == ' ') {
                        doCompletion();
                        return;
                    }
                    return;
                }
                save = true;
                setBackground(new Color(getDisplay(), 250, 240, 240));
            }
        });
        undoStack = new LinkedList<String>();
        redoStack = new LinkedList<String>();
        buildControls();
    }

    /**
	 * Faire la compl�tion
	 */
    public final void doCompletion() {
        int offset = this.getCaretOffset();
        this.completionEngine.doCompletion(offset);
    }

    /**
	 * Pour la gestion des undo redo
	 */
    private void buildControls() {
        this.addExtendedModifyListener(new ExtendedModifyListener() {

            public void modifyText(ExtendedModifyEvent event) {
                String currText = getText();
                String newText = currText.substring(event.start, event.start + event.length);
                if (newText != null && newText.length() > 0) {
                    if (undoStack.size() == MAX_STACK_SIZE) {
                        undoStack.remove(undoStack.size() - 1);
                    }
                    undoStack.add(0, newText);
                }
            }
        });
        this.addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {
                if ((e.stateMask & SWT.CTRL) == 0) return;
                switch(e.keyCode) {
                    case 'z':
                        undo();
                        break;
                    case 'y':
                        redo();
                        break;
                    default:
                }
            }
        });
    }

    /**
	 * undo
	 */
    public void undo() {
        if (undoStack.size() > 0) {
            String lastEdit = (String) undoStack.remove(0);
            int editLength = lastEdit.length();
            String currText = this.getText();
            int startReplaceIndex = currText.length() - editLength;
            this.replaceTextRange(startReplaceIndex, editLength, "");
            redoStack.add(0, lastEdit);
        }
    }

    /**
	 * pour le redo
	 */
    public void redo() {
        if (redoStack.size() > 0) {
            String text = (String) redoStack.remove(0);
            moveCursorToEnd();
            append(text);
            moveCursorToEnd();
        }
    }

    private void moveCursorToEnd() {
        setCaretOffset(getText().length());
    }

    /**
	 * Si le document doit �tre sauvegard�
	 * 
	 * @return
	 */
    public boolean isSave() {
        return save;
    }

    /**
	 * Retourne le fichier
	 * 
	 * @return
	 */
    public File getFile() {
        return file;
    }

    /**
	 * D�finit le fichier
	 * 
	 * @param file
	 */
    public void setFile(File file, boolean save) {
        this.file = file;
        this.save = save;
    }

    /**
	 * Affichage d'une erreur
	 * 
	 * @param msg
	 */
    public void displayError(String msg) {
        MessageBox box = new MessageBox(this.getShell(), SWT.ICON_ERROR);
        box.setMessage(msg);
        box.open();
    }

    /**
	 * Enregistrement du fichier
	 */
    public final void save() {
        String content = this.getText();
        try {
            FileWriter writer = new FileWriter(this.file);
            writer.write(content);
            writer.close();
            this.save = false;
            Color bg = Display.getDefault().getSystemColor(SWT.COLOR_WHITE);
            this.setBackground(bg);
        } catch (Exception err) {
            this.displayError("Impossible d'enregistrer le fichier.");
        }
    }

    /**
	 * Ouverture d'un fichier
	 * 
	 * @param name
	 */
    public void open(File file) {
        if (file != null && file.isFile() && file.canRead()) this.open(file.getAbsolutePath());
    }

    /**
	 * Ouverture d'un fichier
	 * 
	 * @param name
	 */
    public void open(String name) {
        final String textString;
        if ((name == null) || (name.length() == 0)) return;
        File file = new File(name);
        if (!file.exists()) {
            String message = "Err file no exist";
            displayError(message);
            return;
        }
        try {
            FileInputStream stream = new FileInputStream(file.getPath());
            try {
                Reader in = new BufferedReader(new InputStreamReader(stream));
                char[] readBuffer = new char[2048];
                StringBuffer buffer = new StringBuffer((int) file.length());
                int n;
                while ((n = in.read(readBuffer)) > 0) {
                    buffer.append(readBuffer, 0, n);
                }
                textString = buffer.toString();
                stream.close();
            } catch (IOException e) {
                String message = "Err_file_io";
                displayError(message);
                return;
            }
        } catch (FileNotFoundException e) {
            String message = "Err_not_found";
            displayError(message);
            return;
        }
        Display display = this.getDisplay();
        display.asyncExec(new Runnable() {

            public void run() {
                setText(textString);
            }
        });
        lineStyler.parseBlockComments(textString);
        this.file = new File(name);
    }

    /**
	 * Pour le style latex
	 * 
	 * @author Cali
	 * 
	 */
    class LatexLineStyler implements LineStyleListener {

        LatexScanner scanner = new LatexScanner();

        int[] tokenColors;

        Color[] colors;

        Vector<int[]> blockComments = new Vector<int[]>();

        public static final int EOF = -1;

        public static final int EOL = 10;

        public static final int WORD = 0;

        public static final int WHITE = 1;

        public static final int KEY = 2;

        public static final int COMMENT = 3;

        public static final int STRING = 5;

        public static final int OTHER = 6;

        public static final int NUMBER = 7;

        public static final int BRACE = 8;

        public static final int MAXIMUM_TOKEN = 9;

        public LatexLineStyler() {
            initializeColors();
            scanner = new LatexScanner();
        }

        Color getColor(int type) {
            if (type < 0 || type >= tokenColors.length) {
                return null;
            }
            return colors[tokenColors[type]];
        }

        boolean inBlockComment(int start, int end) {
            for (int i = 0; i < blockComments.size(); i++) {
                int[] offsets = blockComments.elementAt(i);
                if ((offsets[0] >= start) && (offsets[0] <= end)) return true;
                if ((offsets[1] >= start) && (offsets[1] <= end)) return true;
                if ((offsets[0] <= start) && (offsets[1] >= end)) return true;
            }
            return false;
        }

        void initializeColors() {
            Display display = Display.getDefault();
            colors = new Color[] { new Color(display, new RGB(0, 0, 0)), new Color(display, new RGB(166, 0, 0)), new Color(display, new RGB(0, 166, 0)), new Color(display, new RGB(100, 100, 200)), new Color(display, new RGB(150, 150, 150)) };
            tokenColors = new int[MAXIMUM_TOKEN];
            tokenColors[WORD] = 0;
            tokenColors[WHITE] = 0;
            tokenColors[KEY] = 3;
            tokenColors[COMMENT] = 4;
            tokenColors[STRING] = 2;
            tokenColors[OTHER] = 1;
            tokenColors[NUMBER] = 0;
            tokenColors[BRACE] = 1;
        }

        void disposeColors() {
            for (int i = 0; i < colors.length; i++) {
                colors[i].dispose();
            }
        }

        /**
		 * Event.detail line start offset (input) Event.text line text (input)
		 * LineStyleEvent.styles Enumeration of StyleRanges, need to be in
		 * order. (output) LineStyleEvent.background line background color
		 * (output)
		 */
        public void lineGetStyle(LineStyleEvent event) {
            Vector<StyleRange> styles = new Vector<StyleRange>();
            int token;
            StyleRange lastStyle;
            if (inBlockComment(event.lineOffset, event.lineOffset + event.lineText.length())) {
                styles.addElement(new StyleRange(event.lineOffset, event.lineText.length(), getColor(COMMENT), null));
                event.styles = new StyleRange[styles.size()];
                styles.copyInto(event.styles);
                return;
            }
            Color defaultFgColor = ((Control) event.widget).getForeground();
            scanner.setRange(event.lineText);
            token = scanner.nextToken();
            while (token != EOF) {
                if (token == OTHER) {
                } else if (token != WHITE) {
                    Color color = getColor(token);
                    if ((!color.equals(defaultFgColor)) || (token == KEY)) {
                        StyleRange style = new StyleRange(scanner.getStartOffset() + event.lineOffset, scanner.getLength(), color, null);
                        if (token == KEY) {
                            style.fontStyle = SWT.BOLD;
                        }
                        if (styles.isEmpty()) {
                            styles.addElement(style);
                        } else {
                            lastStyle = (StyleRange) styles.lastElement();
                            if (lastStyle.similarTo(style) && (lastStyle.start + lastStyle.length == style.start)) {
                                lastStyle.length += style.length;
                            } else {
                                styles.addElement(style);
                            }
                        }
                    }
                } else if ((!styles.isEmpty()) && ((lastStyle = (StyleRange) styles.lastElement()).fontStyle == SWT.BOLD)) {
                    int start = scanner.getStartOffset() + event.lineOffset;
                    lastStyle = (StyleRange) styles.lastElement();
                    if (lastStyle.start + lastStyle.length == start) {
                        lastStyle.length += scanner.getLength();
                    }
                }
                token = scanner.nextToken();
            }
            event.styles = new StyleRange[styles.size()];
            styles.copyInto(event.styles);
        }

        public void parseBlockComments(String text) {
            blockComments = new Vector<int[]>();
            StringReader buffer = new StringReader(text);
            int ch;
            boolean blkComment = false;
            int cnt = 0;
            int[] offsets = new int[2];
            boolean done = false;
            try {
                while (!done) {
                    switch(ch = buffer.read()) {
                        case -1:
                            {
                                if (blkComment) {
                                    offsets[1] = cnt;
                                    blockComments.addElement(offsets);
                                }
                                done = true;
                                break;
                            }
                        case '%':
                            {
                                ch = buffer.read();
                                if ((ch == '*') && (!blkComment)) {
                                    offsets = new int[2];
                                    offsets[0] = cnt;
                                    blkComment = true;
                                    cnt++;
                                } else {
                                    cnt++;
                                }
                                cnt++;
                                break;
                            }
                        case '*':
                            {
                                if (blkComment) {
                                    ch = buffer.read();
                                    cnt++;
                                    if (ch == '/') {
                                        blkComment = false;
                                        offsets[1] = cnt;
                                        blockComments.addElement(offsets);
                                    }
                                }
                                cnt++;
                                break;
                            }
                        default:
                            {
                                cnt++;
                                break;
                            }
                    }
                }
            } catch (IOException e) {
            }
        }

        /**
		 * A simple fuzzy scanner for Java
		 */
        public class LatexScanner {

            protected Hashtable<String, Integer> fgKeys = null;

            protected StringBuffer fBuffer = new StringBuffer();

            protected String fDoc;

            protected int fPos;

            protected int fEnd;

            protected int fStartToken;

            protected boolean fEofSeen = false;

            private String[] fgKeywords = { "documentclass", "boolean", "break", "byte", "case", "catch", "char", "class", "continue", "default", "do", "double", "else", "extends", "false", "final", "finally", "float", "for", "if", "implements", "import", "instanceof", "int", "interface", "long", "native", "new", "null", "package", "private", "protected", "public", "return", "short", "static", "super", "switch", "synchronized", "this", "throw", "throws", "transient", "true", "try", "void", "volatile", "while" };

            public LatexScanner() {
                initialize();
            }

            /**
			 * Returns the ending location of the current token in the document.
			 */
            public final int getLength() {
                return fPos - fStartToken;
            }

            /**
			 * Initialize the lookup table.
			 */
            void initialize() {
                fgKeys = new Hashtable<String, Integer>();
                Integer k = new Integer(KEY);
                for (int i = 0; i < fgKeywords.length; i++) fgKeys.put(fgKeywords[i], k);
            }

            /**
			 * Returns the starting location of the current token in the
			 * document.
			 */
            public final int getStartOffset() {
                return fStartToken;
            }

            /**
			 * Returns the next lexical token in the document.
			 */
            @SuppressWarnings("unused")
            public int nextToken() {
                int c;
                fStartToken = fPos;
                while (true) {
                    switch(c = read()) {
                        case EOF:
                            return EOF;
                        case '%':
                            while (true) {
                                c = read();
                                if ((c == EOF) || (c == EOL)) {
                                    unread(c);
                                    return COMMENT;
                                }
                            }
                        case '$':
                            string: for (; ; ) {
                                c = read();
                                switch(c) {
                                    case '$':
                                        return STRING;
                                }
                            }
                        case '\\':
                            string: for (; ; ) {
                                c = read();
                                switch(c) {
                                    case '{':
                                        unread(c);
                                        return KEY;
                                    case EOF:
                                        unread(c);
                                        return KEY;
                                    case ' ':
                                        unread(c);
                                        return KEY;
                                    case '\\':
                                        c = read();
                                        break;
                                }
                            }
                        case '{':
                            return BRACE;
                        case '}':
                            return BRACE;
                        case '0':
                        case '1':
                        case '2':
                        case '3':
                        case '4':
                        case '5':
                        case '6':
                        case '7':
                        case '8':
                        case '9':
                            do {
                                c = read();
                            } while (Character.isDigit((char) c));
                            unread(c);
                            return NUMBER;
                        default:
                            if (Character.isWhitespace((char) c)) {
                                do {
                                    c = read();
                                } while (Character.isWhitespace((char) c));
                                unread(c);
                                return WHITE;
                            }
                            if (Character.isJavaIdentifierStart((char) c)) {
                                fBuffer.setLength(0);
                                do {
                                    fBuffer.append((char) c);
                                    c = read();
                                } while (Character.isJavaIdentifierPart((char) c));
                                unread(c);
                                Integer i = (Integer) fgKeys.get(fBuffer.toString());
                                if (i != null) return i.intValue();
                                return WORD;
                            }
                            return OTHER;
                    }
                }
            }

            /**
			 * Returns next character.
			 */
            protected int read() {
                if (fPos <= fEnd) {
                    return fDoc.charAt(fPos++);
                }
                return EOF;
            }

            public void setRange(String text) {
                fDoc = text;
                fPos = 0;
                fEnd = fDoc.length() - 1;
            }

            protected void unread(int c) {
                if (c != EOF) fPos--;
            }
        }
    }
}
