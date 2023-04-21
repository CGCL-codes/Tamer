package org.snipsnap.snip;

import org.snipsnap.interceptor.Aspects;
import snipsnap.api.snip.Snip;

/**
 * Single point for creating Snips
 *
 * @author Stephan J. Schmidt
 * @version $Id: SnipFactory.java 1846 2006-02-01 19:37:10Z leo $
 */
public class SnipFactory {

    public static Snip createSnip(String name, String content) {
        return new SnipImpl(name, content);
    }

    public static Snip wrap(Snip snip) {
        return (Snip) Aspects.wrap(snip);
    }
}
