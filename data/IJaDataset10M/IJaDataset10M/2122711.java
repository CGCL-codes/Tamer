package com.oolong.account.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import com.oolong.account.dal.UserDAO;
import com.oolong.account.dal.data.User;
import com.oolong.account.exception.LoginException;
import com.oolong.account.exception.PasswordError;
import com.oolong.account.exception.UserNotExistException;
import com.oolong.account.model.UserModel;

/**
 * @author oolong
 * 
 */
public class ExtraServiceImp implements ExtraService {

    UserDAO userDAO;

    String host;

    String username;

    String password;

    JavaMailSender mailSender;

    @Override
    public boolean validateUserExist(String name) {
        User user = userDAO.selectByName(name);
        if (user != null && user.getId() > 0) {
            return true;
        } else return false;
    }

    @Override
    public UserModel validate(String name, String password) throws Exception {
        User user = userDAO.selectByNamepsw(name, password);
        UserModel userModel = new UserModel();
        if (user != null && user.getId() > 0) {
            userModel.setId(user.getId());
            userModel.setName(user.getName());
            userModel.setEmail(user.getEmail());
        } else if (user == null) {
            throw new UserNotExistException();
        } else {
            throw new LoginException();
        }
        return userModel;
    }

    @Override
    public int createUser(String name, String password, String email) throws Exception {
        User user = new User();
        int insertresult;
        user.setName(name);
        user.setEmail(email);
        user.setPassword(password);
        insertresult = userDAO.insert(user);
        if (insertresult > 0) {
            return insertresult;
        } else {
            throw new Exception();
        }
    }

    @Override
    public void sendMail(String to, String text) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        if (to.compareTo("") == 0) {
            return;
        } else {
            String[] tos = to.split(";");
            for (String toone : tos) {
                mailMessage.setTo(toone);
                mailMessage.setFrom("oolongice@163.com");
                mailMessage.setSubject("来自Oolongice.com发送的账单");
                mailMessage.setText(text);
                mailSender.send(mailMessage);
            }
        }
    }

    @Override
    public int editUser(Integer id, String oldpsw, String newpsw, String email) throws Exception {
        User user = userDAO.selectByPrimaryKey(id);
        int result = 0;
        if (oldpsw.equals(user.getPassword())) {
            user.setEmail(email);
            user.setPassword(newpsw);
            result = userDAO.updateByPrimaryKey(user);
            return result;
        } else {
            throw new PasswordError();
        }
    }

    /**
	 * @param host
	 *            the host to set
	 */
    public void setHost(String host) {
        this.host = host;
    }

    /**
	 * @param username
	 *            the username to set
	 */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
	 * @param password
	 *            the password to set
	 */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
	 * @param userDAO
	 *            the userDAO to set
	 */
    public void setUserDAO(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    /**
	 * @param mailSender
	 *            the mailSender to set
	 */
    public void setMailSender(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }
}
