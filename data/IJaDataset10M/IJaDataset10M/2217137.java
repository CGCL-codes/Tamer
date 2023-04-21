package com.clanwts.bots.bissed;

import java.text.DateFormat;
import java.util.Date;
import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.config.EmbeddedConfiguration;

class Statistics {

    private static final String DB_FILE = "bissed.db";

    private static Statistics instance = new Statistics();

    public static Statistics getInstance() {
        return instance;
    }

    private final ObjectContainer db;

    private Statistics() {
        EmbeddedConfiguration dbconf = Db4oEmbedded.newConfiguration();
        db = Db4oEmbedded.openFile(dbconf, DB_FILE);
    }

    public static class GlobalStats {

        private long totalBissed;

        private Date firstBissed;

        private Statistics.UserStats mostBissed;

        private Statistics.UserStats highestBissedRate;

        public long getTotalBissed() {
            return totalBissed;
        }

        public void setTotalBissed(long totalBissed) {
            this.totalBissed = totalBissed;
        }

        public Date getFirstBissed() {
            return firstBissed;
        }

        public void setFirstBissed(Date firstBissed) {
            this.firstBissed = firstBissed;
        }

        public Statistics.UserStats getMostBissed() {
            return mostBissed;
        }

        public void setMostBissed(Statistics.UserStats mostBissed) {
            this.mostBissed = mostBissed;
        }

        public Statistics.UserStats getHighestBissedRate() {
            return highestBissedRate;
        }

        public void setHighestBissedRate(Statistics.UserStats highestBissedRate) {
            this.highestBissedRate = highestBissedRate;
        }

        public double getBissedRatePerHour(Date end) {
            long interval = end.getTime() - firstBissed.getTime();
            double bissed_per_ms = ((double) totalBissed) / ((double) interval);
            return bissed_per_ms * 1000 * 60 * 60;
        }
    }

    public static class UserStats {

        private String name;

        private long totalBissed;

        private Date firstBissed;

        private Date lastBissed;

        public long getTotalBissed() {
            return totalBissed;
        }

        public void setTotalBissed(long totalBissed) {
            this.totalBissed = totalBissed;
        }

        public Date getFirstBissed() {
            return firstBissed;
        }

        public void setFirstBissed(Date firstBissed) {
            this.firstBissed = firstBissed;
        }

        public Date getLastBissed() {
            return lastBissed;
        }

        public void setLastBissed(Date lastBissed) {
            this.lastBissed = lastBissed;
        }

        public String getName() {
            return name;
        }

        public double getBissedRatePerHour(Date end) {
            long interval = end.getTime() - firstBissed.getTime();
            double bissed_per_ms = ((double) totalBissed) / ((double) interval);
            return bissed_per_ms * 1000 * 60 * 60;
        }
    }

    public GlobalStats getGlobalStats() {
        GlobalStats instance;
        ObjectSet<Statistics.GlobalStats> os = db.query(Statistics.GlobalStats.class);
        if (os.size() != 1) {
            for (Statistics.GlobalStats gs : os) {
                db.delete(gs);
            }
            instance = new GlobalStats();
            db.store(instance);
        } else {
            instance = os.get(0);
        }
        return instance;
    }

    public UserStats getUserStats(String user) {
        UserStats us = new UserStats();
        us.name = user.toLowerCase();
        ObjectSet<Statistics.UserStats> os = db.queryByExample(us);
        if (os.size() != 1) {
            for (Statistics.UserStats us2 : os) {
                db.delete(us2);
            }
            db.store(us);
        } else {
            us = os.get(0);
        }
        return us;
    }

    public void handleUserBissed(String user) {
        UserStats us = getUserStats(user);
        us.setTotalBissed(us.getTotalBissed() + 1);
        Date now = new Date();
        if (us.getFirstBissed() == null) {
            us.setFirstBissed(now);
        }
        us.setLastBissed(now);
        db.store(us);
        GlobalStats gs = getGlobalStats();
        gs.setTotalBissed(gs.getTotalBissed() + 1);
        if (gs.getFirstBissed() == null) {
            gs.setFirstBissed(now);
        }
        if (gs.getMostBissed() == null || gs.getMostBissed().getTotalBissed() < us.getTotalBissed()) {
            gs.setMostBissed(us);
        }
        if (gs.getHighestBissedRate() == null || gs.getHighestBissedRate().getBissedRatePerHour(now) < us.getBissedRatePerHour(now)) {
            gs.setHighestBissedRate(us);
        }
        db.store(gs);
        db.commit();
    }

    public String getGlobalStatsString() {
        GlobalStats gs = getGlobalStats();
        StringBuilder sb = new StringBuilder();
        if (gs.getTotalBissed() == 0) {
            sb.append("No BISSED users recorded.");
        } else {
            long users = db.query(Statistics.UserStats.class).size();
            sb.append("A total of ");
            sb.append(users);
            sb.append((users == 1) ? " user has" : " users have");
            sb.append(" been BISSED ");
            sb.append(gs.getTotalBissed());
            sb.append((gs.getTotalBissed() == 1) ? " time" : " times");
            sb.append(" since ");
            sb.append(DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT).format(gs.getFirstBissed()));
            sb.append(" (");
            sb.append(String.format("%.2f", gs.getBissedRatePerHour(new Date())));
            sb.append(" BISSEDs/hour).");
        }
        return sb.toString();
    }

    public String getUserStatsString(String userName, String displayName) {
        UserStats us = getUserStats(userName);
        StringBuilder sb = new StringBuilder();
        sb.append(displayName);
        if (us.getTotalBissed() == 0) {
            sb.append(" has never been BISSED.");
        } else {
            sb.append(" has been BISSED ");
            sb.append(us.getTotalBissed());
            sb.append((us.getTotalBissed() == 1) ? " time" : " times");
            sb.append(" since ");
            sb.append(DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT).format(us.getFirstBissed()));
            sb.append(" (");
            sb.append(String.format("%.2f", us.getBissedRatePerHour(new Date())));
            sb.append(" BISSEDs/hour).");
        }
        return sb.toString();
    }

    public String getTopStatsString() {
        Statistics.GlobalStats gs = getGlobalStats();
        StringBuilder sb = new StringBuilder();
        if (gs.totalBissed == 0) {
            sb.append("No BISSED users recorded.");
        } else {
            sb.append("The most BISSED user is ");
            sb.append(gs.getMostBissed().name);
            sb.append(" who has been BISSED ");
            sb.append(gs.getMostBissed().getTotalBissed());
            sb.append((gs.getMostBissed().getTotalBissed() == 1) ? " time" : " times");
            sb.append(" since ");
            sb.append(DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT).format(gs.getMostBissed().getFirstBissed()));
            sb.append(" (");
            sb.append(String.format("%.2f", gs.getMostBissed().getBissedRatePerHour(new Date())));
            sb.append(" BISSEDs/hour).");
        }
        return sb.toString();
    }
}
