package com.mac.chriswjohnson.mazewars;

import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.Random;
import java.util.Stack;

/**
 * Created by IntelliJ IDEA.
 * User: chrisj
 * Date: Aug 27, 2008
 * Time: 11:45:28 PM
 * To change this template use File | Settings | File Templates.
 */
public class Maze implements XMLProducer {

    private static final Logger Log = Logger.getLogger(Maze.class.getName());

    private final int Width;

    private final int Height;

    private final Cell[][] Grid;

    public static void main(final String[] args) {
        new Maze(new Random(), 25, 25);
    }

    public Maze(final Random Rndm, final int Width, final int Height) {
        this.Width = Width % 2 == 0 ? Width + 1 : Width;
        this.Height = Height % 2 == 0 ? Height + 1 : Height;
        this.Grid = createGrid(this.Width, this.Height);
        final Stack<Cell> Path = new Stack<Cell>();
        final int StartX = (Rndm.nextInt(this.Width - 2 >> 1) << 1) + 1;
        final int StartY = (Rndm.nextInt(this.Height - 2 >> 1) << 1) + 1;
        Cell C = Grid[StartY][StartX];
        Path.push(C);
        while (true) {
            C = C.linkToRandomUnvisitedNeighbor(Rndm, Grid);
            if (C != null) {
                Path.push(C);
            } else {
                if (Path.isEmpty()) break;
                C = Path.pop();
            }
        }
        Log.log(Level.FINE, "New {0} X {1} maze generated. ASCII representation:\n{2}", new Object[] { this.Width, this.Height, this });
    }

    private static Cell[][] createGrid(final int Width, final int Height) {
        final Cell[][] Grid = new Cell[Height][Width];
        for (int Y = 0; Y < Height; Y++) {
            for (int X = 0; X < Width; X++) Grid[Y][X] = new Cell(X, Y);
        }
        for (int Y = 0; Y < Height; Y++) {
            for (int X = 0; X < Width; X++) Grid[Y][X].init(Grid, Width, Height);
        }
        return Grid;
    }

    public Cell getCell(final int X, final int Y) {
        return Grid[Y][X];
    }

    public Cell getCell(final Location Loc) {
        return Grid[Loc.Y][Loc.X];
    }

    public boolean createRandomPassageway(final Random Rndm, final Cell C) {
        return C.eliminateRandomWall(Rndm, Grid);
    }

    public void appendXML(final String Indent, final StringBuilder Buff) {
        Buff.append(Indent).append("<maze>\n");
        Buff.append(Indent).append("\t<width>").append(Width).append("</width>\n");
        Buff.append(Indent).append("\t<height>").append(Height).append("</height>\n");
        Buff.append(Indent).append("\t<rows>\n");
        for (final Cell[] Row : Grid) {
            Buff.append(Indent).append("\t\t<row>");
            for (final Cell C : Row) Buff.append(C.isWall() ? '#' : C.getContent() instanceof Teleport ? 'O' : ' ');
            Buff.append("</row>\n");
        }
        Buff.append(Indent).append("\t</rows>\n");
        Buff.append(Indent).append("</maze>\n");
    }

    public String toString() {
        final StringBuilder Buff = new StringBuilder();
        for (final Cell[] Row : Grid) {
            for (final Cell C : Row) Buff.append(C.isWall() ? '#' : C.getContent() instanceof Teleport ? 'O' : ' ');
            Buff.append('\n');
        }
        return Buff.toString();
    }
}
