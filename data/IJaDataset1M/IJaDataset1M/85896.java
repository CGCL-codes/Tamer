package org.codecover.instrumentation.cobol85;

import org.codecover.model.utils.*;
import org.codecover.model.extensions.*;

/**
 * The plugin class for the java instrumenter.
 * 
 * @author Steffen Kieß
 * 
 * @version 1.0 ($Id: InstrumenterPlugin.java 1 2007-12-12 17:37:26Z t-scheller $)
 */
public class InstrumenterPlugin extends AbstractPlugin {

    public InstrumenterPlugin() {
        super("Cobol85 Instrumenter Plugin", "", new Extension<?>[] { new AbstractExtension<org.codecover.instrumentation.InstrumenterDescriptor>(org.codecover.instrumentation.InstrumenterDescriptor.class, "org.codecover.instrumentation.cobol85.InstrumenterDescriptor") {

            public org.codecover.instrumentation.InstrumenterDescriptor getObject() {
                return new InstrumenterDescriptor();
            }
        } });
    }
}
