package org.eclipse.update.internal.core;

import org.eclipse.core.runtime.*;
import org.eclipse.update.core.*;

/**
 * ContentConsumer for a non plugin entry of a feature
 */
public class NonPluginEntryContentConsumer extends ContentConsumer {

    private boolean closed = false;

    private IContentConsumer contentConsumer;

    public NonPluginEntryContentConsumer(IContentConsumer contentConsumer) {
        this.contentConsumer = contentConsumer;
    }

    public void store(ContentReference contentReference, IProgressMonitor monitor) throws CoreException {
        if (!closed) {
            contentConsumer.store(contentReference, monitor);
        } else {
            UpdateCore.warn("Attempt to store in a closed NonPluginEntryContentConsumer", new Exception());
        }
    }

    public void close() throws CoreException {
        if (!closed) {
            closed = true;
            contentConsumer.close();
        } else {
            UpdateCore.warn("Attempt to close a closed NonPluginEntryContentConsumer", new Exception());
        }
    }
}
