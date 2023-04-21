package org.jsmtpd.core.common.filter;

import org.jsmtpd.core.common.IGenericPlugin;
import org.jsmtpd.core.mail.Email;

/**
 * Return true if the filter is passed successfully<br>
 * Return false if the filter is not passed<br>
 * <br>
 * Chain will go on until its end, or if FilterTreeFailureException or
 * FilterTreeSuccesException are thrown. Theses exceptions causes the 
 * immediate succes or failure of the chain
 * <br>
 * A filter that causes a failure of chain should excplitly create a new mail
 * to inform the sender of the failure.
 * @author Jean-Francois POUX
 * 
 */
public interface IFilter extends IGenericPlugin {

    /**
     * Processes the mail throught the filter
     * @param input the mail to process
     * @return true if filter pass, false otherwise
     */
    public boolean doFilter(Email input) throws FilterTreeFailureException, FilterTreeSuccesException;
}
