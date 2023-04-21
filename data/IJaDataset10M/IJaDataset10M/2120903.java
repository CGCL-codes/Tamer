package playground.ou.scenario;

import org.apache.log4j.Logger;
import org.matsim.core.config.Config;
import org.matsim.core.gbl.Gbl;
import org.matsim.core.mobsim.cppdeqsim.BinaryEvents2TextEvents;
import org.matsim.core.mobsim.cppdeqsim.DEQSimControler;
import org.matsim.world.World;

public class IterationRun {

    static final String FILENAMEEVENTS = "events.txt.gz";

    private static final Logger log = Logger.getLogger(IterationRun.class);

    public static void main(String[] args) {
        Gbl.startMeasurement();
        String[] args2 = new String[args.length - 2];
        for (int i = 0; i < args.length - 2; i++) {
            args2[i] = args[i];
        }
        int nof_iters = Integer.parseInt(args[args.length - 2]);
        log.info(">>>>>> Number of Iterations: " + nof_iters);
        int strategy = Integer.parseInt(args[args.length - 1]);
        Config config = Gbl.createConfig(args2);
        World world = Gbl.createWorld();
        new FileOperate().saveFileToFile(config.getParam("greentimefractions", "inputGTFfile"), "./output/SIG_OPTI/greentimes_init.xml");
        log.info("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
        log.info("<<<<<<<<<< SIGNAL OPTIMIZATION  STRATEGY " + strategy + " START");
        log.info("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
        log.info("");
        for (int iteration = 0; iteration < nof_iters; iteration++) {
            log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
            log.info(">>>>>> light signal optimization itreration number " + iteration + " <<<<<<");
            log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
            log.info("");
            new FileOperate().deleteFile(config.controler().getOutputDirectory());
            System.out.println("			" + iteration + " ITERATION  START");
            if ((args == null) || (args.length == 0)) {
                System.out.println("No argument given!");
            } else {
                world.getLayers().clear();
                world.getRules().clear();
                DEQSimControler deqSimControler = new DEQSimControler(args);
                deqSimControler.run();
                int last_it = config.controler().getLastIteration();
                String in_events = config.controler().getOutputDirectory();
                in_events = in_events + "/ITERS/it." + last_it + "/" + last_it + ".deq_events.dat";
                BinaryEvents2TextEvents.convert(in_events, config.events().getInputFile());
            }
            new FileOperate().deleteFile(config.getParam("greentimefractions", "inputGTFfile"));
            switch(strategy) {
                case 0:
                    new TraditionalTrafficControl().run(iteration, 0, config);
                    break;
                case 1:
                    new TraditionalTrafficControl().run(iteration, 1, config);
                    break;
                case 2:
                    new TraditionalTrafficControl().run(iteration, 2, config);
                    break;
                case 3:
                    new TraditionalTrafficControl().run(iteration, 3, config);
                    break;
                case 4:
                    new AdaptiveTrafficControl().run(iteration, config);
                    break;
                default:
                    break;
            }
        }
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        log.info(">>>>>> SIGNAL OPTIMIZATION  STRATEGY " + strategy + " END");
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        log.info("");
        Gbl.printElapsedTime();
    }
}
