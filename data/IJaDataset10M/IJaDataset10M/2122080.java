package mx.com.nyak.empresa.service.impl;

import java.util.List;
import mx.com.nyak.base.dto.User;
import mx.com.nyak.base.service.AbstractBaseService;
import mx.com.nyak.base.service.CRUDService;
import mx.com.nyak.base.util.StackTraceUtil;
import mx.com.nyak.empresa.bo.UsuarioBO;
import mx.com.nyak.empresa.exception.BusinessException;
import mx.com.nyak.empresa.exception.ServiceException;

public class UsuarioService extends AbstractBaseService implements CRUDService {

    private UsuarioBO usuarioBO;

    private User user;

    private List<User> users;

    public void delete(Object dataTransferObject) throws ServiceException {
        try {
            usuarioBO.delete(dataTransferObject);
        } catch (BusinessException e) {
            logger.error(StackTraceUtil.getStackTrace(e));
            throw new ServiceException(e);
        }
    }

    public Object findById(Integer id) throws ServiceException {
        try {
            setUser((User) usuarioBO.findById(id));
        } catch (BusinessException e) {
            logger.error(StackTraceUtil.getStackTrace(e));
            throw new ServiceException(e);
        }
        return getUser();
    }

    @SuppressWarnings("unchecked")
    public List<?> findByProperty(String propertyName, Object value) throws ServiceException {
        try {
            setUsers((List<User>) usuarioBO.findByProperty(propertyName, value));
        } catch (BusinessException e) {
            logger.error(StackTraceUtil.getStackTrace(e));
            throw new ServiceException(e);
        }
        return getUsers();
    }

    public void save(Object dataTransferObject) throws ServiceException {
        try {
            usuarioBO.save(dataTransferObject);
        } catch (BusinessException e) {
            logger.error(StackTraceUtil.getStackTrace(e));
            throw new ServiceException(e);
        }
    }

    public void setUsuarioBO(UsuarioBO usuarioBO) {
        this.usuarioBO = usuarioBO;
    }

    public UsuarioBO getUsuarioBO() {
        return usuarioBO;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public List<User> getUsers() {
        return users;
    }
}
