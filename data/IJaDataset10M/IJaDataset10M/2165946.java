package org.gamegineer.engine.internal.core.contexts.command;

import static org.gamegineer.common.core.runtime.Assert.assertArgumentLegal;
import static org.gamegineer.common.core.runtime.Assert.assertArgumentNotNull;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import net.jcip.annotations.Immutable;
import org.gamegineer.engine.core.contexts.command.ICommandContext;

/**
 * Implementation of
 * {@link org.gamegineer.engine.core.contexts.command.ICommandContext}.
 * 
 * <p>
 * This class is immutable.
 * </p>
 */
@Immutable
public final class CommandContext implements ICommandContext {

    /** The collection of context attributes. */
    private final Map<String, Object> m_attributes;

    /**
     * Initializes a new instance of the {@code CommandContext} class with an
     * empty attribute collection.
     */
    public CommandContext() {
        this(Collections.<String, Object>emptyMap());
    }

    /**
     * Initializes a new instance of the {@code CommandContext} class with the
     * specified attribute collection.
     * 
     * @param attributes
     *        The collection of context attributes; must not be {@code null}.
     * 
     * @throws java.lang.IllegalArgumentException
     *         If {@code attributes} contains one or more {@code null} values.
     * @throws java.lang.NullPointerException
     *         If {@code attributes} is {@code null}.
     */
    public CommandContext(final Map<String, Object> attributes) {
        assertArgumentNotNull(attributes, "attributes");
        assertArgumentLegal(!attributes.containsValue(null), "attributes", Messages.CommandContext_attributes_containsNullValue);
        m_attributes = new HashMap<String, Object>(attributes);
    }

    public boolean containsAttribute(final String name) {
        assertArgumentNotNull(name, "name");
        return m_attributes.containsKey(name);
    }

    public Object getAttribute(final String name) {
        assertArgumentNotNull(name, "name");
        return m_attributes.get(name);
    }
}
