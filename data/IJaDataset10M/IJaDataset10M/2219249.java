package jaxlib.sql;

import java.sql.Timestamp;

/**
 * @author  <a href="mailto:joerg.wassmer@web.de">J�rg Wa�mer</a>
 * @version $Id: ImmutableTimestamp.java 1493 2005-12-05 09:12:49Z joerg_wassmer $
 */
public class ImmutableTimestamp extends Timestamp {

    /**
   * @since JaXLib 1.0
   */
    private static final long serialVersionUID = 1L;

    public static Timestamp asImmutableTimestamp(java.util.Date v) {
        if (v == null) return null; else if (v instanceof ImmutableTimestamp) return (ImmutableTimestamp) v; else return new ImmutableTimestamp(v.getTime());
    }

    public ImmutableTimestamp(long timeAsMillis) {
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
    public final void setNanos(int v) {
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
