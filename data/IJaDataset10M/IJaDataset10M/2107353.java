package com.kongur.star.venus.domain.courses;

import java.util.Date;
import java.util.List;
import com.kongur.star.venus.domain.system.SystemUser;

/***
 *
 */
public class CoursesGroupDO {

    private Long id;

    private String groupName;

    private String remark;

    /**
     * ʡ�����ǹ�Ҽ��γ���
     */
    private Integer groupType;

    private Date gmtCreate;

    private Date gmtModify;

    private String creator;

    private String operator;

    /**
     * ���ƻ��Ǹ�ר��
     */
    private Integer subGroupType;

    private Integer year;

    private List<CoursesDO> courses;

    private List<SystemUser> experts;

    private String[] courseIds;

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getSubGroupType() {
        return subGroupType;
    }

    public void setSubGroupType(Integer subGroupType) {
        this.subGroupType = subGroupType;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String[] getCourseIds() {
        return courseIds;
    }

    public void setCourseIds(String[] courseIds) {
        this.courseIds = courseIds;
    }

    public String getCourseNames() {
        StringBuilder sb = new StringBuilder();
        if (courses != null && !courses.isEmpty()) {
            for (int i = 0; i < courses.size(); i++) {
                CoursesDO course = courses.get(i);
                sb.append(course.getName());
                if (i < courses.size() - 1) {
                    sb.append(" �� ");
                }
            }
        }
        return sb.toString();
    }

    public String getExpertNames() {
        StringBuilder sb = new StringBuilder();
        if (experts != null && !experts.isEmpty()) {
            for (int i = 0; i < experts.size(); i++) {
                SystemUser user = experts.get(i);
                sb.append(user.getRealName());
                if (i < experts.size() - 1) {
                    sb.append(" �� ");
                }
            }
        }
        return sb.toString();
    }

    public List<CoursesDO> getCourses() {
        return courses;
    }

    public void setCourses(List<CoursesDO> courses) {
        this.courses = courses;
    }

    public List<SystemUser> getExperts() {
        return experts;
    }

    public void setExperts(List<SystemUser> experts) {
        this.experts = experts;
    }

    public Integer getGroupType() {
        return groupType;
    }

    public void setGroupType(Integer groupType) {
        this.groupType = groupType;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public Date getGmtModify() {
        return gmtModify;
    }

    public void setGmtModify(Date gmtModify) {
        this.gmtModify = gmtModify;
    }

    /***
     * Get id
     */
    public Long getId() {
        return id;
    }

    /***
     * Set id
     * 
     * @param id id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /***
     * Get groupName
     */
    public String getGroupName() {
        return groupName;
    }

    /***
     * Set groupName
     * 
     * @param groupName groupName
     */
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    /***
     * Get remark
     */
    public String getRemark() {
        return remark;
    }

    /***
     * Set remark
     * 
     * @param remark remark
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }

    /***
	 * 
	 */
    @Override
    public String toString() {
        StringBuffer mb = new StringBuffer();
        mb.append("id" + id);
        mb.append("groupName" + groupName);
        mb.append("remark" + remark);
        return new StringBuilder().append("CoursesGroupDO").append(mb).toString();
    }
}
