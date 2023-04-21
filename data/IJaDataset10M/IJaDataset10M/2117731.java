package mapper;

import bean.User;

/**
 * @author uncleja
 * @since 2011-11-19 下午11:51:50
 * @comment
 */
public interface UserMapper {

    public void insertUser(User user);

    public User getUser(String name);
}
