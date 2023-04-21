package l1j.server.server.datatables;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import l1j.server.L1DatabaseFactory;
import l1j.server.server.IdFactory;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1DoorInstance;
import l1j.server.server.templates.L1Npc;
import l1j.server.server.utils.SQLUtil;

public class DoorSpawnTable {

    private static Logger _log = Logger.getLogger(DoorSpawnTable.class.getName());

    private static DoorSpawnTable _instance;

    private final ArrayList<L1DoorInstance> _doorList = new ArrayList<L1DoorInstance>();

    public static DoorSpawnTable getInstance() {
        if (_instance == null) {
            _instance = new DoorSpawnTable();
        }
        return _instance;
    }

    private DoorSpawnTable() {
        FillDoorSpawnTable();
    }

    private void FillDoorSpawnTable() {
        Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        try {
            con = L1DatabaseFactory.getInstance().getConnection();
            pstm = con.prepareStatement("SELECT * FROM spawnlist_door");
            rs = pstm.executeQuery();
            do {
                if (!rs.next()) {
                    break;
                }
                L1Npc l1npc = NpcTable.getInstance().getTemplate(81158);
                if (l1npc != null) {
                    String s = l1npc.getImpl();
                    Constructor constructor = Class.forName("l1j.server.server.model.Instance." + s + "Instance").getConstructors()[0];
                    Object parameters[] = { l1npc };
                    L1DoorInstance door = (L1DoorInstance) constructor.newInstance(parameters);
                    door = (L1DoorInstance) constructor.newInstance(parameters);
                    door.setId(IdFactory.getInstance().nextId());
                    door.setDoorId(rs.getInt(1));
                    door.setGfxId(rs.getInt(3));
                    door.setX(rs.getInt(4));
                    door.setY(rs.getInt(5));
                    door.setMap((short) rs.getInt(6));
                    door.setHomeX(rs.getInt(4));
                    door.setHomeY(rs.getInt(5));
                    door.setDirection(rs.getInt(7));
                    door.setLeftEdgeLocation(rs.getInt(8));
                    door.setRightEdgeLocation(rs.getInt(9));
                    door.setMaxHp(rs.getInt(10));
                    door.setCurrentHp(rs.getInt(10));
                    door.setKeeperId(rs.getInt(11));
                    L1World.getInstance().storeObject(door);
                    L1World.getInstance().addVisibleObject(door);
                    if (door.getKeeperId() != 0) {
                        _doorList.add(door);
                    }
                }
            } while (true);
        } catch (SQLException e) {
            _log.log(Level.SEVERE, e.getLocalizedMessage(), e);
        } catch (SecurityException e) {
            _log.log(Level.SEVERE, e.getLocalizedMessage(), e);
        } catch (ClassNotFoundException e) {
            _log.log(Level.SEVERE, e.getLocalizedMessage(), e);
        } catch (IllegalArgumentException e) {
            _log.log(Level.SEVERE, e.getLocalizedMessage(), e);
        } catch (InstantiationException e) {
            _log.log(Level.SEVERE, e.getLocalizedMessage(), e);
        } catch (IllegalAccessException e) {
            _log.log(Level.SEVERE, e.getLocalizedMessage(), e);
        } catch (InvocationTargetException e) {
            _log.log(Level.SEVERE, e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
    }

    public L1DoorInstance[] getDoorList() {
        return _doorList.toArray(new L1DoorInstance[_doorList.size()]);
    }

    public L1DoorInstance getDoor(int npcId) {
        L1DoorInstance sTemp = null;
        for (L1DoorInstance door : _doorList.toArray(new L1DoorInstance[_doorList.size()])) {
            if (door.getDoorId() == npcId) {
                sTemp = door;
                break;
            }
        }
        return sTemp;
    }
}
