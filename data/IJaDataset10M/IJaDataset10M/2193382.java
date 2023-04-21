package whf.framework.web.template;

import java.util.Map;

/**
 * @author wanghaifeng
 * @create Mar 31, 2007 11:13:37 AM
 * 
 */
public interface ITemplate {

    /**
	 * 模板相关属性
	 * @modify wanghaifeng Mar 31, 2007 11:32:52 AM
	 * @return
	 */
    public Map<String, String> getProperties();

    /**
	 * 模板内容
	 * @modify wanghaifeng Mar 31, 2007 11:33:09 AM
	 * @return
	 */
    public String getTemplateContent();
}
