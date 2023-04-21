package com.companyname.common.sysframe.valid;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import com.companyname.common.sysframe.define.SysFrameParams;
import com.companyname.common.sysframe.define.UserStatus;
import com.companyname.common.sysframe.manager.UserManager;
import com.companyname.common.sysframe.model.User;
import com.companyname.common.util.mvc.valid.UserValidator;
import com.companyname.common.util.mvc.valid.ValidatedResult;

/**
 * <p>Title: 登录</p>
 * <p>Description: 登录 Org </p>
 * <p>Copyright: Copyright (c) 2004-2006</p>
 * <p>Company: 公司名</p>
 * @ $Author: 作者名 $
 * @ $Date: 创建日期 $
 * @ $Revision: 1.0 $
 * @ created in 创建日期
 *
 */
public class LogginValidator extends UserValidator {

    static Logger logger = Logger.getLogger(LogginValidator.class);

    public static final Map errorCount4Wait = new HashMap();

    public static final Map errorCount = new HashMap();

    public static final Map lastLoginTime = new HashMap();

    /**
         * 登陆的校验
         *
         * @ param: HttpServletRequest request
         * @ param: ValidatedResult vr 校验结果存放器
         * @ param: String[] params 配置参数
         */
    public void validing(HttpServletRequest request, ValidatedResult vr, String[] params) {
        logger.debug("开始登录前校验");
        this.monitor();
        UserManager userManager = (UserManager) this.getBean("userManager");
        User user = userManager.getByAccount(request.getParameter("account"));
        if (user == null) {
            vr.setErrMess("errMess", "帐户不存在");
            return;
        }
        if (user.getStatus() != UserStatus.VALID) {
            vr.setErrMess("errMess", "帐号处于" + user.getStatusName() + "状态,无法登录");
            return;
        }
        if (!this.isAllowed2Login(user)) {
            logger.debug("密码错误次数过多");
            vr.setErrMess("errMess", "密码错误次数过多,系统暂停该帐号的访问,请在5分钟后重试");
            return;
        }
        this.monitor();
        if (!user.checkPW(request.getParameter("password"))) {
            this.pwErrCount4WaitPP(user.getAccount());
            if (this.has2BeLocked(user)) {
                logger.info("密码错误次数过多,系统锁定该帐号(" + user.getAccount() + ")");
                vr.setErrMess("errMess", "密码错误次数过多,系统锁定该帐号");
                user.setStatus(UserStatus.LOCKED);
                userManager.update(user);
                this.resetPwErrCount(user.getAccount());
                this.resetPwErrCount4Wait(user.getAccount());
                return;
            }
            vr.setErrMess("errMess", "密码不正确");
            return;
        } else {
            Date now = new Date();
            if (!user.getPwLimtDate().after(now)) {
                vr.setErrMess("errMess", "密码已经过期");
                return;
            }
            if (!user.getIdLimtDate().after(now)) {
                vr.setErrMess("errMess", "帐号已经过期");
                return;
            }
            this.resetPwErrCount(user.getAccount());
            this.resetPwErrCount4Wait(user.getAccount());
            logger.debug("允许登录");
            request.getSession().setAttribute(SysFrameParams.CURRENT_USER_ID, user.getId());
        }
    }

    public boolean lessThanPwErrCount4Wait(User user) {
        if (user.getPwErrorNum() == 0) {
            this.resetPwErrCount4Wait(user.getAccount());
            return true;
        } else {
            return this.getPwErrCount4Wait(user.getAccount()) < user.getPwErrorNum();
        }
    }

    public boolean has2BeLocked(User user) {
        if (user.getPwErrorLock() == 0) {
            this.resetPwErrCount(user.getAccount());
            return false;
        } else {
            return !(this.getPwErrCount(user.getAccount()) < user.getPwErrorLock());
        }
    }

    public int getPwErrCount4Wait(String account) {
        Integer i = (Integer) errorCount4Wait.get(account);
        if (i == null) {
            return 0;
        } else {
            return i;
        }
    }

    public int pwErrCount4WaitPP(String account) {
        this.setLoginTime(account);
        int i = this.getPwErrCount4Wait(account);
        i++;
        synchronized (errorCount4Wait) {
            errorCount4Wait.put(account, i);
        }
        int j = this.getPwErrCount(account);
        j++;
        synchronized (errorCount) {
            errorCount.put(account, j);
        }
        return i;
    }

    public int getPwErrCount(String account) {
        Integer i = (Integer) errorCount.get(account);
        if (i == null) {
            return 0;
        } else {
            return i;
        }
    }

    public void resetPwErrCount4Wait(String account) {
        logger.debug("重置密码错误次数4等待");
        synchronized (errorCount4Wait) {
            errorCount4Wait.remove(account);
        }
        synchronized (lastLoginTime) {
            lastLoginTime.remove(account);
        }
    }

    public void resetPwErrCount(String account) {
        logger.debug("重置密码错误次数");
        synchronized (errorCount) {
            errorCount.remove(account);
        }
    }

    public void setLoginTime(String account) {
        synchronized (lastLoginTime) {
            lastLoginTime.put(account, new Date());
        }
    }

    public Date getLoginTime(String account) {
        return (Date) lastLoginTime.get(account);
    }

    public boolean isAllowed2Login(User user) {
        if (this.lessThanPwErrCount4Wait(user)) {
            return true;
        } else {
            Date llt = this.getLoginTime(user.getAccount());
            if (llt != null) {
                Date now = new Date();
                now.setMinutes(now.getMinutes() - 5);
                if (!now.before(llt)) {
                    logger.debug("密码错误次数置0");
                    this.resetPwErrCount4Wait(user.getAccount());
                    return true;
                } else {
                    return false;
                }
            } else {
                return true;
            }
        }
    }

    public void monitor() {
        if (logger.isDebugEnabled()) {
            logger.debug("＝＝＝＝errorCount＝＝＝＝");
            this.out(errorCount);
            logger.debug("＝＝＝＝errorCount4Wait＝＝＝＝");
            this.out(errorCount4Wait);
            logger.debug("＝＝＝＝lastLoginTime＝＝＝＝");
            this.out(lastLoginTime);
        }
    }

    private void out(Map map) {
        Object[] keys = map.keySet().toArray();
        for (int i = 0; i < keys.length; i++) {
            System.out.println(keys[i] + " : " + map.get(keys[i]));
        }
    }
}
