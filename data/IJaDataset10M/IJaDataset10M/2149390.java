package org.makagiga.plugins.rtfviewer;

import java.awt.Window;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import javax.swing.text.StyleConstants;
import javax.swing.text.rtf.RTFEditorKit;
import org.makagiga.commons.FS;
import org.makagiga.commons.MAction;
import org.makagiga.commons.TK;
import org.makagiga.commons.UI;
import org.makagiga.commons.WTFError;
import org.makagiga.commons.swing.MMenu;
import org.makagiga.commons.swing.MText;
import org.makagiga.commons.swing.MToolBar;
import org.makagiga.editors.AbstractTextEditor;
import org.makagiga.editors.EditorExport;
import org.makagiga.editors.EditorIO;
import org.makagiga.editors.EditorPlugin;
import org.makagiga.editors.EditorZoom;
import org.makagiga.editors.TextUtils;

public final class Main extends AbstractTextEditor<Viewer> implements EditorExport, EditorIO {

    private int currentFontSize = Integer.MIN_VALUE;

    /**
	 * Constructs a new editor instance.
	 */
    Main() {
        super(new Viewer());
    }

    @Override
    public MToolBar.TextPosition getPreferredToolBarTextPosition() {
        return MToolBar.TextPosition.ALONGSIDE_ICONS;
    }

    @Override
    public void setLocked(final boolean value) {
        super.setLocked(value);
        core.setEditable(false);
    }

    @Override
    public Object configureExport(final Window owner, final EditorPlugin.FileType type) throws Exception {
        return null;
    }

    @Override
    public ExportResult exportFile(final EditorPlugin.FileType type, final File outputFile, final OutputStream output, final Object configuration) throws Exception {
        if (type.is("rtf")) copyTo(output); else if (type.is("txt")) FS.write(output, MText.getPlainText(core)); else return ExportResult.NOT_SUPPORTED;
        return ExportResult.OK;
    }

    @Override
    public void loadFile(final InputStream input, final boolean newFile) throws Exception {
        core.setContentType("text/rtf");
        MText.load(core, input);
        updateIndex(MText.getPlainText(core));
    }

    @Override
    public void saveFile(final OutputStream output) throws Exception {
    }

    @Override
    public boolean isZoomEnabled(final EditorZoom.ZoomType type) {
        if (currentFontSize == Integer.MIN_VALUE) return true;
        switch(type) {
            case IN:
                return currentFontSize < TextUtils.MAX_ZOOM;
            case OUT:
                return currentFontSize > TextUtils.MIN_ZOOM;
            default:
                throw new WTFError(type);
        }
    }

    @Override
    public void resetZoom() {
        currentFontSize = UI.getDefaultFontSize();
        updateFontSize();
    }

    @Override
    public void zoom(final EditorZoom.ZoomType type) {
        if (currentFontSize == Integer.MIN_VALUE) {
            currentFontSize = StyleConstants.getFontSize(core.getCharacterAttributes());
            currentFontSize = TK.limit(currentFontSize, TextUtils.MIN_ZOOM, TextUtils.MAX_ZOOM);
        }
        switch(type) {
            case IN:
                currentFontSize += 3;
                break;
            case OUT:
                currentFontSize -= 3;
                break;
            default:
                throw new WTFError(type);
        }
        updateFontSize();
    }

    @Override
    public void updateMenu(final String type, final MMenu menu) {
        if (type.equals(EDIT_MENU)) {
            menu.add(MText.getAction(core, MText.COPY));
        }
    }

    @Override
    public void updateToolBar(final String type, final MToolBar toolBar) {
        if (type.equals(EDITOR_TOOL_BAR)) {
            toolBar.add(MText.getAction(core, MText.COPY), MToolBar.SHOW_TEXT);
        }
    }

    private void updateFontSize() {
        core.selectAll();
        RTFEditorKit.FontSizeAction a = new RTFEditorKit.FontSizeAction(Integer.toString(currentFontSize), currentFontSize);
        MAction.fire(a, core);
        core.setSelectionStart(0);
        core.setSelectionEnd(0);
    }
}
