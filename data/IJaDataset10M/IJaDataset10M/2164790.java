package kr.ac.jejuuniv;

import java.util.List;

public interface IUserRepository {

    public abstract List<User> findAll();

    public abstract List<User> findAllOrderByIdDesc();
}
