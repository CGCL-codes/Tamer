package com.myapp.games.tetris;

import java.util.Iterator;
import com.myapp.games.tetris.Grid.Tile;

public class BlockIterator {

    /** visits each tile that is filled by this block */
    static interface BlockTileVisitor {

        /** visits a tile that is filled by this block
         * 
         * @param tile
         *            the tile that is filled by this block
         * @return true, if the iteration should be continued or, if
         *         false, the
         *         {@link Block#forEachTile(BlockTileVisitor)} method
         *         will return after this tile
         * @param relativeRow
         *            the coordinate of the tile within the
         *            {@link Block#flags} array
         * @param relativeCol
         *            the coordinate of the tile within the
         *            {@link Block#flags} array
         * @see Block#forEachTile(BlockTileVisitor) */
        boolean visit(Tile tile, int relativeRow, int relativeCol);
    }

    /** iterates over all filled tiles and applies the visitor on it
     * @param v the visitor */
    void forEachTile(Block b, BlockTileVisitor v) {
        for (int relRow = 0; relRow < b.flags.length; relRow++) {
            int absRow = b.getRow() + relRow;
            if (absRow < 0 || absRow >= b.game.rows) {
                continue;
            }
            for (int relCol = 0; relCol < b.flags[0].length; relCol++) {
                int absCol = relCol + b.getCol();
                if (absCol < 0 || absCol >= b.game.columns || !b.flags[relRow][relCol]) {
                    continue;
                }
                Tile filled = b.game.getGrid().getTile(absRow, absCol);
                boolean goAhead = v.visit(filled, relRow, relCol);
                if (!goAhead) {
                    return;
                }
            }
        }
    }

    static class VisibleBlockTileIterator implements Iterator<Tile> {

        private int relativeRow, relativeCol;

        private Tile next = null;

        private Block b = null;

        VisibleBlockTileIterator(Block b) {
            this.b = b;
            relativeCol = relativeRow = 0;
            next = searchNext();
        }

        private Tile searchNext() {
            final int flagsLen = b.flags.length;
            for (; relativeRow < flagsLen; relativeRow++) {
                int absRow = calcAbsRow();
                if (absRow < 0 || absRow >= b.game.rows) {
                    continue;
                }
                for (; relativeCol < flagsLen; relativeCol++) {
                    int absCol = calcAbsCol();
                    if (absCol < 0 || absCol >= b.game.columns || !b.flags[relativeRow][relativeCol]) {
                        continue;
                    }
                    if (++relativeCol >= flagsLen) {
                        relativeCol = 0;
                        relativeRow++;
                    }
                    Tile usedByBlock = b.game.getGrid().getTile(absRow, absCol);
                    return usedByBlock;
                }
            }
            return null;
        }

        private int calcAbsCol() {
            return b.getCol() + relativeCol;
        }

        private int calcAbsRow() {
            return b.getRow() + relativeRow;
        }

        public Tile next() {
            Tile result = next;
            next = searchNext();
            return result;
        }

        public boolean hasNext() {
            return next != null;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    void forEachBottomTile(final Block b, final BlockTileVisitor v) {
        forEachTile(b, new BlockTileVisitor() {

            public boolean visit(Tile tile, int relR, int relC) {
                if (relR == b.flags.length - 1) {
                    return v.visit(tile, relR, relC);
                }
                if (!b.flags[relR + 1][relC]) {
                    return v.visit(tile, relR, relC);
                }
                return true;
            }
        });
    }

    void forEachRightTile(final Block b, final BlockTileVisitor v) {
        forEachTile(b, new BlockTileVisitor() {

            public boolean visit(Tile tile, int relRow, int relCol) {
                if (relCol == b.flags.length - 1 || !b.flags[relRow][relCol + 1]) {
                    return v.visit(tile, relRow, relCol);
                }
                return true;
            }
        });
    }

    void forEachLeftTile(final Block b, final BlockTileVisitor v) {
        forEachTile(b, new BlockTileVisitor() {

            public boolean visit(Tile tile, int relRow, int relCol) {
                if (relCol == 0 || (relCol > 0 && !b.flags[relRow][relCol - 1])) {
                    return v.visit(tile, relRow, relCol);
                }
                return true;
            }
        });
    }
}
