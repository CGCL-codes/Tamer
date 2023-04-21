package org.light.portal.core.service.impl;

import static org.light.portal.util.Constants._PORTAL_CLIENT_LISTEN_SERVER_INTERVAL;
import static org.light.portal.util.Constants._STATUS_OFFLINE;
import static org.light.portal.util.Constants._STATUS_ONLINE;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.light.portal.core.service.StatusService;
import org.light.portal.logger.Logger;
import org.light.portal.logger.LoggerFactory;
import org.light.portal.user.model.User;

/**
 * 
 * @author Jianmin Liu
 **/
public class StatusServiceImpl extends BaseServiceImpl implements StatusService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private Map<Long, Integer> statusMap = new ConcurrentHashMap<Long, Integer>();

    private ThreadPoolExecutor executor = new ThreadPoolExecutor(1, 1, 600000, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());

    public StatusServiceImpl() {
        int interval = _PORTAL_CLIENT_LISTEN_SERVER_INTERVAL * 2;
        new Cleaner(interval);
    }

    public void check(User user) {
        executor.execute(new Checker(user));
    }

    private void clean() {
        synchronized (statusMap) {
            Set<Long> userIds = statusMap.keySet();
            if (userIds == null) return;
            logger.debug(String.format("System checking online %d users Status ...", userIds.size()));
            for (long userId : userIds) {
                int status = statusMap.get(userId);
                statusMap.remove(userId);
                if (status == _STATUS_ONLINE) {
                    statusMap.put(userId, _STATUS_OFFLINE);
                } else {
                    User user = (User) this.getEntityById(User.class, userId);
                    if (user != null && user.getCurrentStatus() != _STATUS_OFFLINE) {
                        user.setCurrentStatus(_STATUS_OFFLINE);
                        this.save(user);
                        logger.debug(String.format("System consider User %s[%d] is off line ...", user.getName(), user.getId()));
                    }
                }
            }
        }
    }

    class Checker implements Runnable {

        User user;

        public Checker(User user) {
            this.user = user;
        }

        public void run() {
            if (user == null) return;
            synchronized (statusMap) {
                if (user.getCurrentStatus() == _STATUS_ONLINE) {
                    if (statusMap.get(user.getId()) != null) statusMap.remove(user.getId());
                    statusMap.put(user.getId(), user.getCurrentStatus());
                } else {
                    statusMap.remove(user.getId());
                }
            }
        }
    }

    class Cleaner implements Runnable {

        long interval;

        public Cleaner(long interval) {
            this.interval = interval;
            new Thread(this).start();
        }

        public void run() {
            try {
                while (true) {
                    Thread.sleep(interval);
                    try {
                        clean();
                    } catch (Throwable e) {
                        logger.error("ERROR: " + e.getMessage());
                    }
                }
            } catch (Throwable e) {
                logger.error("ERROR: " + e.getMessage());
            }
        }
    }
}
