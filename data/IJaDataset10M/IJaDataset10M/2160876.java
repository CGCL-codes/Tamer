package playground.anhorni.locationchoice.analysis;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import org.apache.log4j.Logger;
import org.matsim.gbl.Gbl;
import org.matsim.interfaces.basic.v01.population.BasicPlanElement;
import org.matsim.interfaces.core.v01.Activity;
import org.matsim.interfaces.core.v01.Person;
import org.matsim.interfaces.core.v01.Plan;
import org.matsim.interfaces.core.v01.Population;
import org.matsim.network.MatsimNetworkReader;
import org.matsim.network.NetworkLayer;
import org.matsim.population.MatsimPopulationReader;
import org.matsim.population.PopulationImpl;
import org.matsim.population.PopulationReader;
import org.matsim.utils.io.IOUtils;
import org.matsim.utils.misc.Counter;

/**
 * @author anhorni
 */
public class ActChainsPlansAnalyzer {

    private Population plans = null;

    private NetworkLayer network = null;

    private static final Logger log = Logger.getLogger(ActChainsPlansAnalyzer.class);

    /**
	 * @param
	 *  - path of the plans file
	 */
    public static void main(final String[] args) {
        if (args.length < 1 || args.length > 1) {
            System.out.println("Too few or too many arguments. Exit");
            System.exit(1);
        }
        String plansfilePath = args[0];
        log.info(plansfilePath);
        String networkfilePath = "./input/network.xml";
        ActChainsPlansAnalyzer analyzer = new ActChainsPlansAnalyzer();
        analyzer.init(plansfilePath, networkfilePath);
        analyzer.analyze();
    }

    private void init(final String plansfilePath, final String networkfilePath) {
        this.network = (NetworkLayer) Gbl.getWorld().createLayer(NetworkLayer.LAYER_TYPE, null);
        new MatsimNetworkReader(this.network).readFile(networkfilePath);
        log.info("network reading done");
        this.plans = new PopulationImpl(false);
        final PopulationReader plansReader = new MatsimPopulationReader(this.plans);
        plansReader.readFile(plansfilePath);
        log.info("plans reading done");
    }

    private void analyze() {
        try {
            final String header = "Person_id\tnumberOfSL";
            final BufferedWriter out = IOUtils.getBufferedWriter("./output/actchainsplananalysis.txt");
            out.write(header);
            out.newLine();
            Iterator<Person> person_iter = this.plans.getPersons().values().iterator();
            Counter counter = new Counter(" person # ");
            while (person_iter.hasNext()) {
                Person person = person_iter.next();
                counter.incCounter();
                Plan selectedPlan = person.getSelectedPlan();
                final List<? extends BasicPlanElement> actslegs = selectedPlan.getPlanElements();
                int countSL = 0;
                for (int j = 0; j < actslegs.size(); j = j + 2) {
                    final Activity act = (Activity) actslegs.get(j);
                    if (act.getType().startsWith("s") || act.getType().startsWith("l")) {
                        countSL++;
                    } else if (act.getType().startsWith("h") || act.getType().startsWith("w") || act.getType().startsWith("e")) {
                        if (countSL > 0) {
                            out.write(person.getId().toString() + "\t" + String.valueOf(countSL) + "\n");
                            countSL = 0;
                        }
                    }
                }
            }
            out.close();
        } catch (final IOException e) {
            Gbl.errorMsg(e);
        }
    }
}
