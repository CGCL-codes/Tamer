package com.nodeshop.action.admin;

import java.util.Set;
import javax.annotation.Resource;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.beans.BeanUtils;
import com.nodeshop.entity.Member;
import com.nodeshop.entity.MemberRank;
import com.nodeshop.service.MemberRankService;
import com.opensymphony.xwork2.interceptor.annotations.InputConfig;
import com.opensymphony.xwork2.validator.annotations.IntRangeFieldValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredFieldValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import com.opensymphony.xwork2.validator.annotations.Validations;

/**
 * 后台Action类 - 会员分类
 
 * 版权所有 2008-2010 长沙鼎诚软件有限公司，并保留所有权利。
 
 
 
 
 
 * KEY: nodeshop01E1B0AF20100A00A008418505742C55
 
 */
@ParentPackage("admin")
public class MemberRankAction extends BaseAdminAction {

    private static final long serialVersionUID = -5451875129461788865L;

    private MemberRank memberRank;

    @Resource
    private MemberRankService memberRankService;

    public String checkName() {
        String oldValue = getParameter("oldValue");
        String newValue = memberRank.getName();
        if (memberRankService.isUnique("name", oldValue, newValue)) {
            return ajaxText("true");
        } else {
            return ajaxText("false");
        }
    }

    public String list() {
        pager = memberRankService.findByPager(pager);
        return LIST;
    }

    public String delete() {
        for (String id : ids) {
            MemberRank memberRank = memberRankService.load(id);
            Set<Member> memberSet = memberRank.getMemberSet();
            if (memberSet != null && memberSet.size() > 0) {
                return ajaxJsonErrorMessage("会员等级[" + memberRank.getName() + "]下存在会员,删除失败!");
            }
        }
        long totalCount = memberRankService.getTotalCount();
        if (ids.length >= totalCount) {
            return ajaxJsonErrorMessage("删除失败!必须至少保留一个会员等级");
        }
        memberRankService.delete(ids);
        return ajaxJsonSuccessMessage("删除成功！");
    }

    public String add() {
        return INPUT;
    }

    public String edit() {
        memberRank = memberRankService.load(id);
        return INPUT;
    }

    @Validations(requiredStrings = { @RequiredStringValidator(fieldName = "memberRank.name", message = "等级名称不允许为空!") }, requiredFields = { @RequiredFieldValidator(fieldName = "memberRank.preferentialScale", message = "优惠百分比不允许为空!"), @RequiredFieldValidator(fieldName = "memberRank.point", message = "所需积分不允许为空!"), @RequiredFieldValidator(fieldName = "memberRank.isDefault", message = "是否为默认等级不允许为空!") }, intRangeFields = { @IntRangeFieldValidator(fieldName = "memberRank.point", min = "0", message = "所需积分只允许为正整数或零!") })
    @InputConfig(resultName = "error")
    public String save() {
        if (memberRank.getPreferentialScale() < 0) {
            addActionError("优惠百分比必须大于或等于零!");
            return ERROR;
        }
        if (memberRankService.getMemberRankByPoint(memberRank.getPoint()) != null) {
            addActionError("已存在相同积分的会员等级!");
            return ERROR;
        }
        memberRankService.save(memberRank);
        redirectionUrl = "member_rank!list.action";
        return SUCCESS;
    }

    @Validations(requiredStrings = { @RequiredStringValidator(fieldName = "memberRank.name", message = "等级名称不允许为空!") }, requiredFields = { @RequiredFieldValidator(fieldName = "memberRank.preferentialScale", message = "优惠百分比不允许为空!"), @RequiredFieldValidator(fieldName = "memberRank.point", message = "所需积分不允许为空!"), @RequiredFieldValidator(fieldName = "memberRank.isDefault", message = "是否为默认等级不允许为空!") }, intRangeFields = { @IntRangeFieldValidator(fieldName = "memberRank.point", min = "0", message = "所需积分只允许为正整数或零!") })
    @InputConfig(resultName = "error")
    public String update() {
        if (memberRank.getPreferentialScale() < 0) {
            addActionError("优惠百分比必须大于或等于零!");
            return ERROR;
        }
        MemberRank persistent = memberRankService.load(id);
        MemberRank equalPointMemberRank = memberRankService.getMemberRankByPoint(memberRank.getPoint());
        if (equalPointMemberRank != null && equalPointMemberRank != persistent) {
            addActionError("已存在相同积分的会员等级!");
            return ERROR;
        }
        BeanUtils.copyProperties(memberRank, persistent, new String[] { "id", "createDate", "modifyDate", "memberSet" });
        memberRankService.update(persistent);
        redirectionUrl = "member_rank!list.action";
        return SUCCESS;
    }

    public MemberRank getMemberRank() {
        return memberRank;
    }

    public void setMemberRank(MemberRank memberRank) {
        this.memberRank = memberRank;
    }
}
