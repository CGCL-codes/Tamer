package pipes.root;

import junit.framework.TestCase;

public class SourcePieceTest extends TestCase {

    public void testSource() {
        sourcePieceTest(Side.TOP);
        sourcePieceTest(Side.BOTTOM);
        sourcePieceTest(Side.LEFT);
        sourcePieceTest(Side.RIGHT);
    }

    private void sourcePieceTest(Side side) {
        SourcePiece piece = new SourcePiece(side);
        assertFalse(piece.isFull(null));
        assertFalse(piece.isFull(Side.TOP));
        boolean filled = piece.fill(Side.BOTTOM);
        assertFalse(filled);
        assertFalse(piece.isFull(null));
        assertFalse(piece.isFull(Side.BOTTOM));
        filled = piece.fill(null);
        assertTrue(filled);
        assertTrue(piece.isFull(null));
        assertTrue(piece.isFull(Side.LEFT));
        for (Side aSide : Side.values()) {
            if (side != aSide) {
                assertFalse(piece.isPipeAt(aSide));
            }
            assertNull(piece.getFarSide(aSide));
        }
        assertTrue(piece.isPipeAt(side));
    }
}
