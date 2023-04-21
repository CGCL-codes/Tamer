package data.controls;

import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Map;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import data.Group;
import data.Planet;
import data.Production;
import data.ShipType;
import data.constants.GalaxyConstants;
import data.constants.RaceState;
import ogv.OGV;
import ogv.OGV.PlanetInfoMacros;
import util.Evaluator;
import util.TimeStat;
import util.Utils;

public class Macros {

    private static Game game = null;

    private static Evaluator evaluator = null;

    private Macros() {
    }

    public static void init() {
        game = OGV.getGame();
    }

    public static Map<Planet, String> getPlanetInfo(PlanetInfoMacros m) {
        switch(m) {
            default:
            case NONE:
                return Collections.emptyMap();
            case PRODUCTION:
                return planetProduction();
            case SPIES:
                return spies();
            case ATTACKS_SHIPS:
                return attacksShips();
            case COL_CAP:
                return colCap();
            case SHIP_MASS:
                return shipMass();
            case CUSTOM:
                return custom();
        }
    }

    private static Map<Planet, String> planetProduction() {
        Map<Planet, String> hm = new IdentityHashMap<Planet, String>();
        for (Planet p : game.getPlanets()) {
            String s = planetProduction(p);
            if (s != null) hm.put(p, s);
        }
        return hm;
    }

    private static String planetProduction(Planet p) {
        if (p.isUnidentified() || p.isUninhabited()) return null;
        Production prod = p.getProducing();
        if (prod == null) return null;
        ShipType t = prod.getShipType();
        if (t != null) return shipTypeDescription(t);
        return prod.toShortString();
    }

    private static String shipTypeDescription(ShipType t) {
        if (t.getDrive() == 0) {
            if (t.getWeapons() == 0 && t.getCargo() == 0) return "stone";
            if (t.getWeapons() == 0) return "otransport";
            if (t.getAttacks() > 30 && t.getWeapons() < 4) return "operf";
            if (t.getAttacks() > 2 && t.getWeapons() >= 4) return "otur";
            if (t.getWeapons() > 15) return "odulo";
            if (t.getShields() > 30) return "oneproba";
            if (t.getWeapons() < 2 && t.getAttacks() == 1 && t.getShields() == 0 && t.getCargo() == 0) return "minidulo";
            return "obattle";
        } else if (t.getWeapons() == 0) {
            if (t.getCargo() == 0) {
                if (t.getShields() == 0) return "drone"; else return "hdrone";
            } else if (t.getCargo() > 1) return "transport"; else return "colonizer";
        }
        if (t.getAttacks() > 30 && t.getWeapons() < 4) return "perf";
        if (t.getAttacks() > 2 && t.getWeapons() >= 4) return "tur";
        if (t.getWeapons() > 15) return "dulo";
        if (t.getShields() > 30 && t.getCargo() < 2) return "neproba";
        if (t.getCargo() > 2) return "transport";
        return "battle";
    }

    private static Map<Planet, String> spies() {
        Map<Planet, Boolean> spy = new IdentityHashMap<Planet, Boolean>();
        for (Group g : game.getGroups()) {
            if (g.isYour() || g.getOwner().getRelations() == RaceState.PEACE) continue;
            Planet p = g.getDestination();
            if (p != null && p.isYour()) spy.put(p, Boolean.TRUE);
        }
        Map<Planet, String> hm = new IdentityHashMap<Planet, String>();
        for (Planet p : spy.keySet()) hm.put(p, "spy");
        return hm;
    }

    private static Map<Planet, String> attacksShips() {
        Map<Planet, long[]> sums = new IdentityHashMap<Planet, long[]>();
        for (Group g : game.getGroups()) {
            Planet p = g.getLocation();
            if (p == null) continue;
            long[] sum = sums.get(p);
            if (sum == null) {
                sum = new long[2];
                sums.put(p, sum);
            }
            int count = g.getSize();
            sum[0] += (long) g.getAttacks() * count;
            sum[1] += count;
        }
        Map<Planet, String> hm = new IdentityHashMap<Planet, String>();
        for (Map.Entry<Planet, long[]> entry : sums.entrySet()) {
            long[] sum = entry.getValue();
            hm.put(entry.getKey(), sum[0] + "/" + sum[1]);
        }
        return hm;
    }

    private static Map<Planet, String> colCap() {
        Map<Planet, String> hm = new IdentityHashMap<Planet, String>();
        for (Planet p : game.getPlanets()) {
            String s = colCap(p);
            if (s != null) hm.put(p, s);
        }
        return hm;
    }

    private static String colCap(Planet p) {
        if (p.isUnidentified() || p.isUninhabited()) return null;
        double col = (p.getPopulation() - p.getSize()) / GalaxyConstants.COL_PACK + p.getColonists();
        double cap = p.getIndustry() - p.getSize() + p.getCapital();
        String s = Utils.d0(col) + '/' + Utils.d0(cap);
        if ("0/0".equals(s)) return null;
        return s;
    }

    private static Map<Planet, String> shipMass() {
        Map<Planet, double[]> sums = new IdentityHashMap<Planet, double[]>();
        for (Group g : game.getGroups()) {
            Planet p = g.getLocation();
            if (p == null) continue;
            double[] sum = sums.get(p);
            if (sum == null) {
                sum = new double[3];
                sums.put(p, sum);
            }
            if (g.isYour()) sum[0] += g.getMass() * g.getSize(); else if (g.getOwner().getRelations() == RaceState.PEACE) sum[1] += g.getMass() * g.getSize(); else sum[2] += g.getMass() * g.getSize();
        }
        Map<Planet, String> hm = new IdentityHashMap<Planet, String>();
        for (Map.Entry<Planet, double[]> entry : sums.entrySet()) {
            double[] sum = entry.getValue();
            String s = Utils.d0(sum[0]) + '/' + Utils.d0(sum[1]) + '/' + Utils.d0(sum[2]);
            if (!"0/0/0".equals(s)) hm.put(entry.getKey(), s);
        }
        return hm;
    }

    private static final TimeStat ts = new TimeStat("CustomPlanetInfoMacros");

    private static Map<Planet, String> custom() {
        Map<Planet, String> hm = new IdentityHashMap<Planet, String>();
        ts.start();
        initEngine();
        for (Planet p : game.getPlanets()) {
            String s = custom(p);
            if (s != null) hm.put(p, s);
        }
        ts.stop();
        return hm;
    }

    private static String custom(Planet p) {
        if (evaluator == null) return null;
        Object res = evaluator.getValue(p);
        return res == null ? null : res.toString();
    }

    private static void initEngine() {
        ScriptEngineManager mgr = new ScriptEngineManager();
        ScriptEngine engine = mgr.getEngineByName("JavaScript");
        String script = OGV.getCustomPlanetInfoMacros();
        evaluator = Evaluator.create(script, Planet.class, engine);
    }
}
