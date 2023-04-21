package com.palantir.ptoss.cinch.example.extension;

import java.util.List;
import com.google.common.collect.Lists;
import com.palantir.ptoss.cinch.core.BindingWiring;
import com.palantir.ptoss.cinch.core.Bindings;

/**
 * A sample class that returns extended bindings for including customizations to Cinch.
 *
 * @see Bindings
 * @see ExtendedExample
 * @see LoggedModel
 *
 */
public class ExtendedBindings {

    /**
     * Call this instead of {@link Bindings#standard()} to inject the custom bindings.
     */
    public static Bindings extendedBindings() {
        List<BindingWiring> wirings = Lists.newArrayList(Bindings.STANDARD_BINDINGS);
        wirings.add(new LoggedModel.Wiring());
        return new Bindings(wirings);
    }
}
