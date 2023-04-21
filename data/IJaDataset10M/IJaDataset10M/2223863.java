package ces.platform.infoplat.taglib.ds.document;

import java.io.IOException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import ces.coral.log.Logger;
import ces.platform.infoplat.core.DocumentPublish;
import ces.platform.infoplat.core.Documents;
import ces.platform.infoplat.core.base.ConfigInfo;
import ces.platform.infoplat.taglib.ds.DSTag;

/**
 * <p>Title: ������Ϣƽ̨</p>
 * <p>Description:�ĵ��б���tag,����ȡ���ĵ��б�,�ŵ�ҳ����,��ҳ����ʾ </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: �Ϻ�������Ϣ��չ���޹�˾</p>
 * @author ���� ֣��ǿ
 * @version 2.5
 */
public class DocumentsTag extends DSTag {

    public static final String IP_ROW_AMOUNT = "ip_rowAmount";

    public static final String IP_PAGE_AMOUNT = "ip_pageAmount";

    public static final String IP_ROW_NUM = "ip_rowNum";

    public static final String IP_SHOW_AMOUNT = "ip_showAmount";

    private String publishedPath;

    private int resultNum = -1;

    private String resURL;

    private DocumentPublish[] documents = null;

    private int counter = 0;

    private boolean isIterate = true;

    private boolean descendant = false;

    private String orderBy = null;

    private boolean desc = false;

    Logger log = new Logger(getClass());

    public DocumentsTag() {
    }

    public int doStartTag() throws JspException {
        FlipPageTag papa = (FlipPageTag) TagSupport.findAncestorWithClass(this, FlipPageTag.class);
        ConfigInfo cfg = ConfigInfo.getInstance();
        try {
            if (papa != null) {
                if (cfg.getAppServer() != null && cfg.getAppServer().equalsIgnoreCase("websphere4")) {
                    documents = Documents.getPublishedDocList(this.publishedPath, null, null, this.resultNum, descendant, orderBy, desc, true);
                } else if (papa.getCrntPageIndex() == 1) {
                    documents = Documents.getPublishedDocList(this.publishedPath, null, null, this.resultNum, descendant, orderBy, desc, true);
                }
            } else {
                documents = Documents.getPublishedDocList(this.publishedPath, null, null, this.resultNum, descendant, orderBy, desc, true);
            }
        } catch (Exception ex) {
            log.error("<�ĵ��б����Դ>��ȡ��ݳ���", ex);
            throw new JspException(ex);
        }
        int length = 0;
        if (documents != null) {
            length = documents.length;
        }
        if (resultNum < 0) {
            resultNum = length;
        }
        if (papa != null) {
            pageContext.setAttribute(IP_ROW_AMOUNT, String.valueOf(length));
            int rcdPerPage = papa.getRcdPerPage();
            int pageAmount = 1;
            if (rcdPerPage != 0) {
                pageAmount = length / rcdPerPage;
                if (pageAmount * rcdPerPage < length || pageAmount == 0) pageAmount++;
            }
            pageContext.setAttribute(IP_PAGE_AMOUNT, String.valueOf(pageAmount));
            if (papa.getCurrentPage() == null) {
                counter = 0;
            } else {
                counter = (papa.getCrntPageIndex() - 1) * rcdPerPage;
            }
            resultNum = papa.getCrntPageIndex() * rcdPerPage;
        } else {
            counter = 0;
            if (documents == null || documents.length <= 0) {
                return SKIP_BODY;
            }
        }
        pageContext.setAttribute(IP_ROW_NUM, String.valueOf(counter));
        if (isIterate) {
            if (!hasMoreElement()) {
                return SKIP_BODY;
            } else {
                pageContext.setAttribute(id, documents[counter++]);
            }
        } else {
            pageContext.setAttribute(id, documents);
            pageContext.setAttribute(IP_SHOW_AMOUNT, String.valueOf(resultNum));
        }
        return EVAL_BODY_TAG;
    }

    public int doAfterBody() throws JspException {
        if (isIterate) {
            if (hasMoreElement()) {
                pageContext.setAttribute(id, documents[counter++]);
                pageContext.setAttribute(IP_ROW_NUM, String.valueOf(counter));
                return EVAL_BODY_TAG;
            }
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
                throw new JspException("DocumentsTag: ���������ݵ��������");
            }
        }
        resultNum = -1;
        return EVAL_PAGE;
    }

    /**
	 * �Ƿ��и����ĵ�
	 * @return
	 */
    private boolean hasMoreElement() {
        return (counter < documents.length && counter < resultNum) ? true : false;
    }

    /**
	 * ����վ��Ƶ����·��
	 * @param siteChannelpath
	 */
    public void setPublishedPath(String siteChannelpath) {
        this.publishedPath = siteChannelpath;
    }

    /**
	 * ������ʾ�ĵ���������,��¼����Ϊ0����Ϊ��,��ȫ����ʾ
	 * @param resultNum
	 */
    public void setResultNum(int resultNum) {
        this.resultNum = resultNum;
    }

    /** �����ĵ���Դ�ļ������·��
	 * @param string
	 */
    public void setResURL(String string) {
        resURL = string;
    }

    /**
	 * �����Ƿ�ѭ��
	 * @param b
	 */
    public void setIsIterate(boolean b) {
        isIterate = b;
    }

    /**�����Ƿ���ʾ��Ƶ���е��ĵ�
	 * @param b
	 */
    public void setDescendant(boolean b) {
        descendant = b;
    }

    public static void main(String[] args) {
        try {
            DocumentPublish[] documents = Documents.getPublishedList("000000420204902", 0, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
	 * @param b
	 */
    public void setDesc(boolean b) {
        desc = b;
    }

    /**
	 * @param string
	 */
    public void setOrderBy(String string) {
        orderBy = string;
    }
}
