package jaxlib.closure;

import java.nio.FloatBuffer;
import java.nio.ReadOnlyBufferException;

/**
 * @author  <a href="mailto:joerg.wassmer@web.de">J�rg Wa�mer</a>
 * @since   JaXLib 1.0
 * @version $Id: DoFloat.java 1044 2004-04-06 16:37:29Z joerg_wassmer $
 *
 * @todo class comment
 */
public class DoFloat extends Object {

    protected DoFloat() throws InstantiationException {
        throw new InstantiationException();
    }

    public static final FloatClosure BREAK = Do.Final.BREAK;

    public static final FloatClosure CONTINUE = Do.Final.CONTINUE;

    public static FloatClosure putTo(FloatBuffer dest) {
        return new PutToBuffer(dest);
    }

    /**
   * @author  <a href="mailto:joerg.wassmer@web.de">J�rg Wa�mer</a>
   * @since   JaXLib 1.0
   */
    private static final class PutToBuffer extends Object implements FloatClosure {

        private final FloatBuffer buffer;

        PutToBuffer(FloatBuffer buffer) {
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

        public boolean proceed(float e) {
            this.buffer.put(e);
            return true;
        }
    }
}
