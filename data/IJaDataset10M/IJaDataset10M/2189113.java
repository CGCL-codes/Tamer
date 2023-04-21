package net.simpleframework.web.page.component.ui.portal.module;

import net.simpleframework.util.ConvertUtils;
import net.simpleframework.web.page.IForward;
import net.simpleframework.web.page.UrlForward;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.ui.portal.PageletBean;
import net.simpleframework.web.page.component.ui.portal.PageletTitle;
import net.simpleframework.web.page.component.ui.portal.PortalUtils;
import net.simpleframework.web.page.component.ui.portal.module.RssUtils.RssChannel;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class RssModuleHandle extends AbstractPortalModuleHandle {

    public RssModuleHandle(final PageletBean pagelet) {
        super(pagelet);
    }

    private static String[] defaultOptions = new String[] { "_rss_url=", "_rss_rows=6", "_rss_times=0", "_rss_tip=true" };

    @Override
    protected String[] getDefaultOptions() {
        return defaultOptions;
    }

    public RssChannel getRssChannel() {
        return RssUtils.getRssChannel(getPagelet().getOptionProperty("_rss_url"));
    }

    public int getRows() {
        return ConvertUtils.toInt(getPagelet().getOptionProperty("_rss_rows"));
    }

    public boolean isShowTip() {
        return ConvertUtils.toBoolean(getPagelet().getOptionProperty("_rss_tip"));
    }

    @Override
    public IForward getPageletContent(final ComponentParameter compParameter) {
        return new UrlForward(getResourceHomePath() + "/jsp/module/rss.jsp");
    }

    @Override
    public IForward getPageletOptionContent(final ComponentParameter compParameter) {
        return new UrlForward(getResourceHomePath() + "/jsp/module/rss_option.jsp");
    }

    @Override
    public String getOptionUITitle(final ComponentParameter compParameter) {
        final PageletBean pagelet = getPagelet();
        final RssChannel channel = RssUtils.getRssChannel(getPagelet().getOptionProperty("_rss_url"), false);
        final PageletTitle title = pagelet.getTitle();
        title.setValue(channel.getTitle());
        title.setLink(channel.getLink());
        return PortalUtils.getTitleString(compParameter, pagelet);
    }
}
