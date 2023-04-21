package com.l2jserver.gameserver.model;

/**
 ** @author Gnacik
 */
public class L2PremiumItem {

    private int _itemId;

    private long _count;

    private String _sender;

    public L2PremiumItem(int itemid, long count, String sender) {
        _itemId = itemid;
        _count = count;
        _sender = sender;
    }

    public void updateCount(long newcount) {
        _count = newcount;
    }

    public int getItemId() {
        return _itemId;
    }

    public long getCount() {
        return _count;
    }

    public String getSender() {
        return _sender;
    }
}
