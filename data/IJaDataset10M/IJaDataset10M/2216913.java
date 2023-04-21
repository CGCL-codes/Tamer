package com.hk.sms.cmd;

import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import com.hk.bean.HkbLog;
import com.hk.bean.Invite;
import com.hk.bean.Randnum;
import com.hk.bean.ScoreLog;
import com.hk.bean.User;
import com.hk.bean.UserSmsPort;
import com.hk.sms.ReceivedSms;
import com.hk.sms.Sms;
import com.hk.sms2.SmsPortProcessAble;
import com.hk.svr.InviteService;
import com.hk.svr.UserService;
import com.hk.svr.UserSmsPortService;
import com.hk.svr.pub.HkLog;
import com.hk.svr.pub.HkbConfig;
import com.hk.svr.pub.ScoreConfig;

public class Bind2Cmd extends BaseCmd {

    @Autowired
    UserService userService;

    @Autowired
    private InviteService inviteService;

    @Autowired
    private UserSmsPortService userSmsPortService;

    private final Log log = LogFactory.getLog(Bind2Cmd.class);

    @Override
    public String execute(ReceivedSms receivedSms, SmsPortProcessAble smsPortProcessAble) {
        log.info("execute bind2");
        String mobile = receivedSms.getMobile();
        String content = receivedSms.getContent();
        content = content.substring(2, content.length());
        int randvalue = Integer.parseInt(content);
        log.info("mobile [ " + mobile + " ]");
        log.info("randvalue [ " + randvalue + " ]");
        Sms sms = new Sms();
        sms.setMobile(receivedSms.getMobile());
        sms.setLinkid(receivedSms.getLinkid());
        Randnum o = this.userService.getRandnumByRandvalue(randvalue);
        if (o == null) {
            log.warn("randvalue [ " + content + " ] is null");
            sms.setContent("验证码已经过期,请重新申请");
            this.sendMsg(sms);
            return null;
        }
        long userId = o.getUserId();
        if (userId == 0) {
            log.warn("userId is 0");
            sms.setContent("验证码已经过期,请重新申请");
            this.sendMsg(sms);
            return null;
        }
        this.userService.bindMobile(userId, mobile);
        this.userService.clearTimeoutRandnum(o.getSysId());
        User user = this.userService.getUser(userId);
        List<Invite> list = this.inviteService.getSuccessInviteListByFriendId(userId);
        for (Invite i : list) {
            if (i.getAddhkbflg() == Invite.ADDHKBFLG_Y) {
                continue;
            }
            HkbLog hkbLog = HkbLog.create(i.getUserId(), HkLog.INVITE, userId, HkbConfig.getInvite());
            this.userService.addHkb(hkbLog);
            ScoreLog scoreLog = ScoreLog.create(i.getUserId(), HkLog.INVITE, userId, ScoreConfig.getInvite());
            this.userService.addScore(scoreLog);
            i.setAddhkbflg(Invite.ADDHKBFLG_Y);
            this.inviteService.updateInvite(i);
        }
        UserSmsPort port = this.userSmsPortService.getUserSmsPortByUserId(userId);
        if (port == null) {
            this.userSmsPortService.makeAvailableUserSmsPort(userId);
        }
        String msgContent = mobile + "已经成功绑定到" + user.getNickName() + ",以后本账号将可以使用昵称、手机号、email三种方式的登录火酷";
        sms.setContent(msgContent);
        this.sendMsg(sms);
        return null;
    }
}
