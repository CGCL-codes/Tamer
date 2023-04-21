package net.sourceforge.jwbf.actions.http.mw;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Vector;
import net.sourceforge.jwbf.actions.http.ContentProcessable;
import net.sourceforge.jwbf.actions.http.CookieException;
import net.sourceforge.jwbf.actions.http.ProcessException;
import net.sourceforge.jwbf.bots.MediaWikiBot;
import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.HttpMethod;

/**
 * @author Thomas Stock
 *
 */
public abstract class MWAction implements ContentProcessable {

    protected List<HttpMethod> msgs;

    /**
	 * 
	 * 
	 */
    public MWAction() {
        msgs = new Vector<HttpMethod>();
    }

    /**
	 * 
	 * @return a
	 */
    public final List<HttpMethod> getMessages() {
        return msgs;
    }

    /**
	 * @param s
	 *            the returning text
	 * @param hm
	 *            the method object
	 * @return the returning text
	 * @throws ProcessException on processing problems
	 * 
	 */
    public String processReturningText(final String s, final HttpMethod hm) throws ProcessException {
        return processAllReturningText(s);
    }

    /**
	 * @param cs
	 *            a
	 * @param hm
	 *            the method object
	 * @throws CookieException
	 *             never
	 * 
	 */
    public void validateReturningCookies(Cookie[] cs, HttpMethod hm) throws CookieException {
        validateAllReturningCookies(cs);
    }

    /**
	 * @param cs
	 *            a
	 * @throws CookieException
	 *             never
	 * 
	 */
    public void validateAllReturningCookies(final Cookie[] cs) throws CookieException {
    }

    /**
	 * @param s
	 *            the returning text
	 * @return the returning text
	 * @throws ProcessException never
	 * 
	 */
    public String processAllReturningText(final String s) throws ProcessException {
        return s;
    }

    /**
	 * changes to mediawiki default encoding.
	 * @param s a
	 * @return encoded s
	 * @deprecated
	 * TODO delete Method
	 */
    protected String encodeUtf8(final String s) {
        try {
            return new String(s.getBytes(), MediaWikiBot.CHARSET);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (NullPointerException npe) {
            return s;
        }
        return s;
    }
}
