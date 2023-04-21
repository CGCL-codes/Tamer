package com.akcess.dao;

import java.math.*;
import java.sql.*;
import java.net.URL;
import java.util.*;
import java.sql.Date;
import com.akcess.exception.*;
import com.akcess.vo.*;

public interface RecursoDAO {

    public int insert(Recurso recurso, Connection con) throws RecursoException;

    public int update(RecursoPK recursopk, Recurso recurso, Connection con) throws RecursoException;

    public int delete(RecursoPK recursopk, Connection con) throws RecursoException;

    public Recurso findByPrimaryKey(RecursoPK recursopk, Connection con) throws RecursoException;

    public Recurso[] findAll(Connection con) throws RecursoException;

    public Recurso[] findById_recurso(int id_recurso, Connection con) throws RecursoException;

    public Recurso[] findByDisponible(boolean disponible, Connection con) throws RecursoException;

    public Recurso[] findByObservacion(String observacion, Connection con) throws RecursoException;

    public Recurso[] findByBorrado(boolean borrado, Connection con) throws RecursoException;

    public Recurso[] findByFechacreacion(Date fechacreacion, Connection con) throws RecursoException;

    public Recurso[] findExecutingUserSelect(String selectStatement, Object[] sqlParams, Connection con) throws RecursoException;

    public Recurso[] findExecutingUserWhere(String whereClause, Object[] sqlParams, Connection con) throws RecursoException;
}
