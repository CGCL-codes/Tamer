package edu.vt.middleware.gator.web.support;

import java.beans.PropertyEditorSupport;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import edu.vt.middleware.gator.ParamConfig;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Converts between an collection of {@link ParamConfig} items and a text
 * representation of the following form:
 *
 * <pre>
   name=value
   name=value
   name=value
 * </pre>
 *
 * <p>where each name/value pair is separated by a line terminator, either CR or
 * CRLF.</p>
 *
 * @author  Middleware Services
 * @version  $Revision: 1421 $
 */
public class ParametersEditor<T extends ParamConfig> extends PropertyEditorSupport {

    /** Pattern used to match param lines in string representation. */
    private static final Pattern LINE_MATCH_PATTERN = Pattern.compile("(\\w+=.+)\n?");

    /** Pattern to split param line into name/value pairs. */
    private static final Pattern NAME_VALUE_SPLIT_PATTERN = Pattern.compile("=");

    /** Logger instance. */
    private final Log logger = LogFactory.getLog(getClass());

    /** Holds value of editor. */
    private Set<T> paramSet = new LinkedHashSet<T>();

    /** Class of parameter config handled by this instance. */
    private Class<T> handlesClass;

    /**
   * Creates a new instance to handle instances of the given class.
   *
   * @param  clazz  Class of {@link ParamConfig} this editor handles.
   */
    public ParametersEditor(final Class<T> clazz) {
        handlesClass = clazz;
    }

    /** {@inheritDoc} */
    @Override
    public String getAsText() {
        final StringBuilder sb = new StringBuilder();
        for (ParamConfig param : paramSet) {
            sb.append(String.format("%s=%s\n", param.getName(), param.getValue()));
        }
        return sb.toString();
    }

    /** {@inheritDoc} */
    @Override
    public void setAsText(final String text) {
        if (logger.isDebugEnabled()) {
            logger.debug(String.format("Attempting to set value using text [[%s]].", text));
        }
        paramSet.clear();
        final Matcher matcher = LINE_MATCH_PATTERN.matcher(text);
        while (matcher.find()) {
            final String line = matcher.group(1);
            final String[] pair = NAME_VALUE_SPLIT_PATTERN.split(line.trim(), 2);
            if (pair.length != 2) {
                throw new IllegalArgumentException("Param string must be name=value.");
            }
            final T param = newParam();
            param.setName(pair[0]);
            param.setValue(pair[1]);
            paramSet.add(param);
        }
    }

    /** {@inheritDoc} */
    @Override
    public Object getValue() {
        final Set<T> copy = new LinkedHashSet<T>();
        copy.addAll(paramSet);
        return copy;
    }

    /** {@inheritDoc} */
    @Override
    public void setValue(final Object value) {
        if (!(value instanceof Collection<?>)) {
            throw new IllegalArgumentException(value + " is not a collection.");
        }
        for (Object o : (Collection<?>) value) {
            try {
                paramSet.add(handlesClass.cast(o));
            } catch (ClassCastException e) {
                throw new IllegalArgumentException(o + " is not a " + handlesClass.getName());
            }
        }
    }

    /** @return  New {@link ParamConfig} instance. */
    private T newParam() {
        try {
            return handlesClass.newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Cannot instantiate " + handlesClass, e);
        }
    }
}
