package com.ems.system.acl.bs;

import java.util.List;
import com.ems.common.exception.EMSException;
import com.ems.common.model.vo.LoginInfoVO;
import com.ems.system.client.vo.MenuItemVO;

public interface IRealmBS {

    public LoginInfoVO findLoginInfoVO(String loginName, String password) throws EMSException;

    public List<MenuItemVO> findMenuList(Integer userId, Integer menuId) throws EMSException;
}
