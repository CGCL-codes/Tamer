package org.sadhar.sipp.perekamankelompokkegiatan;

import java.util.List;

/**
 *
 * @author hendro
 */
public interface PerekamanKelompokKegiatanDAO {

    public List<PerekamanKelompokKegiatan> list() throws Exception;

    public List<PerekamanKelompokKegiatan> list(String key) throws Exception;

    public void insert(PerekamanKelompokKegiatan kelompokKegiatan) throws Exception;

    public void update(PerekamanKelompokKegiatan kelompokKegiatan) throws Exception;

    public void delete(PerekamanKelompokKegiatan kelompokKegiatan) throws Exception;

    public List select(String kelompok) throws Exception;

    public List select(int kodeKelompok, String kelompokKegiatan) throws Exception;

    public PerekamanKelompokKegiatan select(int kodeKelompok) throws Exception;
}
