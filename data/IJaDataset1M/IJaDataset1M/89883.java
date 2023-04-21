package com.hk.sms.cmd;

import com.hk.bean.Company;
import com.hk.frame.util.DataUtil;
import com.hk.sms.ReceivedSms;
import com.hk.sms2.SmsPortProcessAble;

/**
 * 修改足迹电话
 * 
 * @author yuanwei
 */
public class MgrCmpUpdateTelphoneCmd extends BaseCmd {

    @Override
    public String execute(ReceivedSms receivedSms, SmsPortProcessAble smsPortProcessAble) throws Exception {
        Company company = this.getCompany(receivedSms, smsPortProcessAble);
        if (company == null) {
            return null;
        }
        String ss = receivedSms.getContent().toLowerCase();
        String tel = null;
        if (ss.startsWith("dh+")) {
            tel = ss.substring(3);
        } else {
            tel = ss.substring(2);
        }
        if (DataUtil.isEmpty(tel)) {
            return null;
        }
        company.setTel(tel);
        this.processUpdateCompany(receivedSms, company);
        return null;
    }
}
