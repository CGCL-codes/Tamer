package org.adempiere.webui.session;

import java.util.Properties;
import org.compiere.util.Env;
import org.compiere.util.Language;

/**
 *
 * @author  <a href="mailto:agramdass@gmail.com">Ashley G Ramdass</a>
 * @date    Feb 25, 2007
 * @version $Revision: 0.10 $
 */
@SuppressWarnings("serial")
public final class WebContext extends Properties {

    public WebContext() {
        super();
        this.put(Env.LANGUAGE, Language.getBaseAD_Language());
    }

    private static InheritableThreadLocal context = new InheritableThreadLocal() {

        protected WebContext initialValue() {
            return new WebContext();
        }
    };

    public static WebContext getCurrentInstance() {
        return (WebContext) context.get();
    }

    @SuppressWarnings("unchecked")
    public static void setCurrentInstance(WebContext webCtx) {
        context.set(webCtx);
    }
}
