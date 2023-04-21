package com.akcess.dao;

import java.math.*;
import java.sql.*;
import java.net.URL;
import java.util.*;
import com.akcess.exception.*;
import com.akcess.vo.*;

public interface UsersDAO {

    public int insert(Users users, Connection con) throws UsersException;

    public int update(UsersPK userspk, Users users, Connection con) throws UsersException;

    public int delete(UsersPK userspk, Connection con) throws UsersException;

    public Users findByPrimaryKey(UsersPK userspk, Connection con) throws UsersException;

    public Users[] findAll(Connection con) throws UsersException;

    public Users[] findByName(String name, Connection con) throws UsersException;

    public Users[] findByJcu_username(String jcu_username, Connection con) throws UsersException;

    public Users[] findByJcu_password(String jcu_password, Connection con) throws UsersException;

    public Users[] findExecutingUserSelect(String selectStatement, Object[] sqlParams, Connection con) throws UsersException;

    public Users[] findExecutingUserWhere(String whereClause, Object[] sqlParams, Connection con) throws UsersException;
}
