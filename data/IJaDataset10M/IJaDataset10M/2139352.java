package massim.map;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ObservationMap {

    int sizeX;

    int sizeY;

    Map<Integer, LayerMap<Observation>> maps;

    Map<Integer, Map<Integer, Observation>> lastObservations;

    LayerMap<Boolean> exploredMap;

    public ObservationMap(int x, int y) {
        sizeX = x;
        sizeY = y;
        maps = new HashMap<Integer, LayerMap<Observation>>();
        lastObservations = new HashMap<Integer, Map<Integer, Observation>>(sizeX * sizeY);
        exploredMap = new LayerMap<Boolean>(x, y);
        exploredMap.initialize(false);
    }

    public LayerMap<Boolean> getExploredMap() {
        return exploredMap;
    }

    public void setExploredMap(LayerMap<Boolean> map) {
        this.exploredMap = map;
    }

    private void clearObservation(Observation observation) {
        LayerMap<Observation> map = maps.get(observation.getObjectType());
        map.clear(observation.x, observation.y, observation);
        if (observation.getObjectID() > 0) {
            Map<Integer, Observation> lastObservation = lastObservations.get(observation.getObjectType());
            lastObservation.remove(observation.getObjectID());
        }
    }

    public synchronized void addObservation(Observation observation) {
        if (observation.getObjectType() == Observation.TYPE_FREE_SPACE) {
            Observation obs = this.getObservation(observation.x, observation.y);
            if (obs != null) {
                this.clearObservation(obs);
            }
        } else {
            LayerMap<Observation> map = maps.get(observation.getObjectType());
            if (map == null) {
                map = new LayerMap<Observation>(sizeX, sizeY);
                maps.put(observation.getObjectType(), map);
            }
            map.put(observation.x, observation.y, observation);
            if (observation.getObjectID() > 0) {
                Map<Integer, Observation> lastObservation = lastObservations.get(observation.getObjectType());
                if (lastObservation == null) {
                    lastObservation = new HashMap<Integer, Observation>();
                    lastObservations.put(observation.getObjectType(), lastObservation);
                }
                Observation obs = lastObservation.put(observation.getObjectID(), observation);
                if (obs != null) {
                    if (obs.timeStep < observation.timeStep) map.clear(obs.x, obs.y, obs);
                }
            }
        }
        exploredMap.put(observation.x, observation.y, true);
    }

    public boolean isOccupied(int x, int y) {
        Observation obs = getObservation(x, y);
        if (obs == null) return false;
        return ((obs.getObjectType() == Observation.TYPE_OBSTACLE) || (obs.getObjectType() == Observation.TYPE_SWITCH));
    }

    public synchronized void clearObservations(int type) {
        List<Observation> observations = getObservationsOfType(type);
        int i = 0;
        while (i < observations.size()) {
            Observation observation = observations.get(i++);
            if (lastObservations.get(observation.getObjectType()) != null) {
                lastObservations.get(observation.getObjectType()).remove(observation.getObjectID());
            }
        }
        maps.put(type, new LayerMap<Observation>(sizeX, sizeY));
    }

    public synchronized Observation getObservation(int x, int y) {
        for (Integer key : maps.keySet()) {
            LayerMap<Observation> map = maps.get(key);
            Cell<Observation> cell = map.get(x, y);
            if (cell != null) return cell.value;
        }
        return null;
    }

    public Observation getObservation(int type, int x, int y) {
        LayerMap<Observation> map = maps.get(type);
        if (map != null) {
            Cell<Observation> cell = map.get(x, y);
            if (cell != null) return cell.value;
        }
        return null;
    }

    public Cell<Observation> getCell(int type, int x, int y) {
        LayerMap<Observation> map = maps.get(type);
        if (map != null) {
            return map.get(x, y);
        }
        return null;
    }

    public synchronized List<Observation> getObservationsOfType(int type) {
        LayerMap<Observation> map = maps.get(type);
        if (map == null) return new LinkedList<Observation>();
        return map.getValues();
    }

    public boolean isExplored(int x, int y) {
        Cell<Boolean> cell = exploredMap.get(x, y);
        if (cell == null) return false;
        return cell.value;
    }

    public FloodedMap getFloodedMap(int tx, int ty, boolean bUseSQRT, boolean bIgnoreFences) {
        return new FloodedMap(this, tx, ty, bUseSQRT, bIgnoreFences);
    }

    public List<String> objectCounts() {
        List<String> list = new ArrayList<String>(10);
        for (Integer key : maps.keySet()) {
            LayerMap<Observation> map = maps.get(key);
            list.add(key + ": " + map.getValues().size());
        }
        return list;
    }

    public int getSizeX() {
        return sizeX;
    }

    public int getSizeY() {
        return sizeY;
    }
}
