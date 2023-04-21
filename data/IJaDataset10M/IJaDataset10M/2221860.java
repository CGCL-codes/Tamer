package com.justin.jess.insurance;

import java.util.Iterator;
import jess.Filter;
import jess.JessException;
import jess.Rete;
import jess.WorkingMemoryMarker;

public class InsuranceEngine {

    private Rete engine;

    private WorkingMemoryMarker marker;

    private IDataRepository database;

    public InsuranceEngine(IDataRepository adatabase) throws JessException {
        engine = new Rete();
        engine.reset();
        engine.batch("insurance.clp");
        database = adatabase;
        engine.addAll(database.getDrivers());
        engine.addAll(database.getVehicles());
        engine.addAll(database.getVehicleCoverageTypes());
        marker = engine.mark();
    }

    @SuppressWarnings("unchecked")
    public Iterator run(Class clazz) throws JessException {
        engine.resetToMark(marker);
        engine.run();
        return engine.getObjects(new Filter.ByClass(clazz));
    }
}
