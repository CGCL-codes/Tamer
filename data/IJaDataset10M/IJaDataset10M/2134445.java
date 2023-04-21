package net.sf.wicketdemo.app.links.page;

import org.apache.wicket.PageParameters;
import net.sf.wicketdemo.common.page.ViewBookPage;

/**
 * This page exists solely as a "redirect" to the real <code>ViewBookPage</code>.
 * We need a unique class to mount so <code>urlFor()</code> can't get confused
 * (the first found <code>IRequestTargetUrlCodingStrategy</code> that matches will be used).
 * 
 * @author Bram Bogaert
 */
public class HybridUrlCodingStrategyPage extends ViewBookPage {

    public HybridUrlCodingStrategyPage(final PageParameters parameters) {
        super(parameters);
    }
}
