package tutorial.programming.example10PluggablePlanStrategyFromFile;

import org.matsim.core.config.Config;
import org.matsim.core.controler.Controler;
import org.matsim.core.config.ConfigUtils;

class Main {

    public static void main(final String[] args) {
        Config config;
        if (args.length == 0) {
            config = ConfigUtils.loadConfig("examples/tutorial/programming/pluggablePlanStrategy-config.xml");
        } else {
            config = ConfigUtils.loadConfig(args[0]);
        }
        final Controler controler = new Controler(config);
        controler.setOverwriteFiles(true);
        controler.run();
    }
}
