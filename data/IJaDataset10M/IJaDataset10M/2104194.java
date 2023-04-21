package org.pct4g.data.distribute;

import java.io.File;
import java.util.Vector;
import org.ietf.jgss.GSSCredential;
import org.pct4g.common.Pct4gProperties;
import org.pct4g.data.Destination;
import org.pct4g.data.DownloadProperties;
import org.pct4g.data.Pct4gPrintWriter;
import org.pct4g.data.script.ScriptExecutor;

/**
 * @author sirasuna
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
class SshDistributor extends Distributor {

    private static final String scriptname = "distribute-ssh.sh";

    /**
	 * Constructor for SshDistributor.
	 * @param dataFiles
	 * @param downloadProperties
	 * @param proxy
	 */
    protected SshDistributor(Vector dataFiles, DownloadProperties downloadProperties, GSSCredential proxy, Pct4gPrintWriter statusWriter) {
        super(dataFiles, downloadProperties, proxy, statusWriter);
    }

    public void run(Destination destination) {
        distribute(destination);
        distributeLocal(destination);
    }

    private void distributeLocal(Destination destination) {
        String libDir = Pct4gProperties.getInstance().getLibDirectory();
        File script = new File(libDir, scriptname);
        ScriptExecutor executor = new ScriptExecutor(script, destination.getHost(), destination.getDirectory(), proxy);
    }
}
