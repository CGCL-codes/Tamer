package com.inature.oce.persistence.api;

import java.util.List;
import com.inature.oce.persistence.model.UserNodeRoleRefDTO;
import com.inature.oce.persistence.model.UserNodeRoleRefDTOE;
import com.inature.oce.persistence.spi.PersistenceFactory;

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
 * Provides methods for CRUD operations on user roles store.
 * 
 * @author Yavor Mitkov Gologanov
 *
 */
public class UserRoleStore {

    private PersistenceFactory factory = null;

    /**
	 * 
	 * @param persistenceFactory
	 */
    public UserRoleStore(PersistenceFactory persistenceFactory) {
        this.factory = persistenceFactory;
    }

    /**
	 * Retues a list that contains all gobal user roles.
	 * 
	 * @param userId
	 * @return List<String>
	 * @throws PersistenceException
	 */
    public List<String> getUserRoles(String userId) throws PersistenceException {
        return factory.getUserRoleRefDAM().getRoles(userId);
    }

    /**
	 * Adds a new relation between an user, a role and a permission.
	 * 
	 * @param refDTO
	 * @return String
	 * @throws PersistenceException
	 */
    public String addUserNodeRole(UserNodeRoleRefDTO refDTO) throws PersistenceException {
        return factory.getUserNodeRoleRefDAM().insert(refDTO);
    }

    /**
	 * Removes a relation between an user, a role and a permission by a given unique id.	 
	 * 
	 * @param refId
	 * @throws PersistenceException
	 */
    public int removeUserNodeRole(String refId) throws PersistenceException {
        return factory.getUserNodeRoleRefDAM().delete(refId);
    }

    /**
	 * Returns a list that contains all relations betweens users and roles for a given user.
	 * 
	 * @param nodeId
	 * @return List<UserNodeRoleRefDTOE>
	 * @throws PersistenceException
	 */
    public List<UserNodeRoleRefDTOE> getUserNodeRoles(String nodeId) throws PersistenceException {
        return factory.getUserNodeRoleRefDAM().getRefs(nodeId);
    }

    /**
	 *  Returns a relation id or null if the given relation does not exists.
	 * 
	 * @param refDTO
	 * @return String
	 * @throws PersistenceException
	 */
    public String getUserNodeRoleRefId(UserNodeRoleRefDTO refDTO) throws PersistenceException {
        return factory.getUserNodeRoleRefDAM().getRefId(refDTO);
    }
}
