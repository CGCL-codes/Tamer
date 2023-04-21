package com.entelience.probe.virus;

import java.io.InputStream;
import java.util.Map;
import com.entelience.objects.probes.ProbeParameter;
import com.entelience.probe.FileImport;
import com.entelience.probe.FileState;
import com.entelience.probe.LocalFileState;
import com.entelience.probe.TextFileProbe;
import com.entelience.sql.Db;

/**
 * get threat level from ISS XForce Threat Analysis Service http://xforce.iss.net/xftas/
 */
public class ISSAlertCon extends TextFileProbe {

    public String getName() {
        return "ISS Alert Con";
    }

    public void cliConfigure(Db statusDb) {
    }

    @Override
    public void configureProbeWithParameters(Db statusDb, Map<String, ProbeParameter> pp) throws Exception {
    }

    @Override
    protected void transferProbePreferences(FileImport imp) throws Exception {
    }

    protected void prepare() throws Exception {
    }

    protected void checkWouldConsume(Db statusDb, FileState fs) {
    }

    protected boolean isAssetProbe() {
        return false;
    }

    protected boolean checkFormat(LocalFileState state, InputStream is) throws Exception {
        return true;
    }

    protected FileImport getFileImport() {
        return new ISSAlertConImport();
    }
}
