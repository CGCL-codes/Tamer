package antirashka.map.generation;

import antirashka.ai.IBehavior;
import antirashka.ai.PanicBehavior;
import antirashka.ai.SimpleBehavior;
import antirashka.map.IArea;
import antirashka.map.IItem;
import antirashka.map.ITerrain;
import antirashka.map.items.*;
import antirashka.map.terrains.*;
import antirashka.util.Location;
import java.util.*;

public final class CityGenerator {

    private final GenerationConfig config;

    private final Random rnd;

    private final boolean[][] roads;

    private final short[][] result;

    private Segments widest = null;

    private int maxRoadWidth = -1;

    private final NPCs npc = new NPCs();

    public CityGenerator(GenerationConfig config) {
        this.config = config;
        this.rnd = config.rnd;
        IArea area = config.area;
        int height = area.getHeight();
        int width = area.getWidth();
        final List<Cross> crosses = new ArrayList<Cross>();
        roads = MapGenerator.cut(area, rnd, 3, 10, new int[] { 5, 10, 10 }, new RoadCallback() {

            public void road(Segments segments, int width) {
                if (width >= 1) {
                    int numCrosses = rnd.nextInt(segments.length() / 10 + 1) + 1;
                    segments.cross(crosses, numCrosses, width, rnd);
                }
                if (width > maxRoadWidth) {
                    maxRoadWidth = width;
                    widest = segments;
                }
            }
        });
        short[][] blocks = new short[height][width];
        short counter = MapGenerator.splitToBlocks(area, blocks, roads);
        result = new short[height][width];
        short totalCounter = 0;
        for (short i = 1; i <= counter; i++) {
            BlockArea blockArea = new BlockArea(blocks, i);
            int x0 = blockArea.getMinX();
            int y0 = blockArea.getMinY();
            BuildingGenerator bg = new BuildingGenerator(blockArea, rnd, config, npc, x0 + config.delta, y0 + config.delta);
            short[][] buildings = bg.getBlocks();
            int bw = blockArea.getWidth();
            int bh = blockArea.getHeight();
            for (int y = 0; y < bh; y++) {
                for (int x = 0; x < bw; x++) {
                    if (blockArea.contains(x, y)) {
                        short building = buildings[y][x];
                        if (building != 0) {
                            short inside;
                            if (building < 0) {
                                inside = building;
                            } else {
                                inside = (short) (totalCounter + building);
                            }
                            result[y0 + y][x0 + x] = inside;
                        }
                    }
                }
            }
            totalCounter += bg.getBlockCount();
        }
        for (Cross cross : crosses) {
            Segment segment = cross.cross;
            int length = segment.length();
            if (cross.barricade) {
                Team team = npc.nextTeam(config.desiredProportion, rnd, Team.NATO, Team.RASHKA);
                int side = rnd.nextBoolean() ? 1 : -1;
                for (int i = 0; i < length; i += 2) {
                    Location at = segment.getAt(i);
                    if (area.contains(at.x, at.y)) {
                        result[at.y][at.x] = BuildingGenerator.CRATE;
                        Location npcAt = segment.getAt(i, side);
                        npc.add(npcAt, team, config.delta);
                    }
                }
            } else {
                Location c1;
                Location c2;
                Location c3;
                Location c4;
                if (rnd.nextBoolean()) {
                    c1 = segment.getAt(0);
                    c2 = segment.getAt(2);
                    c3 = segment.getAt(0, 2);
                    c4 = segment.getAt(2, 2);
                } else {
                    c1 = segment.getAt(length - 3);
                    c2 = segment.getAt(length - 1);
                    c3 = segment.getAt(length - 3, 2);
                    c4 = segment.getAt(length - 1, 2);
                }
                addPost(c1, area);
                addPost(c2, area);
                addPost(c3, area);
                addPost(c4, area);
                Location center = new Location((c1.x + c2.x + c3.x + c4.x) / 4, (c1.y + c2.y + c3.y + c4.y) / 4);
                Team team = npc.nextTeam(config.desiredProportion, rnd, Team.RASHKA, Team.NATO);
                npc.add(center, team, config.delta);
                int dx = rnd.nextInt(3) - 1;
                int dy = dx == 0 ? rnd.nextInt(2) * 2 - 1 : 0;
                npc.add(new Location(center.x + dx, center.y + dy), team, config.delta);
            }
        }
    }

    private void addPost(Location at, IArea area) {
        if (area.contains(at.x, at.y)) {
            result[at.y][at.x] = BuildingGenerator.BARREL;
        }
    }

    private static IBehavior<IActor> getDefaultBehavior(Team team) {
        if (team == Team.CIVILIAN) {
            return new PanicBehavior();
        } else {
            return new SimpleBehavior();
        }
    }

    public ITerrain[][] getTerrain(Map<Location, IItem> items, List<IPC> pcs) {
        IArea bigArea = config.bigArea;
        int delta = config.delta;
        int height = bigArea.getHeight();
        int width = bigArea.getWidth();
        ITerrain[][] terrain = new ITerrain[height][width];
        for (int y = 0; y < roads.length; y++) {
            boolean[] row = roads[y];
            for (int x = 0; x < row.length; x++) {
                int xt = x + delta;
                int yt = y + delta;
                short t = result[y][x];
                ITerrain defaultTerrain = row[x] ? Road.INSTANCE : Floor.INSTANCE;
                if (t < 0) {
                    ITerrain ter;
                    switch(t) {
                        case BuildingGenerator.GRASS:
                            ter = Grass.INSTANCE;
                            break;
                        case BuildingGenerator.TREE:
                            ter = Tree.INSTANCE;
                            break;
                        case BuildingGenerator.STATUE:
                            ter = Statue.INSTANCE;
                            break;
                        case BuildingGenerator.WALL:
                            ter = Wall.INSTANCE;
                            break;
                        case BuildingGenerator.CRATE:
                            ter = defaultTerrain;
                            items.put(new Location(xt, yt), new Crate());
                            break;
                        case BuildingGenerator.BARREL:
                            ter = defaultTerrain;
                            items.put(new Location(xt, yt), new Barrel());
                            break;
                        default:
                            ter = Floor.INSTANCE;
                            break;
                    }
                    terrain[yt][xt] = ter;
                } else {
                    terrain[yt][xt] = defaultTerrain;
                }
            }
        }
        for (int y = 0; y < terrain.length; y++) {
            for (int x = 0; x < terrain[y].length; x++) {
                if (terrain[y][x] == null) {
                    terrain[y][x] = Grass.INSTANCE;
                }
            }
        }
        {
            Team rashka = Team.RASHKA;
            IActor rashkin = new NPC(rashka, getDefaultBehavior(rashka));
            Location npcLocation = getRandomFreeFor(terrain, items, rashkin, 1000);
            if (npcLocation != null) {
                npc.add(npcLocation, rashka, 0);
            }
        }
        int npcCount = Math.max(width * height / 30, pcs.size() * 2);
        for (int i = 0; i < npcCount; i++) {
            Team team = npc.nextTeam(config.desiredProportion, rnd, Team.values());
            IActor newNPC = new NPC(team, getDefaultBehavior(team));
            Location npcLocation = getRandomFreeFor(terrain, items, newNPC, 20);
            if (npcLocation == null) break;
            npc.add(npcLocation, team, 0);
        }
        Iterable<NPCPlace> npcPlaces = npc.getNPC(config.population, rnd);
        for (NPCPlace place : npcPlaces) {
            Location at = place.at;
            if (bigArea.contains(at.x, at.y)) {
                items.put(at, new NPC(place.team, getDefaultBehavior(place.team)));
            }
        }
        if (widest != null) {
            Segment segment = widest.segments.get(0);
            int length = maxRoadWidth * 2 + 1;
            int i = 0;
            int pc = 0;
            while (pc < pcs.size()) {
                int row = i / length;
                int col = i % length;
                Segment cross = segment.cross(row * 2, maxRoadWidth);
                Location coord = cross.getAt(col);
                Location put = new Location(coord.x + delta, coord.y + delta);
                if (bigArea.contains(put.x, put.y)) {
                    items.put(put, pcs.get(pc++));
                }
                i++;
            }
        } else {
            while (true) {
                int x0 = rnd.nextInt(width);
                int y0 = rnd.nextInt(height);
                if (bigArea.contains(x0, y0)) {
                    int pc = 0;
                    while (pc < pcs.size()) {
                        int dx = rnd.nextInt(9) - 4;
                        int dy = rnd.nextInt(9) - 4;
                        int x1 = x0 + dx;
                        int y1 = y0 + dy;
                        IPC currentPC = pcs.get(pc);
                        Location putAt = isFreePlace(x1, y1, terrain, items, true, currentPC);
                        if (putAt != null) {
                            items.put(putAt, currentPC);
                            pc++;
                        }
                    }
                    break;
                }
            }
        }
        return terrain;
    }

    private Location getRandomFreeFor(ITerrain[][] terrain, Map<Location, IItem> items, IItem forItem, int maxTries) {
        IArea area = config.bigArea;
        int tries = 0;
        while (tries < maxTries) {
            int x = rnd.nextInt(area.getWidth());
            int y = rnd.nextInt(area.getHeight());
            Location putAt = isFreePlace(x, y, terrain, items, false, forItem);
            if (putAt != null) return putAt;
            tries++;
        }
        return null;
    }

    private Location isFreePlace(int x, int y, ITerrain[][] terrain, Map<Location, IItem> items, boolean allowOverwrite, IItem forItem) {
        if (config.bigArea.contains(x, y)) {
            if (!terrain[y][x].canStep(forItem)) return null;
            Location putAt = new Location(x, y);
            IItem under = items.get(putAt);
            if (under != null) {
                if (allowOverwrite) {
                    if (under instanceof IActor) {
                        return null;
                    } else {
                        return putAt;
                    }
                } else {
                    return null;
                }
            } else {
                return putAt;
            }
        } else {
            return null;
        }
    }
}
