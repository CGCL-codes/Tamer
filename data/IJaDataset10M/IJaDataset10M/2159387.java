package traviaut.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import traviaut.ServerConnect;
import traviaut.ServerConnect.Tribe;

public class BuildInfo implements BuildNames {

    public static final int RESS_CNT = 19;

    public static final int BUILD_CNT = BuildDB.getBuildCnt();

    public static final int MENU_SHIFT = 5;

    private int resslayout;

    private final int[] mapBuildToGID = new int[BUILD_CNT - RESS_CNT + 1];

    private final Building[] buildings = new Building[BUILD_CNT];

    private boolean mainVillage;

    private boolean ressDone;

    private boolean currentRes;

    private boolean currentBuild;

    private final VillageParser parser;

    public BuildInfo(VillageParser p) {
        parser = p;
        Arrays.fill(buildings, new Building());
    }

    public boolean isCurrentBuild() {
        return currentBuild;
    }

    public boolean isCurrentRes() {
        return currentRes;
    }

    public boolean oneBuild(ServerConnect server) {
        Tribe tr = server.getTribe();
        return (tr == Tribe.GAUL) || (tr == Tribe.TEUTON);
    }

    public boolean canBuild() {
        ServerConnect server = parser.getServer();
        boolean onlyOne = oneBuild(server);
        int cur = currentBuild ? 1 : 0;
        if (currentRes || ressDone) cur += 1;
        if (cur == 2) return false;
        if (onlyOne && (currentBuild || currentRes)) return false;
        return true;
    }

    public int[] getNames(List<String> names) {
        int[] mapToID = new int[buildings.length];
        names.add("build this village");
        names.add("min level building");
        names.add("build resources");
        names.add("trade");
        names.add("supply negative crop");
        int pos = 0;
        for (Building b : buildings) {
            if (b.isMaxLvl(mainVillage)) continue;
            names.add(b.getName());
            mapToID[pos++] = b.id;
        }
        return mapToID;
    }

    public boolean doSelect() {
        return true;
    }

    public boolean[] getSelected(int[] mapToID) {
        return parser.getBuildParser().getSelected(mapToID);
    }

    public void setSelected(boolean[] sel, int[] mapToID) {
        parser.getBuildParser().setSelected(sel, mapToID);
    }

    public List<String> getAdeptsNames(Set<Integer> ids, Stock ress) {
        List<String> ret = new ArrayList<String>();
        for (int id : ids) {
            String n = buildings[id].getName();
            if (!buildings[id].checkCost(ress)) n += " XXX";
            ret.add(n);
        }
        return ret;
    }

    public Stock getNextCost(int id) {
        return buildings[id].getCost(true);
    }

    public boolean checkCost(int id, Stock ress) {
        return buildings[id].checkCost(ress);
    }

    public boolean checkFree(int id) {
        if (id < RESS_CNT) {
            if (currentRes) return false;
        } else {
            if (currentBuild) return false;
        }
        return true;
    }

    public Set<Integer> filterFreeSlot(Set<Integer> set) {
        Set<Integer> ret = new LinkedHashSet<Integer>();
        for (int i : set) {
            if (checkFree(i)) ret.add(i);
        }
        return ret;
    }

    public Set<Integer> filterCost(Set<Integer> set, Stock ress) {
        Set<Integer> ret = new LinkedHashSet<Integer>();
        for (int i : set) {
            if (buildings[i].checkCost(ress)) ret.add(i);
        }
        return ret;
    }

    public Set<Integer> filterMaxLvl(Set<Integer> set) {
        Set<Integer> ret = new LinkedHashSet<Integer>();
        for (int i : set) {
            if (!buildings[i].isMaxLvl(mainVillage)) ret.add(i);
        }
        return ret;
    }

    public int getMinRess(int gid, int maxlvl, boolean any, boolean[] allowed) {
        int min = -1;
        int minlvl = 50;
        for (int i = 1; i < RESS_CNT; i++) {
            Building b = buildings[i];
            if (b.isMaxLvl(mainVillage) || !allowed[i]) continue;
            if (any) {
                if (b.gid > gid) continue;
            } else {
                if (b.gid != gid) continue;
            }
            if (maxlvl > 0 && maxlvl < b.lvl) continue;
            if (minlvl > b.lvl) {
                minlvl = b.lvl;
                min = i;
            }
        }
        return min;
    }

    public int getMinBuild(boolean[] allowed, Stock ress) {
        int min = -1;
        int minlvl = 50;
        for (int i = RESS_CNT; i < BUILD_CNT; i++) {
            Building b = buildings[i];
            if (b.isMaxLvl(mainVillage) || !allowed[i]) continue;
            if (!b.checkCost(ress)) continue;
            if (minlvl > b.lvl) {
                minlvl = b.lvl;
                min = i;
            }
        }
        return min;
    }

    public void addBuildID(int gid, int maxlvl, Set<Integer> set) {
        for (Building b : buildings) {
            if (b.isMaxLvl(mainVillage) || (b.gid != gid)) continue;
            if (maxlvl > 0 && maxlvl <= b.lvl) continue;
            set.add(b.id);
        }
    }

    public void readDorf1(Parser p) {
        resslayout = getLayout(p);
        readBuildings(p);
        scanRess();
    }

    private int getLayout(Parser p) {
        String attrname = parser.getServer().getVHandler().getMapLayoutAttrName();
        for (int i = 1; i <= BuildDB.getLayoutCnt(); i++) {
            if (p.findElem("div", attrname, "f" + i) != null) return i - 1;
        }
        return -1;
    }

    private void scanRess() {
        mainVillage = false;
        ressDone = true;
        for (int i = 1; i < RESS_CNT; i++) {
            int lvl = buildings[i].lvl;
            if (lvl > 10) mainVillage = true;
            if (lvl != 10) ressDone = false;
        }
    }

    public void readDorf2(Parser p, ServerConnect sc) {
        fillBuildMap(p, sc);
        readBuildings(p);
        if (ServerConnect.local) printBuildings();
    }

    private void readBuildings(Parser p) {
        if (resslayout < 0) return;
        readMap(p, "rx");
        readMap(p, "map1");
        readMap(p, "map2");
    }

    private void readMap(Parser p, String name) {
        Element map = p.findElem("map", "name", name);
        if (map == null) return;
        NodeList builds = Parser.tags(map, "area");
        for (int i = 0; i < builds.getLength(); i++) {
            Element b = (Element) builds.item(i);
            String strid = b.getAttribute("href");
            int idx = strid.lastIndexOf('=');
            if (idx < 0) continue;
            strid = strid.substring(idx + 1);
            int id = Integer.parseInt(strid);
            String title = b.getAttribute("title");
            buildings[id] = parseBuilding(id, title);
        }
    }

    private Building parseBuilding(int id, String title) {
        if (title == null) return new Building();
        String[] p = title.split(" ");
        int l = p.length;
        if (l < 2) return new Building();
        String lvlstr = p[l - 1];
        try {
            int lvl = Integer.parseInt(lvlstr);
            StringBuilder name = new StringBuilder();
            for (int j = 0; j < l - 2; j++) {
                if (name.length() != 0) name.append(" ");
                name.append(p[j]);
            }
            int gid = getGID(id);
            return new Building(name.toString(), id, gid, lvl);
        } catch (NumberFormatException nfe) {
            return new Building();
        }
    }

    private void fillBuildMap(Parser p, ServerConnect sc) {
        sc.getVHandler().fillBuildMapID(p, mapBuildToGID);
        Element el = p.findElem("img", "src", "img/un/g/g40.gif");
        if (el != null) {
            mapBuildToGID[8] = 40;
        }
        mapBuildToGID[21] = 16;
        mapBuildToGID[22] = parser.server.getTribe().getWallID();
    }

    public void applyCurrentBuild(String name) {
        if (name.length() == 0) return;
        int idx = name.indexOf('(');
        String build = name.substring(0, idx - 1);
        int idx2 = name.indexOf(')');
        String lvlstr = name.substring(idx, idx2);
        lvlstr = lvlstr.split(" ")[1];
        int lvl = Integer.parseInt(lvlstr) - 1;
        for (Building b : buildings) {
            if (b.applyCurrent(build, lvl)) {
                currentID(b.id);
                break;
            }
        }
    }

    public void currentID(int id) {
        if (id < RESS_CNT) currentRes = true; else currentBuild = true;
    }

    public int getGID(int id) {
        int i = id;
        if (id < RESS_CNT) return BuildDB.getRessGid(resslayout, i - 1);
        i = id - RESS_CNT + 1;
        return mapBuildToGID[i];
    }

    public Building getBuildID(int id) {
        return buildings[id];
    }

    public void printBuildings() {
        for (Building b : buildings) {
            if (b.isMaxLvl(mainVillage)) continue;
            String n = b.getBuildDB().name;
            System.out.println(b.id + " " + b.getName() + " " + n);
        }
    }
}
