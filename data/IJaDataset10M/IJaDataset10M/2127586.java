package net.sourceforge.strategema.games.boards;

import net.sourceforge.strategema.games.BoardState;
import net.sourceforge.strategema.games.Coordinate;
import net.sourceforge.strategema.games.Creditable;
import net.sourceforge.strategema.games.GameFile;
import net.sourceforge.strategema.games.PieceSet;
import net.sourceforge.strategema.util.datastructures.Pair;
import java.awt.Color;
import java.awt.Container;
import java.awt.Image;
import java.awt.Shape;
import java.util.List;
import java.util.Set;

/**
 * A board created from Shape objects filled with a single colour. ShapeBoards are always rotatable.
 * 
 * @author Lizzy
 * 
 */
public abstract class ShapeBoard extends AbstractBoard<Image> {

    private Shape[][][] squares;

    private Color[][][] colors;

    private Pair<Integer, Integer> layerSizes;

    private final int maxLayer, maxRank;

    protected ShapeBoard(final BoardState state, final PieceSet<? extends Image> defaultPieceSet, final boolean fourPlayerPerpendicular) throws NullPointerException {
        super(state, defaultPieceSet, fourPlayerPerpendicular);
        this.maxLayer = 0;
        this.maxRank = 0;
    }

    /**
	 * Lays squares out in a grid pattern.
	 * @param template The template to replicate.
	 * @param colours The colours to fill the template shapes.
	 * @param x The starting x-coordinate in board coordinates.
	 * @param y The starting y-coordinate in board coordinates.
	 * @param z The z-coordinate in board coordinates.
	 * @param tx The amount to move across to get to the next stamp position in graphics coordinates.
	 * @param ty The amount to move up to get to the next stamp position in graphics coordinates.
	 * @param dx The amount to increment the board x-coordinate by (usually 1).
	 * @param dy The amount to increment the board y-coordinate by (usually 1).
	 * @param numX The number of times to stamp out the template along the x-axis.
	 * @param numY The number of times to stamp out the template along the y-axis.
	 */
    protected void addSquares(final Shape[][] template, final Color[][] colours, final int x, final int y, final int z, final double tx, final double ty, final int dx, final int dy, final int numX, final int numY) {
    }

    protected void createGap(final int x1, final int y1, final int z1, final int x2, final int y2, final int z2) {
        final int stepX = (x2 > x1 ? 1 : -1);
        final int stepY = (y2 > y1 ? 1 : -1);
        final int stepZ = (z2 > z1 ? 1 : -1);
        int x, y, z = z1 - stepZ;
        Shape[][] yzPlane;
        Shape[] column;
        do {
            z = z + stepZ;
            if (z < 0) {
                break;
            }
            y = y1 - stepY;
            do {
                y = y + stepY;
                if (y < 0) {
                    break;
                }
                x = x1 - stepX;
                do {
                    x = x + stepX;
                    if (x < 0 || x >= this.squares.length) {
                        break;
                    }
                    yzPlane = this.squares[x];
                    if (y < 0 || y >= yzPlane.length) {
                        continue;
                    }
                    column = yzPlane[y];
                    if (z >= 0 && z < column.length) {
                        column[z] = null;
                    }
                } while (x != x2);
            } while (y != y2);
        } while (z != z2);
    }

    @Override
    public void addTo(final Container comp, final Object constraints, final int index) {
    }

    @Override
    public boolean canRedrawSquares() {
        return true;
    }

    @Override
    public synchronized void redrawSquare(final Coordinate<?, ?, ?> square) {
    }

    @Override
    public synchronized void drawItemOnSquare(final Coordinate<?, ?, ?> square, final Image item) {
    }

    @Override
    public Coordinate<?, ?, ?> selectSquare() {
    }

    @Override
    public Class<Image> getVisualRepresentation() {
        return Image.class;
    }

    @Override
    public synchronized void highlightSquares(final Set<Coordinate<?, ?, ?>> squares, final int colour) {
    }

    @Override
    public synchronized void redraw() {
    }

    @Override
    public void setVisible(final boolean visibility) {
    }

    @Override
    public synchronized void showMovement(final List<Coordinate<?, ?, ?>> path, final int player, final Image item) {
    }

    @Override
    public synchronized void unhighlightAllSquares() {
    }

    @Override
    public int getMaxFile() {
        return this.squares.length - 1;
    }

    @Override
    public int getMaxLevel() {
        return this.maxLayer;
    }

    @Override
    public int getMaxRank() {
        return this.maxRank;
    }

    @Override
    public boolean isSquare(final int x, final int y, final int z) {
        final Shape[][] yzPlane;
        final Shape[] column;
        if (x >= this.squares.length) {
            return false;
        }
        yzPlane = this.squares[x];
        if (y >= yzPlane.length) {
            return false;
        }
        column = yzPlane[y];
        return (z < column.length && column[z] != null);
    }

    @Override
    public Set<GameFile> getExternalResources() {
        return this.getPieceSetResources();
    }

    @Override
    public String getCredits() {
        return Creditable.LIZZY;
    }
}
