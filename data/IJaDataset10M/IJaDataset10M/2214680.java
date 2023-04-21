package name.xwork.web.handler;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import name.xwork.util.JSONWriter;
import name.xwork.web.WebContext;
import name.xwork.web.WebResult;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author  zhaoxingyun
 */
public class AjaxRequestHandler implements RequestMappingHandler {

    private static final Log log = LogFactory.getLog(AjaxRequestHandler.class);

    private int orderNum;

    @Override
    public int orderNum() {
        return this.orderNum;
    }

    /**
	 * @param orderNum
	 * @uml.property  name="orderNum"
	 */
    public void setOrderNum(int orderNum) {
        this.orderNum = orderNum;
    }

    @Override
    public boolean requestHandl() throws Exception {
        return true;
    }

    @Override
    public void responseHandl() throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("当前请求返回AJAX：" + WebContext.isAjaxRequest());
        }
        if (WebContext.isAjaxRequest()) {
            if (WebContext.getDefaultResponseRequest() == null) {
                WebResult webResult = new WebResult();
                webResult.setSuccess(true);
                webResult.setMessage("OK!!!");
                WebContext.setDefaultResponseRequest(webResult);
            }
            Writer writer = WebContext.getResponse().getWriter();
            Map<String, Object> values = WebContext.getResponseValues();
            returnValuesToJSON(writer, values);
            writer.close();
            WebContext.getResponse().setStatus(HttpServletResponse.SC_OK);
        }
    }

    /**
	 * 将返回的值转换成JSON格式输出到输出流
	 * 
	 * @param writer
	 * @param values
	 * 
	 * @throws IOException
	 */
    private void returnValuesToJSON(Writer writer, Map<String, Object> values) throws IOException {
        JSONWriter jsonWriter = new JSONWriter(false);
        if (values == null) {
            writer.write("");
        } else {
            writer.write(new String(jsonWriter.write(values).getBytes(System.getProperty("file.encoding")), "ISO8859-1"));
        }
        writer.flush();
    }
}
