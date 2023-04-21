package cn.myapps.core.email.email.ejb;

import java.util.Date;
import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.DAOFactory;
import cn.myapps.base.dao.DataPackage;
import cn.myapps.base.dao.IDesignTimeDAO;
import cn.myapps.base.dao.PersistenceUtils;
import cn.myapps.base.dao.ValueObject;
import cn.myapps.base.ejb.AbstractDesignTimeProcessBean;
import cn.myapps.core.email.email.dao.EmailUserDAO;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.Security;

public class EmailUserProcessBean extends AbstractDesignTimeProcessBean<EmailUser> implements EmailUserProcess {

    /**
	 * 
	 */
    private static final long serialVersionUID = -8921925024606594233L;

    protected IDesignTimeDAO<EmailUser> getDAO() throws Exception {
        return (EmailUserDAO) DAOFactory.getDefaultDAO(EmailUser.class.getName());
    }

    public EmailUser getEmailUser(String account, String domainid) throws Exception {
        return ((EmailUserDAO) getDAO()).queryEmailUser(account, domainid);
    }

    public EmailUser getEmailUserByAccount(String account) throws Exception {
        return ((EmailUserDAO) getDAO()).queryEmailUserByAccount(account);
    }

    public DataPackage<EmailUser> getEmailUsers(String domainid, ParamsTable params) throws Exception {
        return ((EmailUserDAO) getDAO()).queryEmailUsers(domainid, params);
    }

    public static void main(String[] args) {
        try {
            EmailUserProcess processBean = (EmailUserProcess) ProcessFactory.createProcess(EmailUserProcess.class);
            EmailUser user = new EmailUser();
            user.setAccount("tom");
            user.setName("tom");
            user.setPassword("123456");
            processBean.doCreate(user);
            EmailUser user2 = new EmailUser();
            user2.setAccount("tao");
            user2.setName("tao");
            user2.setPassword("123456");
            processBean.doCreate(user2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public EmailUser getEmailUserByOwner(String ownerid, String domainid) throws Exception {
        return ((EmailUserDAO) getDAO()).queryEmailUserByOwner(ownerid, domainid);
    }

    public void doCreateEmailUser(EmailUser user) throws Exception {
        super.doCreate(user);
    }

    @Override
    public void doCreate(ValueObject vo) throws Exception {
        EmailUser user = (EmailUser) vo;
        if (user.getPassword() != null && user.getPassword().trim().length() > 0) {
            user.setPassword(Security.encodeToBASE64(user.getPassword()));
        }
        user.setCreateDate(new Date());
        super.doCreate(vo);
    }

    public void doRemoveEmailUser(String id) throws Exception {
        super.doRemove(id);
    }

    public void doUpdateEmailUser(EmailUser user) throws Exception {
        super.doUpdate(user);
    }

    @Override
    public void doUpdate(ValueObject vo) throws Exception {
        EmailUser user = (EmailUser) vo;
        try {
            PersistenceUtils.beginTransaction();
            EmailUser po = (EmailUser) getDAO().find(vo.getId());
            if (po != null) {
                if (user.getPassword() != null && user.getPassword().trim().length() > 0) {
                    String base64 = Security.encodeToBASE64(user.getPassword());
                    user.setPassword(base64);
                    po.setPassword(base64);
                } else {
                }
                po.setAccount(user.getAccount());
                getDAO().update(po);
            } else {
                if (user.getPassword() != null && user.getPassword().trim().length() > 0) {
                    user.setPassword(Security.encodeToBASE64(user.getPassword()));
                }
                getDAO().update(user);
            }
            PersistenceUtils.commitTransaction();
        } catch (Exception e) {
            PersistenceUtils.rollbackTransaction();
            e.printStackTrace();
            throw e;
        }
    }
}
