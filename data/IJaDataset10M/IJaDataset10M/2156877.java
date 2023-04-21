package com.entelience.probe.httplog;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import com.entelience.objects.probes.ProbeParameter;
import com.entelience.probe.FileImport;
import com.entelience.probe.FileState;
import com.entelience.probe.LocalFileState;
import com.entelience.probe.Metadata;
import com.entelience.probe.NoConsumeException;
import com.entelience.probe.TextFileProbe;
import com.entelience.sql.Db;
import com.entelience.sql.DbHelper;

/**
 * 
 */
public class SunJWS extends GenericHttpLogProbe {

    public String getName() {
        return "Sun Java Web Server";
    }

    /** Retuns the vendor name */
    @Override
    public String getVendorName() {
        return "Oracle";
    }

    /** Returns the product name */
    @Override
    public String getProductName() {
        return "Java Web Server";
    }

    private String fields;

    private boolean addNewPaths = false;

    private int pathDepthLimit = 1;

    private boolean fullPath = false;

    private String[] excludePathPart;

    private String[] fileExtensions;

    private boolean sendIncidentForSpecificFilesWithoutReferers;

    @Override
    public List<ProbeParameter> getProbeParameters() {
        List<ProbeParameter> prefs = super.getProbeParameters();
        prefs.add(new ProbeParameter(getClass().getName(), "addNewPaths", false));
        prefs.add(new ProbeParameter(getClass().getName(), "pathDepthLimit", 1));
        prefs.add(new ProbeParameter(getClass().getName(), "fullPath", false));
        prefs.add(new ProbeParameter(getClass().getName(), "fields", ""));
        prefs.add(new ProbeParameter(getClass().getName(), "excludePathPart", new String[] {}));
        prefs.add(new ProbeParameter(getClass().getName(), "fileExtensions", new String[] {}));
        prefs.add(new ProbeParameter(getClass().getName(), "sendIncidentForSpecificFilesWithoutReferers", false));
        return prefs;
    }

    /**
     * Preferences are 
     *  row_batch_size : # of lines imported in a batch
     *  fields : definition of fields
     *  isIntranet : do we import logs of an intranet server, or a publicly accessible server
     *  addNewPaths : do we add new intranet paths or only use those existing
     *  fullPath : when adding new paths, do we add all paths we see
     *  pathDepthLimit : or do we limit to a specific depth
     */
    @Override
    public void configureProbeWithParameters(Db statusDb, Map<String, ProbeParameter> pp) throws Exception {
        super.configureProbeWithParameters(statusDb, pp);
        fields = pp.get(getClass().getName() + ".fields").getEffectiveValueS();
        addNewPaths = pp.get(getClass().getName() + ".addNewPaths").getEffectiveValueB();
        pathDepthLimit = pp.get(getClass().getName() + ".pathDepthLimit").getEffectiveValueI();
        fullPath = pp.get(getClass().getName() + ".fullPath").getEffectiveValueB();
        excludePathPart = pp.get(getClass().getName() + ".excludePathPart").getEffectiveValueArr();
        fileExtensions = pp.get(getClass().getName() + ".fileExtensions").getEffectiveValueArr();
        sendIncidentForSpecificFilesWithoutReferers = pp.get(getClass().getName() + ".sendIncidentForSpecificFilesWithoutReferers").getEffectiveValueB();
    }

    protected void checkWouldConsume(Db statusDb, FileState fs) throws NoConsumeException {
        Metadata mdSite = fs.getMetadata("site");
        String metaSite = DbHelper.nullify(mdSite.getStringValue());
        if (metaSite == null) throw new NoConsumeException("No metasite available");
    }

    protected void transferProbePreferences(FileImport fi) throws Exception {
        super.transferProbePreferences(fi);
        SunJWSImport imp = (SunJWSImport) fi;
        imp.addNewPaths = addNewPaths;
        imp.pathDepthLimit = pathDepthLimit;
        imp.fullPath = fullPath;
        if (fields != null) imp.configure(fields);
        imp.excludePathPart = excludePathPart;
        imp.fileExtensions = fileExtensions;
        imp.sendIncidentForSpecificFilesWithoutReferers = sendIncidentForSpecificFilesWithoutReferers;
    }

    protected FileImport getFileImport() {
        return new SunJWSImport();
    }
}
