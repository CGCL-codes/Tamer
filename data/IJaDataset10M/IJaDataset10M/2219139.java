package hu.bme.aait.picstore.um;

import hu.bme.aait.picstore.entities.User;
import hu.bme.aait.picstore.exceptions.AlreadyRegisteredException;
import hu.bme.aait.picstore.exceptions.NoSuchGroupException;
import hu.bme.aait.picstore.exceptions.WrongPasswordException;
import javax.ejb.Local;

@Local
public interface UserManagerLocal {

    User register(String emailAddress, String password, String fullName) throws NoSuchGroupException, AlreadyRegisteredException;

    void modify(User user, String currentPassword, String password) throws WrongPasswordException;

    void promoteAdmin(User user);

    User getUser(String email);
}
