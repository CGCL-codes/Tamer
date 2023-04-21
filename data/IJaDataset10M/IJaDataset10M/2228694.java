package org.activebpel.rt.bpel.def.adapter;

import org.activebpel.rt.bpel.def.visitors.IAeDefTraverser;
import org.activebpel.rt.xml.def.IAeAdapter;

/**
 * Child Extension Activity Traversers adapters implement this interface
 */
public interface IAeExtensionTraverserAdapter extends IAeAdapter {

    /**
    * Create and return a custom traverser
    */
    public IAeDefTraverser createTraverser();
}
