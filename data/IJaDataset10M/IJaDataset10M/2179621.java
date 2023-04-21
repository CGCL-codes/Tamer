package org.makados.web.service;

import org.makados.web.domain.Message;
import org.makados.web.repository.ImsDao;
import org.makados.web.utils.smileys.SmileysService;
import org.makados.web.utils.formatter.FormatterService;
import org.makados.web.beans.ImsServiceStatBean;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

/**
 * @author makados
 */
public class ImsServiceImpl implements ImsService {

    private ImsDao imsDao;

    private SmileysService smileysManager;

    private FormatterService formatterService;

    public List<Message> getMessages(long idUser, byte protocol, String uid, Date from, Date till, int offset, int count) {
        List<Message> list = imsDao.getMessages(idUser, protocol, uid, from, till, offset, count);
        prepareText(list);
        return list;
    }

    public long getMessagesCount(long idUser, byte protocol, String uid, Date from, Date till) {
        return imsDao.getMessagesCount(idUser, protocol, uid, from, till);
    }

    public List<Message> searchMessages(long idUser, Hashtable<Integer, Object> params, int offset, int count) {
        List<Message> list = imsDao.searchMessages(idUser, params, offset, count);
        prepareText(list);
        return list;
    }

    public long getSearchCount(long idUser, Hashtable<Integer, Object> params) {
        return imsDao.getSearchCount(idUser, params);
    }

    public ImsServiceStatBean getServiceStat() {
        return imsDao.getServiceStat();
    }

    public void addMessages(long userId, List<Message> messages) {
        imsDao.addMessages(userId, messages);
    }

    private void prepareText(List<Message> list) {
        for (Message message : list) {
            message.setText(formatterService.parse(message.getText()));
            message.setText(smileysManager.parse(message.getText()));
        }
    }

    public ImsDao getImsDao() {
        return imsDao;
    }

    public void setImsDao(ImsDao imsDao) {
        this.imsDao = imsDao;
    }

    public SmileysService getSmileysManager() {
        return smileysManager;
    }

    public void setSmileysManager(SmileysService smileysManager) {
        this.smileysManager = smileysManager;
    }

    public FormatterService getFormatterService() {
        return formatterService;
    }

    public void setFormatterService(FormatterService formatterService) {
        this.formatterService = formatterService;
    }
}
