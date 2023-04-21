package net.slashie.serf.levelGeneration.connectedRooms;

import java.util.ArrayList;
import java.util.List;
import net.slashie.serf.action.Action;
import net.slashie.serf.level.AbstractCell;
import net.slashie.serf.level.AbstractLevel;
import net.slashie.serf.level.BufferedLevel;
import net.slashie.serf.level.Dispatcher;
import net.slashie.serf.level.MapCellFactory;
import net.slashie.serf.levelGeneration.StaticGenerator;
import net.slashie.utils.Position;
import net.slashie.utils.Util;

public abstract class FeatureCarveGenerator extends StaticGenerator {

    private String[][] preLevel;

    private boolean[][] mask;

    private String[][] preLevelB;

    private boolean[][] maskB;

    private List<Position> hotspots = new ArrayList<Position>();

    private List<Position> roomHotspots = new ArrayList<Position>();

    private String solidCell;

    private String corridor;

    private List<Feature> levelFeatures;

    public void initialize(List<Feature> levelFeatures, String solidCell, String corridor, int xdim, int ydim) {
        preLevel = new String[xdim][ydim];
        mask = new boolean[xdim][ydim];
        preLevelB = new String[xdim][ydim];
        maskB = new boolean[xdim][ydim];
        this.solidCell = solidCell;
        this.corridor = corridor;
        this.levelFeatures = levelFeatures;
    }

    boolean checkCorridor = false;

    public void setCheckCorridor(boolean val) {
        checkCorridor = val;
    }

    private void save() {
        for (int i = 0; i < mask.length; i++) {
            System.arraycopy(mask[i], 0, maskB[i], 0, mask[i].length);
            System.arraycopy(preLevel[i], 0, preLevelB[i], 0, preLevel[i].length);
        }
    }

    private void rollBack() {
        for (int i = 0; i < mask.length; i++) {
            System.arraycopy(maskB[i], 0, mask[i], 0, mask[i].length);
            System.arraycopy(preLevelB[i], 0, preLevel[i], 0, preLevel[i].length);
        }
    }

    public AbstractLevel generateLevel(BufferedLevel ret) {
        boolean checked = false;
        boolean placed = false;
        int i = 0;
        go: while (!checked) {
            List<Feature> pendingFeatures = new ArrayList<Feature>(levelFeatures);
            hotspots.clear();
            roomHotspots.clear();
            for (int x = 0; x < getLevelWidth(); x++) {
                for (int y = 0; y < getLevelHeight(); y++) {
                    preLevel[x][y] = solidCell;
                    mask[x][y] = false;
                }
            }
            Position pos = new Position(getLevelWidth() / 2, getLevelHeight() / 2);
            Feature room = null;
            int direction = 0;
            boolean finished = false;
            while (!placed) {
                room = (Feature) Util.randomElementOf(pendingFeatures);
                switch(Util.rand(1, 4)) {
                    case 1:
                        direction = Action.UP;
                        break;
                    case 2:
                        direction = Action.DOWN;
                        break;
                    case 3:
                        direction = Action.LEFT;
                        break;
                    case 4:
                        direction = Action.RIGHT;
                        break;
                }
                if (room.drawOverCanvas(preLevel, pos, direction, mask, hotspots)) {
                    pendingFeatures.remove(room);
                    if (pendingFeatures.isEmpty()) {
                        finished = true;
                        checked = true;
                    }
                    placed = true;
                } else {
                    i++;
                    if (i > 50000) {
                        i = 0;
                        continue go;
                    }
                }
            }
            placed = false;
            save();
            boolean letsRollBack = false;
            while (!finished) {
                pos = (Position) Util.randomElementOf(hotspots);
                int corridors = Util.rand(1, 3);
                int j = 0;
                while (j < corridors && !letsRollBack) {
                    CorridotFeature corridorF = new CorridotFeature(Util.rand(4, 5), corridor);
                    switch(Util.rand(1, 4)) {
                        case 1:
                            direction = Action.UP;
                            break;
                        case 2:
                            direction = Action.DOWN;
                            break;
                        case 3:
                            direction = Action.LEFT;
                            break;
                        case 4:
                            direction = Action.RIGHT;
                            break;
                    }
                    if (corridorF.drawOverCanvas(preLevel, pos, direction, mask, roomHotspots)) {
                        j++;
                        pos = corridorF.getTip();
                    } else {
                        letsRollBack = true;
                    }
                }
                if (letsRollBack) {
                    rollBack();
                    letsRollBack = false;
                    continue;
                }
                room = (Feature) Util.randomElementOf(pendingFeatures);
                if (room.drawOverCanvas(preLevel, pos, direction, mask, hotspots)) {
                    pendingFeatures.remove(room);
                    save();
                    if (pendingFeatures.isEmpty()) {
                        finished = true;
                        checked = true;
                    }
                    placed = true;
                } else {
                    rollBack();
                }
                placed = false;
            }
        }
        ret.setCells(new AbstractCell[1][getLevelWidth()][getLevelHeight()]);
        String[] levelMap = new String[preLevel[0].length];
        for (int y = 0; y < getLevelHeight(); y++) {
            for (int x = 0; x < getLevelWidth(); x++) {
                if (levelMap[y] == null) levelMap[y] = preLevel[x][y]; else levelMap[y] += preLevel[x][y];
            }
        }
        renderOverLevel(ret, levelMap, new Position(0, 0));
        Position entrance = new Position(0, 0);
        Position exit = new Position(0, 0);
        while (true) {
            entrance.x = Util.rand(1, getLevelWidth() - 2);
            entrance.y = Util.rand(1, getLevelHeight() - 2);
            if (ret.isExitPlaceable(entrance) && (!checkCorridor || !preLevel[entrance.x][entrance.y].equals(corridor))) {
                addEntrance(ret, entrance);
                break;
            }
        }
        while (true) {
            exit.x = Util.rand(1, getLevelWidth() - 2);
            exit.y = Util.rand(1, getLevelHeight() - 2);
            if (ret.isExitPlaceable(exit) && (!checkCorridor || !preLevel[exit.x][exit.y].equals(corridor))) {
                addExit(ret, exit);
                break;
            }
        }
        ret.setDispatcher(new Dispatcher());
        return ret;
    }

    public abstract void addExit(BufferedLevel ret, Position exit);

    public abstract void addEntrance(BufferedLevel ret, Position entrance);

    private int getLevelWidth() {
        return preLevel.length;
    }

    private int getLevelHeight() {
        return preLevel[0].length;
    }
}
