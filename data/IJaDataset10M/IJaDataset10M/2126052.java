package plumber.context;

import java.util.List;
import plumber.common.WorkItem;

/**
 * ContextConverter is responsible for converting of Context objects an WorkItem objects to and from the storable form (a string)
 * @author mgarber
 *
 */
public interface ContextConverter {

    public FlowContext contextFromStorableString(String stored);

    public String toStorableString(FlowContext context);

    public WorkItem itemFromStorableString(String stored);

    public String toStorableString(WorkItem item);

    public List<WorkItem> itemsFromStorableString(List<String> stored);

    public List<String> toStorableStrings(List<WorkItem> items);
}
