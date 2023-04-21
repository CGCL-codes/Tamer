package de.banh.bibo.model;

import java.util.List;
import de.banh.bibo.exceptions.CannotDeleteEntityException;
import de.banh.bibo.exceptions.CannotModifyTableException;
import de.banh.bibo.exceptions.CannotSearchEntityException;

public interface MedientypManager {

    public Medientyp createMedientyp(String bezeichnung);

    public Medientyp getMedientypById(long id) throws CannotSearchEntityException;

    public Medientyp getMedientypByBezeichnung(String bezeichnung) throws CannotSearchEntityException;

    public Medientyp getStandardMedientyp() throws CannotSearchEntityException;

    public void saveMedientyp(Medientyp medientyp) throws CannotModifyTableException;

    public void deleteMedientyp(Medientyp medientyp) throws CannotDeleteEntityException;

    public List<Medientyp> getMedientypen() throws CannotSearchEntityException;

    public List<Medientyp> getMedientypen(String suchtext) throws CannotSearchEntityException;
}
