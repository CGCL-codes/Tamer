package sc.fgrid.engine;

import javax.xml.bind.annotation.XmlAccessType;

/**
 * The task information from within an agent (in servicescript). As
 * sc.fgrid.script.Task, but adds the agent type and the content of the script. See
 * https://jaxb.dev.java.net/guide/Adding_behaviors.html . The
 * XmlAccessType.NONE is needed to tell that fields and properties from this,
 * inherited class, are not to be serialized.
 */
@javax.xml.bind.annotation.XmlAccessorType(XmlAccessType.NONE)
public class Task extends sc.fgrid.script.Task {

    private String scriptContent;

    private Agent agent;

    public Task() {
    }

    void init(Agent agent, String scriptContent) {
        this.agent = agent;
        this.scriptContent = scriptContent;
    }

    /**
     * @return the script
     */
    String getScriptContent() {
        return scriptContent;
    }

    /**
     * @return the agent
     */
    public Agent getAgent() {
        return agent;
    }
}
