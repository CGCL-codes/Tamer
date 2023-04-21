package sn;

import java.util.Vector;
import javax.swing.JOptionPane;
import org.gjt.sp.jedit.EditAction;
import org.gjt.sp.jedit.View;
import org.gjt.sp.jedit.jEdit;

public abstract class LookupAction extends EditAction {

    private static final String ACTION_PREFIX = "source-navigator-";

    protected DbDescriptor desc;

    private String prefix;

    public LookupAction(String prefix, String label, DbDescriptor desc) {
        super(ACTION_PREFIX + prefix + "-" + desc.name);
        this.prefix = prefix;
        jEdit.setTemporaryProperty(getName() + ".label", label + desc.label);
        this.desc = desc;
    }

    protected abstract String getTextForAction(View view);

    protected abstract boolean isPrefixLookup();

    protected void noTags(View view, String text) {
        JOptionPane.showMessageDialog(view, SourceNavigatorPlugin.getMessage("no-tags-found"));
    }

    protected abstract void singleTag(View view, String text, DbRecord record);

    protected abstract void multipleTags(View view, String text, Vector<DbRecord> records);

    @Override
    public void invoke(View view) {
        String text = getTextForAction(view);
        if (text == null || text.isEmpty()) {
            String msg = SourceNavigatorPlugin.getMessage("no-tag-for-" + prefix);
            if (msg == null) msg = "No tag selected or identified at the caret location.";
            JOptionPane.showMessageDialog(view, msg);
            return;
        }
        Vector<DbRecord> tags;
        tags = DbAccess.lookup(desc, text, isPrefixLookup());
        if (tags == null || tags.isEmpty()) {
            noTags(view, text);
            return;
        }
        if (tags.size() == 1) {
            singleTag(view, text, tags.get(0));
            return;
        }
        multipleTags(view, text, tags);
    }
}
