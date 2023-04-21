package com.dokumentarchiv.test;

import java.util.Date;
import java.util.concurrent.Callable;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;

/**
 * @author Carsten Burghardt
 * @version $Id: SendMassEmailWorker.java 39 2008-06-24 18:22:30Z cburghardt $
 */
public class SendMassEmailWorker implements Callable<Long> {

    private MimeMessage msg;

    private static int MAX_COUNT = 10;

    public SendMassEmailWorker(MimeMessage msg) {
        this.msg = msg;
    }

    public Long call() throws Exception {
        long duration = 0;
        for (int i = 1; i <= MAX_COUNT; ++i) {
            try {
                long start = new Date().getTime();
                Transport.send(msg);
                duration += new Date().getTime() - start;
                System.out.println(this + " send " + i + " mails");
                Thread.sleep(2000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println(this + " took " + duration + " for " + MAX_COUNT);
        return duration;
    }
}
