package net.sourceforge.jwbf.actions;

import net.sourceforge.jwbf.actions.mw.HttpAction;
import net.sourceforge.jwbf.actions.mw.util.CookieException;
import net.sourceforge.jwbf.actions.mw.util.ProcessException;
import org.apache.commons.httpclient.Cookie;

/**
 * 
 * @author Thomas Stock
 *
 */
public interface ContentProcessable {

    /**
	 * 
	 * @param cs a
	 * @param hm a
	 * @throws CookieException on problems with cookies
	 */
    void validateReturningCookies(final Cookie[] cs, HttpAction hm) throws CookieException;

    /**
	 * 
	 * @param s the returning text
	 * @param hm a
	 * @return the retruning text or a modification of it
	 * @throws ProcessException on internal problems of implementing class
	 */
    String processReturningText(final String s, HttpAction hm) throws ProcessException;

    /**
	 * @return the of messages in this action
	 */
    HttpAction getNextMessage();

    boolean hasMoreMessages();
}
