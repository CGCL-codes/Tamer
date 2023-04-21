package org.nexopenframework.workflow.providers.jbpm31;

import java.io.InputStream;
import junit.framework.TestCase;
import org.jbpm.JbpmConfiguration;
import org.jbpm.JbpmContext;
import org.nexopenframework.context.framework.Contexts;
import org.nexopenframework.context.framework.ThreadLocalContext;
import org.nexopenframework.util.ResourceLocator;
import org.nexopenframework.workflow.context.ProcessContext;
import org.nexopenframework.workflow.context.ProcessContextHolder;
import org.nexopenframework.workflow.providers.jbpm31.JbpmUtils;
import org.objectweb.jotm.Current;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.jta.JotmFactoryBean;
import org.springframework.transaction.jta.JtaTransactionManager;
import org.springframework.transaction.support.DefaultTransactionDefinition;

/**
 * <p>NexTReT Open Framework</p>
 * 
 * <p>Comment here</p>
 * 
 * @author <a href="mailto:fme@nextret.net">Francesc Xavier Magdaleno</a>
 * @version 1.0
 * @since 1.0
 */
public class JbpmSpringSynchronizationTest extends TestCase {

    private JtaTransactionManager tm;

    /**jBPM thread-safe configuration*/
    private JbpmConfiguration cfg;

    private Contexts ctxs;

    public void testSynchronization() throws Exception {
        ProcessContext pc = ProcessContextHolder.getProcessContext();
        assertNotNull(pc);
        TransactionStatus ts = tm.getTransaction(null);
        JbpmContext context = JbpmUtils.getJbpmContext(cfg, null);
        assertNotNull(context);
        TransactionDefinition td = new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_NOT_SUPPORTED);
        TransactionStatus tsnt = tm.getTransaction(td);
        tm.commit(tsnt);
        tm.commit(ts);
        ProcessContextHolder.resetProcessContext();
    }

    protected void setUp() throws Exception {
        ctxs = new Contexts();
        ctxs.addContext(new ThreadLocalContext());
        ProcessContextHolder holder = new ProcessContextHolder();
        assertNotNull(holder);
        InputStream inputStream = ResourceLocator.getResourceAsStream("jbpm.cfg.xml");
        cfg = JbpmConfiguration.parseInputStream(inputStream);
        tm = new JtaTransactionManager();
        JotmFactoryBean jotmfb = new JotmFactoryBean();
        Current current = (Current) jotmfb.getObject();
        tm.setTransactionManager(current);
        tm.setUserTransactionName(null);
        tm.afterPropertiesSet();
    }

    protected void tearDown() throws Exception {
        ctxs.destroy();
    }
}
