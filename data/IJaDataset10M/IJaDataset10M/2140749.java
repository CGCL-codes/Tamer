package com.frameworkset.common.tag.pager.model;

import java.util.Stack;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.PageContext;
import com.frameworkset.common.tag.pager.DataInfo;
import com.frameworkset.common.tag.pager.DataInfoImpl;
import com.frameworkset.common.tag.pager.DefaultDataInfoImpl;
import com.frameworkset.common.tag.pager.tags.LoadDataException;
import com.frameworkset.common.tag.pager.tags.PagerTag;

/**
 * �����ݵ�word��pdf��excel��cvs��xml�ļ���
 * ������ϸ��Ϣ���������ҳ��ݵ�������б���ݵ������
 * ��ҳ�������ְ���ֻ�����ǰҳ��ݺ�ȫ����ݵ�����ģʽ
 * @author biaoping.yin
 * created on 2005-5-18
 * version 1.0
 */
public class PageModel extends PagerTag implements ModelObject {

    /**�洢��ҳ��Ϣ*/
    private PageInfo pageInfo;

    /**��ݻ�ȡ�ӿ�*/
    private DataInfo dataInfo;

    /**�����ȫ����¼����ֱ�Ӵ�ҳ��dataSet�л�ȡ���*/
    private Stack dataSet;

    /**
     * ����dataSet�ĳ�ʼ������Ҫ�󵼳�ȫ����¼ʱ����initial����ʱ�޸�first��ֵ
     */
    private boolean first = true;

    /**
     * ��ʼ�����е�����ļ��ı�Ҫ����
     * @param pageInfo
     * @param dataInfo
     * @param dataSet
     * @param pageContext
     */
    public void setParameter(PageInfo pageInfo, DataInfo dataInfo, Stack dataSet, PageContext pageContext) {
        this.pageInfo = pageInfo;
        this.setPageContext(pageContext);
        this.dataSet = dataSet;
        this.dataInfo = dataInfo;
    }

    /**
     * ��ʼ�����ֱ��ȡÿһҳ����ݣ�ҳ����ʾģʽΪ��ҳʱ���ø÷���
     * @param offset int
     * @param maxPageItem int
     */
    public void initial(long offset, int maxPageItem) {
        HttpServletRequest request = getHttpServletRequest();
        HttpSession session = request.getSession(false);
        {
            if (dataInfo instanceof DefaultDataInfoImpl) {
                DataInfo defaultDataInfo = (DefaultDataInfoImpl) dataInfo;
                defaultDataInfo.initial(pageInfo.getStatement(), pageInfo.getDbName(), offset, maxPageItem, false, request);
            } else if (dataInfo instanceof DataInfoImpl) {
                dataInfo.initial(pageInfo.getSortKey(), pageInfo.isList(), offset, maxPageItem, false, request);
                pageInfo.initContext(offset, dataInfo.getItemCount());
            }
            if (first) {
                this.dataSet = new Stack();
                dataSet.push(this);
            } else {
                dataSet.push(this);
            }
        }
    }

    /**
     * @return Returns the dataInfo.
     */
    public DataInfo getDataInfo() {
        return dataInfo;
    }

    /**
     * @param dataInfo The dataInfo to set.
     */
    public void setDataInfo(DataInfo dataInfo) {
        this.dataInfo = dataInfo;
    }

    /**
     * @return Returns the dataSet.
     */
    public Stack getDataSet() {
        return dataSet;
    }

    /**
     * @param dataSet The dataSet to set.
     */
    public void setDataSet(Stack dataSet) {
        this.dataSet = dataSet;
    }

    /**
     * @return Returns the pageInfo.
     */
    public PageInfo getPageInfo() {
        return pageInfo;
    }

    /**
     * @param pageInfo The pageInfo to set.
     */
    public void setPageInfo(PageInfo pageInfo) {
        this.pageInfo = pageInfo;
    }
}
