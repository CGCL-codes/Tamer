package com.liusoft.dlog4j.formbean;

import org.apache.struts.action.ActionForm;
import com.liusoft.dlog4j.DLOGSecurityManager;
import com.liusoft.dlog4j.util.StringUtils;

/**
 * FormBean�Ļ���
 * @author Winter Lau
 */
public abstract class FormBean extends ActionForm {

    private int id;

    private int sid;

    private String fromPage;

    private String __ClientId;

    public int getSid() {
        return sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    public final int getId() {
        return id;
    }

    public final void setId(int id) {
        this.id = id;
    }

    public final String getFromPage() {
        return fromPage;
    }

    public final void setFromPage(String fromPage) {
        this.fromPage = fromPage;
    }

    public String get__ClientId() {
        return __ClientId;
    }

    public void set__ClientId(String clientId) {
        __ClientId = clientId;
    }

    /**
	 * �Զ��ж��Ƿ�����ݽ��������ֹ���
	 * @param site
	 * @param content
	 * @return
	 */
    protected String autoFiltrate(String content) {
        if (StringUtils.isEmpty(content)) return null;
        return DLOGSecurityManager.IllegalGlossary.autoGlossaryFiltrate(content);
    }
}
