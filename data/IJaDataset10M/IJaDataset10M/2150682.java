package org.systemsbiology.apps.corragui.server.storable;

import java.io.File;
import org.apache.log4j.Logger;
import org.systemsbiology.apps.corragui.domain.StatsSetup;
import org.systemsbiology.apps.corragui.server.CorraWebappConstants;
import org.systemsbiology.apps.corragui.server.corraProjectXml.CorraStepXmlWriter;

public class StorableStatsSetup implements IStorable {

    private StatsSetup setup;

    private String projDir;

    private static final Logger log = Logger.getLogger(StorableStatsSetup.class.getName());

    public StorableStatsSetup(StatsSetup setup, String projDir) {
        this.setup = setup;
        this.projDir = projDir;
    }

    public boolean store() {
        return store(log);
    }

    public boolean store(Logger log) {
        String file = projDir + File.separator + CorraWebappConstants.STATS_SETUP_XML;
        return (new CorraStepXmlWriter().writeStatsSetup(setup, file));
    }
}
