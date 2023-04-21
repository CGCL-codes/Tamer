package ctagsinterface.jedit;

import java.awt.Component;
import java.awt.Point;
import java.util.Vector;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
import javax.swing.SwingUtilities;
import org.gjt.sp.jedit.Buffer;
import org.gjt.sp.jedit.View;
import org.gjt.sp.jedit.gui.CompletionPopup.Candidates;
import org.gjt.sp.jedit.textarea.JEditTextArea;
import org.gjt.sp.jedit.textarea.TextAreaPainter;
import superabbrevs.SuperAbbrevs;
import ctagsinterface.db.Query;
import ctagsinterface.db.TagDB;
import ctagsinterface.main.CtagsInterfacePlugin;
import ctagsinterface.main.KindIconProvider;
import ctagsinterface.main.Tag;

public class TagCompletion {

    private View view;

    private String prefix;

    public static void complete(View view, String prefix) {
        final TagCompletion completion = new TagCompletion(view, prefix);
        final Vector<Tag> tags = completion.getCompletions();
        if (tags == null || tags.isEmpty()) return;
        if (tags.size() > 1) {
            JEditTextArea ta = view.getTextArea();
            int caret = ta.getCaretPosition();
            Point location = ta.offsetToXY(caret - prefix.length());
            TextAreaPainter painter = ta.getPainter();
            location.y += painter.getFontMetrics().getHeight();
            SwingUtilities.convertPointToScreen(location, painter);
            TagCompletionPopup popup = new TagCompletionPopup(view, location);
            TagCandidates candidates = completion.new TagCandidates(tags, popup);
            popup.reset(candidates, true);
        } else completion.complete(tags.get(0));
    }

    class TagCandidates implements Candidates {

        private Vector<Tag> tags;

        private DefaultListCellRenderer renderer;

        private TagCompletionPopup popup;

        @SuppressWarnings("serial")
        public TagCandidates(final Vector<Tag> tags, TagCompletionPopup popup) {
            this.tags = tags;
            this.popup = popup;
            renderer = new DefaultListCellRenderer() {

                @Override
                public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                    Tag tag = tags.get(index);
                    if (isSelected) TagCandidates.this.popup.setSelectedTag(tag);
                    super.getListCellRendererComponent(list, null, index, isSelected, cellHasFocus);
                    String kind = tag.getKind();
                    if (kind == null) kind = "";
                    setIcon(KindIconProvider.getIcon(kind));
                    String prefix = (index <= 9) ? index + ": " : "";
                    setText(prefix + getCompletionString(tag));
                    return this;
                }
            };
        }

        public int indexForKey(char ch) {
            if (Character.isDigit(ch)) return ch - '0';
            return (-1);
        }

        public void complete(int index) {
            TagCompletion.this.complete(tags.get(index));
        }

        public Component getCellRenderer(JList list, int index, boolean isSelected, boolean cellHasFocus) {
            return renderer.getListCellRendererComponent(list, null, index, isSelected, cellHasFocus);
        }

        public String getDescription(int index) {
            return null;
        }

        public int getSize() {
            return tags.size();
        }

        public boolean isValid() {
            return true;
        }
    }

    public TagCompletion(View view, String prefix) {
        this.view = view;
        this.prefix = prefix;
    }

    public String getCompletionString(Tag tag) {
        StringBuffer sb = new StringBuffer();
        sb.append(tag.getName());
        String signature = tag.getExtension("signature");
        if (signature != null && signature.length() > 0) sb.append(signature);
        String namespace = tag.getNamespace();
        if (namespace != null && namespace.length() > 0) sb.append(" - " + namespace);
        return sb.toString();
    }

    public Vector<Tag> getCompletions() {
        Query q = CtagsInterfacePlugin.getBasicScopedTagQuery(view);
        TagDB db = CtagsInterfacePlugin.getDB();
        q.addCondition(db.field(TagDB.TAGS_TABLE, TagDB.TAGS_NAME) + " LIKE " + TagDB.quote(prefix + '%'));
        Vector<Tag> tags = CtagsInterfacePlugin.query(q);
        return tags;
    }

    public String createAbbrev(String signature) {
        StringBuffer sb = new StringBuffer();
        boolean startParam = false;
        for (int i = 0; i < signature.length(); i++) {
            char c = signature.charAt(i);
            switch(c) {
                case ',':
                    sb.append("}");
                case '(':
                    startParam = true;
                    sb.append(c);
                    break;
                case ')':
                    if (!startParam) sb.append("}");
                    sb.append(c);
                    break;
                case ' ':
                case '\t':
                case '\n':
                    sb.append(c);
                    break;
                default:
                    if (startParam) {
                        startParam = false;
                        sb.append("${");
                        sb.append(i + 1);
                        sb.append(":");
                    }
                    sb.append(c);
                    break;
            }
        }
        return sb.toString();
    }

    public void complete(Tag tag) {
        Buffer b = view.getBuffer();
        String s = tag.getName();
        b.insert(view.getTextArea().getCaretPosition(), s.substring(prefix.length()));
        String sig = tag.getExtension("signature");
        if (sig == null || sig.length() == 0) return;
        String abbrev = createAbbrev(sig);
        SuperAbbrevs.expandAbbrev(view, abbrev, null);
    }
}
