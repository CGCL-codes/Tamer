package org.go.test;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.Semaphore;

public class HandlerClient implements Runnable {

    private Map map = null;

    private String code = null;

    private Semaphore sp = null;

    public HandlerClient(String code, Semaphore sp, Map map) {
        this.code = code;
        this.sp = sp;
        this.map = map;
    }

    public HandlerClient() {
    }

    public HandlerClient(String s) {
        code = s;
    }

    @Override
    public void run() {
        System.out.println(code + ",调用时间:" + " " + new Date().getTime());
    }

    @SuppressWarnings("static-access")
    public void run1() {
        try {
            if ("1".equals(code)) {
                System.err.println(Thread.currentThread().getName() + " 进来了");
                if (!map.containsKey("1")) {
                    sp.acquire(1);
                    if (!map.containsKey("1")) {
                        System.err.println("第一次调用接口，不用判断，可以执行");
                        new Handler().invoke();
                        map.put("1", System.currentTimeMillis());
                    } else if (map.containsKey("1")) {
                        System.err.println("......");
                        long time = (Long) map.get("1");
                        if (System.currentTimeMillis() - time > 500) {
                            System.err.println("上次调用时间:" + " " + time);
                            System.err.println("两次时间相隔大于500ms" + "可以执行");
                            map.put("1", System.currentTimeMillis());
                            new Handler().invoke();
                        } else {
                            System.err.println("上次调用时间:" + " " + time);
                            System.err.println("两次时间相隔小于500ms" + "不可以执行");
                            System.err.println(Thread.currentThread().getName() + "休眠" + (500 - (System.currentTimeMillis() - time)) + "毫秒");
                            Thread.currentThread().sleep(500 - (System.currentTimeMillis() - time));
                            map.put("1", System.currentTimeMillis());
                            new Handler().invoke();
                        }
                    }
                    sp.release();
                } else if (map.containsKey("1")) {
                    System.err.println(Thread.currentThread().getName() + " 重复进来了");
                    long time = (Long) map.get("1");
                    if (System.currentTimeMillis() - time > 500) {
                        System.out.println("上次调用时间:" + " " + time);
                        System.out.println("两次时间相隔大于500ms" + "可以执行");
                        new Handler().invoke();
                        map.put("1", System.currentTimeMillis());
                    } else {
                        System.err.println("两次时间相隔小于500ms" + "不可以执行");
                    }
                }
            } else {
                new Handler().invoke();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
