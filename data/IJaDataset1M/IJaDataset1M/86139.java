package org.jplate.foundation.node.attribute;

import org.jplate.foundation.gof.factory.ContextFactoryIfc;

/**
 
    Context factory that creates implementations of AttributeIfc.  The context
    here is represented by the attribute's name.

    <pre>
Modifications:
    $Date: 2007-10-16 15:04:08 -0400 (Tue, 16 Oct 2007) $
    $Revision: 384 $
    $Author: sfloess $
    $HeadURL: http://jplate.svn.sourceforge.net/svnroot/jplate/trunk/src/dev/java/org/jplate/foundation/node/attribute/AttributeFactoryIfc.java $
    </pre>


    @param <A> An implementation of AttributeIfc.

    @param <N> The name type of the attribute.

*/
public interface AttributeFactoryIfc<A extends AttributeIfc<N, ?>, N> extends ContextFactoryIfc<A, N> {
}
