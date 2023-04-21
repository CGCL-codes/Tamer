package uk.ac.ebi.intact.dataexchange.psimi.solr.converter.impl;

import psidev.psi.mi.tab.model.builder.Field;
import uk.ac.ebi.intact.dataexchange.psimi.solr.converter.FieldFilter;

/**
 * Accepts the fields that match the type passed to the constructor.
 *
 * @author Bruno Aranda (baranda@ebi.ac.uk)
 * @version $Id$
 */
public class TypeFieldFilter implements FieldFilter {

    private String type;

    public TypeFieldFilter(String type) {
        this.type = type;
    }

    public boolean acceptField(Field field) {
        return type.equals(field.getType());
    }
}
