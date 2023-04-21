package net.sourceforge.vigilog.ui.filter;

import ca.odell.glazedlists.TextFilterator;
import net.sourceforge.vigilog.model.LogEntry;
import java.util.List;

/**
 * 
 */
class ExcludedLoggersTextFilterator implements TextFilterator<LogEntry> {

    public void getFilterStrings(List<String> baseList, LogEntry element) {
        baseList.add(element.getLogger());
    }
}
