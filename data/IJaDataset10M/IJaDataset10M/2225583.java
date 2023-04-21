package examples.page;

import org.t2framework.t2.annotation.core.Default;
import org.t2framework.t2.annotation.core.Page;
import org.t2framework.t2.contexts.WebContext;
import org.t2framework.t2.navigation.Redirect;
import org.t2framework.t2.spi.Navigation;

/**
 * <#if locale="en">
 * <p>
 * 
 * </p>
 * <#else>
 * <p>
 * 外部サイトへリダイレクトするサンプルです.
 * </p>
 * </#if>
 * 
 * @since 0.4
 * @author shot
 */
@Page("/outer")
public class OuterPage {

    /**
	 * <#if locale="en">
	 * <p>
	 * 
	 * </p>
	 * <#else>
	 * <p>
	 * 外部サイトへリダイレクトする場合は、{@code Redirect.toOuterUrl}メソッドを使います.
	 * </p>
	 * </#if>
	 */
    @Default
    public Navigation index(WebContext context) {
        return Redirect.toOuterUrl("http://www.yahoo.co.jp");
    }
}
