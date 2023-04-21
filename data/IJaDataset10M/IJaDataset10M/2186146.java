package com.x3.monitoring.dao;

import com.x3.monitoring.entity.IjinPusat;
import java.util.List;

/**
 *
 * @author Hendro Steven
 */
public interface IjinPusatDAO {

    public void insert(IjinPusat ijin) throws Exception;

    public void update(IjinPusat ijin) throws Exception;

    public void delete(int id) throws Exception;

    public IjinPusat get(int id) throws Exception;

    public List<IjinPusat> gets(String idPerusahaan) throws Exception;
}
