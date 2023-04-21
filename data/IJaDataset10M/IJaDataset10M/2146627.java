package com.herestudio.service;

import org.springframework.context.MessageSource;
import com.herestudio.config.HahereConfig;
import com.herestudio.manager.IAnalyzerManager;
import com.herestudio.manager.IBasicManager;
import com.herestudio.manager.IGameManager;
import com.herestudio.manager.IPrivilegeManager;
import com.herestudio.manager.IUserIdManager;
import com.herestudio.manager.ICenterManager;
import com.herestudio.manager.IUserselfManager;
import com.herestudio.manager.IWebStatManager;
import com.octo.captcha.service.multitype.MultiTypeCaptchaService;

/**
 * ���з�������
 * 
 * @author luzm
 */
public class ServiceFacade {

    private IBasicManager basicManager;

    private ICenterManager centerManager;

    private IUserselfManager userselfManager;

    private IUserIdManager userIdManager;

    private IWebStatManager webStatManager;

    private IGameManager gameManager;

    private HahereConfig hahereConfig;

    private IPrivilegeManager privilegeManager;

    private IAnalyzerManager analyzerManager;

    private MultiTypeCaptchaService captchaService;

    private MessageSource messageResource;

    private DaoFacade daoFacade;

    public ICenterManager getCenterManager() {
        return centerManager;
    }

    public void setCenterManager(ICenterManager centerManager) {
        this.centerManager = centerManager;
    }

    public void setGameManager(IGameManager gameManager) {
        this.gameManager = gameManager;
    }

    public IWebStatManager getWebStatManager() {
        return webStatManager;
    }

    public void setWebStatManager(IWebStatManager webStatManager) {
        this.webStatManager = webStatManager;
    }

    public HahereConfig getHahereConfig() {
        return hahereConfig;
    }

    public void setHahereConfig(HahereConfig hahereConfig) {
        this.hahereConfig = hahereConfig;
    }

    public IUserIdManager getUserIdManager() {
        return userIdManager;
    }

    public void setUserIdManager(IUserIdManager userIdManager) {
        this.userIdManager = userIdManager;
    }

    public MultiTypeCaptchaService getCaptchaService() {
        return captchaService;
    }

    public void setCaptchaService(MultiTypeCaptchaService captchaService) {
        this.captchaService = captchaService;
    }

    public IGameManager getGameManager() {
        return gameManager;
    }

    public IBasicManager getBasicManager() {
        return basicManager;
    }

    public void setBasicManager(IBasicManager basicManager) {
        this.basicManager = basicManager;
    }

    public IUserselfManager getUserselfManager() {
        return userselfManager;
    }

    public void setUserselfManager(IUserselfManager userselfManager) {
        this.userselfManager = userselfManager;
    }

    public MessageSource getMessageResource() {
        return messageResource;
    }

    public void setMessageResource(MessageSource messageResource) {
        this.messageResource = messageResource;
    }

    public IPrivilegeManager getPrivilegeManager() {
        return privilegeManager;
    }

    public void setPrivilegeManager(IPrivilegeManager privilegeManager) {
        this.privilegeManager = privilegeManager;
    }

    public DaoFacade getDaoFacade() {
        return daoFacade;
    }

    public void setDaoFacade(DaoFacade daoFacade) {
        this.daoFacade = daoFacade;
    }

    public IAnalyzerManager getAnalyzerManager() {
        return analyzerManager;
    }

    public void setAnalyzerManager(IAnalyzerManager analyzerManager) {
        this.analyzerManager = analyzerManager;
    }
}
