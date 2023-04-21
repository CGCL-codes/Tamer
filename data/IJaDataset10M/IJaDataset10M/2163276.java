package com.x3.monitoring.dao;

import com.x3.monitoring.entity.Kelurahan;
import java.util.List;

/**
 *
 * @author Hendro Steven
 */
public interface KelurahanDAO {

    public void insert(Kelurahan kel) throws Exception;

    public void update(Kelurahan kel) throws Exception;

    public void delete(int id) throws Exception;

    public Kelurahan get(int id) throws Exception;

    public List<Kelurahan> gets() throws Exception;
}
