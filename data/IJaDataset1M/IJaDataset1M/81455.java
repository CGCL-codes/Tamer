package br.com.yaw.service;

import br.com.yaw.entity.User;
import br.com.yaw.exception.ServiceException;

public interface EQtalMailService {

    void sendCreateUser(User user) throws ServiceException;
}
