package com.x3.musrenbang.dao;

import com.x3.musrenbang.entity.Skpd;
import com.x3.musrenbang.entity.TahunAnggaran;
import com.x3.musrenbang.entity.UsulanBappeda;
import com.x3.musrenbang.entity.UsulanKecamatan;
import com.x3.musrenbang.entity.UsulanSkpd;
import java.util.List;

/**
 *
 * @author Hendro Steven
 */
public interface UsulanBappedaDAO extends GeneralDAO {

    public UsulanBappeda get(int id) throws Exception;

    public List<UsulanBappeda> getByUsulanKecamatan(UsulanKecamatan usulan);

    public List<UsulanBappeda> gets();

    public List<UsulanBappeda> gets(TahunAnggaran tahun);

    public List<UsulanBappeda> getByUsulanSkpd(UsulanSkpd usulan);

    public List<UsulanBappeda> getByAdvance(TahunAnggaran tahun, Skpd skpd);

    public List<UsulanBappeda> gets(Skpd skpd);
}
