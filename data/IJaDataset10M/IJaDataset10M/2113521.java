package eu.redseeds.sc.terminology.model;

import java.util.List;

public class TermSenseDTOImpl implements TermSenseDTO {

    private String sense;

    private long uid;

    private String type;

    private String lemma;

    private boolean isWordNetEntry;

    private TermSenseDTO parent = null;

    private List<TermSenseDTO> synonyms = null;

    public TermSenseDTOImpl() {
    }

    public String getSense() {
        return sense;
    }

    public void setSense(String sense) {
        this.sense = sense;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return sense;
    }

    public String getLemma() {
        return lemma.replace('_', ' ').toLowerCase();
    }

    public void setLemma(String lemma) {
        this.lemma = lemma;
    }

    @Override
    public boolean isWordNetEntry() {
        return isWordNetEntry;
    }

    @Override
    public void setWordNetEntry(boolean isWordNetEntry) {
        this.isWordNetEntry = isWordNetEntry;
    }

    @Override
    public List<TermSenseDTO> getSynonyms() {
        return synonyms;
    }

    @Override
    public void setSynonyms(List<TermSenseDTO> synonyms) {
        this.synonyms = synonyms;
    }

    @Override
    public TermSenseDTO getParent() {
        return parent;
    }

    @Override
    public void setParent(TermSenseDTO parent) {
        this.parent = parent;
    }
}
