package com.zzsoft.app.base.sysuser.user;

import framework.zze2p.mod.pojodb.PojoDB;

/**
 * �û���Ϣ�����
 * @author Liuxz
 *
 */
public interface UserBOI {

    /**
	 * ����û�
	 * @param pojo
	 * @return
	 */
    public String addUser(PojoDB pojo);

    /**
	 * �޸��û�����
	 * @param pojo
	 * @return
	 */
    public String updateUserPwdById(PojoDB pojo);

    /**
	 * �޸��û���Ϣ
	 * @param pojo
	 * @return
	 */
    public String updateUserById(PojoDB pojo);

    /**
	 * ͨ�����һ���û�
	 * @param pUser
	 * @return
	 */
    public PojoDB findUserById(PojoDB pUser);

    /**
	 * ͨ����Ʋ���һ���û�
	 * @param pUser
	 * @return
	 */
    public PojoDB findUserByName(PojoDB pUser);

    /**
	 * ɾ���û�
	 * @param pUser
	 * @return
	 */
    public String deleteRoleById(PojoDB pUser);
}
