package org.jspar.place;

import java.util.List;
import org.jspar.Options;
import org.jspar.Partition;
import org.jspar.matrix.ConnectionMatrix;
import org.jspar.model.Module;
import org.jspar.model.NetList;
import org.jspar.model.Terminal;
import org.jspar.tile.MostDown;
import org.jspar.tile.MostLeft;
import org.jspar.tile.MostRight;
import org.jspar.tile.MostUp;
import org.jspar.tile.Position;
import org.jspar.tile.Rectangle;
import org.jspar.tile.SVGExporter;
import org.jspar.tile.Tile;

public class Placement {

    public static final int WHITE_SPACE = 3;

    public static final int CC_SPACE = 4;

    public static final int CHANNEL_WIDTH = 4;

    public static final int CHANNEL_HEIGHT = 4;

    public static final int TILE_SPACE_X_LEN = 350;

    public static final int TILE_SPACE_Y_LEN = 350;

    public static final int TILE_SPACE_X_POS = -150;

    public static final int TILE_SPACE_Y_POS = -150;

    private List<Partition> partitions;

    private Options configuration;

    private NetList netlist;

    private ConnectionMatrix matrix;

    private StringBuilder stringBuilder;

    private int xfloor;

    private int yfloor;

    public Placement(Options conf, ConnectionMatrix matrix, NetList netlist, List<Partition> partitions) {
        this.configuration = conf;
        this.netlist = netlist;
        this.matrix = matrix;
        this.stringBuilder = new StringBuilder(conf);
        this.partitions = partitions;
    }

    private Partition currentPartition;

    private Tile currentTile;

    private int currentPartitionWidth;

    private int currentPartitionHeight;

    public Rectangle getBounds() {
        return new Rectangle(xfloor, yfloor, currentPartition.getHeight(), currentPartition.getWidth());
    }

    public Partition run() {
        xfloor = 0;
        yfloor = 0;
        for (Partition p : partitions) {
            currentPartition = p;
            p.setRootTile(currentTile = new Tile(TILE_SPACE_X_POS, TILE_SPACE_Y_POS, TILE_SPACE_X_LEN, TILE_SPACE_Y_LEN, new StringContentsTileDelegate()));
            System.out.println("====== Place inside partition " + currentPartition);
            ModuleString string = stringBuilder.buildInitialString(p);
            System.out.println("initial => " + string);
            placeHead(string);
            placeString(string);
            string.fixupOffset(xfloor, yfloor);
            xfloor = yfloor = 0;
            currentPartitionWidth = string.width();
            currentPartitionHeight = string.height();
            currentTile.insertTile(0, 0, string.width(), string.height(), string);
            System.out.println("====== Place remaining strings");
            while (hasNextToPlace()) {
                string = stringBuilder.buildRemainingString(p);
                System.out.println("best next => " + string);
                placeHead(string);
                placeString(string);
                string.fixupOffset(xfloor, yfloor);
                placeBox(string);
                if (xfloor < 0 || yfloor < 0) {
                    xfloor = 0;
                    yfloor = 0;
                }
            }
            p.setSize(currentPartitionWidth, currentPartitionWidth);
        }
        return partitionPlacement();
    }

    private Partition partitionPlacement() {
        int c_count = partitions.size();
        int next = findMaxDiagonal(partitions.size());
        int order = 1;
        while (partitions.size() > 1) {
            if (next == 0) next = 1;
            Partition part = partitions.remove(next);
            throw new RuntimeException("partition placement not complete");
        }
        if (xfloor < 0 || yfloor < 0) {
            throw new RuntimeException("partition placement not complete");
        }
        SVGExporter.export("place.svg", partitions.get(0).rootTile());
        return partitions.get(0);
    }

    private int findMaxDiagonal(int range) {
        int i, max = 0, index = 0;
        for (i = 1; i < range; ++i) {
            if (matrix.valueAt(i, i) > max) {
                max = matrix.valueAt(i, i);
                index = i;
            }
        }
        return index;
    }

    private void placeHead(ModuleString string) {
        if (string.length() == 0) return;
        Module module = string.moduleAt(0);
        if (string.length() > 1 && module.primaryOut() != null) {
            switch(module.primaryOut().side()) {
                case Terminal.LEFT:
                    module.rotate(Module.R180);
                    break;
                case Terminal.UP:
                    module.rotate(Module.R90);
                    break;
                case Terminal.DOWN:
                    module.rotate(Module.R270);
                    break;
            }
        }
        module.setPosition(WHITE_SPACE * module.countTerminals(Terminal.LEFT), WHITE_SPACE * module.countTerminals(Terminal.UP) + 1);
        module.setFlag(Module.PLACED);
        yfloor = 0;
        string.setSize(module.x() + module.width() + WHITE_SPACE * (module.countTerminals(Terminal.RIGHT) + 1), module.y() + module.width() + WHITE_SPACE * (module.countTerminals(Terminal.DOWN) + 1));
    }

    private int placeString(ModuleString string) {
        int offset = 0;
        Module topCC = null, botCC = null, ccMod = null;
        boolean ccFlag = false;
        Module last = string.moduleAt(0);
        Terminal prevt = last.outputTerminalConnecting(string.length() > 1 ? string.moduleAt(1) : null);
        System.out.println("placeString " + last.name() + " -> " + prevt);
        int originalLength = string.length();
        for (int i = 1; i < originalLength; ++i) {
            Module m = string.moduleAt(i);
            System.out.println("next " + m.name());
            if (!m.hasFlag(Module.PLACED)) {
                Module cc = m;
                Module ttopCC = last;
                int cci = i + 1;
                for (int lev = 1; lev <= configuration.ccSearchLevel && cc != null; ++lev) {
                    if (new CrossCoupledDetector().isCrossCoupled(last, cc, lev)) {
                        System.out.println("cc -> " + last + "/" + cc);
                        ccFlag = true;
                        topCC = ttopCC;
                        botCC = cc;
                        ccMod = last;
                        break;
                    }
                    ttopCC = cc;
                    cc = cci < string.length() ? string.moduleAt(cci++) : null;
                }
                if (m.primaryIn() != null) {
                    switch(m.primaryIn().side()) {
                        case Terminal.DOWN:
                            m.rotate(Module.R90);
                            break;
                        case Terminal.RIGHT:
                            m.rotate(Module.R180);
                            break;
                        case Terminal.UP:
                            m.rotate(Module.R270);
                            break;
                    }
                }
                if (ccFlag && m == botCC) {
                    System.out.println(botCC.name() + " is cross-coupled with " + ccMod.name());
                    botCC.setFlag(Module.PLACED);
                    topCC.clearFlag(Module.PLACED);
                    currentPartition.clearFlags(Module.SEEN);
                    string.setFlagsFrom(Module.SEEN, i);
                    int[] weight = new int[1];
                    ModuleString topString = stringBuilder.longuestPath(currentPartition, topCC, weight);
                    topCC.setFlag(Module.PLACED);
                    System.out.println("=>" + topString);
                    if (topString != null && topString.length() > 1) {
                        offset = placeString(topString);
                        string.mergeWith(topString);
                    }
                    System.out.println("placing " + m + " under " + ccMod);
                    m.setPosition(ccMod.x(), ccMod.y() + ccMod.height() + CC_SPACE + (WHITE_SPACE * (m.countTerminals(Terminal.UP) + 1)) + offset);
                } else {
                    System.out.println("placing " + m + " after " + last);
                    int margin = WHITE_SPACE * (m.countTerminalsWith(last) + 1);
                    m.setPosition(last.x() + last.width() + margin, lineUp(m, prevt));
                }
                m.setFlag(Module.PLACED);
                offset = botCC != null ? botCC.y() + botCC.height() + WHITE_SPACE * botCC.countTerminals(Terminal.DOWN) : 0;
                if (yfloor > (m.y() - WHITE_SPACE * m.countTerminals(Terminal.UP))) yfloor = m.y() - WHITE_SPACE * m.countTerminals(Terminal.UP);
                string.updateSize(m.x() + m.width() + CHANNEL_WIDTH + WHITE_SPACE * (m.countTerminals(Terminal.RIGHT) + 1), m.y() + m.height() + CHANNEL_HEIGHT + WHITE_SPACE * (m.countTerminals(Terminal.DOWN) + 1));
            }
            prevt = m.outputTerminalConnecting((i + 1) < string.length() ? string.moduleAt(i + 1) : null);
            last = m;
        }
        return offset;
    }

    private static int lineUp(Module m, Terminal prevt) {
        int y = 0;
        if (prevt == null) throw new RuntimeException("prevt == null");
        Terminal curr = null;
        Terminal[] terms = m.terminals();
        for (int i = 0; i < terms.length; ++i) {
            if (terms[i].net() == prevt.net()) {
                curr = terms[i];
                break;
            }
        }
        switch(prevt.side()) {
            case Terminal.RIGHT:
                y = prevt.y() + prevt.module().y() - curr.y();
                break;
            case Terminal.UP:
                y = prevt.y() + prevt.module().y() - curr.y() + Placement.WHITE_SPACE;
                break;
            case Terminal.DOWN:
                y = prevt.module().y() - Placement.WHITE_SPACE - curr.y();
                break;
            case Terminal.LEFT:
                if (prevt.module().y() > (2 * prevt.y())) {
                    y = prevt.module().y() - Placement.WHITE_SPACE - curr.y();
                } else {
                    y = prevt.module().y() + prevt.module().y() + Placement.WHITE_SPACE - curr.y();
                }
                break;
        }
        return y;
    }

    private boolean hasNextToPlace() {
        for (Module m : currentPartition) {
            if (!m.hasFlag(Module.PLACED)) return true;
        }
        return false;
    }

    private void placeBox(ModuleString string) {
        SideSelector selector = string.bestSide(currentPartition, configuration.useAveraging);
        Tile boxTile = null;
        if (selector.hasNoSide()) {
            Position pos = defaultPlaceBox(string, selector);
            boxTile = currentTile.insertTile(pos.x(), pos.y(), string.width(), string.height(), string);
        } else {
            Position anchor = determineAnchorPoint(selector);
            Position pivot = setPivot(string.width(), string.height(), selector);
            selector.normalizeSideCounts();
            boxTile = TileOperations.angledTileInsertion(currentTile, anchor, selector, pivot, string.width(), string.height(), string);
        }
        if (boxTile == null) throw new RuntimeException("no valid insertion found");
        int x = boxTile.x();
        int y = boxTile.y();
        string.move(x, y);
        boxTile = currentTile.bestTileInSpace(new MostLeft());
        xfloor = boxTile.x();
        boxTile = currentTile.bestTileInSpace(new MostDown());
        yfloor = boxTile.y();
        boxTile = currentTile.bestTileInSpace(new MostRight());
        int width = boxTile.x() + boxTile.width();
        boxTile = currentTile.bestTileInSpace(new MostUp());
        int height = boxTile.y() + boxTile.height();
        currentPartition.setSize(width, height);
        if (xfloor < 0 || yfloor < 0) currentPartition.fixupPartition(xfloor, yfloor);
    }

    private Position defaultPlaceBox(ModuleString string, SideSelector selector) {
        switch(selector.side()) {
            case Terminal.LEFT:
                return new Position(xfloor - string.width(), selector.yGravityCenter());
            case Terminal.UP:
                return new Position(selector.xGravityCenter() - xfloor, currentPartitionHeight);
            case Terminal.RIGHT:
                return new Position(currentPartitionWidth, selector.yGravityCenter());
            case Terminal.DOWN:
                return new Position(selector.xGravityCenter() - xfloor, yfloor - string.height());
        }
        throw new RuntimeException("bad side");
    }

    private Position determineAnchorPoint(SideSelector selector) {
        Tile t = currentTile.locate((int) selector.xgbr, (int) selector.ygbr);
        if (t == null) return new Position((int) selector.xgbr, (int) selector.ygbr);
        if (Math.abs(selector.ySideCount) > Math.abs(selector.xSideCount) || selector.xSideCount == 0) return new Position((int) selector.xgbr, (int) selector.ygbr);
        if (selector.xSideCount > 0) {
            int anchY = (int) selector.ygbr;
            int anchX = t.content() == null ? (int) selector.xgbr : t.right();
            Tile tt = t.locate(anchX + 1, anchY);
            if (tt != null && tt.content() != null) System.err.println("bad anchor chosen for part @(" + anchX + ", " + anchY + ")");
            return new Position(anchX, anchY);
        }
        int anchY = (int) selector.ygbr;
        int anchX = t.content() == null ? (int) selector.xgbr : t.left();
        Tile tt = t.locate(anchX - 1, anchY);
        if (tt != null && tt.content() != null) System.err.println("bad anchor chosen for part @(" + anchX + ", " + anchY + ")");
        return new Position(anchX, anchY);
    }

    private Position setPivot(int width, int height, SideSelector selector) {
        if (Math.abs(selector.ySideCount) > Math.abs(selector.xSideCount) || selector.xSideCount == 0) return new Position((int) selector.xgr, (int) selector.ygr);
        if (selector.xSideCount < 0) {
            return new Position(width, (int) selector.xgr);
        }
        return new Position(0, (int) selector.ygr);
    }
}
