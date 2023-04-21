package ms.jasim.framework;

import java.util.ArrayList;
import java.util.List;

public class DefaultEventTypeProvider implements IEventTypeProvider {

    private List<JEventType> eventType = new ArrayList<JEventType>();

    public static DefaultEventTypeProvider createStdEventSet() {
        DefaultEventTypeProvider result = new DefaultEventTypeProvider();
        result.eventType.add(new GoalFailEventType());
        result.eventType.add(new InstanceIntroduceEventType());
        return result;
    }

    @Override
    public List<JEventType> getEventTypes() {
        return eventType;
    }
}
