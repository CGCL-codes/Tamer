package com.wonebiz.crm.client.entity;

import java.sql.Timestamp;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "user")
public class User implements java.io.Serializable {

    private static final long serialVersionUID = -3335970045484856922L;

    private long id;

    private String userId;

    private String password;

    private String enName;

    private String nickName;

    private Integer deptId;

    private String email;

    private String cell;

    private String phone;

    private String roleId;

    private String resource;

    private Integer sex;

    private Date birthday;

    private String portrait;

    private Timestamp onborad;

    private String remark;

    private Integer maxCustomer;

    public Integer getMaxCustomer() {
        return maxCustomer;
    }

    public void setMaxCustomer(Integer maxCustomer) {
        this.maxCustomer = maxCustomer;
    }

    private Integer isFrozen;

    private Integer isDelete;

    /** default constructor */
    public User() {
    }

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Column(name = "userId", length = 25)
    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Column(name = "password", length = 20)
    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Column(name = "enName", length = 25)
    public String getEnName() {
        return this.enName;
    }

    public void setEnName(String enName) {
        this.enName = enName;
    }

    @Column(name = "nickName", length = 20)
    public String getNickName() {
        return this.nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    @Column(name = "deptId")
    public Integer getDeptId() {
        return this.deptId;
    }

    public void setDeptId(Integer deptId) {
        this.deptId = deptId;
    }

    @Column(name = "email", length = 40)
    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Column(name = "cell", length = 15)
    public String getCell() {
        return this.cell;
    }

    public void setCell(String cell) {
        this.cell = cell;
    }

    @Column(name = "phone", length = 15)
    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Column(name = "roleId")
    public String getRoleId() {
        return this.roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    @Column(name = "resource")
    public String getResource() {
        return this.resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    @Column(name = "sex")
    public Integer getSex() {
        return this.sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    @Temporal(TemporalType.DATE)
    @Column(name = "birthday", length = 0)
    public Date getBirthday() {
        return this.birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    @Column(name = "portrait", length = 300)
    public String getPortrait() {
        return this.portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }

    @Column(name = "onborad", length = 0)
    public Timestamp getOnborad() {
        return this.onborad;
    }

    public void setOnborad(Timestamp onborad) {
        this.onborad = onborad;
    }

    @Column(name = "remark", length = 300)
    public String getRemark() {
        return this.remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Column(name = "isFrozen")
    public Integer getIsFrozen() {
        return this.isFrozen;
    }

    public void setIsFrozen(Integer isFrozen) {
        this.isFrozen = isFrozen;
    }

    @Column(name = "isDelete")
    public Integer getIsDelete() {
        return this.isDelete;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }
}
