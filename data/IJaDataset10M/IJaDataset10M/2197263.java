package com.hand.action.make;

import com.hand.model.TAccountAdm;
import com.hand.service.IAgentService;
import com.hand.utils.MD5Util;
import com.opensymphony.xwork2.ActionSupport;

/**
 * 修改账户密码Action
 * 功能：本方法用来处理用户修改账户密码的请求
 * 说明：接受客户端修改用户账户密码请求，执行更新密码操作
 * 实现步骤：
 * 		1、接受客户端参数，并对数据进行校验
 * 		2、检查该用户旧密码是否正确
 * 		3、保存新密码
 * @author mengzm
 * @version 0.0.1
 * @createTime 2011/5/20 13:19
 */
public class ModifyPwdAction extends ActionSupport {

    private IAgentService agentService;

    private String oldPassWord;

    private String newPassWord;

    private int userId;

    private int result;

    @Override
    public String execute() throws Exception {
        TAccountAdm accountAdm = agentService.getAgentById(userId);
        if (null != accountAdm) {
            oldPassWord = MD5Util.encrypt(oldPassWord);
            if (accountAdm.getPwd().equals(oldPassWord)) {
                newPassWord = MD5Util.encrypt(newPassWord);
                accountAdm.setPwd(newPassWord);
                if (0 != agentService.updateAgent(accountAdm)) {
                    result = 1;
                }
            } else {
                result = 2;
            }
        }
        return SUCCESS;
    }

    public void setAgentService(IAgentService agentService) {
        this.agentService = agentService;
    }

    public void setOldPassWord(String oldPassWord) {
        this.oldPassWord = oldPassWord;
    }

    public void setNewPassWord(String newPassWord) {
        this.newPassWord = newPassWord;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getResult() {
        return result;
    }
}
