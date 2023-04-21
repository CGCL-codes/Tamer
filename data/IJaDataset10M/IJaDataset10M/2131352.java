package jaxlib.text;

import java.lang.ref.SoftReference;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author  jw
 * @since   JaXLib 1.0
 * @version $Id: StringFormatterCache.java 3009 2011-11-08 05:07:45Z joerg_wassmer $
 */
final class StringFormatterCache extends Object {

    private static final ThreadLocal<SoftReference<StringFormatterCache>> threadLocal = new ThreadLocal();

    @Nonnull
    static StringFormatterCache getThreadLocal() {
        final SoftReference<StringFormatterCache> ref = StringFormatterCache.threadLocal.get();
        if (ref != null) {
            final StringFormatterCache cache = ref.get();
            if (cache != null) return cache;
        }
        final StringFormatterCache cache = new StringFormatterCache();
        StringFormatterCache.threadLocal.set(new SoftReference<StringFormatterCache>(cache));
        return cache;
    }

    private final StringFormatterImpl[] formatters;

    private int formatterCount;

    private final char[][] charArrays;

    private int charArrayCount;

    private final StringBuilder[] stringBuilders;

    private int stringBuilderCount;

    StringFormatterCache() {
        super();
        this.charArrays = new char[4][];
        this.formatters = new StringFormatterImpl[8];
        this.stringBuilders = new StringBuilder[8];
    }

    @Nonnull
    final char[] charArray(final int minCapacity) {
        final int i = this.charArrayCount - 1;
        if (i < 0) return new char[Math.max(64, minCapacity)];
        char[] a = this.charArrays[i];
        this.charArrays[i] = null;
        this.charArrayCount = i;
        if (a.length >= minCapacity) return a;
        a = null;
        return new char[minCapacity];
    }

    @Nonnull
    final StringFormatterImpl formatter() {
        final int i = this.formatterCount - 1;
        if (i < 0) return new StringFormatterImpl(this);
        final StringFormatterImpl f = this.formatters[i];
        this.formatters[i] = null;
        this.formatterCount = i;
        return f;
    }

    final void release(@Nullable final char[] a) {
        if ((a != null) && (a.length <= 4096)) {
            final int i = this.charArrayCount;
            if (i < this.charArrays.length) {
                this.charArrays[i] = a;
                this.charArrayCount = i + 1;
            }
        }
    }

    final void release(@Nullable final StringBuilder sb) {
        if ((sb != null) && (sb.capacity() <= 8192)) {
            final int i = this.stringBuilderCount;
            if (i < this.stringBuilders.length) {
                this.stringBuilders[i] = sb;
                this.stringBuilderCount = i + 1;
            }
        }
    }

    final void release(final StringFormatterImpl f) {
        final int i = this.formatterCount;
        if (i < this.formatters.length) {
            this.formatters[i] = f;
            this.formatterCount = i + 1;
        }
    }

    @Nonnull
    final StringBuilder stringBuilder(final int minCapacity) {
        final int i = this.stringBuilderCount - 1;
        if (i < 0) return new StringBuilder(minCapacity);
        final StringBuilder sb = this.stringBuilders[i];
        this.stringBuilderCount = i;
        this.stringBuilders[i] = null;
        sb.ensureCapacity(minCapacity);
        sb.setLength(0);
        return sb;
    }
}
