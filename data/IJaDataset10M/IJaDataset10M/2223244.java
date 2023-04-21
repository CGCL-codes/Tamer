package org.opennms.netmgt.collectd;

import org.opennms.netmgt.model.RrdRepository;

public class OneToOnePersister extends BasePersister {

    public OneToOnePersister(ServiceParameters params, RrdRepository repository) {
        super(params, repository);
    }

    public void visitAttribute(CollectionAttribute attribute) {
        pushShouldPersist(attribute);
        if (shouldPersist()) {
            createBuilder(attribute.getResource(), attribute.getName(), attribute.getAttributeType());
            storeAttribute(attribute);
        }
    }

    public void completeAttribute(CollectionAttribute attribute) {
        if (shouldPersist()) {
            commitBuilder();
        }
        popShouldPersist();
    }
}
