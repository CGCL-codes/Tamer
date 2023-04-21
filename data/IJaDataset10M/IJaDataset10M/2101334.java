package de.tum.in.elitese.wahlsys.persister.persistence_objects;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import de.tum.in.elitese.wahlsys.persister.PersistenceObject;

/**
 * see Table PARTEI
 * 
 * @author Christoph Frenzel
 * 
 */
public class Partei extends PersistenceObject {

    private static final List<String> FIELD_NAMES = Collections.unmodifiableList(Arrays.asList(new String[] { "NAME", "KUERZEL", "STIMMEN" }));

    private static final String TABLE_NAME = "PARTEI";

    private String fKuerzel;

    private String fName;

    private Integer fStimmen = 0;

    public String getKuerzel() {
        return fKuerzel;
    }

    public String getName() {
        return fName;
    }

    @Override
    public List<String> getPersistanceStrings() {
        List<Object> values = new ArrayList<Object>(3);
        values.add(getName());
        values.add(getKuerzel());
        values.add(getStimmen());
        List<String> result = new ArrayList<String>(1);
        result.add(createInsertString(TABLE_NAME, FIELD_NAMES, Collections.unmodifiableList(values)));
        return result;
    }

    public Integer getStimmen() {
        return fStimmen;
    }

    public void setKuerzel(String kuerzel) {
        fKuerzel = kuerzel;
    }

    public void setName(String name) {
        fName = name;
    }

    public void setStimmen(Integer stimmen) {
        fStimmen = stimmen;
    }
}
