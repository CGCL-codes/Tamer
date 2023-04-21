package org.sadhar.sipp.gelombang;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.sadhar.errhandler.ClassAntiNull;
import org.sadhar.sia.common.ClassConnection;

/**
 * 
 * @author Yohan Hardinugroho
 */
public class GelombangDAOImpl implements GelombangDAO {

    public GelombangDAOImpl() {
        ClassConnection.getTransactionProxyFactoryBean().setTarget(this);
    }

    public List<Gelombang> list() throws Exception {
        List<Gelombang> gelombangList = new ArrayList<Gelombang>();
        List<Map> row = ClassConnection.getJdbc().queryForList("SELECT * FROM kamus.gelombang");
        for (Map m : row) {
            Gelombang gelombang = new Gelombang();
            gelombang.setKd_gelombang(m.get("kd_gelombang").toString());
            gelombang.setNm_gelombang(m.get("nm_gelombang").toString());
            gelombang.setAktifasi(Boolean.parseBoolean(ClassAntiNull.AntiNullString(m.get("aktifasi"))) ? 1 : 0);
            gelombang.setKelompok(ClassAntiNull.AntiNullInt(m.get("kelompok")));
            gelombangList.add(gelombang);
        }
        return gelombangList;
    }

    @Override
    public Gelombang getByKodeGelombang(String kodeGelombang) throws Exception {
        List<Gelombang> gelombangList = new ArrayList<Gelombang>();
        List<Map> row = ClassConnection.getJdbc().queryForList("SELECT * FROM kamus.gelombang g where g.kd_gelombang=?", new Object[] { kodeGelombang });
        Gelombang gelombang = null;
        for (Map m : row) {
            gelombang = new Gelombang();
            gelombang.setKd_gelombang(m.get("kd_gelombang").toString());
            gelombang.setNm_gelombang(m.get("nm_gelombang").toString());
            gelombang.setAktifasi(Boolean.parseBoolean(ClassAntiNull.AntiNullString(m.get("aktifasi"))) ? 1 : 0);
            gelombang.setKelompok(ClassAntiNull.AntiNullInt(m.get("kelompok")));
            gelombangList.add(gelombang);
        }
        return gelombang;
    }
}
