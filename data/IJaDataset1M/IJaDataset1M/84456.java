package playground.johannes.eut;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.matsim.controler.Controler;
import org.matsim.controler.events.IterationStartsEvent;
import org.matsim.controler.listener.IterationStartsListener;
import org.matsim.gbl.Gbl;
import org.matsim.network.Link;
import org.matsim.network.NetworkChangeEvent;
import org.matsim.network.NetworkLayer;
import org.matsim.network.NetworkChangeEvent.ChangeType;
import org.matsim.network.NetworkChangeEvent.ChangeValue;

/**
 * @author illenberger
 *
 */
public class RandomIncidentSimulator implements IterationStartsListener {

    private NetworkLayer network;

    private final double incidentProba;

    private double capReduction = 1;

    private int startIteration = 0;

    private final List<Link> links = new LinkedList<Link>();

    private BufferedWriter writer;

    public RandomIncidentSimulator(NetworkLayer network, double proba) {
        this.network = network;
        this.incidentProba = proba;
        try {
            this.writer = new BufferedWriter(new FileWriter(Controler.getOutputFilename("incidents.txt")));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addLink(Link link) {
        this.links.add(link);
    }

    public void setCapReduction(double factor) {
        this.capReduction = factor;
    }

    public double getCapReduction() {
        return this.capReduction;
    }

    public void setStartIteration(int iteration) {
        this.startIteration = iteration;
    }

    public int getStartIteration() {
        return this.startIteration;
    }

    public void notifyIterationStarts(int iteration) {
        try {
            this.writer.write(String.valueOf(iteration));
            this.writer.write("\t");
            List<NetworkChangeEvent> events = new ArrayList<NetworkChangeEvent>(links.size() * 2);
            for (Link link : this.links) {
                Gbl.random.nextDouble();
                if ((Gbl.random.nextDouble() < this.incidentProba) && (iteration >= this.startIteration)) {
                    NetworkChangeEvent e1 = new NetworkChangeEvent(0);
                    e1.addLink(link);
                    e1.setFlowCapacityChange(new ChangeValue(ChangeType.FACTOR, capReduction));
                    events.add(e1);
                    NetworkChangeEvent e2 = new NetworkChangeEvent(86400);
                    e2.addLink(link);
                    e2.setFlowCapacityChange(new ChangeValue(ChangeType.FACTOR, 1.0 / capReduction));
                    events.add(e2);
                    this.writer.write("\t");
                    this.writer.write(link.getId().toString());
                }
            }
            network.setNetworkChangeEvents(events);
            this.writer.newLine();
            this.writer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void notifyIterationStarts(IterationStartsEvent event) {
        notifyIterationStarts(event.getIteration());
    }
}
