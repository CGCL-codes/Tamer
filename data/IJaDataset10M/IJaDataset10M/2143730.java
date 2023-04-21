package metrics4Asterisk.parse;

import metrics4Asterisk.parse.exception.AgentWarning;
import metrics4Asterisk.metrics.AgentMetric;
import metrics4Asterisk.parse.enumerations.EventWarning;
import metrics4Asterisk.parse.exception.NoQueueStartEventException;
import metrics4Asterisk.parse.exception.NoPauseStartEventException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.log4j.Logger;

/**
 * A class to map an Agent's queue events. This includes login, logout, pause and un pause.
 * 
 * <pre>
 * Example:
 *
 *      LogParser agentParser = new LogParser();
 *
 *      String queueName = "superqueue";
 *      Set<String> agentNames = new HashSet<String>();
 *      // ... add values to agentNames
 *      AgentMapper agentMapper = new AgentMapper(agentNames, superqueue);
 * 
 *      // do the parsing
 *      agentParser.setLogMapper(agentMapper);
 *      CsvReader reader;
 *      // ... initailize the reader object
 *      agentParser.parse(cFromDate, cToDate, reader);
 * 
 *      // finish must be called after parse
 *      agentMapper.finish();
 * 
 *      //all is done, get the map 
 *      Map<String, Object> map = agentMapper.getMap();
 * 
 * </pre>
 * @author lances
 */
public class AgentMapper extends LogMapper<AgentMetric> {

    private Map<String, AgentMetric> map;

    private Set<String> validAgents;

    private static final Logger logger = Logger.getLogger(AgentMapper.class);

    /**
     * A list for events that have no match. These are start events like ADDMEMBER and PAUSE.
     * Without them it is impossible to have accurate results. Warnings of these events are put into this field.
     * If there are end events like REMOVEMEMBER or UNPAUSE that are outside of the data the
     */
    private List<AgentWarning> warnings;

    /**
     * Construct an object that will map all calls in queue names provided in the validQueues argument
     * and calls to agents with extensions provided in the validAgents argument.
     * @param validAgents
     * @param validQueues
     */
    public AgentMapper(final Set<String> validAgents, final Set<String> validQueues) {
        this.validAgents = validAgents;
        super.setValidValues(validQueues);
        map = new HashMap<String, AgentMetric>(this.validAgents.size());
        Iterator<String> keyIt = validAgents.iterator();
        while (keyIt.hasNext()) {
            String key = keyIt.next();
            AgentMetric agentMetric = new AgentMetric();
            agentMetric.setExtension(key);
            map.put(key, agentMetric);
            logger.debug("Added " + key);
        }
        warnings = new ArrayList<AgentWarning>(0);
    }

    /**
     * This method will add the following rows to a map:
     * <pre>
     * 1203352172|NONE|superqueue|Local/6040@internal/n|PAUSE|
     * 1203351421|NONE|superqueue|Local/6042@internal/n|UNPAUSE|
     * 1203350550|MANAGER|internetTechSupportRinger|Local/7010@internal/n|REMOVEMEMBER|
     * 1203350550|MANAGER|internetTechSupportRinger|Local/7010@internal/n|ADDMEMBER|
     * 
     * Fields in an Asterisk queue log 
     * epoch timestamp of listed action 
     * uniqueid of call 
     * queue name 
     * bridged channel 
     * event
     * </pre>
     * @param rowValues an array of row values
     * @param logRecordTime the time in milliseconds for this row's data
     */
    public void processRow(final String[] rowValues, final long logRecordTime) {
        try {
            String extension = rowValues[3];
            String queueName = rowValues[2];
            if (!validAgents.contains(extension) || !super.getValidValues().contains(queueName)) {
                return;
            }
            String event = rowValues[4];
            AgentMetric agentMetric = map.get(extension);
            if (agentMetric == null) {
                logger.error(extension + ": not in map but should be.");
                return;
            }
            if (event.equals("ADDMEMBER")) {
                logger.debug("login to " + extension);
                agentMetric.startQueueTime(logRecordTime);
            } else if (event.equals("REMOVEMEMBER")) {
                logger.debug("logout to " + extension);
                try {
                    agentMetric.stopQueueTime(logRecordTime);
                } catch (NoQueueStartEventException e) {
                    logger.warn("QUEUE_NO_START_EVENT extension " + extension);
                    warnings.add(new AgentWarning(extension, EventWarning.QUEUE_NO_START_EVENT));
                }
            } else if (event.equals("PAUSE")) {
                logger.debug("pause to " + extension);
                agentMetric.startPauseTime(logRecordTime);
            } else if (event.equals("UNPAUSE")) {
                logger.debug("unpause to " + extension);
                try {
                    agentMetric.stopPauseTime(logRecordTime);
                } catch (NoPauseStartEventException e) {
                    logger.warn("PAUSE_NO_START_EVENT extension " + extension);
                    warnings.add(new AgentWarning(extension, EventWarning.PAUSE_NO_START_EVENT));
                }
            }
        } catch (Exception e) {
            logger.error("Row ignored by exception ", e);
        }
    }

    /**
     * The results from the parse
     * @return
     */
    public Map<String, AgentMetric> getMap() {
        return map;
    }

    public Set<String> getValidAgents() {
        return validAgents;
    }

    public void setValidAgents(Set<String> validAgents) {
        this.validAgents = validAgents;
    }

    public List<AgentWarning> getWarnings() {
        return warnings;
    }

    public void setWarnings(List<AgentWarning> warnings) {
        this.warnings = warnings;
    }
}
