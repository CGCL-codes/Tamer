package com.yehongyu.mansys.dao.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * DO��һЩ�����������ֶΡ�����DO��Ҫ�̳д�BaseDO
 * @author yingyang
 * @since 2011-11-11
 */
public class BaseDO implements Serializable {

    /**
	 * ���л�ID
	 */
    private static final long serialVersionUID = 1L;

    /**
	 * ����toString��������ӡDO����
	 */
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    /**
	 * =============================================================================
	 * ����������ʱ��Where��������ݲ�ѯ������IdList���á��������µĶ���ͨ��DO����
	 */
    private List<Long> idList;

    /**
	 * �������¶���ʱ��Ibatis��ȡҪ���µ�IDList
	 */
    public List<Long> getIdList() {
        return idList;
    }

    /**
	 * ��Oracle����Ż�ʱ��ת��table(str2numlist(#idListStr))ʹ��
	 * @return
	 */
    public String getIdListStr() {
        return (idList != null && !idList.isEmpty()) ? idList.toString().replace('[', ' ').replace(']', ' ') : null;
    }

    /**
	 * �������¶���ʱ������Ҫ���µ�IDList���������µĶ���ͨ��DO����
	 */
    public void setIdList(List<Long> idList) {
        this.idList = idList;
    }

    /**
	 * �������¶���ʱ������Ҫ���µ�IDList���������µĶ���ͨ��DO����
	 */
    public void addIdList(Long id) {
        if (idList == null) {
            idList = new ArrayList<Long>();
        }
        idList.add(id);
    }

    /**
	 * ====================================================================
	 * ���������ʱ�ж�DO�Ƿ��Ѿ�У����ˣ�ֻ��TripAgentValidate���á�����ʱ���
	 */
    private boolean isValidate;

    /**
	 * ���������ʱ�ж�DO�Ƿ��Ѿ�У����ˣ�ֻ��TripAgentValidate���á�����ʱ���
	 * @return
	 */
    public boolean isValidate() {
        return isValidate;
    }

    /**
	 * ���������ʱ�ж�DO�Ƿ��Ѿ�У����ˣ�ֻ��TripAgentValidate���á�����ʱ���
	 * @param isValidate
	 */
    public void setValidate(boolean isValidate) {
        this.isValidate = isValidate;
    }
}
