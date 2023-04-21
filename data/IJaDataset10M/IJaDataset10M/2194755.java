package com.meterware.website;

/**
 * @author <a href="mailto:russgold@meterware.com">Russell Gold</a>
 */
public class BasicSiteTemplate implements SiteTemplate {

    public void appendPageHeader(StringBuffer sb, Site site, SiteLocation currentPage) {
        site.appendPageTitle(sb, currentPage);
        site.appendMenu(sb, this, currentPage.getLocation());
        site.appendCommonElements(sb);
        sb.append("<div id='Content'>").append(FragmentTemplate.LINE_BREAK);
    }

    public void appendPageFooter(StringBuffer sb, Site site, SiteLocation currentPage) {
        sb.append("</div>").append(FragmentTemplate.LINE_BREAK);
        sb.append("</body></html>");
    }

    public void appendMenuItem(StringBuffer sb, String sourceLocation, String title, String targetLocation) {
        if (sourceLocation.equals(targetLocation)) {
            sb.append("  <b>").append(title).append("</b>");
        } else {
            sb.append("  <a href='").append(SiteUtils.relativeURL(sourceLocation, targetLocation)).append("'>").append(title).append("</a>");
        }
        appendEndOfItem(sb);
    }

    public void appendMenuSpace(StringBuffer sb) {
        appendEndOfItem(sb);
    }

    private void appendEndOfItem(StringBuffer sb) {
        sb.append("<br/>").append(FragmentTemplate.LINE_BREAK);
    }
}
