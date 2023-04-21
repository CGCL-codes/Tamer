package com.l2jserver.gameserver.instancemanager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.l2jserver.L2DatabaseFactory;
import com.l2jserver.gameserver.InstanceListManager;
import com.l2jserver.gameserver.SevenSigns;
import com.l2jserver.gameserver.model.L2Clan;
import com.l2jserver.gameserver.model.L2ClanMember;
import com.l2jserver.gameserver.model.L2ItemInstance;
import com.l2jserver.gameserver.model.L2Object;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.entity.Castle;
import javolution.util.FastList;

public class CastleManager implements InstanceListManager {

    protected static final Logger _log = Logger.getLogger(CastleManager.class.getName());

    public static final CastleManager getInstance() {
        return SingletonHolder._instance;
    }

    private List<Castle> _castles;

    private static final int _castleCirclets[] = { 0, 6838, 6835, 6839, 6837, 6840, 6834, 6836, 8182, 8183 };

    private CastleManager() {
    }

    public final int findNearestCastleIndex(L2Object obj) {
        return findNearestCastleIndex(obj, Long.MAX_VALUE);
    }

    public final int findNearestCastleIndex(L2Object obj, long maxDistance) {
        int index = getCastleIndex(obj);
        if (index < 0) {
            double distance;
            Castle castle;
            for (int i = 0; i < getCastles().size(); i++) {
                castle = getCastles().get(i);
                if (castle == null) continue;
                distance = castle.getDistance(obj);
                if (maxDistance > distance) {
                    maxDistance = (long) distance;
                    index = i;
                }
            }
        }
        return index;
    }

    public final Castle getCastleById(int castleId) {
        for (Castle temp : getCastles()) {
            if (temp.getCastleId() == castleId) return temp;
        }
        return null;
    }

    public final Castle getCastleByOwner(L2Clan clan) {
        for (Castle temp : getCastles()) {
            if (temp.getOwnerId() == clan.getClanId()) return temp;
        }
        return null;
    }

    public final Castle getCastle(String name) {
        for (Castle temp : getCastles()) {
            if (temp.getName().equalsIgnoreCase(name.trim())) return temp;
        }
        return null;
    }

    public final Castle getCastle(int x, int y, int z) {
        for (Castle temp : getCastles()) {
            if (temp.checkIfInZone(x, y, z)) return temp;
        }
        return null;
    }

    public final Castle getCastle(L2Object activeObject) {
        return getCastle(activeObject.getX(), activeObject.getY(), activeObject.getZ());
    }

    public final int getCastleIndex(int castleId) {
        Castle castle;
        for (int i = 0; i < getCastles().size(); i++) {
            castle = getCastles().get(i);
            if (castle != null && castle.getCastleId() == castleId) return i;
        }
        return -1;
    }

    public final int getCastleIndex(L2Object activeObject) {
        return getCastleIndex(activeObject.getX(), activeObject.getY(), activeObject.getZ());
    }

    public final int getCastleIndex(int x, int y, int z) {
        Castle castle;
        for (int i = 0; i < getCastles().size(); i++) {
            castle = getCastles().get(i);
            if (castle != null && castle.checkIfInZone(x, y, z)) return i;
        }
        return -1;
    }

    public final List<Castle> getCastles() {
        if (_castles == null) _castles = new FastList<Castle>();
        return _castles;
    }

    public final void validateTaxes(int sealStrifeOwner) {
        int maxTax;
        switch(sealStrifeOwner) {
            case SevenSigns.CABAL_DUSK:
                maxTax = 5;
                break;
            case SevenSigns.CABAL_DAWN:
                maxTax = 25;
                break;
            default:
                maxTax = 15;
                break;
        }
        for (Castle castle : _castles) if (castle.getTaxPercent() > maxTax) castle.setTaxPercent(maxTax);
    }

    int _castleId = 1;

    public int getCirclet() {
        return getCircletByCastleId(_castleId);
    }

    public int getCircletByCastleId(int castleId) {
        if (castleId > 0 && castleId < 10) return _castleCirclets[castleId];
        return 0;
    }

    public void removeCirclet(L2Clan clan, int castleId) {
        for (L2ClanMember member : clan.getMembers()) removeCirclet(member, castleId);
    }

    public void removeCirclet(L2ClanMember member, int castleId) {
        if (member == null) return;
        L2PcInstance player = member.getPlayerInstance();
        int circletId = getCircletByCastleId(castleId);
        if (circletId != 0) {
            if (player != null) {
                try {
                    L2ItemInstance circlet = player.getInventory().getItemByItemId(circletId);
                    if (circlet != null) {
                        if (circlet.isEquipped()) player.getInventory().unEquipItemInSlotAndRecord(circlet.getLocationSlot());
                        player.destroyItemByItemId("CastleCircletRemoval", circletId, 1, player, true);
                    }
                    return;
                } catch (NullPointerException e) {
                }
            }
            Connection con = null;
            try {
                con = L2DatabaseFactory.getInstance().getConnection();
                PreparedStatement statement = con.prepareStatement("DELETE FROM items WHERE owner_id = ? and item_id = ?");
                statement.setInt(1, member.getObjectId());
                statement.setInt(2, circletId);
                statement.execute();
                statement.close();
            } catch (Exception e) {
                _log.log(Level.WARNING, "Failed to remove castle circlets offline for player " + member.getName() + ": " + e.getMessage(), e);
            } finally {
                L2DatabaseFactory.close(con);
            }
        }
    }

    public void loadInstances() {
        _log.info("Initializing CastleManager");
        Connection con = null;
        try {
            PreparedStatement statement;
            ResultSet rs;
            con = L2DatabaseFactory.getInstance().getConnection();
            statement = con.prepareStatement("SELECT id FROM castle ORDER BY id");
            rs = statement.executeQuery();
            while (rs.next()) {
                getCastles().add(new Castle(rs.getInt("id")));
            }
            statement.close();
            _log.info("Loaded: " + getCastles().size() + " castles");
        } catch (Exception e) {
            _log.log(Level.WARNING, "Exception: loadCastleData(): " + e.getMessage(), e);
        } finally {
            L2DatabaseFactory.close(con);
        }
    }

    public void updateReferences() {
    }

    public void activateInstances() {
        for (final Castle castle : _castles) {
            castle.activateInstance();
        }
    }

    @SuppressWarnings("synthetic-access")
    private static class SingletonHolder {

        protected static final CastleManager _instance = new CastleManager();
    }
}
