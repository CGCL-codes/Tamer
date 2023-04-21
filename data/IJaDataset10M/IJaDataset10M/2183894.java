package jaxlib.col.concurrent;

import java.io.Serializable;
import java.util.RandomAccess;
import jaxlib.col.XList;

/**
 *
 * @author  <a href="mailto:joerg.wassmer@web.de">J�rg Wa�mer</a>
 * @since   JaXLib 1.0
 * @version $Id: SynchronizedRandomAccessXList.java 1288 2004-11-04 21:24:04Z joerg_wassmer $.00
 */
final class SynchronizedRandomAccessXList<E> extends SynchronizedXList<E> implements RandomAccess, Serializable {

    /**
   * @since JaXLib 1.0
   */
    private static final long serialVersionUID = 1L;

    SynchronizedRandomAccessXList(XList<E> delegate) {
        super(delegate);
    }

    SynchronizedRandomAccessXList(XList<E> delegate, Object mutex) {
        super(delegate, mutex);
    }
}
