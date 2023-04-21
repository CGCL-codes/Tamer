package org.makagiga.tabs;

import static org.makagiga.commons.UI._;
import javax.swing.text.JTextComponent;
import org.makagiga.commons.MApplication;
import org.makagiga.commons.MIcon;
import org.makagiga.commons.help.HelpContext;
import org.makagiga.commons.swing.MMenu;
import org.makagiga.commons.swing.MToolBar;
import org.makagiga.console.Console;
import org.makagiga.editors.AbstractTextEditor;
import org.makagiga.editors.DocumentHelp;

/**
 * @since 3.0, 4.0 (org.makagiga.tabs package)
 */
public class ConsoleTab extends AbstractTextEditor<JTextComponent> implements DocumentHelp {

    private final Console console;

    public ConsoleTab(final Console console) {
        super(console.getOutput(), false);
        this.console = console;
        setMetaInfo(_("Console"), MIcon.stock("ui/console"));
        console.getToolBar().setVisible(false);
        addCenter(console);
    }

    @Override
    public void focus() {
        console.getCommandLine().makeDefault();
    }

    @Override
    public MToolBar.TextPosition getPreferredToolBarTextPosition() {
        return MToolBar.TextPosition.ALONGSIDE_ICONS;
    }

    /**
	 * @inheritDoc
	 *
	 * @since 4.2
	 */
    @Override
    public HelpContext showHelp() {
        console.executeCommand("help", true);
        return null;
    }

    @Override
    public void resetZoom() {
        super.resetZoom();
        MApplication.consoleFontSize.set(getFontSize());
    }

    @Override
    public void zoom(final ZoomType type) {
        super.zoom(type);
        MApplication.consoleFontSize.set(getFontSize());
    }

    @Override
    public void updateMenu(final String type, final MMenu menu) {
        if (type.equals(EDIT_MENU)) {
            console.getActionGroup().updateMenu(menu);
        }
    }

    @Override
    public void updateToolBar(final String type, final MToolBar toolBar) {
        if (type.equals(EDITOR_TOOL_BAR)) {
            console.updateToolBar(toolBar);
        }
    }
}
