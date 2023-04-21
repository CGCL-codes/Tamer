package org.qcmylyn.core.utils.properties;

import java.util.ArrayList;
import java.util.Collection;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.jqc.QcBug;
import org.jqc.QcBugFactory;
import org.jqc.QcConnectionEvents;
import org.jqc.QcLoggedSession;
import org.jqc.QcProjectConnectedSession;
import org.jqc.QcSessionParameters;
import org.junit.Test;
import org.qcmylyn.core.QcConnectionManager;
import org.qcmylyn.core.QcRepositoryConfiguration;
import org.qcmylyn.core.QcRepositoryConfigurationFactory;
import org.qctools4j.exception.QcException;
import org.qctools4j.filters.FieldFilter;
import org.qctools4j.model.defect.FieldNameEnum;

/**
 * TODO Insert class description
 *
 * @author usf02000
 *
 */
public class TestThread {

    private static final class QcConnectionEventsImplementation implements QcConnectionEvents<Object, QcException> {

        public Object connectedToPorject(final QcProjectConnectedSession qcProjectConnectedSession) throws QcException, QcException {
            final QcBugFactory bugFactory = qcProjectConnectedSession.getBugFactory();
            final Collection<FieldFilter> fa = new ArrayList<FieldFilter>();
            fa.add(new FieldFilter(FieldNameEnum.BG_RESPONSIBLE.name(), "[CurrentUser]"));
            final Collection<QcBug> bugs = bugFactory.getBugs(fa);
            for (final QcBug qcBug : bugs) {
                System.out.println(qcBug.getId());
            }
            return null;
        }

        public Object loggedIn(final QcLoggedSession loggedSession) throws QcException, QcException {
            return null;
        }
    }

    private static final class RunnableImplementation implements Runnable {

        private final QcSessionParameters prm;

        private RunnableImplementation(final QcSessionParameters prm) {
            this.prm = prm;
        }

        public boolean ok;

        public void run() {
            try {
                QcConnectionManager.useConnection(prm, new QcConnectionEventsImplementation());
                System.out.println("DONE");
                ok = true;
            } catch (final QcException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void testIt() throws QcException {
        final QcSessionParameters prm = new QcSessionParameters("http://qcp1:8080/qcbin", "Elhananm", "Pnx2116", "DEFAULT", "Tav_framework");
        for (int i = 0; i < 5000; i++) {
            final RunnableImplementation runnableImplementation = new RunnableImplementation(prm);
            final Thread t = new Thread(runnableImplementation);
            t.start();
            System.out.println("Start");
            while (!runnableImplementation.ok) {
            }
            System.out.println("end");
        }
    }

    public void testConfiguration() throws QcException {
        final QcSessionParameters prm = new QcSessionParameters("http://qcp1:8080/qcbin", "Elhananm", "Pnx2116", "DEFAULT", "Tav_framework");
        final TaskRepository convertToRepository = QcConnectionManager.convertToRepository(prm);
        final QcRepositoryConfiguration configuration = QcRepositoryConfigurationFactory.getConfiguration(convertToRepository);
    }

    @Test
    public void testPropes() throws QcException {
        final QcSessionParameters prm = new QcSessionParameters("http://qcp1:8080/qcbin", "Elhananm", "Fnx_2000", "DEFAULT", "Tav_framework");
        final TaskRepository repository = QcConnectionManager.convertToRepository(prm);
        final PropertyTable properties = new RepositoryProperties(repository, "FLD");
    }
}
