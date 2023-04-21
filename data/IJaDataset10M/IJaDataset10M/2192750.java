package org.identifylife.key.editor.web.oxm;

import java.io.IOException;
import org.identifylife.key.editor.web.model.Context;

/**
 * @author dbarnier
 *
 */
@SuppressWarnings("unchecked")
public abstract class ContextBuilder<T extends ContextBuilder> {

    public static JsonContextBuilder jsonBuilder() throws IOException {
        return JsonContextBuilder.Cached.cached();
    }

    public abstract T build(Context response) throws IOException;

    public abstract byte[] bytes() throws IOException;

    public abstract T reset() throws IOException;

    public abstract int size() throws IOException;
}
