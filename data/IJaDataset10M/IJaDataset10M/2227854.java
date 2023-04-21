package org.klomp.snark;

/**
 * Holds all information needed for a partial piece request.
 */
class Request {

    final int piece;

    final byte[] bs;

    final int off;

    final int len;

    /**
     * Creates a new Request.
     * 
     * @param piece
     *            Piece number requested.
     * @param bs
     *            byte array where response should be stored.
     * @param off
     *            the offset in the array.
     * @param len
     *            the number of bytes requested.
     */
    Request(int piece, byte[] bs, int off, int len) {
        this.piece = piece;
        this.bs = bs;
        this.off = off;
        this.len = len;
        if (piece < 0 || off < 0 || len <= 0 || off + len > bs.length) {
            throw new IndexOutOfBoundsException("Illegal Request " + toString());
        }
    }

    @Override
    public int hashCode() {
        return piece ^ off ^ len;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Request) {
            Request req = (Request) o;
            return req.piece == piece && req.off == off && req.len == len;
        }
        return false;
    }

    @Override
    public String toString() {
        return "(" + piece + "," + off + "," + len + ")";
    }
}
