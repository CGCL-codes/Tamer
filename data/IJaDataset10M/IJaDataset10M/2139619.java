package elementmatcher.elements.file;

import elementmatcher.AbstractRegexElementProvider;
import elementmatcher.Element;
import org.apache.commons.collections15.Predicate;
import org.apache.commons.collections15.ResettableIterator;
import org.apache.commons.collections15.iterators.FilterIterator;
import org.apache.commons.collections15.iterators.ObjectArrayIterator;
import org.gjt.sp.jedit.jEdit;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JOptionPane;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.Arrays;
import java.util.Iterator;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileElementProvider extends AbstractRegexElementProvider<File> {

    private static final Pattern PATTERN = Pattern.compile("(\\b[a-zA-Z]:([\\\\/][^\\\\/:\\*\\?<>\\n\\r\"]+)+\\b)" + "|" + "(\\b[a-zA-Z]:([\\\\/][^\\\\/:\\*\\?<>\\n\\r\"]+)+(\\.[a-zA-Z0-9_]+)\\b)");

    private static final char[] SEPARATOR_CHARS = ".,\\/![](){}\"~=;:@#$%^&` ".toCharArray();

    private final OpenInJEditAction openInJEditAction = new OpenInJEditAction();

    private final OpenAction openAction = new OpenAction();

    private final OpenFolderAction openFolderAction = new OpenFolderAction();

    private final Action[] actions = new Action[] { openInJEditAction, openAction, openFolderAction };

    private final ResettableIterator<Action> actionIteratorInternal = new ObjectArrayIterator<Action>(actions);

    private final FilterIterator<Action> actionIterator = new FilterIterator<Action>(null, new EnabledActionPredicate());

    static {
        Arrays.sort(SEPARATOR_CHARS);
    }

    public FileElementProvider() {
        super("FileElementProvider");
    }

    protected Pattern getRegex() {
        return PATTERN;
    }

    protected File getElement(MatchResult match) {
        throw new IllegalStateException();
    }

    @Override
    protected Element<File> getElementInternal(int line, Matcher match) {
        String path = match.group();
        while (!path.isEmpty()) {
            File file = new File(path);
            if (file.exists()) {
                return new Element<File>(this, line, match.start(), match.start() + path.length(), file);
            }
            int lastIndex = path.length() - 2;
            while (lastIndex >= 0 && !isBadPathLetter(path.charAt(lastIndex))) {
                --lastIndex;
            }
            path = path.substring(0, lastIndex);
        }
        return null;
    }

    private boolean isBadPathLetter(char c) {
        return Arrays.binarySearch(SEPARATOR_CHARS, c) >= 0;
    }

    public Iterator<Action> getActions(File data) {
        openInJEditAction.reset(data);
        openAction.reset(data);
        openFolderAction.reset(data);
        actionIteratorInternal.reset();
        actionIterator.setIterator(actionIteratorInternal);
        return actionIterator;
    }

    public String getToolTip(File data) {
        return data.isDirectory() ? "Directory: " + data.getAbsolutePath() : "File: " + data.getAbsolutePath();
    }

    private class OpenInJEditAction extends AbstractAction {

        private File file;

        private void reset(File file) {
            this.file = file;
            setEnabled(file.exists() && !file.isDirectory());
            putValue(NAME, "Open " + file.getAbsolutePath() + " in jEdit");
        }

        public void actionPerformed(ActionEvent evt) {
            jEdit.openFile(jEdit.getActiveView(), file.getAbsolutePath());
        }
    }

    private class OpenAction extends AbstractAction {

        private File file;

        private void reset(File file) {
            this.file = file;
            setEnabled(file.exists());
            putValue(NAME, "Open " + file.getAbsolutePath());
        }

        public void actionPerformed(ActionEvent evt) {
            try {
                Desktop.getDesktop().open(file);
            } catch (Exception e) {
                JOptionPane.showMessageDialog((Component) evt.getSource(), "Cannot open file: " + file.getAbsolutePath(), "ElementMatcher", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private class OpenFolderAction extends AbstractAction {

        private File file;

        private void reset(File file) {
            file = file.getParentFile();
            this.file = file;
            setEnabled(file != null && file.exists());
            if (file != null) {
                putValue(NAME, "Open " + file.getAbsolutePath());
            }
        }

        public void actionPerformed(ActionEvent evt) {
            try {
                Desktop.getDesktop().open(file);
            } catch (Exception e) {
                JOptionPane.showMessageDialog((Component) evt.getSource(), "Cannot open file: " + file.getAbsolutePath(), "ElementMatcher", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private class EnabledActionPredicate implements Predicate<Action> {

        public boolean evaluate(Action object) {
            return object.isEnabled();
        }
    }
}
