package ide;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import jaguar.JaguarPCode;
import jaguar.JaguarRectangle;
import jaguar.JaguarUtils;
import jaguar.JaguarVM;
import jaguar.JaguarVariable;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTextPane;
import javax.swing.JToolTip;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.AbstractDocument;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import pcode.JaguarEXIT;
import pcode.JaguarRETURN;

/**
 * @author peter
 *
 */
public class JaguarTextPane extends JTextPane implements KeyListener, ActionListener, DocumentListener, MouseMotionListener {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private static final int MAX_CHARACTERS = 300000;

    private JaguarVM vm;

    private AbstractDocument doc;

    private JPopupMenu popup;

    private int bpline;

    private JaguarIDE ide;

    private static final String newline = "\n";

    private String searchTarget = null;

    /**
	 * @param doc
	 */
    public JaguarTextPane(JaguarIDE ide, StyledDocument doc) {
        this.doc = (AbstractDocument) doc;
        this.ide = ide;
        this.vm = ide.getVm();
        this.setCaretPosition(0);
        this.setMargin(new Insets(5, 5, 5, 5));
        bpline = 0;
        if (doc == null) {
            StyledDocument styledDoc = this.getStyledDocument();
            if (styledDoc instanceof AbstractDocument) {
                this.doc = (AbstractDocument) styledDoc;
                this.doc.setDocumentFilter(new JaguarIDEFilter(MAX_CHARACTERS));
            } else {
                System.err.println("Text pane's document isn't an AbstractDocument!");
                System.exit(-1);
            }
        }
        addKeyListener(this);
        addMouseMotionListener(this);
        setCursor(new Cursor(Cursor.TEXT_CURSOR));
    }

    public JToolTip createToolTip() {
        JToolTip tt = new JToolTip();
        tt.setBackground(Color.yellow);
        return tt;
    }

    public JaguarTextPane(JaguarIDE ide) {
        this(ide, null);
    }

    public boolean getScrollableTracksViewportWidth() {
        return false;
    }

    public String getText() {
        String txt = super.getText();
        if (File.separatorChar == '\\' && txt != null) txt = txt.replaceAll("\r", "");
        return txt;
    }

    public void initDocument() {
        SimpleAttributeSet[] attrs = initAttributes(false);
        for (int i = 0; i < vm.getStmtSize(); i++) {
            JaguarPCode p = vm.getStmt(i);
            insertLine(doc.getLength(), p, attrs, true);
        }
        addDocumentListener(this);
    }

    private void insertLine(int pos, JaguarPCode p, SimpleAttributeSet[] attrs, boolean nl) {
        try {
            String line = p.toString();
            String qm = "";
            if (line.replace('\t', ' ').trim().startsWith("?")) {
                int q = line.indexOf('?');
                q = line.replace('\t', ' ').indexOf(' ', q);
                if (q < 0) q = line.length();
                qm = line.substring(0, q);
                line = line.substring(q);
            }
            int c = line.indexOf('#');
            String comment = "";
            if (c >= 0) {
                comment = line.substring(c);
                line = line.substring(0, c);
            }
            String[] w = line.trim().split("\\s+");
            int wp = 0;
            if (qm.length() > 0) {
                doc.insertString(pos, qm, attrs[JaguarPCode.KEYWORD]);
                pos += qm.length();
            }
            for (int j = 0; j < w.length; j++) {
                int b = line.indexOf(w[j], wp);
                if (b > wp) {
                    doc.insertString(pos, line.substring(wp, b), attrs[JaguarPCode.COMMENT]);
                    pos += b - wp;
                }
                doc.insertString(pos, w[j], attrs[p.tokenType(j - 1)]);
                pos += w[j].length();
                wp = b + w[j].length();
            }
            if (line.length() > wp) {
                doc.insertString(pos, line.substring(wp), attrs[JaguarPCode.COMMENT]);
                pos += line.length() - wp;
            }
            if (c >= 0) {
                doc.insertString(pos, comment, attrs[JaguarPCode.COMMENT]);
                pos += comment.length();
            }
            if (nl) doc.insertString(pos, newline, attrs[JaguarPCode.COMMENT]);
        } catch (BadLocationException ble) {
            System.err.println("Couldn't insert initial text.");
        }
    }

    protected SimpleAttributeSet[] initAttributes(boolean isBP) {
        SimpleAttributeSet[] attrs = new SimpleAttributeSet[JaguarPCode.NUMTYPES];
        int i = JaguarPCode.OPCODE;
        attrs[i] = new SimpleAttributeSet();
        StyleConstants.setFontFamily(attrs[i], "Courier");
        StyleConstants.setFontSize(attrs[i], 20);
        StyleConstants.setBold(attrs[i], true);
        StyleConstants.setForeground(attrs[i], Color.blue);
        if (isBP) StyleConstants.setBackground(attrs[i], Color.yellow);
        int j = JaguarPCode.KEYWORD;
        attrs[j] = new SimpleAttributeSet(attrs[i]);
        StyleConstants.setBold(attrs[j], false);
        StyleConstants.setFontSize(attrs[j], 16);
        StyleConstants.setBold(attrs[j], true);
        StyleConstants.setForeground(attrs[j], Color.black);
        int k = JaguarPCode.SYMBOL;
        attrs[k] = new SimpleAttributeSet(attrs[j]);
        StyleConstants.setItalic(attrs[k], true);
        StyleConstants.setForeground(attrs[k], Color.magenta);
        k = JaguarPCode.OPERAND;
        attrs[k] = new SimpleAttributeSet(attrs[j]);
        StyleConstants.setFontSize(attrs[k], 16);
        StyleConstants.setBold(attrs[j], false);
        StyleConstants.setForeground(attrs[k], Color.darkGray);
        k = JaguarPCode.COMMENT;
        attrs[k] = new SimpleAttributeSet(attrs[j]);
        StyleConstants.setFontSize(attrs[k], 14);
        StyleConstants.setForeground(attrs[k], Color.red);
        k = JaguarPCode.SYNTAX;
        attrs[k] = new SimpleAttributeSet(attrs[j]);
        StyleConstants.setForeground(attrs[k], Color.red);
        StyleConstants.setFontSize(attrs[k], 16);
        StyleConstants.setUnderline(attrs[k], true);
        return attrs;
    }

    public void addUndoableEditListener(UndoableEditListener listener) {
        doc.addUndoableEditListener(listener);
    }

    public void addDocumentListener(DocumentListener listener) {
        doc.addDocumentListener(listener);
    }

    public void keyPressed(KeyEvent e) {
        if ((e.getModifiers() & KeyEvent.CTRL_MASK) == KeyEvent.CTRL_MASK) {
            if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                ctrlSPACE();
            } else if (e.getKeyCode() == KeyEvent.VK_R) {
                ide.ctrlR();
            } else if (e.getKeyCode() == KeyEvent.VK_S) {
                ctrlS();
            } else if (e.getKeyCode() == KeyEvent.VK_Q) {
                ctrlQ();
            } else if (e.getKeyCode() == KeyEvent.VK_COMMA) {
                ctrlCOMMA();
            } else if (e.getKeyCode() == KeyEvent.VK_PERIOD) {
                ctrlPERIOD();
            }
        } else if ((e.getModifiers() & (KeyEvent.ALT_MASK | KeyEvent.SHIFT_MASK | KeyEvent.CTRL_MASK)) == 0) {
            if (e.getKeyCode() == KeyEvent.VK_F3) {
                F3();
                e.setKeyCode(0);
            } else if (e.getKeyCode() == KeyEvent.VK_F5) {
                ide.F5();
                e.setKeyCode(0);
            } else if (e.getKeyCode() == KeyEvent.VK_F6) {
                ide.F6();
                e.setKeyCode(0);
            } else if (e.getKeyCode() == KeyEvent.VK_F7) {
                ide.F7();
                e.setKeyCode(0);
            } else if (e.getKeyCode() == KeyEvent.VK_F8) {
                ide.F8();
                e.setKeyCode(0);
            } else if (e.getKeyCode() == KeyEvent.VK_F9) {
                F9();
                e.setKeyCode(0);
            }
        }
    }

    /**
	 * search previous word under cursor
	 *
	 */
    private void ctrlCOMMA() {
        Point p = wordAtCaret();
        if (p.x >= p.y) {
            searchTarget = null;
            return;
        }
        String txt = getText().toLowerCase();
        String wrd = txt.substring(p.x, p.y);
        if (searchTarget == null || wrd.indexOf(searchTarget) < 0) {
            searchTarget = wrd;
        } else {
            wrd = searchTarget;
        }
        int i = -1;
        if (p.x > 0) {
            i = txt.lastIndexOf(wrd, p.x - 1);
        }
        if (i < 0) i = txt.lastIndexOf(wrd);
        setCaretPosition(i);
    }

    /**
	 * search next word under cursor 
	 *
	 */
    private void ctrlPERIOD() {
        Point p = wordAtCaret();
        if (p.x >= p.y) {
            searchTarget = null;
            return;
        }
        String txt = getText().toLowerCase();
        String wrd = txt.substring(p.x, p.y);
        if (searchTarget == null || wrd.indexOf(searchTarget) < 0) {
            searchTarget = wrd;
        } else {
            wrd = searchTarget;
        }
        int i = txt.indexOf(wrd, p.y);
        if (i < 0) i = txt.indexOf(wrd);
        setCaretPosition(i);
    }

    /**
	 * inspect var/rect under the Caret
	 */
    public void F3() {
        Point p = wordAtCaret();
        if (p.x >= p.y) return;
        String s = getText().substring(p.x, p.y);
        JaguarVariable var = vm.getVar(s);
        JaguarWatch watcher = null;
        if (var == null) {
            JaguarRectangle r = vm.getRect(s);
            if (r != null) {
                watcher = new JaguarWatch(ide, s, r);
            }
        } else watcher = new JaguarWatch(ide, s, var);
        if (watcher != null) ide.addWatcher(watcher);
    }

    /**
	 * step in
	 */
    public void F5() {
        int bp = rowcol().y;
        int pc = vm.getPc();
        if (bpline == 0 || bp == 0) vm.executeInit();
        vm.setUserInterrupt(false);
        vm.setPc(bp);
        if (bp < vm.getStmtSize()) vm.executeStmt();
        bpline = vm.getPc();
        vm.setPc(pc);
        caretOnLine(bpline);
    }

    /**
	 * step over
	 */
    public void F6() {
        int bp = rowcol().y;
        int pc = vm.getPc();
        int i = bp;
        if (bpline == 0 || bp == 0) vm.executeInit();
        vm.setUserInterrupt(false);
        if (i < vm.getStmtSize() && vm.getStmt(i).isJumper()) {
            vm.setPc(i);
            vm.executeStmt();
            i = vm.getPc();
        } else {
            ++bp;
            while (i != bp && i < vm.getStmtSize()) {
                vm.setPc(i);
                vm.executeStmt();
                i = vm.getPc();
            }
        }
        bpline = i;
        vm.setPc(pc);
        caretOnLine(bpline);
    }

    /**
	 * run to return/exit
	 */
    public void F7() {
        int bp = rowcol().y;
        int pc = vm.getPc();
        int i = bp;
        if (bpline == 0 || bp == 0) vm.executeInit();
        vm.setUserInterrupt(false);
        bp = -1;
        while (i != bp && i < vm.getStmtSize()) {
            JaguarPCode p = vm.getStmt(i);
            vm.setPc(i);
            vm.executeStmt();
            i = vm.getPc();
            if (p.getClass() == JaguarEXIT.class || p.getClass() == JaguarRETURN.class) bp = i;
        }
        bpline = i;
        vm.setPc(pc);
        caretOnLine(bpline);
    }

    /**
	 * run to breakpoint
	 */
    public void F8() {
        int bp = rowcol().y;
        int pc = vm.getPc();
        int i = bp;
        if (bpline == 0 || bp == 0) vm.executeInit();
        vm.setUserInterrupt(false);
        bp = -1;
        while (i != bp && i < vm.getStmtSize()) {
            vm.setPc(i);
            vm.executeStmt();
            i = vm.getPc();
            bp = isBP(i);
        }
        bpline = i;
        vm.setPc(pc);
        caretOnLine(bpline);
    }

    /**
	 * toggle breakpoint
	 */
    public void F9() {
        int bp = rowcol().y;
        if (bp < vm.getStmtSize()) {
            JaguarPCode p = vm.getStmt(bp);
            p.toggleBP();
            repaintLine(bp, p.isBP());
        }
    }

    private void repaintLine(int bp, boolean isBP) {
        if (bp >= vm.getStmtSize()) return;
        int caret = getCaretPosition();
        int selstart = getSelectionStart();
        int selend = getSelectionEnd();
        String text = getText();
        int pos = 0;
        for (int n = 0; n < bp; n++) {
            pos = text.indexOf(newline, pos) + 1;
        }
        int i = text.indexOf(newline, pos);
        if (i < 0) i = text.length() - 1;
        SimpleAttributeSet[] attrs = initAttributes(isBP);
        doc.removeDocumentListener(this);
        doc.removeUndoableEditListener(ide);
        setSelectionStart(pos);
        setSelectionEnd(i);
        setSelectionStart(pos);
        replaceSelection("");
        insertLine(pos, vm.getStmt(bp), attrs, false);
        setSelectionStart(selstart);
        setSelectionEnd(selend);
        setSelectionStart(selstart);
        setCaretPosition(caret);
        doc.addUndoableEditListener(ide);
        doc.addDocumentListener(this);
    }

    /**
	 * @param i
	 * @return -1 when line is not in bp list, otherwise return i
	 */
    private int isBP(int i) {
        if (i < vm.getStmtSize() && vm.getStmt(i).isBP()) return i;
        return -1;
    }

    /**
	 * quit without save
	 *
	 */
    private void ctrlQ() {
        ide.setDone(true);
    }

    /**
	 * save the statements
	 *
	 */
    public void ctrlS() {
        try {
            ArrayList b = new ArrayList();
            ArrayList f = new ArrayList();
            ArrayList d = new ArrayList();
            BufferedWriter bw = null;
            for (int i = 0; i < vm.getStmtSize(); i++) {
                JaguarPCode p = vm.getStmt(i);
                String line = p.toString();
                File pf = p.getSrc();
                int fi = f.indexOf(pf);
                if (fi < 0) {
                    fi = f.size();
                    File bak = new File(pf.getAbsolutePath() + ".bak");
                    if (bak.exists()) bak.delete();
                    pf.renameTo(bak);
                    pf = p.getSrc();
                    bw = new BufferedWriter(new FileWriter(pf));
                    f.add(pf);
                    b.add(bw);
                    d.add(null);
                } else bw = (BufferedWriter) b.get(fi);
                if (line.startsWith("## Last edit by JaguarIDE V")) {
                    line = "## Last edit by JaguarIDE " + JaguarIDE.VERSION + " on " + getDateTime() + " ##";
                    d.remove(fi);
                    d.add(fi, "");
                }
                bw.write(line);
                bw.newLine();
            }
            for (int i = 0; i < b.size(); i++) {
                bw = (BufferedWriter) b.get(i);
                if (d.get(i) == null) {
                    bw.write("## Last edit by JaguarIDE " + JaguarIDE.VERSION + " on " + getDateTime() + " ##");
                    bw.newLine();
                }
                bw.flush();
                bw.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getDateTime() {
        long msec = System.currentTimeMillis();
        Date today = new Date(msec);
        String output;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", new Locale("us US"));
        output = formatter.format(today);
        return output;
    }

    /**
	 * run to cursor
	 *
	 */
    public void ctrlR() {
        int bp = rowcol().y;
        int i = bpline;
        if (i == 0) vm.executeInit();
        while (i != bp && i < vm.getStmtSize()) {
            vm.setPc(i);
            vm.executeStmt();
            i = vm.getPc();
            if (isBP(i) == i) bp = i;
        }
        bpline = i;
        caretOnLine(bpline);
    }

    private void caretOnLine(int n) {
        int dot = 0;
        String txt = getText();
        for (int i = 0; i < n; i++) {
            int ndot = txt.indexOf(newline, dot);
            if (ndot < 0) break;
            dot = ndot;
            if (dot < txt.length() - 1) ++dot;
        }
        getCaret().setVisible(false);
        setCaretPosition(dot);
        getCaret().setVisible(true);
    }

    private Point rowcol() {
        int dot = getCaretPosition();
        return rowcolAtPos(dot);
    }

    public Point rowcolAtPos(int dot) {
        if (dot == 0) return new Point(0, 0);
        Point r = new Point();
        String txt = getText();
        int i = txt.lastIndexOf(newline, dot - 1);
        if (i < 0) {
            r.x = dot;
            r.y = 0;
        } else {
            r.x = dot - 1 - i;
            int j = 0;
            while (i > 0) {
                ++j;
                i = txt.lastIndexOf(newline, i - 1);
            }
            r.y = j;
        }
        return r;
    }

    private void ctrlSPACE() {
        Point p = wordAtCaret();
        Point q = rowcolAtPos(p.y);
        ide.updateRepaintrequest();
        File f = null;
        if (q.y < vm.getStmtSize()) f = vm.getStmt(q.y).getSrc(); else if (vm.getStmtSize() > 0) f = vm.getStmt(vm.getStmtSize() - 1).getSrc(); else f = new File(vm.getVmID());
        JaguarPCode code = JaguarPCode.factory(vm, f, lineAtPos(p.x).substring(0, q.x));
        String[] candidate = code.completions(p.x - p.y + q.x, q.x);
        if (candidate.length == 0) return;
        String completion = candidate[0];
        int sz = p.y - p.x;
        if (candidate.length > 1) {
            for (int i = 1; i < candidate.length; i++) {
                completion = JaguarUtils.head(completion, candidate[i]);
            }
            if (completion.length() == sz) {
                createPopup(candidate);
                int x = getFontMetrics(getFont()).stringWidth(completion);
                int y = getHeight() / 2;
                try {
                    Rectangle r = modelToView(getCaretPosition());
                    x = r.x;
                    y = r.y - r.height;
                    if (y > r.height) y -= r.height; else y += r.height;
                } catch (BadLocationException e1) {
                    e1.printStackTrace();
                }
                popup.show(this, x, y);
            }
        }
        replaceWordAtCaret(completion);
        setCaretPosition(p.x + completion.length());
    }

    private String lineAtPos(int pos) {
        String text = getText();
        int i = text.lastIndexOf(newline, pos);
        if (i == pos && pos > 0) i = text.lastIndexOf(newline, pos - 1);
        int j = text.indexOf(newline, pos);
        if (j < 0) j = text.length();
        return text.substring(i + newline.length(), j);
    }

    public void keyReleased(KeyEvent e) {
    }

    public void keyTyped(KeyEvent e) {
    }

    private boolean isIdentifierChar(char a) {
        return (a == '.' || a == '_' || a == '%' || (a >= '0' && a <= '9') || (a >= 'a' && a <= 'z') || (a >= 'A' && a <= 'Z'));
    }

    private void createPopup(String[] candidate) {
        popup = new JPopupMenu("possible completions");
        for (int i = 0; i < candidate.length; i++) {
            JMenuItem lab = new JMenuItem(" " + candidate[i]);
            lab.addActionListener(this);
            popup.add(lab);
        }
    }

    public void actionPerformed(ActionEvent e) {
        String a = e.getActionCommand();
        if (a.startsWith(" ")) {
            Point p = wordAtCaret();
            if (p.x == p.y) insertWordAtCaret(a.substring(1)); else replaceWordAtCaret(a.substring(1));
            setCaretPosition(p.x + a.length() - 1);
            popup.setVisible(false);
            popup = null;
        }
    }

    private void insertWordAtCaret(String a) {
        int caret = getCaretPosition();
        setSelectionStart(caret);
        setSelectionEnd(caret);
        replaceSelection(a);
        setCaretPosition(caret);
        int i = rowcolAtPos(caret).y;
        repaintLine(i, i == isBP(i));
    }

    private void replaceWordAtCaret(String a) {
        int caret = getCaretPosition();
        Point p = wordAtCaret();
        setSelectionStart(p.x);
        setSelectionEnd(p.y);
        replaceSelection(a);
        setCaretPosition(caret);
        int i = rowcolAtPos(caret).y;
        repaintLine(i, i == isBP(i));
    }

    public Point wordAtCaret() {
        Point p = new Point();
        char[] c = getText().toCharArray();
        int pos = getCaretPosition();
        p.y = pos;
        for (int i = pos; i < c.length; i++) {
            char a = c[i];
            if (!isIdentifierChar(a)) {
                p.y = i;
                break;
            }
        }
        for (int i = pos; i > 0; i--) {
            char a = c[i - 1];
            if (!isIdentifierChar(a)) {
                p.x = i;
                break;
            }
        }
        return p;
    }

    public void changedUpdate(DocumentEvent e) {
        handleUpdate(e);
    }

    public void insertUpdate(DocumentEvent e) {
        handleUpdate(e);
    }

    public void removeUpdate(DocumentEvent e) {
        handleUpdate(e);
    }

    /**
	 * 
	 * @param e
	 * <p>
	 * The only thing we know is that the document has changed from the e.getOffset()
	 * on, we can't expect that the change, insert or delete kept the lines intact... 
	 */
    private void handleUpdate(DocumentEvent e) {
        String[] srcline = getText().split(newline);
        int first = rowcolAtPos(e.getOffset()).y;
        if (first > srcline.length - 1) return;
        int vmLast = vm.getStmtSize() - 1;
        File f = first < vm.getStmtSize() ? vm.getStmt(first).getSrc() : vmLast >= 0 ? vm.getStmt(vmLast).getSrc() : new File(vm.getVmID());
        if (first > vmLast) {
            for (int i = first; i < srcline.length; i++) {
                vm.addStmt(i, JaguarPCode.factory(vm, f, srcline[i]));
            }
            ide.repaintLines(first, srcline.length);
            return;
        }
        int txtLast = srcline.length - 1;
        while (vmLast > first && txtLast > first && srcline[txtLast].equals(vm.getStmt(vmLast).toString())) {
            --vmLast;
            --txtLast;
        }
        while (vmLast > txtLast) {
            vm.removeStmt(txtLast);
            --vmLast;
        }
        while (vmLast < txtLast) {
            vm.addStmt(first + 1, JaguarPCode.factory(vm, f, srcline[txtLast]));
            --txtLast;
        }
        for (int i = first; i < txtLast + 1; i++) {
            vm.setStmt(i, JaguarPCode.factory(vm, f, srcline[i]));
        }
        ide.repaintLines(first, txtLast + 1);
    }

    public void mouseMoved(MouseEvent e) {
        Point p = e.getPoint();
        int pos = viewToModel(p);
        try {
            Rectangle r = modelToView(pos);
            if (p.x < r.x - 20 || p.x > r.x + r.width + 20 || p.y < r.y - 20 || p.y > r.y + r.height + 20) {
                setToolTipText(null);
                return;
            }
        } catch (BadLocationException e1) {
            setToolTipText(null);
            return;
        }
        ide.setTooltipForWordAtPos(pos);
    }

    public Point wordAtPos(int pos) {
        Point p = new Point();
        char[] c = getText().toCharArray();
        for (int i = pos; i < c.length; i++) {
            char a = c[i];
            if (!isIdentifierChar(a)) {
                p.y = i;
                break;
            }
        }
        for (int i = pos; i > 0; i--) {
            char a = c[i - 1];
            if (!isIdentifierChar(a)) {
                p.x = i;
                break;
            }
        }
        return p;
    }

    public void mouseDragged(MouseEvent e) {
    }

    /**
	 * @return the bpline
	 */
    public int getBpline() {
        return bpline;
    }

    /**
	 * @param i
	 */
    public void repaintLine(int i) {
        repaintLine(i, i == isBP(i));
    }
}
