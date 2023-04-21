package com.hk.web.admin.action;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.hk.bean.Badge;
import com.hk.bean.CmpAdminGroup;
import com.hk.bean.Company;
import com.hk.bean.CompanyKind;
import com.hk.bean.CompanyKindUtil;
import com.hk.bean.ParentKind;
import com.hk.frame.util.DataUtil;
import com.hk.frame.util.image.ImageException;
import com.hk.frame.util.image.NotPermitImageFormatException;
import com.hk.frame.util.image.OutOfSizeException;
import com.hk.frame.util.page.SimplePage;
import com.hk.frame.web.http.HkRequest;
import com.hk.frame.web.http.HkResponse;
import com.hk.svr.BadgeService;
import com.hk.svr.CmpAdminGroupService;
import com.hk.svr.CompanyService;
import com.hk.svr.pub.Err;
import com.hk.web.pub.action.BaseAction;

@Component("/admin/badge")
public class AdminBadgeAction extends BaseAction {

    @Autowired
    private BadgeService badgeService;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private CmpAdminGroupService cmpAdminGroupService;

    public String execute(HkRequest req, HkResponse resp) throws Exception {
        String name = req.getString("name");
        SimplePage page = req.getSimplePage(20);
        List<Badge> list = this.badgeService.getBadgeList(name, page.getBegin(), page.getSize() + 1);
        this.processListForPage(page, list);
        req.setAttribute("list", list);
        req.setEncodeAttribute("name", name);
        return this.getWapJsp("admin/badge/list.jsp");
    }

    /**
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception
	 */
    public String sel(HkRequest req, HkResponse resp) throws Exception {
        return this.getWapJsp("admin/badge/sel.jsp");
    }

    /**
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception
	 */
    public String createnolimit(HkRequest req, HkResponse resp) {
        if (req.getInt("ch") == 0) {
            return this.getWapJsp("admin/badge/createnolimit.jsp");
        }
        int num = req.getIntAndSetAttr("num");
        String intro = req.getStringAndSetAttr("intro");
        File file = req.getFile("f");
        Badge o = new Badge();
        String name = req.getStringAndSetAttr("name");
        o.setName(DataUtil.toHtmlRow(name));
        o.setLimitflg(Badge.LIMITFLG_N);
        o.setIntro(DataUtil.toHtmlRow(intro));
        Map<String, String> map = new HashMap<String, String>();
        map.put("checkinnum", String.valueOf(num));
        o.setRuleData(DataUtil.toJson(map));
        try {
            this.badgeService.createBadge(o, file);
            this.setOpFuncSuccessMsg(req);
            return "r:/admin/badge_view.do?badgeId=" + o.getBadgeId() + "&continue=1&limitflg=" + Badge.LIMITFLG_N;
        } catch (ImageException e) {
            req.setText(String.valueOf(Err.IMG_UPLOAD_ERROR));
            return "r:/admin/badge_createnolimit.do?ch=0";
        } catch (NotPermitImageFormatException e) {
            req.setText(String.valueOf(Err.IMG_FMT_ERROR));
            return "r:/admin/badge_createnolimit.do?ch=0";
        } catch (OutOfSizeException e) {
            req.setText(String.valueOf(Err.IMG_OUTOFSIZE_ERROR), "1M");
            return "r:/admin/badge_createnolimit.do?ch=0";
        }
    }

    /**
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception
	 */
    public String createinvite(HkRequest req, HkResponse resp) {
        if (req.getInt("ch") == 0) {
            return this.getWapJsp("admin/badge/createinvite.jsp");
        }
        String intro = req.getStringAndSetAttr("intro");
        File file = req.getFile("f");
        Badge o = new Badge();
        String name = req.getStringAndSetAttr("name");
        o.setName(DataUtil.toHtmlRow(name));
        o.setLimitflg(Badge.LIMITFLG_INVITE);
        o.setIntro(DataUtil.toHtmlRow(intro));
        try {
            this.badgeService.createBadge(o, file);
            this.setOpFuncSuccessMsg(req);
            return "r:/admin/badge_view.do?badgeId=" + o.getBadgeId() + "&continue=1&limitflg=" + Badge.LIMITFLG_INVITE;
        } catch (ImageException e) {
            req.setText(String.valueOf(Err.IMG_UPLOAD_ERROR));
            return "r:/admin/badge_createinvite.do?ch=0";
        } catch (NotPermitImageFormatException e) {
            req.setText(String.valueOf(Err.IMG_FMT_ERROR));
            return "r:/admin/badge_createinvite.do?ch=0";
        } catch (OutOfSizeException e) {
            req.setText(String.valueOf(Err.IMG_OUTOFSIZE_ERROR), "1M");
            return "r:/admin/badge_createinvite.do?ch=0";
        }
    }

    /**
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception
	 */
    public String createsys(HkRequest req, HkResponse resp) {
        if (req.getInt("ch") == 0) {
            return this.getWapJsp("admin/badge/createsys.jsp");
        }
        String intro = req.getStringAndSetAttr("intro");
        File file = req.getFile("f");
        Badge o = new Badge();
        String name = req.getStringAndSetAttr("name");
        o.setName(DataUtil.toHtmlRow(name));
        o.setLimitflg(Badge.LIMITFLG_SYS);
        o.setIntro(DataUtil.toHtmlRow(intro));
        o.setRuleflg(req.getInt("ruleflg"));
        try {
            this.badgeService.createBadge(o, file);
            this.setOpFuncSuccessMsg(req);
            return "r:/admin/badge_view.do?badgeId=" + o.getBadgeId() + "&continue=1&limitflg=" + Badge.LIMITFLG_SYS;
        } catch (ImageException e) {
            req.setText(String.valueOf(Err.IMG_UPLOAD_ERROR));
            return "r:/admin/badge_createsys.do?ch=0";
        } catch (NotPermitImageFormatException e) {
            req.setText(String.valueOf(Err.IMG_FMT_ERROR));
            return "r:/admin/badge_createsys.do?ch=0";
        } catch (OutOfSizeException e) {
            req.setText(String.valueOf(Err.IMG_OUTOFSIZE_ERROR), "1M");
            return "r:/admin/badge_createsys.do?ch=0";
        }
    }

    /**
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception
	 */
    public String createcycle(HkRequest req, HkResponse resp) {
        long companyId = req.getLongAndSetAttr("companyId");
        Company company = this.companyService.getCompany(companyId);
        req.setAttribute("company", company);
        if (req.getInt("ch") == 0) {
            return this.getWapJsp("admin/badge/createcycle.jsp");
        }
        int num = req.getIntAndSetAttr("num");
        int cycle = req.getInt("cycle");
        String intro = req.getStringAndSetAttr("intro");
        File file = req.getFile("f");
        Badge o = new Badge();
        String name = req.getStringAndSetAttr("name");
        o.setName(DataUtil.toHtmlRow(name));
        o.setCompanyId(req.getLong("companyId"));
        o.setLimitflg(Badge.LIMITFLG_Y);
        o.setRuleflg(Badge.RULEFLG_CMP_CYCLE_CHECKIN_NUM);
        o.setIntro(DataUtil.toHtmlRow(intro));
        Map<String, String> map = new HashMap<String, String>();
        map.put("checkinnum", String.valueOf(num));
        map.put("cycle", String.valueOf(cycle));
        map.put("companyname", company.getName());
        o.setRuleData(DataUtil.toJson(map));
        try {
            this.badgeService.createBadge(o, file);
            this.setOpFuncSuccessMsg(req);
            return "r:/admin/badge_view.do?badgeId=" + o.getBadgeId() + "&continue=1&limitflg=" + Badge.LIMITFLG_Y + "&ruleflg=" + Badge.RULEFLG_CMP_CYCLE_CHECKIN_NUM;
        } catch (ImageException e) {
            req.setText(String.valueOf(Err.IMG_UPLOAD_ERROR));
            return "r:/admin/badge_createcycle.do?ch=0";
        } catch (NotPermitImageFormatException e) {
            req.setText(String.valueOf(Err.IMG_FMT_ERROR));
            return "r:/admin/badge_createcycle.do?ch=0";
        } catch (OutOfSizeException e) {
            req.setText(String.valueOf(Err.IMG_OUTOFSIZE_ERROR), "1M");
            return "r:/admin/badge_createcycle.do?ch=0";
        }
    }

    /**
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception
	 */
    public String createcmpcheckin(HkRequest req, HkResponse resp) {
        long companyId = req.getLongAndSetAttr("companyId");
        Company company = this.companyService.getCompany(companyId);
        req.setAttribute("company", company);
        if (req.getInt("ch") == 0) {
            return this.getWapJsp("admin/badge/createcmpcheckin.jsp");
        }
        int num = req.getIntAndSetAttr("num");
        String intro = req.getStringAndSetAttr("intro");
        File file = req.getFile("f");
        Badge o = new Badge();
        String name = req.getStringAndSetAttr("name");
        o.setName(DataUtil.toHtmlRow(name));
        o.setCompanyId(req.getLong("companyId"));
        o.setLimitflg(Badge.LIMITFLG_Y);
        o.setRuleflg(Badge.RULEFLG_CMP_CHECKIN_NUM);
        o.setIntro(DataUtil.toHtmlRow(intro));
        Map<String, String> map = new HashMap<String, String>();
        map.put("checkinnum", String.valueOf(num));
        map.put("companyname", company.getName());
        o.setRuleData(DataUtil.toJson(map));
        try {
            this.badgeService.createBadge(o, file);
            this.setOpFuncSuccessMsg(req);
            return "r:/admin/badge_view.do?badgeId=" + o.getBadgeId() + "&continue=1&limitflg=" + Badge.LIMITFLG_Y + "&ruleflg=" + Badge.RULEFLG_CMP_CHECKIN_NUM;
        } catch (ImageException e) {
            req.setText(String.valueOf(Err.IMG_UPLOAD_ERROR));
            return "r:/admin/badge_createcmpcheckin.do?ch=0";
        } catch (NotPermitImageFormatException e) {
            req.setText(String.valueOf(Err.IMG_FMT_ERROR));
            return "r:/admin/badge_createcmpcheckin.do?ch=0";
        } catch (OutOfSizeException e) {
            req.setText(String.valueOf(Err.IMG_OUTOFSIZE_ERROR), "1M");
            return "r:/admin/badge_createcmpcheckin.do?ch=0";
        }
    }

    /**
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception
	 */
    public String createparentkindcheckin(HkRequest req, HkResponse resp) {
        int parentId = req.getIntAndSetAttr("parentId");
        req.setAttribute("parentKind", CompanyKindUtil.getParentKind(parentId));
        if (req.getInt("ch") == 0) {
            return this.getWapJsp("admin/badge/createparentkindcheckin.jsp");
        }
        int num = req.getIntAndSetAttr("num");
        String intro = req.getStringAndSetAttr("intro");
        File file = req.getFile("f");
        Badge o = new Badge();
        String name = req.getStringAndSetAttr("name");
        o.setName(DataUtil.toHtmlRow(name));
        o.setParentKindId(parentId);
        o.setLimitflg(Badge.LIMITFLG_Y);
        o.setRuleflg(Badge.RULEFLG_CMPKIND_CHECKIN_NUM);
        o.setIntro(DataUtil.toHtmlRow(intro));
        Map<String, String> map = new HashMap<String, String>();
        map.put("checkinnum", String.valueOf(num));
        map.put("parentname", CompanyKindUtil.getParentKind(parentId).getName());
        o.setRuleData(DataUtil.toJson(map));
        try {
            this.badgeService.createBadge(o, file);
            this.setOpFuncSuccessMsg(req);
            return "r:/admin/badge_view.do?badgeId=" + o.getBadgeId() + "&continue=1&limitflg=" + Badge.LIMITFLG_Y + "&ruleflg=" + Badge.RULEFLG_CMPKIND_CHECKIN_NUM;
        } catch (ImageException e) {
            req.setText(String.valueOf(Err.IMG_UPLOAD_ERROR));
            return "r:/admin/badge_createparentkindcheckin.do?ch=0";
        } catch (NotPermitImageFormatException e) {
            req.setText(String.valueOf(Err.IMG_FMT_ERROR));
            return "r:/admin/badge_createparentkindcheckin.do?ch=0";
        } catch (OutOfSizeException e) {
            req.setText(String.valueOf(Err.IMG_OUTOFSIZE_ERROR), "1M");
            return "r:/admin/badge_createparentkindcheckin.do?ch=0";
        }
    }

    /**
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception
	 */
    public String createkindcheckin(HkRequest req, HkResponse resp) {
        int kindId = req.getIntAndSetAttr("kindId");
        req.setAttribute("kind", CompanyKindUtil.getCompanyKind(kindId));
        if (req.getInt("ch") == 0) {
            return this.getWapJsp("admin/badge/createkindcheckin.jsp");
        }
        int num = req.getIntAndSetAttr("num");
        String intro = req.getStringAndSetAttr("intro");
        File file = req.getFile("f");
        Badge o = new Badge();
        String name = req.getStringAndSetAttr("name");
        o.setName(DataUtil.toHtmlRow(name));
        o.setKindId(kindId);
        o.setLimitflg(Badge.LIMITFLG_Y);
        o.setRuleflg(Badge.RULEFLG_CMPCHILDKIND_CHECKIN_NUM);
        o.setIntro(DataUtil.toHtmlRow(intro));
        Map<String, String> map = new HashMap<String, String>();
        map.put("checkinnum", String.valueOf(num));
        map.put("kindname", CompanyKindUtil.getCompanyKind(kindId).getName());
        o.setRuleData(DataUtil.toJson(map));
        try {
            this.badgeService.createBadge(o, file);
            this.setOpFuncSuccessMsg(req);
            return "r:/admin/badge_view.do?badgeId=" + o.getBadgeId() + "&continue=1&limitflg=" + Badge.LIMITFLG_Y + "&ruleflg=" + Badge.RULEFLG_CMPCHILDKIND_CHECKIN_NUM;
        } catch (ImageException e) {
            req.setText(String.valueOf(Err.IMG_UPLOAD_ERROR));
            return "r:/admin/badge_createkindcheckin.do?ch=0";
        } catch (NotPermitImageFormatException e) {
            req.setText(String.valueOf(Err.IMG_FMT_ERROR));
            return "r:/admin/badge_createkindcheckin.do?ch=0";
        } catch (OutOfSizeException e) {
            req.setText(String.valueOf(Err.IMG_OUTOFSIZE_ERROR), "1M");
            return "r:/admin/badge_createkindcheckin.do?ch=0";
        }
    }

    /**
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception
	 */
    public String createcmpgroupcheckin(HkRequest req, HkResponse resp) {
        long groupId = req.getLongAndSetAttr("groupId");
        CmpAdminGroup group = this.cmpAdminGroupService.getCmpAdminGroup(groupId);
        req.setAttribute("cmpAdminGroup", group);
        if (req.getInt("ch") == 0) {
            return this.getWapJsp("admin/badge/createcmpgroupcheckin.jsp");
        }
        int num = req.getIntAndSetAttr("num");
        String intro = req.getStringAndSetAttr("intro");
        File file = req.getFile("f");
        Badge o = new Badge();
        String name = req.getStringAndSetAttr("name");
        o.setName(DataUtil.toHtmlRow(name));
        o.setGroupId(req.getLong("groupId"));
        o.setLimitflg(Badge.LIMITFLG_Y);
        o.setRuleflg(Badge.RULEFLG_CMPGROUP_CHECKIN_NUM);
        o.setIntro(DataUtil.toHtmlRow(intro));
        Map<String, String> map = new HashMap<String, String>();
        map.put("checkinnum", String.valueOf(num));
        map.put("groupname", group.getName());
        o.setRuleData(DataUtil.toJson(map));
        try {
            this.badgeService.createBadge(o, file);
            this.setOpFuncSuccessMsg(req);
            return "r:/admin/badge_view.do?badgeId=" + o.getBadgeId() + "&continue=1&limitflg=" + Badge.LIMITFLG_Y + "&ruleflg=" + Badge.RULEFLG_CMPGROUP_CHECKIN_NUM;
        } catch (ImageException e) {
            req.setText(String.valueOf(Err.IMG_UPLOAD_ERROR));
            return "r:/admin/badge_createcmpgroupcheckin.do?ch=0";
        } catch (NotPermitImageFormatException e) {
            req.setText(String.valueOf(Err.IMG_FMT_ERROR));
            return "r:/admin/badge_createcmpgroupcheckin.do?ch=0";
        } catch (OutOfSizeException e) {
            req.setText(String.valueOf(Err.IMG_OUTOFSIZE_ERROR), "1M");
            return "r:/admin/badge_createcmpgroupcheckin.do?ch=0";
        }
    }

    /**
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception
	 */
    public String editcmpcheckin(HkRequest req, HkResponse resp) {
        long badgeId = req.getLongAndSetAttr("badgeId");
        Badge o = (Badge) req.getAttribute("o");
        if (o == null) {
            o = this.badgeService.getBadge(badgeId);
        }
        req.setAttribute("o", o);
        Company company = this.companyService.getCompany(o.getCompanyId());
        req.setAttribute("company", company);
        if (req.getInt("ch") == 0) {
            return this.getWapJsp("admin/badge/editcmpcheckin.jsp");
        }
        int num = req.getIntAndSetAttr("num");
        String intro = req.getStringAndSetAttr("intro");
        File file = req.getFile("f");
        String name = req.getStringAndSetAttr("name");
        o.setName(DataUtil.toHtmlRow(name));
        o.setLimitflg(Badge.LIMITFLG_Y);
        o.setIntro(DataUtil.toHtmlRow(intro));
        Map<String, String> map = new HashMap<String, String>();
        map.put("checkinnum", String.valueOf(num));
        map.put("companyname", company.getName());
        o.setRuleData(DataUtil.toJson(map));
        try {
            this.badgeService.updateBadge(o, file);
            this.setOpFuncSuccessMsg(req);
            return "r:/admin/badge_view.do?badgeId=" + o.getBadgeId() + "&continue=1&limitflg=" + Badge.LIMITFLG_Y + "&ruleflg=" + Badge.RULEFLG_CMP_CHECKIN_NUM;
        } catch (ImageException e) {
            req.setText(String.valueOf(Err.IMG_UPLOAD_ERROR));
            return "r:/admin/badge_editcmpcheckin.do?ch=0";
        } catch (NotPermitImageFormatException e) {
            req.setText(String.valueOf(Err.IMG_FMT_ERROR));
            return "r:/admin/badge_editcmpcheckin.do?ch=0";
        } catch (OutOfSizeException e) {
            req.setText(String.valueOf(Err.IMG_OUTOFSIZE_ERROR), "1M");
            return "r:/admin/badge_editcmpcheckin.do?ch=0";
        }
    }

    /**
	 * 创建周期方式的徽章
	 * 
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception
	 */
    public String editcmpgroupcheckin(HkRequest req, HkResponse resp) {
        long badgeId = req.getLongAndSetAttr("badgeId");
        Badge o = (Badge) req.getAttribute("o");
        if (o == null) {
            o = this.badgeService.getBadge(badgeId);
        }
        CmpAdminGroup group = this.cmpAdminGroupService.getCmpAdminGroup(o.getGroupId());
        req.setAttribute("cmpAdminGroup", group);
        req.setAttribute("o", o);
        if (req.getInt("ch") == 0) {
            return this.getWapJsp("admin/badge/editcmpgroupcheckin.jsp");
        }
        int num = req.getIntAndSetAttr("num");
        String intro = req.getStringAndSetAttr("intro");
        File file = req.getFile("f");
        String name = req.getStringAndSetAttr("name");
        o.setName(DataUtil.toHtmlRow(name));
        o.setLimitflg(Badge.LIMITFLG_Y);
        o.setIntro(DataUtil.toHtmlRow(intro));
        Map<String, String> map = new HashMap<String, String>();
        map.put("checkinnum", String.valueOf(num));
        map.put("groupname", group.getName());
        o.setRuleData(DataUtil.toJson(map));
        try {
            this.badgeService.updateBadge(o, file);
            this.setOpFuncSuccessMsg(req);
            return "r:/admin/badge_view.do?badgeId=" + o.getBadgeId() + "&continue=1&limitflg=" + Badge.LIMITFLG_Y + "&ruleflg=" + Badge.RULEFLG_CMPGROUP_CHECKIN_NUM;
        } catch (ImageException e) {
            req.setText(String.valueOf(Err.IMG_UPLOAD_ERROR));
            return "r:/admin/badge_editcmpgroupcheckin.do?ch=0";
        } catch (NotPermitImageFormatException e) {
            req.setText(String.valueOf(Err.IMG_FMT_ERROR));
            return "r:/admin/badge_editcmpgroupcheckin.do?ch=0";
        } catch (OutOfSizeException e) {
            req.setText(String.valueOf(Err.IMG_OUTOFSIZE_ERROR), "1M");
            return "r:/admin/badge_editcmpgroupcheckin.do?ch=0";
        }
    }

    /**
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception
	 */
    public String editcycle(HkRequest req, HkResponse resp) {
        long badgeId = req.getLongAndSetAttr("badgeId");
        Badge o = (Badge) req.getAttribute("o");
        if (o == null) {
            o = this.badgeService.getBadge(badgeId);
        }
        Company company = this.companyService.getCompany(o.getCompanyId());
        req.setAttribute("company", company);
        req.setAttribute("o", o);
        if (req.getInt("ch") == 0) {
            return this.getWapJsp("admin/badge/editcycle.jsp");
        }
        int num = req.getIntAndSetAttr("num");
        int cycle = req.getInt("cycle");
        String intro = req.getStringAndSetAttr("intro");
        File file = req.getFile("f");
        String name = req.getStringAndSetAttr("name");
        o.setName(DataUtil.toHtmlRow(name));
        o.setLimitflg(Badge.LIMITFLG_Y);
        o.setIntro(DataUtil.toHtmlRow(intro));
        Map<String, String> map = new HashMap<String, String>();
        map.put("checkinnum", String.valueOf(num));
        map.put("cycle", String.valueOf(cycle));
        map.put("companyname", company.getName());
        o.setRuleData(DataUtil.toJson(map));
        try {
            this.badgeService.updateBadge(o, file);
            this.setOpFuncSuccessMsg(req);
            return "r:/admin/badge_view.do?badgeId=" + o.getBadgeId() + "&continue=1&limitflg=" + Badge.LIMITFLG_Y + "&ruleflg=" + Badge.RULEFLG_CMP_CYCLE_CHECKIN_NUM;
        } catch (ImageException e) {
            req.setText(String.valueOf(Err.IMG_UPLOAD_ERROR));
            return "r:/admin/badge_createcycle.do?ch=0";
        } catch (NotPermitImageFormatException e) {
            req.setText(String.valueOf(Err.IMG_FMT_ERROR));
            return "r:/admin/badge_createcycle.do?ch=0";
        } catch (OutOfSizeException e) {
            req.setText(String.valueOf(Err.IMG_OUTOFSIZE_ERROR), "1M");
            return "r:/admin/badge_createcycle.do?ch=0";
        }
    }

    /**
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception
	 */
    public String editparentkindcheckin(HkRequest req, HkResponse resp) {
        long badgeId = req.getLongAndSetAttr("badgeId");
        Badge o = (Badge) req.getAttribute("o");
        if (o == null) {
            o = this.badgeService.getBadge(badgeId);
        }
        req.setAttribute("parentKind", CompanyKindUtil.getParentKind(o.getParentKindId()));
        req.setAttribute("o", o);
        if (req.getInt("ch") == 0) {
            return this.getWapJsp("admin/badge/editparentkindcheckin.jsp");
        }
        int num = req.getIntAndSetAttr("num");
        String intro = req.getStringAndSetAttr("intro");
        File file = req.getFile("f");
        String name = req.getStringAndSetAttr("name");
        o.setName(DataUtil.toHtmlRow(name));
        o.setLimitflg(Badge.LIMITFLG_Y);
        o.setIntro(DataUtil.toHtmlRow(intro));
        Map<String, String> map = new HashMap<String, String>();
        map.put("checkinnum", String.valueOf(num));
        map.put("parentname", CompanyKindUtil.getParentKind(o.getParentKindId()).getName());
        o.setRuleData(DataUtil.toJson(map));
        try {
            this.badgeService.updateBadge(o, file);
            this.setOpFuncSuccessMsg(req);
            return "r:/admin/badge_view.do?badgeId=" + o.getBadgeId() + "&continue=1&limitflg=" + Badge.LIMITFLG_Y + "&ruleflg=" + Badge.RULEFLG_CMPKIND_CHECKIN_NUM;
        } catch (ImageException e) {
            req.setText(String.valueOf(Err.IMG_UPLOAD_ERROR));
            return "r:/admin/badge_editparentkindcheckin.do?ch=0";
        } catch (NotPermitImageFormatException e) {
            req.setText(String.valueOf(Err.IMG_FMT_ERROR));
            return "r:/admin/badge_editparentkindcheckin.do?ch=0";
        } catch (OutOfSizeException e) {
            req.setText(String.valueOf(Err.IMG_OUTOFSIZE_ERROR), "1M");
            return "r:/admin/badge_editparentkindcheckin.do?ch=0";
        }
    }

    /**
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception
	 */
    public String editkindcheckin(HkRequest req, HkResponse resp) {
        long badgeId = req.getLongAndSetAttr("badgeId");
        Badge o = (Badge) req.getAttribute("o");
        if (o == null) {
            o = this.badgeService.getBadge(badgeId);
        }
        req.setAttribute("kind", CompanyKindUtil.getCompanyKind(o.getKindId()));
        req.setAttribute("o", o);
        if (req.getInt("ch") == 0) {
            return this.getWapJsp("admin/badge/editkindcheckin.jsp");
        }
        int num = req.getIntAndSetAttr("num");
        String intro = req.getStringAndSetAttr("intro");
        File file = req.getFile("f");
        String name = req.getStringAndSetAttr("name");
        o.setName(DataUtil.toHtmlRow(name));
        o.setLimitflg(Badge.LIMITFLG_Y);
        o.setIntro(DataUtil.toHtmlRow(intro));
        Map<String, String> map = new HashMap<String, String>();
        map.put("checkinnum", String.valueOf(num));
        map.put("kindname", CompanyKindUtil.getCompanyKind(o.getKindId()).getName());
        o.setRuleData(DataUtil.toJson(map));
        try {
            this.badgeService.updateBadge(o, file);
            this.setOpFuncSuccessMsg(req);
            return "r:/admin/badge_view.do?badgeId=" + o.getBadgeId() + "&continue=1&limitflg=" + Badge.LIMITFLG_Y + "&ruleflg=" + Badge.RULEFLG_CMPCHILDKIND_CHECKIN_NUM;
        } catch (ImageException e) {
            req.setText(String.valueOf(Err.IMG_UPLOAD_ERROR));
            return "r:/admin/badge_editkindcheckin.do?ch=0";
        } catch (NotPermitImageFormatException e) {
            req.setText(String.valueOf(Err.IMG_FMT_ERROR));
            return "r:/admin/badge_editkindcheckin.do?ch=0";
        } catch (OutOfSizeException e) {
            req.setText(String.valueOf(Err.IMG_OUTOFSIZE_ERROR), "1M");
            return "r:/admin/badge_editkindcheckin.do?ch=0";
        }
    }

    /**
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception
	 */
    public String editnolimit(HkRequest req, HkResponse resp) {
        long badgeId = req.getLongAndSetAttr("badgeId");
        Badge o = (Badge) req.getAttribute("o");
        if (o == null) {
            o = this.badgeService.getBadge(badgeId);
        }
        req.setAttribute("o", o);
        if (req.getInt("ch") == 0) {
            return this.getWapJsp("admin/badge/editnolimit.jsp");
        }
        int num = req.getIntAndSetAttr("num");
        String intro = req.getStringAndSetAttr("intro");
        File file = req.getFile("f");
        String name = req.getStringAndSetAttr("name");
        o.setName(DataUtil.toHtmlRow(name));
        o.setIntro(DataUtil.toHtmlRow(intro));
        Map<String, String> map = new HashMap<String, String>();
        map.put("checkinnum", String.valueOf(num));
        o.setRuleData(DataUtil.toJson(map));
        try {
            this.badgeService.updateBadge(o, file);
            this.setOpFuncSuccessMsg(req);
            return "r:/admin/badge_view.do?badgeId=" + o.getBadgeId() + "&continue=1&limitflg=" + Badge.LIMITFLG_N;
        } catch (ImageException e) {
            req.setText(String.valueOf(Err.IMG_UPLOAD_ERROR));
            return "r:/admin/badge_editnolimit.do?ch=0";
        } catch (NotPermitImageFormatException e) {
            req.setText(String.valueOf(Err.IMG_FMT_ERROR));
            return "r:/admin/badge_editnolimit.do?ch=0";
        } catch (OutOfSizeException e) {
            req.setText(String.valueOf(Err.IMG_OUTOFSIZE_ERROR), "1M");
            return "r:/admin/badge_editnolimit.do?ch=0";
        }
    }

    /**
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception
	 */
    public String editinvite(HkRequest req, HkResponse resp) {
        long badgeId = req.getLongAndSetAttr("badgeId");
        Badge o = (Badge) req.getAttribute("o");
        if (o == null) {
            o = this.badgeService.getBadge(badgeId);
        }
        req.setAttribute("o", o);
        if (req.getInt("ch") == 0) {
            return this.getWapJsp("admin/badge/editinvite.jsp");
        }
        String intro = req.getStringAndSetAttr("intro");
        File file = req.getFile("f");
        String name = req.getStringAndSetAttr("name");
        o.setName(DataUtil.toHtmlRow(name));
        o.setIntro(DataUtil.toHtmlRow(intro));
        try {
            this.badgeService.updateBadge(o, file);
            this.setOpFuncSuccessMsg(req);
            return "r:/admin/badge_view.do?badgeId=" + o.getBadgeId() + "&continue=1&limitflg=" + Badge.LIMITFLG_INVITE;
        } catch (ImageException e) {
            req.setText(String.valueOf(Err.IMG_UPLOAD_ERROR));
            return "r:/admin/badge_editinvite.do?ch=0";
        } catch (NotPermitImageFormatException e) {
            req.setText(String.valueOf(Err.IMG_FMT_ERROR));
            return "r:/admin/badge_editinvite.do?ch=0";
        } catch (OutOfSizeException e) {
            req.setText(String.valueOf(Err.IMG_OUTOFSIZE_ERROR), "1M");
            return "r:/admin/badge_editinvite.do?ch=0";
        }
    }

    /**
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception
	 */
    public String editsys(HkRequest req, HkResponse resp) {
        long badgeId = req.getLongAndSetAttr("badgeId");
        Badge o = (Badge) req.getAttribute("o");
        if (o == null) {
            o = this.badgeService.getBadge(badgeId);
        }
        req.setAttribute("o", o);
        if (req.getInt("ch") == 0) {
            return this.getWapJsp("admin/badge/editsys.jsp");
        }
        String intro = req.getStringAndSetAttr("intro");
        File file = req.getFile("f");
        String name = req.getStringAndSetAttr("name");
        o.setName(DataUtil.toHtmlRow(name));
        o.setIntro(DataUtil.toHtmlRow(intro));
        o.setRuleflg(req.getInt("ruleflg"));
        try {
            this.badgeService.updateBadge(o, file);
            this.setOpFuncSuccessMsg(req);
            return "r:/admin/badge_view.do?badgeId=" + o.getBadgeId() + "&continue=1&limitflg=" + Badge.LIMITFLG_SYS;
        } catch (ImageException e) {
            req.setText(String.valueOf(Err.IMG_UPLOAD_ERROR));
            return "r:/admin/badge_editsys.do?ch=0";
        } catch (NotPermitImageFormatException e) {
            req.setText(String.valueOf(Err.IMG_FMT_ERROR));
            return "r:/admin/badge_editsys.do?ch=0";
        } catch (OutOfSizeException e) {
            req.setText(String.valueOf(Err.IMG_OUTOFSIZE_ERROR), "1M");
            return "r:/admin/badge_editsys.do?ch=0";
        }
    }

    /**
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception
	 */
    public String view(HkRequest req, HkResponse resp) {
        long badgeId = req.getLongAndSetAttr("badgeId");
        req.reSetAttribute("method");
        req.reSetAttribute("continue");
        req.reSetAttribute("limitflg");
        req.reSetAttribute("ruleflg");
        Badge o = this.badgeService.getBadge(badgeId);
        if (o.getCompanyId() > 0) {
            o.setCompany(this.companyService.getCompany(o.getCompanyId()));
        }
        if (o.getGroupId() > 0) {
            o.setCmpAdminGroup(this.cmpAdminGroupService.getCmpAdminGroup(o.getGroupId()));
        }
        if (o.getParentKindId() > 0) {
            o.setParentKind(CompanyKindUtil.getParentKind(o.getParentKindId()));
        }
        if (o.getKindId() > 0) {
            o.setCompanyKind(CompanyKindUtil.getCompanyKind(o.getKindId()));
        }
        req.setAttribute("o", o);
        if (!o.isLimit()) {
            req.setAttribute("edit_method", "editnolimit");
        }
        if (o.getRuleflg() == Badge.RULEFLG_CMP_CYCLE_CHECKIN_NUM) {
        }
        return this.getWapJsp("admin/badge/view.jsp");
    }

    /**
	 * @param req
	 * @param resp
	 * @return
	 */
    public String searchcmp(HkRequest req, HkResponse resp) {
        String name = req.getString("name");
        int ch = req.getIntAndSetAttr("ch");
        if (ch == 1 && name != null) {
            name = DataUtil.getSearchValue(name);
            SimplePage page = req.getSimplePage(20);
            List<Company> list = this.companyService.getCompanyListWithSearch(name, page.getBegin(), page.getSize() + 1);
            this.processListForPage(page, list);
            req.setAttribute("list", list);
            req.setEncodeAttribute("name", name);
        }
        req.reSetAttribute("method");
        return this.getWapJsp("admin/badge/searchcmp.jsp");
    }

    /**
	 * @param req
	 * @param resp
	 * @return
	 */
    public String searchcmpadmingroup(HkRequest req, HkResponse resp) {
        String name = req.getString("name");
        int ch = req.getIntAndSetAttr("ch");
        if (ch == 1 && name != null) {
            name = DataUtil.getSearchValue(name);
            SimplePage page = req.getSimplePage(20);
            List<CmpAdminGroup> list = this.cmpAdminGroupService.getCmpAdminGroupList(name, page.getBegin(), page.getSize() + 1);
            this.processListForPage(page, list);
            req.setAttribute("list", list);
            req.setEncodeAttribute("name", name);
        }
        req.reSetAttribute("method");
        return this.getWapJsp("admin/badge/searchcmpadmingroup.jsp");
    }

    /**
	 * @param req
	 * @param resp
	 * @return
	 */
    public String parentkindlist(HkRequest req, HkResponse resp) {
        List<ParentKind> list = CompanyKindUtil.getParentList();
        req.reSetAttribute("method");
        req.setAttribute("list", list);
        return this.getWapJsp("admin/badge/parentkindlist.jsp");
    }

    /**
	 * @param req
	 * @param resp
	 * @return
	 */
    public String kindlist(HkRequest req, HkResponse resp) {
        int parentId = req.getIntAndSetAttr("parentId");
        List<CompanyKind> list = CompanyKindUtil.getCompanyKindListByParentKindId(parentId);
        req.reSetAttribute("method");
        req.setAttribute("list", list);
        return this.getWapJsp("admin/badge/kindlist.jsp");
    }

    /**
	 * @param req
	 * @param resp
	 * @return
	 */
    public String op(HkRequest req, HkResponse resp) {
        return "r:/admin/badge_" + req.getString("method") + ".do?companyId=" + req.getLong("companyId") + "&groupId=" + req.getLong("groupId") + "&parentId=" + req.getInt("parentId") + "&kindId=" + req.getInt("kindId");
    }

    /**
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception
	 */
    public String create(HkRequest req, HkResponse resp) {
        byte limitflg = req.getByte("limitflg");
        if (limitflg == Badge.LIMITFLG_Y) {
            int ruleflg = req.getInt("ruleflg");
            if (ruleflg == Badge.RULEFLG_CMP_CYCLE_CHECKIN_NUM) {
                return "r:/admin/badge_searchcmp.do?method=createcycle";
            }
            if (ruleflg == Badge.RULEFLG_CMP_CHECKIN_NUM) {
                return "r:/admin/badge_searchcmp.do?method=createcmpcheckin";
            }
            if (ruleflg == Badge.RULEFLG_CMPGROUP_CHECKIN_NUM) {
                return "r:/admin/badge_searchcmpadmingroup.do?method=createcmpgroupcheckin";
            }
            if (ruleflg == Badge.RULEFLG_CMPKIND_CHECKIN_NUM) {
                return "r:/admin/badge_parentkindlist.do";
            }
            if (ruleflg == Badge.RULEFLG_CMPCHILDKIND_CHECKIN_NUM) {
                return "r:/admin/badge_kindlist.do?parentId=" + req.getInt("parentId");
            }
        }
        if (limitflg == Badge.LIMITFLG_SYS) {
            return "r:/admin/badge_createsys.do";
        }
        if (limitflg == Badge.LIMITFLG_INVITE) {
            return "r:/admin/badge_createinvite.do";
        }
        return "r:/admin/badge_createnolimit.do";
    }

    /**
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception
	 */
    public String edit(HkRequest req, HkResponse resp) {
        long badgeId = req.getLong("badgeId");
        Badge badge = this.badgeService.getBadge(badgeId);
        if (badge.isLimit()) {
            if (badge.getRuleflg() == Badge.RULEFLG_CMP_CYCLE_CHECKIN_NUM) {
                return "r:/admin/badge_editcycle.do?badgeId=" + badgeId;
            }
            if (badge.getRuleflg() == Badge.RULEFLG_CMP_CHECKIN_NUM) {
                return "r:/admin/badge_editcmpcheckin.do?badgeId=" + badgeId;
            }
            if (badge.getRuleflg() == Badge.RULEFLG_CMPGROUP_CHECKIN_NUM) {
                return "r:/admin/badge_editcmpgroupcheckin.do?badgeId=" + badgeId;
            }
            if (badge.getRuleflg() == Badge.RULEFLG_CMPKIND_CHECKIN_NUM) {
                return "r:/admin/badge_editparentkindcheckin.do?badgeId=" + badgeId;
            }
            if (badge.getRuleflg() == Badge.RULEFLG_CMPCHILDKIND_CHECKIN_NUM) {
                return "r:/admin/badge_editkindcheckin.do?badgeId=" + badgeId;
            }
        }
        if (badge.isSysLimit()) {
            return "r:/admin/badge_editsys.do?badgeId=" + badgeId;
        }
        if (badge.isInvite()) {
            return "r:/admin/badge_editinvite.do?badgeId=" + badgeId;
        }
        return "r:/admin/badge_editnolimit.do?badgeId=" + badgeId;
    }

    /**
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception
	 */
    public String delete(HkRequest req, HkResponse resp) {
        int cfm = req.getInt("cfm");
        if (cfm == 0) {
            req.reSetAttribute("badgeId");
            return this.getWapJsp("admin/badge/delete.jsp");
        }
        if (req.getString("ok") != null) {
            this.setOpFuncSuccessMsg(req);
            this.badgeService.deleteBadge(req.getLong("badgeId"));
        }
        return "r:/admin/badge.do";
    }
}
