package be.kuleuven.cs.mop.domain.model;

import java.util.Collection;
import java.util.Map;
import be.kuleuven.cs.mop.domain.model.impl.FieldType;
import be.kuleuven.cs.mop.domain.model.impl.Interval;

public interface TaskType {

    public Map<String, FieldType> getFieldsTemplate();

    /**
	 * Returns the name of this <code>TaskType</code>
	 */
    public String getName();

    public Collection<? extends UserType> getOwnerUserTypes();

    public boolean isCreatableBy(UserType userType);

    public Map<? extends ResourceType, Interval> getRequiredResourceTypes();

    public Map<? extends UserType, Interval> getHelperUserTypesConstraints();
}
