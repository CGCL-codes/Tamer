package artem.finance.server.service.user;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import artem.finance.server.dao.UserDAOI;
import artem.finance.server.dao.util.DaoFactory;
import artem.finance.server.persist.Persistancible;
import artem.finance.server.persist.User;
import artem.finance.server.persist.beans.UserBean;
import artem.finance.server.service.user.UserServiceI;

public class UserServiceImpl implements UserServiceI {

    private DaoFactory daoFactory = DaoFactory.getInstance();

    private UserDAOI userDao = daoFactory.getUserDAO();

    public UserServiceImpl() {
    }

    @Override
    public void delete(UserBean userBean) throws RemoteException {
        userDao.delete(userBean.getUser());
    }

    @SuppressWarnings("unchecked")
    public List<UserBean> findAll() {
        List<UserBean> userBeans = new ArrayList<UserBean>();
        List<Object> allUsers = userDao.findAll();
        for (Object user : allUsers) {
            UserBean userBean = new UserBean((User) user);
            userBeans.add(userBean);
        }
        return userBeans;
    }

    @Override
    public Persistancible findById(Long id) throws RemoteException {
        return userDao.findById(id);
    }

    @Override
    public void save(UserBean userBean) throws RemoteException {
        userDao.saveOrUpdate(userBean.getUser());
    }

    @Override
    public void saveOrUpdate(UserBean userBean) throws RemoteException {
        userDao.saveOrUpdate(userBean.getUser());
    }
}
