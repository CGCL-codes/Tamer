package org.log4j.helpers;

import org.log4j.spi.LoggingEvent;

/**

   CyclicBuffer is used by other appenders to hold {@link LoggingEvent
   LoggingEvents} for immediate or differed display.
   
   <p>This buffer gives read access to any element in the buffer not
   just the first or last element.

   @author <A HREF="mailto:cgu@urbanet.ch">Ceki G&uuml;lc&uuml;</a> 
   @since 0.9.0

 */
public class CyclicBuffer {

    LoggingEvent[] ea;

    int first;

    int last;

    int numElems;

    int maxSize;

    /**
     Instantiate a new CyclicBuffer of at most <code>maxSize</code> events.

     The <code>maxSize</code> argument must a positive integer.

     @param maxSize The maximum number of elements in the buffer.
  */
    public CyclicBuffer(int maxSize) throws IllegalArgumentException {
        if (maxSize < 1) {
            throw new IllegalArgumentException("The maxSize argument (" + maxSize + ") is not a positive integer.");
        }
        this.maxSize = maxSize;
        ea = new LoggingEvent[maxSize];
        first = 0;
        last = 0;
        numElems = 0;
    }

    /**
     Add an <code>event</code> as the last event in the buffer.

   */
    public synchronized void add(LoggingEvent event) {
        ea[last] = event;
        if (++last == maxSize) last = 0;
        if (numElems < maxSize) numElems++; else if (++first == maxSize) first = 0;
    }

    /**
     Get the <i>i</i>th oldest event currently in the buffer. If
     <em>i</em> is outside the range 0 to the number of elements
     currently in the buffer, then <code>null</code> is returned.


  */
    public synchronized LoggingEvent get(int i) {
        if (i < 0 || i >= numElems) return null;
        return ea[(first + i) % maxSize];
    }

    public int getMaxSize() {
        return maxSize;
    }

    /**
     Get the number of elements in the buffer. This number is
     guaranteed to be in the range 0 to <code>maxSize</code>
     (inclusive).

  */
    public synchronized int length() {
        return numElems;
    }
}
