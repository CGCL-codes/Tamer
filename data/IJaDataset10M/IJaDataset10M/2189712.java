package ytex.ws;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "simServiceInfo")
public class SimServiceInfo {

    String conceptGraph;

    String description;

    public String getConceptGraph() {
        return conceptGraph;
    }

    public void setConceptGraph(String conceptGraph) {
        this.conceptGraph = conceptGraph;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public SimServiceInfo() {
        super();
    }

    public SimServiceInfo(String conceptGraph, String description) {
        super();
        this.conceptGraph = conceptGraph;
        this.description = description;
    }
}
