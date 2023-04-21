package ch.olsen.products.util.historicalrun;

import java.io.ObjectStreamException;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.TimeZone;
import ch.olsen.products.util.Currency;
import ch.olsen.products.util.Instrument;
import ch.olsen.products.util.configuration.ConfigurationContainer;
import ch.olsen.products.util.configuration.InternalConfigurationException;
import ch.olsen.products.util.database.Tick;
import ch.olsen.products.util.logging.Logger;
import ch.olsen.products.util.logging.LoggerImplSun;
import ch.olsen.products.util.database.DriverShim;

public class HistoricalFeed {

    public ConfigurationContainer<HistoricalRunConfiguration> cfg;

    protected final Map<Instrument, FeedUpdate> dbs;

    List<FeedUpdate> pending = null;

    protected Map<Instrument, AvailableInstrument> availableInstruments;

    private static final long serialVersionUID = 1L;

    Logger log;

    DBThread dbThread;

    boolean killSignal = false;

    boolean killed = true;

    private static final Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));

    public HistoricalFeed() throws InternalConfigurationException {
        this(null);
    }

    public HistoricalFeed(ConfigurationContainer<HistoricalRunConfiguration> cfg) throws InternalConfigurationException {
        if (cfg == null) cfg = new ConfigurationContainer<HistoricalRunConfiguration>(new HistoricalRunConfiguration());
        this.cfg = cfg;
        dbs = new LinkedHashMap<Instrument, FeedUpdate>();
        log = LoggerImplSun.getLogger("FXTickDB");
        dbThread = new DBThread();
        killed = false;
        new Thread(dbThread, "DBThread").start();
    }

    public void setup(Collection<Instrument> c) {
        try {
            for (Instrument i : c) {
                AvailableInstrument ai = getInstruments().get(i);
                if (ai != null) {
                    if (ai.startDate > cfg.get().begin.value().getTime()) dbs.put(i, new SyntheticFeedUpdate(i, ai.startDate)); else {
                        FeedUpdate fu = dbs.get(i);
                        if (fu == null) dbs.put(i, new DBFeedUpdate(i));
                    }
                } else {
                    FeedUpdate fu = dbs.get(i);
                    if (fu == null) dbs.put(i, new SyntheticFeedUpdate(i, Long.MAX_VALUE / 4));
                }
            }
        } catch (Exception e) {
            log.error("Unable to setup historical run: " + e.getMessage());
        }
    }

    public final void dispose() {
        killSignal = true;
        synchronized (dbThread) {
            dbThread.notifyAll();
        }
    }

    public final Tick nextTick() throws NoSuchElementException {
        if (pending == null) {
            pending = new LinkedList<FeedUpdate>();
            Iterator<FeedUpdate> iterator = dbs.values().iterator();
            while (iterator.hasNext()) {
                FeedUpdate fu = iterator.next();
                if (fu instanceof SyntheticFeedUpdate) continue;
                fu.start();
                Tick tick = fu.next();
                if (tick != null) {
                    ListIterator<FeedUpdate> listIt = pending.listIterator(pending.size());
                    while (listIt.hasPrevious()) {
                        FeedUpdate next = listIt.previous();
                        if (next.getLastTick().getStamp().compareTo(fu.getLastTick().getStamp()) <= 0) {
                            listIt.next();
                            break;
                        }
                    }
                    listIt.add(fu);
                } else {
                    log.warn("Instrument " + fu.getInstrument() + " does not contain any tick");
                    iterator.remove();
                }
            }
            iterator = dbs.values().iterator();
            while (iterator.hasNext()) {
                FeedUpdate fu = iterator.next();
                if (fu instanceof DBFeedUpdate) continue;
                fu.start();
                Tick tick = fu.next();
                if (tick != null) {
                    ListIterator<FeedUpdate> listIt = pending.listIterator(pending.size());
                    while (listIt.hasPrevious()) {
                        FeedUpdate next = listIt.previous();
                        if (next.getLastTick().getStamp().compareTo(fu.getLastTick().getStamp()) <= 0) {
                            listIt.next();
                            break;
                        }
                    }
                    listIt.add(fu);
                } else {
                    log.warn("Instrument " + fu.getInstrument() + " does not contain any tick");
                    iterator.remove();
                }
            }
        }
        if (!pending.isEmpty()) {
            FeedUpdate current = pending.remove(0);
            Tick toReturn = current.getLastTick();
            if (current.next() != null) {
                ListIterator<FeedUpdate> listIt = pending.listIterator(pending.size());
                while (listIt.hasPrevious()) {
                    FeedUpdate next = listIt.previous();
                    if (next.getLastTick().getStamp().compareTo(current.getLastTick().getStamp()) <= 0) {
                        listIt.next();
                        break;
                    }
                }
                listIt.add(current);
            } else {
                log.info("Data end for: " + current.getInstrument() + " @ " + current.getLastTick());
            }
            return toReturn;
        }
        throw new NoSuchElementException();
    }

    public interface FeedUpdate extends Serializable {

        void start();

        Tick next();

        Instrument getInstrument();

        Tick getLastTick();
    }

    public class SyntheticFeedUpdate implements FeedUpdate {

        private static final long serialVersionUID = 1L;

        Instrument instrument;

        Tick lastTick;

        DBFeedUpdate db1;

        DBFeedUpdate db2;

        long dbDataStart;

        DBFeedUpdate dbAtEnd;

        /**
		 * 
		 * @param instrument
		 * @param dbDataStart at a certain data, we should start using real db data and not
		 * create synthetic data
		 * @throws Exception
		 */
        public SyntheticFeedUpdate(Instrument instrument, long dbDataStart) throws Exception {
            this.instrument = instrument;
            this.dbDataStart = dbDataStart;
            List<Instrument> l = new LinkedList<Instrument>();
            List<Instrument> s = new LinkedList<Instrument>();
            for (AvailableInstrument ai : getInstruments().values()) {
                if (ai.startDate > cfg.get().begin.value().getTime()) continue;
                Instrument i = ai.i;
                if (i.getPer() == instrument.getPer() || i.getExp() == instrument.getPer()) l.add(i);
                if (i.getPer() == instrument.getExp() || i.getExp() == instrument.getExp()) s.add(i);
            }
            List<Instrument> instr1 = new ArrayList<Instrument>();
            List<Instrument> instr2 = new ArrayList<Instrument>();
            for (Instrument i : l) {
                Currency other = i.getPer() == instrument.getPer() ? i.getExp() : i.getPer();
                Instrument found = null;
                for (Instrument i2 : s) {
                    if (other == i2.getPer() || other == i2.getExp()) {
                        found = i2;
                        break;
                    }
                }
                if (found != null) {
                    instr1.add(i);
                    instr2.add(found);
                }
            }
            if (instr1.size() > 0) {
                int n = 0;
                for (n = 0; n < instr1.size(); n++) {
                    if ((instr1.get(n).getPer() == Currency.USD || instr1.get(n).getExp() == Currency.USD) && (instr2.get(n).getPer() == Currency.USD || instr2.get(n).getExp() == Currency.USD)) break;
                }
                if (n == instr1.size()) {
                    for (n = 0; n < instr1.size(); n++) {
                        if ((instr1.get(n).getPer() == Currency.EUR || instr1.get(n).getExp() == Currency.EUR) && (instr2.get(n).getPer() == Currency.EUR || instr2.get(n).getExp() == Currency.EUR)) break;
                    }
                }
                if (n == instr1.size()) n = 0;
                FeedUpdate db = dbs.get(instr1.get(n));
                if (db == null) {
                    db1 = new DBFeedUpdate(instr1.get(n));
                    dbs.put(instr1.get(n), db1);
                } else {
                    db1 = (DBFeedUpdate) db;
                }
                db = dbs.get(instr2.get(n));
                if (db == null) {
                    db2 = new DBFeedUpdate(instr2.get(n));
                    dbs.put(instr2.get(n), db2);
                } else {
                    db2 = (DBFeedUpdate) db;
                }
            } else {
                throw new Exception("Could not create synthetic data");
            }
        }

        public final Instrument getInstrument() {
            return instrument;
        }

        public final Tick getLastTick() {
            return lastTick;
        }

        public final Tick next() {
            if (lastTick != null && lastTick.getStamp().getTime() > dbDataStart) {
                if (dbAtEnd == null) {
                    dbAtEnd = new DBFeedUpdate(instrument);
                    dbAtEnd.start();
                }
                lastTick = dbAtEnd.next();
                return lastTick;
            }
            if (db1.tickIndex == DBFeedUpdate.NOMOREDATA || db2.tickIndex == DBFeedUpdate.NOMOREDATA) return null;
            double bid = 1.0;
            double ask = 1.0;
            if (instrument.getPer() == db1.instrument.getPer()) {
                ask *= db1.lastTick.getAsk();
                bid *= db1.lastTick.getBid();
            } else {
                ask /= db1.lastTick.getBid();
                bid /= db1.lastTick.getAsk();
            }
            if (instrument.getExp() == db2.instrument.getExp()) {
                ask *= db2.lastTick.getAsk();
                bid *= db2.lastTick.getBid();
            } else {
                ask /= db2.lastTick.getBid();
                bid /= db2.lastTick.getAsk();
            }
            Date stamp = db1.lastTick.getStamp().getTime() > db2.lastTick.getStamp().getTime() ? db1.lastTick.getStamp() : db2.lastTick.getStamp();
            lastTick = new Tick(instrument, bid, ask, stamp);
            return lastTick;
        }

        public final void start() {
        }
    }

    /**
	 * inner class which actually manages the connection to the database
	 * for a single instrument
	 *
	 * 
	 * @author Olsen Ltd.
	 */
    class DBFeedUpdate implements FeedUpdate {

        private static final long serialVersionUID = 1L;

        final Instrument instrument;

        long lastReturned = 0L;

        private static final int querySize = 7000;

        private static final int NOMOREDATA = -1;

        Tick ticks[] = new Tick[querySize];

        Tick ticks2[] = new Tick[querySize];

        int tickIndex = 0;

        Tick lastTick;

        boolean secondDB = false;

        public DBFeedUpdate(Instrument i) {
            this.instrument = i;
        }

        /**
		 * connects to the database and executes the query. Queries are
		 * splitted into many each one quering for ticks of a single day
		 *
		 */
        public final void start() {
            dbThread.addJob(this);
            synchronized (this) {
                while (dbThread.jobs.contains(this)) {
                    try {
                        this.wait();
                    } catch (InterruptedException e) {
                    }
                }
            }
            final Tick tmp[] = ticks;
            ticks = ticks2;
            ticks2 = tmp;
            dbThread.addJob(this);
        }

        /**
		 * return the next tick
		 * @return the next tick
		 */
        public final Tick next() {
            if (tickIndex == NOMOREDATA) return null;
            if (tickIndex == querySize || ticks[tickIndex] == null) {
                getNextData();
            }
            if (tickIndex == NOMOREDATA) return null;
            lastTick = ticks[tickIndex++];
            return lastTick;
        }

        private void getNextData() {
            synchronized (this) {
                long start = Calendar.getInstance().getTimeInMillis();
                while (dbThread.jobs.contains(this)) {
                    try {
                        this.wait(1000);
                    } catch (InterruptedException e) {
                    }
                }
                long end = Calendar.getInstance().getTimeInMillis();
                if (end - start > 1000L) {
                    log.warn("Waited " + (end - start) / 1000 + " secs for new db data");
                }
            }
            final Tick tmp[] = ticks;
            ticks = ticks2;
            ticks2 = tmp;
            tickIndex = 0;
            if (ticks[0] == null) {
                tickIndex = NOMOREDATA;
                return;
            }
            dbThread.addJob(this);
        }

        /**
		 * when serializing, this class has to be excluded since it contains
		 * references to database connection which would became inconsistent
		 * after deserialization
		 * @return a replacement object for serialization
		 * @throws ObjectStreamException
		 */
        protected Object writeReplace() throws ObjectStreamException {
            return new FeedUpdateReplacement(this);
        }

        public final Instrument getInstrument() {
            return instrument;
        }

        public final Tick getLastTick() {
            return lastTick;
        }
    }

    class DBThread implements Runnable {

        FXTickDb db1 = null;

        FXTickDb db2 = null;

        LinkedHashSet<DBFeedUpdate> jobs = new LinkedHashSet<DBFeedUpdate>();

        public void run() {
            HistoricalRunConfiguration cfg = HistoricalFeed.this.cfg.get();
            db1 = new FXTickDb();
            long start = new Date().getTime();
            db1.connect(cfg.dbUser.value(), cfg.dbPwd.value(), cfg.dbServer.value(), cfg.dbName.value());
            long end = new Date().getTime();
            if (end - start > 1000L * 10L) {
                log.warn("Connecting to db took " + (end - start) / 1000 + " secs");
            }
            DBFeedUpdate job;
            long lastActivity = new Date().getTime();
            while (!killSignal) {
                job = null;
                synchronized (dbThread) {
                    if (jobs.size() == 0) {
                        try {
                            dbThread.wait(10000);
                        } catch (InterruptedException e) {
                        }
                    }
                    if (jobs.size() > 0) {
                        job = jobs.iterator().next();
                    } else {
                        long timeNow = new Date().getTime();
                        if (timeNow - lastActivity > 1000L * 60L * 1L) {
                            killSignal = true;
                        }
                    }
                }
                if (job != null) {
                    executeNextQuery(job);
                    synchronized (job) {
                        synchronized (this) {
                            jobs.remove(job);
                        }
                        job.notifyAll();
                    }
                    lastActivity = new Date().getTime();
                }
            }
            die();
        }

        private final void die() {
            killSignal = true;
            db1.disconnect();
            if (db2 != null) db2.disconnect();
            synchronized (this) {
                killed = true;
                this.notifyAll();
            }
        }

        public void addJob(DBFeedUpdate fu) {
            synchronized (this) {
                while (!killed && killSignal) {
                    try {
                        this.wait(1000);
                    } catch (InterruptedException e) {
                    }
                }
                if (killed) {
                    killed = false;
                    killSignal = false;
                    new Thread(dbThread, "DBThread").start();
                }
                jobs.add(fu);
                this.notifyAll();
            }
        }

        /**
		 * executes the query for the next day
		 * @return true if successful
		 */
        final void executeNextQuery(DBFeedUpdate fu) {
            long begin = fu.lastReturned == 0L ? cfg.get().begin.value().getTime() - 1 : fu.lastReturned;
            int n = 0;
            FXTickDb db = fu.secondDB ? db2 : db1;
            try {
                db.executeQuery(fu.instrument.toString(), new Timestamp(begin), 0, DBFeedUpdate.querySize);
                while (db.next()) {
                    if (db.time().getTime() > cfg.get().end.value().getTime()) break;
                    fu.ticks2[n++] = new Tick(fu.instrument, db.bid(), db.ask(), db.time());
                    fu.lastReturned = db.time().getTime();
                }
                if (n == 0 && !fu.secondDB) {
                    fu.secondDB = true;
                    db2 = new FXTickDb();
                    HistoricalRunConfiguration cfg = HistoricalFeed.this.cfg.get();
                    db2.connect(cfg.dbUser.value(), cfg.dbPwd.value(), cfg.dbServer2.value(), cfg.dbName2.value());
                    db = db2;
                    db.executeQuery(fu.instrument.toString(), new Timestamp(begin), 0, DBFeedUpdate.querySize);
                    while (db.next()) {
                        if (db.time().getTime() > cfg.end.value().getTime()) break;
                        fu.ticks2[n++] = new Tick(fu.instrument, db.bid(), db.ask(), db.time());
                        fu.lastReturned = db.time().getTime();
                    }
                }
            } catch (Exception e) {
                if (fu.instrument == Instrument.GAU_GAU || fu.instrument == Instrument.FGN_FGN || fu.instrument == Instrument.FBM_FBM) log.info("No data in " + db.getDbName() + " for " + fu.instrument); else {
                    System.err.println("Could not execute next query: " + e.getMessage());
                    e.printStackTrace();
                    db.reconnect(cfg.get().dbUser.value(), cfg.get().dbPwd.value(), getDBHost(fu), getDBName(fu));
                }
            } finally {
                for (; n < DBFeedUpdate.querySize; n++) fu.ticks2[n] = null;
                try {
                    db.closeResultSet();
                } catch (Exception e) {
                }
            }
        }

        private final String getDBHost(DBFeedUpdate fu) {
            if (!fu.secondDB) return cfg.get().dbServer.value();
            return cfg.get().dbServer2.value();
        }

        private final String getDBName(DBFeedUpdate fu) {
            if (!fu.secondDB) return cfg.get().dbName.value();
            return cfg.get().dbName2.value();
        }
    }

    /**
	 * It is a replacement object for serialization. As soon as it is 
	 * deserialized, it will put again in place the FeedUpdate
	 *
	 * 
	 * @author Olsen Ltd.
	 */
    class FeedUpdateReplacement implements Serializable {

        private static final long serialVersionUID = 1L;

        final Instrument instrument;

        public FeedUpdateReplacement(DBFeedUpdate fu) {
            this.instrument = fu.instrument;
        }

        protected Object readResolve() throws ObjectStreamException {
            return new DBFeedUpdate(instrument);
        }
    }

    public Map<Instrument, AvailableInstrument> getInstruments() throws Exception {
        if (availableInstruments == null) buildAvailableInstruments();
        return availableInstruments;
    }

    public class AvailableInstrument implements Serializable {

        private static final long serialVersionUID = 1L;

        Instrument i;

        long startDate;

        public AvailableInstrument(Instrument i, long startDate) {
            this.i = i;
            this.startDate = startDate;
        }
    }

    private void buildAvailableInstruments() throws Exception {
        try {
            if (Thread.currentThread().getContextClassLoader() != Object.class.getClassLoader()) {
                Driver d = (Driver) Class.forName("com.mysql.jdbc.Driver", true, Thread.currentThread().getContextClassLoader()).newInstance();
                DriverManager.registerDriver(new DriverShim(d));
            } else {
                Class.forName("com.mysql.jdbc.Driver");
            }
            String url = "jdbc:mysql://" + cfg.get().dbServer.value() + "/" + cfg.get().dbName.value() + "?user=" + cfg.get().dbUser.value() + "&password=" + cfg.get().dbPwd.value();
            Connection dbConnection = DriverManager.getConnection(url);
            Statement statement = dbConnection.createStatement();
            String sql = "SHOW TABLES";
            ResultSet result = statement.executeQuery(sql);
            availableInstruments = new HashMap<Instrument, AvailableInstrument>();
            while (result.next()) {
                String s = result.getString(1);
                try {
                    Instrument i = Instrument.valueOf(s);
                    String sql2 = "SELECT time FROM " + s + " where bid > 0 AND ask > 0 ORDER BY time LIMIT 1;";
                    Statement statement2 = dbConnection.createStatement();
                    ResultSet result2 = statement2.executeQuery(sql2);
                    if (result2.next()) {
                        long firstTick = result2.getTimestamp("time", cal).getTime();
                        if (firstTick < 1000) continue;
                        availableInstruments.put(i, new AvailableInstrument(i, firstTick));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (availableInstruments.size() == 0) {
                availableInstruments = null;
                throw new Exception("No Instrument found in database, giving up");
            }
        } catch (Exception e) {
            log.warn("Unable to get list of available instruments: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
}
