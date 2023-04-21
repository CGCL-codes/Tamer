package org.privale.coreclients.server2client;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import org.privale.coreclients.cryptoclient.ServerTimeIndex;
import org.privale.coreclients.cryptoclient.ServerTimeIndexUpdater;
import org.privale.coreclients.cryptoclient.TimeIndexException;
import org.privale.utils.ChannelReader;

public class TimeKeeper implements ServerTimeIndexUpdater, Serializable {

    private static final long serialVersionUID = 1L;

    private long TimeDiff;

    private LinkedList<TimeIndex> Indexes;

    private String ServerString;

    private boolean HasBeenUpdated;

    public TimeKeeper() {
        Indexes = new LinkedList<TimeIndex>();
        HasBeenUpdated = false;
    }

    public void setServerString(String ss) {
        ServerString = ss;
    }

    public void ProcessUpdate(File data) {
        try {
            ChannelReader cr = new ChannelReader(data);
            long curtime = cr.getLong();
            Date d = new Date();
            TimeDiff = curtime - d.getTime();
            int numberindexes = cr.getInt();
            long lastcreated = 0;
            synchronized (Indexes) {
                if (Indexes.size() > 0) {
                    lastcreated = Indexes.getLast().CreateTime;
                }
            }
            for (int cnt = 0; cnt < numberindexes; cnt++) {
                TimeIndex ti = new TimeIndex();
                cr.Read(ti);
                if (ti.CreateTime > lastcreated) {
                    lastcreated = ti.CreateTime;
                    synchronized (Indexes) {
                        if (!Indexes.contains(ti)) {
                            Date da = new Date(ti.CreateTime);
                            System.out.println("  DOES NOT CONTAIN!  " + da + " " + ti.IndexValue);
                            Indexes.add(ti);
                        } else {
                            System.out.println("  OK");
                        }
                    }
                }
            }
            cr.close();
            HasBeenUpdated = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean hasBeenUpdated() {
        return HasBeenUpdated;
    }

    public void RemoveOld() {
        synchronized (Indexes) {
            Date d = new Date();
            long curtime = d.getTime();
            long tooold = curtime - CoreServer2.MAXTIMEINDEXVALID + TimeDiff;
            Iterator i = Indexes.iterator();
            while (i.hasNext()) {
                TimeIndex ti = (TimeIndex) i.next();
                if (ti.CreateTime < tooold) {
                    i.remove();
                }
            }
        }
    }

    public long getNeededBackTime() {
        long backtime = CoreServer2.MAXTIMEINDEXVALID;
        synchronized (Indexes) {
            long curtime = (new Date()).getTime();
            if (Indexes.size() > 0) {
                TimeIndex first = Indexes.getFirst();
                if (first.CreateTime < curtime + TimeDiff + CoreServer2.NEWTIMEINDEXINTERVAL - CoreServer2.MAXTIMEINDEXVALID) {
                    TimeIndex last = Indexes.getLast();
                    long lastindexbacktime = curtime + TimeDiff - last.CreateTime;
                    System.out.println(lastindexbacktime + " = " + curtime + " + " + TimeDiff + " - " + last.CreateTime);
                    System.out.println(lastindexbacktime + " < " + (long) ((double) CoreServer2.NEWTIMEINDEXINTERVAL * 1.5D));
                    if (lastindexbacktime < (long) ((double) CoreServer2.NEWTIMEINDEXINTERVAL * 1.5D)) {
                        backtime = 0;
                    } else {
                        backtime = (long) ((double) lastindexbacktime * 2D);
                    }
                }
            }
        }
        return backtime;
    }

    public boolean isServerTimeIndexValid(ServerTimeIndex idx) {
        boolean rv = false;
        synchronized (Indexes) {
            System.out.println("Checking for index: " + idx.getTimeIndex());
            System.out.print("VALUES IN INDEXES: ");
            for (TimeIndex i : Indexes) {
                System.out.print("," + i.IndexValue);
            }
            System.out.println();
            TimeIndex testi = new TimeIndex();
            testi.IndexValue = idx.getTimeIndex();
            rv = Indexes.contains(testi);
        }
        return rv;
    }

    public void setServerTimeIndex(ServerTimeIndex idx) throws TimeIndexException {
        TimeIndex useidx = null;
        int trys = 60;
        while (Indexes.size() == 0 && trys > 0) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            trys--;
        }
        synchronized (Indexes) {
            if (Indexes.size() > 0) {
                useidx = Indexes.get(Math.max(0, Indexes.size() - 3));
                for (TimeIndex i : Indexes) {
                    System.out.println("== " + i.IndexValue);
                }
            } else {
                throw new TimeIndexException("ERROR: NO INDEXES FOUND!");
            }
        }
        System.out.println("SETTING INDEX: " + useidx.IndexValue);
        idx.setCreationTime(useidx.CreateTime);
        idx.setTimeIndex(useidx.IndexValue);
        idx.setServerString(ServerString);
    }
}
