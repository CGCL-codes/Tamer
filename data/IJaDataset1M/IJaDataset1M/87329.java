package com.coyou.ad.job;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;
import com._5i56.timer.task.Task;
import com._5i56.timer.thread.TaskQueue;
import com._5i56.timer.thread.ThreadPool;
import com.coyou.ad.config.Config;
import com.coyou.ad.data.AbsMailFetcher;
import com.coyou.ad.task.MailSendTask;
import com.coyou.ad.util.Counter;
import com.coyousoft.adsys.entity.Email;
import com.coyousoft.adsys.entity.Proxy;

/**
 * 监控线程,负责监控任务是否到了执行时间
 * @author 贾楠
 *
 */
public class SendJobThread extends Thread {

    protected static final Logger l = Logger.getLogger(SendJobThread.class);

    private ThreadPool sendJobPool = null;

    private AbsMailFetcher fetcher = null;

    private TaskQueue failTaskQueue = null;

    public SendJobThread(ThreadPool sendJobPool, AbsMailFetcher fetcher, TaskQueue failTaskQueue) {
        this.sendJobPool = sendJobPool;
        this.fetcher = fetcher;
        this.failTaskQueue = failTaskQueue;
    }

    @SuppressWarnings("deprecation")
    public void run() {
        l.info("发送邮件调度线程启动!");
        Counter.START_TIME_STR = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        while (true) {
            Counter.RUN_TIME_SECOND += Config.PUT_TASK_SPACE_TIME / 1000.0;
            List<Email> emailList = null;
            try {
                emailList = fetcher.getEmailList(Config.GET_MAIL_SIZE);
                if (emailList != null && emailList.size() != 0) {
                    String advertContent = emailList.get(0).getAdvert().getAdvertContent();
                    if ("".equals(advertContent) || "0".equals(advertContent)) {
                        Thread.sleep(10 * 1000 * 60);
                    }
                }
            } catch (Exception e1) {
                l.error("获取邮件列表失败," + e1.getMessage(), e1);
                try {
                    Thread.sleep(1000 * 60);
                } catch (InterruptedException e) {
                    l.error("休眠失败!");
                    e.printStackTrace();
                }
                continue;
            }
            int size = emailList.size();
            List<Proxy> proxyList = Config.IS_USE_PROXY ? fetcher.getProxyList(Config.GET_PROXY_SIZE) : null;
            l.info("★★★★★★★★★★★★★★★★★一批" + size + "个任务入队开始★★★★★★★★★★★★★★★★★");
            l.info("代理数量:" + (proxyList == null ? "null" : proxyList.size()));
            l.info("每封邮件间隔时间:" + Config.MAIL_SPACE_TIME + "(ms)");
            for (Email email : emailList) {
                Task task = new MailSendTask(email, proxyList, failTaskQueue, sendJobPool);
                if (Config.IS_MULTI_THREAD) {
                    this.sendJobPool.putTask(task);
                } else {
                    try {
                        task.execute();
                    } catch (Exception e) {
                        l.error(e.getMessage(), e);
                        continue;
                    }
                }
                try {
                    Thread.sleep(Config.PUT_TASK_SPACE_TIME);
                } catch (InterruptedException ex) {
                    l.error(ex.getMessage(), ex);
                }
            }
            l.info("★★★★★★★★★★★★★★★★★一批" + size + "个任务入队结束★★★★★★★★★★★★★★★★★");
        }
    }

    public void addThread() {
        this.sendJobPool.addWorkerThread();
    }

    public void removeThread() {
        this.sendJobPool.removeWorkerThread();
    }

    public static void main(String[] args) {
        System.out.println();
    }
}
