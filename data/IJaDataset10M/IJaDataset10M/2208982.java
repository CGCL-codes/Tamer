package de.schwarzrot.jobs.processing;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import java.util.List;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import de.schwarzrot.app.config.ServiceConfig;
import de.schwarzrot.app.support.ApplicationServiceProvider;
import de.schwarzrot.app.support.SimpleApplicationLauncher;
import de.schwarzrot.data.access.support.EqualConditionElement;
import de.schwarzrot.data.transaction.TORead;
import de.schwarzrot.data.transaction.TOSave;
import de.schwarzrot.data.transaction.Transaction;
import de.schwarzrot.data.transaction.TransactionStatus;
import de.schwarzrot.data.transaction.support.TransactionFactory;
import de.schwarzrot.jar.JarExtensionHandler;
import de.schwarzrot.jobs.domain.Job;
import de.schwarzrot.jobs.domain.JobType;
import de.schwarzrot.jobs.domain.data.WorkState;
import de.schwarzrot.jobs.processing.support.ProcessFactory;
import de.schwarzrot.rec.domain.BonusItem;
import de.schwarzrot.recmgr.domain.AudioSelection;

public class JobCreationTest {

    private static long recordingID;

    protected static String[] appContextFiles = { "de/schwarzrot/app/base/ctx/data-access-context.xml", "de/schwarzrot/app/base/ctx/security-context.xml", "de/schwarzrot/app/base/ctx/test-app-context.xml" };

    @Ignore
    @Test
    public void createCutPesJob() {
        createCutJob("Pes");
    }

    @Ignore
    @Test
    public void createCutTSJob() {
        createCutJob("TS");
    }

    @Before
    public void setUp() {
        new SimpleApplicationLauncher<ServiceConfig>(null, appContextFiles, (List<String>) null);
        JarExtensionHandler jh = new JarExtensionHandler();
        jh.loadExtensions("ext");
        if (taFactory == null) taFactory = ApplicationServiceProvider.getService(TransactionFactory.class);
        String recPath = System.getProperty("recording.path");
        Transaction ta = taFactory.createTransaction();
        TORead<BonusItem> tor = new TORead<BonusItem>(BonusItem.class, new EqualConditionElement("path", recPath));
        ta.add(tor);
        ta.setRollbackOnly();
        ta.execute();
        assertNotNull(tor.getResult());
        assertTrue(tor.getResult().size() > 0);
        item = tor.getResult().get(0);
    }

    @Test
    public void tellAvailableProcessDefinitions() {
        ProcessFactory pf = ApplicationServiceProvider.getService(ProcessFactory.class);
        for (String pid : pf.getProcessIDs()) {
            System.out.println("available process definition: " + pid);
        }
    }

    protected void createCutJob(String format) {
        Job job = new Job();
        job.setType(JobType.CUTINPLACE);
        job.setTarget(format);
        job.setMaxSize(650l);
        job.setStatus(WorkState.RELEASED);
        job.setWhichAudio(AudioSelection.ALL);
        job.setPriority(45);
        job.setSubject(item);
        Transaction ta = taFactory.createTransaction();
        ta.add(new TOSave<Job>(job));
        ta.execute();
        assertTrue(ta.getStatus() == TransactionStatus.STATUS_COMMITTED);
    }

    private TransactionFactory taFactory;

    private BonusItem item;
}
