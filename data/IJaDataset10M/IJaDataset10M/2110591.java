package net.sf.japi.swing.app.script;

import java.util.List;
import org.jetbrains.annotations.NotNull;

/**
 * Temporary test class.
 * @deprecated Do not use, this is just a temporary test class.
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 * @since 0.1
 */
public class TryApplication extends Application {

    /** {@inheritDoc} */
    public Document load(@NotNull final String uri) {
        return new Document();
    }

    /** {@inheritDoc} */
    public void quit() {
    }

    /** {@inheritDoc} */
    public List getDocuments() {
        return null;
    }

    /** Document of this TryApplication. */
    public class Document extends Application.Document {

        /** {@inheritDoc} */
        public void save() {
        }

        /** {@inheritDoc} */
        public void saveAs(final String uri) {
        }

        /** {@inheritDoc} */
        public void close() {
        }

        /** {@inheritDoc} */
        public List getViews() {
            return null;
        }
    }
}
