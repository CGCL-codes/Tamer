package com.schinzer.fin.dao.base;

import java.util.List;
import org.springframework.dao.DataAccessException;
import com.schinzer.fin.model.base.Repeat;

public interface RepeatDAO {

    public void delete(Repeat rpt) throws DataAccessException;

    public Repeat insert(Repeat rpt) throws DataAccessException;

    public List<Repeat> select(RepeatFilter filter) throws DataAccessException;

    public Repeat select(long key) throws DataAccessException;

    public boolean update(Repeat rpt) throws DataAccessException;
}
