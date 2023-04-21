package org.peaseplate.templateengine.template.parser;

import org.peaseplate.templateengine.TemplateException;
import org.peaseplate.utils.resource.ResourceKey;

/**
 * An exception raised by the template parser
 * 
 * @author Manfred HANTSCHEL
 */
public class TemplateParserException extends TemplateException {

    private static final long serialVersionUID = 1L;

    public TemplateParserException(final int line, final int column, final String message, final Throwable cause) {
        super(line, column, message, cause);
    }

    public TemplateParserException(final int line, final int column, final String message) {
        super(line, column, message);
    }

    public TemplateParserException(final ResourceKey key, final int line, final int column, final char[] code, final String message, final Throwable cause) {
        super(key, line, column, code, message, cause);
    }

    public TemplateParserException(final String resource, final int line, final int column, final char[] code, final String message, final Throwable cause) {
        super(resource, line, column, code, message, cause);
    }

    public TemplateParserException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public TemplateParserException(final String message) {
        super(message);
    }
}
