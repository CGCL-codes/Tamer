package yapgen.base.knowledge.event;

import org.json.simple.parser.ParseException;
import yapgen.base.Entity;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import yapgen.base.EntityContainer;
import yapgen.base.Word;
import yapgen.base.WordDictionary;

/**
 *
 * @author riccardo
 */
public class EventRule {

    private EventEntityTransformation entityTransformation = new EventEntityTransformation();

    private EventEntityFilter entityFilter = new EventEntityFilter();

    public EventEntityFilter getEntityFilter() {
        return entityFilter;
    }

    public EventEntityTransformation getEntityTransformation() {
        return entityTransformation;
    }

    public Collection<Event> generateEventCandidates(Collection<Entity> entities) {
        Collection<Event> eventCandidates = new ArrayList<Event>();
        return eventCandidates;
    }

    private static EventRule contextEventRule;

    private static EventClass contextEventClass;

    private static void setContext(EventRule contextEventRule, EventClass contextEventClass) {
        EventRule.contextEventRule = contextEventRule;
        EventRule.contextEventClass = contextEventClass;
    }

    private static boolean isSetContext() {
        return contextEventRule != null && contextEventClass != null;
    }

    public static String CLI_COMMAND_withNewEventRule(String eventClassName) {
        Word eventClassWord = WordDictionary.getInstance().get(eventClassName);
        EventClass eventClass = (EventClass) EntityContainer.getInstance().get(eventClassWord);
        EventRule eventRule = new EventRule();
        eventClass.getRules().add(eventRule);
        setContext(eventRule, eventClass);
        return EventClass.class.getSimpleName() + " " + eventClassName + " rule context set (rule " + eventClass.getRules().size() + ")";
    }

    public static String CLI_COMMAND_setFilter(String type, String jsonFilterMap) throws ParseException {
        if (!isSetContext()) {
            return "ERROR: must start rule context by the with-new-event-rule command";
        } else {
            EventEntityFilter.TypeEnum typeEnum = EventEntityFilter.TypeEnum.valueOf(type.toUpperCase());
            switch(typeEnum) {
                case ENTITY:
                    {
                        JSONObject filterMap = (JSONObject) JSONValue.parseWithException(jsonFilterMap);
                        contextEventRule.getEntityFilter().getEntityWeightsMap().clear();
                        for (Object key : filterMap.keySet()) {
                            String attributeName = (String) key;
                            Double attributeWeight = ((Number) filterMap.get(key)).doubleValue();
                            Word attributeWord = WordDictionary.getInstance().get(attributeName);
                            contextEventRule.getEntityFilter().getEntityWeightsMap().put(attributeWord, attributeWeight);
                        }
                    }
                    break;
                case RELATION:
                    {
                        JSONObject filterMap = (JSONObject) JSONValue.parseWithException(jsonFilterMap);
                        contextEventRule.getEntityFilter().getEntityWeightsMap().clear();
                        for (Object relationKey : filterMap.keySet()) {
                            String relationClassName = (String) relationKey;
                            Word relationClassWord = WordDictionary.getInstance().get(relationClassName);
                            List relationFilterArray = (List) filterMap.get(relationKey);
                            for (Object key : relationFilterArray) {
                                String attributeName = (String) key;
                                Double attributeValue = ((Number) filterMap.get(key)).doubleValue();
                                Word attributeWord = WordDictionary.getInstance().get(attributeName);
                                if (contextEventRule.getEntityFilter().getRelationWeightsMap().get(relationClassWord) == null) {
                                    contextEventRule.getEntityFilter().getRelationWeightsMap().put(relationClassWord, new TreeMap<Word, Double>());
                                }
                                contextEventRule.getEntityFilter().getRelationWeightsMap().get(relationClassWord).put(attributeWord, attributeValue);
                            }
                        }
                    }
                    break;
            }
            return type + " filter set for context rule";
        }
    }

    public static String CLI_COMMAND_setTransformation(String type, String jsonTransformationMap) throws ParseException {
        if (!isSetContext()) {
            return "ERROR: must start rule context by the with-new-event-rule command";
        } else {
            EventEntityTransformation.TypeEnum typeEnum = EventEntityTransformation.TypeEnum.valueOf(type.toUpperCase());
            switch(typeEnum) {
                case ACTOR:
                    {
                    }
                    break;
                case INFLUENCED:
                    {
                    }
                    break;
            }
            return type + " transformation set for context rule";
        }
    }
}
