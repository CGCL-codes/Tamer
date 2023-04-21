package org.yaoqiang.graph.editor.swing;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Locale;
import javax.swing.AbstractAction;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.TransferHandler;
import javax.swing.UIManager;
import org.yaoqiang.graph.action.GraphActions;
import org.yaoqiang.graph.editor.action.CheckBoxMenuItem;
import org.yaoqiang.graph.editor.action.EditorActions;
import org.yaoqiang.graph.editor.util.EditorUtils;
import org.yaoqiang.graph.util.Constants;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxResources;

/**
 * BaseMenuBar
 * 
 * @author Shi Yaoqiang(shi_yaoqiang@yahoo.com)
 */
public class BaseMenuBar extends JMenuBar {

    private static final long serialVersionUID = 4060203894740766714L;

    private static CheckBoxMenuItem warningMenuItem;

    private static CheckBoxMenuItem outlineMenuItem;

    private static CheckBoxMenuItem rulersMenuItem;

    private static CheckBoxMenuItem gridMenuItem;

    private static CheckBoxMenuItem auxiliaryMenuItem;

    protected void populateFileMenu(BaseEditor editor) {
        JMenu menu = add(new JMenu(mxResources.get("file")));
        menu.add(editor.bind(mxResources.get("new"), EditorActions.getAction(EditorActions.NEW), "/org/yaoqiang/graph/editor/images/new.png"));
        menu.add(editor.bind(mxResources.get("openFile"), EditorActions.getAction(EditorActions.OPEN), "/org/yaoqiang/graph/editor/images/open.png"));
        menu.add(editor.bind(mxResources.get("reload"), EditorActions.getAction(EditorActions.RELOAD), "/org/yaoqiang/graph/editor/images/reload.png"));
        menu.addSeparator();
        menu.add(editor.bind(mxResources.get("save"), EditorActions.getSaveAction(), "/org/yaoqiang/graph/editor/images/save.png"));
        menu.add(editor.bind(mxResources.get("saveAs"), EditorActions.getSaveAsAction(), "/org/yaoqiang/graph/editor/images/save_as.png"));
        menu.add(editor.bind(mxResources.get("saveAsPNG"), EditorActions.getSaveAsPNG(), "/org/yaoqiang/graph/editor/images/save_as_png.png"));
        menu.addSeparator();
        populateFile2Menu(editor, menu);
    }

    protected void populateFile2Menu(BaseEditor editor, JMenu menu) {
        menu.add(editor.bind(mxResources.get("pageSetup"), GraphActions.getAction(GraphActions.PAGE_SETUP), "/org/yaoqiang/graph/editor/images/page_setup.png"));
        menu.add(editor.bind(mxResources.get("print"), GraphActions.getAction(GraphActions.PRINT), "/org/yaoqiang/graph/editor/images/printer.png"));
        menu.addSeparator();
        JMenu recentFilesmenu = (JMenu) menu.add(new JMenu(mxResources.get("recentFiles")));
        recentFilesmenu.setIcon(new ImageIcon(BaseMenuBar.class.getResource("/org/yaoqiang/graph/editor/images/recent.png")));
        editor.setRecentFilesmenu(recentFilesmenu);
        populateRecentFileMenu(editor);
        menu.addSeparator();
        menu.add(editor.bind(mxResources.get("exit"), EditorActions.getAction(EditorActions.EXIT), "/org/yaoqiang/graph/editor/images/exit.png"));
    }

    protected void populateRecentFileMenu(BaseEditor editor) {
        EditorUtils.initRecentFileList(editor);
    }

    protected void populateEditMenu(BaseEditor editor) {
        JMenu menu = add(new JMenu(mxResources.get("edit")));
        menu.add(editor.bind(mxResources.get("undo"), EditorActions.getAction(EditorActions.UNDO), "/org/yaoqiang/graph/editor/images/undo.png"));
        menu.add(editor.bind(mxResources.get("redo"), EditorActions.getAction(EditorActions.REDO), "/org/yaoqiang/graph/editor/images/redo.png"));
        menu.addSeparator();
        menu.add(editor.bind(mxResources.get("cut"), TransferHandler.getCutAction(), "/org/yaoqiang/graph/editor/images/cut.png"));
        menu.add(editor.bind(mxResources.get("copy"), TransferHandler.getCopyAction(), "/org/yaoqiang/graph/editor/images/copy.png"));
        menu.add(editor.bind(mxResources.get("paste"), TransferHandler.getPasteAction(), "/org/yaoqiang/graph/editor/images/paste.png"));
        menu.addSeparator();
        menu.add(editor.bind(mxResources.get("delete"), GraphActions.getAction(GraphActions.DELETE), "/org/yaoqiang/graph/editor/images/delete.png"));
        menu.addSeparator();
        JMenu addmenu = (JMenu) menu.add(new JMenu(mxResources.get("addPage")));
        addmenu.add(editor.bind(mxResources.get("dirHorizontal"), GraphActions.getAddPageAction(true)));
        addmenu.add(editor.bind(mxResources.get("dirVertical"), GraphActions.getAddPageAction(false)));
        JMenu removemenu = (JMenu) menu.add(new JMenu(mxResources.get("removePage")));
        removemenu.add(editor.bind(mxResources.get("dirHorizontal"), GraphActions.getRemovePageAction(true)));
        removemenu.add(editor.bind(mxResources.get("dirVertical"), GraphActions.getRemovePageAction(false)));
        menu.addSeparator();
        menu.add(editor.bind(mxResources.get("selectAll"), GraphActions.getAction(GraphActions.SELECT_ALL), "/org/yaoqiang/graph/editor/images/select_all.png"));
        menu.add(editor.bind(mxResources.get("selectNone"), GraphActions.getAction(GraphActions.SELECT_NONE), "/org/yaoqiang/graph/editor/images/select_none.png"));
    }

    protected void populateViewMenu(BaseEditor editor) {
        JMenu menu = add(new JMenu(mxResources.get("view")));
        warningMenuItem = new CheckBoxMenuItem(editor, mxResources.get("warning"), "showWarning");
        menu.add(warningMenuItem);
        outlineMenuItem = new CheckBoxMenuItem(editor, mxResources.get("outline"), "showOutline");
        menu.add(outlineMenuItem);
        rulersMenuItem = new CheckBoxMenuItem(editor, mxResources.get("rulers"), "showRulers");
        menu.add(rulersMenuItem);
        gridMenuItem = new CheckBoxMenuItem(editor, mxResources.get("grid"), "showGrid");
        menu.add(gridMenuItem);
        auxiliaryMenuItem = new CheckBoxMenuItem(editor, mxResources.get("auxiliary"), "showAuxiliary");
        menu.add(auxiliaryMenuItem);
        menu.addSeparator();
        JMenu submenu = (JMenu) menu.add(new JMenu(mxResources.get("gridstyle")));
        submenu.setIcon(new ImageIcon(BaseMenuBar.class.getResource("/org/yaoqiang/graph/editor/images/grid.png")));
        submenu.add(editor.bind(mxResources.get("gridSize"), GraphActions.getAction(GraphActions.GRID_SIZE)));
        submenu.add(editor.bind(mxResources.get("gridColor"), GraphActions.getAction(GraphActions.GRID_COLOR)));
        submenu.addSeparator();
        submenu.add(editor.bind(mxResources.get("dot"), GraphActions.getGridStyleAction(mxGraphComponent.GRID_STYLE_DOT)));
        submenu.add(editor.bind(mxResources.get("cross"), GraphActions.getGridStyleAction(mxGraphComponent.GRID_STYLE_CROSS)));
        submenu.add(editor.bind(mxResources.get("line"), GraphActions.getGridStyleAction(mxGraphComponent.GRID_STYLE_LINE)));
        submenu.add(editor.bind(mxResources.get("dashed"), GraphActions.getGridStyleAction(mxGraphComponent.GRID_STYLE_DASHED)));
        menu.addSeparator();
        menu.add(editor.bind(mxResources.get("backgroundColor"), GraphActions.getAction(GraphActions.BACKGROUND), "/org/yaoqiang/graph/editor/images/fillcolor.gif"));
        menu.addSeparator();
        menu.add(editor.bind(mxResources.get("autolayout"), GraphActions.getAutoLayoutAction(), "/org/yaoqiang/graph/editor/images/auto_layout.png"));
        menu.add(editor.bind(mxResources.get("rotateSwimlane"), GraphActions.getAction(GraphActions.ROTATE_SWIMLANE), "/org/yaoqiang/graph/editor/images/rotate_swimlane.png"));
        menu.addSeparator();
        submenu = (JMenu) menu.add(new JMenu(mxResources.get("zoom")));
        submenu.setIcon(new ImageIcon(BaseMenuBar.class.getResource("/org/yaoqiang/graph/editor/images/zoom.png")));
        submenu.add(editor.bind("400%", GraphActions.getScaleAction(4)));
        submenu.add(editor.bind("200%", GraphActions.getScaleAction(2)));
        submenu.add(editor.bind("150%", GraphActions.getScaleAction(1.5)));
        submenu.add(editor.bind("100%", GraphActions.getScaleAction(1)));
        submenu.add(editor.bind("75%", GraphActions.getScaleAction(0.75)));
        submenu.add(editor.bind("50%", GraphActions.getScaleAction(0.5)));
        submenu.addSeparator();
        submenu.add(editor.bind(mxResources.get("page"), GraphActions.getAction(GraphActions.ZOOM_FIT_PAGE)));
        submenu.add(editor.bind(mxResources.get("width"), GraphActions.getAction(GraphActions.ZOOM_FIT_WIDTH)));
        submenu.add(editor.bind(mxResources.get("custom"), GraphActions.getAction(GraphActions.ZOOM_CUSTOM)));
        menu.addSeparator();
        menu.add(editor.bind(mxResources.get("zoomIn"), GraphActions.getAction(GraphActions.ZOOM_IN), "/org/yaoqiang/graph/editor/images/zoom_in.png"));
        menu.add(editor.bind(mxResources.get("actualSize"), GraphActions.getAction(GraphActions.ZOOM_ACTUAL), "/org/yaoqiang/graph/editor/images/zoomactual.png"));
        menu.add(editor.bind(mxResources.get("zoomOut"), GraphActions.getAction(GraphActions.ZOOM_OUT), "/org/yaoqiang/graph/editor/images/zoom_out.png"));
    }

    protected void populateSettingsMenu(BaseEditor editor) {
        JMenu menu = add(new JMenu(mxResources.get("settings")));
        populateDefaultSettingsMenu(editor, menu);
        populateLanguageMenu(editor, menu);
        populateThemeMenu(editor, menu);
    }

    protected void populateDefaultSettingsMenu(BaseEditor editor, JMenu menu) {
        menu.add(editor.bind(mxResources.get("ElementStyles"), EditorActions.getAction(EditorActions.ELEMENT_STYLES), "/org/yaoqiang/graph/editor/images/colors.png"));
        menu.add(editor.bind(mxResources.get("DefaultSettings"), EditorActions.getAction(EditorActions.DEFAULT_SETTINGS), "/org/yaoqiang/graph/editor/images/config.png"));
    }

    protected void populateThemeMenu(final BaseEditor editor, JMenu menu) {
        JMenu submenu = (JMenu) menu.add(new JMenu(mxResources.get("theme")));
        submenu.setIcon(new ImageIcon(BaseMenuBar.class.getResource("/org/yaoqiang/graph/editor/images/theme.png")));
        ButtonGroup themeGroup = new ButtonGroup();
        UIManager.LookAndFeelInfo[] lafs = UIManager.getInstalledLookAndFeels();
        for (int i = lafs.length - 1; i >= 0; i--) {
            final String clazz = lafs[i].getClassName();
            JRadioButtonMenuItem item = new JRadioButtonMenuItem(new AbstractAction(lafs[i].getName()) {

                private static final long serialVersionUID = 1L;

                public void actionPerformed(ActionEvent e) {
                    editor.setLookAndFeel(clazz);
                }
            });
            if (clazz.equals(Constants.SETTINGS.getProperty("Theme", "com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel"))) {
                item.setSelected(true);
            }
            themeGroup.add(item);
            submenu.add(item);
        }
    }

    protected void populateHelpMenu(final BaseEditor editor) {
        JMenu menu = add(new JMenu(mxResources.get("help")));
        JMenuItem item = menu.add(new JMenuItem(mxResources.get("aboutGraphEditor")));
        item.setIcon(new ImageIcon(BaseMenuBar.class.getResource("/org/yaoqiang/graph/editor/images/help.png")));
        item.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                editor.about();
            }
        });
    }

    protected void populateLanguageMenu(BaseEditor editor, JMenu menu) {
        JMenu submenu = (JMenu) menu.add(new JMenu(mxResources.get("language")));
        submenu.setIcon(new ImageIcon(BaseMenuBar.class.getResource("/org/yaoqiang/graph/editor/images/language.png")));
        submenu.add(new JRadioButtonMenuItem(editor.bind(new Locale("de").getDisplayName(), EditorActions.getLocaleAction("de"))));
        submenu.add(new JRadioButtonMenuItem(editor.bind(new Locale("ru").getDisplayName(), EditorActions.getLocaleAction("ru"))));
        submenu.add(new JRadioButtonMenuItem(editor.bind(new Locale("es").getDisplayName(), EditorActions.getLocaleAction("es"))));
        submenu.add(new JRadioButtonMenuItem(editor.bind(new Locale("fr").getDisplayName(), EditorActions.getLocaleAction("fr"))));
        submenu.add(new JRadioButtonMenuItem(editor.bind(new Locale("it").getDisplayName(), EditorActions.getLocaleAction("it"))));
        submenu.add(new JRadioButtonMenuItem(editor.bind(new Locale("nl").getDisplayName(), EditorActions.getLocaleAction("nl"))));
        submenu.add(new JRadioButtonMenuItem(editor.bind(new Locale("pl").getDisplayName(), EditorActions.getLocaleAction("pl"))));
        submenu.add(new JRadioButtonMenuItem(editor.bind(new Locale("pt").getDisplayName(), EditorActions.getLocaleAction("pt"))));
        submenu.add(new JRadioButtonMenuItem(editor.bind(new Locale("en").getDisplayName(), EditorActions.getLocaleAction("en"))));
        submenu.add(new JRadioButtonMenuItem(editor.bind(new Locale("zh").getDisplayName(), EditorActions.getLocaleAction("zh_CN"))));
        for (Component com : submenu.getMenuComponents()) {
            JRadioButtonMenuItem item = (JRadioButtonMenuItem) com;
            if (item.getText().equals(new Locale(Constants.SETTINGS.getProperty("Locale", "en")).getDisplayName())) {
                item.setSelected(true);
            } else if (Constants.SETTINGS.getProperty("Locale", "en").equals("zh_CN") && item.getText().equals(new Locale("zh").getDisplayName())) {
                item.setSelected(true);
            }
        }
    }

    public static CheckBoxMenuItem getWarningMenuItem() {
        return warningMenuItem;
    }

    public static CheckBoxMenuItem getOutlineMenuItem() {
        return outlineMenuItem;
    }

    public static CheckBoxMenuItem getRulersMenuItem() {
        return rulersMenuItem;
    }

    public static CheckBoxMenuItem getGridMenuItem() {
        return gridMenuItem;
    }

    public static CheckBoxMenuItem getAuxiliaryMenuItem() {
        return auxiliaryMenuItem;
    }
}
