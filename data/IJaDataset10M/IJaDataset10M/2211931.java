package es.org.chemi.games.sokoban.events;

import net.sf.pim.game.util.DiamondMap;
import net.sf.pim.game.util.DiamondMap.IsoDirection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import es.org.chemi.games.sokoban.SokobanPlugin;
import es.org.chemi.games.sokoban.ui.Tile;
import es.org.chemi.games.sokoban.ui.TileMap;
import es.org.chemi.games.sokoban.util.Constants;
import es.org.chemi.games.util.Timer;

public class TileMapKeyListener extends KeyAdapter {

    private TileMap map = null;

    public TileMapKeyListener(TileMap map) {
        this.map = map;
    }

    public void keyPressed(KeyEvent ev) {
        boolean key3DMode = map.getPreferences().isKey3DEnabled();
        if (ev.keyCode == SWT.ARROW_RIGHT || ev.character == Character.toLowerCase('d')) if (key3DMode) changeDirection(-1); else move(IsoDirection.NORTH_EAST); else if (ev.keyCode == SWT.ARROW_LEFT || ev.character == Character.toLowerCase('a')) if (key3DMode) changeDirection(1); else move(IsoDirection.SOUTH_WEST); else if (ev.keyCode == SWT.ARROW_UP || ev.character == Character.toLowerCase('w')) if (key3DMode) move(map.getBoyTile().getDirection()); else move(IsoDirection.NORTH_WEST); else if (ev.keyCode == SWT.ARROW_DOWN || ev.character == Character.toLowerCase('s')) if (key3DMode) changeDirection(-99); else move(IsoDirection.SOUTH_EAST); else if (ev.character == Character.toLowerCase('p')) {
            ((Timer) map.getCounters().get(Constants.COUNTER_TIME)).stop();
            map.getPauseAction().setIsGamePaused(true);
        }
    }

    private void changeDirection(int delta) {
        IsoDirection direction = map.getBoyTile().getDirection();
        direction = DiamondMap.changeDirection(delta, direction);
        Tile tile = map.getBoyTile();
        tile.setModified(true);
        if (tile.isHole()) MoveHelper.setTileHoleImage(tile, direction); else MoveHelper.setTileImage(tile, direction);
        map.redraw();
        if (map.getPreferences().isSoundEnabled()) SokobanPlugin.getResourceManager().getSound(Constants.SOUND_MOVE).play();
    }

    public void move(IsoDirection direction) {
        new MoveHelper(map).move(direction);
    }
}
