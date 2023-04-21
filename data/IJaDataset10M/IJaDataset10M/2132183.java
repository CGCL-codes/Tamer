package org.mandiwala.selenium.filter;

import java.util.List;
import org.mandiwala.PrefixedPropertyReader;
import org.mandiwala.selenium.SeleniumConfiguration;

public abstract class AbstractSkippingFilter implements Filter {

    private boolean skip;

    public final List<Invocation> filter(List<Invocation> invocations) throws FilterException {
        if (!skip) {
            return doFilter(invocations);
        }
        return invocations;
    }

    public abstract List<Invocation> doFilter(List<Invocation> invocations) throws FilterException;

    public final void init(SeleniumConfiguration seleniumConfiguration) {
        PrefixedPropertyReader prefixedPropertyReader = new PrefixedPropertyReader(getClass().getName(), seleniumConfiguration.getProps());
        String skipProp = prefixedPropertyReader.getPrefixedProperty("skip", false);
        skip = skipProp == null ? false : Boolean.parseBoolean(skipProp);
        if (!skip) {
            doInit(seleniumConfiguration);
        }
    }

    public abstract void doInit(SeleniumConfiguration seleniumConfiguration);
}
