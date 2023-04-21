package com.l2jserver.gameserver.datatables;

import gnu.trove.TIntObjectHashMap;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.l2jserver.L2DatabaseFactory;
import com.l2jserver.gameserver.templates.StatsSet;
import com.l2jserver.gameserver.templates.item.L2Henna;

/**
 * This class ...
 *
 * @version $Revision$ $Date$
 */
public class HennaTable {

    private static Logger _log = Logger.getLogger(HennaTable.class.getName());

    private TIntObjectHashMap<L2Henna> _henna;

    public static HennaTable getInstance() {
        return SingletonHolder._instance;
    }

    private HennaTable() {
        _henna = new TIntObjectHashMap<L2Henna>();
        restoreHennaData();
    }

    /**
	 *
	 */
    private void restoreHennaData() {
        Connection con = null;
        try {
            con = L2DatabaseFactory.getInstance().getConnection();
            PreparedStatement statement = con.prepareStatement("SELECT symbol_id, symbol_name, dye_id, dye_amount, price, stat_INT, stat_STR, stat_CON, stat_MEM, stat_DEX, stat_WIT FROM henna");
            ResultSet hennadata = statement.executeQuery();
            fillHennaTable(hennadata);
            hennadata.close();
            statement.close();
        } catch (Exception e) {
            _log.log(Level.SEVERE, "Error while creating henna table " + e.getMessage(), e);
        } finally {
            L2DatabaseFactory.close(con);
        }
    }

    private void fillHennaTable(ResultSet HennaData) throws Exception {
        while (HennaData.next()) {
            StatsSet hennaDat = new StatsSet();
            int id = HennaData.getInt("symbol_id");
            hennaDat.set("symbol_id", id);
            hennaDat.set("dye", HennaData.getInt("dye_id"));
            hennaDat.set("price", HennaData.getInt("price"));
            hennaDat.set("amount", HennaData.getInt("dye_amount"));
            hennaDat.set("stat_INT", HennaData.getInt("stat_INT"));
            hennaDat.set("stat_STR", HennaData.getInt("stat_STR"));
            hennaDat.set("stat_CON", HennaData.getInt("stat_CON"));
            hennaDat.set("stat_MEM", HennaData.getInt("stat_MEM"));
            hennaDat.set("stat_DEX", HennaData.getInt("stat_DEX"));
            hennaDat.set("stat_WIT", HennaData.getInt("stat_WIT"));
            L2Henna template = new L2Henna(hennaDat);
            _henna.put(id, template);
        }
        _log.info("HennaTable: Loaded " + _henna.size() + " Templates.");
    }

    public L2Henna getTemplate(int id) {
        return _henna.get(id);
    }

    @SuppressWarnings("synthetic-access")
    private static class SingletonHolder {

        protected static final HennaTable _instance = new HennaTable();
    }
}
