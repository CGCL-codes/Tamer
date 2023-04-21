package persistencia.busqueda;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import Excepciones.ExccepcionFormeteoDeFechas;
import negocio.busqueda.BusquedaYSeleccion;
import negocio.busqueda.Candidato;
import negocio.cliente.Cliente;
import negocio.cv.CV;
import persistencia.DACBase;
import persistencia.SingletonDACBase;
import persistencia.cv.DAOCv;

public class DACCandidato extends DACBase {

    private CallableStatement clstm;

    private ResultSet rs;

    public DACCandidato(DACBase dacBase) {
        super(dacBase);
    }

    public void actualizarCandidatos(BusquedaYSeleccion bys) {
        try {
            for (Candidato candidato : bys.getCandidatosSelecc()) {
                clstm = getConn().prepareCall("{? = call save_candidato(?, ?, ?, ?, ?)}");
                clstm.registerOutParameter(1, java.sql.Types.INTEGER);
                clstm.setInt("idcandidato", candidato.getIdCandidato());
                clstm.setInt("idbusquedayseleccion", bys.getIdBuqsquedaYSeleccion());
                clstm.setString("estado", candidato.getEstado());
                clstm.setString("observacion", " ");
                clstm.setInt("idcv", candidato.getCv().getIdCV());
                clstm.execute();
                if (candidato.getIdCandidato() == 0) {
                    candidato.setIdCandidato(clstm.getInt(1));
                }
            }
            clstm.close();
            clstm = null;
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
            System.out.println("Error en metodo actualizarCandidatos_DacCandidato");
        }
    }

    public List<Candidato> consultarCandidatos(int idBYS) {
        List<Candidato> cs = new ArrayList<Candidato>();
        Candidato c;
        DAOCv daoCV;
        try {
            clstm = getConn().prepareCall("{call candidatoConsultar(?)}");
            clstm.setInt("idbys", idBYS);
            clstm.execute();
            rs = clstm.getResultSet();
            while (rs.next()) {
                c = new Candidato();
                System.out.println("\n entra al dac candidato" + rs.getInt("idcandidato"));
                c.setIdCandidato(rs.getInt("idcandidato"));
                c.setEstado(rs.getString("estado"));
                c.setObservacion(rs.getString("observacion"));
                CV cv = new CV();
                cv.setIdCV(rs.getInt("idcv"));
                c.setCv(cv);
                if (c.getEstado().equals("en preseleccion")) {
                    DACSeguimiento dacSeg = new DACSeguimiento(SingletonDACBase.tomarDACBase());
                    try {
                        c.setSeguimientosDeCandidato(dacSeg.consultarSeguimientos(c.getIdCandidato()));
                    } catch (ExccepcionFormeteoDeFechas ex) {
                        JOptionPane.showMessageDialog(null, ex.getMensaje(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
                cs.add(c);
            }
            rs.close();
            rs = null;
            clstm.close();
            clstm = null;
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
            System.out.println("Error en metodo consultarCandidatos()_DacCandidatos");
        }
        return cs;
    }
}
