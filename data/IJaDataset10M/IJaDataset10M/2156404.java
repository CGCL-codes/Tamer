package examples.statistics;

import java.io.PrintStream;
import org.tm4j.topicmap.Association;
import org.tm4j.topicmap.Topic;
import org.tm4j.topicmap.TopicMap;
import org.tm4j.topicmap.cmd.StatsProcessor;

/**
 * This is some simple example on how to use the StatsProcessor 
 * which just counts the number of topics in the selected tm
 * 
 * @author Harald Kuhn, harald-kuhn@web.de
 */
public class TopicCountStatsProcessor implements StatsProcessor {

    private int topicCount;

    public void initialise() {
        topicCount = 0;
    }

    public void setTopicMap(TopicMap tm) {
    }

    public boolean willProcessTopics() {
        return true;
    }

    public void processTopic(Topic t) {
        topicCount++;
    }

    public boolean willProcessAssociations() {
        return false;
    }

    public void processAssociation(Association a) {
    }

    public void writeStats(int statsStyle, PrintStream out) {
        out.print("counted " + topicCount + "topics");
    }
}
