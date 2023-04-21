package org.yehongyu.websale.common.exception.impl;

/**
 * 
 * exception.xml���Ӧ��һ�����ô����쳣��
 * @author sbm
 * @version 4.0 Apr 16, 2007
 * @since 4.0
 */
public class ExceptionItem {

    /**����*/
    private String code;

    /**ҳ����Ҫ�õ�����ʾ��Ϣ*/
    private String pageMessage;

    /**������Ϣ*/
    private String debugMessage;

    /**��תҳ��ַ**/
    private String topageURL;

    /**�Ƿ�Ҫ��ת��ĳҳ��ֻ����true,topageURL������*/
    private boolean istopage;

    /**
	 * @return Returns the code.
	 */
    public String getCode() {
        return code;
    }

    /**
	 * @param code The code to set.
	 */
    public void setCode(String code) {
        this.code = code;
    }

    /**
	 * @return Returns the debugMessage.
	 */
    public String getDebugMessage() {
        return debugMessage;
    }

    /**
	 * @param debugMessage The debugMessage to set.
	 */
    public void setDebugMessage(String debugMessage) {
        this.debugMessage = debugMessage;
    }

    /**
	 * @return Returns the istopage.
	 */
    public boolean isIstopage() {
        return istopage;
    }

    /**
	 * @param istopage The istopage to set.
	 */
    public void setIstopage(boolean istopage) {
        this.istopage = istopage;
    }

    /**
	 * @return Returns the pageMessage.
	 */
    public String getPageMessage() {
        return pageMessage;
    }

    /**
	 * @param pageMessage The pageMessage to set.
	 */
    public void setPageMessage(String pageMessage) {
        this.pageMessage = pageMessage;
    }

    /**
	 * @return Returns the topageURL.
	 */
    public String getTopageURL() {
        return topageURL;
    }

    /**
	 * @param topageURL The topageURL to set.
	 */
    public void setTopageURL(String topageURL) {
        this.topageURL = topageURL;
    }
}
