package com.companyname.common.sysframe.valid;

import java.io.Serializable;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import com.companyname.common.sysframe.define.SysFrameParams;
import com.companyname.common.sysframe.define.UserStatus;
import com.companyname.common.sysframe.model.User;
import com.companyname.common.util.mvc.valid.UserValidator;
import com.companyname.common.util.mvc.valid.ValidatedResult;

/**
 * <p>Title: 修改密码</p>
 * <p>Description: 修改密码 </p>
 * <p>Copyright: Copyright (c) 2004-2006</p>
 * <p>Company: 公司名</p>
 * @ $Author: 作者名 $
 * @ $Date: 创建日期 $
 * @ $Revision: 1.0 $
 * @ created in 创建日期
 *
 */
public class ChangeUserPassWordValidator extends UserValidator {

    static Logger logger = Logger.getLogger(ChangeUserPassWordValidator.class);

    /**
         * 修改密码
         *
         * @ param: HttpServletRequest request
         * @ param: ValidatedResult vr 校验结果存放器
         * @ param: String[] params 配置参数
         */
    public void validing(HttpServletRequest request, ValidatedResult vr, String[] params) {
        logger.debug("开始进行修改密码前校验");
        User user = (User) this.validBaseObjectById(request, vr, "userManager", "errMess", (Serializable) request.getSession().getAttribute(SysFrameParams.CURRENT_USER_ID), "无法获取当前用户id");
        String oldPassword = this.validString(request, vr, "oldPassWord", 0, 6);
        String password = this.validString(request, vr, "newPassWord", 6, 6);
        if (!user.checkPW(oldPassword)) {
            vr.setErrMess("errMess", "密码不正确");
            return;
        }
        if (vr.isAllValidated()) {
            logger.debug("通过校验");
            user.resetPW(password);
            request.setAttribute(User.class.getName(), user);
        }
    }
}
