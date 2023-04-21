package org.t2framework.t2.util;

/**
 * <#if locale="en">
 * <p>
 * A factory class for {@link UrlTemplate}. It creates {@link UrlTemplate} from
 * just template path or template path with prefix.
 * </p>
 * <#else>
 * <p>
 * {@link UrlTemplate}のファクトリです.テンプレートのURL文字列から{@link UrlTemplate}を生成します.
 * </p>
 * </#if>
 * 
 * @author shot
 * 
 */
public interface UrlTemplateFactory {

    /**
	 * <#if locale="en">
	 * <p>
	 * Get {@link UrlTemplate} by path.
	 * </p>
	 * <#else>
	 * <p>
	 * テンプレートのURL文字列から{@link UrlTemplate}を取得します.
	 * </p>
	 * </#if>
	 * 
	 * @param templatePath
	 * @return url template
	 */
    UrlTemplate getUrlTemplate(final String templatePath);

    /**
	 * <#if locale="en">
	 * <p>
	 * Get {@link UrlTemplate} by prefix and template path.Prefix and template
	 * path concat with slash like aaa/bbb.
	 * 
	 * </p>
	 * <#else>
	 * <p>
	 * プレフィクスとテンプレートのURL文字列から{@link UrlTemplate}
	 * を取得します.プレフィクスとURL文字列はスラッシュで文字列連結されます.
	 * </p>
	 * </#if>
	 * 
	 * @param prefix
	 * @param templatePath
	 * @return url template
	 */
    UrlTemplate getUrlTemplate(final String prefix, final String templatePath);
}
