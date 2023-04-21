package net.sf.psm4j.persistence;

import net.sf.psm4j.model.AuditBean;

/**
 * @author yue
 * @date Dec 2, 2005
 */
public interface AuditDAO {

    /**
	 * ���������־û�����ݿ���
	 * @param auditBean �����õ��Ķ������
	 */
    public void saveAudit(AuditBean auditBean);
}
