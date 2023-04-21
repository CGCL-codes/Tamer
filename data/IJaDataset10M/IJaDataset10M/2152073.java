package com.hs.mail.imap.mailbox;

import java.util.List;
import com.hs.mail.imap.dao.DaoFactory;

/**
 * Convert Unique Identifier (UID) to Message Sequence Number (MSN) and vice
 * versa.
 * 
 * @author Won Chul Doh
 * @since Mar 22, 2010
 * 
 */
public class UidToMsnMapper {

    protected SelectedMailbox selected = null;

    protected List<Long> uids;

    protected boolean useUID = false;

    public UidToMsnMapper(SelectedMailbox selected, List<Long> uids, boolean useUID) {
        this.selected = selected;
        this.uids = uids;
        this.useUID = useUID;
    }

    public UidToMsnMapper(SelectedMailbox selected, boolean useUID) {
        this(selected, selected.getCachedUids(), useUID);
    }

    public long getUID(int msgnum) {
        long uid = selected.getUID(msgnum);
        if (uid == -1) {
            prepare();
            uid = (msgnum <= uids.size()) ? (Long) uids.get(msgnum - 1) : -1;
            if (uid != -1) selected.add(msgnum, uid);
        }
        return uid;
    }

    public int getMessageNumber(long uid) {
        int msgnum = selected.getMessageNumber(uid);
        if (msgnum == -1) {
            prepare();
            if (uid == Long.MAX_VALUE) {
                msgnum = (uids.size() > 0) ? uids.size() : -1;
            } else {
                int i = uids.indexOf(uid);
                msgnum = (i >= 0) ? i + 1 : -1;
                if (msgnum != -1) selected.add(msgnum, uid);
            }
        }
        return msgnum;
    }

    public List<Long> getUIDList() {
        return uids;
    }

    /**
	 * Find the smallest message sequence number whose corresponding unique
	 * identifier is equal or greater than the given unique identifier.
	 * 
	 * @param min
	 *            the key UID to be searched for
	 * @return the smallest message sequence number if found; otherwise -1
	 */
    public long getMinMessageNumber(long min) {
        if (useUID) {
            int msgnum = getMessageNumber(min);
            if (msgnum == -1) {
                if ((msgnum = search(uids, min)) != -1) {
                    return (uids.get(msgnum) >= min) ? msgnum + 1 : msgnum + 2;
                }
            }
            return msgnum;
        } else {
            return min;
        }
    }

    /**
	 * Find the largest message sequence number whose corresponding unique
	 * identifier is equal or less than the given unique identifier.
	 * 
	 * @param max
	 *            the key UID to be searched for
	 * @return the largest message sequence number if found; otherwise -1
	 */
    public long getMaxMessageNumber(long max) {
        if (useUID) {
            int msgnum = getMessageNumber(max);
            if (msgnum == -1) {
                if ((msgnum = search(uids, max)) != -1) {
                    return (uids.get(msgnum) <= max) ? msgnum + 1 : msgnum;
                }
            }
            return msgnum;
        } else {
            return max;
        }
    }

    protected int search(List<Long> v, long t) {
        int low = 0;
        int mid = -1;
        int high = v.size() - 1;
        while (low <= high) {
            mid = (low + high) / 2;
            long c = v.get(mid) - t;
            if (c > 0) high = mid - 1; else if (c < 0) low = mid + 1; else break;
        }
        return mid;
    }

    private void prepare() {
        if (uids == null) uids = DaoFactory.getMessageDao().getMessageIDList(selected.getMailboxID());
    }
}
