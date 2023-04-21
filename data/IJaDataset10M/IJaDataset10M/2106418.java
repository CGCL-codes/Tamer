package com.privilege.admin;

import java.util.*;

public interface EntityConfigurationLoader {

    public EntityConfiguration getEntityTableProperties(String entityName);

    public Map getAllTableProperties();

    public List getEntitiesName();

    public void refreshData();
}
