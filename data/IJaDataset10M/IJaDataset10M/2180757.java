package com.f2ms.bean;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import org.apache.log4j.Logger;
import org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException;
import com.f2ms.enumeration.UserStatus;
import com.f2ms.exception.DAOException;
import com.f2ms.exception.ServiceException;
import com.f2ms.model.Role;
import com.f2ms.model.User;
import com.f2ms.service.common.ICommonService;
import com.f2ms.util.F2MSUtil;
import com.f2ms.util.IF2MSConstants;

public class UserBean extends BaseBean {

    private static Logger logger = Logger.getLogger(UserBean.class);

    private static final String FWD_SEARCH_USER = "searchUser";

    private static final String FWD_CREATE_USER = "createUser";

    private User user;

    private ICommonService commonService;

    private String idNo;

    private boolean editMode;

    private String username;

    private String staffid;

    private String staffName;

    private String status;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ICommonService getCommonService() {
        return commonService;
    }

    public void setCommonService(ICommonService commonService) {
        this.commonService = commonService;
    }

    public String getIdNo() {
        return idNo;
    }

    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }

    public boolean isEditMode() {
        return editMode;
    }

    public void setEditMode(boolean editMode) {
        this.editMode = editMode;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStaffid() {
        return staffid;
    }

    public void setStaffid(String staffid) {
        this.staffid = staffid;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    @PostConstruct
    public void init() {
        user = new User();
        user.setStatus(String.valueOf(UserStatus.ACTIVE.getValue()));
    }

    public String create() {
        editMode = false;
        return FWD_CREATE_USER;
    }

    public String save() {
        String returnPage = FWD_SEARCH_USER;
        String userName = (String) F2MSUtil.getSession().getAttribute(IF2MSConstants.USER_NAME);
        try {
            user.setChangedBy(userName);
            if (editMode) {
                commonService.editUser(user);
            } else {
                user.setCreatedBy(userName);
                commonService.createUser(user);
            }
        } catch (HibernateOptimisticLockingFailureException fe) {
            fe.printStackTrace();
            FacesContext context = FacesContext.getCurrentInstance();
            FacesMessage errMsg = new FacesMessage(getMessage("errOptLock", null));
            context.addMessage("User Exception", errMsg);
            returnPage = FWD_CREATE_USER;
        } catch (DAOException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            returnPage = FWD_GLOBAL_ERROR;
        } catch (ServiceException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            returnPage = FWD_GLOBAL_ERROR;
        }
        return returnPage;
    }

    public String search() {
        return FWD_SEARCH_USER;
    }

    public List<User> getListResult() {
        try {
            User searchCriteria = new User();
            searchCriteria.setUsername(username);
            searchCriteria.setStatus(status);
            return commonService.findUserByCriteria(searchCriteria);
        } catch (DAOException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
        return null;
    }

    public String edit() {
        editMode = true;
        FacesContext context = FacesContext.getCurrentInstance();
        @SuppressWarnings("rawtypes") Map requestMap = context.getExternalContext().getRequestParameterMap();
        String prmIdNo = (String) requestMap.get("idNo");
        try {
            user = commonService.findUserById(Long.parseLong(prmIdNo));
            staffName = user.getStaffObj().getName();
        } catch (NumberFormatException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            return FWD_GLOBAL_ERROR;
        } catch (DAOException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            return FWD_GLOBAL_ERROR;
        }
        return FWD_CREATE_USER;
    }

    public String getUserHeader() {
        String header = getMessage("newUserHeader", null);
        if (editMode) {
            header = getMessage("editUserHeader", null);
        }
        return header;
    }

    public List<SelectItem> getAllRoles() {
        List<SelectItem> items = new ArrayList<SelectItem>();
        try {
            List<Role> listRoles = commonService.findAllRoles();
            for (Iterator<Role> iterator = listRoles.iterator(); iterator.hasNext(); ) {
                Role role = (Role) iterator.next();
                items.add(new SelectItem(role.getRoleCode(), role.getRoleDesc()));
            }
        } catch (DAOException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
        return items;
    }

    public String resetPswd() {
        String defaultPassword = F2MSUtil.getRandomString(10);
        String userName = (String) F2MSUtil.getSession().getAttribute(IF2MSConstants.USER_NAME);
        try {
            User found = commonService.findUserById(user.getIdNo());
            if (found != null) {
                found.setPassword(defaultPassword);
                found.setChangedBy(userName);
                User edited = commonService.editUserOnly(found);
                commonService.sendUserRegistrationEmail(edited);
                FacesContext context = FacesContext.getCurrentInstance();
                FacesMessage errMsg = new FacesMessage(getMessage("msgResetSuccess", null));
                context.addMessage("Booking Exception", errMsg);
            }
        } catch (DAOException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            return FWD_GLOBAL_ERROR;
        } catch (AddressException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            return FWD_GLOBAL_ERROR;
        } catch (MessagingException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            return FWD_GLOBAL_ERROR;
        }
        return FWD_CREATE_USER;
    }
}
