package rpg.gfx;

import java.util.Comparator;
import rpg.Game;
import rpg.Location;
import rpg.Maps.Map;
import rpg.Maps.MapTile;
import rpg.Tiles.RoadTile;
import rpg.entities.Sprite;
import rpg.entities.eventhandlers.PlayerEvents;
import rpg.entities.factories.SpriteFactory;
import rpg.entities.factories.CreatureFactory;
import rpg.entities.properties.CreatureProperty;
import rpg.util.ColorCodes;
import rpg.util.SortedList;

/**
 * rothens.tarhely.biz
 *
 * @author Rothens
 */
public class Screen extends Bitmap {

    public FontSets fs;

    public GUI gui;

    private static Screen instance;

    public class Renderable {

        public MapTile[] tiles;

        public int width;

        public int height;

        public Location topLeft;

        public Location bottomRight;

        public Renderable(Map map, int cX, int cY) {
            topLeft = new Location(cX - 320, cY - 240, false, map);
            bottomRight = new Location(cX + 319, cY + 239, false, map);
            width = bottomRight.getTileX() - topLeft.getTileX() + 1;
            height = bottomRight.getTileY() - topLeft.getTileY() + 1;
            getTiles(map);
        }

        private void getTiles(Map map) {
            tiles = new MapTile[width * height];
            int start = topLeft.getMapCoordinate();
            for (int i = 0; i < width * height; i++) {
                int val = start + i % width + map.getWidth() * (i / width);
                tiles[i] = map.tiles[val];
            }
        }
    }

    private Bitmap viewport;

    int time = 0;

    public int x = 0;

    public int y = 0;

    public static Screen getInstance() {
        return instance;
    }

    public Screen(int width, int height) {
        super(width, height);
        instance = this;
        gui = new GUI(width, height);
        fs = new FontSets();
        viewport = new Bitmap(width, height);
    }

    public void render(Game game) {
        time++;
        CreatureFactory player = (CreatureFactory) SpriteFactory.getInstance(0);
        if (player != null) {
            CreatureProperty playerProperty = (CreatureProperty) player.getProp();
            if (playerProperty.getLocation() != null) {
                Location loc = playerProperty.getLocation();
                x = (loc.getX()) + player.getxRenderOffset();
                y = (loc.getY()) + player.getyRenderOffset();
                Renderable r = new Renderable(Game.getInstance().getActual(), x, y);
                for (int i = 0; i < r.width * r.height; i++) {
                    int dx = (i % r.width) * 64 - ((x - 320) % 64);
                    int dy = (i / r.width) * 64 - ((y - 240) % 64);
                    viewport.draw(r.tiles[i].getLandscape().bitmap, dx, dy);
                    if (r.tiles[i].getRoad() != null) {
                        RoadTile rt = r.tiles[i].getRoad();
                        viewport.draw(rt.bitmaps[rt.getState()], dx, dy);
                    }
                    if (r.tiles[i].getLayer1() != null) {
                        viewport.draw(r.tiles[i].getLayer1().bitmap, dx, dy);
                    }
                }
                SortedList<Sprite> sortedList = new SortedList<Sprite>(new Comparator<Sprite>() {

                    @Override
                    public int compare(final Sprite a, final Sprite b) {
                        if (a.getProp().getLocation().getY() > b.getProp().getLocation().getY()) {
                            return 1;
                        }
                        if (a.getProp().getLocation().getY() < b.getProp().getLocation().getY()) {
                            return -1;
                        }
                        return 0;
                    }
                });
                for (Sprite e : SpriteFactory.getEntites()) {
                    if (e.isInView(r)) {
                        sortedList.add(e);
                    }
                }
                for (Sprite e : sortedList) {
                    viewport.draw(e.getDrawGraphics(), e.getxRenderOffset() - 32, e.getyRenderOffset() - 32);
                    if (e instanceof CreatureFactory) {
                        if (e.equals(player)) {
                            continue;
                        }
                        Bar health = e.getHealthBar();
                        viewport.draw(health, e.getxRenderOffset() - 16, e.getyRenderOffset() - 32);
                    }
                }
                for (int i = 0; i < r.width * r.height; i++) {
                    int dx = (i % r.width) * 64 - ((x - 320) % 64);
                    int dy = (i / r.width) * 64 - ((y - 240) % 64);
                    if (r.tiles[i].getLayer2() != null) {
                        viewport.draw(r.tiles[i].getLayer2().bitmap, dx, dy);
                    }
                }
            }
            gui.renderGUI();
            viewport.draw(gui, 0, 0);
            draw(viewport, 0, 0);
        } else {
            for (int i = 0; i < pixels.length; ++i) {
                pixels[i] = 0x0;
            }
            Bitmap t = fs.getText("Game Over", (byte) 0);
            draw(t, 320 - (t.width / 2), 240 - (t.width / 2));
        }
    }
}
