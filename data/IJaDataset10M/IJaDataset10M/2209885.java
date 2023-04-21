package org.ochan.dpl.service;

import java.util.prefs.Preferences;
import javax.jws.WebService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ochan.dpl.OchanEnvironment;
import org.ochan.dpl.SynchroDPL;
import org.ochan.dpl.replication.TransactionTemplate;
import org.ochan.service.SynchroService;
import org.springframework.jmx.export.annotation.ManagedResource;

/**
 * Can not replicate the synchro service until the proxy is written on top of
 * this.
 * 
 * @author David Seymore Dec 17, 2009
 */
@WebService(endpointInterface = "org.ochan.service.SynchroService")
@ManagedResource(description = "Local Synchro Service", objectName = "Ochan:service=local,name=LocalSynchroService", logFile = "jmx.log")
public class LocalSynchroService implements SynchroService {

    private OchanEnvironment environment;

    private static final Log LOG = LogFactory.getLog(LocalSynchroService.class);

    private static final Preferences PREFERENCES = Preferences.userNodeForPackage(LocalSynchroService.class);

    /**
	 * @param environment
	 *            the environment to set
	 */
    public void setEnvironment(OchanEnvironment environment) {
        this.environment = environment;
    }

    @Override
    public Long getSync() {
        try {
            final SynchroDPL dpl = new SynchroDPL();
            new TransactionTemplate(environment) {

                public void doInTransaction() {
                    environment.synchroByIdentifier().put(dpl);
                }
            }.run();
            return dpl.getIdentifier();
        } catch (Exception e) {
            LOG.error("ugh", e);
        }
        return null;
    }
}
