package com.akcess.impl;

import java.math.*;
import java.sql.*;
import java.net.URL;
import java.util.*;
import com.akcess.exception.*;
import com.akcess.vo.*;
import com.akcess.dao.*;

/**
* 
* Implementation of Atributos_recursosDAO interface 
* 
*/
public class Atributos_recursosDAOImpl implements Atributos_recursosDAO {

    /**
* Method deletes a record from table ATRIBUTOS_RECURSOS
* @param Atributos_recursosPK atributos_recursospk
* @param  Connection  con
* @return  int
*
*/
    public int delete(Atributos_recursosPK atributos_recursospk, Connection con) throws Atributos_recursosException {
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement("delete from  ATRIBUTOS_RECURSOS where id_atributos = ?");
            ps.setInt(1, atributos_recursospk.getId_atributos());
            return (ps.executeUpdate());
        } catch (SQLException sqle) {
            throw new Atributos_recursosException(sqle);
        } catch (Exception e) {
            throw new Atributos_recursosException(e);
        }
    }

    /**
* This method updates a record in table ATRIBUTOS_RECURSOS
* @param Atributos_recursosPK
* @param Atributos_recursos
* @param  Connection con
* @return   int
*/
    public int update(Atributos_recursosPK atributos_recursospk, Atributos_recursos atributos_recursos, Connection con) throws Atributos_recursosException {
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement("update ATRIBUTOS_RECURSOS set NOMBRE = ? , DESCRIPCION = ?  where id_atributos = ?");
            ps.setString(1, atributos_recursos.getNombre());
            ps.setString(2, atributos_recursos.getDescripcion());
            ps.setInt(3, atributos_recursospk.getId_atributos());
            return (ps.executeUpdate());
        } catch (SQLException sqle) {
            throw new Atributos_recursosException(sqle);
        } catch (Exception e) {
            throw new Atributos_recursosException(e);
        }
    }

    /**
* This method inserts data in table ATRIBUTOS_RECURSOS
*
* @param Atributos_recursos atributos_recursos
* @param   Connection con
* @return  Atributos_recursosPK
*/
    public int insert(Atributos_recursos atributos_recursos, Connection con) throws Atributos_recursosException {
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement("insert into ATRIBUTOS_RECURSOS( NOMBRE, DESCRIPCION) values (?, ?)");
            ps.setString(1, atributos_recursos.getNombre());
            ps.setString(2, atributos_recursos.getDescripcion());
            return (ps.executeUpdate());
        } catch (SQLException sqle) {
            throw new Atributos_recursosException(sqle);
        } catch (Exception e) {
            throw new Atributos_recursosException(e);
        }
    }

    /**
* 
* Returns a row from the atributos_recursos table for the primary key passed as parameter.
* 
*/
    public Atributos_recursos findByPrimaryKey(int id_atributos, Connection con) throws Atributos_recursosException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            final String SQLSTATEMENT = "Select id_atributos, nombre, descripcion from atributos_recursos where id_atributos = ?";
            stmt = con.prepareStatement(SQLSTATEMENT);
            stmt.setInt(1, id_atributos);
            rs = stmt.executeQuery();
            return fetchSingleResult(rs);
        } catch (SQLException sqle) {
            throw new Atributos_recursosException(sqle);
        } catch (Exception e) {
            throw new Atributos_recursosException(e);
        } finally {
        }
    }

    /**
* 
* Returns a row from the atributos_recursos table for the primary key object passed as parameter.
* 
* @param  Atributos_recursosPK atributos_recursospk
* @param Connection con
* @return  Atributos_recursos
*/
    public Atributos_recursos findByPrimaryKey(Atributos_recursosPK atributos_recursospk, Connection con) throws Atributos_recursosException {
        return findByPrimaryKey(atributos_recursospk.getId_atributos(), con);
    }

    /**
*
* Returns all rows from atributos_recursos table where ID_ATRIBUTOS= id_atributos
*
* @param   int  id_atributos
* @param   Connection con
* @return  Atributos_recursos[]
*/
    public Atributos_recursos[] findById_atributos(int id_atributos, Connection con) throws Atributos_recursosException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String SQL_STATEMENT = "Select id_atributos, nombre, descripcion from atributos_recursos where id_atributos = ? order by id_atributos";
        try {
            stmt = con.prepareStatement(SQL_STATEMENT);
            stmt.setInt(1, id_atributos);
            rs = stmt.executeQuery();
            return fetchMultiResults(rs);
        } catch (SQLException sqle) {
            throw new Atributos_recursosException(sqle);
        } catch (Exception e) {
            throw new Atributos_recursosException(e);
        } finally {
        }
    }

    /**
*
* Returns all rows from atributos_recursos table where NOMBRE= nombre
*
* @param   String  nombre
* @param   Connection con
* @return  Atributos_recursos[]
*/
    public Atributos_recursos[] findByNombre(String nombre, Connection con) throws Atributos_recursosException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String SQL_STATEMENT = "Select id_atributos, nombre, descripcion from atributos_recursos where nombre = ? order by nombre";
        try {
            stmt = con.prepareStatement(SQL_STATEMENT);
            stmt.setString(1, nombre);
            rs = stmt.executeQuery();
            return fetchMultiResults(rs);
        } catch (SQLException sqle) {
            throw new Atributos_recursosException(sqle);
        } catch (Exception e) {
            throw new Atributos_recursosException(e);
        } finally {
        }
    }

    /**
*
* Returns all rows from atributos_recursos table where DESCRIPCION= descripcion
*
* @param   String  descripcion
* @param   Connection con
* @return  Atributos_recursos[]
*/
    public Atributos_recursos[] findByDescripcion(String descripcion, Connection con) throws Atributos_recursosException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String SQL_STATEMENT = "Select id_atributos, nombre, descripcion from atributos_recursos where descripcion = ? order by descripcion";
        try {
            stmt = con.prepareStatement(SQL_STATEMENT);
            stmt.setString(1, descripcion);
            rs = stmt.executeQuery();
            return fetchMultiResults(rs);
        } catch (SQLException sqle) {
            throw new Atributos_recursosException(sqle);
        } catch (Exception e) {
            throw new Atributos_recursosException(e);
        } finally {
        }
    }

    /**
* Returns all rows from atributos_recursos table 
*
* @param Connection con
* @return  Atributos_recursos[]
*
*/
    public Atributos_recursos[] findAll(Connection con) throws Atributos_recursosException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String SQL_STATEMENT = "Select id_atributos, nombre, descripcion from atributos_recursos";
        try {
            stmt = con.prepareStatement(SQL_STATEMENT);
            rs = stmt.executeQuery();
            return fetchMultiResults(rs);
        } catch (SQLException sqle) {
            throw new Atributos_recursosException(sqle);
        } catch (Exception e) {
            throw new Atributos_recursosException(e);
        } finally {
        }
    }

    /**
* Returns rows from atributos_recursos table by executing the passed sql statement
* after setting the passed values in Object[]
*
* @param String selectStatement
* @param Object[] sqlParams
* @param Connection con
* @return  Atributos_recursos[]
*
*/
    public Atributos_recursos[] findExecutingUserSelect(String selectStatement, Object[] sqlParams, Connection con) throws Atributos_recursosException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        final String SQL_STATEMENT = selectStatement;
        try {
            stmt = con.prepareStatement(SQL_STATEMENT);
            for (int i = 0; i < sqlParams.length; i++) {
                stmt.setObject(i + 1, sqlParams[i]);
            }
            rs = stmt.executeQuery();
            return fetchMultiResults(rs);
        } catch (SQLException sqle) {
            throw new Atributos_recursosException(sqle);
        } catch (Exception e) {
            throw new Atributos_recursosException(e);
        } finally {
        }
    }

    /**
* Returns rows from atributos_recursos table by executing the select all fields statement
* after setting the passed where clause and values in Object[]
*
* @param String whereClause
* @param Object[] sqlParams
* @param Connection con
* @return  Atributos_recursos[]
*
*/
    public Atributos_recursos[] findExecutingUserWhere(String whereClause, Object[] sqlParams, Connection con) throws Atributos_recursosException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String SQL_SELECT = "Select id_atributos, nombre, descripcion from atributos_recursos";
        final String SQL_STATEMENT = SQL_SELECT + " where " + whereClause;
        try {
            stmt = con.prepareStatement(SQL_STATEMENT);
            for (int i = 0; i < sqlParams.length; i++) {
                stmt.setObject(i + 1, sqlParams[i]);
            }
            rs = stmt.executeQuery();
            return fetchMultiResults(rs);
        } catch (SQLException sqle) {
            throw new Atributos_recursosException(sqle);
        } catch (Exception e) {
            throw new Atributos_recursosException(e);
        } finally {
        }
    }

    /**
*
* Populates a Data Transfer Object by fetching single record from resultSet 
*
* @param ResultSet rs
* @return  Atributos_recursos
*
*/
    protected Atributos_recursos fetchSingleResult(ResultSet rs) throws SQLException {
        if (rs.next()) {
            Atributos_recursos dto = new Atributos_recursos();
            populateVO(dto, rs);
            return dto;
        } else {
            return null;
        }
    }

    /**
* 
* Populates a Data Transfer Object by fetching data from  ResultSet
* 
* @param Atributos_recursos dto
* @param   ResultSet rs
* @return  void
*/
    protected void populateVO(Atributos_recursos dto, ResultSet rs) throws SQLException {
        dto.setId_atributos(rs.getInt("id_atributos"));
        dto.setNombre(rs.getString("nombre"));
        dto.setDescripcion(rs.getString("descripcion"));
    }

    /**
* 
* Returns an array of Value Objects by fetching data from resultSet
* 
* @param   ResultSet rs
* @return  Atributos_recursos[]
*/
    protected Atributos_recursos[] fetchMultiResults(ResultSet rs) throws SQLException {
        Collection resultList = new ArrayList();
        while (rs.next()) {
            Atributos_recursos dto = new Atributos_recursos();
            populateVO(dto, rs);
            resultList.add(dto);
        }
        Atributos_recursos ret[] = new Atributos_recursos[resultList.size()];
        resultList.toArray(ret);
        return ret;
    }

    /**
         * Es un metodo  utilizado para recuperar los atributos de un tipo de recurso dado.
         * @param idTipo ID del tipo de recurso que queremos recuperar los atributos.
         * @param con Conexion a la base de datos.
         * @return Un vector de Atributos pertenecientes al tipo especificado.
         * @throws com.akcess.exception.Atributos_recursosException
         */
    public Atributos_recursos[] BuscarAtributosDeTipo(int idTipo, Connection con) throws Atributos_recursosException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String SQL_STATEMENT = "select Atributos_recursos.id_Atributos, Atributos_recursos.Nombre, Atributos_recursos.descripcion from Tipo_tiene_atributos, Atributos_recursos where Tipo_tiene_atributos.id_tipo_recurso= ? and  Atributos_recursos.id_Atributos=Tipo_tiene_atributos.id_Atributos";
        try {
            stmt = con.prepareStatement(SQL_STATEMENT);
            stmt.setInt(1, idTipo);
            rs = stmt.executeQuery();
            return fetchMultiResults(rs);
        } catch (SQLException sqle) {
            throw new Atributos_recursosException(sqle);
        } catch (Exception e) {
            throw new Atributos_recursosException(e);
        } finally {
        }
    }
}
