package com.x3.musrenbang.dao.jpa;

import com.x3.musrenbang.dao.UsulanBappedaDAO;
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
public class UsulanBappedaDAOImpl extends GeneralDAOImpl implements UsulanBappedaDAO {

    public UsulanBappeda get(int id) throws Exception {
        return getJpaTemplate().find(UsulanBappeda.class, id);
    }

    public List<UsulanBappeda> getByUsulanKecamatan(UsulanKecamatan usulan) {
        return getJpaTemplate().find("select u from UsulanBappeda u where u.usulKecamatan=?1", usulan);
    }

    public List<UsulanBappeda> gets() {
        return getJpaTemplate().find("select u from UsulanBappeda u");
    }

    public List<UsulanBappeda> gets(TahunAnggaran tahun) {
        return getJpaTemplate().find("select u from UsulanBappeda u where u.tahunAnggaran=?1", tahun);
    }

    public List<UsulanBappeda> getByUsulanSkpd(UsulanSkpd usulan) {
        return getJpaTemplate().find("select u from UsulanBappeda u where u.usulSkpd=?1", usulan);
    }

    public List<UsulanBappeda> getByAdvance(TahunAnggaran tahun, Skpd skpd) {
        return getJpaTemplate().find("select u from UsulanBappeda u where u.tahunAnggaran=?1 and u.skpd=?2", tahun, skpd);
    }

    public List<UsulanBappeda> gets(Skpd skpd) {
        return getJpaTemplate().find("select u from UsulanBappeda u where u.skpd=?1", skpd);
    }
}
