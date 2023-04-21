package org.jsmtpd.plugins.filters.builtin;

import org.jsmtpd.core.common.PluginInitException;
import org.jsmtpd.core.common.filter.FilterTreeFailureException;
import org.jsmtpd.core.common.filter.FilterTreeSuccesException;
import org.jsmtpd.core.common.filter.IFilter;
import org.jsmtpd.core.mail.Email;

/**
 * Causes the immediate succes of a path in the filter tree
 * @author Jean-Francois POUX
 */
public class ChainSucces implements IFilter {

    public boolean doFilter(Email input) throws FilterTreeFailureException, FilterTreeSuccesException {
        throw new FilterTreeSuccesException();
    }

    public String getPluginName() {
        return "Chain succes: Filter tree chain succes";
    }

    public void initPlugin() throws PluginInitException {
    }

    public void shutdownPlugin() {
    }
}
