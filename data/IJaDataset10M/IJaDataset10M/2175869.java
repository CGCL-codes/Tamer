package mx.ipn.persistencia.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import mx.ipn.to.DiaFestivoTO;
import mx.ipn.persistencia.dao.DiaFestivoDAO;

public class DiaFestivoDAOMySQL implements DiaFestivoDAO {

    static {
        FabricaDeDAOsMySQL.iniciaLog(DiaFestivoDAOMySQL.class);
    }

    private static final String INSERT = "INSERT INTO dia_festivo (descripcion,dia,mes,activo) VALUES(?,?,?,?)";

    private static final String DELETE = "DELETE FROM dia_festivo WHERE id_dia_festivo=?";

    private static final String DELETEDES = "DELETE FROM dia_festivo WHERE descripcion=?";

    private static final String UPDATE = "UPDATE dia_festivo SET descripcion=?,dia=?,mes=?,activo=? WHERE id_dia_festivo=?";

    private static final String FINDBYID = "SELECT * FROM dia_festivo WHERE id_dia_festivo=? AND activo =true";

    private static final String FINDBYMESDIAACTIVO = "SELECT * FROM dia_festivo WHERE mes=? AND dia=? AND activo=true";

    private static final String SELECT = "SELECT * FROM dia_festivo";

    private static final String SELECTMES = "SELECT * FROM dia_festivo WHERE mes=? AND activo=true";

    private static final String SELECTACTIVOS = "SELECT * FROM dia_festivo WHERE activo=?";

    @Override
    public int insertDiaFestivo(DiaFestivoTO diaFestivoTO) {
        try {
            Connection conexion = FabricaDeDAOsMySQL.crearConexion();
            PreparedStatement ps = conexion.prepareStatement(INSERT);
            ps.setString(1, diaFestivoTO.getDescripcion());
            ps.setShort(2, diaFestivoTO.getDia());
            ps.setShort(3, diaFestivoTO.getMes());
            ps.setBoolean(4, diaFestivoTO.getActivo());
            ps.executeUpdate();
            int autoIncIndice = -1;
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                autoIncIndice = rs.getInt(1);
            } else {
                System.err.print("No se pudo agregar campo");
                return 0;
            }
            ps.close();
            conexion.close();
            return autoIncIndice;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public short deleteDiaFestivo(String diaFestivo) {
        try {
            Connection conexion = FabricaDeDAOsMySQL.crearConexion();
            PreparedStatement ps = conexion.prepareStatement(DELETEDES);
            ps.setString(1, diaFestivo);
            ps.executeUpdate();
            if (ps.getUpdateCount() == 0) {
                ps.close();
                conexion.close();
                return 0;
            } else {
                ps.close();
                conexion.close();
                return 1;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return (short) -1;
        }
    }

    public short deleteDiaFestivo(int idDiaFestivo) {
        try {
            Connection conexion = FabricaDeDAOsMySQL.crearConexion();
            PreparedStatement ps = conexion.prepareStatement(DELETE);
            ps.setInt(1, idDiaFestivo);
            ps.executeUpdate();
            if (ps.getUpdateCount() == 0) {
                ps.close();
                conexion.close();
                return 0;
            } else {
                ps.close();
                conexion.close();
                return 1;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return (short) -1;
        }
    }

    public short updateDiaFestivo(DiaFestivoTO diaFestivoTO) {
        try {
            Connection conexion = FabricaDeDAOsMySQL.crearConexion();
            PreparedStatement ps = conexion.prepareStatement(UPDATE);
            ps.setInt(5, diaFestivoTO.getIdDiaFestivo());
            ps.setString(1, diaFestivoTO.getDescripcion());
            ps.setShort(2, diaFestivoTO.getDia());
            ps.setShort(3, diaFestivoTO.getMes());
            ps.setBoolean(4, diaFestivoTO.getActivo());
            ps.setInt(5, diaFestivoTO.getIdDiaFestivo());
            ps.executeUpdate();
            if (ps.getUpdateCount() == 0) {
                ps.close();
                conexion.close();
                return 0;
            } else {
                ps.close();
                conexion.close();
                return 1;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return (short) -1;
        }
    }

    public DiaFestivoTO findDiaFestivoById(int idDiaFestivo) {
        try {
            Connection conexion = FabricaDeDAOsMySQL.crearConexion();
            PreparedStatement ps = conexion.prepareStatement(FINDBYID);
            ps.setInt(1, idDiaFestivo);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                idDiaFestivo = rs.getInt("id_dia_festivo");
                String descripcion = rs.getString("descripcion");
                short dia = rs.getShort("dia");
                short mes = rs.getShort("mes");
                boolean activo = rs.getBoolean("activo");
                DiaFestivoTO diaFestivoTO = new DiaFestivoTO(idDiaFestivo, descripcion, dia, mes, activo);
                ps.close();
                conexion.close();
                return diaFestivoTO;
            }
            ps.close();
            conexion.close();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public DiaFestivoTO findDiaFestivoByMesDiaActivo(short mes, short dia) {
        try {
            Connection conexion = FabricaDeDAOsMySQL.crearConexion();
            PreparedStatement ps = conexion.prepareStatement(FINDBYMESDIAACTIVO);
            ps.setShort(1, mes);
            ps.setShort(2, dia);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int idDiaFestivo = rs.getInt("id_dia_festivo");
                String descripcion = rs.getString("descripcion");
                boolean activo = rs.getBoolean("activo");
                DiaFestivoTO diaFestivoTO = new DiaFestivoTO(idDiaFestivo, descripcion, dia, mes, activo);
                ps.close();
                conexion.close();
                return diaFestivoTO;
            }
            ps.close();
            conexion.close();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public ArrayList<DiaFestivoTO> selectDiasFestivos() {
        try {
            Connection conexion = FabricaDeDAOsMySQL.crearConexion();
            PreparedStatement ps = conexion.prepareStatement(SELECT);
            ResultSet rs = ps.executeQuery();
            ArrayList<DiaFestivoTO> coleccion = null;
            while (rs.next()) {
                if (coleccion == null) coleccion = new ArrayList<DiaFestivoTO>();
                int idDiaFestivo = rs.getInt("id_dia_festivo");
                String descripcion = rs.getString("descripcion");
                short dia = rs.getShort("dia");
                short mes = rs.getShort("mes");
                boolean activo = rs.getBoolean("activo");
                DiaFestivoTO diaFestivoTO = new DiaFestivoTO(idDiaFestivo, descripcion, dia, mes, activo);
                coleccion.add(diaFestivoTO);
            }
            ps.close();
            conexion.close();
            return coleccion;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public ArrayList<DiaFestivoTO> selectDiasFestivosByMes(short mes) {
        try {
            Connection conexion = FabricaDeDAOsMySQL.crearConexion();
            PreparedStatement ps = conexion.prepareStatement(SELECTMES);
            ps.setShort(1, mes);
            ResultSet rs = ps.executeQuery();
            ArrayList<DiaFestivoTO> coleccion = null;
            while (rs.next()) {
                if (coleccion == null) coleccion = new ArrayList<DiaFestivoTO>();
                int idDiaFestivo = rs.getInt("id_dia_festivo");
                String descripcion = rs.getString("descripcion");
                short dia = rs.getShort("dia");
                boolean activo = rs.getBoolean("activo");
                DiaFestivoTO diaFestivoTO = new DiaFestivoTO(idDiaFestivo, descripcion, dia, mes, activo);
                coleccion.add(diaFestivoTO);
            }
            ps.close();
            conexion.close();
            return coleccion;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public ArrayList<DiaFestivoTO> selectDiasFestivosByMes(String mess) {
        short mesShort = 0;
        mess = mess.toUpperCase();
        if (mess.compareTo("ENERO") == 0) mesShort = 1; else if (mess.compareTo("FEBRERO") == 0) mesShort = 2; else if (mess.compareTo("MARZO") == 0) mesShort = 3; else if (mess.compareTo("ABRIL") == 0) mesShort = 4; else if (mess.compareTo("MAYO") == 0) mesShort = 5; else if (mess.compareTo("JUNIO") == 0) mesShort = 6; else if (mess.compareTo("JULIO") == 0) mesShort = 7; else if (mess.compareTo("AGOSTO") == 0) mesShort = 8; else if (mess.compareTo("SEPTIEMBRE") == 0) mesShort = 9; else if (mess.compareTo("OCTUBRE") == 0) mesShort = 10; else if (mess.compareTo("NOVIEMBRE") == 0) mesShort = 11; else if (mess.compareTo("DICIEMBRE") == 0) mesShort = 12;
        return selectDiasFestivosByMes(mesShort);
    }

    public ArrayList<DiaFestivoTO> selectDiasFestivosActivos() {
        try {
            Connection conexion = FabricaDeDAOsMySQL.crearConexion();
            PreparedStatement ps = conexion.prepareStatement(SELECTACTIVOS);
            ps.setBoolean(1, true);
            ResultSet rs = ps.executeQuery();
            ArrayList<DiaFestivoTO> coleccion = null;
            while (rs.next()) {
                if (coleccion == null) coleccion = new ArrayList<DiaFestivoTO>();
                int idDiaFestivo = rs.getInt("id_dia_festivo");
                String descripcion = rs.getString("descripcion");
                short dia = rs.getShort("dia");
                short mes = rs.getShort("mes");
                boolean activo = rs.getBoolean("activo");
                DiaFestivoTO diaFestivoTO = new DiaFestivoTO(idDiaFestivo, descripcion, dia, mes, activo);
                coleccion.add(diaFestivoTO);
            }
            ps.close();
            conexion.close();
            return coleccion;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
