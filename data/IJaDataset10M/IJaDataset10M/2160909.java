package ch.ethz.mxquery.sms.MMimpl;

import ch.ethz.mxquery.datamodel.xdm.TokenInterface;
import ch.ethz.mxquery.exceptions.MXQueryException;

public final class BufferItem {

    private BufferItem next = null;

    private SyncAppendTokenBuffer content = null;

    private int firstTokenId = 0;

    private int lastTokenId = 0;

    private int firstItemId = 0;

    private int lastItemId = 0;

    private boolean isEmpty;

    public BufferItem(int firstItem, int firstToken, int gran) {
        content = new SyncAppendTokenBuffer(gran);
        this.firstItemId = firstItem;
        this.firstTokenId = firstToken;
        this.lastItemId = firstItem;
        this.lastTokenId = firstToken;
        this.isEmpty = true;
    }

    public int getNodeIdFromTokenId(int lastKnownNodeId, int activeTokenId) {
        return content.getNodeIdFromTokenId(lastKnownNodeId - firstItemId, activeTokenId - firstTokenId) + firstItemId;
    }

    public int getTokenIdForNode(int nodeId) throws MXQueryException {
        return content.getTokenIdForNode(nodeId - firstItemId) + firstTokenId;
    }

    public TokenInterface get(int tokenId, int endNode) {
        return content.get(tokenId - firstTokenId, endNode - firstItemId);
    }

    public TokenInterface get(int tokenId) {
        return content.get(tokenId - firstTokenId);
    }

    public boolean hasNode(int nodeId) {
        return content.hasNode(nodeId - firstItemId);
    }

    public boolean isEmpty() {
        return isEmpty;
    }

    public void setNext(BufferItem ib) {
        next = ib;
    }

    public BufferItem getNext() {
        return next;
    }

    public int getLastNodeId() {
        return lastItemId;
    }

    public void setLastNodeId(int node) {
        lastItemId = node;
    }

    public void setFirstNodeId(int first) {
        firstItemId = first;
    }

    public int getLastTokenId() {
        return lastTokenId;
    }

    public void setLastTokenId(int token) {
        lastTokenId = token;
    }

    public void setFirstTokenId(int first) {
        firstTokenId = first;
    }

    public int getFirstTokenId() {
        return firstTokenId;
    }

    public int getFirstItemId() {
        return firstItemId;
    }

    public void clear() {
        isEmpty = true;
        firstItemId = 0;
        firstTokenId = 0;
        lastItemId = 0;
        lastTokenId = 0;
        content.clear();
    }

    public void indexNewNode() {
        content.indexNewNode();
        lastItemId++;
        isEmpty = false;
    }

    public void bufferToken(TokenInterface tok, boolean isEndOfItem) {
        content.bufferToken(tok, isEndOfItem);
        lastTokenId++;
        isEmpty = false;
    }

    public boolean isEndOfStream() {
        return content.isEndOfStream();
    }
}
