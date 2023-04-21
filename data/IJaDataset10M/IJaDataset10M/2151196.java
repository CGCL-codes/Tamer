package mx.ipn.persistencia.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import mx.ipn.persistencia.dao.InstitucionFinancieraDAO;
import mx.ipn.to.InstitucionFinancieraTO;

public class InstitucionFinancieraDAOMySQL implements InstitucionFinancieraDAO {

    static {
        FabricaDeDAOsMySQL.iniciaLog(InstitucionFinancieraDAOMySQL.class);
    }

    private static final String INSERT = "INSERT INTO institucion_financiera (nombre,telefono) VALUES(?,?)";

    private static final String DELETE = "DELETE FROM institucion_financiera WHERE id_institucion_financiera=?";

    private static final String UPDATE = "UPDATE institucion_financiera SET nombre=?,telefono=? WHERE id_institucion_financiera=?";

    private static final String FINDBYID = "SELECT * FROM institucion_financiera WHERE id_institucion_financiera=?";

    private static final String FINDBYNOM = "SELECT * FROM institucion_financiera WHERE nombre=?";

    private static final String FINDBYTEL = "SELECT * FROM institucion_financiera WHERE telefono=?";

    private static final String SELECT = "SELECT * FROM institucion_financiera";

    @Override
    public short deleteInstitucionFinanciera(int idInstitucionFinanciera) {
        try {
            Connection conexion = FabricaDeDAOsMySQL.crearConexion();
            PreparedStatement ps = conexion.prepareStatement(DELETE);
            ps.setInt(1, idInstitucionFinanciera);
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

    @Override
    public InstitucionFinancieraTO findInstitucionFinancieraById(int idInstitucionFinanciera) {
        try {
            Connection conexion = FabricaDeDAOsMySQL.crearConexion();
            PreparedStatement ps = conexion.prepareStatement(FINDBYID);
            ps.setInt(1, idInstitucionFinanciera);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String nombre = rs.getString("nombre");
                String telefono = rs.getString("telefono");
                InstitucionFinancieraTO institucionFinancieraTO = new InstitucionFinancieraTO(idInstitucionFinanciera, nombre, telefono);
                ps.close();
                conexion.close();
                return institucionFinancieraTO;
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
    public InstitucionFinancieraTO findInstitucionFinancieraByNombre(String nombre) {
        try {
            Connection conexion = FabricaDeDAOsMySQL.crearConexion();
            PreparedStatement ps = conexion.prepareStatement(FINDBYNOM);
            ps.setString(1, nombre);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int idInstitucionFinanciera = rs.getInt("id_institucion_financiera");
                String telefono = rs.getString("telefono");
                InstitucionFinancieraTO institucionFinancieraTO = new InstitucionFinancieraTO(idInstitucionFinanciera, nombre, telefono);
                ps.close();
                conexion.close();
                return institucionFinancieraTO;
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
    public boolean insertInstitucionFinanciera(InstitucionFinancieraTO institucionFinancieraTO) {
        try {
            Connection conexion = FabricaDeDAOsMySQL.crearConexion();
            PreparedStatement ps = conexion.prepareStatement(INSERT);
            ps.setString(1, institucionFinancieraTO.getNombre());
            ps.setString(2, institucionFinancieraTO.getTelefono());
            ps.executeUpdate();
            ps.close();
            conexion.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public ArrayList<InstitucionFinancieraTO> selectInstitucionFinanciera() {
        try {
            Connection conexion = FabricaDeDAOsMySQL.crearConexion();
            PreparedStatement ps = conexion.prepareStatement(SELECT);
            ResultSet rs = ps.executeQuery();
            ArrayList<InstitucionFinancieraTO> coleccion = null;
            while (rs.next()) {
                if (coleccion == null) coleccion = new ArrayList<InstitucionFinancieraTO>();
                int idinstitucionFinanciera = rs.getInt("id_institucion_financiera");
                String nombre = rs.getString("nombre");
                String telefono = rs.getString("telefono");
                InstitucionFinancieraTO institucionFinancieraTO = new InstitucionFinancieraTO(idinstitucionFinanciera, nombre, telefono);
                coleccion.add(institucionFinancieraTO);
            }
            ps.close();
            conexion.close();
            return coleccion;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public short updateInstitucionFinanciera(InstitucionFinancieraTO institucionFinancieraTO) {
        try {
            Connection conexion = FabricaDeDAOsMySQL.crearConexion();
            PreparedStatement ps = conexion.prepareStatement(UPDATE);
            ps.setString(1, institucionFinancieraTO.getNombre());
            ps.setString(2, institucionFinancieraTO.getTelefono());
            ps.setInt(3, institucionFinancieraTO.getIdInstitucionFinanciera());
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

    @Override
    public InstitucionFinancieraTO findInstitucionFinancieraByTelefono(String telefono) {
        try {
            Connection conexion = FabricaDeDAOsMySQL.crearConexion();
            PreparedStatement ps = conexion.prepareStatement(FINDBYTEL);
            ps.setString(1, telefono);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int idInstitucionFinanciera = rs.getInt("id_institucion_financiera");
                String nombre = rs.getString("nombre");
                InstitucionFinancieraTO institucionFinancieraTO = new InstitucionFinancieraTO(idInstitucionFinanciera, nombre, telefono);
                ps.close();
                conexion.close();
                return institucionFinancieraTO;
            }
            ps.close();
            conexion.close();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
