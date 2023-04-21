package com.xiaxueqi.service.security;

import org.apache.commons.lang.StringUtils;
import com.niagara.security.auth.UserDetailService;
import com.niagara.security.auth.entity.Permission;
import com.niagara.security.auth.entity.UserDetail;

/**
 * @author Sean.he
 * 
 */
public class UserDetailTestService implements UserDetailService {

    public UserDetail loadUserDetailByLoginId(String loginId) {
        Permission[] ps = new Permission[6];
        Permission p = new Permission();
        p.setPermissionKey("/demo/login-success.action");
        Permission p1 = new Permission();
        p1.setPermissionKey("/demo/login-success.action:p_key_1");
        Permission p2 = new Permission();
        p2.setPermissionKey("/demo/login-success.action:p_key_2");
        Permission p3 = new Permission();
        p3.setPermissionKey("/demo/login-success.action:p_key_3");
        Permission p4 = new Permission();
        p4.setPermissionKey("com.niagara.test.AnnotationTest.doRpcTest");
        Permission p5 = new Permission();
        p5.setPermissionKey("com.xiaxueqi.web.demo.RpcDemo.testSendEmail");
        ps[0] = p;
        ps[1] = p1;
        ps[2] = p2;
        ps[3] = p3;
        ps[4] = p4;
        ps[5] = p5;
        UserDetail detail = new UserDetail(1L, "admin", "a", ps);
        if (StringUtils.equals(loginId, "admin")) return detail; else return null;
    }
}
