package org.makagiga.editors;

import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.lang.ref.WeakReference;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Document;
import javax.swing.text.Highlighter;
import javax.swing.text.JTextComponent;
import org.makagiga.commons.Flags;
import org.makagiga.commons.MColor;
import org.makagiga.commons.MLogger;
import org.makagiga.commons.TK;
import org.makagiga.commons.UI;
import org.makagiga.commons.annotation.Uninstantiable;
import org.makagiga.commons.swing.MCellTip;
import org.makagiga.commons.swing.MHighlighter;
import org.makagiga.commons.swing.MScrollPane;
import org.makagiga.commons.swing.event.MMouseAdapter;

public final class TextUtils {

    public static final int MIN_ZOOM = 8;

    /**
	 * @since 3.4
	 */
    public static final int MAX_ZOOM = 128;

    private static final String HIGHLIGHTER_HANDLER = "org.makagiga.editors.TextUtils.highlighterHandler";

    private static final String OLD_HIGHLIGHTER = "org.makagiga.editors.TextUtils.oldHighlighter";

    public static boolean canZoom(final JComponent component, final EditorZoom.ZoomType type) {
        if (type == EditorZoom.ZoomType.IN) return component.getFont().getSize() < MAX_ZOOM;
        if (type == EditorZoom.ZoomType.OUT) return component.getFont().getSize() > MIN_ZOOM;
        return true;
    }

    /**
	 * @since 3.6
	 */
    public static EditorSearch.SearchResult findText(final JTextComponent textComponent, final String text, final Flags flags) {
        return searchFor(textComponent, text, 0, flags);
    }

    public static EditorSearch.SearchResult findNextText(final JTextComponent textComponent, final String text, final Flags flags) {
        EditorSearch.SearchResult result1 = searchFor(textComponent, text, textComponent.getSelectionStart() + 1, flags);
        if (result1 == EditorSearch.SearchResult.WRAP_AROUND) {
            EditorSearch.SearchResult result2 = findText(textComponent, text, flags);
            return (result2 == EditorSearch.SearchResult.NOT_FOUND) ? EditorSearch.SearchResult.NOT_FOUND : EditorSearch.SearchResult.WRAP_AROUND;
        }
        return result1;
    }

    /**
	 * @since 4.0
	 */
    public static Font resetZoom(final JComponent component, final int fontSize) {
        int newFontSize = (fontSize == -1) ? UI.getDefaultFontSize() : fontSize;
        return setFont(component, newFontSize);
    }

    /**
	 * @since 3.4
	 */
    public static void uninstallSearchHighlighter(final JTextComponent textComponent) {
        StaticHighlighterHandler highlighterHandler = UI.getClientProperty(textComponent, HIGHLIGHTER_HANDLER, null);
        if (highlighterHandler != null) {
            textComponent.getDocument().removeDocumentListener(highlighterHandler);
            textComponent.removeMouseListener(highlighterHandler);
            textComponent.putClientProperty(HIGHLIGHTER_HANDLER, null);
            Highlighter oldHighlighter = UI.getClientProperty(textComponent, OLD_HIGHLIGHTER, null);
            if (oldHighlighter == null) oldHighlighter = new DefaultHighlighter();
            textComponent.setHighlighter(oldHighlighter);
            textComponent.putClientProperty(OLD_HIGHLIGHTER, null);
        }
    }

    public static Font zoom(final JComponent component, final EditorZoom.ZoomType type) {
        Font oldFont = UI.getFont(component);
        int newFontSize = oldFont.getSize() + ((type == EditorZoom.ZoomType.IN) ? 1 : -1);
        return setFont(component, newFontSize);
    }

    @Uninstantiable
    private TextUtils() {
    }

    private static StaticHighlighterHandler installSearchHighlighter(final JTextComponent textComponent, final String textToFind, final Flags flags) {
        StaticHighlighterHandler highlighterHandler = UI.getClientProperty(textComponent, HIGHLIGHTER_HANDLER, null);
        if (highlighterHandler == null) {
            highlighterHandler = new StaticHighlighterHandler(textComponent);
            textComponent.getDocument().addDocumentListener(highlighterHandler);
            textComponent.addMouseListener(highlighterHandler);
            textComponent.putClientProperty(HIGHLIGHTER_HANDLER, highlighterHandler);
            Highlighter oldHighlighter = textComponent.getHighlighter();
            textComponent.putClientProperty(OLD_HIGHLIGHTER, oldHighlighter);
            MHighlighter.install(textComponent, highlighterHandler).setDisableIfSelection(false);
        }
        highlighterHandler.update(textComponent, textToFind, flags);
        return highlighterHandler;
    }

    private static EditorSearch.SearchResult searchFor(final JTextComponent textComponent, final String text, final int fromIndex, final Flags flags) {
        Document doc = textComponent.getDocument();
        int length = doc.getLength();
        String allText;
        try {
            allText = doc.getText(0, length);
        } catch (BadLocationException exception) {
            MLogger.exception(exception);
            uninstallSearchHighlighter(textComponent);
            return EditorSearch.SearchResult.NOT_FOUND;
        }
        if (TK.isEmpty(allText)) {
            uninstallSearchHighlighter(textComponent);
            return EditorSearch.SearchResult.NOT_FOUND;
        }
        if (fromIndex > length - 1) return EditorSearch.SearchResult.WRAP_AROUND;
        String textToFind;
        if (flags.isSet(EditorSearch.CASE_SENSITIVE)) {
            textToFind = text;
        } else {
            allText = allText.toUpperCase();
            textToFind = text.toUpperCase();
        }
        int foundAt = allText.indexOf(textToFind, fromIndex);
        if (foundAt != -1) {
            textComponent.setCaretPosition(foundAt);
            textComponent.setSelectionStart(foundAt);
            textComponent.setSelectionEnd(foundAt + textToFind.length());
            installSearchHighlighter(textComponent, textToFind, flags);
            return EditorSearch.SearchResult.FOUND;
        }
        int selectionStart = textComponent.getSelectionStart();
        if (allText.indexOf(textToFind, selectionStart) == selectionStart) return EditorSearch.SearchResult.WRAP_AROUND;
        uninstallSearchHighlighter(textComponent);
        return (fromIndex == 0) ? EditorSearch.SearchResult.NOT_FOUND : EditorSearch.SearchResult.WRAP_AROUND;
    }

    private static Font setFont(final JComponent component, final int fontSize) {
        if (component instanceof JTable) MCellTip.getInstance().setVisible(false);
        int textModelPos = -1;
        JViewport textViewport = null;
        if (component instanceof JTextComponent) {
            JTextComponent text = (JTextComponent) component;
            textViewport = MScrollPane.getViewport(text);
            if (textViewport != null) textModelPos = text.viewToModel(textViewport.getViewPosition());
        }
        int newFontSize = TK.limit(fontSize, MIN_ZOOM, MAX_ZOOM);
        Font oldFont = UI.getFont(component);
        Font newFont = UI.deriveFontSize(oldFont, newFontSize);
        component.setFont(newFont);
        if (textModelPos != -1) {
            JTextComponent text = (JTextComponent) component;
            try {
                Rectangle r = text.modelToView(textModelPos);
                if (r != null) {
                    r.height = text.getVisibleRect().height;
                    text.scrollRectToVisible(r);
                }
            } catch (BadLocationException exception) {
                MLogger.exception(exception);
            }
        }
        return newFont;
    }

    private static final class StaticHighlighterHandler extends MMouseAdapter implements DocumentListener, MHighlighter.UserHighlight<JTextComponent> {

        private Flags flags = Flags.NONE;

        private String textToFind;

        private final WeakReference<JTextComponent> textComponentRef;

        public void changedUpdate(final DocumentEvent e) {
            onChange();
        }

        public void insertUpdate(final DocumentEvent e) {
            onChange();
        }

        public void removeUpdate(final DocumentEvent e) {
            onChange();
        }

        public void highlight(final MHighlighter<JTextComponent> h, final JTextComponent textComponent, final String text) {
            String allText = text;
            if (flags.isClear(EditorSearch.CASE_SENSITIVE)) allText = allText.toUpperCase();
            int selectionStart = textComponent.getSelectionStart();
            int selectionEnd = textComponent.getSelectionEnd();
            int foundAt;
            int fromIndex = 0;
            while ((foundAt = allText.indexOf(textToFind, fromIndex)) != -1) {
                Color color = ((foundAt >= selectionStart) && (foundAt <= selectionEnd)) ? MColor.SKY_BLUE : MHighlighter.SEARCH_COLOR;
                h.addHighlight(foundAt, foundAt + textToFind.length(), color, true);
                fromIndex = foundAt + 1;
            }
        }

        @Override
        public void mouseClicked(final MouseEvent e) {
            JTextComponent textComponent = textComponentRef.get();
            if (textComponent != null) {
                TextUtils.uninstallSearchHighlighter(textComponent);
                textComponent.repaint();
            }
        }

        private StaticHighlighterHandler(final JTextComponent textComponent) {
            textComponentRef = new WeakReference<JTextComponent>(textComponent);
        }

        private void onChange() {
            JTextComponent textComponent = textComponentRef.get();
            if (textComponent != null) TextUtils.uninstallSearchHighlighter(textComponent);
        }

        private void update(final JTextComponent textComponent, final String textToFind, final Flags flags) {
            this.textToFind = textToFind;
            this.flags = flags;
            MHighlighter.update(textComponent, this);
        }
    }
}
