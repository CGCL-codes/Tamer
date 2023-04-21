package org.jplate.foundation.util;

import java.util.List;
import org.jplate.foundation.gof.factory.FactoryIfc;

/**

    Defines the API to create a {@link java.util.List}.  This interface is useful in
    scenarios when a class needs to be able to create implementations of List, but
    desires to create those implementations independant of another class performing
    the instantiation.  Additionally, the JDK does not provide List factories as
    defined here.

    For example implementations, please refer to
    <ul>
        <li>
            {@link org.jplate.foundation.util.impl.SynchronizedLinkedListFactory}
        </li>

        <li>
            {@link org.jplate.foundation.util.impl.UnsynchronizedLinkedListFactory}
        </li>
    </ul>

    <p/>
    <pre>
Modifications:
    $Date: 2007-04-04 12:13:24 -0400 (Wed, 04 Apr 2007) $
    $Revision: 238 $ 
    $Author: sfloess $
    $HeadURL: http://jplate.svn.sourceforge.net/svnroot/jplate/trunk/src/dev/java/org/jplate/foundation/util/ListFactoryIfc.java $
    </pre>

    @param <V> The value type that will be stored in the {@link java.util.List}'s.

    @see java.util.List

    @see org.jplate.foundation.gof.factory.FactoryIfc

    @see org.jplate.foundation.util.impl.SynchronizedLinkedListFactory

    @see org.jplate.foundation.util.impl.UnsynchronizedLinkedListFactory

*/
public interface ListFactoryIfc<V> extends FactoryIfc<List<V>> {
}
