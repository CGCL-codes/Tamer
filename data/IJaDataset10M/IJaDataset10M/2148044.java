package org.lc.controller.support;

import java.util.Date;

/**
 * ��������¼����,�޸�ʱ�估����
 * ��¼������Ϣ���������Ƚ�ͨ�õ�����.
 *
 */
public interface AuditableEntity {

    public Date getCreateTime();

    public void setCreateTime(Date createTime);

    public Date getModifyTime();

    public void setModifyTime(Date modifyTime);
}
