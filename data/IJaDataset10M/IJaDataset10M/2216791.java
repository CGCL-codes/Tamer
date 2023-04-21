package es.caib.regweb;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

/**
 * Bean que gestiona els models de rebut
 * @author  AROGEL
 * @version 1.0
 */
public class ModeloReciboBean implements SessionBean {

    private static final long serialVersionUID = 1L;

    private String nombre;

    private String contentType;

    private byte[] datos;

    public void leer(String nomModel) throws SQLException, ClassNotFoundException, Exception {
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        String datosleidos = null;
        java.sql.Blob dades = null;
        try {
            conn = ToolsBD.getConn();
            String sentenciaSql = "SELECT MOR_NOM, MOR_CONTYP, MOR_DATA FROM BZMODREB " + "WHERE MOR_NOM=? ";
            ps = conn.prepareStatement(sentenciaSql);
            ps.setString(1, nomModel);
            rs = ps.executeQuery();
            if (rs.next()) {
                datosleidos = rs.getString(2);
                dades = rs.getBlob(3);
                this.datos = dades.getBytes(1, (int) dades.length());
            } else {
                datosleidos = null;
            }
        } catch (Exception e) {
            String mesError = new String("ERROR: Leer: " + e.getMessage());
            System.err.println(mesError);
            throw new Exception(mesError, e);
        } finally {
            ToolsBD.closeConn(conn, ps, rs);
        }
        if (datosleidos != null) {
            this.contentType = datosleidos;
            this.nombre = nomModel;
        } else {
            throw new Exception("No s'ha trobat el model. NomModel:" + nomModel);
        }
    }

    public void eliminar(String nomModel) throws SQLException, ClassNotFoundException, Exception {
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        try {
            conn = ToolsBD.getConn();
            String sentenciaSql = "DELETE FROM BZMODREB  " + "WHERE  ( MOR_NOM = ?)";
            ps = conn.prepareStatement(sentenciaSql);
            ps.setString(1, nomModel);
            ps.execute();
        } catch (Exception e) {
            String resposta = "ERROR: No s'ha pogut esborrar el model.";
            System.err.println(resposta);
            throw new Exception(resposta, e);
        } finally {
            ToolsBD.closeConn(conn, ps, rs);
        }
    }

    public boolean grabar(String nomModel, String conType, byte[] dades) throws SQLException, ClassNotFoundException, Exception {
        this.setNombre(nomModel);
        this.setContentType(conType);
        this.setDatos(dades);
        return this.grabar();
    }

    private boolean grabar() throws SQLException, ClassNotFoundException, Exception {
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        boolean ok = true;
        try {
            conn = ToolsBD.getConn();
            String sentenciaSql = "INSERT INTO BZMODREB ( MOR_NOM, MOR_CONTYP) " + "VALUES (?,?)";
            ps = conn.prepareStatement(sentenciaSql);
            ps.setString(1, this.nombre);
            ps.setString(2, this.contentType);
            ps.execute();
            String sql = "UPDATE BZMODREB SET MOR_DATA=? WHERE MOR_NOM=?";
            int r = 0;
            java.io.InputStream stream = new java.io.ByteArrayInputStream(this.datos);
            ps = conn.prepareStatement(sql);
            ps.setBytes(1, this.datos);
            ps.setString(2, this.nombre);
            r = ps.executeUpdate();
        } catch (Exception e) {
            ok = false;
            e.printStackTrace();
        } finally {
            ToolsBD.closeConn(conn, ps, rs);
        }
        return ok;
    }

    public Vector recuperarRepros(String usuario, String tipo) {
        Vector vectorRegistrosRepro = new Vector();
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        try {
            conn = ToolsBD.getConn();
            String sentenciaSql = "SELECT FZCNREP,FZCDREP FROM BZREPRO " + "WHERE FZCCUSU=? ";
            if (tipo != null) {
                sentenciaSql += " AND FZTIREP=? ";
            }
            sentenciaSql += " ORDER BY FZCNREP ASC";
            ps = conn.prepareStatement(sentenciaSql);
            ps.setString(1, usuario);
            if (tipo != null) {
                ps.setString(2, tipo);
            }
            rs = ps.executeQuery();
            while (rs.next()) {
                RegistroRepro registro = new RegistroRepro();
                registro.setNomRepro(rs.getString("FZCNREP").trim());
                registro.setRepro(rs.getString("FZCDREP").trim());
                registro.setCodUsuario(usuario);
                vectorRegistrosRepro.addElement(registro);
            }
        } catch (Exception e) {
            String resposta = "ERROR: No s'ha pogut llegir les repros de l'usuari:" + usuario;
            System.out.println(resposta);
            e.printStackTrace();
        } finally {
            ToolsBD.closeConn(conn, ps, rs);
        }
        return vectorRegistrosRepro;
    }

    public Vector recuperarRepros(String usuario) {
        return recuperarRepros(usuario, null);
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public byte[] getDatos() {
        return datos;
    }

    public void setDatos(byte[] datos) {
        this.datos = datos;
    }

    public void ejbActivate() throws EJBException, RemoteException {
    }

    public void ejbPassivate() throws EJBException, RemoteException {
    }

    public void ejbRemove() throws EJBException, RemoteException {
    }

    public void ejbCreate() throws CreateException {
    }

    public void setSessionContext(SessionContext arg0) throws EJBException, RemoteException {
    }
}
