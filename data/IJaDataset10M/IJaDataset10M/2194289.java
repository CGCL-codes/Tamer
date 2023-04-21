package simpleorm.simpleweb.core;

import simpleorm.simpleweb.core.WPage;
import simpleorm.simpleweb.context.WPageContext;

/** Created per Servlet to represent either an item or a (sub)menu<p>
 *
 * Menus can be created independently from WPages, used for despatching.<p>
 *
 * Menus are currently three levels -- url mapped as /context/servlet/parentMenu/MenuItem.swb.<p>
 *
 * WARNING.  Menus are created per (global) servelet, must not have any per wpage instance information.
 * The static WPageStructure.getThreadPage provides that.<p>
 *
 * Menus are not shared between servlets, so they do have a context and servlet name.<p>
 *
 * It is the WServelet that sets the page.menu and page.pageItem based on the URL used to
 * find it.
 * If the page is defined on multiple menus this may or may not be correct.
 * But adding the same page to multiple menus is confusing to the user and so a generally bad idea.<p>
 *
 * WARNING These are generally Global (ie final values), see WGlobalState.
 * Do not modify them on a per page basis, but do refer to them using
 * WPageStructure.getPageItem etc. in case there are local coplies.<p>
 */
public abstract class WMenuAbstractGlobal {

    WMenuParentGlobal parent;

    String name;

    /** For items the real url, else the prefix for all the children. */
    String swbUrl;

    protected WMenuAbstractGlobal(WMenuParentGlobal parent) {
        this.parent = parent;
        this.name = getClass().getSimpleName();
    }

    /** Hack to be removed. */
    public String getAbsoluteUrl(WPage page) {
        WPageContext ctx = page.getPageContext();
        String url = ctx.encodeContextUrl(swbUrl);
        return url;
    }

    public String getName() {
        return name;
    }

    public <M extends WMenuAbstractGlobal> M setName(String name) {
        this.name = name;
        return (M) this;
    }

    public WMenuParentGlobal getParent() {
        return parent;
    }

    public String getUrl() {
        return swbUrl;
    }

    public void setUrl(String url) {
        this.swbUrl = url;
    }
}
