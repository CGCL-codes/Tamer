package tsqldeveloper.gui.custom;

import tsqldeveloper.Const;
import java.util.Map;
import java.util.Collection;
import java.util.HashMap;

public class RecentQueriesPopup extends CompletePopup {

    protected Map<String, String> completionValues = new HashMap<String, String>();

    public RecentQueriesPopup(Collection<String> completionWords, int visibleRowsCount, int width, int height) {
        super(completionWords, Const.COMPLETION_RECENT_QUERIES_CAPTION, visibleRowsCount, width, height);
    }

    public boolean changesCase() {
        return false;
    }

    @Override
    public String getSelectedWord() {
        return completionValues.get(super.getSelectedWord());
    }

    public void addCompletionValue(String value) {
        String key = value;
        if (key.length() > 30) {
            key = value.substring(0, Const.COMPLETION_MAX_VISIBLE_CHARS);
        }
        completionWords.add(key);
        completionValues.put(key, value);
    }
}
