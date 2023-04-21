package com.unboundid.util;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * This class provides a data structure which holds information about a SASL
 * mechanism supported for use with the {@link SASLUtils} class.
 */
@NotMutable()
@ThreadSafety(level = ThreadSafetyLevel.COMPLETELY_THREADSAFE)
public final class SASLMechanismInfo {

    private final boolean acceptsPassword;

    private final boolean requiresPassword;

    private final List<SASLOption> options;

    private final String description;

    private final String name;

    /**
   * Creates a new SASL mechanism info object with the provided information.
   *
   * @param  name              The name for the SASL mechanism.
   * @param  description       A description for the SASL mechanism.
   * @param  acceptsPassword   Indicates whether the SASL mechanism allows a
   *                           password to be provided.
   * @param  requiresPassword  Indicates whether the SASL mechanism requires a
   *                           password to be provided.
   * @param  options           The set of options that are associated with the
   *                           SASL mechanism.
   */
    SASLMechanismInfo(final String name, final String description, final boolean acceptsPassword, final boolean requiresPassword, final SASLOption... options) {
        this.name = name;
        this.description = description;
        this.acceptsPassword = acceptsPassword;
        this.requiresPassword = requiresPassword;
        if ((options == null) || (options.length == 0)) {
            this.options = Collections.emptyList();
        } else {
            this.options = Collections.unmodifiableList(Arrays.asList(options));
        }
    }

    /**
   * Retrieves the name of the SASL mechanism.
   *
   * @return  The name of the SASL mechanism.
   */
    public String getName() {
        return name;
    }

    /**
   * Retrieves a description for the SASL mechanism.
   *
   * @return  A description for the SASL mechanism.
   */
    public String getDescription() {
        return description;
    }

    /**
   * Indicates whether the SASL mechanism accepts a password for authentication
   * processing.
   *
   * @return  {@code true} if the SASL mechanism accepts a password for
   *          authentication processing, or {@code false} if not.
   */
    public boolean acceptsPassword() {
        return acceptsPassword;
    }

    /**
   * Indicates whether the SASL mechanism requires a password for authentication
   * processing.
   *
   * @return  {@code true} if the SASL mechanism requires a password for
   *          authentication processing, or {@code false} if not.
   */
    public boolean requiresPassword() {
        return requiresPassword;
    }

    /**
   * Retrieves a list of the options that may be used with the SASL mechanism.
   *
   * @return  A list of the options that may be used with the SASL mechanism, or
   *          an empty list if there are no supported SASL options for the
   *          associated mechanism.
   */
    public List<SASLOption> getOptions() {
        return options;
    }

    /**
   * Retrieves a string representation of this SASL mechanism info object.
   *
   * @return  A string representation of this SASL mechanism info object.
   */
    @Override()
    public String toString() {
        final StringBuilder buffer = new StringBuilder();
        toString(buffer);
        return buffer.toString();
    }

    /**
   * Appends a string representation of this SASL mechanism info object to the
   * provided buffer.
   *
   * @param  buffer  The buffer to which the information should be appended.
   */
    public void toString(final StringBuilder buffer) {
        buffer.append("SASLMechanismInfo(name='");
        buffer.append(name);
        buffer.append("', description='");
        buffer.append(description);
        buffer.append("', acceptsPassword=");
        buffer.append(acceptsPassword);
        buffer.append(", requiresPassword=");
        buffer.append(requiresPassword);
        buffer.append(", options={");
        final Iterator<SASLOption> iterator = options.iterator();
        while (iterator.hasNext()) {
            iterator.next().toString(buffer);
            if (iterator.hasNext()) {
                buffer.append(", ");
            }
        }
        buffer.append("})");
    }
}
