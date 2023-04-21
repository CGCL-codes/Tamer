package it.unina.gaeframework.geneticalgorithm.servlet;

import it.unina.gaeframework.geneticalgorithm.statistics.GeneticStatistics;
import it.unina.gaeframework.geneticalgorithm.util.GAEUtils;
import it.unina.gaeframework.geneticalgorithm.util.GAKeyFactory;
import it.unina.tools.datastore.DatastoreLoadAndSave;
import java.io.IOException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.jsr107cache.Cache;
import com.google.appengine.api.datastore.Key;

@SuppressWarnings("serial")
public class ReduceQueueServlet extends HttpServlet {

    public static final String REDUCE_TASK_NAME = "reduceTask";

    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        doPost(req, resp);
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        reduceQueue(req, resp);
    }

    private void reduceQueue(HttpServletRequest req, HttpServletResponse resp) {
        DatastoreLoadAndSave data = new DatastoreLoadAndSave();
        Cache cache = GAEUtils.getCache();
        Key geneticStatisticsKey = GAKeyFactory.getGAKeyFactoryInstance().getGenetisStatisticsKey();
        GeneticStatistics geneticStatistics = (GeneticStatistics) cache.get(geneticStatisticsKey);
        if (geneticStatistics == null) {
            geneticStatistics = data.loadObjectById(GeneticStatistics.class, geneticStatisticsKey.getId());
        }
        if (geneticStatistics == null) {
            throw new RuntimeException("ReduceQueueServlet: GeneticStatistics non presente nel datastore\n" + "Impossibile continuare l'algoritmo genetico");
        } else {
            String idTask = geneticStatistics.getIdTask();
            String actualIteration = "" + geneticStatistics.getActualIteration();
            String taskName = REDUCE_TASK_NAME + idTask + actualIteration;
            String queueName = "geneticqueue";
            String taskUrl = "/geneticreduceworkertask";
            GAEUtils.enqueueTask(taskName, queueName, taskUrl, null);
        }
    }
}
