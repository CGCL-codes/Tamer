package coolsite.base.dao;

import coolsite.base.entity.Admin;

public interface AdminDao extends BaseDao<Admin, Integer> {

    /**
	 * ����û����жϴ��û��Ƿ����(����ִ�Сд)
	 * 
	 */
    public boolean isExistByUsername(String email);

    /**
	 * ����û����ȡ����Ա����,������Ա������,�򷵻�null(����ִ�Сд)
	 */
    public Admin getAdminByUsername(String email);
}
