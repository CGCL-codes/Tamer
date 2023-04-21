package org.sadhar.sipp.perekamankelompokbiaya;

import java.util.List;
import java.util.Map;

/**
 *
 * @author Yohan Hardinugroho
 */
public interface PerekamanKelompokBiayaDAO {

    public List<Map> listByTahun(String tahun);

    public List<Map> listDetails();

    public List<Map> listDetails(String tahun);

    public List<Map> listDistinct();

    public List<Map> listByTahunAndIsUmum(String tahun, String isUmum) throws Exception;

    public void insert(PerekamanKelompokBiaya kelompok) throws Exception;

    public void update(PerekamanKelompokBiaya kelompok) throws Exception;

    public void delete(PerekamanKelompokBiaya kelompok) throws Exception;

    public void delete(int kodeKelompokBiaya) throws Exception;

    public long countByTahunAndKodePosBiaya(String tahun, int kodePosBiaya) throws Exception;

    public long cekTableAnak(int kodeKelompokBiaya);
}
