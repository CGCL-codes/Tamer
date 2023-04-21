package net.slashie.serf.level;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import net.slashie.serf.action.Actor;
import net.slashie.serf.action.AwareActor;
import net.slashie.serf.action.EnvironmentInfo;
import net.slashie.serf.action.Message;
import net.slashie.serf.baseDomain.AbstractItem;
import net.slashie.serf.fov.FOVMap;
import net.slashie.serf.game.Player;
import net.slashie.serf.game.SworeGame;
import net.slashie.serf.ui.Appearance;
import net.slashie.serf.ui.Effect;
import net.slashie.serf.ui.UserInterface;
import net.slashie.utils.Counter;
import net.slashie.utils.Debug;
import net.slashie.utils.Position;
import net.slashie.utils.SZQueue;

public abstract class AbstractLevel implements FOVMap, Serializable {

    private String ID;

    private Unleasher[] unleashers = new Unleasher[] {};

    private List<AbstractFeature> features;

    private Map<String, List<AbstractFeature>> featuresMap = new HashMap<String, List<AbstractFeature>>();

    private Player player;

    private SZQueue messagesneffects;

    private Dispatcher dispatcher;

    private String description;

    private Map<String, List<AbstractItem>> items = new Hashtable<String, List<AbstractItem>>();

    private Map<String, Position> exits = new Hashtable<String, Position>();

    private Map<String, String> exitPositions = new Hashtable<String, String>();

    private Map<String, Counter> hashCounters = new Hashtable<String, Counter>();

    private List<AbstractFeature> doomedFeatures = new ArrayList<AbstractFeature>();

    private List<AbstractFeature> lightSources = new ArrayList<AbstractFeature>();

    public List<AbstractFeature> getFeatures() {
        return features;
    }

    public void addExit(Position where, String levelID) {
        exits.put(levelID, where);
        exitPositions.put(where.toString(), levelID);
    }

    public Position getExitFor(String levelID) {
        return (Position) exits.get(levelID);
    }

    public void addItem(Position where, AbstractItem what) {
        List<AbstractItem> stack = items.get(where.toString());
        if (stack == null) {
            stack = new ArrayList<AbstractItem>(5);
            items.put(where.toString(), stack);
        }
        stack.add(what);
    }

    public void removeItemFrom(AbstractItem what, Position where) {
        List<AbstractItem> stack = items.get(where.toString());
        if (stack != null) {
            stack.remove(what);
            if (stack.size() == 0) items.values().remove(stack);
        }
    }

    public List<AbstractItem> getItemsAt(Position where) {
        return items.get(where.toString());
    }

    public AbstractLevel() {
        features = new ArrayList<AbstractFeature>(20);
        messagesneffects = new SZQueue(50);
    }

    public void addMessage(Message what) {
        UserInterface.getUI().addMessage(what);
    }

    public void addMessage(String what) {
        addMessage(what, player.getPosition());
    }

    public void addMessage(String what, Position where) {
        addMessage(new Message(what, where, "-"));
    }

    public void addActor(Actor what) {
        Debug.doAssert(what != null, "Tried to add a null actor to the world");
        dispatcher.addActor(what, true);
        what.setLevel(this);
    }

    public void addActor(Actor what, Position where) {
        addActor(what);
        what.setPosition(where);
    }

    public void removeActor(Actor actor) {
        Debug.doAssert(actor != null, "Tried to remove a null actor to the world");
        dispatcher.removeActor(actor);
        actorPositionMap.remove(actor.getPosition().toString());
    }

    public void addEffect(Effect what) {
        UserInterface.getUI().drawEffect(what);
    }

    public SZQueue getMessagesAndEffects() {
        return messagesneffects;
    }

    public abstract AbstractCell getMapCell(int x, int y, int z);

    public List<AbstractFeature> getFeaturesAt(Position p) {
        return featuresMap.get(p.toString());
    }

    /**
	 * @deprecated Using only the first feature is unreliable, use getFeaturesAt instead
	 * @param p
	 * @return
	 */
    public AbstractFeature getFeatureAt(Position p) {
        for (int i = 0; i < features.size(); i++) {
            if (features.get(i).getPosition().equals(p)) {
                return features.get(i);
            }
        }
        return null;
    }

    Position tempFeaturePosition = new Position(0, 0);

    @Deprecated
    public AbstractFeature getFeatureAt(int x, int y, int z) {
        tempFeaturePosition.x = x;
        tempFeaturePosition.y = y;
        tempFeaturePosition.z = z;
        return getFeatureAt(tempFeaturePosition);
    }

    public Actor getActorAt(Position x) {
        return actorPositionMap.get(x.toString());
    }

    public void destroyFeature(AbstractFeature what) {
        if (lightSources.contains(what)) {
            lightSources.remove(what);
            lightAt(what.getPosition(), what.getLight(), false);
            for (int i = 0; i < lightSources.size(); i++) {
                AbstractFeature lightSource = lightSources.get(i);
                if (getDistance(what.getPosition(), lightSource.getPosition()) < 10) {
                    lightAt(lightSource.getPosition(), lightSource.getLight(), true);
                }
            }
        }
        features.remove(what);
        List<AbstractFeature> featuresList = featuresMap.get(what.getPosition().toString());
        featuresList.remove(what);
        if (featuresList.size() == 0) {
            featuresMap.remove(what.getPosition().toString());
        }
    }

    public AbstractCell getMapCell(Position where) {
        return getMapCell(where.x, where.y, where.z);
    }

    public boolean isWalkable(Position where) {
        return getMapCell(where) != null && !getMapCell(where).isSolid();
    }

    public boolean isItemPlaceable(Position where) {
        return isWalkable(where) && !getMapCell(where).isShallowWater() && !getMapCell(where).isWater() && !getMapCell(where).isEthereal();
    }

    public boolean isExitPlaceable(Position where) {
        return getMapCell(where) != null && !getMapCell(where).isSolid();
    }

    public abstract int getWidth();

    public abstract int getHeight();

    public abstract int getDepth();

    public void addFeature(AbstractFeature what, boolean doom) {
        features.add(what);
        List<AbstractFeature> positionList = featuresMap.get(what.getPosition().toString());
        if (positionList == null) {
            positionList = new ArrayList<AbstractFeature>();
            featuresMap.put(what.getPosition().toString(), positionList);
        }
        positionList.add(what);
        what.setLevel(this);
        if (doom && what.getFaint() > 0) {
            doomedFeatures.add(what);
        }
        if (what.getLight() > 0) {
            lightSources.add(what);
            lightAt(what.getPosition(), what.getLight(), true);
        }
        if (what.getSelector() != null) {
            addActor(what);
        }
    }

    public void addFeature(AbstractFeature what) {
        addFeature(what, false);
    }

    private Position lightRunner = new Position(0, 0);

    private void lightAt(Position where, int intensity, boolean light) {
        lightRunner.z = where.z;
        for (int x = where.x - intensity; x <= where.x + intensity; x++) {
            for (int y = where.y - intensity; y <= where.y + intensity; y++) {
                lightRunner.x = x;
                lightRunner.y = y;
                if (!isValidCoordinate(lightRunner)) continue;
                if (getDistance(where, lightRunner) <= intensity) {
                    if (light) markLit(x, y, where.z); else darken(x, y, where.z);
                }
            }
        }
    }

    public void addFeature(AbstractFeature feature, Position location) {
        feature.setPosition(location.x, location.y, location.z);
        addFeature(feature);
    }

    public void addFeature(String featureID, Position location) {
        AbstractFeature x = FeatureFactory.getFactory().buildFeature(featureID);
        x.setPosition(location.x, location.y, location.z);
        addFeature(x);
    }

    public void setPlayer(Player what) {
        player = what;
        if (!dispatcher.contains(what)) dispatcher.addActor(what, true);
        player.setLevel(this);
    }

    public EnvironmentInfo getEnvironmentAroundActor(AwareActor watcher, int x, int y, int z, int xspan, int yspan) {
        EnvironmentInfo ret = new EnvironmentInfo();
        int xstart = x - xspan;
        int ystart = y - yspan;
        int xend = x + xspan;
        int yend = y + yspan;
        AbstractCell[][] cellsAround = new AbstractCell[2 * xspan + 1][2 * yspan + 1];
        int px = 0;
        Position runner = new Position(0, 0);
        for (int ix = xstart; ix <= xend; ix++) {
            runner.x = ix;
            int py = 0;
            for (int iy = ystart; iy <= yend; iy++) {
                runner.y = iy;
                if (ix >= 0 && ix < getWidth() && iy >= 0 && iy < getHeight() && isVisible(ix, iy, z)) {
                    cellsAround[px][py] = getMapCell(ix, iy, z);
                    watcher.seeMapCell(cellsAround[px][py]);
                    if (isValidCoordinate(ix, iy, z) && (cellsAround[px][py] == null || cellsAround[px][py].getID().equals("AIR"))) {
                        int pz = z;
                        while (pz < getDepth() - 1) {
                            if (getMapCell(ix, iy, pz + 1) == null || getMapCell(ix, iy, pz + 1).getID().equals("AIR")) {
                                pz++;
                            } else {
                                cellsAround[px][py] = getMapCell(ix, iy, pz + 1);
                                watcher.seeMapCell(cellsAround[px][py]);
                                break;
                            }
                        }
                    }
                    List<AbstractFeature> feats = getFeaturesAt(runner);
                    if (feats != null) {
                        ret.addFeature(px - xspan, py - yspan, feats);
                    }
                    List<AbstractItem> items = getItemsAt(runner);
                    AbstractItem item = null;
                    if (items != null) {
                        item = items.get(0);
                    }
                    if (item != null) {
                        if (item.isVisible()) {
                            ret.addItem(px - xspan, py - yspan, item);
                        }
                    }
                    Actor actor = getActorAt(runner);
                    if (actor != player && actor != null && !actor.isInvisible()) {
                        ret.addActor(px - xspan, py - yspan, actor);
                    }
                }
                py++;
            }
            px++;
        }
        ret.setCellsAround(cellsAround);
        return ret;
    }

    public AbstractCell[][] getMemoryCellsAround(int x, int y, int z, int xspan, int yspan) {
        int xstart = x - xspan;
        int ystart = y - yspan;
        int xend = x + xspan;
        int yend = y + yspan;
        AbstractCell[][] ret = new AbstractCell[2 * xspan + 1][2 * yspan + 1];
        int px = 0;
        for (int ix = xstart; ix <= xend; ix++) {
            int py = 0;
            for (int iy = ystart; iy <= yend; iy++) {
                if (ix >= 0 && ix < getWidth() && iy >= 0 && iy < getHeight() && remembers(ix, iy, z)) {
                    ret[px][py] = getMapCell(ix, iy, z);
                    if (isValidCoordinate(ix, iy, z) && (ret[px][py] == null || ret[px][py].getID().equals("AIR"))) {
                        int pz = z;
                        while (pz < getDepth() - 1) {
                            if (getMapCell(ix, iy, pz + 1) == null || getMapCell(ix, iy, pz + 1).getID().equals("AIR")) {
                                pz++;
                            } else {
                                ret[px][py] = getMapCell(ix, iy, pz + 1);
                                break;
                            }
                        }
                    }
                }
                py++;
            }
            px++;
        }
        return ret;
    }

    public Actor getPlayer() {
        return player;
    }

    public void setDispatcher(Dispatcher value) {
        Debug.enterMethod(this, "setDispatcher", value);
        dispatcher = value;
        Debug.exitMethod();
    }

    private void validate(Position what) {
        if (what.x < 0) what.x = 0;
        if (what.y < 0) what.y = 0;
        if (what.x > getWidth() - 1) what.x = getWidth() - 1;
        if (what.y > getHeight() - 1) what.y = getHeight() - 1;
    }

    public boolean isValidCoordinate(Position what) {
        return isValidCoordinate(what.x, what.y, what.z);
    }

    public boolean isValidCoordinate(int x, int y) {
        return !(x < 0 || y < 0 || x > getWidth() - 1 || y > getHeight() - 1);
    }

    public boolean isValidCoordinate(int x, int y, int z) {
        return z >= 0 && z < getDepth() && isValidCoordinate(x, y);
    }

    public void updateLevelStatus() {
        if (hashCounters.size() > 0) {
            Collection<Counter> counters = hashCounters.values();
            for (Counter counter : counters) counter.reduce();
        }
        for (AbstractFeature feature : doomedFeatures) {
            feature.setFaint(feature.getFaint() - 1);
        }
        for (int i = 0; i < doomedFeatures.size(); i++) {
            AbstractFeature f = doomedFeatures.get(i);
            if (f.getFaint() <= 0) {
                doomedFeatures.remove(f);
                destroyFeature(f);
                i--;
            }
        }
    }

    public Dispatcher getDispatcher() {
        return dispatcher;
    }

    public void signal(Position center, int range, String message) {
        List<Actor> actors = dispatcher.getActors();
        for (Actor actor : actors) {
            if (getDistance(center, actor.getPosition()) <= range) actor.message(message);
        }
    }

    private List<Actor> tempActors;

    public void savePop() {
        tempActors = new ArrayList<Actor>(dispatcher.getActors());
    }

    public void loadPop() {
        for (Actor actor : tempActors) {
            dispatcher.addActor(actor);
        }
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    private Position LOSPosition = new Position(0, 0, 0);

    public boolean blockLOS(int x, int y) {
        LOSPosition.x = x;
        LOSPosition.y = y;
        LOSPosition.z = 0;
        if (!isValidCoordinate(x, y)) return true;
        List<AbstractFeature> feats = getFeaturesAt(LOSPosition);
        if (feats != null) for (AbstractFeature feat : feats) {
            if (feat != null && feat.isOpaque()) return true;
            if (feat != null) feat.onSeenByPlayer();
        }
        AbstractCell cell = getMapCell(x, y, player.getPosition().z);
        if (cell == null) return false; else return cell.isOpaque();
    }

    private Position tempSeen = new Position(0, 0);

    public void setSeen(int x, int y) {
        if (!isValidCoordinate(x, y)) return;
        tempSeen.x = x;
        tempSeen.y = y;
        tempSeen.z = player.getPosition().z;
        if (getDistance(tempSeen, player.getPosition()) <= player.getSightRangeInDots() || isLit(tempSeen)) {
            markVisible(x, y, player.getPosition().z);
            markRemembered(x, y, player.getPosition().z);
            Actor m = getActorAt(tempSeen);
            if (m != null) {
                m.setWasSeen(true);
            }
        }
    }

    public int getDistance(Position a, Position b) {
        return Position.distance(a, b);
    }

    public void darken() {
        for (int z = 0; z < getDepth(); z++) for (int x = 0; x < getWidth(); x++) for (int y = 0; y < getHeight(); y++) darken(x, y, z);
    }

    protected abstract void markRemembered(int x, int y, int z);

    protected abstract boolean remembers(int x, int y, int z);

    protected abstract void markLit(int x, int y, int z);

    protected abstract void darken(int x, int y, int z);

    protected abstract boolean isLit(Position p);

    protected abstract void markVisible(int x, int y, int z);

    public abstract boolean isVisible(int x, int y, int z);

    private Map<String, Boolean> hashFlags = new Hashtable<String, Boolean>();

    public void setFlag(String flagID, boolean value) {
        hashFlags.remove(flagID);
        hashFlags.put(flagID, new Boolean(value));
    }

    public boolean getFlag(String flagID) {
        Boolean flag = (Boolean) hashFlags.get(flagID);
        if (flag == null || !flag.booleanValue()) return false; else return true;
    }

    public void setUnleashers(Unleasher[] pUnleashers) {
        unleashers = pUnleashers;
    }

    public void checkUnleashers(SworeGame game) {
        for (int i = 0; i < unleashers.length; i++) {
            if (unleashers[i].enabled()) unleashers[i].unleash(this, game);
        }
    }

    public void disableTriggers() {
        for (int i = 0; i < unleashers.length; i++) {
            unleashers[i].disable();
        }
    }

    public String getID() {
        return ID;
    }

    public void setID(String id) {
        ID = id;
    }

    public boolean isExit(Position pos) {
        return getExitOn(pos) != null;
    }

    public String getExitOn(Position pos) {
        return (String) exitPositions.get(pos.toString());
    }

    public Counter getCounter(String id) {
        return (Counter) hashCounters.get(id);
    }

    public void addCounter(String id, int count) {
        hashCounters.put(id, new Counter(count));
    }

    public void removeCounter(String id) {
        hashCounters.remove(id);
    }

    public void removeExit(String exitID) {
        Position where = (Position) exits.get(exitID);
        exitPositions.remove(where.toString());
        exits.remove(exitID);
    }

    public boolean isSolid(Position where) {
        return getMapCell(where) == null || getMapCell(where).isSolid() || areSolidFeaturesAt(where);
    }

    private boolean areSolidFeaturesAt(Position where) {
        List<AbstractFeature> features = getFeaturesAt(where);
        if (features == null) return false; else {
            for (AbstractFeature feature : features) {
                if (feature.isSolid()) return true;
            }
            return false;
        }
    }

    public void lightLights() {
        for (AbstractFeature feature : lightSources) {
            lightAt(feature.getPosition(), feature.getLight(), true);
        }
    }

    private List<AbstractFeature> featureDestructionSchedule = new ArrayList<AbstractFeature>();

    public void scheduleFeatureDestruction(AbstractFeature feature) {
        featureDestructionSchedule.add(feature);
    }

    public void scheduledTasks() {
        for (AbstractFeature feature : featureDestructionSchedule) {
            destroyFeature(feature);
        }
    }

    private Map<String, Actor> actorPositionMap = new HashMap<String, Actor>();

    public void updateActorPosition(Actor actor, int x, int y, int z) {
        actorPositionMap.remove(actor.getPosition().toString());
        actorPositionMap.put(Position.toString(x, y, z), actor);
    }

    public void dropActorOnPosition(Actor actor, Position p) {
        actorPositionMap.put(Position.toString(p.x, p.y, p.z), actor);
    }

    public void updateActorPosition(Actor actor, Position p) {
        updateActorPosition(actor, p.x, p.y, p.z);
    }

    public Appearance filterAppearance(Appearance appearance) {
        return appearance;
    }

    public List<Actor> getActors() {
        return dispatcher.getActors();
    }

    public boolean canMoveAround(Position p) {
        return isValidCoordinate(p) && isValidCoordinate(p.x + 1, p.y) && !isSolid(p) && !isSolid(p.x + 1, p.y, p.z);
    }

    private boolean isSolid(int x, int y, int z) {
        return getMapCell(x, y, z) == null || getMapCell(x, y, z).isSolid();
    }

    public boolean isPersistent() {
        return true;
    }
}
