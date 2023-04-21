package org.hypergraphdb.app.sail.helpers;

import info.aduna.iteration.CloseableIteration;
import info.aduna.iteration.IteratorIteration;
import org.openrdf.model.Namespace;
import org.openrdf.sail.SailException;
import java.util.Iterator;

/**
 * created Jan 29, 2010  - 7:25:42 PM
 *
 * @author IanHolsman
 *         Copyright (C) 2010 by Aol. All Rights Reserved.
 */
public class NamespaceIteration extends IteratorIteration<Namespace, SailException> implements CloseableIteration<Namespace, SailException> {

    public NamespaceIteration(Iterator<? extends Namespace> iter) {
        super(iter);
    }

    public void close() throws SailException {
    }
}
