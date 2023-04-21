package com.makeabyte.jhosting.server.persistence;

import java.util.List;
import com.makeabyte.jhosting.server.persistence.entity.GroupEntity;
import com.makeabyte.jhosting.server.persistence.entity.RoleEntity;
import com.makeabyte.jhosting.server.persistence.entity.AccountEntity;
import com.makeabyte.jhosting.server.persistence.ldap.LdapLookupException;

public interface RoleDAO {

    public void persist(RoleEntity role) throws DataAccessException;

    public void merge(RoleEntity role) throws DataAccessException;

    public void remove(RoleEntity role) throws DataAccessException;

    public boolean exists(RoleEntity entity) throws DataAccessException;

    public RoleEntity get(String id) throws LdapLookupException;

    public String getId(String name) throws LdapLookupException;

    public List<RoleEntity> getRoles() throws LdapLookupException;

    public List<RoleEntity> getRolesForUser(AccountEntity user) throws LdapLookupException;

    public List<RoleEntity> getRolesForGroup(GroupEntity group) throws LdapLookupException;

    public void addGroup(GroupEntity group, RoleEntity role) throws DataAccessException;

    public void removeGroup(GroupEntity group, RoleEntity role) throws DataAccessException;

    public void addUser(AccountEntity user, RoleEntity plugin) throws DataAccessException;

    public void removeUser(AccountEntity user, RoleEntity plugin) throws DataAccessException;
}
