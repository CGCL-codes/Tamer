package it.schedesoftware.dao.durata;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import it.schedesoftware.dao.AbstractMenu;
import it.schedesoftware.dao.SchedeSoftwareSqlConfig;
import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * @author andrea
 *
 */
public class DurataDAO {

    private SqlMapClient sqlMap;

    public DurataDAO() {
        this.sqlMap = SchedeSoftwareSqlConfig.getSqlMapInstance();
    }

    public ArrayList<AbstractMenu> getList() {
        ArrayList<AbstractMenu> ret = null;
        try {
            ret = (ArrayList<AbstractMenu>) this.sqlMap.queryForList("getDurataList");
        } catch (Throwable t) {
            throw new RuntimeException("Errore nel caricamento della lista delle targhe delle durate", t);
        }
        return ret;
    }

    public String getNome(int id) {
        String ret = null;
        try {
            ret = (String) this.sqlMap.queryForObject("getDurataNome", id);
        } catch (Throwable t) {
            throw new RuntimeException("Errore nel caricamento della durata", t);
        }
        return ret;
    }

    @SuppressWarnings("unchecked")
    public Map<Integer, AbstractMenu> getMap() {
        Map<Integer, AbstractMenu> valuesMap = null;
        try {
            valuesMap = (Map<Integer, AbstractMenu>) this.sqlMap.queryForMap("getDurataList", null, "id");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return valuesMap;
    }
}
