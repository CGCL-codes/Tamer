package com.entelience.raci.admin;

import com.entelience.raci.RaciMatrix;
import com.entelience.objects.raci.RaciObjectType;
import com.entelience.objects.raci.RACI;
import com.entelience.objects.raci.RaciableBean;
import com.entelience.sql.Db;

/**
 * Specific RACI class for Location
 */
public class RaciIncident extends RaciAdmin {

    protected static final RaciObjectType raciObjectType = RaciObjectType.INCIDENT;

    public RaciObjectType getRaciObjectType() {
        return raciObjectType;
    }

    @Override
    protected void computeRaciObjectId(Db db) throws Exception {
    }
}
