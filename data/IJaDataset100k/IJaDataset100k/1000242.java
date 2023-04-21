package cx.fbn.nevernote.gui;

import com.trolltech.qt.gui.QAction;
import com.trolltech.qt.gui.QMenu;
import com.trolltech.qt.gui.QMenuBar;
import cx.fbn.nevernote.Global;

public class ExternalBrowserMenuBar extends QMenuBar {

    private final ExternalBrowse parent;

    public QAction printAction;

    public QAction emailAction;

    public QAction editFind;

    public QAction editUndo;

    public QAction editRedo;

    public QAction editCut;

    public QAction editPaste;

    public QAction editPasteWithoutFormat;

    public QAction editCopy;

    public QAction formatBold;

    public QAction formatItalic;

    public QAction formatUnderline;

    public QAction formatStrikethrough;

    public QAction formatSuperscript;

    public QAction formatSubscript;

    public QAction formatNumberList;

    public QAction formatBulletList;

    public QAction alignLeftAction;

    public QAction alignRightAction;

    public QAction alignCenterAction;

    public QAction horizontalLineAction;

    public QAction indentAction;

    public QAction outdentAction;

    public QAction spellCheckAction;

    public QMenu fileMenu;

    public QMenu editMenu;

    private QMenu formatMenu;

    private QMenu listMenu;

    private QMenu indentMenu;

    private QMenu alignMenu;

    public ExternalBrowserMenuBar(ExternalBrowse p) {
        parent = p;
        printAction = new QAction(tr("Print"), this);
        printAction.setToolTip(tr("Print the current note"));
        printAction.triggered.connect(parent, "printNote()");
        setupShortcut(printAction, "File_Print");
        emailAction = new QAction(tr("Email"), this);
        emailAction.setToolTip(tr("Email the current note"));
        emailAction.triggered.connect(parent, "emailNote()");
        setupShortcut(emailAction, "File_Email");
        editFind = new QAction(tr("Find In Note"), this);
        editFind.setToolTip(tr("Find a string in the current note"));
        editFind.triggered.connect(parent, "findText()");
        setupShortcut(editFind, "Edit_Find_In_Note");
        editUndo = new QAction(tr("Undo"), this);
        editUndo.setToolTip(tr("Undo"));
        editUndo.triggered.connect(parent.getBrowserWindow(), "undoClicked()");
        setupShortcut(editUndo, "Edit_Undo");
        editRedo = new QAction(tr("Redo"), this);
        editRedo.setToolTip(tr("Redo"));
        editRedo.triggered.connect(parent.getBrowserWindow(), "redoClicked()");
        setupShortcut(editRedo, "Edit_Redo");
        editCut = new QAction(tr("Cut"), this);
        editCut.setToolTip(tr("Cut"));
        editCut.triggered.connect(parent.getBrowserWindow(), "cutClicked()");
        setupShortcut(editCut, "Edit_Cut");
        editCopy = new QAction(tr("Copy"), this);
        editCopy.setToolTip(tr("Copy"));
        editCopy.triggered.connect(parent.getBrowserWindow(), "copyClicked()");
        setupShortcut(editCopy, "Edit_Copy");
        editPaste = new QAction(tr("Paste"), this);
        editPaste.setToolTip(tr("Paste"));
        editPaste.triggered.connect(parent.getBrowserWindow(), "pasteClicked()");
        setupShortcut(editPaste, "Edit_Paste");
        editPasteWithoutFormat = new QAction(tr("Paste Without Formatting"), this);
        editPasteWithoutFormat.setToolTip(tr("Paste Without Formatting"));
        editPasteWithoutFormat.triggered.connect(parent.getBrowserWindow(), "pasteWithoutFormattingClicked()");
        setupShortcut(editPasteWithoutFormat, "Edit_Paste_Without_Formatting");
        alignLeftAction = new QAction(tr("Left"), this);
        alignLeftAction.setToolTip(tr("Left Align"));
        alignLeftAction.triggered.connect(parent.getBrowserWindow(), "justifyLeftClicked()");
        setupShortcut(alignLeftAction, "Format_Alignment_Left");
        alignRightAction = new QAction(tr("Right"), this);
        alignRightAction.setToolTip(tr("Right Align"));
        alignRightAction.triggered.connect(parent.getBrowserWindow(), "justifyRightClicked()");
        setupShortcut(alignRightAction, "Format_Alignment_Right");
        alignCenterAction = new QAction(tr("Center"), this);
        alignCenterAction.setToolTip(tr("Center Align"));
        alignCenterAction.triggered.connect(parent.getBrowserWindow(), "justifyCenterClicked()");
        setupShortcut(alignCenterAction, "Format_Alignment_Center");
        formatBold = new QAction(tr("Bold"), this);
        formatBold.setToolTip(tr("Bold"));
        formatBold.triggered.connect(parent.getBrowserWindow(), "boldClicked()");
        setupShortcut(formatBold, "Format_Bold");
        formatItalic = new QAction(tr("Italic"), this);
        formatItalic.setToolTip(tr("Italic"));
        formatItalic.triggered.connect(parent.getBrowserWindow(), "italicClicked()");
        setupShortcut(formatItalic, "Format_Italic");
        formatUnderline = new QAction(tr("Underline"), this);
        formatUnderline.setToolTip(tr("Underline"));
        formatUnderline.triggered.connect(parent.getBrowserWindow(), "underlineClicked()");
        setupShortcut(formatUnderline, "Format_Underline");
        formatSuperscript = new QAction(tr("Superscript"), this);
        formatSuperscript.setToolTip(tr("Superscript"));
        formatSuperscript.triggered.connect(parent.getBrowserWindow(), "superscriptClicked()");
        setupShortcut(formatSuperscript, "Format_Superscript");
        formatSubscript = new QAction(tr("Subscript"), this);
        formatSubscript.setToolTip(tr("Subscript"));
        formatSubscript.triggered.connect(parent.getBrowserWindow(), "subscriptClicked()");
        setupShortcut(formatSubscript, "Format_Subscript");
        formatStrikethrough = new QAction(tr("Strikethrough"), this);
        formatStrikethrough.setToolTip(tr("Strikethrough"));
        formatStrikethrough.triggered.connect(parent.getBrowserWindow(), "strikethroughClicked()");
        setupShortcut(formatStrikethrough, "Format_Strikethrough");
        horizontalLineAction = new QAction(tr("Horizontal Line"), this);
        horizontalLineAction.setToolTip(tr("Horizontal Line"));
        horizontalLineAction.triggered.connect(parent.getBrowserWindow(), "hlineClicked()");
        setupShortcut(horizontalLineAction, "Format_Horizontal_Line");
        formatBulletList = new QAction(tr("Bulleted List"), this);
        formatBulletList.triggered.connect(parent.getBrowserWindow(), "bulletListClicked()");
        setupShortcut(formatBulletList, "Format_List_Bullet");
        formatNumberList = new QAction(tr("Numbered List"), this);
        formatNumberList.setText(tr("Numbered list"));
        formatNumberList.triggered.connect(parent.getBrowserWindow(), "numberListClicked()");
        setupShortcut(formatNumberList, "Format_List_Numbered");
        indentAction = new QAction(tr(">> Increase"), this);
        indentAction.setText(tr(">> Increase"));
        indentAction.triggered.connect(parent.getBrowserWindow(), "indentClicked()");
        setupShortcut(indentAction, "Format_Indent_Increase");
        outdentAction = new QAction(tr("<< Decrease"), this);
        outdentAction.setText(tr("<< Decrease"));
        outdentAction.triggered.connect(parent.getBrowserWindow(), "outdentClicked()");
        setupShortcut(outdentAction, "Format_Indent_Decrease");
        spellCheckAction = new QAction(tr("Spell Check"), this);
        spellCheckAction.setToolTip(tr("Check for spelling errors"));
        spellCheckAction.triggered.connect(parent.getBrowserWindow(), "spellCheckClicked()");
        setupShortcut(spellCheckAction, "Tools_Spell_Check");
        setupMenuBar();
    }

    public void setupMenuBar() {
        fileMenu = addMenu(tr("&File"));
        fileMenu.addSeparator();
        fileMenu.addAction(emailAction);
        fileMenu.addAction(printAction);
        fileMenu.addSeparator();
        editMenu = addMenu(tr("&Edit"));
        editMenu.addAction(editFind);
        editMenu.addSeparator();
        editMenu.addAction(editUndo);
        editMenu.addAction(editRedo);
        editMenu.addSeparator();
        editMenu.addAction(editCut);
        editMenu.addAction(editCopy);
        editMenu.addAction(editPaste);
        editMenu.addAction(editPasteWithoutFormat);
        formatMenu = addMenu(tr("&Format"));
        formatMenu.addAction(formatBold);
        formatMenu.addAction(formatUnderline);
        formatMenu.addAction(formatItalic);
        formatMenu.addSeparator();
        formatMenu.addAction(formatStrikethrough);
        formatMenu.addAction(horizontalLineAction);
        formatMenu.addSeparator();
        formatMenu.addAction(formatSuperscript);
        formatMenu.addAction(formatSubscript);
        formatMenu.addSeparator();
        alignMenu = formatMenu.addMenu(tr("Alignment"));
        alignMenu.addAction(alignLeftAction);
        alignMenu.addAction(alignCenterAction);
        alignMenu.addAction(alignRightAction);
        listMenu = formatMenu.addMenu(tr("Lists"));
        listMenu.addAction(formatBulletList);
        listMenu.addAction(formatNumberList);
        indentMenu = formatMenu.addMenu(tr("Indent"));
        indentMenu.addAction(indentAction);
        indentMenu.addAction(outdentAction);
        addMenu(fileMenu);
        addMenu(editMenu);
        addMenu(formatMenu);
    }

    private void setupShortcut(QAction action, String text) {
        if (!Global.shortcutKeys.containsAction(text)) return;
        action.setShortcut(Global.shortcutKeys.getShortcut(text));
    }
}
