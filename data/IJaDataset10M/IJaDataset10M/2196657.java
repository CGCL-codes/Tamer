package uk.ac.lkl.migen.system.server.converter;

import uk.ac.lkl.common.util.reflect.GenericClass;
import uk.ac.lkl.migen.system.ai.um.TaskModelAttribute;
import uk.ac.lkl.migen.system.ai.um.TaskModelAttributeValue;

public class TaskModelAttributeValueXMLConverter extends AbstractAttributeValueXMLConverter<TaskModelAttribute, TaskModelAttributeValue> {

    public TaskModelAttributeValueXMLConverter() {
        super(TaskModelAttribute.class);
    }

    @Override
    public GenericClass<TaskModelAttributeValue> getEntityClass() {
        return GenericClass.getSimple(TaskModelAttributeValue.class);
    }

    @Override
    protected TaskModelAttributeValue createNewAttributeValue(TaskModelAttribute attribute, double value) {
        return new TaskModelAttributeValue(attribute, value);
    }
}
