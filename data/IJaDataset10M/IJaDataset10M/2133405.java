package com.inature.oce.core.impl;

import java.util.List;
import com.inature.oce.core.api.EUserEntity;
import com.inature.oce.core.api.OCEException;
import com.inature.oce.core.api.ObjectExistsException;
import com.inature.oce.core.api.User;
import com.inature.oce.core.api.UserEntity;
import com.inature.oce.core.api.UserManager;
import com.inature.oce.core.service.ServiceLocator;
import com.inature.oce.facade.UserFacade;
import com.inature.oce.facade.UserRoleFacade;
import com.inature.oce.persistence.api.ObjectAlreadyExistsException;
import com.inature.oce.persistence.api.PersistenceException;
import com.inature.oce.persistence.model.UserDTO;

/**
 * Copyright 2007 i-nature
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * @author Yavor Mitkov Gologanov
 *
 */
public class UserManagerImpl implements UserManager {

    /**
	 * 
	 */
    public EUserEntity createUser() {
        return new UserEntityImpl();
    }

    /**
	 * 
	 */
    public User getUser(String userId) throws OCEException {
        UserFacade userFacade = ServiceLocator.getFacadeService().getUserFacade();
        try {
            UserDTO userDTO = userFacade.getUser(userId);
            UserImpl user = new UserImpl(userDTO);
            UserRoleFacade userRoleFacade = ServiceLocator.getFacadeService().getUserRoleFacade();
            List<String> globalRoles = userRoleFacade.getUserRoles(userId);
            if ((globalRoles != null) && (!globalRoles.isEmpty())) {
                user.addGlobalRoles(globalRoles);
            }
            return user;
        } catch (PersistenceException e) {
            throw new OCEException(e);
        }
    }

    /**
	 * 
	 * @param userId
	 * @return
	 * @throws OCEException
	 */
    public EUserEntity getEditableUser(String userId) throws OCEException {
        UserFacade facade = ServiceLocator.getFacadeService().getUserFacade();
        try {
            UserDTO userDTO = facade.getUser(userId);
            return new UserEntityImpl(userDTO);
        } catch (PersistenceException e) {
            throw new OCEException(e);
        }
    }

    /**
	 * 
	 */
    public String saveUser(EUserEntity user) throws OCEException, ObjectExistsException {
        UserEntityImpl userEntityImpl = (UserEntityImpl) user;
        UserFacade facade = ServiceLocator.getFacadeService().getUserFacade();
        try {
            String userId = facade.save(userEntityImpl.getUserDTO());
            return userId;
        } catch (ObjectAlreadyExistsException e) {
            throw new ObjectExistsException(e);
        } catch (PersistenceException e) {
            throw new OCEException(e);
        }
    }

    /**
	 * 
	 */
    public int updateUser(EUserEntity user) throws OCEException, ObjectExistsException {
        UserEntityImpl userEntityImpl = (UserEntityImpl) user;
        try {
            UserFacade facade = ServiceLocator.getFacadeService().getUserFacade();
            UserDTO userDTO = userEntityImpl.getUserDTO();
            int updated = facade.update(userDTO);
            return updated;
        } catch (ObjectAlreadyExistsException e) {
            throw new ObjectExistsException(e);
        } catch (PersistenceException e) {
            throw new OCEException(e);
        }
    }

    /**
	 * 
	 */
    public List<String> getUserNames() throws OCEException {
        UserFacade facade = ServiceLocator.getFacadeService().getUserFacade();
        try {
            return facade.getUserNames();
        } catch (PersistenceException e) {
            throw new OCEException(e);
        }
    }

    /**
	 * 
	 */
    public String getUserId(String userName) throws OCEException {
        UserFacade facade = ServiceLocator.getFacadeService().getUserFacade();
        try {
            return facade.getUserId(userName);
        } catch (PersistenceException e) {
            throw new OCEException(e);
        }
    }

    /**
	 * 
	 */
    public boolean isValidPassword(String userId, String password) throws OCEException {
        UserFacade facade = ServiceLocator.getFacadeService().getUserFacade();
        try {
            return facade.isValidPassword(userId, password);
        } catch (PersistenceException e) {
            throw new OCEException(e);
        }
    }

    /**
	 * 
	 */
    public void changePassword(String userId, String password) throws OCEException {
        UserFacade facade = ServiceLocator.getFacadeService().getUserFacade();
        try {
            facade.changePassword(userId, password);
        } catch (PersistenceException e) {
            throw new OCEException(e);
        }
    }

    /**
	 * 
	 */
    public UserEntity getUserByEmail(String email) throws OCEException {
        UserFacade facade = ServiceLocator.getFacadeService().getUserFacade();
        try {
            UserDTO userDTO = facade.getUserByEmail(email);
            if (userDTO != null) {
                return new UserEntityImpl(userDTO);
            }
            return null;
        } catch (PersistenceException e) {
            throw new OCEException(e);
        }
    }
}
