package ces.platform.infoplat.taglib.ds.channel;

import java.io.IOException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import ces.platform.infoplat.core.Channel;
import ces.platform.infoplat.core.dao.ChannelDAO;
import ces.platform.infoplat.taglib.ds.DSTag;

public class ChannelsTag extends DSTag {

    private String parentPath;

    private int counter = 0;

    private Channel[] channels = null;

    /**
	 * ��ǩ�Ĺ�����
	 *
	 */
    public ChannelsTag() {
    }

    public int doStartTag() throws JspException {
        counter = 0;
        try {
            channels = (new ChannelDAO()).getChannels(parentPath);
        } catch (Exception ex) {
        }
        if (channels.length <= 0) {
            return SKIP_BODY;
        } else {
            pageContext.setAttribute(id, channels[counter++]);
        }
        return EVAL_BODY_TAG;
    }

    public int doAfterBody() throws JspException {
        if (hasMoreElement()) {
            pageContext.setAttribute(id, channels[counter++]);
            return EVAL_BODY_TAG;
        }
        return SKIP_BODY;
    }

    public int doEndTag() throws JspException {
        JspWriter jspwriter = super.pageContext.getOut();
        if (getBodyContent() != null) {
            String content = getBodyContent().getString();
            this.bodyContent = null;
            try {
                jspwriter.print(content);
            } catch (IOException ioexception) {
                throw new JspException("ChannelsTag: ���������ݵ��������");
            }
        }
        return EVAL_PAGE;
    }

    /**
	 * �Ƿ���Ƶ��
	 * @return
	 */
    private boolean hasMoreElement() {
        return (counter < channels.length) ? true : false;
    }

    /** ����Ƶ���б?�ڵ��path
	 * @param string
	 */
    public void setParentPath(String string) {
        parentPath = string;
    }
}
