package zeroj.web.view.config;

/**
 * @author LYL(lylsir at gmail dot com)
 * 
 * @since 1.0
 *
 * 2008-9-18
 */
public class DefaultConfigFactory implements ConfigFactory {

    public String getStorageKey() {
        return null;
    }

    /**
	 * 模板文件缺省放在 /WEB-INF/classes/templates目录下面
	 */
    public String getTemplatePath() {
        return "templates";
    }

    public String getTemplateSuffix() {
        return "html";
    }
}
