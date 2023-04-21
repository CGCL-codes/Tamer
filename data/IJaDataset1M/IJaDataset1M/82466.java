package org.ivoa.util.text;

import org.ivoa.bean.SingletonSupport;
import org.ivoa.util.SystemLogUtil;
import org.ivoa.util.concurrent.ThreadLocalUtils;
import org.ivoa.util.stat.StatLong;
import org.ivoa.util.timer.TimerFactory;
import org.ivoa.util.timer.TimerFactory.UNIT;

/**
 * ThreadLocal StringBuilder for performance
 * 
 * @author Laurent Bourges (voparis) / Gerard Lemson (mpe)
 */
public final class LocalStringBuilder extends SingletonSupport {

    /** flag to execute the micro benchmark at startup */
    private static final boolean DO_MICRO_BENCHMARK = false;

    /** buffer thread Local */
    private static ThreadLocal<StringBuilderContext> bufferLocal = ThreadLocalUtils.registerRequestThreadLocal(new StringBuilderThreadLocal());

    /**
   * Forbidden constructor
   */
    private LocalStringBuilder() {
        super();
    }

    /**
   * Prepare the LocalStringBuilder singleton instance
   * 
   * @throws IllegalStateException if a problem occured
   */
    public static final void prepareInstance() {
        prepareInstance(new LocalStringBuilder());
        if (DO_MICRO_BENCHMARK) {
            microbenchmark();
        }
    }

    /**
   * Concrete implementations of the SingletonSupport's clearStaticReferences() method :<br/>
   * Callback to clean up the possible static references used by this SingletonSupport instance iso
   * clear static references
   * 
   * @see SingletonSupport#clearStaticReferences()
   */
    @Override
    protected void clearStaticReferences() {
        bufferLocal = null;
    }

    /**
   * Concatenates the given values
   * @param values string values to concat
   * @return string result
   */
    public static final String concat(final String... values) {
        final StringBuilder sb = LocalStringBuilder.getBuffer();
        for (final String n : values) {
            sb.append(n);
        }
        return LocalStringBuilder.toString(sb);
    }

    /**
   * Return the string contained in the current local buffer ONLY.<br/>
   * DO NOT release the buffer
   *
   * @param sb buffer
   * @return string contained in the given buffer
   */
    public static final String extract(final StringBuffer sb) {
        String s = null;
        if (sb != null) {
            s = sb.toString();
            sb.setLength(0);
        }
        return s;
    }

    /**
   * Return the string contained in the current local buffer ONLY.<br/>
   * DO NOT release the buffer
   *
   * @param sb buffer
   * @return string contained in the given buffer
   */
    public static final String extract(final StringBuilder sb) {
        String s = null;
        if (sb != null) {
            s = sb.toString();
            StringBuilderContext.resetStringBuilder(sb);
        }
        return s;
    }

    /**
   * Return an empty threadLocal StringBuilder instance.<br/>
   * MUST BE RELEASED after use by calling toString(StringBuilder) or toStringBuilder(StringBuilder)
   * methods
   * 
   * @see #toString(StringBuilder)
   * @see #toStringBuilder(StringBuilder, StringBuilder)
   * @return StringBuilder threadLocal instance
   */
    public static final StringBuilder getBuffer() {
        if (SingletonSupport.isRunning()) {
            return bufferLocal.get().acquire();
        }
        if (StringBuilderContext.DIAGNOSTICS && logB.isInfoEnabled()) {
            logB.info("LocalStringBuilder.getBuffer : createStringBuilder because threadLocal is not running : ", new Throwable());
        }
        return StringBuilderContext.createStringBuilder();
    }

    /**
   * Return the current threadLocal StringBuilder instance or a empty threadLocal StringBuilder
   * instance.<br/>
   * MUST BE RELEASED after use by calling toString(StringBuilder) or toStringBuilder(StringBuilder)
   * methods
   * 
   * @see #toString(StringBuilder)
   * @see #toStringBuilder(StringBuilder, StringBuilder)
   * @return StringBuilder threadLocal instance
   */
    public static final StringBuilder getCurrentBuffer() {
        if (SingletonSupport.isRunning()) {
            final StringBuilderContext ctx = bufferLocal.get();
            StringBuilder sb = ctx.current();
            if (sb == null) {
                sb = ctx.acquire();
            }
            return sb;
        }
        return StringBuilderContext.createStringBuilder();
    }

    /**
   * Free the thread local Context associated to the current thread
   */
    public static final void freeBuffer() {
        if (SingletonSupport.isRunning()) {
            if (logB.isInfoEnabled()) {
                logB.info("LocalStringBuilder.freeBuffer : " + bufferLocal.get());
            }
            bufferLocal.remove();
        }
    }

    /**
   * Return the string contained in the current local buffer and release the buffer
   * 
   * @param sb buffer (optional)
   * @return string contained in the given buffer
   */
    public static final String toString(final StringBuilder sb) {
        String s = null;
        if (SingletonSupport.isRunning()) {
            final StringBuilderContext ctx = bufferLocal.get();
            final StringBuilder sbLocal = ctx.current();
            s = sb != null ? sb.toString() : sbLocal != null ? sbLocal.toString() : null;
            ctx.release(sbLocal);
        } else {
            if (sb != null) {
                s = sb.toString();
                StringBuilderContext.resetStringBuilder(sb);
            }
        }
        return s;
    }

    /**
   * Unsynchronized copy from the current local buffer to another buffer and release the buffer
   * 
   * @param sb buffer (optional)
   * @param sbTo destination buffer
   */
    public static final void toStringBuilder(final StringBuilder sb, final StringBuilder sbTo) {
        if (SingletonSupport.isRunning()) {
            final StringBuilderContext ctx = bufferLocal.get();
            final StringBuilder sbLocal = ctx.current();
            sbTo.append(sb != null ? sb : sbLocal);
            ctx.release(sbLocal);
        } else {
            if (sb != null) {
                sbTo.append(sb);
                StringBuilderContext.resetStringBuilder(sb);
            }
        }
    }

    /**
   * MicroBenchmark : LocalStringBuilder : Low: Timer(ms) {n=9799, min=1676.0, max=9778.0,
   * acc=2.6544453E7, avg=2708.8940708235536} - High: Timer(ms) {n=201, min=10057.0, max=1062985.0,
   * acc=3538450.0, avg=17604.228855721394}
   */
    protected static void microbenchmark() {
        if (logB.isWarnEnabled()) {
            final String value = SystemLogUtil.LOG_LINE_SEPARATOR;
            final StatLong globalStat = new StatLong();
            final int loops = 10;
            final int cycles = 10000;
            long start;
            StringBuilder sb;
            for (int k = 0; k < loops; k++) {
                for (int i = 0; i < cycles; i++) {
                    start = System.nanoTime();
                    sb = LocalStringBuilder.getBuffer();
                    sb.append(value).append(i).append(value);
                    LocalStringBuilder.toString(sb);
                    TimerFactory.getSimpleTimer("LocalStringBuilder", UNIT.ns).addNanoSeconds(start, System.nanoTime());
                }
                if (k > 0) {
                    globalStat.add(TimerFactory.getTimer("LocalStringBuilder").getTimeStatistics());
                }
                logB.warn("LocalStringBuilder : LocalStringBuilder    micro benchmark : " + TimerFactory.dumpTimers());
                TimerFactory.resetTimers();
            }
            logB.warn("LocalStringBuilder : global LocalStringBuilder    statistics : " + globalStat.toString(true));
            globalStat.reset();
            for (int k = 0; k < loops; k++) {
                for (int i = 0; i < cycles; i++) {
                    start = System.nanoTime();
                    sb = new StringBuilder();
                    sb.append(value).append(i).append(value);
                    sb.toString();
                    TimerFactory.getSimpleTimer("StandardStringBuilder", UNIT.ns).addNanoSeconds(start, System.nanoTime());
                }
                if (k > 0) {
                    globalStat.add(TimerFactory.getTimer("StandardStringBuilder").getTimeStatistics());
                }
                logB.warn("LocalStringBuilder : StandardStringBuilder micro benchmark : " + TimerFactory.dumpTimers());
                TimerFactory.resetTimers();
            }
            logB.warn("LocalStringBuilder : global StandardStringBuilder statistics : " + globalStat.toString(true));
        }
    }
}
