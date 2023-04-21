package l1j.server.server.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Logger;
import l1j.server.Config;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1PetInstance;
import l1j.server.server.model.Instance.L1SummonInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.map.L1Map;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.serverpackets.ServerBasePacket;
import l1j.server.server.types.Point;

public class L1World {

    private static Logger _log = Logger.getLogger(L1World.class.getName());

    private final ConcurrentHashMap<String, L1PcInstance> _allPlayers;

    private final ConcurrentHashMap<Integer, L1NpcInstance> _allNpc;

    private final ConcurrentHashMap<Integer, L1PetInstance> _allPets;

    private final ConcurrentHashMap<Integer, L1SummonInstance> _allSummons;

    private final ConcurrentHashMap<Integer, L1Object> _allObjects;

    private final ConcurrentHashMap<Integer, L1Object>[] _visibleObjects;

    private final CopyOnWriteArrayList<L1War> _allWars;

    private final ConcurrentHashMap<String, L1Clan> _allClans;

    private int _weather = 4;

    private boolean _worldChatEnabled = true;

    private boolean _processingContributionTotal = false;

    private static final int MAX_MAP_ID = 8105;

    private static L1World _instance;

    private L1PetRace _PetRace = null;

    private L1PetMember _PetMember = null;

    private L1PetRaceEnd _PetRaceEnd = null;

    private L1World() {
        _allPlayers = new ConcurrentHashMap<String, L1PcInstance>();
        _allNpc = new ConcurrentHashMap<Integer, L1NpcInstance>();
        _allPets = new ConcurrentHashMap<Integer, L1PetInstance>();
        _allSummons = new ConcurrentHashMap<Integer, L1SummonInstance>();
        _allObjects = new ConcurrentHashMap<Integer, L1Object>();
        _visibleObjects = new ConcurrentHashMap[MAX_MAP_ID + 1];
        _allWars = new CopyOnWriteArrayList<L1War>();
        _allClans = new ConcurrentHashMap<String, L1Clan>();
        for (int i = 0; i <= MAX_MAP_ID; i++) {
            _visibleObjects[i] = new ConcurrentHashMap<Integer, L1Object>();
        }
    }

    public static L1World getInstance() {
        if (_instance == null) {
            _instance = new L1World();
        }
        return _instance;
    }

    /**
	 * ��� ���¸� Ŭ���� �Ѵ�.<br>
	 * �����, �׽�Ʈ���� Ư���� ���� �ܷ̿� ȣ���ؼ��� �� �ȴ�.
	 */
    public void clear() {
        _instance = new L1World();
    }

    public void storeObject(L1Object object) {
        if (object == null) {
            throw new NullPointerException();
        }
        _allObjects.put(object.getId(), object);
        if (object instanceof L1PcInstance) {
            _allPlayers.put(((L1PcInstance) object).getName(), (L1PcInstance) object);
        }
        if (object instanceof L1PetInstance) {
            _allPets.put(object.getId(), (L1PetInstance) object);
        }
        if (object instanceof L1NpcInstance) {
            _allNpc.put(((L1NpcInstance) object).getNpcTemplate().get_npcId(), (L1NpcInstance) object);
        }
        if (object instanceof L1SummonInstance) {
            _allSummons.put(object.getId(), (L1SummonInstance) object);
        }
    }

    public void removeObject(L1Object object) {
        if (object == null) {
            throw new NullPointerException();
        }
        _allObjects.remove(object.getId());
        if (object instanceof L1PcInstance) {
            _allPlayers.remove(((L1PcInstance) object).getName());
        }
        if (object instanceof L1PetInstance) {
            _allPets.remove(object.getId());
        }
        if (object instanceof L1NpcInstance) {
            _allNpc.remove(((L1NpcInstance) object).getNpcTemplate().get_npcId());
        }
        if (object instanceof L1SummonInstance) {
            _allSummons.remove(object.getId());
        }
    }

    public L1Object findObject(int oID) {
        return _allObjects.get(oID);
    }

    public L1Object[] getObject2() {
        return _allObjects.values().toArray(new L1Object[_allObjects.size()]);
    }

    public L1NpcInstance findNpc(int id) {
        return _allNpc.get(id);
    }

    private Collection<L1Object> _allValues;

    public Collection<L1Object> getObject() {
        Collection<L1Object> vs = _allValues;
        return (vs != null) ? vs : (_allValues = Collections.unmodifiableCollection(_allObjects.values()));
    }

    public L1GroundInventory getInventory(int x, int y, short map) {
        int inventoryKey = ((x - 30000) * 10000 + (y - 30000)) * -1;
        Object object = _visibleObjects[map].get(inventoryKey);
        if (object == null) {
            return new L1GroundInventory(inventoryKey, x, y, map);
        } else {
            return (L1GroundInventory) object;
        }
    }

    public L1GroundInventory getInventory(L1Location loc) {
        return getInventory(loc.getX(), loc.getY(), (short) loc.getMap().getId());
    }

    public void addVisibleObject(L1Object object) {
        if (object.getMapId() <= MAX_MAP_ID) {
            _visibleObjects[object.getMapId()].put(object.getId(), object);
        }
    }

    public void removeVisibleObject(L1Object object) {
        if (object.getMapId() <= MAX_MAP_ID) {
            _visibleObjects[object.getMapId()].remove(object.getId());
        }
    }

    public void moveVisibleObject(L1Object object, int newMap) {
        if (object.getMapId() != newMap) {
            if (object.getMapId() <= MAX_MAP_ID) {
                _visibleObjects[object.getMapId()].remove(object.getId());
            }
            if (newMap <= MAX_MAP_ID) {
                _visibleObjects[newMap].put(object.getId(), object);
            }
        }
    }

    private ConcurrentHashMap<Integer, Integer> createLineMap(Point src, Point target) {
        ConcurrentHashMap<Integer, Integer> lineMap = new ConcurrentHashMap<Integer, Integer>();
        int E;
        int x;
        int y;
        int key;
        int i;
        int x0 = src.getX();
        int y0 = src.getY();
        int x1 = target.getX();
        int y1 = target.getY();
        int sx = (x1 > x0) ? 1 : -1;
        int dx = (x1 > x0) ? x1 - x0 : x0 - x1;
        int sy = (y1 > y0) ? 1 : -1;
        int dy = (y1 > y0) ? y1 - y0 : y0 - y1;
        x = x0;
        y = y0;
        if (dx >= dy) {
            E = -dx;
            for (i = 0; i <= dx; i++) {
                key = (x << 16) + y;
                lineMap.put(key, key);
                x += sx;
                E += 2 * dy;
                if (E >= 0) {
                    y += sy;
                    E -= 2 * dx;
                }
            }
        } else {
            E = -dy;
            for (i = 0; i <= dy; i++) {
                key = (x << 16) + y;
                lineMap.put(key, key);
                y += sy;
                E += 2 * dx;
                if (E >= 0) {
                    x += sx;
                    E -= 2 * dy;
                }
            }
        }
        return lineMap;
    }

    public ArrayList<L1Object> getVisibleLineObjects(L1Object src, L1Object target) {
        ConcurrentHashMap<Integer, Integer> lineMap = createLineMap(src.getLocation(), target.getLocation());
        int map = target.getMapId();
        ArrayList<L1Object> result = new ArrayList<L1Object>();
        if (map <= MAX_MAP_ID) {
            for (L1Object element : _visibleObjects[map].values()) {
                if (element.equals(src)) {
                    continue;
                }
                int key = (element.getX() << 16) + element.getY();
                if (lineMap.containsKey(key)) {
                    result.add(element);
                }
            }
        }
        return result;
    }

    public ArrayList<L1Object> getVisibleBoxObjects(L1Object object, int heading, int width, int height) {
        int x = object.getX();
        int y = object.getY();
        int map = object.getMapId();
        L1Location location = object.getLocation();
        ArrayList<L1Object> result = new ArrayList<L1Object>();
        int headingRotate[] = { 6, 7, 0, 1, 2, 3, 4, 5 };
        double cosSita = Math.cos(headingRotate[heading] * Math.PI / 4);
        double sinSita = Math.sin(headingRotate[heading] * Math.PI / 4);
        if (map <= MAX_MAP_ID) {
            for (L1Object element : _visibleObjects[map].values()) {
                if (element.equals(object)) {
                    continue;
                }
                if (map != element.getMapId()) {
                    continue;
                }
                if (location.isSamePoint(element.getLocation())) {
                    result.add(element);
                    continue;
                }
                int distance = location.getTileLineDistance(element.getLocation());
                if (distance > height && distance > width) {
                    continue;
                }
                int x1 = element.getX() - x;
                int y1 = element.getY() - y;
                int rotX = (int) Math.round(x1 * cosSita + y1 * sinSita);
                int rotY = (int) Math.round(-x1 * sinSita + y1 * cosSita);
                int xmin = 0;
                int xmax = height;
                int ymin = -width;
                int ymax = width;
                if (rotX > xmin && distance <= xmax && rotY >= ymin && rotY <= ymax) {
                    result.add(element);
                }
            }
        }
        return result;
    }

    public ArrayList<L1Object> getVisibleObjects(L1Object object) {
        return getVisibleObjects(object, -1);
    }

    public ArrayList<L1Object> getVisibleObjects(L1Object object, int radius) {
        L1Map map = object.getMap();
        Point pt = object.getLocation();
        ArrayList<L1Object> result = new ArrayList<L1Object>();
        if (map.getId() <= MAX_MAP_ID) {
            for (L1Object element : _visibleObjects[map.getId()].values()) {
                if (element.equals(object)) {
                    continue;
                }
                if (map != element.getMap()) {
                    continue;
                }
                if (radius == -1) {
                    if (pt.isInScreen(element.getLocation())) {
                        result.add(element);
                    }
                } else if (radius == 0) {
                    if (pt.isSamePoint(element.getLocation())) {
                        result.add(element);
                    }
                } else {
                    if (pt.getTileLineDistance(element.getLocation()) <= radius) {
                        result.add(element);
                    }
                }
            }
        }
        return result;
    }

    public ArrayList<L1Object> getVisiblePoint(L1Location loc, int radius) {
        ArrayList<L1Object> result = new ArrayList<L1Object>();
        int mapId = loc.getMapId();
        if (mapId <= MAX_MAP_ID) {
            for (L1Object element : _visibleObjects[mapId].values()) {
                if (mapId != element.getMapId()) {
                    continue;
                }
                if (loc.getTileLineDistance(element.getLocation()) <= radius) {
                    result.add(element);
                }
            }
        }
        return result;
    }

    public ArrayList<L1PcInstance> getVisiblePlayer(L1Object object) {
        return getVisiblePlayer(object, -1);
    }

    public ArrayList<L1PcInstance> getVisiblePlayer(L1Object object, int radius) {
        int map = object.getMapId();
        Point pt = object.getLocation();
        ArrayList<L1PcInstance> result = new ArrayList<L1PcInstance>();
        for (L1PcInstance element : _allPlayers.values()) {
            if (element.equals(object)) {
                continue;
            }
            if (map != element.getMapId()) {
                continue;
            }
            if (radius == -1) {
                if (pt.isInScreen(element.getLocation())) {
                    result.add(element);
                }
            } else if (radius == 0) {
                if (pt.isSamePoint(element.getLocation())) {
                    result.add(element);
                }
            } else {
                if (pt.getTileLineDistance(element.getLocation()) <= radius) {
                    result.add(element);
                }
            }
        }
        return result;
    }

    public ArrayList<L1PcInstance> getVisiblePlayerExceptTargetSight(L1Object object, L1Object target) {
        int map = object.getMapId();
        Point objectPt = object.getLocation();
        Point targetPt = target.getLocation();
        ArrayList<L1PcInstance> result = new ArrayList<L1PcInstance>();
        for (L1PcInstance element : _allPlayers.values()) {
            if (element.equals(object)) {
                continue;
            }
            if (map != element.getMapId()) {
                continue;
            }
            if (Config.PC_RECOGNIZE_RANGE == -1) {
                if (objectPt.isInScreen(element.getLocation())) {
                    if (!targetPt.isInScreen(element.getLocation())) {
                        result.add(element);
                    }
                }
            } else {
                if (objectPt.getTileLineDistance(element.getLocation()) <= Config.PC_RECOGNIZE_RANGE) {
                    if (targetPt.getTileLineDistance(element.getLocation()) > Config.PC_RECOGNIZE_RANGE) {
                        result.add(element);
                    }
                }
            }
        }
        return result;
    }

    /**
	 * object�� �ν��� �� �ִ� ������ �ִ� �÷��̾ ����Ѵ�
	 * 
	 * @param object
	 * @return
	 */
    public ArrayList<L1PcInstance> getRecognizePlayer(L1Object object) {
        return getVisiblePlayer(object, Config.PC_RECOGNIZE_RANGE);
    }

    public L1PcInstance[] getAllPlayers3() {
        return _allPlayers.values().toArray(new L1PcInstance[_allPlayers.size()]);
    }

    private Collection<L1PcInstance> _allPlayerValues;

    public Collection<L1PcInstance> getAllPlayers() {
        Collection<L1PcInstance> vs = _allPlayerValues;
        return (vs != null) ? vs : (_allPlayerValues = Collections.unmodifiableCollection(_allPlayers.values()));
    }

    /**
	 * ��峻�� �ִ� ������ �̸��� �÷��̾ ����Ѵ�.
	 * 
	 * @param name
	 *            - �÷��̾��(�ҹ��ڡ��빮�ڴ� ���õȴ�)
	 * @return ������ �̸��� L1PcInstance.�ش� �÷��̾ �������� �ʴ� ���� null�� �����ش�.
	 */
    public L1PcInstance getPlayer(String name) {
        if (_allPlayers.contains(name)) {
            return _allPlayers.get(name);
        }
        for (L1PcInstance each : getAllPlayers()) {
            if (each.getName().equalsIgnoreCase(name)) {
                return each;
            }
        }
        return null;
    }

    private Collection<L1PetInstance> _allPetValues;

    public Collection<L1PetInstance> getAllPets() {
        Collection<L1PetInstance> vs = _allPetValues;
        return (vs != null) ? vs : (_allPetValues = Collections.unmodifiableCollection(_allPets.values()));
    }

    private Collection<L1SummonInstance> _allSummonValues;

    public Collection<L1SummonInstance> getAllSummons() {
        Collection<L1SummonInstance> vs = _allSummonValues;
        return (vs != null) ? vs : (_allSummonValues = Collections.unmodifiableCollection(_allSummons.values()));
    }

    public final Map<Integer, L1Object> getAllVisibleObjects() {
        return _allObjects;
    }

    public final Map<Integer, L1Object>[] getVisibleObjects() {
        return _visibleObjects;
    }

    public final Map<Integer, L1Object> getVisibleObjects(int mapId) {
        return _visibleObjects[mapId];
    }

    public Object getRegion(Object object) {
        return null;
    }

    public void addWar(L1War war) {
        if (!_allWars.contains(war)) {
            _allWars.add(war);
        }
    }

    public void removeWar(L1War war) {
        if (_allWars.contains(war)) {
            _allWars.remove(war);
        }
    }

    private List<L1War> _allWarList;

    public List<L1War> getWarList() {
        List<L1War> vs = _allWarList;
        return (vs != null) ? vs : (_allWarList = Collections.unmodifiableList(_allWars));
    }

    public void storeClan(L1Clan clan) {
        L1Clan temp = getClan(clan.getClanName());
        if (temp == null) {
            _allClans.put(clan.getClanName(), clan);
        }
    }

    public void removeClan(L1Clan clan) {
        L1Clan temp = getClan(clan.getClanName());
        if (temp != null) {
            _allClans.remove(clan.getClanName());
        }
    }

    public L1Clan getClan(String clan_name) {
        return _allClans.get(clan_name);
    }

    private Collection<L1Clan> _allClanValues;

    public Collection<L1Clan> getAllClans() {
        Collection<L1Clan> vs = _allClanValues;
        return (vs != null) ? vs : (_allClanValues = Collections.unmodifiableCollection(_allClans.values()));
    }

    public void setWeather(int weather) {
        _weather = weather;
    }

    public int getWeather() {
        return _weather;
    }

    public void set_worldChatElabled(boolean flag) {
        _worldChatEnabled = flag;
    }

    public boolean isWorldChatElabled() {
        return _worldChatEnabled;
    }

    public void setProcessingContributionTotal(boolean flag) {
        _processingContributionTotal = flag;
    }

    public boolean isProcessingContributionTotal() {
        return _processingContributionTotal;
    }

    public L1PetMember getPetMember() {
        return _PetMember;
    }

    public void setPetMember(L1PetMember pm) {
        _PetMember = pm;
    }

    public L1PetRace getPetRace() {
        return _PetRace;
    }

    public void setPetRace(L1PetRace pe) {
        _PetRace = pe;
    }

    public L1PetRaceEnd getPetRaceEnd() {
        return _PetRaceEnd;
    }

    public void setPetRaceEnd(L1PetRaceEnd pe2) {
        _PetRaceEnd = pe2;
    }

    /**
	 * ���� �����ϴ� ��� �÷��̾ ��Ŷ�� �۽��Ѵ�.
	 * 
	 * @param packet
	 *            �۽��ϴ� ��Ŷ�� ��Ÿ���� ServerBasePacket ������Ʈ.
	 */
    public void broadcastPacketToAll(ServerBasePacket packet) {
        _log.finest("players to notify : " + getAllPlayers().size());
        for (L1PcInstance pc : getAllPlayers()) {
            pc.sendPackets(packet);
        }
    }

    /**
	 * ���� �����ϴ� ��� �÷��̾ ���� �޼����� �۽��Ѵ�.
	 * 
	 * @param message
	 *            �۽��ϴ� �޼���
	 */
    public void broadcastServerMessage(String message) {
        broadcastPacketToAll(new S_SystemMessage(message));
    }

    public L1PcInstance[] getAllPlayers2() {
        return _allPlayers.values().toArray(new L1PcInstance[_allPlayers.size()]);
    }
}
