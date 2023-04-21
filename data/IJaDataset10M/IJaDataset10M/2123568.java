package org.geometerplus.fbreader.network.authentication.litres;

import java.util.Map;
import org.geometerplus.zlibrary.core.util.ZLNetworkUtil;
import org.geometerplus.fbreader.network.INetworkLink;
import org.geometerplus.fbreader.network.NetworkBookItem;
import org.geometerplus.fbreader.network.opds.OPDSCatalogItem;

public class LitResRecommendationsItem extends OPDSCatalogItem {

    public LitResRecommendationsItem(INetworkLink link, String title, String summary, String cover, Map<Integer, String> urlByType, Accessibility accessibility) {
        super(link, title, summary, cover, urlByType, accessibility, CatalogType.BY_SERIES);
    }

    @Override
    protected String getUrl() {
        final LitResAuthenticationManager mgr = (LitResAuthenticationManager) Link.authenticationManager();
        final StringBuilder builder = new StringBuilder();
        boolean flag = false;
        for (NetworkBookItem book : mgr.purchasedBooks()) {
            if (flag) {
                builder.append(',');
            } else {
                flag = true;
            }
            builder.append(book.Id);
        }
        return ZLNetworkUtil.appendParameter(URLByType.get(URL_CATALOG), "ids", builder.toString());
    }
}
