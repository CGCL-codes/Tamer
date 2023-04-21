package com.meterware.httpunit.site;

import com.meterware.website.*;

/**
 * @author <a href="mailto:russgold@httpunit.org">Russell Gold</a>
 */
public class SourceForgeSiteTemplate extends BasicSiteTemplate {

    public void appendPageHeader(StringBuffer sb, Site site, SiteLocation currentPage) {
        super.appendPageHeader(sb, site, currentPage);
        if (isLicensePage(currentPage)) sb.append("<p>").append(site.getCopyRight().getNotice()).append("</p>").append(FragmentTemplate.LINE_BREAK);
    }

    public void appendPageFooter(StringBuffer sb, Site site, SiteLocation currentPage) {
        if (!isLicensePage(currentPage)) {
            sb.append("<hr/><div style='position:relative'>").append(FragmentTemplate.LINE_BREAK);
            sb.append("  <div>").append(site.getCopyRight().getNotice()).append("</div>").append(FragmentTemplate.LINE_BREAK);
            ((WebSite) site).appendSourceForgeHostingNotice(sb);
        }
        super.appendPageFooter(sb, site, currentPage);
    }

    private boolean isLicensePage(SiteLocation currentPage) {
        return ((SourceForgeWebPage) currentPage).isLicense();
    }
}
