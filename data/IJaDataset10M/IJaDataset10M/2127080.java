package root;

import java.util.Date;
import java.util.TimeZone;

public class CasterClock {

    TimeZone currentZone;

    int offset;

    public CasterClock(int offset) {
        currentZone = TimeZone.getDefault();
        this.offset = offset;
    }

    public long getEpochTime() {
        return System.currentTimeMillis();
    }

    public long getServerTime() {
        boolean is_DST = TimeZone.getDefault().inDaylightTime(new Date());
        if (is_DST) {
            return getEpochTime() + CstStatic.serverOffset + CstStatic.dstOffset;
        } else {
            return getEpochTime() + CstStatic.serverOffset;
        }
    }

    public long getOffsetTime() {
        return getServerTime() + offset * CstStatic.ERINN_MINUTE;
    }

    public long getLocalTime() {
        return getEpochTime() + currentZone.getOffset(getEpochTime());
    }

    public int getServerHour() {
        return (int) (java.lang.Math.floor(new Double(getServerTime() / CstStatic.IRL_HOUR).doubleValue()) % 24);
    }

    public int getServerMinute() {
        return (int) (java.lang.Math.floor(new Double(getServerTime() / CstStatic.IRL_MINUTE).doubleValue()) % 60);
    }

    public int getServerSecond() {
        return (int) (java.lang.Math.floor(new Double(getServerTime() / CstStatic.IRL_SECOND).doubleValue()) % 60);
    }

    public int getErinnHour() {
        return (int) (java.lang.Math.floor(new Double(getOffsetTime() / CstStatic.ERINN_HOUR).doubleValue()) % 24);
    }

    public int getErinnMinute() {
        return (int) (java.lang.Math.floor(new Double(getOffsetTime() / CstStatic.ERINN_MINUTE).doubleValue()) % 60);
    }

    public int getErinnSecond() {
        return (int) (java.lang.Math.floor(new Double(getOffsetTime() / CstStatic.ERINN_SECOND).doubleValue()) % 60);
    }

    public int getLocalHour() {
        return (int) (java.lang.Math.floor(new Double(getLocalTime() / CstStatic.IRL_HOUR).doubleValue()) % 24);
    }

    public int getLocalMinute() {
        return (int) (java.lang.Math.floor(new Double(getLocalTime() / CstStatic.IRL_MINUTE).doubleValue()) % 60);
    }
}
