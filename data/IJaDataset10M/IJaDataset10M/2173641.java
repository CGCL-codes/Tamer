package com.haipi.util.mail;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * ThreadPoolExecutor的构造方法入参： corePoolSize 线程池维护线程的最少数量 maximumPoolSiz
 * 线程池维护线程的最大数量 keepAliveTime 线程池维护线程所允许的空闲时间 unit 线程池维护线程所允许的空闲时间的单位 workQueue
 * 线程池所使用的缓冲队列 handler 线程池对拒绝任务的处理策略
 * 
 * 当一个任务通过execute(Runnable)方法欲添加到线程池时：
 * 
 * 如果此时线程池中的数量小于corePoolSize，即使线程池中的线程都处于空闲状态，也要创建新的线程来处理被添加的任务。 如果此时线程池中的数量等于
 * corePoolSize，但是缓冲队列 workQueue未满，那么任务被放入缓冲队列。
 * 如果此时线程池中的数量大于corePoolSize，缓冲队列workQueue满，并且线程池中的数量小于maximumPoolSize，建新的线程来处理被添加的任务。
 * 如果此时线程池中的数量大于corePoolSize，缓冲队列workQueue满，并且线程池中的数量等于maximumPoolSize，那么通过
 * handler所指定的策略来处理此任务。 也就是：处理任务的优先级为：
 * 核心线程corePoolSize、任务队列workQueue、最大线程maximumPoolSize，如果三者都满了，使用handler处理被拒绝的任务。
 * 
 * @author Administrator
 * 
 */
public class MailThreadPool {

    private static ThreadPoolExecutor threadPool = new ThreadPoolExecutor(2, 4, 3, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(10), new ThreadPoolExecutor.DiscardOldestPolicy());

    private MailThreadPool() {
    }

    public static ThreadPoolExecutor getInstance() {
        return threadPool;
    }
}
