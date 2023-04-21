package net.solarnetwork.loadtest;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import net.solarnetwork.loadtest.client.HttpAsyncClientTask;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

/**
 * Load test harness.
 * 
 * @author matt
 * @version $Revision: 1211 $
 */
public class LoadTest {

    private ConfigurableApplicationContext appContext;

    private ServerConfig serverConfig;

    private LoadConfig loadConfig;

    private SimpleJdbcTemplate jdbcTemplate;

    private final Logger log = Logger.getLogger(getClass());

    private LoadTest() {
        appContext = new ClassPathXmlApplicationContext("loadtest-context.xml");
        serverConfig = appContext.getBean(ServerConfig.class);
        loadConfig = appContext.getBean(LoadConfig.class);
        jdbcTemplate = appContext.getBean(SimpleJdbcTemplate.class);
        if (loadConfig.getStartDate() == null) {
            Calendar c = new GregorianCalendar();
            c.add(Calendar.YEAR, -10);
            c.set(Calendar.MILLISECOND, c.getActualMinimum(Calendar.MILLISECOND));
            c.set(Calendar.SECOND, c.getActualMinimum(Calendar.SECOND));
            loadConfig.setStartDate(c.getTime());
        }
    }

    private void summarizeResults(Collection<NodeState> nodeStates) {
        long totalCount = 0;
        long totalMs = 0;
        for (NodeState state : nodeStates) {
            long totalNodeCount = 0;
            long totalNodeMs = 0;
            for (String taskName : state.getTaskNames()) {
                TaskState taskState = state.getTaskState(taskName);
                long taskCount = taskState.getSuccessCount().get();
                long taskMs = taskState.getSuccessTotalMs().get();
                totalCount += taskCount;
                totalMs += taskMs;
                totalNodeCount += taskCount;
                totalNodeMs += taskMs;
                if (log.isTraceEnabled()) {
                    log.trace("Node " + state.getNodeId() + " task " + taskName + " completed " + taskCount + " requests in " + totalMs + "ms; average request time " + (taskCount > 0 ? (taskMs / taskCount) : 0) + "ms");
                }
            }
        }
        if (log.isInfoEnabled()) {
            log.info("Completed " + totalCount + " requests in " + totalMs + "ms; average request time " + (totalCount > 0 ? (totalMs / totalCount) : 0) + "ms");
        }
    }

    private HttpClient createHttpClient() {
        if (log.isInfoEnabled()) {
            log.info("Creating HTTP client with maximum of " + loadConfig.getMaxHttpConnections() + " connections");
        }
        ThreadSafeClientConnManager cm = new ThreadSafeClientConnManager();
        cm.setDefaultMaxPerRoute(loadConfig.getMaxHttpConnections());
        cm.setMaxTotal(loadConfig.getMaxHttpConnections());
        HttpClient client = new DefaultHttpClient(cm);
        return client;
    }

    private ExecutorService createExecutorService(int poolSize) {
        ThreadPoolExecutor ex = new ThreadPoolExecutor(poolSize, poolSize, 15, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
        return ex;
    }

    private Map<Long, ExecutorService> createNodeExecutorServiceMap(int poolSize) {
        if (log.isInfoEnabled()) {
            log.info("Creating thread pools of size " + poolSize + " for " + loadConfig.getNumberOfNodes() + " nodes");
        }
        Map<Long, ExecutorService> result = new LinkedHashMap<Long, ExecutorService>(loadConfig.getNumberOfNodes());
        for (int i = loadConfig.getStartingNodeId(), len = loadConfig.getStartingNodeId() + loadConfig.getNumberOfNodes(); i < len; i++) {
            ExecutorService ex = createExecutorService(poolSize);
            result.put(Long.valueOf(i), ex);
        }
        return result;
    }

    private void go() {
        final HttpClient client = createHttpClient();
        final Map<String, HttpAsyncClientTask> tasks = appContext.getBeansOfType(HttpAsyncClientTask.class);
        final Map<Long, ExecutorService> nodeExecutors = createNodeExecutorServiceMap(tasks.size());
        final List<NodeState> nodeStates = new ArrayList<NodeState>(loadConfig.getNumberOfNodes());
        try {
            if (log.isInfoEnabled()) {
                log.info("Creating initial node tasks " + tasks.keySet() + " for " + loadConfig.getNumberOfNodes() + " nodes");
            }
            for (Map.Entry<Long, ExecutorService> me : nodeExecutors.entrySet()) {
                final Long nodeId = me.getKey();
                final ExecutorService nodeExecutor = me.getValue();
                final Long locationId = jdbcTemplate.queryForLong("select loc_id from solarnet.sn_node where node_id = ?", nodeId);
                final Long priceLocationId = Long.valueOf((loadConfig.getStartingPriceLocId() + (long) (Math.random() * loadConfig.getNumberOfPriceLocs())));
                final Map<String, Long> locationIds = new HashMap<String, Long>(2);
                locationIds.put("weather", locationId);
                locationIds.put("price", priceLocationId);
                Calendar c = new GregorianCalendar();
                c.setTime(loadConfig.getStartDate());
                Calendar end = (Calendar) c.clone();
                end.add(Calendar.MINUTE, loadConfig.getMinutes());
                NodeState state = new NodeState(nodeId, locationIds, c, end.getTime(), tasks.size(), nodeExecutor);
                nodeStates.add(state);
                for (final String taskName : tasks.keySet()) {
                    TaskState ts = state.addTask(taskName);
                    nodeExecutor.submit(new NodeTaskCallable(client, ts, tasks.get(taskName), serverConfig, loadConfig));
                }
            }
            log.info("Waiting for node tasks to complete");
            while (nodeExecutors.size() > 0) {
                for (Iterator<ExecutorService> itr = nodeExecutors.values().iterator(); itr.hasNext(); ) {
                    final ThreadPoolExecutor nodeExecutor = (ThreadPoolExecutor) itr.next();
                    int active = nodeExecutor.getActiveCount();
                    if (active < 1) {
                        nodeExecutor.shutdown();
                        itr.remove();
                    }
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            log.info("All node tasks complete");
            summarizeResults(nodeStates);
        } finally {
            client.getConnectionManager().shutdown();
        }
    }

    private static class NodeTaskCallable implements Callable<TaskState> {

        private static Logger log = Logger.getLogger(NodeTaskCallable.class);

        private final HttpClient httpClient;

        private final TaskState state;

        private final HttpAsyncClientTask task;

        private final ServerConfig serverConfig;

        private final LoadConfig loadConfig;

        private NodeTaskCallable(HttpClient httpClient, TaskState state, HttpAsyncClientTask task, ServerConfig serverConfig, LoadConfig loadConfig) {
            this.httpClient = httpClient;
            this.state = state;
            this.task = task;
            this.serverConfig = serverConfig;
            this.loadConfig = loadConfig;
        }

        @Override
        public TaskState call() throws Exception {
            String path = serverConfig.getTaskUrlMap().get(state.getTaskName());
            if (path == null) {
                throw new RuntimeException("ServerConfig task Url mapping missing for task " + state.getTaskName());
            }
            String url = serverConfig.getBaseUrl() + path;
            HttpUriRequest req = task.createTaskRequest(state, url);
            try {
                final long start = System.currentTimeMillis();
                HttpResponse res = httpClient.execute(req, new BasicHttpContext());
                if (log.isTraceEnabled()) {
                    log.trace(state.getDate() + " " + state.getNodeState().getNodeId() + " " + state.getTaskName() + " HTTP response: " + res.getStatusLine());
                }
                EntityUtils.consume(res.getEntity());
                long reqTime = System.currentTimeMillis() - start;
                long successCount = state.getSuccessCount().incrementAndGet();
                state.getSuccessTotalMs().addAndGet(reqTime);
                state.adjustDate(Calendar.MINUTE, loadConfig.getFrequency());
                if (log.isInfoEnabled() && successCount % 5 == 0) {
                    log.info(state.getNodeState().getNodeId() + " " + state.getTaskName() + " completed " + successCount + " tasks");
                }
            } catch (Exception e) {
                req.abort();
                if (log.isDebugEnabled()) {
                    log.debug("Failed HTTP request: " + e.getMessage());
                }
                throw e;
            }
            if (!state.isFinished()) {
                state.getNodeState().getExecutor().submit(new NodeTaskCallable(httpClient, state, task, serverConfig, loadConfig));
            } else {
                if (log.isDebugEnabled()) {
                    log.debug(state.getNodeState().getNodeId() + " " + state.getTaskName() + " finished");
                }
            }
            return state;
        }
    }

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        LoadTest t = new LoadTest();
        t.go();
    }
}
