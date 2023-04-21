package games.strategy.engine.history;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright:
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author not attributable
 * @version 1.0
 */
public abstract class IndexedHistoryNode extends HistoryNode {

    private final int m_changeStartIndex;

    private int m_changeStopIndex = -1;

    public IndexedHistoryNode(final String value, final int changeStartIndex, final boolean allowsChildren) {
        super(value, true);
        m_changeStartIndex = changeStartIndex;
    }

    int getChangeStartIndex() {
        return m_changeStartIndex;
    }

    int getChangeEndIndex() {
        return m_changeStopIndex;
    }

    void setChangeEndIndex(final int index) {
        m_changeStopIndex = index;
    }
}
