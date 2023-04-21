package org.jrest4guice.rest.reader;

import javax.servlet.http.HttpServletRequest;
import org.jrest4guice.client.ModelMap;

/**
 * 
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss (QQ:86895156)</a>
 *
 */
public interface RequestContentReader {

    /**
	 * 从客户端读取数据
	 */
    public void readData(HttpServletRequest request, ModelMap params, String charset);

    /**
	 * 返回当前Reader所对应的内容类型
	 * @return
	 */
    public String getContentType();
}
