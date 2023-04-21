package mae.brow;

import java.io.*;
import java.net.URL;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.text.Document;
import javax.swing.text.BadLocationException;
import mae.util.PropertyManager;
import mae.util.Console;
import mae.util.SourceHandler;
import mae.util.VersionChecker;
import mae.util.SimpleFilter;
import mae.util.UndoManager;
import mae.util.LineNumberPane;
import java.lang.reflect.*;
import java.util.ArrayList;

public class Fide extends JPanel implements mae.util.Editor {

    ArrayList<ObjectItems> items;

    PropertyManager pm;

    boolean exit, juniorCoderCodeCompletation = true, juniorCoderQuickTemplate = true;

    SourceHandler handler;

    String text, searchStr;

    String TAB;

    File file;

    JOptionPane moveP;

    JDialog moveD;

    JMenu recent, trans;

    JFrame frm;

    ObjectPopupMenu popup, templateBlocks;

    final JTextArea src = new JTextArea("", 24, 60);

    final JTextField msg = new JTextField();

    final JMenuBar bar = new JMenuBar();

    final JToolBar tool = new JToolBar();

    final UndoManager undoMgr = new UndoManager(src);

    final Action undo = undoMgr.getUndoAction();

    final Action redo = undoMgr.getRedoAction();

    final Act comp = new Act(COMP);

    final Act exec = new Act(RUN);

    final Act stop = new Act(STOP);

    final Ear ear = new Ear();

    final JFileChooser fileD = new JFileChooser();

    final Map filters = new HashMap();

    static final int GAP = 4, MAX_ITEMS = 10, MAX_SPACE = 16;

    static final String EMPTY = "class XXX {\n//Enter java program\n" + "   public static void main(String[] args) {\n" + "   }\n}";

    static final Font arial = new Font("SansSerif", 0, 11);

    static final Font ttype = new Font("MonoSpaced", 0, 12);

    static final String TITLE = "Fide - ", NEW = "New", OPEN = "Open", QUIT = "Quit", SAVE = "Save", SAVEAS = "Save As...", STOP = "Stop", UNDO = "Undo", REDO = "Redo", TAB_SIZE = "Set TAB Size", FIND = "Find", AGAIN = "Next", GOTO = "Display Line", COMP = "Compile", RUN = "Run", ABOUT = "About", PREFERENCES = "Preferences";

    Fide() {
        items = new ArrayList<ObjectItems>();
        System.out.println("Fide begins " + new Date());
        exit = (JFrame.getFrames().length == 0);
        setLayout(new BorderLayout(GAP, GAP));
        src.setFont(ttype);
        if (Console.setDragEnabled(src)) Console.setDragFeedback(src);
        src.setToolTipText(null);
        add(new JScrollPane(src), "Center");
        LineNumberPane.addLineNumbers(src);
        src.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_TAB, 0), new Tab());
        src.getInputMap().put(KeyStroke.getKeyStroke(' ', KeyEvent.CTRL_MASK), new CtrlSpace(this));
        src.getInputMap().put(KeyStroke.getKeyStroke("control B"), new CtrlB(this));
        Object brk = src.getInputMap().get(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0));
        Enter enter = new Enter(src.getActionMap().get(brk));
        System.out.println("New: " + enter + "\nOld: " + enter.old);
        if (enter.old != null) src.getActionMap().put(brk, enter);
        msg.setEditable(false);
        add(msg, "South");
        popup = new ObjectPopupMenu(src);
        templateBlocks = new ObjectPopupMenu(src, BlockElements.Templates.values());
        frm = new JFrame("Fide");
        frm.addWindowListener(ear);
        frm.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frm.setJMenuBar(setupMenus());
        frm.setContentPane(this);
        frm.pack();
        loadProps();
        URL u = getClass().getResource("img/fide.gif");
        if (u != null) PropertyManager.setIcon(frm, u);
    }

    public java.awt.Frame getFrame() {
        return frm;
    }

    public PropertyManager propertyManager() {
        return pm;
    }

    public String getText() {
        return src.getText();
    }

    public void select(int i, int j) {
        src.select(i, j);
    }

    public void setMessage(String s) {
        msg.setText(s);
        System.out.println(s);
    }

    public void setMessage(Exception x) {
        setMessage(x.getClass().getName() + ": " + x.getMessage());
    }

    public void setMessage(String s, boolean OK) {
        setMessage(s);
        exec.setEnabled(OK);
    }

    public void startRunning() {
        Console.getInstance().show();
        setMessage("Running", false);
        stop.setEnabled(true);
    }

    public void stopRunning(String s) {
        setMessage(s, true);
        stop.setEnabled(false);
    }

    public SourceHandler getHandler() {
        return handler;
    }

    public void compile() {
        if (handler == null) return;
        if (handler.requiresSave()) save();
        handler.compile();
    }

    public void run() {
        if (handler == null) return;
        if (!src.getText().equals(text)) compile();
        handler.run();
    }

    public void stop() {
        if (handler == null) return;
        handler.stop();
    }

    public void open() {
        confirmedSave();
        if (file != null) {
            fileD.setSelectedFile(file);
            setFileFilter();
        }
        int k = fileD.showOpenDialog(this);
        if (k != JFileChooser.APPROVE_OPTION) return;
        File f = fileD.getSelectedFile();
        if (f == null || f.equals(file)) return;
        open(f);
    }

    public void open(File f) {
        confirmedSave();
        setSource(Browser.fileToString(f), f);
        src.select(0, 0);
    }

    public void empty() {
        confirmedSave();
        setSource(EMPTY, null);
        src.select(6, 9);
    }

    void addToRecent(File f) {
        String s = "" + f;
        for (int i = 0; i < recent.getItemCount(); i++) if (recent.getItem(i).getText().equals(s)) recent.remove(i);
        JMenuItem mi = newItem(s);
        mi.setFont(arial);
        recent.add(mi, 0);
        if (recent.getItemCount() > MAX_ITEMS) recent.remove(MAX_ITEMS);
    }

    void setSource(String t, File f) {
        text = t;
        file = f;
        String s = (f == null) ? "[new file]" : f.getName();
        if (f != null) addToRecent(f);
        handler = SourceHandler.newHandler(f, this);
        if (trans != null) {
            bar.remove(trans);
            bar.repaint();
        }
        trans = (handler == null) ? null : handler.menu();
        if (trans != null) {
            bar.add(trans, 3);
        }
        boolean OK = (handler != null);
        boolean C = OK && handler.canCompile();
        comp.setEnabled(C);
        boolean run = C && handler.readyToRun();
        setMessage(s + " opened", run);
        src.setText(t);
        undoMgr.discardAllEdits();
        stop.setEnabled(false);
        frm.setTitle(TITLE + s);
        frm.show();
        src.requestFocus();
    }

    void confirmedSave() {
        String s = src.getText();
        if (text == null || s.equals(text)) return;
        String msg = "Do you want to save";
        if (file != null) msg += "\n" + file;
        int opt = JOptionPane.YES_NO_CANCEL_OPTION;
        int typ = JOptionPane.QUESTION_MESSAGE;
        String[] but = { "Save", "Discard", "Cancel" };
        int reply = JOptionPane.showOptionDialog(this, msg, "Text is modified", opt, typ, null, but, null);
        if (reply == JOptionPane.YES_OPTION) save(); else if (reply != JOptionPane.NO_OPTION) throw new RuntimeException("action cancelled");
    }

    public void save() {
        String s = src.getText();
        if (text == null || s.equals(text)) return;
        if (file == null) saveAs(); else {
            Console.saveToFile(src.getText(), file);
            text = s;
        }
    }

    public void saveAs() {
        String s = src.getText();
        if (s.equals("")) return;
        if (file == null) {
            setFileFilter("java");
            fileD.setSelectedFile(new File(".java"));
        } else {
            setFileFilter();
            fileD.setSelectedFile(file);
        }
        int k = fileD.showSaveDialog(this);
        if (k != JFileChooser.APPROVE_OPTION) throw new RuntimeException("save cancelled");
        File f = fileD.getSelectedFile();
        if (!Console.confirm(f, this)) return;
        Console.saveToFile(s, f);
        if (!s.equals(text) || !f.equals(file)) setSource(s, f);
        System.out.println(s.length() + " bytes saved");
    }

    void setFileFilter() {
        setFileFilter(SimpleFilter.extension(file));
    }

    void setFileFilter(String ext) {
        if (ext == null) return;
        SimpleFilter f = (SimpleFilter) filters.get(ext);
        if (f != null) fileD.setFileFilter(f); else {
            f = new SimpleFilter(ext, "*." + ext);
            filters.put(ext, f);
            fileD.addChoosableFileFilter(f);
        }
    }

    /** Shows Find dialog and searches for the string entered */
    public void find() {
        String t = src.getSelectedText();
        if (t == null || t.length() > 30) t = searchStr;
        int k = showFindDialog(t);
        if (k != JOptionPane.YES_OPTION) return;
        String s = "" + moveP.getInputValue();
        if (s == null || s.equals("")) return;
        searchStr = s;
        doSearch();
    }

    int showFindDialog(String msg) {
        if (moveD == null) {
            int m = JOptionPane.QUESTION_MESSAGE;
            int y = JOptionPane.YES_NO_OPTION;
            moveP = new JOptionPane("Search String:", m, y);
            moveD = moveP.createDialog(this, "Find");
            moveP.setWantsInput(true);
        }
        moveP.setInitialSelectionValue(msg);
        moveD.pack();
        moveD.show();
        Object res = moveP.getValue();
        if (res == null) return -1;
        return ((Integer) res).intValue();
    }

    /** Searches for the current search string  */
    public void doSearch() {
        if (searchStr == null) find();
        if (searchStr.equals("")) return;
        int k = src.getSelectionEnd();
        k = src.getText().indexOf(searchStr, k);
        if (k >= 0) src.select(k, k + searchStr.length()); else getToolkit().beep();
    }

    public void preferences() {
        Preferences dialog = new Preferences(propertyManager());
        saveProps();
        loadProps();
    }

    /** Sets selection to a given line */
    public void goTo() {
        String s = JOptionPane.showInputDialog(this, GOTO);
        if (s == null) return;
        int k = 0;
        try {
            k = Integer.parseInt(s);
            int i = src.getLineStartOffset(k - 1);
            src.select(i, i);
        } catch (NumberFormatException e) {
            setMessage(e);
        } catch (BadLocationException e) {
            setMessage("Line " + k + " not found");
        }
    }

    /** Sets TAB size, after asking the user */
    public void setTAB() {
        String t = "" + TAB.length();
        String s = JOptionPane.showInputDialog(this, TAB_SIZE, t);
        if (s == null || s.equals(t)) return;
        try {
            setTAB(Integer.parseInt(s));
        } catch (NumberFormatException e) {
            setMessage(e);
        }
    }

    /** Sets TAB to n blanks */
    void setTAB(int n) {
        TAB = "";
        if (0 <= n && n >= MAX_SPACE) n = MAX_SPACE;
        for (int i = 0; i < n; i++) TAB += " ";
    }

    void loadProps() {
        Dimension t = getToolkit().getScreenSize();
        int W = 560, H = 430, x = t.width - W, y = t.height - H - 25;
        pm = new PropertyManager("mae", "Fide", getClass());
        frm.setBounds(pm.getBounds("frame", x, y, W, H));
        Font f = new Font("Monospaced", 0, 12);
        src.setFont(pm.getFont("font", f));
        setTAB(pm.getInteger("tab.size", 4));
        juniorCoderCodeCompletation = Boolean.parseBoolean(pm.getProperty("JuniorCoder.CodeCompletation", true + ""));
        juniorCoderQuickTemplate = Boolean.parseBoolean(pm.getProperty("JuniorCoder.QuickTemplate", true + ""));
        recent.removeAll();
        for (int i = 0; i < MAX_ITEMS; i++) {
            String s = pm.getProperty("recent." + i);
            if (s == null) continue;
            JMenuItem mi = newItem(s);
            mi.setFont(arial);
            recent.add(mi);
        }
    }

    void saveProps() {
        pm.setBounds("frame", frm.getBounds());
        pm.setProperty("tab.size", "" + TAB.length());
        int n = recent.getItemCount();
        for (int i = 0; i < n && i < MAX_ITEMS; i++) {
            String s = recent.getItem(i).getText();
            pm.setProperty("recent." + i, s);
        }
        pm.save("Fide properties");
    }

    class ReflectedPart {

        boolean isMethod = false;

        boolean isEnumCnstant = false;

        boolean isAbstract = false;

        java.lang.Class type;

        String modifier;

        String name;

        java.lang.Class returnType;

        java.lang.Class[] paramTypes;

        public ReflectedPart() {
            isMethod = false;
            type = null;
            modifier = "";
            name = "";
            returnType = null;
            paramTypes = null;
        }

        String getParams() {
            String paramStr = "";
            for (int j = 0; j < paramTypes.length; j++) {
                if (j == 0) paramStr += "(" + paramTypes[j]; else paramStr += "," + paramTypes[j];
                if ((j + 1) == paramTypes.length) paramStr += ")";
            }
            return paramStr;
        }

        public String toString() {
            String result = "";
            result += this.modifier + " ";
            if (isMethod) {
                result += this.returnType + " ";
            } else {
                result += this.type + " ";
            }
            result += this.name + " ";
            if (isMethod) {
                result += "(" + getParams() + ")";
            }
            return result;
        }
    }

    void reflect(String classAdi) {
        ArrayList<ReflectedPart> al = new ArrayList<Fide.ReflectedPart>();
        int modifier = 0, tempModifier = 0;
        int abst = 0, fin = 0, intface = 0, ntive = 0, prv = 0, prt = 0, pub = 0, statc = 0, strct = 0, sync = 0, trnsient = 0, volat = 0;
        java.lang.Class klass;
        java.lang.Class[] paramTypes;
        try {
            File dir = new File("." + File.separator);
            File fArr[] = file.getParentFile().listFiles(new FilenameFilter() {

                public boolean accept(File dir, String name) {
                    return name.endsWith(".class");
                }
            });
            for (File tmp : fArr) {
                tmp.renameTo(new File(dir, tmp.getName()));
            }
            String tempStr = classAdi;
            try {
                tempStr = classAdi.substring(0, classAdi.indexOf(".java"));
            } catch (IndexOutOfBoundsException ex) {
                tempStr = classAdi;
            }
            klass = java.lang.Class.forName(tempStr);
            java.lang.reflect.Method[] methods = klass.getMethods();
            for (java.lang.reflect.Method m : methods) {
                abst = fin = intface = ntive = prv = prt = pub = statc = strct = sync = trnsient = volat = 0;
                ReflectedPart rp = new ReflectedPart();
                modifier = m.getModifiers();
                String str = "";
                tempModifier = modifier;
                if ((modifier %= 2048) != tempModifier) {
                    strct = 1;
                    str += "strict ";
                }
                tempModifier = modifier;
                if ((modifier %= 1024) != tempModifier) {
                    abst = 1;
                    str += "abstract ";
                }
                tempModifier = modifier;
                if ((modifier %= 512) != tempModifier) {
                    intface = 1;
                    str += "interface ";
                }
                tempModifier = modifier;
                if ((modifier %= 256) != tempModifier) {
                    ntive = 1;
                    str += "native ";
                }
                tempModifier = modifier;
                if ((modifier %= 128) != tempModifier) {
                    trnsient = 1;
                    str += "transient ";
                }
                tempModifier = modifier;
                if ((modifier %= 64) != tempModifier) {
                    volat = 1;
                    str += "volatile ";
                }
                tempModifier = modifier;
                if ((modifier %= 32) != tempModifier) {
                    sync = 1;
                    str += "synchronized ";
                }
                tempModifier = modifier;
                if ((modifier %= 16) != tempModifier) {
                    fin = 1;
                    str += "final ";
                }
                tempModifier = modifier;
                if ((modifier %= 8) != tempModifier) {
                    statc = 1;
                    str += "static ";
                }
                tempModifier = modifier;
                if ((modifier %= 4) != tempModifier) {
                    prt = 1;
                    str += "protected ";
                }
                tempModifier = modifier;
                if ((modifier %= 2) != tempModifier) {
                    prv = 1;
                    str += "private ";
                }
                tempModifier = modifier;
                if ((modifier %= 1) != tempModifier) {
                    pub = 1;
                    str += "public ";
                }
                if (pub == 1) rp.modifier = "public"; else if (prt == 1) rp.modifier = "protected"; else if (prv == 1) rp.modifier = "private"; else rp.modifier = "package";
                if (abst == 1) {
                    rp.modifier += " abstract";
                    rp.isAbstract = true;
                }
                rp.returnType = m.getReturnType();
                rp.name = m.getName();
                rp.paramTypes = m.getParameterTypes();
                rp.isMethod = true;
                al.add(rp);
            }
            java.lang.reflect.Field[] fields = klass.getDeclaredFields();
            for (java.lang.reflect.Field f : fields) {
                abst = fin = intface = ntive = prv = prt = pub = statc = strct = sync = trnsient = volat = 0;
                ReflectedPart rp = new ReflectedPart();
                modifier = f.getModifiers();
                String str = "";
                tempModifier = modifier;
                if ((modifier %= 2048) != tempModifier) {
                    strct = 1;
                    str += "strict ";
                }
                tempModifier = modifier;
                if ((modifier %= 1024) != tempModifier) {
                    abst = 1;
                    str += "abstract ";
                }
                tempModifier = modifier;
                if ((modifier %= 512) != tempModifier) {
                    intface = 1;
                    str += "interface ";
                }
                tempModifier = modifier;
                if ((modifier %= 256) != tempModifier) {
                    ntive = 1;
                    str += "native ";
                }
                tempModifier = modifier;
                if ((modifier %= 128) != tempModifier) {
                    trnsient = 1;
                    str += "transient ";
                }
                tempModifier = modifier;
                if ((modifier %= 64) != tempModifier) {
                    volat = 1;
                    str += "volatile ";
                }
                tempModifier = modifier;
                if ((modifier %= 32) != tempModifier) {
                    sync = 1;
                    str += "synchronized ";
                }
                tempModifier = modifier;
                if ((modifier %= 16) != tempModifier) {
                    fin = 1;
                    str += "final ";
                }
                tempModifier = modifier;
                if ((modifier %= 8) != tempModifier) {
                    statc = 1;
                    str += "static ";
                }
                tempModifier = modifier;
                if ((modifier %= 4) != tempModifier) {
                    prt = 1;
                    str += "protected ";
                }
                tempModifier = modifier;
                if ((modifier %= 2) != tempModifier) {
                    prv = 1;
                    str += "private ";
                }
                tempModifier = modifier;
                if ((modifier %= 1) != tempModifier) {
                    pub = 1;
                    str += "public ";
                }
                if (pub == 1) rp.modifier = "public"; else if (prt == 1) rp.modifier = "protected"; else if (prv == 1) rp.modifier = "private"; else rp.modifier = "package";
                if (abst == 1) {
                    rp.modifier += " abstract";
                    rp.isAbstract = true;
                }
                rp.type = f.getType();
                rp.name = f.getName();
                rp.isEnumCnstant = f.isEnumConstant();
                al.add(rp);
            }
            for (int k = 0; k < al.size(); k++) {
                if (!al.get(k).isMethod) {
                    if (al.get(k).type.toString().equals("int") || al.get(k).type.toString().equals("byte") || al.get(k).type.toString().equals("short") || al.get(k).type.toString().equals("long") || al.get(k).type.toString().equals("float") || al.get(k).type.toString().equals("double") || al.get(k).type.toString().equals("char") || al.get(k).type.toString().equals("boolean")) {
                        items.add(new Field(al.get(k).name, al.get(k).type.toString()));
                    } else {
                        items.add(new Class(al.get(k).name));
                    }
                } else {
                    items.add(new Method(al.get(k).name, al.get(k).returnType.toString(), al.get(k).getParams()));
                }
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Fide.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    class CtrlSpace extends AbstractAction {

        private Fide fide;

        public CtrlSpace(Fide fide) {
            this.fide = fide;
        }

        public void actionPerformed(ActionEvent e) {
            if (juniorCoderCodeCompletation) {
                try {
                    fide.compile();
                    Thread.sleep(1000);
                    if (file != null) {
                        reflect(file.getName());
                    }
                    popup.setDataListByArraList(items);
                    popup.show(fide, src.getCaret().getMagicCaretPosition().x + 30, src.getCaret().getMagicCaretPosition().y + 20);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Fide.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    class CtrlB extends AbstractAction {

        private Fide fide;

        public CtrlB(Fide fide) {
            this.fide = fide;
        }

        public void actionPerformed(ActionEvent e) {
            if (juniorCoderQuickTemplate) templateBlocks.show(fide, src.getCaret().getMagicCaretPosition().x + 30, src.getCaret().getMagicCaretPosition().y + 20);
        }
    }

    class Tab extends AbstractAction {

        public void actionPerformed(ActionEvent e) {
            int k = src.getSelectionStart();
            int p = src.getSelectionEnd();
            if (k != p) getToolkit().beep(); else try {
                String t = (TAB.length() == 0) ? "\t" : TAB;
                src.getDocument().insertString(p, t, null);
            } catch (BadLocationException x) {
                setMessage(x);
            }
        }
    }

    class Enter extends AbstractAction {

        Action old;

        Enter(Action a) {
            old = a;
        }

        public void actionPerformed(ActionEvent e) {
            old.actionPerformed(e);
            Document doc = src.getDocument();
            int p = src.getSelectionEnd();
            try {
                int k = src.getLineOfOffset(p);
                int i = src.getLineStartOffset(k - 1);
                int len = Math.min(MAX_SPACE, p - i);
                String t = doc.getText(i, len);
                int j = 0;
                String s = "";
                while (j < len && t.charAt(j++) == ' ') s += " ";
                if (s.length() > 0) doc.insertString(p, s, null);
            } catch (BadLocationException x) {
                setMessage(x);
            }
        }
    }

    class Act extends AbstractAction {

        public Act(String s) {
            super(s);
        }

        public void setName(String s) {
            putValue(NAME, s);
        }

        public void setDesc(String s) {
            putValue(Action.SHORT_DESCRIPTION, s);
        }

        public void actionPerformed(ActionEvent e) {
            msg.setText("");
            String cmd = (String) getValue(NAME);
            try {
                if (cmd.equals(QUIT)) ear.windowClosing(null); else if (cmd.equals(COMP)) compile(); else if (cmd.equals(RUN)) run(); else if (cmd.equals(STOP)) stop(); else if (cmd.equals(FIND)) find(); else if (cmd.equals(AGAIN)) doSearch(); else if (cmd.equals(GOTO)) goTo(); else if (cmd.equals(TAB_SIZE)) setTAB(); else if (cmd.equals(NEW)) empty(); else if (cmd.equals(OPEN)) open(); else if (cmd.equals(SAVE)) save(); else if (cmd.equals(SAVEAS)) saveAs(); else if (cmd.equals(PREFERENCES)) preferences(); else open(new File(cmd));
            } catch (Exception x) {
                setMessage(x);
            }
            src.requestFocus();
        }
    }

    class Ear extends WindowAdapter {

        public void windowClosing(WindowEvent e) {
            try {
                confirmedSave();
                saveProps();
                frm.dispose();
                if (exit) System.exit(0);
            } catch (Exception x) {
                setMessage(x);
            }
        }
    }

    JMenu newMenu(String s, char c) {
        JMenu f = new JMenu(s);
        f.setMnemonic(c);
        return f;
    }

    JMenuItem newItem(String s) {
        return newItem(s, (char) 0);
    }

    JMenuItem newItem(String s, char c) {
        int m = ActionEvent.CTRL_MASK;
        return newItem(new Act(s), m, c);
    }

    JMenuItem newItem(Action a, int mask, char c) {
        JMenuItem mi = new JMenuItem(a);
        if (c > 0) {
            mi.setMnemonic(c);
            mi.setAccelerator(KeyStroke.getKeyStroke(c, mask));
        }
        String name = (String) a.getValue(Action.NAME);
        String s = "img/" + name + ".gif";
        URL u = getClass().getResource(s);
        if (u != null) {
            Icon icn = new ImageIcon(u);
            a.putValue(Action.SMALL_ICON, icn);
            a.putValue(Action.SHORT_DESCRIPTION, name);
            JButton but = tool.add(a);
            but.setText("");
        }
        return mi;
    }

    JMenuBar setupMenus() {
        JMenu f = newMenu("File", 'F');
        f.add(newItem(NEW, 'N'));
        f.add(newItem(OPEN, 'O'));
        recent = newMenu("Open Recent", 'T');
        f.add(recent);
        f.addSeparator();
        f.add(newItem(SAVE, 'S'));
        f.add(newItem(SAVEAS));
        f.addSeparator();
        f.add(newItem(QUIT, 'Q'));
        bar.add(f);
        tool.addSeparator();
        JMenu e = newMenu("Edit", 'E');
        e.add(newItem(undo, ActionEvent.CTRL_MASK, 'Z'));
        e.add(newItem(redo, ActionEvent.CTRL_MASK, 'Y'));
        e.addSeparator();
        e.add(newItem(FIND, 'F'));
        JMenuItem i = newItem(AGAIN, 'N');
        i.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F3, 0));
        e.add(i);
        e.addSeparator();
        e.add(newItem(GOTO, 'L'));
        e.add(newItem(TAB_SIZE));
        e.add(newItem(PREFERENCES, 'P'));
        bar.add(e);
        tool.addSeparator();
        JMenu p = newMenu("Program", 'P');
        p.add(newItem(comp, ActionEvent.ALT_MASK, 'C'));
        p.add(newItem(exec, ActionEvent.ALT_MASK, 'R'));
        p.add(newItem(stop, 0, (char) 0));
        p.addSeparator();
        JMenuItem a = newItem(ABOUT);
        a.setEnabled(false);
        p.add(a);
        bar.add(p);
        tool.setFloatable(false);
        bar.add(tool);
        bar.add(Box.createHorizontalGlue());
        JLabel lab = new JLabel(Browser.version);
        lab.setForeground(BrowserPanel.verColor);
        lab.setFont(BrowserPanel.verFont);
        bar.add(lab);
        Dimension dim = new Dimension(GAP, 0);
        bar.add(Box.createRigidArea(dim));
        return bar;
    }

    static Fide start(File f) {
        if (!VersionChecker.accept("Fide", "1.3")) return null;
        Fide d = new Fide();
        Console.getInstance();
        if (f == null) d.empty(); else d.open(f);
        return d;
    }

    public static Fide main() {
        return start(null);
    }

    public static void main(String[] args) {
        if (args.length == 0) start(null); else start(new File(args[0]));
    }
}
