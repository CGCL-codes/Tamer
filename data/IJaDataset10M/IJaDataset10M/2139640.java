package com.akcess.impl;

import java.math.*;
import java.sql.*;
import java.net.URL;
import java.util.*;
import java.sql.Date;
import com.akcess.exception.*;
import com.akcess.vo.*;
import com.akcess.dao.*;

/**
* 
* Implementation of Log_reservasDAO interface 
* 
*/
public class Log_reservasDAOImpl implements Log_reservasDAO {

    /**
* Method deletes a record from table LOG_RESERVAS
* @param Log_reservasPK log_reservaspk
* @param  Connection  con
* @return  int
*
*/
    public int delete(Log_reservasPK log_reservaspk, Connection con) throws Log_reservasException {
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement("delete from  LOG_RESERVAS where id_log_reserv = ?");
            ps.setInt(1, log_reservaspk.getId_log_reserv());
            return (ps.executeUpdate());
        } catch (SQLException sqle) {
            throw new Log_reservasException(sqle);
        } catch (Exception e) {
            throw new Log_reservasException(e);
        }
    }

    /**
* This method updates a record in table LOG_RESERVAS
* @param Log_reservasPK
* @param Log_reservas
* @param  Connection con
* @return   int
*/
    public int update(Log_reservasPK log_reservaspk, Log_reservas log_reservas, Connection con) throws Log_reservasException {
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement("update LOG_RESERVAS set ID_RESERVA = ? , ID_OPERADOR = ? , FECHA = ? , HORA = ? , OPERACION = ? , ID_SOLICITANTE = ? , ID_RECURSO = ? , ID_UBICACION = ? , CANCELADA = ? , HORAINICIO_ASIGNADA = ? , HORAFIN_ASIGNADA = ? , FECHA_CREACION = ? , FECHA_RESERVA = ? , NOMBRE_RESPONSABLE = ? , C_I_RESPONSABLE = ?  where id_log_reserv = ?");
            ps.setLong(1, log_reservas.getId_reserva());
            ps.setInt(2, log_reservas.getId_operador());
            ps.setDate(3, log_reservas.getFecha());
            ps.setTime(4, log_reservas.getHora());
            ps.setString(5, log_reservas.getOperacion());
            ps.setInt(6, log_reservas.getId_solicitante());
            ps.setLong(7, log_reservas.getId_recurso());
            ps.setInt(8, log_reservas.getId_ubicacion());
            ps.setBoolean(9, log_reservas.getCancelada());
            ps.setTime(10, log_reservas.getHorainicio_asignada());
            ps.setTime(11, log_reservas.getHorafin_asignada());
            ps.setDate(12, log_reservas.getFecha_creacion());
            ps.setDate(13, log_reservas.getFecha_reserva());
            ps.setString(14, log_reservas.getNombre_responsable());
            ps.setString(15, log_reservas.getC_i_responsable());
            ps.setInt(16, log_reservaspk.getId_log_reserv());
            return (ps.executeUpdate());
        } catch (SQLException sqle) {
            throw new Log_reservasException(sqle);
        } catch (Exception e) {
            throw new Log_reservasException(e);
        }
    }

    /**
* This method inserts data in table LOG_RESERVAS
*
* @param Log_reservas log_reservas
* @param   Connection con
* @return  Log_reservasPK
*/
    public int insert(Log_reservas log_reservas, Connection con) throws Log_reservasException {
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement("insert into LOG_RESERVAS( ID_RESERVA, ID_OPERADOR, FECHA, HORA, OPERACION, ID_SOLICITANTE, ID_RECURSO, ID_UBICACION, CANCELADA, HORAINICIO_ASIGNADA, HORAFIN_ASIGNADA, FECHA_CREACION, FECHA_RESERVA, NOMBRE_RESPONSABLE, C_I_RESPONSABLE) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            ps.setLong(1, log_reservas.getId_reserva());
            ps.setInt(2, log_reservas.getId_operador());
            ps.setDate(3, log_reservas.getFecha());
            ps.setTime(4, log_reservas.getHora());
            ps.setString(5, log_reservas.getOperacion());
            ps.setInt(6, log_reservas.getId_solicitante());
            ps.setLong(7, log_reservas.getId_recurso());
            ps.setInt(8, log_reservas.getId_ubicacion());
            ps.setBoolean(9, log_reservas.getCancelada());
            ps.setTime(10, log_reservas.getHorainicio_asignada());
            ps.setTime(11, log_reservas.getHorafin_asignada());
            ps.setDate(12, log_reservas.getFecha_creacion());
            ps.setDate(13, log_reservas.getFecha_reserva());
            ps.setString(14, log_reservas.getNombre_responsable());
            ps.setString(15, log_reservas.getC_i_responsable());
            return (ps.executeUpdate());
        } catch (SQLException sqle) {
            throw new Log_reservasException(sqle);
        } catch (Exception e) {
            throw new Log_reservasException(e);
        }
    }

    /**
* 
* Returns a row from the log_reservas table for the primary key passed as parameter.
* 
*/
    public Log_reservas findByPrimaryKey(int id_log_reserv, Connection con) throws Log_reservasException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            final String SQLSTATEMENT = "Select id_log_reserv, id_reserva, id_operador, fecha, hora, operacion, id_solicitante, id_recurso, id_ubicacion, cancelada, horainicio_asignada, horafin_asignada, fecha_creacion, fecha_reserva, nombre_responsable, c_i_responsable from log_reservas where id_log_reserv = ?";
            stmt = con.prepareStatement(SQLSTATEMENT);
            stmt.setInt(1, id_log_reserv);
            rs = stmt.executeQuery();
            return fetchSingleResult(rs);
        } catch (SQLException sqle) {
            throw new Log_reservasException(sqle);
        } catch (Exception e) {
            throw new Log_reservasException(e);
        } finally {
        }
    }

    /**
* 
* Returns a row from the log_reservas table for the primary key object passed as parameter.
* 
* @param  Log_reservasPK log_reservaspk
* @param Connection con
* @return  Log_reservas
*/
    public Log_reservas findByPrimaryKey(Log_reservasPK log_reservaspk, Connection con) throws Log_reservasException {
        return findByPrimaryKey(log_reservaspk.getId_log_reserv(), con);
    }

    /**
*
* Returns all rows from log_reservas table where ID_LOG_RESERV= id_log_reserv
*
* @param   int  id_log_reserv
* @param   Connection con
* @return  Log_reservas[]
*/
    public Log_reservas[] findById_log_reserv(int id_log_reserv, Connection con) throws Log_reservasException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String SQL_STATEMENT = "Select id_log_reserv, id_reserva, id_operador, fecha, hora, operacion, id_solicitante, id_recurso, id_ubicacion, cancelada, horainicio_asignada, horafin_asignada, fecha_creacion, fecha_reserva, nombre_responsable, c_i_responsable from log_reservas where id_log_reserv = ? order by id_log_reserv";
        try {
            stmt = con.prepareStatement(SQL_STATEMENT);
            stmt.setInt(1, id_log_reserv);
            rs = stmt.executeQuery();
            return fetchMultiResults(rs);
        } catch (SQLException sqle) {
            throw new Log_reservasException(sqle);
        } catch (Exception e) {
            throw new Log_reservasException(e);
        } finally {
        }
    }

    /**
*
* Returns all rows from log_reservas table where ID_RESERVA= id_reserva
*
* @param   long  id_reserva
* @param   Connection con
* @return  Log_reservas[]
*/
    public Log_reservas[] findById_reserva(long id_reserva, Connection con) throws Log_reservasException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String SQL_STATEMENT = "Select id_log_reserv, id_reserva, id_operador, fecha, hora, operacion, id_solicitante, id_recurso, id_ubicacion, cancelada, horainicio_asignada, horafin_asignada, fecha_creacion, fecha_reserva, nombre_responsable, c_i_responsable from log_reservas where id_reserva = ? order by id_reserva";
        try {
            stmt = con.prepareStatement(SQL_STATEMENT);
            stmt.setLong(1, id_reserva);
            rs = stmt.executeQuery();
            return fetchMultiResults(rs);
        } catch (SQLException sqle) {
            throw new Log_reservasException(sqle);
        } catch (Exception e) {
            throw new Log_reservasException(e);
        } finally {
        }
    }

    /**
*
* Returns all rows from log_reservas table where ID_OPERADOR= id_operador
*
* @param   int  id_operador
* @param   Connection con
* @return  Log_reservas[]
*/
    public Log_reservas[] findById_operador(int id_operador, Connection con) throws Log_reservasException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String SQL_STATEMENT = "Select id_log_reserv, id_reserva, id_operador, fecha, hora, operacion, id_solicitante, id_recurso, id_ubicacion, cancelada, horainicio_asignada, horafin_asignada, fecha_creacion, fecha_reserva, nombre_responsable, c_i_responsable from log_reservas where id_operador = ? order by id_operador";
        try {
            stmt = con.prepareStatement(SQL_STATEMENT);
            stmt.setInt(1, id_operador);
            rs = stmt.executeQuery();
            return fetchMultiResults(rs);
        } catch (SQLException sqle) {
            throw new Log_reservasException(sqle);
        } catch (Exception e) {
            throw new Log_reservasException(e);
        } finally {
        }
    }

    /**
*
* Returns all rows from log_reservas table where FECHA= fecha
*
* @param   Date  fecha
* @param   Connection con
* @return  Log_reservas[]
*/
    public Log_reservas[] findByFecha(Date fecha, Connection con) throws Log_reservasException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String SQL_STATEMENT = "Select id_log_reserv, id_reserva, id_operador, fecha, hora, operacion, id_solicitante, id_recurso, id_ubicacion, cancelada, horainicio_asignada, horafin_asignada, fecha_creacion, fecha_reserva, nombre_responsable, c_i_responsable from log_reservas where fecha = ? order by fecha";
        try {
            stmt = con.prepareStatement(SQL_STATEMENT);
            stmt.setDate(1, fecha);
            rs = stmt.executeQuery();
            return fetchMultiResults(rs);
        } catch (SQLException sqle) {
            throw new Log_reservasException(sqle);
        } catch (Exception e) {
            throw new Log_reservasException(e);
        } finally {
        }
    }

    /**
*
* Returns all rows from log_reservas table where HORA= hora
*
* @param   Time  hora
* @param   Connection con
* @return  Log_reservas[]
*/
    public Log_reservas[] findByHora(Time hora, Connection con) throws Log_reservasException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String SQL_STATEMENT = "Select id_log_reserv, id_reserva, id_operador, fecha, hora, operacion, id_solicitante, id_recurso, id_ubicacion, cancelada, horainicio_asignada, horafin_asignada, fecha_creacion, fecha_reserva, nombre_responsable, c_i_responsable from log_reservas where hora = ? order by hora";
        try {
            stmt = con.prepareStatement(SQL_STATEMENT);
            stmt.setTime(1, hora);
            rs = stmt.executeQuery();
            return fetchMultiResults(rs);
        } catch (SQLException sqle) {
            throw new Log_reservasException(sqle);
        } catch (Exception e) {
            throw new Log_reservasException(e);
        } finally {
        }
    }

    /**
*
* Returns all rows from log_reservas table where OPERACION= operacion
*
* @param   String  operacion
* @param   Connection con
* @return  Log_reservas[]
*/
    public Log_reservas[] findByOperacion(String operacion, Connection con) throws Log_reservasException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String SQL_STATEMENT = "Select id_log_reserv, id_reserva, id_operador, fecha, hora, operacion, id_solicitante, id_recurso, id_ubicacion, cancelada, horainicio_asignada, horafin_asignada, fecha_creacion, fecha_reserva, nombre_responsable, c_i_responsable from log_reservas where operacion = ? order by operacion";
        try {
            stmt = con.prepareStatement(SQL_STATEMENT);
            stmt.setString(1, operacion);
            rs = stmt.executeQuery();
            return fetchMultiResults(rs);
        } catch (SQLException sqle) {
            throw new Log_reservasException(sqle);
        } catch (Exception e) {
            throw new Log_reservasException(e);
        } finally {
        }
    }

    /**
*
* Returns all rows from log_reservas table where ID_SOLICITANTE= id_solicitante
*
* @param   int  id_solicitante
* @param   Connection con
* @return  Log_reservas[]
*/
    public Log_reservas[] findById_solicitante(int id_solicitante, Connection con) throws Log_reservasException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String SQL_STATEMENT = "Select id_log_reserv, id_reserva, id_operador, fecha, hora, operacion, id_solicitante, id_recurso, id_ubicacion, cancelada, horainicio_asignada, horafin_asignada, fecha_creacion, fecha_reserva, nombre_responsable, c_i_responsable from log_reservas where id_solicitante = ? order by id_solicitante";
        try {
            stmt = con.prepareStatement(SQL_STATEMENT);
            stmt.setInt(1, id_solicitante);
            rs = stmt.executeQuery();
            return fetchMultiResults(rs);
        } catch (SQLException sqle) {
            throw new Log_reservasException(sqle);
        } catch (Exception e) {
            throw new Log_reservasException(e);
        } finally {
        }
    }

    /**
*
* Returns all rows from log_reservas table where ID_RECURSO= id_recurso
*
* @param   long  id_recurso
* @param   Connection con
* @return  Log_reservas[]
*/
    public Log_reservas[] findById_recurso(long id_recurso, Connection con) throws Log_reservasException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String SQL_STATEMENT = "Select id_log_reserv, id_reserva, id_operador, fecha, hora, operacion, id_solicitante, id_recurso, id_ubicacion, cancelada, horainicio_asignada, horafin_asignada, fecha_creacion, fecha_reserva, nombre_responsable, c_i_responsable from log_reservas where id_recurso = ? order by id_recurso";
        try {
            stmt = con.prepareStatement(SQL_STATEMENT);
            stmt.setLong(1, id_recurso);
            rs = stmt.executeQuery();
            return fetchMultiResults(rs);
        } catch (SQLException sqle) {
            throw new Log_reservasException(sqle);
        } catch (Exception e) {
            throw new Log_reservasException(e);
        } finally {
        }
    }

    /**
*
* Returns all rows from log_reservas table where ID_UBICACION= id_ubicacion
*
* @param   int  id_ubicacion
* @param   Connection con
* @return  Log_reservas[]
*/
    public Log_reservas[] findById_ubicacion(int id_ubicacion, Connection con) throws Log_reservasException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String SQL_STATEMENT = "Select id_log_reserv, id_reserva, id_operador, fecha, hora, operacion, id_solicitante, id_recurso, id_ubicacion, cancelada, horainicio_asignada, horafin_asignada, fecha_creacion, fecha_reserva, nombre_responsable, c_i_responsable from log_reservas where id_ubicacion = ? order by id_ubicacion";
        try {
            stmt = con.prepareStatement(SQL_STATEMENT);
            stmt.setInt(1, id_ubicacion);
            rs = stmt.executeQuery();
            return fetchMultiResults(rs);
        } catch (SQLException sqle) {
            throw new Log_reservasException(sqle);
        } catch (Exception e) {
            throw new Log_reservasException(e);
        } finally {
        }
    }

    /**
*
* Returns all rows from log_reservas table where CANCELADA= cancelada
*
* @param   boolean  cancelada
* @param   Connection con
* @return  Log_reservas[]
*/
    public Log_reservas[] findByCancelada(boolean cancelada, Connection con) throws Log_reservasException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String SQL_STATEMENT = "Select id_log_reserv, id_reserva, id_operador, fecha, hora, operacion, id_solicitante, id_recurso, id_ubicacion, cancelada, horainicio_asignada, horafin_asignada, fecha_creacion, fecha_reserva, nombre_responsable, c_i_responsable from log_reservas where cancelada = ? order by cancelada";
        try {
            stmt = con.prepareStatement(SQL_STATEMENT);
            stmt.setBoolean(1, cancelada);
            rs = stmt.executeQuery();
            return fetchMultiResults(rs);
        } catch (SQLException sqle) {
            throw new Log_reservasException(sqle);
        } catch (Exception e) {
            throw new Log_reservasException(e);
        } finally {
        }
    }

    /**
*
* Returns all rows from log_reservas table where HORAINICIO_ASIGNADA= horainicio_asignada
*
* @param   Time  horainicio_asignada
* @param   Connection con
* @return  Log_reservas[]
*/
    public Log_reservas[] findByHorainicio_asignada(Time horainicio_asignada, Connection con) throws Log_reservasException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String SQL_STATEMENT = "Select id_log_reserv, id_reserva, id_operador, fecha, hora, operacion, id_solicitante, id_recurso, id_ubicacion, cancelada, horainicio_asignada, horafin_asignada, fecha_creacion, fecha_reserva, nombre_responsable, c_i_responsable from log_reservas where horainicio_asignada = ? order by horainicio_asignada";
        try {
            stmt = con.prepareStatement(SQL_STATEMENT);
            stmt.setTime(1, horainicio_asignada);
            rs = stmt.executeQuery();
            return fetchMultiResults(rs);
        } catch (SQLException sqle) {
            throw new Log_reservasException(sqle);
        } catch (Exception e) {
            throw new Log_reservasException(e);
        } finally {
        }
    }

    /**
*
* Returns all rows from log_reservas table where HORAFIN_ASIGNADA= horafin_asignada
*
* @param   Time  horafin_asignada
* @param   Connection con
* @return  Log_reservas[]
*/
    public Log_reservas[] findByHorafin_asignada(Time horafin_asignada, Connection con) throws Log_reservasException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String SQL_STATEMENT = "Select id_log_reserv, id_reserva, id_operador, fecha, hora, operacion, id_solicitante, id_recurso, id_ubicacion, cancelada, horainicio_asignada, horafin_asignada, fecha_creacion, fecha_reserva, nombre_responsable, c_i_responsable from log_reservas where horafin_asignada = ? order by horafin_asignada";
        try {
            stmt = con.prepareStatement(SQL_STATEMENT);
            stmt.setTime(1, horafin_asignada);
            rs = stmt.executeQuery();
            return fetchMultiResults(rs);
        } catch (SQLException sqle) {
            throw new Log_reservasException(sqle);
        } catch (Exception e) {
            throw new Log_reservasException(e);
        } finally {
        }
    }

    /**
*
* Returns all rows from log_reservas table where FECHA_CREACION= fecha_creacion
*
* @param   Date  fecha_creacion
* @param   Connection con
* @return  Log_reservas[]
*/
    public Log_reservas[] findByFecha_creacion(Date fecha_creacion, Connection con) throws Log_reservasException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String SQL_STATEMENT = "Select id_log_reserv, id_reserva, id_operador, fecha, hora, operacion, id_solicitante, id_recurso, id_ubicacion, cancelada, horainicio_asignada, horafin_asignada, fecha_creacion, fecha_reserva, nombre_responsable, c_i_responsable from log_reservas where fecha_creacion = ? order by fecha_creacion";
        try {
            stmt = con.prepareStatement(SQL_STATEMENT);
            stmt.setDate(1, fecha_creacion);
            rs = stmt.executeQuery();
            return fetchMultiResults(rs);
        } catch (SQLException sqle) {
            throw new Log_reservasException(sqle);
        } catch (Exception e) {
            throw new Log_reservasException(e);
        } finally {
        }
    }

    /**
*
* Returns all rows from log_reservas table where FECHA_RESERVA= fecha_reserva
*
* @param   Date  fecha_reserva
* @param   Connection con
* @return  Log_reservas[]
*/
    public Log_reservas[] findByFecha_reserva(Date fecha_reserva, Connection con) throws Log_reservasException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String SQL_STATEMENT = "Select id_log_reserv, id_reserva, id_operador, fecha, hora, operacion, id_solicitante, id_recurso, id_ubicacion, cancelada, horainicio_asignada, horafin_asignada, fecha_creacion, fecha_reserva, nombre_responsable, c_i_responsable from log_reservas where fecha_reserva = ? order by fecha_reserva";
        try {
            stmt = con.prepareStatement(SQL_STATEMENT);
            stmt.setDate(1, fecha_reserva);
            rs = stmt.executeQuery();
            return fetchMultiResults(rs);
        } catch (SQLException sqle) {
            throw new Log_reservasException(sqle);
        } catch (Exception e) {
            throw new Log_reservasException(e);
        } finally {
        }
    }

    /**
*
* Returns all rows from log_reservas table where NOMBRE_RESPONSABLE= nombre_responsable
*
* @param   String  nombre_responsable
* @param   Connection con
* @return  Log_reservas[]
*/
    public Log_reservas[] findByNombre_responsable(String nombre_responsable, Connection con) throws Log_reservasException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String SQL_STATEMENT = "Select id_log_reserv, id_reserva, id_operador, fecha, hora, operacion, id_solicitante, id_recurso, id_ubicacion, cancelada, horainicio_asignada, horafin_asignada, fecha_creacion, fecha_reserva, nombre_responsable, c_i_responsable from log_reservas where nombre_responsable = ? order by nombre_responsable";
        try {
            stmt = con.prepareStatement(SQL_STATEMENT);
            stmt.setString(1, nombre_responsable);
            rs = stmt.executeQuery();
            return fetchMultiResults(rs);
        } catch (SQLException sqle) {
            throw new Log_reservasException(sqle);
        } catch (Exception e) {
            throw new Log_reservasException(e);
        } finally {
        }
    }

    /**
*
* Returns all rows from log_reservas table where C_I_RESPONSABLE= c_i_responsable
*
* @param   String  c_i_responsable
* @param   Connection con
* @return  Log_reservas[]
*/
    public Log_reservas[] findByC_i_responsable(String c_i_responsable, Connection con) throws Log_reservasException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String SQL_STATEMENT = "Select id_log_reserv, id_reserva, id_operador, fecha, hora, operacion, id_solicitante, id_recurso, id_ubicacion, cancelada, horainicio_asignada, horafin_asignada, fecha_creacion, fecha_reserva, nombre_responsable, c_i_responsable from log_reservas where c_i_responsable = ? order by c_i_responsable";
        try {
            stmt = con.prepareStatement(SQL_STATEMENT);
            stmt.setString(1, c_i_responsable);
            rs = stmt.executeQuery();
            return fetchMultiResults(rs);
        } catch (SQLException sqle) {
            throw new Log_reservasException(sqle);
        } catch (Exception e) {
            throw new Log_reservasException(e);
        } finally {
        }
    }

    /**
* Returns all rows from log_reservas table 
*
* @param Connection con
* @return  Log_reservas[]
*
*/
    public Log_reservas[] findAll(Connection con) throws Log_reservasException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String SQL_STATEMENT = "Select id_log_reserv, id_reserva, id_operador, fecha, hora, operacion, id_solicitante, id_recurso, id_ubicacion, cancelada, horainicio_asignada, horafin_asignada, fecha_creacion, fecha_reserva, nombre_responsable, c_i_responsable from log_reservas";
        try {
            stmt = con.prepareStatement(SQL_STATEMENT);
            rs = stmt.executeQuery();
            return fetchMultiResults(rs);
        } catch (SQLException sqle) {
            throw new Log_reservasException(sqle);
        } catch (Exception e) {
            throw new Log_reservasException(e);
        } finally {
        }
    }

    /**
* Returns rows from log_reservas table by executing the passed sql statement
* after setting the passed values in Object[]
*
* @param String selectStatement
* @param Object[] sqlParams
* @param Connection con
* @return  Log_reservas[]
*
*/
    public Log_reservas[] findExecutingUserSelect(String selectStatement, Object[] sqlParams, Connection con) throws Log_reservasException {
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
            throw new Log_reservasException(sqle);
        } catch (Exception e) {
            throw new Log_reservasException(e);
        } finally {
        }
    }

    /**
* Returns rows from log_reservas table by executing the select all fields statement
* after setting the passed where clause and values in Object[]
*
* @param String whereClause
* @param Object[] sqlParams
* @param Connection con
* @return  Log_reservas[]
*
*/
    public Log_reservas[] findExecutingUserWhere(String whereClause, Object[] sqlParams, Connection con) throws Log_reservasException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String SQL_SELECT = "Select id_log_reserv, id_reserva, id_operador, fecha, hora, operacion, id_solicitante, id_recurso, id_ubicacion, cancelada, horainicio_asignada, horafin_asignada, fecha_creacion, fecha_reserva, nombre_responsable, c_i_responsable from log_reservas";
        final String SQL_STATEMENT = SQL_SELECT + " where " + whereClause;
        try {
            stmt = con.prepareStatement(SQL_STATEMENT);
            for (int i = 0; i < sqlParams.length; i++) {
                stmt.setObject(i + 1, sqlParams[i]);
            }
            rs = stmt.executeQuery();
            return fetchMultiResults(rs);
        } catch (SQLException sqle) {
            throw new Log_reservasException(sqle);
        } catch (Exception e) {
            throw new Log_reservasException(e);
        } finally {
        }
    }

    /**
*
* Populates a Data Transfer Object by fetching single record from resultSet 
*
* @param ResultSet rs
* @return  Log_reservas
*
*/
    protected Log_reservas fetchSingleResult(ResultSet rs) throws SQLException {
        if (rs.next()) {
            Log_reservas dto = new Log_reservas();
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
* @param Log_reservas dto
* @param   ResultSet rs
* @return  void
*/
    protected void populateVO(Log_reservas dto, ResultSet rs) throws SQLException {
        dto.setId_log_reserv(rs.getInt("id_log_reserv"));
        dto.setId_reserva(rs.getLong("id_reserva"));
        dto.setId_operador(rs.getInt("id_operador"));
        dto.setFecha(rs.getDate("fecha"));
        dto.setHora(rs.getTime("hora"));
        dto.setOperacion(rs.getString("operacion"));
        dto.setId_solicitante(rs.getInt("id_solicitante"));
        dto.setId_recurso(rs.getLong("id_recurso"));
        dto.setId_ubicacion(rs.getInt("id_ubicacion"));
        dto.setCancelada(rs.getBoolean("cancelada"));
        dto.setHorainicio_asignada(rs.getTime("horainicio_asignada"));
        dto.setHorafin_asignada(rs.getTime("horafin_asignada"));
        dto.setFecha_creacion(rs.getDate("fecha_creacion"));
        dto.setFecha_reserva(rs.getDate("fecha_reserva"));
        dto.setNombre_responsable(rs.getString("nombre_responsable"));
        dto.setC_i_responsable(rs.getString("c_i_responsable"));
    }

    /**
* 
* Returns an array of Value Objects by fetching data from resultSet
* 
* @param   ResultSet rs
* @return  Log_reservas[]
*/
    protected Log_reservas[] fetchMultiResults(ResultSet rs) throws SQLException {
        Collection resultList = new ArrayList();
        while (rs.next()) {
            Log_reservas dto = new Log_reservas();
            populateVO(dto, rs);
            resultList.add(dto);
        }
        Log_reservas ret[] = new Log_reservas[resultList.size()];
        resultList.toArray(ret);
        return ret;
    }
}
