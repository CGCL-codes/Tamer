package pl.edu.agh.uddiProxy.dao;

import java.util.List;
import pl.edu.agh.uddiProxy.types.Authority;
import pl.edu.agh.uddiProxy.types.User;

public interface UserDAO {

    void create(User user);

    void update(User user);

    void delete(User user);

    boolean exists(String login);

    boolean exists(Long id);

    List<User> getAll();

    List<Long> getAllIds();

    User getById(Long id);

    User loadUserByUsername(String login);

    Authority getAuthority(String authority);
}
