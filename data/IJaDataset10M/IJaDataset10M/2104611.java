package playground.andreas.bln;

import org.matsim.api.basic.v01.BasicScenarioImpl;
import org.matsim.api.basic.v01.Id;
import org.matsim.core.api.population.Person;
import org.matsim.core.api.population.Population;
import org.matsim.core.basic.v01.IdImpl;
import org.matsim.core.gbl.Gbl;
import org.matsim.core.network.MatsimNetworkReader;
import org.matsim.core.network.NetworkLayer;
import org.matsim.core.population.MatsimPopulationReader;
import org.matsim.core.population.PopulationImpl;
import org.matsim.core.population.PopulationReader;

/** 
 * Create numberOfcopies additional persons for a given plan.
 * 
 * @author aneumann
 *
 */
public class DuplicatePlans extends NewPopulation {

    private int numberOfCopies;

    public DuplicatePlans(Population plans, String filename, int numberOfCopies) {
        super(plans, filename);
        this.numberOfCopies = numberOfCopies;
    }

    @Override
    public void run(Person person) {
        this.popWriter.writePerson(person);
        Id personId = person.getId();
        for (int i = 1; i < this.numberOfCopies + 1; i++) {
            person.setId(new IdImpl(personId.toString() + "X" + i));
            this.popWriter.writePerson(person);
        }
    }

    public static void main(final String[] args) {
        Gbl.startMeasurement();
        BasicScenarioImpl sc = new BasicScenarioImpl();
        Gbl.setConfig(sc.getConfig());
        String networkFile = "./bb_cl.xml.gz";
        String inPlansFile = "./plan_korridor.xml.gz";
        String outPlansFile = "./plan_korridor_50x.xml.gz";
        NetworkLayer net = new NetworkLayer();
        new MatsimNetworkReader(net).readFile(networkFile);
        Population inPop = new PopulationImpl();
        PopulationReader popReader = new MatsimPopulationReader(inPop, net);
        popReader.readFile(inPlansFile);
        DuplicatePlans dp = new DuplicatePlans(inPop, outPlansFile, 49);
        dp.run(inPop);
        dp.writeEndPlans();
        Gbl.printElapsedTime();
    }
}
