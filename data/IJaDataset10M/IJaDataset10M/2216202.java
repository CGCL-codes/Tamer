package kr.ac.jejuuniv;

import java.util.List;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class User {

    private String id;

    private String name;

    private String password;

    private String prefix;

    private IUserRepository userRepository;

    public User() {
        init();
    }

    public void init() {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("root-context.xml");
        userRepository = (IUserRepository) applicationContext.getBean("userRepository");
    }

    public User(String prefix) {
        this.prefix = prefix;
        init();
    }

    public List<User> list() {
        List<User> users = userRepository.findAll();
        settingPrefix(users);
        return users;
    }

    public List<User> listOrderByIdDesc() {
        List<User> users = userRepository.findAllOrderByIdDesc();
        settingPrefix(users);
        return users;
    }

    private void settingPrefix(List<User> users) {
        for (User user : users) {
            user.setId(prefix + user.getId());
            user.setName(prefix + user.getName());
            user.setPassword(prefix + user.getPassword());
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
