package com.akcess.impl;

import java.math.*;
import java.sql.*;
import java.net.URL;
import java.util.*;
import com.akcess.exception.*;
import com.akcess.vo.*;
import com.akcess.dao.*;
import java.sql.Date;

/**
 * 
 * Implementation of Solicitudes_reservasDAO interface 
 * 
 */
public class Solicitudes_reservasDAOImpl implements Solicitudes_reservasDAO {

    /**
     * Method deletes a record from table SOLICITUDES_RESERVAS
     * @param Solicitudes_reservasPK solicitudes_reservaspk
     * @param  Connection  con
     * @return  int
     *
     */
    public int delete(Solicitudes_reservasPK solicitudes_reservaspk, Connection con) throws Solicitudes_reservasException {
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement("delete from  SOLICITUDES_RESERVAS where id_solic_reserva = ?");
            ps.setInt(1, solicitudes_reservaspk.getId_solic_reserva());
            return (ps.executeUpdate());
        } catch (SQLException sqle) {
            throw new Solicitudes_reservasException(sqle);
        } catch (Exception e) {
            throw new Solicitudes_reservasException(e);
        }
    }

    /**
     * This method updates a record in table SOLICITUDES_RESERVAS, no se actualiza la columna de ide de la Ubicacion, debido 
     * a que las unicas veces en que se actualiza esta Tabla es para "Aprobar", "Rechazar" o "Anular" una solicitud.
     * @param Solicitudes_reservasPK
     * @param Solicitudes_reservas
     * @param  Connection con
     * @return   int
     */
    public int update(Solicitudes_reservasPK solicitudes_reservaspk, Solicitudes_reservas solicitudes_reservas, Connection con) throws Solicitudes_reservasException {
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement("update SOLICITUDES_RESERVAS set ID_SOLICITANTE = ? , ID_RECURSO = ? , ID_UBICACION = ?, ID_ESTADO = ? , ID_OPERADOR = ? , FECHA_RESERVA = ? , HORA_INICIO = ? , HORA_FINAL = ? , NOMBRE_RESPONSABLE = ? , C_I_RESPONSABLE = ?  where id_solic_reserva = ?");
            ps.setInt(1, solicitudes_reservas.getId_solicitante());
            ps.setLong(2, solicitudes_reservas.getId_recurso());
            ps.setInt(3, solicitudes_reservas.getId_ubicacion());
            ps.setShort(4, solicitudes_reservas.getId_estado());
            ps.setInt(5, solicitudes_reservas.getId_operador());
            ps.setDate(6, solicitudes_reservas.getFecha_reserva());
            ps.setTime(7, solicitudes_reservas.getHora_inicio());
            ps.setTime(8, solicitudes_reservas.getHora_final());
            ps.setString(9, solicitudes_reservas.getNombre_responsable());
            ps.setString(10, solicitudes_reservas.getC_i_responsable());
            ps.setInt(11, solicitudes_reservaspk.getId_solic_reserva());
            return (ps.executeUpdate());
        } catch (SQLException sqle) {
            throw new Solicitudes_reservasException(sqle);
        } catch (Exception e) {
            throw new Solicitudes_reservasException(e);
        }
    }

    /**
     * Este metodo solo modifica el campo "Estado"
     * @param Solicitudes_reservasPK
     * @param Solicitudes_reservas
     * @param  Connection con
     * @return   int
     */
    public int cambiarEstadoYOperador(Solicitudes_reservasPK solicitudes_reservaspk, Solicitudes_reservas solicitudes_reservas, Connection con) throws Solicitudes_reservasException {
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement("update SOLICITUDES_RESERVAS set ID_ESTADO = ?,ID_OPERADOR = ?  where id_solic_reserva = ?");
            ps.setShort(1, solicitudes_reservas.getId_estado());
            ps.setInt(2, solicitudes_reservas.getId_operador());
            ps.setInt(3, solicitudes_reservaspk.getId_solic_reserva());
            return (ps.executeUpdate());
        } catch (SQLException sqle) {
            throw new Solicitudes_reservasException(sqle);
        } catch (Exception e) {
            throw new Solicitudes_reservasException(e);
        }
    }

    /**
     * This method inserts data in table SOLICITUDES_RESERVAS
     *
     * @param Solicitudes_reservas solicitudes_reservas
     * @param   Connection con
     * @return  Solicitudes_reservasPK
     */
    public int insert(Solicitudes_reservas solicitudes_reservas, Connection con) throws Solicitudes_reservasException {
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement("insert into SOLICITUDES_RESERVAS( ID_SOLICITANTE, ID_RECURSO, ID_UBICACION, ID_ESTADO, FECHA_RESERVA, HORA_INICIO, HORA_FINAL, NOMBRE_RESPONSABLE, C_I_RESPONSABLE) values (?, ?, ?, ?, ?, ?, ?, ?, ?)");
            ps.setInt(1, solicitudes_reservas.getId_solicitante());
            ps.setLong(2, solicitudes_reservas.getId_recurso());
            ps.setInt(3, solicitudes_reservas.getId_ubicacion());
            ps.setShort(4, solicitudes_reservas.getId_estado());
            ps.setDate(5, solicitudes_reservas.getFecha_reserva());
            ps.setTime(6, solicitudes_reservas.getHora_inicio());
            ps.setTime(7, solicitudes_reservas.getHora_final());
            ps.setString(8, solicitudes_reservas.getNombre_responsable());
            ps.setString(9, solicitudes_reservas.getC_i_responsable());
            return (ps.executeUpdate());
        } catch (SQLException sqle) {
            throw new Solicitudes_reservasException(sqle);
        } catch (Exception e) {
            throw new Solicitudes_reservasException(e);
        }
    }

    /**
     * Inserta una nueva solicitud sin Ubicacion.
     *
     * @param Solicitudes_reservas solicitudes_reservas
     * @param   Connection con
     * @return  Solicitudes_reservasPK
     */
    public int insertSinUbicacion(Solicitudes_reservas solicitudes_reservas, Connection con) throws Solicitudes_reservasException {
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement("insert into SOLICITUDES_RESERVAS( ID_SOLICITANTE, ID_RECURSO, ID_ESTADO, FECHA_RESERVA, HORA_INICIO, HORA_FINAL, NOMBRE_RESPONSABLE, C_I_RESPONSABLE) values (?, ?, ?, ?, ?, ?, ?, ?)");
            ps.setInt(1, solicitudes_reservas.getId_solicitante());
            ps.setLong(2, solicitudes_reservas.getId_recurso());
            ps.setShort(3, solicitudes_reservas.getId_estado());
            ps.setDate(4, solicitudes_reservas.getFecha_reserva());
            ps.setTime(5, solicitudes_reservas.getHora_inicio());
            ps.setTime(6, solicitudes_reservas.getHora_final());
            ps.setString(7, solicitudes_reservas.getNombre_responsable());
            ps.setString(8, solicitudes_reservas.getC_i_responsable());
            return (ps.executeUpdate());
        } catch (SQLException sqle) {
            throw new Solicitudes_reservasException(sqle);
        } catch (Exception e) {
            throw new Solicitudes_reservasException(e);
        }
    }

    /**
     * 
     * Returns a row from the solicitudes_reservas table for the primary key passed as parameter.
     * 
     */
    public Solicitudes_reservas findByPrimaryKey(int id_solic_reserva, Connection con) throws Solicitudes_reservasException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            final String SQLSTATEMENT = "Select id_solic_reserva, id_solicitante, id_recurso, id_ubicacion, id_estado, id_operador, fecha_creacion, fecha_reserva, hora_inicio, hora_final, nombre_responsable, c_i_responsable from solicitudes_reservas where id_solic_reserva = ?";
            stmt = con.prepareStatement(SQLSTATEMENT);
            stmt.setInt(1, id_solic_reserva);
            rs = stmt.executeQuery();
            return fetchSingleResult(rs);
        } catch (SQLException sqle) {
            throw new Solicitudes_reservasException(sqle);
        } catch (Exception e) {
            throw new Solicitudes_reservasException(e);
        } finally {
        }
    }

    /**
     * 
     * Returns a row from the solicitudes_reservas table for the primary key object passed as parameter.
     * 
     * @param  Solicitudes_reservasPK solicitudes_reservaspk
     * @param Connection con
     * @return  Solicitudes_reservas
     */
    public Solicitudes_reservas findByPrimaryKey(Solicitudes_reservasPK solicitudes_reservaspk, Connection con) throws Solicitudes_reservasException {
        return findByPrimaryKey(solicitudes_reservaspk.getId_solic_reserva(), con);
    }

    /**
     *
     * Returns all rows from solicitudes_reservas table where ID_SOLIC_RESERVA= id_solic_reserva
     *
     * @param   int  id_solic_reserva
     * @param   Connection con
     * @return  Solicitudes_reservas[]
     */
    public Solicitudes_reservas[] findById_solic_reserva(int id_solic_reserva, Connection con) throws Solicitudes_reservasException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String SQL_STATEMENT = "Select id_solic_reserva, id_solicitante, id_recurso, id_ubicacion, id_estado, id_operador, fecha_creacion, fecha_reserva, hora_inicio, hora_final, nombre_responsable, c_i_responsable from solicitudes_reservas where id_solic_reserva = ? order by id_solic_reserva";
        try {
            stmt = con.prepareStatement(SQL_STATEMENT);
            stmt.setInt(1, id_solic_reserva);
            rs = stmt.executeQuery();
            return fetchMultiResults(rs);
        } catch (SQLException sqle) {
            throw new Solicitudes_reservasException(sqle);
        } catch (Exception e) {
            throw new Solicitudes_reservasException(e);
        } finally {
        }
    }

    /**
     *
     * Returns all rows from solicitudes_reservas table where ID_SOLICITANTE= id_solicitante
     *
     * @param   int  id_solicitante
     * @param   Connection con
     * @return  Solicitudes_reservas[]
     */
    public Solicitudes_reservas[] findById_solicitante(int id_solicitante, Connection con) throws Solicitudes_reservasException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String SQL_STATEMENT = "Select id_solic_reserva, id_solicitante, id_recurso, id_ubicacion, id_estado, id_operador, fecha_creacion, fecha_reserva, hora_inicio, hora_final, nombre_responsable, c_i_responsable from solicitudes_reservas where id_solicitante = ? order by id_solicitante";
        try {
            stmt = con.prepareStatement(SQL_STATEMENT);
            stmt.setInt(1, id_solicitante);
            rs = stmt.executeQuery();
            return fetchMultiResults(rs);
        } catch (SQLException sqle) {
            throw new Solicitudes_reservasException(sqle);
        } catch (Exception e) {
            throw new Solicitudes_reservasException(e);
        } finally {
        }
    }

    /**
     *
     * Returns all rows from solicitudes_reservas table where ID_RECURSO= id_recurso
     *
     * @param   long  id_recurso
     * @param   Connection con
     * @return  Solicitudes_reservas[]
     */
    public Solicitudes_reservas[] findById_recurso(long id_recurso, Connection con) throws Solicitudes_reservasException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String SQL_STATEMENT = "Select id_solic_reserva, id_solicitante, id_recurso, id_ubicacion, id_estado, id_operador, fecha_creacion, fecha_reserva, hora_inicio, hora_final, nombre_responsable, c_i_responsable from solicitudes_reservas where id_recurso = ? order by id_recurso";
        try {
            stmt = con.prepareStatement(SQL_STATEMENT);
            stmt.setLong(1, id_recurso);
            rs = stmt.executeQuery();
            return fetchMultiResults(rs);
        } catch (SQLException sqle) {
            throw new Solicitudes_reservasException(sqle);
        } catch (Exception e) {
            throw new Solicitudes_reservasException(e);
        } finally {
        }
    }

    /**
     *
     * Returns all rows from solicitudes_reservas table where ID_UBICACION= id_ubicacion
     *
     * @param   int  id_ubicacion
     * @param   Connection con
     * @return  Solicitudes_reservas[]
     */
    public Solicitudes_reservas[] findById_ubicacion(int id_ubicacion, Connection con) throws Solicitudes_reservasException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String SQL_STATEMENT = "Select id_solic_reserva, id_solicitante, id_recurso, id_ubicacion, id_estado, id_operador, fecha_creacion, fecha_reserva, hora_inicio, hora_final, nombre_responsable, c_i_responsable from solicitudes_reservas where id_ubicacion = ? order by id_ubicacion";
        try {
            stmt = con.prepareStatement(SQL_STATEMENT);
            stmt.setInt(1, id_ubicacion);
            rs = stmt.executeQuery();
            return fetchMultiResults(rs);
        } catch (SQLException sqle) {
            throw new Solicitudes_reservasException(sqle);
        } catch (Exception e) {
            throw new Solicitudes_reservasException(e);
        } finally {
        }
    }

    /**
     *
     * Returns all rows from solicitudes_reservas table where ID_ESTADO= id_estado
     *
     * @param   short  id_estado
     * @param   Connection con
     * @return  Solicitudes_reservas[]
     */
    public Solicitudes_reservas[] findById_estado(short id_estado, Connection con) throws Solicitudes_reservasException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String SQL_STATEMENT = "Select id_solic_reserva, id_solicitante, id_recurso, id_ubicacion, id_estado, id_operador, fecha_creacion, fecha_reserva, hora_inicio, hora_final, nombre_responsable, c_i_responsable from solicitudes_reservas where id_estado = ? order by id_estado";
        try {
            stmt = con.prepareStatement(SQL_STATEMENT);
            stmt.setShort(1, id_estado);
            rs = stmt.executeQuery();
            return fetchMultiResults(rs);
        } catch (SQLException sqle) {
            throw new Solicitudes_reservasException(sqle);
        } catch (Exception e) {
            throw new Solicitudes_reservasException(e);
        } finally {
        }
    }

    /**
     *
     * Returns all rows from solicitudes_reservas table where ID_OPERADOR= id_operador
     *
     * @param   int  id_operador
     * @param   Connection con
     * @return  Solicitudes_reservas[]
     */
    public Solicitudes_reservas[] findById_operador(int id_operador, Connection con) throws Solicitudes_reservasException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String SQL_STATEMENT = "Select id_solic_reserva, id_solicitante, id_recurso, id_ubicacion, id_estado, id_operador, fecha_creacion, fecha_reserva, hora_inicio, hora_final, nombre_responsable, c_i_responsable from solicitudes_reservas where id_operador = ? order by id_operador";
        try {
            stmt = con.prepareStatement(SQL_STATEMENT);
            stmt.setInt(1, id_operador);
            rs = stmt.executeQuery();
            return fetchMultiResults(rs);
        } catch (SQLException sqle) {
            throw new Solicitudes_reservasException(sqle);
        } catch (Exception e) {
            throw new Solicitudes_reservasException(e);
        } finally {
        }
    }

    /**
     *
     * Returns all rows from solicitudes_reservas table where FECHA_CREACION= fecha_creacion
     *
     * @param   Date  fecha_creacion
     * @param   Connection con
     * @return  Solicitudes_reservas[]
     */
    public Solicitudes_reservas[] findByFecha_creacion(Date fecha_creacion, Connection con) throws Solicitudes_reservasException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String SQL_STATEMENT = "Select id_solic_reserva, id_solicitante, id_recurso, id_ubicacion, id_estado, id_operador, fecha_creacion, fecha_reserva, hora_inicio, hora_final, nombre_responsable, c_i_responsable from solicitudes_reservas where fecha_creacion = ? order by fecha_creacion";
        try {
            stmt = con.prepareStatement(SQL_STATEMENT);
            stmt.setDate(1, fecha_creacion);
            rs = stmt.executeQuery();
            return fetchMultiResults(rs);
        } catch (SQLException sqle) {
            throw new Solicitudes_reservasException(sqle);
        } catch (Exception e) {
            throw new Solicitudes_reservasException(e);
        } finally {
        }
    }

    /**
     *
     * Returns all rows from solicitudes_reservas table where FECHA_RESERVA= fecha_reserva
     *
     * @param   Date  fecha_reserva
     * @param   Connection con
     * @return  Solicitudes_reservas[]
     */
    public Solicitudes_reservas[] findByFecha_reserva(Date fecha_reserva, Connection con) throws Solicitudes_reservasException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String SQL_STATEMENT = "Select id_solic_reserva, id_solicitante, id_recurso, id_ubicacion, id_estado, id_operador, fecha_creacion, fecha_reserva, hora_inicio, hora_final, nombre_responsable, c_i_responsable from solicitudes_reservas where fecha_reserva = ? order by fecha_reserva";
        try {
            stmt = con.prepareStatement(SQL_STATEMENT);
            stmt.setDate(1, fecha_reserva);
            rs = stmt.executeQuery();
            return fetchMultiResults(rs);
        } catch (SQLException sqle) {
            throw new Solicitudes_reservasException(sqle);
        } catch (Exception e) {
            throw new Solicitudes_reservasException(e);
        } finally {
        }
    }

    /**
     *
     * Returns all rows from solicitudes_reservas table where HORA_INICIO= hora_inicio
     *
     * @param   Time  hora_inicio
     * @param   Connection con
     * @return  Solicitudes_reservas[]
     */
    public Solicitudes_reservas[] findByHora_inicio(Time hora_inicio, Connection con) throws Solicitudes_reservasException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String SQL_STATEMENT = "Select id_solic_reserva, id_solicitante, id_recurso, id_ubicacion, id_estado, id_operador, fecha_creacion, fecha_reserva, hora_inicio, hora_final, nombre_responsable, c_i_responsable from solicitudes_reservas where hora_inicio = ? order by hora_inicio";
        try {
            stmt = con.prepareStatement(SQL_STATEMENT);
            stmt.setTime(1, hora_inicio);
            rs = stmt.executeQuery();
            return fetchMultiResults(rs);
        } catch (SQLException sqle) {
            throw new Solicitudes_reservasException(sqle);
        } catch (Exception e) {
            throw new Solicitudes_reservasException(e);
        } finally {
        }
    }

    /**
     *
     * Returns all rows from solicitudes_reservas table where HORA_FINAL= hora_final
     *
     * @param   Time  hora_final
     * @param   Connection con
     * @return  Solicitudes_reservas[]
     */
    public Solicitudes_reservas[] findByHora_final(Time hora_final, Connection con) throws Solicitudes_reservasException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String SQL_STATEMENT = "Select id_solic_reserva, id_solicitante, id_recurso, id_ubicacion, id_estado, id_operador, fecha_creacion, fecha_reserva, hora_inicio, hora_final, nombre_responsable, c_i_responsable from solicitudes_reservas where hora_final = ? order by hora_final";
        try {
            stmt = con.prepareStatement(SQL_STATEMENT);
            stmt.setTime(1, hora_final);
            rs = stmt.executeQuery();
            return fetchMultiResults(rs);
        } catch (SQLException sqle) {
            throw new Solicitudes_reservasException(sqle);
        } catch (Exception e) {
            throw new Solicitudes_reservasException(e);
        } finally {
        }
    }

    /**
     *
     * Returns all rows from solicitudes_reservas table where NOMBRE_RESPONSABLE= nombre_responsable
     *
     * @param   String  nombre_responsable
     * @param   Connection con
     * @return  Solicitudes_reservas[]
     */
    public Solicitudes_reservas[] findByNombre_responsable(String nombre_responsable, Connection con) throws Solicitudes_reservasException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String SQL_STATEMENT = "Select id_solic_reserva, id_solicitante, id_recurso, id_ubicacion, id_estado, id_operador, fecha_creacion, fecha_reserva, hora_inicio, hora_final, nombre_responsable, c_i_responsable from solicitudes_reservas where nombre_responsable = ? order by nombre_responsable";
        try {
            stmt = con.prepareStatement(SQL_STATEMENT);
            stmt.setString(1, nombre_responsable);
            rs = stmt.executeQuery();
            return fetchMultiResults(rs);
        } catch (SQLException sqle) {
            throw new Solicitudes_reservasException(sqle);
        } catch (Exception e) {
            throw new Solicitudes_reservasException(e);
        } finally {
        }
    }

    /**
     *
     * Returns all rows from solicitudes_reservas table where C_I_RESPONSABLE= c_i_responsable
     *
     * @param   String  c_i_responsable
     * @param   Connection con
     * @return  Solicitudes_reservas[]
     */
    public Solicitudes_reservas[] findByC_i_responsable(String c_i_responsable, Connection con) throws Solicitudes_reservasException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String SQL_STATEMENT = "Select id_solic_reserva, id_solicitante, id_recurso, id_ubicacion, id_estado, id_operador, fecha_creacion, fecha_reserva, hora_inicio, hora_final, nombre_responsable, c_i_responsable from solicitudes_reservas where c_i_responsable = ? order by c_i_responsable";
        try {
            stmt = con.prepareStatement(SQL_STATEMENT);
            stmt.setString(1, c_i_responsable);
            rs = stmt.executeQuery();
            return fetchMultiResults(rs);
        } catch (SQLException sqle) {
            throw new Solicitudes_reservasException(sqle);
        } catch (Exception e) {
            throw new Solicitudes_reservasException(e);
        } finally {
        }
    }

    /**
     * Returns all rows from solicitudes_reservas table 
     *
     * @param Connection con
     * @return  Solicitudes_reservas[]
     *
     */
    public Solicitudes_reservas[] findAll(Connection con) throws Solicitudes_reservasException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String SQL_STATEMENT = "Select id_solic_reserva, id_solicitante, id_recurso, id_ubicacion, id_estado, id_operador, fecha_creacion, fecha_reserva, hora_inicio, hora_final, nombre_responsable, c_i_responsable from solicitudes_reservas";
        try {
            stmt = con.prepareStatement(SQL_STATEMENT);
            rs = stmt.executeQuery();
            return fetchMultiResults(rs);
        } catch (SQLException sqle) {
            throw new Solicitudes_reservasException(sqle);
        } catch (Exception e) {
            throw new Solicitudes_reservasException(e);
        } finally {
        }
    }

    /**
     * Returns rows from solicitudes_reservas table by executing the passed sql statement
     * after setting the passed values in Object[]
     *
     * @param String selectStatement
     * @param Object[] sqlParams
     * @param Connection con
     * @return  Solicitudes_reservas[]
     *
     */
    public Solicitudes_reservas[] findExecutingUserSelect(String selectStatement, Object[] sqlParams, Connection con) throws Solicitudes_reservasException {
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
            throw new Solicitudes_reservasException(sqle);
        } catch (Exception e) {
            throw new Solicitudes_reservasException(e);
        } finally {
        }
    }

    /**
     * Returns rows from solicitudes_reservas table by executing the select all fields statement
     * after setting the passed where clause and values in Object[]
     *
     * @param String whereClause
     * @param Object[] sqlParams
     * @param Connection con
     * @return  Solicitudes_reservas[]
     *
     */
    public Solicitudes_reservas[] findExecutingUserWhere(String whereClause, Object[] sqlParams, Connection con) throws Solicitudes_reservasException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String SQL_SELECT = "Select id_solic_reserva, id_solicitante, id_recurso, id_ubicacion, id_estado, id_operador, fecha_creacion, fecha_reserva, hora_inicio, hora_final, nombre_responsable, c_i_responsable from solicitudes_reservas";
        final String SQL_STATEMENT = SQL_SELECT + " where " + whereClause;
        try {
            stmt = con.prepareStatement(SQL_STATEMENT);
            for (int i = 0; i < sqlParams.length; i++) {
                stmt.setObject(i + 1, sqlParams[i]);
            }
            rs = stmt.executeQuery();
            return fetchMultiResults(rs);
        } catch (SQLException sqle) {
            throw new Solicitudes_reservasException(sqle);
        } catch (Exception e) {
            throw new Solicitudes_reservasException(e);
        } finally {
        }
    }

    /**
     *
     * Populates a Data Transfer Object by fetching single record from resultSet 
     *
     * @param ResultSet rs
     * @return  Solicitudes_reservas
     *
     */
    protected Solicitudes_reservas fetchSingleResult(ResultSet rs) throws SQLException {
        if (rs.next()) {
            Solicitudes_reservas dto = new Solicitudes_reservas();
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
     * @param Solicitudes_reservas dto
     * @param   ResultSet rs
     * @return  void
     */
    protected void populateVO(Solicitudes_reservas dto, ResultSet rs) throws SQLException {
        dto.setId_solic_reserva(rs.getInt("id_solic_reserva"));
        dto.setId_solicitante(rs.getInt("id_solicitante"));
        dto.setId_recurso(rs.getLong("id_recurso"));
        dto.setId_ubicacion(rs.getInt("id_ubicacion"));
        dto.setId_estado(rs.getShort("id_estado"));
        dto.setId_operador(rs.getInt("id_operador"));
        dto.setFecha_creacion(rs.getDate("fecha_creacion"));
        dto.setFecha_reserva(rs.getDate("fecha_reserva"));
        dto.setHora_inicio(rs.getTime("hora_inicio"));
        dto.setHora_final(rs.getTime("hora_final"));
        dto.setNombre_responsable(rs.getString("nombre_responsable"));
        dto.setC_i_responsable(rs.getString("c_i_responsable"));
    }

    /**
     * 
     * Returns an array of Value Objects by fetching data from resultSet
     * 
     * @param   ResultSet rs
     * @return  Solicitudes_reservas[]
     */
    protected Solicitudes_reservas[] fetchMultiResults(ResultSet rs) throws SQLException {
        Collection resultList = new ArrayList();
        while (rs.next()) {
            Solicitudes_reservas dto = new Solicitudes_reservas();
            populateVO(dto, rs);
            resultList.add(dto);
        }
        Solicitudes_reservas ret[] = new Solicitudes_reservas[resultList.size()];
        resultList.toArray(ret);
        return ret;
    }

    /**
     * Retornara todas las solicitudes existentes con su nombre de estado y demas informaciones, que sean sobre reservas de Aulas.
     * @param con Conexion a la BD.
     * @return SolicitudReservaEstado Un vector conteniendo ciertas informaciones sobre las solicitudes, asi como el nombre del estado de la solicitud y 
     * nombre y apellido del solicitante.
     * @throws com.akcess.exception.Solicitudes_reservasException Lanza excepciones del tipo Solicitudes_reservasException, creada por el DAOGenerator.
     */
    public SolicitudReservaEstado[] ObtenerSolicitudesAulas(Connection con) throws Solicitudes_reservasException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String SQL_STATEMENT = "Select id_solic_reserva, solicitudes_reservas.id_solicitante, solicitudes_reservas.id_recurso, aulas.id_ubicacion,solicitudes_reservas.id_estado, solicitudes_reservas.id_operador, fecha_creacion, fecha_reserva, hora_inicio, hora_final, nombre_responsable, c_i_responsable,usuarios.nombre as nombre_solicitante,usuarios.apellido as apellido_solicitante, estado.nombre as nombre_estado, aulas.nombre as nombre_recurso from solicitudes_reservas,usuarios,estado,aulas where NOT EXISTS (Select * from Sala_de_maquinas where aulas.id_recurso=Sala_de_maquinas.id_sala) and estado.id_estado=solicitudes_reservas.id_estado and solicitudes_reservas.id_solicitante=usuarios.id_usuario and solicitudes_reservas.id_recurso=aulas.id_recurso";
        try {
            stmt = con.prepareStatement(SQL_STATEMENT);
            rs = stmt.executeQuery();
            return convertMultiResults(rs);
        } catch (SQLException sqle) {
            throw new Solicitudes_reservasException(sqle);
        } catch (Exception e) {
            throw new Solicitudes_reservasException(e);
        } finally {
        }
    }

    /**
     * Retorna un arreglo que contiene los datos de las solicitudes asi como el nombre del estado en que se encuentra, nombre y apellido del solicitante.
     * @param   ResultSet rs Recibe el resultado de la consulta realizada en SolicitudesNombreEstado(Connection con).
     * @return  SolicitudReservaEstado[] El retorno es un vector de tipo SolicitudReservaEstado, conteniendo toda la informacion descrita anteriormente.
     */
    protected SolicitudReservaEstado[] convertMultiResults(ResultSet rs) throws SQLException {
        Collection resultList = new ArrayList();
        while (rs.next()) {
            SolicitudReservaEstado dto = new SolicitudReservaEstado();
            populateVO(dto, rs);
            resultList.add(dto);
        }
        SolicitudReservaEstado ret[] = new SolicitudReservaEstado[resultList.size()];
        resultList.toArray(ret);
        return ret;
    }

    /**
     * Puebla un objeto de tipo SolicitudReservaEstado con los datos de una fila contenida en el ResultSet, producida
     * por la consulta realizada en SolicitudesNombreEstado(Connection con).
     * @param SolicitudReservaEstado dto representa el objeto que sera poblado.
     * @param   ResultSet rs Contiene una fila del resultado obtenido por la consulta.
     * @return  void No retorna nada.
     */
    protected void populateVO(SolicitudReservaEstado dto, ResultSet rs) throws SQLException {
        dto.setId_solic_reserva(rs.getInt("id_solic_reserva"));
        dto.setFecha_creacion(rs.getDate("fecha_creacion"));
        dto.setId_estado(rs.getShort("id_estado"));
        dto.setId_operador(rs.getInt("id_operador"));
        dto.setC_i_responsable(rs.getString("c_i_responsable"));
        dto.setApellidoSolicitante(rs.getString("apellido_solicitante"));
        dto.setFecha_reserva(rs.getDate("fecha_reserva"));
        dto.setNombreEstado(rs.getString("nombre_estado"));
        dto.setNombre_responsable(rs.getString("nombre_responsable"));
        dto.setId_ubicacion(rs.getInt("id_ubicacion"));
        dto.setId_solicitante(rs.getInt("id_solicitante"));
        dto.setId_recurso(rs.getInt("id_recurso"));
        dto.setNombreSolicitante(rs.getString("nombre_solicitante"));
        dto.setHora_inicio(rs.getTime("hora_inicio"));
        dto.setHora_final(rs.getTime("hora_final"));
        dto.setNombreRecurso(rs.getString("nombre_recurso"));
    }

    /**
     * Retornara todas las solicitudes existentes con su nombre de estado y demas informaciones, que sean sobre reservas de Sala de Maquinas.
     * @param con Conexion a la BD.
     * @return SolicitudReservaEstado Un vector conteniendo ciertas informaciones sobre las solicitudes, asi como el nombre del estado de la solicitud y 
     * nombre y apellido del solicitante.
     * @throws com.akcess.exception.Solicitudes_reservasException Lanza excepciones del tipo Solicitudes_reservasException, creada por el DAOGenerator.
     */
    public SolicitudReservaEstado[] ObtenerSolicitudesSM(Connection con) throws Solicitudes_reservasException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String SQL_STATEMENT = "Select id_solic_reserva, solicitudes_reservas.id_solicitante, solicitudes_reservas.id_recurso, aulas.id_ubicacion,solicitudes_reservas.id_estado, solicitudes_reservas.id_operador, fecha_creacion, fecha_reserva, hora_inicio, hora_final, nombre_responsable, c_i_responsable,usuarios.nombre as nombre_solicitante,usuarios.apellido as apellido_solicitante, estado.nombre as nombre_estado, aulas.nombre as nombre_recurso from solicitudes_reservas,usuarios,estado,aulas where EXISTS (Select * from Sala_de_maquinas where aulas.id_recurso=Sala_de_maquinas.id_sala) and estado.id_estado=solicitudes_reservas.id_estado and solicitudes_reservas.id_solicitante=usuarios.id_usuario and solicitudes_reservas.id_recurso=aulas.id_recurso";
        try {
            stmt = con.prepareStatement(SQL_STATEMENT);
            rs = stmt.executeQuery();
            return convertMultiResults(rs);
        } catch (SQLException sqle) {
            throw new Solicitudes_reservasException(sqle);
        } catch (Exception e) {
            throw new Solicitudes_reservasException(e);
        } finally {
        }
    }

    /**
     * Retornara todas las solicitudes existentes con su nombre de estado y demas informaciones, que sean sobre reservas de Recursos Moviles.
     * @param con Conexion a la BD.
     * @param tipoRM Identifica el tipo de Recurso Movil, cuyas solicitudes se quieren obtener. 
     * @return SolicitudReservaEstado Un vector conteniendo ciertas informaciones sobre las solicitudes, asi como el nombre del estado de la solicitud y 
     * nombre y apellido del solicitante.
     * @throws com.akcess.exception.Solicitudes_reservasException Lanza excepciones del tipo Solicitudes_reservasException, creada por el DAOGenerator.
     */
    public SolicitudReservaEstado[] ObtenerSolicitudesRM(int tipoRM, Connection con) throws Solicitudes_reservasException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String SQL_STATEMENT = "Select distinct id_solic_reserva, solicitudes_reservas.id_solicitante, solicitudes_reservas.id_recurso, solicitudes_reservas.id_ubicacion,solicitudes_reservas.id_estado, solicitudes_reservas.id_operador, fecha_creacion, fecha_reserva, hora_inicio, hora_final, nombre_responsable, c_i_responsable,usuarios.nombre as nombre_solicitante,usuarios.apellido as apellido_solicitante, estado.nombre as nombre_estado, recurso_movil.nombre as nombre_recurso from usuarios,solicitudes_reservas,recurso_movil,datos_atributo,recurso,estado where  datos_atributo.id_tipo_recurso= ? and recurso_movil.id_recurso=recurso.id_recurso and recurso_movil.id_recurso=datos_atributo.id_recurso and solicitudes_reservas.id_solicitante=usuarios.id_usuario and recurso_movil.id_recurso=solicitudes_reservas.id_recurso and estado.id_estado=solicitudes_reservas.id_estado";
        try {
            stmt = con.prepareStatement(SQL_STATEMENT);
            stmt.setInt(1, tipoRM);
            rs = stmt.executeQuery();
            return convertMultiResults(rs);
        } catch (SQLException sqle) {
            throw new Solicitudes_reservasException(sqle);
        } catch (Exception e) {
            throw new Solicitudes_reservasException(e);
        } finally {
        }
    }

    /**
     * Retornara la solicitud existente en la BD identificada por el id de Solicitud Recibido y cuya solicitud se trata de un reserva de
     * Aula, con su nombre de estado y demas informaciones.
     * @param con Conexion a la BD.
     * @param idSolicitud Identifica la solicitud que se desea obtener. 
     * @return SolicitudReservaEstado Un vector conteniendo ciertas informaciones sobre las solicitudes, asi como el nombre del estado de la solicitud y 
     * nombre y apellido del solicitante.
     * @throws com.akcess.exception.Solicitudes_reservasException Lanza excepciones del tipo Solicitudes_reservasException, creada por el DAOGenerator.
     */
    public SolicitudReservaEstado ObtenerSolicitudAula(int idSolicitud, Connection con) throws Solicitudes_reservasException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String SQL_STATEMENT = "Select id_solic_reserva, solicitudes_reservas.id_solicitante, solicitudes_reservas.id_recurso, aulas.id_ubicacion,solicitudes_reservas.id_estado, solicitudes_reservas.id_operador, fecha_creacion, fecha_reserva, hora_inicio, hora_final, nombre_responsable, c_i_responsable,usuarios.nombre as nombre_solicitante,usuarios.apellido as apellido_solicitante, estado.nombre as nombre_estado, aulas.nombre as nombre_recurso from solicitudes_reservas,usuarios,estado,aulas where NOT EXISTS (Select * from Sala_de_maquinas where aulas.id_recurso=Sala_de_maquinas.id_sala) and estado.id_estado=solicitudes_reservas.id_estado and solicitudes_reservas.id_solicitante=usuarios.id_usuario and solicitudes_reservas.id_recurso=aulas.id_recurso and solicitudes_reservas.id_solic_reserva= ? ";
        try {
            stmt = con.prepareStatement(SQL_STATEMENT);
            stmt.setInt(1, idSolicitud);
            rs = stmt.executeQuery();
            return convertSingleResults(rs);
        } catch (SQLException sqle) {
            throw new Solicitudes_reservasException(sqle);
        } catch (Exception e) {
            throw new Solicitudes_reservasException(e);
        } finally {
        }
    }

    /**
     * Retornara la solicitud existente en la BD identificada por el id de Solicitud Recibido y cuya solicitud se trata de un reserva de
     * Sala de Maquinas, con su nombre de estado y demas informaciones.
     * @param con Conexion a la BD.
     * @param idSolicitud Identifica la solicitud que se desea obtener. 
     * @return SolicitudReservaEstado Un vector conteniendo ciertas informaciones sobre las solicitudes, asi como el nombre del estado de la solicitud y 
     * nombre y apellido del solicitante.
     * @throws com.akcess.exception.Solicitudes_reservasException Lanza excepciones del tipo Solicitudes_reservasException, creada por el DAOGenerator.
     */
    public SolicitudReservaEstado ObtenerSolicitudSM(int idSolicitud, Connection con) throws Solicitudes_reservasException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String SQL_STATEMENT = "Select id_solic_reserva, solicitudes_reservas.id_solicitante, solicitudes_reservas.id_recurso, aulas.id_ubicacion,solicitudes_reservas.id_estado, solicitudes_reservas.id_operador, fecha_creacion, fecha_reserva, hora_inicio, hora_final, nombre_responsable, c_i_responsable,usuarios.nombre as nombre_solicitante,usuarios.apellido as apellido_solicitante, estado.nombre as nombre_estado, aulas.nombre as nombre_recurso from solicitudes_reservas,usuarios,estado,aulas where EXISTS (Select * from Sala_de_maquinas where aulas.id_recurso=Sala_de_maquinas.id_sala) and estado.id_estado=solicitudes_reservas.id_estado and solicitudes_reservas.id_solicitante=usuarios.id_usuario and solicitudes_reservas.id_recurso=aulas.id_recurso and solicitudes_reservas.id_solic_reserva= ? ";
        try {
            stmt = con.prepareStatement(SQL_STATEMENT);
            stmt.setInt(1, idSolicitud);
            rs = stmt.executeQuery();
            return convertSingleResults(rs);
        } catch (SQLException sqle) {
            throw new Solicitudes_reservasException(sqle);
        } catch (Exception e) {
            throw new Solicitudes_reservasException(e);
        } finally {
        }
    }

    /**
     * Retornara la solicitud existente en la BD identificada por el id de Solicitud Recibido y cuya solicitud se trata de un reserva de
     * Recursos Moviles, con su nombre de estado y demas informaciones.
     * @param con Conexion a la BD.
     * @param idSolicitud Identifica la solicitud que se desea obtener. 
     * @param tipoRM Identifica el tipo de Recurso Movil, cuya solicitud se quiere obtener. 
     * @return SolicitudReservaEstado Un vector conteniendo ciertas informaciones sobre las solicitudes, asi como el nombre del estado de la solicitud y 
     * nombre y apellido del solicitante.
     * @throws com.akcess.exception.Solicitudes_reservasException Lanza excepciones del tipo Solicitudes_reservasException, creada por el DAOGenerator.
     */
    public SolicitudReservaEstado ObtenerSolicitudRM(int idSolicitud, int tipoRM, Connection con) throws Solicitudes_reservasException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String SQL_STATEMENT = "Select distinct id_solic_reserva, solicitudes_reservas.id_solicitante, solicitudes_reservas.id_recurso, solicitudes_reservas.id_ubicacion,solicitudes_reservas.id_estado, solicitudes_reservas.id_operador, fecha_creacion, fecha_reserva, hora_inicio, hora_final, nombre_responsable, c_i_responsable,usuarios.nombre as nombre_solicitante,usuarios.apellido as apellido_solicitante, estado.nombre as nombre_estado, recurso_movil.nombre as nombre_recurso from usuarios,solicitudes_reservas,recurso_movil,datos_atributo,recurso,estado where  datos_atributo.id_tipo_recurso= ? and recurso_movil.id_recurso=recurso.id_recurso and recurso_movil.id_recurso=datos_atributo.id_recurso and solicitudes_reservas.id_solicitante=usuarios.id_usuario and recurso_movil.id_recurso=solicitudes_reservas.id_recurso and estado.id_estado=solicitudes_reservas.id_estado and solicitudes_reservas.id_solic_reserva= ? ";
        try {
            stmt = con.prepareStatement(SQL_STATEMENT);
            stmt.setInt(1, tipoRM);
            stmt.setInt(2, idSolicitud);
            rs = stmt.executeQuery();
            return convertSingleResults(rs);
        } catch (SQLException sqle) {
            throw new Solicitudes_reservasException(sqle);
        } catch (Exception e) {
            throw new Solicitudes_reservasException(e);
        } finally {
        }
    }

    /**
     * Retorna un solo objeto que contiene los datos de la solicitud asi como el nombre del estado en que se encuentra, nombre y apellido del solicitante.
     * @param   ResultSet rs Recibe el resultado de la consulta realizada en SolicitudesNombreEstado(Connection con).
     * @return  SolicitudReservaEstado El retorno es un objeto de tipo SolicitudReservaEstado, conteniendo toda la informacion descrita anteriormente.
     */
    protected SolicitudReservaEstado convertSingleResults(ResultSet rs) throws SQLException {
        if (rs.next()) {
            SolicitudReservaEstado dto = new SolicitudReservaEstado();
            populateVO(dto, rs);
            return dto;
        } else {
            return null;
        }
    }
}
