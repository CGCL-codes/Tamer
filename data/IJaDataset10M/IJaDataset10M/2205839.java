package org.matsim.contrib.evacuation.run;

import java.util.HashMap;
import org.apache.log4j.Logger;
import org.matsim.api.core.v01.Id;
import org.matsim.contrib.evacuation.base.EvacuationAreaFileReader;
import org.matsim.contrib.evacuation.base.EvacuationAreaLink;
import org.matsim.contrib.evacuation.base.EvacuationNetGenerator;
import org.matsim.contrib.evacuation.base.EvacuationPlansGenerator;
import org.matsim.contrib.evacuation.config.EvacuationConfigGroup;
import org.matsim.core.basic.v01.IdImpl;
import org.matsim.core.config.Module;
import org.matsim.core.controler.Controler;
import org.matsim.core.network.NetworkWriter;

/**
 * @author glaemmel
 */
public class EvacuationQSimControler extends Controler {

    private final HashMap<Id, EvacuationAreaLink> evacuationAreaLinks = new HashMap<Id, EvacuationAreaLink>();

    private static final Logger log = Logger.getLogger(EvacuationQSimControler.class);

    public EvacuationQSimControler(final String[] args) {
        super(args);
    }

    @Override
    protected void setUp() {
        Module m = this.config.getModule("evacuation");
        EvacuationConfigGroup ec = new EvacuationConfigGroup(m);
        this.config.getModules().put("evacuation", ec);
        String evacuationAreaLinksFile = ((EvacuationConfigGroup) this.config.getModule("evacuation")).getEvacuationAreaFile();
        new EvacuationAreaFileReader(this.evacuationAreaLinks).readFile(evacuationAreaLinksFile);
        log.info("generating initial evacuation plans... ");
        new EvacuationNetGenerator(this.network, this.config).run();
        new EvacuationPlansGenerator(this.population, this.network, this.network.getLinks().get(new IdImpl("el1"))).run();
        log.info("done");
        log.info("writing network xml file... ");
        new NetworkWriter(this.network).write(getControlerIO().getOutputFilename("evacuation_net.xml"));
        log.info("done");
        super.setUp();
    }

    public static void main(final String[] args) {
        final Controler controler = new EvacuationQSimControler(args);
        controler.run();
        System.exit(0);
    }
}
