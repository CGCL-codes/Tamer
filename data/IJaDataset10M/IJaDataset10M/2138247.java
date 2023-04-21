package es.org.chemi.games.sokoban.events;

import java.util.List;
import net.sf.pim.game.util.DiamondMap;
import net.sf.pim.game.util.DiamondMap.IsoDirection;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;
import es.org.chemi.games.sokoban.SokobanPlugin;
import es.org.chemi.games.sokoban.ui.LevelCompletedDialog;
import es.org.chemi.games.sokoban.ui.Tile;
import es.org.chemi.games.sokoban.ui.TileMap;
import es.org.chemi.games.sokoban.util.Constants;
import es.org.chemi.games.util.Counter;
import es.org.chemi.games.util.Timer;

/**
 * 从TileMapMouseListener中抽取出来的移动辅助类
 * @author levin
 */
public class MoveHelper {

    private TileMap map = null;

    private static int step = 0;

    public MoveHelper(TileMap map) {
        this.map = map;
    }

    public void move(IsoDirection direction) {
        Tile currentTile;
        Tile nextTile;
        Tile followingTile;
        boolean isEndOfGame = false;
        checkTimer();
        enableUndo();
        currentTile = map.getBoyTile();
        nextTile = map.neighborTile(currentTile, direction);
        followingTile = map.neighborTile(nextTile, direction);
        if (followingTile.equals(nextTile)) {
            return;
        }
        if (nextTile.isFloor() || (nextTile.isBox() && followingTile.isFloor())) isEndOfGame = move(currentTile, nextTile, followingTile, direction); else {
            if (currentTile.isHole()) setTileHoleImage(currentTile, direction); else setTileImage(currentTile, direction);
            currentTile.setModified(true);
        }
        if (currentTile.isTrack()) currentTile.setTrack(false);
        map.redraw();
        postMove(isEndOfGame);
    }

    public void move(final List<Point> list) {
        if (list == null || list.size() == 1 || step != 0) return;
        step = 1;
        new Runnable() {

            public void run() {
                try {
                    if (step >= list.size()) {
                        step = 0;
                        return;
                    }
                    move(DiamondMap.getDirection(list.get(step - 1), list.get(step)));
                    step++;
                    Display.getCurrent().timerExec(Constants.ANIMATION_SPEED, this);
                } catch (Exception ex) {
                    step = 0;
                }
            }
        }.run();
    }

    public void move(final String lruds) {
        if (lruds != null && step == 0) {
            new Runnable() {

                public void run() {
                    if (step >= lruds.length()) {
                        step = 0;
                        return;
                    }
                    switch(lruds.charAt(step)) {
                        case 'L':
                        case 'l':
                            move(IsoDirection.NORTH_WEST);
                            break;
                        case 'R':
                        case 'r':
                            move(IsoDirection.SOUTH_EAST);
                            break;
                        case 'U':
                        case 'u':
                            move(IsoDirection.NORTH_EAST);
                            break;
                        case 'D':
                        case 'd':
                            move(IsoDirection.SOUTH_WEST);
                            break;
                        default:
                    }
                    step++;
                    Display.getCurrent().timerExec(Constants.ANIMATION_SPEED / 4, this);
                }
            }.run();
        }
    }

    private boolean move(Tile currentTile, Tile nextTile, Tile followingTile, IsoDirection direction) {
        boolean isEndOfGame = false;
        currentTile.setFloor(true);
        currentTile.setModified(true);
        ((Counter) map.getCounters().get(Constants.COUNTER_MOVES)).increase();
        if (nextTile.isBox()) {
            if (map.getPreferences().isSoundEnabled()) SokobanPlugin.getResourceManager().getSound(Constants.SOUND_PUSH).play();
            ((Counter) map.getCounters().get(Constants.COUNTER_PUSHES)).increase();
            nextTile.setBox(false);
            if (followingTile.isHole()) followingTile.setImage(SokobanPlugin.getResourceManager().getImage(Constants.IMAGE_BOX_OK)); else followingTile.setImage(SokobanPlugin.getResourceManager().getImage(Constants.IMAGE_BOX));
            followingTile.setBox(true);
            followingTile.setModified(true);
            isEndOfGame = map.checkEndOfGame();
        }
        if (nextTile.isHole()) {
            setTileHoleImage(nextTile, direction);
        } else {
            setTileImage(nextTile, direction);
        }
        nextTile.setModified(true);
        map.setBoyTile(nextTile);
        if (map.getPreferences().isSoundEnabled()) SokobanPlugin.getResourceManager().getSound(Constants.SOUND_MOVE).play();
        return isEndOfGame;
    }

    public static void setTileHoleImage(Tile tile, IsoDirection direction) {
        tile.setDirection(direction);
        if (direction == IsoDirection.NORTH_EAST) tile.setImage(SokobanPlugin.getResourceManager().getImage(Constants.IMAGE_BOY_UP_TRANSPARENT)); else if (direction == IsoDirection.SOUTH_WEST) tile.setImage(SokobanPlugin.getResourceManager().getImage(Constants.IMAGE_BOY_DOWN_TRANSPARENT)); else if (direction == IsoDirection.SOUTH_EAST) tile.setImage(SokobanPlugin.getResourceManager().getImage(Constants.IMAGE_BOY_RIGHT_TRANSPARENT)); else if (direction == IsoDirection.NORTH_WEST) tile.setImage(SokobanPlugin.getResourceManager().getImage(Constants.IMAGE_BOY_LEFT_TRANSPARENT));
    }

    public static void setTileImage(Tile tile, IsoDirection direction) {
        tile.setDirection(direction);
        if (direction == IsoDirection.NORTH_EAST) tile.setImage(SokobanPlugin.getResourceManager().getImage(Constants.IMAGE_BOY_UP)); else if (direction == IsoDirection.SOUTH_WEST) tile.setImage(SokobanPlugin.getResourceManager().getImage(Constants.IMAGE_BOY_DOWN)); else if (direction == IsoDirection.SOUTH_EAST) tile.setImage(SokobanPlugin.getResourceManager().getImage(Constants.IMAGE_BOY_RIGHT)); else if (direction == IsoDirection.NORTH_WEST) tile.setImage(SokobanPlugin.getResourceManager().getImage(Constants.IMAGE_BOY_LEFT));
    }

    private void postMove(boolean isEndOfGame) {
        if (isEndOfGame) {
            map.unhookListener();
            ((Timer) map.getCounters().get(Constants.COUNTER_TIME)).stop();
            if (map.getPreferences().isSoundEnabled()) SokobanPlugin.getResourceManager().getSound(Constants.SOUND_FINISH).play();
            LevelCompletedDialog dialog = new LevelCompletedDialog(Display.getCurrent().getActiveShell(), map);
            dialog.open();
        }
    }

    private void enableUndo() {
        map.saveTiles();
        map.getUndoAction().setEnabled(true);
    }

    private void checkTimer() {
        if (!((Timer) map.getCounters().get(Constants.COUNTER_TIME)).isRunning()) {
            ((Timer) map.getCounters().get(Constants.COUNTER_TIME)).start();
            map.getPauseAction().setIsGamePaused(false);
        }
    }
}
