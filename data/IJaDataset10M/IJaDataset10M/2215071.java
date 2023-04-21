package net.sf.gham.core.entity.match;

import net.sf.gham.core.control.ObjectShower;
import net.sf.gham.swing.table.ClickableCell;
import net.sf.gham.swing.util.GlassPaneSwingWorker;

/**
 * @author fabio
 *
 */
public class ClickableMatchResult implements ClickableCell {

    private final IMatch match;

    private final String result;

    public ClickableMatchResult(IMatch match, String result) {
        this.match = match;
        this.result = result;
    }

    public void mouseClicked() {
        new GlassPaneSwingWorker<Integer, Integer>() {

            @Override
            protected Integer executeInBackground() throws Exception {
                int matchID = match.getMatchID();
                ObjectShower.singleton().showMatch(matchID);
                return matchID;
            }
        }.execute();
    }

    public String getResult() {
        return result;
    }

    public boolean isClickable() {
        return !match.isYouth();
    }

    @Override
    public String toString() {
        return result;
    }
}
