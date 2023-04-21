package com.wiai.untl.core.dao;

import com.wiai.untl.core.entity.Fakultas;
import java.util.List;

/**
 *
 * @author Hendro Steven
 */
public interface FakultasDAO extends GeneralDAO {

    public List<Fakultas> getAllFakultas();

    public Fakultas getById(long id);
}
