package jaxlib.cache.jmx;

import java.io.IOException;
import java.util.Date;
import jaxlib.jmx.annotation.JmxInt;
import jaxlib.jmx.annotation.JmxLong;

/**
 * 
 * 
 * @author  <a href="mailto:joerg.wassmer@web.de">Joerg Wassmer</a>
 * @since   JaXLib 1.0
 * @version $Id: CacheMBean.java 2297 2007-03-28 05:25:19Z joerg_wassmer $
 */
@javax.management.MXBean
public interface CacheMBean {

    public void clear() throws IOException;

    public void close() throws IOException;

    @JmxInt(min = 0)
    public int getCapacity() throws IOException;

    public Date getDateClosed();

    public Date getDateOpened();

    @JmxLong(min = 0)
    public long getHits();

    @JmxLong(min = 0)
    public long getHitsInMemoryStore();

    @JmxLong(min = 0)
    public long getHitsInPersistentStore();

    @JmxLong(min = 0)
    public int getLargestSize();

    @JmxLong(min = 0)
    public long getMisses();

    @JmxLong(min = 0)
    public long getMissesBecauseAbsent();

    @JmxLong(min = 0)
    public long getMissesBecauseInvalid();

    @JmxInt(min = 0)
    public int getSize() throws IOException;

    public boolean isOpen();
}
