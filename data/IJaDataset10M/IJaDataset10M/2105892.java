package jp.hpl.terminal;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.WindowConstants;
import src.backend.Level;
import src.backend.TermChunk;
import src.backend.TermEntry;
import src.backend.exception.OutOfEntriesIndexException;
import src.backend.exception.OutOfIndexException;
import src.backend.terminal.FontChange;
import src.backend.terminal.TerminalGrouping;
import jp.hpl.common.data.DefaultMarathonDataModel;
import jp.hpl.common.data.IEditorOneFrame;
import jp.hpl.common.gui.DialogTool;
import jp.hpl.common.gui.Reminder;
import jp.hpl.common.thread.ProgressBarDialog;
import jp.hpl.map.mouseevent.MapCanvas;
import jp.hpl.terminal.gui.FontChangesList;
import jp.hpl.terminal.gui.GroupingInformationPanel;
import jp.hpl.terminal.gui.TerminalGroupingsList;
import jp.hpl.terminal.gui.TerminalRenderPanel;
import jp.hpl.terminal.gui.TerminalTextArea;
import jp.hpl.terminal.gui.TerminalsList;
import jp.hpl.terminal.prefs.TerminalEditorPreferences;

/**
 * Terminal viewer/editor
 * 
 * TODO some gui items need interface for modifying data
 * @author hogepiyo
 *
 */
public class MainTerminalFrame extends JFrame implements IEditorOneFrame {

    /**
	 * title prefix.
	 * title is set as "[PREFIX] [Loaded/Editing file name]"
	 */
    private static final String TITLE_PREFIX = "Terminal Editor One J (Powered by JUICE)";

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    /** data modification model */
    private DefaultMarathonDataModel model;

    /** progress bar dialog */
    private ProgressBarDialog progressBarDialog;

    /** text area for showing and/or editing terminal */
    private TerminalTextArea terminalText;

    private JScrollPane scrollTerminalText;

    /***/
    private TerminalsList terminals;

    private TerminalGroupingsList terminalGroupings;

    private FontChangesList fontChanges;

    private TerminalRenderPanel renderPanel;

    /** level for edit/viewing */
    private int levelIndex = 0;

    private Level levelForView;

    private String storedFileName = "";

    /** label that shows name of the level */
    private JLabel levelName = new JLabel("Level name");

    /** the chunk now editing */
    private TermChunk chunkForDraw = new TermChunk();

    /** reminder */
    public Reminder reminder;

    /** functions for menu items */
    private TerminalEditorMenuFunctions menuFunc;

    /**
	 * default constructor
	 *
	 */
    public MainTerminalFrame() {
        this.model = new DefaultMarathonDataModel(this);
        final MainTerminalFrame frame = this;
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent we) {
                if (reminder.isModified()) {
                    Toolkit.getDefaultToolkit().beep();
                    String[] obj = { "Not yet saved changes.", "Do you want to save this before closing?" };
                    int ret = JOptionPane.showConfirmDialog(frame, obj, "Confirm", JOptionPane.YES_NO_CANCEL_OPTION);
                    if (ret == JOptionPane.YES_OPTION) {
                        frame.menuFunc.doMenuItemSave();
                        frame.dispose();
                    } else if (ret == JOptionPane.NO_OPTION) {
                        frame.dispose();
                    } else {
                        frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
                    }
                }
            }
        });
        setTitle(TITLE_PREFIX);
        setSize(600, 480);
        reminder = new Reminder();
        JMenuBar menuBar = new JMenuBar();
        this.setJMenuBar(menuBar);
        menuFunc = new TerminalEditorMenuFunctions(this);
        menuFunc.setupMenuBar(menuBar);
        JPanel p = getPanel();
        this.getContentPane().add(p);
        progressBarDialog = new ProgressBarDialog(this);
        TerminalEditorPreferences preference = TerminalEditorPreferences.getInstance();
        try {
            preference.loadFromFile();
        } catch (IOException e1) {
            JOptionPane.showMessageDialog(this, "I/O error when reading/writing preference file");
            preference.setDefault();
        }
        setLocationRelativeTo(null);
    }

    private JPanel getPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        JPanel palette = getListPanel();
        split.add(palette);
        JSplitPane sp = getRenderAndTextSplitePane();
        split.add(sp);
        panel.add(split);
        return panel;
    }

    /**
	 * 
	 * @return
	 */
    private JSplitPane getRenderAndTextSplitePane() {
        JSplitPane sp = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        JPanel rPanel = new JPanel();
        rPanel.setLayout(new BoxLayout(rPanel, BoxLayout.Y_AXIS));
        renderPanel = new TerminalRenderPanel(this);
        rPanel.add(renderPanel);
        sp.add(rPanel);
        terminalText = new TerminalTextArea(this);
        terminalText.setLineWrap(true);
        scrollTerminalText = new JScrollPane(terminalText);
        JPanel p5 = new JPanel();
        p5.setLayout(new BoxLayout(p5, BoxLayout.Y_AXIS));
        p5.add(new JLabel("Terminal Text"));
        p5.add(scrollTerminalText);
        sp.add(p5);
        return sp;
    }

    /**
	 * create palette panel of terminal item list(at the left)
	 * @return palette panel
	 */
    private JPanel getListPanel() {
        terminals = new TerminalsList(this, new DefaultListModel());
        terminalGroupings = new TerminalGroupingsList(this, new DefaultListModel());
        fontChanges = new FontChangesList(this, new DefaultListModel());
        JScrollPane scrollTerminals = new JScrollPane(terminals);
        JScrollPane scrollTerminalGroupings = new JScrollPane(terminalGroupings);
        JScrollPane scrollFontChanges = new JScrollPane(fontChanges);
        JPanel palette = new JPanel();
        palette.setLayout(new BoxLayout(palette, BoxLayout.Y_AXIS));
        palette.add(levelName);
        palette.add(new JLabel("Terminals"));
        palette.add(scrollTerminals);
        palette.add(new JLabel("Terminal Groupings"));
        palette.add(scrollTerminalGroupings);
        palette.add(new JLabel("Font Changes"));
        palette.add(scrollFontChanges);
        return palette;
    }

    /**
	 * fill font changes list box with selected terminal's font changing settings
	 * @param entryIndex
	 * @param chunk
	 * @throws OutOfIndexException 
	 */
    public void fillFontChangesList(int entryIndex, TermChunk chunk) throws OutOfIndexException {
        this.fontChanges.fontChangesModel.clear();
        try {
            TermEntry entry = chunk.getEntry(entryIndex);
            for (int i = 0; i < entry.getFontChangesCount(); i++) {
                FontChange font = entry.getFontChange(i);
                String p = "";
                p += "From:" + font.getIndex();
                p += " face:";
                if (font.isBold()) {
                    p += "B";
                }
                if (font.isItalic()) {
                    p += "I";
                }
                if (font.isUnderline()) {
                    p += "U";
                }
                p += " color:" + font.getColor();
                fontChanges.fontChangesModel.addElement(p);
            }
        } catch (OutOfEntriesIndexException e) {
        }
    }

    /**
	 * fill the list of terminal groupings
	 * @param terminalEntryIndex terminal index
	 * @throws OutOfIndexException out of index for groupings list
	 */
    public void fillTerminalGroupingsList(int terminalEntryIndex, TermChunk chunk) throws OutOfIndexException {
        terminalGroupings.terminalGroupingsModel.clear();
        try {
            TermEntry entry = chunk.getEntry(terminalEntryIndex);
            for (int i = 0; i < entry.getTerminalGroupingCount(); i++) {
                TerminalGrouping group = entry.getTerminalGrouping(i);
                String prefix = "";
                int gtype = group.getType();
                if (gtype == TerminalGrouping.TYPE_UNFINISHED_GROUP || gtype == TerminalGrouping.TYPE_SUCCESS_GROUP || gtype == TerminalGrouping.TYPE_FAILURE_GROUP || gtype == TerminalGrouping.TYPE_END_GROUP) {
                } else if (gtype == TerminalGrouping.TYPE_LOGOFF_GROUP || gtype == TerminalGrouping.TYPE_LOGON_GROUP) {
                    prefix = " --";
                } else if (gtype == TerminalGrouping.TYPE_CAMERA_GROUP || gtype == TerminalGrouping.TYPE_CHECKPOINT_GROUP || gtype == TerminalGrouping.TYPE_INFORMATION_GROUP || gtype == TerminalGrouping.TYPE_INTERLEVEL_TELEPORT_GROUP || gtype == TerminalGrouping.TYPE_INTRALEVEL_TELEPORT_GROUP || gtype == TerminalGrouping.TYPE_MOVIE_GROUP || gtype == TerminalGrouping.TYPE_PICT_GROUP || gtype == TerminalGrouping.TYPE_SOUND_GROUP || gtype == TerminalGrouping.TYPE_STATIC_GROUP || gtype == TerminalGrouping.TYPE_TAG_GROUP || gtype == TerminalGrouping.TYPE_TRACK_GROUP) {
                    prefix = " ----";
                }
                terminalGroupings.terminalGroupingsModel.addElement(prefix + group.getTypeName());
            }
            if (entry.getTerminalGroupingCount() > 0) {
                setTerminalText(entry, 0);
                terminalGroupings.setSelectedIndex(0);
            }
            renderPanel.getInfoPanel().update();
        } catch (OutOfEntriesIndexException e) {
        }
    }

    /**
	 * set or change terminal text
	 * @param entry terminal entry in the chunk(terminal)
	 * @param gid grouping index
	 * @throws OutOfIndexException 
	 */
    public void setTerminalText(TermEntry entry, int gid) throws OutOfIndexException {
        String str = new String(entry.getTextGroupingDecoded(gid));
        str = str.replaceAll("\r", "\n");
        this.terminalText.setText(str);
        scrollTerminalText.getVerticalScrollBar().setValue(0);
        updateRenderPanel(entry, gid);
    }

    public void updateRenderPanel(TermEntry entry, int gid) {
        this.renderPanel.setTerminal(entry, gid);
        try {
            renderPanel.updatePanel(menuFunc.getEntry(), terminalGroupings.getSelectedIndex());
        } catch (OutOfEntriesIndexException e) {
        }
    }

    /**
	 * fill list of terminals (entries per chunk)
	 * @throws OutOfIndexException 
	 * @throws OutOfEntriesIndexException 
	 */
    public void fillTerminals(TermChunk chunk) throws OutOfEntriesIndexException, OutOfIndexException {
        chunkForDraw = chunk;
        terminals.terminalsModel.clear();
        for (int i = 0; i < chunkForDraw.getNumEntries(); i++) {
            terminals.terminalsModel.addElement("terminal " + i);
        }
        terminalGroupings.removeAll();
        fontChanges.removeAll();
        terminalText.setText("");
        if (chunkForDraw.getNumEntries() > 0) {
            terminals.setSelectedIndex(0);
            fillTerminalGroupingsList(0, chunkForDraw);
            fillFontChangesList(0, chunkForDraw);
        }
    }

    public void addChildFrame(JFrame frm) throws Exception {
    }

    public MapCanvas getCanvas() {
        return null;
    }

    public int getLevelIndex() {
        return this.levelIndex;
    }

    public void initLoad() throws Exception {
        loadLevel(0);
        menuFunc.getMenuLevel().setEnabled(true);
    }

    public void loadLevel(int level) {
        this.levelIndex = level;
        this.levelForView = model.getMapData().getLevel(level);
        chunkForDraw = levelForView.getTermChunk();
        levelName.setText("\"" + levelForView.getlevelSelectName() + "\"");
        terminals.terminalsModel.clear();
        this.terminalGroupings.terminalGroupingsModel.clear();
        this.fontChanges.fontChangesModel.clear();
        try {
            fillTerminals(chunkForDraw);
        } catch (OutOfEntriesIndexException e1) {
            DialogTool.warn(e1, this, OutOfEntriesIndexException.ERROR_MESSAGE);
        } catch (OutOfIndexException e1) {
            DialogTool.warn(e1, this, OutOfIndexException.ERROR_MESSAGE);
        }
    }

    public void removeChildFrame(JFrame frm) throws Exception {
    }

    public void setCameraCenter(int x, int y) {
    }

    public void turnProgOff() {
        progressBarDialog.setVisible(false);
    }

    public void turnProgOn() {
        progressBarDialog.setVisible(true);
    }

    public TermChunk getChunkForDraw() {
        return chunkForDraw;
    }

    public FontChangesList getFontChanges() {
        return fontChanges;
    }

    public TerminalGroupingsList getTerminalGroupings() {
        return terminalGroupings;
    }

    public TerminalsList getTerminals() {
        return terminals;
    }

    public TerminalTextArea getTerminalText() {
        return terminalText;
    }

    public String getStoredFileName() {
        return storedFileName;
    }

    public void setStoredFileName(String storedFileName) {
        this.storedFileName = storedFileName;
    }

    public DefaultMarathonDataModel getModel() {
        return model;
    }

    public void setTitle(String suffix) {
        super.setTitle(TITLE_PREFIX + " [" + suffix + "]");
    }

    public ProgressBarDialog getProgressBarDialog() {
        return progressBarDialog;
    }

    public TerminalEditorMenuFunctions getMenuFunc() {
        return menuFunc;
    }

    public void updateTerminalRenderPanel(TermEntry entry, int gid) {
        renderPanel.updatePanel(entry, gid);
    }
}
