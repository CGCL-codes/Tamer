package com.tien.utils.mail;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Tien  E-mail:g.tianxin@gmail.com
 * @version Time：2011-6-28 下午11:11:35
 */
public class RetryQueue extends Thread {

    private static final Logger log = LoggerFactory.getLogger(RetryQueue.class);

    private static final Map<Email, Integer> queue = new ConcurrentHashMap<Email, Integer>();

    private boolean terminated = false;

    private int retries;

    private int retryInterval;

    @Override
    public void run() {
        while (!terminated) {
            try {
                log.debug("Sleeping for {}ms", retryInterval);
                Thread.sleep(retryInterval);
            } catch (InterruptedException e) {
            }
            for (Email email : queue.keySet()) {
                int numberOfRetries = queue.get(email);
                if (numberOfRetries < retries) {
                    log.debug("Retrying to send email to {}, failed {} times previously", email.getToAddresses(), numberOfRetries + 1);
                    try {
                        email.send();
                        queue.remove(email);
                    } catch (EmailException e) {
                        log.debug("Sending email to {} failed again. Putting back to retry queue...", email.getToAddresses());
                        queue.put(email, numberOfRetries + 1);
                    }
                } else {
                    log.debug("Maximum number of retries for email to {} reached. Removing from retry queue...", email.getToAddresses());
                    queue.remove(email);
                }
            }
        }
    }

    public void put(Email email) {
        queue.put(email, 0);
    }

    public void terminate() {
        log.info("Received termination signal, terminating...");
        terminated = true;
        this.interrupt();
    }

    public int getRetries() {
        return retries;
    }

    public void setRetries(int retries) {
        this.retries = retries;
    }

    public int getRetryInterval() {
        return retryInterval;
    }

    public void setRetryInterval(int retryInterval) {
        this.retryInterval = retryInterval;
    }
}
