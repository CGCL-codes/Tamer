package org.mushroomdb.query.impl;

import org.mushroomdb.catalog.Catalog;
import org.mushroomdb.catalog.Table;
import org.mushroomdb.engine.EvaluationEngine;
import org.mushroomdb.query.Query;

/**
 * @author mchiodi
 *
 */
public class DropTableQuery extends Query {

    private Table table;

    /**
	 * Constructor
	 */
    public DropTableQuery(Table table) {
        super();
        this.table = table;
    }

    /**
	 * @see org.mushroomdb.query.Query#execute(org.mushroomdb.engine.EvaluationEngine)
	 */
    public Object execute(EvaluationEngine evaluationEngine) {
        Catalog catalog = Catalog.getInstance();
        boolean result = catalog.dropTable(this.table);
        return new Boolean(result);
    }
}
