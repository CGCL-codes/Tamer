package jstudio.db;

import java.io.Serializable;
import java.util.Map;

public interface DatabaseObject extends Serializable {

    public Long getId();

    public void setId(Long l);

    public Map<String, String> getPrintData();
}
