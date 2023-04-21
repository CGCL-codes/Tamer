package com.cusp.pt.service.model;

import java.util.List;
import com.cusp.pt.model.security.Credentials;
import com.cusp.pt.model.security.User;
import com.testapp.helper.Client;

public interface UserService {

    public User getUserByCredentials(Credentials credentials);

    public User getUserByUserName(String userName);

    public List<User> getUsersForClient(Client client);

    public void createUser(User user);

    public void updateUser(User user);
}
