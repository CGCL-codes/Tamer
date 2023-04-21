package org.yehongyu.websale.db.bean;

import java.util.List;

/**
 * ����˵������ҳ��ѯ���Bean
 * @author yehongyu.org
 * @version 1.0 2007-11-30 ����02:20:51
 */
public class Page {

    /** ��ѯ��� */
    private List lstResult;

    /** ��ҳ��ϢBean */
    private PageBean pageBean;

    /**
	 * (��)
	 */
    public Page() {
    }

    /**
	 * ��ݲ�ѯ����ҳ��Ϣ����
	 * @param lstResult ��ѯ���
	 * @param pageBean ��ҳ��ϢBean
	 */
    public Page(List lstResult, PageBean pageBean) {
        this.lstResult = lstResult;
        this.pageBean = pageBean;
    }

    /**
	 * ȡ�ò�ѯ���
	 * @return ��ѯ���
	 */
    public List getLstResult() {
        return lstResult;
    }

    /**
	 * ���ò�ѯ���
	 * @param lstResult ��ѯ���
	 */
    public void setLstResult(List lstResult) {
        this.lstResult = lstResult;
    }

    /**
	 * ȡ�÷�ҳ��ϢBean
	 * @return ��ҳ��ϢBean
	 */
    public PageBean getPageBean() {
        return pageBean;
    }

    /**
	 * ���÷�ҳ��ϢBean
	 * @param pageBean ��ҳ��ϢBean
	 */
    public void setPageBean(PageBean pageBean) {
        this.pageBean = pageBean;
    }
}
