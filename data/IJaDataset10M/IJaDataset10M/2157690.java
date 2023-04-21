package jmodnews.modules.gui.mainpanes.navigate;

import jmodnews.modules.gui.mainpanes.HeaderTableModel;
import jmodnews.modules.gui.mainpanes.ThreadedOverview;

/**
 * 
 * @author Michael Schierl <schierlm@gmx.de>
 */
public class MessageIDNavigator extends RawArticleNavigator {

    private final String mid;

    public MessageIDNavigator(String mid) {
        this.mid = mid;
    }

    public int navigateToRawArticle(HeaderTableModel htm, int rawCurrent) {
        for (int i = 0; i < htm.getRawRowCount(); i++) {
            ThreadedOverview tov = htm.getRawRow(i);
            if (mid.equals(tov.getOverview().getHeader("Message-ID"))) {
                return i;
            }
        }
        return -1;
    }
}
