package bop.datamodel;

import java.util.*;

public interface EvidenceI {

    public void setResultSpan(ResultI rs);

    public ResultI getResultSpan();

    public void setType(String type);

    public String getType();

    public void setDescription(String description);

    public String getDescription();
}
