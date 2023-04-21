package civquest.nation;

import civquest.Player;
import civquest.Science;
import civquest.city.City;
import civquest.core.Game;
import civquest.core.HasID;
import civquest.core.TimeRules;
import civquest.gameChange.GameChange;
import civquest.gameChange.GameChangeException;
import civquest.gameChange.GameChangeListener;
import civquest.gameChange.GameChangeManager;
import civquest.gameChange.RulesetGameChangeLoader;
import civquest.io.DataToSave;
import civquest.io.LoadedData;
import civquest.io.Messages;
import civquest.io.Persistent.LoadingStep;
import civquest.io.Persistent;
import civquest.map.MapData;
import civquest.map.gameChange.SetMapObjectTime;
import civquest.nation.gameChange.ActivateNation;
import civquest.nation.gameChange.NationChange;
import civquest.parser.ruleset.Registry;
import civquest.parser.ruleset.Ruleset;
import civquest.parser.ruleset.Section;
import civquest.parser.ruleset.exception.RulesetException;
import civquest.swing.CivQuest;
import civquest.units.Unit;
import civquest.units.gameChange.ResetUnitTime;
import civquest.units.gameChange.UnitChange;
import civquest.visibility.VisibilityManager;
import civquest.visibility.blackWhite.BlackWhiteVisibilityManager;
import civquest.visibility.reality.RealityVisibilityManager;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Nation implements GameChangeListener, HasID, Persistent {

    private Long id;

    private RestrictedToNation resToThisNation;

    /** Implements the visibility-concept used by the nation. */
    private VisibilityManager visibilityManager;

    private Registry nationRegistry;

    private Registry visibilityRegistry;

    /** The Player controlling the Nation */
    private Player player = null;

    private String name = "A nation";

    private int governmentType;

    private int treasury;

    private int science;

    private int tax;

    private int luxury;

    /** Cities the Nation owns - keys are the city-ids, values the City-objects */
    private Map<Long, City> cities = new HashMap<Long, City>();

    /** Units the nation owns - keys are the unit-ids, values the Unit-objects */
    private Map<Long, Unit> units = new HashMap<Long, Unit>();

    private static final String REALITY_VISIBILITY = "reality";

    private static final String BLACK_WHITE_VISIBILITY = "blackWhite";

    private static final String NO_VISIBILITY = "none";

    private LoadedData loadedData = null;

    /** Constructor for constructing a new nation in game, _not_ from savegame.
	 *  Place things which have to be done in both cases (in game, from savegame)
	 *  not here, but in method initCommonParts below.
	 */
    public Nation(String name, int governmentType, Registry nationRegistry, Registry visRegistry) throws RulesetException {
        id = Game.getGame().getNewID();
        resToThisNation = new RestrictedToNation(id);
        initCommonParts(nationRegistry, visRegistry);
        setName(name);
        setUpVisibilityManager();
    }

    /** See the commont of the other constructor.
	 */
    public Nation(Registry nationRegistry, Registry visRegistry, LoadedData data, LoadingStep step) throws Exception {
        initCommonParts(nationRegistry, visRegistry);
        this.loadedData = data;
        setPersistentData(step);
    }

    private void initCommonParts(Registry nationRegistry, Registry visRegistry) throws RulesetException {
        this.nationRegistry = nationRegistry;
        this.visibilityRegistry = visRegistry;
    }

    public Long getID() {
        return id;
    }

    public RestrictedToNation getResToThisNation() {
        return resToThisNation;
    }

    private String getWhichVisManager() throws RulesetException {
        Ruleset settingsRuleset = nationRegistry.getRuleset("settings");
        Section visSection = settingsRuleset.getSection("visibility");
        return visSection.getField("whichManager").getStringValue();
    }

    private void setUpVisibilityManager() throws RulesetException {
        String whichVisManager = getWhichVisManager();
        setUpVisibilityManager(whichVisManager);
    }

    private void setUpVisibilityManager(String whichVisManager) throws RulesetException {
        if (whichVisManager.equals(REALITY_VISIBILITY)) {
            setUpRealityVisManager();
        } else if (whichVisManager.equals(BLACK_WHITE_VISIBILITY)) {
            setUpBlackWhiteVisManager();
        } else {
            Messages messages = Messages.getMessages();
            messages.info("Nation", "GlobRuleConf", "FallDefault", whichVisManager + " is not a valid visibilityManager!");
            messages.info("Nation", "GlobRuleConf", "FallDefault", "Using BlackWhiteVisibilityManager instead.");
            setUpBlackWhiteVisManager();
        }
    }

    private void setUpRealityVisManager() throws RulesetException {
        Registry realityRegistry = visibilityRegistry.getSubRegistry("reality");
        this.visibilityManager = new RealityVisibilityManager(realityRegistry, this);
    }

    private void setUpBlackWhiteVisManager() throws RulesetException {
        Registry blackWhiteRegistry = visibilityRegistry.getSubRegistry("blackWhite");
        this.visibilityManager = new BlackWhiteVisibilityManager(blackWhiteRegistry, this);
    }

    public void setVisibilityManager(VisibilityManager manager) {
        this.visibilityManager = manager;
    }

    public VisibilityManager getVisibilityManager() {
        return visibilityManager;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    public void setName(String nation) {
        this.name = nation;
    }

    public String getName() {
        return name;
    }

    public void addCity(City city) {
        cities.put(city.getID(), city);
    }

    public void removeCity(City city) {
        cities.remove(city.getID());
    }

    public Iterator<City> getCityIterator() {
        return cities.values().iterator();
    }

    public String toString() {
        return name;
    }

    public Unit getUnit(long id) {
        return (Unit) (units.get(id));
    }

    public int getUnitCount() {
        return units.size();
    }

    public void addUnit(Unit unit) {
        units.put(unit.getID(), unit);
    }

    public void removeUnit(Unit unit) {
        units.remove(unit.getID());
    }

    public void removeUnit(long id) {
        units.remove(id);
    }

    public Iterator<Unit> getUnitIterator() {
        return units.values().iterator();
    }

    public long getCurrentNation() {
        return this.getID();
    }

    public RestrictedToNation[] getResToNations() {
        return new RestrictedToNation[] { new RestrictedToNation() };
    }

    public DataToSave getPersistentData() {
        DataToSave data = new DataToSave();
        data.put("id", id);
        data.put("whichVisManager", getVMEncodingFromInstance());
        data.put("name", name);
        data.put("visManager", visibilityManager);
        return data;
    }

    private String getVMEncodingFromInstance() {
        if (visibilityManager instanceof RealityVisibilityManager) {
            return REALITY_VISIBILITY;
        } else if (visibilityManager instanceof BlackWhiteVisibilityManager) {
            return BLACK_WHITE_VISIBILITY;
        } else {
            return NO_VISIBILITY;
        }
    }

    public void setPersistentData(LoadingStep step) throws Exception {
        if (step == LoadingStep.CONSTRUCT) {
            id = loadedData.getLong("id");
            resToThisNation = new RestrictedToNation(id);
            name = loadedData.getString("name");
            String whichVisManager = loadedData.getString("whichVisManager");
            LoadedData visData = loadedData.getSubData("visManager");
            if (whichVisManager.equals(REALITY_VISIBILITY)) {
                Registry realityRegistry = visibilityRegistry.getSubRegistry("reality");
                this.visibilityManager = new RealityVisibilityManager(realityRegistry, this);
            } else if (whichVisManager.equals(BLACK_WHITE_VISIBILITY)) {
                Registry blackWhiteRegistry = visibilityRegistry.getSubRegistry("blackWhite");
                this.visibilityManager = new BlackWhiteVisibilityManager(blackWhiteRegistry, this, visData, step);
            } else {
                assert false : "TODO: Throw exception!";
            }
        } else if (step == LoadingStep.REFERENCE) {
        }
        if (step != LoadingStep.CONSTRUCT) {
            visibilityManager.setPersistentData(step);
        }
    }
}
