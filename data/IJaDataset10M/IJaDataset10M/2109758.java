package org.djpp.preprocessor.directives;

import java.util.logging.Logger;
import org.djpp.preprocessor.ParserStateMachine;

/**
 * @author Daniel Holmen
 */
public class DefineDirective extends Directive {

    private Logger logger = Logger.getLogger(DefineDirective.class.getName());

    public ParserStateMachine doDirective(ParserStateMachine state, String parameter) {
        String[] split = parameter.split(" |\t");
        if (split.length > 1) {
            String property = split[1];
            String value = "1";
            if (split.length > 2) value = parameter.split(" |\t")[2];
            logger.finest("Property: " + property + " = " + value);
            state.getProperties().setProperty(property, value);
        }
        return state;
    }
}
