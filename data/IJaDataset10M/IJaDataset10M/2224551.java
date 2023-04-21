package be.hoornaert.tom.spring.exercise6;

import java.util.List;

public interface UserDAO {

    public void save(User user);

    public User get(Long id);

    public void delete(User user);

    public List<User> getAll();

    public List<User> findByFirstLetterOfLastName(char letter);
}
