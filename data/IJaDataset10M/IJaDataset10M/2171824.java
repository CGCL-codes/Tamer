package org.sadhar.sipp.laporanrekapitulasipendapatan;

import java.util.List;
import java.util.Map;

/**
 *
 * @author kris
 */
public interface LaporanRekapitulasiPendapatanDAO {

    public List<Map> getTahunFromPosPendapatanUnit() throws Exception;

    public List<Map> getNamaUnitKerja(String tahun) throws Exception;

    public List<Map> getPosPendapatan(String tahun) throws Exception;

    public Double getHasil(String tahunAnggaran, String namaUnit, String namaPos) throws Exception;
}
