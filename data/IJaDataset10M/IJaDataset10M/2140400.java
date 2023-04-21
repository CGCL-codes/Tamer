package com.googlecode.semrs.server.dao;

import java.util.Collection;
import java.util.Map;
import com.googlecode.semrs.model.DiagnosisContainer;
import com.googlecode.semrs.model.Encounter;
import com.googlecode.semrs.server.exception.DeleteException;
import com.googlecode.semrs.server.exception.SaveOrUpdateException;

public interface EncounterDAO {

    public Encounter getEncounter(String id, boolean deep);

    public Collection<Encounter> listEncounters();

    public Collection<Encounter> listEncounters(String order, String orderBy, String limit, String offset);

    public int getEncounterCount();

    public Collection<Encounter> listEncountersByQuery(Map params, String order, String orderBy, String limit, String offset);

    public int getEncounterCount(Map params);

    public void deleteEncounter(Encounter encounter) throws DeleteException;

    public Encounter saveEncounter(Encounter encounter) throws SaveOrUpdateException;

    public Encounter saveDeepEncounter(Encounter encounter) throws SaveOrUpdateException;

    public Collection<DiagnosisContainer> getAssistedDiagnosis(Map params);

    public Collection<DiagnosisContainer> getAssistedDiagnosisPrediction(Map params);
}
