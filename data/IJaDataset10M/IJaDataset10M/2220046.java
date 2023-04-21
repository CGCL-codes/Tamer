package org.nexopenframework.tasks.threadpool;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.nexopenframework.core.task.ThreadPoolTaskExecutor;
import org.nexopenframework.tasks.JobTask;
import org.nexopenframework.tasks.JobTaskContext;
import org.nexopenframework.tasks.TaskExecutionException;
import org.nexopenframework.tasks.TaskExecutor;
import org.nexopenframework.transaction.NullPlatformTransactionManager;
import org.springframework.transaction.interceptor.MatchAlwaysTransactionAttributeSource;
import junit.framework.TestCase;

/**
 * <p>NexOpen Framework</p>
 * 
 * <p>Easy TestCase for dealing with main features of
 * {@link ThreadPoolTaskExecutorProvider}</p>
 * 
 * @author <a href="mailto:fme@nextret.net">Francesc Xavier Magdaleno</a>
 * @version 1.0
 * @since 1.0
 */
public class ThreadPoolTaskExecutorProviderTest extends TestCase {

    private TaskExecutor executor;

    /**
	 * @throws InterruptedException
	 */
    public void testExecute() throws InterruptedException {
        TaskExecutor.executeTask(MyJobTask.class);
        Map context = new HashMap(2);
        context.put("now", new Date());
        context.put("description", "some lovely description");
        TaskExecutor.executeTask(MyJobTask.class, context);
        Thread.sleep(1 * 1000);
        Map _context = new HashMap(1);
        _context.put("throwException", Boolean.TRUE);
        TaskExecutor.executeTask(MyJobTask.class, _context);
        Thread.sleep(1 * 1000);
    }

    /**
	 * 
	 * @see junit.framework.TestCase#setUp()
	 */
    protected void setUp() throws Exception {
        ThreadPoolTaskExecutorProvider provider = new ThreadPoolTaskExecutorProvider();
        ThreadPoolTaskExecutor tpte = new ThreadPoolTaskExecutor();
        tpte.setCorePoolSize(10);
        tpte.setMaxPoolSize(20);
        tpte.afterPropertiesSet();
        provider.setTransactionManager(new NullPlatformTransactionManager());
        provider.setTransactionAttributeSource(new MatchAlwaysTransactionAttributeSource());
        provider.setThreadPoolTaskExecutor(tpte);
        executor = new TaskExecutor();
        executor.setProvider(provider);
    }

    /**
	 * 
	 * @see junit.framework.TestCase#tearDown()
	 */
    protected void tearDown() throws Exception {
        executor.destroy();
    }

    public static class MyJobTask implements JobTask {

        public void execute(JobTaskContext context) throws TaskExecutionException {
            System.out.println("MyJobTask.execute() :: " + context);
            Map data = context.getData();
            boolean throwException = data.containsKey("throwException") ? ((Boolean) data.get("throwException")).booleanValue() : false;
            if (throwException) {
                throw new IllegalStateException("Controlled exception");
            }
        }
    }
}
