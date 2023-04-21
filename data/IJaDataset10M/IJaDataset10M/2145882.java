package com.taobao.api.internal.stream.message;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import com.taobao.api.internal.stream.NamedThreadFactory;

/**
 * 消息处理工厂
 * @author zhenzi
 * 2011-8-9 下午03:06:53
 */
public class StreamMsgConsumeFactory {

    private int minThreads;

    private int maxThreads;

    private int queueSize;

    private ThreadPoolExecutor threadPool;

    public StreamMsgConsumeFactory(int minThreads, int maxThreads, int queueSize) {
        if (minThreads <= 0 || maxThreads <= 0 || queueSize <= 0) {
            throw new RuntimeException("minThread,maxThread and queueSize must large than 0");
        }
        this.minThreads = minThreads;
        this.maxThreads = maxThreads;
        this.queueSize = queueSize;
        threadPool = new ThreadPoolExecutor(this.minThreads, this.maxThreads, 60, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(this.queueSize), new NamedThreadFactory("pool-msg-consume", true));
    }

    public void consume(Runnable task) throws RejectedExecutionException, NullPointerException {
        threadPool.submit(task);
    }

    public void shutdown() {
        threadPool.shutdown();
    }
}
