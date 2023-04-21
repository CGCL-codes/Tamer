package org.starobjects.tested.documentor.memory;

import org.nakedobjects.config.NakedObjectConfiguration;
import org.starobjects.tested.doclib.Documentor;
import org.starobjects.tested.documentor.DocumentorFactoryAbstract;

public class InMemoryDocumentorFactory extends DocumentorFactoryAbstract {

    @Override
    protected Documentor newDocumentor(final NakedObjectConfiguration configuration, final String className) {
        return new InMemoryDocumentor(configuration, className);
    }
}
