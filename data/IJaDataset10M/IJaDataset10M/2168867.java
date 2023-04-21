package net.sourceforge.jcpusim.ui.tabs;

import net.sourceforge.jcpusim.Core;
import net.sourceforge.jcpusim.ui.MenuBuilder;
import net.sourceforge.jcpusim.ui.MenuBuilderItem;
import net.sourceforge.jcpusim.ui.SyntaxHighlighter;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.events.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;

/**
 * The Document class defines a document
 */
public class Document implements Tab, PaintListener {

    public static final int MODE_EDIT = 0;

    public static final int MODE_RUN = 1;

    /** shared resources and methods */
    protected Core core;

    /** the parent CTabFolder */
    public CTabFolder folder;

    /** the tab */
    public CTabItem cTabItem;

    /** */
    protected StyledText text;

    /** */
    protected StyledText lineNumbers;

    /** */
    protected String buffer;

    /** */
    protected int noOfLines = 1;

    /** */
    protected int lastTopPixel = 0;

    protected boolean modified;

    protected int mode;

    protected SyntaxHighlighter syntaxHighlighter;

    protected int line;

    protected Caret caret;

    /** menubar data, in order to be positioned on screen */
    protected final MenuBuilderItem[] textMenuData = { new MenuBuilderItem(200, MenuBuilderItem.MENUBAR_NO_PARENT, SWT.DROP_DOWN, "&Edit", true), new MenuBuilderItem(201, 200, SWT.PUSH, "&Undo\tCtrl+Z", true, "undo"), new MenuBuilderItem(202, 200, SWT.PUSH, "&Redo\tCtrl+Y", true, "redo"), new MenuBuilderItem(203, 200, SWT.SEPARATOR), new MenuBuilderItem(204, 200, SWT.PUSH, "Cu&t\tCtrl+X", true, "editcut"), new MenuBuilderItem(205, 200, SWT.PUSH, "&Copy\tCtrl+C", true, "editcopy"), new MenuBuilderItem(206, 200, SWT.PUSH, "&Paste\tCtrl+V", true, "editpaste"), new MenuBuilderItem(207, 200, SWT.SEPARATOR), new MenuBuilderItem(208, 200, SWT.PUSH, "&Find...\tCtrl+F", true, "find"), new MenuBuilderItem(207, 200, SWT.SEPARATOR), new MenuBuilderItem(208, 200, SWT.PUSH, "&Add Breakpoint\tCtrl+A", true), new MenuBuilderItem(208, 200, SWT.PUSH, "&Remove Breakpoint\tCtrl+R", true) };

    /**
	 * Create a new Document
	 * @param core - shared resources and methods
	 * @param folder
	 */
    public Document(Core core, CTabFolder folder) {
        this.core = core;
        this.folder = folder;
        modified = false;
        mode = MODE_EDIT;
        cTabItem = new CTabItem(folder, SWT.NONE);
        cTabItem.setImage(core.getIcon(core.ICON_SMALL, "edit"));
        cTabItem.setText("*Untitled.asm");
        Composite composite = new Composite(folder, SWT.NONE);
        composite.setLayout(new FormLayout());
        Menu textMenu = new Menu(folder.getShell(), SWT.CASCADE);
        MenuItem textCut = new MenuItem(textMenu, SWT.PUSH);
        textCut.setText("Cu&t\tCtrl+X");
        textCut.setImage(core.getIcon(core.ICON_SMALL, "editcut"));
        MenuItem textCopy = new MenuItem(textMenu, SWT.PUSH);
        textCopy.setText("&Copy\tCtrl+C");
        textCopy.setImage(core.getIcon(core.ICON_SMALL, "editcopy"));
        MenuItem textPaste = new MenuItem(textMenu, SWT.PUSH);
        textPaste.setText("&Paste\tCtrl+V");
        textPaste.setImage(core.getIcon(core.ICON_SMALL, "editpaste"));
        MenuItem textAddBreak = new MenuItem(textMenu, SWT.PUSH);
        textAddBreak.setText("&Add Breakpoint");
        MenuItem textRemBreak = new MenuItem(textMenu, SWT.PUSH);
        textRemBreak.setText("&Remove Breakpoint");
        textRemBreak.setEnabled(false);
        Menu labelMenu = new Menu(folder.getShell(), SWT.CASCADE);
        MenuItem labelAddBreak = new MenuItem(labelMenu, SWT.PUSH);
        labelAddBreak.setText("&Add Breakpoint");
        MenuItem labelRemBreak = new MenuItem(labelMenu, SWT.PUSH);
        labelRemBreak.setText("&Remove Breakpoint");
        labelRemBreak.setEnabled(false);
        lineNumbers = new StyledText(composite, SWT.RIGHT_TO_LEFT | SWT.Deactivate | SWT.READ_ONLY);
        lineNumbers.setFont(new Font(core.getDisplay(), "Lucida Console", 10, SWT.NORMAL));
        lineNumbers.setForeground(new Color(core.getDisplay(), 120, 120, 120));
        lineNumbers.setBackground(core.getDisplay().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
        lineNumbers.setCaret(null);
        lineNumbers.setCursor(new Cursor(core.getDisplay(), SWT.CURSOR_ARROW));
        lineNumbers.addSelectionListener(new SelectionListener() {

            public void widgetSelected(SelectionEvent e) {
                lineNumbers.setSelection(0, 0);
            }

            public void widgetDefaultSelected(SelectionEvent e) {
            }
        });
        lineNumbers.addFocusListener(new FocusListener() {

            public void focusGained(FocusEvent e) {
                text.setFocus();
            }

            public void focusLost(FocusEvent e) {
            }
        });
        lineNumbers.setMenu(labelMenu);
        lineNumbers.setText("1");
        text = new StyledText(composite, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
        text.setFont(new Font(core.getDisplay(), "Lucida Console", 10, SWT.NORMAL));
        text.setMenu(textMenu);
        FormData LineNumbersData = new FormData();
        LineNumbersData.top = new FormAttachment(0, 7);
        LineNumbersData.left = new FormAttachment(0, 5);
        LineNumbersData.right = new FormAttachment(0, 43);
        LineNumbersData.bottom = new FormAttachment(100, -24);
        lineNumbers.setLayoutData(LineNumbersData);
        FormData textData = new FormData();
        textData.top = new FormAttachment(0, 5);
        textData.left = new FormAttachment(lineNumbers, 1);
        textData.right = new FormAttachment(100, -5);
        textData.bottom = new FormAttachment(100, -5);
        text.setLayoutData(textData);
        syntaxHighlighter = new SyntaxHighlighter(core, this);
        text.addLineStyleListener(syntaxHighlighter);
        text.addPaintListener(this);
        text.addLineBackgroundListener(syntaxHighlighter);
        text.addTraverseListener(syntaxHighlighter);
        text.addMouseListener(syntaxHighlighter);
        cTabItem.setControl(composite);
        folder.setSelection(cTabItem);
        text.setFocus();
    }

    public boolean close() {
        if (modified) {
            MessageBox messageBox = new MessageBox(core.getDisplay().getActiveShell(), SWT.ICON_QUESTION | SWT.YES | SWT.NO | SWT.CANCEL);
            messageBox.setText("JCPUSim - Save");
            messageBox.setMessage("Do you want to save this file before you close it?");
            if (messageBox.open() == SWT.CANCEL) return false;
        }
        cTabItem.dispose();
        return true;
    }

    /**
	 * Set the mode
	 * @param mode to set
	 */
    public void setMode(int mode) {
        if (this.mode == mode) return;
        switch(mode) {
            case MODE_EDIT:
                text.setEditable(true);
                text.setCaret(caret);
                text.addLineBackgroundListener(syntaxHighlighter);
                reset();
                break;
            case MODE_RUN:
                text.setEditable(false);
                caret = text.getCaret();
                text.setCaret(null);
                text.setCursor(new Cursor(core.getDisplay(), SWT.CURSOR_ARROW));
                text.addSelectionListener(new SelectionListener() {

                    public void widgetSelected(SelectionEvent e) {
                        lineNumbers.setSelection(0, 0);
                    }

                    public void widgetDefaultSelected(SelectionEvent e) {
                    }
                });
                text.addFocusListener(new FocusListener() {

                    public void focusGained(FocusEvent e) {
                    }

                    public void focusLost(FocusEvent e) {
                    }
                });
                text.removeLineBackgroundListener(syntaxHighlighter);
                text.setSelection(0);
                line = 0;
                break;
        }
        this.mode = mode;
    }

    /**
	 * Get the mode
	 * @return mode
	 */
    public int getMode() {
        return mode;
    }

    /**
	 * 
	 */
    public void step() {
        text.setLineBackground(0, text.getLineCount(), core.getDisplay().getSystemColor(SWT.COLOR_LIST_BACKGROUND));
        if (hasLines()) {
            text.setSelection(text.getOffsetAtLine(line));
            text.setLineBackground(line++, 1, new Color(core.getDisplay(), 220, 255, 220));
        }
    }

    /**
	 * If there are lines, return true
	 * @return true if the editor has more lines
	 */
    public boolean hasLines() {
        if (line == text.getLineCount()) return false;
        return true;
    }

    /**
	 * 
	 */
    public void reset() {
        line = 0;
    }

    /**
	 * Get the text
	 * @return text 
	 */
    public StyledText getText() {
        return text;
    }

    /**
	 * Get the line numbers
	 * @return line numbers
	 */
    public StyledText getLineNumbers() {
        return lineNumbers;
    }

    /**
	 * Update all line numbers if top line changed
	 */
    public void updateLineNumbers() {
        StringBuffer b = new StringBuffer();
        String tmp = "" + text.getLineCount();
        ((FormData) lineNumbers.getLayoutData()).right = new FormAttachment(0, ((tmp.length() * 13) - (4 * (tmp.length() - 1))) + 1);
        lineNumbers.getParent().layout();
        if (noOfLines != text.getLineCount()) {
            for (int i = 1; i < text.getLineCount() + 1; i++) {
                b.append(i + "\n");
            }
            lineNumbers.setText(b.toString());
            noOfLines = text.getLineCount();
        }
    }

    public void paintControl(PaintEvent e) {
        if (lastTopPixel != text.getTopPixel()) {
            lineNumbers.setTopPixel(text.getTopPixel());
            lastTopPixel = text.getTopPixel();
        }
    }

    public void init() {
    }

    public void update() {
    }
}
