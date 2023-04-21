package mx.ipn.persistencia.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import mx.ipn.to.ColorAutoTO;
import mx.ipn.to.SubmarcaTO;
import mx.ipn.to.UnidadTO;
import mx.ipn.to.EconomicoTO;
import mx.ipn.persistencia.FabricaDeDAOs;
import mx.ipn.persistencia.dao.UnidadDAO;
import mx.ipn.persistencia.dao.EconomicoDAO;
import mx.ipn.persistencia.dao.SubmarcaDAO;
import mx.ipn.persistencia.dao.ColorAutoDAO;

public class UnidadDAOMySQL implements UnidadDAO {

    static {
        FabricaDeDAOsMySQL.iniciaLog(UnidadDAOMySQL.class);
    }

    private static final String INSERT = "INSERT INTO unidad (placas,modelo,id_economico,id_submarca,id_color_auto,activo) VALUES(?,?,?,?,?,?)";

    private static final String ACTIVADESACTIVAUNIDAD = "UPDATE unidad SET activo=? WHERE id_unidad=?";

    private static final String UPDATE = "UPDATE unidad SET placas=?,modelo=? ,id_economico=?,id_submarca=?,id_color_auto=?,activo=? WHERE id_unidad=?";

    private static final String FINDBYID = "SELECT * FROM unidad WHERE id_unidad=?";

    private static final String FINDACTIVOBYPLACAS = "SELECT * FROM unidad WHERE placas=? AND activo=true";

    private static final String FINDACTIVOBYECONOMICO = "SELECT * FROM unidad WHERE id_economico=? AND activo=true";

    private static final String SELECT = "SELECT * FROM unidad";

    private static final String SELECTACTIVA = "SELECT * FROM unidad WHERE activo=true";

    private static final String SELECTACTIVABYMODELO = "SELECT * FROM unidad WHERE modelo=? AND activo=true";

    private static final String SELECTBYPLACAS = "SELECT * FROM unidad WHERE placas=?";

    private static final String SELECTBYECONOMICO = "SELECT * FROM unidad WHERE id_economico=?";

    FabricaDeDAOs fabricaMySQL;

    public boolean insertUnidad(UnidadTO unidad) {
        try {
            Connection conexion = FabricaDeDAOsMySQL.crearConexion();
            PreparedStatement ps = conexion.prepareStatement(INSERT);
            ps.setString(1, unidad.getPlacas());
            ps.setInt(2, unidad.getModelo());
            ps.setInt(3, unidad.getEconomicoTO().getIdEconomico());
            ps.setInt(4, unidad.getSubmarcaTO().getIdSubmarca());
            ps.setShort(5, unidad.getColorAutoTO().getIdColorAuto());
            ps.setBoolean(6, unidad.getActivo());
            ps.executeUpdate();
            ps.close();
            conexion.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public short activaDesactivaUnidad(int idUnidad, boolean activo) {
        try {
            Connection conexion = FabricaDeDAOsMySQL.crearConexion();
            PreparedStatement ps = conexion.prepareStatement(ACTIVADESACTIVAUNIDAD);
            ps.setBoolean(1, activo);
            ps.setInt(2, idUnidad);
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

    public short updateUnidad(UnidadTO unidad) {
        try {
            Connection conexion = FabricaDeDAOsMySQL.crearConexion();
            PreparedStatement ps = conexion.prepareStatement(UPDATE);
            ps.setString(1, unidad.getPlacas());
            ps.setInt(2, unidad.getModelo());
            ps.setInt(3, unidad.getEconomicoTO().getIdEconomico());
            ps.setInt(4, unidad.getSubmarcaTO().getIdSubmarca());
            ps.setShort(5, unidad.getColorAutoTO().getIdColorAuto());
            ps.setBoolean(6, unidad.getActivo());
            ps.setInt(7, unidad.getIdUnidad());
            ps.executeUpdate();
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

    public UnidadTO findUnidadById(int idUnidad) {
        try {
            Connection conexion = FabricaDeDAOsMySQL.crearConexion();
            PreparedStatement ps = conexion.prepareStatement(FINDBYID);
            ps.setInt(1, idUnidad);
            ResultSet rs = ps.executeQuery();
            fabricaMySQL = FabricaDeDAOs.getFabricaDeDAOs(mx.ipn.Constantes.FABRICA_DAOS_MYSQL);
            EconomicoDAO economicoDAO = fabricaMySQL.getEconomicoDAO();
            ColorAutoDAO colorAutoDAO = fabricaMySQL.getColorAutoDAO();
            SubmarcaDAO submarcaDAO = fabricaMySQL.getSubmarcaDAO();
            if (rs.next()) {
                idUnidad = rs.getInt("id_unidad");
                String placas = rs.getString("placas");
                int modelo = rs.getInt("modelo");
                EconomicoTO economicoTO = economicoDAO.findEconomicoById(rs.getInt("id_economico"));
                SubmarcaTO submarcaTO = submarcaDAO.findSubmarcaById(rs.getInt("id_submarca"));
                ColorAutoTO colorAutoTO = colorAutoDAO.findColorAutoById(rs.getShort("id_color_auto"));
                boolean activo = rs.getBoolean("activo");
                UnidadTO unidad = new UnidadTO(idUnidad, placas, modelo, economicoTO, submarcaTO, colorAutoTO, activo);
                ps.close();
                conexion.close();
                return unidad;
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
    public UnidadTO findUnidadActivoByEconomico(int idEconomico) {
        try {
            Connection conexion = FabricaDeDAOsMySQL.crearConexion();
            PreparedStatement ps = conexion.prepareStatement(FINDACTIVOBYECONOMICO);
            ps.setInt(1, idEconomico);
            ResultSet rs = ps.executeQuery();
            fabricaMySQL = FabricaDeDAOs.getFabricaDeDAOs(mx.ipn.Constantes.FABRICA_DAOS_MYSQL);
            EconomicoDAO economicoDAO = fabricaMySQL.getEconomicoDAO();
            ColorAutoDAO colorAutoDAO = fabricaMySQL.getColorAutoDAO();
            SubmarcaDAO submarcaDAO = fabricaMySQL.getSubmarcaDAO();
            if (rs.next()) {
                int idUnidad = rs.getInt("id_unidad");
                String placas = rs.getString("placas");
                int modelo = rs.getInt("modelo");
                EconomicoTO economicoTO = economicoDAO.findEconomicoById(rs.getInt("id_economico"));
                SubmarcaTO submarcaTO = submarcaDAO.findSubmarcaById(rs.getInt("id_submarca"));
                ColorAutoTO colorAutoTO = colorAutoDAO.findColorAutoById(rs.getShort("id_color_auto"));
                boolean activo = rs.getBoolean("activo");
                UnidadTO unidad = new UnidadTO(idUnidad, placas, modelo, economicoTO, submarcaTO, colorAutoTO, activo);
                ps.close();
                conexion.close();
                return unidad;
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
    public UnidadTO findUnidadActivoByPlacas(String placas) {
        try {
            Connection conexion = FabricaDeDAOsMySQL.crearConexion();
            PreparedStatement ps = conexion.prepareStatement(FINDACTIVOBYPLACAS);
            ps.setString(1, placas);
            ResultSet rs = ps.executeQuery();
            fabricaMySQL = FabricaDeDAOs.getFabricaDeDAOs(mx.ipn.Constantes.FABRICA_DAOS_MYSQL);
            EconomicoDAO economicoDAO = fabricaMySQL.getEconomicoDAO();
            ColorAutoDAO colorAutoDAO = fabricaMySQL.getColorAutoDAO();
            SubmarcaDAO submarcaDAO = fabricaMySQL.getSubmarcaDAO();
            if (rs.next()) {
                int idUnidad = rs.getInt("id_unidad");
                placas = rs.getString("placas");
                int modelo = rs.getInt("modelo");
                EconomicoTO economicoTO = economicoDAO.findEconomicoById(rs.getInt("id_economico"));
                SubmarcaTO submarcaTO = submarcaDAO.findSubmarcaById(rs.getInt("id_submarca"));
                ColorAutoTO colorAutoTO = colorAutoDAO.findColorAutoById(rs.getShort("id_color_auto"));
                boolean activo = rs.getBoolean("activo");
                UnidadTO unidad = new UnidadTO(idUnidad, placas, modelo, economicoTO, submarcaTO, colorAutoTO, activo);
                ps.close();
                conexion.close();
                return unidad;
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
    public ArrayList<UnidadTO> selectActivaByModelo(int modelo) {
        try {
            Connection conexion = FabricaDeDAOsMySQL.crearConexion();
            PreparedStatement ps = conexion.prepareStatement(SELECTACTIVABYMODELO);
            ps.setInt(1, modelo);
            ResultSet rs = ps.executeQuery();
            fabricaMySQL = FabricaDeDAOs.getFabricaDeDAOs(mx.ipn.Constantes.FABRICA_DAOS_MYSQL);
            EconomicoDAO economicoDAO = fabricaMySQL.getEconomicoDAO();
            ColorAutoDAO colorAutoDAO = fabricaMySQL.getColorAutoDAO();
            SubmarcaDAO submarcaDAO = fabricaMySQL.getSubmarcaDAO();
            ArrayList<UnidadTO> coleccion = null;
            while (rs.next()) {
                if (coleccion == null) coleccion = new ArrayList<UnidadTO>();
                int idUnidad = rs.getInt("id_unidad");
                String placas = rs.getString("placas");
                EconomicoTO economicoTO = economicoDAO.findEconomicoById(rs.getInt("id_economico"));
                SubmarcaTO submarcaTO = submarcaDAO.findSubmarcaById(rs.getInt("id_submarca"));
                ColorAutoTO colorAutoTO = colorAutoDAO.findColorAutoById(rs.getShort("id_color_auto"));
                boolean activo = rs.getBoolean("activo");
                UnidadTO unidad = new UnidadTO(idUnidad, placas, modelo, economicoTO, submarcaTO, colorAutoTO, activo);
                coleccion.add(unidad);
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
    public ArrayList<UnidadTO> selectUnidad() {
        try {
            Connection conexion = FabricaDeDAOsMySQL.crearConexion();
            PreparedStatement ps = conexion.prepareStatement(SELECT);
            ResultSet rs = ps.executeQuery();
            fabricaMySQL = FabricaDeDAOs.getFabricaDeDAOs(mx.ipn.Constantes.FABRICA_DAOS_MYSQL);
            EconomicoDAO economicoDAO = fabricaMySQL.getEconomicoDAO();
            ColorAutoDAO colorAutoDAO = fabricaMySQL.getColorAutoDAO();
            SubmarcaDAO submarcaDAO = fabricaMySQL.getSubmarcaDAO();
            ArrayList<UnidadTO> coleccion = null;
            while (rs.next()) {
                if (coleccion == null) coleccion = new ArrayList<UnidadTO>();
                int idUnidad = rs.getInt("id_unidad");
                String placas = rs.getString("placas");
                int modelo = rs.getInt("modelo");
                EconomicoTO economicoTO = economicoDAO.findEconomicoById(rs.getInt("id_economico"));
                SubmarcaTO submarcaTO = submarcaDAO.findSubmarcaById(rs.getInt("id_submarca"));
                ColorAutoTO colorAutoTO = colorAutoDAO.findColorAutoById(rs.getShort("id_color_auto"));
                boolean activo = rs.getBoolean("activo");
                UnidadTO unidad = new UnidadTO(idUnidad, placas, modelo, economicoTO, submarcaTO, colorAutoTO, activo);
                coleccion.add(unidad);
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
    public ArrayList<UnidadTO> selectUnidadActiva() {
        try {
            Connection conexion = FabricaDeDAOsMySQL.crearConexion();
            PreparedStatement ps = conexion.prepareStatement(SELECTACTIVA);
            ResultSet rs = ps.executeQuery();
            fabricaMySQL = FabricaDeDAOs.getFabricaDeDAOs(mx.ipn.Constantes.FABRICA_DAOS_MYSQL);
            EconomicoDAO economicoDAO = fabricaMySQL.getEconomicoDAO();
            ColorAutoDAO colorAutoDAO = fabricaMySQL.getColorAutoDAO();
            SubmarcaDAO submarcaDAO = fabricaMySQL.getSubmarcaDAO();
            ArrayList<UnidadTO> coleccion = null;
            while (rs.next()) {
                if (coleccion == null) coleccion = new ArrayList<UnidadTO>();
                int idUnidad = rs.getInt("id_unidad");
                String placas = rs.getString("placas");
                int modelo = rs.getInt("modelo");
                EconomicoTO economicoTO = economicoDAO.findEconomicoById(rs.getInt("id_economico"));
                SubmarcaTO submarcaTO = submarcaDAO.findSubmarcaById(rs.getInt("id_submarca"));
                ColorAutoTO colorAutoTO = colorAutoDAO.findColorAutoById(rs.getShort("id_color_auto"));
                boolean activo = rs.getBoolean("activo");
                UnidadTO unidad = new UnidadTO(idUnidad, placas, modelo, economicoTO, submarcaTO, colorAutoTO, activo);
                coleccion.add(unidad);
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
    public UnidadTO selectUnidadByEconomico(int idEconomico) {
        try {
            Connection conexion = FabricaDeDAOsMySQL.crearConexion();
            FabricaDeDAOs fabricaMySQL = null;
            EconomicoDAO economicoDAO = null;
            SubmarcaDAO submarcaDAO = null;
            ColorAutoDAO colorAutoDAO = null;
            ArrayList<Integer> listIdUnidad = null;
            ArrayList<String> listPlacas = null;
            ArrayList<Integer> listModelo = null;
            ArrayList<SubmarcaTO> listSubmarcaTO = null;
            ArrayList<ColorAutoTO> listColorAutoTO = null;
            ArrayList<Boolean> listActivo = null;
            UnidadTO unidadTO = null;
            PreparedStatement ps = conexion.prepareStatement(SELECTBYECONOMICO);
            ps.setInt(1, idEconomico);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                if (listIdUnidad == null) {
                    listIdUnidad = new ArrayList<Integer>();
                    listPlacas = new ArrayList<String>();
                    listModelo = new ArrayList<Integer>();
                    listSubmarcaTO = new ArrayList<SubmarcaTO>();
                    listColorAutoTO = new ArrayList<ColorAutoTO>();
                    listActivo = new ArrayList<Boolean>();
                    fabricaMySQL = FabricaDeDAOs.getFabricaDeDAOs(mx.ipn.Constantes.FABRICA_DAOS_MYSQL);
                    economicoDAO = fabricaMySQL.getEconomicoDAO();
                    submarcaDAO = fabricaMySQL.getSubmarcaDAO();
                    colorAutoDAO = fabricaMySQL.getColorAutoDAO();
                }
                listIdUnidad.add(rs.getInt("id_unidad"));
                listPlacas.add(rs.getString("placas"));
                listModelo.add(rs.getInt("modelo"));
                listSubmarcaTO.add(submarcaDAO.findSubmarcaById(rs.getInt("id_submarca")));
                listColorAutoTO.add(colorAutoDAO.findColorAutoById(rs.getShort("id_color_auto")));
                listActivo.add(rs.getBoolean("activo"));
            }
            if (listIdUnidad != null) {
                Integer[] arrIdUnidad = (Integer[]) listIdUnidad.toArray(new Integer[listIdUnidad.size()]);
                Boolean[] arrActivo = (Boolean[]) listActivo.toArray(new Boolean[listActivo.size()]);
                String[] arrPlacas = (String[]) listPlacas.toArray(new String[listPlacas.size()]);
                Integer[] arrModelo = (Integer[]) listModelo.toArray(new Integer[listModelo.size()]);
                SubmarcaTO[] arrSubmarcaTO = (SubmarcaTO[]) listSubmarcaTO.toArray(new SubmarcaTO[listSubmarcaTO.size()]);
                ColorAutoTO[] arrColorAutoTO = (ColorAutoTO[]) listColorAutoTO.toArray(new ColorAutoTO[listColorAutoTO.size()]);
                unidadTO = new UnidadTO(arrIdUnidad, arrPlacas, arrModelo, economicoDAO.findEconomicoById(idEconomico), arrSubmarcaTO, arrColorAutoTO, arrActivo);
            }
            ps.close();
            conexion.close();
            return unidadTO;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public ArrayList<UnidadTO> selectUnidadByPlacas(String placas) {
        try {
            Connection conexion = FabricaDeDAOsMySQL.crearConexion();
            PreparedStatement ps = conexion.prepareStatement(SELECTBYPLACAS);
            ps.setString(1, placas);
            ResultSet rs = ps.executeQuery();
            fabricaMySQL = FabricaDeDAOs.getFabricaDeDAOs(mx.ipn.Constantes.FABRICA_DAOS_MYSQL);
            EconomicoDAO economicoDAO = fabricaMySQL.getEconomicoDAO();
            ColorAutoDAO colorAutoDAO = fabricaMySQL.getColorAutoDAO();
            SubmarcaDAO submarcaDAO = fabricaMySQL.getSubmarcaDAO();
            ArrayList<UnidadTO> coleccion = null;
            while (rs.next()) {
                if (coleccion == null) coleccion = new ArrayList<UnidadTO>();
                int idUnidad = rs.getInt("id_unidad");
                int modelo = rs.getInt("modelo");
                EconomicoTO economicoTO = economicoDAO.findEconomicoById(rs.getInt("id_economico"));
                SubmarcaTO submarcaTO = submarcaDAO.findSubmarcaById(rs.getInt("id_submarca"));
                ColorAutoTO colorAutoTO = colorAutoDAO.findColorAutoById(rs.getShort("id_color_auto"));
                boolean activo = rs.getBoolean("activo");
                UnidadTO unidad = new UnidadTO(idUnidad, placas, modelo, economicoTO, submarcaTO, colorAutoTO, activo);
                coleccion.add(unidad);
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
