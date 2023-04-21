package com.asoft.common.sysframe.valid;

import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import com.asoft.common.sysframe.define.OrgStatus;
import com.asoft.common.sysframe.manager.OrgManager;
import com.asoft.common.sysframe.model.Org;
import com.asoft.common.util.mvc.valid.UserValidator;
import com.asoft.common.util.mvc.valid.ValidatedResult;

/**
 * <p>Title: 机构</p>
 * <p>Description: 机构 Org </p>
 * <p>Copyright: Copyright (c) 2004-2006</p>
 * <p>Company: asoft</p>
 * @ $Author: amon.lei $
 * @ $Date: 2007-2-20 $
 * @ $Revision: 1.0 $
 * @ created in 2007-2-20
 *
 */
public class UpdateOrgValidator extends UserValidator {

    static Logger logger = Logger.getLogger(UpdateOrgValidator.class);

    /**
         * 修改机构
         *
         * @ param: HttpServletRequest request
         * @ param: ValidatedResult vr 校验结果存放器
         * @ param: String[] params 配置参数
         */
    public void validing(HttpServletRequest request, ValidatedResult vr, String[] params) {
        logger.debug("开始进行修改机构前校验");
        OrgManager orgManager = (OrgManager) this.getBean("orgManager");
        Org org = (Org) this.validBaseObjectById(request, vr, orgManager, "id", "记录已经不存在");
        if (org == null) {
            return;
        }
        String name = this.validString(request, vr, "name", 1, 50);
        Org superOrg = null;
        String superOrgId = request.getParameter("parentId");
        if (superOrgId != null) {
            superOrg = (Org) this.validBaseObjectById(request, vr, orgManager, "parentId", "请重新选择");
        }
        String country = this.validString(request, vr, "country", 0, 50);
        String province = this.validString(request, vr, "province", 0, 50);
        String city = this.validString(request, vr, "city", 0, 50);
        String street = this.validString(request, vr, "street", 0, 100);
        String office = this.validString(request, vr, "office", 0, 50);
        String linkMan = this.validString(request, vr, "linkMan", 0, 50);
        String phone = this.validString(request, vr, "phone", 0, 50);
        String fax = this.validString(request, vr, "fax", 0, 50);
        String site = this.validString(request, vr, "site", 0, 50);
        String email = this.validString(request, vr, "email", 0, 50);
        String remark = this.validString(request, vr, "remark", 0, 500);
        if (vr.isAllValidated()) {
            logger.debug("校验通过");
            org.setName(name);
            org.setSuperOrg(superOrg);
            org.setCountry(country);
            org.setProvince(province);
            org.setCity(city);
            org.setStreet(street);
            org.setOffice(office);
            org.setLinkMan(linkMan);
            org.setPhone(phone);
            org.setFax(fax);
            org.setSite(site);
            org.setEmail(email);
            org.setRemark(remark);
            org.setStatus(OrgStatus.VALID);
            request.setAttribute(Org.class.getName(), org);
        }
    }
}
