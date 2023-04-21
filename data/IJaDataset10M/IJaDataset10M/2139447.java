package net.javlov;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.jdom.Element;
import net.javlov.util.ObjectFactory;
import net.javlov.util.SAXReader;

public class XMLAgentBuilder {

    private List<Agent> agents;

    private PolicyFactory polFactory;

    private Iterator<Agent> agentItr;

    public Agent buildAgent() {
        if (agentItr.hasNext()) return agentItr.next();
        return null;
    }

    public void load(String configFile) {
        Element root = SAXReader.read(configFile).getRootElement();
        loadAgents(root.getChildren("agent"));
        agentItr = agents.iterator();
    }

    public void setPolicyFactory(PolicyFactory pf) {
        polFactory = pf;
    }

    private void loadAgents(List<Element> agentEls) {
        agents = new ArrayList<Agent>();
        Agent a;
        Element javaClass;
        int number;
        for (Element el : agentEls) {
            number = Integer.parseInt(el.getAttributeValue("number"));
            javaClass = el.getChild("javaclass");
            for (int i = 0; i < number; i++) {
                a = (Agent) ObjectFactory.createObject(javaClass);
                if (polFactory != null) a.setPolicy(polFactory.create());
                agents.add(a);
            }
        }
    }
}
