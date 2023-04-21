package org.databene.commons.format;

import java.text.Format;

/**
 * {@link Format} that exhibits the information which Java class it can format and parse.<br/>
 * <br/>
 * Created: 11.05.2005 22:04:05
 * @since 0.1
 * @author Volker Bergmann
 */
@SuppressWarnings("serial")
public abstract class TypedFormat<S> extends Format {

    public abstract Class<S> getSourceType();
}
