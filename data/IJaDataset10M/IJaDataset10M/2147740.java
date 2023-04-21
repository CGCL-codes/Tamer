package com.kongur.star.venus.domain.system;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;
import com.eyeieye.melody.web.cookyjar.SelfDependence;
import com.eyeieye.melody.web.cookyjar.util.SelfUtil;
import com.kongur.star.venus.enums.EnumUserType;
import com.kongur.star.venus.enums.PermissionEnum;
import com.kongur.star.venus.web.util.MenuIDNameMap;

/**
 * ��̨�û���cookie�־û�����
 * @author gaojf
 * @version $Id: SystemAgent.java,v 0.1 2012-3-28 ����10:27:15 gaojf Exp $
 */
public class SystemAgent implements Serializable, SelfDependence {

    private static final long serialVersionUID = -3921778874150895446L;

    private Long accountId;

    private String account;

    private String menuId;

    private BigInteger functions;

    private Integer userType;

    public SystemAgent() {
        super();
    }

    public SystemAgent(SystemUser systemUser) {
        super();
        this.accountId = systemUser.getId();
        this.account = systemUser.getLoginName();
        this.functions = new BigInteger("0");
        this.userType = systemUser.getUserType();
    }

    /**
     * ��ָ����2����λ���Ƿ���Ȩ��
     *
     * @param index
     * @return
     */
    public boolean haveFunction(int index) {
        if ("admin".equalsIgnoreCase(this.account) || this.functions.testBit(PermissionEnum.SUPER_ADMIN.getCode())) {
            return true;
        }
        if (functions == null) {
            return false;
        }
        return this.functions.testBit(index);
    }

    /**
     * �Ƿ��д���Ȩ��ö����������֮һ��Ȩ��
     *
     * @param permissionName
     * @return
     */
    public boolean haveFunction(String... permissionName) {
        if ("admin".equalsIgnoreCase(this.account) || this.functions.testBit(PermissionEnum.SUPER_ADMIN.getCode())) {
            return true;
        }
        for (String pName : permissionName) {
            PermissionEnum p = PermissionEnum.indexOf(pName);
            if (null == p) {
                return false;
            }
            if (this.functions.testBit(p.getCode())) {
                return true;
            }
        }
        return false;
    }

    /**
     * ���Ƿ���й���ģ��Ȩ��
     * @param module
     * @return
     */
    public boolean haveModule(int module) {
        if ("admin".equalsIgnoreCase(this.account) || this.functions.testBit(PermissionEnum.SUPER_ADMIN.getCode())) {
            return true;
        }
        List<PermissionEnum> listPermissionEnum = PermissionEnum.indexModule(module);
        if (listPermissionEnum.isEmpty()) {
            return false;
        } else {
            for (PermissionEnum each : listPermissionEnum) {
                if (haveFunction(each)) {
                    return true;
                }
            }
            return false;
        }
    }

    /**
     * �Ƿ�������֮һģ��Ȩ��
     * @param modules
     * @return
     */
    public boolean haveOneModuleOf(int... modules) {
        if (null == modules || modules.length < 1) {
            throw new java.lang.IllegalArgumentException("modules must have one more!");
        }
        for (int module : modules) {
            if (haveModule(module)) {
                return true;
            }
        }
        return false;
    }

    /**
     * �Ƿ�ӵ��Ȩ��
     *
     * @param fe
     * @return
     */
    public boolean haveFunction(PermissionEnum fe) {
        return haveFunction(fe.getCode());
    }

    /**
     * �����û���Ȩ��,ʵ��Ӧ�ÿ���ʹ��������Ȩ�ޱ�id��Ψһ����������������滻�����enum.ordinal
     *
     * @param funs
     */
    public void setFunctions(List<PermissionEnum> funs) {
        this.functions = new BigInteger("0");
        for (PermissionEnum en : funs) {
            this.functions = this.functions.setBit(en.getCode());
        }
    }

    public void setFunctions(int pos) {
        if (this.functions == null) {
            this.functions = new BigInteger("0");
        }
        this.functions = this.functions.setBit(pos);
    }

    public BigInteger getFunctions() {
        return functions;
    }

    public void setFunctions(BigInteger functions) {
        this.functions = functions;
    }

    public String lieDown() {
        return SelfUtil.format(String.valueOf(this.accountId), this.account, this.menuId, this.functions.toString(36), String.valueOf(userType));
    }

    public SelfDependence riseUp(String value) {
        String[] values = SelfUtil.recover(value);
        this.accountId = Long.valueOf(values[0]);
        this.account = values[1];
        this.menuId = values[2];
        this.functions = new BigInteger(values[3], 36);
        this.userType = Integer.valueOf(values[4]);
        return this;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public Long getAccountId() {
        return accountId;
    }

    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }

    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public String[] getMenuName() {
        return MenuIDNameMap.indexNameById(this.menuId);
    }

    public boolean isSchoolManager() {
        return EnumUserType.isSchoolManager(this.userType);
    }

    public boolean isExpert() {
        return EnumUserType.isExpert(this.userType);
    }

    public boolean isCoursesManager() {
        return EnumUserType.isCoursesManager(this.userType);
    }

    public boolean isAdmin() {
        return EnumUserType.isAdmin(this.userType);
    }
}
