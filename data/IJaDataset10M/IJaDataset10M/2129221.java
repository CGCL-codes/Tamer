package com.entelience.probe.compliance;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import com.entelience.objects.probes.ProbeParameter;
import com.entelience.probe.FileImport;
import com.entelience.probe.FileState;
import com.entelience.probe.LocalFileState;
import com.entelience.probe.NoConsumeException;
import com.entelience.probe.TextFileProbe;
import com.entelience.sql.Db;

/**
 * probe for importing Promisec Spectator CSV files
 */
public class PromisecSpectator extends TextFileProbe {

    public String getName() {
        return "Promisec Spectator";
    }

    public void cliConfigure(Db db) {
    }

    protected void prepare() throws Exception {
    }

    @Override
    public List<ProbeParameter> getProbeParameters() {
        List<ProbeParameter> prefs = super.getProbeParameters();
        prefs.add(new ProbeParameter(getClass().getName(), "antivirusNames", new String[] { "McAfee Framework Service", "Virus Scan Enterprise" }));
        prefs.add(new ProbeParameter(getClass().getName(), "sendIncidentForNewIPsWithoutLocations", false));
        return prefs;
    }

    @Override
    public void configureProbeWithParameters(Db statusDb, Map<String, ProbeParameter> pp) throws Exception {
        antivirusNames = pp.get(getClass().getName() + ".antivirusNames").getEffectiveValueArr();
        sendIncidentForNewIPsWithoutLocations = pp.get(getClass().getName() + ".sendIncidentForNewIPsWithoutLocations").getEffectiveValueB();
    }

    /**
     * need the date provided as metadata
     *
     */
    protected void checkWouldConsume(Db statusDb, FileState fs) throws NoConsumeException {
        if (!fs.hasMetaDate()) {
            throw new NoConsumeException(fs + " does not have the required date meta-data");
        }
    }

    protected boolean isAssetProbe() {
        return false;
    }

    protected boolean checkFormat(LocalFileState state, InputStream is) {
        return true;
    }

    private boolean sendIncidentForNewIPsWithoutLocations;

    private String[] antivirusNames;

    protected void transferProbePreferences(FileImport fi) throws Exception {
        PromisecSpectatorImport imp = (PromisecSpectatorImport) fi;
        imp.setAntivirusNames(antivirusNames);
        imp.setSendIncidentForNewIPsWithoutLocations(sendIncidentForNewIPsWithoutLocations);
    }

    protected FileImport getFileImport() {
        return new PromisecSpectatorImport();
    }
}
