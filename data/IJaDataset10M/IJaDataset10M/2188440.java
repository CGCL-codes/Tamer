package cn.adamkts.admin.administrator.entity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Component;

/**
 * 管理员对象
 */
@Component
public class Admin implements java.io.Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 8673469521488408877L;

    private Integer id;

    private String realName;

    private String userName;

    private String password;

    private Boolean sex;

    private String telephone;

    private String address;

    private Boolean islock;

    private String remark;

    private Set<AdminGroup> adminGroups = new HashSet<AdminGroup>();

    /**
	 * 返回本组下以“,”分割的模块Id集合
	 * @return
	 */
    public String getPermissionIdString() {
        String str = "";
        for (AdminGroup adminGroup : this.adminGroups) {
            str += "," + adminGroup.getPermissionIdString();
        }
        return str.substring(1);
    }

    /**
	 * 返回本组下以“,”分割并使用引号引起来的模块URL集合
	 * @return
	 */
    public String getPermissionUrlString() {
        String str = "";
        for (AdminGroup adminGroup : this.adminGroups) {
            str += "," + adminGroup.getPermissionUrlString();
        }
        return str.substring(1);
    }

    /**
	 * 返回本模块下以“,”分割的资源Id集合
	 * @return
	 */
    public String getResourcesIdString() {
        String str = "";
        for (AdminGroup adminGroup : this.adminGroups) {
            for (Permission permission : adminGroup.getPermissions()) {
                str += "," + permission.getResourcesIdString();
            }
        }
        return str.substring(1);
    }

    /**
	 * 返回本模块下以“,”分割并使用引号引起来的资源URL集合
	 * @return
	 */
    public String getResourcesUrlString() {
        String str = "";
        for (AdminGroup adminGroup : this.adminGroups) {
            for (Permission permission : adminGroup.getPermissions()) {
                str += "," + permission.getResourcesUrlString();
            }
        }
        return str.substring(1);
    }

    /** default constructor */
    public Admin() {
    }

    public Admin(Integer id, String realName, String userName, String password, Boolean sex, String telephone, String address, Boolean islock, String remark, Set<AdminGroup> adminGroups) {
        super();
        this.id = id;
        this.realName = realName;
        this.userName = userName;
        this.password = password;
        this.sex = sex;
        this.telephone = telephone;
        this.address = address;
        this.islock = islock;
        this.remark = remark;
        this.adminGroups = adminGroups;
    }

    public String getIslockName() {
        return this.islock ? "是" : "否";
    }

    public String getGroupNames() {
        String groupName = "";
        if (this.adminGroups != null && this.adminGroups.size() > 0) {
            for (AdminGroup adminGroup : this.adminGroups) {
                groupName += "," + adminGroup.getName();
            }
        }
        groupName = groupName.equals("") ? "未知组" : groupName.substring(1);
        return groupName;
    }

    public Integer getId() {
        return this.id;
    }

    public Set<AdminGroup> getAdminGroups() {
        return adminGroups;
    }

    public void setAdminGroups(Set<AdminGroup> adminGroups) {
        this.adminGroups = adminGroups;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRealName() {
        return this.realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getSex() {
        return this.sex;
    }

    public void setSex(Boolean sex) {
        this.sex = sex;
    }

    public String getTelephone() {
        return this.telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Boolean getIslock() {
        return this.islock;
    }

    public void setIslock(Boolean islock) {
        this.islock = islock;
    }

    public String getRemark() {
        return this.remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
