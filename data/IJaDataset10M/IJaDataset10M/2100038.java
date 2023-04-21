package com.ohua.engine.operators.categories;

import com.ohua.engine.resource.management.ResourceConnection;

public interface TransactionalIOOperator {

    public String getResourceID();

    public ResourceConnection getConnection();

    public int getCommitPeriod();

    public boolean strictCommitRequirements();
}
