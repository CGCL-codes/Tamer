package pos.data;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.mysql.jdbc.Connection;
import pos.domain.PaisImpl;
import pos.domain.Perfil;
import pos.domain.PerfilImpl;
import pos.utils.UIDGenerator;

public class JDBCPerfilDAO implements IPerfilDAO {

    private Connection conn;

    public JDBCPerfilDAO() {
        conn = (Connection) ConnectionManager.getInstance().checkOut();
    }

    @Override
    protected void finalize() {
        ConnectionManager.getInstance().checkIn(conn);
    }

    public List<Perfil> recuperarPerfiles() {
        List<Perfil> p = new ArrayList<Perfil>();
        String sql = "SELECT * FROM perfiles";
        PreparedStatement stmt = null;
        ResultSet result = null;
        try {
            stmt = conn.prepareStatement(sql);
            result = stmt.executeQuery();
            while (result.next()) {
                PerfilImpl perfil = new PerfilImpl();
                perfil.setIdPerfil(result.getString("IDPerfil"));
                perfil.setNombreUsuario(result.getString("nombre"));
                perfil.setApellidos(result.getString("apellidos"));
                perfil.setEdad(result.getInt("edad"));
                perfil.setIdPais(result.getString("IDPais"));
                perfil.setIdPoblacion(result.getString("IDProvincia"));
                perfil.setMovilOS(result.getString("IDSO2"));
                perfil.setPcOS(result.getString("IDSO1"));
                p.add(perfil);
            }
        } catch (SQLException e) {
            System.out.println("Message: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("ErrorCode: " + e.getErrorCode());
        } finally {
            try {
                if (result != null) {
                    result.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException e) {
            }
        }
        return p;
    }

    @Override
    public Perfil insertarPerfil(Perfil p) {
        String sql = "INSERT INTO perfiles (IDPerfil, nombre, apellidos, edad, IDPais, IDProvincia,IDSO1, IDSO2) VALUES (?,?,?,?,?,?,?,?) ";
        PreparedStatement stmt = null;
        p.setIdPerfil(UIDGenerator.getInstance().getKey());
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, p.getIdPerfil());
            stmt.setString(2, p.getNombreUsuario());
            stmt.setString(3, p.getApellidos());
            stmt.setInt(4, p.getEdad());
            stmt.setString(5, p.getIdPais());
            stmt.setString(6, p.getIdPoblacion());
            stmt.setString(7, p.getPcOS());
            stmt.setString(8, p.getMovilOS());
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Message: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("ErrorCode: " + e.getErrorCode());
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException e) {
            }
        }
        return p;
    }

    @Override
    public Perfil recuperarPerfil(String idPerfil) {
        Perfil perfil = new PerfilImpl();
        String sql = "SELECT * FROM perfiles where ( IDPerfil = ? )";
        PreparedStatement stmt = null;
        ResultSet result = null;
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, idPerfil);
            result = stmt.executeQuery();
            while (result.next()) {
                perfil.setIdPerfil(result.getString("IDPerfil"));
                perfil.setNombreUsuario(result.getString("nombre"));
                perfil.setApellidos(result.getString("apellidos"));
                perfil.setEdad(result.getInt("edad"));
                perfil.setIdPais(result.getString("IDPais"));
                perfil.setIdPoblacion(result.getString("IDProvincia"));
                perfil.setMovilOS(result.getString("IDSO2"));
                perfil.setPcOS(result.getString("IDSO1"));
            }
        } catch (SQLException e) {
            System.out.println("Message: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("ErrorCode: " + e.getErrorCode());
        } finally {
            try {
                if (result != null) {
                    result.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException e) {
            }
        }
        return perfil;
    }

    @Override
    public void borrarPerfil(String idPerfil) {
        String sql = "DELETE FROM encuestas WHERE (IDPerfil = ?) ";
        PreparedStatement stmt = null;
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, idPerfil);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Message: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("ErrorCode: " + e.getErrorCode());
        } finally {
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
            }
        }
    }

    @Override
    public void actualizarPerfil(Perfil p) {
        String sql = "UPDATE perfiles SET nombre = ?, apellidos = ?, edad = ?, IDPais = ?, IDProvincia = ?, IDSO1 = ?, IDSO2 = ? WHERE (IDPerfil = ?)";
        PreparedStatement stm = null;
        try {
            stm = conn.prepareStatement(sql);
            stm.setString(1, p.getNombreUsuario());
            stm.setString(2, p.getApellidos());
            stm.setInt(3, p.getEdad());
            stm.setString(4, p.getIdPais());
            stm.setString(5, p.getIdPoblacion());
            stm.setString(6, p.getPcOS());
            stm.setString(7, p.getMovilOS());
            stm.setString(8, p.getIdPerfil());
            stm.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Message: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("ErrorCode: " + e.getErrorCode());
        } finally {
            try {
                if (stm != null) {
                    stm.close();
                }
            } catch (SQLException e) {
            }
        }
    }
}
