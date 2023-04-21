package org.cojen.tupl;

/**
 * Short-lived object for capturing the state of a partially completed node split.
 *
 * @author Brian S O'Neill
 */
class Split {

    final boolean mSplitRight;

    private final long mSiblingId;

    private final Node mSibling;

    private final byte[] mSplitKey;

    /**
     * @param sibling must have exclusive lock when called; is released as a side-effect
     */
    Split(boolean splitRight, Node sibling, byte[] splitKey) {
        mSplitRight = splitRight;
        mSiblingId = sibling.mId;
        mSibling = sibling;
        mSplitKey = splitKey;
        sibling.releaseExclusive();
    }

    /**
     * Compares to the split key, returning <0 if given key is lower, 0 if
     * equal, >0 if greater.
     */
    int compare(byte[] key) {
        return Utils.compareKeys(key, 0, key.length, mSplitKey, 0, mSplitKey.length);
    }

    /**
     * Allows a search to continue into a split node by selecting the original node or the
     * sibling. If the original node is returned, its shared lock is still held. If the
     * sibling is returned, it will have a shared latch held and the original node's latch
     * is released.
     *
     * @param node node which was split; shared latch must be held
     * @return original node or sibling
     */
    Node selectNodeShared(Database db, Node node, byte[] key) {
        Node sibling = mSibling;
        sibling.acquireShared();
        Node left, right;
        if (mSplitRight) {
            left = node;
            right = sibling;
        } else {
            left = sibling;
            right = node;
        }
        if (compare(key) < 0) {
            right.releaseShared();
            return left;
        } else {
            left.releaseShared();
            return right;
        }
    }

    /**
     * Allows a search/insert/update to continue into a split node by selecting the
     * original node or the sibling. If the original node is returned, its exclusive lock
     * is still held. If the sibling is returned, it will have an exclusive latch held and
     * the original node's latch is released.
     *
     * @param node node which was split; exclusive latch must be held
     * @return original node or sibling
     */
    Node selectNodeExclusive(Database db, Node node, byte[] key) {
        Node sibling = latchSibling(db);
        Node left, right;
        if (mSplitRight) {
            left = node;
            right = sibling;
        } else {
            left = sibling;
            right = node;
        }
        if (compare(key) < 0) {
            right.releaseExclusive();
            return left;
        } else {
            left.releaseExclusive();
            return right;
        }
    }

    /**
     * Performs a binary search against the split, returning the position
     * within the original node as if it had not split.
     */
    int binarySearch(Database db, Node node, byte[] key) {
        Node sibling = latchSibling(db);
        Node left, right;
        if (mSplitRight) {
            left = node;
            right = sibling;
        } else {
            left = sibling;
            right = node;
        }
        int searchPos;
        if (compare(key) < 0) {
            searchPos = left.binarySearch(key);
        } else {
            int highestPos = left.highestLeafPos();
            searchPos = right.binarySearch(key);
            if (searchPos < 0) {
                searchPos = searchPos - highestPos - 2;
            } else {
                searchPos = highestPos + 2 + searchPos;
            }
        }
        sibling.releaseExclusive();
        return searchPos;
    }

    /**
     * Returns the highest position within the original node as if it had not split.
     */
    int highestLeafPos(Database db, Node node) {
        Node sibling = latchSibling(db);
        int pos = node.highestLeafPos() + sibling.highestLeafPos() + 2;
        sibling.releaseExclusive();
        return pos;
    }

    /**
     * Return the left split node, latched exclusively. Other node is unlatched.
     */
    Node latchLeft(Database db, Node node) {
        if (mSplitRight) {
            return node;
        }
        Node sibling = latchSibling(db);
        node.releaseExclusive();
        return sibling;
    }

    /**
     * @return sibling with exclusive latch held
     */
    Node latchSibling(Database db) {
        Node sibling = mSibling;
        sibling.acquireExclusive();
        return sibling;
    }

    /**
     * @param frame frame affected by split; exclusive latch for sibling must also be held
     */
    void rebindFrame(TreeCursorFrame frame, Node sibling) {
        Node node = frame.mNode;
        int pos = frame.mNodePos;
        if (mSplitRight) {
            int highestPos = node.highestPos();
            if (pos >= 0) {
                if (pos <= highestPos) {
                } else {
                    frame.unbind();
                    frame.bind(sibling, pos - highestPos - 2);
                }
                return;
            }
            pos = ~pos;
            if (pos <= highestPos) {
                return;
            }
            if (pos == highestPos + 2) {
                byte[] key = frame.mNotFoundKey;
                if (compare(key) < 0) {
                    return;
                }
            }
            frame.unbind();
            frame.bind(sibling, ~(pos - highestPos - 2));
        } else {
            int highestPos = sibling.highestPos();
            if (pos >= 0) {
                if (pos <= highestPos) {
                    frame.unbind();
                    frame.bind(sibling, pos);
                } else {
                    frame.mNodePos = pos - highestPos - 2;
                }
                return;
            }
            pos = ~pos;
            if (pos <= highestPos) {
                frame.unbind();
                frame.bind(sibling, ~pos);
                return;
            }
            if (pos == highestPos + 2) {
                byte[] key = frame.mNotFoundKey;
                if (compare(key) < 0) {
                    frame.unbind();
                    frame.bind(sibling, ~pos);
                    return;
                }
            }
            frame.mNodePos = ~(pos - highestPos - 2);
        }
    }

    /**
     * @return length of entry generated by copySplitKeyToParent
     */
    int splitKeyEncodedLength() {
        return Node.calculateKeyLength(mSplitKey);
    }

    /**
     * @param dest destination page of parent internal node
     * @param destLoc location in destination page
     * @return length of internal node encoded key entry
     */
    int copySplitKeyToParent(final byte[] dest, final int destLoc) {
        final byte[] key = mSplitKey;
        final int keyLen = key.length;
        int loc = destLoc;
        if (keyLen <= 64 && keyLen > 0) {
            dest[loc++] = (byte) (keyLen - 1);
        } else {
            dest[loc++] = (byte) (0x80 | (keyLen >> 8));
            dest[loc++] = (byte) keyLen;
        }
        System.arraycopy(key, 0, dest, loc, keyLen);
        return loc + keyLen - destLoc;
    }
}
