package es.viewerfree.gwt.server.service;

import java.util.List;
import es.viewerfree.gwt.shared.dto.UserDto;
import es.viewerfree.gwt.shared.service.ServiceException;

public interface IUserService {

    public UserDto getCredentials(String user, String password) throws ServiceException;

    public UserDto getUser(String user) throws ServiceException;

    public UserDto getUser(Long id) throws ServiceException;

    public void createUser(UserDto userDto) throws ServiceException;

    public List<UserDto> getAllUsers() throws ServiceException;

    public void modifyUser(UserDto userDto) throws ServiceException;
}
