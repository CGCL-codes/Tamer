package org.echarts;

public final class DefaultInitialPseudostateConfiguration extends PseudostateConfiguration {

    public static final long serialVersionUID = 1;

    static final String rcsid = "$Name $Id";

    public static final String name = "DEFAULT_INITIAL";

    public boolean covers(StateConfiguration config) {
        boolean result = false;
        if (config instanceof DefaultInitialPseudostateConfiguration) {
            result = true;
        } else if (config instanceof BasicStateConfiguration) {
            result = false;
        } else if (config instanceof TerminalPseudostateConfiguration) {
            result = true;
        } else if (config instanceof AnyPseudostateConfiguration) {
            result = false;
        } else if (config instanceof VariableStateConfiguration) {
            result = false;
        } else if (config instanceof MultiStateConfiguration) {
            boolean allvar = true;
            final MultiStateConfiguration multiConfig = (MultiStateConfiguration) config;
            for (int i = 0; i < multiConfig.configurations.length && allvar == true; i++) allvar = (multiConfig.configurations[i] instanceof VariableStateConfiguration);
            if (allvar) result = false;
        }
        return result;
    }
}
