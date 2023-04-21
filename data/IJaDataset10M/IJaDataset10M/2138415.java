package antlr;

import java.io.IOException;

public class TokenBuffer {

    protected TokenStream input;

    int nMarkers = 0;

    int markerOffset = 0;

    int numToConsume = 0;

    TokenQueue queue;

    /** Create a token buffer */
    public TokenBuffer(TokenStream input_) {
        input = input_;
        queue = new TokenQueue(1);
    }

    /** Reset the input buffer to empty state */
    public final void reset() {
        nMarkers = 0;
        markerOffset = 0;
        numToConsume = 0;
        queue.reset();
    }

    /** Mark another token for deferred consumption */
    public final void consume() {
        numToConsume++;
    }

    /** Ensure that the token buffer is sufficiently full */
    private final void fill(int amount) throws TokenStreamException {
        syncConsume();
        while (queue.nbrEntries < amount + markerOffset) {
            queue.append(input.nextToken());
        }
    }

    /** return the Tokenizer (needed by ParseView) */
    public TokenStream getInput() {
        return input;
    }

    /** Get a lookahead token value */
    public final int LA(int i) throws TokenStreamException {
        fill(i);
        return queue.elementAt(markerOffset + i - 1).getType();
    }

    /** Get a lookahead token */
    public final Token LT(int i) throws TokenStreamException {
        fill(i);
        return queue.elementAt(markerOffset + i - 1);
    }

    /**Return an integer marker that can be used to rewind the buffer to
     * its current state.
     */
    public final int mark() {
        syncConsume();
        nMarkers++;
        return markerOffset;
    }

    /**Rewind the token buffer to a marker.
     * @param mark Marker returned previously from mark()
     */
    public final void rewind(int mark) {
        syncConsume();
        markerOffset = mark;
        nMarkers--;
    }

    /** Sync up deferred consumption */
    private final void syncConsume() {
        while (numToConsume > 0) {
            if (nMarkers > 0) {
                markerOffset++;
            } else {
                queue.removeFirst();
            }
            numToConsume--;
        }
    }
}
