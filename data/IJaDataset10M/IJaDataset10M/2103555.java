package org.aitools.programd.processor.botconfiguration;

import org.w3c.dom.Element;
import org.aitools.programd.Core;
import org.aitools.programd.parser.BotsConfigurationFileParser;

/**
 * <p>
 * Sets bot predicate values at load-time.
 * </p>
 * 
 * @version 4.5
 * @since 4.1.2
 * @author Thomas Ringate, Pedro Colla
 * @author <a href="mailto:noel@aitools.org">Noel Bush</a>
 */
public class PropertyProcessor extends BotConfigurationElementProcessor {

    /** The label (as required by the registration scheme). */
    public static final String label = "property";

    /**
     * Creates a new PropertyProcessor using the given Core.
     * 
     * @param coreToUse the Core object to use
     */
    public PropertyProcessor(Core coreToUse) {
        super(coreToUse);
    }

    /**
     * @see BotConfigurationElementProcessor#process(Element,
     *      BotsConfigurationFileParser)
     */
    @Override
    public String process(Element element, BotsConfigurationFileParser parser) {
        parser.getCurrentBot().setPropertyValue(element.getAttribute(NAME), element.getAttribute(VALUE));
        return EMPTY_STRING;
    }
}
