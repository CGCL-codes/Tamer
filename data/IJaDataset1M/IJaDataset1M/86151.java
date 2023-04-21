package com.wrupple.muba.desktop.server.bussiness.seo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.servlet.ModelAndView;
import com.wrupple.muba.desktop.server.integration.DesktopController;
import com.wrupple.muba.desktop.server.integration.ModelAndViewProcessingFilter;
import com.wrupple.muba.desktop.server.presentation.seo.SEOCrawlableView;

/**
 * Tells the desktop sequentialViewWrapper to be rendered as a non-ajax search-engine-friendly
 * fashion.
 * 
 * this bean is autowired, and looks for a "seoRequestFilter" that implements
 * SEORequestFilter to use as filter
 * 
 * @author japi
 * 
 */
public class SEOProcessingFilter implements ModelAndViewProcessingFilter {

    protected SEOCrawlableView view;

    @Autowired
    @Qualifier("seoRequestFilter")
    protected SEORequestFilter filter;

    private DesktopController desktopController;

    @Override
    public boolean processDesktopModelAndView(ModelAndView mv, HttpServletRequest req, HttpServletResponse resp) {
        if (filter == null) {
            throw new NullPointerException("No SEORequestFilter is set in the filter property, this should be set");
        }
        if (filter.shouldRedirectToStandardView(req)) {
            return true;
        } else {
            this.emptyModelAndView(mv);
            this.view.setDesktopController(desktopController);
            mv.setView(view);
            return false;
        }
    }

    private void emptyModelAndView(ModelAndView mv) {
        mv.setView(null);
        mv.setViewName(null);
    }

    /**
	 * @param sequentialViewWrapper
	 *            the sequentialViewWrapper responsable for rendering a crawlable version of cms
	 *            contents
	 */
    public void setView(SEOCrawlableView view) {
        this.view = view;
    }

    /**
	 * @param filter
	 *            the filter used to check if a request should be rendered in a
	 *            SEO friendly fashion
	 */
    @Required
    public void setFilter(SEORequestFilter filter) {
        this.filter = filter;
    }

    @Override
    public void setDesktopController(DesktopController controller) {
        this.desktopController = controller;
    }
}
