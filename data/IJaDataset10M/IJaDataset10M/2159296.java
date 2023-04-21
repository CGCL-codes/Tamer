package net.community.chest.net.proto.text.imap4;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import net.community.chest.CoVariantReturn;
import net.community.chest.ParsableString;
import net.community.chest.net.TextNetConnection;

/**
 * <P>Copyright 2008 as per GPLv2</P>
 *
 * @author Lyor G.
 * @since Mar 27, 2008 11:22:11 AM
 */
public class IMAP4SearchRspHandler extends AbstractIMAP4UntaggedResponseHandlerHelper {

    private final IMAP4SearchResponse _srchRes;

    private Collection<Long> _results;

    private final int _maxResults;

    @Override
    @CoVariantReturn
    protected IMAP4SearchResponse getResponse() {
        return _srchRes;
    }

    public IMAP4SearchRspHandler(final TextNetConnection conn, final boolean isUID, final int maxResults) {
        super(conn);
        _srchRes = new IMAP4SearchResponse();
        _srchRes.setUID(isUID);
        _maxResults = maxResults;
    }

    /**
	 * Updates the accumulated results into the response object
	 * @param res response object to be updated (Note: msg IDs array should NOT be allocated)
	 * @return 0 if successful
	 */
    protected int updateResults(final IMAP4SearchResponse res) {
        final int resNum = (null == _results) ? 0 : _results.size();
        if (resNum <= 0) return 0;
        long[] msgIds = res.getMsgIds();
        if (msgIds != null) return (-1011);
        msgIds = new long[resNum];
        final Iterator<Long> iter = _results.iterator();
        for (int resIndex = 0; (iter != null) && iter.hasNext(); ) {
            final Long idl = iter.next();
            if ((idl != null) && (resIndex < resNum)) {
                msgIds[resIndex] = idl.longValue();
                resIndex++;
            }
        }
        res.setMsgIds(msgIds);
        return 0;
    }

    @Override
    public int handleUntaggedResponse(final ParsableString ps, final int startPos) throws IOException {
        final int curPos = ps.findNonEmptyDataStart(startPos), maxIndex = ps.getMaxIndex();
        if ((curPos < startPos) || (startPos >= maxIndex)) return 0;
        final int nextPos = ps.findNonEmptyDataEnd(curPos + 1);
        if ((nextPos <= curPos) || (nextPos > maxIndex)) return 0;
        if (!ps.compareTo(curPos, nextPos, IMAP4Protocol.IMAP4SearchCmdChars, true)) return 0;
        for (int curResults = 0, numPos = nextPos; curResults < Short.MAX_VALUE; curResults++) {
            if (haveCRLF()) {
                if ((numPos = _psHelper.findNonEmptyDataStart(numPos)) < _psHelper.getStartIndex()) return 0;
            }
            if ((_maxResults > 0) && (curResults >= _maxResults)) return 0;
            NumInfo numVal = extractSimpleNumber(numPos);
            if (null == numVal) return (-1012);
            if (numVal.num <= 0) return (-1013);
            if (null == _results) _results = new LinkedList<Long>();
            _results.add(Long.valueOf(numVal.num));
            numPos = numVal.startPos;
        }
        return (-1014);
    }
}
