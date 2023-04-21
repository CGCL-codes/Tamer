package org.sadhar.sipp.perekamankegiatanunit;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.sadhar.errhandler.ClassAntiNull;
import org.sadhar.sia.common.ClassConnection;
import org.sadhar.sia.common.ClassSession;

/**
 *
 * @author hendro
 */
public class PerekamanKegiatanUnitDAOImpl implements PerekamanKegiatanUnitDAO {

    public PerekamanKegiatanUnitDAOImpl() {
        ClassConnection.getTransactionProxyFactoryBean().setTarget(this);
    }

    public List<PerekamanKegiatanUnit> list() throws Exception {
        List<PerekamanKegiatanUnit> list = new ArrayList<PerekamanKegiatanUnit>();
        List<Map> rows = ClassConnection.getJdbc().queryForList("SELECT * FROM ppmk.kegiatanUnit");
        for (Map m : rows) {
            PerekamanKegiatanUnit kegiatanUnit = new PerekamanKegiatanUnit();
            kegiatanUnit.setKodeKegiatanUnit(ClassAntiNull.AntiNullInt(m.get("kodeKegiatanUnit")));
            kegiatanUnit.setKodeKelompokKegiatan(ClassAntiNull.AntiNullInt(m.get("kodeKelompokKegiatan")));
            kegiatanUnit.setKodeJenisKegiatan(ClassAntiNull.AntiNullInt(m.get("kodeJenisKegiatan")));
            kegiatanUnit.setNamaSubKegiatan(ClassAntiNull.AntiNullString(m.get("namaSubKegiatan")));
            kegiatanUnit.setKeterangan(ClassAntiNull.AntiNullString(m.get("keterangan")));
            kegiatanUnit.setTglRencanaAwalKegiatan(ClassAntiNull.AntiNullDate(m.get("tglRencanaAwalKegiatan")));
            kegiatanUnit.setTglRencanaAkhirKegiatan(ClassAntiNull.AntiNullDate(m.get("tglRencanaAkhirKegiatan")));
            kegiatanUnit.setNoIndikatorKinerja(ClassAntiNull.AntiNullString(m.get("noIndikatorKinerja")));
            kegiatanUnit.setTahun(ClassAntiNull.AntiNullString(m.get("tahun")));
            kegiatanUnit.setIsRencana(ClassAntiNull.AntiNullString(m.get("isRencana")));
            list.add(kegiatanUnit);
        }
        return list;
    }

    public void insert(PerekamanKegiatanUnit kegiatanUnit) throws Exception {
        String sql = "INSERT INTO ppmk.kegiatanUnit(kodeKelompokKegiatan," + "namaSubKegiatan," + "kodeJenisKegiatan," + "keterangan," + "tglRencanaAwalKegiatan," + "tglRencanaAkhirKegiatan," + "noIndikatorKinerja," + "tahun," + "kodeUnit," + "isRencana) VALUES(?,?,?,?,?,?,?,?,?,?)";
        ClassSession sesi = new ClassSession("2008", "212");
        String tahun = sesi.getThnAnggaran();
        String kdUnit = sesi.getKodeUnit();
        ClassConnection.getJdbc().update(sql, new Object[] { kegiatanUnit.getKodeKelompokKegiatan(), kegiatanUnit.getNamaSubKegiatan(), kegiatanUnit.getKodeJenisKegiatan(), kegiatanUnit.getKeterangan(), ClassAntiNull.AntiNullDateMysqlString(kegiatanUnit.getTglRencanaAwalKegiatan()), ClassAntiNull.AntiNullDateMysqlString(kegiatanUnit.getTglRencanaAkhirKegiatan()), kegiatanUnit.getNoIndikatorKinerja(), tahun, kdUnit, "Y" });
    }

    public List<PerekamanKegiatanUnit> list(String tahun, String kodeUnit) throws Exception {
        List<PerekamanKegiatanUnit> list = new ArrayList<PerekamanKegiatanUnit>();
        List<Map> rows = ClassConnection.getJdbc().queryForList("SELECT * FROM ppmk.kegiatanUnit WHERE tahun=? AND kodeUnit=?", new Object[] { tahun, kodeUnit });
        for (Map m : rows) {
            PerekamanKegiatanUnit kegiatanUnit = new PerekamanKegiatanUnit();
            kegiatanUnit.setKodeKegiatanUnit(ClassAntiNull.AntiNullInt(m.get("kodeKegiatanUnit")));
            kegiatanUnit.setKodeKelompokKegiatan(ClassAntiNull.AntiNullInt(m.get("kodeKelompokKegiatan")));
            kegiatanUnit.setKodeJenisKegiatan(ClassAntiNull.AntiNullInt(m.get("kodeJenisKegiatan")));
            kegiatanUnit.setNamaSubKegiatan(ClassAntiNull.AntiNullString(m.get("namaSubKegiatan")));
            kegiatanUnit.setKeterangan(ClassAntiNull.AntiNullString(m.get("keterangan")));
            kegiatanUnit.setTglRencanaAwalKegiatan(ClassAntiNull.AntiNullDate(m.get("tglRencanaAwalKegiatan")));
            kegiatanUnit.setTglRencanaAkhirKegiatan(ClassAntiNull.AntiNullDate(m.get("tglRencanaAkhirKegiatan")));
            kegiatanUnit.setNoIndikatorKinerja(ClassAntiNull.AntiNullString(m.get("noIndikatorKinerja")));
            kegiatanUnit.setTahun(ClassAntiNull.AntiNullString(m.get("tahun")));
            kegiatanUnit.setIsRencana(ClassAntiNull.AntiNullString(m.get("isRencana")));
            list.add(kegiatanUnit);
        }
        return list;
    }

    public void delete(PerekamanKegiatanUnit kegiatanUnit) throws Exception {
        String sql = "DELETE FROM ppmk.kegiatanUnit WHERE kodeKegiatanUnit=?";
        ClassConnection.getJdbc().update(sql, new Object[] { kegiatanUnit.getKodeKegiatanUnit() });
    }

    public void update(PerekamanKegiatanUnit kegiatanUnit) throws Exception {
        String sql = "UPDATE ppmk.kegiatanUnit SET kodeKelompokKegiatan=?," + "namaSubKegiatan=?," + "kodeJenisKegiatan=?," + "keterangan=?," + "tglRencanaAwalKegiatan=?," + "tglRencanaAkhirKegiatan=?," + "noIndikatorKinerja=?," + "tahun=?," + "kodeUnit=?," + "isRencana=? WHERE kodeKegiatanUnit=?";
        ClassSession sesi = new ClassSession("2008", "212");
        String tahun = sesi.getThnAnggaran();
        String kdUnit = sesi.getKodeUnit();
        ClassConnection.getJdbc().update(sql, new Object[] { kegiatanUnit.getKodeKelompokKegiatan(), kegiatanUnit.getNamaSubKegiatan(), kegiatanUnit.getKodeJenisKegiatan(), kegiatanUnit.getKeterangan(), ClassAntiNull.AntiNullDateMysqlString(kegiatanUnit.getTglRencanaAwalKegiatan()), ClassAntiNull.AntiNullDateMysqlString(kegiatanUnit.getTglRencanaAkhirKegiatan()), kegiatanUnit.getNoIndikatorKinerja(), tahun, kdUnit, "Y", kegiatanUnit.getKodeKegiatanUnit() });
    }

    public List<PerekamanKegiatanUnit> list(int kodeKelompokKegiatan, int jenisKegiatan, String namaKegiatan) throws Exception {
        List<PerekamanKegiatanUnit> list = new ArrayList<PerekamanKegiatanUnit>();
        List<Map> rows = ClassConnection.getJdbc().queryForList("SELECT * FROM ppmk.kegiatanUnit WHERE kodeKelompokKegiatan=? AND kodeJenisKegiatan=? AND upper(namaSubKegiatan)=?", new Object[] { kodeKelompokKegiatan, jenisKegiatan, namaKegiatan.toUpperCase() });
        for (Map m : rows) {
            PerekamanKegiatanUnit kegiatanUnit = new PerekamanKegiatanUnit();
            kegiatanUnit.setKodeKegiatanUnit(ClassAntiNull.AntiNullInt(m.get("kodeKegiatanUnit")));
            kegiatanUnit.setKodeKelompokKegiatan(ClassAntiNull.AntiNullInt(m.get("kodeKelompokKegiatan")));
            kegiatanUnit.setKodeJenisKegiatan(ClassAntiNull.AntiNullInt(m.get("kodeJenisKegiatan")));
            kegiatanUnit.setNamaSubKegiatan(ClassAntiNull.AntiNullString(m.get("namaSubKegiatan")));
            kegiatanUnit.setKeterangan(ClassAntiNull.AntiNullString(m.get("keterangan")));
            kegiatanUnit.setTglRencanaAwalKegiatan(ClassAntiNull.AntiNullDate(m.get("tglRencanaAwalKegiatan")));
            kegiatanUnit.setTglRencanaAkhirKegiatan(ClassAntiNull.AntiNullDate(m.get("tglRencanaAkhirKegiatan")));
            kegiatanUnit.setNoIndikatorKinerja(ClassAntiNull.AntiNullString(m.get("noIndikatorKinerja")));
            kegiatanUnit.setTahun(ClassAntiNull.AntiNullString(m.get("tahun")));
            kegiatanUnit.setIsRencana(ClassAntiNull.AntiNullString(m.get("isRencana")));
            list.add(kegiatanUnit);
        }
        return list;
    }

    public String isDikunci() throws Exception {
        ClassSession sesi = new ClassSession("2008", "212");
        String tahun = sesi.getThnAnggaran();
        String kdUnit = sesi.getKodeUnit();
        return (String) ClassConnection.getJdbc().queryForObject("SELECT isDikunci FROM ppmk.kunciAnggaranBiaya WHERE tahunAnggaran=? AND kodeUnit=? " + "and kodeJenisAnggaran=2 and isRevisi='Y' ", new Object[] { tahun, kdUnit }, String.class);
    }
}
