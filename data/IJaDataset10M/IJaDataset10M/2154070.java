package pl.edu.mimuw.mas.agent;

import jade.content.onto.basic.Action;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.edu.mimuw.mas.chart.Chart;
import pl.edu.mimuw.mas.ontology.VisualizationCoreOntology;
import pl.edu.mimuw.mas.ontology.concept.LogEntry;
import pl.edu.mimuw.mas.util.Position;

public abstract class AbstractAgent extends ClientAgent {

    private static final long serialVersionUID = 13104110539297165L;

    protected Chart chart;

    protected Position position;

    protected int r, g, b;

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    public void log(String stringMsg) {
        try {
            ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
            msg.addReceiver(getLogAgent());
            msg.setOntology(VisualizationCoreOntology.ONTOLOGY_NAME);
            msg.setLanguage(FIPANames.ContentLanguage.FIPA_SL);
            Action a = new Action(getAID(), new LogEntry(getName(), stringMsg));
            getContentManager().fillContent(msg, a);
            send(msg);
        } catch (Exception e) {
            logger.error("{}: Błąd podczas logowania", getName(), e);
        }
    }

    public Position getPosition() {
        return position;
    }

    public int getR() {
        return r;
    }

    public int getG() {
        return g;
    }

    public int getB() {
        return b;
    }

    public void setColor(int r, int g, int b) {
        this.r = r;
        this.g = g;
        this.b = b;
    }

    public Chart getChart() {
        return chart;
    }

    public void setChart(Chart chart) {
        this.chart = chart;
    }
}
