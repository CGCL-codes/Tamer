package org.waveprotocol.wave.model.document.util;

import org.waveprotocol.wave.model.document.Doc;
import org.waveprotocol.wave.model.document.ObservableDocument;

/**
 * A DocumentEventRouter specialized for the non-generic Document interface.
 *
 */
public interface DocEventRouter extends DocumentEventRouter<Doc.N, Doc.E, Doc.T> {

    @Override
    public ObservableDocument getDocument();
}
