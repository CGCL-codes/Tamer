package com.google.code.burija.normal.service.impl;

import org.apache.log4j.Logger;
import org.seasar.framework.container.S2Container;
import com.google.code.burija.common.exception.BusinessException;
import com.google.code.burija.normal.dao.SysUserDao;
import com.google.code.burija.normal.dto.SysUserDto;
import com.google.code.burija.normal.service.NormalUserService;

/**
 * NormalUserServiceの実装。
 * @author imaitakafumi
 *
 */
public class NormalUserServiceImpl implements NormalUserService {

    static Logger LOG = Logger.getLogger(NormalUserServiceImpl.class);

    /** S2Container */
    private S2Container container;

    public boolean existsId(String loginId) {
        SysUserDao dao = (SysUserDao) this.container.getComponent(SysUserDao.class);
        if (dao.selectBySysLoginId(loginId) != null) {
            return true;
        }
        return false;
    }

    public long login(String loginId, String loginPw) throws BusinessException {
        SysUserDao dao = (SysUserDao) this.container.getComponent(SysUserDao.class);
        if (dao.selectBySysLoginId(loginId) == null) {
            throw new BusinessException("そんなID登録されてないっす＞＜");
        }
        SysUserDto dto = dao.selectByLoginIdLoginPw(loginId, loginPw);
        if (dto == null) {
            throw new BusinessException("多分パスワード間違ってるっす＞＜");
        }
        return dto.getSysUserId();
    }

    public void regist(String loginId, String loginPw, String sysUserName, int gender) {
        SysUserDto dto = new SysUserDto(loginId, loginPw, sysUserName, gender);
        SysUserDao dao = (SysUserDao) this.container.getComponent(SysUserDao.class);
        dao.insert(dto);
    }

    public void edit(long sysUserId, String loginId, String loginPw, String sysUserName, int gender) {
        SysUserDto dto = new SysUserDto(sysUserId, loginId, loginPw, sysUserName, gender);
        SysUserDao dao = (SysUserDao) this.container.getComponent(SysUserDao.class);
        dao.update(dto);
    }

    public S2Container getContainer() {
        return container;
    }

    public void setContainer(S2Container container) {
        this.container = container;
    }
}
