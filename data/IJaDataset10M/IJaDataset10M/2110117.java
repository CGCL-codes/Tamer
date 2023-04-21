package jaxlib.sql;

import java.sql.Time;

/**
 * @author  <a href="mailto:joerg.wassmer@web.de">J�rg Wa�mer</a>
 * @version $Id: ImmutableTime.java 1493 2005-12-05 09:12:49Z joerg_wassmer $
 */
public final class ImmutableTime extends Time {

    /**
   * @since JaXLib 1.0
   */
    private static final long serialVersionUID = 1L;

    public static Time asImmutableTime(java.util.Date v) {
        if (v == null) return null; else if (v instanceof ImmutableTime) return (ImmutableTime) v; else return new ImmutableTime(v.getTime());
    }

    public ImmutableTime(long timeAsMillis) {
        super(timeAsMillis);
    }

    @Override
    @Deprecated
    public final void setDate(int v) {
        throw new UnsupportedOperationException();
    }

    @Override
    @Deprecated
    public final void setMinutes(int v) {
        throw new UnsupportedOperationException();
    }

    @Override
    @Deprecated
    public final void setMonth(int v) {
        throw new UnsupportedOperationException();
    }

    @Override
    @Deprecated
    public final void setSeconds(int v) {
        throw new UnsupportedOperationException();
    }

    @Override
    @Deprecated
    public final void setTime(long v) {
        throw new UnsupportedOperationException();
    }

    @Override
    @Deprecated
    public final void setYear(int v) {
        throw new UnsupportedOperationException();
    }
}
