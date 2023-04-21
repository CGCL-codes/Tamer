package gruntspud.ui.preferences;

import gruntspud.ColorUtil;
import gruntspud.Constants;
import gruntspud.Gruntspud;
import gruntspud.GruntspudContext;
import gruntspud.GruntspudUtil;
import gruntspud.StringUtil;
import gruntspud.ui.ColorComboBox;
import gruntspud.ui.GruntspudCheckBox;
import gruntspud.ui.JNumericTextField;
import gruntspud.ui.StringListComboBox;
import gruntspud.ui.UIUtil;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;

/**
 * UI component for editing of display options
 *
 * @author magicthize
 */
public class DisplayOptionsTab extends AbstractOptionsTab implements ActionListener {

    public static final String[] TAB_PLACEMENT = { "Top", "Left", "Bottom", "Right" };

    public static final String[] EXPLORER_LINE_STYLE = { "None", "Angled", "Horizontal" };

    private GruntspudCheckBox useSplitExplorer;

    private GruntspudCheckBox hideFilesInTree;

    private GruntspudCheckBox showRootTreeNode;

    private GruntspudCheckBox substTypesInTree;

    private GruntspudCheckBox useSystemIcons;

    private GruntspudCheckBox showLineEndings;

    private GruntspudCheckBox dockReportsAsTabs;

    private GruntspudCheckBox useToggledFlatMode;

    private GruntspudCheckBox useScrollingTabs;

    private GruntspudCheckBox caseInsensitiveSort;

    private GruntspudCheckBox sortFoldersFirst;

    private GruntspudCheckBox highlightReadOnlyAndMissing;

    private GruntspudCheckBox showFiltersOnToolBar;

    private GruntspudCheckBox showFullPathForHomeLocation;

    private GruntspudCheckBox showTextOnMainTabs;

    private GruntspudCheckBox filterTab;

    private GruntspudCheckBox ctrlModifier, altModifier, shiftModifier;

    private StringListComboBox dateFormat;

    private StringListComboBox flatCVSNodeTextMask;

    private StringListComboBox explorerCVSNodeTextMask;

    private StringListComboBox fileCVSNodeTextMask;

    private ColorComboBox editorBackground;

    private ColorComboBox editorForeground;

    private JComboBox tabPlacement, explorerLineStyle;

    private JButton chooseEditorFont;

    private FontLabel editorFont;

    private JNumericTextField tabSize;

    private GruntspudCheckBox commandOptionsNonModal;

    /**
     * Constructor for the DisplayOptionsTab object
     */
    public DisplayOptionsTab() {
        super("Display", UIUtil.getCachedIcon(Constants.ICON_TOOL_DISPLAY));
    }

    /**
     * DOCUMENT ME!
     *
     * @param context DOCUMENT ME!
     */
    public void init(GruntspudContext context) {
        super.init(context);
        setTabToolTipText("Options for how things are displayed.");
        setTabMnemonic('r');
        setTabLargeIcon(UIUtil.getCachedIcon(Constants.ICON_TOOL_LARGE_DISPLAY));
        setTabContext("UI");
        JPanel k = new JPanel(new FlowLayout(FlowLayout.CENTER));
        k.setOpaque(false);
        TitledBorder kt = new TitledBorder("Command options bypass modifiers");
        k.setBorder(kt);
        k.setLayout(new BorderLayout());
        JPanel q = new JPanel(new FlowLayout());
        q.setOpaque(false);
        q.add(ctrlModifier = new GruntspudCheckBox("Ctrl"));
        q.add(altModifier = new GruntspudCheckBox("Alt"));
        q.add(shiftModifier = new GruntspudCheckBox("Shift"));
        k.add(q, BorderLayout.CENTER);
        k.add(commandOptionsNonModal = new GruntspudCheckBox("Non modal dialog", context.getHost().getBooleanProperty(Constants.OPTIONS_DISPLAY_COMMAND_OPTIONS_NON_MODAL, false)), BorderLayout.SOUTH);
        int i = context.getHost().getIntegerProperty(Constants.OPTIONS_DISPLAY_COMMAND_OPTIONS_BYPASS_MASK, KeyEvent.CTRL_MASK + KeyEvent.SHIFT_MASK);
        ctrlModifier.setSelected((i & KeyEvent.CTRL_MASK) > 0);
        shiftModifier.setSelected((i & KeyEvent.SHIFT_MASK) > 0);
        altModifier.setSelected((i & KeyEvent.ALT_MASK) > 0);
        JPanel e = new JPanel(new GridBagLayout());
        e.setOpaque(false);
        TitledBorder et = new TitledBorder("Explorer");
        e.setBorder(et);
        GridBagConstraints gbce = new GridBagConstraints();
        gbce.insets = new Insets(3, 3, 3, 3);
        gbce.anchor = GridBagConstraints.WEST;
        gbce.fill = GridBagConstraints.BOTH;
        gbce.weightx = 2.0;
        UIUtil.jGridBagAdd(e, useSplitExplorer = new GruntspudCheckBox("Use 'split' explorer"), gbce, GridBagConstraints.REMAINDER);
        useSplitExplorer.setSelected(context.getHost().getBooleanProperty(Constants.OPTIONS_DISPLAY_SPLIT_EXPLORER, true));
        useSplitExplorer.setMnemonic('s');
        UIUtil.jGridBagAdd(e, caseInsensitiveSort = new GruntspudCheckBox("Case insensitive sort"), gbce, GridBagConstraints.REMAINDER);
        caseInsensitiveSort.setSelected(context.getHost().getBooleanProperty(Constants.OPTIONS_DISPLAY_CASE_INSENSITIVE_SORT, false));
        caseInsensitiveSort.setMnemonic('i');
        UIUtil.jGridBagAdd(e, sortFoldersFirst = new GruntspudCheckBox("Sort folders first"), gbce, GridBagConstraints.REMAINDER);
        sortFoldersFirst.setSelected(context.getHost().getBooleanProperty(Constants.OPTIONS_DISPLAY_SORT_FOLDERS_FIRST, false));
        sortFoldersFirst.setMnemonic('f');
        UIUtil.jGridBagAdd(e, showRootTreeNode = new GruntspudCheckBox("Show root node in tree"), gbce, GridBagConstraints.REMAINDER);
        showRootTreeNode.setSelected(context.getHost().getBooleanProperty(Constants.OPTIONS_DISPLAY_SHOW_ROOT_TREE_NODE, false));
        showRootTreeNode.setMnemonic('h');
        UIUtil.jGridBagAdd(e, substTypesInTree = new GruntspudCheckBox("Show subst. type icons"), gbce, GridBagConstraints.REMAINDER);
        substTypesInTree.setSelected(context.getHost().getBooleanProperty(Constants.OPTIONS_DISPLAY_SUBST_TYPES_IN_TREE, true));
        substTypesInTree.setMnemonic('i');
        UIUtil.jGridBagAdd(e, hideFilesInTree = new GruntspudCheckBox("Hide files in tree"), gbce, GridBagConstraints.REMAINDER);
        hideFilesInTree.setSelected(context.getHost().getBooleanProperty(Constants.OPTIONS_DISPLAY_HIDE_FILES_IN_TREE, false));
        hideFilesInTree.setMnemonic('h');
        gbce.weightx = 0.0;
        UIUtil.jGridBagAdd(e, new JLabel("Text Mask: "), gbce, GridBagConstraints.RELATIVE);
        gbce.weightx = 1.0;
        UIUtil.jGridBagAdd(e, explorerCVSNodeTextMask = new StringListComboBox(context, context.getHost().getProperty(Constants.OPTIONS_DISPLAY_EXPLORER_CVS_NODE_TEXT_MASK, Constants.DEFAULT_EXPLORER_CVS_NODE_TEXT_MASK), false), gbce, GridBagConstraints.REMAINDER);
        gbce.weightx = 0.0;
        UIUtil.jGridBagAdd(e, new JLabel("Line style: "), gbce, GridBagConstraints.RELATIVE);
        gbce.weightx = 1.0;
        UIUtil.jGridBagAdd(e, explorerLineStyle = new JComboBox(EXPLORER_LINE_STYLE), gbce, GridBagConstraints.REMAINDER);
        explorerLineStyle.setSelectedItem(context.getHost().getProperty(Constants.OPTIONS_DISPLAY_EXPLORER_LINE_STYLE, "None"));
        gbce.weightx = 2.0;
        gbce.weighty = 1.0;
        UIUtil.jGridBagAdd(e, new JLabel(), gbce, GridBagConstraints.REMAINDER);
        JPanel f = new JPanel(new GridBagLayout());
        f.setOpaque(false);
        TitledBorder ef = new TitledBorder("Flat");
        f.setBorder(ef);
        GridBagConstraints gbcf = new GridBagConstraints();
        gbcf.insets = gbce.insets;
        gbcf.anchor = GridBagConstraints.WEST;
        gbcf.fill = GridBagConstraints.BOTH;
        gbcf.weightx = 2.0;
        UIUtil.jGridBagAdd(f, useToggledFlatMode = new GruntspudCheckBox("Use toggled flat mode"), gbcf, GridBagConstraints.REMAINDER);
        useToggledFlatMode.setSelected(context.getHost().getBooleanProperty(Constants.OPTIONS_DISPLAY_USE_TOGGLED_FLATMODE, true));
        useToggledFlatMode.setMnemonic('s');
        gbcf.weightx = 0.0;
        gbcf.weighty = 1.0;
        UIUtil.jGridBagAdd(f, new JLabel("Text Mask: "), gbcf, GridBagConstraints.RELATIVE);
        gbcf.weightx = 1.0;
        UIUtil.jGridBagAdd(f, flatCVSNodeTextMask = new StringListComboBox(context, context.getHost().getProperty(Constants.OPTIONS_DISPLAY_FLAT_CVS_NODE_TEXT_MASK, Constants.DEFAULT_FLAT_CVS_NODE_TEXT_MASK), false), gbcf, GridBagConstraints.REMAINDER);
        JPanel d = new JPanel(new GridBagLayout());
        d.setOpaque(false);
        TitledBorder df = new TitledBorder("File");
        d.setBorder(df);
        GridBagConstraints gbcd = new GridBagConstraints();
        gbcd.insets = gbcf.insets;
        gbcd.anchor = GridBagConstraints.WEST;
        gbcd.fill = GridBagConstraints.BOTH;
        gbcd.weightx = 0.0;
        gbcd.weighty = 1.0;
        UIUtil.jGridBagAdd(d, new JLabel("Text Mask: "), gbcd, GridBagConstraints.RELATIVE);
        gbcd.weightx = 1.0;
        UIUtil.jGridBagAdd(d, fileCVSNodeTextMask = new StringListComboBox(context, context.getHost().getProperty(Constants.OPTIONS_DISPLAY_FILE_CVS_NODE_TEXT_MASK, Constants.DEFAULT_FILE_CVS_NODE_TEXT_MASK), false), gbcd, GridBagConstraints.REMAINDER);
        JPanel o = new JPanel(new GridBagLayout());
        o.setOpaque(false);
        o.setBorder(BorderFactory.createTitledBorder("General"));
        GridBagConstraints gbco = new GridBagConstraints();
        gbco.insets = gbcd.insets;
        gbco.anchor = GridBagConstraints.WEST;
        gbco.fill = GridBagConstraints.HORIZONTAL;
        if (Gruntspud.is14Plus()) {
            UIUtil.jGridBagAdd(o, useScrollingTabs = new GruntspudCheckBox("Use scrolling tabs"), gbco, GridBagConstraints.REMAINDER);
            useScrollingTabs.setSelected(context.getHost().getBooleanProperty(Constants.OPTIONS_DISPLAY_USE_SCROLLING_TABS, true));
            useScrollingTabs.setMnemonic('c');
        }
        UIUtil.jGridBagAdd(o, filterTab = new GruntspudCheckBox("Filter tab"), gbco, GridBagConstraints.REMAINDER);
        filterTab.setSelected(context.getHost().getBooleanProperty(Constants.OPTIONS_DISPLAY_FILTER_TAB, false));
        filterTab.setMnemonic('t');
        UIUtil.jGridBagAdd(o, showFiltersOnToolBar = new GruntspudCheckBox("Show filters on address bar"), gbco, GridBagConstraints.REMAINDER);
        showFiltersOnToolBar.setSelected(context.getHost().getBooleanProperty(Constants.OPTIONS_DISPLAY_SHOW_FILTERS_ON_TOOL_BAR, false));
        hideFilesInTree.setMnemonic('f');
        UIUtil.jGridBagAdd(o, showFullPathForHomeLocation = new GruntspudCheckBox("Show full home location"), gbco, GridBagConstraints.REMAINDER);
        showFullPathForHomeLocation.setSelected(context.getHost().getBooleanProperty(Constants.OPTIONS_DISPLAY_SHOW_FULL_PATH_FOR_HOME_LOCATION, true));
        showFullPathForHomeLocation.setMnemonic('u');
        UIUtil.jGridBagAdd(o, useSystemIcons = new GruntspudCheckBox("Use system icons if possible"), gbco, GridBagConstraints.REMAINDER);
        useSystemIcons.setSelected(context.getHost().getBooleanProperty(Constants.OPTIONS_DISPLAY_USE_SYSTEM_ICONS, true));
        useSystemIcons.setMnemonic('s');
        UIUtil.jGridBagAdd(o, showLineEndings = new GruntspudCheckBox("Show line endings (slow)"), gbco, GridBagConstraints.REMAINDER);
        showLineEndings.setSelected(context.getHost().getBooleanProperty(Constants.OPTIONS_DISPLAY_SHOW_LINE_ENDINGS, false));
        showLineEndings.setMnemonic('l');
        UIUtil.jGridBagAdd(o, highlightReadOnlyAndMissing = new GruntspudCheckBox("Highlight read only / missing"), gbco, GridBagConstraints.REMAINDER);
        highlightReadOnlyAndMissing.setSelected(context.getHost().getBooleanProperty(Constants.OPTIONS_DISPLAY_HIGHLIGHT_READ_ONLY_AND_MISSING_FILES, true));
        showLineEndings.setMnemonic('l');
        UIUtil.jGridBagAdd(o, showTextOnMainTabs = new GruntspudCheckBox("Show text on tabs (requires restart)"), gbco, GridBagConstraints.REMAINDER);
        showTextOnMainTabs.setSelected(context.getHost().getBooleanProperty(Constants.OPTIONS_DISPLAY_SHOW_TEXT_ON_MAIN_TABS, true));
        showTextOnMainTabs.setMnemonic('t');
        UIUtil.jGridBagAdd(o, dockReportsAsTabs = new GruntspudCheckBox("Dock reports as tabs"), gbco, GridBagConstraints.REMAINDER);
        dockReportsAsTabs.setSelected(context.getHost().getBooleanProperty(Constants.OPTIONS_DISPLAY_DOCK_REPORTS_AS_TABS, true));
        dockReportsAsTabs.setMnemonic('s');
        gbco.weightx = 0.0;
        UIUtil.jGridBagAdd(o, new JLabel("Tab placement"), gbco, GridBagConstraints.RELATIVE);
        gbco.weightx = 1.0;
        UIUtil.jGridBagAdd(o, tabPlacement = new JComboBox(TAB_PLACEMENT), gbco, GridBagConstraints.REMAINDER);
        tabPlacement.setSelectedIndex(context.getHost().getIntegerProperty(Constants.OPTIONS_DISPLAY_TAB_PLACEMENT, SwingConstants.TOP) - 1);
        gbco.weightx = 0.0;
        UIUtil.jGridBagAdd(o, new JLabel("Date format "), gbco, GridBagConstraints.RELATIVE);
        gbco.weightx = 1.0;
        UIUtil.jGridBagAdd(o, dateFormat = new StringListComboBox(context, context.getHost().getProperty(Constants.OPTIONS_DISPLAY_DATE_FORMAT, "&" + Constants.DEFAULT_DATE_FORMAT + "|dd/MM/yyyy hh:mm:ss|hh:mm:ss MM/dd/yyyy|MM/dd/yyyy hh:mm:ss"), false), gbco, GridBagConstraints.REMAINDER);
        dateFormat.setToolTipText("Uses Java's SimpleDateFormat class");
        gbco.weightx = 2.0;
        gbco.weighty = 1.0;
        UIUtil.jGridBagAdd(o, new JLabel(), gbco, GridBagConstraints.REMAINDER);
        JPanel z = new JPanel(new GridBagLayout());
        z.setOpaque(false);
        z.setBorder(BorderFactory.createTitledBorder("Editor"));
        GridBagConstraints gbcz = new GridBagConstraints();
        gbcz.insets = gbco.insets;
        gbcz.anchor = GridBagConstraints.WEST;
        gbcz.fill = GridBagConstraints.HORIZONTAL;
        gbcz.weighty = 0.0;
        gbcz.weightx = 0.0;
        UIUtil.jGridBagAdd(z, new JLabel("Font"), gbcz, 1);
        String s = context.getHost().getProperty(Constants.OPTIONS_EDITOR_FONT, "");
        Font font = StringUtil.stringToFont(s);
        if (font == null) {
            font = UIManager.getFont("EditorPane.font");
        }
        editorFont = new FontLabel(font);
        gbcz.weightx = 1.0;
        UIUtil.jGridBagAdd(z, editorFont, gbcz, GridBagConstraints.RELATIVE);
        gbcz.weightx = 0.0;
        UIUtil.jGridBagAdd(z, chooseEditorFont = new JButton("Choose"), gbcz, GridBagConstraints.REMAINDER);
        gbcz.weightx = 0.0;
        UIUtil.jGridBagAdd(z, new JLabel("Background"), gbcz, 1);
        gbcz.weightx = 2.0;
        UIUtil.jGridBagAdd(z, editorBackground = new ColorComboBox(), gbcz, GridBagConstraints.REMAINDER);
        editorBackground.setColor(ColorUtil.getColor(Constants.OPTIONS_EDITOR_BACKGROUND, UIManager.getColor("TextPane.background"), context));
        gbcz.weightx = 0.0;
        UIUtil.jGridBagAdd(z, new JLabel("Foreground"), gbcz, 1);
        gbcz.weightx = 2.0;
        UIUtil.jGridBagAdd(z, editorForeground = new ColorComboBox(), gbcz, GridBagConstraints.REMAINDER);
        editorForeground.setColor(ColorUtil.getColor(Constants.OPTIONS_EDITOR_FOREGROUND, UIManager.getColor("TextPane.foreground"), context));
        chooseEditorFont.addActionListener(this);
        chooseEditorFont.setMnemonic('c');
        gbcz.weighty = 1.0;
        UIUtil.jGridBagAdd(z, new JLabel("Tab size"), gbcz, 1);
        gbcz.weightx = 2.0;
        UIUtil.jGridBagAdd(z, tabSize = new JNumericTextField(new Integer(1), new Integer(256), new Integer(context.getHost().getIntegerProperty(Constants.OPTIONS_EDITOR_TAB_SIZE, 4))), gbcz, GridBagConstraints.REMAINDER);
        JPanel l = new JPanel(new GridBagLayout());
        l.setOpaque(false);
        GridBagConstraints gbcl = new GridBagConstraints();
        gbcl.anchor = GridBagConstraints.WEST;
        gbcl.fill = GridBagConstraints.BOTH;
        gbcl.weighty = 0.0;
        gbcl.weightx = 1.0;
        UIUtil.jGridBagAdd(l, k, gbcl, GridBagConstraints.REMAINDER);
        UIUtil.jGridBagAdd(l, d, gbcl, GridBagConstraints.REMAINDER);
        UIUtil.jGridBagAdd(l, f, gbcl, GridBagConstraints.REMAINDER);
        gbcl.weighty = 1.0;
        UIUtil.jGridBagAdd(l, e, gbcl, GridBagConstraints.REMAINDER);
        JPanel r = new JPanel(new GridBagLayout());
        r.setOpaque(false);
        GridBagConstraints gbcr = new GridBagConstraints();
        gbcr.anchor = GridBagConstraints.WEST;
        gbcr.fill = GridBagConstraints.BOTH;
        gbcr.weighty = 0.0;
        gbcr.weightx = 1.0;
        UIUtil.jGridBagAdd(r, z, gbcr, GridBagConstraints.REMAINDER);
        gbcr.weighty = 1.0;
        UIUtil.jGridBagAdd(r, o, gbcr, GridBagConstraints.REMAINDER);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc2 = new GridBagConstraints();
        gbc2.anchor = GridBagConstraints.CENTER;
        gbc2.fill = GridBagConstraints.BOTH;
        gbc2.weighty = 1.0;
        gbc2.weightx = 2.0;
        UIUtil.jGridBagAdd(this, l, gbc2, GridBagConstraints.RELATIVE);
        gbc2.weightx = 2.0;
        UIUtil.jGridBagAdd(this, r, gbc2, GridBagConstraints.REMAINDER);
    }

    /**
     * DOCUMENT ME!
     *
     * @param evt DOCUMENT ME!
     */
    public void actionPerformed(ActionEvent evt) {
        if (evt.getSource() == chooseEditorFont) {
            Font f = FontChooser.showDialog(this, editorFont.getChosenFont(), getContext());
            if (f != null) {
                editorFont.setChosenFont(f);
            }
        }
    }

    /**
     * Validate the options
     *
     * @return <code>true</code> if the options were ok
     */
    public boolean validateTab() {
        try {
            if (tabPlacement.getSelectedIndex() == -1) {
                throw new Exception("Must selected a tab placement");
            }
        } catch (Exception e) {
            GruntspudUtil.showErrorMessage(this, "Error", e);
            return false;
        }
        return true;
    }

    /**
     * Invoked when the tab is selection
     */
    public void tabSelected() {
    }

    /**
     * Apply the options
     */
    public void applyTab() {
        getContext().getHost().setBooleanProperty(Constants.OPTIONS_DISPLAY_COMMAND_OPTIONS_NON_MODAL, commandOptionsNonModal.isSelected());
        getContext().getHost().setIntegerProperty(Constants.OPTIONS_DISPLAY_COMMAND_OPTIONS_BYPASS_MASK, (ctrlModifier.isSelected() ? KeyEvent.CTRL_MASK : 0) + (shiftModifier.isSelected() ? KeyEvent.SHIFT_MASK : 0) + (altModifier.isSelected() ? KeyEvent.ALT_MASK : 0));
        getContext().getHost().setProperty(Constants.OPTIONS_DISPLAY_DATE_FORMAT, dateFormat.getStringListPropertyString());
        getContext().getHost().setBooleanProperty(Constants.OPTIONS_DISPLAY_USE_SYSTEM_ICONS, useSystemIcons.isSelected());
        getContext().getHost().setBooleanProperty(Constants.OPTIONS_DISPLAY_SPLIT_EXPLORER, useSplitExplorer.isSelected());
        getContext().getHost().setBooleanProperty(Constants.OPTIONS_DISPLAY_HIDE_FILES_IN_TREE, hideFilesInTree.isSelected());
        getContext().getHost().setBooleanProperty(Constants.OPTIONS_DISPLAY_SHOW_ROOT_TREE_NODE, showRootTreeNode.isSelected());
        getContext().getHost().setBooleanProperty(Constants.OPTIONS_DISPLAY_SHOW_LINE_ENDINGS, showLineEndings.isSelected());
        getContext().getHost().setProperty(Constants.OPTIONS_DISPLAY_EXPLORER_LINE_STYLE, (String) explorerLineStyle.getSelectedItem());
        getContext().getHost().setBooleanProperty(Constants.OPTIONS_DISPLAY_CASE_INSENSITIVE_SORT, caseInsensitiveSort.isSelected());
        getContext().getHost().setBooleanProperty(Constants.OPTIONS_DISPLAY_SORT_FOLDERS_FIRST, sortFoldersFirst.isSelected());
        getContext().getHost().setBooleanProperty(Constants.OPTIONS_DISPLAY_SHOW_FULL_PATH_FOR_HOME_LOCATION, showFullPathForHomeLocation.isSelected());
        getContext().getHost().setBooleanProperty(Constants.OPTIONS_DISPLAY_SUBST_TYPES_IN_TREE, substTypesInTree.isSelected());
        getContext().getHost().setBooleanProperty(Constants.OPTIONS_DISPLAY_USE_TOGGLED_FLATMODE, useToggledFlatMode.isSelected());
        getContext().getHost().setBooleanProperty(Constants.OPTIONS_DISPLAY_HIGHLIGHT_READ_ONLY_AND_MISSING_FILES, highlightReadOnlyAndMissing.isSelected());
        getContext().getHost().setBooleanProperty(Constants.OPTIONS_DISPLAY_DOCK_REPORTS_AS_TABS, dockReportsAsTabs.isSelected());
        getContext().getHost().setBooleanProperty(Constants.OPTIONS_DISPLAY_SHOW_TEXT_ON_MAIN_TABS, showTextOnMainTabs.isSelected());
        getContext().getHost().setBooleanProperty(Constants.OPTIONS_DISPLAY_SHOW_FILTERS_ON_TOOL_BAR, showFiltersOnToolBar.isSelected());
        getContext().getHost().setBooleanProperty(Constants.OPTIONS_DISPLAY_FILTER_TAB, filterTab.isSelected());
        if (useScrollingTabs != null) {
            getContext().getHost().setBooleanProperty(Constants.OPTIONS_DISPLAY_USE_SCROLLING_TABS, dockReportsAsTabs.isSelected());
        }
        getContext().getHost().setIntegerProperty(Constants.OPTIONS_DISPLAY_TAB_PLACEMENT, tabPlacement.getSelectedIndex() + 1);
        Font f = editorFont.getChosenFont();
        getContext().getHost().setProperty(Constants.OPTIONS_EDITOR_FONT, f.getName() + "," + f.getStyle() + "," + f.getSize());
        getContext().getHost().setProperty(Constants.OPTIONS_DISPLAY_EXPLORER_CVS_NODE_TEXT_MASK, explorerCVSNodeTextMask.getStringListPropertyString());
        getContext().getHost().setProperty(Constants.OPTIONS_DISPLAY_FILE_CVS_NODE_TEXT_MASK, fileCVSNodeTextMask.getStringListPropertyString());
        getContext().getHost().setProperty(Constants.OPTIONS_DISPLAY_FLAT_CVS_NODE_TEXT_MASK, flatCVSNodeTextMask.getStringListPropertyString());
        getContext().getHost().setProperty(Constants.OPTIONS_EDITOR_BACKGROUND, StringUtil.colorToString(editorBackground.getColor()));
        getContext().getHost().setProperty(Constants.OPTIONS_EDITOR_FOREGROUND, StringUtil.colorToString(editorForeground.getColor()));
        getContext().getHost().setProperty(Constants.OPTIONS_EDITOR_TAB_SIZE, String.valueOf(((Integer) tabSize.getValue()).intValue()));
    }
}
