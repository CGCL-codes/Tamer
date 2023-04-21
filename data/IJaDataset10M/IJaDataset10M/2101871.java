package jaxlib.closure.tlong;

import java.nio.LongBuffer;
import java.nio.ReadOnlyBufferException;
import jaxlib.closure.Closures;

/**
 * Provides common implementations of {@link LongClosure}.
 *
 * @author  <a href="mailto:joerg.wassmer@web.de">J�rg Wa�mer</a>
 * @since   JaXLib 1.0
 * @version $Id: LongClosures.java,v 1.1.1.1 2004/08/02 20:55:57 joerg_wassmer Exp $
 */
public final class LongClosures extends Object {

    private LongClosures() {
    }

    public static final LongClosure BREAK = (LongClosure) Closures.BREAK;

    public static final LongClosure CONTINUE = (LongClosure) Closures.CONTINUE;

    public static LongClosure putTo(LongBuffer dest) {
        return new PutToBuffer(dest);
    }

    /**
   * @author  <a href="mailto:joerg.wassmer@web.de">J�rg Wa�mer</a>
   * @since   JaXLib 1.0
   */
    private static final class PutToBuffer extends Object implements LongClosure {

        private final LongBuffer buffer;

        PutToBuffer(LongBuffer buffer) {
            super();
            if (buffer.isReadOnly()) throw new ReadOnlyBufferException();
            this.buffer = buffer;
        }

        public boolean equals(Object o) {
            if (o == this) return true;
            if (!(o instanceof PutToBuffer)) return false;
            return this.buffer == ((PutToBuffer) o).buffer;
        }

        public int hashCode() {
            return System.identityHashCode(this.buffer) + 32;
        }

        public boolean proceed(long e) {
            this.buffer.put(e);
            return true;
        }
    }
}
