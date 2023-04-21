package com.rbnb.api;

import com.rbnb.utility.SortedVector;
import com.rbnb.utility.SortException;
import com.rbnb.utility.ToString;

final class TimeRelativeRequest {

    /**
     * request starts after the reference time.
     * <p>
     *
     * @author Ian Brown
     *
     * @see #AT
     * @see #AT_OR_AFTER
     * @see #AT_OR_BEFORE
     * @see #BEFORE
     * @since V2.2
     * @version 10/10/2003
     */
    public static final byte AFTER = DataRequest.GREATER;

    /**
     * request starts at or after the reference time.
     * <p>
     *
     * @author Ian Brown
     *
     * @see #AFTER
     * @see #AT_OR_BEFORE
     * @see #BEFORE
     * @since V2.2
     * @version 10/10/2003
     */
    public static final byte AT_OR_AFTER = DataRequest.GREATER_EQUAL;

    /**
     * request starts at the reference time.
     * <p>
     *
     * @author Ian Brown
     *
     * @since V2.2
     * @version 11/06/2003
     */
    public static final byte AT = DataRequest.EQUAL;

    /**
     * request ends at or before the reference time.
     * <p>
     *
     * @author Ian Brown
     *
     * @see #AFTER
     * @see #AT_OR_AFTER
     * @see #BEFORE
     * @since V2.2
     * @version 10/10/2003
     */
    public static final byte AT_OR_BEFORE = DataRequest.LESS_EQUAL;

    /**
     * request ends before the reference time.
     * <p>
     *
     * @author Ian Brown
     *
     * @see #AFTER
     * @see #AT_OR_AFTER
     * @see #AT_OR_BEFORE
     * @since V2.2
     * @version 10/10/2003
     */
    public static final byte BEFORE = DataRequest.LESS;

    /**
     * the list of channel references by channel name.
     * <p>
     *
     * @author Ian Brown
     *
     * @since V2.2
     * @version 10/10/2003
     */
    private SortedVector byChannel = new SortedVector(TimeRelativeChannel.SORT_CHANNEL_NAME);

    /**
     * the offset within the channel names to compare to.
     * <p>
     *
     * @author Ian Brown
     *
     * @since V2.2
     * @version 11/05/2003
     */
    private int nameOffset = 0;

    /**
     * relationship to reference.
     * <p>
     *
     * @author Ian Brown
     *
     * @since V2.2
     * @version 10/10/2003
     */
    private byte relationship = AT_OR_BEFORE;

    /**
     * the <code>TimeRange</code> for the request.
     * <p>
     *
     * @author Ian Brown
     *
     * @since V2.2
     * @version 11/04/2003
     */
    private TimeRange timeRange = null;

    public TimeRelativeRequest() {
        super();
    }

    public final void addChannel(TimeRelativeChannel channelI) throws SortException {
        getByChannel().add(channelI);
    }

    public static final TimeRelativeRequest createFromRequest(Rmap requestI) throws com.rbnb.api.AddressException, com.rbnb.api.SerializeException, java.io.EOFException, java.io.InterruptedIOException, java.io.IOException, java.lang.InterruptedException {
        TimeRelativeRequest requestR = null;
        if (requestI instanceof DataRequest) {
            DataRequest request = (DataRequest) requestI;
            requestR = new TimeRelativeRequest();
            requestR.setRelationship(request.getRelationship());
            Rmap child = request.getChildAt(0);
            while ((child.getTrange() == null) && (child.getNchildren() != 0)) {
                child = child.getChildAt(0);
            }
            requestR.setTimeRange(child.getTrange());
            String[] names = request.extractNames();
            TimeRelativeChannel trc;
            for (int idx = 0; idx < names.length; ++idx) {
                trc = new TimeRelativeChannel();
                trc.setChannelName(names[idx]);
                try {
                    requestR.addChannel(trc);
                } catch (com.rbnb.utility.SortException e) {
                    throw new com.rbnb.compat.InternalError();
                }
            }
        }
        return (requestR);
    }

    public final int compareToLimits(DataArray limitsI) {
        int statusR = 2;
        double limitStart = limitsI.getStartTime();
        if (limitStart > -Double.MAX_VALUE) {
            double limitEnd = limitStart + limitsI.getDuration();
            if (getTimeRange().getTime() < limitStart) {
                statusR = -1;
            } else if (getTimeRange().getTime() > limitEnd) {
                statusR = 1;
            } else if (!((TimeRange) limitsI.timeRanges.lastElement()).getInclusive() && (getTimeRange().getTime() == limitEnd)) {
                statusR = 1;
            } else {
                statusR = 0;
            }
        }
        return (statusR);
    }

    public final int compareToTimeRange(TimeRange timeRangeI) {
        int statusR = 0;
        double trStart = timeRangeI.getTime();
        double trEnd = timeRangeI.getPtimes()[timeRangeI.getNptimes() - 1] + timeRangeI.getDuration();
        if (getTimeRange().getTime() < trStart) {
            statusR = -1;
        } else if (getTimeRange().getTime() > trEnd) {
            statusR = 1;
        } else if (!timeRangeI.getInclusive() && (getTimeRange().getTime() == trEnd)) {
            statusR = 1;
        }
        return (statusR);
    }

    public final double determineNextReference(Rmap matchI) throws com.rbnb.api.AddressException, com.rbnb.api.SerializeException, java.io.EOFException, java.io.IOException, java.lang.InterruptedException {
        TimeRelativeChannel trc;
        double referenceR;
        trc = (TimeRelativeChannel) getByChannel().firstElement();
        referenceR = trc.determineNextReference(matchI, this);
        double reference;
        for (int idx = 1; !Double.isNaN(referenceR) && (idx < getByChannel().size()); ++idx) {
            trc = (TimeRelativeChannel) getByChannel().elementAt(idx);
            reference = trc.determineNextReference(matchI, this);
            if (Double.isNaN(reference)) {
                referenceR = Double.NaN;
            } else if (reference != referenceR) {
                referenceR = Double.NaN;
            }
        }
        return (referenceR);
    }

    public final TimeRelativeChannel findByChannel(String channelNameI) throws SortException {
        return ((TimeRelativeChannel) getByChannel().find(channelNameI));
    }

    public final SortedVector getByChannel() {
        return (byChannel);
    }

    public final int getNameOffset() {
        return (nameOffset);
    }

    public final byte getRelationship() {
        return (relationship);
    }

    public final TimeRange getTimeRange() {
        return (timeRange);
    }

    public final void setByChannel(SortedVector byChannelI) {
        byChannel = byChannelI;
    }

    public final void setNameOffset(int nameOffsetI) {
        nameOffset = nameOffsetI;
    }

    public final void setRelationship(byte relationshipI) {
        relationship = relationshipI;
    }

    public final void setTimeRange(TimeRange timeRangeI) {
        timeRange = timeRangeI;
    }

    public final java.util.Vector splitByNameLevel() {
        java.util.Vector splitR = new java.util.Vector();
        TimeRelativeRequest trr = new TimeRelativeRequest();
        TimeRelativeChannel trc;
        String part = null;
        String cPart;
        trr.setNameOffset(getNameOffset());
        trr.setRelationship(getRelationship());
        trr.setTimeRange(getTimeRange());
        for (int idx = 0; idx < getByChannel().size(); ++idx) {
            trc = (TimeRelativeChannel) getByChannel().elementAt(idx);
            cPart = trc.nextNameLevel(getNameOffset());
            if (cPart != null) {
                if ((part != null) && cPart.equals(part)) {
                    try {
                        trr.addChannel(trc);
                    } catch (com.rbnb.utility.SortException e) {
                        throw new com.rbnb.compat.InternalError();
                    }
                } else {
                    if (part != null) {
                        splitR.addElement(trr);
                        trr = new TimeRelativeRequest();
                        trr.setNameOffset(getNameOffset());
                        trr.setRelationship(getRelationship());
                        trr.setTimeRange(getTimeRange());
                    }
                    try {
                        trr.addChannel(trc);
                    } catch (com.rbnb.utility.SortException e) {
                        throw new com.rbnb.compat.InternalError();
                    }
                    part = cPart;
                }
            }
        }
        if (trr.getByChannel().size() > 0) {
            splitR.addElement(trr);
        }
        return (splitR);
    }

    public final String toString() {
        String stringR = "TimeRelativeRequest: " + getRelationship() + " " + getTimeRange();
        TimeRelativeChannel trc;
        for (int idx = 0; idx < getByChannel().size(); ++idx) {
            trc = (TimeRelativeChannel) getByChannel().elementAt(idx);
            stringR += "\n  " + trc;
        }
        return (stringR);
    }
}
