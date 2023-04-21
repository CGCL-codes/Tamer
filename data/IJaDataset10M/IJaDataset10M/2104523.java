package com.akcess.dao;

import java.math.*;
import java.sql.*;
import java.net.URL;
import java.util.*;
import com.akcess.exception.*;
import com.akcess.vo.*;

public interface Tiene_soDAO {

    public int insert(Tiene_so tiene_so, Connection con) throws Tiene_soException;

    public int delete(Tiene_soPK tiene_sopk, Connection con) throws Tiene_soException;

    public Tiene_so findByPrimaryKey(Tiene_soPK tiene_sopk, Connection con) throws Tiene_soException;

    public Tiene_so[] findAll(Connection con) throws Tiene_soException;

    public Tiene_so[] findById_so(int id_so, Connection con) throws Tiene_soException;

    public Tiene_so[] findById_sala(long id_sala, Connection con) throws Tiene_soException;

    public Tiene_so[] findExecutingUserSelect(String selectStatement, Object[] sqlParams, Connection con) throws Tiene_soException;

    public Tiene_so[] findExecutingUserWhere(String whereClause, Object[] sqlParams, Connection con) throws Tiene_soException;
}
