package org.tolven.plugin.registry.xml;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.java.plugin.registry.Documentation;
import org.java.plugin.registry.Identity;

/**
 * @version $Id$
 */
public class DocumentationImpl<T extends Identity> implements Documentation<T> {

    /**
     *  Logger object.
     */
    protected static Log log = LogFactory.getLog(DocumentationImpl.class);

    private final T identity;

    private final ModelDocumentation model;

    private List<Reference<T>> references;

    DocumentationImpl(final T anIdentity, final ModelDocumentation aModel) {
        identity = anIdentity;
        model = aModel;
        if ((model.getCaption() == null) || (model.getCaption().trim().length() == 0)) {
            model.setCaption("");
        }
        references = new ArrayList<Reference<T>>(model.getReferences().size());
        for (ModelDocumentationReference reference : model.getReferences()) references.add(new ReferenceImpl(reference));
        references = Collections.unmodifiableList(references);
        if (model.getText() == null) {
            model.setText("");
        }
        if (log.isDebugEnabled()) {
            log.debug("object instantiated: " + this);
        }
    }

    /**
     * @see org.java.plugin.registry.Documentation#getCaption()
     */
    public String getCaption() {
        return model.getCaption();
    }

    /**
     * @see org.java.plugin.registry.Documentation#getText()
     */
    public String getText() {
        return model.getText();
    }

    /**
     * @see org.java.plugin.registry.Documentation#getReferences()
     */
    public Collection<Reference<T>> getReferences() {
        return references;
    }

    /**
     * @see org.java.plugin.registry.Documentation#getDeclaringIdentity()
     */
    public T getDeclaringIdentity() {
        return identity;
    }

    private class ReferenceImpl implements Reference<T> {

        private final ModelDocumentationReference modelRef;

        ReferenceImpl(final ModelDocumentationReference aModel) {
            modelRef = aModel;
            if ((modelRef.getCaption() == null) || (modelRef.getCaption().trim().length() == 0)) {
                modelRef.setCaption("");
            }
            if ((modelRef.getPath() == null) || (modelRef.getPath().trim().length() == 0)) {
                modelRef.setPath("");
            }
            if (log.isDebugEnabled()) {
                log.debug("object instantiated: " + this);
            }
        }

        /**
         * @see org.java.plugin.registry.Documentation.Reference#getCaption()
         */
        public String getCaption() {
            return modelRef.getCaption();
        }

        /**
         * @see org.java.plugin.registry.Documentation.Reference#getRef()
         */
        public String getRef() {
            return modelRef.getPath();
        }

        /**
         * @see org.java.plugin.registry.Documentation.Reference#getDeclaringIdentity()
         */
        public T getDeclaringIdentity() {
            return DocumentationImpl.this.getDeclaringIdentity();
        }
    }
}
