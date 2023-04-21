package dao;

import struts.form.UserForm;
import bean.User;
import java.util.*;

public interface IUserDao {

    public boolean checkUser(String userName);

    public void delete(int id);

    public User findById(int id);

    public void register(UserForm userForm);

    public void update(User user);

    public User findByUserName(String userName);
}
