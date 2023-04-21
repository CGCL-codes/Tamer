package net.paoding.rose.web.taglibs;

import java.io.IOException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import net.paoding.rose.web.Invocation;
import net.paoding.rose.web.InvocationUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 在jsp中支持 flash标签
 * 
 * @author 王志亮 [qieqie.wang@gmail.com]
 * 
 */
public class FlashTag extends TagSupport {

    private static final long serialVersionUID = -1951600183488515087L;

    private static Log logger = LogFactory.getLog(FlashTag.class);

    private String key;

    private String prefix = "";

    private String suffix = "";

    public void setKey(String key) {
        this.key = key;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    @Override
    public int doStartTag() throws JspException {
        Invocation invocation = InvocationUtils.getCurrentThreadInvocation();
        if (invocation != null) {
            String msg = invocation.getFlash().get(key);
            if (logger.isDebugEnabled()) {
                logger.debug("getFlashMessage: " + key + "=" + msg);
            }
            if (msg != null) {
                try {
                    if (StringUtils.isNotEmpty(prefix)) {
                        pageContext.getOut().print(prefix);
                    }
                    pageContext.getOut().print(msg);
                    if (StringUtils.isNotEmpty(suffix)) {
                        pageContext.getOut().print(suffix);
                    }
                } catch (IOException e) {
                    throw new JspException("", e);
                }
            }
        }
        return super.doStartTag();
    }
}
