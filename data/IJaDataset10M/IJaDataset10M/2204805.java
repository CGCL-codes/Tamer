package com.wikipy.aspect.feeds;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import com.wikipy.repository.RepositoryService;

public class FeedAspectFetchJob implements Job {

    private RepositoryService repositoryService;

    public void execute(JobExecutionContext jcontext) throws JobExecutionException {
        repositoryService = (RepositoryService) jcontext.getMergedJobDataMap().get("repositoryService");
        Map<String, Object> map = new HashMap<String, Object>();
        map.put(RepositoryService.PROP_ASPECT, "feeds");
        Collection<Map<String, Object>> items = repositoryService.selectItems(map);
        for (Map<String, Object> item : items) {
            try {
                FeedClient.doImport(item.get("feedUrl").toString(), item.get(RepositoryService.PROP_ID).toString());
                repositoryService.updateProp(item.get(RepositoryService.PROP_ID).toString(), "feedUpdated", new Date());
            } catch (Exception e) {
            }
        }
    }

    private final Timer timer = new Timer();

    public void init() {
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
            }
        }, 2 * 60 * 1000);
    }
}
