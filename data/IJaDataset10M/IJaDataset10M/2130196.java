package eu.redseeds.sc.terminology.model;

import java.util.List;

public interface TermSenseDTO {

    public String getSense();

    public void setSense(String sense);

    public long getUid();

    public void setUid(long uid);

    public String getType();

    public void setType(String type);

    public String getLemma();

    public void setLemma(String lemma);

    public void setWordNetEntry(boolean isWordNetEntry);

    public boolean isWordNetEntry();

    public List<TermSenseDTO> getSynonyms();

    public void setSynonyms(List<TermSenseDTO> synonyms);

    public void setParent(TermSenseDTO parent);

    public TermSenseDTO getParent();
}
