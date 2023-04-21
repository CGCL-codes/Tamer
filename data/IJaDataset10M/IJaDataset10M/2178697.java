package com.x3.dishub.dao;

import com.x3.dishub.entity.IjinUsahaAngkutanBarang;
import com.x3.dishub.entity.Perusahaan;
import java.util.List;

/**
 *
 * @author Hendro Steven
 */
public interface IjinUsahaAngkutanBarangDAO extends GeneralDAO {

    public List<IjinUsahaAngkutanBarang> getAllIjinUsahaAngkutanBarang();

    public List<IjinUsahaAngkutanBarang> getByNomor(String nomor);

    public List<IjinUsahaAngkutanBarang> getByPerusahaan(Perusahaan perusahaan);
}
