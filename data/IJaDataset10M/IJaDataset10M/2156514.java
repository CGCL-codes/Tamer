package com.pos.spatobiz.common.dao;

import com.pos.spatobiz.common.error.SpatoBizException;
import com.pos.spatobiz.common.entity.Karyawan;
import java.util.List;

/**
 *
 * @author echo
 */
public interface KaryawanDao {

    public void insertKaryawan(Karyawan karyawan) throws SpatoBizException;

    public void updateKaryawan(Karyawan karyawan) throws SpatoBizException;

    public void deleteKaryawan(Karyawan karyawan) throws SpatoBizException;

    public void deleteKaryawan(Long id) throws SpatoBizException;

    public Karyawan getKaryawan(Long id) throws SpatoBizException;

    public Karyawan getKaryawan(String kode) throws SpatoBizException;

    public List<Karyawan> selectKaryawan() throws SpatoBizException;
}
