package de.tum.in.elitese.wahlsys.persister.persistence_objects;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import de.tum.in.elitese.wahlsys.persister.PersistenceObject;

/**
 * see Table ERSTSTIMMEN
 * 
 * @author Christoph Frenzel
 * 
 */
public class Erststimmen extends PersistenceObject {

    private static final List<String> FIELD_NAMES = Collections.unmodifiableList(Arrays.asList(new String[] { "WAHLBEZIRK", "WAHLKREIS", "BUNDESLAND", "DIREKTKANDIDAT", "ANZAHL" }));

    private static final String TABLE_NAME = "ERSTSTIMMEN";

    private Integer fAnzahl;

    private Direktkandidat fDirektkandidat;

    private Integer fId;

    private Wahlbezirk fWahlbezirk;

    public int getAnzahl() {
        return fAnzahl;
    }

    public Direktkandidat getDirektkandidat() {
        return fDirektkandidat;
    }

    public Integer getId() {
        return fId;
    }

    @Override
    public List<String> getPersistanceStrings() {
        List<Object> values = new ArrayList<Object>(5);
        values.add(getWahlbezirk().getName());
        values.add(getWahlbezirk().getWahlkreis().getNummer());
        values.add(getWahlbezirk().getWahlkreis().getBundesland().getName());
        if (getDirektkandidat() != null) {
            values.add(getDirektkandidat().getPersonalausweisnummer());
        } else {
            values.add(null);
        }
        values.add(getAnzahl());
        List<String> result = new ArrayList<String>(1);
        result.add(createInsertString(TABLE_NAME, FIELD_NAMES, Collections.unmodifiableList(values)));
        return result;
    }

    public Wahlbezirk getWahlbezirk() {
        return fWahlbezirk;
    }

    public void setAnzahl(int anzahl) {
        fAnzahl = anzahl;
    }

    public void setDirektkandidat(Direktkandidat direktkandidat) {
        fDirektkandidat = direktkandidat;
    }

    public void setWahlbezirk(Wahlbezirk wahlbezirk) {
        fWahlbezirk = wahlbezirk;
    }
}
