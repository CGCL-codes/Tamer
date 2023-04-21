package org.impalaframework.module.definition;

import org.impalaframework.module.ModuleDefinition;
import org.impalaframework.util.ObjectUtils;

/**
 * Implementation of {@link ModuleDefinitionCallback} used to assist in toString
 * method implementation
 * 
 * @author Phil Zoio
 */
public class ToStringCallback implements ChildModuleDefinitionCallback {

    private static final String LINE_SEPARATOR = System.getProperty("line.separator");

    private StringBuffer buffer = new StringBuffer();

    private int spaces;

    private boolean hasMatched;

    public boolean matches(ModuleDefinition moduleDefinition) {
        final ToStringAppendable appendable = ObjectUtils.cast(moduleDefinition, ToStringAppendable.class);
        if (hasMatched) {
            buffer.append(LINE_SEPARATOR);
        }
        hasMatched = true;
        for (int i = 0; i < spaces; i++) {
            buffer.append(" ");
        }
        appendable.toString(buffer);
        return false;
    }

    public void beforeChild(ModuleDefinition moduleDefinition) {
        spaces += 2;
    }

    public void afterChild(ModuleDefinition moduleDefinition) {
        spaces -= 2;
    }

    public String toString() {
        return buffer.toString();
    }
}
