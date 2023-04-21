package com.googlecode.semrs.server.service;

import java.util.Collection;
import java.util.Map;
import com.googlecode.semrs.model.Drug;
import com.googlecode.semrs.model.Symptom;
import com.googlecode.semrs.server.exception.DeleteException;
import com.googlecode.semrs.server.exception.SaveOrUpdateException;

public interface DrugService {

    public Drug save(Drug drug) throws SaveOrUpdateException;

    public Collection<Drug> listDrugs();

    public Drug getDrug(String id, boolean deep);

    public Collection<Drug> listDrugs(String order, String orderBy, String limit, String offset);

    public int getDrugCount();

    public Collection<Drug> listDrugsByQuery(Map params, String order, String orderBy, String limit, String offset);

    public int getDrugCount(Map params);

    public void deleteDrug(Drug drug) throws DeleteException;

    public Collection<Symptom> getAvailableSymptoms(String drugId, Map params, String order, String orderBy, String limit, String offset);

    public int getAvailableSymptomsCount(String drugId, Map params);
}
