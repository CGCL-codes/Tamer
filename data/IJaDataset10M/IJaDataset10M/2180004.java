package com.belt.desk.session;

import java.util.ArrayList;
import java.util.List;
import com.belt.desk.entity.Users;

/**
 * @author Tiziano
 *
 */
public interface UsersEdit {

    public String selectUser(Users selectedUser);

    public String newUser();

    public String save();

    public void cancel();

    public void destroy();

    public String update();
}
