package com.easyblog.core.dao;

import java.util.List;
import com.easyblog.core.dto.Visits;

public interface VisitsDao {

    public Visits findVisitsById(long id);

    public void insertVisits(Visits object);

    public void updateVisits(Visits object);

    public List<Visits> getAllVisitss();

    public void removeVisits(Visits object);
}
