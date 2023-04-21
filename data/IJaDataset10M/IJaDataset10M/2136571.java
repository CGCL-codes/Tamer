package org.xblackcat.rojac.data;

/**
 * 27.03.12 16:21
 *
 * @author xBlackCat
 */
public class ItemStatisticData<T> {

    private final T messageData;

    private final DiscussionStatistic readStatistic;

    public ItemStatisticData(T messageData, DiscussionStatistic readStatistic) {
        this.messageData = messageData;
        this.readStatistic = readStatistic;
    }

    public T getItem() {
        return messageData;
    }

    public DiscussionStatistic getItemReadStatistic() {
        return readStatistic;
    }
}
