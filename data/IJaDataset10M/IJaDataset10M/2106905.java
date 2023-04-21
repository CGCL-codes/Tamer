package com.sun.ebxml.registry.query.filter;

import com.sun.ebxml.registry.*;
import org.oasis.ebxml.registry.bindings.query.*;
import org.oasis.ebxml.registry.bindings.rs.*;

/**
 * Class Declaration for ClassificationSchemeQueryProcessor
 * @see
 * @author Nikola Stojanovic
 */
public class ClassificationSchemeQueryProcessor extends RegistryEntryQueryProcessor {

    private ClassificationSchemeQueryType classificationSchemeQuery = null;

    protected String getName() {
        return "ClassificationScheme";
    }

    protected void setNativeQuery(RegistryObjectQueryType query) {
        classificationSchemeQuery = (ClassificationSchemeQuery) query;
        super.setNativeQuery((RegistryObjectQueryType) query);
    }

    protected void buildFilterClauses() throws RegistryException {
        convertClassificationSchemeFilter();
        super.buildFilterClauses();
    }

    protected void buildQueryClauses() throws RegistryException {
        super.buildQueryClauses();
    }

    protected void buildBranchClauses() throws RegistryException {
        super.buildBranchClauses();
    }

    private void convertClassificationSchemeFilter() throws RegistryException {
        if (classificationSchemeQuery.getClassificationSchemeFilter() != null) {
            whereClause = filterProcessor.addNativeWhereClause(whereClause, classificationSchemeQuery.getClassificationSchemeFilter());
        }
    }
}
