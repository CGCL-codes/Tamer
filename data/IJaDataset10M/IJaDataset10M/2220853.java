package playground.dgrether.satellic;

import java.util.Arrays;
import org.matsim.contrib.otfvis.OTFVis;
import org.matsim.core.config.Config;
import org.matsim.core.config.MatsimConfigReader;
import org.matsim.core.config.groups.QSimConfigGroup;
import org.matsim.core.controler.Controler;
import org.matsim.core.controler.events.BeforeMobsimEvent;
import org.matsim.core.controler.listener.BeforeMobsimListener;
import playground.dgrether.DgOTFVisConfigWriter;
import playground.dgrether.DgPaths;

public class DgSatellicWithindayTestStarter {

    public static void main(String[] args) {
        final String configfile = DgPaths.EXAMPLEBASE + "equil/configPlans100.xml";
        Config config = new Config();
        config.addCoreModules();
        MatsimConfigReader confReader = new MatsimConfigReader(config);
        confReader.readFile(configfile);
        config.controler().setLastIteration(0);
        config.addQSimConfigGroup(new QSimConfigGroup());
        config.controler().setSnapshotFormat(Arrays.asList("otfvis"));
        config.getQSimConfigGroup().setSnapshotPeriod(10.0);
        config.getQSimConfigGroup().setSnapshotStyle("queue");
        Controler controler = new Controler(config);
        controler.setOverwriteFiles(true);
        controler.addControlerListener(new BeforeMobsimListener() {

            @Override
            public void notifyBeforeMobsim(BeforeMobsimEvent event) {
                event.getControler().setMobsimFactory(new DgWithindayMobsimFactory());
            }
        });
        controler.run();
        controler.addControlerListener(new DgOTFVisConfigWriter());
        String outdir = controler.getConfig().controler().getOutputDirectory();
        String file = controler.getControlerIO().getIterationFilename(0, "otfvis.mvi");
        OTFVis.playMVI(file);
    }
}
