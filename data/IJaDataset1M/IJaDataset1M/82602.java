package org.jcvi.fluvalidator.errors;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.jcvi.common.util.ArrayIterable;

/**
 *
 *
 * @author jsitz@jcvi.org
 */
public class AmbiguitiesError extends ValidationError {

    public static final int GROUP_THRESHOLD = 5;

    private final List<Ambiguity> ambiguities;

    /**
     * Creates a new <code>AmbiguitiesError</code>.
     */
    public AmbiguitiesError() {
        super();
        this.ambiguities = new ArrayList<Ambiguity>();
    }

    public AmbiguitiesError(Iterable<Ambiguity> errors) {
        this();
        for (final Ambiguity ambiguity : errors) {
            this.addAmbiguity(ambiguity);
        }
    }

    public AmbiguitiesError(Ambiguity... errors) {
        this(new ArrayIterable<Ambiguity>(errors));
    }

    public final void addAmbiguity(Ambiguity ambiguity) {
        this.ambiguities.add(ambiguity);
    }

    public final void addAmbiguities(Collection<Ambiguity> ambiguity) {
        this.ambiguities.addAll(ambiguity);
    }

    public final List<Ambiguity> getAmbiguties() {
        return Collections.unmodifiableList(this.ambiguities);
    }

    @Override
    public String getMessage() {
        return "The coding region contains " + this.ambiguities.size() + " ambiguity errors.";
    }

    @Override
    public String getLongMessage() {
        final StringBuilder message = new StringBuilder(this.getMessage());
        message.append(" ( ");
        for (final Ambiguity ambiguity : this.ambiguities) {
            message.append(ambiguity).append(' ');
        }
        message.append(")");
        return message.toString();
    }
}
