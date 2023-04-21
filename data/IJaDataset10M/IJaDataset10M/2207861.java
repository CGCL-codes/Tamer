package jp.joogoo.web.controller.cron;

import java.util.List;
import java.util.logging.Logger;
import jp.joogoo.web.model.M_joogoo;
import jp.joogoo.web.service.AuthorizeService;
import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;

/**
 * サービスから新着情報を取得する処理をタスクキューに積み込むコントローラです。
 * 
 * @since 1.0
 * @author skitazaki
 */
public class FetcherController extends Controller {

    private static final AuthorizeService auth = new AuthorizeService();

    private static final Logger logger = Logger.getLogger(FetcherController.class.getName());

    @Override
    protected Navigation run() throws Exception {
        List<M_joogoo> accounts = auth.getAllAccounts();
        Queue queue = QueueFactory.getDefaultQueue();
        for (M_joogoo account : accounts) {
            queue.add(TaskOptions.Builder.withUrl("/fetcher").param("userId", account.getUserId()));
        }
        logger.info("Total=" + accounts.size());
        return null;
    }
}
