package org.zeroexchange.dataset.criteria.processor.filter.fulltext;

import java.util.ArrayList;
import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.zeroexchange.common.processors.DataProcessor;
import org.zeroexchange.model.collaboration.Contract;
import org.zeroexchange.model.resource.Resource;

/**
 * @author black
 *
 */
public class ContractSearchableFieldsProvider implements SearchableFieldsProvider, DataProcessor {

    @Autowired
    private SearchableFieldsFactory searchableFieldsFactory;

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<String> getSearchableFields() {
        final Collection<String> fields = new ArrayList<String>();
        fields.add(Contract.FIELD_TITLE);
        Collection<String> resourceFields = searchableFieldsFactory.getSearcheableFields(Resource.class);
        for (String resourceField : resourceFields) {
            fields.add(Contract.FIELD_RESOURCES + "." + resourceField);
        }
        return fields;
    }

    @Override
    public Class getProcessingClass() {
        return Contract.class;
    }
}
