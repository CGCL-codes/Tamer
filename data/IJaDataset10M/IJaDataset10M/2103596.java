package net.opensesam.user;

import java.io.Serializable;
import java.util.List;
import net.opensesam.entity.Resource;
import net.opensesam.entity.User;

public interface UserBo extends Serializable {

    void add(User user);

    void delete(User user);

    void update(User user);

    List<User> findAll();

    List<User> findAll(String searchPhrase, int resultPage, int resultLimit, String sortOrder, String sortColumnName);

    long findAllSize(String searchPhrase);

    List<User> findAllAdmins();

    List<User> findAllNonAdmins();

    User findById(int id);

    List<User> findByLogin(String login);

    List<Resource> getAllResources();
}
