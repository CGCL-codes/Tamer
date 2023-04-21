package com.frameworkset.common.tag.pager.tags;

/**
 * ����������ֵ���valueֵ��ƥ��ʱ�������ǩ�����ݣ��������
 * @author biaoping.ying
 * @version 1.0
 * 2004-9-14
 */
public class LogicNotMatchTag extends MatchTag {

    /**
	 *  Description: ������
	 * @return boolean
	 * @see com.frameworkset.common.tag.pager.tags.MatchTag#match()
	 */
    protected boolean match() {
        if (actualValue == null && getValue() == null) return true;
        if (actualValue == null || getValue() == null) return true;
        if (String.valueOf(actualValue).compareTo(String.valueOf(getValue())) != 0) return true; else return false;
    }
}
